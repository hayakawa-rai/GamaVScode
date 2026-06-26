package test1.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import test.BlueEnemy;
import test.Enemy;
import test.GreenEnemy;
import test.RedEnemy;
import test.YellowEnemy;
import test1.model.MapData;

public class MapView {

	private final MapData model;
	private double lastBaseAngle = 0;

	public MapView(MapData model) {
		this.model = model;
	}
	
	// 新しいコンストラクタ（引数2つ用）
		public MapView(MapData model, Pane root) {
			this.model = model;
		
			root.sceneProperty().addListener((observable, oldScene, newScene) -> {
				if (newScene != null) {
					test.test2.GameController.applyMobileControls(newScene, this.model);
				}
			});
		}

	// CSSの色を安全に取得する処理
	private Color getColorFromCSS(GraphicsContext gc, String styleClass, Color fallbackColor) {
		try {
			javafx.scene.Scene scene = gc.getCanvas().getScene();
			if (scene == null)
				return fallbackColor;

			javafx.scene.layout.Region dummy = new javafx.scene.layout.Region();
			dummy.getStyleClass().add(styleClass);

			javafx.scene.Parent root = scene.getRoot();
			if (root instanceof javafx.scene.layout.BorderPane) {
				javafx.scene.layout.BorderPane bp = (javafx.scene.layout.BorderPane) root;
				bp.getChildren().add(dummy);
				bp.applyCss();

				if (dummy.getBackground() != null && !dummy.getBackground().getFills().isEmpty()) {
					javafx.scene.paint.Paint paint = dummy.getBackground().getFills().get(0).getFill();
					bp.getChildren().remove(dummy);
					if (paint instanceof Color) {
						return (Color) paint;
					}
				}
				bp.getChildren().remove(dummy);
			}
		} catch (Exception e) {
			return fallbackColor;
		}
		return fallbackColor;
	}

	// マップ描画（test.test2仕様：四隅に余白をつけてブロック感を出す）
	public void drawStage(GraphicsContext gc) {
		int[][] map = model.getMap();
		Color wallColor = getColorFromCSS(gc, "game-wall", Color.BLUE);
		gc.setFill(wallColor);

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] == 1) {
					// ★ test.test2 と同様に、+2の隙間と幅-4を適用して独立した正方形ブロックにする
					gc.fillRect(
							(x * MapData.TILE_SIZE) + 2,
							(y * MapData.TILE_SIZE) + 2,
							MapData.TILE_SIZE - 4,
							MapData.TILE_SIZE - 4);
				}
			}
		}

		// スコア表示（右下）
		// ※ modelにgetScore()あるいはそれに類するメソッドが実装されている前提です
		try {
			// 例として model.getScore() で取得を試みます。メソッド名が異なる場合は適宜修正してください。
			// String scoreText = "SCORE: " + model.getScore();
			// gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			// gc.setFill(Color.WHITE);
			// double textX = (map[0].length * MapData.TILE_SIZE) - 140;
			// double textY = (map.length * MapData.TILE_SIZE) - 20;
			// gc.fillText(scoreText, textX, textY);
		} catch (Exception e) {
			// スコアメソッドがmodelにない場合はエラーで落ちないようにスルー
		}
	}

	// パックマン描画（中心座標基準・向きの修正対応）
	public void drawPacman(GraphicsContext gc) {
		Color pacmanColor = getColorFromCSS(gc, "game-pacman", Color.YELLOW);
		gc.setFill(pacmanColor);

		if (model.getDirX() == 1)   lastBaseAngle = 0;
		if (model.getDirX() == -1)  lastBaseAngle = 180;
		if (model.getDirY() == -1)  lastBaseAngle = 90;
		if (model.getDirY() == 1)   lastBaseAngle = 270;

		double finalStartAngle = lastBaseAngle + model.getMouthAngle();

		gc.fillArc(
				model.getPacX() - MapData.TILE_SIZE / 2.0,
				model.getPacY() - MapData.TILE_SIZE / 2.0,
				MapData.TILE_SIZE,
				MapData.TILE_SIZE,
				finalStartAngle,
				360 - model.getMouthAngle() * 2,
				ArcType.ROUND);
	}

	// アイテム描画
	private void drawItems(GraphicsContext gc) {
		for (int y = 0; y < model.getMap().length; y++) {
			for (int x = 0; x < model.getMap()[0].length; x++) {
				var item = model.getItem(x, y);
				if (item != null) {
					item.draw(
							gc,
							x * MapData.TILE_SIZE,
							y * MapData.TILE_SIZE,
							MapData.TILE_SIZE);
				}
			}
		}
	}

	// すべての敵を一斉にループ描画するメソッド
//	private void drawEnemies(GraphicsContext gc) {
//		if (model.getEnemies() != null) {
//			for (Enemy enemy : model.getEnemies()) {
//				drawEnemyInstance(gc, enemy);
//			}
//		}
//	}

	// 中心ズレ補正版の個別敵描画ロジック
	private void drawEnemyInstance(GraphicsContext gc, Enemy enemy) {
		if (enemy == null) return;

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
	}

	// 全体描画
	public void draw(GraphicsContext gc) {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		int mapW = model.getMap()[0].length * MapData.TILE_SIZE;
		int mapH = model.getMap().length * MapData.TILE_SIZE;

		double scaleX = gc.getCanvas().getWidth() / mapW;
		double scaleY = gc.getCanvas().getHeight() / mapH;
		double scale = Math.min(scaleX, scaleY);

		double offsetX = (gc.getCanvas().getWidth() - mapW * scale) / 2;
		double offsetY = (gc.getCanvas().getHeight() - mapH * scale) / 2;

		gc.save();
		gc.translate(offsetX, offsetY);
		gc.scale(scale, scale);

		// 背景を黒で塗りつぶし
		Color bgColor = getColorFromCSS(gc, "game-bg", Color.BLACK);
		gc.setFill(bgColor);
		gc.fillRect(0, 0, mapW, mapH);

		// 順番通りに1回ずつ描画
		drawStage(gc);   // 1. 壁・スコア
		drawItems(gc);   // 2. アイテム
		drawPacman(gc);  // 3. パックマン
		//drawEnemies(gc); // 4. 敵

		gc.restore();
	}
}