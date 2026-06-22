// 遠くにいるときは追跡、近づくと自分の縄張りへ戻る　GreenEnemy(緑)
package Characters;
/*
public class GreenEnemy extends Enemy {
	
	// この距離以上ならプレイヤーを追いかける
	private static final double BORDER = 8 * CELL_SIZE;

	// 縄張り(退避場所)の座標
	// マップ左下を指定
	private static final int CORNER_COL = 0;
	private static final int CORNER_ROW = 30;

	public GreenEnemy(ImageView imageView) {

		super(imageView, 15 * CELL_SIZE, 15 * CELL_SIZE, 1);
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player) {

		double distance = getDistanceTo(player);

		// 遠いなら追跡
		if (distance >= BORDER) {

			// プレイヤーの座標(px)をセル座標に変換

			int playerCol = (int) ((player.getX() + CELL_SIZE / 2.0) / CELL_SIZE);

			int playerRow = (int) ((player.getY() + CELL_SIZE / 2.0) / CELL_SIZE);
			// プレイヤーに最も近づける方向を選択

			return getClosestDirection(validDirections, playerCol, playerRow);
		}

		// 近いなら縄張りへ戻る(左下) //後から設定
		return getClosestDirection(validDirections, CORNER_COL, CORNER_ROW);
	}
	
}*/