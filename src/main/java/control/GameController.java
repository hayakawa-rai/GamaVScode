package control;

import Characters.Direction;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import start.Start;
import story.Stageclear1;
import story.Stageclear2;
import story.Stageclear3;
import story.Story1;
import story.Story2;
import story.Story3;
import story.Story4;
import test1.Main1;
import test1.PracticeMain1;
import test2.Main2;
import test2.PracticeMain2;
import test3.Main3;
import test3.PracticeMain3;

public class GameController {

	private final Object model; // パックマンの位置やマップ状態を持つデータソース
	private final Object view; // 描画処理
	private final Canvas canvas; // 描画先キャンバス
	private AnimationTimer timer; // ゲームループ(毎フレーム実行)
	private javafx.scene.layout.VBox pauseLayer;

	// 画面遷移のためにStageを保持する変数
	private final javafx.stage.Stage stage;

	// 現在のステージ番号（1〜3）を記憶する変数
	private final int stageNumber;
	private final boolean isPractice;

	public GameController(Object model, Object view, Canvas canvas, Scene scene, javafx.stage.Stage stage,
			int stageNumber, boolean isPractice) {
		this.model = model;
		this.view = view;
		this.canvas = canvas;
		this.stage = stage;
		this.stageNumber = stageNumber; // ⭐ ステージ番号を記憶
		this.isPractice = isPractice;

		playStageBgm(stageNumber); // ★追加
		
		// キーボードの入力を登録
		attachInput(scene);

		// スマホ用の十字キーコントローラーを画面に適用
		applyMobileControls(scene, model);

		// メインゲームループ(AnimationTimer)の開始
		startLoop();
	}

	// ステージ画面に十字キーを追加する スマホ用のメソッド
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

		dPad.setPickOnBounds(false);

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

		try {
			// rootがPaneクラス（またはその子クラス）の場合だけキャストして処理する
			if (root instanceof javafx.scene.layout.Pane) {
				javafx.scene.layout.Pane rootPane = (javafx.scene.layout.Pane) root;

				// rootPaneから「タイトルへ戻る」ボタンを探す
				for (javafx.scene.Node node : rootPane.getChildren()) {
					if (node instanceof javafx.scene.control.Button
							&& "タイトルへ戻る".equals(((javafx.scene.control.Button) node).getText())) {

						// 発見したら、安全に最前面のベースホルダー（baseHolder）へ引っ越しさせる
						javafx.application.Platform.runLater(() -> {
							rootPane.getChildren().remove(node); // 元の背景レイヤーから削除
							baseHolder.getChildren().add(node); // 最前面のレイヤーへ追加！
						});
						break; // 見つかったのでループを抜ける
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace(); // 万が一エラーが出た場合はログに出力
		}
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
					
					if (pauseLayer != null) {
						boolean isPaused = (boolean) isPausedMethod.invoke(model);
						if (isPaused) {
							pauseLayer.setMouseTransparent(false); // ★クリックできるようにする
							pauseLayer.setVisible(true);           // 十字キーやスコアを完全に覆い隠して表示
							pauseLayer.requestFocus();             // ボタンをクリック・選択可能にする
						} else {
							pauseLayer.setMouseTransparent(true);  // ★クリックを完全にスルーさせる（透明化）
							pauseLayer.setVisible(false);          // ポーズ解除時はレイヤーを隠す
							canvas.requestFocus();                 // 操作権をゲーム（Canvas）側に戻す
						}
					}
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

		if (canvas.getScene() != null) {
			// すでに存在しているかもしれない古いバインドを念のため解除
			canvas.widthProperty().unbind();
			canvas.heightProperty().unbind();

			// 💡 ウィンドウ（Scene）の幅・高さとCanvasの幅・高さを完全にバインド（同期）させる
			canvas.widthProperty().bind(canvas.getScene().widthProperty());
			canvas.heightProperty().bind(canvas.getScene().heightProperty());
		}

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				try {
					java.lang.reflect.Method isPausedMethod = model.getClass().getMethod("isPaused");
					java.lang.reflect.Method updateMethod = model.getClass().getMethod("update");
					java.lang.reflect.Method isGameOverMethod = model.getClass().getMethod("isGameOver");
					java.lang.reflect.Method isClearedMethod = model.getClass().getMethod("isCleared");
					java.lang.reflect.Method getsyujinkouMethod = model.getClass().getMethod("getsyujinkou");

					// 💡 練習モード用の復活メソッドを事前に取得
					final java.lang.reflect.Method respawnDotsMethod = model.getClass().getMethod("respawnDots");

					// 💡 一時停止フラグをここで変数に保存
					boolean isPaused = (boolean) isPausedMethod.invoke(model);

					// 一時停止中でない（通常プレイ中）のときだけ、移動やゲームクリア判定を行う
					if (!isPaused) {
						// ゲーム状態の更新
						updateMethod.invoke(model);

						// 敵に捕まった（ゲームオーバー）かチェック
						if ((boolean) isGameOverMethod.invoke(model)) {
							stop();
							start.Bgm.stopBGM(); // ★追加
							System.out.println("💀 敵に捕まりました...ゲームオーバー画面へ遷移します。");

							// スコアを安全に取得する処理
							int finalScore = 0;
							try {
								Object syujinkou = getsyujinkouMethod.invoke(model);
								if (syujinkou != null) {
									java.lang.reflect.Method getScoreMethod = syujinkou.getClass().getMethod("getScore");
									finalScore = (int) getScoreMethod.invoke(syujinkou);
								}
							} catch (Exception e) {
								// メソッドがない場合は0のまま進む
							}

							// 綺麗に一本化したゲームオーバー遷移を呼び出す（スコアも引き渡す）
							switchToGameover(stage, stageNumber, isPractice,finalScore);
							return;
						}

						// すべてのドットを食べ終えたかチェック
						if ((boolean) isClearedMethod.invoke(model)) {
							if (isPractice) {
								// 💡 練習モード：画面遷移せず、エサを復活させてループを継続
								respawnDotsMethod.invoke(model);
							} else {
								// 💡 本番モード：タイマーを止めて各ステージのクリア画面へ遷移
								stop();
								start.Bgm.stopBGM(); // ★追加
								System.out.println("🏁 本番モード：ステージクリア！次の画面へ。");

								int finalScore = 0;
								Object syujinkou = getsyujinkouMethod.invoke(model);
								if (syujinkou != null) {
									java.lang.reflect.Method getScoreMethod = syujinkou.getClass().getMethod("getScore");
									finalScore = (int) getScoreMethod.invoke(syujinkou);
								}

								switch (stageNumber) {
								case 1:
									switchToStageclear1(stage, finalScore);
									break;
								case 2:
									switchToStageclear2(stage, finalScore);
									break;
								case 3:
									switchToStageclear3(stage, finalScore);
									break;
								default:
									switchToStageclear1(stage, finalScore);
									break;
								}
								return;
							}
						}
					}

					// 💡 描画処理（draw）は if (!isPaused) の外側に置くことで、一時停止中も常に実行される！
					double currentWidth = canvas.getWidth();
					double currentHeight = canvas.getHeight();

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


	// 既存の処理を一切壊さず、外部（MapView）から安全にループを停止させてタイトルへ戻るための専用メソッド
	public void forceBackToTitle() {
		try {
			System.out.println("① forceBackToTitle開始");

			stop();
			start.Bgm.stopBGM(); // ★追加
			System.out.println("② timer停止");

			switchStart(this.stage);
			System.out.println("③ switchStart完了");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//BGM処理
	private void playStageBgm(int stageNumber) {
	    start.Bgm.playStageBGM(stageNumber); // ★変更：Bgm側に処理を委譲
	}

	
	public void setPauseLayer(javafx.scene.layout.VBox pauseLayer) {
		this.pauseLayer = pauseLayer;
		
		if (pauseLayer != null && this.canvas != null && this.canvas.getScene() != null) {
			javafx.scene.Parent root = this.canvas.getScene().getRoot();
			
			// 十字キー自動生成によって作られた、本物の最前面 StackPane を捕まえる
			if (root instanceof javafx.scene.layout.StackPane) {
				javafx.scene.layout.StackPane trueRoot = (javafx.scene.layout.StackPane) root;
				
				// いったん古い親（Main1のroot）からポーズ画面を引き剥がす
				if (pauseLayer.getParent() instanceof javafx.scene.layout.Pane) {
					((javafx.scene.layout.Pane) pauseLayer.getParent()).getChildren().remove(pauseLayer);
				}
				
				// 十字キーよりもさらに上（本当の最前面）にポーズ画面を配置する！
				javafx.application.Platform.runLater(() -> {
					if (!trueRoot.getChildren().contains(pauseLayer)) {
						trueRoot.getChildren().add(pauseLayer);
					}
				});
			}
		}
	}
	
	// ゲーム停止
	public void stop() {
		if (timer != null)
			timer.stop();
	}

	// === 画面遷移用のメソッド群 ===

	// 画面変更start
	public static void switchStart(javafx.stage.Stage stage) {
		try {
			Start App = new Start();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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

	// 画面変更start→story
	public static void startToStory(javafx.stage.Stage stage) {
		try {
			Story1 App = new Story1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更Story2
	public static void switchStory2(javafx.stage.Stage stage) {
		try {
			Story2 App = new Story2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更Story3
	public static void switchStory3(javafx.stage.Stage stage) {
		try {
			Story3 App = new Story3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更Story4
	public static void switchStory4(javafx.stage.Stage stage) {
		try {
			Story4 App = new Story4();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 画面変更Main1へ
	public static void switchToGame1(javafx.stage.Stage stage) {
		try {
	        Main1 App = new Main1();
	        App.start(stage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// 画面変更Main2へ
	public static void switchToGame2(javafx.stage.Stage stage) {
		try {
			Main2 App = new Main2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更Main3へ
	public static void switchToGame3(javafx.stage.Stage stage) {
		try {
			Main3 App = new Main3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	// Stageclear2画面へ変更するためのメソッド（引数に score を追加）
	public static void switchToStageclear2(javafx.stage.Stage stage, int score) {
		try {
			Stageclear2 App = new Stageclear2();
			App.setScore(score); // 受け取った score を確実に引き渡す
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Stageclear3画面へ変更するためのメソッド（引数に score を追加）
	public static void switchToStageclear3(javafx.stage.Stage stage, int score) {
		try {
			Stageclear3 App = new Stageclear3();
			App.setScore(score);
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更PracticeMain1へ
	public static void switchToPracticeGame1(javafx.stage.Stage stage) {
		try {
			PracticeMain1 App = new PracticeMain1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更PracticeMani2へ
	public static void switchToPracticeGame2(javafx.stage.Stage stage) {
		try {
			PracticeMain2 App = new PracticeMain2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画面変更PracticeMain3へ
	public static void switchToPracticeGame3(javafx.stage.Stage stage) {
		try {
			PracticeMain3 App = new PracticeMain3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Gameover画面へ変更するためのメソッド（引数4つ版に綺麗に統一！）
	public static void switchToGameover(javafx.stage.Stage stage, int stageNum, boolean isPractice, int score) {
		try {
			Runnable retryAction;

			// ステージ番号 と 練習モードフラグ に応じて、リトライ時に起動するクラスを完全に切り替える
			switch (stageNum) {
			case 1:
				if (isPractice) {
					retryAction = () -> test1.PracticeMain1.createAndStart(stage); // 練習モード1へ
				} else {
					retryAction = () -> test1.Main1.createAndStart(stage); // 本番モード1へ
				}
				break;
			case 2:
				if (isPractice) {
					retryAction = () -> test2.PracticeMain2.createAndStart(stage); // 練習モード2へ
				} else {
					retryAction = () -> test2.Main2.createAndStart(stage); // 本番モード2へ
				}
				break;
			case 3:
				if (isPractice) {
					retryAction = () -> test3.PracticeMain3.createAndStart(stage); // 練習モード3へ
				} else {
					retryAction = () -> test3.Main3.createAndStart(stage); // 本番モード3へ
				}
				break;
			default:
				retryAction = () -> test1.Main1.createAndStart(stage);
				break;
			}

			// Gameoverクラスに、stageと組み立てた適切なリトライ処理、そしてスコアを渡す！
			stage.setScene(story.Gameover.create(stage, retryAction, score));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}