package util;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WindowUtil {

    public static void fillScreen(Stage stage) {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        }
        if (!stage.isMaximized()) {
            stage.setMaximized(true);
        }
        if (!stage.isShowing()) {
            stage.show();
        }
        // ここでは幅を触らない。実際のリサイズ完了はリスナー側に任せる。
    }

    // Canvasをrootに完全追従させる。手動でサイズを計算・代入しない。
    public static void bindCanvasToRoot(Canvas canvas, StackPane root) {
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        // Canvasはリサイズされても自動で再描画されないため、
        // サイズが変わるたびに描画処理を呼び出す必要がある
        canvas.widthProperty().addListener((obs, oldV, newV) -> redraw(canvas));
        canvas.heightProperty().addListener((obs, oldV, newV) -> redraw(canvas));
    }

    private static void redraw(Canvas canvas) {
        // ここでゲーム側の描画メソッドを呼ぶ
        // 例: GameRenderer.render(canvas.getGraphicsContext2D());
    }
}
/*package util;

import javafx.stage.Stage;

//フルスクリーンver.(これは遷移するときにがくがくする)
public class WindowUtil {

    private static boolean alreadyFullScreen = false;
	
    public static void fillScreen(Stage stage) {
    		//既に全画面状態なら何もしない（再度呼ぶとOSレベルのアニメーションが再発生しガクつくため）
        if (stage.isFullScreen()) {
            return;
        }
        
        //通常のmaximizedモードは使わず、OSレベルの本当の全画面表示にする
        stage.setFullScreen(true);

        //「ESCキーで終了できます」という案内メッセージを非表示にする
        stage.setFullScreenExitHint("");

        // ESCキーで全画面が解除されないようにする場合は以下を有効化
        // stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        alreadyFullScreen = true;
    
    }
}*/

/*package util;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

//ちょっと小さいver.
public class WindowUtil {

    private static Rectangle2D cachedBounds = null;

    public static void fillScreen(Stage stage) {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        }

        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        }

        if (cachedBounds == null) {
            //VisualBounds(タスクバーを除いた作業領域)ではなく、
            //Bounds(画面全体の物理サイズ)を使うことでタスクバーの上に被せる
            cachedBounds = Screen.getPrimary().getBounds();
        }

        stage.setX(cachedBounds.getMinX());
        stage.setY(cachedBounds.getMinY());
        stage.setWidth(cachedBounds.getWidth());
        stage.setHeight(cachedBounds.getHeight());

        if (!stage.isShowing()) {
            stage.show();
        }

        //幅を1pxだけ揺らして描画エンジンにリサイズを検知させ、スケールを再計算させる
        double targetWidth = stage.getWidth();
        stage.setWidth(targetWidth - 1);
        Platform.runLater(() -> stage.setWidth(targetWidth));
    }
}*/

/*package util;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

//もっと小さいver.
public class WindowUtil {

    private static Rectangle2D cachedBounds = null;

    public static void fillScreen(Stage stage) {
        if (stage.isMaximized()) {
            stage.setMaximized(false);
        }

        if (cachedBounds == null) {
            // 初回だけ実際に画面から取得する
            cachedBounds = Screen.getPrimary().getVisualBounds();
        }

        stage.setX(cachedBounds.getMinX());
        stage.setY(cachedBounds.getMinY());
        stage.setWidth(cachedBounds.getWidth());
        stage.setHeight(cachedBounds.getHeight());
        
      //画面の拡大を強制的にしている
        double targetWidth = stage.getWidth();
        stage.setWidth(targetWidth - 1);
        Platform.runLater(() -> stage.setWidth(targetWidth));
    }
}*/