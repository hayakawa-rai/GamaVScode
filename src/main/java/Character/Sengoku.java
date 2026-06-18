package Character;

public class Sengoku extends Character {

	//一旦大枠とmoveだけ書いたよ間違ってたらごめん
	
	    // 属性（Attributes）
	    private int hp;        // 体力
	    private int score;     // スコア

	    // コンストラクタ
	    public Sengoku(int x, int y, int hp) {
	        super(x, y);   // Character の位置情報を初期化
	        this.hp = hp;
	        this.score = 0;
	    }

	    // キャラクターを移動させる
	    public void move() {
	        // 移動処理とかスピード設定を入れる
	    }
	        // 壁判定つき move()
	        public void move(GameMap map) {

	            int nextX = x;
	            int nextY = y;

	            switch (direction) {
	                case UP:
	                    nextY -= 1;
	                    break;
	                case DOWN:
	                    nextY += 1;
	                    break;
	                case LEFT:
	                    nextX -= 1;
	                    break;
	                case RIGHT:
	                    nextX += 1;
	                    break;
	                case NONE:
	                default:
	                    return; // 動かない
	            }

	            // 壁判定：壁なら移動しない
	            if (!map.isWall(nextX, nextY)) {
	                x = nextX;
	                y = nextY;
	            }  
	        }
	        
	    // 死亡処理
	    public void die() {
	        this.hp = 0;
	    }

	    // ダメージを受ける
	    public void takeDamage() {
	        if (hp > 0) {
	            hp--;
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
