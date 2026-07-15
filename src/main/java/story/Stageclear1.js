/**
 * STAGE1 CLEAR画面
 */
export class StageClear1 {

    /**
     * 初期化
     *
     * @param {number} score
     */
    static create(score) {

        // ==========================
        // CLEAR音
        // ==========================

        setTimeout(() => {

            const clearSound =
                new Audio("../../resources/music/yay.mp3"
                );

            clearSound.volume = 0.5;

            clearSound.play();

        }, 500);

        // ==========================
        // スコア表示
        // ==========================

        const scoreLabel =
            document.getElementById(
                "score-label"
            );

        scoreLabel.textContent =
            `SCORE : ${score}`;

        // ==========================
        // ボタン取得
        // ==========================

        const nextBtn =
            document.getElementById(
                "next-btn"
            );

        const titleBtn =
            document.getElementById(
                "title-btn"
            );

        // ==========================
        // 次のステージ
        // ==========================

        nextBtn.addEventListener(
            "click",
            () => {

                SoundManager.play(
                    SoundManager.SELECT
                );

                setTimeout(() => {

                    GameController
                        .switchStory2();

                }, 500);

            }
        );

        // ==========================
        // タイトルへ
        // ==========================

        titleBtn.addEventListener(
            "click",
            () => {

                SoundManager.play(
                    SoundManager.CANCEL
                );

                setTimeout(() => {

                    GameController
                        .switchStart();

                }, 500);

            }
        );
    }
}

/* テスト用 */
StageClear1.create(5000);