package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PacmanSample extends Application {

    private final int[][] map = {
            {1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
            {1,0,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,0,1},
            {1,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,1},
            {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
            {1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1},
            {1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1},
            {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
            {1,1,1,1,0,1,0,1,1,9,1,1,0,1,0,1,1,1,1},
            {9,0,0,0,0,1,0,1,9,9,9,1,0,1,0,0,0,0,9},
            {1,1,1,1,0,1,0,1,9,9,9,1,0,1,0,1,1,1,1},
            {1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
            {1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1},
            {1,0,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,0,1},
            {1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1},
            {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,9,1,1,1,1,1,1,1,1,1}
    };

    private final int TILE = 30;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(map[0].length * TILE, map.length * TILE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        draw(gc);

        Scene scene = new Scene(new Pane(canvas));
        stage.setScene(scene);
        stage.setTitle("Pacman Map Only");
        stage.show();
    }

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 800);

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {

                if (map[y][x] == 1) {
                    gc.setFill(Color.BLUE);   // 壁
                } else if (map[y][x] == 9) {
                    gc.setFill(Color.DARKGRAY); // 特殊マス（通路扱い）
                } else {
                    gc.setFill(Color.BLACK);  // 通路
                }

                gc.fillRect(x * TILE, y * TILE, TILE, TILE);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
