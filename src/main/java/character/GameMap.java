package Character;

public interface GameMap {
    // 指定座標が壁かどうかを判定する最小限のメソッド
    boolean isWall(int x, int y);
}
