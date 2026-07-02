// syujinkouの4マス先を狙う YellowEnemy(黄) 
package sample;
/*
import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class YellowEnemy extends Enemy {

	// スタート位置(マップ中心 エネミーハウス内)
	private static final int START_COL = 14;
	private static final int START_ROW = 14;

	// プレイヤーの進行方向の4マス先を狙う
	private static final int PREDICT_TILES = 4;

	// 縄張りエリア（左上）（仮座標）
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 3;

	// 出発時間の記録
	private long startTime;

	// 巣から出たか
	private boolean released = false;

	public YellowEnemy(MapData mapData) {

		// マスの中心座標を初期位置として Enemy に渡す
		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
				START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 1);

		this.mapData = mapData;

		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える
		String imagePath = "/picture/nari_EnemyYellow.png"; // デフォルト（ステージ1用）

		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:
				imagePath = "/picture/nari_EnemyYellow.png"; // ステージ1の画像
				break;
			case 2:
				imagePath = "/picture/taku_EnemyYellow.png"; // ステージ2の画像
				break;
			case 3:
				imagePath = "/picture/aniki_EnemyYellow.png"; // ステージ3の画像
				break;
			default:
				break;
			}
		}

		// 生成時刻を記録
		this.startTime = System.currentTimeMillis();

		// 画像の読み込み
		try {
			java.io.InputStream is = getClass().getResourceAsStream(imagePath);
			if (is == null) {
				System.err.println("❌【エラー】画像が見つかりません: " + imagePath);
			} else {
				this.normalImage = new Image(is);
				System.out.println("⭕【成功】ステージ" + this.mapData.getStageNumber() + "用の画像を読み込みました！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 10秒経過後に出撃
	@Override
	public void move(int[][] map) {
		if (!released) {
			long elapsed = System.currentTimeMillis() - startTime;

			// ゲーム開始から10秒は待機
			if (elapsed < 10000) {
				return;
			}

			// 出撃
			released = true;
		}
		super.move(map);
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {
		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}

		// プレイヤーのタイル座標
		int targetCol = (int) (mapData.getPacX() / MapData.TILE_SIZE);
		int targetRow = (int) (mapData.getPacY() / MapData.TILE_SIZE);

		// プレイヤーの向きの4マス先
		switch (mapData.getsyujinkou().getDirection()) {
		case UP:
			targetRow -= PREDICT_TILES;
			break;
		case DOWN:
			targetRow += PREDICT_TILES;
			break;
		case LEFT:
			targetCol -= PREDICT_TILES;
			break;
		case RIGHT:
			targetCol += PREDICT_TILES;
			break;
		default:
			break;
		}

		// 共通処理
		Direction special = handleSpecialState(validDirections, targetCol, targetRow);

		if (special != null) {
			return special;
		}
		// 親クラスの 最短ルート計算メソッドにターゲットマスを渡して、最短ルートで次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
*/