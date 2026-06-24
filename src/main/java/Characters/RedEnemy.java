// 最短距離でプレイヤーを追跡する敵（赤）
package Characters;
/*
import java.util.List;
import javafx.scene.image.ImageView;

public class RedEnemy extends Enemy {

	// マップ中心 エネミーハウス上 （仮座標）
	private static final int START_COL = 19;
	private static final int START_ROW = 14;

	 // 縄張りエリアの中心（右上）（仮座標）
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 3;

	public RedEnemy(ImageView imageView) {

		super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1);
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player) {

		// プレイヤーの現在セル座標
		int playerCol = (int) ((player.getX() + CELL_SIZE / 2.0) / CELL_SIZE);

		int playerRow = (int) ((player.getY() + CELL_SIZE / 2.0) / CELL_SIZE);

		// プレイヤーへ最短で近づく方向
		return getClosestDirection(validDirections, playerCol, playerRow);
	}
}
*/