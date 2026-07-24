export class OrientationWarning {
  static #model = null;
  static #pauseLayer = null;
  static #isInitialized = false;

  static init(model = null, pauseLayer = null) {
    if (model) OrientationWarning.#model = model;
    if (pauseLayer) OrientationWarning.#pauseLayer = pauseLayer;

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
      const isLandscape = window.matchMedia("(orientation: landscape)").matches;
      const isMobile = window.innerWidth <= 900;

      const currentModel = OrientationWarning.#model;
      const currentLayer = OrientationWarning.#pauseLayer;

      if (isLandscape && isMobile) {
        warningDiv.style.display = "flex";

        if (currentModel && typeof currentModel.isPaused === "function" && !currentModel.isPaused()) {
          // pause()があれば優先、無ければtogglePause()にフォールバック
          if (typeof currentModel.pause === "function") {
            currentModel.pause();
          } else if (typeof currentModel.togglePause === "function") {
            currentModel.togglePause();
          }
          if (currentLayer) currentLayer.classList.add("visible");
        }
      } else {
        warningDiv.style.display = "none";
      }
    };

    // 外から再チェックできるように保持しておく
    OrientationWarning.#recheck = checkOrientation;

    if (!OrientationWarning.#isInitialized) {
      OrientationWarning.#isInitialized = true;
      window.addEventListener("resize", checkOrientation);
      window.addEventListener("orientationchange", checkOrientation);
    }

    checkOrientation();
  }

  static #recheck = null;

  // GameControllerのようにmodelとpauseLayerが別タイミングで揃う場合に使う
  static setPauseLayer(pauseLayer) {
    OrientationWarning.#pauseLayer = pauseLayer;
    if (OrientationWarning.#recheck) OrientationWarning.#recheck();
  }
}