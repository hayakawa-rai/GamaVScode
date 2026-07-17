import { GameController } from "../../control/GameController.js";

import { SoundManager } from "../../sound/SoundManager.js";

/**
 * STAGE3 CLEAR画面
 */
export class StageClear3 {
  /**
   * 初期化
   *
   * @param {number} score スコア
   */
  static create(score) {
    console.log("StageClear3 create");

    // CLEAR音
    setTimeout(() => {
      const clearSound = new Audio("../../resources/music/yay.mp3");

      clearSound.volume = 0.5;

      clearSound.play().catch((error) => {
        console.error("CLEAR音再生失敗", error);
      });
    }, 500);

    // スコア表示
    const scoreLabel = document.getElementById("score-label");

    if (scoreLabel) {
      scoreLabel.textContent = `SCORE : ${score}`;
    }

    // ボタン取得
    const nextBtn = document.getElementById("next-btn");

    const titleBtn = document.getElementById("title-btn");

    // 次のステージ
    nextBtn.addEventListener("click", () => {
      SoundManager.play(SoundManager.SELECT);

      setTimeout(() => {
        GameController.switchToStory4();
      }, 500);
    });

    // タイトルへ
    titleBtn.addEventListener("click", () => {
      SoundManager.play(SoundManager.CANCEL);

      setTimeout(() => {
        GameController.switchStart();
      }, 500);
    });
  }
}

window.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);

  const score = Number(params.get("score")) || 0;

  StageClear3.create(score);
});
