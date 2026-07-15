/**
 * GameOver画面
 */
export class GameOver {

    /**
     * ゲームオーバー画面初期化
     *
     * @param {Function} retryAction リトライ処理
     * @param {number} score スコア
     * @param {boolean} isNewRecord 新記録フラグ
     */
    static create(
        retryAction,
        score,
        isNewRecord
    ) {

        // ===================================
        // スコア表示
        // ===================================
        const scoreLabel =
            document.getElementById(
                "score-label"
            );

        scoreLabel.textContent =
            `SCORE : ${score}`;

        // ===================================
        // GAME OVER 効果音
        // ===================================
        SoundManager.play(
            SoundManager.GAMEOVER
        );

        // ===================================
        // 要素取得
        // ===================================
        const newRecordLabel =
            document.getElementById(
                "new-record-label"
            );

        const retryBtn =
            document.getElementById(
                "retry-btn"
            );

        const titleBtn =
            document.getElementById(
                "title-btn"
            );

        // ===================================
        // NEW RECORD 表示
        // ===================================
        if (isNewRecord) {

            newRecordLabel.style.display =
                "block";

        }

        // ===================================
        // リトライ
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

            }
        );

        // ===================================
        // タイトルへ戻る
        // ===================================
        titleBtn.addEventListener(
            "click",
            () => {

                SoundManager.play(
                    SoundManager.SELECT
                );

                setTimeout(() => {

                    GameController.switchStart();

                }, 500);

            }
        );
    }
}