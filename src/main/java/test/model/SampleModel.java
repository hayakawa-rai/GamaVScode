package test.model;

import Characters.Direction;
import Characters.Sengoku;
import Items.Chii;
import Items.Item;
import Items.Point;

public class SampleModel {

    public static final int TILE_SIZE = 30;

    // 0: 道, 1: 壁, 9: ワープ
    private final int[][] map = {
        { 1,1,1,1,1,1,1,1,1,9,1,1,1,1,1,1,1,1,1 },
        { 1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1 },
        { 1,2,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,2,1 },
        { 1,0,1,1,0,0,0,0,0,1,0,0,0,0,0,1,1,0,1 },
        { 1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1 },
        { 1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1 },
        { 1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,1 },
        { 1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1 },
        { 1,1,1,1,0,1,0,1,1,0,1,1,0,1,0,1,1,1,1 },
        { 9,0,0,0,0,1,0,1,0,0,0,1,0,1,0,0,0,0,9 },
        { 1,1,1,1,0,1,0,1,0,0,0,1,0,1,0,1,1,1,1 },
        { 1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1 },
        { 1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1 },
        { 1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1 },
        { 1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1 },
        { 1,0,1,1,0,1,1,1,1,1,1,1,1,1,0,1,1,0,1 },
        { 1,2,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,2,1 },
        { 1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1 },
        { 1,1,1,1,1,1,1,1,1,9,1,1,1,1,1,1,1,1,1 }
    };
    //Chii,Pointクラスにある設定を持っていく用のMap
    private final Item[][] itemMap;
    // パックマンの状態（タイル中央）
    private final Sengoku sengoku;
    private boolean paused = false;

    // 口パク
    private double mouthAngle = 45;
    private int mouthOpening = -1;
    private boolean isBlocked = false;

    // ワープ抑止
    private boolean justWarped = false;
    private int lastWarpX = -1;
    private int lastWarpY = -1;

    public SampleModel() {
        this.sengoku = new Sengoku(10 * TILE_SIZE, 14 * TILE_SIZE, 2);
        //マップと同じ大きさのアイテム配列を用意し、初期配置する
        this.itemMap = new Item[map.length][map[0].length];
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                double pixelX = col * TILE_SIZE + TILE_SIZE / 2.0;
                double pixelY = row * TILE_SIZE + TILE_SIZE / 2.0;

                if (map[row][col] == 0) {
                    itemMap[row][col] = new Point(pixelX, pixelY); // Pointインスタンス生成
                } else if (map[row][col] == 2) {
                    itemMap[row][col] = new Chii(pixelX, pixelY);  // Chiiインスタンス生成
                }
            }
        }
    }

    public void togglePause() {
        paused = !paused;
    }

 // --- 更新ロジック ---
    public void updatePacman() {
        if (paused || !sengoku.isAlive()) return;

        //  Sengokuの現在の座標から、現在のタイル位置を計算（CharacterクラスにgetX(), getY()がある前提だよ）
        int tileX = (int) (sengoku.getX() / TILE_SIZE);
        int tileY = (int) (sengoku.getY() / TILE_SIZE);

        // --- ワープ抑止ロジック ---
        boolean skipWarp = false;
        if (justWarped) {
            if (tileX == lastWarpX && tileY == lastWarpY) {
                skipWarp = true;
            } else {
                justWarped = false;
                lastWarpX = -1;
                lastWarpY = -1;
            }
        }

        // --- ワープ処理 ---
        if (!skipWarp && tileX >= 0 && tileX < map[0].length && tileY >= 0 && tileY < map.length) {
            if (map[tileY][tileX] == 9) {
                int warpX = tileX;
                int warpY = tileY;

                // Sengokuの現在の進行方向（sengoku.getDirection()）を元に、ワープ先を探す
                Direction currentDir = sengoku.getDirection();
                
                if (currentDir != Direction.NONE) {
                    // 横方向のワープ
                    if (currentDir.getDX() != 0) {
                        for (int x = 0; x < map[0].length; x++) {
                            if (map[tileY][x] == 9 && x != tileX) {
                                warpX = x;
                                break;
                            }
                        }
                    }
                    // 縦方向のワープ
                    if (currentDir.getDY() != 0) {
                        for (int y = 0; y < map.length; y++) {
                            if (map[y][tileX] == 9 && y != tileY) {
                                warpY = y;
                                break;
                            }
                        }
                    }
                }

                //  Sengokuの位置を直接ワープ先のタイル中央に更新する（CharacterクラスにsetPositionメソッドがあるね！）
                double newPacX = warpX * TILE_SIZE;
                double newPacY = warpY * TILE_SIZE;
                sengoku.setPosition(newPacX, newPacY);

                justWarped = true;
                lastWarpX = warpX;
                lastWarpY = warpY;

                return; // ワープしたフレームは移動処理をスキップして終了
            }
        }

        //  ワープゾーンにいない場合は、通常通りSengoku自身の移動ロジックを実行！
        sengoku.move(map);
        
     //移動した後の新しいマス目の位置を再計算
        int currentTileX = (int) ((sengoku.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
        int currentTileY = (int) ((sengoku.getY() + TILE_SIZE / 2.0) / TILE_SIZE);

        // マップの範囲内であることを確認
        if (currentTileY >= 0 && currentTileY < map.length && currentTileX >= 0 && currentTileX < map[0].length) {
        	Item item = itemMap[currentTileY][currentTileX];
        	//触ったものがnull出ない場合。実行
        	if (item != null) {
                //各クラス（PointやChii）に定義された onEaten を実行してスコア加算
                item.onEaten(sengoku);
                
                // 食べたので配列から消去する
                itemMap[currentTileY][currentTileX] = null; 
                System.out.println("アイテムを食べた！現在のスコア: " + sengoku.getScore());
            }
        }
    }
    public void updateMouth() {
        if (paused || !sengoku.isAlive() || sengoku.getDirection() == Direction.NONE) return;
        mouthAngle += mouthOpening * 2;
        if (mouthAngle <= 10) mouthOpening = +1;
        if (mouthAngle >= 45) mouthOpening = -1;
    }

    public void setNextDirection(Direction dir) {
        sengoku.setnextdirection(dir);
    }

    // --- getters ---
    public int[][] getMap() { return map; }
    //Viewがアイテム配列を取得するためのゲッター
    public Item[][] getItemMap() { return itemMap; }
    public boolean isPaused() { return paused; }
    public double getMouthAngle() { return mouthAngle; }
    public Sengoku getSengoku() { return sengoku; }
}