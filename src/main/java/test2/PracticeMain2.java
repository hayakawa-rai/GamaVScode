package test2;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import test2.controller.GameController;
import test2.model.MapData;
import test2.view.MapView;

// パックマン・練習用ステージの起動クラス
public class PracticeMain2 extends Application {

	@Override
	public void start(Stage stage) {

		// 1. ModelとViewの作成
		MapData model = new MapData(); 
		//model.MapData(true);

		// MapData model = new MapData(true); // trueなら練習用

		MapView view = new MapView(model); 

		// 2. 画面サイズ取得
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		double screenW = bounds.getWidth();
		double screenH = bounds.getHeight();

		// HUD（情報表示領域）
		double hudHeight = 50;

		// 3. Canvas作成
		Canvas gameCanvas = new Canvas(screenW, screenH - hudHeight * 2);
		Canvas topHud = new Canvas(screenW, hudHeight);
		Canvas bottomHud = new Canvas(screenW, hudHeight);

		// 4. 画面レイアウト作成
		BorderPane root = new BorderPane();

		// 【変更点】練習用と分かりやすいように背景画像を変更（例: practice_room.jpg）
		String bgUrl = getClass().getResource("/picture/practice_room.jpg").toExternalForm();
		root.setStyle("-fx-background-image: url('" + bgUrl
				+ "'); -fx-background-size: cover; -fx-background-position: center;");
		
		root.setTop(topHud); 
		root.setCenter(gameCanvas); 
		root.setBottom(bottomHud); 

		// 5. シーン生成
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());

		// 6. ウィンドウ設定
		stage.setScene(scene);

		// 【変更点】タイトルを練習用ステージに変更
		stage.setTitle("Pacman MVC - 練習用ステージ");

		stage.setMaximized(true);

		// 7. Controller生成とゲーム開始
		new GameController(model, view, gameCanvas, scene);

		// 8. ウィンドウ表示
		stage.show();

		// キーボード入力を受け取る設定
		gameCanvas.setFocusTraversable(true);
		gameCanvas.requestFocus();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

