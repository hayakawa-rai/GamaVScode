/**
 * GameOver画面
 * JavaFX版 Gameover.java を JavaScript化したもの
 */
export class GameOver {

    /***     * ゲームオーバー画面を初期化
     *
     **@param {Function} retryAction
    ** @param {number} score
     * @param {boolean} isNewRecord
     */
  static create(retryAction, score, isNewRecord) {

        // ===================================
        // 効果音再生（ゲームオーバー）
        // ===================================
        SoundManager.play(SoundMa*ager.GAMEOVER);

        // ====================================
        // HTML要素取得
        // ====================================
        const gameText =
            document.getElementById("game-text");
        const overText =
            document.getElementById("over-t*xt");

        const scoreLabel =
            document.getElementById("score-label");

        const newRecordLabel =
            document.getElementById("new-record-label");
        const icon =
            document.getElementById("character-*con");

        const retryBtn =
            document.getElementById("retry-btn");

        const titleBtn =
            document.getElementById("title-btn");

        const contentLayout =
            document.getElementById("content-layout");
        // ====================================
        // スコア表示
        // ====================================
        scoreLabel.textContent =
            `SCORE : ${score}`;

        // ====================================
        // NEW RECORD表示
        // ====================================
        if(isNewRecord) {
            newRecordLabel.style.display =
               "block";
        }

        // ====================================
        // リトライボタン
        // ===================================
        retryBtn.addEventListener(
            "click",
            () => {

                SoundManager.play(
                    SoundManager.RETRY
                );

              setTimeout(() => {

                   if (retryAction) {
                        retryAction();
                    }

               }, 500);
            });

        // ====================================
        // タイトルへ戻る
        // ====================================
        titleBtn.addEventListener(
           "click",
            () => {

               SoundManager.play(
                   SoundManager.SELECT);

               setTimeout(() => {

                   GameController.switchStart();

                }, 500);
           }
        );

        // ====================================
        // レスポンシブ対応
        // ====================================
       function updateLayout() {

           const width =
               window.innerWidth;

           if (width < 600) {

              // スマホ表示

                gameText.style.fontSize =
                   "42px";

                overText.style.fontSize =
                   "42px";

                scoreLabel.style.fontSize =
                  "24px";

                newRecordLabel.style.fontSize =
                   "28px";

               newRecordLabel.style.color =
                   "gold";

               icon.style.width =
                   "180px";

               icon.style.height =
                   "230px";

                retryBtn.style.width =
                   "260px";

                retryBtn.style.height =
                   "55px";

                titleBtn.style.width =
                   "260px";

                titleBtn.style.height =
                   "55px";

                contentLayout.style.transform =
                   "translateY(-30px)";

               contentLayout.style.gap =
                    "15px";
            } else {

               // PC表示

                gameText.style.fontSize =
                    "70px";

                overText.style.fontSize =
                    "70px";

                scoreLabel.style.fontSize =
                    "36px";

                newRecordLabel.style.fontSize =
                    "40px";

                newRecordLabel.style.color =
                    "gold";

                icon.style.width =
                    "320px";

                icon.style.height =
                    "416px";

                retryBtn.style.width =
                    "320px";

                retryBtn.style.height =
                    "70px";

                titleBtn.style.width =
                    "320px";

                titleBtn.style.height =
                    "70px";

                contentLayout.style.transform =
                    "translateY(0)";

                contentLayout.style.gap =
                    "25px";
            }
        }

        // 初回実行
        updateLayout();

        // リサイズ時
        window.addEventListener(
            "resize",
            updateLayout
        );
    }
}