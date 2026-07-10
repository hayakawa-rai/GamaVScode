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

	// 登録済みのStageを記録しておく（WeakHashMapで自動解放）
	private static final Set<Stage> LISTENER_ATTACHED = Collections.newSetFromMap(new WeakHashMap<>());

	// デフォルトのスマホ対応最小サイズ
	private static final double DEFAULT_MIN_WIDTH = 320.0;
	private static final double DEFAULT_MIN_HEIGHT = 480.0;
	
	// デフォルトの最大サイズ制限（ブラウザ最大化等に対応）
	private static final double DEFAULT_MAX_WIDTH = 1920.0;
	private static final double DEFAULT_MAX_HEIGHT = 1080.0;

	/**
	 * 【追加】ウィンドウサイズ制限を個別に指定して全画面化するメソッド
	 */
	public static void fullScreen(Stage stage, double minWidth, double minHeight, double maxWidth, double maxHeight) {
		if (stage == null) return;

		// 💡 前の画面の制限を引きずらないように、遷移直後にサイズ制限を一回リセット・上書きする
		stage.setMinWidth(minWidth);
		stage.setMinHeight(minHeight);
		stage.setMaxWidth(maxWidth);
		stage.setMaxHeight(maxHeight);

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

	/**
	 * ウィンドウを画面いっぱいに広げる処理。（通常用：スマホ対応サイズにリセット）
	 * 画面遷移のたびに毎回呼び出される想定。
	 */
	public static void fullScreen(Stage stage) {
		// デフォルトのモバイル両対応サイズ（320x480 〜 1920x1080）を適用する
		fullScreen(stage, DEFAULT_MIN_WIDTH, DEFAULT_MIN_HEIGHT, DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT);
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

	// サイズを1pxだけ揺らしてJavaFXに変化を検知させ、再計算を強制する
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

		canvas.widthProperty().addListener((obs, oldV, newV) -> redraw(canvas));
		canvas.heightProperty().addListener((obs, oldV, newV) -> redraw(canvas));
	}

	private static void redraw(Canvas canvas) {
		// ここでゲーム側の描画メソッドを呼ぶ
	}
}