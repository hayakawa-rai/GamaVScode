package test.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import test.model.SampleModel;
import test.view.SampleView;

public class SampleController {

    private final SampleModel model;
    private final SampleView view;
    private final Canvas canvas;
    private AnimationTimer timer;

    public SampleController(SampleModel model, SampleView view, Canvas canvas, Scene scene) {
        this.model = model;
        this.view = view;
        this.canvas = canvas;
        attachInput(scene);
        startLoop();
    }

    private void attachInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.P) {
                model.togglePause();
                return;
            }
            if (model.isPaused()) return;

            if (code == KeyCode.W) model.setNextDirection(0, -1);
            if (code == KeyCode.S) model.setNextDirection(0, 1);
            if (code == KeyCode.A) model.setNextDirection(-1, 0);
            if (code == KeyCode.D) model.setNextDirection(1, 0);
        });
    }

    private void startLoop() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                model.updatePacman();
                model.updateMouth();
                view.drawStage(gc);
                view.drawPacman(gc);
            }
        };
        timer.start();
    }

    public void stop() {
        if (timer != null) timer.stop();
    }
}
