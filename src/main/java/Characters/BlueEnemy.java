// RedEnemy と連携してはさみうちにする BlueEnemy(青) 
package Characters;

import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

public class BlueEnemy extends Enemy {

	// スタート位置(マップ中心 エネミーハウス上)
	private static final int START_COL = 14;
	private static final int START_ROW = 13;

	// プレイヤーの進行方向の2マス先を狙う
	private static final int PREDICT_TILES = 2;

	// 縄張りエリアの中心（右下）（仮座標）
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 26;

	// 出発時間の記録
	private long startTime;

	//ゲーム開始した瞬間にタイマーをスタート
	private boolean timerStarted = false;
	// 巣から出たか
	private boolean released = false;

	// 赤の位置を参照
	private RedEnemy red;

	public BlueEnemy(GameMap mapData) {

		// マスの中心座標を初期位置として Enemy に渡す
		super(START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0,
				START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0, 2);
		this.mapData = mapData;
		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える
		// デフォルト（ステージ1用）
		String imagePath = "/picture/narita_EnemyBlue.png";

		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:
				
				// ステージ1の画像
				imagePath = "/picture/narita_EnemyBlue.png";
				break;
			case 2:
				
				// ステージ2の画像
				imagePath = "/picture/wada_EnemyBlue.png";
				break;
			case 3:
				
				// ステージ3の画像
				imagePath = "/picture/hayakawa_EnemyBlue.png";
				break;
			default:
				break;
			}
		}

		// RedをMapDataから探す
		for (Enemy e : mapData.getEnemies()) {
			if (e instanceof RedEnemy) {
				this.red = (RedEnemy) e;
				break;
			}
		}

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

	// 2秒経過後に出撃
	@Override
	public void move(int[][] map) {
	    if (mapData.isWaitingStart()) {
	        return;
	    }

	    // 初回入力後に初めてタイマー開始
	    if (!timerStarted) {
	        startTime = System.currentTimeMillis();
	        timerStarted = true;
	    }

	    if (!released) {
	        long elapsed = System.currentTimeMillis() - startTime;
			if (elapsed < 2000) {
				return;
			}

	        released = true;
	    }

	    super.move(map);
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData) {

		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}

		// プレイヤーのタイル座標
		int pacCol = (int) (mapData.getPacX() / GameConfig.TILE_SIZE);
		int pacRow = (int) (mapData.getPacY() / GameConfig.TILE_SIZE);

		// プレイヤーの向きの2マス先
		switch (mapData.getPlayerDirection()) {
		case UP:
			pacRow -= PREDICT_TILES;
			break;
		case DOWN:
			pacRow += PREDICT_TILES;
			break;
		case LEFT:
			pacCol -= PREDICT_TILES;
			break;
		case RIGHT:
			pacCol += PREDICT_TILES;
			break;
		default:
			break;
		}

		// RedEnemy の位置
		int redCol = (int) (red.getX() / GameConfig.TILE_SIZE);
		int redRow = (int) (red.getY() / GameConfig.TILE_SIZE);

		// ベクトル計算
		int vx = pacCol - redCol;
		int vy = pacRow - redRow;

		// 2倍した先がターゲット
		int targetCol = pacCol + vx;
		int targetRow = pacRow + vy;

		// 縄張りモード
		if (currentState == Characters.EnemyState.SCATTER) {
			return getClosestDirection(
					validDirections,
					TERRITORY_COL,
					TERRITORY_ROW);
		}

		// 共通処理
		Direction special = handleSpecialState(validDirections, pacCol, pacRow, map);

		if (special != null) {
			return special;
		}

		// 親クラスの最短ルート計算メソッドにターゲットマスを渡して、最短ルートで次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}

	@Override
	public void resetToStartPosition() {
		super.resetToStartPosition();
		released = false;
		timerStarted = false;
	}

}
