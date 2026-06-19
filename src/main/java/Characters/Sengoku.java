package Characters;

public class Sengoku extends Character {

	// 基本ステータス
	private int hp = 3; // 体力(0になると死亡)
	private int score = 0; // スコア
	private boolean isAlive = true; //生存判定
	//private final double rispornx; //スタート地点x
	//private final double risporny; //スタート地点y
	private Direction nextdirection = Direction.NONE; //入力された次に進みたい方向
	private static final int CELL_SIZE = 30; //1マスの大きさ(px)

	// コンストラクタ
	public Sengoku(double x, double y, int speed) {
		super(x, y, speed); // Character の位置情報を初期化
	}

	//入力方向を保存
	public void setnextdirection(Direction direction) {
		this.nextdirection = direction;
	}

	@Override
	public void move(int[][] map) {

		// キャラクターの中心点を計算
		double centerX = this.getX() + CELL_SIZE / 2.0;
		double centerY = this.getY() + CELL_SIZE / 2.0;

		//現在マスの中心からのズレ
		int currentMeshX = (int) centerX % CELL_SIZE;
		int currentMeshY = (int) centerY % CELL_SIZE;
		int centerOffset = CELL_SIZE / 2;

		//方向転換(マスの中心に来たら先行入力の方向に曲がる)
		if (Math.abs(currentMeshX - centerOffset) < this.speed && Math.abs(currentMeshY - centerOffset) < this.speed) {
			//反対方向入力なら即方向転換（Uターン）
			if (isOppositeDirection(nextdirection, this.direction)) {
				this.direction = nextdirection;
			}
			//次の方向に進めるならその方向へ変更
			if (canmove(nextdirection, map)) {
				int col = (int) (centerX / CELL_SIZE);
				int row = (int) (centerY / CELL_SIZE);
				// マスの中心に位置を揃える（ズレ防止）
				this.x = col * CELL_SIZE;
				this.y = row * CELL_SIZE;

				this.direction = nextdirection;
			}
		}
			// 実際の移動処理
			if (canmovego(this.direction, map)) {
				// 移動可能ならその方向へ進む
				this.x += this.direction.getDX() * this.speed;
				this.y += this.direction.getDY() * this.speed;
			} else {
				// 壁にぶつかる場合 → マス中央まで移動し停止
				int col = (int) ((this.x + CELL_SIZE / 2.0) / CELL_SIZE);
				int row = (int) ((this.y + CELL_SIZE / 2.0) / CELL_SIZE);
				//座標
				this.x = col * CELL_SIZE;
				this.y = row * CELL_SIZE;
				//壁があると動かないようにしてる
				this.direction = Direction.NONE;
			}

		
	}

	//1が先行入力2が今動いてる方向
	private boolean isOppositeDirection(Direction dir1, Direction dir2) {
		//今動いてない、入力もしてないとfalse
		if (dir1 == Direction.NONE || dir2 == Direction.NONE)
			return false;
		// dxが逆、またはdyが逆（足して0になる組み合わせ）なら真逆,
		return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
	}

	// マスの途中では進み続けていいか判定(pxずつの確認)
	private boolean canmovego(Direction direction, int[][] map) {
		//入力なしなら移動不可
		if (direction == Direction.NONE)
			return false;
		//中心座標
		double centerX = this.x + CELL_SIZE / 2.0;
		double centerY = this.y + CELL_SIZE / 2.0;

		// マス内の位置
		int currentMeshX = (int) centerX % CELL_SIZE;
		int currentMeshY = (int) centerY % CELL_SIZE;
		int centerOffset = CELL_SIZE / 2;
		//方向転換(マスの中心に来たら先行入力の方向に曲がる)？たぶん
		if (Math.abs(currentMeshX - centerOffset) < this.speed && Math.abs(currentMeshY - centerOffset) < this.speed) {
			return canmove(direction, map);
		}

		// マスの途中ならそのまま進んでOK
		return true;

	}

	//「移動中にその方向へ進み続けてよいか」判定,1マスずつの確認
	private boolean canmove(Direction direction, int[][] map) {
		if (direction == Direction.NONE) {
			return false;
		}
		//現在位置（マス単位）
		int currentCol = (int) ((this.x + CELL_SIZE / 2.0) / CELL_SIZE); //縦
		int currentRow = (int) ((this.y + CELL_SIZE / 2.0) / CELL_SIZE); //横

		//次に進むマス
		int nextCol = currentCol + (int) direction.getDX();
		int nextRow = currentRow + (int) direction.getDY();

		// マップ外チェック
		if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
			return false;
		}
		// 1は壁 → 通れない
		return map[nextRow][nextCol] != 1;
	}

	// 位置を直接更新する
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// HP・スコア関連
	// 死亡処理
	public void die() {
		this.hp = 0;
		this.direction = Direction.NONE; // 止まる
	}

	// ダメージを受ける
	public void takeDamage() {
		if (hp > 0) {
			hp--;

			// HP が 0 になったら死亡
			if (hp == 0) {
				die();
			}
		}
	}

	// 指定ポイントを加算
	public void addScore(int point) {
		this.score += point;
	}

	// 現在のスコアを返す
	public int getScore() {
		return this.score;
	}

	// 現在の HP を返す
	public int getHp() {
		return this.hp;
	}

	// 生存判定
	public boolean isAlive() {
		return this.hp > 0;
	}

}
