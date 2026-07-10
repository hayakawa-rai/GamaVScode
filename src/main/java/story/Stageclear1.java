package story;

import control.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.WindowUtil;

public class Stageclear1 extends Application {

	private AudioClip clearSound;
	private AudioClip clickSound;
	private AudioClip cancelSound;
	private int score = 0;
	private PauseTransition delay;
	private PauseTransition pause;
	private Stage stage;

	public Stageclear1() {
		// 引数なしコンストラクタ（JProの動的生成に必須）
	}

	// 💡【JPro対応】外部からStageとスコアを安全に引き継いで開始する静的メソッド
	public static void createAndStart(Stage currentStage, int finalScore) {
		if (currentStage == null)
			return;

		Stageclear1 instance = new Stageclear1();
		instance.setScore(finalScore);
		instance.stage = currentStage;// スコアを確実に格納

		// 1. ボタンやテキストが含まれる新しいSceneを完全に作成
		Scene newScene = instance.clear(currentStage);

		// 2. setRootではなく、Stageに対してSceneごと安全に丸ごと差し替える
		currentStage.setScene(newScene);
		currentStage.setTitle("stage1CLEAR");
		currentStage.centerOnScreen();
		WindowUtil.fullScreen(currentStage);
		currentStage.show();
	}

	private void cleanup() {
		if (delay != null) {
			delay.stop();
			delay = null;
		}
		if (pause != null) {
			pause.stop();
			pause = null;
		}
		if (clearSound != null) {
			clearSound.stop();
			clearSound = null;
		}
		if (clickSound != null) {
			clickSound.stop();
			clickSound = null;
		}
		if (cancelSound != null) {
			cancelSound.stop();
			cancelSound = null;
		}
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("stage1CLEAR");
		stage.setScene(clear(stage));
		stage.centerOnScreen();
		WindowUtil.fullScreen(stage);
		stage.show();
	}

	public Scene clear() {
		return clear(this.stage);
	}

	public Scene clear(Stage currentStage) {
		if (currentStage != null) {
			this.stage = currentStage;
		}
		// クリア音
		clearSound = new AudioClip(
				getClass().getResource("/music/yay.mp3").toExternalForm());
		clearSound.setVolume(0.5);

		delay = new PauseTransition(Duration.seconds(0.5));
		delay.setOnFinished(e -> {
			if (clearSound != null)
				clearSound.play();
		});
		delay.play();

		// -------------------------------------------------
		// 📦 ベースコンテナとScene設定
		// -------------------------------------------------
		StackPane root = new StackPane();
		
		// 💡 固定値(1000, 800)の生成制限を排除！ 柔軟にサイズが引き継がれるようにする
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());

		// 💡 画面の縦横で「短いほうの長さ」を基準にする（スマホの縦持ちでも画面外にはみ出さなくなる魔法）
		NumberBinding minSide = Bindings.min(scene.widthProperty(), scene.heightProperty());

		VBox buttonBox = new VBox();
		buttonBox.setAlignment(Pos.CENTER);
		// パーツ同士の間隔も画面サイズに合わせて変動
		buttonBox.spacingProperty().bind(minSide.multiply(0.035));
		root.getChildren().add(buttonBox);

		// -------------------------------------------------
		// 🔤 各UI要素のレスポンシブ化（フォント・画像の自動伸縮）
		// -------------------------------------------------
		Text title = new Text("STAGE1    CLEAR!");
		// 💡 画面の大きさに合わせて、文字サイズが自動計算されるようにバインド
		title.styleProperty().bind(Bindings.format(
				"-fx-font-size: %.0fpx; -fx-fill: rgb(180,180,180); -fx-font-weight: bold;", 
				minSide.multiply(0.075))); 

		Text text = new Text("鍵を獲得しました！！");
		text.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: gray;", minSide.multiply(0.025)));

		// 鍵の画像を読み込み
		Image image = new Image(
				getClass().getResourceAsStream("/picture/kagi.png"));
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);
		// 鍵のサイズも画面の大きさに連動
		imageView.fitWidthProperty().bind(minSide.multiply(0.15));

		HBox textAndImage = new HBox();
		textAndImage.setAlignment(Pos.CENTER);
		textAndImage.spacingProperty().bind(minSide.multiply(0.015));
		textAndImage.getChildren().addAll(imageView, text);

		clickSound = new AudioClip(
				getClass().getResource("/music/select.mp3").toExternalForm());
		clickSound.setVolume(0.4);

		cancelSound = new AudioClip(
				getClass().getResource("/music/cancel.mp3").toExternalForm());
		cancelSound.setVolume(0.4);

		// -------------------------------------------------
		// 🖱️ ボタンコンポーネント（固定値を廃止してレスポンシブ化）
		// -------------------------------------------------
		// 次に進むボタン
		Button next = new Button("次のステージへ");
		next.getStyleClass().add("game-button2");
		
		// 💡 ボタンの横幅・縦幅・文字の大きさを画面に合わせて自動伸縮
		next.prefWidthProperty().bind(minSide.multiply(0.4));
		next.prefHeightProperty().bind(minSide.multiply(0.12));
		next.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-font-weight: bold;", minSide.multiply(0.028)));
		
		next.setOnAction(e -> {
			if (clickSound != null) {
				clickSound.stop();
				clickSound.play();
			}
			pause = new PauseTransition(Duration.seconds(0.5));
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					GameController.switchStory2(this.stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			pause.play();
		});

		// スコア表示
		Text scoreLabel = new Text("SCORE: " + this.score);
		scoreLabel.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: gray;", minSide.multiply(0.035)));

		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button2");
		
		// サイズと文字の大きさは上のボタンと同じ比率で追従
		backButton.prefWidthProperty().bind(minSide.multiply(0.4));
		backButton.prefHeightProperty().bind(minSide.multiply(0.12));
		backButton.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-font-weight: bold;", minSide.multiply(0.028)));
		
		backButton.setOnAction(e -> {
			if (cancelSound != null) {
				cancelSound.stop();
				cancelSound.play();
			}
			pause = new PauseTransition(Duration.seconds(0.5));
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					GameController.switchStart(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			pause.play();
		});

		// 全パーツを格納
		buttonBox.getChildren().addAll(title, textAndImage, scoreLabel, next, backButton);

		// 最後にWindowUtilを呼ぶことで、JPro環境ならサイズ制限の適用だけを行って安全に終了します
		WindowUtil.fullScreen(stage);
		return scene;
	}

	public void setScore(int score) {
		this.score = score;
	}
}