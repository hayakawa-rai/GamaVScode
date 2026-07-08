package story;

import control.GameController;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Storyclear extends Application {

	private Stage stage;
	private AudioClip clickSound;
	private MediaPlayer endingBgm;
	private TranslateTransition roll;
	private PauseTransition startWait;
	private PauseTransition endWait;

	// 💡【JPro対応】GameControllerのエラーを消すための静的メソッド
	public static void createAndStart(Stage currentStage) {
		if (currentStage == null) return;

		Storyclear app = new Storyclear();
		app.stage = currentStage;
		
		// 新しいSceneを生成して適用
		Scene scene = app.clearScene(currentStage);
		currentStage.setScene(scene);
		currentStage.setTitle("STORY CLEAR");
		currentStage.centerOnScreen();
		currentStage.show();
	}

	// 💡【JPro対応】アニメーションやBGMなどの安全なリソース解放処理
	private void cleanup() {
		if (roll != null) { roll.stop(); roll = null; }
		if (startWait != null) { startWait.stop(); startWait = null; }
		if (endWait != null) { endWait.stop(); endWait = null; }
		if (endingBgm != null) { endingBgm.stop(); endingBgm = null; }
		if (clickSound != null) { clickSound.stop(); clickSound = null; }
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("STORY CLEAR");
		stage.setScene(clearScene(stage));
		stage.centerOnScreen();
		stage.show();
	}

	// 引数なしの互換用メソッド
	public Scene clearScene() {
		return clearScene(this.stage);
	}

	// 💡 引数付きのメインScene構築メソッド
	public Scene clearScene(Stage currentStage) {
		if (currentStage != null) {
			this.stage = currentStage;
		}

		// ==================================================
		// 音声・BGMの読み込み（JPro対応 try-catch 保護）
		// ==================================================
		try {
			var selectUrl = getClass().getResource("/music/select.mp3");
			if (selectUrl != null) {
				clickSound = new AudioClip(selectUrl.toExternalForm());
			}
		} catch (Exception e) {
			System.err.println("選択SEの読み込みに失敗しました: " + e.getMessage());
		}

		try {
			var bgmUrl = getClass().getResource("/music/Storyclear_bgm2.mp3");
			if (bgmUrl != null) {
				Media media = new Media(bgmUrl.toExternalForm());
				endingBgm = new MediaPlayer(media);
				endingBgm.setVolume(0.5);
				endingBgm.setCycleCount(MediaPlayer.INDEFINITE);
			}
		} catch (Exception e) {
			System.err.println("BGMの読み込みに失敗しました（環境による制限の可能性があります）: " + e.getMessage());
		}

		// ==================================================
		// STORY CLEAR!! タイトル演出
		// ==================================================
		Text storyClear = new Text("STORY CLEAR!!");
		// 💡 フォントサイズは下部のレスポンシブバインドで動的に制御されます
		storyClear.setStyle("-fx-font-family:'PixelMplus12'; -fx-font-weight:bold; -fx-fill:black; -fx-stroke:white; -fx-stroke-width:3;");

		try {
			var clearSoundUrl = getClass().getResource("/music/Storyclear_sound.mp3");
			if (clearSoundUrl != null) {
				AudioClip clearSound = new AudioClip(clearSoundUrl.toExternalForm());
				clearSound.play();
			}
		} catch (Exception e) {
			System.err.println("クリア音の再生に失敗しました: " + e.getMessage());
		}

		// ==================================================
		// スタッフロール本文
		// ==================================================
		Text credits = new Text("━━━━━━━━━━━━━━━━━━\n\n" +
				"PROGRAMMER\n\n" +
				"N.Y\n" + "H.R\n" + "O.S\n" + "K.S\n" + "W.M\n" + "W.T\n" + "F.O\n" + "M.R\n\n" +
				"━━━━━━━━━━━━━━━━━━\n\n" +
				"COOPERATION\n\n" +
				"先輩社員\n\n" +
				"━━━━━━━━━━━━━━━━━━\n\n" +
				"SPECIAL THANKS\n\n" +
				"遊んでくださった皆様\n\n" +
				"━━━━━━━━━━━━━━━━━━\n\n" +
				"最後まで諦めず\n" + "会社を取り戻してくれて\n" + "ありがとうございました。\n\n" +
				"━━━━━━━━━━━━━━━━━━\n\n" +
				"平和を取り戻した会社で\n" + "今日もまた\n" + "新しい一日が始まります。\n\n" +
				"━━━━━━━━━━━━━━━━━━\n\n"); 

		credits.setStyle("-fx-font-family: 'PixelMplus12'; -fx-fill: #334455;");
		credits.setTextAlignment(TextAlignment.CENTER);

		// ==================================================
		// ゲームタイトルロゴ & THANK YOU
		// ==================================================
		Image logoImage = new Image(getClass().getResourceAsStream("/picture/title.png"));
		ImageView logoView = new ImageView(logoImage);
		logoView.setPreserveRatio(true);

		Text endText = new Text("THANK YOU FOR PLAYING!!");
		endText.setStyle("-fx-font-family:'PixelMplus12'; -fx-font-weight:bold; -fx-fill:black; -fx-stroke:white; -fx-stroke-width:3;");
		endText.setVisible(false);
		endText.setTextAlignment(TextAlignment.CENTER);

		// ==================================================
		// タイトルへ戻るボタン
		// ==================================================
		Button titleButton = new Button("タイトルへ");
		titleButton.getStyleClass().add("game-button2");
		titleButton.setVisible(false);
		titleButton.setStyle("-fx-font-family:'PixelMplus12'; -fx-background-color:#EAF4FF; -fx-text-fill:#2B4A6A; -fx-border-color:white; -fx-border-width:2; -fx-background-radius:10; -fx-border-radius:10;");

		titleButton.setOnAction(e -> {
			if (clickSound != null) {
				clickSound.stop();
				clickSound.play();
			}
			if (endingBgm != null) {
				endingBgm.stop();
			}
			cleanup(); // 画面遷移前に安全にリソースを全て停止
			GameController.switchStart(this.stage);
		});

		// レイアウトコンテナの構築
		VBox thankBox = new VBox(30);
		thankBox.setAlignment(Pos.CENTER);
		thankBox.getChildren().addAll(logoView, endText, titleButton);

		VBox rollBox = new VBox(30);
		rollBox.setAlignment(Pos.TOP_CENTER);
		rollBox.getChildren().add(credits);

		// 背景画像とフィルター層
		Image bgImage = new Image(getClass().getResourceAsStream("/picture/EMD_picture.jpg"));
		ImageView bgView = new ImageView(bgImage);
		bgView.setPreserveRatio(false);

		Rectangle whiteFilter = new Rectangle();
		whiteFilter.setFill(Color.rgb(255, 255, 255, 0.70));

		// ==================================================
		// ウィンドウ全体のレイヤーを順に配置
		// ==================================================
		StackPane root = new StackPane();
		root.getChildren().addAll(bgView, whiteFilter, storyClear);

		// ==================================================
		// ウィンドウサイズを設定
		// ==================================================
		Scene scene = new Scene(root, 1000, 800);

		// ==================================================
		// スタッフロールアニメーション（レスポンシブ動的バインディング）
		// ==================================================
		roll = new TranslateTransition(Duration.seconds(23), rollBox);
		
		// 開始時の縦位置を「現在の画面の高さ」にバインド（常に画面の下からスタート）
		rollBox.translateYProperty().bind(scene.heightProperty());
		
		// ⭕【JPro修正】ブラウザ毎のフォント高さのズレに耐えられるよう、正確に画面外へ抜けきる終了式
		roll.toYProperty().bind(rollBox.heightProperty().add(scene.heightProperty()).multiply(-1));

		// ==================================================
		// コピーライト & 会社ロゴ
		// ==================================================
		Text copyrightText = new Text("2026年度 EMD 新卒一同");
		copyrightText.setStyle("-fx-font-family:'PixelMplus12'; -fx-fill:#334455;");

		Image companyLogoImage = new Image(getClass().getResourceAsStream("/picture/EMD_logo.png"));
		ImageView companyLogoView = new ImageView(companyLogoImage);
		companyLogoView.setPreserveRatio(true);

		VBox companyBox = new VBox(5);
		companyBox.setAlignment(Pos.BOTTOM_RIGHT);
		companyBox.getChildren().addAll(companyLogoView, copyrightText);
		companyBox.setMouseTransparent(true);
		StackPane.setAlignment(companyBox, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(companyBox, new Insets(0, 30, 20, 0));

		// ==================================================
		// 各種イベント・タイムライン制御
		// ==================================================
		// スタッフロール終了後の処理
		roll.setOnFinished(e -> {
			root.getChildren().remove(rollBox);
			root.getChildren().addAll(thankBox, companyBox);
			endText.setVisible(true);
			
			endWait = new PauseTransition(Duration.seconds(2));
			endWait.setOnFinished(ev -> {
				titleButton.setVisible(true);
			});
			endWait.play();
		});

		// 演出スタート待機（4秒間 STORY CLEAR!! を表示後にロール開始）
		startWait = new PauseTransition(Duration.seconds(4));
		startWait.setOnFinished(e -> {
			root.getChildren().remove(storyClear);
			root.getChildren().add(rollBox);

			// 💡 アニメーション開始直前にバインドを解除して挙動を安定化させる
			rollBox.translateYProperty().unbind();

			if (endingBgm != null) {
				endingBgm.play();
			}
			roll.play();
		});
		startWait.play();

		// ==================================================
		// コンポーネントの画面比率同期（レスポンシブ・JPro最適化）
		// ==================================================
		// 背景・フィルターサイズ同期
		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());
		whiteFilter.widthProperty().bind(scene.widthProperty());
		whiteFilter.heightProperty().bind(scene.heightProperty());
		
		// 各種テキストのフォントサイズをウィンドウ幅に応じて動的に変更（文字崩れ防止）
		storyClear.styleProperty().bind(Bindings.format("-fx-font-family:'PixelMplus12'; -fx-font-size:%.0fpx; -fx-font-weight:bold; -fx-fill:black; -fx-stroke:white; -fx-stroke-width:3;", scene.widthProperty().multiply(0.08)));
		endText.styleProperty().bind(Bindings.format("-fx-font-family:'PixelMplus12'; -fx-font-size:%.0fpx; -fx-font-weight:bold; -fx-fill:black; -fx-stroke:white; -fx-stroke-width:3;", scene.widthProperty().multiply(0.065)));
		credits.styleProperty().bind(Bindings.format("-fx-font-family:'PixelMplus12'; -fx-font-size:%.0fpx; -fx-fill:#334455;", scene.widthProperty().multiply(0.024)));
		copyrightText.styleProperty().bind(Bindings.format("-fx-font-family:'PixelMplus12'; -fx-font-size:%.0fpx; -fx-fill:#334455;", scene.widthProperty().multiply(0.018)));

		// 画像サイズ同期
		logoView.fitWidthProperty().bind(scene.widthProperty().multiply(0.35));
		companyLogoView.fitWidthProperty().bind(scene.widthProperty().multiply(0.06));

		// ボタンサイズ・フォント同期
		titleButton.prefWidthProperty().bind(scene.widthProperty().multiply(0.17));
		titleButton.prefHeightProperty().bind(scene.heightProperty().multiply(0.07));
		titleButton.styleProperty().bind(Bindings.format("-fx-font-family:'PixelMplus12'; -fx-font-size:%.0fpx; -fx-background-color:#EAF4FF; -fx-text-fill:#2B4A6A; -fx-border-color:white; -fx-border-width:2; -fx-background-radius:10; -fx-border-radius:10;", scene.widthProperty().multiply(0.024)));

		// ⭕【JPro対応】CSS読み込み時の安全対策（Nullチェック追加）
		var cssUrl = getClass().getResource("/css/style.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}
		
		// ステージのサイズ制限を設定
		if (this.stage != null) {
			this.stage.setMinWidth(1000);
			this.stage.setMinHeight(800);
			this.stage.setMaxWidth(1920);
			this.stage.setMaxHeight(1080);
		}

		return scene;
	}
}