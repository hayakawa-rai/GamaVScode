package test;

import java.util.ArrayList;
import java.util.List;

import test2.model.MapData; 

public abstract class Enemy extends Character {

    protected javafx.scene.image.ImageView imageView;
    protected static final int CELL_SIZE = 30; // 1マスの大きさ
    
    protected MapData mapData;

    protected Characters.EnemyState currentState = Characters.EnemyState.SCATTER;

    protected javafx.scene.image.Image normalImage;
    protected javafx.scene.image.Image feverImage;
    protected javafx.scene.image.Image deadImage;

    public Enemy(double startX, double startY, int speed) {
        super(startX, startY, speed);
    }
    // ★ 抽象メソッドの第3引数を MapData に変更
    protected abstract Direction decideNextDirection(List<Direction> validDirections, int[][] map, MapData mapData);

    @Override
    public void move(int[][] map) {
        // 現在いるマスの行列インデックスを計算
        int tileX = (int) (this.x / CELL_SIZE);
        int tileY = (int) (this.y / CELL_SIZE);

        // 範囲外防止
        if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
            return;
        }

        // 現在のタイルの中心ピクセル座標を計算
        double cx = tileX * CELL_SIZE + CELL_SIZE / 2.0;
        double cy = tileY * CELL_SIZE + CELL_SIZE / 2.0;

        // 現在のスピードの計算
        double currentSpeed = this.getSpeed();
        if (this.currentState == Characters.EnemyState.DEAD) {
            currentSpeed = this.getSpeed() * 2; // 死亡時は爆速
        }

        // タイルの中心に近づいたか判定 (パックマンの atCenter と同期)
        boolean atCenter = Math.abs(this.x - cx) < currentSpeed 
                        && Math.abs(this.y - cy) < currentSpeed;

        // 完全に停止している(NONE)か、マスの中心に到達したら方向転換
        if (this.direction == Direction.NONE || atCenter) {
            List<Direction> validDirections = getValidDirections(map);

            if (!validDirections.isEmpty()) {
                //保持している mapData を引数に渡す
                Direction chosenDirection = decideNextDirection(validDirections, map, this.mapData);

                // 中心にぴったり位置補正（軸ズレによるスタック防止）
                this.x = cx;
                this.y = cy;
                this.direction = chosenDirection;
            } else {
                this.direction = Direction.NONE;
            }
        }

        // 決定した方向に実際に移動する処理
        if (this.direction != Direction.NONE) {
            this.x += this.direction.getDX() * currentSpeed;
            this.y += this.direction.getDY() * currentSpeed;

            // 左右移動中は上下を、上下移動中は左右を中心へとマイルドに補正
			if (this.direction.getDX() != 0) {
				this.y += (cy - this.y) * 0.2;
			}
			if (this.direction.getDY() != 0) {
				this.x += (cx - this.x) * 0.2;
			}
        }
    }

    // 三平方の定理を使って目的地に一番近い方向を選ぶ共通処理
    protected Direction getClosestDirection(List<Direction> validDirections, int targetCol, int targetRow) {
        Direction bestDirection = Direction.NONE;
        double minDistance = Double.MAX_VALUE;

        int currentCol = (int) (this.x / CELL_SIZE);
        int currentRow = (int) (this.y / CELL_SIZE);

        for (Direction dir : validDirections) {
            int nextCol = currentCol + (int) dir.getDX();
            int nextRow = currentRow + (int) dir.getDY();

            double distanceSq = Math.pow(nextCol - targetCol, 2) + Math.pow(nextRow - targetRow, 2);

            if (distanceSq < minDistance) {
                minDistance = distanceSq;
                bestDirection = dir;
            }
        }
        return bestDirection != Direction.NONE ? bestDirection : validDirections.get(0);
    }

    private boolean isOppositeDirection(Direction dir1, Direction dir2) {
        if (dir1 == Direction.NONE || dir2 == Direction.NONE) return false;
        return (dir1.getDX() + dir2.getDX() == 0) && (dir1.getDY() + dir2.getDY() == 0);
    }

    private List<Direction> getValidDirections(int[][] map) {
        List<Direction> list = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (dir == Direction.NONE) continue;

            if (this.currentState != Characters.EnemyState.DEAD && isOppositeDirection(dir, this.direction)) {
                continue; // 原則Uターン禁止
            }
            if (canmove(dir, map)) {
                list.add(dir);
            }
        }
        return list;
    }

    private boolean canmove(Direction direction, int[][] map) {
        if (direction == Direction.NONE) return false;

        int currentCol = (int) (this.x / CELL_SIZE);
        int currentRow = (int) (this.y / CELL_SIZE);

        int nextCol = currentCol + (int) direction.getDX();
        int nextRow = currentRow + (int) direction.getDY();

        if (nextRow < 0 || nextRow >= map.length || nextCol < 0 || nextCol >= map[0].length) {
            return false;
        }
        
        // ゴーストの巣への通常侵入禁止ルール
        if (this.currentState != Characters.EnemyState.DEAD) {
            if (nextRow >= 13 && nextRow <= 15 && nextCol >= 12 && nextCol <= 15) {
                return false;
            }
        }

        return map[nextRow][nextCol] != 1; // 1は壁
    }

    public Characters.EnemyState getCurrentState() { return currentState; }
    public void setCurrentState(Characters.EnemyState state) { this.currentState = state; }
    public double getX() { return x; }
    public double getY() { return y; }
}
