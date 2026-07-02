// 遠くにいるときは追跡、近づくと縄張りへ戻る敵（緑）
package sample;
/*
import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class GreenEnemy extends Enemy {

	// スタート位置 (マップ中心 エネミーハウス内)
	private static final int START_COL = 14;
	private static final int START_ROW = 14;

	// 8 マス以上離れていたら追跡
	private static final double BORDER = 8;

	// 縄張りエリア（左下） (仮)
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 26;

	// 出撃待機用
	private long startTime;

	// 巣から出たか
	private boolean released = false;

	public GreenEnemy(MapData mapData) {

		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
				START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 1);

		this.mapData = mapData;

		// FEVER画像をステージごとに読み込む
		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える
		String imagePath = "/picture/nari_EnemyGreen.png"; // デフォルト（ステージ1用）
		
		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
				case 1:
					imagePath = "/picture/nari_EnemyGreen.png"; // ステージ1の画像
					break;
				case 2:
					imagePath = "/picture/taku_EnemyGreen.png";        // ステージ2の画像
					break;
				case 3:
					imagePath = "/picture/aniki_EnemyGreen.png";         // ステージ3の画像
					break;
				default:
					break;
			}
		}

		// 生成時刻を記録
		this.startTime = System.currentTimeMillis();

		// 画像読み込み
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

	// 20秒経過後に出撃
	@Override
	public void move(int[][] map) {
		if (!released) {
			long elapsed = System.currentTimeMillis() - startTime;

			// ゲーム開始から20秒は待機
			if (elapsed < 20000) {
				return;
			}
			// 出撃
			released = true;
		}
		super.move(map);
	}

	// 遠い → 追跡
	// 近い → 左下の縄張りへ戻る
	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {

		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}

		// パックマンの位置
		double pacX = mapData.getPacX();
		double pacY = mapData.getPacY();

		int targetCol = (int) (pacX / MapData.TILE_SIZE);
		int targetRow = (int) (pacY / MapData.TILE_SIZE);

		// 共通処理
		Direction special = handleSpecialState(validDirections, targetCol, targetRow);

		if (special != null) {
			return special;
		}

		// 自分の位置
		int myCol = (int) (this.x / MapData.TILE_SIZE);
		int myRow = (int) (this.y / MapData.TILE_SIZE);

		// プレイヤーとの距離（マス単位）
		double distance = Math.sqrt(Math.pow(myCol - targetCol, 2) + Math.pow(myRow - targetRow, 2));

		// 遠いなら追跡
		if (distance >= BORDER) {
			return getClosestDirection(validDirections, targetCol, targetRow);
		}

		// 近いなら縄張りへ戻る
		return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
	}
}
*/