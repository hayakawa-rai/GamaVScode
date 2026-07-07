//　YellowEnemy（黄）
//　プレイヤーの進行方向を予測して追跡する敵
//　プレイヤーの4マス先を目標地点として移動する

package Characters;

import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

public class YellowEnemy extends Enemy {

	// 初期位置（エネミーハウス内）
	private static final int START_COL = 13;
	private static final int START_ROW = 14;
	// プレイヤーの進行方向の4マス先を狙う
	private static final int PREDICT_TILES = 4;
	// SCATTER状態時の縄張り座標（左上）
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 3;
	// 出撃タイマー用
	private long startTime;
	// タイマー開始フラグ
	private boolean timerStarted = false;
	// 出撃済みかどうか
	private boolean released = false;

	// ==================================================
	// コンストラクタ
	// ==================================================
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
		String imagePath = "/picture/nari_EnemyYellow.png";
		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:
				// ステージ1の画像
				imagePath = "/picture/nari_EnemyYellow.png";
				break;
			case 2:
				// ステージ2の画像
				imagePath = "/picture/taku_EnemyYellow.png";
				break;
			case 3:
				// ステージ3の画像
				imagePath = "/picture/aniki_EnemyYellow.png";
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

	// ==================================================
	// タイマー
	// ==================================================
	// ポーズ中の時間を出撃タイマーへ反映する
	@Override
	public void resumeTimer() {
		// 出撃待機中のみ補正を行う
		if (timerStarted && !released) {
			// ポーズしていた時間を計算
			long pauseDuration = System.currentTimeMillis() - pauseStartTime;
			// タイマーを補正
			startTime += pauseDuration;
		}
	}
	
	// ==================================================
	// ポジション
	// ==================================================
	// プレイヤーが被弾時に元の場所、出撃時間をリセット
	@Override
	public void resetToStartPosition() {
		// Enemy共通のリセット処理
		super.resetToStartPosition();
		// 出撃状態を初期化
		released = false;
		// タイマーをリセット
		timerStarted = false;
	}
	
	// ==================================================
	// 動き
	// ==================================================
	// 6秒経過後に出撃
	@Override
	public void move(int[][] map) {
		// READY中は移動しない
		if (mapData.isWaitingStart()) {
			return;
		}
		// 初回入力後にタイマー開始
		if (!timerStarted) {
			startTime = System.currentTimeMillis();
			timerStarted = true;
		}
		// 出撃待機中
		if (!released) {
			// 経過時間を取得
			long elapsed = System.currentTimeMillis() - startTime;
			// 6秒経過するまで待機
			if (elapsed < 6000) {
				return;
			}
			// 出撃開始
			released = true;
		}
		// Enemy共通の移動処理
		super.move(map);
	}
	
	// ==================================================
	// 方向決定
	// ==================================================
	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData) {
		// 移動可能な方向が存在しない場合
		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}
		// プレイヤーのタイル座標
		int targetCol = (int) (mapData.getPacX() / GameConfig.TILE_SIZE);
		int targetRow = (int) (mapData.getPacY() / GameConfig.TILE_SIZE);
		// プレイヤーの進行方向の4マス先を予測
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

		// 縄張りモード(SCATTER状態)
		if (currentState == EnemyState.SCATTER) {
			return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
		}
		// FEVER・DEAD状態の共通処理
		Direction special = handleSpecialState(validDirections, targetCol, targetRow, map);
		if (special != null) {
			return special;
		}
		
		// ==================================================
		// getter
		// ==================================================
		// YellowEnemy固有AI
		// プレイヤーの4マス先を最短距離で追跡する
		return getClosestDirection(validDirections, targetCol, targetRow);
	}

}
