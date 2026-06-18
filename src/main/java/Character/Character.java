package Character;

public abstract class Character {

	private int x; // 現在のX座標(左右移動)
	private int y; // 現在のY座標（上下位置）
	private int speed; // 移動速度
	private Direction direction; // キャラクターが現在向いている方向

	public Character(int x, int y, int speed) {
		this.x = x; // 初期X座標
		this.y = y; // 初期Y座標
		this.speed = speed; // 初期移動速度
		this.direction = Direction.NONE; // 初期状態では停止
	}

	public int getX() {  //子クラスからx座標の情報をGET
		return x;
	}

	public int getY() {  //子クラスからy座標の情報をGET
		return y;
	}

	public int getSpeed() {  //子クラスから速度の情報をGET
		return speed;
	}

	public Direction getDirection() {  //子クラスから向いている方向の情報をGET
		return direction;
	}

	// キャラクターを移動させる処理
	public abstract void move();
}