package sample;

public abstract class Character {

	protected double x; // 現在のX座標(左右移動)
	protected double y; // 現在のY座標（上下位置）
	protected int speed; // 移動速度
	protected Direction direction; // キャラクターが現在向いている方向

	public Character(double x, double y, int speed) {
		this.x = x; // 初期X座標
		this.y = y; // 初期Y座標
		this.speed = speed; // 初期移動速度
		this.direction = Direction.NONE; // 初期状態では停止
	}

	// 追加のコンストラクタ：速さデフォルトを 1 にして、Sengoku の呼び出しと互換性を持たせる
	public Character(double x, double y) {
		this(x, y, 1);
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