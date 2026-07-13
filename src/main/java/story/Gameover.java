/**
 * GameOver画面
 * JavaFX版 Gameover.java を JavaScript化したもの
 */
class GameOver {

    /**
     * ゲームオーバー画面を生成
     * @param {Function} retryAction リトライ時の処理
     * @param {number} score スコア
     * @param {boolean} isNewRecord 新記録かどうか
     */
    static create(retryAction, score, isNewRecord) {

        // =====================================
        // 効果音再生（ゲームオーバー）
        // =====================================
        SoundManager.play(SoundManager.GAMEOVER);

        // =====================================
        // ルートコンテナ
        // =====================================
        const root = document.createElement("div");
        root.className = "gameover-root";

        // =====================================
        // 背景画像
        // =====================================
        const bg = document.createElement("img");
        bg.src = "picture/gameover.jpg";
        bg.className = "gameover-background";

        // =====================================
        // 白いオーバーレイ
        // =====================================
        const overlay = document.createElement("div");
        overlay.className = "white-overlay";

        // =====================================
        // メインコンテンツ
        // =====================================
        const contentLayout = document.createElement("div");
        contentLayout.className = "content-layout";

        // =====================================
        // GAME OVER タイトル
        // =====================================
        const gameText = document.createElement("h1");
        gameText.textContent = "GAME";

        const overText = document.createElement("h1");
        overText.textContent = "OVER";

        // =====================================
        // スコア表示
        // =====================================
        const scoreLabel = document.createElement("div");
        scoreLabel.textContent = `SCORE : ${score}`;

        // =====================================
        // 新記録表示
        // =====================================
        const newRecordLabel = document.createElement("div");
        newRecordLabel.textContent = "NEW RECORD!!";

        // =====================================
        // タイトル部分コンテナ
        // =====================================
        const titleBox = document.createElement("div");
        titleBox.className = "title-box";

        titleBox.appendChild(gameText);
        titleBox.appendChild(overText);
        titleBox.appendChild(scoreLabel);

        // 新記録なら表示
        if (isNewRecord) {
            titleBox.appendChild(newRecordLabel);
        }

        // =====================================
        // キャラクター画像（仙石さん）
        // =====================================
        const icon = document.createElement("img");
        icon.src = "picture/syujinkou(gameover).png";
        icon.className = "character-image";

        // =====================================
        // リトライボタン
        // =====================================
        const retryBtn = document.createElement("button");
        retryBtn.textContent = "リトライする";

        retryBtn.addEventListener("click", () => {

            // 効果音
            SoundManager.play(SoundManager.RETRY);

            // 500ms後にリトライ処理
            setTimeout(() => {
                if (retryAction) {
                    retryAction();
                }
            }, 500);

        });

        // =====================================
        // タイトルへ戻るボタン
        // =====================================
        const titleBtn = document.createElement("button");
        titleBtn.textContent = "タイトルへ";

        titleBtn.addEventListener("click", () => {

            // 効果音
            SoundManager.play(SoundManager.SELECT);

            // 500ms待機
            setTimeout(() => {
                GameController.switchStart();
            }, 500);

        });

        // =====================================
        // ボタンエリア
        // =====================================
        const buttonColumn = document.createElement("div");
        buttonColumn.className = "button-column";

        buttonColumn.appendChild(retryBtn);
        buttonColumn.appendChild(titleBtn);

        // =====================================
        // メインレイアウトに追加
        // =====================================
        contentLayout.appendChild(titleBox);
        contentLayout.appendChild(icon);
        contentLayout.appendChild(buttonColumn);

        // =====================================
        // レスポンシブ対応
        // JavaFXの widthProperty の代替
        // =====================================
        function updateLayout() {

            const width = window.innerWidth;

            if (width < 600) {

                // ==========================
                // スマホ表示
                // ==========================
                gameText.style.fontSize = "42px";
                overText.style.fontSize = "42px";

                scoreLabel.style.fontSize = "24px";

                newRecordLabel.style.fontSize = "28px";
                newRecordLabel.style.color = "gold";

                icon.style.width = "180px";
                icon.style.height = "230px";

                retryBtn.style.width = "260px";
                retryBtn.style.height = "55px";

                titleBtn.style.width = "260px";
                titleBtn.style.height = "55px";

                contentLayout.style.transform = "translateY(-30px)";
                contentLayout.style.gap = "15px";

            } else {

                // ==========================
                // PC表示
                // ==========================
                gameText.style.fontSize = "70px";
                overText.style.fontSize = "70px";

                scoreLabel.style.fontSize = "36px";

                newRecordLabel.style.fontSize = "40px";
                newRecordLabel.style.color = "gold";

                icon.style.width = "320px";
                icon.style.height = "416px";

                retryBtn.style.width = "320px";
                retryBtn.style.height = "70px";

                titleBtn.style.width = "320px";
                titleBtn.style.height = "70px";

                contentLayout.style.transform = "translateY(0)";
                contentLayout.style.gap = "25px";
            }

        }

        // 初回実行
        updateLayout();

        // ウィンドウサイズ変更時
        window.addEventListener("resize", updateLayout);

        // =====================================
        // 画面構築
        // =====================================
        root.appendChild(bg);
        root.appendChild(overlay);
        root.appendChild(contentLayout);

        // bodyへ追加
        document.body.innerHTML = "";
        document.body.appendChild(root);
    }
}