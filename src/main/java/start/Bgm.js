/**
 * BGM管理クラス (JavaFXのBgmクラスを移植)
 */
export class Bgm {
    // Javaの private static フィールドの再現
    static #bgmPlayer = null;
    static #currentPath = null;
    static #currentStageNumber = 1;
    static #volume = 0.1; // JavaFXと同じく0.1に設定

    /**
     * BGMを指定のパスで再生します。
     * @param {string} path 
     */
    static playBGM(path) {
        // 同一のBGMが既に流れている場合はスキップ
        if (this.#bgmPlayer !== null && path === this.#currentPath) return;
        
        this.stopBGM();

        try {
            // HTML5 Audio インスタンスの作成 (MediaPlayerに相当)
            const audio = new Audio(path);
            
            // ループ設定 (INDEFINITEに相当)
            audio.loop = true;
            // 音量設定
            audio.volume = this.#volume;

            this.#bgmPlayer = audio;
            this.#currentPath = path;

            // 再生を開始
            const playPromise = this.#bgmPlayer.play();

            // ブラウザの自動再生ブロック（User Interaction制限）への対策
            if (playPromise !== undefined) {
                playPromise.catch(error => {
                    console.warn("自動再生がブロックされました。ユーザー操作後に再生されます。:", error);
                    // ユーザーが画面を一度クリックした時点で再生を再試行するイベントを登録
                    const playOnGesture = () => {
                        if (this.#bgmPlayer && this.#currentPath === path) {
                            this.#bgmPlayer.play().catch(e => console.error(e));
                        }
                        document.removeEventListener('click', playOnGesture);
                    };
                    document.addEventListener('click', playOnGesture);
                });
            }
        } catch (error) {
            console.error("BGMファイルの読み込みまたは再生に失敗しました: " + path, error);
        }
    }

    /**
     * ステージ番号からBGMパスを決定して再生
     * @param {number} stageNumber 
     */
    static playStageBGM(stageNumber) {
        this.#currentStageNumber = stageNumber;
        switch (stageNumber) {
            case 1: this.playBGM("../../resources/music/stage1_bgm.mp3"); break;
            case 2: this.playBGM("../../resources/music/stage2_bgm.mp3"); break;
            case 3: this.playBGM("../../resources/music/stage3_bgm.mp3"); break;
            default: this.playBGM("../../resources/music/stage1_bgm.mp3"); break;
        }
    }

    /**
     * FEVER開始
     */
    static playFeverBGM() {
        this.playBGM("../../resources/music/feverbgm.mp3");
    }

    /**
     * FEVER終了 → 元のステージBGMに復帰
     */
    static stopFeverBGM() {
        this.playStageBGM(this.#currentStageNumber);
    }

    /**
     * BGMを完全に停止してリソースを解放します (dispose相当)
     */
    static stopBGM() {
        if (this.#bgmPlayer !== null) {
            this.#bgmPlayer.pause();
            this.#bgmPlayer.currentTime = 0; // 再生位置を先頭に戻す
            this.#bgmPlayer = null;
            this.#currentPath = null;
        }
    }

    /**
     * BGMを一時停止します
     */
    static pauseBGM() {
        if (this.#bgmPlayer !== null) {
            this.#bgmPlayer.pause();
        }
    }

    /**
     * BGMの一時停止を解除して再生します
     */
    static resumeBGM() {
        if (this.#bgmPlayer !== null) {
            this.#bgmPlayer.play().catch(e => console.error("BGMの再開に失敗:", e));
        }
    }
}