package test2.view;

import javafx.scene.canvas.GraphicsContext;

/**
 * マップ上の「壁ではないマス（道）」の外周に沿って輪郭線を描画するクラス。
 * マップの数値そのもの（1=壁）を直接塗りつぶすのではなく、道タイルの
 * 上下左右の辺のうち、隣が壁になっている辺だけを線として描画することで、
 * 迷路のような輪郭表現を作り出す。
 * 
 * JProでの描画バグを回避するため、GraphicsContext の translate を使わず、
 * 線の描画座標自体に直接スケールとオフセットを適用する設計に拡張しています。
 */
public class WallOutline {

	// 描画対象のマップデータ（1=壁、それ以外=道として扱う）
	private final int[][] map;
	// 1マスのピクセルサイズ
	private final int tile;

	// 💡 画面のスケーリングと中央配置用オフセットを保持する変数を追加
	private double scale = 1.0;
	private double offsetX = 0.0;
	private double offsetY = 0.0;

	/**
	 * コンストラクタ。
	 *
	 * @param map  壁判定に使うマップの二次元配列
	 * @param tile 1マスのピクセルサイズ
	 */
	public WallOutline(int[][] map, int tile) {
		this.map = map;
		this.tile = tile;
	}

	/**
	 * 💡 MapViewの描画ループから現在のスケーリング情報を同期するためのメソッド。
	 *
	 * @param scale   現在の画面倍率
	 * @param offsetX 画面の左側余白
	 * @param offsetY 画面の上側余白
	 */
	public void setGeometry(double scale, double offsetX, double offsetY) {
		this.scale = scale;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	/**
	 * マップ全体を走査し、壁ではない（道の）マスそれぞれについて
	 * drawRoadEdgesを呼び出し、壁と隣接する辺に輪郭線を描画する。
	 *
	 * @param gc 描画先のGraphicsContext
	 */
	public void drawOutline(GraphicsContext gc) {
		for (int ty = 0; ty < map.length; ty++) {
			for (int tx = 0; tx < map[ty].length; tx++) {
				if (!isWall(tx, ty)) {
					drawRoadEdges(gc, tx, ty);
				}
			}
		}
	}

	/**
	 * 指定した道タイル(tx, ty)について、上下左右のうち壁と隣接している辺を検出し、
	 * その辺に沿って線を描画する。
	 * 
	 * 全ての計算に scale を乗算し、座標の基準点に offsetX / offsetY を加算することで
	 * 画面の拡大縮小と中央配置に対応させます。
	 *
	 * @param gc 描画先のGraphicsContext
	 * @param tx 対象タイルの列インデックス
	 * @param ty 対象タイルの行インデックス
	 */
	private void drawRoadEdges(GraphicsContext gc, int tx, int ty) {
		// 💡 tileサイズとずらし量(inset)にスケールを適用
		double scaledTile = tile * scale;
		double inset = scaledTile * 0.38;

		// 💡 タイルの四隅の座標を、スケールと画面オフセット(offsetX, offsetY)を考慮して直接計算
		double x0 = (tx * tile) * scale + offsetX;
		double y0 = (ty * tile) * scale + offsetY;
		double x1 = x0 + scaledTile;
		double y1 = y0 + scaledTile;

		// 上辺
		if (isWall(tx, ty - 1)) {
			if (!(!isWall(tx - 1, ty) && isWall(tx - 1, ty - 1))) {
				int ex = tx;
				while (ex + 1 < map[ty].length && !isWall(ex + 1, ty) && isWall(ex + 1, ty - 1)) {
					ex++;
				}
				double startX = isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
				// 💡 終点の計算式もスケールとオフセットに対応
				double endX = isWall(ex + 1, ty) ? ((ex + 1) * tile) * scale + offsetX + inset
						: ((ex + 1) * tile) * scale + offsetX - inset;
				gc.beginPath();
				gc.moveTo(startX, y0 - inset);
				gc.lineTo(endX, y0 - inset);
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
				// 💡 終点の計算式もスケールとオフセットに対応
				double endX = isWall(ex + 1, ty) ? ((ex + 1) * tile) * scale + offsetX + inset
						: ((ex + 1) * tile) * scale + offsetX - inset;
				gc.beginPath();
				gc.moveTo(startX, y1 + inset);
				gc.lineTo(endX, y1 + inset);
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
				// 💡 終点の計算式もスケールとオフセットに対応
				double endY = isWall(tx, ey + 1) ? ((ey + 1) * tile) * scale + offsetY + inset
						: ((ey + 1) * tile) * scale + offsetY - inset;
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
				// 💡 終点の計算式もスケールとオフセットに対応
				double endY = isWall(tx, ey + 1) ? ((ey + 1) * tile) * scale + offsetY + inset
						: ((ey + 1) * tile) * scale + offsetY - inset;
				gc.beginPath();
				gc.moveTo(x1 + inset, startY);
				gc.lineTo(x1 + inset, endY);
				gc.stroke();
			}
		}
	}

	/**
	 * 指定した座標(x, y)のマスが壁かどうかを判定する。
	 * マップ範囲外の座標は壁ではない（false）として扱う。
	 * マップ上の値が1のマスだけを壁とみなし、7(扉)・8(巣)などは壁として描画しない。
	 *
	 * @param x 判定するタイルの列インデックス
	 * @param y 判定するタイルの行インデックス
	 * @return 壁（値が1）であればtrue、それ以外またはマップ範囲外であればfalse
	 */
	private boolean isWall(int x, int y) {
		if (x < 0 || y < 0 || y >= map.length || x >= map[0].length)
			return false;
		return map[y][x] == 1; // 1のみ壁扱い（8,7は壁として描画しない）
	}
}