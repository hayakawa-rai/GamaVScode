package story;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class StoryUtils {
	//ジャンプ用メソッド
	public static Timeline createJumpAnimation(Node charView, AudioClip jumpSound) {
	    return new Timeline(
	        // 1回目
	        new KeyFrame(Duration.millis(300), e -> {
	            jumpSound.stop();
	            jumpSound.play();
	        },
	            new KeyValue(charView.translateYProperty(), -90)
	        ),

	        new KeyFrame(Duration.millis(700),
	            new KeyValue(charView.translateYProperty(), 0)
	        ),

	        // 2回目
	        new KeyFrame(Duration.millis(1000), e -> {
	            jumpSound.stop();
	            jumpSound.play();
	            jumpSound.stop();
	        },
	            new KeyValue(charView.translateYProperty(), -90)
	        ),

	        new KeyFrame(Duration.millis(1400),
	            new KeyValue(charView.translateYProperty(), 0)
	            
	        )
	    );
	}
	
		//▼の点滅アニメーション(Timeline:一定時間ごとに処理を実行する)
		public static Timeline createBlink(Text nextMark) {
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
		    return blink;
		}
		
		
		//▼を上下に揺らす
		public static Timeline createArrowMove(Text target) {
		    Timeline arrowMove = new Timeline(
		    	//0秒時点でのy軸の座標を0に設定
		        new KeyFrame(Duration.seconds(0),
		            new KeyValue(target.translateYProperty(), 0)
		        ),
		        //0.5秒時にy座標が5になる
		        new KeyFrame(Duration.seconds(0.5),
		            new KeyValue(target.translateYProperty(), 5)
		        )
		    );
		    // 無限ループ
		    arrowMove.setCycleCount(Timeline.INDEFINITE);
		    //0→5の動きのコードしか書いていないけどこれをつけることで5→0も自動で再生される
		    arrowMove.setAutoReverse(true);

		    return arrowMove;
		}
		
		
}
