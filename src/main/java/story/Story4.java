package story;

import java.util.Arrays;
import java.util.List;

import control.GameController;
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

	private Timeline blink;
	private Timeline arrowMove;
	private AudioClip jumpSound;
	private AudioClip aSound;
	private TranslateTransition fall;
	private boolean isEndingStarted = false;
	private int messageIndex = 0;
	private int charIndex = 0;
	private boolean isTyping = false;
	private Text text;
	private Timeline timeline;
	private Timeline jumpAniki;
	private Timeline jumpsyujinkou;
	private Timeline jumpnari;
	private Timeline jumptaku;
	private Stage stage;

	// 💡 クリックによる画像切り替えで何度も再利用するため、あらかじめフィールドとして保持
	private Image anikiNormalImage;
	private Image anikiAngryImage;

	public Story4() {
		// 引数なしコンストラクタ（JProに必須）
	}

	// 💡【JPro対応】他の画面からこのストーリー画面に安全に遷移するための静的メソッド
	public static void createAndStart(Stage currentStage) {
		if (currentStage == null)
			return;

		Story4 instance = new Story4();
		instance.stage = currentStage;

		Scene newScene = instance.story4(currentStage);
		currentStage.setScene(newScene);
		currentStage.setTitle("story4");
		currentStage.centerOnScreen();
		currentStage.show();
	}

	private void cleanup(Scene scene, StackPane base) {
		// 文字タイピング

		if (timeline != null) {
			timeline.stop();
			timeline = null;
		}
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
		if (blink != null) {
			blink.stop();
			blink = null;
		}
		if (arrowMove != null) {
			arrowMove.stop();
			arrowMove = null;
		}
		if (fall != null) {
			fall.stop();
			fall = null;
		}
		if (jumpSound != null) {
			jumpSound.stop();
			jumpSound = null;
		}
		if (aSound != null) {
			aSound.stop();
			aSound = null;
		}

		Bgm.stopBGM();

		if (scene != null) {
			scene.setOnMouseClicked(null);
			scene.setOnKeyPressed(null);
		}
		if (base != null) {
			base.getChildren().clear();
		}
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("story4");
		stage.setScene(story4(stage));
		stage.centerOnScreen();
		stage.show();
	}

	private void startTyping() {
		charIndex = 0;
		if (text != null) {
			text.setText("");
		}
		isTyping = true;
		if (timeline != null) {
			timeline.playFromStart();
		}
	}

	// 💡 互換性のための引数なしメソッド
	public Scene story4() {
		return story4(this.stage);
	}

	// 💡 メインロジック（JPro用にリサイズ・バインディング設計を強化）
	public Scene story4(Stage currentStage) {
		if (currentStage != null) {
			this.stage = currentStage;
		}

		// BGM再生
		Bgm.stopBGM();
		Bgm.playBGM("/music/endhing.mp3");

		// ⭕【JPro対応】各効果音を個別に try-catch 保護し安全化
		try {
			var res = getClass().getResource("/music/jump06.mp3");
			if (res != null)
				jumpSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) {
			System.err.println("jumpSoundの読み込みに失敗");
		}
		if (jumpSound != null)
			jumpSound.setVolume(0.2);

		try {
			var res = getClass().getResource("/music/damage2.mp3");
			if (res != null)
				aSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) {
			System.err.println("aSoundの読み込みに失敗");
		}
		if (aSound != null)
			aSound.setVolume(0.4);

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

		text = new Text("");
		text.setStyle("-fx-font-family: monospace;");
		text.setTranslateY(-5);

		Rectangle box = new Rectangle();
		box.setFill(Color.rgb(0, 0, 0, 0.8)); // 視認性向上のため少し暗く
		box.setStroke(Color.WHITE);
		box.setStrokeWidth(3);

		Text nextMark = new Text("▼");
		nextMark.setFill(Color.WHITE);
		nextMark.setVisible(false);
		nextMark.setTranslateY(40);

		blink = StoryUtils.createBlink(nextMark);
		arrowMove = StoryUtils.createArrowMove(nextMark);

		Text nameText = new Text();
		if (!dialogues.isEmpty()) {
			nameText.setText(dialogues.get(messageIndex).speaker);
		}

		VBox bubble = new VBox();
		bubble.setPadding(new Insets(15));
		bubble.setSpacing(5);
		bubble.setAlignment(Pos.CENTER_LEFT);

		StackPane arrowBox = new StackPane(nextMark);
		arrowBox.setPadding(new Insets(0, 0, 15, 0));
		bubble.getChildren().addAll(nameText, text, arrowBox);

		// ⭕【JPro対応】すべての画像読み込みを getResourceAsStream と try-catch へ修正
		ImageView bgView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/shatyoroom.jpg");
			if (stream != null)
				bgView.setImage(new Image(stream));
		} catch (Exception e) {
			System.err.println("背景画像の読み込みに失敗");
		}
		bgView.setPreserveRatio(false);

		// 💡 あにき画像のバリエーションを事前にストリーム経由でロード
		try {
			var stream1 = getClass().getResourceAsStream("/picture/aniki-udekumi.png");
			if (stream1 != null)
				anikiNormalImage = new Image(stream1);
			var stream2 = getClass().getResourceAsStream("/picture/aniki2.png");
			if (stream2 != null)
				anikiAngryImage = new Image(stream2);
		} catch (Exception e) {
			System.err.println("あにき用各種画像の読み込みに失敗");
		}

		ImageView anikiView = new ImageView();
		if (anikiNormalImage != null)
			anikiView.setImage(anikiNormalImage);
		anikiView.setPreserveRatio(true);

		ImageView syujinkouView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/syujinkou(hello).png");
			if (stream != null)
				syujinkouView.setImage(new Image(stream));
		} catch (Exception e) {
			System.err.println("主人公画像の読み込みに失敗");
		}
		syujinkouView.setPreserveRatio(true);

		ImageView nariView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/nari.png");
			if (stream != null)
				nariView.setImage(new Image(stream));
		} catch (Exception e) {
			System.err.println("なりなり画像の読み込みに失敗");
		}
		nariView.setPreserveRatio(true);

		ImageView takuView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/taku2.png");
			if (stream != null)
				takuView.setImage(new Image(stream));
		} catch (Exception e) {
			System.err.println("わだたく画像の読み込みに失敗");
		}
		takuView.setPreserveRatio(true);

		nariView.setVisible(false);
		anikiView.setVisible(true);
		takuView.setVisible(false);

		StackPane messageBox = new StackPane();
		messageBox.getChildren().addAll(box, bubble);

		BorderPane root = new BorderPane();
		root.setBottom(messageBox);
		root.setStyle("-fx-background-color: transparent;");

		ImageView menuView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/menu.jpeg");
			if (stream != null)
				menuView.setImage(new Image(stream));
		} catch (Exception e) {
			System.err.println("メニュー画像の読み込みに失敗");
		}
		menuView.setFitWidth(40);
		menuView.setFitHeight(40);

		Button menuBtn = new Button("");
		menuBtn.setGraphic(menuView);
		menuBtn.setStyle("-fx-background-color: transparent;");
		StackPane.setAlignment(menuBtn, Pos.TOP_LEFT);

		StackPane base = new StackPane();
		// 💡 各人物画像の基準配置をCENTER（中央下部）に変更し、リサイズによる位置ズレを防止
		base.getChildren().addAll(bgView, syujinkouView, anikiView, nariView, takuView, root);
		StackPane.setAlignment(syujinkouView, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(anikiView, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(nariView, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(takuView, Pos.BOTTOM_CENTER);

		Scene scene = new Scene(base, 1000, 800);

		// メニューオーバーレイ
		StackPane menuOverlay = new StackPane();
		menuOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.5);");
		menuOverlay.setVisible(false);

		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);
		menuBox.setMaxWidth(300);
		menuBox.setMaxHeight(250);
		menuBox.setStyle(
				"-fx-background-color: rgba(40,40,50,0.95); -fx-background-radius: 20; -fx-padding: 25; -fx-border-radius: 20; -fx-border-color: white; -fx-border-width: 2;");

		Button resume = new Button("再開");
		Button titleBtn = new Button("タイトルへ");
		resume.getStyleClass().add("game-button2");
		titleBtn.getStyleClass().add("game-button2");
		resume.setPrefWidth(180);
		titleBtn.setPrefWidth(180);

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
			GameController.switchStart(stage);
		});

		menuBox.getChildren().addAll(resume, titleBtn);
		menuOverlay.getChildren().add(menuBox);
		base.getChildren().addAll(menuBtn, menuOverlay);

		// 💡 【JPro対応】レスポンシブ用の動的バインディング設計
		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());

		// キャラクターサイズとX軸オフセットの最適化
		// 人物画像(あにき)をウィンドウサイズに合わせる(右に表示)
		anikiView.fitWidthProperty().bind(scene.widthProperty().multiply(0.7));
		anikiView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
		anikiView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
		// 人物画像(なりなり)をウィンドウサイズに合わせる(右に表示)
		nariView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
		nariView.fitHeightProperty().bind(scene.heightProperty().multiply(0.9));
		nariView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
		// 人物画像(たく)をウィンドウサイズに合わせる(右に表示)
		takuView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
		takuView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
		takuView.translateXProperty().bind(scene.widthProperty().multiply(0.25));
		// 人物画像(仙石)をウィンドウサイズに合わせる(左に表示)(下に調整)
		syujinkouView.fitWidthProperty().bind(scene.widthProperty().multiply(0.3));
		syujinkouView.fitHeightProperty().bind(scene.heightProperty().multiply(0.9));
		syujinkouView.translateXProperty().bind(scene.widthProperty().multiply(-0.25));

		// メッセージウィンドウ全体のパディング・サイズバインディング
		BorderPane.setMargin(messageBox, new Insets(0, 0, 20, 0));
		box.widthProperty().bind(scene.widthProperty().multiply(0.92));
		box.heightProperty().bind(scene.heightProperty().multiply(0.22));
		text.wrappingWidthProperty().bind(box.widthProperty().multiply(0.90));
		bubble.prefWidthProperty().bind(box.widthProperty());
		bubble.maxWidthProperty().bind(box.widthProperty());

		// フォントサイズのレスポンシブ化
		text.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-font-family: monospace;",
				scene.widthProperty().multiply(0.026)));
		nextMark.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: white;", scene.widthProperty().multiply(0.02)));
		nameText.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: #ffcc00; -fx-font-weight: bold;",
						scene.widthProperty().multiply(0.028)));

		StackPane.setMargin(menuBtn, new Insets(20));

		// キーイベント設定
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				menuOverlay.setVisible(true);
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
			if (timeline != null)
				timeline.pause();
			if (blink != null)
				blink.pause();
			if (arrowMove != null)
				arrowMove.pause();
		});

		fall = new TranslateTransition(Duration.seconds(1), takuView);
		fall.setToY(100);

		// 文字タイピングタイマー
		timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
			Dialogue d = dialogues.get(messageIndex);
			if (charIndex < d.message.length()) {
				charIndex++;
				if (messageIndex == 0 && charIndex == 1) {
					if (fall != null)
						fall.playFromStart();
				}
				String speaker = d.speaker;
				nameText.setText(speaker);
				text.setFill(d.textColor);

				if (speaker.equals("あにき")) {
					anikiView.setVisible(true);
					nariView.setVisible(false);
					takuView.setVisible(false);
				} else if (speaker.equals("仙石さん")) {
					// 仙石さんは常時表示のままでOK
				} else if (speaker.equals("なりなり")) {
					anikiView.setVisible(false);
					nariView.setVisible(true);
					takuView.setVisible(false);
				} else if (speaker.equals("わだたく")) {
					anikiView.setVisible(false);
					nariView.setVisible(false);
					takuView.setVisible(true);
				}

				text.setText(d.message.substring(0, charIndex));
			} else {
				isTyping = false;
				timeline.stop();
				nextMark.setVisible(true);
				if (blink != null)
					blink.play();
				if (arrowMove != null)
					arrowMove.play();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// 💡 クリック（タップ）でのシナリオ進行制御
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (menuOverlay.isVisible()) {
				return;
			}

			if (isTyping) {
				timeline.stop();
				Dialogue d = dialogues.get(messageIndex);
				text.setText(d.message);
				isTyping = false;
				nextMark.setVisible(true);
				if (blink != null)
					blink.play();
				if (arrowMove != null)
					arrowMove.play();
				return;
			}

			if (messageIndex < dialogues.size() - 1) {
				messageIndex++;
				startTyping();
				nextMark.setVisible(false);
				if (blink != null)
					blink.stop();
				if (arrowMove != null)
					arrowMove.stop();

				Dialogue d = dialogues.get(messageIndex);
				String speaker = d.speaker;

				// ⭕【JPro対応】毎回new Imageせず、事前に安全にロードしたインスタンスを切り替える
				if (messageIndex >= 2 && messageIndex <= 10) {
					if (anikiAngryImage != null)
						anikiView.setImage(anikiAngryImage);
				} else {
					if (anikiNormalImage != null)
						anikiView.setImage(anikiNormalImage);
				}

				if (d.sound != null && d.sound != jumpSound) {
					d.sound.stop();
					d.sound.play();
				}

				if (d.sound == jumpSound) {
					if (speaker.equals("あにき")) {
						jumpAniki = StoryUtils.createJumpAnimation(anikiView, d.sound);
						if (jumpAniki != null)
							jumpAniki.playFromStart();
					} else if (speaker.equals("仙石さん")) {
						jumpsyujinkou = StoryUtils.createJumpAnimation(syujinkouView, d.sound);
						if (jumpsyujinkou != null)
							jumpsyujinkou.playFromStart();
					} else if (speaker.equals("なりなり")) {
						jumpnari = StoryUtils.createJumpAnimation(nariView, d.sound);
						if (jumpnari != null)
							jumpnari.playFromStart();
					} else if (speaker.equals("わだたく")) {
						jumptaku = StoryUtils.createJumpAnimation(takuView, d.sound);
						if (jumptaku != null)
							jumptaku.playFromStart();
					}
				}
			} else {
				if (isEndingStarted)
					return;
				isEndingStarted = true;

				nextMark.setVisible(false);

				Rectangle fadeRect = new Rectangle(1000, 800, Color.BLACK);
				fadeRect.setOpacity(0);
				base.getChildren().add(fadeRect);

				FadeTransition fade = new FadeTransition(Duration.seconds(1.5), fadeRect);
				fade.setFromValue(0);
				fade.setToValue(1);

				fadeRect.widthProperty().bind(scene.widthProperty());
				fadeRect.heightProperty().bind(scene.heightProperty());

				fade.setOnFinished(ev -> {
					cleanup(scene, base);
					GameController.switchStoryClear(stage);
				});

				fade.play();
			}
		});

		if (!dialogues.isEmpty()) {
			Dialogue d = dialogues.get(messageIndex);
			if (d.sound != null && d.sound != jumpSound) {
				d.sound.stop();
				d.sound.play();
			}
		}

		// ⭕【JPro対応】CSSファイルの読み込みに安全なURL存在チェックを追加
		var cssUrl = getClass().getResource("/css/style.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}

		startTyping();

		if (this.stage != null) {
			this.stage.setMinWidth(1000);
			this.stage.setMinHeight(800);
			this.stage.setMaxWidth(1920);
			this.stage.setMaxHeight(1080);
		}

		return scene;
	}
}