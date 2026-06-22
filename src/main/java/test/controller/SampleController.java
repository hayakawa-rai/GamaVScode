package test.controller;

import Characters.Direction;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import test.SamplepracticeApp;
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

	public static void switchToStart(javafx.stage.Stage stage) {
		try {
			// startクラスのインスタンスを作る
			sample.start titleScreen = new sample.start();
			// ウィンドウの権利(stage)を渡して、タイトル画面を起動・上書きする！
			titleScreen.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void switchTopractice(javafx.stage.Stage stage) {
		try {
			// practiceクラスのインスタンスを作る
			sample.practice practiceScreen = new sample.practice();
			// ウィンドウの権利(stage)を渡して、練習モード画面を起動・上書きする！
			practiceScreen.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToGame(javafx.stage.Stage stage) {
		try {
			SamplepracticeApp App = new SamplepracticeApp();
			App.starts(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void attachInput(Scene scene) {
		scene.setOnKeyPressed(e -> {
			KeyCode code = e.getCode();
			if (code == KeyCode.P) {
				model.togglePause();
				return;
			}
			if (model.isPaused())
				return;

			if (code == KeyCode.W)
				model.setNextDirection(Direction.UP);
			if (code == KeyCode.S)
				model.setNextDirection(Direction.DOWN);
			if (code == KeyCode.A)
				model.setNextDirection(Direction.LEFT);
			if (code == KeyCode.D)
				model.setNextDirection(Direction.RIGHT);
		});
	}

	private void startLoop() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// model.updatePacman();
				// model.updateMouth();

				// パックマンの移動、アイテム捕食、口パク、敵の移動がすべて入った update()
				model.update();

				view.drawStage(gc);
				view.drawPacman(gc);
			}
		};
		timer.start();
	}

	public void stop() {
		if (timer != null)
			timer.stop();
	}
}
