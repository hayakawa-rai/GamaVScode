package test.model;

import Characters.Direction;
import Characters.Sengoku;
import Items.Chii;
import Items.Item;
import Items.Point;
import test.Enemy;
import test.RedEnemy;

public class SampleModel {

	public static final int TILE_SIZE = 30;

	// 0: 道, 1: 壁, 9: ワープ
	private final int[][] map = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
			{ 1, 2, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 2, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 1 },
			{ 9, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 9 },
			{ 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 2, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 2, 1 },
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};
	//Chii,Pointクラスにある設定を持っていく用のMap
	private final Item[][] itemMap;
	// 初期アイテム配置（エサ復活用）(古田)
	private final Item[][] initialItemMap;

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
					itemMap[row][col] = new Chii(pixelX, pixelY); // Chiiインスタンス生成
				}
			}
		}
		// 初期アイテム配置を保存（エサ復活用）(古田)
		this.initialItemMap = copyItemMap(itemMap);
	}

	// --- itemMap をコピーする ---(古田)
	private Item[][] copyItemMap(Item[][] src) {
		Item[][] dst = new Item[src.length][src[0].length];
		for (int r = 0; r < src.length; r++) {
			for (int c = 0; c < src[0].length; c++) {
				dst[r][c] = src[r][c];
			}
		}
		return dst;
	}

	public void togglePause() {
		paused = !paused;
	}

	// --- 更新ロジック ---
	public void updatePacman() {
		if (paused || !sengoku.isAlive())
			return;

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
		// ★ 全部食べたかチェック(古田)
		checkAllEaten();
	}

	// --- 全部食べたかチェック ---(古田)
	private void checkAllEaten() {
		for (int r = 0; r < itemMap.length; r++) {
			for (int c = 0; c < itemMap[0].length; c++) {
				if (itemMap[r][c] != null)
					return; // まだ残っている
			}
		}

		// ★ 全部食べた → 復活(古田)
		resetItems();
	}

	// --- エサ復活 ---(古田)
	private void resetItems() {
		for (int r = 0; r < itemMap.length; r++) {
			for (int c = 0; c < itemMap[0].length; c++) {
				itemMap[r][c] = initialItemMap[r][c];
			}
		}
	}

	public void updateMouth() {
		if (paused || !sengoku.isAlive() || sengoku.getDirection() == Direction.NONE)
			return;
		mouthAngle += mouthOpening * 2;
		if (mouthAngle <= 10)
			mouthOpening = +1;
		if (mouthAngle >= 45)
			mouthOpening = -1;
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
    
 // ① メンバー変数エリアに追加（実際のゴーストクラスを保持する）
    private Enemy enemy; 

    // ② コンストラクタの中で初期化する
    // ※ImageView、初期座標、スピード、プレイヤー(sengoku)を渡します
    public void initEnemy(javafx.scene.image.ImageView enemyImageView) {
        // ここでは例として赤ゴースト(Blinky)の子クラスを生成する想定です
        // もし子クラス名が異なる場合は、ここを適切なクラス名（new RedGhost 等）に変えてください
        this.enemy = new RedEnemy(enemyImageView, this.sengoku);
    }

    // ③ update() メソッドの中に移動処理を追加
    public void update() {
        if (paused) return;

        // パックマンの移動
        sengoku.move(map);
        
        // ★【追加】敵キャラが存在すれば移動ロジックを実行！
        if (enemy != null) {
            enemy.move(map); // Enemy側の内部で自動的にImageViewの位置も同期されます
        }

        // （以下、既存のアイテム捕食処理や口パク処理はそのまま）
        int currentTileX = (int) ((sengoku.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
        int currentTileY = (int) ((sengoku.getY() + TILE_SIZE / 2.0) / TILE_SIZE);
        if (currentTileY >= 0 && currentTileY < map.length && currentTileX >= 0 && currentTileX < map[0].length) {
            Item item = itemMap[currentTileY][currentTileX];
            if (item != null) {
                item.onEaten(sengoku);
                itemMap[currentTileY][currentTileX] = null; 
            }
        }
        updateMouth();
    }

    // ④ 外部から敵を取得するためのゲッター
    public Enemy getEnemy() {
        return enemy;
    }
}