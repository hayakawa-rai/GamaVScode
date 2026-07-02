//敵キャラクターの共通処理を管理する抽象クラス
//移動処理、状態管理（SCATTER、FEVER、DEAD）、画像管理、巣への帰還処理

package Characters;

import java.util.ArrayList;
import java.util.List;

import common.GameConfig;
import common.GameMap;

public abstract class Enemy extends Character {

	// 敵キャラクターの画像表示用
	protected javafx.scene.image.ImageView imageView;

	// 現在のステージ情報
	protected GameMap mapData;

	// 現在の敵の状態（通常・FEVER・DEAD）
	protected Characters.EnemyState currentState = Characters.EnemyState.SCATTER;

	// 通常時の画像
	protected javafx.scene.image.Image normalImage;

	// FEVER状態時の画像
	protected javafx.scene.image.Image feverImage;

	// DEAD状態時の画像
	protected javafx.scene.image.Image deadImage;

	// ポーズ時間
	protected long pauseStartTime = 0;

	// 敵の初期位置（リスポーン用）
	protected final double startX;
	protected final double startY;

	// 各エネミーの初期設定
	public Enemy(double startX, double startY, int speed) {
		super(startX, startY, speed);

		// 初期位置を保存
		this.startX = startX;
		this.startY = startY;
	}

	// ポーズ開始時間を記録する、出撃タイマーや状態タイマー停止用
	public void pauseTimer() {
		pauseStartTime = System.currentTimeMillis();
	}

	// ポーズ解除時の処理、必要に応じてタイマー補正を行
	public void resumeTimer() {
	}

	// 敵ごとのAIで次の進行方向を決定する
	protected abstract Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData);

	@Override
	public void move(int[][] map) {
		// 現在位置をタイル座標へ変換
		int tileX = (int) (this.x / GameConfig.TILE_SIZE);
		int tileY = (int) (this.y / GameConfig.TILE_SIZE);

		// 範囲外防止
		if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
			return;
		}

		// 現在いるタイルの中心座標を計算
		double cx = tileX * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0;
		double cy = tileY * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0;

		// 現在のマス情報を取得
		int currentTileType = map[tileY][tileX];

		// DEAD状態で巣の床(8)に戻ったら復活
		if (currentState == Characters.EnemyState.DEAD) {
			if (currentTileType == 8) {
				currentState = Characters.EnemyState.SCATTER;
				System.out.println(getClass().getSimpleName() + "が巣に帰還し、復活しました");
			}
		}

		// 現在のスピードの計算
		double currentSpeed = this.getSpeed();

		// FEVER時は減速
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

			// 現在進行可能な方向を取得
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

					// 敵固有AIで次の移動方向を決定
					Direction chosenDirection = decideNextDirection(validDirections, map, this.mapData);

					// 中心にぴったり位置補正（軸ズレによるスタック防止）
					this.x = cx;
					this.y = cy;
					this.direction = chosenDirection;
				}
			} else {
				// 進行できる方向が無い場合は停止
				this.direction = Direction.NONE;
			}
		}

		// 決定した方向に実際に移動する処理
		if (this.direction != Direction.NONE) {

			this.x += this.direction.getDX() * currentSpeed;
			this.y += this.direction.getDY() * currentSpeed;

			// 横移動時はY座標を中心へ補正
			if (this.direction.getDX() != 0) {
				this.y += (cy - this.y) * 0.2;
			}

			// 縦移動時はX座標を中心へ補正
			if (this.direction.getDY() != 0) {
				this.x += (cx - this.x) * 0.2;
			}
		}
	}

	// DEAD・FEVERの共通処理移動処理
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

	// 目標地点から最も遠ざかる方向を選択する
	protected Direction getFarthestDirection(List<Direction> validDirections, int targetCol, int targetRow) {

		// 最終的に選択する方向
		Direction bestDirection = Direction.NONE;

		// 最大距離を記録する変数
		double maxDistance = -1;

		// 現在のタイル座標を取得
		int currentCol = (int) (this.x / GameConfig.TILE_SIZE);
		int currentRow = (int) (this.y / GameConfig.TILE_SIZE);

		// 移動可能な全方向を調べる
		for (Direction dir : validDirections) {

			// 1マス移動後の座標を計算
			int nextCol = currentCol + (int) dir.getDX();
			int nextRow = currentRow + (int) dir.getDY();

			// 目標地点までの距離の二乗を計算
			double distanceSq = Math.pow(nextCol - targetCol, 2) + Math.pow(nextRow - targetRow, 2);

			// 今までで最も遠くなる方向なら更新
			if (distanceSq > maxDistance) {
				maxDistance = distanceSq;
				bestDirection = dir;
			}
		}
		// 選択された方向を返す
		// 万が一見つからなければ先頭の方向を返す
		return bestDirection != Direction.NONE ? bestDirection : validDirections.get(0);
	}

	// 2つの方向が互いに反対方向（Uターン方向）か判定する
	private boolean isOppositeDirection(Direction dir1, Direction dir2) {

		// NONE は反対方向として扱わない
		if (dir1 == Direction.NONE || dir2 == Direction.NONE)
			return false;

		// ベクトルの和が(0,0)なら反対方向
		return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
	}

	// 現在位置から移動可能な方向一覧を取得する
	private List<Direction> getValidDirections(int[][] map) {

		// 移動可能な方向を格納するリスト
		List<Direction> list = new ArrayList<>();

		// 全方向をチェック
		for (Direction dir : Direction.values()) {
			// NONE は判定対象外
			if (dir == Direction.NONE)
				continue;
			// 常にUターン禁止
			if (isOppositeDirection(dir, this.direction))
				continue;
			// 実際に移動可能なら候補に追加
			if (canmove(dir, map)) {
				list.add(dir);
			}
		}
		return list;

	}

	// 指定した方向へ移動できるか判定する
	private boolean canmove(Direction direction, int[][] map) {

		// 移動しない場合は不可
		if (direction == Direction.NONE)
			return false;
		// 現在位置のタイル座標を取得
		int currentCol = (int) (this.x / GameConfig.TILE_SIZE);
		int currentRow = (int) (this.y / GameConfig.TILE_SIZE);

		// 移動先のタイル座標を計算
		int nextCol = currentCol + (int) direction.getDX();
		int nextRow = currentRow + (int) direction.getDY();

		// マップ範囲外は移動不可
		if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
			return false;
		}
		// 壁(1)には進めない
		if (map[nextRow][nextCol] == 1) {
			return false;
		}

		// 現在地と移動先のマス情報を取得
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
		String feverPath = "/picture/nari_EnemyFever.png";

		// 現在のステージ番号に応じて画像を切り替える
		if (mapData != null) {
			switch (mapData.getStageNumber()) {
			case 1:
				feverPath = "/picture/nari_EnemyFever.png";
				break;

			case 2:
				feverPath = "/picture/taku_EnemyFever.png";
				break;

			case 3:
				feverPath = "/picture/aniki_EnemyFever.png";
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
		String deadPath = "/picture/nari_EnemyDead.png";

		// 現在のステージ番号に応じて画像を切り替える
		if (mapData != null) {
			switch (mapData.getStageNumber()) {
			case 1:
				deadPath = "/picture/nari_EnemyDead.png";
				break;

			case 2:
				deadPath = "/picture/taku_EnemyDead.png";
				break;

			case 3:
				deadPath = "/picture/aniki_EnemyDead.png";
				break;
			}
		}

		try {
			// リソースから画像を読み込む
			java.io.InputStream is = getClass().getResourceAsStream(deadPath);
			// 読み込み成功
			if (is != null) {
				deadImage = new javafx.scene.image.Image(is);
				System.out.println("DEAD画像読込成功: " + deadPath);
			} else {
				// 読み込み失敗
				System.err.println("DEAD画像が見つかりません: " + deadPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ---getter---

// 現在の状態に対応した画像を返す
	public javafx.scene.image.Image getEnemyImage() {

		// 撃破状態
		if (currentState == Characters.EnemyState.DEAD) {
			return deadImage;
		}

		// FEVER状態
		if (currentState == Characters.EnemyState.FEVER) {
			return feverImage;
		}

		// 通常状態
		return normalImage;
	}

	// 現在の敵の状態を取得する
	public Characters.EnemyState getCurrentState() {
		return currentState;
	}

	// 現在のX座標を取得する
	public double getX() {
		return x;
	}

	// 現在のY座標を取得する
	public double getY() {
		return y;
	}

	// ---setter---
	//　敵の状態を変更する

	public void setCurrentState(Characters.EnemyState state) {
		this.currentState = state;
	}

	// プレイヤーが被弾時に元の場所、出撃時間をリセット、状態を通常状態(SCATTER)へ戻す
	public void resetToStartPosition() {
		this.x = startX;
		this.y = startY;
		this.direction = Direction.NONE;
		this.currentState = Characters.EnemyState.SCATTER;
	}

}
