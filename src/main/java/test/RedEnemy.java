package test;

import java.util.List;

import Characters.Sengoku;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RedEnemy extends Enemy {

    private static final int START_COL = 13;
    private static final int START_ROW = 11;

    public RedEnemy(ImageView imageView, Sengoku sengoku) {
        super(imageView, START_COL * CELL_SIZE, START_ROW * CELL_SIZE, 1, sengoku);
        
        try {
            java.io.InputStream is = getClass().getResourceAsStream("/picture/hayakawa-udekumi.png");
            
            if (is == null) {
                System.err.println("❌【重大エラー】画像 '/picture/hayakawa-udekumi.png' が見つかりません！");
            } else {
                this.normalImage = new Image(is);
                this.imageView.setImage(this.normalImage);
                System.out.println("⭕【大成功】早川さんの画像を敵キャラに設定しました！");
            }
        } catch (Exception e) {
            System.err.println("❌【例外発生】画像読み込み中にエラーが起きました。");
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
