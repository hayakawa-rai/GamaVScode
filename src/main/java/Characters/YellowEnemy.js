import { Enemy } from "./Enemy.js";
import { GameConfig } from "../common/GameConfig.js";
import { EnemyState } from "./EnemyState.js";
import { Direction } from "./Direction.js";

// ==================================================
// YellowEnemy（黄）
//
// プレイヤーの進行方向を予測して追跡する敵
// プレイヤーの4マス先を目標地点として移動する
// ==================================================

export class YellowEnemy extends Enemy {
    
  // 初期位置（エネミーハウス内）
  static START_COL = 13;
  static START_ROW = 14;

  // プレイヤーの進行方向の4マス先を狙う
  static PREDICT_TILES = 4;

  // SCATTER状態時の縄張り座標（左上）
  static TERRITORY_COL = 3;
  static TERRITORY_ROW = 3;

  // ==================================================
  // コンストラクタ
  // ==================================================
  constructor(mapData) {

    // マスの中心座標を初期位置としてEnemyへ渡す
    super(
      YellowEnemy.START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2,
      YellowEnemy.START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2,
      2.5,
    );

    this.mapData = mapData;

    // 出撃タイマー用
    this.startTime = 0;

    // タイマー開始フラグ
    this.timerStarted = false;

    // 出撃済みか
    this.released = false;

    // FEVER画像を読み込む
    this.loadFeverImage();

    // DEAD画像を読み込む
    this.loadDeadImage();

    // 現在のステージ番号によって画像を切り替える
    let imagePath = "../../resources/picture/nari_EnemyYellow.png";

    if (this.mapData) {
      switch (this.mapData.getStageNumber()) {
        case 1:
          imagePath = "../../resources/picture/nari_EnemyYellow.png";
          break;

        case 2:
          imagePath = "../../resources/picture/taku_EnemyYellow.png";
          break;

        case 3:
          imagePath = "../../resources/picture/aniki_EnemyYellow.png";
          break;

        default:
          break;
      }
    }

    // 画像読み込み
    this.normalImage = new Image();

    this.normalImage.onload = () => {
      console.log(
        "【成功】ステージ" +
          this.mapData.getStageNumber() +
          "用の画像を読み込みました！",
      );
    };

    this.normalImage.onerror = () => {
      console.error("【エラー】画像が見つかりません: " + imagePath);
    };

    this.normalImage.src = imagePath;
  }

  // ==================================================
  // タイマー
  // ==================================================

  // ポーズ中の時間を出撃タイマーへ反映する
  resumeTimer() {
    // 出撃待機中のみ補正を行う
    if (this.timerStarted && !this.released) {
      // ポーズ時間
      const pauseDuration = Date.now() - this.pauseStartTime;

      // タイマー補正
      this.startTime += pauseDuration;
    }
  }

  // ==================================================
  // ポジション
  // ==================================================

  // プレイヤー被弾時に初期位置へ戻す
  resetToStartPosition() {
    // Enemy共通処理
    super.resetToStartPosition();

    // 出撃状態初期化
    this.released = false;

    // タイマー初期化
    this.timerStarted = false;
  }

  // ==================================================
  // 動き
  // ==================================================

  // 6秒経過後に出撃
  move(map) {
    // READY中は移動しない
    if (this.mapData.isWaitingStart()) {
      return;
    }

    // 初回入力後にタイマー開始
    if (!this.timerStarted) {
      this.startTime = Date.now();
      this.timerStarted = true;
    }

    // 出撃待機中
    if (!this.released) {
      const elapsed = Date.now() - this.startTime;

      // 6秒経過するまで待機
      if (elapsed < 6000) {
        return;
      }

      // 出撃開始
      this.released = true;
    }

    // Enemy共通処理
    super.move(map);
  }

  // ==================================================
  // 方向決定
  // ==================================================

  decideNextDirection(validDirections, map, mapData) {
    // 移動可能方向が無い
    if (!mapData || validDirections.length === 0) {
      return Direction.NONE;
    }

    // プレイヤー位置
    let targetCol = Math.floor(mapData.getPacX() / GameConfig.TILE_SIZE);

    let targetRow = Math.floor(mapData.getPacY() / GameConfig.TILE_SIZE);

    // プレイヤーの4マス先を予測
    switch (mapData.getPlayerDirection()) {
      case Direction.UP:
        targetRow -= YellowEnemy.PREDICT_TILES;
        break;

      case Direction.DOWN:
        targetRow += YellowEnemy.PREDICT_TILES;
        break;

      case Direction.LEFT:
        targetCol -= YellowEnemy.PREDICT_TILES;
        break;

      case Direction.RIGHT:
        targetCol += YellowEnemy.PREDICT_TILES;
        break;

      default:
        break;
    }

    // 縄張りモード
    if (this.currentState === EnemyState.SCATTER) {
      return this.getClosestDirection(
        validDirections,
        YellowEnemy.TERRITORY_COL,
        YellowEnemy.TERRITORY_ROW,
      );
    }

    // FEVER・DEAD状態
    const special = this.handleSpecialState(
      validDirections,
      targetCol,
      targetRow,
      map,
    );

    if (special !== null) {
      return special;
    }

    // ==================================================
    // YellowEnemy固有AI
    // プレイヤーの4マス先を追跡
    // ==================================================

    return this.getClosestDirection(validDirections, targetCol, targetRow);
  }
}
