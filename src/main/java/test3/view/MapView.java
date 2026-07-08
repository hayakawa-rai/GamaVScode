package test3.view;

import Characters.BlueEnemy;
import Characters.Enemy;
import Characters.GreenEnemy;
import Characters.RedEnemy;
import Characters.Syujinkou;
import Characters.YellowEnemy;
import Items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import test3.model.MapData;

public class MapView {

	private final MapData model;

	// CSSの色を吸い取るための「見えないダミー部品」
	private final Region wallDummy = new Region();
	private final Region pacmanDummy = new Region();

	// ヘッダー
	private static final double INFO_HEIGHT = 40;

	// JPro対応: サーバー/クライアント間の通信ラグに影響されない安定した点滅用カウンター
	private int blinkTick = 0;

	// 互換コンストラクタ（引数1つ用）
	public MapView(MapData model) {
		this.model = model;
	}

	// 新しいコンストラクタ（引数2つ用）
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
				control.GameController.applyMobileControls(newScene, this.model);
			}
		});
	}

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

		// ★【重要】パックマンが動く「ステージの四角い枠内だけ」を真っ白に塗りつぶします
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, stageWidth, stageHeight);

		// 7. 実際の描画処理を呼び出す（💡 JProのズレ対策用に、現在の拡大・移動情報を渡す）
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
			gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
			gc.setTextBaseline(javafx.geometry.VPos.TOP);
			
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));

			// スコア
			gc.setFill(Color.WHITE);
			gc.fillText("SCORE : " + syujinkou.getScore(), 20, 12);

			// ライフ
			gc.setFill(Color.RED);
			gc.fillText("❤".repeat(syujinkou.getHp()), canvasWidth - 100, 12);

			// 区切り線
			gc.setStroke(Color.DARKGRAY);
			gc.strokeLine(0, INFO_HEIGHT, canvasWidth, INFO_HEIGHT);
		}
	}

	private void drawStageContent(GraphicsContext gc, int cols, int rows, double stageWidth, double stageHeight, 
			Color wallColor, double scale, double offsetX, double offsetY) {
		Item[][] itemMap = model.getItemMap();

		// 💡 JProの描画ズレバグ対策：壁の輪郭を描画する瞬間だけ拡大・移動を完全にリセット
		gc.save(); 
		gc.restore(); 
		
		gc.save(); // 輪郭用の一時保存
		gc.setTransform(1, 0, 0, 1, 0, 0); // グラフィックスの拡大・移動を完全にリセット（完全に生の画面座標にする）

		// WallOutline で壁を描画（幾何情報を同期）
		WallOutline outline = new WallOutline(model.getMap(), MapData.TILE_SIZE);
		outline.setGeometry(scale, offsetX, offsetY);
		gc.setStroke(wallColor);
		gc.setLineWidth(2 * scale); // 💡 線の太さも画面の拡大率に追従させる
		outline.drawOutline(gc);
		
		gc.restore(); // アイテムやキャラを描画するために、拡大・移動が適用された元の座標系に復元する

		// ★ アイテムを描画
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
		Items.Fruit fruit = model.getCurrentFruit();
		if (fruit != null) {
			int fx = model.getFruitCol() * MapData.TILE_SIZE;
			int fy = model.getFruitRow() * MapData.TILE_SIZE;
			fruit.draw(gc, fx, fy, MapData.TILE_SIZE);
		}
	}
	
	private final javafx.scene.image.Image pacmanImage = new javafx.scene.image.Image(
			getClass().getResource("/picture/syujinkou.png").toExternalForm());
	private final javafx.scene.image.Image pacmanFeverImage = new javafx.scene.image.Image(
			getClass().getResource("/picture/syujinkou_Fever.png").toExternalForm());

	public void drawPacman(GraphicsContext gc) {
		Syujinkou syujinkou = model.getsyujinkou();

		if (syujinkou == null)
			return;
		
		if(syujinkou.isDyingAnimation()) {
			drawDyingsyujinkou(gc, syujinkou);
			return;
		}
		
		if(!syujinkou.isAlive())
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

		// JPro対応: System.currentTimeMillis() を排し、安定して点滅させる
		if(syujinkou.isFever()) {
			long remain = model.getFeverRemainingTime();
			if(remain <= 3000) {
				// 8フレームごとに表示・非表示を滑らかに切り替え
				if((blinkTick / 8) % 2 == 0) {
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

	public void setupEnemyView(javafx.scene.image.ImageView enemyImageView) {
		enemyImageView.setFitWidth(MapData.TILE_SIZE);
		enemyImageView.setFitHeight(MapData.TILE_SIZE);
		enemyImageView.setPreserveRatio(true);
	}

	private void drawEnemyInstance(GraphicsContext gc, Enemy enemy) {
		if (enemy == null)
			return;

		javafx.scene.image.Image img = null;

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
				gc.setFill(javafx.scene.paint.Color.RED);
			} else if (enemy instanceof GreenEnemy) {
				gc.setFill(javafx.scene.paint.Color.GREEN);
			} else if (enemy instanceof YellowEnemy) {
				gc.setFill(javafx.scene.paint.Color.YELLOW);
			} else if (enemy instanceof BlueEnemy) {
				gc.setFill(javafx.scene.paint.Color.BLUE);
			}
			gc.fillOval(enemyLeftX, enemyTopY, MapData.TILE_SIZE, MapData.TILE_SIZE);
			gc.setFill(javafx.scene.paint.Color.BLACK);
			gc.fillOval(enemy.getX() - 2, enemy.getY() - 2, 4, 4);
		}

		// 撃破時のスコアポップアップ表示
		if (enemy.isScorePopupActive()) {
			double progress = enemy.getScorePopupProgress();
			double riseOffset = progress * 20;
			double alpha = 1.0 - progress;

			double popupX = enemy.getDefeatX();
			double popupY = enemy.getDefeatY() - MapData.TILE_SIZE / 2.0 - 6 - riseOffset;

			gc.save();
			gc.setGlobalAlpha(alpha);

			gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
			gc.setTextBaseline(javafx.geometry.VPos.CENTER);
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

			gc.setStroke(Color.BLACK);
			gc.setLineWidth(3);
			gc.strokeText("+" + enemy.getLastDefeatScore(), popupX, popupY);

			gc.setFill(Color.WHITE);
			gc.fillText("+" + enemy.getLastDefeatScore(), popupX, popupY);

			gc.restore();
		}
	}

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
			// JProタイムラグ対策用フォールバック
		}
		return defaultColor;
	}
}