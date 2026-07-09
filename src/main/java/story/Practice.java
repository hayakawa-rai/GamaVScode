package story;

import common.HighScoreManager;
import control.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;
import start.Bgm;
import util.ResponsiveUtil;

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
		stage.show();
		// 2. 画面が表示された「次のフレーム」でアニメーションを開始する
	    Platform.runLater(() -> {
	        if (timer != null) {
	            timer.start();
	        }
	    });
	}

	public Scene createScene() {

		// 重ねて表示するためのレイアウト(レイヤー構造の作成)
		StackPane root = new StackPane();
		
		// 1000×800のベースウィンドウを作成（以降はレスポンシブ可変）
		Scene scene = new Scene(root, 1000, 800);

		// タイトル
		Label title = new Label("練習モード");
		title.getStyleClass().add("responsive-title"); // スタイルは基本的にCSSかバインドで行う
		title.setStyle("-fx-font-family: 'PixelMplus12';" +
				"-fx-text-fill: white;" + "-fx-effect: dropshadow(gaussian, rgba(0,120,220,0.8), 20, 0.6, 0, 3);");

		// ステージ選択ボタン
		Button btn1 = new Button("STAGE 1");
		Button btn2 = new Button("STAGE 2");
		Button btn3 = new Button("STAGE 3");

		btn1.getStyleClass().add("game-button");
		btn2.getStyleClass().add("game-button");
		btn3.getStyleClass().add("game-button");

		// ハイスコア表示用（トロフィーアイコン）
		Label scoreInfo = new Label("🏆");
		
		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button");

		// 音声読み込み
		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());
		cancelSound = new AudioClip(getClass().getResource("/music/cancel.mp3").toExternalForm());

		// 音量調整
		clickSound.setVolume(0.4);
		cancelSound.setVolume(0.4);

		// ボタンアクション設定
		btn1.setOnAction(e -> {
			clickSound.stop();
			clickSound.play();
			Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
				cleanup();
				GameController.switchToPracticeGame1(stage);
			}));
			delay.play();
		});

		btn2.setOnAction(e -> {
			clickSound.stop();
			clickSound.play();
			Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
				cleanup();
				GameController.switchToPracticeGame2(stage);
			}));
			delay.play();
		});

		btn3.setOnAction(e -> {
			clickSound.stop();
			clickSound.play();
			Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
				cleanup();
				GameController.switchToPracticeGame3(stage);
			}));
			delay.play();
		});

		backButton.setOnAction(e -> {
			cancelSound.stop();
			cancelSound.play();
			Timeline delay = new Timeline(new KeyFrame(Duration.millis(500), ev -> {
				cleanup();
				try {
					GameController.switchStart(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}));
			delay.play();
		});

		// ステージボタンをまとめる箱
		VBox stageButtons = new VBox(20, btn1, btn2, btn3);
		stageButtons.setAlignment(Pos.CENTER);

		// 画面中央に並べるUI全体の箱（タイトル＋ステージボタン）
		VBox uiContainer = new VBox(40, title, stageButtons);
		uiContainer.setAlignment(Pos.CENTER);

		// 背景用の画像を読み込み
		Image bgImage = new Image(Practice.class.getResourceAsStream("/picture/background.png"));
		double bgWidth = bgImage.getWidth();
		double bgHeight = bgImage.getHeight();

		Pane bgPane = new Pane();
		final double[] scrollX = { 0 };

		// 背景アニメーション
		this.timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				scrollX[0] -= 1;
				if (scrollX[0] <= -bgWidth) {
					scrollX[0] = 0;
				}
				ImagePattern pattern = new ImagePattern(bgImage, scrollX[0], 0, bgWidth, bgHeight, false);
				bgPane.setBackground(new Background(new BackgroundFill(pattern, null, null)));
			}
		};

		// ハイスコア用ツールチップ設定
		Tooltip highScoreTip = new Tooltip(
				"★ HIGH SCORE ★\n\n"
						+ "STAGE1 : " + HighScoreManager.loadHighScore(1)
						+ "\n\nSTAGE2 : " + HighScoreManager.loadHighScore(2)
						+ "\n\nSTAGE3 : " + HighScoreManager.loadHighScore(3));

		scoreInfo.setOnMouseClicked(e -> {
			if (highScoreTip.isShowing()) {
				highScoreTip.hide();
			} else {
				double x = e.getScreenX() - 350;
				double y = e.getScreenY() + 20;
				if (x < 20) {
					x = 20;
				}
				highScoreTip.show(scoreInfo, x, y);
			}
		});

		// レイヤー構造の組み立て
		root.getChildren().addAll(bgPane, uiContainer, scoreInfo, backButton);

		// 右上配置の固定パーツ（トロフィー）
		StackPane.setAlignment(scoreInfo, Pos.TOP_RIGHT);
		StackPane.setMargin(scoreInfo, new Insets(15, 15, 0, 0));

		// 右下配置の固定パーツ（戻るボタン）
		StackPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(backButton, new Insets(0, 30, 30, 0));


		// ===== 📐 レスポンシブ対応の追加 📐 =====

		// 1. 背景Paneをシーンサイズに追従させる
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		// 2. メインUIコンテナの横幅を画面の90%に制限（スマホでの横はみ出し防止）
		ResponsiveUtil.bindMaxWidth(uiContainer, scene.widthProperty(), 0.9);

		// 3. タイトル文字の大きさを画面幅の4.8%に可変（最小24px〜最大48px）
		title.styleProperty().bind(
			javafx.beans.binding.Bindings.concat(
				"-fx-font-family: 'PixelMplus12'; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,120,220,0.8), 20, 0.6, 0, 3); -fx-font-size: ",
				javafx.beans.binding.Bindings.max(24, scene.widthProperty().multiply(0.048)).asString("%.0fpx;")
			)
		);

		// 4. トロフィーアイコンの大きさを画面幅の5%に可変（最小24px〜最大50px）
		scoreInfo.styleProperty().bind(
			javafx.beans.binding.Bindings.concat(
				"-fx-text-fill: gold; -fx-font-size: ",
				javafx.beans.binding.Bindings.max(24, scene.widthProperty().multiply(0.05)).asString("%.0fpx;")
			)
		);

		// 5. ステージボタン群（btn1〜3）を画面幅の35%、高さ10%に追従（スマホ用の最小サイズも確保）
		for (Button b : new Button[] { btn1, btn2, btn3 }) {
			ResponsiveUtil.bindPrefSize(b, scene.widthProperty(), 0.35, scene.heightProperty(), 0.10, 160, 44);
			ResponsiveUtil.bindButtonFontAndPadding(b, scene.widthProperty(), 14, 0.045, 26, 8, 0.02, 20);
		}

		// 6. 戻るボタンを画面幅の20%、高さ7%に追従（最小サイズ 120x36）
		ResponsiveUtil.bindPrefSize(backButton, scene.widthProperty(), 0.20, scene.heightProperty(), 0.07, 120, 36);
		ResponsiveUtil.bindButtonFontAndPadding(backButton, scene.widthProperty(), 12, 0.035, 20, 6, 0.015, 14);

		// 7. ツールチップのフォントサイズ動的リサイズ（元のロジックを流用）
		double initialFontSize = Math.max(18, scene.getWidth() * 0.02);
		highScoreTip.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:" + (int) initialFontSize + "px;"
				+ "-fx-background-color:rgba(0,0,0,0.95);" + "-fx-text-fill:white;" + "-fx-padding:15;");

		scene.widthProperty().addListener((obs, oldVal, newVal) -> {
			double newFontSize = Math.max(18, newVal.doubleValue() * 0.02);
			highScoreTip.setStyle("-fx-font-family:'PixelMplus12';" + "-fx-font-size:" + (int) newFontSize + "px;"
					+ "-fx-background-color:rgba(0,0,0,0.95);" + "-fx-text-fill:white;" + "-fx-padding:15;");
		});

		// ===== レスポンシブ対応ここまで =====


		// CSSを接続
		var cssUrl = getClass().getResource("/css/style.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}

		// ウィンドウサイズ制限をスマホ対応に変更
		stage.setMinWidth(320);
		stage.setMinHeight(480);
		// 最大サイズ制限は解除し、ブラウザいっぱいに広げられるようにする

		return scene;
	}

	public static void main(String[] args) {
		launch();
	}
}