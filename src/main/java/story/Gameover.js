import { SoundManager } from "../start/SoundManager.js";
import { GameController } from "../control/GameController.js";
import { HighScoreManager } from "../common/HighScoreManager.js";
import { OrientationWarning } from "../common/OrientationWarning.js";

export class GameOver {
  static init() {
    //横向き対応の初期化
    OrientationWarning.init();
    // URLパラメータの取得
    const params = new URLSearchParams(window.location.search);
    const stage = params.get("stage") || "1";
    const isPractice = params.get("practice") === "true";
    const score = Number(params.get("score")) || 0;

    // ハイスコア判定と保存
    const oldHighScore = HighScoreManager.loadHighScore(stage);
    const isNewRecord = score > oldHighScore;
    if (isNewRecord) {
        HighScoreManager.updateHighScore(stage, score);
    }

    // スコア表示
    const scoreLabel = document.getElementById("score-label");
    scoreLabel.textContent = `SCORE : ${score}`;

    // NEW RECORD 表示
    if (isNewRecord) {
      document.getElementById("new-record-label").style.display = "block";
    }

    // GAME OVER 効果音
    SoundManager.play(SoundManager.GAMEOVER);

    // リトライ
    const retryBtn = document.getElementById("retry-btn");
    retryBtn.addEventListener("click", () => {
      SoundManager.play(SoundManager.RETRY);
      setTimeout(() => {
        window.location.href = isPractice
            ? `../test${stage}/PracticeMain${stage}.html`
            : `../test${stage}/Main${stage}.html`;
      }, 500);
    });

    // タイトルへ戻る
    const titleBtn = document.getElementById("title-btn");
    titleBtn.addEventListener("click", () => {
      SoundManager.play(SoundManager.SELECT);
      setTimeout(() => {
        GameController.switchStart();
      }, 500);
    });
  }
}