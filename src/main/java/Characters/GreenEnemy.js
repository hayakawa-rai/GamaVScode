// ==================================================
// GreenEnemy（緑）
//
// プレイヤーとの距離によって行動を変える敵
// 遠いとき → プレイヤーを追跡
// 近いとき → 縄張りへ戻る
// ==================================================

import { Enemy } from "./Enemy.js";
import { GameConfig } from "../common/GameConfig.js";
import { EnemyState } from "./EnemyState.js";
import { Direction } from "./Direction.js";

export class GreenEnemy extends Enemy {

    // 初期位置（エネミーハウス内）
    static START_COL = 14;
    static START_ROW = 14;

    // 8マス以上離れていたら追跡
    static BORDER = 8;

    // SCATTER状態時の縄張り座標（左下）
    static TERRITORY_COL = 3;
    static TERRITORY_ROW = 26;

    // ==================================================
    // コンストラクタ
    // ==================================================
    constructor(mapData) {

        // マスの中心座標を初期位置としてEnemyに渡す
        super(
            GreenEnemy.START_COL * GameConfig.TILE_SIZE +
            GameConfig.TILE_SIZE / 2,

            GreenEnemy.START_ROW * GameConfig.TILE_SIZE +
            GameConfig.TILE_SIZE / 2,

            2
        );

        // マップデータ保持
        this.mapData = mapData;

        // 出撃時間を記録
        this.startTime = 0;

        // 出撃タイマーが開始されたか
        this.timerStarted = false;

        // 巣から出撃済みか
        this.released = false;

        // FEVER画像を読み込む
        this.loadFeverImage();

        // DEAD画像を読み込む
        this.loadDeadImage();

        // ==========================================
        // ステージごとに画像を切り替える
        // ==========================================

        let imagePath =
             "../../resources/picture/nari_EnemyGreen.png";

        if (this.mapData) {

            switch (
                this.mapData.getStageNumber()
            ) {

                case 1:
                    // ステージ1
                    imagePath =
                         "../../resources/picture/nari_EnemyGreen.png";
                    break;

                case 2:
                    // ステージ2
                    imagePath =
                         "../../resources/picture/taku_EnemyGreen.png";
                    break;

                case 3:
                    // ステージ3
                    imagePath =
                         "../../resources/picture/aniki_EnemyGreen.png";
                    break;

                default:
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
                "用の画像を読み込みました！"
            );
        };

        this.normalImage.onerror = () => {

            console.error(
                "【エラー】画像が見つかりません: " +
                imagePath
            );
        };

        this.normalImage.src = imagePath;
    }

    // ==================================================
    // タイマー
    // ==================================================

    // ポーズ中の時間を出撃タイマーへ反映する
    resumeTimer() {

        // 出撃待機中のみタイマー補正を行う
        if (
            this.timerStarted &&
            !this.released
        ) {

            // ポーズしていた時間を計算
            const pauseDuration =
                Date.now() -
                this.pauseStartTime;

            // タイマーを補正
            this.startTime += pauseDuration;
        }
    }

    // ==================================================
    // ポジション
    // ==================================================

    // プレイヤー被弾時に初期位置へ戻す
    resetToStartPosition() {

        // Enemy共通のリセット処理
        super.resetToStartPosition();

        // 出撃状態を初期化
        this.released = false;

        // 出撃タイマーをリセット
        this.timerStarted = false;
    }

    // ==================================================
    // 動き
    // ==================================================

    // 10秒経過後に出撃
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

            // 経過時間を取得
            const elapsed =
                Date.now() -
                this.startTime;

            // 10秒経過するまで待機
            if (elapsed < 10000) {
                return;
            }

            // 出撃開始
            this.released = true;
        }

        // Enemy共通の移動処理
        super.move(map);
    }

    // ==================================================
    // 方向決定
    // ==================================================

    // GreenEnemyの移動方向を決定する
    // 遠い → 追跡
    // 近い → 左下の縄張りへ戻る
    decideNextDirection(
        validDirections,
        map,
        mapData
    ) {

        // 移動可能な方向が存在しない場合
        if (
            !mapData ||
            validDirections.length === 0
        ) {
            return Direction.NONE;
        }

        // プレイヤーの現在座標を取得
        const pacX = mapData.getPacX();
        const pacY = mapData.getPacY();

        // プレイヤーの位置をタイル座標へ変換
        const targetCol =
            Math.floor(
                pacX /
                GameConfig.TILE_SIZE
            );

        const targetRow =
            Math.floor(
                pacY /
                GameConfig.TILE_SIZE
            );

        // ==========================================
        // 縄張りモード
        // ==========================================

        if (
            this.currentState ===
            EnemyState.SCATTER
        ) {

            return this.getClosestDirection(
                validDirections,
                GreenEnemy.TERRITORY_COL,
                GreenEnemy.TERRITORY_ROW
            );
        }

        // ==========================================
        // FEVER・DEAD状態の共通処理
        // ==========================================

        const special =
            this.handleSpecialState(
                validDirections,
                targetCol,
                targetRow,
                map
            );

        if (special !== null) {
            return special;
        }

        // 自分の現在位置を取得
        const myCol =
            Math.floor(
                this.x /
                GameConfig.TILE_SIZE
            );

        const myRow =
            Math.floor(
                this.y /
                GameConfig.TILE_SIZE
            );

        // プレイヤーとの距離計算（マス単位）
        const distance =
            Math.sqrt(
                Math.pow(
                    myCol - targetCol,
                    2
                ) +
                Math.pow(
                    myRow - targetRow,
                    2
                )
            );

        // プレイヤーが遠い場合は追跡
        if (
            distance >=
            GreenEnemy.BORDER
        ) {

            return this.getClosestDirection(
                validDirections,
                targetCol,
                targetRow
            );
        }

        // ==================================================
        // GreenEnemy固有AI
        //
        // プレイヤーが近い場合は
        // 左下の縄張りへ戻る
        // ==================================================

        return this.getClosestDirection(
            validDirections,
            GreenEnemy.TERRITORY_COL,
            GreenEnemy.TERRITORY_ROW
        );
    }
}