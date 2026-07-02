package Characters;

public class Sengoku extends Character {

	private int hp = 3;
	private int score = 0;
	private boolean isAlive = true;
	private boolean fever = false;

	private Direction nextdirection = Direction.NONE;
	private static final int CELL_SIZE = 30;

	// ミス演出のための変数
	private final double startX;
	private final double startY;
	private boolean isDyingAnimation = false;
	private int dyingTimer = 0;

	public Sengoku(double x, double y, int speed) {
		super(x, y, speed);
		this.startX = x;
		this.startY = y;
	}

	public void setNextDirection(Direction direction) {
		this.nextdirection = direction;
		if (this.direction == Direction.NONE && !isDyingAnimation) {
			this.direction = direction;
		}
	}

	@Override
	public void move(int[][] map) {

		System.out.println("x=" + this.x + " y=" + this.y + " dir=" + this.direction + " next=" + this.nextdirection
				+ " aligned=" + isAligned() + " canMoveNext=" + canmove(nextdirection, map) + " canMoveCur="
				+ canmove(this.direction, map));

		// 死亡アニメーション中、または死亡時は移動しない
		if (isDyingAnimation || !isAlive())
			return;

		// 新しい方向に曲がれるか（マス境界付近のみ許可）
		if (isAligned() && canmove(nextdirection, map)) {
			this.direction = nextdirection;
			// 曲がる瞬間にマス境界にスナップ
			this.x = Math.round(this.x / CELL_SIZE) * CELL_SIZE;
			this.y = Math.round(this.y / CELL_SIZE) * CELL_SIZE;
		}

		// 現在の方向が壁なら停止
		if (!canmove(this.direction, map)) {
			this.direction = Direction.NONE;
		}

		// 移動
		if (this.direction != Direction.NONE) {
			this.x += this.direction.getDX() * this.speed;
			this.y += this.direction.getDY() * this.speed;
		}
	}

	// 外部から状態をチェックするためのゲッタ-
	public boolean isDyingAnimation() {
		return isDyingAnimation;
	}

	public int getDyingTimer() {
		return dyingTimer;
	}

	// マス境界に揃っているか（曲がれるタイミング）
	private boolean isAligned() {
		return (this.x % CELL_SIZE == 0) && (this.y % CELL_SIZE == 0);
	}

	private boolean canmove(Direction direction, int[][] map) {
		if (direction == Direction.NONE) {
			return false;
		}

		// 進行方向の「先端座標」を計算する
		double checkX = this.x;
		double checkY = this.y;

		if (direction == Direction.RIGHT) {
			
			// 右端 + speed分先
			checkX = this.x + CELL_SIZE - 1 + this.speed;
		} else if (direction == Direction.LEFT) {
			
			// 左端 - speed分先
			checkX = this.x - this.speed;
		} else if (direction == Direction.DOWN) {
			
			// 下端 + speed分先
			checkY = this.y + CELL_SIZE - 1 + this.speed;
		} else if (direction == Direction.UP) {
			
			// 上端 - speed分先
			checkY = this.y - this.speed;
		}

		int checkCol = (int) (checkX / CELL_SIZE);
		int checkRow = (int) (checkY / CELL_SIZE);

		// マップ範囲外チェック
		if (checkRow < 0 || checkRow >= map.length || checkCol < 0 || checkCol >= map[0].length) {
			return false;
		}
		return map[checkRow][checkCol] != 1;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// ミスが起きたときにアニメーションを開始する
	public void startDying() {
		
		// テスト用
	    System.out.println("死亡アニメーション開始");
		
		this.isDyingAnimation = true;
		this.dyingTimer = 0;
		this.direction = Direction.NONE;
		this.nextdirection = Direction.NONE;
	}

	// 死亡アニメーションの更新
	public boolean updateDyingAnimation() {

	    if (!isDyingAnimation)
	        return false;

	    dyingTimer++;

	  //テスト用
	    System.out.println("dyingTimer=" + dyingTimer);

	    if (dyingTimer < 60) {
	        return false;
	    }

	    isDyingAnimation = false;

	    return true;
	}

	// 初期位置に戻すリセットメソッド
	public void resetToStartPosition() {
		this.x = this.startX;
		this.y = this.startY;
		this.direction = Direction.NONE;
		this.nextdirection = Direction.NONE;
		this.isDyingAnimation = false;
		this.dyingTimer = 0;
	}

	// 残機を1つ減らすメソッド
	public void decreaseHp() {
		if (this.hp > 0) {
			this.hp--;
			if (this.hp <= 0) {
				this.isAlive = false;
			}
		}
	}

	public void die() {
		this.hp = 0;
		this.direction = Direction.NONE;
	}

	public void takeDamage() {
		if (hp > 0) {
			hp--;

			if (hp == 0) {
				die();
			}
		}
	}

	//死んだときのアニメーション
	public double getDyingProgress() {
		return Math.min(1.0, dyingTimer / 60.0);
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

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	// 呼び出し側の「大文字・小文字のブレ」を吸収するためのメソッド
	public void setnextDirection(Direction direction) {
		this.setNextDirection(direction);
	}

	public void setnextdirection(Direction direction) {
		this.setNextDirection(direction);
	}

	public boolean isFever() {
		return fever;
	}

	public void setFever(boolean fever) {
		this.fever = fever;
	}
}
