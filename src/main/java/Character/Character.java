package Character;

public abstract class Character {
	
	private int x; //左右移動
	private int y; //上下移動
	
	private int speed; //移動速度　定義
	private Direction direction; //現在向いている方向
	

	public Character(int speed) { //コンストラクタ
        this.speed = speed;
    }

	public void move() {         //キャラクターの移動処理
		switch(direction) {
			case UP:
				y -= speed; // 上へ移動
                break;

            case DOWN:
                y += speed; // 下へ移動
                break;

            case LEFT:
                x -= speed; // 左へ移動
                break;

            case RIGHT:
                x += speed; // 右へ移動
                break;

            case NONE: //不動
                break;

		}
	}
}
