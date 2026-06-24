package story;

import java.util.Arrays;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
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
import start.Bgm;

public class Story1 extends Application {

    private Stage stage;
    
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(story());
        stage.setTitle("story1");
        stage.show();
    }

    private Timeline blink;
    private Timeline arrowMove;
    private AudioClip jumpSound;

    //ストーリー終了処理を1回だけにする用

    
    private boolean isEndingStarted = false;
    private int messageIndex = 0;
    private int charIndex = 0;
    private boolean isTyping = false;
    private Text text;
    private Timeline timeline;
    
    // 他のアニメーションタイマーも確実に止めるためにメンバ変数化・初期化
    private Timeline blink;
    private Timeline arrowMove;
    private Timeline jumpAniki;
    private Timeline jumpSengoku;
    private Timeline jumpNarinari;

    private void startTyping() {
        charIndex = 0;
        text.setText("");
        isTyping = true;
        if (timeline != null) {
            timeline.playFromStart();
        }
    }
    
    // 【重要】この画面で使ったすべてのタイマーを完全に停止してメモリを解放するメソッド
    private void cleanup() {
        if (timeline != null) timeline.stop();
        if (blink != null) blink.stop();
        if (arrowMove != null) arrowMove.stop();
        if (jumpAniki != null) jumpAniki.stop();
        if (jumpSengoku != null) jumpSengoku.stop();
        if (jumpNarinari != null) jumpNarinari.stop();
        
        // 参照をクリアしてガベージコレクションに回収してもらう
        timeline = null;
        blink = null;
        arrowMove = null;
        jumpAniki = null;
        jumpSengoku = null;
        jumpNarinari = null;
    }
    
    private void cleanup(Scene scene) {

        // 文字タイピング
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }

        // ジャンプ
        if (jumpAniki != null) {
            jumpAniki.stop();
            jumpAniki = null;
        }
        if (jumpSengoku != null) {
            jumpSengoku.stop();
            jumpSengoku = null;
        }
        if (jumpNarinari != null) {
            jumpNarinari.stop();
            jumpNarinari = null;
        }

        // ▼アニメーション
        if (blink != null) {
            blink.stop();
            blink = null;
        }
        if (arrowMove != null) {
            arrowMove.stop();
            arrowMove = null;
        }

        // 効果音
        if (jumpSound != null) {
            jumpSound.stop();
            jumpSound = null;
        }

        // BGM停止
        Bgm.stopBGM();

        // クリックイベント解除
        if (scene != null) {
            scene.setOnMouseClicked(null);   // 念のため
            scene.removeEventFilter(MouseEvent.MOUSE_CLICKED, null);
        }
    }
    
    public Scene story() {
        
        Bgm.stopBGM();
        Bgm.playBGM("/music/storybgm.mp3");
    
<<<<<<< HEAD
    	 //ジャンプ音の読み込み
        jumpSound = new AudioClip(
        	    getClass().getResource("/music/jump06.mp3").toExternalForm()
        	);
        //音量調整
=======
        AudioClip jumpSound = new AudioClip(getClass().getResource("/music/jump06.mp3").toExternalForm());
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
        jumpSound.setVolume(0.2); 
      
        List<Dialogue> dialogues = Arrays.asList( 
            new Dialogue("仙石さん", "おはよ～～！！", jumpSound, Color.WHITE),
            new Dialogue("あにき", "先輩社員サン、ですか。", jumpSound, Color.RED),
            new Dialogue("あにき", "今日からここの社長は俺だ。", jumpSound, Color.RED),
            new Dialogue("あにき", "休憩時間以外は全て俺のものだ！！！", jumpSound, Color.RED),
            new Dialogue("仙石さん", "ふざけるな。\n"+ "ここは俺たちの会社だ。", jumpSound, Color.WHITE),
            new Dialogue("仙石さん", "取り戻してやる！！", jumpSound, Color.WHITE),
            new Dialogue("あにき", "クク……熱いねえ", jumpSound, Color.RED),
            new Dialogue("あにき", "だが、まずは順番ってものがある。", jumpSound, Color.RED),
            new Dialogue("あにき", "新入社員を育てるのも、\n"+ "上司の務めだろう？", jumpSound, Color.RED),
            new Dialogue("なりなり", "ここから先は通しませんよ、先輩。", jumpSound, Color.ORANGE),
            new Dialogue("なりなり", "自分、もう\"研修\"は終わってるんで。", jumpSound, Color.ORANGE),
            new Dialogue("仙石さん", "研修で覚えたのは、会社を乗っ取ることか？", jumpSound, Color.WHITE),
            new Dialogue("仙石さん", "教育しなおしてやる！！", jumpSound, Color.WHITE)
        );
        
        text = new Text("");
        text.setStyle("-fx-font-family: monospace;");
        text.setTranslateY(-5);
        
        Rectangle box = new Rectangle();
        text.setWrappingWidth(850);
        box.setFill(Color.rgb(0, 0, 0, 0.7));
        box.setStroke(Color.WHITE);
        box.setStrokeWidth(3);
        box.setArcWidth(0);
        box.setArcHeight(0);
    
        Text nextMark = new Text("▼");
        nextMark.setFill(Color.WHITE);
        nextMark.setVisible(false);
        nextMark.setTranslateY(40);
<<<<<<< HEAD
        //▼のアニメーション設定
        blink = StoryUtils.createBlink(nextMark);
        arrowMove = StoryUtils.createArrowMove(nextMark);
=======
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
        
        // メンバ変数に代入（のちほどクリーンアップするため）
        blink = StoryUtils.createBlink(nextMark);
        arrowMove = StoryUtils.createArrowMove(nextMark);
        
        Text nameText = new Text();
        nameText.setText(dialogues.get(messageIndex).speaker);
       
        VBox bubble = new VBox();
        bubble.setPadding(new Insets(10));
        bubble.setSpacing(5);
        bubble.setPrefWidth(850);
        bubble.setMaxWidth(850);
        bubble.setAlignment(Pos.CENTER_LEFT);
        StackPane.setAlignment(bubble, Pos.BOTTOM_CENTER);
        
        StackPane arrowBox = new StackPane(nextMark);
        arrowBox.setPadding(new Insets(0, 0, 15, 0));
        bubble.getChildren().addAll(nameText, text, arrowBox);
        
        Image bgImage = new Image(getClass().getResourceAsStream("/picture/emd-nottori.jpg"));
        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(false);
        
        Image anikiImage = new Image(getClass().getResourceAsStream("/picture/hayakawa-udekumi.png"));
        ImageView anikiView = new ImageView(anikiImage);
        anikiView.setPreserveRatio(true);
        
        Image sengokuImage = new Image(getClass().getResourceAsStream("/picture/sengoku.png"));
        ImageView sengokuView = new ImageView(sengokuImage);
        sengokuView.setPreserveRatio(true);
        
        Image narinariImage = new Image(getClass().getResourceAsStream("/picture/narinari.png"));
        ImageView narinariView = new ImageView(narinariImage);
        narinariView.setPreserveRatio(true);
        
        narinariView.setVisible(false);
        anikiView.setVisible(true);
        
        StackPane messageBox = new StackPane();
        messageBox.getChildren().addAll(box, bubble);
        
        StackPane back = new StackPane();
        back.getChildren().add(bgView);
        
        BorderPane root = new BorderPane();
        root.setBottom(messageBox);
        root.setStyle("-fx-background-color: transparent;");
        BorderPane.setMargin(messageBox, new Insets(0, 0, 30, 0));
    
        StackPane base = new StackPane();
        base.getChildren().addAll(bgView, sengokuView, anikiView, narinariView, root);
        Scene scene = new Scene(base, 1000, 800);
        
        // 各種バインディング処理
        bgView.fitWidthProperty().bind(scene.widthProperty());
        bgView.fitHeightProperty().bind(scene.heightProperty());
        anikiView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
        anikiView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
        anikiView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
        narinariView.fitWidthProperty().bind(scene.widthProperty().multiply(0.5));
        narinariView.fitHeightProperty().bind(scene.heightProperty().multiply(0.9));
        narinariView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
        sengokuView.fitWidthProperty().bind(scene.widthProperty().multiply(0.6));
        sengokuView.fitHeightProperty().bind(scene.heightProperty().multiply(1.0));
        sengokuView.translateXProperty().bind(scene.widthProperty().multiply(-0.25));
        box.widthProperty().bind(scene.widthProperty().multiply(0.9));
        box.heightProperty().bind(scene.heightProperty().multiply(0.18));
        text.wrappingWidthProperty().bind(box.widthProperty().multiply(0.95));
        bubble.prefWidthProperty().bind(box.widthProperty());
        bubble.maxWidthProperty().bind(box.widthProperty());
<<<<<<< HEAD
        //フォントサイズも変化
        text.styleProperty().bind(
        		Bindings.format(
        				"-fx-font-size: %.0fpx; -fx-font-family: monospace;",
        				scene.widthProperty().multiply(0.03)
        		)
        );
        //▼のサイズも変化
        nextMark.styleProperty().bind(
        		Bindings.format(
        				"-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;",
        				scene.widthProperty().multiply(0.02)
        		)
        );
        //名前表示も変化
        nameText.styleProperty().bind(
        	    Bindings.format(
        	        "-fx-font-size: %.0fpx; -fx-fill: lightgray;",
        	        scene.widthProperty().multiply(0.025)
        	    )
        );
        //ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
=======
        
        text.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;", scene.widthProperty().multiply(0.03)));
        nextMark.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;", scene.widthProperty().multiply(0.02)));
        nameText.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: lightgray;", scene.widthProperty().multiply(0.025)));
        
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        timeline = new Timeline(
            new KeyFrame(Duration.millis(50), e -> {
                Dialogue d = dialogues.get(messageIndex);
                if (charIndex < d.message.length()) {
                    charIndex++;
                    String speaker = d.speaker;
                    nameText.setText(speaker);
                    text.setFill(d.textColor);
                    if (speaker.equals("あにき")) {
                        anikiView.setVisible(true);
                        narinariView.setVisible(false);
                    } else if (speaker.equals("なりなり")) {
                        anikiView.setVisible(false);
                        narinariView.setVisible(true);
                    }
                    text.setText(d.message.substring(0, charIndex));
                } else {
                    isTyping = false;
                    timeline.stop();
                    nextMark.setVisible(true);
                    blink.play();
                    arrowMove.play();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
                
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (isTyping) {
                timeline.stop();
                Dialogue d = dialogues.get(messageIndex);
                text.setText(d.message);
                isTyping = false;
                nextMark.setVisible(true);
                blink.play();
                arrowMove.play();
                return;
            }
            if (messageIndex < dialogues.size() - 1) {
                messageIndex++;
                startTyping();
                nextMark.setVisible(false);
                blink.stop();
                arrowMove.stop();
                
                Dialogue d = dialogues.get(messageIndex);
                String speaker = d.speaker;
                
                if (speaker.equals("あにき")) {
                    jumpAniki = StoryUtils.createJumpAnimation(anikiView, d.sound);
                    jumpAniki.playFromStart();
                } else if (speaker.equals("仙石さん")) {
                    jumpSengoku = StoryUtils.createJumpAnimation(sengokuView, d.sound);
                    jumpSengoku.playFromStart();
                } else if (speaker.equals("なりなり")) {
                    jumpNarinari = StoryUtils.createJumpAnimation(narinariView, d.sound);
                    jumpNarinari.playFromStart();
                }
            } else {
                if (isEndingStarted) return;
                isEndingStarted = true;

                nextMark.setVisible(false);

                Rectangle fadeRect = new Rectangle(1000, 800, Color.BLACK);
                fadeRect.setOpacity(0);
                base.getChildren().add(fadeRect);

                FadeTransition fade = new FadeTransition(Duration.seconds(1.5), fadeRect);
                fade.setFromValue(0);
                fade.setToValue(1);
                
                fadeRect.widthProperty().bind(scene.widthProperty());
                fadeRect.heightProperty().bind(scene.heightProperty());
                
                fade.setOnFinished(ev -> {
                    Bgm.stopBGM();
                    
                    // 【最重要】次の画面に行く直前に、この画面のタイマーをすべて止めてお掃除する
                    cleanup(); 

<<<<<<< HEAD
        	    //フェードアウト
        	    FadeTransition fade = new FadeTransition(Duration.seconds(1.5), fadeRect);
        	    fade.setFromValue(0);
        	    fade.setToValue(1);
        	    
        	    //サイズをウィンドウに合わせる
        	    fadeRect.widthProperty().bind(scene.widthProperty());
        	    fadeRect.heightProperty().bind(scene.heightProperty());
        	    
        	    fade.setOnFinished(ev -> {
        	    	cleanup(scene); 
        	    	base.getChildren().clear();
        	        //次の画面へ
        	        test.test2.GameController.switchToGame1(stage);
        	    });

        	    fade.play();
        	}
=======
                    // 次の画面へ（既存のstageを受け渡す）
                    test.test2.GameController.switchToGame1(stage);
                });

                fade.play();
            }
>>>>>>> branch 'master' of https://github.com/hayakawa-rai/GameApplication.git
        });
        
        startTyping();
        return scene;
    }
}