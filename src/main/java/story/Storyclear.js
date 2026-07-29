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

  // ==================================================
  // 4. タップ後にエンドロール開始
  // ==================================================

  const tapStart = document.getElementById("tap-start");

  let started = false;

  function startEnding() {
    if (started) return;
    started = true;

    tapStart.remove();

    // STORY CLEAR 効果音
    clearSound.play().catch(console.error);

    // 4秒間 STORY CLEAR!! を表示
    setTimeout(() => {
      storyClearText.classList.add("hidden");

      // BGM開始
      endingBgm.play().catch(console.error);

      // スクロール距離計算
      const scrollDistance = rollContainer.offsetHeight + window.innerHeight;

      rollContainer.style.setProperty(
        "--scroll-distance",
        `-${scrollDistance}px`,
      );

      // スタッフロール開始
      rollContainer.classList.add("start-roll");

      // スタッフロール終了
      setTimeout(() => {
        rollContainer.classList.add("hidden");

        thankBox.classList.remove("hidden");
        companyBox.classList.remove("hidden");

        // 2秒後にタイトルボタン表示
        setTimeout(() => {
          titleBtn.classList.remove("hidden");
        }, 2000);
      }, 23000);
    }, 4000);
  }

  tapStart.addEventListener("click", startEnding);
  tapStart.addEventListener("touchstart", startEnding);

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
