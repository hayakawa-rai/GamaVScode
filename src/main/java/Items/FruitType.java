package Items;

import javafx.scene.paint.Color;

/**
 * フルーツの種類を表す列挙型。
 * 種類ごとにスコアと色（仮描画用）を持つ。
 * 画像を使いたい場合はcolorの代わりにimagePathを持たせる形に変更可能。
 */
public enum FruitType {

    CHERRY(100, Color.RED),
    STRAWBERRY(300, Color.HOTPINK),
    ORANGE(500, Color.ORANGE),
    APPLE(700, Color.LIMEGREEN),
    GRAPE(1000, Color.PURPLE);

    private final int score;
    private final Color color;

    FruitType(int score, Color color) {
        this.score = score;
        this.color = color;
    }

    public int getScore() { return score; }
    public Color getColor() { return color; }

    private static final FruitType[] VALUES = values();

    /**
     * 全種類の中からランダムに1つ選んで返す。
     * MapData#spawnFruit() から呼ばれる。
     */
    public static FruitType random(java.util.Random random) {
        return VALUES[random.nextInt(VALUES.length)];
    }
}