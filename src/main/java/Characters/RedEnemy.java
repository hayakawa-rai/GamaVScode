package Characters;

public class RedEnemy extends Enemy {

	public RedEnemy(int x, int y) {
		super(x, y, 1);
	}
	
	@Override
	public void move(Map map, Sengoku sengoku){
		
		int dx = sengoku.getX() - x;
		int dy = sengoku.getY() - y;
		
		if(Math.abs(dx) > Math.abs(dy)) {
			x += Integer.signum(dx);
		} else {
			y += Integer.signum(dy);
			
		}
	}
}