package Items;

import Characters.Sengoku;
import javafx.scene.canvas.GraphicsContext;
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
    @Override
    public void draw(GraphicsContext gc, double x, double y, double tileSize) {
        Circle circle = (Circle) this.view;
        double radius = circle.getRadius();
        
        gc.setFill(circle.getFill());
        gc.fillOval(
            x + tileSize / 2.0 - radius,
            y + tileSize / 2.0 - radius,
            radius * 2, radius * 2
        );
    }
}