package test3;

import control.GameController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import start.Bgm;
import test3.model.MapData;
import test3.view.MapView;

public class Main3 extends Application {

	private GameController controller;

	@Override
	public void start(Stage stage) {
		starts(stage);
	}

	public static void createAndStart(Stage stage) {
		Main3 app = new Main3();
		app.starts(stage);
	}

	public void starts(Stage stage) {
		// 多重起動を確実に防止
		if (this.controller != null) {
			this.controller.stop();
			controller = null;
		}

		Bgm.stopBGM(); // リトライ・多重起動時の重複再生防止

		MapData model = new MapData();

		StackPane root = new StackPane();
		root.getStyleClass().add("stage3");

		// 1000x800 でSceneを生成
		Scene scene = new Scene(root, 1000, 800);
		scene.getStylesheets().add(
				getClass().getResource("/css/test.css").toExternalForm());

		ImageView backgroundView = new ImageView();

		try {
			// src/main/resources/picture/companyroom.jpg から画像を読み込む
			Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/shatyoroom.jpg"));
			backgroundView = new ImageView(backgroundImage);
			// 画像のサイズも、ウィンドウ（root）のサイズに完全に連動（バインド）させる
			backgroundView.fitWidthProperty().bind(root.widthProperty());
			backgroundView.fitHeightProperty().bind(root.heightProperty());
			backgroundView.setPreserveRatio(false);

			// 背景をぼかす
			backgroundView.setEffect(new GaussianBlur(10));

		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。パスを確認してください: " + e.getMessage());
		}

		Pane gameBase = new Pane();
		gameBase.getStyleClass().add("stage1");

		MapView view = new MapView(model, gameBase);

		// ゲーム描画用Canvas（マップの実寸サイズで固定）
		Canvas canvas = new Canvas();
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());
		gameBase.getChildren().add(canvas);

		VBox pauseLayer = new VBox(25);
		pauseLayer.setAlignment(Pos.CENTER);
		pauseLayer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);"); // 全体を暗くする
		pauseLayer.setVisible(false);
		pauseLayer.setMouseTransparent(true);

		Label pauseLabel = new Label("PAUSE");
		pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
		pauseLabel.setTextFill(Color.YELLOW);

		Label subLabel = new Label("もう一度 Pキー を押すと再開します");
		subLabel.setFont(Font.font("Meiryo", FontWeight.BOLD, 16));
		subLabel.setTextFill(Color.WHITE);

		// ==== 操作説明用 ====
		Label howToPlayText = new Label(
				"移動 : ↑↓←→ / WASD\n画面下ボタン(スマホ用)");
		howToPlayText.setFont(Font.font("Meiryo", FontWeight.NORMAL, 14));
		howToPlayText.setTextFill(Color.WHITE);
		howToPlayText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
		howToPlayText.setWrapText(true);
		// 見やすくするための背景パネル（枠と余白をつける）
		howToPlayText.setStyle(
				"-fx-background-color: rgba(255, 255, 255, 0.12);" + // うっすら白背景
						"-fx-background-radius: 8;" +
						"-fx-border-color: rgba(255, 255, 255, 0.4);" + // 薄い枠線
						"-fx-border-radius: 8;" +
						"-fx-border-width: 1;" +
						"-fx-padding: 12 20 12 20;");
		howToPlayText.setVisible(false);
		howToPlayText.setManaged(false); // 非表示のときレイアウトの隙間を作らない

		Button howToPlayButton = new Button("操作説明");
		howToPlayButton.setFont(Font.font("Meiryo", FontWeight.BOLD, 14));
		howToPlayButton.setPrefSize(160, 40);
		howToPlayButton.setOnAction(e -> {
			boolean nowVisible = !howToPlayText.isVisible();
			howToPlayText.setVisible(nowVisible);
			howToPlayText.setManaged(nowVisible);
		});

		Button titleButton = new Button("タイトルへ戻る");
		titleButton.setFont(Font.font("Meiryo", FontWeight.BOLD, 14));
		titleButton.setPrefSize(160, 40);

		titleButton.setOnAction(e -> {
			if (this.controller != null) {
				System.out.println("タイトル画面へ戻ります");
				this.controller.forceBackToTitle();
			}
		});
		pauseLayer.getChildren().addAll(
				pauseLabel, subLabel, howToPlayButton, howToPlayText, titleButton);

		// StackPaneに下から「ゲームUI本編」→「ポーズ最前面レイヤー」の順で重ねる
		root.getChildren().addAll(backgroundView, gameBase, pauseLayer);

		// 敵描画呼び出し
		model.initEnemy(new ImageView());

		// 準備ができたコントローラーを生成 (stageNumber=1, isPractice=true)
		this.controller = new GameController(model, view, canvas, scene, stage, 3, false);
		// コントローラーが最前面のポーズレイヤーを制御できるように登録
		this.controller.setPauseLayer(pauseLayer);

		stage.setTitle("仙石さん - ステージ 3");
		stage.setScene(scene);

		// ウィンドウのサイズ制限
		stage.setMinWidth(1000);
		stage.setMinHeight(800);
		stage.setMaxWidth(1920);
		stage.setMaxHeight(1080);

		stage.show();

		canvas.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
