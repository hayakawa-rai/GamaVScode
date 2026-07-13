// ==================================================
// YellowEnemy（黄）
//
// プレイヤーの進行方向を予測して追跡する敵
// プレイヤーの4マス先を目標地点として移動する
// ==================================================

class YellowEnemy extends Enemy {

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
            YellowEnemy.START_COL * GameConfig.TILE_SIZE +
            GameConfig.TILE_SIZE / 2,

            YellowEnemy.START_ROW * GameConfig.TILE_SIZE +
            GameConfig.TILE_SIZE / 2,

            2
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
        let imagePath = "/picture/nari_EnemyYellow.png";

        if (this.mapData) {

            switch (this.mapData.getStageNumber()) {

                case 1:
                    imagePath =
                        "/picture/nari_EnemyYellow.png";
                    break;

                case 2:
                    imagePath =
                        "/picture/taku_EnemyYellow.png";
                    break;

                case 3:
                    imagePath =
                        "/picture/aniki_EnemyYellow.png";

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

        // 出撃待機中のみ補正を行う
        if (
            this.timerStarted &&
            !this.released
    