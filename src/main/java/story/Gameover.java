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
		// WindowUtil.fillScreen(stage);  最大化
		stage.setScene(create(stage, null, score));
		stage.centerOnScreen();
		stage.show();
	}

	public static Scene create(Stage stage, Runnable retryAction, int score) {

		// GAME OVER
		Label gameOverLabel = new Label("GAME OVER");
		gameOverLabel.getStyleClass().add("gameover-title");

		// ハイスコア表示
		Label newRecordLabel = new Label("NEW RECORD!!");
		newRecordLabel.setStyle("-fx-font-size: 40px;" + "-fx-font-weight: bold;" + "-fx-text-fill: gold;");

		// スコア表示
		Label scoreLabel = new Label("SCORE : " + score);
		scoreLabel.setStyle("-fx-font-size: 32px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

		VBox titleBox = new VBox(20);

		titleBox.getChildren().addAll(gameOverLabel, scoreLabel);

		if (GameController.isNewRecord()) {
			titleBox.getChildren().add(newRecordLabel);
		}
		titleBox.setAlignment(Pos.CENTER);


		// ゲームオーバー仙石さん
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
		icon.setFitWidth(350);
		icon.setFitHeight(455);
		icon.setTranslateY(-70);

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
			try {
				
				// 画面遷移
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
		BorderPane.setMargin(titleBox, new Insets(60, 0, 20, 0)); // 上60px、下20pxの余白

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

		// 白い透明レイヤー
		Rectangle whiteOverlay = new Rectangle();
		whiteOverlay.setFill(Color.rgb(255, 255, 255, 0.15));

		// rootのサイズに合わせて伸縮させる
		bg.fitWidthProperty().bind(root.widthProperty());
		bg.fitHeightProperty().bind(root.heightProperty());

		whiteOverlay.widthProperty().bind(root.widthProperty());
		whiteOverlay.heightProperty().bind(root.heightProperty());

		// rootに追加
		root.getChildren().addAll(bg, whiteOverlay, ui);
		Scene scene = new Scene(root, 1000, 800);

	    //CSSを接続
		scene.getStylesheets().add(
				Gameover.class.getResource("/css/gameover.css").toExternalForm());
	    
		//ウィンドウの最小限のサイズを設定
		stage.setMinWidth(1000);
		stage.setMinHeight(800);
		stage.setMaxWidth(1920); // PC大画面やブラウザ最大化時の最大サイズ制限
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