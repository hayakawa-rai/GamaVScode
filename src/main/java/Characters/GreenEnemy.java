// 遠くにいるときは追跡、近づくと縄張りへ戻る敵（緑）
package Characters;

import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

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

	//ゲーム開始した瞬間にタイマーをスタート
	private boolean timerStarted = false;

	// 巣から出たか
	private boolean released = false;

	public GreenEnemy(GameMap mapData) {
		super(START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0,
			START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0, 2);
		this.mapData = mapData;

		// FEVER画像をステージごとに読み込む
		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える
		// デフォルト（ステージ1用）
		String imagePath = "/picture/narita_EnemyGreen.png";
		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:
				
				// ステージ1の画像
				imagePath = "/picture/narita_EnemyGreen.png";
				break;
			case 2:
				
				// ステージ2の画像
				imagePath = "/picture/wada_EnemyGreen.png";
				break;
			case 3:
				
				// ステージ3の画像
				imagePath = "/picture/hayakawa_EnemyGreen.png";
				break;
			default:
				break;
			}
		}

		// 画像読み込み
		try {
			java.io.InputStream is = getClass().getResourceAsStream(imagePath);
			if (is == null) {
				System.err.println("【エラー】画像が見つかりません: " + imagePath);
			} else {
				this.normalImage = new Image(is);
				System.out.println("【成功】ステージ" + this.mapData.getStageNumber() + "用の画像を読み込みました！");
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
		if (mapData.isWaitingStart()) {
			return;
		}

		// 初回入力後にタイマー開始
		if (!timerStarted) {

			startTime = System.currentTimeMillis();
			timerStarted = true;
		}

		if (!released) {
			long elapsed = System.currentTimeMillis() - startTime;

			// 20秒待機
			if (elapsed < 20000) {
				return;
			}
			released = true;
		}
		super.move(map);
	}

	// 遠い → 追跡
	// 近い → 左下の縄張りへ戻る
	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData) {
		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}

		// パックマンの位置
		double pacX = mapData.getPacX();
		double pacY = mapData.getPacY();

		// 目指すべき「ターゲットのマス」を算出
		int targetCol = (int) (pacX / GameConfig.TILE_SIZE);
		int targetRow = (int) (pacY / GameConfig.TILE_SIZE);

		// 縄張りモード
		if (currentState == Characters.EnemyState.SCATTER) {
			return getClosestDirection(
					validDirections,
					TERRITORY_COL,
					TERRITORY_ROW);
		}

		// 共通処理
		Direction special = handleSpecialState(validDirections, targetCol, targetRow, map);
		if (special != null) {
			return special;
		}

		// 自分の位置
		int myCol = (int) (this.x / GameConfig.TILE_SIZE);
		int myRow = (int) (this.y / GameConfig.TILE_SIZE);

		// プレイヤーとの距離（マス単位）
		double distance = Math.sqrt(Math.pow(myCol - targetCol, 2) + Math.pow(myRow - targetRow, 2));

		// 遠いなら追跡
		if (distance >= BORDER) {
			return getClosestDirection(validDirections, targetCol, targetRow);
		}

		// 近いなら縄張りへ戻る
		return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
	}

	@Override
	public void resetToStartPosition() {
		super.resetToStartPosition();
		released = false;
		timerStarted = false;
	}

}