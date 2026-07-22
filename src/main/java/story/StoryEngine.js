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

        // 【修正】インスタンスに保持する BGM パスを保存
        this.bgmPath = options.bgmPath;

        // 【修正】個別の new Audio() は作成せず、Bgmクラスのミキサーを使ってBGMを再生
        Bgm.playBGM(this.bgmPath);

        this.init();
    }

    init() {
        this.ui.container.addEventListener("click", () => this.handleInput());
        this.ui.menuBtn.addEventListener("click", (e) => { e.stopPropagation(); this.pause(); });
        this.ui.resumeBtn.addEventListener("click", () => this.resume());
        this.ui.titleBtn.addEventListener("click", () => {
            // 【修正】BGM停止をBgmクラスに委譲
            Bgm.stopBGM();
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
        this.startTyping();
    }

    startTyping() {
        const d = this.dialogues[this.index];
        this.ui.nameText.textContent = d.speaker;
        this.ui.messageText.style.color = d.textColor || d.color || "white";
        this.ui.messageText.textContent = "";
        this.ui.nextMark.classList.add("hidden");

        // 音声再生はここで一括管理
        if (d.sound) this.playSound(d.sound, d.volume); 

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

    // 全ての音の再生はここを通す（すでに playOneShot でミキサーを完璧に通っています！）
    playSound(path, volume = null) {
        if (!path) return;
        Bgm.playOneShot(path, volume); 
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
                // 【修正】BGM停止をBgmクラスに委譲
                Bgm.stopBGM();
                this.onEnd();
            }
        }
    }

    pause() {
        this.isPaused = true;
        clearTimeout(this.typingTimer);
        this.ui.menuOverlay.style.display = "flex";
        // 【修正】BGMを一時停止
        Bgm.pauseBGM();
    }

    resume() {
        this.isPaused = false;
        this.ui.menuOverlay.style.display = "none";
        // 【修正】BGMを安全に再開
        Bgm.resumeBGM();
        if (this.isTyping) this.typeLoop(this.dialogues[this.index].message);
    }

    changeBGM(newPath, volume = null) {
        if (this.bgmPath === newPath) return;
        this.bgmPath = newPath;
        // 【修正】ミキサー経由でBGMを切り替え
        Bgm.playBGM(newPath);
    }
}