/**
 * ゲーム全体を管理・制御するコントローラークラス (GameController)
 */
export class GameController {
    // クラス共通の静的（static）プロパティ
    static #touchStart = [0, 0];       // スワイプ開始座標 [x, y]
    static #FLICK_THRESHOLD = 30.0;    // スワイプ判定の閾値（ピクセル）
    static #newRecord = false;         // ハイスコア更新フラグ

    static isNewRecord() {
        return GameController.#newRecord;
    }

    /**
     * コンストラクタ
     * @param {Object} model - ゲームのデータ・ロジック（パックマンの位置や一時停止状態など）
     * @param {Object} view - 描画クラス
     * @param {HTMLCanvasElement} canvas - 描画先キャンバス
     * @param {number} stageNumber - ステージ番号 (1～3)
     * @param {boolean} isPractice - 練習モードかどうか
     */
    constructor(model, view, canvas, stageNumber, isPractice) {
        this.model = model;
        this.view = view;
        this.canvas = canvas;
        this.gc = canvas.getContext('2d');
        this.stageNumber = stageNumber;
        this.isPractice = isPractice;

        this.timerId = null;            // requestAnimationFrameのID
        this.pauseLayer = null;         // ポーズ画面のDOM要素
        this.isTransitioning = false;   // 二重遷移防止フラグ

        this.playStageBgm(stageNumber);
        this.attachInput();
        GameController.applyMobileControls(this.model);
        this.startLoop();
    }

    /**
     * スマホ・画面操作用：スワイプ（フリック）で移動方向を制御
     * @param {Object} model 
     */
    static applyMobileControls(model) {
        if (!model) return;

        // 共通の方向送信処理
        const sendDirection = (dir) => {
            // modelに該当のメソッドがあるか安全にチェックして実行（Javaのリフレクションの代替）
            if (typeof model.isPaused === 'function' && !model.isPaused()) {
                if (typeof model.setNextDirection === 'function') {
                    model.setNextDirection(dir);
                }
            }
        };

        // タッチ開始（マウスダウン）
        window.addEventListener('mousedown', (e) => {
            GameController.#touchStart[0] = e.clientX;
            GameController.#touchStart[1] = e.clientY;
        });

        // タッチ終了（マウスアップ）
        window.addEventListener('mouseup', (e) => {
            const deltaX = e.clientX - GameController.#touchStart[0];
            const deltaY = e.clientY - GameController.#touchStart[1];

            const absX = Math.abs(deltaX);
            const absY = Math.abs(deltaY);

            if (absX > GameController.#FLICK_THRESHOLD || absY > GameController.#FLICK_THRESHOLD) {
                if (absX > absY) {
                    // 横方向スワイプ
                    sendDirection(deltaX > 0 ? "RIGHT" : "LEFT");
                } else {
                    // 縦方向スワイプ
                    sendDirection(deltaY > 0 ? "DOWN" : "UP");
                }
            }
        });
    }

    /**
     * キーボード入力の受付（Pキーでポーズ、Cキーで強制クリア、WASD/矢印で移動）
     */
    attachInput() {
        window.addEventListener('keydown', (e) => {
            const key = e.key.toUpperCase();

            // 1. ポーズ処理 (Pキー)
            if (key === 'P') {
                if (typeof this.model.togglePause === 'function') {
                    this.model.togglePause();
                }

                if (this.pauseLayer) {
                    const isPaused = this.model.isPaused ? this.model.isPaused() : false;
                    if (isPaused) {
                        this.pauseLayer.style.pointerEvents = "auto"; // クリック許可
                        this.pauseLayer.style.display = "flex";       // レイヤー表示
                    } else {
                        this.pauseLayer.style.pointerEvents = "none";  // クリック透過
                        this.pauseLayer.style.display = "none";       // レイヤー非表示
                    }
                }
                return;
            }

            // 2. デバッグ用：強制ステージクリア (Cキー)
            if (key === 'C') {
                if (typeof this.model.forceStageClear === 'function') {
                    this.model.forceStageClear();
                } else {
                    console.log("⚠️ このモデルには forceStageClear メソッドがありません");
                }
                return;
            }

            // ポーズ中は移動キーを無視
            if (this.model.isPaused && this.model.isPaused()) return;

            // 3. 移動キー受付 (W, A, S, D / 矢印キー)
            if (typeof this.model.setNextDirection === 'function') {
                if (key === 'W' || e.key === 'ArrowUp')    this.model.setNextDirection("UP");
                if (key === 'S' || e.key === 'ArrowDown')  this.model.setNextDirection("DOWN");
                if (key === 'A' || e.key === 'ArrowLeft')  this.model.setNextDirection("LEFT");
                if (key === 'D' || e.key === 'ArrowRight') this.model.setNextDirection("RIGHT");
            }
        });
    }

    /**
     * メインのゲームループ (AnimationTimerの代替)
     */
    startLoop() {
        // キャンバスのリサイズ処理（Sceneとのバインドの再現）
        const resizeCanvas = () => {
            this.canvas.width = window.innerWidth;
            this.canvas.height = window.innerHeight;
        };
        window.addEventListener('resize', resizeCanvas);
        resizeCanvas();

        // 毎フレーム実行されるループ関数
        const loop = () => {
            try {
                const isPaused = this.model.isPaused ? this.model.isPaused() : false;

                if (!isPaused) {
                    // モデルロジックの更新
                    if (typeof this.model.update === 'function') {
                        this.model.update();
                    }

                    // 💀 ゲームオーバー判定
                    if (this.model.isGameOver && this.model.isGameOver()) {
                        this.stop();
                        if (this.isTransitioning) return;
                        this.isTransitioning = true;

                        if (window.Bgm) Bgm.stopBGM();
                        console.log("💀 敵に捕まりました...ゲームオーバー画面へ遷移します。");

                        let finalScore = 0;
                        if (this.model.getsyujinkou) {
                            const syujinkou = this.model.getsyujinkou();
                            if (syujinkou && typeof syujinkou.getScore === 'function') {
                                finalScore = syujinkou.getScore();
                            }
                        }

                        // 練習モードのときだけハイスコア更新
                        if (this.isPractice && window.HighScoreManager) {
                            GameController.#newRecord = HighScoreManager.updateHighScore(this.stageNumber, finalScore);
                        } else {
                            GameController.#newRecord = false;
                        }

                        // 画面遷移の実行（非同期処理の Platform.runLater の代わりに setTimeout を利用）
                        setTimeout(() => {
                            GameController.switchToGameover(this.stageNumber, this.isPractice, finalScore);
                        }, 0);
                        return;
                    }

                    // 🏁 ステージクリア判定
                    if (this.model.isCleared && this.model.isCleared()) {
                        if (this.isPractice) {
                            // 練習モード：エサを復活させてそのまま続行
                            if (typeof this.model.respawnDots === 'function') {
                                this.model.respawnDots();
                            }
                        } else {
                            // 本番モード：ループを止めてクリア画面へ
                            this.stop();
                            if (this.isTransitioning) return;
                            this.isTransitioning = true;

                            if (window.Bgm) Bgm.stopBGM();
                            console.log("🏁 ステージクリア！");

                            let finalScore = 0;
                            if (this.model.getsyujinkou) {
                                const syujinkou = this.model.getsyujinkou();
                                if (syujinkou && typeof syujinkou.getScore === 'function') {
                                    finalScore = syujinkou.getScore();
                                }
                            }

                            setTimeout(() => {
                                GameController.switchToStageclear(this.stageNumber, finalScore);
                            }, 0);
                            return;
                        }
                    }
                }

                // 🎨 描画処理 (一時停止中も常に画面は描画する)
                if (this.view && typeof this.view.draw === 'function') {
                    this.view.draw(this.gc, this.canvas.width, this.canvas.height);
                }

                // 次のフレームを要求
                this.timerId = requestAnimationFrame(loop);

            } catch (ex) {
                console.error("ループ内エラー:", ex);
                this.stop();
            }
        };

        // ループ開始
        this.timerId = requestAnimationFrame(loop);
    }

    /**
     * タイトルへ強制的に戻る（メニューのボタン等から呼ばれる）
     */
    forceBackToTitle() {
        this.stop();
        if (window.Bgm) Bgm.stopBGM();
        console.log("② timer停止");
        GameController.switchStart();
    }

    /**
     * BGM再生の委譲
     */
    playStageBgm(stageNumber) {
        if (window.Bgm && typeof Bgm.playStageBGM === 'function') {
            Bgm.playStageBGM(stageNumber);
        }
    }

    /**
     * ポーズ画面用レイヤー(DOM要素)の紐付け
     */
    setPauseLayer(pauseLayerElement) {
        this.pauseLayer = pauseLayerElement;
    }
	
    /**
     * メニュー内の「一時停止ボタン」用メソッド
     */
    togglePauseByButton() {
        if (typeof this.model.togglePause === 'function') {
            this.model.togglePause();
        }

        if (this.pauseLayer) {
            const isPaused = this.model.isPaused ? this.model.isPaused() : false;
            if (isPaused) {
                this.pauseLayer.style.pointerEvents = "auto";
                this.pauseLayer.style.display = "flex";
            } else {
                this.pauseLayer.style.pointerEvents = "none";
                this.pauseLayer.style.display = "none";
            }
        }
    }

    /**
     * ゲームループを停止する
     */
    stop() {
        if (this.timerId) {
            cancelAnimationFrame(this.timerId);
            this.timerId = null;
        }
    }

    // ==========================================
    // 画面遷移メソッド群（Web環境では各URLへのリダイレクトや、SPAなら画面パーツの切り替えを行います）
    // ==========================================
    static switchStart() {
        window.location.href = "../start/Start.html"; 
    }

    static switchToHelp() {
        window.location.href = "Help.html";
    }

    static switchToPractice() {
        window.location.href = "../story/Practice.html";
    }

    static startToStory() {
        window.location.href = "../story/Story1.html";
    }

    static startToStory2() {
        window.location.href = "../story/Story2.html";
    }

    static startToStory3() {
        window.location.href = "Story3.html";
    }

    static startToStory4() {
        window.location.href = "Story4.html";
    }

    static switchToStageclear(stageNum, score) {
        // 例: stageclear1.html?score=1200 のようにクエリパラメータでスコアを渡す設計
        window.location.href = `../story/Stageclear${stageNum}.html?score=${score}`;
    }

    static switchToGameover(stageNum, isPractice, score) {
        window.location.href = `../story/Gameover.html?stage=${stageNum}&practice=${isPractice}&score=${score}`;
    }
}