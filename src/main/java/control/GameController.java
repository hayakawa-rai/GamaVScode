package control;

import Characters.Direction;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import start.Start;

public class GameController {

	private final Object model;
	private final Object view;
	private final Canvas canvas;
	private AnimationTimer timer;
	private javafx.scene.layout.VBox pauseLayer;
	private final javafx.stage.Stage stage;
	private final int stageNumber;
	private final boolean isPractice;
	private boolean isTransitioning = false; // 💡 二重遷移を防ぐフラグ

	public GameController(Object model, Object view, Canvas canvas, Scene scene, javafx.stage.Stage stage,
			int stageNumber, boolean isPractice) {
		this.model = model;
		this.view = view;
		this.canvas = canvas;
		this.stage = stage;
		this.stageNumber = stageNumber;
		this.isPractice = isPractice;

		playStageBgm(stageNumber);
		attachInput(scene);
		applyMobileControls(scene, model);
		startLoop();
	}

	public static void applyMobileControls(javafx.scene.Scene gameScene, Object model) {
		if (model == null)
			return;

		javafx.scene.Parent root = gameScene.getRoot();
		javafx.scene.layout.StackPane baseHolder;
		if (root instanceof javafx.scene.layout.StackPane) {
			baseHolder = (javafx.scene.layout.StackPane) root;
		} else {
			baseHolder = new javafx.scene.layout.StackPane();
			gameScene.setRoot(baseHolder);
			baseHolder.getChildren().add(root);
		}

		javafx.scene.layout.GridPane dPad = new javafx.scene.layout.GridPane();
		dPad.setAlignment(javafx.geometry.Pos.BOTTOM_LEFT);
		dPad.setPadding(new javafx.geometry.Insets(0, 0, 40, 40));
		dPad.setHgap(10);
		dPad.setVgap(10);
		dPad.setStyle("-fx-background-color: transparent;");
		dPad.setPickOnBounds(false);

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

		java.util.function.Consumer<Characters.Direction> sendDirection = (dir) -> {
			try {
				java.lang.reflect.Method isPausedMethod = model.getClass().getMethod("isPaused");
				boolean isPaused = (boolean) isPausedMethod.invoke(model);

				if (!isPaused) {
					java.lang.reflect.Method setDirMethod = model.getClass().getMethod("setNextDirection",
							Characters.Direction.class);
					setDirMethod.invoke(model, dir);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		};

		btnUp.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.UP));
		btnDown.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.DOWN));
		btnLeft.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.LEFT));
		btnRight.setOnMousePressed(e -> sendDirection.accept(Characters.Direction.RIGHT));

		baseHolder.getChildren().add(dPad);

		try {
			if (root instanceof javafx.scene.layout.Pane) {
				javafx.scene.layout.Pane rootPane = (javafx.scene.layout.Pane) root;

				for (javafx.scene.Node node : rootPane.getChildren()) {
					if (node instanceof javafx.scene.control.Button
							&& "タイトルへ戻る".equals(((javafx.scene.control.Button) node).getText())) {

						javafx.application.Platform.runLater(() -> {
							rootPane.getChildren().remove(node);
							baseHolder.getChildren().add(node);
						});
						break;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void attachInput(Scene scene) {
		scene.setOnKeyPressed(e -> {
			try {
				KeyCode code = e.getCode();

				java.lang.reflect.Method togglePauseMethod = model.getClass().getMethod("togglePause");
				java.lang.reflect.Method isPausedMethod = model.getClass().getMethod("isPaused");
				java.lang.reflect.Method setNextDirectionMethod = model.getClass().getMethod("setNextDirection",
						Characters.Direction.class);

				if (code == KeyCode.P) {
					togglePauseMethod.invoke(model);

					if (pauseLayer != null) {
						boolean isPaused = (boolean) isPausedMethod.invoke(model);
						if (isPaused) {
							pauseLayer.setMouseTransparent(false);
							pauseLayer.setVisible(true);
							pauseLayer.requestFocus();
						} else {
							pauseLayer.setMouseTransparent(true);
							pauseLayer.setVisible(false);
							canvas.requestFocus();
						}
					}
					return;
				}
				
				if (code == KeyCode.C) {
					try {
						java.lang.reflect.Method forceClearMethod = model.getClass().getMethod("forceStageClear");
						forceClearMethod.invoke(model);
					} catch (NoSuchMethodException ex) {
						System.out.println("⚠️ このモデルには forceStageClear メソッドがありません");
					}
					return;
				}

				if ((boolean) isPausedMethod.invoke(model))
					return;

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

	private void startLoop() {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		if (canvas.getScene() != null) {
			canvas.widthProperty().unbind();
			canvas.heightProperty().unbind();
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

					final java.lang.reflect.Method respawnDotsMethod = model.getClass().getMethod("respawnDots");

					boolean isPaused = (boolean) isPausedMethod.invoke(model);

					if (!isPaused) {
						updateMethod.invoke(model);

						// 💀 ゲームオーバー判定
						if ((boolean) isGameOverMethod.invoke(model)) {
							stop();
							if (isTransitioning)
								return;
							isTransitioning = true; // 💡 二重処理防止

							start.Bgm.stopBGM();
							System.out.println("💀 ゲームオーバー");

							int finalScore = 0;
							try {
								Object syujinkou = getsyujinkouMethod.invoke(model);
								if (syujinkou != null) {
									java.lang.reflect.Method getScoreMethod = syujinkou.getClass()
											.getMethod("getScore");
									finalScore = (int) getScoreMethod.invoke(syujinkou);
								}
							} catch (Exception e) {
							}

							final int scoreToSend = finalScore;
							// 💡 【超重要】ループ処理の『直後』に元の画面遷移を安全に実行する
							javafx.application.Platform.runLater(() -> {
								switchToGameover(stage, stageNumber, isPractice, scoreToSend);
							});
							return;
						}

						// 🏁 ステージクリア判定
						if ((boolean) isClearedMethod.invoke(model)) {
							if (isPractice) {
								respawnDotsMethod.invoke(model);
							} else {
								stop();
								if (isTransitioning)
									return;
								isTransitioning = true; // 💡 二重処理防止

								start.Bgm.stopBGM();
								System.out.println("🏁 ステージクリア！");

								int finalScore = 0;
								Object syujinkou = getsyujinkouMethod.invoke(model);
								if (syujinkou != null) {
									java.lang.reflect.Method getScoreMethod = syujinkou.getClass()
											.getMethod("getScore");
									finalScore = (int) getScoreMethod.invoke(syujinkou);
								}

								final int scoreToSend = finalScore;
								// 💡 【超重要】ループ処理の『直後』に元の画面遷移を安全に実行する
								javafx.application.Platform.runLater(() -> {
									switch (stageNumber) {
									case 1:
										switchToStageclear1(stage, scoreToSend);
										break;
									case 2:
										switchToStageclear2(stage, scoreToSend);
										break;
									case 3:
										switchToStageclear3(stage, scoreToSend);
										break;
									default:
										switchToStageclear1(stage, scoreToSend);
										break;
									}
								});
								return;
							}
						}
					}

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

		timer.start();
	}

	public void forceBackToTitle() {
		try {
			stop();
			start.Bgm.stopBGM();
			switchStart(this.stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void playStageBgm(int stageNumber) {
		start.Bgm.playStageBGM(stageNumber);
	}

	public void setPauseLayer(javafx.scene.layout.VBox pauseLayer) {
		this.pauseLayer = pauseLayer;

		if (pauseLayer != null && this.canvas != null && this.canvas.getScene() != null) {
			javafx.scene.Parent root = this.canvas.getScene().getRoot();

			if (root instanceof javafx.scene.layout.StackPane) {
				javafx.scene.layout.StackPane trueRoot = (javafx.scene.layout.StackPane) root;

				if (pauseLayer.getParent() instanceof javafx.scene.layout.Pane) {
					((javafx.scene.layout.Pane) pauseLayer.getParent()).getChildren().remove(pauseLayer);
				}

				javafx.application.Platform.runLater(() -> {
					if (!trueRoot.getChildren().contains(pauseLayer)) {
						trueRoot.getChildren().add(pauseLayer);
					}
				});
			}
		}
	}

	public void stop() {
		if (timer != null)
			timer.stop();
	}

	// ======================================================
	// 💡 元通りの完璧な挙動をする画面遷移（JProのバグを回避するタイミングに修正）
	// ======================================================

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
			story.Practice App = new story.Practice();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startToStory(javafx.stage.Stage stage) {
		try {
			story.Story1 App = new story.Story1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchStory2(javafx.stage.Stage stage) {
		try {
			story.Story2 App = new story.Story2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchStory3(javafx.stage.Stage stage) {
		try {
			story.Story3 App = new story.Story3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchStory4(javafx.stage.Stage stage) {
		try {
			story.Story4 App = new story.Story4();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchStoryClear(javafx.stage.Stage stage) {
		try {
			story.Storyclear App = new story.Storyclear();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToGame1(javafx.stage.Stage stage) {
		try {
			test1.Main1 App = new test1.Main1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToGame2(javafx.stage.Stage stage) {
		try {
			test2.Main2 App = new test2.Main2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToGame3(javafx.stage.Stage stage) {
		try {
			test3.Main3 App = new test3.Main3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToStageclear1(javafx.stage.Stage stage, int score) {
		try {
			story.Stageclear1 App = new story.Stageclear1();
			try {
				java.lang.reflect.Method setScoreMethod = App.getClass().getMethod("setScore", int.class);
				setScoreMethod.invoke(App, score);
			} catch (Exception e) {
			}
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToStageclear2(javafx.stage.Stage stage, int score) {
		try {
			story.Stageclear2 App = new story.Stageclear2();
			try {
				java.lang.reflect.Method setScoreMethod = App.getClass().getMethod("setScore", int.class);
				setScoreMethod.invoke(App, score);
			} catch (Exception e) {
			}
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToStageclear3(javafx.stage.Stage stage, int score) {
		try {
			story.Stageclear3 App = new story.Stageclear3();
			try {
				java.lang.reflect.Method setScoreMethod = App.getClass().getMethod("setScore", int.class);
				setScoreMethod.invoke(App, score);
			} catch (Exception e) {
			}
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToPracticeGame1(javafx.stage.Stage stage) {
		try {
			test1.PracticeMain1 App = new test1.PracticeMain1();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToPracticeGame2(javafx.stage.Stage stage) {
		try {
			test2.PracticeMain2 App = new test2.PracticeMain2();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToPracticeGame3(javafx.stage.Stage stage) {
		try {
			test3.PracticeMain3 App = new test3.PracticeMain3();
			App.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void switchToGameover(javafx.stage.Stage stage, int stageNum, boolean isPractice, int score) {
		try {
			Runnable retryAction;

			switch (stageNum) {
			case 1:
				retryAction = isPractice ? () -> switchToPracticeGame1(stage) : () -> switchToGame1(stage);
				break;
			case 2:
				retryAction = isPractice ? () -> switchToPracticeGame2(stage) : () -> switchToGame2(stage);
				break;
			case 3:
				retryAction = isPractice ? () -> switchToPracticeGame3(stage) : () -> switchToGame3(stage);
				break;
			default:
				retryAction = () -> switchToGame1(stage);
				break;
			}

			javafx.scene.Scene gameoverScene = story.Gameover.create(stage, retryAction, score);
			if (gameoverScene != null) {
				stage.setScene(gameoverScene);
			}
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}