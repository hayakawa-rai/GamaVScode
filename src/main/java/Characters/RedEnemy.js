import { Enemy } from "./Enemy.js";
import { GameConfig } from "../common/GameConfig.js";
import { EnemyState } from "./EnemyState.js";
import { Direction } from "./Direction.js";
// ==================================================
// RedEnemy（赤）
//
// 最短距離でプレイヤーを追跡する敵
// ==================================================
export class RedEnemy extends Enemy {

  // 初期位置（エネミーハウス中央付近）
  static START_COL = 13;
  static START_ROW = 13;

  // SCATTER状態で向かう縄張り（右上）
  static TERRITORY_COL = 24;
  static TERRITORY_ROW = 3;

  // ==================================================
  // コンストラクタ
  // ==================================================
  constructor(mapData) {

    // マスの中心座標を初期位置として親クラスへ渡す
    super(
      RedEnemy.START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2,
      RedEnemy.START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2,
      2.5,
    );

    // ステージ情報を保存
    this.mapData = mapData;

    // FEVER状態用画像を読み込む
    this.loadFeverImage();

    // DEAD状態用画像を読み込む
    this.loadDeadImage();

    // 通常時に使用する画像パス
    let imagePath = "../../resources/picture/nari_EnemyRed.png";

    if (this.mapData) {
      switch (this.mapData.getStageNumber()) {
        case 1:
          imagePath = "../../resources/picture/nari_EnemyRed.png";
          break;
        case 2:
          imagePath = "../../resources/picture/taku_EnemyRed.png";
          break;
        case 3:
          imagePath = "../../resources/picture/aniki_EnemyRed.png";
          break;
      }
    }

    // ==========================================
    // 画像読み込み
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
  // 方向決定
  // ==================================================
  decideNextDirection(validDirections, map, mapData) {
    // 移動可能な方向がない場合
    if (!mapData || validDirections.length === 0) {
      return Direction.NONE;
    }

    // プレイヤーの現在位置を取得
    const pacX = mapData.getPacX() + GameConfig.TILE_SIZE / 2;

    const pacY = mapData.getPacY() + GameConfig.TILE_SIZE / 2;

    // プレイヤー位置をタイル座標へ変換
    const targetCol = Math.floor(pacX / GameConfig.TILE_SIZE);

    const targetRow = Math.floor(pacY / GameConfig.TILE_SIZE);

    // ==========================================
    // 縄張りモード（SCATTER）
    // ==========================================

    if (this.currentState === EnemyState.SCATTER) {
      return this.getClosestDirection(
        validDirections,
        RedEnemy.TERRITORY_COL,
        RedEnemy.TERRITORY_ROW,
      );
    }

    // ==========================================
    // FEVER・DEAD状態共通処理
    // ==========================================

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
    // RedEnemy固有AI
    //
    // プレイヤーへ最短距離で接近する
    // ==================================================

    return this.getClosestDirection(validDirections, targetCol, targetRow);
  }
}
