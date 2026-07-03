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
import util.WindowUtil;

public class Story2 extends Application {

	//ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
	private Stage stage;

	//javafxではstartを呼び出さないと起動しないため、親クラスのstartを上書きすることで子クラスを起動
	@Override
	public void start(Stage stage) {
		//受け取った変数Stageを自分のStageに保存
		this.stage = stage;
		//ウィンドウの中身を決定
		stage.setTitle("story1");
		WindowUtil.fillScreen(stage);
		stage.setScene(story2());

	}

	private Timeline blink;
	private Timeline arrowMove;

	private AudioClip jumpSound;
	private AudioClip cuteSound;
	private AudioClip appearSound;
	private AudioClip mysteriousSound;
	private AudioClip shineSound;
	private AudioClip damageSound;
	private AudioClip aSound;
	private AudioClip atacSound;

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

	private void cleanup(Scene scene) {

		// テキストタイピング
		if (timeline != null) {
			timeline.stop();
			timeline = null;
		}

		// ジャンプ系
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

		// ▼アニメーション
		if (blink != null) {
			blink.stop();
			blink = null;
		}
		if (arrowMove != null) {
			arrowMove.stop();
			arrowMove = null;
		}

		// 効果音（全部止める）
		if (jumpSound != null)
			jumpSound.stop();
		if (cuteSound != null)
			cuteSound.stop();
		if (appearSound != null)
			appearSound.stop();
		if (mysteriousSound != null)
			mysteriousSound.stop();
		if (shineSound != null)
			shineSound.stop();
		if (damageSound != null)
			damageSound.stop();
		if (aSound != null)
			aSound.stop();
		if (atacSound != null)
			atacSound.stop();

		jumpSound = null;
		cuteSound = null;
		appearSound = null;
		mysteriousSound = null;
		shineSound = null;
		damageSound = null;
		aSound = null;
		atacSound = null;

		// BGM停止
		Bgm.stopBGM();

		// イベント解除
		if (scene != null) {
			scene.setOnMouseClicked(null);
		}
	}

	public Scene story2() {

		//BGMの再生
		Bgm.stopBGM();
		Bgm.playBGM("/music/naribgm.mp3");
		//ジャンプ音の読み込み
		jumpSound = new AudioClip(
				getClass().getResource("/music/jump06.mp3").toExternalForm());
		//音量調整
		jumpSound.setVolume(0.2);
		//足音の読み込み
		cuteSound = new AudioClip(
				getClass().getResource("/music/footsteps.mp3").toExternalForm());
		//音量調整
		cuteSound.setVolume(0.3);
		//登場音の読み込み
		appearSound = new AudioClip(
				getClass().getResource("/music/appearance.mp3").toExternalForm());
		//音量調整
		appearSound.setVolume(0.3);
		//まぬけな音の読み込み
		mysteriousSound = new AudioClip(
				getClass().getResource("/music/nari.mp3").toExternalForm());
		//音量調整
		mysteriousSound.setVolume(0.3);
		//輝く音の読み込み
		shineSound = new AudioClip(
				getClass().getResource("/music/shine.mp3").toExternalForm());
		//音量調整
		shineSound.setVolume(0.3);
		//ダメージ音の読み込み
		damageSound = new AudioClip(
				getClass().getResource("/music/damage.mp3").toExternalForm());
		//音量調整
		damageSound.setVolume(0.3);
		//ダメージ音の読み込み
		aSound = new AudioClip(
				getClass().getResource("/music/damage2.mp3").toExternalForm());
		//音量調整
		aSound.setVolume(0.3);
		//攻撃音の読み込み
		atacSound = new AudioClip(
				getClass().getResource("/music/atac.mp3").toExternalForm());
		//音量調整
		atacSound.setVolume(0.3);
		//会話内容を設定
		List<Dialogue> dialogues = Arrays.asList(
				new Dialogue("なりなり", "あ、あれっ…！？", mysteriousSound, Color.ORANGE), //0
				new Dialogue("仙石さん", "弱いな！？", jumpSound, Color.WHITE), //1
				new Dialogue("なりなり", "ま、まだだ…まだ終わってない…！", null, Color.ORANGE), //2
				new Dialogue("仙石さん", "もう終わってる。", mysteriousSound, Color.WHITE), //3
				new Dialogue("なりなり", "ぐああああああ！！", damageSound, Color.ORANGE), //4
				new Dialogue("あにき", "クク…やはり雑魚か。", null, Color.RED), //5
				new Dialogue("仙石さん", "おい、完全に遊ばれてるぞ。", jumpSound, Color.WHITE), //6
				new Dialogue("あにき", "まあいい。次は特別だ。", null, Color.RED), //7
				new Dialogue("あにき", "来い、わだたく。", jumpSound, Color.RED), //8
				new Dialogue("わだたく", "……とてとて…", cuteSound, Color.PINK), //9
				new Dialogue("わだたく", "……ぴょこっ", appearSound, Color.PINK), //10
				new Dialogue("仙石さん", "……ん？", mysteriousSound, Color.WHITE), //11
				new Dialogue("仙石さん", "なんだこのかわいい生き物は。", jumpSound, Color.WHITE), //12
				new Dialogue("わだたく", "わだ〜たく〜…♪", shineSound, Color.PINK), //13
				new Dialogue("わだたく", "よろしくね♪", shineSound, Color.PINK), //14
				new Dialogue("仙石さん", "……弱そうだな。", mysteriousSound, Color.WHITE), //15
				new Dialogue("あにき", "見た目で判断するな。", null, Color.RED), //16
				new Dialogue("わだたく", "えいっ", atacSound, Color.PINK), //17
				new Dialogue("仙石さん", "ぐっ！？", aSound, Color.WHITE), //18
				new Dialogue("仙石さん", "な、何だ今の一撃は…！", null, Color.WHITE), //19
				new Dialogue("わだたく", "あそぼ？♪", shineSound, Color.RED), //20
				new Dialogue("わだたく", "いっぱいあそぼ〜♪", shineSound, Color.RED), //21
				new Dialogue("あにき", "そいつは俺のペットでな。", null, Color.RED), //22
				new Dialogue("あにき", "強そうに見えないが、遊ばれたら最後だ。", jumpSound, Color.RED)//23
		);

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
				getClass().getResourceAsStream("/picture/companyroom.jpg"));
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
				getClass().getResourceAsStream("/picture/taku.png"));
		//人物画像の表示
		ImageView takuView = new ImageView(takuImage);
		//縦横比率を維持
		takuView.setPreserveRatio(true);
		//最初どの画像を表示するか設定
		nariView.setVisible(false);
		anikiView.setVisible(true);
		takuView.setVisible(false);

		//挿絵画像の読み込み
		Image insertImage = new Image(
				getClass().getResourceAsStream("/picture/insert.png"));
		//画像の表示
		ImageView insertView = new ImageView(insertImage);
		insertView.setPreserveRatio(false); // 画面にフィットさせるため OFF
		insertView.setVisible(false); // 最初は非表示

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
		base.getChildren().addAll(bgView, syujinkouView, anikiView, nariView, takuView, root, insertView);
		// 画面サイズに合わせてSceneを作ることで、最大化済みStageでも中身が縮まないようにする
		Scene scene = new Scene(base);
		scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());

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
			cleanup(scene);

			//スタート画面へ
			control.GameController.switchStart(stage);
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
		//差し込み用の画像を調整
		insertView.fitWidthProperty().bind(scene.widthProperty());
		insertView.fitHeightProperty().bind(scene.heightProperty());
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
		stage.setMinWidth(800);
		stage.setMinHeight(600);

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

		//文字表示用のタイマーを作成、50ミリ秒ごとに処理
		timeline = new Timeline(
				new KeyFrame(Duration.millis(50), e -> {
					//今再生されている会話テキストのリスト番号を取得
					Dialogue d = dialogues.get(messageIndex);
					//まだ文字が残っているかどうかを判断(文字が残っている間は処理を実行)
					if (charIndex < d.message.length()) {
						//文字カウントを増やす
						charIndex++;
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
							//登場するまで画像非表示
							if (messageIndex < 10) {
								takuView.setVisible(false);
							} else {
								takuView.setVisible(true);
							}
							//あにきとなりなりの画像を非表示
							anikiView.setVisible(false);
							nariView.setVisible(false);
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

				//差し込み絵の処理
				if (messageIndex == 4) {
					insertView.setVisible(true);
				}

				if (messageIndex == 5) {
					insertView.setVisible(false);
					//BGMの再生
					Bgm.stopBGM();
					Bgm.playBGM("/music/storybgm.mp3");
				}

				//ダメージ受けたときの横揺れ
				if (messageIndex == 18) {

					syujinkouView.setOpacity(0.5);

					// bind一旦外す
					syujinkouView.translateXProperty().unbind();

					TranslateTransition shake = new TranslateTransition(Duration.millis(100), syujinkouView);
					shake.setByX(20);
					shake.setCycleCount(6);
					shake.setAutoReverse(true);
					shake.play();

					Timeline recover = new Timeline(
							new KeyFrame(Duration.millis(300), e2 -> {
								syujinkouView.setOpacity(1.0);
								// bind戻す
								syujinkouView.setTranslateX(-250);

							}));
					recover.play();
				}

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
				//設定した音をならす

				if (d.sound != null && d.sound != jumpSound) {
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
					cleanup(scene);
					base.getChildren().clear();
					//次の画面へ
					control.GameController.switchToGame2(stage);
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
