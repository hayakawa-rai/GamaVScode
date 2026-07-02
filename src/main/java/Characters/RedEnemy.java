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

	// 縄張りエリアの中心（右上）(仮)
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 3;

	public RedEnemy(GameMap sampleModel) {

		// マスの中心座標で生成

		super(START_COL * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0,

				START_ROW * GameConfig.TILE_SIZE + GameConfig.TILE_SIZE / 2.0, 2); // スピードは 2

		this.mapData = sampleModel;

		// FEVER画像をステージごとに読み込む
		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える
		// デフォルト（ステージ1用）
		String imagePath = "/picture/narita_EnemyRed.png";
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

		// 画像の読み込み処理
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

		// 進める方向がない場合は NONE、または最初の方向を返す
		if (mapData == null || validDirections.isEmpty())
			return Direction.NONE;

		// キーボード操作で動いている本物のパックマン座標(px)をMapDataから取得
		double pacX = mapData.getPacX() + GameConfig.TILE_SIZE / 2.0;
		double pacY = mapData.getPacY() + GameConfig.TILE_SIZE / 2.0;

		// ピクセル座標から、AIが目指すべき「ターゲットのマス」を算出
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

		// 赤専用AI(最短追尾)
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
