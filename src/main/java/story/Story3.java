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

public class Story3 extends Application {

	private Timeline blink;
	private Timeline arrowMove;
	private AudioClip jumpSound;
	private AudioClip downSound;
	private AudioClip feelSound;
	private AudioClip endSound;
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

	public Story3() {
		// 引数なしコンストラクタ（JProの動的生成に必須）
	}

	// 💡 【JPro対応】外部から安全にStageを引き継いでStory3を開始する静的メソッド
	public static void createAndStart(Stage currentStage) {
		if (currentStage == null)
			return;

		Story3 instance = new Story3();
		instance.stage = currentStage;

		// Stageが確実に格納された状態でSceneを構築
		Scene newScene = instance.story3();

		currentStage.setScene(newScene);
		currentStage.setTitle("story3");
		currentStage.centerOnScreen();
		currentStage.show();
	}

	private void cleanup(Scene scene, StackPane base) {
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
		if (downSound != null) {
			downSound.stop();
			downSound = null;
		}
		if (feelSound != null) {
			feelSound.stop();
			feelSound = null;
		}
		if (endSound != null) {
			endSound.stop();
			endSound = null;
		}
		Bgm.stopBGM();
		if (scene != null) {
			scene.setOnMouseClicked(null);
		}
		if (base != null) {
			base.getChildren().clear();
		}
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle("story3");
		stage.setScene(story3());
		stage.centerOnScreen();
		stage.show();
	}

	private void startTyping() {
		charIndex = 0;
		text.setText("");
		isTyping = true;
		timeline.playFromStart();
	}

	public Scene story3() {
		Bgm.stopBGM();
		Bgm.playBGM("/music/takubgm.mp3");

		// ⭕【JPro対応】各効果音を個別に try-catch 保護し安全化
		try {
			var res = getClass().getResource("/music/jump06.mp3");
			if (res != null) jumpSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("jumpSoundの読み込みに失敗"); }
		if (jumpSound != null) jumpSound.setVolume(0.2);

		try {
			var res = getClass().getResource("/music/down.mp3");
			if (res != null) downSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("downSoundの読み込みに失敗"); }
		if (downSound != null) downSound.setVolume(0.3);

		try {
			var res = getClass().getResource("/music/feel.mp3");
			if (res != null) feelSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("feelSoundの読み込みに失敗"); }
		if (feelSound != null) feelSound.setVolume(0.5);

		try {
			var res = getClass().getResource("/music/end.mp3");
			if (res != null) endSound = new AudioClip(res.toExternalForm());
		} catch (Exception e) { System.err.println("endSoundの読み込みに失敗"); }
		if (endSound != null) endSound.setVolume(0.4);

		List<Dialogue> dialogues = Arrays.asList(
				new Dialogue("わだたく", "……あれ……？もう、あそべない……？", downSound, Color.RED),
				new Dialogue("仙石さん", "終わったか……", null, Color.WHITE),
				new Dialogue("あにき", "……ペットがやられたな。まあいい。", jumpSound, Color.RED),
				new Dialogue("あにき", "代わりはいくらでもいる。", null, Color.RED),
				new Dialogue("仙石さん", "……ふざけるな。", feelSound, Color.WHITE),
				new Dialogue("仙石さん", "社員を、道具みたいに扱いやがって……！", null, Color.WHITE),
				new Dialogue("仙石さん", "会社は、お前の遊び場じゃない！", jumpSound, Color.WHITE),
				new Dialogue("あにき", "会社？", null, Color.RED),
				new Dialogue("あにき", "ここはもう俺の支配下だ。", jumpSound, Color.RED),
				new Dialogue("あにき", "来るか、先輩社員サン。", jumpSound, Color.RED),
				new Dialogue("仙石さん", "取り戻す。ここは俺たちの会社だ！", jumpSound, Color.WHITE),
				new Dialogue("あにき", "いいだろう。", null, Color.RED),
				new Dialogue("あにき", "絶望を教えてやる！！", endSound, Color.RED));

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

		// ⭕【JPro対応】すべての画像読み込みを getResourceAsStream と try-catch へ修正
		ImageView bgView = new ImageView();
		try {
			var stream = getClass().getResourceAsStream("/picture/shatyoroom.jpg");
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
			var stream = getClass().getResourceAsStream("/picture/taku2.png");
			if (stream != null) takuView.setImage(new Image(stream));
		} catch (Exception e) { System.err.println("わだたく画像の読み込みに失敗"); }
		takuView.setPreserveRatio(true);

		nariView.setVisible(false);
		anikiView.setVisible(true);
		takuView.setVisible(false);

		fall = new TranslateTransition(Duration.millis(800), takuView);
		fall.setByY(200);

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
		base.getChildren().addAll(bgView, anikiView, syujinkouView, nariView, takuView, root);
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
			cleanup(scene, base);
			GameController.switchStart(stage);
		});

		menuBox.getChildren().addAll(resume, titleBtn);
		menuOverlay.getChildren().add(menuBox);
		base.getChildren().addAll(menuBtn, menuOverlay);

		bgView.fitWidthProperty().bind(scene.widthProperty());
		bgView.fitHeightProperty().bind(scene.heightProperty());

		anikiView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
		anikiView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
		anikiView.translateXProperty().bind(scene.widthProperty().multiply(0.25));

		nariView.fitWidthProperty().bind(scene.widthProperty().multiply(0.5));
		nariView.fitHeightProperty().bind(scene.heightProperty().multiply(0.9));
		nariView.translateXProperty().bind(scene.widthProperty().multiply(0.25));

		takuView.fitWidthProperty().bind(scene.widthProperty().multiply(0.8));
		takuView.fitHeightProperty().bind(scene.heightProperty().multiply(1.2));
		takuView.translateXProperty().bind(scene.widthProperty().multiply(0.25));

		syujinkouView.fitWidthProperty().bind(scene.widthProperty().multiply(0.37));
		syujinkouView.fitHeightProperty().bind(scene.heightProperty().multiply(1.4));
		syujinkouView.translateXProperty().bind(scene.widthProperty().multiply(-0.25));

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
						if (messageIndex == 0) {
							if (fall != null) fall.play();
						}
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
							anikiView.setVisible(false);
							nariView.setVisible(false);
							takuView.setVisible(true);
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

				if (messageIndex == 12) {
					Timeline shakeSlot = new Timeline(
							new KeyFrame(Duration.millis(0), e2 -> {
								base.setTranslateX(Math.random() * 30 - 15);
								base.setTranslateY(Math.random() * 20 - 10);
							}),
							new KeyFrame(Duration.millis(40)));
					shakeSlot.setCycleCount(15);
					shakeSlot.setOnFinished(e2 -> {
						base.setTranslateX(0);
						base.setTranslateY(0);
					});
					shakeSlot.play();
				}

				startTyping();
				nextMark.setVisible(false);
				if (blink != null) blink.stop();
				if (arrowMove != null) arrowMove.stop();

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
					cleanup(scene, base);
					GameController.switchToGame3(stage);
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