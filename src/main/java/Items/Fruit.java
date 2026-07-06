package Items;

import Characters.Syujinkou;
import javafx.scene.canvas.GraphicsContext;

/**
 * 固定位置に出現するボーナスフルーツ。
 * itemMap配列に直接配置される
 * 「普通のアイテム」として扱えるようにシンプル化した。
 * 座標情報は持たず、描画時に呼び出し元(MapView)から渡されるx, yをそのまま使う。
 */
public class Fruit extends Item {

    private final FruitType type;

    public Fruit(FruitType type) {
        super(type.getScore(), null); // Node（getView）は使わずCanvas描画のみ利用
        this.type = type;
    }

    public FruitType getType() { return type; }

    /**
     * プレイヤーがこのフルーツを取得した時の処理。スコアを加算する。
     */
    @Override
    public void onEaten(Syujinkou player) {
        player.addScore(score);
        System.out.println(type + "を食べた！ +" + score + "点");
    }

    /**
     * フルーツを画面に描画する。種類ごとの色で丸を描く仮実装。
     * 画像を使いたい場合はここをdrawImageに差し替える。
     */
    @Override
    public void draw(GraphicsContext gc, double x, double y, double tileSize) {
        gc.setFill(type.getColor());
        gc.fillOval(x + tileSize * 0.15, y + tileSize * 0.15, tileSize * 0.7, tileSize * 0.7);
        gc.setFill(javafx.scene.paint.Color.GREEN);
        gc.fillRect(x + tileSize * 0.45, y + tileSize * 0.05, tileSize * 0.1, tileSize * 0.2);
    }
}