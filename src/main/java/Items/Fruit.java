package Items;

import Characters.Syujinkou;
import javafx.scene.canvas.GraphicsContext;

/**
 * 固定位置に出現し、プレイヤーに食べられるのを待つボーナスフルーツ。
 * 移動ロジックが不要になったため、非常にシンプル。
 */
public class Fruit extends Item {

    private final FruitType type;
    private final double x; // 描画用の中心ピクセル座標
    private final double y;

    public Fruit(FruitType type, double startX, double startY) {
        // FruitTypeからスコアを取得して親クラス(Item)に渡す
        super(type.getScore(), null); 
        this.type = type;
        this.x = startX;
        this.y = startY;
    }

    public FruitType getType() { return type; }

    @Override
    public void onEaten(Syujinkou player) {
        player.addScore(score); // スコア加算
        System.out.println("【ご褒美】" + type + "を食べた！ +" + score + "点");
    }

    @Override
    public void draw(GraphicsContext gc, double leftX, double topY, double tileSize) {
        // 仮描画：種類ごとの色で丸を描き、上に緑のヘタを描く
        gc.setFill(type.getColor());
        gc.fillOval(leftX + tileSize * 0.15, topY + tileSize * 0.15, tileSize * 0.7, tileSize * 0.7);
        gc.setFill(javafx.scene.paint.Color.GREEN);
        gc.fillRect(leftX + tileSize * 0.45, topY + tileSize * 0.05, tileSize * 0.1, tileSize * 0.2);
    }
}