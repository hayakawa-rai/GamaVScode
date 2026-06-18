package test.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import test.model.SampleModel;

public class SampleView {

    private final SampleModel model;

    public SampleView(SampleModel model) {
        this.model = model;
    }

    public void drawStage(GraphicsContext gc) {
        int cols = model.getMap()[0].length;
        int rows = model.getMap().length;
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, cols * SampleModel.TILE_SIZE, rows * SampleModel.TILE_SIZE);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tile = model.getMap()[row][col];
                int x = col * SampleModel.TILE_SIZE;
                int y = row * SampleModel.TILE_SIZE;
                if (tile == 1) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(x + 2, y + 2, SampleModel.TILE_SIZE - 4, SampleModel.TILE_SIZE - 4);
                }

            }
        }
    }

    public void drawPacman(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        double pacX = model.getPacX();
        double pacY = model.getPacY();
        double mouthAngle = model.getMouthAngle();

        double startAngle = 0;
        if (model.getDirX() == 1) startAngle = mouthAngle;
        if (model.getDirX() == -1) startAngle = 180 + mouthAngle;
        if (model.getDirY() == -1) startAngle = 90 + mouthAngle;
        if (model.getDirY() == 1) startAngle = 270 + mouthAngle;

        gc.fillArc(
            pacX - SampleModel.TILE_SIZE / 2,
            pacY - SampleModel.TILE_SIZE / 2,
            SampleModel.TILE_SIZE, SampleModel.TILE_SIZE,
            startAngle,
            360 - mouthAngle * 2,
            javafx.scene.shape.ArcType.ROUND
        );
    }
}
