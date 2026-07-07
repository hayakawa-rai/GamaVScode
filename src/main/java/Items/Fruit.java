package Items;

import Characters.Syujinkou;
import javafx.scene.canvas.GraphicsContext;

public class Fruit extends Item {
    private final FruitType type;
    
    //タイマー用変数
    private int remainingTicks; // 残りフレーム数（60FPSなら600で10秒）
    private boolean isExpired = false;

    public Fruit(FruitType type) {
        super(type.getScore(), null);
        this.type = type;
        this.remainingTicks = 600; // 10秒間表示
    }

    public FruitType getType() { return type; }
    public boolean isExpired() { return isExpired; }

    /**
     * 毎フレーム呼び出される更新処理
     */
    public void update() {
        if (remainingTicks > 0) {
            remainingTicks--;
        } else {
            isExpired = true;
        }
    }

    @Override
    public void onEaten(Syujinkou player) {
        player.addScore(score);
        System.out.println(type + "を食べた！ +" + score + "点");
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double tileSize) {
        // 残り2秒（120フレーム）以下になったら点滅させる演出（お好みで）
        if (remainingTicks < 120 && (remainingTicks / 10) % 2 == 0) {
            return; // 描画をスキップして点滅させる
        }
        
        gc.drawImage(
                type.getImage(),
                x + tileSize * 0.1,
                y + tileSize * 0.1,
                tileSize * 0.8,
                tileSize * 0.8
            );    }
}
