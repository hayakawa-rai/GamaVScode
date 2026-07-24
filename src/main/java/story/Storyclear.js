import { GameController } from "../control/GameController.js";
import { OrientationWarning } from "../common/OrientationWarning.js";
import { Bgm } from "../start/Bgm.js";

document.addEventListener("DOMContentLoaded", () => {
  //横向き対応の初期化
    OrientationWarning.init();
  // ==================================================
  // 1. DOM要素の取得
  // ==================================================
  const storyClearText = document.getElementById("story-clear-text");
  const rollContainer = document.getElementById("roll-container");
  const creditsText = document.getElementById("credits-text");
  const thankBox = document.getElementById("thank-box");
  const titleBtn = document.getElementById("title-btn");
  const companyBox = document.getElementById("company-box");

  // ==================================================
  // 2. スタッフロールのテキスト設定（JavaFXより完全移植）
  // ==================================================
  creditsText.textContent =
    "━━━━━━━━━━━━━━━━━━\n\n" +
    "PROGRAMMER\n\n" +
    "N.Y\n" +
    "H.R\n" +
    "O.S\n" +
    "K.S\n" +
    "W.M\n" +
    "W.T\n" +
    "F.O\n" +
    "M.R\n\n" +
    "━━━━━━━━━━━━━━━━━━\n\n" +
    "COOPERATION\n\n" +
    "先輩社員\n\n" +
    "━━━━━━━━━━━━━━━━━━\n\n" +
    "SPECIAL THANKS\n\n" +
    "遊んでくださった皆様\n\n" +
    "━━━━━━━━━━━━━━━━━━\n\n" +
    "最後まで諦めず\n" +
    "会社を取り戻してくれて\n" +
    "ありがとうございました。\n\n" +
    "━━━━━━━━━━━━━━━━━━\n\n" +
    "平和を取り戻した会社で\n" +
    "今日もまた\n" +
    "新しい一日が始まります。\n\n" +
    "━━━━━━━━━━━━━━━━━━\n\n";

  // ==================================================
  // 3. オーディオ要素の生成と初期設定
  // ==================================================
  const clickSound = new Audio("../../resources/music/select.mp3");
  const clearSound = new Audio("../../resources/music/Storyclear_sound.mp3");
  const endingBgm = new Audio("../../resources/music/Storyclear_bgm2.mp3");

  endingBgm.volume = 0.5;
  endingBgm.loop = true;

  // 画面がロードされたら即座にクリアファンファーレを鳴らす
  clearSound
    .play()
    .catch((err) =>
      console.log(
        "ブラウザの制限：ユーザー操作後に音声が再生可能になります",
        err,
      ),
    );

  // ==================================================
  // 4. JavaFXベースの時間差演出タイムライン
  // ==================================================

  // 4秒間 「STORY CLEAR!!」 を見せたのち、スタッフロールを始動
  setTimeout(() => {
    storyClearText.classList.add("hidden"); // 中央の文字を消す
    endingBgm.play().catch((e) => console.log(e)); // BGMループ再生開始

    // 画面外（下）から画面外（上）に完璧に引き抜く距離を算出
    const scrollDistance = rollContainer.offsetHeight + window.innerHeight;

    // CSS変数（--scroll-distance）に計算結果を設定
    rollContainer.style.setProperty(
      "--scroll-distance",
      `-${scrollDistance}px`,
    );

    // CSSのクラスを付与してGPU加速の効いた滑らかな23秒アニメーションをトリガー
    rollContainer.classList.add("start-roll");

    // 23秒間のスタッフロールスクロールが終了した時の処理（23秒後）
    setTimeout(() => {
      rollContainer.classList.add("hidden"); // クレジットを消す
      thankBox.classList.remove("hidden"); // ロゴとTHANK YOUを表示
      companyBox.classList.remove("hidden"); // 右下のコピーライトを表示

      // そこからさらに2秒の余韻待機（PauseTransition）を経てボタンを出現させる
      setTimeout(() => {
        titleBtn.classList.remove("hidden");
      }, 2000);
    }, 23000);
  }, 4000);

  // ==================================================
  // 5. 「タイトルへ」ボタンクリック処理
  // ==================================================
  titleBtn.addEventListener("click", () => {
    clickSound.play();

    // BGMを止めてリセット
    endingBgm.pause();
    endingBgm.currentTime = 0;

    // タイトル画面のHTMLへ遷移
    GameController.switchStart();
  });
});
