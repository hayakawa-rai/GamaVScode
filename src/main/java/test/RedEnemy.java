// 最短で追いかける RedEnemy(赤)
package test;

import java.util.List;

import Characters.Sengoku;
import javafx.scene.image.Image; // ★インポートを追加
import javafx.scene.image.ImageView;

public class RedEnemy extends Enemy {

    private static final int START_COL = 9;
    private static final int START_ROW = 9;

    public RedEnemy(ImageView imageView, Sengoku sengoku) {
        super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1, sengoku);
        
        //画像を読み込んでエネミーに設定する処理
        try {
            // resources/images/red_ghost.png を読み込む
            this.normalImage = new Image(getClass().getResourceAsStream("/picture/hayakawa-udekumi.png"));
            
            if (this.normalImage != null) {
                // 読み込みに成功したらImageViewにセット
                this.imageView.setImage(this.normalImage);
            }
        } catch (Exception e) {
            System.out.println("赤ゴーストの画像読み込みに失敗しました。パスを確認してください。");
            e.printStackTrace();
        }
    }

    @Override
    protected Direction decideNextDirection(List<Direction> validDirections, int[][] map, Sengoku player) {
        int targetCol = (int) ((player.getX() + CELL_SIZE / 2.0) / CELL_SIZE);
        int targetRow = (int) ((player.getY() + CELL_SIZE / 2.0) / CELL_SIZE);
        return getClosestDirection(validDirections, targetCol, targetRow);
    }
}
