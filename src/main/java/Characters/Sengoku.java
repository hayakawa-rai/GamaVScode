package Characters;

public class Sengoku extends Character {

	private int hp = 3;
	private int score = 0;
	private boolean isAlive = true;
	private Direction nextdirection = Direction.NONE;
	private static final int CELL_SIZE = 30;

	public Sengoku(double x, double y, int speed) {
		super(x, y, speed);
	}

	public void setnextdirection(Direction direction) {
		this.nextdirection = direction;
	}

	@Override
	public void move(int[][] map) {

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

		}else {
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

}
