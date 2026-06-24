// 最短距離でプレイヤーを追跡する敵（赤）
package Characters;
/*
import java.util.List;
import javafx.scene.image.ImageView;

public class RedEnemy extends Enemy {

	// スタート位置(マップ中心 エネミーハウス上)
	private static final int START_COL = 12;
	private static final int START_ROW = 12;

	// 縄張りエリアの中心（右上）（仮座標）
	private static final int TERRITORY_COL = 24;
	private static final int TERRITORY_ROW = 3;

	public RedEnemy(ImageView imageView) {

		super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1);
	}// 引数を MapData に一本化し、正しいコンストラクタの形に直した

	public RedEnemy(MapData sampleModel) {
		// パックマンと同じくマスの「中心ピクセル座標」を初期位置として親に渡す
		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0,
				START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 2); // スピードは 2

		this.mapData = sampleModel; // 親クラスのフィールドに代入して保持

		// 画像の読み込み処理
		try{
			java.io.InputStream is = getClass().getResourceAsStream("/picture/hayakawa-udekumi.png");
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
		if (this.currentState == Characters.EnemyState.DEAD)
			return deadImage;
		if (this.currentState == Characters.EnemyState.FEVER)
			return feverImage;
		return normalImage;
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {

		// 安全対策: 進める方向がない場合は NONE、または最初の方向を返す
		if (mapData == null || validDirections.isEmpty())
			return Direction.NONE;

		// キーボード操作で動いている本物のパックマン座標(px)をMapDataから取得
		double pacX = mapData.getPacX();
		double pacY = mapData.getPacY();

		// プレイヤーの現在セル座標
		int targetCol = (int) (pacX / MapData.TILE_SIZE);
		int targetRow = (int) (pacY / MapData.TILE_SIZE);

		// プレイヤーへ最短で近づく方向
		return getClosestDirection(validDirections, targetCol, targetRow);
	}
}
*/