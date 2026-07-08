package test1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Characters.BlueEnemy;
import Characters.Direction;
import Characters.Enemy;
import Characters.EnemyState;
import Characters.GreenEnemy;
import Characters.RedEnemy;
import Characters.Syujinkou;
import Characters.YellowEnemy;
import Items.Chii;
import Items.Fruit;
import Items.FruitType;
import Items.Item;
import Items.Point;
import common.GameMap;
import javafx.scene.image.ImageView;
import start.Bgm;
import start.SoundManager;

public class MapData implements GameMap {

	// マップ定義(28×31マス)
	// 1マスのサイズ(30×30ピクセル)
	public static final int TILE_SIZE = 30;

	// 0：道 1：壁 2：パワーエサ 3:仙石さん 7:扉 8:巣 9: ワープ
	private final int[][] map = {

			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■
			{ 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 1 }, // ■　　　餌　　　　　　　　■■　　　　　　　　餌　　　■
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, // ■　■■■■■　■■■■　■■　■■■■　■■■■■　■
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, // ■　■■■■■　■■■■　■■　■■■■　■■■■■　■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 }, // ■　　　　　　　■■■■　■■　■■■■　　　　　　　■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　　　　　　　　　　　　　　■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　■■　■■■■■■　■■　■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　■■　■■■■■■　■■　■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　■■　　　■■　　　■■　■■　■■■■
			{ 9, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 9 }, // ワ　　　　　　　■■■■　■■　■■■■　　　　　　　ワ
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　■■■■　■■　■■■■　■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　　　　　　　　　　　　　　■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 7, 7, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■■■　■■■扉扉■■■　■■■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■■■　■巣巣巣巣巣巣■　■■■■　■■■■
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, // ■■■■　　　　　　■巣巣巣巣巣巣■　　　　　　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■■■　■巣巣巣巣巣巣■　■■■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■■■　■■■■■■■■　■■■■　■■■■
			{ 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9 }, // ワ　　　　　　　　　　　　　　　　　　　　　　　　　　ワ
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■■■■■■　■■　■■■■■■■　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■■■■■■　■■　■■■■■■■　■■■■
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, // ■■■■　　　　　　　　　■■　　　　　　　　　■■■■
			{ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 }, // ■■■■　■■　■■　■■■■■■　■■　■■　■■■■
			{ 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1 }, // ■　　　　■■　■■　■■■■■■　■■　■■　　　　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, // ■　■■　■■　■■　　　　仙　　　■■　■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, // ■　■■　■■　■■　■■　■■■　■■　■■　■■　■
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, // ■　■■　　　　　　　■■　■■■　　　　　　　■■　■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■　■■■　■■■■　■■　　■■　■■■■　■■■　■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■　■■■　■■■■　■■■　■■　■■■■　■■■　■
			{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }, // ■　■■■　■■■■　■■■　■■　■■■■　■■■　■
			{ 1, 0, 0, 2, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 2, 0, 0, 1 }, // ■　　餌　　■■■■　　　　　　　　■■■■　　餌　　■
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	};

	// 各マス目に配置されたアイテム(ドット・パワーエサ)を保持する二次元配列
	private Item[][] itemMap;

	// プレイヤーのキャラクターオブジェクト
	private Syujinkou syujinkou;

	// 敵のリスト管理
	private final List<Enemy> enemies = new ArrayList<>();

	// ゲームが一時停止中かどうかの確認
	private boolean paused = false;

	// 初期アイテム配置（エサ復活用）
	private Item[][] initialItemMap;

	// クラスのフィールド（メンバ変数）に、最大数を記憶する変数を追加
	private int totalItems;

	// エサ復活を有効にするか？
	private boolean enableRespawn;

	// 現在のステージ番号を書く(1 = ステージ1, 2 = ステージ2, 3 = ステージ3）
	private int stageNumber = 1;

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

	// フルーツ関連
	public static final int FRUIT_VALUE = 3;
	private int fruitRow = -1;
	private int fruitCol = -1; // map配列内でのフルーツを表す数値

	private Fruit currentFruit = null; // 現在出現中のフルーツ(nullなら未出現)
	private long lastFruitSpawnTime = 0; // 最後にフルーツを出した時刻
	private int lastFruitScore = 0; // 最後にフルーツを出した時点のスコア

	private static final long FRUIT_TIME_INTERVAL = 15000; // 15秒ごとに出現チャンス
	private static final int FRUIT_SCORE_INTERVAL = 1000; // 1000点ごとに出現チャンス

	// フルーツスコアポップアップ用
	private boolean fruitPopupActive = false;
	private long fruitPopupStartTime = 0;
	private int fruitPopupScore = 0;
	private static final long FRUIT_POPUP_DURATION = 1000; // 表示時間(ms)
	private double fruitPopupX = 0; // 食べた瞬間のX座標（固定・ピクセル）
	private double fruitPopupY = 0; // 食べた瞬間のY座標（固定・ピクセル）

	// booleanを受け取る新しいコンストラクターを追加
	public MapData(boolean paused) {
		this(); // 上にある引数なしのコンストラクターを呼び出して初期化を行う
		this.paused = paused; // 受け取った値をpausedフィールドにセットする
	}

	/**
	 * 練習モード用の初期化メソッド。 プレイヤーの初期位置を通常とは別の座標に設定し、アイテム(ドット・パワーエサ)を
	 * マップ全体に配置する。enableRespawn が true の場合は、エサ復活用に 初期状態のitemMapのコピーを保存しておく。
	 *
	 * enableRespawn エサ（ドット）を食べ切ったあとに復活させるかどうか
	 */
	public void SampleModel(boolean enableRespawn) {
		this.enableRespawn = enableRespawn; // これで練習/ストーリーを切り替えられる（エサ復活用）
		this.syujinkou = new Syujinkou(10 * TILE_SIZE, 14 * TILE_SIZE, 2);
		this.itemMap = new Item[map.length][map[0].length];
		this.remainingItems = 0;
		this.lastFruitSpawnTime = System.currentTimeMillis();
		this.lastFruitScore = 0;

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
	/**
	 * itemMap（アイテム配置の二次元配列）のディープではない浅いコピーを作成する。
	 * エサ復活機能で「初期状態のアイテム配置」を保存・復元するために使用する。
	 */
	private Item[][] copyItemMap(Item[][] src) {
		Item[][] dst = new Item[src.length][src[0].length];
		for (int r = 0; r < src.length; r++) {
			for (int c = 0; c < src[0].length; c++) {
				dst[r][c] = src[r][c];
			}
		}
		return dst;
	}

	/**
	 * 本番モード（ストーリーモード）用のデフォルトコンストラクタ。 プレイヤーの初期位置を設定し、マップ上の道(0)とパワーエサ(2)の位置に
	 * アイテムを配置、敵を初期化する。最後にエサ復活用の初期状態を保存し、 総アイテム数(totalItems)を記録する。
	 */
	public MapData() {
		// 初期設定
		this.enableRespawn = false;
		this.syujinkou = new Syujinkou(14 * TILE_SIZE, 23 * TILE_SIZE, 2);
		this.itemMap = new Item[map.length][map[0].length];
		this.remainingItems = 0;
		this.lastFruitSpawnTime = System.currentTimeMillis();
		this.lastFruitScore = 0;

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

		// 最初に配置し終わった時の総数を記憶しておく
		this.totalItems = this.remainingItems;
	}

	/**
	 * 敵キャラクター（赤・緑・黄・青）を初期化してenemiesリストに追加する。 既存のリストを一度クリアしてから追加するため、複数回呼んでも敵が重複しない。
	 * 追加後、全ての敵の状態をSCATTER（散開）にリセットする。
	 */
	public void initEnemy(ImageView enemyImageView) {

		// ⭕ リストを一度クリアして、敵をどんどん追加する
		enemies.clear();
		enemies.add(new RedEnemy(this)); // 今後Map3Enemyなどもここに enemies.add(...) するだけで追加可能
		enemies.add(new GreenEnemy(this));
		enemies.add(new YellowEnemy(this));
		enemies.add(new BlueEnemy(this));

		// 安全対策: リスト内の全ての敵の初期状態をセット
		for (Enemy e : enemies) {
			if (e != null) {
				e.setCurrentState(EnemyState.SCATTER);
			}
		}
	}

	private long pauseStartTime = 0;

	/**
	 * ゲームの一時停止／再開を切り替える。 一時停止に入るときは開始時刻を記録し、敵のタイマーを止める。
	 * 再開するときは一時停止していた時間分だけ、FEVERタイマーやCHASE/SCATTERタイマーを 後ろにずらして帳尻を合わせ、敵のタイマーを再開する。
	 */
	public void togglePause() {

		if (!paused) {

			paused = true;
			pauseStartTime = System.currentTimeMillis();
			Bgm.pauseBGM();
			for (Enemy e : enemies) {
				e.pauseTimer();
			}
		} else {
			paused = false;
			Bgm.resumeBGM();
			long pauseDuration = System.currentTimeMillis() - pauseStartTime;

			// FEVER停止
			if (feverEndTime > 0) {
				feverEndTime += pauseDuration;
			}
			// CHASE/SCATTERタイマー停止
			if (modeStartTime > 0) {
				modeStartTime += pauseDuration;
			}

			//フルーツのタイマーもポーズ時間分ずらす
			if (lastFruitSpawnTime > 0) {
				lastFruitSpawnTime += pauseDuration;
			}

			for (Enemy e : enemies) {
				e.resumeTimer();
			}
		}
	}

	// ゲーム全体の定期更新
	/**
	 * 1. 一時停止中は何もしない 2. プレイヤーが死亡アニメーション中なら、アニメーションの進行のみ行い、
	 * アニメーション終了時にHPが残っていればリスポーン、HPが0ならgameOverをtrueにする 3.
	 * 死亡アニメーション中でなければ、プレイヤー移動・FEVER終了判定・
	 * CHASE/SCATTERモードの切り替え・敵の移動・口パク更新・当たり判定を順に行う
	 */
	public void update() {
		if (paused)
			return;

		// 死んだときのアニメーション
		if (syujinkou.isDyingAnimation()) {
			if (syujinkou.updateDyingAnimation()) {
				if (syujinkou.isAlive()) {
					syujinkou.resetToStartPosition();
					for (Enemy enemy : enemies) {
						enemy.resetToStartPosition();
						enemy.setCurrentState(EnemyState.SCATTER);
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
			syujinkou.setFever(false);
			Bgm.stopFeverBGM(); // ステージBGMに復帰

			for (Enemy e : enemies) {
				if (e.getCurrentState() == EnemyState.FEVER) {
					e.setCurrentState(EnemyState.SCATTER);
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

					if (e.getCurrentState() != EnemyState.DEAD
							&& e.getCurrentState() != EnemyState.FEVER) {

						e.setCurrentState(EnemyState.SCATTER);
					}
				}

				System.out.println("SCATTER開始");
			}

			else if (!chaseMode && elapsed >= 7000) {

				chaseMode = true;
				modeStartTime = System.currentTimeMillis();

				for (Enemy e : enemies) {

					if (e.getCurrentState() != EnemyState.DEAD
							&& e.getCurrentState() != EnemyState.FEVER) {

						e.setCurrentState(EnemyState.CHASE);
					}
				}

				System.out.println("CHASE開始");
			}

			// 敵移動
			for (Enemy e : enemies) {
				e.move(map);
			}
			checkFruitSpawn();
			updateFruit();
		}
		// パックマンと敵の当たり判定を毎フレーム確認
		checkCollision();
	}

	/**
	 * 時間経過 または スコア到達 を条件にフルーツを固定位置に出現させる
	 */
	private void checkFruitSpawn() {
		if (currentFruit != null)
			return; // 既に出現中なら何もしない

		long now = System.currentTimeMillis();
		int score = syujinkou.getScore();

		boolean timeCondition = (now - lastFruitSpawnTime) >= FRUIT_TIME_INTERVAL;
		boolean scoreCondition = (score - lastFruitScore) >= FRUIT_SCORE_INTERVAL;

		if (timeCondition || scoreCondition) {
			spawnFruit();
			lastFruitSpawnTime = now;
			lastFruitScore = score;
		}
	}

	private void spawnFruit() {

		// 道(0)かつ、まだドットが残っていない(itemMapがnull)マスだけを候補にする
		List<int[]> candidates = new ArrayList<>();
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				if (map[row][col] == 0 && itemMap[row][col] == null) {
					candidates.add(new int[] { row, col });
				}
			}
		}

		if (candidates.isEmpty()) {
			return; // 万が一、道が無ければ何もしない
		}

		// ランダムに1マス選ぶ
		Random random = new Random();
		int[] chosen = candidates.get(random.nextInt(candidates.size()));
		this.fruitRow = chosen[0];
		this.fruitCol = chosen[1];

		FruitType type = FruitType.random(random);
		currentFruit = new Fruit(type);
		map[fruitRow][fruitCol] = FRUIT_VALUE;

		System.out.println(type + "が (" + fruitRow + ", " + fruitCol + ") に出現しました！");
	}

	/**
	 * フルーツのタイマー更新。時間切れになったら消す。
	 */
	private void updateFruit() {
		if (currentFruit == null)
			return;

		currentFruit.update();
		if (currentFruit.isExpired()) {
			map[fruitRow][fruitCol] = 0; // 消えたら道に戻す
			currentFruit = null;
			fruitRow = -1;
			fruitCol = -1;
			System.out.println("フルーツが消えました");
		}
	}

	/**
	 * プレイヤーの移動処理を行う。 ワープマスの検出・ワープ処理・壁として扱う扉(7)/巣(8)の判定・実際の移動、
	 * そして移動後にいるマスにアイテムがあれば取得（スコア加算・FEVER発動）を行う。 一時停止中、またはプレイヤーが死亡している場合は何もしない。
	 */
	public void updatePacman() {
		if (paused || !syujinkou.isAlive())
			return;

		// 追加箇所 移動先のタイルを予測検出し、壁(1), 扉(7), 巣(8)への進入を防ぐ
		int tileX = (int) ((syujinkou.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
		int tileY = (int) ((syujinkou.getY() + TILE_SIZE / 2.0) / TILE_SIZE);

		// --- ワープ抑止ロジック ---
		boolean skipWarp = false;
		if (justWarped) {
			if (tileX == lastWarpX && tileY == lastWarpY) {
				skipWarp = true;

				// ワープ直後は、プレイヤーの入力を上書きして強制直進（先行入力を固定）
				if (lastWarpX == 27) {
					syujinkou.setNextDirection(Direction.LEFT);
				} else if (lastWarpX == 0) {
					syujinkou.setNextDirection(Direction.RIGHT);
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
				Direction currentDir = syujinkou.getDirection();

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

				syujinkou.setX(newPacX);
				syujinkou.setY(newPacY);

				// 効果音
				SoundManager.play(SoundManager.WARP);

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

		syujinkou.move(moveMap);

		int currentTileX = (int) ((syujinkou.getX() + TILE_SIZE / 2.0) / TILE_SIZE);
		int currentTileY = (int) ((syujinkou.getY() + TILE_SIZE / 2.0) / TILE_SIZE);

		if (currentTileY >= 0 && currentTileY < map.length && currentTileX >= 0 && currentTileX < map[0].length) {
			Item item = itemMap[currentTileY][currentTileX];

			if (item != null) {
				item.onEaten(syujinkou);

				// パワーエサ(2)を食べたらFEVER
				if (map[currentTileY][currentTileX] == 2) {

					System.out.println("FEVER開始！");
					Bgm.playFeverBGM(); // ★追加

					syujinkou.setFever(true);
					// 毎回7秒にリセット
					feverEndTime = System.currentTimeMillis() + 7000;

					for (Enemy e : enemies) {
						if (e.getCurrentState() != EnemyState.DEAD) {
							e.setCurrentState(EnemyState.FEVER);
						}
					}

					// ★パワーエサを食べたので50点加算（メソッド名はsyujinkouクラスに合わせてね）
					syujinkou.addScore(50);
				} else {
					// ★普通のドットを食べたので10点加算
					syujinkou.addScore(10);
				}

				itemMap[currentTileY][currentTileX] = null;
				remainingItems--; // ★1個食べたのでカウントを減らす
				System.out.println("残りのドット数: " + remainingItems); // デバッグ用ログ
			}
		}
		// --- updatePacman()内、既存のitemMap判定の直後あたりに追加 ---
		// フルーツを食べたかチェック
		if (currentFruit != null && currentTileY == fruitRow && currentTileX == fruitCol) {
			currentFruit.onEaten(syujinkou);

			// スコアポップアップ開始
			fruitPopupScore = currentFruit.getType().getScore();
			fruitPopupStartTime = System.currentTimeMillis();
			fruitPopupActive = true;
			fruitPopupX = fruitCol * TILE_SIZE + TILE_SIZE / 2.0;
			fruitPopupY = fruitRow * TILE_SIZE + TILE_SIZE / 2.0;

			map[fruitRow][fruitCol] = 0;
			currentFruit = null;
			fruitRow = -1;
			fruitCol = -1;

		}

		// 全部食べたかチェック（エサ復活用）
		// checkAllEaten();

	}

	// --- 全部食べたかチェック ---（エサ復活用）

	/*
	 * private void checkAllEaten() { if (!enableRespawn) return; // ← ストーリーでは復活しない
	 * 
	 * for (int r = 0; r < itemMap.length; r++) { for (int c = 0; c <
	 * itemMap[0].length; c++) { if (itemMap[r][c] != null) return; // まだ残っている } }
	 * // 全部食べた → 復活（エサ復活用） resetItems(); }
	 * 
	 * // --- エサ復活 ---（エサ復活用）
	 * 
	 * private void resetItems() { if (!enableRespawn || initialItemMap == null)
	 * return;
	 * 
	 * this.itemMap = copyItemMap(this.initialItemMap);
	 * System.out.println("ステージクリア！エサが復活しました！"); }
	 */

	/**
	 * 練習モード用：itemMapを初期状態に戻し、残りアイテム数を最大数にリセットする。 これにより isCleared() が再び false
	 * に戻り、ゲームを終わらせずに エサを食べ続けられるようになる（練習モードのループ継続用）。
	 */
	public void respawnDots() {
		if (this.initialItemMap != null) {
			// 1. マップのアイテム配置を初期状態にコピー
			this.itemMap = copyItemMap(this.initialItemMap);

			// 2. 残りアイテム数を初期の総数にリセット（これで isCleared() が false に戻る）
			this.remainingItems = this.totalItems;

			//フルーツ関連の状態もリセット
			if (fruitRow != -1 && fruitCol != -1) {
				this.map[fruitRow][fruitCol] = 0;
			}
			this.currentFruit = null;
			this.fruitRow = -1;
			this.fruitCol = -1;
			this.lastFruitSpawnTime = System.currentTimeMillis();
			this.lastFruitScore = (syujinkou != null) ? syujinkou.getScore() : 0;

			System.out.println("【練習モード】エサが再配置され、残りカウントが " + this.remainingItems + " にリセットされました。");
		}
	}

	/*
	 * public void updateMouth() { if (paused || !syujinkou.isAlive() ||
	 * syujinkou.getDirection() == Characters.Direction.NONE) return;
	 * 
	 * mouthAngle += mouthOpening * 2; if (mouthAngle <= 10) mouthOpening = +1; if
	 * (mouthAngle >= 45) mouthOpening = -1; }
	 */

	/**
	 * キー入力などから呼ばれ、プレイヤーの次の移動方向をセットする。 ゲームがまだ開始待ち(waitingStart)の場合は、この最初の入力をトリガーとして
	 * ゲームを開始状態にし、CHASE/SCATTERタイマーを開始する。
	 *
	 * dir→プレイヤーに設定する次の移動方向
	 */
	public void setNextDirection(Direction dir) {

		// syujinkou.setNextDirection(dir);
		if (syujinkou != null) {
			// 古い sample.Direction への変換をやめ、そのまま dir を渡します
			syujinkou.setNextDirection(dir);
		}

		// 初回入力でゲーム開始
		if (waitingStart) {

			waitingStart = false;

			modeStartTime = System.currentTimeMillis();

			lastFruitSpawnTime = System.currentTimeMillis();

			System.out.println("ゲーム開始");
		}
	}

	// 敵との当たり判定
	/**
	 * プレイヤーと各敵との距離をチェックし、一定距離(collisionThreshold)以内なら 「衝突」とみなす当たり判定処理。
	 * 敵がFEVER状態の場合はプレイヤーが敵を倒したことになり、スコア加算＆敵をDEAD状態にする。
	 * それ以外（通常状態の敵）に衝突した場合は、プレイヤーがダメージを受け(takeDamage)、
	 * 死亡（ミス）アニメーションを開始する(startDying)。 すでにプレイヤーが死んでいる場合は何もしない。
	 */
	private void checkCollision() {

		if (!syujinkou.isAlive())
			return;

		double pacCenterX = syujinkou.getX() + TILE_SIZE / 2.0;
		double pacCenterY = syujinkou.getY() + TILE_SIZE / 2.0;
		double collisionThreshold = TILE_SIZE * 0.8;

		for (Enemy e : enemies) {

			if (e.getCurrentState() == EnemyState.DEAD) {
				continue;
			}

			double dx = pacCenterX - e.getX();
			double dy = pacCenterY - e.getY();

			if (Math.sqrt(dx * dx + dy * dy) < collisionThreshold) {
				// FEVER中の敵は食べられる
				if (e.getCurrentState() == EnemyState.FEVER) {

					// 効果音
					SoundManager.play(SoundManager.ENEMY_DEAD);

					// 敵を倒したのでスコアを加算し、その場にスコア表示を開始する
					int defeatScore = 200;
					syujinkou.addScore(defeatScore);
					e.onDefeated(defeatScore);
					continue;
				}

				if (e.getCurrentState() == EnemyState.DEAD) {
					continue;
				}

				System.out.println("💥敵に捕まった！");

				// 効果音
				SoundManager.play(SoundManager.DAMAGE);

				syujinkou.takeDamage();
				syujinkou.startDying();

				/*
				 * if (syujinkou.getHp() <= 0) {
				 * 
				 * this.gameOver = true; this.paused = true;
				 * 
				 * } else {
				 * 
				 * syujinkou.resetToStartPosition();
				 * 
				 * for (Enemy enemy : enemies) { enemy.resetToStartPosition(); }
				 * 
				 * for (Enemy enemy : enemies) {
				 * enemy.setCurrentState(Characters.EnemyState.SCATTER); }
				 * 
				 * // タイマーリセット modeStartTime = 0;
				 * 
				 * // 初期状態に戻す chaseMode = false;
				 * 
				 * // 再入力待ち waitingStart = true;
				 * 
				 * }
				 */

				return;
			}
		}
	}

	// ゲームが一時停止中かどうか返す。
	public boolean isPaused() {
		return paused;
	}

	// マップデータ(壁・道・アイテム種別を表す二次元配列)を返す。
	@Override
	public int[][] getMap() {
		return map;
	}

	// プレイヤーの現在のX座標(ピクセル)を返す。syujinkouがnullの場合は0を返す。
	@Override
	public double getPacX() {
		return syujinkou != null ? syujinkou.getX() : 0;
	}

	// プレイヤーの現在のY座標(ピクセル)を返す。syujinkouがnullの場合は0を返す。
	@Override
	public double getPacY() {
		return syujinkou != null ? syujinkou.getY() : 0;
	}

	// 現在のステージ番号(1～3)を返す。
	@Override
	public int getStageNumber() {
		return stageNumber;
	}

	// ゲームがまだプレイヤーの初回入力を待っている状態かどうかを返す。
	@Override
	public boolean isWaitingStart() {
		return waitingStart;
	}

	// 敵キャラクターのリストを返す。
	@Override
	public List<Enemy> getEnemies() {
		return enemies;
	}

	// ※ common.Direction と Characters.Direction の型が合わない場合はキャストや変換を行ってください
	/**
	 * プレイヤーの現在の移動方向を取得する。 syujinkou、またはsyujinkouの方向がnullの場合はDirection.NONEを返す。
	 * 名前ベースでCharacters.Directionへの変換を試み、失敗した場合もNONEを返す。
	 */
	@Override
	public Direction getPlayerDirection() {
		if (syujinkou == null || syujinkou.getDirection() == null) {
			return Direction.NONE;
		}

		// Characters.Direction から 正解の test.Direction へ名前ベースで型変換
		try {
			return Direction.valueOf(syujinkou.getDirection().name());
		} catch (IllegalArgumentException e) {
			return Direction.NONE;
		}
	}

	// --- getters ---
	// 各マスに配置されているアイテムの二次元配列を返す。
	public Item[][] getItemMap() {
		return itemMap;
	}

	// 口パクアニメーション用の現在の角度を返す。
	/*
	 * public double getMouthAngle() { return mouthAngle; }
	 */
	// プレイヤーのキャラクターオブジェを返す。
	public Syujinkou getsyujinkou() {
		return syujinkou;
	}

	/**
	 * FEVER状態の残り時間（ミリ秒）を返す。 一時停止中は、一時停止した時点での残り時間を固定して返す。
	 * FEVERが発動していない、または既に終了している場合は0以上の値（実質0）を返す。
	 */
	public long getFeverRemainingTime() {

		if (paused && feverEndTime > 0) {
			return Math.max(0, feverEndTime - pauseStartTime);
		}
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

	// 残りアイテム数が0以下、つまり全てのドット・パワーエサを食べ終えたかどうかを返す。(ステージクリア判定)。
	public boolean isCleared() {
		return remainingItems <= 0;
	}

	// ゲームオーバーになったかどうかを返す(プレイヤーのHPが0になった後、死亡アニメーション終了時にtrueになる)。
	public boolean isGameOver() {
		return gameOver;
	}

	//フルーツ
	public Fruit getCurrentFruit() {
		return currentFruit;
	}

	// フルーツのスコアポップアップがまだ表示中か判定する（時間経過で自動的にfalseになる）
	public boolean isFruitPopupActive() {
		if (fruitPopupActive && System.currentTimeMillis() - fruitPopupStartTime > FRUIT_POPUP_DURATION) {
			fruitPopupActive = false;
		}
		return fruitPopupActive;
	}

	// 表示するフルーツのスコア値を返す
	public int getFruitPopupScore() {
		return fruitPopupScore;
	}

	// 食べた瞬間のX座標を返す（ポップアップ表示用、固定値・ピクセル）
	public double getFruitPopupX() {
		return fruitPopupX;
	}

	// 食べた瞬間のY座標を返す（ポップアップ表示用、固定値・ピクセル）
	public double getFruitPopupY() {
		return fruitPopupY;
	}

	// ポップアップの進行度を0.0(開始)〜1.0(終了)で返す
	public double getFruitPopupProgress() {
		long elapsed = System.currentTimeMillis() - fruitPopupStartTime;
		return Math.min(1.0, Math.max(0.0, elapsed / (double) FRUIT_POPUP_DURATION));
	}

	public int getFruitRow() {
		return fruitRow;
	}

	public int getFruitCol() {
		return fruitCol;
	}

}
