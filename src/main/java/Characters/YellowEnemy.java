// RedEnemy と連携してはさみうちにする BlueEnemy(青) 

package Characters;
/*
import java.util.List;

import javafx.scene.image.Image;
import test.Enemy;
import test.test2.MapData;

public class YellowEnemy extends Enemy {

	// スタート位置(マップ中心 エネミーハウス上)
	private static final int START_COL = 16;
	private static final int START_ROW = 15;

	// 縄張りエリアの中心（左上）（仮座標）
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 3;

	// プレイヤーの進行方向の4マス先を狙う
	private static final int PREDICT_TILES = 4;
	// 出発遅延（10秒後に動き始める)
	private static final long DELAY = 10000; 

	// 出発時間の記録
	private long startTime;

	public YellowEnemy(MapData mapData) {

		// マスの中心座標を初期位置として Enemy に渡す
		super(
				START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
				START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
				1);

		this.mapData = mapData;
		this.startTime = System.currentTimeMillis();

		// 画像の読み込み処理
		try {
			java.io.InputStream is = getClass().getResourceAsStream("/picture/■■.png");
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

	// MapView から現在の画像を取り出すためのゲッター
	public Image getEnemyImage() {
		if (this.currentState == Characters.EnemyState.DEAD)
			return deadImage;
		if (this.currentState == Characters.EnemyState.FEVER)
			return feverImage;
		return normalImage;
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {

		// まだ出発時間に達していない → 動かない
		if (System.currentTimeMillis() - startTime < DELAY) {
			return Direction.NONE;
		}
		// 進める方向がない場合は停止
		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}
		
		// FEVER 時はランダム移動
        if (this.currentState == EnemyState.FEVER) {
            return getRandomDirection(validDirections);
        }
        
        // DEAD 時はハウスへ帰還
        if (this.currentState == EnemyState.DEAD) {
            return getClosestDirection(validDirections, START_COL, START_ROW);
        }

        // プレイヤーのタイル座標
        int pacCol = (int)(mapData.getPacX() / MapData.TILE_SIZE);
        int pacRow = (int)(mapData.getPacY() / MapData.TILE_SIZE);

        // プレイヤーの向きの4マス先
        switch (mapData.getSengoku().getDirection()) {
            case UP:    pacRow -= PREDICT_TILES; break;
            case DOWN:  pacRow += PREDICT_TILES; break;
            case LEFT:  pacCol -= PREDICT_TILES; break;
            case RIGHT: pacCol += PREDICT_TILES; break;
            default: break;
        }

		// 親クラスの最短ルート計算メソッドにターゲットマスを渡して、最短ルートで次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}*/