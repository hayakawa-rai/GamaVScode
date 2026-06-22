package test.test2;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

//パックマン・アプリケーションの起動クラス
public class TestMainapp extends Application {

	//JavaFX起動時に最初に呼ばれるメソッド
    @Override
    public void start(Stage stage) {

    	//ModelとViewの作成
        MapData model = new MapData();  //ゲームデータ(マップ・パックマン状態)
        MapView view = new MapView(model);  //描画担当

        // 画面サイズ取得
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double screenW = bounds.getWidth();
        double screenH = bounds.getHeight();

        //HUD（情報表示領域）
        // HUDの高さ
        double hudHeight = 50;

        //Canvas作成
        //メインゲーム描画用キャンバス
        Canvas gameCanvas = new Canvas(screenW, screenH - hudHeight * 2);
        //上部HUD
        Canvas topHud = new Canvas(screenW, hudHeight);
        //下部HUD
        Canvas bottomHud = new Canvas(screenW, hudHeight);

        //画面レイアウト作成
        BorderPane root = new BorderPane();
        root.setTop(topHud);  //上段
        root.setCenter(gameCanvas);  //中央(ゲーム画面配置)
        root.setBottom(bottomHud);  //下段

        //シーン生成
        Scene scene = new Scene(root);

        //ウィンドウ設定
        stage.setScene(scene);
        
        //ウィンドウタイトル
        stage.setTitle("Pacman MVC");
        
        //起動時に最大化
        stage.setMaximized(true);

        //Controller生成
        //MVCを接続しゲーム開始
        new GameController(model, view, gameCanvas, scene);

        //ウィンドウ表示
        stage.show();

        //キーボード入力を受け取る設定
        gameCanvas.setFocusTraversable(true);
        gameCanvas.requestFocus();
    }

    //プログラム開始地点
    public static void main(String[] args) {
        launch(args);
    }
}