package test.test2;

import Characters.Direction;
import Characters.Sengoku;
import Items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import test.BlueEnemy;
import test.Enemy;
import test.GreenEnemy;
import test.RedEnemy;
import test.YellowEnemy;

public class MapView {

	private final MapData model;
	
	// CSSの色を吸い取るための「見えないダミー部品」
	private final Region wallDummy = new Region();
	private final Region pacmanDummy = new Region();
    
	// 口の向きを記憶しておく。（初期は右向き）
	private double lastBaseAngle = 0;

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
	}

	/**
	 * ステージ全体を画面サイズに合わせて拡大縮小・中央配置して描画するメインメソッド
	 */
	public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
		// 1. まずはCanvasを一度綺麗に消す（透明にする）
		gc.clearRect(0, 0, canvasWidth, canvasHeight); 
        
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
		double offsetY = (canvasHeight - (stageHeight * scale)) / 2.0;

		// 4. 【重要】画面全体の黒塗りを廃止（これで後ろの背景画像が透けます）
		// gc.setFill(Color.BLACK);
		// gc.fillRect(0, 0, canvasWidth, canvasHeight);

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
		drawPacman(gc, pacmanColor);
		
		// 敵の描画メソッド
		if (model.getEnemies() != null) {
			for (Enemy enemy : model.getEnemies()) {
				drawEnemyInstance(gc, enemy); 
			}
		}

		// 8. グラフィックスの状態を元に戻す
		gc.restore();
	}

	private void drawStageContent(GraphicsContext gc, int cols, int rows, double stageWidth, double stageHeight, Color wallColor) {
		Item[][] itemMap = model.getItemMap();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int tile = model.getMap()[row][col];
				int x = col * MapData.TILE_SIZE;
				int y = row * MapData.TILE_SIZE;
				Item item = itemMap[row][col];
				
				// 壁の描画
				if (tile == 1) {
					gc.setFill(wallColor); 
					gc.fillRect(x + 2, y + 2, MapData.TILE_SIZE - 4, MapData.TILE_SIZE - 4);
				}
				
				// アイテムの描画
				if (item != null) {
					item.draw(gc, x, y, MapData.TILE_SIZE);
				}
			}
		}

		// スコアを表示させるためのコード
		Sengoku sengoku = model.getSengoku();
		if (sengoku != null) {
			String scoreText = "SCORE: " + sengoku.getScore();
			gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
			gc.setFill(Color.WHITE);
			
			// ステージの右下に配置
			double textX = stageWidth - 140; 
			double textY = stageHeight - 20; 
			
			gc.fillText(scoreText, textX, textY);
		}
	}

	public void drawPacman(GraphicsContext gc, Color pacmanColor) {
		Sengoku sengoku = model.getSengoku();
		if (sengoku == null || !sengoku.isAlive()) return;

		gc.setFill(pacmanColor); 
		
		double pacX = sengoku.getX() + MapData.TILE_SIZE / 2.0;
		double pacY = sengoku.getY() + MapData.TILE_SIZE / 2.0;
		double mouthAngle = model.getMouthAngle();
		Direction currentDir = sengoku.getDirection();

		if (currentDir != null) {
			if (currentDir.getDX() == 1)  lastBaseAngle = 0;   
			if (currentDir.getDX() == -1) lastBaseAngle = 180; 
			if (currentDir.getDY() == -1) lastBaseAngle = 90;  
			if (currentDir.getDY() == 1)  lastBaseAngle = 270; 
		}

		double finalStartAngle = lastBaseAngle + mouthAngle;
		
		gc.fillArc(
			pacX - MapData.TILE_SIZE / 2.0,
			pacY - MapData.TILE_SIZE / 2.0,
			MapData.TILE_SIZE, MapData.TILE_SIZE,
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

	private void drawEnemy(GraphicsContext gc) {
		Enemy enemy = model.getEnemy();
		if (enemy == null) return;
 
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
				gc.fillOval(red.getX() + MapData.TILE_SIZE / 2.0 - 2,
				            red.getY() + MapData.TILE_SIZE / 2.0 - 2, 4, 4);
			}
		}
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
			// ⭕ 青の画像を取得
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
			}
			gc.fillOval(enemyLeftX, enemyTopY, MapData.TILE_SIZE, MapData.TILE_SIZE);

			gc.setFill(javafx.scene.paint.Color.BLACK);
			gc.fillOval(enemy.getX() - 2, enemy.getY() - 2, 4, 4);
		}
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