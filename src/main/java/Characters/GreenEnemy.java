// 遠くにいるときは追跡、近づくと自分の縄張りへ戻る GreenEnemy(緑)
package Characters;

/*import java.util.List;
 
import javafx.scene.image.ImageView;


public class GreenEnemy extends Enemy {


    // この距離以上ならプレイヤーを追いかける（8マス）
    private static final double BORDER = 8 * CELL_SIZE;

    // 縄張りエリアの中心（左下）（仮座標）
    private static final int TERRITORY_COL = 3;
	private static final int TERRITORY_ROW = 26;


    // 初期位置(出撃待機)
    private long startTime;

    // リスから出たかどうか
    private boolean released = false;

    public GreenEnemy(ImageView imageView) {

        // エネミーハウス中央付近に生成
        super(imageView, 15 * CELL_SIZE, 15 * CELL_SIZE, 1);

        // 生成時刻を保存
        startTime = System.currentTimeMillis();
    }

    // 移動処理
    // ゲーム開始から20秒間は巣で待機
    @Override
    public void move(int[][] map) {

        // まだ解放されていない場合
        if (!released) {

            // 生成からの経過時間を取得
            long elapsed =
                    System.currentTimeMillis() - startTime;

            // 20秒未満なら待機
            if (elapsed < 20000) {
                return;
            }

            // 20秒経過したので出撃
            released = true;
        }

        // 通常の敵移動処理
        super.move(map);
    }

    // プレイヤーが遠い場合 → 追跡
    // プレイヤーが近い場合 → 縄張り（左下）へ戻る

    @Override
    protected Direction decideNextDirection(
            List<Direction> validDirections,
            int[][] map,
            Sengoku player) {

        // プレイヤーとの距離を取得
        double distance = getDistanceTo(player);

        // プレイヤーが遠い場合は追跡
        if (distance >= BORDER) {

            // プレイヤーのセル座標を取得
            int playerCol =
                    (int)((player.getX() + CELL_SIZE / 2.0)
                            / CELL_SIZE);

            int playerRow =
                    (int)((player.getY() + CELL_SIZE / 2.0)


	// プレイヤーに最も近づく方向を選択
            return getClosestDirection(
                    validDirections,
                    playerCol,
                    playerRow);
        }

        // プレイヤーが近い場合は縄張りへ戻る
       
			return getClosestDirection(
        			validDirections,
        			TERRITORY_COL,
        			TERRITORY_ROW);

    }
}
*/