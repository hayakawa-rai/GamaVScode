package test;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import test.controller.SampleController;
import test.model.SampleModel;
import test.view.SampleView;

public class SampleMainApp extends Application {
	
	  //コントローラーが消滅（GC）しないように保持する変数を用意
	private SampleController controller;
	
	@Override
    public void start(Stage stage) {
        // 起動した瞬間に、ボタン操作なしでコントローラーの遷移処理を呼び出す
        SampleController.switchToStart(stage);
    }
    
    public void starts(Stage stage) {
        SampleModel model = new SampleModel();
        SampleView view = new SampleView(model);

        Group root = new Group();
        int viewWidth = model.getMap()[0].length * SampleModel.TILE_SIZE;
        int viewHeight = model.getMap().length * SampleModel.TILE_SIZE;

        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);
        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);
        
        // ★【追加】敵の見た目となる ImageView を生成
        javafx.scene.image.ImageView enemyImageView = new javafx.scene.image.ImageView();
        
        // ★【追加】ビューを通してImageViewのサイズなどを調整
        view.setupEnemyView(enemyImageView);
        
        // ★【追加】モデル側で敵クラス（Blinky等）をインスタンス化
        model.initEnemy(enemyImageView);
        
        // ★【追加】敵の ImageView を画面(root)に表示対象として登録する！
        root.getChildren().add(enemyImageView);
        
        //  メンバ変数（controller）に代入して、インスタンスの消滅を防ぐ
        this.controller = new SampleController(model, view, canvas, scene);

        // SampleController を起動
        new SampleController(model, view, canvas, scene);

        stage.setTitle("JavaFX Pacman Stage MVC");
        stage.setScene(scene);
        stage.show();
        
        // キーボード入力を確実に受け付けるためにフォーカスを当てる
        canvas.requestFocus(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}
