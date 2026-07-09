package start;

import control.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.ResponsiveUtil;

public class Start extends Application {
	private AnimationTimer timer;
	private AudioClip clickSound;
	private PauseTransition pause;

	private void cleanup() {

		// 背景アニメーション停止
		if (timer != null) {
			timer.stop();
			timer = null;
		}

		// 遅延処理停止
		if (pause != null) {
			pause.stop();
			pause = null;
		}

		// 効果音停止
		if (clickSound != null) {
			clickSound.stop();
			clickSound = null;
		}

		// BGM停止
		Bgm.stopBGM();
	}

	@Override
	public void start(Stage stage) {
		try {
			// ファイルをストリームとして読み込む
			var fontStream = getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf");

			if (fontStream == null) {
				// コンソールにこの警告が出たら、フォントファイルの場所（パス）が間違っています
				System.err.println("【警告】フォントファイルが見つかりません。パスを確認してください。");
			} else {
				// 2. JavaFXにフォントを登録
				Font loadedFont = Font.loadFont(fontStream, 20);

				if (loadedFont == null) {
					System.err.println("【警告】フォントファイルの読み込みに失敗しました（ファイルが壊れているか形式が違います）");
				} else {
					// 成功した場合、JavaFXが認識した正確な「フォント名」をコンソールに出します
					System.out.println("【成功】フォントを読み込みました。名前: " + loadedFont.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 画面を作る前にレトロフォントファイルを読み込む
		try {
			Font.loadFont(getClass().getResourceAsStream("/font/PixelMplus12-Regular.ttf"), 20);
		} catch (Exception e) {
			System.err.println("フォントの読み込みに失敗しました: " + e.getMessage());
		}

		//背景用の画像を読み込み
		Image bgImage = new Image(
				getClass().getResource("/picture/background.png").toExternalForm());

		// 画像の元のサイズを取得
		double bgWidth = bgImage.getWidth();
		double bgHeight = bgImage.getHeight();
		// 背景をタイルのように敷き詰めるためのPaneを作成
		Pane bgPane = new Pane();
		final double[] scrollX = { 0 };

		// アニメーション
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// 1pxずつ左に動かす
				scrollX[0] -= 1;

				// 画像の横幅分動いたら0に戻す（これで無限ループ）
				if (scrollX[0] <= -bgWidth) {
					scrollX[0] = 0;
				}

				// 画像は元のサイズのまま、表示位置だけをずらして背景を再描画
				ImagePattern pattern = new ImagePattern(
						bgImage,
						scrollX[0], 0, // X座標をずらす, Y座標は固定
						bgWidth, bgHeight, // 画像の本来のサイズを維持
						false // 絶対座標指定
				);

				// bgPane全体の背景をこのパターンで塗りつぶす
				bgPane.setBackground(new Background(
						new BackgroundFill(pattern, null, null)));

			}
		};
		//ここから自動的にループ開始(AnimationTimerとペアで使用)
		//timer.start();
		//重ねて表示するためのレイアウト(レイヤー構造の作成)
		StackPane root = new StackPane();

		// 🛠️【白飛び対策】初期背景色を「黒」に指定
		root.setStyle("-fx-background-color: black;");

		//縦に並べるための箱を作成
		VBox ui = new VBox();
		// UIが広がりすぎないよう最大幅を制限
		ui.setMaxWidth(800);
		//uiによる配置の間隔を設定
		ui.setSpacing(20);
		//中央に設定
		ui.setAlignment(Pos.CENTER);

		//title用の画像読み込み
		Image image = new Image(getClass().getResource("/picture/title.png").toExternalForm());
		//画像を表示、画像サイズを調整
		ImageView imageView = new ImageView(image);
		
		// 🛠️【修正点】固定の大きいサイズ(500x400)がレスポンシブ処理と競合していたため削除
		// 縦横比維持だけを残し、サイズは下のResponsiveUtilに完全に任せます。
		imageView.setPreserveRatio(true);

		//縦に並べるための箱を作成
		VBox buttonBox = new VBox();
		// buttonBoxによる配置の間隔を設定
		buttonBox.setSpacing(20);
		// 中央に設定
		buttonBox.setAlignment(Pos.CENTER);
		// BGMの再生
		Bgm.playBGM("/music/startbgm.mp3");

		// 音声読み込み
		clickSound = new AudioClip(
				getClass().getResource("/music/select.mp3").toExternalForm());
		// 音量調整
		clickSound.setVolume(0.4);

		// ストーリーモードへ飛ぶボタンを作成
		Button btn1 = new Button("▶ストーリー");
		// 🛠️【修正点】固定の `setPrefSize(300, 100);` はレスポンシブと競合するため削除

		//btn1にCSSのgame-buttonを付与
		btn1.getStyleClass().add("game-button");
		btn1.setOnAction(e -> {
			try {

				// 音を鳴らす
				clickSound.stop();
				clickSound.play();

				// 0.5秒待つ
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {

							//音と背景停止
							cleanup();

							//画面遷移
							GameController.startToStory(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//練習モードへ飛ぶボタン作成
		Button btn2 = new Button("⚔練習モード");
		// 🛠️【修正点】固定の `setPrefSize(300, 100);` は削除

		//setOnAction:クリックしたときに実行する処理を記述
		//(e->:クリックされたら実行される処理を書いていくという記号)
		btn2.setOnAction(e -> {
			try {

				// 音を鳴らす
				clickSound.stop();
				clickSound.play();

				// 0.5秒待つ
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {

							// 背景停止
							cleanup(); // 🛠️ 安全のため cleanup() に統一

							// 画面遷移
							GameController.switchToPractice(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//btn2にCSSのgame-buttonを付与
		btn2.getStyleClass().add("game-button");

		//無限モードへ飛ぶボタン作成
		Button btn3 = new Button("ゲーム終了");
		// 🛠️【修正点】固定の `setPrefSize(300, 100);` は削除

		//btn3にCSSのgame-buttonを付与
		btn3.getStyleClass().add("game-button");

		// クリックしたときにアプリを終了する処理
		btn3.setOnAction(e -> {
			try {

				// 効果音を鳴らす
				clickSound.stop();
				clickSound.play();

				// 他のボタンに合わせて0.5秒後にアプリを閉じる
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {

							// 背景アニメーションやBGMを安全に停止
							cleanup();

							// JavaFXアプリを終了する
							Platform.exit();
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//3個のボタンを縦に並ぶ箱に入れる
		buttonBox.getChildren().addAll(btn1, btn2, btn3);

		//titleの画像と3個のボタンが入った箱を縦に並ぶ箱に入れる
		ui.getChildren().addAll(imageView, buttonBox);

		// 操作説明画面へ飛ぶ「？」アイコンボタン
		Button btnHelp = new Button("？");
		// 🛠️【修正点】固定の `setPrefSize(50, 50);` は削除
		btnHelp.getStyleClass().add("help-button"); // CSSで丸くする

		btnHelp.setOnAction(e -> {
			try {
				// 音を鳴らす
				clickSound.stop();
				clickSound.play();

				// 0.5秒待つ
				Timeline delay = new Timeline(
						new KeyFrame(Duration.millis(500), ev -> {

							// 背景アニメーションやBGMを安全に停止
							cleanup();

							// 操作説明画面へ遷移
							GameController.switchToHelp(stage);
						}));
				delay.play();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		//下から背景、UIの箱に入れたものの順でレイヤー構造のrootに入れる
		root.getChildren().addAll(bgPane, ui);

		// ？ボタンを画面右上に固定表示するための配置
		StackPane.setAlignment(btnHelp, Pos.TOP_RIGHT);
		StackPane.setMargin(btnHelp, new Insets(20));
		root.getChildren().add(btnHelp);

		//rootを中身とした1000×800のウィンドウを作成
		Scene scene = new Scene(root, 1000, 800);
		//
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		// ===== ここからレスポンシブ対応の追加分 =====

		// UI全体の最大幅をシーン幅の90%に制限（画面幅に応じて伸縮）
		ResponsiveUtil.bindMaxWidth(ui, scene.widthProperty(), 0.9);

		// タイトル画像の幅をシーン幅の50%に、比率維持で追従させる
		ResponsiveUtil.bindImageFitWidth(imageView, scene.widthProperty(), 0.5);

		// ボタン群の幅・高さをシーンサイズの割合で追従させる（最小サイズも確保）
		for (Button b : new Button[] { btn1, btn2, btn3 }) {
			ResponsiveUtil.bindPrefSize(b, scene.widthProperty(), 0.35, scene.heightProperty(), 0.11, 160, 44);
		}

		// メインボタン(btn1〜3)のフォントサイズと横パディングをシーン幅に応じて可変にする
		for (Button b : new Button[] { btn1, btn2, btn3 }) {
			ResponsiveUtil.bindButtonFontAndPadding(b, scene.widthProperty(),
					14, 0.045, 30, // フォント: 14px〜30px
					8, 0.02, 20); // 横パディング: 8px〜20px
		}

		// ？ボタンはシーンサイズに応じて小さめに追従（正方形を維持）
		btnHelp.prefWidthProperty().bind(scene.widthProperty().multiply(0.05));
		btnHelp.prefHeightProperty().bind(btnHelp.widthProperty());
		btnHelp.setMinWidth(36);
		btnHelp.setMinHeight(36);

		// ？ボタンのフォントサイズもシーン幅に応じて可変にする（12px〜20px）
		ResponsiveUtil.bindButtonFontAndPadding(btnHelp, scene.widthProperty(),
				12, 0.02, 20, // フォント: 12px〜20px
				4, 0.0, 4); // パディングは元のCSS(4 4)寄りに固定気味にする

		// ===== レスポンシブ対応の追加分ここまで =====


		//CSSを接続
		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());
		
		// 🛠️【スマホ対応】ウィンドウの最小サイズを320x480に引き下げ
		stage.setMinWidth(320);
		stage.setMinHeight(480);
		stage.setMaxWidth(1920); // PC大画面やブラウザ最大化時の最大サイズ制限
		stage.setMaxHeight(1080);

		//ウィンドウの名前を設定
		stage.setTitle("スタート画面");

		//ウィンドウの中身を設定・表示
		stage.setScene(scene);
		stage.show();
		
		// 🛠️ 画面表示が完了した次のフレームでタイマーを開始する（白飛び対策）
		Platform.runLater(() -> {
			if (timer != null) {
				timer.start();
			}
		});
	}

	//launchをmainで呼び出すことでjavafxのアプリが起動
	public static void main(String[] args) {
		launch();
	}

}