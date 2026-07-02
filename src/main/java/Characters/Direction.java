package Characters;

public enum Direction {
	// JavaFXの画面座標は「上がマイナスの値、下がプラスの値」になっている
	
	// 上下左右に1ピクセル移動
	UP(0,-1),
	DOWN(0,1),
	LEFT(-1,0),
	RIGHT(1,0),
	// 押されていない停止状態。始まるまで仙石さんを停止させるため
	NONE(0,0);
	
	private final double dx;
	private final double dy;
	
	// コンストラクタ
	Direction(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	// 移動した量を取得するゲッター
	public double getDX() {return dx;}
	public double getDY() {return dy;}
	
	
	// JavaFXのキー入力(KeyCode)から対応するDirection(動き)を返すメソッド
	public static Direction fromKeyCode(javafx.scene.input.KeyCode code) {
		return switch(code) {
		// 矢印上
		case UP -> UP;		
		// 矢印下
		case DOWN -> DOWN;
		// 矢印左
		case LEFT -> LEFT;
		// 矢印右
		case RIGHT -> RIGHT;
		// それ以外のキー
		default -> NONE;
		
		};
	}
}
