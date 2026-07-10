// ステージ3起動クラス
// ゲーム画面の生成と初期化を行う。

package test3;

import control.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
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

	// ゲーム制御クラス
	private GameController controller;

	@Override
	public void start(Stage stage) {
		starts(stage);
	}

	// 他クラスからステージ3を起動
	public static void createAndStart(Stage stage) {
		Main3 app = new Main3();
		app.starts(stage);
	}

	// ステージ3初期化処理
	public void starts(Stage stage) {

		// ===================================================
		// 再起動時の後始末
		// ===================================================

		// 多重起動防止
		if (this.controller != null) {
			this.controller.stop();
			controller = null;
		}
		// BGM重複再生防止
		Bgm.stopBGM();

		// ====================================================
		// モデル生成
		// ====================================================
		MapData model = new MapData();

		// =====================================================
		// Scene・Root生成
		// =====================================================
		StackPane root = new StackPane();
		root.getStyleClass().add("stage3");

		// CSS適用
		Scene scene = new Scene(root, 1000, 800);
		scene.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());

		// =====================================================
		// 背景画像設定
		// =====================================================
		ImageView backgroundView = new ImageView();

		try {
			Image backgroundImage = new Image(getClass().getResourceAsStream("/picture/shatyoroom.jpg"));
			backgroundView = new ImageView(backgroundImage);

			// ウィンドウサイズに追従
			backgroundView.fitWidthProperty().bind(root.widthProperty());
			backgroundView.fitHeightProperty().bind(root.heightProperty());
			backgroundView.setPreserveRatio(false);

			// 背景をぼかす
			backgroundView.setEffect(new GaussianBlur(10));

		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。パスを確認してください: " + e.getMessage());
		}

		// =====================================================
		// ゲーム画面生成
		// =====================================================
		Pane gameBase = new Pane();
		gameBase.getStyleClass().add("stage1");

		MapView view = new MapView(model, gameBase);

		// ゲーム描画用Canvas（マップの実寸サイズで固定）
		Canvas canvas = new Canvas();
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());
		gameBase.getChildren().add(canvas);

		// =====================================================
		// ポーズ画面生成
		// =====================================================
		VBox pauseLayer = new VBox(25);
		pauseLayer.setAlignment(Pos.CENTER);
		pauseLayer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.65);"); // 全体を暗くする

		// 初期状態は非表示
		pauseLayer.setVisible(false);
		pauseLayer.setMouseTransparent(true);

		// ポーズタイトル
		Label pauseLabel = new Label("PAUSE");
		pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
		pauseLabel.setTextFill(Color.YELLOW);
		
		// ゲーム再開ボタン
		Button ToPlayButton = new Button("ゲーム再開");
		ToPlayButton.setFont(Font.font("Meiryo", FontWeight.BOLD, 14));
		ToPlayButton.setPrefSize(160, 40);
		
		// 押すとメニューボタンと同じtogglePauseByButton()を呼んで再開する
		ToPlayButton.setOnAction(e -> {
			if (this.controller != null) {
				this.controller.togglePauseByButton();
			}
		});

		// =====================================================
		// 操作説明UI
		// =====================================================
		// 操作説明テキスト
		Label howToPlayText = new Label("移動 : ↑↓←→ / WASD\n画面下ボタン(スマホ用)");
		howToPlayText.setFont(Font.font("Meiryo", FontWeight.NORMAL, 14));
		howToPlayText.setTextFill(Color.WHITE);
		howToPlayText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
		howToPlayText.setWrapText(true);

		// 見やすくするための背景パネル（枠と余白をつける）
		howToPlayText.setStyle("-fx-background-color: rgba(255, 255, 255, 0.12);" + // うっすら白背景
				"-fx-background-radius: 8;" + "-fx-border-color: rgba(255, 255, 255, 0.4);" + // 薄い枠線
				"-fx-border-radius: 8;" + "-fx-border-width: 1;" + "-fx-padding: 12 20 12 20;");

		// 初期状態は非表示
		howToPlayText.setVisible(false);
		howToPlayText.setManaged(false); // 非表示のときレイアウトの隙間を作らない

		// 操作説明ボタン
		Button howToPlayButton = new Button("操作説明");
		howToPlayButton.setFont(Font.font("Meiryo", FontWeight.BOLD, 14));
		howToPlayButton.setPrefSize(160, 40);

		// 表示切替
		howToPlayButton.setOnAction(e -> {
			boolean nowVisible = !howToPlayText.isVisible();
			howToPlayText.setVisible(nowVisible);
			howToPlayText.setManaged(nowVisible);
		});

		// =====================================================
		// タイトルへ戻るボタン
		// =====================================================
		Button titleButton = new Button("タイトルへ戻る");
		titleButton.setFont(Font.font("Meiryo", FontWeight.BOLD, 14));
		titleButton.setPrefSize(160, 40);

		titleButton.setOnAction(e -> {
			if (this.controller != null) {
				System.out.println("タイトル画面へ戻ります");
				this.controller.forceBackToTitle();
			}
		});
		// ポーズ画面へ部品を追加
		pauseLayer.getChildren().addAll(pauseLabel, ToPlayButton, howToPlayButton, howToPlayText, titleButton);

		// =====================================================
		// メニューアイコンボタン（Story1と同じ見た目・配置）
		// =====================================================
		Image menuImg = new Image(getClass().getResourceAsStream("/picture/menu.jpeg"));
		ImageView menuIconView = new ImageView(menuImg);
		menuIconView.setFitWidth(40);
		menuIconView.setFitHeight(40);

		Button menuBtn = new Button("");
		menuBtn.setGraphic(menuIconView);
		menuBtn.setStyle("-fx-background-color: transparent;");
		StackPane.setAlignment(menuBtn, Pos.TOP_LEFT);
		// Insets(top, right, bottom, left) で上の余白だけ増やす
		StackPane.setMargin(menuBtn, new Insets(60, 0, 0, 30));

		menuBtn.setOnAction(e -> {
			if (this.controller != null) {
				this.controller.togglePauseByButton();
			}
		});

		// =====================================================
		// レイヤー構成
		// 背景 → ゲーム画面 → ポーズ画面
		// =====================================================
		root.getChildren().addAll(backgroundView, gameBase, menuBtn, pauseLayer);

		// =====================================================
		// 敵初期化
		// =====================================================
		model.initEnemy(new ImageView());

		// =====================================================
		//  コントローラー生成(stageNumber=1, isPractice=true)
		// =====================================================
		this.controller = new GameController(model, view, canvas, scene, stage, 3, false);

		// 十字キー(dPad)より手前にメニューボタンを持ってくる
		menuBtn.toFront();
		
		// ポーズ画面を登録
		this.controller.setPauseLayer(pauseLayer);

		// =====================================================
		// ステージ設定
		// =====================================================
		
		stage.setTitle("仙石さん - ステージ 3");
		stage.setScene(scene);

		// ウィンドウのサイズ制限
		stage.setMinWidth(1000);
		stage.setMinHeight(800);
		stage.setMaxWidth(1920);
		stage.setMaxHeight(1080);

		stage.show();

		// キーボード入力受付
		canvas.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
