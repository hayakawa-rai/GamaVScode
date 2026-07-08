package start;

import control.GameController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * 操作説明画面
 * Start画面の「？」ボタンから遷移してくる
 */
public class Help extends Application {

	private AnimationTimer timer;

	@Override
	public void start(Stage stage) {

		// ===== 背景（Start画面と同じ横スクロール背景） =====
		Image bgImage = new Image(getClass().getResource("/picture/background.png").toExternalForm());
		double bgWidth = bgImage.getWidth();
		double bgHeight = bgImage.getHeight();
		Pane bgPane = new Pane();
		final double[] scrollX = { 0 };

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				scrollX[0] -= 1;
				if (scrollX[0] <= -bgWidth) {
					scrollX[0] = 0;
				}
				ImagePattern pattern = new ImagePattern(
						bgImage, scrollX[0], 0, bgWidth, bgHeight, false);
				bgPane.setBackground(new Background(new BackgroundFill(pattern, null, null)));
			}
		};
		timer.start();

		StackPane root = new StackPane();

		// ===== 中央の半透明パネル =====
		VBox panel = new VBox(16);
		panel.setAlignment(Pos.CENTER);
		panel.setMaxWidth(560);
		panel.setPadding(new Insets(35, 45, 35, 45));
		panel.setStyle(
				"-fx-background-color: rgba(0,0,0,0.6);"
				+ "-fx-border-color: #4FD8E8;"
				+ "-fx-border-width: 2;"
				+ "-fx-background-radius: 4;"
				+ "-fx-border-radius: 4;");

		// タイトル
		Label title = new Label("操作方法");
		title.setTextFill(Color.web("#F4C022"));
		title.setFont(Font.font("PixelMplus12", FontWeight.BOLD, 30));
		panel.getChildren().add(title);

		// 操作一覧（PAUSE画面と同じ「ラベル：キー」の1行表記）
		panel.getChildren().add(makeLine("移動： ↑ / ↓ / ← / →   または   W / A / S / D"));
		panel.getChildren().add(makeLine("画面右下ボタン（スマホ用）"));
		panel.getChildren().add(makeLine("一時停止・再開： P"));

		// 区切り
		Label divider = new Label("――――――――――――――――");
		divider.setTextFill(Color.web("#4a4a5a"));
		divider.setFont(Font.font("PixelMplus12", 12));
		panel.getChildren().add(divider);

		// 補足ルール（ドット・パワーエサはアイコン付きで表示）
		panel.getChildren().add(makeIconNote(makeDotIcon(), "ドットを食べるとスコアが加算されます"));
		panel.getChildren().add(makeIconNote(makePowerPelletIcon(), "パワーエサを食べると一定時間ゴーストを食べられます"));
		panel.getChildren().add(makeNote("・ゴーストに触れるとミスになります"));
		panel.getChildren().add(makeNote("・ゲーム画面の「タイトルへ戻る」ボタンでいつでも中断できます"));

		// 戻るボタン（PAUSE画面の「操作説明」「タイトルへ戻る」ボタンと同じ見た目）
		Button backBtn = new Button("戻る");
		backBtn.setPrefSize(220, 60);
		backBtn.getStyleClass().add("panel-button");
		backBtn.setOnAction(e -> {
			timer.stop();
			GameController.switchStart(stage);
		});
		panel.getChildren().add(backBtn);

		root.getChildren().addAll(bgPane, panel);

		Scene scene = new Scene(root, 1000, 800);
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		// Startと同じスタイルシートを流用
		try {
			scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.setTitle("操作説明");
		stage.setScene(scene);
		stage.show();
	}

	// PAUSE画面と同じ「1行テキスト」を作る補助メソッド
	private Label makeLine(String text) {
		Label l = new Label(text);
		l.setTextFill(Color.WHITE);
		l.setFont(Font.font("PixelMplus12", 16));
		return l;
	}

	// 補足ノート用の小さいラベルを作る補助メソッド
	private Label makeNote(String text) {
		Label l = new Label(text);
		l.setTextFill(Color.web("#9a9ab0"));
		l.setFont(Font.font("PixelMplus12", 13));
		l.setWrapText(true);
		return l;
	}

	// アイコン＋説明文を横並びにする補助メソッド
	private HBox makeIconNote(Node icon, String text) {
		Label l = new Label(text);
		l.setTextFill(Color.web("#9a9ab0"));
		l.setFont(Font.font("PixelMplus12", 13));
		l.setWrapText(true);

		HBox row = new HBox(10, icon, l);
		row.setAlignment(Pos.CENTER_LEFT);
		return row;
	}

	// 本編と同じ見た目の「ドット」を図形で作る補助メソッド
	private Circle makeDotIcon() {
		Circle dot = new Circle(4);
		dot.setFill(Color.web("#F4C022"));
		return dot;
	}

	// パワーエサの画像アイコンを作る補助メソッド
	private ImageView makePowerPelletIcon() {
		Image img = new Image(getClass().getResource("/picture/Chii_Item.png").toExternalForm());
		ImageView view = new ImageView(img);
		view.setFitWidth(22);
		view.setFitHeight(22);
		view.setPreserveRatio(true);
		return view;
	}
}
