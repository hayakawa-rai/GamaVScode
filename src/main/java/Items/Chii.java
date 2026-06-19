package Items;

import Characters.Sengoku;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

// パワーエサ（Chii）
public class Chii extends Item {
    
    // 💡 ここの数値を変更するだけで、いつでも画像の大きさを変えられます！
    private static final double IMAGE_SIZE = 16.0; 

    public Chii(double pixelX, double pixelY) {
        // 親クラス（Item）には、一旦何も持たないダミーのImageViewを渡しておきます
        super(50, new ImageView());
        
        try {
            // 画像ファイルを読み込む で、なぜか画像が読み込まれなくて頭抱えている
        	Image img = new Image(Chii.class.getResourceAsStream("/Chii.png"));
            ImageView iv = new ImageView(img);
            
            // 💡 画像のサイズを自由に変更（上で設定したIMAGE_SIZEになります）
            iv.setFitWidth(IMAGE_SIZE); 
            iv.setFitHeight(IMAGE_SIZE);
            
            // マスの中心に画像がくるように、位置を調整（サイズの半分を引く）
            iv.setX(pixelX - (IMAGE_SIZE / 2.0)); 
            iv.setY(pixelY - (IMAGE_SIZE / 2.0));
            
            // 完成したImageViewを、Itemクラスの「view」に上書きセットする
            this.view = iv;
            
        } catch (Exception e) {
            System.out.println("画像の読み込みに失敗しました。パスが正しいか確認してください。");
            // 画像が読み込めなかった時の保険として、臨時の色円を表示させておく
            this.view = new javafx.scene.shape.Circle(pixelX, pixelY, 8, javafx.scene.paint.Color.CHARTREUSE);
        }
    }

    @Override
    public void onEaten(Sengoku player) {
        player.addScore(this.score);
        //ここにゴーストをイジケさせる処理を追加予定！！
    }
        public Image getImage() {
            if (this.view instanceof ImageView) {
                return ((ImageView) this.view).getImage();
            }
            return null;
        }

        // 💡 【新設】設定された画像のサイズを返す
        public double getSize() {
            return IMAGE_SIZE;
        }
        @Override
        public void draw(GraphicsContext gc, double x, double y, double tileSize) {
            if (this.view instanceof ImageView) {
                Image img = ((ImageView) this.view).getImage();
                if (img != null) {
                    gc.drawImage(
                        img,
                        x + tileSize / 2.0 - (IMAGE_SIZE / 2.0),
                        y + tileSize / 2.0 - (IMAGE_SIZE / 2.0),
                        IMAGE_SIZE, IMAGE_SIZE
                    );
                    return;
                }
            }
            
            // 保険用の黄緑の円
            Circle circle = (Circle) this.view;
            double radius = circle.getRadius();
            gc.setFill(circle.getFill());
            gc.fillOval(x + tileSize / 2.0 - radius, y + tileSize / 2.0 - radius, radius * 2, radius * 2);
        }
}