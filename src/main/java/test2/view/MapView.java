package test2.view;

import Characters.BlueEnemy;
import Characters.Enemy;
import Characters.GreenEnemy;
import Characters.RedEnemy;
import Characters.Syujinkou;
import Characters.YellowEnemy;
import Items.Fruit;
import Items.Item;
import common.HighScoreManager;
import control.GameController;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import test2.model.MapData;

public class MapView {

	private final MapData model;

	// CSSの色を吸い取るための「見えないダミー部品」
	private final Region wallDummy = new Region();
	private final Region pacmanDummy = new Region();

	// ヘッダー
	private static final double INFO_HEIGHT = 40;
	
	// JPro対応: サーバー/クライアント間のラグに影響されない安定した点滅用カウンター
	private int blinkTick = 0;

	/**
	 * CSSからの色取得やモバイル操作の初期化は行わず、モデルの参照だけを保持する。
	 * @param model 描画対象のゲームデータ
	 */
	public MapView(MapData model) {
		this.model = model;
	}

	/**
	 * CSSの色をJavaFXの描画色として取り出すための「見えないダミー部品」をrootに追加し、
	 * Sceneが設定されたタイミングでモバイル用の十字キーコントローラーを適用する。
	 * @param model 描画対象のゲームデータ
	 * @param root  ダミー部品を追加する親コンテナ
	 */
	public MapView(MapData model, Pane root) {
		this.model = model;

		// ダミー部品にCSSのクラス名をセット
		wallDummy.getStyleClass().add("game-wall");
		pacmanDummy.getStyleClass().add("game-pacman");

		// 画面には表示させず、rootの子要素にする
		wallDummy.setVisible(false);
		pacmanDummy.setVisible(false);
		root.getChildren().addAll(wallDummy, pacmanDummy);

		root.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				GameController.applyMobileControls(newScene, this.model);
			}
		});
	}

	/**
	 * ステージ全体を画面サイズに合わせて拡大縮小・中央配置して描画するメインメソッド
	 * 
	 * 1. キャンバスをクリアし、上部の情報バー(INFO_HEIGHT)を黒で塗りつぶす 
	 * 2. CSSから壁色・パックマン色を取得する 
	 * 3. ステージ全体が画面に収まるようスケール・オフセットを計算する 
	 * 4. 変換行列を適用してステージ本体（壁・アイテム・プレイヤー・敵）を描画する 
	 * 5. スコア・残りライフ・区切り線などのUIを描画する 
	 * 6. 一時停止中であれば、画面を暗くして「PAUSE」の文字を表示する
	 *
	 * @param gc           描画先のGraphicsContext
	 * @param canvasWidth  キャンバスの現在の幅
	 * @param canvasHeight キャンバスの現在の高さ
	 */
	public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
		// 内部カウンターを進める（JProでの正確なアニメーション同期用）
		blinkTick++;

		// 1. まずはCanvasを一度綺麗に消す（透明にする）
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvasWidth, INFO_HEIGHT);

		Color wallColor = getColorFromCSS(wallDummy, Color.BLUE);
		Color pacmanColor = getColorFromCSS(pacmanDummy, Color.YELLOW);

		// 1. ステージ本来のサイズを計算
		int cols = model.getMap()[0].length;
		int rows = model.getMap().length;

		double stageWidth = cols * MapData.TILE_SIZE;
		double stageHeight = rows * MapData.TILE_SIZE;
		double scaleX = canvasWidth / stageWidth;
		double scaleY = canvasHeight / stageHeight;

		// 2. 全体を90%の大きさに縮小する
		double bufferRatio = 0.9;
		double scale = Math.min(scaleX, scaleY) * bufferRatio;

		// 3. 中央に配置するための余白（オフセット）を計算
		double offsetX = (canvasWidth - (stageWidth * scale)) / 2.0;
		double offsetY = ((canvasHeight - INFO_HEIGHT) - (stageHeight * scale)) / 2.0 + INFO_HEIGHT;

		// 5. グラフィックスの状態を保存
		gc.save();

		// 6. 変換行列を適用（中央へ移動させてから、拡大する）
		gc.translate(offsetX, offsetY);
		gc.scale(scale, scale);

		// パックマンが動く「ステージの四角い枠内だけ」を真っ黒に塗りつぶします
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, stageWidth, stageHeight);

		// 7. 実際の描画処理を呼び出す（💡 引数に現在の scale, offsetX, offsetY を追加して渡す）
		drawStageContent(gc, cols, rows, stageWidth, stageHeight, wallColor, scale, offsetX, offsetY);
		drawPacman(gc);

		// 敵の描画メソッド
		if (model.getEnemies() != null) {
			for (Enemy enemy : model.getEnemies()) {
				drawEnemyInstance(gc, enemy);
			}
		}

		// 8. グラフィックスの状態を元に戻す
		gc.restore();
		Syujinkou syujinkou = model.getsyujinkou();

		if (syujinkou != null) {
			// 後続の描画（スコアなど）が崩れないように、基準点をデフォルト（左、トップ）に戻しておく
			gc.setTextAlign(TextAlignment.LEFT);
			gc.setTextBaseline(VPos.TOP);

			// スコア // ハイスコア
			gc.setFont(Font.font("PixelMplus12", FontWeight.BOLD, 18));
			gc.setFill(Color.WHITE);
			gc.fillText("SCORE : " + syujinkou.getScore() + "  /  " + "HIGH SCORE : "
					+ HighScoreManager.loadHighScore(model.getStageNumber()), 20, 12);

			// ライフ
			gc.setFill(Color.RED);
			gc.fillText("❤".repeat(syujinkou.getHp()), canvasWidth - 100, 12);

			// 区切り線
			gc.setFont(Font.font("PixelMplus12", FontWeight.BOLD, 18));
			gc.setStroke(Color.DARKGRAY);
			gc.strokeLine(0, INFO_HEIGHT, canvasWidth, INFO_HEIGHT);
		}
	}

	/**
	 * ステージの中身（壁の輪郭とアイテム）を描画する内部メソッド。
	 * JProでの描画ズレバグを防ぐため、壁の描画時のみ座標を一時的に等倍へ復元する処理を含む。
	 *
	 * @param gc          描画先のGraphicsContext
	 * @param cols        マップの列数
	 * @param rows        マップの行数
	 * @param stageWidth  ステージ全体の幅（ピクセル）
	 * @param stageHeight ステージ全体の高さ（ピクセル）
	 * @param wallColor   壁の輪郭を描画する色
	 * @param scale       計算された現在の拡大率
	 * @param offsetX     中央配置用のXオフセット
	 * @param offsetY     中央配置用のYオフセット
	 */
	private void drawStageContent(GraphicsContext gc, int cols, int rows, double stageWidth, double stageHeight, 
			Color wallColor, double scale, double offsetX, double offsetY) {
		Item[][] itemMap = model.getItemMap();

		// 💡 JProの描画ズレバグ対策：壁の輪郭を描画する瞬間だけ拡大・移動を完全にリセット
		gc.save(); 
		gc.setTransform(1, 0, 0, 1, 0, 0); // グラフィックスの拡大・移動を完全にリセット（完全に生の画面座標にする）

		// 新しいWallOutlineオブジェクトを作り、幾何情報を同期
		WallOutline outline = new WallOutline(model.getMap(), MapData.TILE_SIZE);
		outline.setGeometry(scale, offsetX, offsetY);
		gc.setStroke(wallColor);
		gc.setLineWidth(2 * scale); // 💡 線の太さも画面の拡大率に追従させる
		outline.drawOutline(gc);
		
		gc.restore(); // アイテムやキャラを描画するために、拡大・移動が適用された元の座標系に復元する

		// アイテムを描画
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = col * MapData.TILE_SIZE;
				int y = row * MapData.TILE_SIZE;
				Item item = itemMap[row][col];
				if (item != null) {
					item.draw(gc, x, y, MapData.TILE_SIZE);
				}
			}
		}
		
		// フルーツを描画
		Fruit fruit = model.getCurrentFruit();
		if (fruit != null) {
			int fx = model.getFruitCol() * MapData.TILE_SIZE;
			int fy = model.getFruitRow() * MapData.TILE_SIZE;
			fruit.draw(gc, fx, fy, MapData.TILE_SIZE);
		}

		// フルーツ撃破時のスコアポップアップ（ふわっと上に浮かびながらフェードアウト）
		if (model.isFruitPopupActive()) {
			double progress = model.getFruitPopupProgress(); // 0.0〜1.0
			double riseOffset = progress * 20;
			double alpha = 1.0 - progress;

			double popupX = model.getFruitPopupX();
			double popupY = model.getFruitPopupY() - MapData.TILE_SIZE / 2.0 - 6 - riseOffset;

			gc.save();
			gc.setGlobalAlpha(alpha);

			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

			gc.setStroke(Color.BLACK);
			gc.setLineWidth(3);
			gc.strokeText("+" + model.getFruitPopupScore(), popupX, popupY);

			gc.setFill(Color.WHITE);
			gc.fillText("+" + model.getFruitPopupScore(), popupX, popupY);

			gc.restore();
		}
	}
	
	private final Image pacmanImage = new Image(
			getClass().getResource("/picture/syujinkou.png").toExternalForm());
	private final Image pacmanFeverImage = new Image(
			getClass().getResource("/picture/syujinkou_Fever.png").toExternalForm());

	/**
	 * プレイヤー（主人公）を描画する。
	 * 死亡アニメーション中は drawDyingsyujinkou に処理を委譲して回転・縮小・フェードアウト演出を行い、
	 * 死亡している（isAlive()がfalse）場合は何も描画しない。
	 * FEVER中は専用画像に切り替え、FEVER終了間際（残り3秒以内）は一定間隔で点滅させる。
	 * 画像が読み込めていない場合は代わりに黄色い円を描画する。
	 *
	 * @param gc 描画先のGraphicsContext
	 */
	public void drawPacman(GraphicsContext gc) {
		Syujinkou syujinkou = model.getsyujinkou();

		if (syujinkou == null)
			return;

		if (syujinkou.isDyingAnimation()) {
			drawDyingsyujinkou(gc, syujinkou);
			return;
		}

		if (!syujinkou.isAlive())
			return;

		if (pacmanImage == null) {
			gc.setFill(Color.YELLOW);
			gc.fillOval(syujinkou.getX(), syujinkou.getY(), MapData.TILE_SIZE, MapData.TILE_SIZE);
			return;
		}

		double pacX = syujinkou.getX() + MapData.TILE_SIZE / 2.0;
		double pacY = syujinkou.getY() + MapData.TILE_SIZE / 2.0;

		gc.save();
		gc.translate(pacX, pacY);

		// JPro対応: System.currentTimeMillis() を排し、drawが呼ばれた回数ベース(blinkTick)で安定して点滅させる
		if (syujinkou.isFever()) {
			long remain = model.getFeverRemainingTime();
			if (remain <= 3000) {
				// 8フレームごとに表示・非表示を切り替える
				if ((blinkTick / 8) % 2 == 0) {
					gc.restore();
					return;
				}
			}
		}
		
		Image currentImage = pacmanImage;
		if (syujinkou.isFever()) {
			currentImage = pacmanFeverImage;
		}

		gc.drawImage(currentImage, -MapData.TILE_SIZE / 2.0, -MapData.TILE_SIZE / 2.0, MapData.TILE_SIZE, MapData.TILE_SIZE);
		gc.restore();
	}

	/**
	 * 敵用のImageViewの表示サイズをタイルサイズに合わせて設定する（アスペクト比は維持）。
	 *
	 * @param enemyImageView サイズ設定を行う敵の画像View
	 */
	public void setupEnemyView(ImageView enemyImageView) {
		enemyImageView.setFitWidth(MapData.TILE_SIZE);
		enemyImageView.setFitHeight(MapData.TILE_SIZE);
		enemyImageView.setPreserveRatio(true);
	}

	/**
	 * 敵1体分を描画する。敵の種類（赤・緑・黄・青）に応じて対応する画像を取得し、その位置に描画する。
	 * 画像が取得できない場合は、敵の種類ごとの色で円と黒い目を描画する簡易表示にフォールバックする。
	 *
	 * @param gc    描画先のGraphicsContext
	 * @param enemy 描画対象の敵（nullの場合は何もしない）
	 */
	private void drawEnemyInstance(GraphicsContext gc, Enemy enemy) {
		if (enemy == null)
			return;

		Image img = null;

		if (enemy instanceof RedEnemy) {
			img = ((RedEnemy) enemy).getEnemyImage();
		} else if (enemy instanceof GreenEnemy) {
			img = ((GreenEnemy) enemy).getEnemyImage();
		} else if (enemy instanceof YellowEnemy) {
			img = ((YellowEnemy) enemy).getEnemyImage();
		} else if (enemy instanceof BlueEnemy) {
			img = ((BlueEnemy) enemy).getEnemyImage();
		}

		double enemyLeftX = enemy.getX() - MapData.TILE_SIZE / 2.0;
		double enemyTopY = enemy.getY() - MapData.TILE_SIZE / 2.0;

		if (img != null) {
			gc.drawImage(img, enemyLeftX, enemyTopY, MapData.TILE_SIZE, MapData.TILE_SIZE);
		} else {
			if (enemy instanceof RedEnemy) {
				gc.setFill(Color.RED);
			} else if (enemy instanceof GreenEnemy) {
				gc.setFill(Color.GREEN);
			} else if (enemy instanceof YellowEnemy) {
				gc.setFill(Color.YELLOW);
			} else if (enemy instanceof BlueEnemy) {
				gc.setFill(Color.BLUE);
			}
			gc.fillOval(enemyLeftX, enemyTopY, MapData.TILE_SIZE, MapData.TILE_SIZE);
			gc.setFill(Color.BLACK);
			gc.fillOval(enemy.getX() - 2, enemy.getY() - 2, 4, 4);
		}

		// 撃破時のスコアポップアップ表示（ふわっと上に浮かびながらフェードアウト）
		if (enemy.isScorePopupActive()) {
			double progress = enemy.getScorePopupProgress(); // 0.0〜1.0
			double riseOffset = progress * 20; // 上方向へのオフセット（最大20pxくらい浮かせる）
			double alpha = 1.0 - progress; // フェードアウト（後半から徐々に透明に）

			double popupX = enemy.getDefeatX();
			double popupY = enemy.getDefeatY() - MapData.TILE_SIZE / 2.0 - 6 - riseOffset;

			gc.save();
			gc.setGlobalAlpha(alpha);

			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

			// 縁取り（黒）
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(3);
			gc.strokeText("+" + enemy.getLastDefeatScore(), popupX, popupY);

			// 本体（白文字）
			gc.setFill(Color.WHITE);
			gc.fillText("+" + enemy.getLastDefeatScore(), popupX, popupY);

			gc.restore();
		}
	}

	/**
	 * プレイヤーの死亡（ミス）演出を描画する。
	 * 死亡進行度(progress: 0.0〜1.0)に応じて、画像を720度回転させながら
	 * 縮小・フェードアウトさせるアニメーションを行う。
	 *
	 * @param gc        描画先のGraphicsContext
	 * @param syujinkou 死亡アニメーション中のプレイヤー
	 */
	private void drawDyingsyujinkou(GraphicsContext gc, Syujinkou syujinkou) {
		double progress = syujinkou.getDyingProgress();

		double centerX = syujinkou.getX() + MapData.TILE_SIZE / 2.0;
		double centerY = syujinkou.getY() + MapData.TILE_SIZE / 2.0;

		double scale = 1.0 - progress;

		gc.save();
		gc.translate(centerX, centerY);
		gc.rotate(progress * 720);
		gc.scale(scale, scale);
		gc.setGlobalAlpha(1.0 - progress);

		gc.drawImage(pacmanImage, -MapData.TILE_SIZE / 2.0, -MapData.TILE_SIZE / 2.0, MapData.TILE_SIZE, MapData.TILE_SIZE);

		gc.restore();
		gc.setGlobalAlpha(1.0);
	}

	/**
	 * 非表示のダミー部品(Region)に適用されたCSSの背景色を取得する。
	 * これにより、壁やパックマンの色をCanvas描画側でもCSSファイルから一元管理できる。
	 * CSSクラスが設定されていない、または色の取得に失敗した場合はdefaultColorを返す。
	 *
	 * @param node         色を取得する対象のダミー部品
	 * @param defaultColor 取得に失敗した場合に使うデフォルト色
	 * @return CSSから取得した色、または defaultColor
	 */
	private Color getColorFromCSS(Region node, Color defaultColor) {
		if (node.getStyleClass().isEmpty()) {
			return defaultColor;
		}
		try {
			node.applyCss();
			Background bg = node.getBackground();
			if (bg != null && !bg.getFills().isEmpty()) {
				var fill = bg.getFills().get(0).getFill();
				if (fill instanceof Color) {
					return (Color) fill;
				}
			}
		} catch (Exception e) {
		}
		return defaultColor;
	}
}