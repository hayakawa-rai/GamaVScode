//package Characters;
//
//public class YellowEnemy extends Enemy {
//
//	private Sengoku target; // プレイヤー
//	private static final int PREDICT_TILES = 4; // 4マス先を狙う
//	private static final int CELL_SIZE = 24; // ゲームのマスサイズ
//
//	public YellowEnemy(double x, double y, Sengoku target) {
//        super(x, y); // speed は Enemy 側で決める
//        this.target = target;
//    }
//
//	@Override
//	public void move(int[][] map) {
//
//		// プレイヤーの向きを確認
//		Direction dir = target.getDirection();
//
//		// プレイヤーの現在位置を取得
//		double tx = target.getX();
//		double ty = target.getY();
//
//		// 4マス先の予測位置
//		switch (dir) {
//		case UP:
//			ty -= PREDICT_TILES * CELL_SIZE;
//			break;
//		case DOWN:
//			ty += PREDICT_TILES * CELL_SIZE;
//			break;
//		case LEFT:
//			tx -= PREDICT_TILES * CELL_SIZE;
//			break;
//		case RIGHT:
//			tx += PREDICT_TILES * CELL_SIZE;
//			break;
//		default:
//			// 止まっている時は現在位置を狙う
//			break;
//		}
//
//		// 敵から見た予測位置への方向の計算
//		double dx = tx - this.x; //敵から見て、右にどれくらい離れているか
//		double dy = ty - this.y; //敵から見て、下にどれくらい離れているか
//
//		// X方向優先 or Y方向優先
//		if (Math.abs(dx) > Math.abs(dy)) {
//			this.x += Math.signum(dx) * speed; //横の距離が大きい → 横に動く
//		} else {
//			this.y += Math.signum(dy) * speed; //縦の距離が大きい → 縦に動く
//		}
//	}
//}
//
///*例 
// プレイヤー → 右向き
// プレイヤー位置 → (100, 100)
//
// プレイヤーは右に進んでる
// じゃあ 4マス先は 100 + (4 * 24) = 196
//  (196, 100) を目指して動く？たぶん？*/
