package test1;

import control.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import test1.model.MapData;
import test1.view.MapView;
import util.WindowUtil;

public class Main1 extends Application {

	private GameController controller;

	@Override
	public void start(Stage stage) {
		starts(stage);
	}

	public static void createAndStart(Stage stage) {
		Main1 app = new Main1();
		app.starts(stage);
	}

	public void starts(Stage stage) {
		// 多重起動を確実に防止
		if (this.controller != null) {
			this.controller.stop();
		}

		MapData model = new MapData();
		
		javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane();
		Pane gameBase = new Pane();
		gameBase.getStyleClass().add("stage1");

		MapView view = new MapView(model, gameBase);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("/css/test.css").toExternalForm());

		root.getStyleClass().add("stage1");

		// ★背景用Pane（CSSを効かせる対象）
		Pane bg = new Pane();
		bg.getStyleClass().add("game-bg");
		//bg.setPrefSize(viewWidth, viewHeight);

		bg.prefWidthProperty().bind(scene.widthProperty());
		bg.prefHeightProperty().bind(scene.heightProperty());

		bg.setMouseTransparent(true);

		try {
			// src/main/resources/picture/companyroom.jpg から画像を読み込む
			Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/emd-nottori.jpg"));
			ImageView backgroundView = new ImageView(backgroundImage);

			// 画像のサイズも、ウィンドウ（root）のサイズに完全に連動（バインド）させる
			backgroundView.fitWidthProperty().bind(root.widthProperty());
			backgroundView.fitHeightProperty().bind(root.heightProperty());
			backgroundView.setPreserveRatio(false);

			// 背景用Paneに画像を追加
			bg.getChildren().add(backgroundView);
		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。パスを確認してください: " + e.getMessage());
		}

		// ★ゲーム描画Canvas
		Canvas canvas = new Canvas();

		gameBase.getChildren().addAll(bg, canvas);

		// ★十字キー・スコア・残機よりもさらに上に重なる「一時停止（ポーズ）専用の最前面レイヤー」を作成
				javafx.scene.layout.VBox pauseLayer = new javafx.scene.layout.VBox(25); // 縦並び、隙間25px
				pauseLayer.setAlignment(javafx.geometry.Pos.CENTER); // 画面中央に配置
				pauseLayer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);"); // 全体を暗くする半透明の黒幕
				pauseLayer.setVisible(false); // 最初は隠しておく
				pauseLayer.setMouseTransparent(true); // ★【追加】最初はマウス入力を下にスルーさせる！

		// 一時停止用のUIテキストとボタンを構築
		javafx.scene.control.Label pauseLabel = new javafx.scene.control.Label("PAUSE");
		pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
		pauseLabel.setTextFill(Color.YELLOW);

		javafx.scene.control.Label subLabel = new javafx.scene.control.Label("もう一度 Pキー を押すと再開します");
		subLabel.setFont(Font.font("Meiryo", FontWeight.BOLD, 16));
		subLabel.setTextFill(Color.WHITE);

		javafx.scene.control.Button titleButton = new javafx.scene.control.Button("タイトルへ戻る");
		titleButton.setFont(Font.font("Meiryo", FontWeight.BOLD, 14));
		titleButton.setPrefSize(160, 40);
		
		// ボタンが押されたらタイトルに戻るアクション（既存の画面遷移ロジックを呼び出してください）
		titleButton.setOnAction(e -> {
			if (this.controller != null) {
				System.out.println("タイトル画面へ戻ります");
				this.controller.forceBackToTitle();
			}
		});

		pauseLayer.getChildren().addAll(pauseLabel, subLabel, titleButton);

		// ★【変更点】StackPaneに下から「ゲームUI本編」→「ポーズ最前面レイヤー」の順で重ねる
		root.getChildren().addAll(gameBase, pauseLayer);

		model.initEnemy(null);
		model.initEnemy(new javafx.scene.image.ImageView());

		// 完璧に準備ができた最後にコントローラーを1回だけ生成
		this.controller = new GameController(model, view, canvas, scene, stage, 1, false);
		
		// ★【変更点】コントローラーが最前面のポーズレイヤーを制御できるように登録
		this.controller.setPauseLayer(pauseLayer);

		stage.setTitle("JavaFX Pacman Stage MVC");
		stage.setScene(scene);
		
		WindowUtil.fillScreen(stage);
		canvas.requestFocus();
	}
		

	public static void main(String[] args) {
		launch(args);
	}
}
