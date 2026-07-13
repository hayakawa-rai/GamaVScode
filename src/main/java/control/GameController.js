// ==========================================
// ゲーム本編コントローラー (GameController.js)
// ==========================================

// 方向の定義 (JavaFXのCharacters.Directionに相当)
const Direction = {
    UP: 'UP',
    DOWN: 'DOWN',
    LEFT: 'LEFT',
    RIGHT: 'RIGHT'
};

class GameController {
    /**
     * @param {Object} model - キャラ位置やマップ状態のデータソース
     * @param {Object} view - 描画処理クラス (drawメソッドを持つもの)
     * @param {HTMLCanvasElement} canvas - 描画先キャンバス
     * @param {number} stageNumber - 現在のステージ番号 (1〜3)
     * @param {boolean} isPractice - 練習モードフラグ
     */
    constructor(model, view, canvas, stageNumber, isPractice) {
        this.model = model;
        this.view = view;
        this.canvas = canvas;
        this.stageNumber = stageNumber;
        this.isPractice = isPractice;

        this.ctx = canvas.getContext('2d');
        this.animationFrameId = null; 
        this.isTransitioning = false;  

        // タッチ・フリック操作用
        this.touchStart = [0, 0];
        this.FLICK_THRESHOLD = 30.0;

        // ポーズ画面用レイヤー (HTML要素)
        this.pauseLayer = null;

        // 初期化処理の実行
        this.playStageBgm(this.stageNumber);
        this.attachInput();
        this.applyMobileControls();
        this.startLoop();
    }

    // ステージに応じたBGM再生
    playStageBgm(stageNumber) {
        if (typeof Bgm !== 'undefined' && Bgm.playStageBGM) {
            Bgm.playStageBGM(stageNumber);
        } else {
            console.log(`BGM Play: Stage ${stageNumber}`);
        }
    }

    // キーボード入力処理
    attachInput() {
        window.addEventListener('keydown', (e) => {
            if (!this.model) return;

            const code = e.key;

            // 1. 【Pキー】：ポーズ切り替え
            if (code === 'p' || code === 'P') {
                this.togglePauseProperties();
                return;
            }

            // 2. 【Cキー】：デバッグ用強制クリア
            if (code === 'c' || code === 'C') {
                if (typeof this.model.forceStageClear === 'function') {
                    this.model.forceStageClear();
                } else {
                    console.log("⚠️ forceStageClear メソッドがありません");
                }
                return;
            }

            // ポーズ中は操作を受け付けない
            if (typeof this.model.isPaused === 'function' && this.model.isPaused()) {
                return;
            }

            // 3. 移動方向のセット (WASD / 矢印キー)
            if (typeof this.model.setNextDirection === 'function') {
                if (code === 'w' || code === 'ArrowUp') this.model.setNextDirection(Direction.UP);
                if (code === 's' || code === 'ArrowDown') this.model.setNextDirection(Direction.DOWN);
                if (code === 'a' || code === 'ArrowLeft') this.model.setNextDirection(Direction.LEFT);
                if (code === 'd' || code === 'ArrowRight') this.model.setNextDirection(Direction.RIGHT);
            }
        });
    }

    // フリック操作（スマホ用）の検知処理
    applyMobileControls() {
        if (!this.model) return;

        const sendDirection = (dir) => {
            if (typeof this.model.isPaused === 'function' && this.model.isPaused()) return;
            if (typeof this.model.setNextDirection === 'function') {
                this.model.setNextDirection(dir);
            }
        };

        window.addEventListener('touchstart', (e) => {
            this.touchStart[0] = e.touches[0].clientX;
            this.touchStart[1] = e.touches[0].clientY;
        }, { passive: true });

        window.addEventListener('touchend', (e) => {
            const deltaX = e.changedTouches[0].clientX - this.touchStart[0];
            const deltaY = e.changedTouches[0].clientY - this.touchStart[1];

            const absX = Math.abs(deltaX);
            const absY = Math.abs(deltaY);

            if (absX > this.FLICK_THRESHOLD || absY > this.FLICK_THRESHOLD) {
                if (absX > absY) {
                    if (deltaX > 0) sendDirection(Direction.RIGHT);
                    else sendDirection(Direction.LEFT);
                } else {
                    if (deltaY > 0) sendDirection(Direction.DOWN);
                    else sendDirection(Direction.UP);
                }
            }
        }, { passive: true });
    }

    // ゲームループ(毎フレーム実行)
    startLoop() {
        const loop = () => {
            try {
                if (!this.model || !this.view) return;

                const isPaused = typeof this.model.isPaused === 'function' ? this.model.isPaused() : false;

                if (!isPaused) {
                    if (typeof this.model.update === 'function') {
                        this.model.update();
                    }

                    // 💀 ゲームオーバー判定
                    if (typeof this.model.isGameOver === 'function' && this.model.isGameOver()) {
                        this.stop();
                        if (this.isTransitioning) return;
                        this.isTransitioning = true;

                        if (typeof Bgm !== 'undefined' && Bgm.stopBGM) Bgm.stopBGM();
                        console.log("💀 ゲームオーバー画面へ遷移します。");

                        let finalScore = 0;
                        if (this.model.getsyujinkou && this.model.getsyujinkou()) {
                            const syujinkou = this.model.getsyujinkou();
                            if (syujinkou.getScore) finalScore = syujinkou.getScore();
                        }

                        if (this.isPractice && typeof HighScoreManager !== 'undefined') {
                            HighScoreManager.updateHighScore(this.stageNumber, finalScore);
                        }

                        setTimeout(() => {
                            this.switchToGameover(this.stageNumber, this.isPractice, finalScore);
                        }, 0);
                        return;
                    }

                    // 🏁 ステージクリア判定
                    if (typeof this.model.isCleared === 'function' && this.model.isCleared()) {
                        if (this.isPractice) {
                            if (typeof this.model.respawnDots === 'function') {
                                this.model.respawnDots(); // 練習モードはドット再配置
                            }
                        } else {
                            this.stop();
                            if (this.isTransitioning) return;
                            this.isTransitioning = true;

                            if (typeof Bgm !== 'undefined' && Bgm.stopBGM) Bgm.stopBGM();
                            console.log("🏁 ステージクリア！");

                            let finalScore = 0;
                            if (this.model.getsyujinkou && this.model.getsyujinkou()) {
                                const syujinkou = this.model.getsyujinkou();
                                if (syujinkou.getScore) finalScore = syujinkou.getScore();
                            }

                            setTimeout(() => {
                                this.switchToStageclear(this.stageNumber, finalScore);
                            }, 0);
                            return;
                        }
                    }
                }

                // 🎨 描画処理
                const currentWidth = this.canvas.width;
                const currentHeight = this.canvas.height;
                if (typeof this.view.draw === 'function') {
                    this.view.draw(this.ctx, currentWidth, currentHeight);
                }

                this.animationFrameId = requestAnimationFrame(loop);

            } catch (ex) {
                console.error(ex);
                this.stop();
            }
        };

        this.animationFrameId = requestAnimationFrame(loop);
    }

    // ポーズレイヤーの表示切り替え
    togglePauseProperties() {
        if (!this.model || typeof this.model.togglePause !== 'function') return;

        this.model.togglePause();
        const isPaused = this.model.isPaused();

        if (this.pauseLayer) {
            if (isPaused) {
                this.pauseLayer.style.pointerEvents = "auto";
                this.pauseLayer.style.display = "flex";
            } else {
                this.pauseLayer.style.pointerEvents = "none";
                this.pauseLayer.style.display = "none";
                this.canvas.focus();
            }
        }
    }

    togglePauseByButton() {
        this.togglePauseProperties();
    }

    setPauseLayer(pauseLayerElement) {
        this.pauseLayer = pauseLayerElement;
    }

    stop() {
        if (this.animationFrameId) {
            cancelAnimationFrame(this.animationFrameId);
            this.animationFrameId = null;
        }
    }

    // ゲーム中からタイトル画面へ強制離脱する処理
    forceBackToTitle() {
        this.stop();
        if (typeof Bgm !== 'undefined' && Bgm.stopBGM) Bgm.stopBGM();
        console.log("インゲームループ停止 -> タイトルへ遷移");
        
        window.location.href = "index.html"; 
    }

    switchToStageclear(stageNum, score) {
        console.log(`Navigate to StageClear ${stageNum} with score: ${score}`);
        // 必要に応じて window.location.href = "clear.html"; などを追加
    }

    switchToGameover(stageNum, isPractice, score) {
        console.log(`Navigate to GameOver. Stage: ${stageNum}, Practice: ${isPractice}, Score: ${score}`);
        // 必要に応じて window.location.href = "gameover.html"; などを追加
    }
}