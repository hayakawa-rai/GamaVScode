package story;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import start.Start;

public class Gameover extends Application {

	@Override
	public void start(Stage stage) {
		stage.setScene(create(stage));
		stage.setTitle("ゲーム");
		stage.setMaximized(true);
		stage.show();
	}

	public static Scene create(Stage stage) {

		//GAME OVER
		Label gameOverLabel = new Label("GAME OVER");
		
		gameOverLabel.getStyleClass().add("gameover-title");


		VBox titleBox = new VBox(gameOverLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setStyle("-fx-padding: 150px 0 40px 0;");

		//いらすとや
		Image img = new Image(Gameover.class.getResource("/irasutoya(gameover).png").toExternalForm());
		ImageView icon = new ImageView(img);
		icon.setFitWidth(200);
		icon.setFitHeight(200);

		
		//練習画面へ戻る
		Button retryBtn = new Button("練習モードへ");
		retryBtn.setPrefSize(300, 70);
		retryBtn.getStyleClass().add("gameover-button");
		retryBtn.setOnAction(e -> {
			Practice practiceScreen = new Practice();
			try {
				practiceScreen.start(stage);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//タイトル画面へ戻る
		Button titleBtn = new Button("タイトルへ");
		titleBtn.setPrefSize(300, 70);
		titleBtn.getStyleClass().add("gameover-button");
		titleBtn.setOnAction(e -> {
			Start titleScreen = new Start();
			try {
				titleScreen.start(stage);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//ボタンを縦に並べる
		VBox buttonColumn = new VBox(20, retryBtn, titleBtn);
		buttonColumn.setAlignment(Pos.CENTER_LEFT);

		//画像とボタン列を横に並べる
		HBox centerBox = new HBox(40, icon, buttonColumn);
		centerBox.setAlignment(Pos.CENTER);

		//レイアウト
		BorderPane ui = new BorderPane();
		ui.setTop(titleBox);
		ui.setCenter(centerBox);

		StackPane root = new StackPane();

		//背景
		Image bgImage = new Image(Gameover.class.getResource("/picture/gameover.jpg").toExternalForm());
		ImageView bg = new ImageView(bgImage);
		bg.setPreserveRatio(false);

		// 白い透明レイヤー（背景を柔らかくする）
		Rectangle whiteOverlay = new Rectangle();
		whiteOverlay.setFill(Color.rgb(255, 255, 255, 0.15));

		//rootのサイズに合わせて伸縮させる
		bg.fitWidthProperty().bind(root.widthProperty());
		bg.fitHeightProperty().bind(root.heightProperty());
		
		whiteOverlay.widthProperty().bind(root.widthProperty());
		whiteOverlay.heightProperty().bind(root.heightProperty());

		//rootに追加
		root.getChildren().addAll(bg, whiteOverlay, ui);
		
		Scene scene = new Scene(root, 1000, 800);
		//ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
        stage.setMinWidth(800);
        stage.setMinHeight(600);
		scene.getStylesheets().add(Gameover.class.getResource("/css/gameover.css").toExternalForm());

		return scene;

	}

	public static void main(String[] args) {
		launch();
	}

}
