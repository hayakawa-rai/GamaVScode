import { Character } from "./Character.js";
import { Direction, DirectionValues } from "./Direction.js";
import { EnemyState } from "./EnemyState.js";
import { GameConfig } from "../common/GameConfig.js";

//敵キャラクターの共通処理を管理する抽象クラス
//移動処理、状態管理（SCATTER、FEVER、DEAD）、画像管理、巣への帰還処理
export class Enemy extends Character {
	
  // 敵キャラクターの画像表示用(実質未使用)
  ImageView = null;
  // 現在のステージ情報
  mapData;
  // 現在の敵の状態（通常・FEVER・DEAD）
  currentState = EnemyState.SCATTER;
  // 通常時の画像
  normalImage;
  // FEVER状態時の画像
  feverImage;
  // DEAD状態時の画像
  deadImage;
  // ポーズ時間
  pauseStartTime = 0;
  // 敵の初期位置（リスポーン用）
  startX;
  startY;
  // スコアポップアップ表示用
  lastDefeatScore = 0;
  scorePopupStartTime = 0;
  scorePopupActive = false;
  static SCORE_POPUP_DURATION = 1000; // 表示時間(ms)
  defeatX = 0; // 倒された瞬間のX座標（固定）
  defeatY = 0; // 倒された瞬間のY座標（固定）

  // ==================================================
  // コンストラクタ
  // ==================================================
  constructor(startX, startY, speed) {
    super(startX, startY, speed);

    // Enemy も直接 new させない（abstract class の再現）
    if (new.target === Enemy) {
      throw new TypeError("Enemy は抽象クラスなので直接生成できません");
    }

    // 初期位置を保存
    this.startX = startX;
    this.startY = startY;
  }

  // ==================================================
  // タイマー
  // ==================================================
  // ポーズ開始時間を記録する、出撃タイマーや状態タイマー停止用
  pauseTimer() {
    this.pauseStartTime = Date.now();
  }

  // ポーズ解除時の処理、必要に応じてタイマー補正を行う
  resumeTimer() {}

  // ==================================================
  // 画像読み込み
  // ==================================================

  // FEVER状態で使用する画像をステージごとに読み込む
  loadFeverImage() {
    // デフォルト画像(ステージ1)
    let feverPath = "../../resources/picture/nari_EnemyFever.png";

    // 現在のステージ番号に応じて画像を切り替える
    if (this.mapData) {
      switch (this.mapData.getStageNumber()) {
        case 1:
          feverPath = "../../resources/picture/nari_EnemyFever.png";
          break;
        case 2:
          feverPath = "../../resources/picture/taku_EnemyFever.png";
          break;
        case 3:
          feverPath = "../../resources/picture/aniki_EnemyFever.png";
          break;
      }
    }

    this.feverImage = new Image();

    this.feverImage.onload = () => {
      console.log("FEVER画像読込成功: " + feverPath);
    };

    this.feverImage.onerror = () => {
      console.error("FEVER画像が見つかりません: " + feverPath);
    };

    this.feverImage.src = feverPath;
  }

  // DEAD状態で使用する画像をステージごとに読み込む
  loadDeadImage() {
    // デフォルトはステージ1
    let deadPath = "../../resources/picture/nari_EnemyDead.png";

    // 現在のステージ番号に応じて画像を切り替える
    if (this.mapData) {
      switch (this.mapData.getStageNumber()) {
        case 1:
          deadPath = "../../resources/picture/nari_EnemyDead.png";
          break;
        case 2:
          deadPath = "../../resources/picture/taku_EnemyDead.png";
          break;
        case 3:
          deadPath = "../../resources/picture/aniki_EnemyDead.png";
          break;
      }
    }

    this.deadImage = new Image();

    this.deadImage.onload = () => {
      console.log("DEAD画像読込成功: " + deadPath);
    };

    this.deadImage.onerror = () => {
      console.error("DEAD画像が見つかりません: " + deadPath);
    };

    this.deadImage.src = deadPath;
  }

  // ==================================================
  // スコア表示
  // ==================================================
  // ポップアップをまだ表示すべきか判定する（時間経過で自動的にfalseになる）
  isScorePopupActive() {
    if (
      this.scorePopupActive &&
      Date.now() - this.scorePopupStartTime > Enemy.SCORE_POPUP_DURATION
    ) {
      this.scorePopupActive = false;
    }
    return this.scorePopupActive;
  }

  // 敵をDEAD状態にし、撃破スコアの表示を開始する
  onDefeated(score) {
    this.lastDefeatScore = score;
    this.scorePopupStartTime = Date.now();
    this.scorePopupActive = true;
    this.defeatX = this.x; // 倒された瞬間の位置を固定
    this.defeatY = this.y;
    this.setCurrentState(EnemyState.DEAD);
  }

  // ==================================================
  // ポジション
  // ==================================================
  // プレイヤーが被弾時に元の場所、出撃時間をリセット、状態を縄張りモード(SCATTER)へ戻す
  resetToStartPosition() {
    this.x = this.startX;
    this.y = this.startY;
    this.direction = Direction.NONE;
    this.currentState = EnemyState.SCATTER;
  }

  // ==================================================
  // 動き
  // ==================================================

  move(map) {
    // 現在位置をタイル座標へ変換
    const tileX = Math.floor(this.x / GameConfig.TILE_SIZE);
    const tileY = Math.floor(this.y / GameConfig.TILE_SIZE);

    // 範囲外防止
    if (
      tileY < 0 ||
      tileY >= map.length ||
      tileX < 0 ||
      tileX >= map[0].length
    ) {
      return;
    }

    // 現在いるタイルの中心座標を計算
    const cx = tileX * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2;
    const cy = tileY * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2;

    // 現在のマス情報を取得
    const currentTileType = map[tileY][tileX];

    // DEAD状態で巣の床(8)に戻ったら復活
    if (this.currentState == EnemyState.DEAD) {
      if (currentTileType == 8) {
        this.currentState = EnemyState.SCATTER;
        console.log(`${this.constructor.name}が巣に帰還し、復活しました`);
      }
    }

    // 現在のスピードの計算
    let currentSpeed = this.getSpeed();
    // FEVER時は減速
    if (this.currentState == EnemyState.FEVER) {
      currentSpeed = this.getSpeed() * 0.5;
    }
    // DEAD時は高速帰還
    if (this.currentState == EnemyState.DEAD) {
      currentSpeed = this.getSpeed() * 3;
    }
    // タイルの中心に近づいたか判定
    const atCenter =
      Math.abs(this.x - cx) < currentSpeed &&
      Math.abs(this.y - cy) < currentSpeed;

    // 完全に停止している(NONE)か、マスの中心に到達したら方向転換
    if (this.direction == Direction.NONE || atCenter) {
      // 現在進行可能な方向を取得
      const validDirections = this.#getValidDirections(map);

      if (validDirections.length > 0) {
        // 現在のタイル座標を一時的に取得（条件判定用）
        const currentRow = Math.floor(this.y / GameConfig.TILE_SIZE);
        const currentCol = Math.floor(this.x / GameConfig.TILE_SIZE);

        // 巣の中にいる間は、ターゲットを強制的に巣のすぐ外に移動する
        if (
          this.currentState !== EnemyState.DEAD &&
          currentRow >= 12 &&
          currentRow <= 15 &&
          currentCol >= 12 &&
          currentCol <= 15
        ) {
          this.y = cy;
          this.x = cx;
          this.direction = Direction.UP;
        } else {
          // 敵固有AIで次の移動方向を決定
          const chosenDirection = this.decideNextDirection(
            validDirections,
            map,
            this.mapData,
          );

          // 中心にぴったり位置補正（軸ズレによるスタック防止）
          this.x = cx;
          this.y = cy;
          this.direction = chosenDirection;
        }
      } else {
        // 進行できる方向が無い場合は停止
        this.direction = Direction.NONE;
      }
    }

    // 決定した方向に実際に移動する処理
    if (this.direction !== Direction.NONE) {
      this.x += this.direction.dx * currentSpeed;
      this.y += this.direction.dy * currentSpeed;
      // 横移動時はY座標を中心へ補正
      if (this.direction.dx !== 0) {
        this.y += (cy - this.y) * 0.2;
      }
      // 縦移動時はX座標を中心へ補正
      if (this.direction.dy !== 0) {
        this.x += (cx - this.x) * 0.2;
      }
    }
  }

  // 指定した方向へ移動できるか判定する
  #canmove(direction, map) {
    // 移動しない場合は不可
    if (direction === Direction.NONE) return false;

    // 現在位置のタイル座標を取得
    const currentCol = Math.floor(this.x / GameConfig.TILE_SIZE);
    const currentRow = Math.floor(this.y / GameConfig.TILE_SIZE);

    // 移動先のタイル座標を計算
    const nextCol = currentCol + direction.dx;
    const nextRow = currentRow + direction.dy;

    // マップ範囲外は移動不可
    if (
      nextRow < 0 ||
      nextRow >= map.length ||
      nextCol < 0 ||
      nextCol >= map[0].length
    ) {
      return false;
    }
    // 壁(1)には進めない
    if (map[nextRow][nextCol] === 1) {
      return false;
    }

    // 現在地と移動先のマス情報を取得
    const currentTileType = map[currentRow][currentCol];
    const nextTileType = map[nextRow][nextCol];

    // 通常状態の敵の「巣（扉含む）」への侵入制限
    if (this.currentState !== EnemyState.DEAD) {
      // 外(8以外)から、扉(7)や床(8)に入ろうとしたら通行不可
      if (currentTileType !== 8 && (nextTileType === 7 || nextTileType === 8)) {
        return false;
      }
    }
    return true;
  }

  // DEAD・FEVERの共通処理移動処理
  handleSpecialState(validDirections, targetCol, targetRow, map) {
    // DEAD状態なら、自動的にマップ内の「7（扉）」の中から【一番近い場所】を探してそこへ帰る
    if (this.currentState === EnemyState.DEAD) {
      const currentMap = map;

      // デフォルトのバックアップ座標
      let bestGateCol = 14;
      let bestGateRow = 13;
      let minDistanceSq = Infinity;

      // 今の自分の位置（マス単位）
      const myCol = Math.floor(this.x / GameConfig.TILE_SIZE);
      const myRow = Math.floor(this.y / GameConfig.TILE_SIZE);

      // マップ全体からすべての「7」を探し、一番近いものを選択する
      for (let r = 0; r < currentMap.length; r++) {
        for (let c = 0; c < currentMap[r].length; c++) {
          if (currentMap[r][c] === 7) {
            // 自分の現在地からの距離を計算（三平方の定理）
            const distSq = (c - myCol) ** 2 + (r - myRow) ** 2;
            if (distSq < minDistanceSq) {
              minDistanceSq = distSq;
              bestGateCol = c;
              bestGateRow = r;
            }
          }
        }
      }

      // 最も近い扉（右半分にいるときは右側の7、左半分にいるときは左側の7）に向かわせる
      return this.getClosestDirection(
        validDirections,
        bestGateCol,
        bestGateRow,
      );
    }

    if (this.currentState === EnemyState.FEVER) {
      return this.getFarthestDirection(validDirections, targetCol, targetRow);
    }
    return null;
  }

  // ==================================================
  // 方向決定
  // ==================================================
  // 三平方の定理を使って目的地に一番近い方向を選ぶ共通処理
  getClosestDirection(validDirections, targetCol, targetRow) {
    let bestDirection = Direction.NONE;
    let minDistance = Infinity;
    const currentCol = Math.floor(this.x / GameConfig.TILE_SIZE);
    const currentRow = Math.floor(this.y / GameConfig.TILE_SIZE);

    for (const dir of validDirections) {
      const nextCol = currentCol + dir.dx;
      const nextRow = currentRow + dir.dy;
      const distanceSq =
        (nextCol - targetCol) ** 2 + (nextRow - targetRow) ** 2;

      if (distanceSq < minDistance) {
        minDistance = distanceSq;
        bestDirection = dir;
      }
    }
    return bestDirection !== Direction.NONE
      ? bestDirection
      : validDirections[0];
  }

  // 目標地点から最も遠ざかる方向を選択する
  getFarthestDirection(validDirections, targetCol, targetRow) {
    // 最終的に選択する方向
    let bestDirection = Direction.NONE;

    // 最大距離を記録する変数
    let maxDistance = -1;

    // 現在のタイル座標を取得
    const currentCol = Math.floor(this.x / GameConfig.TILE_SIZE);
    const currentRow = Math.floor(this.y / GameConfig.TILE_SIZE);

    // 移動可能な全方向を調べる
    for (const dir of validDirections) {
      // 1マス移動後の座標を計算
      const nextCol = currentCol + dir.dx;
      const nextRow = currentRow + dir.dy;

      // 目標地点までの距離の二乗を計算
      const distanceSq =
        (nextCol - targetCol) ** 2 + (nextRow - targetRow) ** 2;

      // 今までで最も遠くなる方向なら更新
      if (distanceSq > maxDistance) {
        maxDistance = distanceSq;
        bestDirection = dir;
      }
    }
    // 選択された方向を返す
    // 万が一見つからなければ先頭の方向を返す
    return bestDirection !== Direction.NONE
      ? bestDirection
      : validDirections[0];
  }

  // 2つの方向が互いに反対方向（Uターン方向）か判定する
  #isOppositeDirection(dir1, dir2) {
    // NONE は反対方向として扱わない
    if (dir1 === Direction.NONE || dir2 === Direction.NONE) return false;

    // ベクトルの和が(0,0)なら反対方向
    return dir1.dx + dir2.dx === 0 && dir1.dy + dir2.dy === 0;
  }

  // 現在位置から移動可能な方向一覧を取得する
  #getValidDirections(map) {
    // 移動可能な方向を格納するリスト
    const list = [];

    // 全方向をチェック
    for (const dir of DirectionValues) {
      // NONE は判定対象外
      if (dir === Direction.NONE) continue;

      // 常にUターン禁止
      if (this.#isOppositeDirection(dir, this.direction)) continue;

      // 実際に移動可能なら候補に追加
      if (this.#canmove(dir, map)) {
        list.push(dir);
      }
    }
    return list;
  }

  // 敵ごとのAIで次の進行方向を決定する
  decideNextDirection(validDirections, map, mapData) {
    throw new Error("decideNextDirection() はサブクラスで実装してください");
  }

  // ==================================================
  // getter
  // ==================================================
  // 現在の状態に対応した画像を返す
  getEnemyImage() {
    // 撃破状態
    if (this.currentState === EnemyState.DEAD) {
      return this.deadImage;
    }
    // FEVER状態
    if (this.currentState === EnemyState.FEVER) {
      return this.feverImage;
    }
    // 通常状態
    return this.normalImage;
  }

  // ポップアップの進行度を0.0(開始)〜1.0(終了)で返す
  getScorePopupProgress() {
    const elapsed = Date.now() - this.scorePopupStartTime;
    return Math.min(1.0, Math.max(0.0, elapsed / Enemy.SCORE_POPUP_DURATION));
  }

  // 現在の敵の状態を取得する
  getCurrentState() {
    return this.currentState;
  }

  // 現在のX座標を取得する
  getX() {
    return this.x;
  }

  // 現在のY座標を取得する
  getY() {
    return this.y;
  }
  // 表示するスコア値を返す
  getLastDefeatScore() {
    return this.lastDefeatScore;
  }

  // 倒された瞬間のX座標を返す（ポップアップ表示用、固定値）
  getDefeatX() {
    return this.defeatX;
  }

  // 倒された瞬間のY座標を返す（ポップアップ表示用、固定値）
  getDefeatY() {
    return this.defeatY;
  }

  // ==================================================
  // setter
  // ==================================================
  //　敵の状態を変更する
  setCurrentState(state) {
    this.currentState = state;
  }
}
