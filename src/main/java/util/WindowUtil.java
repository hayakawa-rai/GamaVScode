/*package util;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
        
      //画面の拡大を強制的にしています。
        double targetWidth = stage.getWidth();
        stage.setWidth(targetWidth - 1);
        Platform.runLater(() -> stage.setWidth(targetWidth));
    }
}*/

/*package util;

import javafx.stage.Stage;

public class WindowUtil {

    public static void fillScreen(Stage stage) {
        // 💡 通常のmaximizedモードは使わず、OSレベルの本当の全画面表示にする
        stage.setFullScreen(true);

        // 「ESCキーで終了できます」という案内メッセージを非表示にする
        stage.setFullScreenExitHint("");

        // ESCキーで全画面が解除されないようにする場合は以下を有効化
        // （ゲーム側で独自にメニューを閉じる操作を用意しているなら、こちらを有効にした方が安全です）
        // stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}*/

package util;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
            // 💡 VisualBounds(タスクバーを除いた作業領域)ではなく、
            //    Bounds(画面全体の物理サイズ)を使うことでタスクバーの上に被せる
            cachedBounds = Screen.getPrimary().getBounds();
        }

        stage.setX(cachedBounds.getMinX());
        stage.setY(cachedBounds.getMinY());
        stage.setWidth(cachedBounds.getWidth());
        stage.setHeight(cachedBounds.getHeight());

        if (!stage.isShowing()) {
            stage.show();
        }

        // 💡 幅を1pxだけ揺らして描画エンジンにリサイズを検知させ、スケールを再計算させる
        double targetWidth = stage.getWidth();
        stage.setWidth(targetWidth - 1);
        Platform.runLater(() -> stage.setWidth(targetWidth));
    }
}