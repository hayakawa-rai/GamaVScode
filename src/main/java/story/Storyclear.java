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

		// クリック音
		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());

		// GAME CLEAR
		Text clearTitle = new Text("GAME CLEAR");
		clearTitle.setStyle("-fx-font-size: 90px;" + "-fx-font-weight: bold;" + "-fx-fill: white;");

		// 感謝メッセージ
		Text thanks = new Text("ここまで遊んでいただき\n" + "本当にありがとうございました！\n\n" + "少しでも楽しんでいただけたなら幸いです。");

		thanks.setStyle("-fx-font-size: 24px;" + "-fx-fill: #334455;");
		thanks.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

		// タイトルへ戻る
		Button titleButton = new Button("タイトルへ戻る");
		titleButton.getStyleClass().add("game-button2");
		titleButton.setPrefSize(250, 80);

		titleButton.setOnAction(e -> {

			clickSound.stop();
			clickSound.play();

			GameController.switchStart(stage);
		});

		// レイアウト
		VBox content = new VBox();
		content.setAlignment(Pos.CENTER);
		content.setSpacing(35);

		content.getChildren().addAll(clearTitle, thanks, titleButton);

		// 背景画像
		Image bgImage = new Image(getClass().getResourceAsStream("/picture/EMD_picture.jpg"));

		ImageView bgView = new ImageView(bgImage);
		bgView.setPreserveRatio(false);

		// 白フィルター
		Rectangle whiteFilter = new Rectangle();
		whiteFilter.setFill(Color.rgb(255, 255, 255, 0.30));

		// レイアウト
		StackPane root = new StackPane();
		root.getChildren().addAll(bgView, whiteFilter, content);

		Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());

		whiteFilter.widthProperty().bind(scene.widthProperty());
		whiteFilter.heightProperty().bind(scene.heightProperty());

		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

		return scene;
	}
}