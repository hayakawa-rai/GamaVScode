// Pause効果音管理クラス
import { SoundManager } from "../../start/SoundManager.js";

/**
 * ポーズ画面のヘルプ機能を初期化する
 * ・ヘルプの表示 / 非表示
 * ・ページ送り / ページ戻し
 * ・インジケータ更新
 */
export function initPauseHelp() {
  // ヘルプ画面本体
  const helpPanel = document.getElementById("help-panel");

  // ヘルプを開く・閉じるボタン
  const openBtn = document.getElementById("how-to-play-btn");
  const backBtn = document.getElementById("back-btn");

  // ヘルプページ一覧
  const pages = document.querySelectorAll(".page-wrapper");

  // 下部のページインジケータ
  const dots = document.querySelectorAll(".indicator-dot");

  // ページ移動ボタン
  const prevBtn = document.getElementById("prev-btn");
  const nextBtn = document.getElementById("next-btn");

  // 現在表示中のページ番号
  let currentPage = 0;

  /**
   * 指定ページを表示する
   * @param {number} index 表示するページ番号
   */
  function updatePage(index) {
    // 範囲外でも循環できるようにする
    currentPage = (index + pages.length) % pages.length;

    // 対象ページのみ表示
    pages.forEach((p, i) => {
      p.style.display = i === currentPage ? "block" : "none";
    });

    // インジケータ更新
    dots.forEach((d, i) => {
      d.classList.toggle("active", i === currentPage);
    });
  }

  // 初期表示は1ページ目
  updatePage(0);

  // 前のページへ
  prevBtn.onclick = () => {
    SoundManager.play(SoundManager.SELECT);
    updatePage(currentPage - 1);
  };

  // 次のページへ
  nextBtn.onclick = () => {
    SoundManager.play(SoundManager.SELECT);
    updatePage(currentPage + 1);
  };

  // ヘルプを開く
  openBtn.onclick = () => {
    SoundManager.play(SoundManager.SELECT);

    helpPanel.classList.remove("hidden");

    // 開くたびに先頭ページへ戻す
    updatePage(0);
  };

  // ヘルプを閉じる
  backBtn.onclick = () => {
    SoundManager.play(SoundManager.SELECT);

    helpPanel.classList.add("hidden");
  };

  // 外部から利用できるよう返却
  return {
    updatePage,
  };
}
