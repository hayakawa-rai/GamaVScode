// 最短距離でプレイヤーを追跡する敵（赤）
package test;

import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class RedEnemy extends Enemy {

	// スタート位置（エネミーハウス付近上）
	private static final int START_COL = 13;
	private static final int START_ROW = 12;

    // 縄張りエリアの中心（右上）(仮)
    private static final int TERRITORY_COL = 24;
    private static final int TERRITORY_ROW = 3;
    
	//引数を MapData に一本化し、正しいコンストラクタの形に直した
	public RedEnemy(MapData sampleModel) {
		 // マスの中心座標で生成
		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
		      START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
		      1); // スピードは 2

		this.mapData = sampleModel; // 親クラスのフィールドに代入して保持

		// 現在のステージ番号によって、読み込む画像を切り替える
		String imagePath = "/picture/hayakawa-udekumi.png"; // デフォルト（ステージ1用）
		
		if (this.mapData != null) {
			switch (this.mapData.getStageNumber()) {
				case 1:
					imagePath = "/picture/hayakawa-udekumi.png"; // ステージ1の画像
					break;
				case 2:
					imagePath = "/picture/hayakawa2.png";        // ステージ2の画像
					break;
				case 3:
					imagePath = "/picture/narinari.png";         // ステージ3の画像
					break;
				default:
					break;
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

	// MapView から現在の画像を取り出すためのゲッター
	public Image getEnemyImage() {
		if (this.currentState == Characters.EnemyState.DEAD) return deadImage;
		if (this.currentState == Characters.EnemyState.FEVER) return feverImage;
		return normalImage;
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {
		// 安全対策: 進める方向がない場合は NONE、または最初の方向を返す
		if (mapData == null || validDirections.isEmpty()) return Direction.NONE;

		// キーボード操作で動いている本物のパックマン座標(px)をMapDataから取得
        double pacX = mapData.getPacX() + MapData.TILE_SIZE / 2.0;
        double pacY = mapData.getPacY() + MapData.TILE_SIZE / 2.0;

		// ピクセル座標から、AIが目指すべき「ターゲットのマス」を算出
		int targetCol = (int) (pacX / MapData.TILE_SIZE);
		int targetRow = (int) (pacY / MapData.TILE_SIZE);

		// 親クラスの最短ルート計算メソッドにターゲットマスを渡して、次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
