package story;

import control.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Gameover extends Application {

	private int score = 0;

	@Override
	public void start(Stage stage) {
		stage.setTitle("ゲームオーバー");
		stage.setScene(create(stage, null, score));
		stage.centerOnScreen();
		stage.show();
	}

public static Scene create(Stage stage, Runnable retryAction, int score) {
		
		// ゲームオーバー画面が表示されたタイミングで効果音を再生
		start.SoundManager.play(start.SoundManager.GAMEOVER);

		// ===== ベースとなるルートコンテナ =====
		StackPane root = new StackPane();

		// ===== 1. タイトル・スコア部 =====
		Label gameText = new Label("GAME");
		Label overText = new Label("OVER");
		gameText.getStyleClass().add("gameover-title");
		overText.getStyleClass().add("gameover-title");

		Label scoreLabel = new Label("SCORE : " + score);
		scoreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

		Label newRecordLabel = new Label("NEW RECORD!!");
		newRecordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gold;");

		VBox titleBox = new VBox(5);
		titleBox.getChildren().addAll(gameText, overText, scoreLabel);
		if (GameController.isNewRecord()) {
			titleBox.getChildren().add(newRecordLabel);
		}
		titleBox.setAlignment(Pos.CENTER);

		// ===== 2. キャラクター画像（仙石さん）=====
		ImageView icon = new ImageView();
		try {
			var imgStream = Gameover.class.getResourceAsStream("/picture/syujinkou(gameover).png");
			if (imgStream != null) {
				icon.setImage(new Image(imgStream));
			}
		} catch (Exception e) {
			System.out.println("⚠️ 画像の読み込みに失敗しました。");
		}
		icon.setPreserveRatio(true);

		// ===== 3. 操作ボタン部 =====
		Button retryBtn = new Button("リトライする");
		retryBtn.getStyleClass().add("gameover-button");
		retryBtn.setOnAction(e -> {
			start.SoundManager.play(start.SoundManager.RETRY); 
			Timeline delay = new Timeline(
					new KeyFrame(Duration.millis(500), ev -> {
						if (retryAction != null) {
							retryAction.run();
						}
					}));
			delay.play();
		});

		Button titleBtn = new Button("タイトルへ");
		titleBtn.getStyleClass().add("gameover-button");
		titleBtn.setOnAction(e -> {
			start.SoundManager.play(start.SoundManager.SELECT);
			Timeline delay = new Timeline(
					new KeyFrame(Duration.millis(500), ev -> {
						GameController.switchStart(stage);
					}));
			delay.play();
		});

		VBox buttonColumn = new VBox(12, retryBtn, titleBtn);
		buttonColumn.setAlignment(Pos.CENTER);

		// ===== 4. メインレイアウト =====
		VBox contentLayout = new VBox(20);
		contentLayout.setAlignment(Pos.CENTER);
		contentLayout.setPadding(new Insets(20));
		contentLayout.setMaxWidth(800); // PCでも見栄えが良い最大幅
		contentLayout.getChildren().addAll(titleBox, icon, buttonColumn);

		StackPane uiContainer = new StackPane(contentLayout);
		uiContainer.setAlignment(Pos.CENTER);

		// ==========================================
		// 🛠️【ここが魔法！】画面幅に応じた動的レスポンシブ処理
		// ==========================================
		root.widthProperty().addListener((obs, oldVal, newVal) -> {
			double width = newVal.doubleValue();
			
			if (width < 600) {
				// 📱 スマホ画面（幅600px未満）のときの設定
				gameText.setStyle("-fx-font-size: 42px;");
				overText.setStyle("-fx-font-size: 42px;");
				scoreLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
				newRecordLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: gold;");
				
				// 仙石さんをスマホサイズに
				icon.setFitWidth(180);
				icon.setFitHeight(230);
				
				// ボタンをスマホサイズに
				retryBtn.setPrefSize(260, 55);
				titleBtn.setPrefSize(260, 55);
				
				// 全体を少し上に引き上げる
				contentLayout.setTranslateY(-30);
				contentLayout.setSpacing(15);
			} else {
				// 💻 PC画面（幅600px以上）のときの設定（迫力ある大画面モード！）
				gameText.setStyle("-fx-font-size: 70px;");
				overText.setStyle("-fx-font-size: 70px;");
				scoreLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");
				newRecordLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: gold;");
				
				// 仙石さんをPC用に大きく復活！
				icon.setFitWidth(320);
				icon.setFitHeight(416);
				
				// ボタンも押しやすいPCサイズに拡大
				retryBtn.setPrefSize(320, 70);
				titleBtn.setPrefSize(320, 70);
				
				// PCは画面が広いので中央にどっしり構える
				contentLayout.setTranslateY(0);
				contentLayout.setSpacing(25);
			}
		});

		// ===== 5. 背景・ルート構成 =====
		ImageView bg = new ImageView();
		try {
			var bgStream = Gameover.class.getResourceAsStream("/picture/gameover.jpg");
			if (bgStream != null) {
				bg.setImage(new Image(bgStream));
			}
		} catch (Exception e) {
			System.out.println("⚠️ 背景画像の読み込みに失敗しました。");
		}
		bg.setPreserveRatio(false);

		Rectangle whiteOverlay = new Rectangle();
		whiteOverlay.setFill(Color.rgb(255, 255, 255, 0.15));

		bg.fitWidthProperty().bind(root.widthProperty());
		bg.fitHeightProperty().bind(root.heightProperty());
		whiteOverlay.widthProperty().bind(root.widthProperty());
		whiteOverlay.heightProperty().bind(root.heightProperty());

		root.getChildren().addAll(bg, whiteOverlay, uiContainer);
		
		Scene scene = new Scene(root);
		
		var cssUrl = Gameover.class.getResource("/css/gameover.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}
		
		stage.setMinWidth(320);
		stage.setMinHeight(568);
		stage.setMaxWidth(1920);
		stage.setMaxHeight(1080);
		
		return scene;
	}
}