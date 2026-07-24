/**
 * ポーズ画面のヘルプHTMLを読み込み、
 * pause-help-container に挿入する
 */
export async function loadPauseHelp() {
  // 共通化したヘルプ画面HTMLを取得
  const response = await fetch("../common/PauseHelp/PauseHelp.html");

  // HTML文字列として読み込み
  const html = await response.text();

  // 指定コンテナへ挿入
  document.getElementById("pause-help-container").innerHTML = html;
}
