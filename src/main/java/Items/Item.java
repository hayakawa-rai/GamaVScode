package Items;

import Characters.Sengoku;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext; // 💡 Canvas描画に必須のインポート

public abstract class Item {
    // 子クラスからもアクセスできるように pryzotected にしてあります
    protected int score;
    protected Node view;

    public Item(int score, Node view) {
        this.score = score;
        this.view = view;
    }

    // ゲッターメソッド
    public Node getView() { 
        return view; 
    }
    
    // 食べた時の処理（子クラスでそれぞれ中身を実装する）
    public abstract void onEaten(Sengoku player);
    
    // 自分自身を画面（Canvas）に描画する命令
    // これがあるおかげで、View側は「item.draw(...)」と呼ぶだけで適切なサイズや画像で描画されるよ！
    public abstract void draw(GraphicsContext gc, double x, double y, double tileSize);
}