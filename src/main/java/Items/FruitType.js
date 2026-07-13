/**
 * フルーツの種類を管理するクラス。
 * 種類ごとにスコアと画像を持つ。
 */
class FruitType {

    // =========================
    // コンストラクタ
    // =========================
    constructor(score, imagePath) {

        // スコア
        this.score = score;

        // 画像
        this.image = new Image();

        // 読み込み失敗時
        this.image.onerror = () => {
            console.error(
                "画像が見つかりません: " +
                imagePath
            );
        };

        // 画像読み込み
        this.image.src = imagePath;
    }

    // =========================
    // getter
    // =========================

    getScore() {
        return this.score;
    }

    getImage() {
        return this.image;
    }

    // =========================
    // ランダム選択
    // =========================

    /**
     * 全種類の中からランダムに1つ返す
     * MapData.spawnFruit() から呼ばれる
     */
    static random() {

        const values = FruitType.VALUES;

        return values[
            Math.floor(
                Math.random() * values.length
            )
        ];
    }
}

// =========================
// フルーツ定義
// =========================

// サクランボ
FruitType.CHERRY =
    new FruitType(
        100,
        "/picture/sakuranbo.png"
    );

// イチゴ
FruitType.STRAWBERRY =
    new FruitType(
        300,
        "/picture/ichigo.png"
    );

// オレンジ
FruitType.ORANGE =
    new FruitType(
        500,
        "/picture/orange.png"
    );

// リンゴ
FruitType.APPLE =
    new FruitType(
        700,
        "/picture/ringo.png"
    );

// ブドウ
FruitType.GRAPE =
    new FruitType(
        1000,
        "/picture/budo.png"
    );

// enumのvalues()相当
FruitType.VALUES = [
    FruitType.CHERRY,
    FruitType.STRAWBERRY,
    FruitType.ORANGE,
    FruitType.APPLE,
    FruitType.GRAPE
];

export default FruitType;