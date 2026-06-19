package sample;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class story1 extends Application{
	

	//ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
    private Stage stage;
    //javafxではstartを呼び出さないと起動しないため、親クラスのstartを上書きすることで子クラスを起動
    @Override
    public void start(Stage stage) {
    	//受け取った変数Stageを自分のStageに保存
        this.stage = stage;
        //ウィンドウの中身を決定
        stage.setScene(story());
        stage.setTitle("story1");
        stage.show();
    }
    //表示する会話内容をリストに格納
    private List<String> messages = Arrays.asList(
			"フハハハハ！","この会社はすでに我が支配下だ！","今日から社長は俺、そしてお前は“元・社員”だ！",
			"返還請求だと？残念だがこの会社、もう休憩時間以外は全部俺のものだ",
			"さあ先輩社員よ！業務命令だ、全力でかかってこい！","ただし残業代は出ない！！"
	);
    
    //今どのメッセージを表示しているかのカウント用
    private int messageIndex = 0;
    //何文字目まで表示するか(タイピング演出のためのカウンター)
    private int charIndex = 0;
    //文字を表示途中か表示完了しているかどうか
    private boolean isTyping = false;
    //画面に表示するメッセージを入れる変数
    private Text text;
    //一定時間ことに処理を実行するタイマー
    private Timeline timeline;
    
    //新しいメッセージを表示するための準備用メソッド
    private void startTyping() {
    	//文字カウントをリセット
        charIndex = 0;
        //画面を一旦空にする
        text.setText("");
        //今打ち込み中ですよという状態にする
        isTyping = true;
        timeline.playFromStart();
    }
    
    public Scene story() {
    	
    	//テキストクラスのインスタンスを作成
    	text = new Text("");
    	text.setStyle(
    		"-fx-fill:white;" + //文字の色
    		"-fx-font-family: monospace;"  //等間隔フォント
    	);
    	//上にあげる
        text.setTranslateY(-10);
        
        
    	//吹き出し(textの背景)作成
    	Rectangle box = new Rectangle();
    	//横幅が760pxを超えたら自動で改行する
    	text.setWrappingWidth(850);
    	//吹き出しの色
    	box.setFill(Color.rgb(0,0,0,0.7));
    	//白枠
    	box.setStroke(Color.WHITE);
    	box.setStrokeWidth(3);
    	//吹き出しの角調整(0:カクカク、数値を大きくすると丸い)
    	box.setArcWidth(0);
    	box.setArcHeight(0);
    
    	
        //▼マーク
        Text nextMark = new Text("▼");
        //色を白色に設定
        nextMark.setFill(Color.WHITE);
        //フォントサイズを設定
        //nextMark.setStyle("-fx-font-size: 20px;");
        //最初は非表示にする
        nextMark.setVisible(false);
        //下に下げる
        nextMark.setTranslateY(40);
        //▼の点滅アニメーション(Timeline:一定時間ごとに処理を実行する)
        Timeline blink = new Timeline(
        //0.5秒ごとに処理を実行
        new KeyFrame(Duration.seconds(0.5), e -> {
        	//setVisible：表示するかどうかを切り替える
        	//isVisible：今表示中かを確認
            nextMark.setVisible(!nextMark.isVisible());
        	})
        );
        //Timeline.INDEFINITE→無限ループ
        blink.setCycleCount(Timeline.INDEFINITE);
        //▼を上下に揺らす
        Timeline arrowMove = new Timeline(
        	new KeyFrame(Duration.seconds(0),
        		new KeyValue(nextMark.translateYProperty(), 0)
        	),
        	new KeyFrame(Duration.seconds(0.5),
        		new KeyValue(nextMark.translateYProperty(), 5)
        	)
        );
        // 無限ループ
        arrowMove.setCycleCount(Timeline.INDEFINITE);
        // 行ったり来たり
        arrowMove.setAutoReverse(true);

        
        //textと▼をまとめる
        //縦に並べる箱を作成
        VBox bubble = new VBox();
        //内側の余白を作成
        bubble.setPadding(new Insets(10));
        //部品同士の間隔の設定
        bubble.setSpacing(5);
        //幅を設定理想と最大をどちらも850pxに設定
        bubble.setPrefWidth(850);
        bubble.setMaxWidth(850);
        //中央左寄りに配置
        bubble.setAlignment(Pos.CENTER_LEFT);
        //bubble自体をウィンドウの中央下に配置
        StackPane.setAlignment(bubble, Pos.BOTTOM_CENTER);
        // ▼を中央に配置、下に余白を作成
        StackPane arrowBox = new StackPane(nextMark);
        arrowBox.setPadding(new Insets(0, 0, 15, 0));
        //テキストの下に▼を配置
        bubble.getChildren().addAll(text, arrowBox);
        
        
        //背景画像を読み込み
        Image bgImage = new Image(
        		getClass().getResourceAsStream("/emd-nottori.jpg")
        );
        //背景画像の表示
        ImageView bgView = new ImageView(bgImage);

        //余白を生まないために縦横比を無視
        bgView.setPreserveRatio(false);
        
        
        //人物画像の読み込み
        Image charImage = new Image(
        		getClass().getResourceAsStream("/hayakawa-udekumi.png")
        );
        //人物画像の表示
        ImageView charView = new ImageView(charImage);
        //縦横比率を維持
        charView.setPreserveRatio(true);
        
        
        //box(吹き出し)とbubble(テキストと▼)をまとめる
        //StackPaneにより同じ位置の前後に置かれるので重なって見える
        StackPane messageBox = new StackPane();
        messageBox.getChildren().addAll(box,bubble);
        
        
        //背景の設定(1番最初に入れたものが1番後ろになる)
        StackPane back = new StackPane();
        back.getChildren().add(bgView);
        
        
        //レイヤー構造を使用し吹き出しとテキストの位置を設定
        //mesageBoxによりまとめられたものを、ウィンドウのどこに表示するかを設定する
        BorderPane root = new BorderPane();
        //吹き出しを中央下に配置
        root.setBottom(messageBox);
        //rooのuiレイアウト背景を透明にする(背景などが映るようにするため)
        root.setStyle("-fx-background-color: transparent;");
        //Borderpaneにより一番下に表示されてしまうので、下に余白を設定する
        BorderPane.setMargin(messageBox, new Insets(0, 0, 30, 0));
    
        
        //ウィンドウ全体のレイヤー(下から背景、人物画像、吹き出しの順に配置)
        StackPane base = new StackPane();
        base.getChildren().addAll(bgView,charView, root);
        //rootを中身とした1000×800のウィンドウを作成
        Scene scene = new Scene(base,1000,800);
        
        
        // 背景画像をウィンドウサイズに合わせる
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        // 人物画像をウィンドウサイズに合わせる
        charView.fitWidthProperty().bind(scene.widthProperty());
        charView.fitHeightProperty().bind(scene.heightProperty());
        // Rectangle固定やめる
        box.widthProperty().bind(scene.widthProperty().multiply(0.9));
        box.heightProperty().bind(scene.heightProperty().multiply(0.18));
        // テキストも追従
        text.wrappingWidthProperty().bind(box.widthProperty().multiply(0.95));
        // bubbleも合わせる
        bubble.prefWidthProperty().bind(box.widthProperty());
        bubble.maxWidthProperty().bind(box.widthProperty());
        //フォントサイズも変化
        text.styleProperty().bind(
        		Bindings.format(
        				"-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;",
        				scene.widthProperty().multiply(0.02)
        		)
        );
        //▼のサイズも変化
        nextMark.styleProperty().bind(
        		Bindings.format(
        				"-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;",
        				scene.widthProperty().multiply(0.02)
        		)
        );
        //ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
        stage.setMinWidth(800);
        stage.setMinHeight(600);
       
        
        //ジャンプ音の読み込み
        AudioClip jumpSound = new AudioClip(
        	    getClass().getResource("/jump06.mp3").toExternalForm()
        	);
        //音量調整
        jumpSound.setVolume(0.3); 
      
        
        // ボスをぴょんぴょんさせるアニメーション
        Timeline jump = new Timeline(
        		// 1回目
        		//300ミリ秒後にe->以降の処理を実行
        	    new KeyFrame(Duration.millis(300), e -> {
        	    	//音が途中から再生されることを防ぐために一度止めてリセットしてから再生
        	        jumpSound.stop();
        	        jumpSound.play();
        	    },
        	    	//人物画像をy座標の上方向に90px移動	
        	        new KeyValue(charView.translateYProperty(), -90)
        	    ),
        	    //700ミリ秒かけて人物画像のy座標を0に戻す
        	    new KeyFrame(Duration.millis(700),
        	        new KeyValue(charView.translateYProperty(), 0)
        	    ),
        	    // 2回目(1000ミリ秒後に実行)
        	    new KeyFrame(Duration.millis(1000), e -> {
        	        jumpSound.stop();
        	        jumpSound.play();
        	    },
        	        new KeyValue(charView.translateYProperty(), -90)
        	    ),
        	    // 戻る(1400ミリ秒かけて戻す)
        	    new KeyFrame(Duration.millis(1400),
        	        new KeyValue(charView.translateYProperty(), 0)
        	    )
        	);
       
        
        //文字表示用のタイマーを作成、50ミリ秒ごとに処理
        timeline = new Timeline(
        	new KeyFrame(Duration.millis(50),e->{
        		//まだ文字が残っているかどうかを判断(文字が残っている間は処理を実行)
        		if(charIndex < messages.get(messageIndex).length()) {
        			//文字カウントを増やす
        			charIndex++;
        			//表示しているメッセージに対して1文ずつ表示する文字数を増やしていく処理
        			//例：メッセージがhelloのとき、h→he→hel→hell→hello
        			text.setText(messages.get(messageIndex).substring(0,charIndex));
        			if(Math.random() < 0.5) {
        			}
        		}else {//全て表示し終わった後の処理
        			//fales：タイピング中じゃない
        			isTyping = false;
        			//タイピング(文字を1文字ずつ表示するアニメーション)を停止
        			timeline.stop();
        			//▼を表示
        			nextMark.setVisible(true);
        			//▼点滅アニメーション表示
        			blink.play();
        			//▼を上下に揺らすアニメーション表示
        			arrowMove.play();
        		}
        	})
        );
        //Timeline.INDEFINITE：無限ループ
        timeline.setCycleCount(Timeline.INDEFINITE);
        		
        
        //乱数を作成
        Random rand = new Random();
        //クリックされたときの処理
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
        	//文字表示中ならスキップして全文表示する処理
        	if(isTyping) {
        		//タイピング停止
        		timeline.stop();
        		//一気に全文表示
        		text.setText(messages.get(messageIndex));
        		//状態を更新してタイピングが終わったことにする
        		isTyping = false;
        		//▼を表示
        		nextMark.setVisible(true);
        		//▼点滅アニメーション表示
        		blink.play();
        		//▼を上下に揺らすアニメーション表示
        		arrowMove.play();
        		return;
        	}
        	//まだメッセージがある場合if文内のの処理を実行
        	if (messageIndex < messages.size() - 1) {
        		//メッセージカウントを増やす
        	    messageIndex++;
        	    //タイピングを再スタート
        	    startTyping();
        	    //▼を消す
        	    nextMark.setVisible(false);
        	    //▼の点滅を消す
        	    blink.stop();
        	    //▼を上下に揺らすアニメーションを停止
        	    arrowMove.stop();
        	    //ボスのジャンプ
        	    if (rand.nextInt(2) == 0) { // 1/3の確率
        	    	jump.playFromStart();
        	    }
        	} else {//メッセージの最後まで行った後の処理
        		// 1. 各種タイマーやアニメーションを安全に停止
                timeline.stop();
                blink.stop();
                arrowMove.stop();
                jump.stop();
                
                // 2. 司令塔を経由してパックマンのゲーム画面へ一瞬で遷移！
                test.controller.SampleController.switchToGame(stage);
        	}
        });
        //最初の文章を表示(部品のすべての処理を終えてから文字を表示するため最後に記述)
        startTyping();
        
		return scene;
	
    }
    
}
