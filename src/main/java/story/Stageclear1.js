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
    // ==========================
    nextBtn.addEventListener("click", () => {
      console.log("次のステージボタン押下");a

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
window.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);

  const score = Number(params.get("score")) || 0;

  StageClear1.create(score);
});
