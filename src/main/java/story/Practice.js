// ==================================================// STAGE1// ==================================================
document.getElementById("stage1-btn").addEventListener("click", () => {  cleanup(); // BGM停止
  window.location.href = "practiceGame1.html";});
// ==================================================// STAGE2// ==================================================
document.getElementById("stage2-btn").addEventListener("click", () => {  cleanup();
  window.location.href = "practiceGame2.html";});
// ==================================================// STAGE3// ==================================================
document.getElementById("stage3-btn").addEventListener("click", () => {  cleanup();
  window.location.href = "practiceGame3.html";});
// ==================================================// タイトルへ戻る// ==================================================
document.getElementById("back-btn").addEventListener("click", () => {  cleanup();
  window.location.href = "start.html";});
// ==================================================// トロフィー（ハイスコア表示ボタン）// ==================================================
// トロフィーアイコンを取得const scoreInfo = document.getElementById("score-info");
// ハイスコア表示用のパネルを取得const tooltip = document.getElementById("highscore-tooltip");
// トロフィーがクリックされたときscoreInfo.addEventListener("click", () => {  // 既に表示中なら非表示にする  if (tooltip.style.display === "block") {    tooltip.style.display = "none";  } else {    // 非表示中なら表示する    tooltip.style.display = "block";  }});
// ==================================================// BGM// ==================================================
const practiceBgm = new Audio("../../resources/music/startbgm.mp3");
practiceBgm.volume = 0.1;practiceBgm.loop = true;
practiceBgm.play().catch(() => {});
// ==================================================// BGM停止// ==================================================
function cleanup() {  practiceBgm.pause();
  practiceBgm.currentTime = 0;}　