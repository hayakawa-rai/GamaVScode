//package Items;
//
//import java.util.Map;
//
//import Characters.Sengoku;
//import javafx.scene.Scene;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//import scene.Story1;
//import scene.Story2;
//import scene.Story3;
//
//public class GameController {
//	
//	private Stage stage;
//    private Story1 storyScreen; // ストーリー画面のインスタンスを保持
//
//    // ストーリーの進行状況を管理する変数
//    private int currentPage = 1;
//    
//	private Map gameMap;         	      // MAP
//    private Sengoku player;                 // プレイヤー（パックマン）
////    private List<Enemy> ghosts;            // 敵（ゴースト）のリスト
//    private boolean isRunning = false;     // ゲームが動いているか（falseの時は一時停止、ゲームオーバー時）
//    
// // start.java から最初に呼ばれる
//    public void start(Stage stage) {
//        this.stage = stage;
//        
//        // 最初の画面「Story1」を表示
//        showStoryPage(1);
//    }
//    
//    public void showStoryPage(int page) {
//    		Scene nextScene = null;
//        
//    		// ページ番号に応じて、別々のクラスのインスタンスを作る
//    		switch (page) {
//    			case 1 -> {
//    				Story1 page1 = new Story1(this);
//    				nextScene = page1.createScene();
//                }
//                case 2 -> {
//                		Story2 page2 = new Story2(this);
//                		nextScene = page2.createScene();
//                }
//                case 3 -> {
//                		Story3 page3 = new Story3(this);
//                		nextScene = page3.createScene();
//                }
//    		}
//    		if (nextScene != null) {
//    			this.stage.setScene(nextScene);
//    		}
//    }
//    // 各ストーリー画面で「キー入力」があったら呼ばれるメソッド
//    public void advanceStory() {
//        currentPage++;
//
//        if (currentPage <= 3) {
//            // 3ページ目までは順番に画面を切り替える
//            showStoryPage(currentPage);
//        } else {
//            // 3ページを超えたらゲーム本番へ
//            proceedToStage1();
//        }
//    }
//
//    // 各ストーリー画面で「スキップボタン」が押されたら呼ばれるメソッド
//    public void skipToGame() {
//        System.out.println("ストーリーがスキップされました。");
//        proceedToStage1(); // 途中のページを飛ばして本番へ
//    }
//
// // ゲーム本番（Stage1）への遷移
//    private void proceedToStage1() {
//
//        // ==========================================
//        // 2. 【追加】指定されたクラスの一斉インスタンス化
//        // ==========================================
//        
//        // MAPのインスタンス化
//       // this.gameMap = new Map();
//        
//        // プレイヤー（仙石さん）のインスタンス化
//        // 引数は Sengoku(初期X座標, 初期Y座標, 移動スピード) 
//        // ※座標の初期位置を0とし、速度を仮に「2」として設定しています。環境に合わせて調整してください。
//        this.player = new Sengoku(0, 0, 2);
//        
//        // 敵リストのインスタンス化
//      //  this.ghosts = new ArrayList<>();
//        
//        // 敵のインスタンスだけをリストに追加（例として初期座標違いで3匹分）
//        // ※Enemyクラスのコンストラクタがあると仮定しています。
//       // this.ghosts.add(new Enemy(24, 24, 2));
//       // this.ghosts.add(new Enemy(48, 48, 2));
//       // this.ghosts.add(new Enemy(72, 72, 2));
//
//        // ==========================================
//        // 3. 画面の構築と切り替え
//        // ==========================================
//        
//        // ゲーム画面（GridPane）の組み立て
//        GridPane gameGrid = new GridPane();
//        // (省略: マップなどの描画処理)
//
//        Scene gameScene = new Scene(gameGrid, 800, 600);
//
//        // ゲーム用のキー入力を受け付ける
//        gameScene.setOnKeyPressed(event -> {
//            this.handleKeyPress(event);
//        });
//
//        this.stage.setScene(gameScene);
//        this.isRunning = true;
//        
//        System.out.println("すべてのストーリーが終了。各インスタンス化が完了しました！");
//        System.out.println("Stage1を開始します！ isRunning: " + isRunning);
//    }
//
//    public void handleKeyPress(javafx.scene.input.KeyEvent event) {
//        if (!isRunning) return;
//        // ゲーム中の移動処理
//    }
//}
