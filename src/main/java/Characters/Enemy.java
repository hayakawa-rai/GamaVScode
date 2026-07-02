//エネミークラス
package Characters;

import java.util.ArrayList;
import java.util.List;

import common.GameConfig;
import common.GameMap;

public abstract class Enemy extends Character {

	protected javafx.scene.image.ImageView imageView;
	protected GameMap mapData;
	protected Characters.EnemyState currentState = Characters.EnemyState.SCATTER;

	protected javafx.scene.image.Image normalImage;
	protected javafx.scene.image.Image feverImage;
	protected javafx.scene.image.Image deadImage;

	protected final double startX;
	protected final double startY;

	public Enemy(double startX, double startY, int speed) {
		super(startX, startY, speed);

		this.startX = startX;
		this.startY = startY;
	}

	protected abstract Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData);

	@Override
	public void move(int[][] map) {
		int tileX = (int) (this.x / GameConfig.TILE_SIZE);
		int tileY = (int) (this.y / GameConfig.TILE_SIZE);

		// 範囲外防止
		if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
			return;
		}

		double cx = tileX * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0;
		double cy = tileY * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0;

		int currentTileType = map[tileY][tileX]; // 今いるマスの種類を取得

		// 修正：固定座標(14,14)ではなく、今いるマスが巣の床(8)なら自動で復活させる
		if (currentState == Characters.EnemyState.DEAD) {
			if (currentTileType == 8) {
				currentState = Characters.EnemyState.SCATTER;
				System.out.println(getClass().getSimpleName() + "が巣に帰還し、復活しました");
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
			currentSpeed = this.getSpeed() * 3;
		}

		// タイルの中心に近づいたか判定
		boolean atCenter = Math.abs(this.x - cx) < currentSpeed && Math.abs(this.y - cy) < currentSpeed;

		// 完全に停止している(NONE)か、マスの中心に到達したら方向転換
		if (this.direction == Direction.NONE || atCenter) {
			List<Direction> validDirections = getValidDirections(map);
			if (!validDirections.isEmpty()) {
				
				// 現在のタイル座標を一時的に取得（条件判定用）
				int currentRow = (int) (this.y / GameConfig.TILE_SIZE);
				int currentCol = (int) (this.x / GameConfig.TILE_SIZE);
				
				// 巣の中にいる間は、ターゲットを強制的に巣のすぐ外に移動する
				if (currentState != Characters.EnemyState.DEAD && currentRow >= 12 && currentRow <= 15
						&& currentCol >= 12 && currentCol <= 15) {
					this.y = cy;
					this.x = cx;
					this.direction = Direction.UP;
				} else {

					// 巣の外にいる時だけ、ターゲットを追いかける通常のAI処理を行う
					Direction chosenDirection = decideNextDirection(validDirections, map, this.mapData);

					// 中心にぴったり位置補正（軸ズレによるスタック防止）
					this.x = cx;
					this.y = cy;
					this.direction = chosenDirection;
				}

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
	protected Direction handleSpecialState(List<Direction> validDirections, int targetCol, int targetRow, int[][] map) {
		
		// DEAD状態なら、自動的にマップ内の「7（扉）」の中から【一番近い場所】を探してそこへ帰る
		if (currentState == Characters.EnemyState.DEAD) {
			int[][] currentMap = map;

			// デフォルトのバックアップ座標
			int bestGateCol = 14;
			int bestGateRow = 13;
			double minDistanceSq = Double.MAX_VALUE;

			// 今の自分の位置（マス単位）
			int myCol = (int) (this.x / GameConfig.TILE_SIZE);
			int myRow = (int) (this.y / GameConfig.TILE_SIZE);

			// マップ全体からすべての「7」を探し、一番近いものを選択する（outerLoopとbreakは削除）
			for (int r = 0; r < currentMap.length; r++) {
				for (int c = 0; c < currentMap[r].length; c++) {
					if (currentMap[r][c] == 7) {
						
						// 自分の現在地からの距離を計算（三平方の定理）
						double distSq = Math.pow(c - myCol, 2) + Math.pow(r - myRow, 2);
						if (distSq < minDistanceSq) {
							minDistanceSq = distSq;
							bestGateCol = c;
							bestGateRow = r;
						}
					}
				}
			}

			// 最も近い扉（右半分にいるときは右側の7、左半分にいるときは左側の7）に向かわせる
			return getClosestDirection(validDirections, bestGateCol, bestGateRow);
		}

		if (currentState == Characters.EnemyState.FEVER) {
			return getFarthestDirection(validDirections, targetCol, targetRow);
		}
		return null;
	}

	// 三平方の定理を使って目的地に一番近い方向を選ぶ共通処理
	protected Direction getClosestDirection(List<Direction> validDirections, int targetCol, int targetRow) {
		Direction bestDirection = Direction.NONE;
		double minDistance = Double.MAX_VALUE;
		int currentCol = (int) (this.x / GameConfig.TILE_SIZE);
		int currentRow = (int) (this.y / GameConfig.TILE_SIZE);

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
		int currentCol = (int) (this.x / GameConfig.TILE_SIZE);
		int currentRow = (int) (this.y / GameConfig.TILE_SIZE);

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
			if (isOppositeDirection(dir, this.direction))
				continue;
			if (canmove(dir, map)) {
				list.add(dir);
			}
		}
		return list;

	}

	private boolean canmove(Direction direction, int[][] map) {
		if (direction == Direction.NONE)
			return false;

		int currentCol = (int) (this.x / GameConfig.TILE_SIZE);
		int currentRow = (int) (this.y / GameConfig.TILE_SIZE);
		int nextCol = currentCol + (int) direction.getDX();
		int nextRow = currentRow + (int) direction.getDY();

		if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
			return false;
		}
		if (map[nextRow][nextCol] == 1) {
			return false;
		}

		int currentTileType = map[currentRow][currentCol];
		int nextTileType = map[nextRow][nextCol];

		// 通常状態の敵の「巣（扉含む）」への侵入制限
		if (this.currentState != Characters.EnemyState.DEAD) {
			// 外(8以外)から、扉(7)や床(8)に入ろうとしたら通行不可
			if (currentTileType != 8 && (nextTileType == 7 || nextTileType == 8)) {
				return false;
			}
		}

		return true;
	}

	// FEVER状態で使用する画像をステージごとに読み込む
	protected void loadFeverImage() {

		// デフォルト画像（ステージ1）
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
			
			// 指定したパスから画像を取得
			java.io.InputStream is = getClass().getResourceAsStream(feverPath);

			// 読み込み成功時
			if (is != null) {
				feverImage = new javafx.scene.image.Image(is);
				System.out.println("FEVER画像読込成功: " + feverPath);
			}

			// 読み込み失敗時
			else {
				System.err.println("FEVER画像が見つかりません: " + feverPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DEAD状態で使用する画像をステージごとに読み込む
	protected void loadDeadImage() {

		// デフォルトはステージ1
		String deadPath = "/picture/narita_EnemyDead.png";

		// 現在のステージ番号に応じて画像を切り替える
		if (mapData != null) {
			switch (mapData.getStageNumber()) {
			case 1:
				deadPath = "/picture/narita_EnemyDead.png";
				break;

			case 2:
				deadPath = "/picture/wada_EnemyDead.png";
				break;

			case 3:
				deadPath = "/picture/hayakawa_EnemyDead.png";
				break;
			}
		}

		// リソースからDEAD画像を読み込む
		try {
			java.io.InputStream is = getClass().getResourceAsStream(deadPath);
			if (is != null) {
				deadImage = new javafx.scene.image.Image(is);
				System.out.println("DEAD画像読込成功: " + deadPath);
			} else {
				System.err.println("DEAD画像が見つかりません: " + deadPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ---getter---
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

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	// ---setter---
	public void setCurrentState(Characters.EnemyState state) {
		this.currentState = state;
	}
	
	//プレイヤーが被弾時に元の場所、出撃時間をリセット
	public void resetToStartPosition() {
		this.x = startX;
		this.y = startY;
		this.direction = Direction.NONE;
		this.currentState = Characters.EnemyState.SCATTER;
	}

}
