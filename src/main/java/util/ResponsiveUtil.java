package util;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * スマホ〜PCまで画面幅に応じてUIを追従させるための共通ヘルパー。
 * 各画面クラス（Story画面, Practice画面, Help画面 など）から
 * static メソッドを呼ぶだけで、Start画面と同じレスポンシブ処理を使い回せる。
 *
 * 使い方の例（各コントローラのstart()内で）:
 *
 *   ResponsiveUtil.bindMaxWidth(rootBox, scene.widthProperty(), 0.9);
 *   ResponsiveUtil.bindImageFitWidth(logoView, scene.widthProperty(), 0.5);
 *   ResponsiveUtil.bindPrefSize(myButton, scene.widthProperty(), 0.35, scene.heightProperty(), 0.11, 160, 44);
 *   ResponsiveUtil.bindButtonFontAndPadding(myButton, scene.widthProperty(), 14, 0.045, 30, 8, 0.02, 20);
 */
public final class ResponsiveUtil {

	private ResponsiveUtil() {
		// インスタンス化不要のユーティリティクラス
	}

	/**
	 * 値をmin〜maxの範囲に収める
	 */
	public static double clamp(double min, double value, double max) {
		return Math.max(min, Math.min(value, max));
	}

	/**
	 * ノードのmaxWidthを「基準値(widthProp) × ratio」に追従させる。
	 * 例: ui全体の最大幅をシーン幅の90%にしたい場合など。
	 */
	public static void bindMaxWidth(Region node, ObservableDoubleValue widthProp, double ratio) {
		node.maxWidthProperty().bind(Bindings.createDoubleBinding(
				() -> widthProp.get() * ratio, widthProp));
	}

	/**
	 * ImageViewのfitWidthを「基準値(widthProp) × ratio」に追従させる。
	 * setPreserveRatio(true) は呼び出し側で設定しておくこと。
	 */
	public static void bindImageFitWidth(ImageView imageView, ObservableDoubleValue widthProp, double ratio) {
		imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(
				() -> widthProp.get() * ratio, widthProp));
	}

	/**
	 * ノードのprefWidth/prefHeightを画面サイズの割合に追従させつつ、
	 * 最小サイズ(minW, minH)を下回らないようにする。
	 */
	public static void bindPrefSize(Region node,
			ObservableDoubleValue widthProp, double widthRatio,
			ObservableDoubleValue heightProp, double heightRatio,
			double minW, double minH) {
		node.prefWidthProperty().bind(Bindings.createDoubleBinding(
				() -> widthProp.get() * widthRatio, widthProp));
		node.prefHeightProperty().bind(Bindings.createDoubleBinding(
				() -> heightProp.get() * heightRatio, heightProp));
		node.setMinWidth(minW);
		node.setMinHeight(minH);
	}

	/**
	 * ボタンのフォントサイズと横パディングを画面幅に応じて可変にする。
	 * CSSクラス側の -fx-font-size 固定指定を、インラインstyleで上書きする。
	 *
	 * @param fontMin  フォントサイズの下限(px)
	 * @param fontRatio widthProp に掛ける係数
	 * @param fontMax  フォントサイズの上限(px)
	 * @param padMin   横パディングの下限(px)
	 * @param padRatio widthProp に掛ける係数
	 * @param padMax   横パディングの上限(px)
	 */
	public static void bindButtonFontAndPadding(Button button, ObservableDoubleValue widthProp,
			double fontMin, double fontRatio, double fontMax,
			double padMin, double padRatio, double padMax) {
		button.styleProperty().bind(Bindings.createStringBinding(
				() -> {
					double w = widthProp.get();
					double fontSize = clamp(fontMin, w * fontRatio, fontMax);
					double paddingH = clamp(padMin, w * padRatio, padMax);
					return "-fx-font-size: " + fontSize + "px; "
							+ "-fx-padding: 8 " + paddingH + " 8 " + paddingH + ";";
				},
				widthProp));
	}
}