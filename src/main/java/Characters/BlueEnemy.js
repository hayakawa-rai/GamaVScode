import { Enemy } from "./Enemy.js";
import { GameConfig } from "../common/GameConfig.js";
import { EnemyState } from "./EnemyState.js";
import { Direction } from "./Direction.js";
import { RedEnemy } from "./RedEnemy.js";
// ==================================================
// BlueEnemy（青）
//
// RedEnemyと連携してプレイヤーをはさみうちにする敵
// プレイヤーの進行方向の先を予測し、
// RedEnemyとの位置関係から追跡地点を決定する
// ==================================================
export class BlueEnemy extends Enemy {
    
  // 初期位置（エネミーハウス中央付近）
  static START_COL = 14;
  static START_ROW = 13;

  // プレイヤーの進行方向+2マス先を狙う
  static PREDICT_TILES = 2;

  // SCATTER状態時の縄張り座標（右下）
  static TERRITORY_COL = 24;
  static TERRITORY_ROW = 26;

  // ==================================================
  // コンストラクタ
  // ==================================================
  constructor(mapData) {

    // マスの中心座標を初期位置としてEnemyに渡す
    super(
      BlueEnemy.START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2,
      BlueEnemy.START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2,
      2.5,
    );

    this.mapData = mapData;

    // 出撃時間管理用
    this.startTime = 0;

    // 出撃タイマー開始フラグ
    this.timerStarted = false;

    // 巣から出撃済みか判定
    this.released = false;

    // 赤の位置を参照
    this.red = null;

    // FEVER画像をステージごとに読み込む
    this.loadFeverImage();

    // DEAD画像を読み込む
    this.loadDeadImage();

    // ==========================================
    // 現在のステージ番号によって
    // 読み込む画像を切り替える
    // ==========================================

    // デフォルト（ステージ1）
    let imagePath = "../../resources/picture/nari_EnemyBlue.png";

    if (this.mapData) {
      switch (this.mapData.getStageNumber()) {
        case 1:
          // ステージ1
          imagePath = "../../resources/picture/nari_EnemyBlue.png";
          break;

        case 2:
          // ステージ2
          imagePath = "../../resources/picture/taku_EnemyBlue.png";
          break;

        case 3:
          // ステージ3
          imagePath = "../../resources/picture/aniki_EnemyBlue.png";
          break;

        default:
          break;
      }
    }

    // ==========================================
    // RedEnemyをMapDataから探す
    // ==========================================

    if (this.mapData && this.mapData.getEnemies()) {
      for (const enemy of this.mapData.getEnemies()) {
        if (enemy instanceof RedEnemy) {
          this.red = enemy;
          break;
        }
      }
    }

    // ==========================================
    // 画像の読み込み
    // ==========================================

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
      // ポーズ時間を計算
      const pauseDuration = Date.now() - this.pauseStartTime;

      // タイマーをその分だけ後ろへずらす
      this.startTime += pauseDuration;
    }
  }

  // ==================================================
  // 動き
  // ==================================================

  // ゲーム開始から2秒後に出撃させる
  move(map) {
    // READY中は移動しない
    if (this.mapData.isWaitingStart()) {
      return;
    }

    // 初回入力後に初めてタイマー開始
    if (!this.timerStarted) {
      this.startTime = Date.now();
      this.timerStarted = true;
    }

    // 出撃待機中
    if (!this.released) {
      // 経過時間を計算
      const elapsed = Date.now() - this.startTime;

      // 2秒経過するまで待機
      if (elapsed < 2000) {
        return;
      }

      // 出撃許可
      this.released = true;
    }

    // Enemy共通の移動処理
    super.move(map);
  }

  // ==================================================
  // ポジション
  // ==================================================

  // プレイヤーが被弾時に元の場所、
  // 出撃時間をリセット
  resetToStartPosition() {
    // Enemy共通のリセット処理
    super.resetToStartPosition();

    // 出撃状態を初期化
    this.released = false;

    // 出撃タイマーを未開始状態へ戻す
    this.timerStarted = false;
  }

  // ==================================================
  // 方向決定
  // ==================================================

  decideNextDirection(validDirections, map, mapData) {
    // 移動可能な方向が存在しない場合
    if (!mapData || validDirections.length === 0) {
      return Direction.NONE;
    }

    // プレイヤーの現在位置を
    // タイル座標で取得
    let pacCol = Math.floor(mapData.getPacX() / GameConfig.TILE_SIZE);

    let pacRow = Math.floor(mapData.getPacY() / GameConfig.TILE_SIZE);

    // ==========================================
    // プレイヤーの進行方向の
    // 2マス先を予測
    // ==========================================

    switch (mapData.getPlayerDirection()) {
      case Direction.UP:
        pacRow -= BlueEnemy.PREDICT_TILES;
        break;

      case Direction.DOWN:
        pacRow += BlueEnemy.PREDICT_TILES;
        break;

      case Direction.LEFT:
        pacCol -= BlueEnemy.PREDICT_TILES;
        break;

      case Direction.RIGHT:
        pacCol += BlueEnemy.PREDICT_TILES;
        break;

      default:
        break;
    }

    // ==========================================
    // RedEnemyの現在位置を取得
    // ==========================================

    const redCol = Math.floor(this.red.getX() / GameConfig.TILE_SIZE);

    const redRow = Math.floor(this.red.getY() / GameConfig.TILE_SIZE);

    // ==========================================
    // RedEnemy→予測地点のベクトル
    // ==========================================

    const vx = pacCol - redCol;
    const vy = pacRow - redRow;

    // ベクトルを2倍した地点をターゲットにする
    const targetCol = pacCol + vx;
    const targetRow = pacRow + vy;

    // ==========================================
    // 縄張りモード
    // ==========================================

    if (this.currentState === EnemyState.SCATTER) {
      return this.getClosestDirection(
        validDirections,
        BlueEnemy.TERRITORY_COL,
        BlueEnemy.TERRITORY_ROW,
      );
    }

    // ==========================================
    // FEVER・DEAD状態の共通処理
    // ==========================================

    const special = this.handleSpecialState(
      validDirections,
      pacCol,
      pacRow,
      map,
    );

    if (special !== null) {
      return special;
    }

    // ==================================================
    // BlueEnemy固有AI
    //
    // RedEnemyと連携した
    // ターゲット地点へ向かう
    // ==================================================

    return this.getClosestDirection(validDirections, targetCol, targetRow);
  }
}
