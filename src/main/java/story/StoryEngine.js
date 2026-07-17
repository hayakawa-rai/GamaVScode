import { Bgm } from "../start/Bgm.js";
import { StoryUtils } from "./StoryUtils.js";

/**
 * ストーリー用の効果音を呼び出す用のクラス
 * その場その場で音が変わるため、分けている。
 */
export class StoryEngine {
    constructor(dialogues, options) {
        this.dialogues = dialogues;
        this.options = options;
        this.ui = options.ui;
        this.onStep = options.onStep || (() => {});
        this.onEnd = options.onEnd || (() => {});

        this.index = 0;
        this.charIndex = 0;
        this.isTyping = false;
        this.isPaused = false;
        this.typingTimer = null;

        // BGM音量：指定が無ければ0.3をデフォルトにする
        this.bgmVolume = options.bgmVolume ?? 0.3;

        this.bgm = new Audio(options.bgmPath);
        this.bgm.loop = true;
        this.bgm.volume = this.bgmVolume;

        this.init();
    }

    init() {
        this.ui.container.addEventListener("click", () => this.handleInput());
        this.ui.menuBtn.addEventListener("click", (e) => { e.stopPropagation(); this.pause(); });
        this.ui.resumeBtn.addEventListener("click", () => this.resume());
        this.ui.titleBtn.addEventListener("click", () => {
            this.bgm.pause();
            this.options.onTitle();
        });

        document.addEventListener("keydown", (e) => {
            if (e.key === "Escape") this.isPaused ? this.resume() : this.pause();
        });

        StoryUtils.NARROW_QUERY.addEventListener("change", () => {
            const current = this.dialogues[this.index];
            StoryUtils.updateCharacterDisplay(current.speaker, this.ui.wrappers);
        });
        
        this.onStep(this.index, this.ui);
        Bgm.unlockPlay(this.bgm);
        this.startTyping();
    }

    startTyping() {
        const d = this.dialogues[this.index];
        this.ui.nameText.textContent = d.speaker;
        this.ui.messageText.style.color = d.textColor || d.color || "white";
        this.ui.messageText.textContent = "";
        this.ui.nextMark.classList.add("hidden");

        // 音声再生はここで一括管理
        if (d.sound) this.playSound(d.sound, d.volume); // d.volumeがあれば個別指定も可能に

        this.isTyping = true;
        this.charIndex = 0;
        this.typeLoop(d.message);
    }

    typeLoop(message) {
        if (!this.isTyping) return;
        if (this.charIndex < message.length) {
            this.ui.messageText.textContent += message.charAt(this.charIndex);
            this.charIndex++;
            this.typingTimer = setTimeout(() => this.typeLoop(message), 50);
        } else {
            this.finishTyping();
        }
    }

    finishTyping() {
        clearTimeout(this.typingTimer);
        this.isTyping = false;
        this.ui.messageText.textContent = this.dialogues[this.index].message;
        this.ui.nextMark.classList.remove("hidden");
    }

    // 全ての音の再生はここを通す
    playSound(path, volume = null) {
        if (!path) return;
        Bgm.playOneShot(path, volume); // volume省略時はBgm側のテーブルを自動参照
    }

    handleInput() {
        if (this.isPaused) return;
        if (this.isTyping) {
            this.finishTyping();
        } else {
            if (this.index < this.dialogues.length - 1) {
                this.index++;
                this.onStep(this.index, this.ui);
                this.startTyping();
            } else {
                this.bgm.pause();
                this.onEnd();
            }
        }
    }

    pause() {
        this.isPaused = true;
        clearTimeout(this.typingTimer);
        this.ui.menuOverlay.style.display = "flex";
        this.bgm.pause();
    }

    resume() {
        this.isPaused = false;
        this.ui.menuOverlay.style.display = "none";
        Bgm.unlockPlay(this.bgm);
        if (this.isTyping) this.typeLoop(this.dialogues[this.index].message);
    }

    changeBGM(newPath, volume = null) {
        if (this.bgm.src.includes(newPath)) return;
        this.bgm.pause();
        this.bgm = new Audio(newPath);
        this.bgm.loop = true;
        this.bgm.volume = volume ?? this.bgmVolume; // 変更後もデフォルト音量を維持
        Bgm.unlockPlay(this.bgm);
    }
}