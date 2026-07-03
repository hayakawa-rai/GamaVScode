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
import util.WindowUtil;

public class Gameover extends Application {

	private int score = 0;

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void start(Stage stage) {
		stage.setScene(create(stage, null, score));
		stage.setTitle("ゲーム");
		//画面の強制再設定
		WindowUtil.fillScreen(stage);
		stage.setScene(create(stage, null, score)); // その後でSceneをセット

	}

	public static Scene create(Stage stage, Runnable retryAction, int score) {

		// GAME OVER
		Label gameOverLabel = new Label("GAME OVER");
		gameOverLabel.getStyleClass().add("gameover-title");

		// スコア表示
		Label scoreLabel = new Label("SCORE : " + score);
		scoreLabel.setStyle("-fx-font-size: 32px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

		VBox titleBox = new VBox(20, gameOverLabel, scoreLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setStyle("-fx-padding: 150px 0 40px 0;");

		// いらすとや
		ImageView icon = new ImageView();
		try {

			java.net.URL imgUrl = Gameover.class.getResource("/picture/syujinkou(gameover).png");

			if (imgUrl != null) {
				icon.setImage(new Image(imgUrl.toExternalForm()));
			} else {
				System.out.println("⚠️ 警告: /syujinkou(gameover).png が見つかりません。画像の表示をスキップします。");
			}
		} catch (Exception e) {
			System.out.println("⚠️ 画像の読み込みに失敗しました。");
		}
		icon.setFitWidth(300);
		icon.setFitHeight(400);

		// 直前のステージをやり直す
		Button retryBtn = new Button("リトライする");
		retryBtn.setPrefSize(300, 70);
		retryBtn.getStyleClass().add("gameover-button");
		retryBtn.setOnAction(e -> {
			if (retryAction != null) {
				// 渡された各ステージの createAndStart(stage) が実行される
				retryAction.run();
			} else {
				System.out.println("⚠️ リトライ処理が登録されていません。");
			}
		});

		// タイトル画面へ戻る
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

		// ボタンを縦に並べる
		VBox buttonColumn = new VBox(20, retryBtn, titleBtn);
		buttonColumn.setAlignment(Pos.CENTER_LEFT);

		// 画像とボタン列を横に並べる
		HBox centerBox = new HBox(40, icon, buttonColumn);
		centerBox.setAlignment(Pos.CENTER);

		// レイアウト
		BorderPane ui = new BorderPane();
		ui.setTop(titleBox);
		ui.setCenter(centerBox);

		StackPane root = new StackPane();

		// 背景
		ImageView bg = new ImageView();
		try {
			java.net.URL bgUrl = Gameover.class.getResource("/picture/gameover.jpg");
			if (bgUrl != null) {
				bg.setImage(new Image(bgUrl.toExternalForm()));
			} else {
				System.out.println("⚠️ 警告: /picture/gameover.jpg が見つかりません。背景の表示をスキップします。");
			}
		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。");
		}
		bg.setPreserveRatio(false);

		// 白い透明レイヤー（背景を柔らかくする）
		Rectangle whiteOverlay = new Rectangle();
		whiteOverlay.setFill(Color.rgb(255, 255, 255, 0.15));

		// rootのサイズに合わせて伸縮させる
		bg.fitWidthProperty().bind(root.widthProperty());
		bg.fitHeightProperty().bind(root.heightProperty());

		whiteOverlay.widthProperty().bind(root.widthProperty());
		whiteOverlay.heightProperty().bind(root.heightProperty());

		// rootに追加
		root.getChildren().addAll(bg, whiteOverlay, ui);

		// 固定サイズを渡さない。StageのサイズにScene側が自動追従する。
	    Scene scene = new Scene(root);
		// ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
		stage.setMinWidth(1000);
		stage.setMinHeight(800);
		scene.getStylesheets().add(Gameover.class.getResource("/css/gameover.css").toExternalForm());

		return scene;

	}

	public static void main(String[] args) {
		launch();
	}
}