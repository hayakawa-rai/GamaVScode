package test.model;

public class SampleModel {

    public static final int TILE_SIZE = 30;

    // 0: 道, 1: 壁, 9: ワープ
    private final int[][] map = {
        { 1,1,1,1,1,1,1,1,1,9,1,1,1,1,1,1,1,1,1 },
        { 1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1 },
        { 1,0,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,0,1 },
        { 1,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,1 },
        { 1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1 },
        { 1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1 },
        { 1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1 },
        { 1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1 },
        { 1,1,1,1,0,1,0,1,1,0,1,1,0,1,0,1,1,1,1 },
        { 9,0,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,9 },
        { 1,1,1,1,0,1,0,1,0,0,0,1,0,1,0,1,1,1,1 },
        { 1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1 },
        { 1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1 },
        { 1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1 },
        { 1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1 },
        { 1,0,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,0,1 },
        { 1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1 },
        { 1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1 },
        { 1,1,1,1,1,1,1,1,1,9,1,1,1,1,1,1,1,1,1 }
    };

    // パックマンの状態（タイル中央）
    private double pacX = 10 * TILE_SIZE + TILE_SIZE / 2;
    private double pacY = 14 * TILE_SIZE + TILE_SIZE / 2;

    private int dirX = 1;
    private int dirY = 0;

    private int nextDirX = 1;
    private int nextDirY = 0;

    private double speed = 2.0;
    private boolean paused = false;

    // 口パク
    private double mouthAngle = 45;
    private int mouthOpening = -1;
    private boolean isBlocked = false;

    // ワープ抑止
    private boolean justWarped = false;
    private int lastWarpX = -1;
    private int lastWarpY = -1;

    // --- getters / setters ---
    public int[][] getMap() { return map; }
    public double getPacX() { return pacX; }
    public double getPacY() { return pacY; }
    public double getMouthAngle() { return mouthAngle; }
    public int getDirX() { return dirX; }
    public int getDirY() { return dirY; }
    public boolean isPaused() { return paused; }
    public boolean isBlocked() { return isBlocked; }

    public void setNextDirection(int nx, int ny) {
        nextDirX = nx;
        nextDirY = ny;
    }

    public void togglePause() {
        paused = !paused;
    }

    // --- 更新ロジック（外部から呼ぶ） ---
    public void updatePacman() {
        if (paused) return;

        int tileX = (int) (pacX / TILE_SIZE);
        int tileY = (int) (pacY / TILE_SIZE);

        // ワープ抑止ロジック
        boolean skipWarp = false;
        if (justWarped) {
            if (tileX == lastWarpX && tileY == lastWarpY) {
                skipWarp = true;
            } else {
                justWarped = false;
                lastWarpX = -1;
                lastWarpY = -1;
            }
        }

        // ワープ処理
        if (!skipWarp && tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length) {
            if (map[tileY][tileX] == 9) {
                int warpX = tileX;
                int warpY = tileY;

                if (dirX != 0) {
                    for (int x = 0; x < map[0].length; x++) {
                        if (map[tileY][x] == 9 && x != tileX) {
                            warpX = x;
                            break;
                        }
                    }
                }
                if (dirY != 0) {
                    for (int y = 0; y < map.length; y++) {
                        if (map[y][tileX] == 9 && y != tileY) {
                            warpY = y;
                            break;
                        }
                    }
                }

                pacX = warpX * TILE_SIZE + TILE_SIZE / 2;
                pacY = warpY * TILE_SIZE + TILE_SIZE / 2;

                justWarped = true;
                lastWarpX = warpX;
                lastWarpY = warpY;

                return;
            }
        }

        // 方向転換（タイル中央でのみ）
        boolean atCenter = Math.abs(pacX - (tileX * TILE_SIZE + TILE_SIZE / 2)) < 2 &&
                           Math.abs(pacY - (tileY * TILE_SIZE + TILE_SIZE / 2)) < 2;

        if (atCenter) {
            int nx = tileX + nextDirX;
            int ny = tileY + nextDirY;
            if (nx >= 0 && nx < map[0].length &&
                ny >= 0 && ny < map.length &&
                map[ny][nx] != 1) {
                dirX = nextDirX;
                dirY = nextDirY;
            }
        }

        // 次のタイル判定
        int nextTileX = tileX + dirX;
        int nextTileY = tileY + dirY;

        if (nextTileX >= 0 && nextTileX < map[0].length &&
            nextTileY >= 0 && nextTileY < map.length) {

            if (map[nextTileY][nextTileX] == 1) {
                isBlocked = true;
                double centerX = tileX * TILE_SIZE + TILE_SIZE / 2;
                double centerY = tileY * TILE_SIZE + TILE_SIZE / 2;
                pacX += (centerX - pacX) * 0.3;
                pacY += (centerY - pacY) * 0.3;
                return;
            }
        }

        isBlocked = false;

        // 移動
        pacX += dirX * speed;
        pacY += dirY * speed;

        if (dirX != 0) pacY = tileY * TILE_SIZE + TILE_SIZE / 2;
        if (dirY != 0) pacX = tileX * TILE_SIZE + TILE_SIZE / 2;
    }

    public void updateMouth() {
        if (paused || isBlocked) return;
        mouthAngle += mouthOpening * 2;
        if (mouthAngle <= 10) mouthOpening = +1;
        if (mouthAngle >= 45) mouthOpening = -1;
    }
}
