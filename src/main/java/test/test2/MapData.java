package test.test2;

import java.util.ArrayList;
import java.util.List;

import Characters.Direction;
import Characters.Sengoku;
import Items.Chii;
import Items.Item;
import Items.Point;
import test.Enemy;
import test.GreenEnemy;
import test.RedEnemy;

public class MapData {

	public static final int TILE_SIZE = 30;

	// 0: 道, 1: 壁, 2: パワーエサ, 9: ワープ
	private final int[][] map = {
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 },
			{ 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
			{ 9, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 9 },
			{ 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 },
			{ 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0 },
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	private final Item[][] itemMap;
	private final Sengoku sengoku;
	//敵のリスト管理
	private final List<Enemy> enemies = new ArrayList<>();

	private boolean paused = false;

	// 口パク
	private double mouthAngle = 45;
	private int mouthOpening = -1;
	private boolean isBlocked = false;

	// ワープ抑止
	private boolean justWarped = false;
	private int lastWarpX = -1;
	private int lastWarpY = -1;

	//booleanを受け取る新しいコンストラクターを追加
	public MapData(boolean paused) {
		this(); // 上にある引数なしのコンストラクターを呼び出して初期化を行う
		this.paused = paused; // 受け取った値をpausedフィールドにセットする
	}

	public MapData() {
		this.sengoku = new Sengoku(14 * TILE_SIZE, 23 * TILE_SIZE, 2);
		this.itemMap = new Item[map.length][map[0].length];
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				double pixelX = col * TILE_SIZE + TILE_SIZE / 2.0;
				double pixelY = row * TILE_SIZE + TILE_SIZE / 2.0;

				if (map[row][col] == 0) {
					itemMap[row][col] = new Point(pixelX, pixelY);
				} else if (map[row][col] == 2) {
					itemMap[row][col] = new Chii(pixelX, pixelY);
				}
			}
		}
		initEnemy(null); 
	}

	//コード追加　成田
	public void initEnemy(javafx.scene.image.ImageView enemyImageView) {
		// ⭕ リストを一度クリアして、敵をどんどん追加する
		enemies.clear();
		enemies.add(new RedEnemy(this));
		enemies.add(new GreenEnemy(this)); // 今後Map3Enemyなどもここに enemies.add(...) するだけで追加可能

		// 安全対策: リスト内の全ての敵の初期状態をセット
		for (Enemy e : enemies) {
			e.setCurrentState(Characters.EnemyState.SCATTER);
		}
	}

	public void togglePause() {
		paused = !paused;
	}

	//ゲーム全体の定期更新

	public void update() {
		if (paused)
			return;

		//パックマンの移動処理
		updatePacman();
		
		//敵キャラが存在すれば移動ロジックを実行
		for (Enemy e : enemies) {
			e.move(map);
		}

		//口パクの更新
		updateMouth();

		//パックマンと敵の当たり判定を毎フレーム確認
		checkCollision();
	}

	public void updatePacman() {
		if (paused || !sengoku.isAlive())
			return;

		int tileX = (int) (sengoku.getX() / TILE_SIZE);
		int tileY = (int) (sengoku.getY() / TILE_SIZE);

		// --- ワープ抑止ロジック ---
		boolean skipWarp = false;
		if (justWarped) {
			if (tileX == lastWarpX && tileY == lastWarpY) {
				sengoku.move(map);
				return;
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

				Direction currentDir = sengoku.getDirection();

				if (currentDir != Direction.NONE) {
					if (currentDir.getDX() != 0) {
						for (int x = 0; x < map[0].length; x++) {
							if (map[tileY][x] == 9 && x != tileX) {
								warpX = x;
								break;
							}
						}
					}
					if (currentDir.getDY() != 0) {
						for (int y = 0; y < map.length; y++) {
							if (map[y][tileX] == 9 && y != tileY) {
								warpY = y;
								break;
							}
						}
					}
				}

				double newPacX = warpX * TILE_SIZE;
				double newPacY = warpY * TILE_SIZE;
				sengoku.setPosition(newPacX, newPacY);

				justWarped = true;
				lastWarpX = warpX;
				lastWarpY = warpY;

				return;
			}
		}

		sengoku.move(map);

		int currentTileX = (int) ((sengoku.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
		int currentTileY = (int) ((sengoku.getY() + TILE_SIZE / 2.0) / TILE_SIZE);

		if (currentTileY >= 0 && currentTileY < map.length && currentTileX >= 0 && currentTileX < map[0].length) {
			Item item = itemMap[currentTileY][currentTileX];
			if (item != null) {
				item.onEaten(sengoku);
				itemMap[currentTileY][currentTileX] = null;
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

	//敵との当たり判定

	private void checkCollision() {
		if (!sengoku.isAlive())
			return;

		double pacCenterX = sengoku.getX() + TILE_SIZE / 2.0;
		double pacCenterY = sengoku.getY() + TILE_SIZE / 2.0;
		double collisionThreshold = TILE_SIZE * 0.8;

		// ⭕ ループ処理でリスト内のどの敵に捕まったかを一斉に判定
		for (Enemy e : enemies) {
			double dx = pacCenterX - (e.getX() + TILE_SIZE / 2.0);
			double dy = pacCenterY - (e.getY() + TILE_SIZE / 2.0);
			if (Math.sqrt(dx * dx + dy * dy) < collisionThreshold) {
				String enemyName = (e instanceof RedEnemy) ? "赤敵" : "緑敵";
				System.out.println("💥 " + enemyName + "に捕まった！ゲームオーバー！");
				this.paused = true;
				return;
			}
		}
	}

	// --- getters ---
	public int[][] getMap() {
		return map;
	}

	public Item[][] getItemMap() {
		return itemMap;
	}

	public boolean isPaused() {
		return paused;
	}

	public double getMouthAngle() {
		return mouthAngle;
	}

	public Sengoku getSengoku() {
		return sengoku;
	}

	// ⭕ 既存の古いゲッターもエラー防止で残し、リストの先頭(赤)を返す
		public Enemy getEnemy() { return enemies.isEmpty() ? null : enemies.get(0); }

		// ⭕ MapViewでループ描画するためのリストゲッター
		public List<Enemy> getEnemies() { return enemies; }

		public double getPacX1() { return sengoku != null ? sengoku.getX() : 0; }
		public double getPacY1() { return sengoku != null ? sengoku.getY() : 0; }

	//追加項目
	public double getPacX() {
		return sengoku != null ? sengoku.getX() : 0;
	}

	public double getPacY() {
		return sengoku != null ? sengoku.getY() : 0;
	}

}
