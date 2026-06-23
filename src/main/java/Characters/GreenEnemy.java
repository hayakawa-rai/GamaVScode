// 遠くにいるときは追跡、近づくと縄張りへ戻る敵（緑）
package Characters;
/*

import java.util.List;
import javafx.scene.image.ImageView;

public class GreenEnemy extends Enemy {

    // マップ中心 エネミーハウス内（仮座標）
    private static final int START_COL = 15;
    private static final int START_ROW = 15;

    // この距離以上ならプレイヤーを追いかける（8マス）
    private static final double BORDER = 8 * CELL_SIZE;

    // 縄張りエリアの中心（左下）（仮座標）
    private static final int TERRITORY_COL = 3;
    private static final int TERRITORY_ROW = 26;

    // 出撃待機用
    private long startTime;

    // 巣から出たかどうか
    private boolean released = false;

    public GreenEnemy(ImageView imageView) {

        super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1);

        startTime = System.currentTimeMillis();
    }

    @Override
    public void move(int[][] map) {

        // ゲーム開始から20秒間は待機
        if (!released) {

            long elapsed =
                    System.currentTimeMillis() - startTime;

            if (elapsed < 20000) {
                return;
            }

            released = true;
        }

        super.move(map);
    }

    @Override
    protected Direction decideNextDirection(
            List<Direction> validDirections,
            int[][] map,
            Sengoku player) {

        // プレイヤーとの距離を取得
        double distance = getDistanceTo(player);

        // プレイヤーが遠い場合は追跡
        if (distance >= BORDER) {

            int playerCol =
                    (int) ((player.getX() + CELL_SIZE / 2.0)
                            / CELL_SIZE);

            int playerRow =
                    (int) ((player.getY() + CELL_SIZE / 2.0)
                            / CELL_SIZE);

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
