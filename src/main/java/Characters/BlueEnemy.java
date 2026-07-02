//　BlueEnemy（青）
//　RedEnemyと連携してプレイヤーをはさみうちにする敵
//　プレイヤーの進行方向の先を予測し、RedEnemyとの位置関係から追跡地点を決定する

package Characters;

import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

public class BlueEnemy extends Enemy {

	// 初期位置（エネミーハウス中央付近）
	private static final int START_COL = 14;
	private static final int START_ROW = 13;

	// プレイヤーの進行方向+2マス先を狙う
	private static final int PREDICT_TILES = 2;

	// SCATTER状態時の縄張り座標（右下）
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 26;

	// 出撃時間管理用
	private long startTime;

	// 出撃タイマー開始フラグ
	private boolean timerStarted = false;

	// 巣から出撃済みか判定
	private boolean released = false;

	// 赤の位置を参照
	private RedEnemy red;

	public BlueEnemy(GameMap mapData) {

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
				System.err.println("【エラー】画像が見つかりません: " + imagePath);
			} else {
				this.normalImage = new Image(is);
				System.out.println("【成功】ステージ" + this.mapData.getStageNumber() + "用の画像を読み込みました！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ゲーム開始から2秒後に出撃させる
	@Override
	public void move(int[][] map) {

		// READY中は移動しない
		if (mapData.isWaitingStart()) {
			return;
		}

		// 初回入力後に初めてタイマー開始
		if (!timerStarted) {
			startTime = System.currentTimeMillis();
			timerStarted = true;
		}

		// 出撃待機中
		if (!released) {

			// 経過時間を計算
			long elapsed = System.currentTimeMillis() - startTime;

			// 2秒経過するまで待機
			if (elapsed < 2000) {
				return;
			}
			// 出撃許可
			released = true;
		}

		// Enemy共通の移動処理を実行
		super.move(map);
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData) {

		// 移動可能な方向が存在しない場合
		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}

		// プレイヤーの現在位置をタイル座標で取得
		int pacCol = (int) (mapData.getPacX() / GameConfig.TILE_SIZE);
		int pacRow = (int) (mapData.getPacY() / GameConfig.TILE_SIZE);

		// プレイヤーの進行方向の2マス先を予測
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

		// RedEnemyの現在位置を取得
		int redCol = (int) (red.getX() / GameConfig.TILE_SIZE);
		int redRow = (int) (red.getY() / GameConfig.TILE_SIZE);

		// RedEnemy→予測地点のベクトルを計算
		int vx = pacCol - redCol;
		int vy = pacRow - redRow;

		// ベクトルを2倍した地点をターゲットとする
		int targetCol = pacCol + vx;
		int targetRow = pacRow + vy;

		// 縄張りモード
		if (currentState == Characters.EnemyState.SCATTER) {
			return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
		}

		// FEVER・DEAD状態の共通処理
		Direction special = handleSpecialState(validDirections, pacCol, pacRow, map);

		if (special != null) {
			return special;
		}

		// BlueEnemy固有AI
		// RedEnemyと連携したターゲット地点へ向かう
		return getClosestDirection(validDirections, targetCol, targetRow);
	}

	// プレイヤーが被弾時に元の場所、出撃時間をリセット
	@Override
	public void resetToStartPosition() {

		// Enemy共通のリセット処理
		super.resetToStartPosition();

		// 出撃状態を初期化
		released = false;

		// 出撃タイマーを未開始状態へ戻す
		timerStarted = false;
	}

	// ポーズ中の時間を出撃タイマーへ反映する
	@Override
	public void resumeTimer() {

		// 出撃待機中のみ補正を行う
		if (timerStarted && !released) {

			// 出撃待機中のみ補正を行う
			long pauseDuration = System.currentTimeMillis() - pauseStartTime;

			// タイマーをその分だけ後ろへずらす
			startTime += pauseDuration;
		}
	}
}
