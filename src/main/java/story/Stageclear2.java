/**
 * Stage2クリア画面
 * JavaFX版 Stageclear2.java を JavaScript化
 */
class Stageclear2 {

    constructor() {

        // 効果音
        this.clearSound = null;
        this.clickSound = null;
        this.cancelSound = null;

        // スコア
        this.score = 0;

        // タイマー管理
        this.delayTimeout = null;
        this.pauseTimeout = null;
    }

    /**
     * スコア設定
     */
    setScore(score) {
        this.score = score;
    }

    /**
     * Java版 createAndStart()
     */
    static createAndStart(finalScore) {

        const instance = new Stageclear2();

        instance.setScore(finalScore);

        instance.createScene();
    }

    /**
     * 後始末
     */
    cleanup() {

        if (this.delayTimeout) {
            clearTimeout(this.delayTimeout);
            this.delayTimeout = null;
        }

        if (this.pauseTimeout) {
            clearTimeout(this.pauseTimeout);
            this.pauseTimeout = null;
        }

        if (this.clearSound) {
            this.clearSound.pause();
            this.clearSound.currentTime = 0;
        }

        if (this.clickSound) {
            this.clickSound.pause();
            this.clickSound.currentTime = 0;
        }

        if (this.cancelSound) {
            this.cancelSound.pause();
            this.cancelSound.currentTime = 0;
        }
    }

    /**
     * 画面生成
     */
    createScene() {

        // =====================================
        // クリア音読み込み
        // =====================================
        try {

            this.clearSound =
                new Audio("music/yay.mp3");

            this.clearSound.volume = 0.5;

        } catch (e) {

            console.error(
                "クリア音の読み込みに失敗しました",
                e
            );
        }

        // =====================================
        // 決定音読み込み
        // =====================================
        try {

            this.clickSound =
                new Audio("music/select.mp3");

            this.clickSound.volume = 0.4;

        } catch (e) {

            console.error(
                "選択SEの読み込みに失敗しました",
                e
            );
        }

        // =====================================
        // キャンセル音読み込み
        // =====================================
        try {

            this.cancelSound =
                new Audio("music/cancel.mp3");

            this.cancelSound.volume = 0.4;

        } catch (e) {

            console.error(
                "キャンセルSEの読み込みに失敗しました",
                e
            );
        }

        // =====================================
        // 0.5秒後にクリア音再生
        // =====================================
        this.delayTimeout = setTimeout(() => {

            if (this.clearSound) {
                this.clearSound.play();
            }

        }, 500);

        // =====================================
        // ルートコンテナ
        // =====================================
        const root =
            document.createElement("div");

        root.className =
            "stageclear-root";

        // =====================================
        // タイトル
        // =====================================
        const title =
            document.createElement("h1");

        title.textContent =
            "STAGE2 CLEAR!";

        title.style.color =
            "rgb(180,180,180)";

        // =====================================
        // 説明文
        // =====================================
        const text =
            document.createElement("div");

        text.textContent =
            "契約書を獲得しました！！";

        text.style.color =
            "gray";

        // =====================================
        // 契約書画像
        // =====================================
        const imageView =
            document.createElement("img");

        try {

            imageView.src =
                "picture/keiyakusho.png";

        } catch (e) {

            console.error(
                "契約書画像の読み込みに失敗しました",
                e
            );
        }

        // =====================================
        // 横並びコンテナ(HBox)
        // =====================================
        const textAndImage =
            document.createElement("div");

        textAndImage.style.display = "flex";
        textAndImage.style.flexDirection = "row";
        textAndImage.style.alignItems = "center";
        textAndImage.style.justifyContent = "center";
        textAndImage.style.gap = "10px";

        textAndImage.appendChild(imageView);
        textAndImage.appendChild(text);

        // =====================================
        // スコア表示
        // =====================================
        const scoreLabel =
            document.createElement("div");

        scoreLabel.textContent =
            `SCORE: ${this.score}`;

        scoreLabel.style.color =
            "gray";

        // =====================================
        // 次のステージへボタン
        // =====================================
        const next =
            document.createElement("button");

        next.textContent =
            "次のステージへ";

        next.classList.add(
            "game-button2"
        );

        next.addEventListener(
            "click",
            () => {

                if (this.clickSound) {

                    this.clickSound.pause();
                    this.clickSound.currentTime = 0;

                    this.clickSound.play();
                }

                // 0.5秒待つ
                this.pauseTimeout =
                    setTimeout(() => {

                        this.cleanup();

                        // Story3へ遷移
                        GameController.switchStory3();

                    }, 500);
            }
        );

        // =====================================
        // タイトルへ戻るボタン
        // =====================================
        const backButton =
            document.createElement("button");

        backButton.textContent =
            "タイトルへ";

        backButton.classList.add(
            "game-button2"
        );

        backButton.addEventListener(
            "click",
            () => {

                if (this.cancelSound) {

                    this.cancelSound.pause();
                    this.cancelSound.currentTime = 0;

                    this.cancelSound.play();
                }

                this.pauseTimeout =
                    setTimeout(() => {

                        this.cleanup();

                        GameController.switchStart();

                    }, 500);
            }
        );

        // =====================================
        // VBox相当
        // =====================================
        const buttonBox =
            document.createElement("div");

        buttonBox.style.display = "flex";
        buttonBox.style.flexDirection = "column";
        buttonBox.style.alignItems = "center";
        buttonBox.style.justifyContent = "center";
        buttonBox.style.gap = "20px";

        buttonBox.appendChild(title);
        buttonBox.appendChild(textAndImage);
        buttonBox.appendChild(scoreLabel);
        buttonBox.appendChild(next);
        buttonBox.appendChild(backButton);

        root.appendChild(buttonBox);

        // =====================================
        // JavaFX Bindings の代替
        // レスポンシブ処理
        // =====================================
        const updateResponsive = () => {

            const width =
                window.innerWidth;

            const height =
                window.innerHeight;

            // タイトル
            title.style.fontSize =
                `${width * 0.06}px`;

            title.style.fontWeight =
                "bold";

            // 説明文
            text.style.fontSize =
                `${width * 0.015}px`;

            // スコア
            scoreLabel.style.fontSize =
                `${width * 0.02}px`;

            scoreLabel.style.fontWeight =
                "bold";

            // 画像サイズ
            imageView.style.width =
                `${width * 0.15}px`;

            imageView.style.height =
                `${height * 0.18}px`;

            imageView.style.objectFit =
                "contain";

            // 次へボタン
            next.style.width =
                `${width * 0.17}px`;

            next.style.height =
                `${height * 0.09}px`;

            // 戻るボタン
            backButton.style.width =
                `${width * 0.17}px`;

            backButton.style.height =
                `${height * 0.09}px`;

            // ボタン文字
            next.style.fontSize =
                `${width * 0.013}px`;

            backButton.style.fontSize =
                `${width * 0.013}px`;
        };

        // 初回実行
        updateResponsive();

        // リサイズ時再計算
        window.addEventListener(
            "resize",
            updateResponsive
        );

        // =====================================
        // 画面表示
        // =====================================
        document.body.innerHTML = "";

        document.body.appendChild(root);

        return root;
    }
}