package Characters;

public class Sengoku extends Character {

	private int hp = 3;
	private int score = 0;
	private boolean isAlive = true;
	private Direction nextdirection = Direction.NONE;
	private static final int CELL_SIZE = 30;

	//初期位置を記憶する変数
	private double startX = -1;
	private double startY = -1;
	//本家風ミス演出用の変数
	private boolean isDyingAnimation = false; // 死亡アニメーション中か？
	private int dyingTimer = 0; // アニメーション用タイマー

	public Sengoku(double x, double y, int speed) {
		super(x, y, speed);
		this.startX = x;
		this.startY = y;
	}

	public void setnextdirection(Direction direction) {
		this.nextdirection = direction;

		//もし今止まっている（待機状態など）なら、押された方向に即座に動き出させる
		if (this.direction == Direction.NONE && !isDyingAnimation) {
			this.direction = direction;
		}
	}

	//ミスが起きたときにアニメーションを開始する
	public void startDying() {
		this.isDyingAnimation = true;
		this.dyingTimer = 0;
		this.direction = Direction.NONE;
		this.nextdirection = Direction.NONE;
	}

	//死亡アニメーションの更新（毎フレーム呼ばれる
	// 戻り値が true になったらアニメーション終了の合図
	public boolean updateDyingAnimation() {
		if (!isDyingAnimation)
			return false;

		dyingTimer++;

		//約1秒経過したらアニメーションを終了し、実際に残機を減らす
		if (dyingTimer >= 60) {
			isDyingAnimation = false;
			decreaseHp();
			return true;
		}
		return false;
	}

	// --- 【追加】初期位置に戻すリセットメソッド ---
	public void resetToStartPosition() {

		if (startX == -1 || startY == -1) {
			this.startX = this.x;
			this.startY = this.y;
		}
		this.x = this.startX;
		this.y = this.startY;
		this.direction = Direction.NONE;
		this.nextdirection = Direction.NONE;
		this.isDyingAnimation = false;
		this.dyingTimer = 0;
	}

	// --- 【追加】残機を1つ減らすメソッド ---
	public void decreaseHp() {
		if (this.hp > 0) {
			this.hp--;
			if (this.hp <= 0) {
				this.isAlive = false;
				die();
			}
		}
	}

	@Override
	public void move(int[][] map) {
		// 【追加】死亡アニメーション中、または完全ゲームオーバー時は移動しない
		if (isDyingAnimation || !isAlive())
			return;

		double centerX = this.getX() + CELL_SIZE / 2.0;
		double centerY = this.getY() + CELL_SIZE / 2.0;

		int currentMeshX = (int) centerX % CELL_SIZE;
		int currentMeshY = (int) centerY % CELL_SIZE;
		int centerOffset = CELL_SIZE / 2;

		if (Math.abs(currentMeshX - centerOffset) < this.speed && Math.abs(currentMeshY - centerOffset) < this.speed) {
			if (isOppositeDirection(nextdirection, this.direction)) {
				this.direction = nextdirection;
			}
			if (canmove(nextdirection, map)) {
				int col = (int) (centerX / CELL_SIZE);
				int row = (int) (centerY / CELL_SIZE);
				this.x = col * CELL_SIZE;
				this.y = row * CELL_SIZE;

				this.direction = nextdirection;
			}
			if (canmovego(this.direction, map)) {
				this.x += this.direction.getDX() * this.speed;
				this.y += this.direction.getDY() * this.speed;
			} else {
				int col = (int) ((this.x + CELL_SIZE / 2.0) / CELL_SIZE);
				int row = (int) ((this.y + CELL_SIZE / 2.0) / CELL_SIZE);
				this.x = col * CELL_SIZE;
				this.y = row * CELL_SIZE;
				this.direction = Direction.NONE;
			}

		} else {
			this.x += this.direction.getDX() * this.speed;
			this.y += this.direction.getDY() * this.speed;
		}
	}

	private boolean isOppositeDirection(Direction dir1, Direction dir2) {
		if (dir1 == Direction.NONE || dir2 == Direction.NONE)
			return false;
		return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
	}

	private boolean canmovego(Direction direction, int[][] map) {
		if (direction == Direction.NONE)
			return false;
		double centerX = this.x + CELL_SIZE / 2.0;
		double centerY = this.y + CELL_SIZE / 2.0;

		int currentMeshX = (int) centerX % CELL_SIZE;
		int currentMeshY = (int) centerY % CELL_SIZE;
		int centerOffset = CELL_SIZE / 2;
		if (Math.abs(currentMeshX - centerOffset) < this.speed && Math.abs(currentMeshY - centerOffset) < this.speed) {
			return canmove(direction, map);
		}

		return true;
	}

	private boolean canmove(Direction direction, int[][] map) {
		if (direction == Direction.NONE) {
			return false;
		}
		int currentCol = (int) ((this.x + CELL_SIZE / 2.0) / CELL_SIZE);
		int currentRow = (int) ((this.y + CELL_SIZE / 2.0) / CELL_SIZE);

		int nextCol = currentCol + (int) direction.getDX();
		int nextRow = currentRow + (int) direction.getDY();

		if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
			return false;
		}
		return map[nextRow][nextCol] != 1;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void die() {
		this.hp = 0;
		this.direction = Direction.NONE;
		this.nextdirection = Direction.NONE;
	}

	public void takeDamage() {
		decreaseHp(); // 【変更】残機減少メソッドと連動
	}

	public void addScore(int point) {
		this.score += point;
	}

	public int getScore() {
		return this.score;
	}

	public int getHp() {
		return this.hp;
	}

	public boolean isAlive() {
		return this.hp > 0;
	}

	public boolean isDyingAnimation() {
		return isDyingAnimation;
	}

	public int getDyingTimer() {
		return dyingTimer;
	}
}
