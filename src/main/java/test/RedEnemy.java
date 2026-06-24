package test;

import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class RedEnemy extends Enemy {

	// 開けた安全な通路からスタートさせる（壁埋まり・フリーズ防止）
	private static final int START_COL = 12;
	private static final int START_ROW = 1;

	//引数を MapData に一本化し、正しいコンストラクタの形に直した
	public RedEnemy(MapData sampleModel) {
		// パックマンと同じくマスの「中心ピクセル座標」を初期位置として親に渡す
		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
		      START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
		      2); // スピードは 2

		this.mapData = sampleModel; // 親クラスのフィールドに代入して保持

		// 画像の読み込み処理
		try {
			java.io.InputStream is = getClass().getResourceAsStream("/hayakawa-udekumi.png");
			if (is == null) {
				System.err.println("❌【エラー】画像が見つかりません");
			} else {
				this.normalImage = new Image(is);
				System.out.println("⭕【成功】早川さんの画像を読み込みました！");
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
		double pacX = mapData.getPacX();
		double pacY = mapData.getPacY();

		// ピクセル座標から、AIが目指すべき「ターゲットのマス」を算出
		int targetCol = (int) (pacX / MapData.TILE_SIZE);
		int targetRow = (int) (pacY / MapData.TILE_SIZE);

		// 親クラスの最短ルート計算メソッドにターゲットマスを渡して、次の一歩を決める
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
