// Sengokuの4マス先を狙う YellowEnemy(黄) 
package Characters;

import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

public class YellowEnemy extends Enemy {

	// スタート位置(マップ中心 エネミーハウス内)
	private static final int START_COL = 13;
	private static final int START_ROW = 14;

	// プレイヤーの進行方向の4マス先を狙う
	private static final int PREDICT_TILES = 4;

	// 縄張りエリア（左上）（仮座標）
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 3;

	// 出発時間の記録
	private long startTime;

	//ゲーム開始した瞬間にタイマーをスタート
	private boolean timerStarted = false;

	// 巣から出たか
	private boolean released = false;

	public YellowEnemy(GameMap mapData) {

		// マスの中心座標を初期位置として Enemy に渡す
		super(START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0,
				START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0, 2);
		this.mapData = mapData;
		
		// FEVER画像をステージごとに読み込む
		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える
		// デフォルト（ステージ1用）
		String imagePath = "/picture/narita_EnemyYellow.png";

		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:
				
				// ステージ1の画像
				imagePath = "/picture/narita_EnemyYellow.png";
				break;
			case 2:
				
				// ステージ2の画像
				imagePath = "/picture/wada_EnemyYellow.png";
				break;
			case 3:
				
				// ステージ3の画像
				imagePath = "/picture/hayakawa_EnemyYellow.png";
				break;
			default:
				break;
			}
		}

		// 画像の読み込み
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

	// 10秒経過後に出撃
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
			
			//ゲーム開始から10秒後
			if (elapsed < 10000) {
				return;
			}
			
			//出撃
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
		int targetCol = (int) (mapData.getPacX() / GameConfig.TILE_SIZE);
		int targetRow = (int) (mapData.getPacY() / GameConfig.TILE_SIZE);

		// プレイヤーの向きの4マス先
		switch (mapData.getPlayerDirection()) {
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
		
		// 親クラスの 最短ルート計算メソッドにターゲットマスを渡して、最短ルートで次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}

	@Override
	public void resetToStartPosition() {
		super.resetToStartPosition();
		released = false;
		timerStarted = false;
	}

}
