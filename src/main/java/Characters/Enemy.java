package Characters;

public class Enemy extends Character{
	
	//敵の現在の状態を管理する変数(EnemyState型)
	private EnemyState state = EnemyState.NORMAL;
	
	//ゲーム用の変数:1マスのサイズと、当たり判定に使うキャラクターのサイズ
	private final double bliockSize = 28.0;
	private final double characterSize = 24.0;
	
	//状態ごとの画像(こんな感じで書く(多分))
	//Image img = new Image(Chii.class.getResourceAsStream("/Chii.png"));通常状態敵
	//Image img = new Image(Chii.class.getResourceAsStream("/Chii.png"));パワーアイテムを取得して逃げてる敵
	//Image img = new Image(Chii.class.getResourceAsStream("/Chii.png"));食べられて初期地点に戻る敵
	
	//コンストラクタ
	public Enemy(ImageView imageView,double x,double y,int speed) {
		super(imageView,x,y,speed);
		this.imageView.setImage(normalImage);
	}
	
	@Override
	public void move() {
		double originalSpeed = this.speed;
		//Enumの値を使って条件分岐
		if(this.state == EnemyState.FFEVER) {
		   this.speed = originalSpeed * 0.5;
		}else if (this.state == EnemyState.DEAD) {
			this.speed = 
		}
	}

}
