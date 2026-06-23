package Character;

public abstract class Character {

	protected double x; // 現在のX座標(左右移動)
	protected double y; // 現在のY座標（上下位置）
	protected int speed; // 移動速度
	protected Direction direction = Direction.NONE; // キャラクターが現在向いている方向


	// 追加のコンストラクタ：速さデフォルトを 1 にして、Sengoku の呼び出しと互換性を持たせる
	public Character(int x, int y, int speed) {
		this.x = x;
	    this.y = y;
	    this.speed = speed;
	}
	
	public void setDirection(Direction direction) {
	    this.direction = direction;
	}

	public double getX() {  //子クラスからx座標の情報をGET
		return x;
	}

	public double getY() {  //子クラスからy座標の情報をGET
		return y;
	}

	public int getSpeed() {  //子クラスから速度の情報をGET
		return speed;
	}

	public Direction getDirection() {  //子クラスから向いている方向の情報をGET
		return direction;
	}

	// キャラクターを移動させる処理
	public abstract void move(int[][] map);
}