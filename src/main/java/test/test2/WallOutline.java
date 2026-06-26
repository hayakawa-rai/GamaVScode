
package test.test2;

import javafx.scene.canvas.GraphicsContext;

public class WallOutline {

	private final int[][] map;
	private final int tile;

	public WallOutline(int[][] map, int tile) {
		this.map = map;
		this.tile = tile;
	}

	public void drawOutline(GraphicsContext gc) {
	    for (int ty = 0; ty < map.length; ty++) {
	        for (int tx = 0; tx < map[ty].length; tx++) {
	            if (!isWall(tx, ty)) {
	                drawRoadEdges(gc, tx, ty);
	            }
	        }
	    }
	}

	private void drawRoadEdges(GraphicsContext gc, int tx, int ty) {
	    double x0 = tx * tile;
	    double y0 = ty * tile;
	    double x1 = x0 + tile;
	    double y1 = y0 + tile;

	    // 上辺：上が壁、かつ左の道マスがまだ描いていない場合のみ
	    // → 「自分より左の道マスが同じ上辺を持っていない」＝左が壁か左が道でも上が壁でない
	    if (isWall(tx, ty - 1)) {
	        // 水平線の左端：左が壁でない道マスなら自分が左端
	        if (!(!isWall(tx - 1, ty) && isWall(tx - 1, ty - 1))) {
	            // 右端まで連続して壁が続く範囲を探す
	            int ex = tx;
	            while (ex + 1 < map[ty].length && !isWall(ex + 1, ty) && isWall(ex + 1, ty - 1)) {
	                ex++;
	            }
	            gc.beginPath();
	            gc.moveTo(x0, y0);
	            gc.lineTo((ex + 1) * tile, y0);
	            gc.stroke();
	        }
	    }

	    // 左辺：左が壁、かつ上の道マスがまだ描いていない場合のみ
	    if (isWall(tx - 1, ty)) {
	        if (!(!isWall(tx, ty - 1) && isWall(tx - 1, ty - 1))) {
	            int ey = ty;
	            while (ey + 1 < map.length && !isWall(tx, ey + 1) && isWall(tx - 1, ey + 1)) {
	                ey++;
	            }
	            gc.beginPath();
	            gc.moveTo(x0, y0);
	            gc.lineTo(x0, (ey + 1) * tile);
	            gc.stroke();
	        }
	    }

	    // 下辺（最下行の道マスのみ）
	    if (isWall(tx, ty + 1)) {
	        if (!(!isWall(tx - 1, ty) && isWall(tx - 1, ty + 1))) {
	            int ex = tx;
	            while (ex + 1 < map[ty].length && !isWall(ex + 1, ty) && isWall(ex + 1, ty + 1)) {
	                ex++;
	            }
	            gc.beginPath();
	            gc.moveTo(x0, y1);
	            gc.lineTo((ex + 1) * tile, y1);
	            gc.stroke();
	        }
	    }

	    // 右辺
	    if (isWall(tx + 1, ty)) {
	        if (!(!isWall(tx, ty - 1) && isWall(tx + 1, ty - 1))) {
	            int ey = ty;
	            while (ey + 1 < map.length && !isWall(tx, ey + 1) && isWall(tx + 1, ey + 1)) {
	                ey++;
	            }
	            gc.beginPath();
	            gc.moveTo(x1, y0);
	            gc.lineTo(x1, (ey + 1) * tile);
	            gc.stroke();
	        }
	    }
	}

	private boolean isWall(int x, int y) {
	    if (x < 0 || y < 0 || y >= map.length || x >= map[0].length)
	        return false;
	    return map[y][x] == 1; // 1のみ壁扱い（8,7は壁として描画しない）
	}
}