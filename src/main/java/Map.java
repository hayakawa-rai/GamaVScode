
public class Map {
	private static final int CELL_SIZE = 24;
	//空の二次元配列。Controllerクラスからもらったデータを使う。
	private int[][] mapdata;
	
	public Map(int[][] selectmap) {
		this.mapdata = selectmap;
	}
}
