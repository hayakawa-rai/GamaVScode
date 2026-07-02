// 最短距離でプレイヤーを追跡する敵（赤）

package Characters;

import java.util.List;

import common.GameConfig;
import common.GameMap;
import javafx.scene.image.Image;

public class RedEnemy extends Enemy {

	// スタート位置（エネミーハウス付近上）
	private static final int START_COL = 13;
	private static final int START_ROW = 13;

	// SCATTER状態で向かう縄張り（右上）
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 3;

	public RedEnemy(GameMap sampleModel) {

		// マスの中心座標を初期位置として親クラスへ渡す
		super(START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0,
				START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0, 2);

		// ステージ情報を保存
		this.mapData = sampleModel;

		// FEVER状態用画像を読み込む
		loadFeverImage();

		// DEAD状態用画像を読み込む
		loadDeadImage();

		// 通常時に使用する画像パス
		String imagePath = "/picture/narita_EnemyRed.png";

		// ステージごとに画像を切り替える
		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
			case 1:

				// ステージ1の画像
				imagePath = "/picture/narita_EnemyRed.png";
				break;
			case 2:

				// ステージ2の画像
				imagePath = "/picture/wada_EnemyRed.png";
				break;
			case 3:

				// ステージ3の画像
				imagePath = "/picture/hayakawa_EnemyRed.png";
				break;
			default:
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

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, GameMap mapData) {

		// 移動可能な方向がない場合は停止、または最初の方向を返す
		if (mapData == null || validDirections.isEmpty())
			return Direction.NONE;

		// プレイヤーの現在位置を取得
		double pacX = mapData.getPacX() + GameConfig.TILE_SIZE / 2.0;
		double pacY = mapData.getPacY() + GameConfig.TILE_SIZE / 2.0;

		// プレイヤーの位置をタイル座標へ変換
		int targetCol = (int) (pacX / GameConfig.TILE_SIZE);
		int targetRow = (int) (pacY / GameConfig.TILE_SIZE);

		// 縄張りモード
		if (currentState == Characters.EnemyState.SCATTER) {
			return getClosestDirection(validDirections, TERRITORY_COL, TERRITORY_ROW);
		}

		// FEVER・DEAD状態の共通処理
		Direction special = handleSpecialState(validDirections, targetCol, targetRow, map);
		if (special != null) {
			return special;
		}

		// 赤エネミー固有AI
		// プレイヤーへ最短距離で接近する
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
