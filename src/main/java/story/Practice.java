package story;

import control.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import start.Bgm;
import javafx.scene.control.Tooltip;
import common.HighScoreManager;

public class Practice extends Application {

	private AnimationTimer timer;
	private AudioClip clickSound;
	private AudioClip cancelSound;
	private PauseTransition pause;
	private Stage stage;

	private void cleanup() {

		// 背景アニメーション停止
		if (timer != null) {
			timer.stop();
			timer = null;
		}

		// 遅延処理停止
		if (pause != null) {
			pause.stop();
			pause = null;
		}

		// 効果音停止
		if (clickSound != null) {
			clickSound.stop();
			clickSound = null;
		}

		if (cancelSound != null) {
			cancelSound.stop();
			cancelSound = null;
		}

		// BGM停止
		Bgm.stopBGM();
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("練習モード");
		// WindowUtil.fillScreen(stage); // 最大化
		stage.setScene(createScene()); // その後でSceneをセット
	}

	public Scene createScene() {

		// タイトル
		Label title = new Label("練習モード");
		title.setStyle("-fx-font-family: 'PixelMplus12';" + "-fx-font-size: 48px;" +
		// "-fx-font-weight: 900;" +
				"-fx-text-fill: white;" + "-fx-effect: dropshadow(gaussian, rgba(0,120,220,0.8), 20, 0.6, 0, 3);");

		// ステージ選択
		Button btn1 = new Button("STAGE 1");
		Button btn2 = new Button("STAGE 2");
		Button btn3 = new Button("STAGE 3");

		btn1.getStyleClass().add("game-button");
		btn2.getStyleClass().add("game-button");
		btn3.getStyleClass().add("game-button");

		btn1.setPrefWidth(400);
		btn2.setPrefWidth(400);
		btn3.setPrefWidth(400);

		btn1.setPrefHeight(80);
		btn2.setPrefHeight(80);
		btn3.setPrefHeight(80);

		// ハイスコア表示
		Label scoreInfo = new Label("🏆");
		scoreInfo.setStyle("-fx-font-size: 50px;" + "-fx-text-fill: gold;");

		// 音声読み込み
		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());

		// 音声読み込み
		cancelSound = new AudioClip(getClass().getResource("/music/cancel.mp3").toExternalForm());

		// 音量調整
		clickSound.setVolume(0.4);

		// 音量調整
		cancelSound.setVolume(0.4);

		btn1.setOnAction(e -> {
			// 音を鳴らす
			clickSound.stop();
			clickSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));
			// 音と背景停止
			cleanup();

			// 画面遷移
			GameController.switchToPracticeGame1(stage);
		});

		btn2.setOnAction(e -> {
			// 音を鳴らす
			clickSound.stop();
			clickSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));
			// 音と背景停止
			cleanup();
			// 画面遷移！
			GameController.switchToPracticeGame2(stage);
		});

		btn3.setOnAction(e -> {

			// 音を鳴らす
			clickSound.stop();
			clickSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));
			// 音と背景停止
			cleanup();

			// 画面遷移
			GameController.switchToPracticeGame3(stage);
		});
		VBox stageButtons = new VBox(20, btn1, btn2, btn3);
		stageButtons.setAlignment(Pos.CENTER);

		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button");
		backButton.setPrefHeight(60);
		backButton.setPrefWidth(200);

		// ★ master側の処理を残す
		backButton.setOnAction(e -> {
			cancelSound.stop();
			cancelSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));

			// 待った後に画面遷移
			pause.setOnFinished(ev -> {
				// 背景停止
				cleanup();
				try {
					// 画面遷移
					GameController.switchStart(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			// タイマー開始
			pause.play();
		});

		// 背景用の画像を読み込み
		Image bgImage = new Image(Practice.class.getResource("/picture/background.png").toExternalForm());

		// 画像の元のサイズを取得
		double bgWidth = bgImage.getWidth();
		double bgHeight = bgImage.getHeight();

		// 背景をタイルのように敷き詰めるためのPaneを作成
		Pane bgPane = new Pane();
		final double[] scrollX = { 0 };

		// アニメーション
		this.timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// 1pxずつ左に動かす
				scrollX[0] -= 1;

				// 画像の横幅分動いたら0に戻す（これで無限ループ）
				if (scrollX[0] <= -bgWidth) {
					scrollX[0] = 0;
				}

				// 画像は元のサイズのまま、表示位置だけをずらして背景を再描画
				javafx.scene.paint.ImagePattern pattern = new javafx.scene.paint.ImagePattern(bgImage, scrollX[0], 0, // bgPane全体の背景をこのパターンで塗りつぶす
						bgWidth, bgHeight, // 画像の本来のサイズを維持
						false // 絶対座標指定
				);

				// bgPane全体の背景
				bgPane.setBackground(new javafx.scene.layout.Background(
						new javafx.scene.layout.BackgroundFill(pattern, null, null)));
			}
		};

		// ハイスコア用 表示
		Tooltip highScoreTip = new Tooltip(

				"★ HIGH SCORE ★\n\n"

						+ "STAGE1 : " + HighScoreManager.loadHighScore(1)

						+ "\n\nSTAGE2 : " + HighScoreManager.loadHighScore(2)

						+ "\n\nSTAGE3 : " + HighScoreManager.loadHighScore(3));

		highScoreTip.setStyle("-fx-font-family: 'PixelMplus12';" + "-fx-font-size: 32px;"
				+ "-fx-background-color: rgba(0,0,0,0.95);" + "-fx-text-fill: white;" + "-fx-padding: 20;");

		scoreInfo.setOnMouseClicked(e -> {

			if (highScoreTip.isShowing()) {

				highScoreTip.hide();

			} else {

				double x = e.getScreenX() - 350;
				double y = e.getScreenY() + 20;

				// 左にはみ出さない
				if (x < 20) {
					x = 20;
				}

				highScoreTip.show(scoreInfo, x, y);
			}
		});

		StackPane.setAlignment(scoreInfo, Pos.TOP_RIGHT);

		StackPane.setMargin(scoreInfo, new javafx.geometry.Insets(15, 15, 0, 0));

		StackPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);

		StackPane.setMargin(backButton, new javafx.geometry.Insets(0, 30, 30, 0));

		// ここから自動的にループ開始(AnimationTimerとペアで使用)
		this.timer.start();

		// 重ねて表示するためのレイアウト(レイヤー構造の作成)
		StackPane root = new StackPane();
		BorderPane ui = new BorderPane();

		BorderPane.setAlignment(scoreInfo, Pos.TOP_RIGHT);
		BorderPane.setMargin(scoreInfo, new javafx.geometry.Insets(20, 20, 0, 0));

		// UIが広がりすぎないよう最大幅を制限
		ui.setMaxWidth(800);
		ui.setTop(title);
		ui.setCenter(stageButtons);

		// タイトルラベル自体を上部の中央に配置する
		BorderPane.setAlignment(title, Pos.CENTER);

		// 「上200px、下20px」の余白をマージンとして直接設定する
		BorderPane.setMargin(title, new javafx.geometry.Insets(200, 0, 20, 0));

		// 下から背景、UIの箱に入れたものの順でレイヤー構造のrootに入れる
		root.getChildren().addAll(bgPane, ui, scoreInfo, backButton);

		StackPane.setAlignment(scoreInfo, Pos.TOP_RIGHT);

		StackPane.setMargin(scoreInfo, new javafx.geometry.Insets(15, 15, 0, 0));

		StackPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);

		StackPane.setMargin(backButton, new javafx.geometry.Insets(0, 30, 30, 0));

		// rootを中身とした1000×800のウィンドウを作成
		Scene scene = new Scene(root, 1000, 800);

		// ★初期値設定
		double fontSize = Math.max(18, scene.getWidth() * 0.02);

		highScoreTip.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:" + (int) fontSize + "px;"
				+ "-fx-background-color:rgba(0,0,0,0.95);" + "-fx-text-fill:white;" + "-fx-padding:15;");

		// ★リサイズ時更新
		scene.widthProperty().addListener((obs, oldVal, newVal) -> {

			double newFontSize = Math.max(18, newVal.doubleValue() * 0.02);

			highScoreTip.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:" + (int) newFontSize + "px;"
					+ "-fx-background-color:rgba(0,0,0,0.95);" + "-fx-text-fill:white;" + "-fx-padding:15;");
		});

		//
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		// CSSを接続
		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

		// ウィンドウの最小限のサイズを設定
		stage.setMinWidth(1000);
		stage.setMinHeight(800);
		stage.setMaxWidth(1920); // PC大画面やブラウザ最大化時の最大サイズ制限
		stage.setMaxHeight(1080);

		return scene;
	}

	public static void main(String[] args) {
		launch();
	}
}