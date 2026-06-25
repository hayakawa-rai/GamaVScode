package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

public class start extends Application {
	
	//javafxのApplicationにもともとあるstartを上書き
	@Override
	public void start(Stage stage) {
		//背景用の画像を読み込み
		Image bgImage = new Image(
				getClass().getResource("/picture/background.png").toExternalForm());

		// 背景用画像表示を２つ設定
		ImageView bg1 = new ImageView(bgImage);
		ImageView bg2 = new ImageView(bgImage);

		//bg2をbg1の横に置く(bg2の位置を取得した画像の横幅分右に配置)
		bg2.setLayoutX(bgImage.getWidth());

		// AnimationTimer:javafxでのループ処理(handle()のみを繰り返し呼び出す)
		AnimationTimer timer = new AnimationTimer() {
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
		
		
		AudioClip clickSound = new AudioClip(
			getClass().getResource("/music/select.mp3").toExternalForm()
		);
		// 音量調整（おすすめ）
		clickSound.setVolume(0.4);

		//ストーリーモードへ飛ぶボタンを作成
		Button btn1 = new Button("▶ストーリー");
		btn1.setPrefSize(300,100);
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
		    				timer.stop();
		    	            new story1().start(stage);
		    	})
		    );
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

		        // 背景停止
		        timer.stop();

		        // 画面遷移
		        new practice().start(stage);

		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		});
		//btn2にCSSのgame-buttonを付与
		btn2.getStyleClass().add("game-button");

		//無限モードへ飛ぶボタン作成
		Button btn3 = new Button("∞無限モード");
		btn3.setPrefSize(300, 100);
		//btn3にCSSのgame-buttonを付与
		btn3.getStyleClass().add("game-button");
		
		//3個のボタンを縦に並ぶ箱に入れる
		buttonBox.getChildren().addAll(btn1, btn2, btn3);
		
        //titleの画像と3個のボタンが入った箱を縦に並ぶ箱に入れる
        ui.getChildren().addAll(imageView, buttonBox);
        //下から背景、uiの箱に入れたものの順でレイヤー構造のrootに入れる
        root.getChildren().addAll(bgPane,ui);
        
        //rootを中身とした1000×800のウィンドウを作成
        Scene scene = new Scene(root,1000,800);
        //CSSを接続
        scene.getStylesheets().add(
         	    getClass().getResource("/css/style.css").toExternalForm()
        );
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