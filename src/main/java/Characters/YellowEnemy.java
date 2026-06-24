/*package Characters;

import java.util.List;

import javafx.scene.image.ImageView;
import test.Enemy;

public class YellowEnemy extends Enemy {

	private Sengoku player; // プレイヤー
	
    private static final int PREDICT_TILES = 4;
    private static final int CELL_SIZE = 24;

    private long startTime;
    private static final long DELAY = 10000; // 10秒遅れて出発

    // エネミーハウス左上（仮）
    private static final int START_COL = 13;
    private static final int START_ROW = 11;

    public YellowEnemy(ImageView imageView, Sengoku player) {
        super(START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1);
        this.player = player;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player) {

        // 10秒遅延
        if (System.currentTimeMillis() - startTime < DELAY) {
            return Direction.NONE; 
        }

        // プレイヤーの現在セル
        int pCol = (int) ((player.getX() + CELL_SIZE / 2.0) / CELL_SIZE);
        int pRow = (int) ((player.getY() + CELL_SIZE / 2.0) / CELL_SIZE);

        int targetCol = pCol;
        int targetRow = pRow;

        // プレイヤーの向きに応じて4マス先を狙う
        switch (player.getDirection()) {
            case UP:    targetRow -= PREDICT_TILES; break;
            case DOWN:  targetRow += PREDICT_TILES; break;
            case LEFT:  targetCol -= PREDICT_TILES; break;
            case RIGHT: targetCol += PREDICT_TILES; break;
            default: break;
        }

     // 親クラスの最短ルート計算メソッドにターゲットマスを渡して、次の一歩を決める
     		return getClosestDirection(validDirections, targetCol, targetRow);
    }
}*/
