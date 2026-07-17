/**
 * STAGE1 CLEAR画面
 */

import { GameController } from "../control/GameController.js";

import { SoundManager } from "../start/SoundManager.js";

export class StageClear1 {
  /**
   * 初期化
   *
   * @param {number} score スコア
   */
  static create(score) {
    console.log("StageClear1 create");

    // ==========================
    // CLEAR音
    // ==========================

    setTimeout(() => {
      const clearSound = new Audio("../../resources/music/yay.mp3");

      clearSound.volume = 0.5;

      clearSound.play().catch((error) => {
        console.error("CLEAR音再生失敗", error);
      });
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
    // ==========================
    nextBtn.addEventListener("click", () => {
      console.log("次のステージボタン押下");

      SoundManager.play(SoundManager.SELECT);

      setTimeout(() => {
        console.log("Story2へ遷移");

        GameController.switchToStory2();
      }, 500);
    });

    // ==========================
    // タイトルへ
    // ==========================
    titleBtn.addEventListener("click", () => {
      console.log("タイトルボタン押下");

      SoundManager.play(SoundManager.SELECT);

      setTimeout(() => {
        console.log("タイトルへ遷移");

        GameController.switchStart();
      }, 500);
    });
  }
}
