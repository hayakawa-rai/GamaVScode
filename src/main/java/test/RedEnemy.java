// 最短で追いかける RedEnemy(赤)
package test;

import java.util.List;

import Characters.Sengoku;
import javafx.scene.image.ImageView;

// 仙石さんを最短距離で追いかける
public class RedEnemy extends Enemy {

    // マップ中心 エネミーハウス上 （仮座標）
    // ※現在のSampleModelのマップ(19x19)に合わせて、初期位置を[行11, 列9]付近の道に設定しておきます
    private static final int START_COL = 9;
    private static final int START_ROW = 11;

    // 【修正】引数に Sengoku player を追加し、親クラス(super)へ正しく渡す
    public RedEnemy(ImageView imageView, Characters.Sengoku sengoku) {
        // 引数: ImageView, 開始X, 開始Y, スピード(1), プレイヤー
        super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1, sengoku);
        
        // ★ここにゴーストの画像を読み込む処理を入れると、画面に姿が表示されます！
        // this.normalImage = new javafx.scene.image.Image(getClass().getResourceAsStream("/images/red_ghost.png"));
        // if (this.normalImage != null) imageView.setImage(this.normalImage);
    }

    // 次に進む方向を決定
    @Override
    protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player) {

        // プレイヤーの現在マス
        int targetCol = (int) ((player.getX() + CELL_SIZE / 2.0) / CELL_SIZE);
        int targetRow = (int) ((player.getY() + CELL_SIZE / 2.0) / CELL_SIZE);

        // 一番近づける方向をEnemyクラスに選ばせる
        return getClosestDirection(validDirections, targetCol, targetRow);
    }
}
