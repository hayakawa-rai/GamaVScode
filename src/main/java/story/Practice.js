/**
 * Practice画面
 * JavaFX版 Practice.java を JavaScript化
 */
class Practice {

    constructor() {

        // 背景アニメーションID
        this.animationId = null;

        // 効果音
        this.clickSound = new Audio("music/select.mp3");
        this.cancelSound = new Audio("music/cancel.mp3");

        this.clickSound.volume = 0.4;
        this.cancelSound.volume = 0.4;

        // 背景スクロール位置
        this.scrollX = 0;
    }

    /**
     * 画面終了時の後始末
     */
    cleanup() {

        // 背景アニメーション停止
        if (this.animationId) {
            cancelAnimationFrame(this.animationId);
            this.animationId = null;
        }

        // 効果音停止
        this.clickSound.pause();
        this.clickSound.currentTime = 0;

        this.cancelSound.pause();
        this.cancelSound.currentTime = 0;

        // BGM停止
        Bgm.stopBGM();
    }

    /**
     * 画面作成
     */
    createScene() {

        // =====================================
        // ルートコンテナ
        // =====================================
        const root = document.createElement("div");
        root.className = "practice-root";

        // =====================================
        // 背景レイヤー
        // =====================================
        const bgPane = document.createElement("div");
        bgPane.className = "background-pane";

        // =====================================
        // タイトル
        // =====================================
        const title = document.createElement("h1");
        title.textContent = "練習モード";

        // =====================================
        // ステージボタン
        // =====================================
        const btn1 = document.createElement("button");
        btn1.textContent = "STAGE 1";

        const btn2 = document.createElement("button");
        btn2.textContent = "STAGE 2";

        const btn3 = document.createElement("button");
        btn3.textContent = "STAGE 3";

        btn1.classList.add("game-button");
        btn2.classList.add("game-button");
        btn3.classList.add("game-button");

        // =====================================
        // ハイスコアアイコン
        // =====================================
        const scoreInfo = document.createElement("div");
        scoreInfo.textContent = "🏆";
        scoreInfo.className = "score-info";

        // =====================================
        // ハイスコアツールチップ
        // =====================================
        const highScoreTip = document.createElement("div");
        highScoreTip.className = "tooltip";

        highScoreTip.innerText =

    `★ HIGH SCORE ★

        STAGE1 : ${HighScoreManager.loadHighScore(1)}

        STAGE2 : ${HighScoreManager.loadHighScore(2)}

        STAGE3 : ${HighScoreManager.loadHighScore(3)}`;

        highScoreTip.style.display = "none";

        scoreInfo.addEventListener("click", (e) => {

            if (highScoreTip.style.display === "block") {

                highScoreTip.style.display = "none";

            } else {

                highScoreTip.style.left =
                    `${Math.max(20, e.pageX - 350)}px`;

                highScoreTip.style.top =
                    `${e.pageY + 20}px`;

                highScoreTip.style.display = "block";
            }
        });

        // =====================================
        // タイトルへ戻るボタン
        // =====================================
        const backButton = document.createElement("button");
        backButton.textContent = "タイトルへ";
        backButton.classList.add("game-button");

        // =====================================
        // ステージボタンエリア
        // =====================================
        const stageButtons = document.createElement("div");
        stageButtons.className = "stage-buttons";

        stageButtons.appendChild(btn1);
        stageButtons.appendChild(btn2);
        stageButtons.appendChild(btn3);

        // =====================================
        // 中央UI
        // =====================================
        const uiContainer = document.createElement("div");
        uiContainer.className = "ui-container";

        uiContainer.appendChild(title);
        uiContainer.appendChild(stageButtons);

        // =====================================
        // STAGE1
        // =====================================
        btn1.addEventListener("click", () => {

            this.clickSound.pause();
            this.clickSound.currentTime = 0;
            this.clickSound.play();

            setTimeout(() => {

                this.cleanup();

                GameController.switchToPracticeGame1();

            }, 500);

        });

        // =====================================
        // STAGE2
        // =====================================
        btn2.addEventListener("click", () => {

            this.clickSound.pause();
            this.clickSound.currentTime = 0;
            this.clickSound.play();

            setTimeout(() => {

                this.cleanup();

                GameController.switchToPracticeGame2();

            }, 500);

        });

        // =====================================
        // STAGE3
        // =====================================
        btn3.addEventListener("click", () => {

            this.clickSound.pause();
            this.clickSound.currentTime = 0;
            this.clickSound.play();

            setTimeout(() => {

                this.cleanup();

                GameController.switchToPracticeGame3();

            }, 500);

        });

        // =====================================
        // タイトルへ戻る
        // =====================================
        backButton.addEventListener("click", () => {

            this.cancelSound.pause();
            this.cancelSound.currentTime = 0;
            this.cancelSound.play();

            setTimeout(() => {

                this.cleanup();

                GameController.switchStart();

            }, 500);

        });

        // =====================================
        // 背景スクロールアニメーション
        // =====================================
        let bgPosition = 0;

        const animateBackground = () => {

            bgPosition--;

            bgPane.style.backgroundPositionX =
                `${bgPosition}px`;

            this.animationId =
                requestAnimationFrame(animateBackground);

        };

        animateBackground();

        // =====================================
        // レスポンシブ処理
        // =====================================
        const updateResponsive = () => {

            const width = window.innerWidth;
            const height = window.innerHeight;

            // タイトルサイズ
            const titleSize =
                Math.max(24, width * 0.048);

            title.style.fontSize =
                `${Math.min(titleSize, 48)}px`;

            // トロフィーサイズ
            scoreInfo.style.fontSize =
                `${Math.max(24, width * 0.05)}px`;

            // ステージボタン
            const stageWidth =
                Math.max(160, width * 0.35);

            const stageHeight =
                Math.max(44, height * 0.10);

            [btn1, btn2, btn3].forEach(btn => {

                btn.style.width =
                    `${stageWidth}px`;

                btn.style.height =
                    `${stageHeight}px`;

                btn.style.fontSize =
                    `${Math.min(
                        26,
                        Math.max(14, width * 0.045)
                    )}px`;
            });

            // 戻るボタン
            backButton.style.width =
                `${Math.max(120, width * 0.20)}px`;

            backButton.style.height =
                `${Math.max(36, height * 0.07)}px`;

            backButton.style.fontSize =
                `${Math.min(
                    20,
                    Math.max(12, width * 0.035)
                )}px`;

            // ツールチップ
            const tooltipFontSize =
                Math.max(18, width * 0.02);

            highScoreTip.style.fontSize =
                `${tooltipFontSize}px`;
        };

        updateResponsive();

        window.addEventListener(
            "resize",
            updateResponsive
        );

        // =====================================
        // レイヤー組み立て
        // =====================================
        root.appendChild(bgPane);
        root.appendChild(uiContainer);
        root.appendChild(scoreInfo);
        root.appendChild(backButton);
        root.appendChild(highScoreTip);

        // =====================================
        // bodyに配置
        // =====================================
        document.body.innerHTML = "";
        document.body.appendChild(root);

        return root;
    }
}