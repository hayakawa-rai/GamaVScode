import { GameController } from "../control/GameController.js";
import { OrientationWarning } from "../common/OrientationWarning.js";

// 画面遷移の多重実行防止フラグ
let navigated = false;

/**
 * 現在横向きかどうかを判定するヘルパー
 */
function isLandscapeMode() {
  const warningDiv = document.getElementById("orientation-warning");
  return (warningDiv && window.getComputedStyle(warningDiv).display !== "none") ||
         (window.innerWidth > window.innerHeight && window.innerWidth <= 900);
}

/**
 * スタート画面へ遷移する
 * タップ連打や複数入力による二重遷移を防止する
 */
function goToStart() {
  if (isLandscapeMode()) {
    // 横向きのときはタッチされてもイベントが消えてしまわないよう再登録してスルーする
    attachListeners();
    return;
  }

  if (navigated) return;

  navigated = true;

  // 連打対策として一時的に画面全体の操作を無効化
  document.body.style.pointerEvents = "none";

  setTimeout(() => {
    try {
      GameController.switchStart();
    } catch (err) {
      // エラー時は再操作できるよう状態を復元
      navigated = false;
      document.body.style.pointerEvents = "auto";
      attachListeners();
    }
  }, 50);
}

/**
 * タップ・クリック・キー入力のイベントを登録する
 */
function attachListeners() {
  document.addEventListener("pointerdown", goToStart, { once: true });
  document.addEventListener("keydown", goToStart, { once: true });
}

/**
 * 初回表示時のイベント登録
 */
document.addEventListener("DOMContentLoaded", () => {
  // 起動時に横向き警告機能を初期化
  OrientationWarning.init();

  attachListeners();
});

/**
 * ブラウザの戻る操作などで
 * ページがキャッシュから復元された場合の再初期化
 */
window.addEventListener("pageshow", (event) => {
  if (event.persisted) {
    // 遷移フラグをリセット
    navigated = false;

    // 操作無効状態を解除
    document.body.style.pointerEvents = "auto";

    // 警告機能の再初期化
    OrientationWarning.init();

    // once:trueで削除されたイベントを再登録
    attachListeners();
  }
});