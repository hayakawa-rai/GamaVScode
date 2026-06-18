package test;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class test extends Application {

    private static final int TILE_SIZE = 30;

    // 0: 道, 1: 壁, 9: ワープ
    private final int[][] map = {
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
            { 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
            { 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
            { 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1 },
            { 9, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 9 },
            { 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1 },
            { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
            { 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
            { 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
            { 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 },
            { 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
            { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
    };

    // パックマンの位置（タイル中央）
    private double pacX = 10 * TILE_SIZE + TILE_SIZE / 2;
    private double pacY = 14 * TILE_SIZE + TILE_SIZE / 2;

    // 現在の方向
    private int dirX = 1;
    private int dirY = 0;

    // 入力された方向（タイル中央でのみ反映）
    private int nextDirX = 1;
    private int nextDirY = 0;

    private double speed = 2.0;
    private boolean paused = false;

    // 口パク
    private double mouthAngle = 45;
    private int mouthOpening = -1;
    private boolean isBlocked = false;

    // ワープ抑止用
    private boolean justWarped = false;
    private int lastWarpX = -1;
    private int lastWarpY = -1;

    @Override
    public void start(Stage stage) {

        Group root = new Group();
        int viewWidth = map[0].length * TILE_SIZE;
        int viewHeight = map.length * TILE_SIZE;

        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);

        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();

            if (code == KeyCode.P) {
                paused = !paused;
                return;
            }
            if (paused)
                return;

            if (code == KeyCode.W) {
                nextDirX = 0;
                nextDirY = -1;
            }
            if (code == KeyCode.S) {
                nextDirX = 0;
                nextDirY = 1;
            }
            if (code == KeyCode.A) {
                nextDirX = -1;
                nextDirY = 0;
            }
            if (code == KeyCode.D) {
                nextDirX = 1;
                nextDirY = 0;
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePacman();
                updateMouth();
                drawStage(gc);
                drawPacman(gc);
            }
        }.start();

        stage.setTitle("JavaFX Pacman Stage");
        stage.setScene(scene);
        stage.show();
    }

    private void updatePacman() {
        if (paused)
            return;

        int tileX = (int) (pacX / TILE_SIZE);
        int tileY = (int) (pacY / TILE_SIZE);

        // --- ワープ抑止ロジック ---
        boolean skipWarp = false;
        if (justWarped) {
            if (tileX == lastWarpX && tileY == lastWarpY) {
                skipWarp = true; // まだワープ先にいる -> ワープ判定しない
            } else {
                // ワープ先を離れたら抑止解除
                justWarped = false;
                lastWarpX = -1;
                lastWarpY = -1;
            }
        }

        // --- ワープ処理（9 を踏んだら反対側の 9 に飛ぶ） ---
        if (!skipWarp && tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length) {
            if (map[tileY][tileX] == 9) {

                int warpX = tileX;
                int warpY = tileY;

                // 横方向ワープ（左右）
                if (dirX != 0) {
                    for (int x = 0; x < map[0].length; x++) {
                        if (map[tileY][x] == 9 && x != tileX) {
                            warpX = x;
                            break;
                        }
                    }
                }

                // 縦方向ワープ（上下）
                if (dirY != 0) {
                    for (int y = 0; y < map.length; y++) {
                        if (map[y][tileX] == 9 && y != tileY) {
                            warpY = y;
                            break;
                        }
                    }
                }

                // ワープ先へ移動（タイル中央）
                pacX = warpX * TILE_SIZE + TILE_SIZE / 2;
                pacY = warpY * TILE_SIZE + TILE_SIZE / 2;

                // ワープ抑止を有効化（ワープ先の座標を記憶）
                justWarped = true;
                lastWarpX = warpX;
                lastWarpY = warpY;

                return; // このフレームは終了
            }
        }

        // --- ここから方向転換 ---
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

        // --- 次のタイル ---
        int nextTileX = tileX + dirX;
        int nextTileY = tileY + dirY;

        // 範囲外はワープ処理に任せるので止めない
        if (nextTileX >= 0 && nextTileX < map[0].length &&
            nextTileY >= 0 && nextTileY < map.length) {

            // 壁判定
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

        // --- 移動 ---
        pacX += dirX * speed;
        pacY += dirY * speed;

        // 中央吸着
        if (dirX != 0)
            pacY = tileY * TILE_SIZE + TILE_SIZE / 2;
        if (dirY != 0)
            pacX = tileX * TILE_SIZE + TILE_SIZE / 2;
    }

    private void updateMouth() {
        if (paused || isBlocked)
            return;

        mouthAngle += mouthOpening * 2;

        if (mouthAngle <= 10)
            mouthOpening = +1;
        if (mouthAngle >= 45)
            mouthOpening = -1;
    }

    private void drawStage(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, map[0].length * TILE_SIZE, map.length * TILE_SIZE);

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int tile = map[row][col];
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                if (tile == 1) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
                }
            }
        }
    }

    private void drawPacman(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);

        double startAngle = 0;

        if (dirX == 1)
            startAngle = mouthAngle; // 右
        if (dirX == -1)
            startAngle = 180 + mouthAngle; // 左
        if (dirY == -1)
            startAngle = 90 + mouthAngle; // 上
        if (dirY == 1)
            startAngle = 270 + mouthAngle; // 下

        gc.fillArc(
                pacX - TILE_SIZE / 2,
                pacY - TILE_SIZE / 2,
                TILE_SIZE, TILE_SIZE,
                startAngle,
                360 - mouthAngle * 2,
                javafx.scene.shape.ArcType.ROUND);
    }
}
