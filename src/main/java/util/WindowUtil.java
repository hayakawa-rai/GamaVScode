package util;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WindowUtil {

    // 既にリスナーを登録済みのStageを記録(二重登録防止)
    private static final Set<Stage> LISTENER_ATTACHED =
            Collections.newSetFromMap(new WeakHashMap<>());

    public static void fillScreen(Stage stage) {
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        }

        if (!stage.isShowing()) {
            stage.show();
        }

        if (LISTENER_ATTACHED.add(stage)) {
            stage.renderScaleXProperty().addListener((obs, oldV, newV) -> forceRelayout(stage));
            stage.renderScaleYProperty().addListener((obs, oldV, newV) -> forceRelayout(stage));
        }

        if (!stage.isMaximized()) {
            stage.setMaximized(true);
        } else {
            nudge(stage);
        }
    }

    private static void forceRelayout(Stage stage) {
        if (stage.getScene() != null) {
            Parent root = stage.getScene().getRoot();
            if (root != null) {
                root.applyCss();
                root.requestLayout();
            }
        }
        nudge(stage);
    }

    private static void nudge(Stage stage) {
        double w = stage.getWidth();
        if (w <= 0) return;
        stage.setWidth(w - 1);
        Platform.runLater(() -> stage.setWidth(w));
    }

    public static void bindCanvasToRoot(Canvas canvas, StackPane root) {
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().addListener((obs, oldV, newV) -> redraw(canvas));
        canvas.heightProperty().addListener((obs, oldV, newV) -> redraw(canvas));
    }

    private static void redraw(Canvas canvas) {
        // ここでゲーム側の描画メソッドを呼ぶ
    }
}