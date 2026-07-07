package story;

import control.GameController;
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

public class Stageclear2 extends Application {

	private AudioClip clearSound;
	private AudioClip clickSound;
	private AudioClip cancelSound;
	// スコアを数値として保持する変数を追加
	private int score = 0;
	private PauseTransition delay;
	private PauseTransition pause;
	// ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
	private Stage stage;

	// 引数なしのコンストラクタ（GameControllerの new Stageclear2() で必要）
	public Stageclear2() {
		// 引数なしでインスタンス化できるように空で用意
	}

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

	// javafxではstartを呼び出さないと起動しないため、親クラスのstartを上書きすることで子クラスを起動
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("stage2CLEAR");
		//WindowUtil.fillScreen(stage);	最大化
	    stage.setScene(clear(stage)); // 安全にstageを渡す
		stage.centerOnScreen();
		stage.show();
	}
	
	// 引数なしの clear() 
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
		// フォントサイズとカラーを指定
		title.setStyle("-fx-font-size: 80px; -fx-fill: rgb(180,180,180);");

		// 獲得したアイテムを表示
		Text text = new Text("契約書を獲得しました！！");
		// フォントサイズとカラーを指定
		text.setStyle("-fx-font-size: 20px; -fx-fill: gray;");

		// 獲得したアイテムの画像読み込み
		Image image = new Image(getClass().getResource("/picture/keiyakusho.png").toExternalForm());
		// 読み込んだ画像を表示
		ImageView imageView = new ImageView(image);
		// 画像のサイズ調整
		imageView.setFitWidth(150);
		imageView.setFitHeight(150);

		// 横並びにする箱を設定
		HBox textAndImage = new HBox();
		// textと画像の間隔を設定
		textAndImage.setSpacing(10);
		// 中央に配置
		textAndImage.setAlignment(Pos.CENTER);
		// 画像とtextを箱に入れる
		textAndImage.getChildren().addAll(imageView, text);

		// 音声読み込み
		clickSound = new AudioClip(
				getClass().getResource("/music/select.mp3").toExternalForm());
		// 音量調整
		clickSound.setVolume(0.4);
		
		// 音声読み込み
		cancelSound = new AudioClip(
				getClass().getResource("/music/cancel.mp3").toExternalForm());
		// 音量調整
		cancelSound.setVolume(0.4);

		// 次に進むボタン
		Button next = new Button("次のステージへ");
		// ボタンにcssに記述したgame-button2を付与、ボタンサイズを指定
		next.getStyleClass().add("game-button2");
		next.setPrefSize(250, 80);
		// 次の画面に遷移
		next.setOnAction(e -> {
			// 音を再生
			clickSound.stop();
			clickSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));

			// 待った後に画面遷移
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					// 画面遷移
					GameController.switchStory3(this.stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});

			// タイマー開始
			pause.play();
		});

		// スコア表示
		Text scoreLabel = new Text("SCORE: " + this.score);
		scoreLabel.setStyle("-fx-font-size: 30px; -fx-fill:  gray;");
		
		// 戻るボタン
		Button backButton = new Button("タイトルへ");
		// ボタンにcssに記述したgame-button2を付与、ボタンサイズを指定
		backButton.getStyleClass().add("game-button2");
		backButton.setPrefSize(250, 80);
		// スタート画面へ戻る
		backButton.setOnAction(e -> {
			cancelSound.stop();
			cancelSound.play();

			// 0.5秒待つ
			pause = new PauseTransition(Duration.seconds(0.5));
			// 待った後に画面遷移
			pause.setOnFinished(ev -> {
				try {
					cleanup();
					// 画面遷移
					GameController.switchStart(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			// タイマー開始
			pause.play();
		});

		// 縦並びにする箱を設定
		VBox buttonBox = new VBox(20); // 隙間20px
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(title, textAndImage, scoreLabel, next, backButton);
		// 現在のStage（window）から実際のサイズを取得する
		StackPane root = new StackPane();
		root.getChildren().add(buttonBox);
		// 
		Scene scene = new Scene(root, 1000, 800);
		// CSSを接続
		scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
		//ウィンドウの最小限のサイズを設定
  		stage.setMinWidth(1000);
  		stage.setMinHeight(800);
  		stage.setMaxWidth(1920);  // PC大画面やブラウザ最大化時の最大サイズ制限
  		stage.setMaxHeight(1080);
		return scene;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
}