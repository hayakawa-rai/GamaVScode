package test.test2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TestMainapp extends Application {
    
    private GameController controller;
    
    @Override
    public void start(Stage stage) {
        GameController.switchToStart(stage);
    }
    
    public void starts(Stage stage) {
        // 多重起動を確実に防止
        if (this.controller != null) {
            this.controller.stop();
        }

        MapData model = new MapData();
        MapView view = new MapView(model);

        Group root = new Group();
        int viewWidth = model.getMap()[0].length * MapData.TILE_SIZE;
        int viewHeight = model.getMap().length * MapData.TILE_SIZE;

        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK);
        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);
        
        model.initEnemy(null);
      /*コメントで隠してるのが前の描写方法  
        // 先に空の ImageView を用意
        javafx.scene.image.ImageView redImageView = new javafx.scene.image.ImageView();
        
        // モデル側で敵を生成し、内部で画像（Image）を確実にセットさせる
        model.initEnemy(redImageView);
        
        //  画像が入った「後」に、ビューを通してサイズを30x30にフィットさせる
        view.setupEnemyView(redImageView);
        
        //  敵の ImageView を画面に登録
        root.getChildren().add(redImageView);
        */
        //敵描画呼び出し　成田
        model.initEnemy(new javafx.scene.image.ImageView());
        
        
        //  完璧に準備ができた【最後】にコントローラーを1回だけ生成（重複は削除！）
        this.controller = new GameController(model, view, canvas, scene);

        stage.setTitle("JavaFX Pacman Stage MVC");
        stage.setScene(scene);
        stage.show();
        
        canvas.requestFocus(); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}
