import { GameController } from "../../control/GameController.js";
import { SoundManager } from "../../sound/SoundManager.js";

//==================================================
// STAGE1
// ==================================================
document.getElementById("stage1-btn").addEventListener("click", () => {
  cleanup(); // BGM停止
  window.location.href = "../test1/PracticeMain1.html";
});

// ==================================================
// STAGE2
// ==================================================
document.getElementById("stage2-btn").addEventListener("click", () => {
  cleanup();
  window.location.href = "../test2/PracticeMain2.html";
});

// ==================================================
// STAGE3
// ==================================================
document.getElementById("stage3-btn").addEventListener("click", () => {
  cleanup();
  window.location.href = "../test3/PracticeMain3.html";
});

// ==================================================
// タイトルへ戻る
// ==================================================

document.getElementById("back-btn").addEventListener("click", () => {
  SoundManager.play(SoundManager.CANCEL);

  cleanup();

  setTimeout(() => {
    GameController.switchStart();
  }, 500);
});

// ==================================================
// トロフィー（ハイスコア表示ボタン）
// ==================================================

// トロフィーアイコンを取得
const scoreInfo = document.getElementById("score-info");
// ハイスコア表示用のパネルを取得
const tooltip = document.getElementById("highscore-tooltip");
// トロフィーがクリックされたとき
scoreInfo.addEventListener("click", () => {
  // 既に表示中なら非表示にする
  if (tooltip.style.display === "block") {
    tooltip.style.display = "none";
  } else {
    // 非表示中なら表示する
    tooltip.style.display = "block";
  }
});

// ==================================================
// BGM
// ==================================================
const practiceBgm = new Audio("../../resources/music/startbgm.mp3");
practiceBgm.volume = 0.1;
practiceBgm.loop = true;
practiceBgm.play().catch(() => {});

// ==================================================
// BGM停止
// ==================================================
function cleanup() {
  practiceBgm.pause();
  practiceBgm.currentTime = 0;
}
