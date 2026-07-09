package start;

import com.jpro.webapi.WebAPI;

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

public class Start extends Application {
	private AnimationTimer timer;
	private AudioClip clickSound;
	private PauseTransition pause;

	// 🛠️【フリーズ対策】最優先でタイマーだけを安全に止めるためのメソッド
	private void stopTimerOnly() {
		if (timer != null) {
			timer.stop();
		}
	}

	private void cleanup() {
		// 背景アニメーション停止（念のため）
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
			var fontStream = getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf");
			if (fontStream != null) {
				Font loadedFont = Font.loadFont(fontStream, 20);
				if (loadedFont != null) {
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

		Image bgImage = new Image(getClass().getResource("/picture/background.png").toExternalForm());
		double bgWidth = bgImage.getWidth();
		double bgHeight = bgImage.getHeight();
		Pane bgPane = new Pane();
		final double[] scrollX = { 0 };

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				scrollX[0] -= 1;
				if (scrollX[0] <= -bgWidth) {
					scrollX[0] = 0;
				}
				ImagePattern pattern = new ImagePattern(bgImage, scrollX[0], 0, bgWidth, bgHeight, false);
				bgPane.setBackground(new Background(new BackgroundFill(pattern, null, null)));
			}
		};

		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: black;");

		VBox ui = new VBox();
		ui.setMaxWidth(800);
		ui.setSpacing(20);
		ui.setAlignment(Pos.CENTER);

		Image image = new Image(getClass().getResource("/picture/title.png").toExternalForm());
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(500);
		imageView.setFitHeight(400);
		imageView.setPreserveRatio(true);

		VBox buttonBox = new VBox();
		buttonBox.setSpacing(20);
		buttonBox.setAlignment(Pos.CENTER);
		
		Bgm.playBGM("/music/startbgm.mp3");

		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());
		clickSound.setVolume(0.4);

		// 1. ストーリーモードボタン
		Button btn1 = new Button("▶ストーリー");
		btn1.setPrefSize(300, 100);
		btn1.getStyleClass().add("game-button");
		btn1.setOnAction(e -> {
			try {
				stopTimerOnly(); // 👈【対策】押した瞬間にタイマーを即停止！
				clickSound.stop();
				clickSound.play();

				Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
					cleanup();
					GameController.startToStory(stage);
				}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		// 2. 練習モードボタン
		Button btn2 = new Button("⚔練習モード");
		btn2.setPrefSize(300, 100);
		btn2.getStyleClass().add("game-button");
		btn2.setOnAction(e -> {
			try {
				stopTimerOnly(); // 👈【対策】押した瞬間にタイマーを即停止！
				clickSound.stop();
				clickSound.play();

				Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
					cleanup();
					GameController.switchToPractice(stage);
				}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		// 3. ゲーム終了ボタン
		Button btn3 = new Button("ゲーム終了");
		btn3.setPrefSize(300, 100);
		btn3.getStyleClass().add("game-button");
		btn3.setOnAction(e -> {
			try {
				stopTimerOnly(); // 👈【対策】押した瞬間にタイマーを即停止！
				clickSound.stop();
				clickSound.play();

				Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
					cleanup();
					WebAPI webAPI = WebAPI.getWebAPI(stage);
					if (webAPI != null) {
						webAPI.executeScript("window.location.href = 'https://www.bing.com';");
					} else {
						Platform.exit();
					}
				}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		buttonBox.getChildren().addAll(btn1, btn2, btn3);
		ui.getChildren().addAll(imageView, buttonBox);

		// 4. 「？」ヘルプボタン
		Button btnHelp = new Button("？");
		btnHelp.setPrefSize(50, 50);
		btnHelp.getStyleClass().add("help-button");
		btnHelp.setOnAction(e -> {
			try {
				stopTimerOnly(); // 👈【対策】押した瞬間にタイマーを即停止！
				clickSound.stop();
				clickSound.play();

				Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
					cleanup();
					GameController.switchToHelp(stage);
				}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		// 重ね順の制御：btnHelpが必ず最前面
		StackPane.setAlignment(btnHelp, Pos.TOP_RIGHT);
		StackPane.setMargin(btnHelp, new Insets(20));
		root.getChildren().addAll(bgPane, ui, btnHelp);

		Scene scene = new Scene(root, 1000, 800);
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.setMaxWidth(1920);
		stage.setMaxHeight(1080);
		stage.setTitle("スタート画面");
		stage.setScene(scene);
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