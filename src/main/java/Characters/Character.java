package Characters;

public abstract class Character {
	// 現在のX座標（左右位置）
	protected double x;
	// 現在のY座標（上下位置）
	protected double y;
	// 移動速度
	protected int speed;
	// キャラクターが現在向いている方向
	protected Direction direction;
	
	// ==================================================
	// コンストラクタ
	// ==================================================
	// 速さデフォルトを 1 にして、syujinkou の呼び出しと互換性を持たせる
	public Character(double x, double y) {
		this(x, y, 1);
	}
	
	public Character(double x, double y, int speed) {
		// 初期X座標
		this.x = x;
		// 初期Y座標
		this.y = y;
		// 初期移動速度
		this.speed = speed;
		// 初期状態では停止
		this.direction = Direction.NONE;
	}
	
	// ==================================================
	// 動き
	// ==================================================
	// キャラクターを移動させる処理
	public abstract void move(int[][] map);

	//==================================================
	//getter
	//==================================================
	// 子クラスからx座標の情報を取得
	public double getX() {
		return x;
	}
	
	// 子クラスからy座標の情報を取得
	public double getY() {
		return y;
	}

	// 子クラスから速度の情報を取得
	public int getSpeed() {
		return speed;
	}

	// 子クラスから向いている方向の情報を取得
	public Direction getDirection() {
		return direction;
	}

}