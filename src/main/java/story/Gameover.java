package story;

import control.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
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

public class Gameover extends Application {

	private int score = 0;

	@Override
	public void start(Stage stage) {
		stage.setScene(create(stage, null, score));
		stage.setTitle("ゲームオーバー");
		stage.centerOnScreen();
		stage.show();
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
			// ⭕【JPro対応】getResourceAsStream に変更し、安全にストリーム読み込み
			var imgStream = Gameover.class.getResourceAsStream("/picture/syujinkou(gameover).png");
			if (imgStream != null) {
				icon.setImage(new Image(imgStream));
			} else {
				System.out.println("⚠️ 警告: /picture/syujinkou(gameover).png が見つかりません。画像の表示をスキップします。");
			}
		} catch (Exception e) {
			System.out.println("⚠️ 画像の読み込みに失敗しました。");
		}
		icon.setFitWidth(400);
		icon.setFitHeight(500);
		icon.setTranslateY(-80);

		// 直前のステージをやり直す
		Button retryBtn = new Button("リトライする");
		retryBtn.setPrefSize(300, 70);
		retryBtn.getStyleClass().add("gameover-button");
		retryBtn.setOnAction(e -> {
			if (retryAction != null) {
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
			try {
				GameController.switchStart(stage);
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
		BorderPane.setAlignment(titleBox, Pos.CENTER);
		BorderPane.setMargin(titleBox, new Insets(150, 0, 40, 0));
		
		ui.setTop(titleBox);
		ui.setCenter(centerBox);

		StackPane root = new StackPane();

		// 背景
		ImageView bg = new ImageView();
		try {
			// ⭕【JPro対応】こちらも getResourceAsStream に修正
			var bgStream = Gameover.class.getResourceAsStream("/picture/gameover.jpg");
			if (bgStream != null) {
				bg.setImage(new Image(bgStream));
			} else {
				System.out.println("⚠️ 警告: /picture/gameover.jpg が見つかりません。背景の表示をスキップします。");
			}
		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。");
		}
		bg.setPreserveRatio(false);

		// 白い透明レイヤー
		Rectangle whiteOverlay = new Rectangle();
		whiteOverlay.setFill(Color.rgb(255, 255, 255, 0.15));

		bg.fitWidthProperty().bind(root.widthProperty());
		bg.fitHeightProperty().bind(root.heightProperty());

		whiteOverlay.widthProperty().bind(root.widthProperty());
		whiteOverlay.heightProperty().bind(root.heightProperty());

		root.getChildren().addAll(bg, whiteOverlay, ui);

		Scene scene = new Scene(root, 1000, 800);
		
		// ⭕【JPro対応】CSSの安全な読み込み（Nullチェック追加 ＆ 不要な空白を除去）
		var cssUrl = Gameover.class.getResource("/css/gameover.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}
		
		// ⭕【エラー修正】全角スペースなどの不正な隠れ文字を取り除き、すっきり整形
		stage.setMinWidth(1000);
		stage.setMinHeight(800);
		stage.setMaxWidth(1920);
		stage.setMaxHeight(1080);
		return scene;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public static void main(String[] args) {
		launch();
	}
}