/**
 * BGM管理クラス
 * ※ 自動再生ブロック対策(unlockPlay)は、BGM以外の効果音・台詞音でも
 *    共通で使うためこのクラスの静的メソッドとして公開している。
 */
export class Bgm {
    static #bgmPlayer = null;
    static #currentPath = null;
    static #currentStageNumber = 1;
    static #volume = 0.1;

    // ==================================================
    // ★自動再生ブロック対策（旧AudioUnlock.js）
    // ==================================================
    static #pending = new Set();
    static #listenersAttached = false;

    static #retryPending() {
        Bgm.#pending.forEach(audio => {
            audio.play().catch(err => {
                console.warn("再生に失敗しました:", audio.src, err);
            });
        });
        Bgm.#pending.clear();
        Bgm.#detachListeners();
    }

    static #attachListeners() {
        if (Bgm.#listenersAttached) return;
        Bgm.#listenersAttached = true;
        document.addEventListener("pointerdown", Bgm.#retryPending, { passive: true });
        document.addEventListener("keydown", Bgm.#retryPending);
    }

    static #detachListeners() {
        document.removeEventListener("pointerdown", Bgm.#retryPending);
        document.removeEventListener("keydown", Bgm.#retryPending);
        Bgm.#listenersAttached = false;
    }

    /**
     * audioElement.play() の代わりに必ずこれを呼ぶ。
     * BGM・効果音・台詞音、すべてこのメソッド経由で再生する。
     * 自動再生がブロックされた場合は、次のユーザー操作(クリック/タップ/キー入力)で
     * 保留中の音がまとめて再試行される。呼び出し側でcatchを書く必要はない。
     */
    static unlockPlay(audioElement) {
        const playPromise = audioElement.play();
        if (playPromise !== undefined) {
            playPromise.catch(() => {
                Bgm.#pending.add(audioElement);
                Bgm.#attachListeners();
            });
        }
        return playPromise;
    }

    // ==================================================
    // 既存のBGM管理機能
    // ==================================================
    static playBGM(path) {
        if (this.#bgmPlayer !== null && path === this.#currentPath) return;

        this.stopBGM();

        try {
            const audio = new Audio(path);
            audio.loop = true;
            audio.volume = this.#volume;

            this.#bgmPlayer = audio;
            this.#currentPath = path;

            Bgm.unlockPlay(this.#bgmPlayer);
        } catch (error) {
            console.error("BGMファイルの読み込みまたは再生に失敗しました: " + path, error);
        }
    }

    static playStageBGM(stageNumber) {
        this.#currentStageNumber = stageNumber;
        switch (stageNumber) {
            case 1: this.playBGM("../../resources/music/stage1_bgm.mp3"); break;
            case 2: this.playBGM("../../resources/music/stage2_bgm.mp3"); break;
            case 3: this.playBGM("../../resources/music/stage3_bgm.mp3"); break;
            default: this.playBGM("../../resources/music/stage1_bgm.mp3"); break;
        }
    }

    static playFeverBGM() {
        this.playBGM("../../resources/music/feverbgm.mp3");
    }

    static stopFeverBGM() {
        this.playStageBGM(this.#currentStageNumber);
    }

    static stopBGM() {
        if (this.#bgmPlayer !== null) {
            this.#bgmPlayer.pause();
            this.#bgmPlayer.currentTime = 0;
            this.#bgmPlayer = null;
            this.#currentPath = null;
        }
    }

    static pauseBGM() {
        if (this.#bgmPlayer !== null) {
            this.#bgmPlayer.pause();
        }
    }

    static resumeBGM() {
        if (this.#bgmPlayer !== null) {
            Bgm.unlockPlay(this.#bgmPlayer);
        }
    }
}