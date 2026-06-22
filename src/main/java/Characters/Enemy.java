
/*
package Characters;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public abstract class Enemy extends Character {

	protected ImageView imageView;
	protected static final int CELL_SIZE = 24;//1マスの大きさ
	
	//Sengokuをフィールドとして保持
	protected Sengoku player;

	//[4つのモード]初期状態の「縄張りモード」からスタートする
	protected EnemyState currentState = EnemyState.SCATTER;

	//状態ごとの画像(こんな感じで書く(多分))
	protected javafx.scene.image.Image normalImage; // 通常状態敵
	protected javafx.scene.image.Image feverImage; // パワーアイテムを取得して逃げてる敵
	protected javafx.scene.image.Image deadImage; // 食べられて初期地点に戻る敵

	//コンストラクタ
	public Enemy(ImageView imageView, double startX, double startY, int speed,Sengoku player) {
		super(startX, startY, speed);
		this.imageView = imageView;
		this.player = player;
		//初期位置をImageViewに反映
		this.imageView.setLayoutX(startX);
		this.imageView.setLayoutY(startY);
	}


	//全ての敵に共通する物理移動のルール
	@Override
	public void move(int[][] map) {
		double centerX = this.getX() + CELL_SIZE / 2.0;
		double centerY = this.getY() + CELL_SIZE / 2.0;

		int currentMeshX = (int) centerX % CELL_SIZE;
		int currentMeshY = (int) centerY % CELL_SIZE;
		int centerOffset = CELL_SIZE / 2;

		//マスの中心に来たら、次の進行方向を決める
		if (Math.abs(currentMeshX - centerOffset) < this.getSpeed()
				&& Math.abs(currentMeshY - centerOffset) < this.getSpeed()) {

			//上下左右の中で、物理的に進める方向(壁がなく、Uターンではない方向)をリストアップ
			List<Direction> validDirections = getValidDirections(map);

			if (!validDirections.isEmpty()) {
				//具体的に進む方向は,4つのモードとプレイヤー情報を使って子クラスのAIに選ばせる
				Direction chosenDirection = decideNextDirection(validDirections, map, player);

				//位置補正

				int col = (int) (centerX / CELL_SIZE);
				int row = (int) (centerY / CELL_SIZE);
				this.x = col * CELL_SIZE;
				this.y = row * CELL_SIZE;

				this.direction = chosenDirection;
			}
		}

		//  実際の移動処理（スピード決定）
		int currentSpeed = this.getSpeed();

		// 【死亡状態（DEAD）】のときは、巣に戻るために爆速（例: 普段の2倍）にする
		if (this.currentState == EnemyState.DEAD) {
			currentSpeed = this.getSpeed() * 2;
		}

		// 移動継続チェックを行って座標を更新
		if (canmovego(this.direction, map)) {
			this.x += this.direction.getDX() * currentSpeed;
			this.y += this.direction.getDY() * currentSpeed;
		} else {
			// 万が一壁にぶつかった場合の安全停止
			int col = (int) ((this.getX() + CELL_SIZE / 2.0) / CELL_SIZE);
			int row = (int) ((this.getY() + CELL_SIZE / 2.0) / CELL_SIZE);
			this.x = col * CELL_SIZE;
			this.y = row * CELL_SIZE;
			this.direction = Direction.NONE;
		}
		//計算した内部座標をJavaFXのImageView（画面上の見た目）に同期
		this.imageView.setLayoutX(this.getX());
		this.imageView.setLayoutY(this.getY());

	}

	// 【抽象メソッド】4つの子クラス（各ゴーストのAI）がそれぞれの意思決定を記述する部分

	protected abstract Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player);

	//【すべての敵共通】三平方の定理（直線距離の2乗）を使って、目的地に一番近い方向を1つ選ぶ

	protected Direction getClosestDirection(List<Direction> validDirections, int targetCol, int targetRow) {
		Direction bestDirection = Direction.NONE;
		double minDistance = Double.MAX_VALUE;

		// 現在の敵のマス
		int currentCol = (int) ((this.getX() + CELL_SIZE / 2.0) / CELL_SIZE);
		int currentRow = (int) ((this.getY() + CELL_SIZE / 2.0) / CELL_SIZE);

		for (Direction dir : validDirections) {
			int nextCol = currentCol + (int) dir.getDX();
			int nextRow = currentRow + (int) dir.getDY();

			// 三平方の定理： (x1 - x2)^2 + (y1 - y2)^2
			double distanceSq = Math.pow(nextCol - targetCol, 2) + Math.pow(nextRow - targetRow, 2);

			if (distanceSq < minDistance) {
				minDistance = distanceSq;
				bestDirection = dir;
			}
		}
		// ベストな方向を返す（万が一なければ候補の最初の方向）
		return bestDirection != Direction.NONE ? bestDirection : validDirections.get(0);
	}

	// --- 状態管理用のゲッターとセッター ---
	public EnemyState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(EnemyState state) {
		if(this.currentState != state) {
		this.currentState = state;
		//現在のモードに合わせて自動で画像を切り替える
		updateImage();
		}
	}

	// 共通：真逆の方向（Uターン）チェック
	private boolean isOppositeDirection(Direction dir1, Direction dir2) {
		if (dir1 == Direction.NONE || dir2 == Direction.NONE)
			return false;
		return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
	}

	// 共通：物理的に進める方向のリストアップ
	private List<Direction> getValidDirections(int[][] map) {
		List<Direction> list = new ArrayList<>();
		for (Direction dir : Direction.values()) {
			if (dir == Direction.NONE)
				continue;

			//死亡状態(DEAD)の時以外は、真後ろへのUターン移動は禁止
			if (this.currentState != EnemyState.DEAD && isOppositeDirection(dir, this.direction)) {
				continue;
			}
			//1マス先が壁じゃなければ候補に入れる
			if (canmove(dir, map)) {
				list.add(dir);
			}
		}
		return list;
	}

	//ドット（px）単位での移動継続チェック
	private boolean canmovego(Direction direction, int[][] map) {
		if (direction == Direction.NONE)
			return false;

		double centerX = this.getX() + CELL_SIZE / 2.0;
		double centerY = this.getY() + CELL_SIZE / 2.0;

		int currentMeshX = (int) centerX % CELL_SIZE;
		int currentMeshY = (int) centerY % CELL_SIZE;
		int centerOffset = CELL_SIZE / 2;

		if (Math.abs(currentMeshX - centerOffset) < this.getSpeed()
				&& Math.abs(currentMeshY - centerOffset) < this.getSpeed()) {
			return canmove(direction, map);
		}
		return true;
	}

	//1マス先の壁チェック
	private boolean canmove(Direction direction, int[][] map) {
		if (direction == Direction.NONE)
			return false;

		int currentCol = (int) ((this.getX() + CELL_SIZE / 2.0) / CELL_SIZE);
		int currentRow = (int) ((this.getY() + CELL_SIZE / 2.0) / CELL_SIZE);

		int nextCol = currentCol + (int) direction.getDX();
		int nextRow = currentRow + (int) direction.getDY();

		// 通常の画面外チェック（縦31マス：map.length, 横28マス：map[0].length）
		if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
			return false;
		}

		return map[nextRow][nextCol] != 1; // 1は壁
	}

	// 現在の状態（currentState）に合わせて、ImageViewの画像を切り替える処理

	private void updateImage() {
		if (this.currentState == EnemyState.DEAD) {
			if (deadImage != null)
				this.imageView.setImage(deadImage);
		} else if (this.currentState == EnemyState.FEVER) {
			if (feverImage != null)
				this.imageView.setImage(feverImage);
		} else {
			// SCATTER（縄張り） や CHASE（追跡） などの通常時は通常画像
			if (normalImage != null)
				this.imageView.setImage(normalImage);
		}
	}

	public ImageView getImageView() {
		return imageView;
	}
}
*/