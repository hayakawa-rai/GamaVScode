package start;

import control.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.ResponsiveUtil;

public class Start extends Application {
	private AnimationTimer timer;
	private AudioClip clickSound;
	private PauseTransition pause;

	private void cleanup() {

		// 背景アニメーション停止
		if (timer != null) {
			timer.stop();
			timer = null;
		}

		// 遅延処理停止
		if (pause != null) {
			pause.stop();
			pause = null;
		}

		// 効果音停止
		if (clickSound != null) {
			clickSound.stop();
			clickSound = null;
		}

		// BGM停止
		Bgm.stopBGM();
	}

	@Override
	public void start(Stage stage) {
		try {
			// ファイルをストリームとして読み込む
			var fontStream = getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf");

			if (fontStream == null) {
				System.err.println("【警告】フォントファイルが見つかりません。パスを確認してください。");
			} else {
				Font loadedFont = Font.loadFont(fontStream, 20);
				if (loadedFont == null) {
					System.err.println("【警告】フォントファイルの読み込みに失敗しました（ファイルが壊れているか形式が違います）");
				} else {
					System.out.println("【成功】フォントを読み込みました。名前: " + loadedFont.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Font.loadFont(getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf"), 20);
		} catch (Exception e) {
			System.err.println("フォントの読み込みに失敗しました: " + e.getMessage());
		}

		
		Image bgImage = new Image(
				getClass().getResource("/picture/background.png").toExternalForm());

		double bgWidth = bgImage.getWidth();
		double bgHeight = bgImage.getHeight();
		Pane bgPane = new Pane();
		final double[] scrollX = { 0 };

		// アニメーション
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				scrollX[0] -= 1;
				if (scrollX[0] <= -bgWidth) {
					scrollX[0] = 0;
				}

				ImagePattern pattern = new ImagePattern(
						bgImage,
						scrollX[0], 0,
						bgWidth, bgHeight,
						false
				);

				bgPane.setBackground(new Background(
						new BackgroundFill(pattern, null, null)));
			}
		};


		StackPane root = new StackPane();
		
		VBox ui = new VBox();
		ui.setSpacing(20);
		ui.setAlignment(Pos.CENTER);

		Image image = new Image(getClass().getResource("/picture/title.png").toExternalForm());
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);

		VBox buttonBox = new VBox();
		buttonBox.setSpacing(20);
		buttonBox.setAlignment(Pos.CENTER);
		
		Bgm.playBGM("/music/startbgm.mp3");

		clickSound = new AudioClip(
				getClass().getResource("/music/select.mp3").toExternalForm());
		clickSound.setVolume(0.4);

		Button btn1 = new Button("▶ストーリー");
		btn1.getStyleClass().add("game-button");
		btn1.setOnAction(e -> {
			try {
				clickSound.stop();
				clickSound.play();
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							cleanup();
							GameController.startToStory(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		Button btn2 = new Button("⚔練習モード");
		btn2.setOnAction(e -> {
			try {
				clickSound.stop();
				clickSound.play();
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							cleanup(); // 🛠️ 安全のため timer.stop() だけでなく cleanup() に統一
							GameController.switchToPractice(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		btn2.getStyleClass().add("game-button");

		Button btn3 = new Button("ゲーム終了");
		btn3.getStyleClass().add("game-button");
		btn3.setOnAction(e -> {
			try {
				clickSound.stop();
				clickSound.play();
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							cleanup();
							Platform.exit();
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		buttonBox.getChildren().addAll(btn1, btn2, btn3);
		ui.getChildren().addAll(imageView, buttonBox);

		Button btnHelp = new Button("？");
		btnHelp.getStyleClass().add("help-button");
		btnHelp.setOnAction(e -> {
			try {
				clickSound.stop();
				clickSound.play();
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							cleanup();
							GameController.switchToHelp(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		root.getChildren().addAll(bgPane, ui);

		StackPane.setAlignment(btnHelp, Pos.TOP_RIGHT);
		StackPane.setMargin(btnHelp, new Insets(20));
		root.getChildren().add(btnHelp);

		Scene scene = new Scene(root, 1000, 800);

		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		// ===== レスポンシブ対応 =====
		ResponsiveUtil.bindMaxWidth(ui, scene.widthProperty(), 0.9);
		ResponsiveUtil.bindImageFitWidth(imageView, scene.widthProperty(), 0.5);

		for (Button b : new Button[] { btn1, btn2, btn3 }) {
			ResponsiveUtil.bindPrefSize(b, scene.widthProperty(), 0.35, scene.heightProperty(), 0.11, 160, 44);
			ResponsiveUtil.bindButtonFontAndPadding(b, scene.widthProperty(),
					14, 0.045, 30,
					8, 0.02, 20);
		}

		btnHelp.prefWidthProperty().bind(scene.widthProperty().multiply(0.05));
		btnHelp.prefHeightProperty().bind(btnHelp.widthProperty());
		btnHelp.setMinWidth(36);
		btnHelp.setMinHeight(36);

		ResponsiveUtil.bindButtonFontAndPadding(btnHelp, scene.widthProperty(),
				12, 0.02, 20,
				4, 0.0, 4);
		// ===== レスポンシブ対応ここまで =====

		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());

		stage.setMinWidth(320);
		stage.setMinHeight(480);

		stage.setTitle("スタート画面");
		stage.setScene(scene);
		
		// 🛠️【変更点4】まず画面を表示させ、描画が落ち着いた次のフレームでタイマーを開始する
		stage.show();
		
		Platform.runLater(() -> {
			if (timer != null) {
				timer.start();
			}
		});
	}

	public static void main(String[] args) {
		launch();
	}
}