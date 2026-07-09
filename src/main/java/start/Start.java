package start;

import control.GameController;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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

public class Start extends Application {
	private AnimationTimer timer;
	private AudioClip clickSound;
	private PauseTransition pause;

	/**
	 * 値をmin〜maxの範囲に収める（フォントサイズ・パディングの可変計算用）
	 */
	private static double clamp(double min, double value, double max) {
		return Math.max(min, Math.min(value, max));
	}

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
		timer.start();
		//重ねて表示するためのレイアウト(レイヤー構造の作成)
		StackPane root = new StackPane();

		//縦に並べるための箱を作成
		VBox ui = new VBox();
		//uiによる配置の間隔を設定
		ui.setSpacing(20);
		//中央に設定
		ui.setAlignment(Pos.CENTER);

		//title用の画像読み込み
		Image image = new Image(getClass().getResource("/picture/title.png").toExternalForm());
		//画像を表示、画像サイズを調整
		ImageView imageView = new ImageView(image);
		// 縦横比維持
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
							timer.stop();

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

		//rootを中身とした1000×800のウィンドウを作成（初期サイズ。以降は可変）
		Scene scene = new Scene(root, 1000, 800);

		// 背景Paneをシーンサイズに追従させる
		bgPane.prefWidthProperty().bind(scene.widthProperty());
		bgPane.prefHeightProperty().bind(scene.heightProperty());

		// ===== ここからレスポンシブ対応の追加分 =====

		// UI全体の最大幅をシーン幅の90%に制限（画面幅に応じて伸縮）
		ui.maxWidthProperty().bind(scene.widthProperty().multiply(0.9));

		// タイトル画像の幅をシーン幅の50%に、比率維持で追従させる
		imageView.fitWidthProperty().bind(scene.widthProperty().multiply(0.5));

		// ボタン群の幅・高さをシーンサイズの割合で追従させる
		for (Button b : new Button[] { btn1, btn2, btn3 }) {
			b.prefWidthProperty().bind(scene.widthProperty().multiply(0.35));
			b.prefHeightProperty().bind(scene.heightProperty().multiply(0.11));
			// 小さい画面でボタンが潰れすぎないよう最小サイズを確保
			b.setMinWidth(160);
			b.setMinHeight(48);
		}

		// メインボタン(btn1〜3)のフォントサイズと横パディングをシーン幅に応じて可変にする
		// （CSSの game-button クラスは -fx-font-size: 30px 固定なので、
		//   ここでインラインstyleとして上書きし、狭い画面でテキストがはみ出さないようにする）
		for (Button b : new Button[] { btn1, btn2, btn3 }) {
			b.styleProperty().bind(Bindings.createStringBinding(
					() -> {
						double w = scene.getWidth();
						// フォントサイズ: 14px〜30pxの範囲でシーン幅の4.5%に追従
						double fontSize = clamp(14, w * 0.045, 30);
						// 横パディング: 8px〜20pxの範囲でシーン幅の2%に追従
						double paddingH = clamp(8, w * 0.02, 20);
						return "-fx-font-size: " + fontSize + "px; "
								+ "-fx-padding: 8 " + paddingH + " 8 " + paddingH + ";";
					},
					scene.widthProperty()));
		}

		// ？ボタンはシーンサイズに応じて小さめに追従（正方形を維持）
		btnHelp.prefWidthProperty().bind(scene.widthProperty().multiply(0.05));
		btnHelp.prefHeightProperty().bind(btnHelp.widthProperty());
		btnHelp.setMinWidth(36);
		btnHelp.setMinHeight(36);

		// ？ボタンのフォントサイズもシーン幅に応じて可変にする（12px〜20px）
		btnHelp.styleProperty().bind(Bindings.createStringBinding(
				() -> "-fx-font-size: " + clamp(12, scene.getWidth() * 0.02, 20) + "px;",
				scene.widthProperty()));

		// ===== レスポンシブ対応の追加分ここまで =====

		//CSSを接続
		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());

		// ウィンドウの最小限のサイズを設定（スマホの幅にも対応できるよう縮小）
		stage.setMinWidth(320);
		stage.setMinHeight(480);
		// 最大サイズの制限は撤廃し、ブラウザ／画面いっぱいまで拡大できるようにする
		// stage.setMaxWidth(1920);
		// stage.setMaxHeight(1080);

		//ウィンドウの名前を設定
		stage.setTitle("スタート画面");

		//ウィンドウの中身を設定・表示
		stage.setScene(scene);
		//WindowUtil.fillScreen(stage);	//最大化
		//リセットするため、一度隠してから再表示する
		//stage.hide();
		stage.show();
	}

	//launchをmainで呼び出すことでjavafxのアプリが起動
	public static void main(String[] args) {
		launch();
	}

}