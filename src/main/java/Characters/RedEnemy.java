package Characters;

//最短距離で追跡する敵 赤
public class RedEnemy extends Enemy {

    // 仮の初期位置（後で修正）
    private static final int START_X = 700;
    private static final int START_Y = 50;

    //マップ右上に出現
    public RedEnemy() {
        super(START_X, START_Y, 1);
    }

    //追跡
    public void move(int[][] map, Sengoku sengoku) {

        // 仙石さんとの距離
        double dx = sengoku.getX() - this.x;
        double dy = sengoku.getY() - this.y;

        // 横方向が遠い場合
        if (Math.abs(dx) > Math.abs(dy)) {
            this.x += Math.signum(dx) * speed;
        }
        // 縦方向が遠い場合
        else {
            this.y += Math.signum(dy) * speed;
        }
    }

    //Characterクラスの抽象メソッド
    //現状は使用しないため空実装
    @Override
    public void move(int[][] map) {

    }
}