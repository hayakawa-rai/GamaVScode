// 遠くにいるときは追跡、近づくと縄張りへ戻る敵（緑）
package test;

import java.util.List;

import javafx.scene.image.Image;
import test.test2.MapData;

public class GreenEnemy extends Enemy {

	// マップ中心 エネミーハウス内（仮座標）
	private static final int START_COL = 12;
	private static final int START_ROW = 12;

	// この距離以上ならプレイヤーを追いかける（8マス）
	private static final double BORDER = 8 * MapData.TILE_SIZE;

	// 縄張りエリアの中心（左下）（仮座標）
	private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 26;

	// 出撃待機用
	private long startTime;

	// 巣から出たか
	private boolean released = false;

	public GreenEnemy(MapData mapData) {
		// ⭕ 軸ズレ・スタック防止のため、マスの「中心ピクセル座標」を計算して親に渡すように修正
		super(START_COL * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 
		      START_ROW * MapData.TILE_SIZE + MapData.TILE_SIZE / 2.0, 
		      1); // スピードは 1

		this.mapData = mapData; // 親クラスのフィールドに保持
		this.startTime = System.currentTimeMillis();

		// 画像の読み込み処理
		try {
			java.io.InputStream is = getClass().getResourceAsStream("/picture/narinari.png");
			if (is == null) {
				System.err.println("❌【エラー】画像が見つかりません");
			} else {
				this.normalImage = new Image(is);
				System.out.println("⭕【成功】narinariの画像を読み込みました！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ⭕ MapView から現在の画像を取り出して中心補正描画を行うためのゲッターメソッドを追加！
	public Image getEnemyImage() {
		if (this.currentState == Characters.EnemyState.DEAD) return deadImage;
		if (this.currentState == Characters.EnemyState.FEVER) return feverImage;
		return normalImage;
	}
	
	@Override
	public void move(int[][] map) {
		// ゲーム開始から20秒間は待機
		if (!released) {
			long elapsed = System.currentTimeMillis() - startTime;
			if (elapsed < 20000) {
				return;
			}
			released = true;
		}
		super.move(map);
	}

	@Override
	protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData) {
		// 安全対策: 進める方向がない場合は NONE、または最初の方向を返す
		if (mapData == null || validDirections.isEmpty())
			return Direction.NONE;

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
