
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class start extends Application {

	@Override
	public void start(Stage stage) {

		Image bgImage = new Image(
				getClass().getResource("/background.png").toExternalForm());

		// 背景用ImageView
		ImageView bg1 = new ImageView(bgImage);
		ImageView bg2 = new ImageView(bgImage);

		// 横に並べる（ループ用）
		bg2.setLayoutX(bgImage.getWidth());

		// アニメーション
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				bg1.setLayoutX(bg1.getLayoutX() - 1);
				bg2.setLayoutX(bg2.getLayoutX() - 1);

				// 画面外に出たらループ
				if (bg1.getLayoutX() + bgImage.getWidth() <= 0) {
					bg1.setLayoutX(bg2.getLayoutX() + bgImage.getWidth());
				}
				if (bg2.getLayoutX() + bgImage.getWidth() <= 0) {
					bg2.setLayoutX(bg1.getLayoutX() + bgImage.getWidth());
				}
			}
		};

		timer.start();

		StackPane root = new StackPane();

		// 背景用（自由に動かす）
		Pane bgPane = new Pane();
		bgPane.getChildren().addAll(bg1, bg2);//背景配置

		VBox ui = new VBox();
		ui.setSpacing(1); //画像とボタンの間隔
		ui.setAlignment(Pos.CENTER);//中央に配置

		Image image = new Image(
				getClass().getResource("/title.png").toExternalForm());
		ImageView imageView = new ImageView(image);//画像読み込み
		imageView.setFitWidth(500); // 幅
		imageView.setFitHeight(400); // 高さ
		imageView.setPreserveRatio(true); // 縦横比維持

		VBox buttonBox = new VBox();
		buttonBox.setSpacing(20); // ボタン配置の間隔
		buttonBox.setAlignment(Pos.CENTER);//中央に配置

		Button btn1 = new Button("▶ストーリー");
		btn1.setPrefSize(200, 50); //ストーリーボタン作成
		btn1.getStyleClass().add("game-button");
        
		Button btn2 = new Button("⚔練習モード");
		btn2.setPrefSize(200, 50); //練習モードボタン作成

		btn2.setOnAction(e -> {
			practice practiceScreen = new practice();
			try {
				practiceScreen.start(stage);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});	
		btn2.getStyleClass().add("game-button");

		Button btn3 = new Button("∞無限モード");
		btn3.setPrefSize(200, 50); //無限モードボタン作成
		btn3.getStyleClass().add("game-button");

		buttonBox.getChildren().addAll(btn1, btn2, btn3);//ボタンを配置
		
        // 全体に追加
        ui.getChildren().addAll(imageView, buttonBox);
        root.getChildren().addAll(bgPane,ui);
        
        Scene scene = new Scene(root,800,600);
        scene.getStylesheets().add(
        	    getClass().getResource("/style.css").toExternalForm()
        );
        stage.setTitle("スタート画面");//画面の名前
        stage.setScene(scene);
        stage.show();
    }


	public static void main(String[] args) {
		launch();
	}

}
