//最短で追いかける　RedEnemy(赤)

package Characters;

import java.util.List;

import javafx.scene.image.ImageView;

// 仙石さんを最短距離で追いかける
public class RedEnemy extends Enemy {

	// マップ右上からスタート （仮座標）
	public RedEnemy(ImageView imageView) {
		super(imageView, 27 * CELL_SIZE, 0, 1);
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