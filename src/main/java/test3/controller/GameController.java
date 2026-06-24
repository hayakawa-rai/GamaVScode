package test3.controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import test3.Main3;
import test3.model.MapData;
import test3.view.MapView;

public class GameController {

	//制御コンポーネント
	private final MapData model; //パックマンの位置やマップ状態を持つデータソース
	private final MapView view; //描画処理
	private final Canvas canvas; //描画先キャンバス
	private final Scene scene;  //Sceneを使いまわせるようにフィールドを保持する
	private AnimationTimer timer; //ゲームループ(毎フレーム実行)

	//ゲームコントローラーの初期化と同時にゲームをスタートする
	public GameController(MapData model, MapView view, Canvas canvas, Scene scene) {
		this.model = model;
		this.view = view;
		this.canvas = canvas;
		this.scene = scene;

		//キーボードの入力を登録
		attachInput(scene);

		// ★ 起動時にステージ2の見た目をCanvasに適用する
		changeStage(3);

		//メインゲームループ(AnimationTimer)の開始
		startLoop();

	}

	// ★ ステージの見た目をCSSクラスで切り替えるメソッドを追加
	public void changeStage(int stageNum) {
		// ★ Canvasからではなく、画面全体のルート（BorderPane）からクラスを削除・追加する
	    javafx.scene.Parent root = scene.getRoot();
	    root.getStyleClass().removeAll("stage1", "stage2", "stage3");
	    root.getStyleClass().add("stage" + stageNum);
	}

	//キーボード入力処理
	private void attachInput(Scene scene) {
		scene.setOnKeyPressed(e -> {
			KeyCode code = e.getCode();

			//Pキーでゲームを一時停止・再開
			if (code == KeyCode.P) {
				model.togglePause();
				return;
			}

			//一時停止中はパックマンの方向転換入力を受け付けない
			if (model.isPaused())
				return;

			//パックマンの方向入力(先行入力・予約システム)
			if (code == KeyCode.W || code == KeyCode.UP)
				model.setNextDirection(0, -1); //上方向を予約

			if (code == KeyCode.S || code == KeyCode.DOWN)
				model.setNextDirection(0, 1); //下方向を予約

			if (code == KeyCode.A || code == KeyCode.LEFT)
				model.setNextDirection(-1, 0); //左方向を予約

			if (code == KeyCode.D || code == KeyCode.RIGHT)
				model.setNextDirection(1, 0); //右方向を予約

		});

	}

	//メインゲームループ
	private void startLoop() {
		//キャンバスの2Dグラフィックスコンテキスト(描写機能)を取得
		GraphicsContext gc = canvas.getGraphicsContext2D();

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {

				//ゲーム状態更新
				model.updatePacman(); //位置・衝突・ワープ処理
				model.updateMouth(); //口のアニメーション

				//デバックログ
				System.out.println("LOOP");

				System.out.println(
						"dir=" + model.getDirX() + "," + model.getDirY()
								+ " pac=" + model.getPacX() + "," + model.getPacY());

				//画面描写
				view.draw(gc);
			}
		};

		//タイマーを指導し、パックマンの世界を動かす
		timer.start();
	}

	//ゲーム停止
	public void stop() {

		if (timer != null)
			timer.stop();

	}
	//画面変更
		public static void switchToGame(javafx.stage.Stage stage) {
			try { 
				Main3 App = new Main3();
				App.start(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

}
