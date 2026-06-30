package control;

import Characters.Direction;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import story.Gameover;
import test1.PracticeMain1;
import test2.PracticeMain2;
import test3.PracticeMain3;

public class PracticeGameController {

	// 制御コンポーネント
	// 🌟 依存を無くすため、model と view は特定のパッケージではなく Object型 で保持します
	private final Object model; // パックマンの位置やマップ状態を持つデータソース
	private final Object view; // 描画処理
	private final Canvas canvas; // 描画先キャンバス
	private AnimationTimer timer; // ゲームループ(毎フレーム実行)

	// 画面遷移のためにStageを保持する変数
	private final javafx.stage.Stage stage;

	// ⭐ 追加：現在のステージ番号（1〜3）を記憶する変数
	private final int stageNumber;

	// ⭐ コンストラクタの最後に「int stageNumber」を追加しました
	public PracticeGameController(Object model, Object view, Canvas canvas, Scene scene, javafx.stage.Stage stage,
			int stageNumber) {
		this.model = model;
		this.view = view;
		this.canvas = canvas;
		this.stage = stage;
		this.stageNumber = stageNumber; // ⭐ ステージ番号を記憶

		// キーボードの入力を登録
		attachInput(scene);

		// ⭐ 追加：スマホ用の十字キーコントローラーを画面に適用
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

		btnUp.setFocusTraversable(false);
		btnDown.setFocusTraversable(false);
		btnLeft.setFocusTraversable(false);
		btnRight.setFocusTraversable(false);

		dPad.add(btnUp, 1, 0);
		dPad.add(btnLeft, 0, 1);
		dPad.add(btnRight, 2, 1);
		dPad.add(btnDown, 1, 2);

		//リフレクションを使って、どのパッケージの MapData からでも安全にメソッドを呼び出す共通処理
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


	// キーボード入力処理（🌟リフレクション化により、どのステージのModelでも動作可能）
	private void attachInput(Scene scene) {
		scene.setOnKeyPressed(e -> {
			try {
				KeyCode code = e.getCode();

				java.lang.reflect.Method togglePauseMethod = model.getClass().getMethod("togglePause");
				java.lang.reflect.Method isPausedMethod = model.getClass().getMethod("isPaused");
				java.lang.reflect.Method setNextDirectionMethod = model.getClass().getMethod("setNextDirection",
						Characters.Direction.class);

				// Pキーでゲームを一時停止・再開
				if (code == KeyCode.P) {
					togglePauseMethod.invoke(model);
					return;
				}

				// 一時停止中は入力を受け付けない
				if ((boolean) isPausedMethod.invoke(model))
					return;

				// 矢印キー操作の委譲
				if (code == KeyCode.W || code == KeyCode.UP)
					setNextDirectionMethod.invoke(model, Direction.UP);
				if (code == KeyCode.S || code == KeyCode.DOWN)
					setNextDirectionMethod.invoke(model, Direction.DOWN);
				if (code == KeyCode.A || code == KeyCode.LEFT)
					setNextDirectionMethod.invoke(model, Direction.LEFT);
				if (code == KeyCode.D || code == KeyCode.RIGHT)
					setNextDirectionMethod.invoke(model, Direction.RIGHT);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	// メインゲームループ
	private void startLoop() {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				try {
					java.lang.reflect.Method isPausedMethod = model.getClass().getMethod("isPaused");
					java.lang.reflect.Method updateMethod = model.getClass().getMethod("update");
					java.lang.reflect.Method isGameOverMethod = model.getClass().getMethod("isGameOver");
					java.lang.reflect.Method isClearedMethod = model.getClass().getMethod("isCleared");
					java.lang.reflect.Method getSengokuMethod = model.getClass().getMethod("getSengoku");

					if ((boolean) isPausedMethod.invoke(model))
						return;

					// ゲーム状態の更新
					updateMethod.invoke(model);

					// 敵に捕まった（ゲームオーバー）かチェック
					if ((boolean) isGameOverMethod.invoke(model)) {
						stop();
						System.out.println("💀 敵に捕まりました...ゲームオーバー画面へ遷移します。");
						switchToGameover(stage);
						return;
					}

					// すべてのドットを食べ終えたかチェック
					if ((boolean) isClearedMethod.invoke(model)) {
						stop();
						System.out.println("ステージクリア！次の画面へ遷移します。");

						int finalScore = 0;
						Object sengoku = getSengokuMethod.invoke(model);
						if (sengoku != null) {
							java.lang.reflect.Method getScoreMethod = sengoku.getClass().getMethod("getScore");
							finalScore = (int) getScoreMethod.invoke(sengoku);
						}

						return;
					}

					double currentWidth = canvas.getWidth();
					double currentHeight = canvas.getHeight();

					// 🌟 どのパッケージの MapView からでも引数3つの draw メソッドを安全に呼び出す
					java.lang.reflect.Method drawMethod = view.getClass().getMethod("draw", GraphicsContext.class,
							double.class, double.class);
					drawMethod.invoke(view, gc, currentWidth, currentHeight);

				} catch (Exception ex) {
					ex.printStackTrace();
					stop();
				}
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

	// === 画面遷移用のメソッド群 ===


	public static void switchToPractice(javafx.stage.Stage stage) {
		try {
			// practiceクラスのインスタンスを作る
			story.Practice practiceScreen = new story.Practice();
			// ウィンドウの権利(stage)を渡して、練習モード画面を起動・上書きする！
			practiceScreen.start(stage);
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

	//画面変更PracticeMain1へ
	public static void switchToPracticeGame1(javafx.stage.Stage stage) {
		try {
			PracticeMain1 App = new PracticeMain1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//画面変更PracticeMani2へ
	public static void switchToPracticeGame2(javafx.stage.Stage stage) {
		try {
			PracticeMain2 App = new PracticeMain2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//画面変更PracticeMain3へ
	public static void switchToPracticeGame3(javafx.stage.Stage stage) {
		try {
			PracticeMain3 App = new PracticeMain3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}