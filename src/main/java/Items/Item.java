package Items;

import Characters.Syujinkou;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext; // 💡 Canvas描画に必須のインポート

public abstract class Item {
    protected int score;
    protected Node view;

    public Item(int score, Node view) {
        this.score = score;
        this.view = view;
    }
    
    // 食べた時の処理（子クラスでそれぞれ中身を実装する）
    public abstract void onEaten(Syujinkou player);
    
    // 自分自身を画面（Canvas）に描画する命令
    public abstract void draw(GraphicsContext gc, double x, double y, double tileSize);

    // ==================================================
    // getter
    // ==================================================
    public Node getView() { 
        return view; 
    }
}