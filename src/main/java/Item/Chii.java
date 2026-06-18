package Item;


import Character.Sengoku;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// パワーエサ（Chii）
public class Chii extends Item {
    
    // 💡 ここの数値を変更するだけで、いつでも画像の大きさを変えられます！
    private static final double IMAGE_SIZE = 16.0; 

    public Chii(double pixelX, double pixelY) {
        // 親クラス（Item）には、一旦何も持たないダミーのImageViewを渡しておきます
        super(50, new ImageView());
        
        try {
            // 画像ファイルを読み込む で、なぜか画像が読み込まれなくて頭抱えている
        	Image img = new Image(Chii.class.getResourceAsStream("resources/Chii.png"));
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
}