package test2.model;

import java.util.ArrayList;
import java.util.List;

import Characters.BlueEnemy;
import Characters.Direction;
import Characters.Enemy;
import Characters.GreenEnemy;
import Characters.RedEnemy;
import Characters.Sengoku;
import Characters.YellowEnemy;
import Items.Chii;
import Items.Item;
import Items.Point;
import common.GameMap;

public class MapData implements GameMap {

	// マップ定義(28×31マス)
	// 1マスのサイズ(30×30ピクセル)
	public static final int TILE_SIZE = 30;

	// 0：道 1：壁 2：パワーエサ 7:扉 8:巣 9: ワープ
	private final int[][] map = {

			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // ■■■■■■■■■■■■
																									// ■■■■■■■■■■■■
			{ 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1 }, // ■ ■ ■ ■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■ ■■■ ■■■■ ■ ■
																									// ■■■■ ■■■ ■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■ ■■■ ■■■■ ■ ■
																									// ■■■■ ■■■ ■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 }, // ■ ■■ ■■■■■■ ■■ ■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■ ■■ ■■ ■■ ■■
																									// ■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■ ■■ ■■ ■■■ ■■ ■■
																									// ■■ ■
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, // ■ ■■■ ■■ ■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■ ■■■■■ ■■ ■■
																									// ■■■■■ ■■■■
			{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 }, // ■ ■■■■■ ■■ ■■■
																									// ■■■■■ ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■■ ■■ ■■■
																									// ■■■■■ ■■ ■
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, // ■ ■■ ■■ ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 7, 7, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■ ■■■ ■■■
																									// ■■■■ ■■ ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■ ■ ■
																									// ■■■■ ■■ ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■ ■ ■
																									// ■■■■ ■■ ■
			{ 9, 0, 0, 0, 2, 0, 0, 0, 0, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 0, 0, 0, 0, 2, 0, 0, 0, 9 }, // ■ ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■
																									// ■■■■■■■■ ■■■■ ■■
																									// ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■ ■■■■ ■■
																									// ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■ ■■■■■
																									// ■■ ■■■■ ■■ ■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, // ■ ■■ ■■■■ ■■■■■
																									// ■■ ■■■■ ■■ ■
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, // ■ ■■ ■■ ■■ ■■ ■
			{ 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1 }, // ■ ■■■■ ■■ ■■■■■
																									// ■■■■ ■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■ ■■■■ ■■
																									// ■■■■■ ■■■■ ■■■■
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, // ■ ■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■ ■■ ■■ ■■■■■■ ■■
																									// ■■ ■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■ ■■ ■■ ■■■■■■
																									// ■■ ■■ ■■■■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 }, // ■ ■■ ■■ ■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■ ■■■ ■■■■ ■■■■■■
																									// ■■■■ ■■■ ■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■ ■■■ ■■■■ ■ ■
																									// ■■■■ ■■■ ■
			{ 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1 }, // ■ ■ ■ ■
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } // ■■■■■■■■■■■■
																									// ■■■■■■■■■■■■

	};

	private Item[][] itemMap;

	private Sengoku sengoku;

	// 敵のリスト管理

	private final List<Enemy> enemies = new ArrayList<>();

	private boolean paused = false;

	// 初期アイテム配置（エサ復活用）
	private Item[][] initialItemMap;

	// エサ復活を有効にするか？
	private boolean enableRespawn;

	// 現在のステージ番号を書く(1 = ステージ1, 2 = ステージ2, 3 = ステージ3）
	private int stageNumber = 2;

	// 口パク
	private double mouthAngle = 45;
	private int mouthOpening = -1;
	private boolean isBlocked = false;

	// ワープ抑止
	private boolean justWarped = false;
	private int lastWarpX = -1;
	private int lastWarpY = -1;

	// 残りアイテム数をカウントする変数
	private int remainingItems = 0;
	private boolean gameOver = false;

	// CHASE/SCATTER管理
	private long modeStartTime = 0;
	private boolean chaseMode = false;

	// ゲーム開始待ち
	private boolean waitingStart = true;

	// FEVER終了時刻
	private long feverEndTime = 0;

	// booleanを受け取る新しいコンストラクターを追加

	public MapData(boolean paused) {
		this(); // 上にある引数なしのコンストラクターを呼び出して初期化を行う
		this.paused = paused; // 受け取った値をpausedフィールドにセットする
	}

	public void SampleModel(boolean enableRespawn) {
		this.enableRespawn = enableRespawn; // これで練習/ストーリーを切り替えられる（エサ復活用）
		this.sengoku = new Sengoku(10 * TILE_SIZE, 14 * TILE_SIZE, 2);
		this.itemMap = new Item[map.length][map[0].length];
		this.remainingItems = 0;

		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				double pixelX = col * TILE_SIZE + TILE_SIZE / 2.0;
				double pixelY = row * TILE_SIZE + TILE_SIZE / 2.0;

				if (map[row][col] == 0) {
					itemMap[row][col] = new Point(pixelX, pixelY);
					this.remainingItems++;
				} else if (map[row][col] == 2) {
					itemMap[row][col] = new Chii(pixelX, pixelY);
					this.remainingItems++;
				}
			}
		}

		// エサ復活が有効なときだけ初期状態を保存（エサ復活用）
		if (enableRespawn) {
			this.initialItemMap = copyItemMap(itemMap);
		} else {
			this.initialItemMap = null;
		}
	}

	// --- itemMap をコピーする ---（エサ復活用）
	private Item[][] copyItemMap(Item[][] src) {
		Item[][] dst = new Item[src.length][src[0].length];
		for (int r = 0; r < src.length; r++) {
			for (int c = 0; c < src[0].length; c++) {
				dst[r][c] = src[r][c];
			}
		}
		return dst;
	}

	public MapData() {
		// 初期設定
		this.enableRespawn = false;
		this.sengoku = new Sengoku(14 * TILE_SIZE, 23 * TILE_SIZE, 2);
		this.itemMap = new Item[map.length][map[0].length];
		this.remainingItems = 0;

		// アイテムの配置
		for (int row = 0; row < map.length; row++) {

			for (int col = 0; col < map[0].length; col++) {
				double pixelX = col * TILE_SIZE + TILE_SIZE / 2.0;
				double pixelY = row * TILE_SIZE + TILE_SIZE / 2.0;

				if (map[row][col] == 0) {
					itemMap[row][col] = new Point(pixelX, pixelY);
					remainingItems++; // ドットを配置したらカウントアップ
				} else if (map[row][col] == 2) {
					itemMap[row][col] = new Chii(pixelX, pixelY);
					remainingItems++;// パワーエサもクリア条件に含めるならカウントアップ
				}
			}
		}
		// 敵の初期位置
		initEnemy(null);

		// アイテムが完全に配置し終わった後で、バックアップを取り、復活を有効にする
		this.initialItemMap = copyItemMap(itemMap);
		this.enableRespawn = true;
	}

	public void initEnemy(javafx.scene.image.ImageView enemyImageView) {

		// ⭕ リストを一度クリアして、敵をどんどん追加する

		enemies.clear();
		enemies.add(new RedEnemy(this));
		enemies.add(new GreenEnemy(this)); // 今後Map3Enemyなどもここに enemies.add(...) するだけで追加可能
		enemies.add(new YellowEnemy(this));
		enemies.add(new BlueEnemy(this));

		// 安全対策: リスト内の全ての敵の初期状態をセット
		for (Enemy e : enemies) {
			if (e != null) {
				e.setCurrentState(Characters.EnemyState.SCATTER);
			}
		}
	}

	public void togglePause() {
		paused = !paused;
	}

	// ゲーム全体の定期更新
	public void update() {
		if (paused)
			return;
		
		//死んだときのアニメーション
		if (sengoku.isDyingAnimation()) {

		    if (sengoku.updateDyingAnimation()) {

		        if (sengoku.isAlive()) {

		            sengoku.resetToStartPosition();

		            for (Enemy enemy : enemies) {
		                enemy.resetToStartPosition();
		                enemy.setCurrentState(
		                    Characters.EnemyState.SCATTER);
		            }

		            modeStartTime = 0;
		            chaseMode = false;
		            waitingStart = true;

		        } else {

		            gameOver = true;
		            paused = true;
		        }
		    }

		    return;
		}

		// パックマンの移動処理
		updatePacman();

		// FEVER終了判定
		if (feverEndTime > 0 && System.currentTimeMillis() >= feverEndTime) {
			feverEndTime = 0;
			sengoku.setFever(false);

			for (Enemy e : enemies) {
				if (e.getCurrentState() == Characters.EnemyState.FEVER) {
					e.setCurrentState(Characters.EnemyState.SCATTER);
				}
			}
			System.out.println("FEVER終了");
		}

		// CHASE/SCATTER管理
		if (!waitingStart) {

			long elapsed = System.currentTimeMillis() - modeStartTime;

			if (chaseMode && elapsed >= 20000) {

				chaseMode = false;
				modeStartTime = System.currentTimeMillis();

				for (Enemy e : enemies) {

					if (e.getCurrentState() != Characters.EnemyState.DEAD
							&& e.getCurrentState() != Characters.EnemyState.FEVER) {

						e.setCurrentState(Characters.EnemyState.SCATTER);
					}
				}

				System.out.println("SCATTER開始");
			}

			else if (!chaseMode && elapsed >= 7000) {

				chaseMode = true;
				modeStartTime = System.currentTimeMillis();

				for (Enemy e : enemies) {

					if (e.getCurrentState() != Characters.EnemyState.DEAD
							&& e.getCurrentState() != Characters.EnemyState.FEVER) {

						e.setCurrentState(Characters.EnemyState.CHASE);
					}
				}

				System.out.println("CHASE開始");
			}

			// 敵移動
			for (Enemy e : enemies) {
				e.move(map);
			}
		}
		// 口パクの更新
		updateMouth();
		// パックマンと敵の当たり判定を毎フレーム確認
		checkCollision();
	}

	public void updatePacman() {
		if (paused || !sengoku.isAlive())
			return;

		// 追加箇所 移動先のタイルを予測検出し、壁(1), 扉(7), 巣(8)への進入を防ぐ
		int tileX = (int) ((sengoku.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
		int tileY = (int) ((sengoku.getY() + TILE_SIZE / 2.0) / TILE_SIZE);

		// --- ワープ抑止ロジック ---
		boolean skipWarp = false;
		if (justWarped) {
			if (tileX == lastWarpX && tileY == lastWarpY) {
				skipWarp = true;

				// ワープ直後は、プレイヤーの入力を上書きして強制直進（先行入力を固定）
				if (lastWarpX == 27) {
					sengoku.setNextDirection(Direction.LEFT);
				} else if (lastWarpX == 0) {
					sengoku.setNextDirection(Direction.RIGHT);
				}
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

				sengoku.setX(newPacX);
				sengoku.setY(newPacY);

				justWarped = true;
				lastWarpX = warpX;
				lastWarpY = warpY;
				return;
			}
		}
		// 巣(8)と扉(7)を同様に壁扱いしている。
		int[][] moveMap = new int[map.length][map[0].length];
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[0].length; c++) {
				if (map[r][c] == 7 || map[r][c] == 8) {
					moveMap[r][c] = 1; // 7(扉)と8(巣)を、移動処理の時だけ壁(1)に化けさせる
				} else {
					moveMap[r][c] = map[r][c];
				}
			}
		}

		sengoku.move(moveMap);

		int currentTileX = (int) ((sengoku.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
		int currentTileY = (int) ((sengoku.getY() + TILE_SIZE / 2.0) / TILE_SIZE);

		if (currentTileY >= 0 && currentTileY < map.length && currentTileX >= 0 && currentTileX < map[0].length) {
			Item item = itemMap[currentTileY][currentTileX];

			if (item != null) {
				item.onEaten(sengoku);

				// パワーエサ(2)を食べたらFEVER
				if (map[currentTileY][currentTileX] == 2) {

					System.out.println("FEVER開始！");

					sengoku.setFever(true);
					// 毎回7秒にリセット
					feverEndTime = System.currentTimeMillis() + 7000;

					for (Enemy e : enemies) {
						if (e.getCurrentState() != Characters.EnemyState.DEAD) {
							e.setCurrentState(Characters.EnemyState.FEVER);
						}
					}

					// ★パワーエサを食べたので50点加算（メソッド名はSengokuクラスに合わせてね）
					sengoku.addScore(50);
				} else {
					// ★普通のドットを食べたので10点加算
					sengoku.addScore(10);
				}

				itemMap[currentTileY][currentTileX] = null;
				remainingItems--; // ★1個食べたのでカウントを減らす
				System.out.println("残りのドット数: " + remainingItems); // デバッグ用ログ
			}
		}

		// 全部食べたかチェック（エサ復活用）
		checkAllEaten();

	}

	// --- 全部食べたかチェック ---（エサ復活用）

	private void checkAllEaten() {
		if (!enableRespawn)
			return; // ← ストーリーでは復活しない

		for (int r = 0; r < itemMap.length; r++) {
			for (int c = 0; c < itemMap[0].length; c++) {
				if (itemMap[r][c] != null)
					return; // まだ残っている
			}
		}
		// 全部食べた → 復活（エサ復活用）
		resetItems();
	}

	// --- エサ復活 ---（エサ復活用）

	private void resetItems() {
		if (!enableRespawn || initialItemMap == null)
			return;

		this.itemMap = copyItemMap(this.initialItemMap);
		System.out.println("ステージクリア！エサが復活しました！");
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

		// ★★sengoku.setNextDirection(dir);
		if (sengoku != null) {
			// 古い sample.Direction への変換をやめ、そのまま dir を渡します★★
			sengoku.setNextDirection(dir);
		}

		// 初回入力でゲーム開始
		if (waitingStart) {

			waitingStart = false;

			modeStartTime = System.currentTimeMillis();

			System.out.println("ゲーム開始");
		}
	}
	// 敵との当たり判定

	private void checkCollision() {

		if (!sengoku.isAlive())
			return;

		double pacCenterX = sengoku.getX() + TILE_SIZE / 2.0;
		double pacCenterY = sengoku.getY() + TILE_SIZE / 2.0;
		double collisionThreshold = TILE_SIZE * 0.8;

		for (Enemy e : enemies) {

			if (e.getCurrentState() == Characters.EnemyState.DEAD) {
				continue;
			}

			double dx = pacCenterX - e.getX();

			double dy = pacCenterY - e.getY();

			if (Math.sqrt(dx * dx + dy * dy) < collisionThreshold) {
				// FEVER中の敵は食べられる
				if (e.getCurrentState() == Characters.EnemyState.FEVER) {
					e.setCurrentState(Characters.EnemyState.DEAD);
					continue;
				}

				if (e.getCurrentState() == Characters.EnemyState.DEAD) {
					continue;
				}

				System.out.println("💥敵に捕まった！");
				
				sengoku.takeDamage();
				sengoku.startDying();

				/*if (sengoku.getHp() <= 0) {

					this.gameOver = true;
					this.paused = true;

				} else {

					sengoku.resetToStartPosition();

					for (Enemy enemy : enemies) {
						enemy.resetToStartPosition();
					}

					for (Enemy enemy : enemies) {
						enemy.setCurrentState(Characters.EnemyState.SCATTER);
					}

					// タイマーリセット
					modeStartTime = 0;

					// 初期状態に戻す
					chaseMode = false;

					// 再入力待ち
					waitingStart = true;

				}*/

				return;
			}
		}
	}

	public boolean isPaused() {
		return paused;
	}

	@Override
	public int[][] getMap() {
		return map;
	}

	@Override
	public double getPacX() {
		return sengoku != null ? sengoku.getX() : 0;
	}

	@Override
	public double getPacY() {
		return sengoku != null ? sengoku.getY() : 0;
	}

	@Override
	public int getStageNumber() {
		return stageNumber;
	}

	@Override
	public boolean isWaitingStart() {
		return waitingStart;
	}

	@Override
	public List<Enemy> getEnemies() {
		return enemies;
	}

	// ※ common.Direction と Characters.Direction の型が合わない場合はキャストや変換を行ってください
	@Override
	public Characters.Direction getPlayerDirection() {
		if (sengoku == null || sengoku.getDirection() == null) {
			return Characters.Direction.NONE;
		}

		// Characters.Direction から 正解の test.Direction へ名前ベースで型変換
		try {
			return Characters.Direction.valueOf(sengoku.getDirection().name());
		} catch (IllegalArgumentException e) {
			return Characters.Direction.NONE;
		}
	}

	// --- getters ---
	public Item[][] getItemMap() {
		return itemMap;
	}

	public double getMouthAngle() {
		return mouthAngle;
	}

	public Sengoku getSengoku() {
		return sengoku;
	}

	public long getFeverRemainingTime() {
		return Math.max(0, feverEndTime - System.currentTimeMillis());
	}

	// ⭕ 既存の古いゲッターもエラー防止で残し、リストの先頭(赤)を返す
	public Enemy getEnemy() {
		return enemies.isEmpty() ? null : enemies.get(0);
	}

	// ⭕ ステージが切り替わったときに外から数値を変更できるようにする
	// --- setters ---
	public void setStageNumber(int stageNum) {
		this.stageNumber = stageNum;
	}

	public boolean isCleared() {
		return remainingItems <= 0;
	}

	public boolean isGameOver() {
		return gameOver;
	}

}
