export class OrientationWarning {
  static #isInitialized = false;

  /**
   * 画面の向きを監視し、横向き時にモデルの自動ポーズを行う
   * @param {Object} [model] 停止・再開を制御したいゲームモデル
   * @param {HTMLElement} [pauseLayer] ポーズ時に連動させたいUIレイヤー要素
   */
  static init(model = null, pauseLayer = null) {
    let warningDiv = document.getElementById("orientation-warning");
    if (!warningDiv) {
      warningDiv = document.createElement("div");
      warningDiv.id = "orientation-warning";
      warningDiv.innerHTML = `
        <h2>縦画面専用のゲームです</h2>
        <p>スマホを縦向きにしてプレイしてください。</p>
      `;
      document.body.prepend(warningDiv);
    }

    // --- ここから追加：向きを判定して表示を切り替える処理 ---
    const checkOrientation = () => {
      // スマホかつ横向き（landscape）の場合に表示
      // 画面の幅が高さより大きい、または matchMedia を使う方法
      const isLandscape = window.matchMedia("(orientation: landscape)").matches;
      const isMobile = window.innerWidth <= 900; // スマホ判定の閾値（必要に応じて調整）

      if (isLandscape && isMobile) {
        warningDiv.style.display = "flex";
        // 横向きになったらゲームを強制的にポーズする
        if (model && typeof model.pause === "function" && !model.isPaused()) {
          model.pause();
          if (pauseLayer) pauseLayer.classList.add("visible");
        }
      } else {
        warningDiv.style.display = "none";
      }
    };

    // 初期化時と、画面の向き・サイズが変わったときにチェック
    checkOrientation();
    window.addEventListener("resize", checkOrientation);
    window.addEventListener("orientationchange", checkOrientation);
  }
}