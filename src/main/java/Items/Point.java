package Items;

import Character.Sengoku;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point extends Item {
    public Point(double pixelX, double pixelY) {
        // 10点、半径3の黄色い円
        super(10, new Circle(pixelX, pixelY, 3, Color.YELLOW));
    }

    @Override
    public void onEaten(Sengoku player) {
        player.addScore(this.score);
    }
}