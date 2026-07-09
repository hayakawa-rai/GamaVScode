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

public class Story2 extends Application {

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

	public Story2() {
		// 引数なしコンストラクタ（JProの動的生成に必要）
	}

	// 💡 【JPro対応】外部（GameController）から安全にStageを引き継いでStory2を開始する静的メソッド
	public static void createAndStart(Stage currentStage) {
		if (currentStage == null)
			return;

		Story2 instance = new Story2();
		instance.stage = currentStage;

		// Stageが確実にセットされた状態でSceneを生成する
		Scene newScene = instance.story2();

		currentStage.setScene(newScene);
		currentStage.setTitle("story2");
		currentStage.centerOnScreen();
		currentStage.show();
	}

	private void cleanup(Scene scene) {
		// テキストタイピング等の各タイムラインを停止
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
		if (jumpSound != null) {
			jumpSound.stop();
			jumpSound = null;
		}
		if (cuteSound != null) {
			cuteSound.stop();
			cuteSound = null;
		}
		if (appearSound != null) {
			appearSound.stop();
			appearSound = null;
		}
		if (mysteriousSound != null) {
			mysteriousSound.stop();
			mysteriousSound = null;
		}
		if (shineSound != null) {
			shineSound.stop();
			shineSound = null;
		}
		if (damageSound != null) {
			damageSound.stop();
			damageSound = null;
		}
		if (aSound != null) {
			aSound.stop();
			aSound = null;
		}
		if (atacSound != null) {
			atacSound.stop();
			atacSound = null;
		}
		Bgm.stopBGM();
		if (scene != null) {
			scene.setOnMouseClicked(null);
		}
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("story2");
		stage.setScene(story2());
		stage.centerOnScreen();
		stage.show();
	}

	private void startTyping() {
		charIndex = 0;
		text.setText("");
		isTyping = true;
		timeline.playFromStart();
	}

	public Scene story2() {
		Bgm.stopBGM();
		Bgm.playBGM("/music/naribgm.mp3");

		// ⭕【JPro対応】大量の効果音読み込みをすべて個別に try-catch 保護し安全化
		try {
			var res = getClass().getResource("/music/jump06.mp3");
			if (res != null) jumpSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("jumpSoundの読み込みに失敗"); }
		if (jumpSound != null) jumpSound.setVolume(0.2);

		try {
			var res = getClass().getResource("/music/footsteps.mp3");
			if (res != null) cuteSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("cuteSoundの読み込みに失敗"); }
		if (cuteSound != null) cuteSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/appearance.mp3");
			if (res != null) appearSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("appearSoundの読み込みに失敗"); }
		if (appearSound != null) appearSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/nari.mp3");
			if (res != null) mysteriousSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("mysteriousSoundの読み込みに失敗"); }
		if (mysteriousSound != null) mysteriousSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/shine.mp3");
			if (res != null) shineSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("shineSoundの読み込みに失敗"); }
		if (shineSound != null) shineSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/damage.mp3");
			if (res != null) damageSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("damageSoundの読み込みに失敗"); }
		if (damageSound != null) damageSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/damage2.mp3");
			if (res != null) aSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("aSoundの読み込みに失敗"); }
		if (aSound != null) aSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/atac.mp3");
			if (res != null) atacSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("atacSoundの読み込みに失敗"); }
		if (atacSound != null) atacSound.setVolume(0.3);

		List<Dialogue> dialogues = Arrays.asList(
				new Dialogue("なりなり", "あ、あれっ…！？", mysteriousSound, Color.ORANGE),
				new Dialogue("仙石さん", "弱いな！？", jumpSound, Color.WHITE),
				new Dialogue("なりなり", "ま、まだだ…まだ終わってない…！", null, Color.ORANGE),
				new Dialogue("仙石さん", "もう終わってる。", mysteriousSound, Color.WHITE),
				new Dialogue("なりなり", "ぐああaaaa転！！", damageSound, Color.ORANGE),
				new Dialogue("あにき", "クク…やはり雑魚か。", null, Color.RED),
				new Dialogue("仙石さん", "おい、完全に遊ばれてるぞ。", jumpSound, Color.WHITE),
				new Dialogue("あにき", "まあいい。次は特別だ。", null, Color.RED),
				new Dialogue("あにき", "来い、わだたく。", jumpSound, Color.RED),
				new Dialogue("わだたく", "……とてとて…", cuteSound, Color.PINK),
				new Dialogue("わだたく", "……ぴょこっ", appearSound, Color.PINK),
				new Dialogue("仙石さん", "……ん？", mysteriousSound, Color.WHITE),
				new Dialogue("仙石さん", "なんだこのかわいい生き物は。", jumpSound, Color.WHITE),
				new Dialogue("わだたく", "わだ〜たく〜…♪", shineSound, Color.PINK),
				new Dialogue("わだたく", "よろしくね♪", shineSound, Color.PINK),
				new Dialogue("仙石さん", "……弱そうだな。", mysteriousSound, Color.WHITE),
				new Dialogue("あにき", "見た目で判断するな。", null, Color.RED),
				new Dialogue("わだたく", "えいっ", atacSound, Color.PINK),
				new Dialogue("仙石さん", "ぐっ！？", aSound, Color.WHITE),
				new Dialogue("仙石さん", "な、何だ今の一撃は…！", null, Color.WHITE),
				new Dialogue("わだたく", "あそぼ？♪", shineSound, Color.RED),
				new Dialogue("わだたく", "いっぱいあそぼ〜♪", shineSound, Color.RED),
				new Dialogue("あにき", "そいつは俺のペットでな。", null, Color.RED),
				new Dialogue("あにき", "強そうに見えないが、遊ばれたら最後だ。", jumpSound, Color.RED));

		text = new Text("");
		text.setStyle("-fx-font-family: monospace;");
		text.setTranslateY(-5);

		Rectangle box = new Rectangle();
		text.setWrappingWidth(850);
		box.setFill(Color.rgb(0, 0, 0, 0.7));
		box.setStroke(Color.WHITE);
		box.setStrokeWidth(3);
		box.setArcWidth(0);
		box.setArcHeight(0);

		Text nextMark = new Text("▼");
		nextMark.setFill(Color.WHITE);
		nextMark.setVisible(false);
		nextMark.setTranslateY(40);
		blink = StoryUtils.createBlink(nextMark);
		arrowMove = StoryUtils.createArrowMove(nextMark);

		// 💡 インデックスの安全チェック
		Text nameText = new Text();
		if (!dialogues.isEmpty()) {
			nameText.setText(dialogues.get(messageIndex).speaker);
		}

		VBox bubble = new VBox();
		bubble.setPadding(new Insets(10));
		bubble.setSpacing(5);
		bubble.setPrefWidth(850);
		bubble.setMaxWidth(850);
		bubble.setAlignment(Pos.CENTER_LEFT);

		StackPane.setAlignment(bubble, Pos.BOTTOM_CENTER);
		StackPane arrowBox = new StackPane(nextMark);
		arrowBox.setPadding(new Insets(0, 0, 15, 0));
		bubble.getChildren().addAll(nameText, text, arrowBox);

		// ⭕【JPro対応】画像読み込みをすべて getResourceAsStream と try-catch へ修正
		ImageView bgView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/companyroom.jpg");
			if (stream != null) bgView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("背景画像の読み込みに失敗"); }
		bgView.setPreserveRatio(false);

		ImageView anikiView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/aniki-udekumi.png");
			if (stream != null) anikiView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("あにき画像の読み込みに失敗"); }
		anikiView.setPreserveRatio(true);

		ImageView syujinkouView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/syujinkou(hello).png");
			if (stream != null) syujinkouView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("主人公画像の読み込みに失敗"); }
		syujinkouView.setPreserveRatio(true);

		ImageView nariView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/nari.png");
			if (stream != null) nariView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("なりなり画像の読み込みに失敗"); }
		nariView.setPreserveRatio(true);

		ImageView takuView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/taku.png");
			if (stream != null) takuView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("わだたく画像の読み込みに失敗"); }
		takuView.setPreserveRatio(true);

		nariView.setVisible(false);
		anikiView.setVisible(true);
		takuView.setVisible(false);

		ImageView insertView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/insert.png");
			if (stream != null) insertView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("インサート画像の読み込みに失敗"); }
		insertView.setPreserveRatio(false);
		insertView.setVisible(false);

		StackPane messageBox = new StackPane();
		messageBox.getChildren().addAll(box, bubble);

		BorderPane root = new BorderPane();
		root.setBottom(messageBox);
		root.setStyle("-fx-background-color: transparent;");
		BorderPane.setMargin(messageBox, new Insets(0, 0, 30, 0));

		ImageView menuView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/menu.jpeg");
			if (stream != null) menuView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("メニュー画像の読み込みに失敗"); }
		menuView.setFitWidth(40);
		menuView.setFitHeight(40);

		Button menuBtn = new Button("");
		menuBtn.setGraphic(menuView);
		menuBtn.setStyle("-fx-background-color: transparent;");
		StackPane.setAlignment(menuBtn, Pos.TOP_LEFT);
		StackPane.setMargin(menuBtn, new Insets(30));

		StackPane base = new StackPane();
		base.getChildren().addAll(bgView, syujinkouView, anikiView, nariView, takuView, root, insertView);
		Scene scene = new Scene(base, 1000, 800);
		scene.setOnMouseClicked(e -> scene.getRoot().requestFocus());

		StackPane menuOverlay = new StackPane();
		menuOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.3);");
		menuOverlay.setVisible(false);
		menuOverlay.setPickOnBounds(true);

		VBox menuBox = new VBox(20);
		menuBox.setAlignment(Pos.CENTER);
		menuBox.setMaxWidth(300);
		menuBox.setMaxHeight(250);
		menuBox.setStyle(
				"-fx-background-color: rgba(40,40,50,0.95);" +
						"-fx-background-radius: 20;" +
						"-fx-padding: 25;" +
						"-fx-border-radius: 20;" +
						"-fx-border-color: white;" +
						"-fx-border-width: 2;");

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
			cleanup(scene);
			GameController.switchStart(stage);
		});

		menuBox.getChildren().addAll(resume, titleBtn);
		menuOverlay.getChildren().add(menuBox);
		base.getChildren().addAll(menuBtn, menuOverlay);
		// 背景画像をウィンドウサイズに合わせる
		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());
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

		insertView.fitWidthProperty().bind(scene.widthProperty());
		insertView.fitHeightProperty().bind(scene.heightProperty());

		box.widthProperty().bind(scene.widthProperty().multiply(0.9));
		box.heightProperty().bind(scene.heightProperty().multiply(0.18));

		text.wrappingWidthProperty().bind(box.widthProperty().multiply(0.95));
		bubble.prefWidthProperty().bind(box.widthProperty());
		bubble.maxWidthProperty().bind(box.widthProperty());

		text.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-font-family: monospace;",
				scene.widthProperty().multiply(0.03)));
		nextMark.styleProperty()
				.bind(Bindings.format("-fx-font-size: %.0fpx; -fx-fill: white; -fx-font-family: monospace;",
						scene.widthProperty().multiply(0.02)));
		nameText.styleProperty().bind(
				Bindings.format("-fx-font-size: %.0fpx; -fx-fill: lightgray;", scene.widthProperty().multiply(0.025)));

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

		timeline = new Timeline(
				new KeyFrame(Duration.millis(50), e -> {
					Dialogue d = dialogues.get(messageIndex);
					if (charIndex < d.message.length()) {
						charIndex++;
						String speaker = d.speaker;
						nameText.setText(speaker);
						text.setFill(d.textColor);
						if (speaker.equals("あにき")) {
							anikiView.setVisible(true);
							nariView.setVisible(false);
							takuView.setVisible(false);
						} else if (speaker.equals("仙石さん")) {
						} else if (speaker.equals("なりなり")) {
							anikiView.setVisible(false);
							nariView.setVisible(true);
							takuView.setVisible(false);
						} else if (speaker.equals("わだたく")) {
							if (messageIndex < 10) {
								takuView.setVisible(false);
							} else {
								takuView.setVisible(true);
							}
							anikiView.setVisible(false);
							nariView.setVisible(false);
						}
						text.setText(d.message.substring(0, charIndex));
					} else {
						isTyping = false;
						timeline.stop();
						nextMark.setVisible(true);
						blink.play();
						arrowMove.play();
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (menuOverlay.isVisible()) {
				if (e.getTarget() == menuBtn)
					return;
				e.consume();
				return;
			}

			if (isTyping) {
				timeline.stop();
				Dialogue d = dialogues.get(messageIndex);
				text.setText(d.message);
				isTyping = false;
				nextMark.setVisible(true);
				blink.play();
				arrowMove.play();
				return;
			}

			if (messageIndex < dialogues.size() - 1) {
				messageIndex++;
				if (messageIndex == 4) {
					insertView.setVisible(true);
				}
				if (messageIndex == 5) {
					insertView.setVisible(false);
					Bgm.stopBGM();
					Bgm.playBGM("/music/storybgm.mp3");
				}

				if (messageIndex == 18) {
					syujinkouView.setOpacity(0.5);
					syujinkouView.translateXProperty().unbind();
					TranslateTransition shake = new TranslateTransition(Duration.millis(100), syujinkouView);
					shake.setByX(20);
					shake.setCycleCount(6);
					shake.setAutoReverse(true);
					shake.play();

					Timeline recover = new Timeline(
							new KeyFrame(Duration.millis(300), e2 -> {
								syujinkouView.setOpacity(1.0);
								if (this.stage != null) {
									syujinkouView.translateXProperty().bind(scene.widthProperty().multiply(-0.25));
								}
							}));
					recover.play();
				}

				startTyping();
				nextMark.setVisible(false);
				blink.stop();
				arrowMove.stop();

				Dialogue d = dialogues.get(messageIndex);
				String speaker = d.speaker;

				if (d.sound != null && d.sound != jumpSound) {
					d.sound.stop();
					d.sound.play();
				}
				if (d.sound == jumpSound) {
					if (speaker.equals("あにき")) {
						jumpAniki = StoryUtils.createJumpAnimation(anikiView, d.sound);
						if (jumpAniki != null) jumpAniki.playFromStart();
					} else if (speaker.equals("仙石さん")) {
						jumpsyujinkou = StoryUtils.createJumpAnimation(syujinkouView, d.sound);
						if (jumpsyujinkou != null) jumpsyujinkou.playFromStart();
					} else if (speaker.equals("なりなり")) {
						jumpnari = StoryUtils.createJumpAnimation(nariView, d.sound);
						if (jumpnari != null) jumpnari.playFromStart();
					} else if (speaker.equals("わだたく")) {
						jumptaku = StoryUtils.createJumpAnimation(takuView, d.sound);
						if (jumptaku != null) jumptaku.playFromStart();
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
					cleanup(scene);
					base.getChildren().clear();
					GameController.switchToGame2(stage);
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

		if (stage != null) {
			stage.setMinWidth(1000);
			stage.setMinHeight(800);
			stage.setMaxWidth(1920);
			stage.setMaxHeight(1080);
		}
		return scene;
	}
}