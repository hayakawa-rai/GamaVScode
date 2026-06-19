//最短で追いかける　RedEnemy(赤)
package Characters;
/*
import java.util.List;

import javafx.scene.image.ImageView;

// 仙石さんを最短距離で追いかける
public class RedEnemy extends Enemy {

	// マップ中心 エネミーハウス上 （仮座標）
	private static final int START_COL = 13;
	private static final int START_ROW = 11;

	public RedEnemy(ImageView imageView) {
		super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1);
	}

	// 次に進む方向を決定
	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player) {

		// プレイヤーの現在マス
		int targetCol = (int) ((player.getX() + CELL_SIZE / 2.0) / CELL_SIZE);

		int targetRow = (int) ((player.getY() + CELL_SIZE / 2.0) / CELL_SIZE);

		// 一番近づける方向をEnemyクラスに選ばせる
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
*/