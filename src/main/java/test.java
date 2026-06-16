import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class test extends Application {
	// 1マス辺りのサイズ（ピクセル）
    private static final int TILE_SIZE = 30;

    // ステージの構造（1: 壁, 0: エサ）
    // 19行 × 19列 のマップ
    private final int[][] map = {
    		{1,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1},	//	■■■■■■■■■　■■■■■■■■■
        {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},	//	■■■■　　　　　　　　　　　■■■■
        {1,0,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,0,1},	//	■　　　　■　■■■■■　■　　　　■
        {1,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,1},	//	■　■■　　　　　■　　　　　■■　■
        {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},	//	■　■■　■■■　■　■■■　■■　■
        {1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1},	//	■　■■　　　　　　　　　　　■■　■
        {1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1},	//	■　　　　■■■■■■■■■　　　　■
        {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},	//	■■■■　　　　　　　　　　　■■■■
        {1,1,1,1,0,1,0,1,1,9,1,1,0,1,0,1,1,1,1},	//	■■■■　■　■■　■■　■　■■■■
        {9,0,0,0,0,1,0,1,9,9,9,1,0,1,0,0,0,0,9},	//	　　　　　■　■　　　■　■　　　　　
        {1,1,1,1,0,1,0,1,9,9,9,1,0,1,0,1,1,1,1},	//	■■■■　■　■　　　■　■　■■■■
        {1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1},	//	■■■■　■　■■■■■　■　■■■■
        {1,0,0,0,0,0,0,0,0,9,0,0,0,0,0,0,0,0,1},	//	■　　　　　　　　　　　　　　　　　■
        {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},	//	■　■■　■■■　■　■■■　■■　■
        {1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1},	//	■　■■　　　　　　　　　　　■■　■
        {1,0,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,0,1},	//	■　■■　■■■■■■■■■　■■　■
        {1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1},	//	■　　　　■■■■■■■■■　　　　■
        {1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1},	//	■■■■　　　　　　　　　　　■■■■
        {1,1,1,1,1,1,1,1,1,9,1,1,1,1,1,1,1,1,1}	//	■■■■■■■■■　■■■■■■■■■
    };

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        
        // マップの大きさに合わせて画面サイズを計算 (横19マス, 縦20マス)
        int viewWidth = map[0].length * TILE_SIZE;
        int viewHeight = map.length * TILE_SIZE;
        
        Scene scene = new Scene(root, viewWidth, viewHeight, Color.BLACK); // 背景を黒に
        
        Canvas canvas = new Canvas(viewWidth, viewHeight);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // ゲームループ
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawStage(gc);
            }
        }.start();

        stage.setTitle("JavaFX Pacman Stage");
        stage.setScene(scene);
        stage.show();
    }

    // ステージを描画するメソッド
    private void drawStage(GraphicsContext gc) {
        // 一度画面を真っ黒にクリア
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, map[0].length * TILE_SIZE, map.length * TILE_SIZE);

        // 2次元配列をループして、壁とエサを配置していく
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                int tile = map[row][col];
                
                // 描画するX座標とY座標を計算
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;

                if (tile == 1) {
                    // 壁の描画（青い四角）
                    gc.setFill(Color.BLUE);
                    gc.fillRect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4); // すき間を空けて網目状に x座標、y座標、幅の大きさ、高さの大きさ
                } else if (tile == 0) {
                    // エサの描画（真ん中に小さな黄色の丸）
                    gc.setFill(Color.YELLOW);
                    int dotSize = 6;
                    gc.fillOval(x + (TILE_SIZE / 2) - (dotSize / 2), 
                                y + (TILE_SIZE / 2) - (dotSize / 2), 
                                dotSize, dotSize);
                }
            }
        }
    }
//コミット
    public static void main(String[] args) {
        launch(args);
    }
}