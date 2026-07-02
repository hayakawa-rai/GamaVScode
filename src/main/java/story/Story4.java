package story;

import java.util.Arrays;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import start.Bgm;

public class Story4 extends Application {

	//ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
	private Stage stage;

	//javafxではstartを呼び出さないと起動しないため、親クラスのstartを上書きすることで子クラスを起動
	@Override
	public void start(Stage stage) {
		//受け取った変数Stageを自分のStageに保存
		this.stage = stage;
		//ウィンドウの中身を決定
		stage.setScene(story4());
		stage.setTitle("story4");
		
		stage.setWidth(javafx.stage.Screen.getPrimary().getVisualBounds().getWidth());
		stage.setHeight(javafx.stage.Screen.getPrimary().getVisualBounds().getHeight());
		
		stage.show();
	}

	private Timeline blink;
	private Timeline arrowMove;
	private AudioClip jumpSound;
	private AudioClip aSound;

	private TranslateTransition fall;
	//ストーリー終了処理を1回だけにする用
	private boolean isEndingStarted = false;
	//今どのメッセージを表示しているかのカウント用
	private int messageIndex = 0;
	//何文字目まで表示するか(タイピング演出のためのカウンター)
	private int charIndex = 0;
	//文字を表示途中か表示完了しているかどうか
	private boolean isTyping = false;
	//画面に表示するメッセージを入れる変数
	private Text text;
	//一定時間ことに処理を実行するタイマー
	private Timeline timeline;
	//ジャンプアクションをフィールドで管理
	private Timeline jumpAniki;
	private Timeline jumpsyujinkou;
	private Timeline jumpnari;
	private Timeline jumptaku;

	//新しいメッセージを表示するための準備用メソッド
	private void startTyping() {
		//文字カウントをリセット
		charIndex = 0;
		//画面を一旦空にする
		text.setText("");
		//今打ち込み中ですよという状態にする
		isTyping = true;
		timeline.playFromStart();
	}

	private void cleanup(Scene scene, StackPane base) {

		// タイピング
		if (timeline != null) {
			timeline.stop();
			timeline = null;
		}

		// ジャンプ
		if (jumpAniki != null) {
			jumpAniki.stop();
			jumpAniki = null;
		}
		if (jumpsyujinkou != null) {
			jumpsyujinkou.stop();
			jumpsyujinkou = null;
		}
		if (jumpnari != null) {
			jumpnari.stop();
			jumpnari = null;
		}
		if (jumptaku != null) {
			jumptaku.stop();
			jumptaku = null;
		}

		// ▼アニメ
		if (blink != null) {
			blink.stop();
			blink = null;
		}
		if (arrowMove != null) {
			arrowMove.stop();
			arrowMove = null;
		}

		// 落下アニメ
		if (fall != null) {
			fall.stop();
			fall = null;
		}

		// 効果音停止
		if (jumpSound != null)
			jumpSound.stop();
		if (aSound != null)
			aSound.stop();

		jumpSound = null;
		aSound = null;

		// BGM停止
		Bgm.stopBGM();

		// イベント解除
		if (scene != null) {
			scene.setOnMouseClicked(null);
		}

		// UI削除（推奨）
		if (base != null) {
			base.getChildren().clear();
		}
	}

	public Scene story4() {

		//BGMの再生
		Bgm.stopBGM();
		Bgm.playBGM("/music/endhing.mp3");
		//ジャンプ音の読み込み
		jumpSound = new AudioClip(
				getClass().getResource("/music/jump06.mp3").toExternalForm());
		//音量調整
		jumpSound.setVolume(0.2);
		//ダメージ音の読み込み
		aSound = new AudioClip(
				getClass().getResource("/music/damage2.mp3").toExternalForm());
		//音量調整
		aSound.setVolume(0.4);

		//会話内容を設定
		List<Dialogue> dialogues = Arrays.asList(
				new Dialogue("あにき", "……!?。", aSound, Color.RED),
				new Dialogue("仙石さん", "……終わりだな。", null, Color.WHITE),
				new Dialogue("あにき", "……ああ、負けだ。", null, Color.RED),
				new Dialogue("仙石さん", "やりすぎだ。会社まで巻き込んで。", jumpSound, Color.WHITE),
				new Dialogue("あにき", "分かってる。もうやめる。", jumpSound, Color.RED),
				new Dialogue("仙石さん", "ならいい。今ならまだ戻せる。", null, Color.WHITE),
				new Dialogue("あにき", "……悪かった。全部返します。", jumpSound, Color.RED),
				new Dialogue("仙石さん", "……はぁ。やっとか。", null, Color.WHITE),
				new Dialogue("仙石さん", "これで普通に働けるな。", jumpSound, Color.WHITE),
				new Dialogue("あにき", "ああ。一社員としてしっかり働きます。", jumpSound, Color.RED),
				new Dialogue("仙石さん", "よし。しっかり反省してるみたいだな。", null, Color.WHITE),
				new Dialogue("仙石さん", "戻るぞ。仕事が待ってる。", jumpSound, Color.WHITE),
				new Dialogue("あにき", "はい。先輩！！", jumpSound, Color.RED));

		//テキストクラスのインスタンスを作成
		text = new Text("");
		text.setStyle(
				"-fx-font-family: monospace;" //等間隔フォント
		);
		//上にあげる
		text.setTranslateY(-5);

		//吹き出し(textの背景)作成
		Rectangle box = new Rectangle();
		//横幅が760pxを超えたら自動で改行する
		text.setWrappingWidth(850);
		//吹き出しの色
		box.setFill(Color.rgb(0, 0, 0, 0.7));
		//白枠
		box.setStroke(Color.WHITE);
		box.setStrokeWidth(3);
		//吹き出しの角調整(0:カクカク、数値を大きくすると丸い)
		box.setArcWidth(0);
		box.setArcHeight(0);

		//▼マーク
		Text nextMark = new Text("▼");
		//色を白色に設定
		nextMark.setFill(Color.WHITE);
		//フォントサイズを設定
		//nextMark.setStyle("-fx-font-size: 20px;");
		//最初は非表示にする
		nextMark.setVisible(false);
		//下に下げる
		nextMark.setTranslateY(40);
		//▼のアニメーション設定
		blink = StoryUtils.createBlink(nextMark);
		arrowMove = StoryUtils.createArrowMove(nextMark);

		//会話している人の名前表示用
		Text nameText = new Text();
		nameText.setText(dialogues.get(messageIndex).speaker);

		//textと▼をまとめる
		//縦に並べる箱を作成
		VBox bubble = new VBox();
		//内側の余白を作成
		bubble.setPadding(new Insets(10));
		//部品同士の間隔の設定
		bubble.setSpacing(5);
		//幅を設定理想と最大をどちらも850pxに設定
		bubble.setPrefWidth(850);
		bubble.setMaxWidth(850);
		//中央左寄りに配置
		bubble.setAlignment(Pos.CENTER_LEFT);
		//bubble自体をウィンドウの中央下に配置
		StackPane.setAlignment(bubble, Pos.BOTTOM_CENTER);
		// ▼を中央に配置、下に余白を作成
		StackPane arrowBox = new StackPane(nextMark);
		arrowBox.setPadding(new Insets(0, 0, 15, 0));
		//テキストの下に▼を配置
		bubble.getChildren().addAll(nameText, text, arrowBox);

		//背景画像を読み込み
		Image bgImage = new Image(
				getClass().getResourceAsStream("/picture/shatyoroom.jpg"));
		//背景画像の表示
		ImageView bgView = new ImageView(bgImage);
		//余白を生まないために縦横比を無視
		bgView.setPreserveRatio(false);

		//人物画像の読み込み(あにき)
		Image anikiImage = new Image(
				getClass().getResourceAsStream("/picture/aniki-udekumi.png"));
		//人物画像の表示
		ImageView anikiView = new ImageView(anikiImage);
		//縦横比率を維持
		anikiView.setPreserveRatio(true);
		//人物画像の読み込み(仙石さん)
		Image syujinkouImage = new Image(
				getClass().getResourceAsStream("/picture/syujinkou(hello).png"));
		//人物画像の表示
		ImageView syujinkouView = new ImageView(syujinkouImage);
		//縦横比率を維持
		syujinkouView.setPreserveRatio(true);
		//人物画像の読み込み(なりなり)
		Image nariImage = new Image(
				getClass().getResourceAsStream("/picture/nari.png"));
		//人物画像の表示
		ImageView nariView = new ImageView(nariImage);
		//縦横比率を維持
		nariView.setPreserveRatio(true);
		//人物画像の読み込み(わだたく)
		Image takuImage = new Image(
				getClass().getResourceAsStream("/picture/taku2.png"));
		//人物画像の表示
		ImageView takuView = new ImageView(takuImage);
		//縦横比率を維持
		takuView.setPreserveRatio(true);
		//最初どの画像を表示するか設定
		nariView.setVisible(false);
		anikiView.setVisible(true);
		takuView.setVisible(false);

		//box(吹き出し)とbubble(テキストと▼)をまとめる
		//StackPaneにより同じ位置の前後に置かれるので重なって見える
		StackPane messageBox = new StackPane();
		messageBox.getChildren().addAll(box, bubble);

		//背景の設定(1番最初に入れたものが1番後ろになる)
		StackPane back = new StackPane();
		back.getChildren().add(bgView);

		//レイヤー構造を使用し吹き出しとテキストの位置を設定
		//mesageBoxによりまとめられたものを、ウィンドウのどこに表示するかを設定する
		BorderPane root = new BorderPane();
		//吹き出しを中央下に配置
		root.setBottom(messageBox);
		//rooのuiレイアウト背景を透明にする(背景などが映るようにするため)
		root.setStyle("-fx-background-color: transparent;");
		//Borderpaneにより一番下に表示されてしまうので、下に余白を設定する
		BorderPane.setMargin(messageBox, new Insets(0, 0, 30, 0));

		//ウィンドウ全体のレイヤー(下から背景、人物画像、吹き出しの順に配置)
		StackPane base = new StackPane();
		base.getChildren().addAll(bgView, syujinkouView, anikiView, nariView, takuView, root);
		// 現在のStage（window）から実際のサイズを取得する
		Scene scene = new Scene(base, 1000, 800);

		//メニューボタン作成

		Image menuImg = new Image(
				getClass().getResourceAsStream("/picture/menu.jpeg"));

		ImageView menuView = new ImageView(menuImg);
		menuView.setFitWidth(40);
		menuView.setFitHeight(40);

		Button menuBtn = new Button("");

		menuBtn.setGraphic(menuView);
		menuBtn.setStyle("-fx-background-color: transparent;");

		// 右上に配置
		StackPane.setAlignment(menuBtn, Pos.TOP_LEFT);
		StackPane.setMargin(menuBtn, new Insets(30));

		//メニュー画面追加
		StackPane menuOverlay = new StackPane();

		// 背景（うっすら暗く）
		menuOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
		menuOverlay.setVisible(false);
		menuOverlay.setPickOnBounds(true);
		// 中央のかわいいパネル
		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);

		// サイズを小さめにする
		menuBox.setMaxWidth(300);
		menuBox.setMaxHeight(250);

		//かわいい見た目
		menuBox.setStyle(
				"-fx-background-color: rgba(40,40,50,0.95);" + // 少し透明
						"-fx-background-radius: 20;" + // 角丸
						"-fx-padding: 25;" +
						"-fx-border-radius: 20;" +
						"-fx-border-color: white;" +
						"-fx-border-width: 2;");

		// ボタン
		Button resume = new Button("再開");
		Button titleBtn = new Button("タイトルへ");

		// ボタンをかわいく
		resume.getStyleClass().add("game-button2");
		titleBtn.getStyleClass().add("game-button2");

		// サイズ
		resume.setPrefWidth(180);
		titleBtn.setPrefWidth(180);

		// ボタン処理
		resume.setOnAction(e -> {
			menuOverlay.setVisible(false);

			if (timeline != null)
				timeline.play();
			if (blink != null)
				blink.play();
			if (arrowMove != null)
				arrowMove.play();
		});

		titleBtn.setOnAction(e -> {
			cleanup(scene, base);

			//スタート画面へ
			test.test2.GameController.switchStart(stage);
		});

		// 追加
		menuBox.getChildren().addAll(resume, titleBtn);
		menuOverlay.getChildren().add(menuBox);

		//最前面に追加
		base.getChildren().add(menuBtn);
		base.getChildren().add(menuOverlay);

		// 背景画像をウィンドウサイズに合わせる
		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());
		// 人物画像(あにき)をウィンドウサイズに合わせる(右に表示)
		anikiView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
		anikiView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
		anikiView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
		// 人物画像(なりなり)をウィンドウサイズに合わせる(右に表示)
		nariView.fitWidthProperty().bind(scene.widthProperty().multiply(0.5));
		nariView.fitHeightProperty().bind(scene.heightProperty().multiply(0.9));
		nariView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
		// 人物画像(わだたく)をウィンドウサイズに合わせる(右に表示)
		takuView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
		takuView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
		takuView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
		// 人物画像(仙石)をウィンドウサイズに合わせる(左に表示)(下に調整)
		syujinkouView.fitWidthProperty().bind(scene.widthProperty().multiply(0.34));
		syujinkouView.fitHeightProperty().bind(scene.heightProperty().multiply(1.4));
		syujinkouView.translateXProperty().bind(scene.widthProperty().multiply(-0.25));
		//boxのサイズをウィンドウに合わせる
		box.widthProperty().bind(scene.widthProperty().multiply(0.9));
		box.heightProperty().bind(scene.heightProperty().multiply(0.18));
		// テキストも追従
		text.wrappingWidthProperty().bind(box.widthProperty().multiply(0.95));
		// bubbleも合わせる
		bubble.prefWidthProperty().bind(box.widthProperty());
		bubble.maxWidthProperty().bind(box.widthProperty());
		//フォントサイズも変化
		text.styleProperty().bind(
				Bindings.format(
						"-fx-font-size: %.0fpx; -fx-font-family: monospace;",
						scene.widthProperty().multiply(0.03)));
		//▼のサイズも変化
		nextMark.styleProperty().bind(
				Bindings.format(
						"-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;",
						scene.widthProperty().multiply(0.02)));
		//名前表示も変化
		nameText.styleProperty().bind(
				Bindings.format(
						"-fx-font-size: %.0fpx; -fx-fill: lightgray;",
						scene.widthProperty().multiply(0.025)));
		//ウィンドウの最小限のサイズを設定(吹き出しから全てが飛び出してしまうため)
		stage.setMinWidth(1000);
		stage.setMinHeight(800);

		//メニュー表示処理
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {

				// メニュー表示
				menuOverlay.setVisible(true);

				// ストーリー停止
				if (timeline != null)
					timeline.pause();
				if (blink != null)
					blink.pause();
				if (arrowMove != null)
					arrowMove.pause();
			}
		});
		menuBtn.setOnAction(e -> {
			menuOverlay.setVisible(true);

			// ストーリー停止（ESCと同じ処理）
			if (timeline != null)
				timeline.pause();
			if (blink != null)
				blink.pause();
			if (arrowMove != null)
				arrowMove.pause();
		});

		fall = new TranslateTransition(Duration.seconds(1), takuView);
		fall.setToY(100);

		//文字表示用のタイマーを作成、50ミリ秒ごとに処理
		timeline = new Timeline(
				new KeyFrame(Duration.millis(50), e -> {
					//今再生されている会話テキストのリスト番号を取得
					Dialogue d = dialogues.get(messageIndex);
					//まだ文字が残っているかどうかを判断(文字が残っている間は処理を実行)
					if (charIndex < d.message.length()) {
						//文字カウントを増やす
						charIndex++;
						//最初に画像を下に落とす
						if (messageIndex == 0 && charIndex == 1) {
							fall.playFromStart();
						}
						//誰が話しているか情報取得(話者によって話者名・テキストの色を変化)
						String speaker = d.speaker;
						nameText.setText(speaker);
						text.setFill(d.textColor);
						if (speaker.equals("あにき")) {
							//あにきの画像を表示・なりなりの画像を非表示
							anikiView.setVisible(true);
							nariView.setVisible(false);
							takuView.setVisible(false);
						} else if (speaker.equals("仙石さん")) {
						} else if (speaker.equals("なりなり")) {
							//あにきの画像を非表示・なりなりの画像を表示
							anikiView.setVisible(false);
							nariView.setVisible(true);
							takuView.setVisible(false);
						} else if (speaker.equals("わだたく")) {
							//あにきの画像を非表示・わだたくの画像を表示
							anikiView.setVisible(false);
							nariView.setVisible(false);
							takuView.setVisible(true);
						}

						//表示しているメッセージに対して1文ずつ表示する文字数を増やしていく処理
						//例：メッセージがhelloのとき、h→he→hel→hell→hello
						text.setText(d.message.substring(0, charIndex));
						if (Math.random() < 0.5) {
						}
					} else {//全て表示し終わった後の処理
							//fales：タイピング中じゃない
						isTyping = false;
						//タイピング(文字を1文字ずつ表示するアニメーション)を停止
						timeline.stop();
						//▼を表示
						nextMark.setVisible(true);
						//▼点滅アニメーション表示
						blink.play();
						//▼を上下に揺らすアニメーション表示
						arrowMove.play();
					}
				}));
		//Timeline.INDEFINITE：無限ループ
		timeline.setCycleCount(Timeline.INDEFINITE);

		//クリックされたときの処理
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (menuOverlay.isVisible()) {
				if (e.getTarget() == menuBtn)
					return;
				e.consume();
				return;
			}
			//文字表示中ならスキップして全文表示する処理
			if (isTyping) {
				//タイピング停止
				timeline.stop();
				//今再生されている会話テキストのリスト番号を取得
				Dialogue d = dialogues.get(messageIndex);
				//一気に全文表示
				text.setText(d.message);
				//状態を更新してタイピングが終わったことにする
				isTyping = false;
				//▼を表示
				nextMark.setVisible(true);
				//▼点滅アニメーション表示
				blink.play();
				//▼を上下に揺らすアニメーション表示
				arrowMove.play();
				return;
			}
			//まだメッセージがある場合if文内のの処理を実行
			if (messageIndex < dialogues.size() - 1) {
				//メッセージカウントを増やす
				messageIndex++;

				//タイピングを再スタート
				startTyping();
				//▼を消す
				nextMark.setVisible(false);
				//▼の点滅を消す
				blink.stop();
				//▼を上下に揺らすアニメーションを停止
				arrowMove.stop();
				//今再生されている会話テキストのリスト番号を取得
				Dialogue d = dialogues.get(messageIndex);
				//誰が話しているかの情報取得
				String speaker = d.speaker;
				//あにきの画像差し替え
				if (messageIndex >= 2 && messageIndex <= 10) {
					// 差し替え画像
					anikiView.setImage(new Image(
							getClass().getResourceAsStream("/picture/aniki2.png")));
					// サイズ変更
					anikiView.fitWidthProperty().bind(scene.widthProperty().multiply(0.7));
					anikiView.fitHeightProperty().bind(scene.heightProperty().multiply(1.1));
					anikiView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
				} else {
					// 元の画像に戻す
					anikiView.setImage(anikiImage);
					anikiView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
					anikiView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
					anikiView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
				}
				//設定した音をならす
				if (d.sound != null && d.sound != jumpSound) {
					d.sound.stop();
					d.sound.play();
				}
				if (d.sound == jumpSound) {
					if (speaker.equals("あにき")) {
						jumpAniki = StoryUtils.createJumpAnimation(anikiView, d.sound);
						jumpAniki.playFromStart();

					} else if (speaker.equals("仙石さん")) {
						jumpsyujinkou = StoryUtils.createJumpAnimation(syujinkouView, d.sound);
						jumpsyujinkou.playFromStart();

					} else if (speaker.equals("なりなり")) {
						jumpnari = StoryUtils.createJumpAnimation(nariView, d.sound);
						jumpnari.playFromStart();
					} else if (speaker.equals("わだたく")) {
						jumptaku = StoryUtils.createJumpAnimation(takuView, d.sound);
						jumptaku.playFromStart();
					}
				}
			} else {//メッセージの最後まで行った後の処理
				if (isEndingStarted)
					return;
				isEndingStarted = true;

				nextMark.setVisible(false);

				//黒いフェード用
				Rectangle fadeRect = new Rectangle(1000, 800, Color.BLACK);
				fadeRect.setOpacity(0);
				base.getChildren().add(fadeRect);

				//フェードアウト
				FadeTransition fade = new FadeTransition(Duration.seconds(1.5), fadeRect);
				fade.setFromValue(0);
				fade.setToValue(1);

				//サイズをウィンドウに合わせる
				fadeRect.widthProperty().bind(scene.widthProperty());
				fadeRect.heightProperty().bind(scene.heightProperty());

				fade.setOnFinished(ev -> {
					cleanup(scene, base);
					base.getChildren().clear();
					// 画面遷移
					control.GameController.switchStart(stage);
				});

				fade.play();
			}
		});

		Dialogue d = dialogues.get(messageIndex);
		// jumpSound以外は普通に再生
		if (d.sound != null && d.sound != jumpSound) {
			d.sound.stop();
			d.sound.play();
		}

		//CSSを接続
		scene.getStylesheets().add(
				getClass().getResource("/css/style.css").toExternalForm());
		//最初の文章を表示(部品のすべての処理を終えてから文字を表示するため最後に記述)
		startTyping();

		return scene;
	}
}
