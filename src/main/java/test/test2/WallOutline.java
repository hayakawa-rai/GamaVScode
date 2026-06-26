
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
		double inset = tile * 0.38; // 外側へのずらし量(0.1～0.4ぐらいがちょうどいいです)
		 
	    double x0 = tx * tile;
	    double y0 = ty * tile;
	    double x1 = x0 + tile;
	    double y1 = y0 + tile;

	    //連続線の始点・終点のずらし方を、隣が道かどうかで切り替えます：
	    // 上辺
	    if (isWall(tx, ty - 1)) {
	        if (!(!isWall(tx - 1, ty) && isWall(tx - 1, ty - 1))) {
	            int ex = tx;
	            while (ex + 1 < map[ty].length && !isWall(ex + 1, ty) && isWall(ex + 1, ty - 1)) {
	                ex++;
	            }
	            // 始点：左が道なら inset 分右に引っ込める、壁ならそのまま外側へ
	            double startX = isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
	            // 終点：右が道なら inset 分左に引っ込める、壁ならそのまま外側へ
	            double endX   = isWall(ex + 1, ty) ? (ex + 1) * tile + inset : (ex + 1) * tile - inset;
	            gc.beginPath();
	            gc.moveTo(startX, y0 - inset);
	            gc.lineTo(endX,   y0 - inset);
	            gc.stroke();
	        }
	    }

	    // 下辺
	    if (isWall(tx, ty + 1)) {
	        if (!(!isWall(tx - 1, ty) && isWall(tx - 1, ty + 1))) {
	            int ex = tx;
	            while (ex + 1 < map[ty].length && !isWall(ex + 1, ty) && isWall(ex + 1, ty + 1)) {
	                ex++;
	            }
	            double startX = isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
	            double endX   = isWall(ex + 1, ty) ? (ex + 1) * tile + inset : (ex + 1) * tile - inset;
	            gc.beginPath();
	            gc.moveTo(startX, y1 + inset);
	            gc.lineTo(endX,   y1 + inset);
	            gc.stroke();
	        }
	    }

	    // 左辺
	    if (isWall(tx - 1, ty)) {
	        if (!(!isWall(tx, ty - 1) && isWall(tx - 1, ty - 1))) {
	            int ey = ty;
	            while (ey + 1 < map.length && !isWall(tx, ey + 1) && isWall(tx - 1, ey + 1)) {
	                ey++;
	            }
	            double startY = isWall(tx, ty - 1) ? y0 - inset : y0 + inset;
	            double endY   = isWall(tx, ey + 1) ? (ey + 1) * tile + inset : (ey + 1) * tile - inset;
	            gc.beginPath();
	            gc.moveTo(x0 - inset, startY);
	            gc.lineTo(x0 - inset, endY);
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
	            double startY = isWall(tx, ty - 1) ? y0 - inset : y0 + inset;
	            double endY   = isWall(tx, ey + 1) ? (ey + 1) * tile + inset : (ey + 1) * tile - inset;
	            gc.beginPath();
	            gc.moveTo(x1 + inset, startY);
	            gc.lineTo(x1 + inset, endY);
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