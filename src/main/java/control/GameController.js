import { Bgm } from "../start/Bgm.js";
import { HighScoreManager } from "../common/HighScoreManager.js";
import { OrientationWarning } from "../common/OrientationWarning.js"; 
/**
 * ゲーム全体を管理・制御するコントローラークラス (GameController)
 * Main1〜3 / PracticeMain1〜3 共通で使用する
 */
export class GameController {
  static #touchStart = [0, 0];
  static #FLICK_THRESHOLD = 30.0;
  static #newRecord = false;

  static isNewRecord() {
    return GameController.#newRecord;
  }

  /**
   * @param {Object} model
   * @param {Object} view
   * @param {HTMLCanvasElement} canvas
   * @param {number} stageNumber
   * @param {boolean} isPractice
   */
  constructor(model, view, canvas, stageNumber, isPractice) {
    this.model = model;
    this.view = view;
    this.canvas = canvas;
    this.gc = canvas.getContext("2d");
    this.stageNumber = stageNumber;
    this.isPractice = isPractice;

    this.timerId = null;
    this.pauseLayer = null;
    this.isTransitioning = false;
    this.bgmStarted = false; // 主人公が動くまでBGMを再生しない

    this.attachInput();
    GameController.applyMobileControls(this.model, () => this.startBgmOnce());
    
    OrientationWarning.init(this.model);

    this.startLoop();
  }

  /**
   * スマホ・画面操作用：スワイプ（フリック）で移動方向を制御
   * @param {Object} model
   * @param {Function} onMove スワイプ操作が発生した瞬間に呼ばれるコールバック(BGM開始トリガー用)
   */
  static applyMobileControls(model, onMove) {
    if (!model) return;

    const sendDirection = (dir) => {
      if (typeof model.isPaused === "function" && !model.isPaused()) {
        if (typeof model.setNextDirection === "function") {
          if (typeof onMove === "function") onMove();
          model.setNextDirection(dir);
        }
      }
    };

    // --- マウス操作用 ---
    window.addEventListener("mousedown", (e) => {
      GameController.#touchStart[0] = e.clientX;
      GameController.#touchStart[1] = e.clientY;
    });

    window.addEventListener("mouseup", (e) => {
      const deltaX = e.clientX - GameController.#touchStart[0];
      const deltaY = e.clientY - GameController.#touchStart[1];
      const absX = Math.abs(deltaX);
      const absY = Math.abs(deltaY);

      if (
        absX > GameController.#FLICK_THRESHOLD ||
        absY > GameController.#FLICK_THRESHOLD
      ) {
        if (absX > absY) {
          sendDirection(deltaX > 0 ? "RIGHT" : "LEFT");
        } else {
          sendDirection(deltaY > 0 ? "DOWN" : "UP");
        }
      }
    });
    // --- タッチ操作用（スマホ対応） ---
    window.addEventListener(
      "touchstart",
      (e) => {
        if (e.touches && e.touches.length > 0) {
          GameController.#touchStart[0] = e.touches[0].clientX;
          GameController.#touchStart[1] = e.touches[0].clientY;
        }
      },
      { passive: true },
    );

    window.addEventListener(
      "touchend",
      (e) => {
        if (e.changedTouches && e.changedTouches.length > 0) {
          const deltaX =
            e.changedTouches[0].clientX - GameController.#touchStart[0];
          const deltaY =
            e.changedTouches[0].clientY - GameController.#touchStart[1];
          const absX = Math.abs(deltaX);
          const absY = Math.abs(deltaY);

          if (
            absX > GameController.#FLICK_THRESHOLD ||
            absY > GameController.#FLICK_THRESHOLD
          ) {
            if (absX > absY) {
              sendDirection(deltaX > 0 ? "RIGHT" : "LEFT");
            } else {
              sendDirection(deltaY > 0 ? "DOWN" : "UP");
            }
          }
        }
      },
      { passive: true },
    );
  }

  /**
   * キーボード入力の受付（Escキーでポーズ、Cキーで強制クリア、WASD/矢印で移動）
   */
  attachInput() {
    window.addEventListener("keydown", (e) => {
      const key = e.key.toUpperCase();

      // 1. ポーズ処理 (Escキー)
      if (key === "ESCAPE" || key === "ESC") {
        this.togglePauseByButton();
        return;
      }

      // 2. デバッグ用：強制ステージクリア (Cキー)
      if (key === "C") {
        if (typeof this.model.forceStageClear === "function") {
          this.model.forceStageClear();
        } else {
          console.log("このモデルには forceStageClear メソッドがありません");
        }
        return;
      }

      // ポーズ中は移動キーを無視
      if (this.model.isPaused && this.model.isPaused()) return;

      // 3. 移動キー受付 (W, A, S, D / 矢印キー)
      if (typeof this.model.setNextDirection === "function") {
        const isMoveKey =
          key === "W" ||
          e.key === "ArrowUp" ||
          key === "S" ||
          e.key === "ArrowDown" ||
          key === "A" ||
          e.key === "ArrowLeft" ||
          key === "D" ||
          e.key === "ArrowRight";

        if (isMoveKey) {
          this.startBgmOnce(); // 移動キーが押された瞬間にBGM開始

          if (key === "W" || e.key === "ArrowUp")
            this.model.setNextDirection("UP");
          if (key === "S" || e.key === "ArrowDown")
            this.model.setNextDirection("DOWN");
          if (key === "A" || e.key === "ArrowLeft")
            this.model.setNextDirection("LEFT");
          if (key === "D" || e.key === "ArrowRight")
            this.model.setNextDirection("RIGHT");
        }
      }
    });
  }

  /**
   * メインのゲームループ (AnimationTimerの代替)
   * Canvasのリサイズは #game-root の実寸(clientWidth/clientHeight)を基準にする
   */
  startLoop() {
    const container = document.getElementById("game-root");
    const resizeCanvas = () => {
      this.canvas.width = container ? container.clientWidth : window.innerWidth;
      this.canvas.height = container
        ? container.clientHeight
        : window.innerHeight;
    };
    window.addEventListener("resize", resizeCanvas);
    resizeCanvas();

    const loop = () => {
      try {
        const isPaused = this.model.isPaused ? this.model.isPaused() : false;

        if (!isPaused) {
          if (typeof this.model.update === "function") {
            this.model.update();
          }

          // ゲームオーバー判定
          if (this.model.isGameOver && this.model.isGameOver()) {
            this.stop();
            if (this.isTransitioning) return;
            this.isTransitioning = true;

            if (typeof Bgm.stopBGM === "function") Bgm.stopBGM();

            let finalScore = 0;
            if (this.model.getSyujinkou) {
              const syujinkou = this.model.getSyujinkou();
              if (syujinkou && typeof syujinkou.getScore === "function") {
                finalScore = syujinkou.getScore();
              }
            }

            setTimeout(() => {
              GameController.switchToGameover(
                this.stageNumber,
                this.isPractice,
                finalScore,
              );
            }, 0);
            return;
          }

          // ステージクリア判定
          if (this.model.isCleared && this.model.isCleared()) {
            if (this.isPractice) {
              // 練習モード：クリアの概念はなく、エサを復活させて延々と続行するだけ
              if (typeof this.model.respawnDots === "function") {
                this.model.respawnDots();
              }
            } else {
              // 本番モード：ループを止めてクリア画面へ
              this.stop();
              if (this.isTransitioning) return;
              this.isTransitioning = true;

              if (typeof Bgm.stopBGM === "function") Bgm.stopBGM();
              console.log("🏁 ステージクリア！");

              let finalScore = 0;
              if (this.model.getSyujinkou) {
                const syujinkou = this.model.getSyujinkou();
                if (syujinkou && typeof syujinkou.getScore === "function") {
                  finalScore = syujinkou.getScore();
                }
              }

              // 本番モード：ステージクリア時のスコアでハイスコア判定
              GameController.#newRecord = HighScoreManager.updateHighScore(
                this.stageNumber,
                finalScore,
              );

              setTimeout(() => {
                GameController.switchToStageclear(this.stageNumber, finalScore);
              }, 0);
              return;
            }
          }
        }

        if (this.view && typeof this.view.draw === "function") {
          this.view.draw(this.gc, this.canvas.width, this.canvas.height);
        }

        this.timerId = requestAnimationFrame(loop);
      } catch (ex) {
        console.error("ループ内エラー:", ex);
        this.stop();
      }
    };

    this.timerId = requestAnimationFrame(loop);
  }

  /**
   * タイトル(本番)/練習モード選択画面(Practice)へ強制的に戻る
   */
  forceBackToTitle() {
    this.stop();
    if (typeof Bgm.stopBGM === "function") Bgm.stopBGM();
    if (this.isPractice) {
      GameController.switchToPractice();
    } else {
      GameController.switchStart();
    }
  }

  startBgmOnce() {
    if (!this.bgmStarted) {
      this.bgmStarted = true;
      this.playStageBgm(this.stageNumber);
    }
  }

  playStageBgm(stageNumber) {
    if (typeof Bgm.playStageBGM === "function") {
      Bgm.playStageBGM(stageNumber);
    }
  }

  setPauseLayer(pauseLayerElement) {
    this.pauseLayer = pauseLayerElement;
     OrientationWarning.setPauseLayer(pauseLayerElement); // ここで再チェック
  }

  /**
   * ポーズのON/OFFを切り替える（メニューボタン・Escキー共通の窓口）
   */
  togglePauseByButton() {
    if (typeof this.model.togglePause === "function") {
      this.model.togglePause();
    }

    if (this.pauseLayer) {
      const isPaused = this.model.isPaused ? this.model.isPaused() : false;
      this.pauseLayer.classList.toggle("visible", isPaused);
    }
  }

  stop() {
    if (this.timerId) {
      cancelAnimationFrame(this.timerId);
      this.timerId = null;
    }
  }

  // ==========================================
  // 画面遷移メソッド群
  // ==========================================
  static switchStart() {
    window.location.href = "../start/Start.html";
  }
  static switchToHelp() {
    window.location.href = "Help.html";
  }
  static switchToPractice() {
    window.location.href = "../story/Practice.html";
  }
  static switchToStory() {
    window.location.href = "../story/Story1.html";
  }
  static switchToStory2() {
    window.location.href = "../story/Story2.html";
  }
  static switchToStory3() {
    window.location.href = "../story/Story3.html";
  }
  static switchToStory4() {
    window.location.href = "../story/Story4.html";
  }
  static switchStoryClear() {
    window.location.href = "../story/Storyclear.html";
  }
  static switchToGame1() {
    window.location.href = "../test1/Main1.html";
  }
  static switchToGame2() {
    window.location.href = "../test2/Main2.html";
  }
  static switchToGame3() {
    window.location.href = "../test3/Main3.html";
  }
  static switchToPracticeGame1() {
    window.location.href = "../test1/PracticeMain1.html";
  }
  static switchToPracticeGame2() {
    window.location.href = "../test2/PracticeMain2.html";
  }
  static switchToPracticeGame3() {
    window.location.href = "../test3/PracticeMain3.html";
  }
  static switchToStageclear(stageNum, score) {
    window.location.href = `../story/Stageclear${stageNum}.html?score=${score}`;
  }
  static switchToGameover(stageNum, isPractice, score) {
    window.location.href = `../story/Gameover.html?stage=${stageNum}&practice=${isPractice}&score=${score}`;
  }
}