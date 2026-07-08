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
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Help extends Application {

	private AnimationTimer timer;
	private static final double ICON_COL_WIDTH = 40;

	// ページ管理用（2ページ分）
	private VBox[] pages;
	private int currentPage = 0;

	@Override
	public void start(Stage stage) {
		
		// BGMの再生
		Bgm.stopBGM();
		Bgm.playBGM("/music/startbgm.mp3");

		// ===== 背景 =====
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
				ImagePattern pattern = new ImagePattern(bgImage, scrollX[0], 0, bgWidth, bgHeight, false);
				bgPane.setBackground(new Background(new BackgroundFill(pattern, null, null)));
			}
		};
		timer.start();

		StackPane root = new StackPane();

		// ===== 中央の半透明パネル =====
		VBox panel = new VBox(16);
		panel.setMaxHeight(Region.USE_PREF_SIZE);
		panel.setAlignment(Pos.CENTER);
		panel.setMinWidth(560);   // ← 追加：最小幅を固定
		panel.setPrefWidth(560);  // ← 追加：基準幅を固定
		panel.setMaxWidth(560);   // 既存：最大幅
		panel.setPadding(new Insets(35, 45, 35, 45));
		panel.setStyle(
				"-fx-background-color: rgba(0,0,0,0.6);"
						+ "-fx-border-width: 2;"
						+ "-fx-background-radius: 4;"
						+ "-fx-border-radius: 4;");

		// タイトル
		Label title = new Label("操作方法");
		title.setTextFill(Color.web("#F4C022"));
		title.setFont(Font.font("PixelMplus12", FontWeight.BOLD, 40));

		// ===== ページ1：操作説明 =====
		VBox page1 = new VBox(16);
		page1.setAlignment(Pos.CENTER);
		page1.getChildren().addAll(
				makeLine("移動： ↑ / ↓ / ← / →   または   W / A / S / D"),
				makeLine("モバイルデバイスでの移動： 画面右下の矢印ボタン"));

		// ===== ページ2：アイテム説明 =====
		VBox page2 = new VBox(10);
		page2.setAlignment(Pos.CENTER_LEFT);
		page2.getChildren().addAll(
				makeNoteRow(makeDotIcon(), "エサを食べるとスコアが加算されます。"),
				makeNoteRow(makePowerPelletIcon(), "パワーエサを食べると一定時間敵を撃退することができます。"),
				makeNoteRow(null, "フルーツは一定数のエサを食べると出現し、一定時間が過ぎると消えてなくなります。"),
				makeNoteRow(null, "取得するとフルーツの種類に合わせたスコアが加算されます。"));

		// ===== ページ3：ルール説明 =====
		VBox page3 = new VBox(10);
		page3.setAlignment(Pos.CENTER_LEFT);
		page3.getChildren().addAll(
				makeNoteRow(null, "敵に触れるとゲームオーバーになります。"),
				makeNoteRow(null, "全てのエサを食べると一面クリアになります。"),
				makeNoteRow(null, "迷路の中段にある左端と右端の通路は「ワープトンネル」で、左端と右端が繫がった状態の通路になっています。"));

		VBox page1Wrapped = wrapWithHeading("操作", page1);
		VBox page2Wrapped = wrapWithHeading("アイテム", page2);
		VBox page3Wrapped = wrapWithHeading("ルール", page3);

		// pages配列・pageStackは「Wrapped」版を使う
		pages = new VBox[] { page1Wrapped, page2Wrapped, page3Wrapped };
		// 両ページを重ねて、表示中の1枚だけ見せる
		StackPane pageStack = new StackPane(page1Wrapped, page2Wrapped, page3Wrapped);

		// ===== ページインジケーター（● ○ のような小さい点） =====
		HBox indicator = new HBox(8);
		indicator.setAlignment(Pos.CENTER);
		Circle dotP1 = new Circle(4, Color.web("#4FD8E8"));
		Circle dotP2 = new Circle(4, Color.web("#4a4a5a"));
		Circle dotP3 = new Circle(4, Color.web("#4a4a5a"));
		indicator.getChildren().addAll(dotP1, dotP2, dotP3);

		Circle[] dots = { dotP1, dotP2, dotP3 };

		Runnable updatePage = () -> {
			for (int i = 0; i < pages.length; i++) {
				boolean show = (i == currentPage);
				pages[i].setVisible(show);
				pages[i].setManaged(show);
				dots[i].setFill(show ? Color.web("#4FD8E8") : Color.web("#4a4a5a"));
			}
		};
		updatePage.run();

		// 区切り線
		Separator divider = new Separator();
		divider.setStyle("-fx-background-color: #4a4a5a;");

		panel.getChildren().addAll(title, divider, pageStack, indicator);

		// ===== 左右の矢印ボタン（ページ切り替え） =====
		Button leftArrow = makeArrowButton("◀");
		Button rightArrow = makeArrowButton("▶");
		leftArrow.setOnAction(e -> {
			currentPage = (currentPage - 1 + pages.length) % pages.length;
			updatePage.run();
		});
		rightArrow.setOnAction(e -> {
			currentPage = (currentPage + 1) % pages.length;
			updatePage.run();
		});

		// ===== 戻るボタン（パネルの外・下に配置） =====
		Button backBtn = new Button("戻る");
		backBtn.setPrefSize(220, 60);
		backBtn.getStyleClass().add("help-button");
		backBtn.setOnAction(e -> {
			timer.stop();
			GameController.switchStart(stage);
		});

		// パネル本体 + 戻るボタンを縦に並べる（戻るボタンは黒枠の外）
		VBox panelWithBack = new VBox(20, panel, backBtn);
		panelWithBack.setAlignment(Pos.CENTER);

		// 矢印ボタンをパネルの左右に配置する横並びレイアウト
		HBox contentRow = new HBox(20, leftArrow, panelWithBack, rightArrow);
		contentRow.setAlignment(Pos.CENTER);

		root.getChildren().addAll(bgPane, contentRow);

		Scene scene = new Scene(root, 1000, 800);
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

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
	
	private VBox wrapWithHeading(String heading, VBox content) {
		Label h = new Label(heading);
		h.setTextFill(Color.web("#4FD8E8"));
		h.setFont(Font.font("PixelMplus12", FontWeight.BOLD, 18));
		VBox wrapper = new VBox(12, h, content);
		wrapper.setAlignment(Pos.CENTER_LEFT);
		return wrapper;
	}
	
	// 矢印ボタンを作る補助メソッド
	private Button makeArrowButton(String symbol) {
		Button btn = new Button(symbol);
		btn.setPrefSize(48, 48);
		btn.setFont(Font.font("PixelMplus12", FontWeight.BOLD, 20));
		btn.setStyle(
				"-fx-background-color: rgba(0,0,0,0.6);"
						+ "-fx-text-fill: #4FD8E8;"
						+ "-fx-border-width: 2;"
						+ "-fx-background-radius: 24;"
						+ "-fx-border-radius: 24;");
		return btn;
	}

	private Region spacer(double height) {
		Region r = new Region();
		r.setPrefHeight(height);
		return r;
	}

	private Label makeLine(String text) {
		Label l = new Label(text);
		l.setTextFill(Color.WHITE);
		l.setFont(Font.font("PixelMplus12", 16));
		return l;
	}

	private HBox makeNoteRow(Node icon, String text) {
		StackPane iconBox = new StackPane();
		iconBox.setMinWidth(ICON_COL_WIDTH);
		iconBox.setPrefWidth(ICON_COL_WIDTH);
		iconBox.setAlignment(Pos.CENTER);
		if (icon != null) {
			iconBox.getChildren().add(icon);
		}

		Label l = new Label(text);
		l.setTextFill(Color.WHITE);
		l.setFont(Font.font("PixelMplus12", 14));
		l.setWrapText(true);
		l.setMaxWidth(400);

		HBox row = new HBox(10, iconBox, l);
		row.setAlignment(Pos.CENTER_LEFT);
		return row;
	}

	private Circle makeDotIcon() {
		Circle dot = new Circle(4);
		dot.setFill(Color.web("#F4C022"));
		return dot;
	}

	private ImageView makePowerPelletIcon() {
		Image img = new Image(getClass().getResource("/picture/Chii_Item.png").toExternalForm());
		ImageView view = new ImageView(img);
		view.setFitWidth(40);
		view.setFitHeight(40);
		view.setPreserveRatio(true);
		return view;
	}
	
}