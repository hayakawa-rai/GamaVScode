// 最短距離でプレイヤーを追跡する敵（赤）

package test;

import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class RedEnemy extends Enemy {

	// スタート位置（エネミーハウス付近上）
	private static final int START_COL = 14;
	private static final int START_ROW = 14;

	// 縄張りエリアの中心（右上）(仮)
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 3;

	// 引数を MapData に一本化し、正しいコンストラクタの形に直した

	public RedEnemy(MapData sampleModel) {

		// マスの中心座標で生成

		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,

				START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 1); // スピードは 2
		
		this.mapData = sampleModel;

		// FEVER画像をステージごとに読み込む
		loadFeverImage();

		// DEAD画像を読み込む
		loadDeadImage();

		// 現在のステージ番号によって、読み込む画像を切り替える

		String imagePath = "/picture/narita_EnemyRed.png"; // デフォルト（ステージ1用）

		if (this.mapData != null) {

			switch (this.mapData.getStageNumber()) {

			case 1:

				imagePath = "/picture/narita_EnemyRed.png"; // ステージ1の画像

				break;

			case 2:

				imagePath = "/picture/wada_EnemyRed.png"; // ステージ2の画像

				break;

			case 3:

				imagePath = "/picture/hayakawa_EnemyRed.png"; // ステージ3の画像

				break;

			default:

			}
		}

		// 画像の読み込み処理

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


	@Override

	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {

		// 安全対策: 進める方向がない場合は NONE、または最初の方向を返す

		if (mapData == null || validDirections.isEmpty())

			return Direction.NONE;

		// キーボード操作で動いている本物のパックマン座標(px)をMapDataから取得

		double pacX = mapData.getPacX() + MapData.TILE_SIZE / 2.0;

		double pacY = mapData.getPacY() + MapData.TILE_SIZE / 2.0;

		// ピクセル座標から、AIが目指すべき「ターゲットのマス」を算出

		int targetCol = (int) (pacX / MapData.TILE_SIZE);

		int targetRow = (int) (pacY / MapData.TILE_SIZE);

		// 共通処理

		Direction special = handleSpecialState(validDirections, targetCol, targetRow);

		if (special != null) {

			return special;

		}

		// 赤専用AI(最短追尾)

		return getClosestDirection(validDirections, targetCol, targetRow);

	}

}
