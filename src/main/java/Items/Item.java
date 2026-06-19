package Items;
import Characters.Sengoku;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

public abstract class Item {
    protected int score;
    protected Node view; // CircleもImageViewもNodeとして一括管理

    public Item(int score, Node view) {
        this.score = score;
        this.view = view;
    }

    public Node getView() { return view; }
    public abstract void onEaten(Sengoku player);
    //自分自身が画面に描画できるように勝手にやれの命令
    public abstract void draw(GraphicsContext gc, double x, double y, double tileSize);
}