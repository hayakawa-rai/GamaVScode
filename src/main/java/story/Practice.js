import { GameController } from "../control/GameController.js";
import { Bgm } from "../start/Bgm.js";
import { HighScoreManager } from "../common/HighScoreManager.js"; // ★追加

document.addEventListener("DOMContentLoaded", () => {
    // ==================================================
    // 1. DOM要素の取得
    // ==================================================
    const stage1Btn = document.getElementById("stage1-btn");
    const stage2Btn = document.getElementById("stage2-btn");
    const stage3Btn = document.getElementById("stage3-btn");
    const backBtn = document.getElementById("back-btn");
    const scoreInfo = document.getElementById("score-info");
    const tooltip = document.getElementById("highscore-tooltip");

    // ==================================================
    // 1.5 ハイスコア表示を実データで更新 ★追加
    // ==================================================
    function renderHighScores() {
        if (!tooltip) return;
        const s1 = HighScoreManager.loadHighScore(1);
        const s2 = HighScoreManager.loadHighScore(2);
        const s3 = HighScoreManager.loadHighScore(3);

        tooltip.innerHTML = `
            ★ HIGH SCORE ★
            <br><br>
            STAGE1 : ${s1}
            <br><br>
            STAGE2 : ${s2}
            <br><br>
            STAGE3 : ${s3}
        `;
    }
    renderHighScores();

    // ==================================================
    // 2. オーディオの生成と初期設定
    // ==================================================
    const clickSound = new Audio("../../resources/music/select.mp3");
    const practiceBgm = new Audio("../../resources/music/startbgm.mp3");

    clickSound.volume = 0.4;
    practiceBgm.volume = 0.3;
    practiceBgm.loop = true;

    // Bgm.unlockPlay を使って自動再生ブロックを回避する
    Bgm.unlockPlay(practiceBgm);

    // ==================================================
    // 3. BGM停止関数
    // ==================================================
    function cleanup() {
        practiceBgm.pause();
        practiceBgm.currentTime = 0;
    }

    /**
     * 変更点: 引数をURL文字列ではなく、実行する関数（callback）に変更
     * @param {Function} action - 遷移時に実行したい GameController のメソッド
     */
    function transitionTo(action) {
        clickSound.pause();
        clickSound.currentTime = 0;
        Bgm.unlockPlay(clickSound);
    
        setTimeout(() => {
            cleanup();
            action(); // ここで渡された GameController のメソッドを実行する
        }, 500);
    }

    // ==================================================
    // 4. 各ボタンのイベント登録
    // ==================================================
    if (stage1Btn) {
        stage1Btn.addEventListener("click", () => {
            transitionTo(() => GameController.switchToPracticeGame1());
        });
    }
    if (stage2Btn) {
        stage2Btn.addEventListener("click", () => {
            transitionTo(() => GameController.switchToPracticeGame2());
        });
    }
    if (stage3Btn) {
        stage3Btn.addEventListener("click", () => {
            transitionTo(() => GameController.switchToPracticeGame3());
        });
    }
    if (backBtn) {
        backBtn.addEventListener("click", () => {
            transitionTo(() => GameController.switchStart());
        });
    }

    // トロフィー（ハイスコア表示ボタン）
    if (scoreInfo && tooltip) {
        scoreInfo.addEventListener("click", () => {
            renderHighScores(); // ★開くたびに最新の値を反映
            if (tooltip.style.display === "block") {
                tooltip.style.display = "none";
            } else {
                tooltip.style.display = "block";
            }
        });
    }
});