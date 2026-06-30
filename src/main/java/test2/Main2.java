package test2;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import test2.controller.GameController;
import test2.model.MapData;
import test2.view.MapView;

public class Main2 extends Application {

	private GameController controller;

	@Override
	public void start(Stage stage) {
<<<<<<< HEAD
=======

>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
		starts(stage);
<<<<<<< HEAD
=======

>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
		starts(stage);

		// ModelとViewの作成
		MapData model = new MapData(); // ゲームデータ(マップ・パックマン状態)
		MapView view = new MapView(model); // 描画担当

		// 画面サイズ取得
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		double screenW = bounds.getWidth();
		double screenH = bounds.getHeight();

		// HUD（情報表示領域）
		// HUDの高さ
		double hudHeight = 50;

		// Canvas作成
		// メインゲーム描画用キャンバス
		Canvas gameCanvas = new Canvas(screenW, screenH - hudHeight * 2);
		// 上部HUD
		Canvas topHud = new Canvas(screenW, hudHeight);
		// 下部HUD
		Canvas bottomHud = new Canvas(screenW, hudHeight);

		// 画面レイアウト作成
		BorderPane root = new BorderPane();

		// 余白部分に画像貼り付け
		String bgUrl = getClass().getResource("/picture/companyroom.jpg").toExternalForm();
		root.setStyle("-fx-background-image: url('" + bgUrl
				+ "'); -fx-background-size: cover; -fx-background-position: center;");

		root.setTop(topHud); // 上段
		root.setCenter(gameCanvas); // 中央(ゲーム画面配置)
		root.setBottom(bottomHud); // 下段

		// シーン生成
		Scene scene = new Scene(root);

		// CSSファイルを読み込む処理
		scene.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());

		// ウィンドウ設定
		stage.setScene(scene);

		// ウィンドウタイトル
		stage.setTitle("Pacman MVC");

		// 起動時に最大化
		stage.setMaximized(true);

		// Controller生成
		// MVCを接続しゲーム開始
		new GameController(model, view, gameCanvas, scene, stage);

		// ウィンドウ表示
		stage.show();

		// キーボード入力を受け取る設定
		gameCanvas.setFocusTraversable(true);
		gameCanvas.requestFocus();
	}

	public void starts(Stage stage) {
		// 多重起動を確実に防止
		if (this.controller != null) {
			this.controller.stop();
		}

		MapData model = new MapData();
		Pane root = new Pane();

		MapView view = new MapView(model, root);

		int viewWidth = model.getMap()[0].length * MapData.TILE_SIZE;
		int viewHeight = model.getMap().length * MapData.TILE_SIZE;

		Scene scene = new Scene(root, viewWidth, viewHeight);
		scene.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());

		root.getStyleClass().add("stage2");

		// ★背景用Pane（CSSを効かせる対象）
		Pane bg = new Pane();
		bg.getStyleClass().add("game-bg");
		bg.setPrefSize(viewWidth, viewHeight);
		bg.setMouseTransparent(true);

		try {
			// src/main/resources/picture/companyroom.jpg から画像を読み込む
			Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/companyroom.jpg"));
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
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		root.getChildren().addAll(bg, canvas);

		model.initEnemy(null);
		/*
		 * コメントで隠してるのが前の描写方法 // 先に空の ImageView を用意 javafx.scene.image.ImageView
		 * redImageView = new javafx.scene.image.ImageView();
		 * 
		 * // モデル側で敵を生成し、内部で画像（Image）を確実にセットさせる model.initEnemy(redImageView);
		 * 
		 * // 画像が入った「後」に、ビューを通してサイズを30x30にフィットさせる view.setupEnemyView(redImageView);
		 * 
		 * // 敵の ImageView を画面に登録 root.getChildren().add(redImageView);
		 */
		// 敵描画呼び出し 成田
		model.initEnemy(new javafx.scene.image.ImageView());

		// 完璧に準備ができた【最後】にコントローラーを1回だけ生成（重複は削除！）
		this.controller = new GameController(model, view, canvas, scene, stage);

		stage.setTitle("JavaFX Pacman Stage MVC");
		stage.setScene(scene);
		stage.show();

		canvas.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
