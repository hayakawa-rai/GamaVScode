package Items;

import Characters.Syujinkou;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

// パワーエサ（Chii）
public class Chii extends Item {

	// ここの数値を変更するだけで、いつでも画面上の画像の大きさを変えられます！
	private static final double IMAGE_SIZE = 37.0;
	
	// ==================================================
	// コンストラクタ
	// ==================================================
	public Chii(double pixelX, double pixelY) {
		// 親クラス（Item）には、一旦何も持たないダミーのImageViewを渡しておく
		super(50, new ImageView());

		try {
			// 画像ファイルを読み込む
			Image img = new Image(Chii.class.getResourceAsStream("/picture/Chii_Item.png"));
			ImageView iv = new ImageView(img);

			// 縦横比を維持
			iv.setPreserveRatio(true);

			// サイズ変更（幅を基準にする）
			iv.setFitWidth(IMAGE_SIZE);

			// マスの中心に画像がくるように位置を調整（サイズの半分を引く）
			iv.setX(pixelX - (IMAGE_SIZE / 2.0));
			iv.setY(pixelY - (IMAGE_SIZE / 2.0));

			// 完成したImageViewを、Itemクラスの「view」にセットする
			this.view = iv;

		} catch (Exception e) {
			System.out.println("画像の読み込みに失敗しました。パスが正しいか確認してください。");
			
			// 画像が読み込めなかった時の保険として、臨時の色円を表示させておく設定
			this.view = new Circle(pixelX, pixelY, 8, javafx.scene.paint.Color.CHARTREUSE);
		}
	}
	// ==================================================
	// 食べる処理
	// ==================================================
	@Override
	public void onEaten(Syujinkou player) {
		player.addScore(this.score);
	}
	
	// ==================================================
	// 描画処理
	// ==================================================
	@Override
	public void draw(GraphicsContext gc, double x, double y, double tileSize) {

		// 画像が正常に読み込めている場合
		if (this.view instanceof ImageView) {
			Image img = ((ImageView) this.view).getImage();
			if (img != null) {

				// Canvas（GraphicsContext）を使って画像を中央に描画
				gc.drawImage(
						img,
						x + tileSize / 2.0 - (IMAGE_SIZE / 2.0),
						y + tileSize / 2.0 - (IMAGE_SIZE / 2.0),
						IMAGE_SIZE, IMAGE_SIZE);
				
				// 描画が終わったので終了
				return;
			}
		}

		// 画像がない場合は、catchで生成した黄緑の円を描画する
		Circle circle = (Circle) this.view;
		double radius = circle.getRadius();
		gc.setFill(circle.getFill());
		gc.fillOval(
				x + tileSize / 2.0 - radius,
				y + tileSize / 2.0 - radius,
				radius * 2, radius * 2);
	}
	
	// ==================================================
	// getter
	// ==================================================
	// 外部から画像単体を取り出したい時用
	public Image getImage() {
		if (this.view instanceof ImageView) {
			return ((ImageView) this.view).getImage();
		}
		return null;
	}

	// 外部からサイズを知りたい時用
	public double getSize() {
		return IMAGE_SIZE;
	}
}