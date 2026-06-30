package test.test2;

import Characters.BlueEnemy;
import Characters.Enemy;
import Characters.GreenEnemy;
import Characters.RedEnemy;
import Characters.Sengoku;
import Characters.YellowEnemy;
import Items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MapView {

	private final MapData model;

	private final Region wallDummy = new Region();
	private final Region pacmanDummy = new Region();

	private double lastBaseAngle = 0;

	public MapView(MapData model) {
		this.model = model;
	}

	public MapView(MapData model, Pane root) {
		this.model = model;

		wallDummy.getStyleClass().add("game-wall");
		pacmanDummy.getStyleClass().add("game-pacman");

		wallDummy.setVisible(false);
		pacmanDummy.setVisible(false);
		root.getChildren().addAll(wallDummy, pacmanDummy);

		root.sceneProperty().addListener((observable, oldScene, newScene) -> {
			if (newScene != null) {
				test.test2.GameController.applyMobileControls(newScene, this.model);
			}
		});
	}

	public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {

		gc.clearRect(0, 0, canvasWidth, canvasHeight);

		Color wallColor = getColorFromCSS(wallDummy, Color.BLUE);
		Color pacmanColor = getColorFromCSS(pacmanDummy, Color.YELLOW);

		int cols = model.getMap()[0].length;
		int rows = model.getMap().length;
		double stageWidth = cols * MapData.TILE_SIZE;
		double stageHeight = rows * MapData.TILE_SIZE;

		double scaleX = canvasWidth / stageWidth;
		double scaleY = canvasHeight / stageHeight;
		double bufferRatio = 0.9;
		double scale = Math.min(scaleX, scaleY) * bufferRatio;

		double offsetX = (canvasWidth - (stageWidth * scale)) / 2.0;
		double offsetY = (canvasHeight - (stageHeight * scale)) / 2.0;

		gc.save();

		gc.translate(offsetX, offsetY);
		gc.scale(scale, scale);

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, stageWidth, stageHeight);

		drawStageContent(gc, cols, rows, stageWidth, stageHeight, wallColor);

		drawPacman(gc, pacmanColor);

		if (model.getEnemies() != null) {
			for (Enemy enemy : model.getEnemies()) {
				drawEnemyInstance(gc, enemy);
			}
		}

		gc.restore();
	}

	private void drawStageContent(GraphicsContext gc, int cols, int rows, double stageWidth, double stageHeight,
			Color wallColor) {

		// ★ 壁を先に描画
		WallOutline outline = new WallOutline(model.getMap(), MapData.TILE_SIZE);
		gc.setStroke(wallColor);
		gc.setLineWidth(2);
		outline.drawOutline(gc);

		// ★ アイテムを描画
		Item[][] itemMap = model.getItemMap();
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

		// ★ スコア・残機をステージ内に表示（gc.restore() は draw() に任せる）
		Sengoku sengoku = model.getSengoku();
		if (sengoku != null) {
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
			gc.setFill(Color.WHITE);
			gc.fillText("SCORE : " + sengoku.getScore(), 15, 25);

			gc.setFill(Color.RED);
			gc.fillText("❤".repeat(sengoku.getHp()), 15, 50);
		}
	}

	public void drawPacman(GraphicsContext gc, Color pacmanColor) {
		Sengoku sengoku = model.getSengoku();
		if (sengoku == null || !sengoku.isAlive()) return;

		gc.setFill(pacmanColor);

		// ★ パックマンは左上基準 → 中心を求めて描画
		double pacX = sengoku.getX() + MapData.TILE_SIZE / 2.0;
		double pacY = sengoku.getY() + MapData.TILE_SIZE / 2.0;

		double mouthAngle = model.getMouthAngle();
		Characters.Direction currentDir = sengoku.getDirection();

		if (currentDir != null) {
			if (currentDir.getDX() == 1)  lastBaseAngle = 0;   // 右
			if (currentDir.getDX() == -1) lastBaseAngle = 180; // 左
			if (currentDir.getDY() == -1) lastBaseAngle = 90;  // 上
			if (currentDir.getDY() == 1)  lastBaseAngle = 270; // 下
		}

		double finalStartAngle = lastBaseAngle + mouthAngle;

		// ★ 中心から TILE_SIZE/2 引いて fillArc の左上基準に変換
		gc.fillArc(
				pacX - MapData.TILE_SIZE / 2.0,
				pacY - MapData.TILE_SIZE / 2.0,
				MapData.TILE_SIZE,
				MapData.TILE_SIZE,
				finalStartAngle,
				360 - mouthAngle * 2,
				javafx.scene.shape.ArcType.ROUND
		);
	}

	public void setupEnemyView(javafx.scene.image.ImageView enemyImageView) {
		enemyImageView.setFitWidth(MapData.TILE_SIZE);
		enemyImageView.setFitHeight(MapData.TILE_SIZE);
		enemyImageView.setPreserveRatio(true);
	}

	private void drawEnemyInstance(GraphicsContext gc, Enemy enemy) {
		if (enemy == null) return;

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

		// ★ 敵は中心基準 → TILE_SIZE/2 引いて左上基準に変換して描画
		double drawX = enemy.getX() - MapData.TILE_SIZE / 2.0;
		double drawY = enemy.getY() - MapData.TILE_SIZE / 2.0;

		if (img != null) {
			gc.drawImage(img, drawX, drawY, MapData.TILE_SIZE, MapData.TILE_SIZE);
		} else {
			// 画像なし時の代替描画
			if (enemy instanceof RedEnemy) {
				gc.setFill(javafx.scene.paint.Color.RED);
			} else if (enemy instanceof GreenEnemy) {
				gc.setFill(javafx.scene.paint.Color.GREEN);
			} else if (enemy instanceof YellowEnemy) {
				gc.setFill(javafx.scene.paint.Color.YELLOW);
			} else {
				gc.setFill(javafx.scene.paint.Color.BLUE);
			}
			gc.fillOval(drawX, drawY, MapData.TILE_SIZE, MapData.TILE_SIZE);
		}
	}

	private Color getColorFromCSS(Region node, Color defaultColor) {
		if (node.getStyleClass().isEmpty()) return defaultColor;
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