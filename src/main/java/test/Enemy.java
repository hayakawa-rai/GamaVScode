//エネミークラス
package test;

import java.util.ArrayList;
import java.util.List;

import test.test2.MapData;

public abstract class Enemy extends Character {

	protected javafx.scene.image.ImageView imageView;

	protected MapData mapData;

	protected Characters.EnemyState currentState = Characters.EnemyState.SCATTER;

	protected javafx.scene.image.Image normalImage;
	protected javafx.scene.image.Image feverImage;
	protected javafx.scene.image.Image deadImage;

	public Enemy(double startX, double startY, int speed) {
		super(startX, startY, speed);
	}

	protected abstract Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData);

	@Override
	public void move(int[][] map) {

		int tileX = (int) (this.x / MapData.TILE_SIZE);
		int tileY = (int) (this.y / MapData.TILE_SIZE);

		// 範囲外防止
		if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
			return;
		}

		double cx = tileX * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0;
		double cy = tileY * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0;

		if (currentState == Characters.EnemyState.DEAD) {

			int col = (int) (x / MapData.TILE_SIZE);
			int row = (int) (y / MapData.TILE_SIZE);

			// 巣に到着
			if (col == 14 && row == 14) {

				currentState = Characters.EnemyState.SCATTER;

				System.out.println(getClass().getSimpleName() + "復活");
			}
		}

		// 現在のスピードの計算
		double currentSpeed = this.getSpeed();

		// FEVER時は半速
		if (this.currentState == Characters.EnemyState.FEVER) {
			currentSpeed = this.getSpeed() * 0.5;
		}
		// DEAD時は高速帰還
		if (this.currentState == Characters.EnemyState.DEAD) {
			currentSpeed = this.getSpeed() * 2;
		}

		// タイルの中心に近づいたか判定
		boolean atCenter = Math.abs(this.x - cx) < currentSpeed && Math.abs(this.y - cy) < currentSpeed;

		// 完全に停止している(NONE)か、マスの中心に到達したら方向転換
		if (this.direction == Direction.NONE || atCenter) {
			List<Direction> validDirections = getValidDirections(map);

			if (!validDirections.isEmpty()) {
				Direction chosenDirection = decideNextDirection(validDirections, map, this.mapData);

				// 中心にぴったり位置補正（軸ズレによるスタック防止）
				this.x = cx;
				this.y = cy;
				this.direction = chosenDirection;
			} else {
				this.direction = Direction.NONE;
			}
		}

		// 決定した方向に実際に移動する処理
		if (this.direction != Direction.NONE) {
			this.x += this.direction.getDX() * currentSpeed;
			this.y += this.direction.getDY() * currentSpeed;

			if (this.direction.getDX() != 0) {
				this.y += (cy - this.y) * 0.2;
			}
			if (this.direction.getDY() != 0) {
				this.x += (cx - this.x) * 0.2;
			}
		}
	}

	// DEAD・FEVERの共通処理
	protected Direction handleSpecialState(List<Direction> validDirections, int targetCol, int targetRow) {

		// DEADなら巣へ帰る
		if (currentState == Characters.EnemyState.DEAD) {
			return getClosestDirection(validDirections, 14, 14);
		}

		// FEVERなら仙石さんから逃げる
		if (currentState == Characters.EnemyState.FEVER) {
			return getFarthestDirection(validDirections, targetCol, targetRow);
		}

		return null;
	}

	// 三平方の定理を使って目的地に一番近い方向を選ぶ共通処理
	protected Direction getClosestDirection(List<Direction> validDirections, int targetCol, int targetRow) {

		Direction bestDirection = Direction.NONE;
		double minDistance = Double.MAX_VALUE;

		int currentCol = (int) (this.x / MapData.TILE_SIZE);
		int currentRow = (int) (this.y / MapData.TILE_SIZE);

		for (Direction dir : validDirections) {
			int nextCol = currentCol + (int) dir.getDX();
			int nextRow = currentRow + (int) dir.getDY();

			double distanceSq = Math.pow(nextCol - targetCol, 2) + Math.pow(nextRow - targetRow, 2);

			if (distanceSq < minDistance) {
				minDistance = distanceSq;
				bestDirection = dir;
			}
		}
		return bestDirection != Direction.NONE ? bestDirection : validDirections.get(0);
	}

	protected Direction getFarthestDirection(List<Direction> validDirections, int targetCol, int targetRow) {

		Direction bestDirection = Direction.NONE;
		double maxDistance = -1;

		int currentCol = (int) (this.x / MapData.TILE_SIZE);
		int currentRow = (int) (this.y / MapData.TILE_SIZE);

		for (Direction dir : validDirections) {

			int nextCol = currentCol + (int) dir.getDX();
			int nextRow = currentRow + (int) dir.getDY();

			double distanceSq = Math.pow(nextCol - targetCol, 2) + Math.pow(nextRow - targetRow, 2);

			if (distanceSq > maxDistance) {
				maxDistance = distanceSq;
				bestDirection = dir;
			}
		}

		return bestDirection != Direction.NONE ? bestDirection : validDirections.get(0);
	}

	private boolean isOppositeDirection(Direction dir1, Direction dir2) {
		if (dir1 == Direction.NONE || dir2 == Direction.NONE)
			return false;
		return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
	}

	private List<Direction> getValidDirections(int[][] map) {
		List<Direction> list = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			if (dir == Direction.NONE)
				continue;

			// 常にUターン禁止
			if (isOppositeDirection(dir, this.direction)) {
				continue;
			}

			if (canmove(dir, map)) {
				list.add(dir);
			}
		}
		return list;
	}

	private boolean canmove(Direction direction, int[][] map) {
		if (direction == Direction.NONE)
			return false;

		int currentCol = (int) (this.x / MapData.TILE_SIZE);
		int currentRow = (int) (this.y / MapData.TILE_SIZE);

		int nextCol = currentCol + (int) direction.getDX();
		int nextRow = currentRow + (int) direction.getDY();

		if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
			return false;
		}

		// ゴーストの巣への通常侵入禁止ルール
		if (this.currentState != Characters.EnemyState.DEAD) {
			if (nextRow >= 13 && nextRow <= 15 && nextCol >= 12 && nextCol <= 15) {
				return false;
			}
		}

		return map[nextRow][nextCol] != 1; // 1は壁
	}

	// FEVER状態で使用する画像をステージごとに読み込む
	protected void loadFeverImage() {

		// デフォルトはステージ1
		String feverPath = "/picture/narita_EnemyFever.png";

		// 現在のステージ番号に応じて画像を切り替える
		if (mapData != null) {

			switch (mapData.getStageNumber()) {

			case 1:
				feverPath = "/picture/narita_EnemyFever.png";
				break;

			case 2:
				feverPath = "/picture/wada_EnemyFever.png";
				break;

			case 3:
				feverPath = "/picture/hayakawa_EnemyFever.png";
				break;
			}
		}

		try {

			java.io.InputStream is = getClass().getResourceAsStream(feverPath);

			if (is != null) {
				feverImage = new javafx.scene.image.Image(is);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public javafx.scene.image.Image getEnemyImage() {

		if (currentState == Characters.EnemyState.DEAD) {
			return deadImage;
		}

		if (currentState == Characters.EnemyState.FEVER) {
			return feverImage;
		}

		return normalImage;
	}

	public Characters.EnemyState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Characters.EnemyState state) {
		this.currentState = state;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}