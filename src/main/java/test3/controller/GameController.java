package test3.controller;

import Characters.Direction;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import start.Start;
import story.Gameover;
import story.Practice;
import story.Stageclear1;
import story.Story1;
import story.Story2;
import story.Story3;
import story.Story4;
import test.test2.TestMainapp;
import test1.Main1;
import test2.Main2;
import test3.Main3;
import test3.model.MapData;
import test3.view.MapView;

public class GameController {

	// 制御コンポーネント
	private final MapData model; // パックマンの位置やマップ状態を持つデータソース
	private final MapView view; // 描画処理
	private final Canvas canvas; // 描画先キャンバス
	private AnimationTimer timer; // ゲームループ(毎フレーム実行)

	// 画面遷移のためにStageを保持する変数
	private final javafx.stage.Stage stage;

	// ゲームコントローラーの初期化と同時にゲームをスタートする
	public GameController(MapData model, MapView view, Canvas canvas, Scene scene, javafx.stage.Stage stage) {
		this.model = model;
		this.view = view;
		this.canvas = canvas;
		this.stage = stage;

		// キーボードの入力を登録
		attachInput(scene);

		//スマホ用の十字キーコントローラーを画面に表示・適用する
		applyMobileControls(scene, model);

		// メインゲームループ(AnimationTimer)の開始
		startLoop();
	}

	//ステージ画面に十字キーを追加する	スマホ用のメソッド
	public static void applyMobileControls(javafx.scene.Scene gameScene, Object model) {
		if (model == null)
			return;

		// 画面のルートコンテナを取得し、StackPaneで包み込む
		javafx.scene.Parent root = gameScene.getRoot();
		javafx.scene.layout.StackPane baseHolder;
		if (root instanceof javafx.scene.layout.StackPane) {
			baseHolder = (javafx.scene.layout.StackPane) root;
		} else {
			baseHolder = new javafx.scene.layout.StackPane();
			gameScene.setRoot(baseHolder);
			baseHolder.getChildren().add(root);
		}

		// 十字キー（GridPane）を作成
		javafx.scene.layout.GridPane dPad = new javafx.scene.layout.GridPane();
		dPad.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
		dPad.setPadding(new javafx.geometry.Insets(0, 0, 40, 40));
		dPad.setHgap(10);
		dPad.setVgap(10);
		dPad.setStyle("-fx-background-color: transparent;");

		// ボタン作成とスタイル適用
		javafx.scene.control.Button btnUp = new javafx.scene.control.Button("▲");
		javafx.scene.control.Button btnDown = new javafx.scene.control.Button("▼");
		javafx.scene.control.Button btnLeft = new javafx.scene.control.Button("◀");
		javafx.scene.control.Button btnRight = new javafx.scene.control.Button("▶");

		String buttonStyle = "-fx-font-size: 24px; -fx-min-width: 60px; -fx-min-height: 60px; "
				+ "-fx-background-radius: 30px; -fx-background-color: rgba(255, 255, 255, 0.4); -fx-text-fill: white;";

		btnUp.setStyle(buttonStyle);
		btnDown.setStyle(buttonStyle);
		btnLeft.setStyle(buttonStyle);
		btnRight.setStyle(buttonStyle);

		// ★★★ 【王道の修正】 ★★★
		// ボタンがキーボードの「フォーカス」を持つことを完全に禁止する（クリック・タップ専用化）
		btnUp.setFocusTraversable(false);
		btnDown.setFocusTraversable(false);
		btnLeft.setFocusTraversable(false);
		btnRight.setFocusTraversable(false);

		dPad.add(btnUp, 1, 0);
		dPad.add(btnLeft, 0, 1);
		dPad.add(btnRight, 2, 1);
		dPad.add(btnDown, 1, 2);

		// ★ リフレクションを使って、どのパッケージの MapData からでも安全にメソッドを呼び出す共通処理
		java.util.function.Consumer<Characters.Direction> sendDirection = (dir) -> {
			try {
				// 一時停止中かチェック (isPaused メソッドを実行)
				java.lang.reflect.Method isPausedMethod = model.getClass().getMethod("isPaused");
				boolean isPaused = (boolean) isPausedMethod.invoke(model);

				if (!isPaused) {
					// 方向をセット (setNextDirection メソッドを実行)
					java.lang.reflect.Method setDirMethod = model.getClass().getMethod("setNextDirection",
							Characters.Direction.class);
					setDirMethod.invoke(model, dir);
				}
			} catch (Exception ex) {
				ex.printStackTrace(); // メソッド名が違ったりした場合のエラーログ
			}
		};

		// タップイベント（上記の共通処理を呼び出す）
		btnUp.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.UP));
		btnDown.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.DOWN));
		btnLeft.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.LEFT));
		btnRight.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.RIGHT));

		// 最前面のレイヤーとして十字キーを追加
		baseHolder.getChildren().add(dPad);
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
			if (code == KeyCode.W || code == KeyCode.UP)
				model.setNextDirection(Direction.UP);
			if (code == KeyCode.S || code == KeyCode.DOWN)
				model.setNextDirection(Direction.DOWN);
			if (code == KeyCode.A || code == KeyCode.LEFT)
				model.setNextDirection(Direction.LEFT);
			if (code == KeyCode.D || code == KeyCode.RIGHT)
				model.setNextDirection(Direction.RIGHT);
		});
	}

	// メインゲームループ
	private void startLoop() {
		// キャンバスの2Dグラフィックスコンテキスト(描写機能)を取得
		GraphicsContext gc = canvas.getGraphicsContext2D();

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (model.isPaused())
					return;

				// ゲーム状態の更新
				model.update();

				// ★追加：敵に捕まった（ゲームオーバー）かチェック
				// ※model側に isGameOver() という判定メソッドがある前提です
				if (model.isGameOver()) {
					stop(); // ゲームループ（タイマー）を止める
					System.out.println("💀 敵に捕まりました...ゲームオーバー画面へ遷移します。");

					switchToGameover(stage); // ゲームオーバー画面へ遷移
					return; // これ以降の処理はスキップ
				}

				// すべてのドットを食べ終えたかチェック
				if (model.isCleared()) {
					timer.stop(); // ゲームループ（タイマー）を止める
					System.out.println("ステージクリア！次の画面へ遷移します。");

					// ★現在の最終スコアを取得する
					int finalScore = 0;
					if (model.getSengoku() != null) {
						finalScore = model.getSengoku().getScore();
					}

					// クリア画面（Stageclear1）に遷移させる
					switchToStageclear1(stage, finalScore);

					// もし直接ステー12のゲーム画面にいかせたい場合はこちら↓
					// switchToGame1(stage);

					return; // クリアしたのでこれ以降の描画処理はスキップ

				}

				// 画面描写（ステージ背景とパックマンの描画を分離して実行）
				//Canvasの現在のリアルタイムな横幅・縦幅を取得してビューに渡す
				double currentWidth = canvas.getWidth();
				double currentHeight = canvas.getHeight();

				// 新しく統合した draw メソッドを呼び出す
				view.draw(gc, currentWidth, currentHeight);
			}
		};

		// タイマーを始動
		timer.start();
	}

	// ゲーム停止
	public void stop() {
		if (timer != null)
			timer.stop();
	}

	// Stageclear1画面へ変更するためのメソッド（引数に score を追加）
	public static void switchToStageclear1(javafx.stage.Stage stage, int score) {

		try {
			Stageclear1 App = new Stageclear1();
			App.setScore(score); // 受け取った score を確実に引き渡す
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ★追加：Gameover画面へ変更するためのメソッド
	public static void switchToGameover(javafx.stage.Stage stage) {
		try {
			Gameover App = new Gameover();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
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