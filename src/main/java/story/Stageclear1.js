import { GameController } from "../control/GameController.js";
import { SoundManager } from "../start/SoundManager.js";
import { OrientationWarning } from "../common/OrientationWarning.js";
/**
 * STAGE1 CLEAR画面
 */
export class StageClear1 {
  /**
   * 初期化
   * @param {number} score スコア
   */
  static create(score) {
    console.log("StageClear1 create");

    // ==========================
    // CLEAR音
    // ==========================
    setTimeout(() => {
      SoundManager.play(SoundManager.CLEAR);
    }, 500);

    // ==========================
    // スコア表示
    // ==========================
    const scoreLabel = document.getElementById("score-label");

    if (scoreLabel) {
      scoreLabel.textContent = `SCORE : ${score}`;
    }

    // ==========================
    // ボタン取得
    // ==========================
    const nextBtn = document.getElementById("next-btn");
    const titleBtn = document.getElementById("title-btn");

    // ==========================
    // 次のステージ
    // Story2へ
    // ==========================
    nextBtn.addEventListener("click", () => {
      SoundManager.play(SoundManager.SELECT);

      setTimeout(() => {
        GameController.switchToStory2();
      }, 500);
    });

    // ==========================
    // タイトルへ
    // ==========================
    titleBtn.addEventListener("click", () => {
      SoundManager.play(SoundManager.SELECT);

      setTimeout(() => {
        GameController.switchStart();
      }, 500);
    });
  }
}

window.addEventListener("DOMContentLoaded", () => {
  //横向き対応の初期化
    OrientationWarning.init();
  const params = new URLSearchParams(window.location.search);

  const score = Number(params.get("score")) || 0;

  StageClear1.create(score);
});
