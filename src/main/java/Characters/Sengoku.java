package Characters;

public class Sengoku extends Character {

	// 属性（Attributes）
	private int hp = 3; // 体力
	private int score = 0; // スコア
	private boolean isAlive = true; //生存判定
	//private final double rispornx; //スタート地点x
	//private final double risporny; //スタート地点y
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
	public void move(int[][] map) {
		
		// キャラクターの中心点を計算
		double centerX = this.getX() + CELL_SIZE / 2.0;         
		double centerY = this.getY() + CELL_SIZE / 2.0;
		
		//
		int currentMeshX = (int)centerX % CELL_SIZE;         
		int currentMeshY = (int)centerY % CELL_SIZE;         
		int centerOffset = CELL_SIZE / 2;
		
		//方向転換(マスの中心に来たら先行入力の方向に曲がる)
		if (Math.abs(currentMeshX - centerOffset) < this.speed && Math.abs(currentMeshY - centerOffset) < this.speed) {
			//プレーヤーが入力した方向が、進んでいる方向と180度反対かしらべている
			if (isOppositeDirection(nextdirection, this.direction)) {
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
		//
		if (canmovego(this.direction, map)) {
            this.x += this.direction.getDX() * this.speed;
            this.y += this.direction.getDY() * this.speed;
        } else {
            // 次が壁なら、中途半端な位置で止まらないようにマスの中心にスナップして完全停止
        	 	//今いるマス目
            int col = (int)((this.x + CELL_SIZE / 2.0) / CELL_SIZE);
            int row = (int)((this.y + CELL_SIZE / 2.0) / CELL_SIZE);
            //座標
            this.x = col * CELL_SIZE;
            this.y = row * CELL_SIZE;
            //壁があると動かないようにしてる
            this.direction = Direction.NONE;
        }
 
	}
	}
	//1が先行入力2が今動いてる方向
	private boolean isOppositeDirection(Direction dir1, Direction dir2) {
		//今動いてない、入力もしてないとfalse
	     if (dir1 == Direction.NONE || dir2 == Direction.NONE) return false;
	     // dxが逆、またはdyが逆（足して0になる組み合わせ）なら真逆,
	        return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
	}
		
	//pxずつの確認
	private boolean canmovego(Direction direction, int[][] map) {
		//入力されてるかされてないか
		if (direction == Direction.NONE) return false;
		//マスの中心を調べている
        double centerX = this.x + CELL_SIZE / 2.0;
        double centerY = this.y + CELL_SIZE / 2.0;
 
        // ちょうどマスの中心付近にいるときだけ、1マス先が壁か調べる,プレーヤーが縦横何マス目にいるか
        int currentMeshX = (int)centerX % CELL_SIZE;
        int currentMeshY = (int)centerY % CELL_SIZE;
        int centerOffset = CELL_SIZE / 2;
        //方向転換(マスの中心に来たら先行入力の方向に曲がる)？たぶん
        if (Math.abs(currentMeshX - centerOffset) < this.speed && Math.abs(currentMeshY - centerOffset) < this.speed) {
            return canmove(direction, map);
        }
 
        // マスの途中にいる間は、そのまま進んでよし
        return true;
 
	}
		
	//1マスずつの確認
	private boolean canmove(Direction direction, int[][] map) {
			if (direction == Direction.NONE) {
				return false;
			}
			//プレーヤーが縦横何マス目にいるか
			int currentCol = (int)((this.x + CELL_SIZE / 2.0) / CELL_SIZE);  //縦
			int currentRow = (int)((this.y + CELL_SIZE / 2.0) / CELL_SIZE);  //横
			
			//
			int nextCol = currentCol + (int)direction.getDX();
			int nextRow = currentRow + (int)direction.getDY();
			 
		    // 画面外の安全チェック(場外に行ってないか)
	        if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
	            return false;
	        }
	        //1は壁の数字です
	        return map[nextRow][nextCol] != 1;
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
