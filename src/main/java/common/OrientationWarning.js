export class OrientationWarning {
  /**
   * 画面の向きを監視し、横向き時に警告表示とモデルの自動ポーズを行う
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

    const checkOrientation = () => {
      const isLandscape = window.innerWidth > window.innerHeight && window.innerWidth <= 900;

      if (isLandscape) {
        warningDiv.style.display = "flex";

        if (model && typeof model.isPaused === "function" && !model.isPaused()) {
          if (typeof model.togglePause === "function") {
            model.togglePause();
            if (pauseLayer) {
              pauseLayer.classList.add("visible");
            }
          }
        }
      } else {
        warningDiv.style.display = "none";
      }
    };

    window.addEventListener("resize", checkOrientation);
    window.addEventListener("orientationchange", checkOrientation);
    
    checkOrientation();
  }
}