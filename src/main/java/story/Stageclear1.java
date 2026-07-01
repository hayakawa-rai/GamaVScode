package story;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import test.test2.GameController;

public class Stageclear1 extends Application {
	
	// ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
	private Stage stage;
	
	// ★ スコアを数値として保持する変数を追加
	private int score = 0;

	// ★ 引数なしのコンストラクタ（GameControllerの new Stageclear1() で必要）
	public Stageclear1() {
	}

	// ★ GameControllerの App.setScore(score); でスコアを受け取るためのメソッド
	public void setScore(int score) {
		this.score = score;
	}

	// javafxではstartを呼び出さないと起動しないため、親クラスのstartを上書きすることで子クラスを起動
	@Override
	public void start(Stage stage) {
		// 受け取った変数Stageを自分のStageに保存
		this.stage = stage;
		// ウィンドウの中身を決定
		stage.setScene(clear());
		stage.setTitle("stage1CLEAR");
		stage.show();
	}
	
	private AudioClip clearSound;
	private AudioClip clickSound;
	private AudioClip cancelSound;
	private PauseTransition delay;
	private PauseTransition pause;

	private void cleanup() {
		// 遅延処理
		if (delay != null) {
			delay.stop();
			delay = null;
		}

		if (pause != null) {
			pause.stop();
			pause = null;
		}

		// 効果音停止
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

	public Scene clear() {
		// クリア音
		clearSound = new AudioClip(
			getClass().getResource("/music/yay.mp3").toExternalForm()
		);
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
		Text title = new Text("STAGE1    CLEAR!");
		// フォントサイズとカラーを指定
		title.setStyle("-fx-font-size: 80px; -fx-fill: rgb(180,180,180);");
		// 獲得したアイテムを表示
		Text text = new Text("鍵を獲得しました！！");
		// フォントサイズとカラーを指定
		text.setStyle("-fx-font-size: 20px; -fx-fill: gray;");
		// 獲得したアイテムの画像読み込み
		Image image = new Image(
				getClass().getResource("/picture/kagi.png").toExternalForm()
		);
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

		// 縦並びにする箱を設定
		VBox buttonBox = new VBox();
		// ボタン配置に間隔を設定
		buttonBox.setSpacing(20); 
		// 中央に配置
		buttonBox.setAlignment(Pos.CENTER);
		
		// 音声読み込み
		clickSound = new AudioClip(
			getClass().getResource("/music/select.mp3").toExternalForm()
		);
		// 音量調整
		clickSound.setVolume(0.4);
		
		// 音声読み込み
		cancelSound = new AudioClip(
			getClass().getResource("/music/cancel.mp3").toExternalForm()
		);
		// 音量調整
		cancelSound.setVolume(0.4);
			
		// スコア表示
		Text scoreLabel = new Text();
		
		// ★引数で受け取った score 変数を使ってテキストを設定する形に変更
		scoreLabel.setText("SCORE: " + this.score);
		scoreLabel.setStyle("-fx-font-size: 30px; -fx-fill:  gray;");
		
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
					GameController.switchStory2(stage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});

			// タイマー開始
			pause.play();
		});

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

		// titleと画像とtextをまとめたもの、ボタン2つを箱に入れる。
		buttonBox.getChildren().addAll(title, textAndImage, scoreLabel, next, backButton);
		
		// 現在のStage（window）から実際のサイズを取得する
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();

        // 取得したサイズで新しいSceneを作成
        Scene scene = new Scene(buttonBox, currentWidth, currentHeight);
        stage.setScene(scene);
		// ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		// CSSを接続
		scene.getStylesheets().add(
			getClass().getResource("/css/style.css").toExternalForm()
		);
		// 画面に表示させたいものを返す
		return scene;
	}
}