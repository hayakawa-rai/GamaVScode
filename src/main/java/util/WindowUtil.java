package util;

//このコードが今までで一番いい出来です
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WindowUtil {

	// 画面遷移のたびにリスナーが重複登録されるのを防ぐため、
	// 登録済みのStageを記録しておく（WeakHashMapで自動解放
	private static final Set<Stage> LISTENER_ATTACHED = Collections.newSetFromMap(new WeakHashMap<>());

	/**
	 * ウィンドウを画面いっぱいに広げる処理。
	 * 画面遷移のたびに毎回呼び出される想定。
	 */
	public static void fillScreen(Stage stage) {
		// 万が一OSレベルの全画面モードになっていたら解除しておく
		if (stage.isFullScreen()) {
			stage.setFullScreen(false);
		}
		// ウィンドウがまだ表示されていなければ表示する
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

	// renderScale変化時にレイアウトとサイズを強制的に再計算させる
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

	// サイズを1pxだけ揺らしてJavaFXに変化を検知させ、
	// DPIスケール・レイアウトの再計算を強制する。
	// 幅だけだと縦がズレたままになるので高さも必ず揺らす
	private static void nudge(Stage stage) {
		double w = stage.getWidth();
		double h = stage.getHeight();
		if (w <= 0 || h <= 0)
			return;

		stage.setWidth(w - 1);
		stage.setHeight(h - 1);

		Platform.runLater(() -> {
			stage.setWidth(w);
			stage.setHeight(h);
		});
	}

	// Canvasをrootのサイズに自動追従させる
	public static void bindCanvasToRoot(Canvas canvas, StackPane root) {
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());

		// Canvasはサイズが変わっても自動再描画されないので手動で呼ぶ
		canvas.widthProperty().addListener((obs, oldV, newV) -> redraw(canvas));
		canvas.heightProperty().addListener((obs, oldV, newV) -> redraw(canvas));
	}

	private static void redraw(Canvas canvas) {
		// ここでゲーム側の描画メソッドを呼ぶ
	}
}