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

public class Storyclear extends Application {

	private Stage stage;

	private AudioClip clickSound;

	@Override
	public void start(Stage stage) {
		this.stage = stage;

		stage.setTitle("GAME CLEAR");
		stage.setScene(clearScene());
		stage.show();
	}

	public Scene clearScene() {

		// ==================================================
		// SE読み込み
		// ==================================================
		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());

		// ==================================================
		// STORY CLEAR!!
		// ==================================================
		Text storyClear = new Text("STORY CLEAR!!");

		storyClear.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:100px;" + "-fx-font-weight:bold;"
				+ "-fx-fill:black;" + "-fx-stroke:white;" + "-fx-stroke-width:3;");

		// ==================================================
		// スタッフロール本文(後ほど名前変更)
		// ==================================================
		Text credits = new Text("━━━━━━━━━━━━━━━━━━\n\n" +

				"PROGRAMMER\n\n" +

				"うさぎ　ちいかわ\n" + "カピバラ\n" + "堀京子\n" + "マイメロディ\n" + "五条悟\n" + "服部平次\n" + "名探偵コナン\n\n" +

				"━━━━━━━━━━━━━━━━━━\n\n" +

				"COOPERATION\n\n" +

				"徳田 減衰\n\n" +

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
		titleButton.setTranslateY(90);

		titleButton.setOnAction(e -> {

			clickSound.stop();
			clickSound.play();

			GameController.switchStart(stage);
		});

		// ==================================================
		// THANK YOU + ボタン
		// ==================================================
		StackPane thankBox = new StackPane();
		thankBox.getChildren().addAll(endText, titleButton);

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

		rollBox.setTranslateY(scene.getHeight());
		roll.setToY(-900);

		// ==================================================
		// スタッフロール終了
		// ==================================================
		roll.setOnFinished(e -> {

			root.getChildren().remove(rollBox);

			endText.setVisible(true);

			root.getChildren().add(thankBox);

			PauseTransition endWait = new PauseTransition(Duration.seconds(2));

			endWait.setOnFinished(ev -> {
				titleButton.setVisible(true);
			});

			endWait.play();
		});

		// ==================================================
		// STORY CLEAR!! を2秒表示
		// ==================================================
		PauseTransition startWait = new PauseTransition(Duration.seconds(2));

		startWait.setOnFinished(e -> {

			root.getChildren().remove(storyClear);

			root.getChildren().add(rollBox);

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