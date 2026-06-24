// RedEnemy と連携してはさみうちにする BlueEnemy(青) 
package test;

import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class BlueEnemy extends Enemy {

	// スタート位置(マップ中心 エネミーハウス上)
	private static final int START_COL = 16;
	private static final int START_ROW = 16;
	
	// プレイヤーの進行方向の2マス先を狙う
	private static final int PREDICT_TILES = 2;

	// 縄張りエリアの中心（右下）（仮座標）
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 26;

	// 出発時間の記録
	private long startTime;
	// 巣から出たか
	private boolean released = false;
	
	// 赤の位置を参照
	private RedEnemy red;
	// プレイヤー座標取得用
	private MapData mapData;

	public BlueEnemy(MapData mapData, RedEnemy red) {
		// マスの中心座標を初期位置として Enemy に渡す
		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
				START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 1);

		this.mapData = mapData;
		this.red = red;
		// 生成時刻を記録
		this.startTime = System.currentTimeMillis();

		// 画像の読み込み
		try {
			java.io.InputStream is = getClass().getResourceAsStream("/picture/wadataku.png");
			if (is == null) {
				System.err.println("❌【エラー】画像が見つかりません");
			} else {
				this.normalImage = new Image(is);
				System.out.println("⭕【成功】早川さんの画像を読み込みました！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 画像の読み込み処理
	public Image getEnemyImage() {
		if (this.currentState == Characters.EnemyState.DEAD) {
			return deadImage;
		}
		if (this.currentState == Characters.EnemyState.FEVER) {
			return feverImage;
		}
		return normalImage;
	}
	
	//2秒経過後に出撃
		@Override
		public void move(int[][] map) {
			if (!released) {
				long elapsed = System.currentTimeMillis() - startTime;

				// ゲーム開始から10秒は待機
				if (elapsed < 2000) {
					return;
				}

				// 出撃
				released = true;
			}
			super.move(map);
		}


	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {
		
		// FEVER 時はランダム移動
        //if (this.currentState == EnemyState.FEVER) {
        //   return getRandomDirection(validDirections);
        //}
        
        // DEAD 時はハウスへ帰還
        //if (this.currentState == EnemyState.DEAD) {
        //    return getClosestDirection(validDirections, START_COL, START_ROW);
        //}

		// プレイヤーのタイル座標
    		int pacCol = (int)(mapData.getPacX() / MapData.TILE_SIZE);
    		int pacRow = (int)(mapData.getPacY() / MapData.TILE_SIZE);

		// プレイヤーの向きの2マス先
		switch (mapData.getSengoku().getDirection()) {
		case UP:
			pacRow -= PREDICT_TILES * MapData.TILE_SIZE;
			break;
		case DOWN:
			pacRow += PREDICT_TILES * MapData.TILE_SIZE;
			break;
		case LEFT:
			pacCol -= PREDICT_TILES * MapData.TILE_SIZE;
			break;
		case RIGHT:
			pacCol += PREDICT_TILES * MapData.TILE_SIZE;
			break;
		default:
			break;
		}

		// RedEnemy の位置
		int redCol = (int)(red.getX() / MapData.TILE_SIZE);
    		int redRow = (int)(red.getY() / MapData.TILE_SIZE);

		// ベクトル計算
        int vx = pacCol - redCol;
        int vy = pacRow - redRow;

        // 2倍した先がターゲット
        int targetCol = pacCol + vx;
        int targetRow = pacRow + vy;

		// 親クラスの最短ルート計算メソッドにターゲットマスを渡して、最短ルートで次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}

