package test;
/*
public class Sengoku extends Character {

	private int hp = 3;
	private int score = 0;
	private boolean isAlive = true;
	private Direction nextdirection = Direction.NONE;
	private static final int CELL_SIZE = 24;

	public Sengoku(double x, double y, int speed) {
		super(x, y, speed);
	}

	public void setnextdirection(Direction direction) {
		this.nextdirection = direction;
	}

	@Override
	public void move(int[][] map) {
		
	    System.out.println("x=" + this.x + " y=" + this.y 
	            + " dir=" + this.direction 
	            + " next=" + this.nextdirection
	            + " aligned=" + isAligned()
	            + " canMoveNext=" + canmove(nextdirection, map)
	            + " canMoveCur=" + canmove(this.direction, map));
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
	        checkX = this.x + CELL_SIZE - 1 + this.speed; // 右端 + speed分先
	    } else if (direction == Direction.LEFT) {
	        checkX = this.x - this.speed;                  // 左端 - speed分先
	    } else if (direction == Direction.DOWN) {
	        checkY = this.y + CELL_SIZE - 1 + this.speed; // 下端 + speed分先
	    } else if (direction == Direction.UP) {
	        checkY = this.y - this.speed;                  // 上端 - speed分先
	    }

	    int checkCol = (int) (checkX / CELL_SIZE);
	    int checkRow = (int) (checkY / CELL_SIZE);

	    // マップ範囲外チェック
	    if (checkRow < 0 || checkRow >= map.length || checkCol < 0 || checkCol >= map[0].length) {
	        return false;
	    }

	    return map[checkRow][checkCol] != 1;
	}

	protected void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
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

}*/
