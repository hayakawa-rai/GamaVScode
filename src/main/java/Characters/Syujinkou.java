//　主人公（仙石さん）クラス
//　プレイヤーの移動、残機管理、スコア管理、
// FEVER状態、死亡アニメーションの管理を行う

package Characters;

public class Syujinkou extends Character {

	// 残機
	private int hp = 3;

	// スコア
	private int score = 0;

	// 生存状態
	private boolean isAlive = true;

	// FEVER状態
	private boolean fever = false;

	// 次に進みたい方向（先行入力）
	private Direction nextdirection = Direction.NONE;

	// 1マスのサイズ
	private static final int CELL_SIZE = 30;

	// ===== 死亡アニメーション関連 =====

	// 初期位置
	private final double startX;
	private final double startY;

	// 死亡アニメーション中か
	private boolean isDyingAnimation = false;

	// アニメーション経過時間
	private int dyingTimer = 0;

	// コンストラクタ
	public Syujinkou(double x, double y, int speed) {
		super(x, y, speed);
		this.startX = x;
		this.startY = y;
	}

	// 次に進みたい方向を設定
	public void setNextDirection(Direction direction) {
		this.nextdirection = direction;

		// 停止中は即座に方向変更
		if (this.direction == Direction.NONE && !isDyingAnimation) {
			this.direction = direction;
		}
	}

	// プレイヤー移動処理
	@Override
	public void move(int[][] map) {

		System.out.println("x=" + this.x + " y=" + this.y + " dir=" + this.direction + " next=" + this.nextdirection
				+ " aligned=" + isAligned() + " canMoveNext=" + canmove(nextdirection, map) + " canMoveCur="
				+ canmove(this.direction, map));

		// 死亡中は移動禁止
		if (isDyingAnimation || !isAlive())
			return;

		// 曲がれる場合は方向転換
		if (isAligned() && canmove(nextdirection, map)) {
			this.direction = nextdirection;

			// マスの中心へ補正
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

	// //アニメーションタイマー取得
	public int getDyingTimer() {
		return dyingTimer;
	}

	// マス境界に揃っているか（曲がれるタイミング）
	private boolean isAligned() {
		return (this.x % CELL_SIZE == 0) && (this.y % CELL_SIZE == 0);
	}

	// 指定方向へ進めるか判定
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
		// 壁(1)以外なら通行可能
		return map[checkRow][checkCol] != 1;
	}

	// 座標を直接変更
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// ミスが起きたときに 死亡アニメーション開始
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

		// テスト用
		System.out.println("dyingTimer=" + dyingTimer);

		// 約60フレーム継続
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

	// 即死亡
	public void die() {
		this.hp = 0;
		this.direction = Direction.NONE;
	}

	// ダメージ処理
	public void takeDamage() {
		if (hp > 0) {
			hp--;

			if (hp == 0) {
				die();
			}
		}
	}

	// 死亡アニメーション進行率取得
	public double getDyingProgress() {
		return Math.min(1.0, dyingTimer / 60.0);
	}

	// スコア加算
	public void addScore(int point) {
		this.score += point;
	}

	// スコア取得
	public int getScore() {
		return this.score;
	}

	// 残機取得
	public int getHp() {
		return this.hp;
	}

	// 生存判定
	public boolean isAlive() {
		return this.hp > 0;
	}

	// X座標設定
	public void setX(double x) {
		this.x = x;
	}

	// Y座標設定
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

	// FEVER状態取得
	public boolean isFever() {
		return fever;
	}

	// FEVER状態設定
	public void setFever(boolean fever) {
		this.fever = fever;
	}
}
