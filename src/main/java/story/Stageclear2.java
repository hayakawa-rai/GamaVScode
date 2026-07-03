package story;

import javafx.animation.PauseTransition;
import javafx.application.Application;
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
import sample.Syujinkou;
import util.WindowUtil;

public class Stageclear2 extends Application {

	// ★ スコアを保持する変数
	private int finalScore = 0;

	// ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
	private Stage stage;

	// キャラクター保持用の変数
	private Syujinkou syujinkou;

	// ★★★ GameController の new Stageclear2() でエラーを出さないためのコンストラクタ ★★★
	public Stageclear2() {
		// 引数なしでインスタンス化できるように空で用意
	}

	// 既存の引数ありコンストラクタ
	public Stageclear2(Syujinkou syujinkou) {
		this.syujinkou = syujinkou;
		if (syujinkou != null) {
			this.finalScore = syujinkou.getScore();
		}
	}

	// ★ GameControllerからスコアを直接受け取るためのメソッド
	public void setScore(int score) {
		this.finalScore = score;
	}

	private AudioClip clearSound;
	private AudioClip clickSound;
	private AudioClip cancelSound;

	private PauseTransition delay;
	private PauseTransition pause;

	// メディアやタイマーを安全に停止するクリーンアップ処理
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

	// JavaFXのエントリーポイント
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("stage2CLEAR");
		WindowUtil.fillScreen(stage); // 先に最大化を確定
		stage.setScene(clear()); // その後でSceneをセット

	}

	// クリア画面のシーン生成
	public Scene clear() {
		// クリア音
		clearSound = new AudioClip(getClass().getResource("/music/yay.mp3").toExternalForm());
		clearSound.setVolume(0.5);

		// 0.5秒待つ
		delay = new PauseTransition(Duration.seconds(0.5));

		// 時間経過後に再生
		delay.setOnFinished(e -> {
			clearSound.play();
		});

		// タイマー開始
		delay.play();

		// どこのステージをクリアしたか表示する
		Text title = new Text("STAGE2    CLEAR!");
		title.setStyle("-fx-font-size: 80px; -fx-fill: rgb(180,180,180);");

		// ★★★ 獲得スコアを表示するテキスト ★★★
		Text scoreText = new Text("SCORE: " + finalScore);
		scoreText.setStyle("-fx-font-size: 32px; -fx-fill: #ffcc00; -fx-font-weight: bold;");

		// 獲得したアイテムを表示
		Text text = new Text("契約書を獲得しました！！");
		text.setStyle("-fx-font-size: 20px; -fx-fill: gray;");

		// 獲得したアイテムの画像読み込み
		Image image = new Image(getClass().getResource("/picture/keiyakusho.png").toExternalForm());
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(150);
		imageView.setFitHeight(150);

		// 横並びにする箱を設定
		HBox textAndImage = new HBox();
		textAndImage.setSpacing(10);
		textAndImage.setAlignment(Pos.CENTER);
		textAndImage.getChildren().addAll(imageView, text);

		// 縦並びにする箱を設定
		VBox buttonBox = new VBox();
		buttonBox.setSpacing(20);
		buttonBox.setAlignment(Pos.CENTER);

		// 音声読み込み
		clickSound = new AudioClip(getClass().getResource("/music/select.mp3").toExternalForm());
		clickSound.setVolume(0.4);

		cancelSound = new AudioClip(getClass().getResource("/music/cancel.mp3").toExternalForm());
		cancelSound.setVolume(0.4);

		// 次に進むボタン
		Button next = new Button("次のステージへ");
		next.getStyleClass().add("game-button2");
		next.setPrefSize(250, 80);
		next.setOnAction(e -> {
			clickSound.stop();
			clickSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));

			// 待った後に画面遷移
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					test.test2.GameController.switchStory3(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});

			// タイマー開始
			pause.play();
		});

		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		backButton.getStyleClass().add("game-button2");
		backButton.setPrefSize(250, 80);
		backButton.setOnAction(e -> {
			cancelSound.stop();
			cancelSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));

			// 待った後に画面遷移
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					test.test2.GameController.switchStart(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			// タイマー開始
			pause.play();
		});

		// 各パーツを縦並びの箱に入れる
		buttonBox.getChildren().addAll(title, scoreText, textAndImage, next, backButton);
		// 現在のStage（window）から実際のサイズを取得する
		StackPane root = new StackPane();
		root.getChildren().add(buttonBox);
		// 固定サイズを渡さない。StageのサイズにScene側が自動追従する。
		Scene scene = new Scene(root);
		// ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
		stage.setMinWidth(1000);
		stage.setMinHeight(800);

		// CSSを接続
		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

		return scene;
	}
}