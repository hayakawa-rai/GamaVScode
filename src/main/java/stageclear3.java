import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class stageclear3 extends Application{
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setScene(clear());
        stage.setTitle("stage3CLEAR");
        stage.show();
    }
    public Scene clear() {
    
    Text title = new Text("STAGE3    CLEAR!");
    title.setStyle("-fx-font-size: 80px; -fx-fill: rgb(180,180,180);");
    
    Text text = new Text("ハンコを獲得しました！！");
    text.setStyle("-fx-font-size: 20px; -fx-fill: gray;");
    Image image = new Image(
    		getClass().getResource("/hanko.png").toExternalForm()
    );

    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(150);  // サイズ調整（重要）
    imageView.setFitHeight(150);


    // 横並びにする
    HBox textAndImage = new HBox();
    textAndImage.setSpacing(10); // 間隔
    textAndImage.setAlignment(Pos.CENTER);
    textAndImage.getChildren().addAll(imageView,text);

    
    VBox buttonBox = new VBox();
	buttonBox.setSpacing(20); // ボタン配置の間隔
	buttonBox.setAlignment(Pos.CENTER);//中央に配置
	
    // 次に進むボタン
    Button next = new Button("次のステージへ");
    next.getStyleClass().add("game-button2");
    next.setPrefSize(250, 80);
    
    // 戻るボタン
    Button backButton = new Button("タイトルへ");
    backButton.getStyleClass().add("game-button2");
    backButton.setPrefSize(250, 80);
    backButton.setOnAction(e -> {
        start titleScreen = new start();
        try {
            titleScreen.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });
    
    buttonBox.getChildren().addAll(title,textAndImage,next, backButton);//ボタンを配置
    
    
    Scene scene = new Scene(buttonBox, 1000, 800);
    scene.getStylesheets().add(
        getClass().getResource("/style.css").toExternalForm()
    );
    return scene;
    	
    }
}
