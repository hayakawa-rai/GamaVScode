package Character;

public class Sengoku extends Character {

	// 属性（Attributes）
	private int hp = 3; // 体力
	private int score = 0; // スコア
	private boolean isAlive = true; //生存判定
	private final double rispornx; //スタート地点x
	private final double risporny; //スタート地点y
	private Direction nextdirection = Direction.NONE; //次に進みたい方向
	private static final int CELL_SIZE = 24; //1マスの大きさ

	// コンストラクタ
	public Sengoku(double x, double y, int speed) {
		super(x, y, speed); // Character の位置情報を初期化
	}

	public void setnextdirection(Direction direction) {
		this.nextdirection = direction;
	}

	@Override
	public void move(Int[][] map) {
		
		// キャラクターの中心点を計算
		double centerX = this.getX() + CELL_SIZE / 2.0;         
		double centerY = this.getY() + CELL_SIZE / 2.0;
		
		//
		int currentMeshX = (int)centerX % CELL_SIZE;         
		int currentMeshY = (int)centerY % CELL_SIZE;         
		int centerOffset = CELL_SIZE / 2;
		
		//方向転換(マスの中心に来たら先行入力の方向に曲がる)
		if (Math.abs(currentMeshX - centerOffset) < this.getSpeed() && Math.abs(currentMeshY - centerOffset) < this.getSpeed()) {
			//プレーヤーが入力した方向が、進んでいる方向と180度反対かしらべている
			if (isOppositeDirection(nextdirection, this.getDirection())) {
			this.direction = nextdirection;
		}
			//進行方向に行けるかどうか
			if (canmove(nextdirection, map)) {
                int col = (int)(centerX / CELL_SIZE);
                int row = (int)(centerY / CELL_SIZE);
                this.x = col * CELL_SIZE;
                this.y = row * CELL_SIZE;
                
                this.direction = nextdirection;
            }
 
	}
		
	//
	private boolean canmove(Direction direction, Int[][] map) {
			if (direction == Direction.NONE)
				return false;
			//プレーヤーが縦横何マス目にいるか
			int currentCol = (int)((this.x + CELL_SIZE / 2.0) / CELL_SIZE);  //縦
			int currentRow = (int)((this.y + CELL_SIZE / 2.0) / CELL_SIZE);  //横
			
			//
			int nextCol = currentCol + direction.getDx();
			int nextRow = currentRow + direction.getDy();
			 
		    // 画面外の安全チェック(場外に行ってないか)
	        if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
	            return false;
	        }
	        //1は壁の数字です
	        return map[nextRow][nextCol] != 1;
	}
		

		// 壁判定
		if (!map.isWall(nextX, nextY)) {
			// Character の x,y を更新する setter が必要
			setPosition(nextX, nextY);
		}
	}

	// Character に setter が無いので追加が必要
	private void setPosition(int x, int y) {
		try {
			java.lang.reflect.Field fx = Character.class.getDeclaredField("x");
			java.lang.reflect.Field fy = Character.class.getDeclaredField("y");
			fx.setAccessible(true);
			fy.setAccessible(true);
			fx.set(this, x);
			fy.set(this, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

			// HP が 0 になったら死亡処理
			if (hp == 0) {
				die();
			}
		}
	}

	// スコアを 1 加算
	public void addScore() {
		this.score++;
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
