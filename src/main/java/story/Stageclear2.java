package story;

import control.GameController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
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

public class Stageclear2 extends Application {

	private AudioClip clearSound;
	private AudioClip clickSound;
	private AudioClip cancelSound;
	private int score = 0;
	private PauseTransition delay;
	private PauseTransition pause;
	private Stage stage;

	public Stageclear2() {
		// 引数なしコンストラクタ（JProの動的生成に必須）
	}

	// 💡【JPro対応】外部からStageとスコアを安全に引き継いで開始する静的メソッド
	public static void createAndStart(Stage currentStage, int finalScore) {
		if (currentStage == null) 
			return;

		Stageclear2 instance = new Stageclear2();
		instance.stage = currentStage;
		instance.setScore(finalScore); // スコアを確実に格納

		// Stageとスコアがセットされた状態でSceneを構築
		Scene newScene = instance.clear(currentStage);

		// 2. setRootではなく、Stageに対してSceneごと安全に丸ごと差し替える
		currentStage.setScene(newScene);
		currentStage.setTitle("stage2CLEAR");
		currentStage.centerOnScreen();
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
		stage.setTitle("stage2CLEAR");
		stage.setScene(clear(stage));
		stage.centerOnScreen();
		stage.show();
	}
	
	public Scene clear() {
		return clear(this.stage);
	}

	public Scene clear(Stage currentStage) {
		if (currentStage != null) {
			this.stage = currentStage;
		}

		// ⭕【JPro対応】クリア音の読み込みをtry-catchで保護
		try {
			var yayUrl = getClass().getResource("/music/yay.mp3");
			if (yayUrl != null) {
				clearSound = new AudioClip(yayUrl.toExternalForm());
				clearSound.setVolume(0.5);
			}
		} catch (Exception e) {
			System.err.println("クリア音の読み込みに失敗しました: " + e.getMessage());
		}

		delay = new PauseTransition(Duration.seconds(0.5));
		delay.setOnFinished(e -> {
			if (clearSound != null) {
				clearSound.play();
			}
		});
		delay.play();

		// ⭕【エラー修正】全角スペースや特殊な非表示スペースを除去し、安全な半角スペースに統一
		Text title = new Text("STAGE2    CLEAR!");
		title.setStyle("-fx-fill: rgb(180,180,180);");

		Text text = new Text("契約書を獲得しました！！");
		text.setStyle("-fx-fill: gray;");

		// ⭕【JPro対応】getResourceAsStream に修正して画像の生読み込みを安全化
		ImageView imageView = new ImageView();
		try {
			var imgStream = getClass().getResourceAsStream("/picture/keiyakusho.png");
			if (imgStream != null) {
				imageView.setImage(new Image(imgStream));
			}
		} catch (Exception e) {
			System.err.println("契約書画像の読み込みに失敗しました: " + e.getMessage());
		}

		HBox textAndImage = new HBox();
		textAndImage.setSpacing(10);
		textAndImage.setAlignment(Pos.CENTER);
		textAndImage.getChildren().addAll(imageView, text);

		// ⭕【JPro対応】効果音（クリック・キャンセル）の読み込みをtry-catchで一括保護
		try {
			var selectUrl = getClass().getResource("/music/select.mp3");
			if (selectUrl != null) {
				clickSound = new AudioClip(selectUrl.toExternalForm());
				clickSound.setVolume(0.4);
			}
		} catch (Exception e) {
			System.err.println("選択SEの読み込みに失敗しました: " + e.getMessage());
		}
		
		try {
			var cancelUrl = getClass().getResource("/music/cancel.mp3");
			if (cancelUrl != null) {
				cancelSound = new AudioClip(cancelUrl.toExternalForm());
				cancelSound.setVolume(0.4);
			}
		} catch (Exception e) {
			System.err.println("キャンセルSEの読み込みに失敗しました: " + e.getMessage());
		}

		// 次に進むボタン
		Button next = new Button("次のステージへ");
		next.getStyleClass().add("game-button2");
		next.setOnAction(e -> {
			// 音を再生（Nullチェックをして安全に再生・停止）
			if (clickSound != null) {
				clickSound.stop();
				clickSound.play();
			}

			// 0.5秒待つ（タイマー開始）
			pause = new PauseTransition(Duration.seconds(0.5));
			
			// 待った後に画面遷移
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					GameController.switchStory3(this.stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			pause.play();
		});

		// スコア表示
		Text scoreLabel = new Text("SCORE: " + this.score);
		scoreLabel.setStyle("-fx-fill: gray;");
		
		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button2");
		backButton.setOnAction(e -> {
			if (cancelSound != null) {
				cancelSound.stop();
				cancelSound.play();
			}

			pause = new PauseTransition(Duration.seconds(0.5));
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					GameController.switchStart(this.stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			pause.play();
		});

		// 全パーツを格納するコンテナ
		VBox buttonBox = new VBox(20);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(title, textAndImage, scoreLabel, next, backButton);

		StackPane root = new StackPane();
		root.getChildren().add(buttonBox);
		
		Scene scene = new Scene(root, 1000, 800);
		
		// ⭕【JPro対応】CSS読み込みに安全対策を追加
		var cssUrl = getClass().getResource("/css/style.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}

		// ブラウザの画面リサイズに追従する動的バインディング
		title.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: rgb(180,180,180); -fx-font-weight: bold;",scene.widthProperty().multiply(0.06)));
		text.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: gray;", scene.widthProperty().multiply(0.015)));
		scoreLabel.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: gray; -fx-font-weight: bold;",scene.widthProperty().multiply(0.02)));

		imageView.fitWidthProperty().bind(scene.widthProperty().multiply(0.15));
		imageView.fitHeightProperty().bind(scene.heightProperty().multiply(0.18));
		imageView.setPreserveRatio(true);

		next.prefWidthProperty().bind(scene.widthProperty().multiply(0.17));
		next.prefHeightProperty().bind(scene.heightProperty().multiply(0.09));
		backButton.prefWidthProperty().bind(scene.widthProperty().multiply(0.17));
		backButton.prefHeightProperty().bind(scene.heightProperty().multiply(0.09));

		next.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx;", scene.widthProperty().multiply(0.013)));
		backButton.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx;", scene.widthProperty().multiply(0.013)));

		if (this.stage != null) {
			this.stage.setMinWidth(1000);
			this.stage.setMinHeight(800);
			this.stage.setMaxWidth(1920);
			this.stage.setMaxHeight(1080);
		}
		return scene;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}