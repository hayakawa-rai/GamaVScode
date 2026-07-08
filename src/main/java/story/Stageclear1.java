package story;

import control.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Stageclear1 extends Application {

	private AudioClip clearSound;
	private AudioClip clickSound;
	private AudioClip cancelSound;
	private int score = 0;
	private PauseTransition delay;
	private PauseTransition pause;
	private Stage stage;

	public Stageclear1() {
		// 引数なしコンストラクタ
	}

	// 💡 【修正】重複エラーとボタン消滅を完璧に防ぐ修正版メソッド
	public static void createAndStart(Stage currentStage, int finalScore) {
		if (currentStage == null)
			return;

		Stageclear1 instance = new Stageclear1();
		instance.setScore(finalScore);
		instance.stage = currentStage;

		// 1. ボタンやテキストが含まれる新しいSceneを完全に作成
		Scene newScene = instance.clear(currentStage);

		// 2. setRootではなく、Stageに対してSceneごと安全に丸ごと差し替える
		currentStage.setScene(newScene);

		currentStage.setTitle("stage1CLEAR");
		currentStage.centerOnScreen();
		currentStage.show();
	}

	private void cleanup() {
		if (delay != null) {
			delay.stop();
			delay = null;
		}
		if (pause != null) {
			pause.stop();
			pause = null;
		}
		if (clearSound != null) {
			clearSound.stop();
			clearSound = null;
		}
		if (clickSound != null) {
			clickSound.stop();
			clickSound = null;
		}
		if (cancelSound != null) {
			cancelSound.stop();
			cancelSound = null;
		}
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("stage1CLEAR");
		stage.setScene(clear(stage));
		stage.centerOnScreen();
		stage.show();
	}

	public Scene clear() {
		return clear(this.stage);
	}

	public Scene clear(Stage currentStage) {
		if (currentStage != null) {
			this.stage = currentStage;
		}
		// クリア音
		clearSound = new AudioClip(
				getClass().getResource("/music/yay.mp3").toExternalForm());
		clearSound.setVolume(0.5);

		delay = new PauseTransition(Duration.seconds(0.5));
		delay.setOnFinished(e -> {
			if (clearSound != null)
				clearSound.play();
		});
		delay.play();

		// テキスト類
		Text title = new Text("STAGE1    CLEAR!");
		title.setStyle("-fx-font-size: 80px; -fx-fill: rgb(180,180,180);");

		Text text = new Text("鍵を獲得しました！！");
		text.setStyle("-fx-font-size: 20px; -fx-fill: gray;");

		// 鍵の画像を読み込み（JPro対応）
		Image image = new Image(
				getClass().getResourceAsStream("/picture/kagi.png"));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(150);
		imageView.setFitHeight(150);

		HBox textAndImage = new HBox();
		textAndImage.setSpacing(10);
		textAndImage.setAlignment(Pos.CENTER);
		textAndImage.getChildren().addAll(imageView, text);

		clickSound = new AudioClip(
				getClass().getResource("/music/select.mp3").toExternalForm());
		clickSound.setVolume(0.4);

		cancelSound = new AudioClip(
				getClass().getResource("/music/cancel.mp3").toExternalForm());
		cancelSound.setVolume(0.4);

		// 次に進むボタン
		Button next = new Button("次のステージへ");
		next.getStyleClass().add("game-button2");
		next.setPrefSize(250, 80);
		next.setOnAction(e -> {
			if (clickSound != null) {
				clickSound.stop();
				clickSound.play();
			}
			pause = new PauseTransition(Duration.seconds(0.5));
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					GameController.switchStory2(this.stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			pause.play();
		});

		// スコア表示
		Text scoreLabel = new Text("SCORE: " + this.score);
		scoreLabel.setStyle("-fx-font-size: 30px; -fx-fill:  gray;");

		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button2");
		backButton.setPrefSize(250, 80);
		backButton.setOnAction(e -> {
			if (cancelSound != null) {
				cancelSound.stop();
				cancelSound.play();
			}
			pause = new PauseTransition(Duration.seconds(0.5));
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					GameController.switchStart(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			pause.play();
		});

		// 全パーツを格納するコンテナ
		VBox buttonBox = new VBox(20);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(title, textAndImage, scoreLabel, next, backButton);

		StackPane root = new StackPane();
		root.getChildren().add(buttonBox);

		Scene scene = new Scene(root, 1000, 800);
		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());

		if (stage != null) {
			stage.setMinWidth(1000);
			stage.setMinHeight(800);
			stage.setMaxWidth(1920);
			stage.setMaxHeight(1080);
		}
		return scene;
	}

	public void setScore(int score) {
		this.score = score;
	}
}