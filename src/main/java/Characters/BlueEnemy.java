// RedEnemy と連携してはさみうちにする BlueEnemy(青) 
package Characters;
/*

public class BlueEnemy extends Enemy {

   private Sengoku player;
   private RedEnemy red;
   
   private long startTime;
   private static final long DELAY = 2000; // ★ 2秒遅れて出発

   private static final int CELL_SIZE = 24;
   private static final int PREDICT_TILES = 2;

   public BlueEnemy(double x, double y, Sengoku player, RedEnemy red) {
       super(x, y);
       this.player = player;
       this.red = red;
   }

// エネミーハウス右下 （仮）
private static final int START_COL = 13;
private static final int START_ROW = 11;

public BlueEnemy(ImageView imageView) {
	super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1);
	this.player = player;
    this.red = red;
	
	// 生成された瞬間の時間を記録
    this.startTime = System.currentTimeMillis();
}


   @Override
   public void move(int[][] map) {
   
    	// 経過時間を計算
       long elapsed = System.currentTimeMillis() - startTime;

       // 2秒経つまで動かない
       if (elapsed < DELAY) return;

       // プレイヤーの現在位置
       double px = player.getX();
       double py = player.getY();

       // プレイヤーの向きの 2 マス先
       switch (player.getDirection()) {
           case UP:    py -= PREDICT_TILES * CELL_SIZE; break;
           case DOWN:  py += PREDICT_TILES * CELL_SIZE; break;
           case LEFT:  px -= PREDICT_TILES * CELL_SIZE; break;
           case RIGHT: px += PREDICT_TILES * CELL_SIZE; break;
           default: break;
       }

       // RedEnemy の位置
       double rx = red.getX();
       double ry = red.getY();

       // ベクトル計算
       double vx = px - rx;
       double vy = py - ry;

       // 2倍した先をターゲット
       double tx = px + vx;
       double ty = py + vy;

       // BlueEnemy → ターゲットへの差分
       double dx = tx - this.x;
       double dy = ty - this.y;

       // X優先 or Y優先
       if (Math.abs(dx) > Math.abs(dy)) {
           double nextX = this.x + Math.signum(dx) * speed;
           if (canMove(nextX, this.y, map)) {
               this.x = nextX;
               return;
           }
       }

       double nextY = this.y + Math.signum(dy) * speed;
       if (canMove(this.x, nextY, map)) {
           this.y = nextY;
       }
   }
}*/