import { Bgm } from "../start/Bgm.js";
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
        
        this.bgm = new Audio(options.bgmPath);
        this.bgm.loop = true;

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
        if (d.sound) this.playSound(d.sound);

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
    playSound(path) {
        if (!path) return;
        const audio = new Audio(path);
        audio.volume = 0.2;
        Bgm.unlockPlay(audio);
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
    changeBGM(newPath) {
        if (this.bgm.src.includes(newPath)) return; // 同じ曲なら何もしない
        this.bgm.pause();
        this.bgm = new Audio(newPath);
        this.bgm.loop = true;
        Bgm.unlockPlay(this.bgm);
    }
}