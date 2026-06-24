package test.test2;

import Characters.Direction;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import start.Start;
import story.Practice;
import story.Story1;
import story.Story2;
import story.Story3;
import story.Story4;
import test1.Main1;
import test2.Main2;
import test3.Main3;

public class GameController {

	// 制御コンポーネント
	private final MapData model;   // パックマンの位置やマップ状態を持つデータソース
	private final MapView view;    // 描画処理
	private final Canvas canvas;   // 描画先キャンバス
	private AnimationTimer timer;  // ゲームループ(毎フレーム実行)

	// ゲームコントローラーの初期化と同時にゲームをスタートする
	public GameController(MapData model, MapView view, Canvas canvas, Scene scene) {
		this.model = model;
		this.view = view;
		this.canvas = canvas;

		// キーボードの入力を登録
		attachInput(scene);

		// メインゲームループ(AnimationTimer)の開始
		startLoop();
	}

	// === 画面遷移用のメソッド群 ===
	
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
			TestMainapp App = new TestMainapp();
			App.starts(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// キーボード入力処理
	private void attachInput(Scene scene) {
		scene.setOnKeyPressed(e -> {
			KeyCode code = e.getCode();

			// Pキーでゲームを一時停止・再開
			if (code == KeyCode.P) {
				model.togglePause();
				return;
			}

			// 一時停止中はパックマンの方向転換入力を受け付けない
			if (model.isPaused())
				return;

			// 矢印キーでも操作できるように拡張
            if (code == KeyCode.W || code == KeyCode.UP)    model.setNextDirection(Direction.UP);
            if (code == KeyCode.S || code == KeyCode.DOWN)  model.setNextDirection(Direction.DOWN);
            if (code == KeyCode.A || code == KeyCode.LEFT)  model.setNextDirection(Direction.LEFT);
            if (code == KeyCode.D || code == KeyCode.RIGHT) model.setNextDirection(Direction.RIGHT);
        });
    }
	// メインゲームループ
	private void startLoop() {
		// キャンバスの2Dグラフィックスコンテキスト(描写機能)を取得
		GraphicsContext gc = canvas.getGraphicsContext2D();

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (model.isPaused()) return;

				// ゲーム状態の更新
				model.update();
				
				// 画面描写（ステージ背景とパックマンの描画を分離して実行）
				//Canvasの現在のリアルタイムな横幅・縦幅を取得してビューに渡す
				double currentWidth = canvas.getWidth();
				double currentHeight = canvas.getHeight();
				
				// 新しく統合した draw メソッドを呼び出す
				view.draw(gc, currentWidth, currentHeight);
			}
		};

		// タイマーを始動し、パックマンの世界を動かす
		timer.start();
	}

	// ゲーム停止
	public void stop() {
		if (timer != null)
			timer.stop();
	}
	
	//画面変更Main1へ
	public static void switchToGame1(javafx.stage.Stage stage) {
		try { 
			Main1 App = new Main1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//画面変更Mani2へ
	public static void switchToGame2(javafx.stage.Stage stage) {
		try { 
			Main2 App = new Main2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更Main3へ
	public static void switchToGame3(javafx.stage.Stage stage) {
		try { 
			Main3 App = new Main3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更start→practice
	public static void startToPractice(javafx.stage.Stage stage) {
		try { 
			Practice App = new Practice();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更start→story
	public static void startToStory(javafx.stage.Stage stage) {
		try { 
			Story1 App = new Story1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更start
	public static void switchStart(javafx.stage.Stage stage) {
		try { 
			Start App = new Start();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更Story2
	public static void switchStory2(javafx.stage.Stage stage) {
		try { 
			Story2 App = new Story2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更Story3
	public static void switchStory3(javafx.stage.Stage stage) {
		try { 
			Story3 App = new Story3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//画面変更Story4
	public static void switchStory4(javafx.stage.Stage stage) {
		try { 
			Story4 App = new Story4();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}