//　GreenEnemy（緑）
//　プレイヤーとの距離によって行動を変える敵
//　遠いとき → プレイヤーを追跡
//　近いとき → 縄張りへ戻る

package Characters;

import java.io.InputStream;
import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

public class GreenEnemy extends Enemy {

	// 初期位置（エネミーハウス内）
	private static final int START_COL = 14;
	private static final int START_ROW = 14;
	// 8 マス以上離れていたら追跡
	private static final double BORDER = 8;
	// SCATTER状態時の縄張り座標（左下）
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 26;
	// 出撃時間を記録する
	private long startTime;
	// 出撃タイマーが開始されたか
	private boolean timerStarted = false;
	// 巣から出撃済みか
	private boolean released = false;

	// ==================================================
	// コンストラクタ
	// ==================================================
	public GreenEnemy(GameMap mapData) {
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
		String imagePath = "/picture/nari_EnemyGreen.png";
		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:
				// ステージ1の画像
				imagePath = "/picture/nari_EnemyGreen.png";
				break;
			case 2:
				// ステージ2の画像
				imagePath = "/picture/taku_EnemyGreen.png";
				break;
			case 3:
				// ステージ3の画像
				imagePath = "/picture/aniki_EnemyGreen.png";
				break;
			default:
				break;
			}
		}

		// 画像読み込み
		try {
			InputStream is = getClass().getResourceAsStream(imagePath);
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
		// 出撃待機中のみタイマー補正を行う
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
		// 出撃タイマーをリセット
		timerStarted = false;
	}
	
	// ==================================================
	// 動き
	// ==================================================
	// 10秒経過後に出撃
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
			// 10秒経過するまで待機
			if (elapsed < 10000) {
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
	// GreenEnemyの移動方向を決定する
	// 遠い → 追跡
	// 近い → 左下の縄張りへ戻る
	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData) {

		// 移動可能な方向が存在しない場合
		if (mapData == null || validDirections.isEmpty()) {
			return Direction.NONE;
		}

		// プレイヤーの現在座標を取得
		double pacX = mapData.getPacX();
		double pacY = mapData.getPacY();

		// プレイヤーの位置をタイル座標へ変換
		int targetCol = (int) (pacX / GameConfig.TILE_SIZE);
		int targetRow = (int) (pacY / GameConfig.TILE_SIZE);

		// 縄張りモード
		if (currentState == EnemyState.SCATTER) {
			return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
		}

		// FEVER・DEAD状態の共通処理
		Direction special = handleSpecialState(validDirections, targetCol, targetRow, map);
		if (special != null) {
			return special;
		}

		// 自分の現在位置を取得
		int myCol = (int) (this.x / GameConfig.TILE_SIZE);
		int myRow = (int) (this.y / GameConfig.TILE_SIZE);

		// プレイヤーとの距離計算（マス単位）
		double distance = Math.sqrt(Math.pow(myCol - targetCol, 2) + Math.pow(myRow - targetRow, 2));
		// プレイヤーが遠い場合は追跡
		if (distance >= BORDER) {
			return getClosestDirection(validDirections, targetCol, targetRow);
		}
		
		// ==================================================
		// getter
		// ==================================================
		// プレイヤーが近い場合は縄張りへ戻る
		return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
	}

}