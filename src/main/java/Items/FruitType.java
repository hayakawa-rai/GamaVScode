package Items;

import javafx.scene.image.Image;


/**
 * フルーツの種類を表す列挙型。
 * 種類ごとにスコアと色（仮描画用）を持つ。
 */
public enum FruitType {
    CHERRY(100, "/picture/sakuranbo.png"),         // サクランボ
    STRAWBERRY(300, "/picture/ichigo.png"), // イチゴ
    ORANGE(500, "/picture/orange.png"),         // オレンジ
    APPLE(700, "/picture/ringo.png"),           // リンゴ
    GRAPE(1000, "/picture/budo.png");          // ブドウ

    private final int score;
    private final Image image;

    FruitType(int score, String imagePath) {
        this.score = score;
        var stream = getClass().getResourceAsStream(imagePath);
        if (stream == null) {
            throw new IllegalStateException("画像が見つかりません: " + imagePath);
        }
        this.image = new Image(stream);
    }

    private static final FruitType[] VALUES = values();

    /**
     * 全種類の中からランダムに1つ選んで返す。
     * MapData#spawnFruit() から呼ばれる。
     */
    public static FruitType random(java.util.Random random) {
        return VALUES[random.nextInt(VALUES.length)];
    }
    
    // ==================================================
 	// getter
 	// ==================================================
    public int getScore() { return score; }
    public Image getImage() { return image; }
}