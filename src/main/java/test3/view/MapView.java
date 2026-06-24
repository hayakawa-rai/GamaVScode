package test3.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import test3.model.MapData;

public class MapView {

	//モデル（ゲームデータ）
	private final MapData model;

	public MapView(MapData model) {
		this.model = model;
	}

	// CSSの背景色からColorオブジェクトを安全に引っ張り出す正しい処理
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

	// マップ描画（隙間ゼロの一体化ソリッドブロック）
	public void drawStage(GraphicsContext gc) {
		int[][] map = model.getMap();

		// 壁の色を取得（例: 鮮やかなピンクやシアン）
		Color wallColor = getColorFromCSS(gc, "game-wall", Color.BLUE);
		gc.setFill(wallColor);

		// 枠線（Stroke）は一切使わず、塗りつぶし（Fill）だけで描画します
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				if (map[y][x] == 1) {
					// 縦横のサイズを「TILE_SIZEぴったり」にします。
					// これにより、隣り合うブロック同士が完全に1本のぶっとい壁に同化します！
					gc.fillRect(
							x * MapData.TILE_SIZE,
							y * MapData.TILE_SIZE,
							MapData.TILE_SIZE,
							MapData.TILE_SIZE
					);
				}
			}
		}
	}

	//パックマン描画
	public void drawPacman(GraphicsContext gc) {
		Color pacmanColor = getColorFromCSS(gc, "game-pacman", Color.YELLOW);
		gc.setFill(pacmanColor);

		double startAngle = 0;

		if (model.getDirX() == 1) startAngle = model.getMouthAngle();
		if (model.getDirX() == -1) startAngle = 180 + model.getMouthAngle();
		if (model.getDirY() == -1) startAngle = 90 + model.getMouthAngle();
		if (model.getDirY() == 1) startAngle = 270 + model.getMouthAngle();

		gc.fillArc(
				model.getPacX() - MapData.TILE_SIZE / 2,
				model.getPacY() - MapData.TILE_SIZE / 2,
				MapData.TILE_SIZE,
				MapData.TILE_SIZE,
				startAngle,
				360 - model.getMouthAngle() * 2,
				ArcType.ROUND);
	}

	//全体描画
	public void draw(GraphicsContext gc) {
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

		// 背景色で画面全体を塗りつぶし（ここが暗いネオンカラーのベースになります）
		Color bgColor = getColorFromCSS(gc, "game-bg", Color.BLACK);
		gc.setFill(bgColor);
		gc.fillRect(0, 0, mapW, mapH);

		//壁の描画
		drawStage(gc);

		//パックマンの描画
		drawPacman(gc);

		gc.restore();
	}
}