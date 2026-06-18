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

public class stageclear1 extends Application{
	//ウィンドウを保存してどのクラスでも共通のウィンドウを使用するため
    private Stage stage;
    //javafxではstartを呼び出さないと起動しないため、親クラスのstartを上書きすることで子クラスを起動
    @Override
    public void start(Stage stage) {
    	//受け取った変数Stageを自分のStageに保存
        this.stage = stage;
        //ウィンドウの中身を決定
        stage.setScene(clear());
        stage.setTitle("stage1CLEAR");
        stage.show();
    }
    public Scene clear() {
    //どこのステージをクリアしたか表示する
    Text title = new Text("STAGE1    CLEAR!");
    //フォントサイズとカラーを指定
    title.setStyle("-fx-font-size: 80px; -fx-fill: rgb(180,180,180);");
    //獲得したアイテムを表示
    Text text = new Text("鍵を獲得しました！！");
    //フォントサイズとカラーを指定
    text.setStyle("-fx-font-size: 20px; -fx-fill: gray;");
    //獲得したアイテムの画像読み込み
    Image image = new Image(
    		getClass().getResource("/kagi.png").toExternalForm()
    );
    //読み込んだ画像を表示
    ImageView imageView = new ImageView(image);
    //画像のサイズ調整
    imageView.setFitWidth(150);  
    imageView.setFitHeight(150);
    
    //横並びにする箱を設定
    HBox textAndImage = new HBox();
    //textと画像の間隔を設定
    textAndImage.setSpacing(10); 
    //中央に配置
    textAndImage.setAlignment(Pos.CENTER);
    //画像とtextを箱に入れる
    textAndImage.getChildren().addAll(imageView,text);

    //縦並びにする箱を設定
    VBox buttonBox = new VBox();
    //ボタン配置に間隔を設定
	buttonBox.setSpacing(20); 
	//中央に配置
	buttonBox.setAlignment(Pos.CENTER);
	
    // 次に進むボタン
    Button next = new Button("次のステージへ");
    //ボタンにcssに記述したgame-button2を付与、ボタンサイズを指定
    next.getStyleClass().add("game-button2");
    next.setPrefSize(250, 80);
    
    // 戻るボタン
    Button backButton = new Button("タイトルへ");
    //ボタンにcssに記述したgame-button2を付与、ボタンサイズを指定
    backButton.getStyleClass().add("game-button2");
    backButton.setPrefSize(250, 80);
    //スタート画面へ戻る
    backButton.setOnAction(e -> {
        start titleScreen = new start();
        try {
            titleScreen.start(stage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });
    //titleと画像とtextをまとめたもの、ボタン2つを箱に入れる。
    buttonBox.getChildren().addAll(title,textAndImage,next, backButton);
    
    //buttonBoxを中身とした1000×800のウィンドウを作成
    Scene scene = new Scene(buttonBox, 1000, 800);
    //CSSを接続
    scene.getStylesheets().add(
        getClass().getResource("/style.css").toExternalForm()
    );
    //画面に表示させたいものを返す
    return scene;
    	
    }
}
