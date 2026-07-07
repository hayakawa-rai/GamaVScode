package story;

import control.GameController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.text.TextAlignment;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Storyclear extends Application {

	private Stage stage;
	private AudioClip clickSound;
	private MediaPlayer endingBgm;

	@Override
	public void start(Stage stage) {
		this.stage = stage;

		stage.setTitle("STORY CLEAR");
		stage.setScene(clearScene());
		stage.show();
	}

	public Scene clearScene() {

		// ==================================================
		// SE読み込み
		// ==================================================
		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());

		// ==================================================
		// エンドロールBGM
		// ==================================================
		Media media = new Media(getClass().getResource("/music/Storyclear_bgm2.mp3").toExternalForm());

		endingBgm = new MediaPlayer(media);

		// 音量
		endingBgm.setVolume(0.5);

		// BGMループ
		endingBgm.setCycleCount(MediaPlayer.INDEFINITE);

		// ==================================================
		// STORY CLEAR!!
		// ==================================================
		Text storyClear = new Text("STORY CLEAR!!");

		storyClear.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:100px;" + "-fx-font-weight:bold;"
				+ "-fx-fill:black;" + "-fx-stroke:white;" + "-fx-stroke-width:3;");

		// 効果音
		AudioClip clearSound = new AudioClip(getClass().getResource("/music/Storyclear_sound.mp3").toExternalForm());

		clearSound.play();

		// ==================================================
		// スタッフロール本文(後ほど名前変更)
		// ==================================================
		Text credits = new Text("━━━━━━━━━━━━━━━━━━\n\n" +

				"PROGRAMMER\n\n" +

				"N.Y\n" + "H.R\n" + "O.S\n" + "K.S\n" + "W.M\n" + "W.T\n" + "F.O\n" + "M.R\n\n" +

				"━━━━━━━━━━━━━━━━━━\n\n" +

				"COOPERATION\n\n" +

				"先輩社員\n\n" +

				"━━━━━━━━━━━━━━━━━━\n\n" +

				"SPECIAL THANKS\n\n" +

				"遊んでくださった皆様\n\n" +

				"━━━━━━━━━━━━━━━━━━\n\n" +

				"最後まで諦めず\n" + "会社を取り戻してくれて\n" + "ありがとうございました。\n\n" +

				"━━━━━━━━━━━━━━━━━━\n\n" +

				"平和を取り戻した会社で\n" + "今日もまた\n" + "新しい一日が始まります。\n\n" +

				"\n\n\n\n\n\n\n\n\n\n");

		credits.setStyle("-fx-font-family: 'PixelMplus12';" + "-fx-font-size: 24px;" + "-fx-fill: #334455;");

		credits.setTextAlignment(TextAlignment.CENTER);

		// ==================================================
		// ゲームタイトル
		// ==================================================

		Image logoImage = new Image(getClass().getResourceAsStream("/picture/title.png"));

		ImageView logoView = new ImageView(logoImage);

		logoView.setPreserveRatio(true);
		logoView.setFitWidth(350);

		// ==================================================
		// THANK YOU FOR PLAYING!!
		// ==================================================

		Text endText = new Text("THANK YOU FOR PLAYING!!");

		endText.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:80px;" + "-fx-font-weight:bold;"
				+ "-fx-fill:black;" + "-fx-stroke:white;" + "-fx-stroke-width:3;");

		endText.setVisible(false);
		endText.setTextAlignment(TextAlignment.CENTER);

		// ==================================================
		// タイトルへ戻るボタン
		// ==================================================
		Button titleButton = new Button("タイトルへ");

		titleButton.getStyleClass().add("game-button2");
		titleButton.setPrefSize(170, 55);
		titleButton.setVisible(false);

		titleButton.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:24px;" +

				"-fx-background-color:#EAF4FF;" + "-fx-text-fill:#2B4A6A;" +

				"-fx-border-color:white;" + "-fx-border-width:2;" +

				"-fx-background-radius:10;" + "-fx-border-radius:10;");

		titleButton.setOnAction(e -> {

			clickSound.stop();
			clickSound.play();

			if (endingBgm != null) {
				endingBgm.stop();
			}

			GameController.switchStart(stage);
		});

		// ==================================================
		// THANK YOU + ボタン
		// ==================================================
		VBox thankBox = new VBox(30);

		thankBox.setAlignment(Pos.CENTER);
		thankBox.getChildren().addAll(logoView, endText, titleButton);

		// ==================================================
		// スタッフロール用VBox
		// ==================================================
		VBox rollBox = new VBox(30);
		rollBox.setAlignment(Pos.TOP_CENTER);
		rollBox.getChildren().add(credits);

		// ==================================================
		// 背景画像
		// =================================================
		Image bgImage = new Image(getClass().getResourceAsStream("/picture/EMD_picture.jpg"));

		ImageView bgView = new ImageView(bgImage);
		bgView.setPreserveRatio(false);

		// ==================================================
		// 白フィルター
		// ==================================================
		Rectangle whiteFilter = new Rectangle();
		whiteFilter.setFill(Color.rgb(255, 255, 255, 0.70));

		// ==================================================
		// Root
		// ==================================================
		StackPane root = new StackPane();

		root.getChildren().addAll(bgView, whiteFilter, storyClear);

		// ==================================================
		// Scene
		// ==================================================
		Scene scene = new Scene(root, 1600, 900);

		// ==================================================
		// スタッフロールアニメーション
		// =================================================
		TranslateTransition roll = new TranslateTransition(Duration.seconds(23), rollBox);

		rollBox.setTranslateY(1050);
		roll.setToY(-900);

		// ==================================================
		// コピーライト
		// ==================================================
		Text copyrightText = new Text("2026年度 EMD 新卒一同");

		copyrightText.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:18px;" + "-fx-fill:#334455;");

		// ==================================================
		// 会社ロゴ
		// ==================================================
		Image companyLogoImage = new Image(getClass().getResourceAsStream("/picture/EMD_logo.png"));

		ImageView companyLogoView = new ImageView(companyLogoImage);

		companyLogoView.setPreserveRatio(true);
		companyLogoView.setFitWidth(60);

		// ==================================================
		// 右下表示
		// ==================================================
		VBox companyBox = new VBox(5);

		companyBox.setAlignment(Pos.BOTTOM_RIGHT);
		companyBox.getChildren().addAll(companyLogoView, copyrightText);
		companyBox.setMouseTransparent(true);

		StackPane.setAlignment(companyBox, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(companyBox, new javafx.geometry.Insets(0, 30, 20, 0));

		// ==================================================
		// スタッフロール終了
		// ==================================================
		roll.setOnFinished(e -> {

			root.getChildren().remove(rollBox);
			root.getChildren().addAll(thankBox, companyBox);
			
			endText.setVisible(true);
			
			PauseTransition endWait = new PauseTransition(Duration.seconds(2));

			endWait.setOnFinished(ev -> {
				titleButton.setVisible(true);
			});

			endWait.play();
		});

		// ==================================================
		// STORY CLEAR!! を4秒表示
		// ==================================================
		PauseTransition startWait = new PauseTransition(Duration.seconds(4));

		startWait.setOnFinished(e -> {

			root.getChildren().remove(storyClear);
			root.getChildren().add(rollBox);

			endingBgm.play();

			roll.play();
		});

		startWait.play();

		// ==================================================
		// 背景サイズ同期
		// ==================================================
		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());

		whiteFilter.widthProperty().bind(scene.widthProperty());
		whiteFilter.heightProperty().bind(scene.heightProperty());

		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

		return scene;
	}
}