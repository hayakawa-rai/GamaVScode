package test1.view;

import Characters.BlueEnemy;
import Characters.Enemy;
import Characters.GreenEnemy;
import Characters.RedEnemy;
import Characters.Sengoku;
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
import test1.model.MapData;

public class MapView {

	private final MapData model;

	// CSSの色を吸い取るための「見えないダミー部品」
	private final Region wallDummy = new Region();
	private final Region pacmanDummy = new Region();

	// ヘッダー
	private static final double INFO_HEIGHT = 40;

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
				test.test2.GameController.applyMobileControls(newScene, this.model);
			}
		});
	}

	/**
	 * ステージ全体を画面サイズに合わせて拡大縮小・中央配置して描画するメインメソッド
	 */
	public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {


		// 1. まずはCanvasを一度綺麗に消す（透明にする）
		gc.clearRect(0, 0, canvasWidth, canvasHeight);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvasWidth, INFO_HEIGHT);

		Color wallColor = getColorFromCSS(wallDummy, Color.BLUE);
		Color pacmanColor = getColorFromCSS(pacmanDummy,Color.YELLOW);

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

		// ★【重要】パックマンが動く「ステージの四角い枠内だけ」を真っ黒に塗りつぶします
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, stageWidth, stageHeight);

		// 7. 実際の描画処理を呼び出す
		drawStageContent(gc, cols, rows, stageWidth, stageHeight, wallColor);
		drawPacman(gc);

		// 敵の描画メソッド
		if (model.getEnemies() != null) {
			for (Enemy enemy : model.getEnemies()) {
				drawEnemyInstance(gc, enemy);
			}
		}

		// 8. グラフィックスの状態を元に戻す
		gc.restore();
		Sengoku sengoku = model.getSengoku();

		if (sengoku != null) {
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));

			// スコア
			gc.setFill(Color.WHITE);
			gc.fillText("SCORE : " + sengoku.getScore(), 20, 28);

			// ライフ
			gc.setFill(Color.RED);
			gc.fillText("❤".repeat(sengoku.getHp()), canvasWidth - 100, 28);

			// 区切り線
			gc.setStroke(Color.DARKGRAY);
			gc.strokeLine(0, INFO_HEIGHT, canvasWidth, INFO_HEIGHT);
		}

		// モデルが一時停止中（paused）だったら、画面中央にテキストを描画する
		if (model.isPaused() && !model.isGameOver() && !model.isCleared()) {

			// 1. 画面全体を少し暗くする（半透明の黒いフィルターを重ねる）
			gc.setFill(Color.rgb(0, 0, 0, 0.6)); // 最後の0.6が不透明度（60%）
			gc.fillRect(0, 0, canvasWidth, canvasHeight);

			// 2. 「PAUSE」の文字を設定
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 48)); // 大きめのフォント
			gc.setFill(Color.YELLOW); // 目立つ黄色

			// 3. 文字が中央にぴったり配置されるように、文字の基準点を「中央」にする
			gc.setTextAlign(javafx.scene.text.TextAlignment.CENTER);
			gc.setTextBaseline(javafx.geometry.VPos.CENTER);

			// 4. キャンバスの真ん中（横幅 / 2, 高さ / 2）に描画
			gc.fillText("PAUSE", canvasWidth / 2.0, canvasHeight / 2.0);

			// ここから日本語サブテキストの描画 
			gc.setFont(Font.font("Meiryo", FontWeight.BOLD, 16)); // メイリオで少し太めに
			gc.setFill(Color.WHITE); // 白文字

			// PAUSEの文字から、縦に「45ピクセル」下にずらした位置に描画
			gc.fillText("もう一度 Pキー を押すと再開します", canvasWidth / 2.0, (canvasHeight / 2.0) + 45);

			// 後続の描画（スコアなど）が崩れないように、基準点をデフォルト（左、トップ）に戻しておく
			gc.setTextAlign(javafx.scene.text.TextAlignment.LEFT);
			gc.setTextBaseline(javafx.geometry.VPos.TOP);
		}
	}

	// drawStage から背景クリアとパックマン呼び出しを分離・整理した内部メソッド
	private void drawStageContent(GraphicsContext gc, int cols, int rows, double stageWidth, double stageHeight,
			Color wallColor) {
		Item[][] itemMap = model.getItemMap();

		// WallOutline で壁を描画
		WallOutline outline = new WallOutline(model.getMap(), MapData.TILE_SIZE);
		gc.setStroke(wallColor);
		gc.setLineWidth(2);
		outline.drawOutline(gc);

		// ★ アイテムを描画
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = col * MapData.TILE_SIZE;
				int y = row * MapData.TILE_SIZE;
				Item item = itemMap[row][col];

				// アイテムの描画
				if (item != null) {
					item.draw(gc, x, y, MapData.TILE_SIZE);
				}
			}
		}
	}
	
	//MapViewのフィールドにPac-man画像を追加
	
	private final javafx.scene.image.Image pacmanImage = new javafx.scene.image.Image(
			getClass().getResource("/picture/sengoku.png").toExternalForm());
	private final javafx.scene.image.Image pacmanFeverImage = new javafx.scene.image.Image(
			getClass().getResource("/picture/sengoku_Fever.png").toExternalForm());

	public void drawPacman(GraphicsContext gc) {
		Sengoku sengoku = model.getSengoku();

		if (sengoku == null)
			return;
		
		if(sengoku.isDyingAnimation()) {
			drawDyingSengoku(gc,sengoku);
			return;
		}
		
		if(!sengoku.isAlive())
			return;

		if (pacmanImage == null) {
			gc.setFill(Color.YELLOW);
			gc.fillOval(sengoku.getX(), sengoku.getY(), MapData.TILE_SIZE, MapData.TILE_SIZE);
			return;
		}

		double pacX = sengoku.getX() + MapData.TILE_SIZE / 2.0;
		double pacY = sengoku.getY() + MapData.TILE_SIZE / 2.0;
		
		Characters.Direction dir = sengoku.getDirection();
		double angle = 0;
		
		gc.save();

		gc.translate(pacX, pacY);
		gc.rotate(angle);

		//FEVER終了時は点滅
		if(sengoku.isFever()) {
			
			long remain = model.getFeverRemainingTime();
				
			if(remain <= 3000) {
			
				if((System.currentTimeMillis() / 150) % 2 == 0) {
					gc.restore();
					return;
				}
			}
		}
		
		//使用画像を指定
		
		Image currentImage = pacmanImage;

		if (sengoku.isFever()) {
			currentImage = pacmanFeverImage;

		}

		gc.drawImage(currentImage, -MapData.TILE_SIZE / 2.0, -MapData.TILE_SIZE / 2.0, MapData.TILE_SIZE,
				MapData.TILE_SIZE);
		gc.restore();
	}

	public void setupEnemyView(javafx.scene.image.ImageView enemyImageView) {
		enemyImageView.setFitWidth(MapData.TILE_SIZE);
		enemyImageView.setFitHeight(MapData.TILE_SIZE);
		enemyImageView.setPreserveRatio(true);
	}

	private void drawEnemy(GraphicsContext gc) {
		Enemy enemy = model.getEnemy();
		if (enemy == null)
			return;

		if (enemy instanceof RedEnemy) {
			RedEnemy red = (RedEnemy) enemy;
			Image img = red.getEnemyImage();
			double enemyLeftX = red.getX() - MapData.TILE_SIZE / 2.0;
			double enemyTopY = red.getY() - MapData.TILE_SIZE / 2.0;

			if (img != null) {
				gc.drawImage(img, enemyLeftX, enemyTopY, MapData.TILE_SIZE, MapData.TILE_SIZE);
			} else {
				gc.setFill(Color.RED);
				gc.fillOval(red.getX(), red.getY(), MapData.TILE_SIZE, MapData.TILE_SIZE);
				gc.setFill(Color.BLACK);
				gc.fillOval(red.getX() + MapData.TILE_SIZE / 2.0 - 2, red.getY() + MapData.TILE_SIZE / 2.0 - 2, 4, 4);
			}
		}
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
	}

	private void drawDyingSengoku(GraphicsContext gc, Sengoku sengoku) {
		double progress = sengoku.getDyingProgress();

		double centerX = sengoku.getX() + MapData.TILE_SIZE / 2.0;
		double centerY = sengoku.getY() + MapData.TILE_SIZE / 2.0;

		double scale = 1.0 - progress;

		gc.save();
		gc.translate(centerX, centerY);
		gc.rotate(progress * 720);
		gc.scale(scale, scale);
		gc.setGlobalAlpha(1.0 - progress);

		gc.drawImage(
				pacmanImage,
				-MapData.TILE_SIZE / 2.0,
				-MapData.TILE_SIZE / 2.0,
				MapData.TILE_SIZE,
				MapData.TILE_SIZE);

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
		}
		return defaultColor;
	}
}