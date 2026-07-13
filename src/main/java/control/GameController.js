// 方向の定義（JavaのDirection Enumの代わり）
const Direction = {
    UP: 'UP',
    DOWN: 'DOWN',
    LEFT: 'LEFT',
    RIGHT: 'RIGHT'
};

class GameController {
    // ハイスコア用の静的プロパティ
    static newRecord = false;

    static isNewRecord() {
        return GameController.newRecord;
    }

    constructor(model, view, canvas, stageNumber, isPractice) {
        this.model = model;          // パックマンの位置やマップ状態を持つデータソース
        this.view = view;            // 描画処理クラス
        this.canvas = canvas;        // HTML5 Canvas要素
        this.ctx = canvas.getContext('2d');
        this.stageNumber = stageNumber;
        this.isPractice = isPractice;

        this.animationFrameId = null; // JavaScriptのゲームループ用ID
        this.pauseLayer = null;       // ポーズ画面のDOM要素

        // フリック操作に必要な変数
        this.touchStart = [0, 0];
        this.FLICK_THRESHOLD = 30.0;

        // BGMの再生
        this.playStageBgm(stageNumber);

        // キーボードの入力を登録
        this.attachInput();

        // スマホ用の十字キー・スワイプコントローラーを適用
        this.applyMobileControls();

        // キャンバスサイズをウィンドウに同期
        this.resizeCanvas();
        window.addEventListener('resize', () => this.resizeCanvas());

        // メインゲームループの開始
        this.startLoop();
    }

    // キャンバスサイズの同期 (JavaFXのbindに相当)
    resizeCanvas() {
        if (this.canvas) {
            this.canvas.width = window.innerWidth;
            this.canvas.height = window.innerHeight;
        }
    }

    // スマホ用のコントローラーとスワイプ操作を画面に適用
    applyMobileControls() {
        if (!this.model) return;

        // --- 1. 十字キー (HTML/DOMで配置することを想定) ---
        // JSではHTML側であらかじめ用意したボタンにイベントを設定するか、
        // 動的にDOMを生成してbody等に追加します。ここではイベント設定の共通ロジックを示します。
        const sendDirection = (dir) => {
            try {
                if (typeof this.model.isPaused === 'function' && !this.model.isPaused()) {
                    if (typeof this.model.setNextDirection === 'function') {
                        this.model.setNextDirection(dir);
                    }
                }
            } catch (ex) {
                console.error(ex);
            }
        };

        // HTML側に id="btnUp" 等のボタンがある場合のバインド例
        // ※ 実際のHTML構成に合わせて調整してください
        const ids = { btnUp: Direction.UP, btnDown: Direction.DOWN, btnLeft: Direction.LEFT, btnRight: Direction.RIGHT };
        Object.entries(ids).forEach(([id, dir]) => {
            const btn = document.getElementById(id);
            if (btn) {
                // JavaFXのOnMousePressedに対応（スマホの連打を考慮して pointerdown や mousedown）
                btn.addEventListener('pointerdown', (e) => {
                    e.preventDefault();
                    sendDirection(dir);
                });
            }
        });

        // --- 2. スワイプ操作を検知する処理 ---
        window.addEventListener('pointerdown', (e) => {
            this.touchStart[0] = e.clientX;
            this.touchStart[1] = e.clientY;
        });

        window.addEventListener('pointerup', (e) => {
            const deltaX = e.clientX - this.touchStart[0];
            const deltaY = e.clientY - this.touchStart[1];

            const absX = Math.abs(deltaX);
            const absY = Math.abs(deltaY);

            if (absX > this.FLICK_THRESHOLD || absY > this.FLICK_THRESHOLD) {
                if (absX > absY) {
                    // 横方向のフリック
                    if (deltaX > 0) {
                        sendDirection(Direction.RIGHT);
                    } else {
                        sendDirection(Direction.LEFT);
                    }
                } else {
                    // 縦方向のフリック
                    if (deltaY > 0) {
                        sendDirection(Direction.DOWN);
                    } else {
                        sendDirection(Direction.UP);
                    }
                }
            }
        });
    }

    // キーボード入力処理
    attachInput() {
        window.addEventListener('keydown', (e) => {
            if (!this.model) return;

            try {
                const key = e.key.toUpperCase();

                // Pキーでゲームを一時停止・再開
                if (key === 'P') {
                    if (typeof this.model.togglePause === 'function') {
                        this.model.togglePause();
                    }

                    if (this.pauseLayer) {
                        const isPaused = this.model.isPaused();
                        if (isPaused) {
                            this.pauseLayer.style.display = 'block'; // 表示
                            this.pauseLayer.focus();
                        } else {
                            this.pauseLayer.style.display = 'none';  // 非表示
                            this.canvas.focus();
                        }
                    }
                    return;
                }

                // 一時停止中は入力を受け付けない
                if (typeof this.model.isPaused === 'function' && this.model.isPaused()) {
                    return;
                }

                // 矢印キーおよび WASD 操作の委譲
                if (typeof this.model.setNextDirection === 'function') {
                    if (key === 'W' || e.key === 'ArrowUp') this.model.setNextDirection(Direction.UP);
                    if (key === 'S' || e.key === 'ArrowDown') this.model.setNextDirection(Direction.DOWN);
                    if (key === 'A' || e.key === 'ArrowLeft') this.model.setNextDirection(Direction.LEFT);
                    if (key === 'D' || e.key === 'ArrowRight') this.model.setNextDirection(Direction.RIGHT);
                }
            } catch (ex) {
                console.error(ex);
            }
        });
    }

    // メインゲームループ (AnimationTimerの代わり)
    startLoop() {
        const loop = (now) => {
            try {
                if (!this.model || !this.view) return;

                // 一時停止フラグの確認
                const isPaused = typeof this.model.isPaused === 'function' ? this.model.isPaused() : false;

                if (!isPaused) {
                    // ゲーム状態の更新
                    if (typeof this.model.update === 'function') {
                        this.model.update();
                    }

                    // 敵に捕まった（ゲームオーバー）かチェック
                    if (typeof this.model.isGameOver === 'function' && this.model.isGameOver()) {
                        this.stop();
                        if (typeof Bgm !== 'undefined') Bgm.stopBGM();
                        console.log("💀 敵に捕まりました...ゲームオーバー画面へ遷移します。");

                        let finalScore = 0;
                        if (typeof this.model.getsyujinkou === 'function') {
                            const syujinkou = this.model.getsyujinkou();
                            if (syujinkou && typeof syujinkou.getScore === 'function') {
                                finalScore = syujinkou.getScore();
                            }
                        }

                        // 練習モードだけハイスコア更新
                        if (this.isPractice) {
                            if (typeof HighScoreManager !== 'undefined') {
                                GameController.newRecord = HighScoreManager.updateHighScore(this.stageNumber, finalScore);
                            }
                        } else {
                            GameController.newRecord = false;
                        }

                        // GameOver画面へ切り替え
                        GameController.switchToGameover(this.stageNumber, this.isPractice, finalScore);
                        return;
                    }

                    // すべてのドットを食べ終えたかチェック
                    if (typeof this.model.isCleared === 'function' && this.model.isCleared()) {
                        if (this.isPractice) {
                            // 練習モード：エサを復活させてループを継続
                            if (typeof this.model.respawnDots === 'function') {
                                this.model.respawnDots();
                            }
                        } else {
                            // 本番モード：タイマーを止めてクリア画面へ遷移
                            this.stop();
                            if (typeof Bgm !== 'undefined') Bgm.stopBGM();
                            console.log("🏁 本番モード：ステージクリア！次の画面へ。");

                            let finalScore = 0;
                            if (typeof this.model.getsyujinkou === 'function') {
                                const syujinkou = this.model.getsyujinkou();
                                if (syujinkou && typeof syujinkou.getScore === 'function') {
                                    finalScore = syujinkou.getScore();
                                }
                            }

                            switch (this.stageNumber) {
                                case 1: GameController.switchToStageclear1(finalScore); break;
                                case 2: GameController.switchToStageclear2(finalScore); break;
                                case 3: GameController.switchToStageclear3(finalScore); break;
                                default: GameController.switchToStageclear1(finalScore); break;
                            }
                            return;
                        }
                    }
                }

                // 描画処理 (一時停止中も常に実行)
                if (typeof this.view.draw === 'function') {
                    this.view.draw(this.ctx, this.canvas.width, this.canvas.height);
                }

                // 次のフレームを要求
                this.animationFrameId = requestAnimationFrame(loop);

            } catch (ex) {
                console.error(ex);
                this.stop();
            }
        };

        // ループを開始
        this.animationFrameId = requestAnimationFrame(loop);
    }

    // 外部から安全にループを停止させてタイトルへ戻るための専用メソッド
    forceBackToTitle() {
        try {
            console.log("① forceBackToTitle開始");
            this.stop();
            if (typeof Bgm !== 'undefined') Bgm.stopBGM();
            console.log("② timer停止");
            GameController.switchStart();
            console.log("③ switchStart完了");
        } catch (e) {
            console.error(e);
        }
    }

    // BGM処理
    playStageBgm(stageNumber) {
        if (typeof Bgm !== 'undefined' && typeof Bgm.playStageBGM === 'function') {
            Bgm.playStageBGM(stageNumber);
        }
    }

    // ポーズレイヤー（DOM要素）の設定
    setPauseLayer(pauseLayerElement) {
        this.pauseLayer = pauseLayerElement;
    }

    // ゲーム停止
    stop() {
        if (this.animationFrameId) {
            cancelAnimationFrame(this.animationFrameId);
            this.animationFrameId = null;
        }
    }

    // === 画面遷移用のスタブメソッド群 ===
    // Webアプリ全体の画面遷移システム（React, Vue, あるいは自作のRouter等）に合わせて中身を実装してください。
    static switchStart() { console.log("画面遷移: Start"); }
    static switchToHelp() { console.log("画面遷移: Help"); }
    static switchToPractice() { console.log("画面遷移: Practice"); }
    static startToStory() { console.log("画面遷移: Story1"); }
    static switchStory2() { console.log("画面遷移: Story2"); }
    static switchStory3() { console.log("画面遷移: Story3"); }
    static switchStory4() { console.log("画面遷移: Story4"); }
    static switchStoryClear() { console.log("画面遷移: StoryClear"); }
    static switchToGame1() { console.log("画面遷移: Main1"); }
    static switchToGame2() { console.log("画面遷移: Main2"); }
    static switchToGame3() { console.log("画面遷移: Main3"); }
    static switchToStageclear1(score) { console.log(`画面遷移: Stageclear1 (Score: ${score})`); }
    static switchToStageclear2(score) { console.log(`画面遷移: Stageclear2 (Score: ${score})`); }
    static switchToStageclear3(score) { console.log(`画面遷移: Stageclear3 (Score: ${score})`); }
    static switchToPracticeGame1() { console.log("画面遷移: PracticeMain1"); }
    static switchToPracticeGame2() { console.log("画面遷移: PracticeMain2"); }
    static switchToPracticeGame3() { console.log("画面遷移: PracticeMain3"); }

    static switchToGameover(stageNum, isPractice, score) {
        console.log(`画面遷移: Gameover (Stage: ${stageNum}, Practice: ${isPractice}, Score: ${score})`);
        // ここでリトライ処理を組み立ててGameover画面に渡すロジックを実装します
    }
}