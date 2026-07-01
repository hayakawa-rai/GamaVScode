package start;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Start extends Application {
	private AnimationTimer timer;
	private AudioClip clickSound;

	private void cleanup() {

		// 背景アニメーション停止
		if (timer != null) {
			timer.stop();
			timer = null;
		}

		// 効果音停止
		if (clickSound != null) {
			clickSound.stop();
			clickSound = null;
		}

		// BGM停止
		Bgm.stopBGM();
	}

	//javafxのApplicationにもともとあるstartを上書き
	@Override
	public void start(Stage stage) {
		
		// startメソッドの最初の方に配置
		try {
		    // 1. ファイルをストリームとして読み込む
		    var fontStream = getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf");
		    
		    if (fontStream == null) {
		        // コンソールにこの警告が出たら、フォントファイルの場所（パス）が間違っています
		        System.err.println("【警告】フォントファイルが見つかりません。パスを確認してください。");
		    } else {
		        // 2. JavaFXにフォントを登録
		        Font loadedFont = Font.loadFont(fontStream, 20);
		        
		        if (loadedFont == null) {
		            System.err.println("【警告】フォントファイルの読み込みに失敗しました（ファイルが壊れているか形式が違います）");
		        } else {
		            // 成功した場合、JavaFXが認識した正確な「フォント名」をコンソールに出します
		            System.out.println("【成功】フォントを読み込みました。名前: " + loadedFont.getName());
		        }
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

		// ★2. 画面を作る前にレトロフォントファイルを読み込む
		// 他のリソース（pictureやmusic）と同じ並びの「font」フォルダに置くのがオススメです
		try {
			Font.loadFont(getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf"), 20);
		} catch (Exception e) {
			System.err.println("フォントの読み込みに失敗しました: " + e.getMessage());
		}


		//背景用の画像を読み込み
		Image bgImage = new Image(
				getClass().getResource("/picture/background.png").toExternalForm());

		// 背景用画像表示を２つ設定
		ImageView bg1 = new ImageView(bgImage);
		ImageView bg2 = new ImageView(bgImage);

		//bg2をbg1の横に置く(bg2の位置を取得した画像の横幅分右に配置)
		bg2.setLayoutX(bgImage.getWidth());

		// AnimationTimer:javafxでのループ処理(handle()のみを繰り返し呼び出す)
		timer = new AnimationTimer() {
			//Applicationにhandleというメソッドがあるため書き換え(AnimationTimerはhandl()しか呼び出せないためhandleを使用)
			@Override
			public void handle(long now) {
				//背景画像を1pxづつ左に動かしている
				bg1.setLayoutX(bg1.getLayoutX() - 1);
				bg2.setLayoutX(bg2.getLayoutX() - 1);

				// 画面外に出たらループ
				//big1が画面外に完全に出たらbg2の右端に移動
				if (bg1.getLayoutX() + bgImage.getWidth() <= 0) {
					bg1.setLayoutX(bg2.getLayoutX() + bgImage.getWidth());
				}
				//big2が画面外に完全に出たらbg1の右端に移動
				if (bg2.getLayoutX() + bgImage.getWidth() <= 0) {
					bg2.setLayoutX(bg1.getLayoutX() + bgImage.getWidth());
				}
			}
		};
		//ここから自動的にループ開始(AnimationTimerとペアで使用)
		timer.start();
		//重ねて表示するためのレイアウト(レイヤー構造の作成)
		StackPane root = new StackPane();

		// 背景用の画像をbagPaneに追加
		Pane bgPane = new Pane();
		bgPane.getChildren().addAll(bg1, bg2);

		//縦に並べるための箱を作成
		VBox ui = new VBox();
		//uiによる配置の間隔を設定
		ui.setSpacing(20);
		//中央に設定
		ui.setAlignment(Pos.CENTER);

		//title用の画像読み込み
		Image image = new Image(getClass().getResource("/picture/title.png").toExternalForm());
		//画像を表示、画像サイズを調整
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(500);
		imageView.setFitHeight(400);
		// 縦横比維持
		imageView.setPreserveRatio(true);

		//縦に並べるための箱を作成
		VBox buttonBox = new VBox();
		//buttonBoxによる配置の間隔を設定
		buttonBox.setSpacing(20);
		//中央に設定
		buttonBox.setAlignment(Pos.CENTER);
		//BGMの再生
		Bgm.playBGM("/music/startbgm.mp3");

		//音声読み込み
		clickSound = new AudioClip(
				getClass().getResource("/music/select.mp3").toExternalForm());
		// 音量調整
		clickSound.setVolume(0.4);

		//ストーリーモードへ飛ぶボタンを作成
		Button btn1 = new Button("▶ストーリー");
		btn1.setPrefSize(300, 100);
		//btn1にCSSのgame-buttonを付与
		btn1.getStyleClass().add("game-button");
		btn1.setOnAction(e -> {
			try {
				// 音を鳴らす
				clickSound.stop();
				clickSound.play();
				// 0.15秒後に画面遷移
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							//音と背景停止
							cleanup();
							//画面遷移
							test.test2.GameController.startToStory(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		//練習モードへ飛ぶボタン作成
		Button btn2 = new Button("⚔練習モード");
		btn2.setPrefSize(300, 100);
		//setOnAction:クリックしたときに実行する処理を記述
		//(e->:クリックされたら実行される処理を書いていくという記号)
		btn2.setOnAction(e -> {
			try {
				// 音を鳴らす
				clickSound.stop();
				clickSound.play();

				// 0.15秒後に画面遷移
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							// 背景停止
							timer.stop();
							// 画面遷移
							control.GameController.switchToPractice(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		//btn2にCSSのgame-buttonを付与
		btn2.getStyleClass().add("game-button");

		//無限モードへ飛ぶボタン作成
		Button btn3 = new Button("🔚ゲーム終了");
		btn3.setPrefSize(300, 100);
		//btn3にCSSのgame-buttonを付与
		btn3.getStyleClass().add("game-button");

		// クリックしたときにアプリを終了する処理
		btn3.setOnAction(e -> {
			try {
				// 効果音を鳴らす
				clickSound.stop();
				clickSound.play();

				// 他のボタンに合わせて0.5秒後にアプリを閉じる
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {
							// 背景アニメーションやBGMを安全に停止
							cleanup();
							// JavaFXアプリを終了する
							Platform.exit();
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//3個のボタンを縦に並ぶ箱に入れる
		buttonBox.getChildren().addAll(btn1, btn2, btn3);

		//titleの画像と3個のボタンが入った箱を縦に並ぶ箱に入れる
		ui.getChildren().addAll(imageView, buttonBox);
		//下から背景、uiの箱に入れたものの順でレイヤー構造のrootに入れる
		root.getChildren().addAll(bgPane, ui);

		//rootを中身とした1000×800のウィンドウを作成
		Scene scene = new Scene(root, 1000, 800);
		//CSSを接続
		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());
		//ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		//ウィンドウの名前を設定
		stage.setTitle("スタート画面");
		//ウィンドウの中身を設定・表示
		stage.setScene(scene);
		stage.show();
	}

	//launchをmainで呼び出すことでjavafxのアプリが起動
	public static void main(String[] args) {
		launch();
	}

}