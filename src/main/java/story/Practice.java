package story;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import start.Start;

public class Practice extends Application {

	private Stage stage;
	// hold the background animation so we can stop it when switching scenes
	private AnimationTimer timer;

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setScene(createScene());
		stage.setTitle("練習モード");
		stage.show();
	}

	public Scene createScene() {
		
		// タイトル
		Label title = new Label("練習モード");
		title.setStyle(
			    "-fx-font-size: 48px;" +
			    "-fx-font-weight: 900;" +
			    "-fx-text-fill: white;" +
			    "-fx-effect: dropshadow(gaussian, rgba(0,120,220,0.8), 20, 0.6, 0, 3);"
			);



		// ステージ選択
		Button stage1 = new Button("STAGE 1");
		Button stage2 = new Button("STAGE 2");
		Button stage3 = new Button("STAGE 3");

		stage1.getStyleClass().add("game-button");
		stage2.getStyleClass().add("game-button");
		stage3.getStyleClass().add("game-button");

		stage1.setPrefWidth(400);
		stage2.setPrefWidth(400);
		stage3.setPrefWidth(400);

		stage1.setPrefHeight(80);
		stage2.setPrefHeight(80);
		stage3.setPrefHeight(80);
		
		//音声読み込み
		AudioClip clickSound = new AudioClip(
			getClass().getResource("/music/select.mp3").toExternalForm()
		);
		// 音量調整
		clickSound.setVolume(0.4);
		//音声読み込み
		AudioClip cancelSound = new AudioClip(
			getClass().getResource("/music/cancel.mp3").toExternalForm()
		);
		// 音量調整
		cancelSound.setVolume(0.4);
		
		stage1.setOnAction(e -> {
			//音をつける
	    	clickSound.stop();
	    	clickSound.play();
	    	
	    	// 0.5秒待つ
	        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
			// 1. 練習モードの背景アニメーションを停止
			if (timer != null) {
				timer.stop();
			}
			
			// 2. SampleController の遷移メソッドを直接呼び出す！
			// (※ メソッド名が switchToStart で合っているか、確認してね！)
			test.test2.GameController.switchToGame(stage);
		});
		VBox stageButtons = new VBox(20, stage1, stage2, stage3);
		stageButtons.setAlignment(Pos.CENTER);

		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button");
		backButton.setPrefHeight(60);
		backButton.setPrefWidth(200);

		// ★ master側の処理を残す
		backButton.setOnAction(e -> {
	    	cancelSound.stop();
	    	cancelSound.play();
	    	
	    	// 0.5秒待つ
	        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));

	        // 待った後に画面遷移
	        pause.setOnFinished(ev -> {
	        Start titleScreen = new Start();
	        // 背景停止
	        timer.stop();
	        try {
	            titleScreen.start(stage);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    });
	        // タイマー開始
	        pause.play();
	    });

		HBox backBox = new HBox(backButton);
		backBox.setAlignment(Pos.BOTTOM_RIGHT);
		backBox.setStyle("-fx-padding: 20px;");

		// 背景
		Image bgImage = new Image(
				Practice.class.getResource("/picture/background.png").toExternalForm());

		ImageView bg1 = new ImageView(bgImage);
		ImageView bg2 = new ImageView(bgImage);

		bg2.setLayoutX(bgImage.getWidth());

		// アニメーション
		this.timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				bg1.setLayoutX(bg1.getLayoutX() - 1);
				bg2.setLayoutX(bg2.getLayoutX() - 1);

				if (bg1.getLayoutX() + bgImage.getWidth() <= 0) {
					bg1.setLayoutX(bg2.getLayoutX() + bgImage.getWidth());
				}
				if (bg2.getLayoutX() + bgImage.getWidth() <= 0) {
					bg2.setLayoutX(bg1.getLayoutX() + bgImage.getWidth());
				}
			}
		};
		this.timer.start();

		StackPane root = new StackPane();

		Pane bgPane = new Pane();
		bgPane.getChildren().addAll(bg1, bg2);

		VBox titleBox = new VBox(title);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setStyle("-fx-padding: 200px 0 20px 0;");

		BorderPane ui = new BorderPane();
		ui.setTop(titleBox);
		ui.setCenter(stageButtons);
		ui.setBottom(backBox);

		root.getChildren().addAll(bgPane, ui);

		Scene scene = new Scene(root, 1000, 800);
		//ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
        stage.setMinWidth(800);
        stage.setMinHeight(600);

		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());

		return scene;
	}

	public static void main(String[] args) {
		launch();
	}
}