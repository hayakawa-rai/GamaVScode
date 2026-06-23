package test.test2;

import Characters.Direction;
import Characters.Sengoku;
import Items.Item;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MapView {

	private final MapData model;
	// 口の向きを記憶しておく。（初期は右向き）
	private double lastBaseAngle = 0;

	public MapView(MapData model) {
		this.model = model;
	}

	/**
	 * ステージ全体を画面サイズに合わせて拡大縮小・中央配置して描画するメインメソッド
	 */
	public void draw(GraphicsContext gc, double canvasWidth, double canvasHeight) {
		// 1. ステージ本来のサイズを計算
		int cols = model.getMap()[0].length;
		int rows = model.getMap().length;
		double stageWidth = cols * MapData.TILE_SIZE;
		double stageHeight = rows * MapData.TILE_SIZE;
		
		double scaleX = canvasWidth / stageWidth;
		double scaleY = canvasHeight / stageHeight;

		// 2. 画面にぴったり収まる拡大率に「0.9」を掛けて、全体を90%の大きさに縮小する
		double bufferRatio = 0.9; // ★ここを変えることでサイズを自由に調整できます（0.8なら80%）
		double scale = Math.min(scaleX, scaleY) * bufferRatio;

		// 3. 小さくなった分も含めて、改めて中央に配置するための余白（オフセット）を計算
		double offsetX = (canvasWidth - (stageWidth * scale)) / 2.0;
		double offsetY = (canvasHeight - (stageHeight * scale)) / 2.0;

		// 4. 背景の黒を画面全体に塗る（余白も含めて真っ黒にする場合）
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);

		// 5. グラフィックスの状態を保存
		gc.save();

		// 6. 変換行列を適用（中央へ移動させてから、拡大する）
		gc.translate(offsetX, offsetY);
		gc.scale(scale, scale);

		// 7. 実際の描画処理を呼び出す
		drawStageContent(gc, cols, rows, stageWidth, stageHeight);
		drawPacman(gc);

		// 8. グラフィックスの状態を元に戻す（これを行わないと次回呼び出し時にズレが増幅します）
		gc.restore();
	}

	// drawStage から背景クリアとパックマン呼び出しを分離・整理した内部メソッド
	private void drawStageContent(GraphicsContext gc, int cols, int rows, double stageWidth, double stageHeight) {
		Item[][] itemMap = model.getItemMap();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int tile = model.getMap()[row][col];
				int x = col * MapData.TILE_SIZE;
				int y = row * MapData.TILE_SIZE;
				Item item = itemMap[row][col];
				
				// 壁の描画
				if (tile == 1) {
					gc.setFill(Color.BLUE);
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

	//内部の座標計算
	public void drawPacman(GraphicsContext gc) {
		Sengoku sengoku = model.getSengoku();
		if (sengoku == null || !sengoku.isAlive()) return;

		gc.setFill(Color.YELLOW);
		
		double pacX = sengoku.getX() + MapData.TILE_SIZE / 2.0;
		double pacY = sengoku.getY() + MapData.TILE_SIZE / 2.0;
		double mouthAngle = model.getMouthAngle();
		Direction currentDir = sengoku.getDirection();

		if (currentDir != null) {
			if (currentDir.getDX() == 1)  lastBaseAngle = 0;   // 右
			if (currentDir.getDX() == -1) lastBaseAngle = 180; // 左
			if (currentDir.getDY() == -1) lastBaseAngle = 90;  // 上
			if (currentDir.getDY() == 1)  lastBaseAngle = 270; // 下
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
}