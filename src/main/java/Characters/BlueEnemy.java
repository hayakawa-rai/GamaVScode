package Characters;
/*
public class BlueEnemy extends Enemy {

    private Sengoku player;
    private RedEnemy red; // 赤の位置が必要
    private static final int PREDICT_TILES = 2;
    private static final int CELL_SIZE = 24;

    public BlueEnemy(double x, double y, Sengoku player, RedEnemy red) {
        super(x, y);
        this.player = player;
        this.red = red;
    }

    @Override
    protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku p) {

        // プレイヤーの現在位置
        int px = (int)((player.getX() + CELL_SIZE/2) / CELL_SIZE);
        int py = (int)((player.getY() + CELL_SIZE/2) / CELL_SIZE);

        // プレイヤーの向きの 2 マス先
        switch (player.getDirection()) {
            case UP:    py -= PREDICT_TILES; break;
            case DOWN:  py += PREDICT_TILES; break;
            case LEFT:  px -= PREDICT_TILES; break;
            case RIGHT: px += PREDICT_TILES; break;
            default: break;
        }

        // RedEnemy のマス
        int rx = (int)((red.getX() + CELL_SIZE/2) / CELL_SIZE);
        int ry = (int)((red.getY() + CELL_SIZE/2) / CELL_SIZE);

        // ベクトル計算（予測タイル → RedEnemy）
        int vx = px - rx;
        int vy = py - ry;

        // ベクトルを 2 倍した先をターゲットにする
        int targetX = px + vx;
        int targetY = py + vy;

        // 最も近づく方向を返す（Enemy にある共通メソッド）
        return getClosestDirection(validDirections, targetX, targetY);
    }
    
}*/