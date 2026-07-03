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
