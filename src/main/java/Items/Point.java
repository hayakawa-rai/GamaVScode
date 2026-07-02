package Items;

import Characters.Sengoku;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point extends Item {
    
    public Point(double pixelX, double pixelY) {

        // 親クラス（Item）に、10点というスコアと、半径3の黄色い円（Circle）を渡す
        super(10, new Circle(pixelX, pixelY, 3, Color.YELLOW));
    }

    @Override
    public void onEaten(Sengoku player) {

    		// プレイヤーのスコアを加算
        player.addScore(this.score);
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y, double tileSize) {

    		// 自分が持っている Circle オブジェクトから半径と色を自動取得
        Circle circle = (Circle) this.view;
        double radius = circle.getRadius();        
        gc.setFill(circle.getFill());

        // マスの中心（tileSize / 2.0）を基準に、円を描画する
        gc.fillOval(
            x + tileSize / 2.0 - radius,
            y + tileSize / 2.0 - radius,
            radius * 2, radius * 2
        );
    }
}