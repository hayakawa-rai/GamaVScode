package test.test2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapView {

	//モデル（ゲームデータ）
	private final MapData model;

	public MapView(MapData model) {
		this.model = model;
	}

	//マップ描画（壁など）
	public void drawStage(GraphicsContext gc) {
		int[][] map = model.getMap();

		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {

				//壁だけ描画
				if (map[y][x] == 1) {
					gc.setFill(Color.BLUE);
					gc.fillRect(
							x * MapData.TILE_SIZE,
							y * MapData.TILE_SIZE,
							MapData.TILE_SIZE,
							MapData.TILE_SIZE);
				}
			}
		}
	}

	//パックマン描画
	public void drawPacman(GraphicsContext gc) {

		//パックマンの色
		gc.setFill(Color.YELLOW);

		//扇形を描き始める基準の角度
		double startAngle = 0;

		//向きに応じて口の向きを変える
		if (model.getDirX() == 1)
			//【右向き】基準は0度。上下に半分ずつ口が開く
			startAngle = model.getMouthAngle();

		if (model.getDirX() == -1)
			//【左向き】基準は180度
			startAngle = 180 + model.getMouthAngle();

		if (model.getDirY() == -1)
			//【上向き】基準は90度
			startAngle = 90 + model.getMouthAngle();

		if (model.getDirY() == 1)
			//【下向き】基準は270度
			startAngle = 270 + model.getMouthAngle();

		//円弧でパックマンを描画（口が開く）
		gc.fillArc(
				model.getPacX() - MapData.TILE_SIZE / 2,  //描画の左上X座標に変換
				model.getPacY() - MapData.TILE_SIZE / 2,  //描画の左上Y座標に変換
				MapData.TILE_SIZE,  //横幅（30px）
				MapData.TILE_SIZE,  //縦幅（30px）
				startAngle,  //口が開く手前の角度からスタート
				360 - model.getMouthAngle() * 2,  //360度から口の開き分を引いた「円の残り」を描画
				javafx.scene.shape.ArcType.ROUND);  //中心からピザのピースのように閉じる
	}

	//全体描画
	public void draw(GraphicsContext gc) {

		//マップの実サイズ（ピクセル）
	    int mapW = model.getMap()[0].length * MapData.TILE_SIZE;
	    int mapH = model.getMap().length * MapData.TILE_SIZE;

	    //画面サイズに合わせて縮尺計算
	    double scaleX = gc.getCanvas().getWidth() / mapW;
	    double scaleY = gc.getCanvas().getHeight() / mapH;

	    //アスペクト比（縦横比）を維持するため、小さい方の倍率に合わせる
	    double scale = Math.min(scaleX, scaleY);

	    //中央に表示するためのオフセット
	    double offsetX = (gc.getCanvas().getWidth() - mapW * scale) / 2;
	    double offsetY = (gc.getCanvas().getHeight() - mapH * scale) / 2;

	    //描画状態を一時保存
	    gc.save();

	    //画面を中央＆スケール調整
	    gc.translate(offsetX, offsetY);
	    gc.scale(scale, scale);

	    //背景（黒)
	    gc.setFill(Color.BLACK);
	    gc.fillRect(0, 0, mapW, mapH);

	    //壁の描画
	    drawStage(gc);
	    
	    //パックマンの描画
	    drawPacman(gc);

	    //元に戻す
	    gc.restore();
	}
}
