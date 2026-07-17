/**
 * ステージ2のゲームデータ管理クラス
 *
 * 主な役割
 * ・マップ管理
 * ・主人公管理
 * ・敵管理
 * ・アイテム管理
 * ・フルーツ管理
 * ・FEVER管理
 * ・ポーズ管理
 * ・ステージクリア判定
 */

import { BlueEnemy } from "../../Characters/BlueEnemy.js";
import { Direction } from "../../Characters/Direction.js";
import { EnemyState } from "../../Characters/EnemyState.js";
import { GreenEnemy } from "../../Characters/GreenEnemy.js";
import { RedEnemy } from "../../Characters/RedEnemy.js";
import { Syujinkou } from "../../Characters/Syujinkou.js";
import { YellowEnemy } from "../../Characters/YellowEnemy.js";
import { Chii } from "../../Items/Chii.js";
import { Fruit } from "../../Items/Fruit.js";
import { FruitType } from "../../Items/FruitType.js";
import { Point } from "../../Items/Point.js";
import { Bgm } from "../../start/Bgm.js";
import { SoundManager } from "../../start/SoundManager.js";
import { WallOutline } from "../view/WallOutline.js";

// MapData.js の先頭付近(import群のあと)に追加
const DIRECTION_MAP = {
  UP: Direction.UP,
  DOWN: Direction.DOWN,
  LEFT: Direction.LEFT,
  RIGHT: Direction.RIGHT,
};

export class MapData {

	// ==================================================
	// マップ定義(28×31マス)
	// ==================================================

	// 1マスのサイズ(30×30px)
	static TILE_SIZE = 30;

	// 0：道 1：壁 2：パワーエサ 3:仙石さん 7:扉 8:巣 9: ワープ
	#map = [

			[ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ], // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■
			[ 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1 ], // ■　　餌　　　　　　　■■■■■■　　　　　　　餌　　■
			[ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 ], // ■　■■■　■■■■　■■■■■■　■■■■　■■■　■
			[ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 ], // ■　■■■　■■■■　■■■■■■　■■■■　■■■　■
			[ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 ], // ■　　　　　　　■■　■■■■■■　■■　　　　　　　■
			[ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 ], // ■■■■　■■　■■　　　　　　　　■■　■■　■■■■
			[ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 ], // ■■■■　■■　■■　■■■　■■　■■　■■　■■■■
			[ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 ], // ■■■■　　　　　　　■■■　■■　　　　　　　■■■■
			[ 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 ], // ■■■■　■■■■■　■■　　■■　■■■■■　■■■■
			[ 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1 ], // ■　　　　■■■■■　■■　■■■　■■■■■　　　　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■■　■■　■■■　■■■■■　■■　■
			[ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 ], // ■　■■　　　　　　　　　　　　　　　　　　　　■■　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 7, 7, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　■■■扉扉■■■　■■■■　■■　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　■巣巣巣巣巣巣■　■■■■　■■　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　■巣巣巣巣巣巣■　■■■■　■■　■
			[ 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8, 8, 8, 8, 8, 8, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9 ], // ワ　　　　　　　　　■巣巣巣巣巣巣■　　　　　　　　　ワ
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　■■■■■■■■　■■■■　■■　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　　　　　　　　　　■■■■　■■　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　■■■■■　■■　■■■■　■■　■
			[ 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1 ], // ■　■■　■■■■　■■■■■　■■　■■■■　■■　■
			[ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 ], // ■　■■　　　　　　■■　　　　■■　　　　　　■■　■
			[ 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1 ], // ■　　　　■■■■　■■　■■■■■　■■■■　　　　■
			[ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 ], // ■■■■　■■■■　■■　■■■■■　■■■■　■■■■
			[ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 ], // ■■■■　　　　　　　　　　　　仙　　　　　　　■■■■
			[ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 ], // ■■■■　■■　■■　■■■■■■　■■　■■　■■■■
			[ 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1 ], // ■■■■　■■　■■　■■■■■■　■■　■■　■■■■
			[ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 ], // ■　　　　　　　■■　　　　　　　　■■　　　　　　　■
			[ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 ], // ■　■■■　■■■■　■■■■■■　■■■■　■■■　■
			[ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 ], // ■　■■■　■■■■　■■■■■■　■■■■　■■■　■
			[ 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1 ], // ■　　餌　　　　　　　■■■■■■　　　　　　　餌　　■
			[ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ], // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■
	];

	// ==================================================
	// キャラクター管理
	// ==================================================

	// 主人公キャラクター
	#syujinkou;

	// 敵キャラクター一覧
	#enemies = [];

	// ==================================================
	// ゲーム状態管理
	// ==================================================

	// ゲームが一時停止中か
	#paused = false;

	// ゲームオーバー判定
	#gameOver = false;

	// ゲーム開始待ち
	#waitingStart = true;

	// 現在のステージ番号を書く(1 = ステージ1, 2 = ステージ2, 3 = ステージ3）
	#stageNumber = 2;

  // 壁のアウトラインを保持するプロパティ
  #wallOutline;

	// ==================================================
	// アイテム管理
	// ==================================================

	// マップ上のアイテム配置
	#itemMap;

	// 初期状態のアイテム配置（復活用)
	#initialItemMap;

	// アイテム総数
	#totalItems;

	// 残りアイテム数をカウントする変数
	#remainingItems = 0;

	// エサ復活を有効にするか？
	#enableRespawn = false;

	// ==================================================
	// ポーズ管理
	// ==================================================

	#pauseStartTime = 0;

	// ==================================================
	// ワープ管理
	// ==================================================

	// ワープ直後の連続ワープ防止
	#justWarped = false;

	// 最後にワープした座標
	#lastWarpX = -1;
	#lastWarpY = -1;

	// ==================================================
	// FEVER管理
	// ==================================================

	// FEVER終了時刻
	#feverEndTime = 0;

	// ==================================================
	// 敵行動モード管理
	// ==================================================

	// モード開始時刻
	#modeStartTime = 0;

	// true:CHASE false:SCATTER
	#chaseMode = false;

	// ==================================================
	// フルーツ管理
	// ==================================================

	// フルーツ用マップ値
	static FRUIT_VALUE = 3;

	// 出現位置
	#fruitRow = -1;
	#fruitCol = -1; // map配列内でのフルーツを表す数値

	// 現在出現中のフルーツ(nullなら未出現)
	#currentFruit = null;

	// 最後にフルーツを出した時刻
	#lastFruitSpawnTime = 0;

	// 最後にフルーツを出した時点のスコア
	#lastFruitScore = 0;

	// 出現条件
	static #FRUIT_TIME_INTERVAL = 15000; // 15秒ごと
	static #FRUIT_SCORE_INTERVAL = 1000; // 1000点ごと

	// ==================================================
	// フルーツスコア表示管理
	// ==================================================

	// フルーツスコアポップアップ用
	#fruitPopupActive = false;

	// 表示開始時刻
	#fruitPopupStartTime = 0;

	// 表示スコア
	#fruitPopupScore = 0;

	// 表示時間(ms)
	static #FRUIT_POPUP_DURATION = 1000;

	// 表示位置
	#fruitPopupX = 0; // 食べた瞬間のX座標（固定・ピクセル）
	#fruitPopupY = 0; // 食べた瞬間のY座標（固定・ピクセル）

	/**
	 * 練習モード用の初期化メソッド。プレイヤーの初期位置を通常とは別の座標に設定し、
	 * アイテム(ドット・パワーエサ)をマップ全体に配置する。enableRespawn が true の場合は、
	 * エサ復活用に初期状態のitemMapのコピーを保存しておく。
	 *
	 * ※元のJavaコードでは `SampleModel` という大文字始まりのメソッド名だったが、
	 *   （コンストラクタと紛らわしい命名だったため）JSではメソッドの命名規則に
	 *   合わせて `sampleModel` とキャメルケースに直している。
	 *
	 * @param enableRespawn エサ（ドット）を食べ切ったあとに復活させるかどうか
	 */
	sampleModel(enableRespawn) {
		this.#enableRespawn = enableRespawn;
		this.#syujinkou = new Syujinkou(14 * MapData.TILE_SIZE, 23 * MapData.TILE_SIZE, 2);
		this.#itemMap = new Array(this.#map.length);
		this.#remainingItems = 0;
		this.#lastFruitSpawnTime = Date.now();
		this.#lastFruitScore = 0;

		for (let row = 0; row < this.#map.length; row++) {
			this.#itemMap[row] = new Array(this.#map[0].length).fill(null);

			for (let col = 0; col < this.#map[0].length; col++) {
				const pixelX = col * MapData.TILE_SIZE + MapData.TILE_SIZE / 2;
				const pixelY = row * MapData.TILE_SIZE + MapData.TILE_SIZE / 2;

				if (this.#map[row][col] === 0) {
					this.#itemMap[row][col] = new Point(pixelX, pixelY);
					this.#remainingItems++;
				} else if (this.#map[row][col] === 2) {
					this.#itemMap[row][col] = new Chii(pixelX, pixelY);
					this.#remainingItems++;
				}
			}
		}

		// エサ復活が有効なときだけ初期状態を保存（エサ復活用）
		if (enableRespawn) {
			this.#initialItemMap = this.#copyItemMap(this.#itemMap);
		} else {
			this.#initialItemMap = null;
		}
	}

	/**
	 * itemMap（アイテム配置の二次元配列）の浅いコピーを作成する。
	 * エサ復活機能で「初期状態のアイテム配置」を保存・復元するために使用する。
	 */
	#copyItemMap(src) {
		const dst = new Array(src.length);
		for (let r = 0; r < src.length; r++) {
			dst[r] = new Array(src[0].length);
			for (let c = 0; c < src[0].length; c++) {
				dst[r][c] = src[r][c];
			}
		}
		return dst;
	}

	/**
	 * 本番モード（ストーリーモード）用のデフォルトコンストラクタ相当。
	 * プレイヤーの初期位置を設定し、マップ上の道(0)とパワーエサ(2)の位置に
	 * アイテムを配置、敵を初期化する。最後にエサ復活用の初期状態を保存し、
	 * 総アイテム数(totalItems)を記録する。
	 *
	 * ※Javaでは MapData() と MapData(boolean paused) の2つのコンストラクタが
	 *   オーバーロードされていたが、JSはオーバーロードできないため
	 *   デフォルト引数(paused = false)で1本化している。
	 *   new MapData() は Java の MapData() 相当、
	 *   new MapData(true) は Java の MapData(true) 相当になる。
	 */
	constructor(paused = false) {
		this.#syujinkou = new Syujinkou(14 * MapData.TILE_SIZE, 23 * MapData.TILE_SIZE, 2);
		this.#itemMap = new Array(this.#map.length);
		this.#remainingItems = 0;
		this.#lastFruitSpawnTime = Date.now();
		this.#lastFruitScore = 0;

		// アイテムの配置
		for (let row = 0; row < this.#map.length; row++) {
			this.#itemMap[row] = new Array(this.#map[0].length).fill(null);

			for (let col = 0; col < this.#map[0].length; col++) {
				const pixelX = col * MapData.TILE_SIZE + MapData.TILE_SIZE / 2;
				const pixelY = row * MapData.TILE_SIZE + MapData.TILE_SIZE / 2;

				if (this.#map[row][col] === 0) {
					this.#itemMap[row][col] = new Point(pixelX, pixelY);
					this.#remainingItems++;
				} else if (this.#map[row][col] === 2) {
					this.#itemMap[row][col] = new Chii(pixelX, pixelY);
					this.#remainingItems++;
				}
			}
		}

		// 敵の初期位置
		this.initEnemy(null);
		this.#wallOutline = new WallOutline(this.#map, MapData.TILE_SIZE);

		// アイテムが完全に配置し終わった後で、バックアップを取り、復活を有効にする
		this.#initialItemMap = this.#copyItemMap(this.#itemMap);
		this.#enableRespawn = true;

		// 最初に配置し終わった時の総数を記憶しておく
		this.#totalItems = this.#remainingItems;

		// 受け取ったpausedを最後に反映(Javaのオーバーロード版コンストラクタ相当)
		this.#paused = paused;
	}

	/**
	 * 敵キャラクター（赤・緑・黄・青）を初期化してenemies配列に追加する。
	 * 既存の配列を一度クリアしてから追加するため、複数回呼んでも敵が重複しない。
	 * 追加後、全ての敵の状態をSCATTER（散開）にリセットする。
	 *
	 * ※enemyImageView 引数は元のJavaコードでも本文内で一度も使われていないため、
	 *   JS版でも未使用のまま残している（互換性のためシグネチャだけ維持）。
	 */
	initEnemy(enemyImageView) {
		this.#enemies.length = 0; // 配列をクリア
		this.#enemies.push(new RedEnemy(this));
		this.#enemies.push(new GreenEnemy(this));
		this.#enemies.push(new YellowEnemy(this));
		this.#enemies.push(new BlueEnemy(this));

		for (const e of this.#enemies) {
			if (e != null) {
				e.setCurrentState(EnemyState.SCATTER);
			}
		}
	}

	/**
	 * ゲームの一時停止／再開を切り替える。一時停止に入るときは開始時刻を記録し、
	 * 敵のタイマーを止める。再開するときは一時停止していた時間分だけ、
	 * FEVERタイマーやCHASE/SCATTERタイマーを後ろにずらして帳尻を合わせ、
	 * 敵のタイマーを再開する。
	 */
	togglePause() {
		if (!this.#paused) {
			this.#paused = true;
			this.#pauseStartTime = Date.now();
			Bgm.pauseBGM();
			for (const e of this.#enemies) {
				e.pauseTimer();
			}
		} else {
			this.#paused = false;
			Bgm.resumeBGM();
			const pauseDuration = Date.now() - this.#pauseStartTime;

			// FEVER停止
			if (this.#feverEndTime > 0) {
				this.#feverEndTime += pauseDuration;
			}
			// CHASE/SCATTERタイマー停止
			if (this.#modeStartTime > 0) {
				this.#modeStartTime += pauseDuration;
			}

			if (this.#lastFruitSpawnTime > 0) {
				this.#lastFruitSpawnTime += pauseDuration;
			}

			for (const e of this.#enemies) {
				e.resumeTimer();
			}
		}
	}

	/**
	 * ゲーム全体を1フレーム更新する。
	 *
	 * 更新内容: ・死亡アニメーション ・プレイヤー移動 ・FEVER終了判定
	 * ・CHASE/SCATTER切替 ・敵移動 ・フルーツ管理 ・当たり判定
	 */
	update() {
		if (this.#paused) return;

		// 死んだときのアニメーション
		if (this.#syujinkou.isDyingAnimation()) {
			if (this.#syujinkou.updateDyingAnimation()) {
				if (this.#syujinkou.isAlive()) {
					this.#syujinkou.resetToStartPosition();
					for (const enemy of this.#enemies) {
						enemy.resetToStartPosition();
						enemy.setCurrentState(EnemyState.SCATTER);
					}

					this.#modeStartTime = 0;
					this.#chaseMode = false;
					this.#waitingStart = true;
				} else {
					this.#gameOver = true;
					this.#paused = true;
				}
			}
			return;
		}

		// パックマンの移動処理
		this.updatePacman();

		// FEVER終了判定
		if (this.#feverEndTime > 0 && Date.now() >= this.#feverEndTime) {
			this.#feverEndTime = 0;
			this.#syujinkou.setFever(false);
			Bgm.stopFeverBGM(); // ステージBGMに復帰

			for (const e of this.#enemies) {
				if (e.getCurrentState() === EnemyState.FEVER) {
					e.setCurrentState(EnemyState.SCATTER);
				}
			}
			console.log("FEVER終了");
		}

		// CHASE/SCATTER管理
		if (!this.#waitingStart) {
			const elapsed = Date.now() - this.#modeStartTime;

			if (this.#chaseMode && elapsed >= 20000) {
				this.#chaseMode = false;
				this.#modeStartTime = Date.now();

				for (const e of this.#enemies) {
					if (e.getCurrentState() !== EnemyState.DEAD && e.getCurrentState() !== EnemyState.FEVER) {
						e.setCurrentState(EnemyState.SCATTER);
					}
				}
				console.log("SCATTER開始");
			} else if (!this.#chaseMode && elapsed >= 7000) {
				this.#chaseMode = true;
				this.#modeStartTime = Date.now();

				for (const e of this.#enemies) {
					if (e.getCurrentState() !== EnemyState.DEAD && e.getCurrentState() !== EnemyState.FEVER) {
						e.setCurrentState(EnemyState.CHASE);
					}
				}
				console.log("CHASE開始");
			}

			// 敵移動
			for (const e of this.#enemies) {
				e.move(this.#map);
			}

			this.#checkFruitSpawn();
			this.#updateFruit();
		}

		// パックマンと敵の当たり判定を毎フレーム確認
		this.#checkCollision();
	}

	/**
	 * 時間経過 または スコア到達 を条件にフルーツを固定位置に出現させる
	 */
	#checkFruitSpawn() {
		if (this.#currentFruit != null) return; // 既に出現中なら何もしない

		const now = Date.now();
		const score = this.#syujinkou.getScore();

		const timeCondition = now - this.#lastFruitSpawnTime >= MapData.#FRUIT_TIME_INTERVAL;
		const scoreCondition = score - this.#lastFruitScore >= MapData.#FRUIT_SCORE_INTERVAL;

		if (timeCondition || scoreCondition) {
			this.#spawnFruit();
			this.#lastFruitSpawnTime = now;
			this.#lastFruitScore = score;
		}
	}

	#spawnFruit() {
		// 道(0)かつ、まだドットが残っていない(itemMapがnull)マスだけを候補にする
		const candidates = [];
		for (let row = 0; row < this.#map.length; row++) {
			for (let col = 0; col < this.#map[0].length; col++) {
				if (this.#map[row][col] === 0 && this.#itemMap[row][col] == null) {
					candidates.push([row, col]);
				}
			}
		}

		if (candidates.length === 0) {
			return; // 万が一、道が無ければ何もしない
		}

		// ランダムに1マス選ぶ
		const chosen = candidates[Math.floor(Math.random() * candidates.length)];
		this.#fruitRow = chosen[0];
		this.#fruitCol = chosen[1];

		const type = FruitType.random();
		this.#currentFruit = new Fruit(type);
		this.#map[this.#fruitRow][this.#fruitCol] = MapData.FRUIT_VALUE;

		console.log(`${type}が (${this.#fruitRow}, ${this.#fruitCol}) に出現しました！`);
	}

	/**
	 * フルーツのタイマー更新。時間切れになったら消す。
	 */
	#updateFruit() {
		if (this.#currentFruit == null) return;

		this.#currentFruit.update();
		if (this.#currentFruit.isExpiredFruit()) {
			this.#map[this.#fruitRow][this.#fruitCol] = 0; // 消えたら道に戻す
			this.#currentFruit = null;
			this.#fruitRow = -1;
			this.#fruitCol = -1;
			console.log("フルーツが消えました");
		}
	}

	/**
	 * 主人公の移動更新処理。
	 *
	 * 処理内容: ・ワープ処理 ・壁判定 ・移動実行 ・ドット取得 ・パワーエサ取得
	 * ・FEVER開始 ・フルーツ取得
	 */
	updatePacman() {
		if (this.#paused || !this.#syujinkou.isAlive()) return;

		// 移動先のタイルを予測検出し、壁(1), 扉(7), 巣(8)への進入を防ぐ
		const tileX = Math.floor((this.#syujinkou.getX() + MapData.TILE_SIZE / 2) / MapData.TILE_SIZE);
		const tileY = Math.floor((this.#syujinkou.getY() + MapData.TILE_SIZE / 2) / MapData.TILE_SIZE);

		// --- ワープ抑止ロジック ---
		let skipWarp = false;
		if (this.#justWarped) {
			if (tileX === this.#lastWarpX && tileY === this.#lastWarpY) {
				skipWarp = true;

				// ワープ直後は、プレイヤーの入力を上書きして強制直進（先行入力を固定）
				if (this.#lastWarpX === 27) {
					this.#syujinkou.setNextDirection(Direction.LEFT);
				} else if (this.#lastWarpX === 0) {
					this.#syujinkou.setNextDirection(Direction.RIGHT);
				}
			} else {
				this.#justWarped = false;
				this.#lastWarpX = -1;
				this.#lastWarpY = -1;
			}
		}

		// --- ワープ処理 ---
		if (!skipWarp && tileX >= 0 && tileX < this.#map[0].length && tileY >= 0 && tileY < this.#map.length) {
			if (this.#map[tileY][tileX] === 9) {
				let warpX = tileX;
				let warpY = tileY;
				const currentDir = this.#syujinkou.getDirection();

				if (currentDir !== Direction.NONE) {
					if (currentDir.dx !== 0) {
						for (let x = 0; x < this.#map[0].length; x++) {
							if (this.#map[tileY][x] === 9 && x !== tileX) {
								warpX = x;
								break;
							}
						}
					}

					if (currentDir.dy !== 0) {
						for (let y = 0; y < this.#map.length; y++) {
							if (this.#map[y][tileX] === 9 && y !== tileY) {
								warpY = y;
								break;
							}
						}
					}
				}

				const newPacX = warpX * MapData.TILE_SIZE;
				const newPacY = warpY * MapData.TILE_SIZE;

				this.#syujinkou.setX(newPacX);
				this.#syujinkou.setY(newPacY);

				// 効果音
				SoundManager.play(SoundManager.WARP);

				this.#justWarped = true;
				this.#lastWarpX = warpX;
				this.#lastWarpY = warpY;
				return;
			}
		}

		// 巣(8)と扉(7)を同様に壁扱いしている。
		const moveMap = new Array(this.#map.length);
		for (let r = 0; r < this.#map.length; r++) {
			moveMap[r] = new Array(this.#map[0].length);
			for (let c = 0; c < this.#map[0].length; c++) {
				if (this.#map[r][c] === 7 || this.#map[r][c] === 8) {
					moveMap[r][c] = 1; // 7(扉)と8(巣)を、移動処理の時だけ壁(1)に化けさせる
				} else {
					moveMap[r][c] = this.#map[r][c];
				}
			}
		}

		this.#syujinkou.move(moveMap);

		const currentTileX = Math.floor((this.#syujinkou.getX() + MapData.TILE_SIZE / 2) / MapData.TILE_SIZE);
		const currentTileY = Math.floor((this.#syujinkou.getY() + MapData.TILE_SIZE / 2) / MapData.TILE_SIZE);

		if (
			currentTileY >= 0 &&
			currentTileY < this.#map.length &&
			currentTileX >= 0 &&
			currentTileX < this.#map[0].length
		) {
			const item = this.#itemMap[currentTileY][currentTileX];

			if (item != null) {
				item.onEaten(this.#syujinkou);

				// パワーエサ(2)を食べたらFEVER
				if (this.#map[currentTileY][currentTileX] === 2) {
					console.log("FEVER開始！");
					SoundManager.play(SoundManager.POWER_EAT); // パワーエサ効果音を先に再生

					// 0.4秒遅らせてからFEVER BGMを開始
					// ※JavaFXの PauseTransition + Duration.seconds(0.4) を setTimeout に置き換え
					setTimeout(() => {
						Bgm.playFeverBGM();
					}, 400);

					this.#syujinkou.setFever(true);
					// 毎回7秒にリセット
					this.#feverEndTime = Date.now() + 7000;

					for (const e of this.#enemies) {
						if (e.getCurrentState() !== EnemyState.DEAD) {
							e.setCurrentState(EnemyState.FEVER);
						}
					}

					// パワーエサを食べたので50点加算
					this.#syujinkou.addScore(50);
				} else {
					// 普通のドットを食べたので10点加算
					this.#syujinkou.addScore(10);
				}

				this.#itemMap[currentTileY][currentTileX] = null;
				this.#remainingItems--; // 1個食べたのでカウントを減らす
				console.log(`残りのドット数: ${this.#remainingItems}`); // デバッグ用ログ
			}
		}

		// フルーツを食べたかチェック
		if (this.#currentFruit != null && currentTileY === this.#fruitRow && currentTileX === this.#fruitCol) {
			this.#currentFruit.onEaten(this.#syujinkou);

			// スコアポップアップ開始
			this.#fruitPopupScore = this.#currentFruit.getType().getScore();
			this.#fruitPopupStartTime = Date.now();
			this.#fruitPopupActive = true;
			this.#fruitPopupX = this.#fruitCol * MapData.TILE_SIZE + MapData.TILE_SIZE / 2;
			this.#fruitPopupY = this.#fruitRow * MapData.TILE_SIZE + MapData.TILE_SIZE / 2;

			this.#map[this.#fruitRow][this.#fruitCol] = 0;
			this.#currentFruit = null;
			this.#fruitRow = -1;
			this.#fruitCol = -1;
		}
	}

	/**
	 * 練習モード用：itemMapを初期状態に戻し、残りアイテム数を最大数にリセットする。
	 * これにより isCleared() が再び false に戻り、ゲームを終わらせずに
	 * エサを食べ続けられるようになる（練習モードのループ継続用）。
	 */
	respawnDots() {
		if (this.#initialItemMap != null) {
			// 1. マップのアイテム配置を初期状態にコピー
			this.#itemMap = this.#copyItemMap(this.#initialItemMap);

			// 2. 残りアイテム数を初期の総数にリセット（これで isCleared() が false に戻る）
			this.#remainingItems = this.#totalItems;

			// フルーツ関連の状態もリセット
			if (this.#fruitRow !== -1 && this.#fruitCol !== -1) {
				this.#map[this.#fruitRow][this.#fruitCol] = 0;
			}
			this.#currentFruit = null;
			this.#fruitRow = -1;
			this.#fruitCol = -1;
			this.#lastFruitSpawnTime = Date.now();
			this.#lastFruitScore = this.#syujinkou != null ? this.#syujinkou.getScore() : 0;

			console.log(`【練習モード】エサが再配置され、残りカウントが ${this.#remainingItems} にリセットされました。`);
		}
	}

	/**
	 * キー入力などから呼ばれ、プレイヤーの次の移動方向をセットする。
	 * ゲームがまだ開始待ち(waitingStart)の場合は、この最初の入力をトリガーとして
	 * ゲームを開始状態にし、CHASE/SCATTERタイマーを開始する。
	 *
	 * @param dir プレイヤーに設定する次の移動方向
	 */
	setNextDirection(dir) {
		// 文字列("UP"など)で渡された場合はDirectionオブジェクトに変換する
		const resolvedDir = typeof dir === "string" ? DIRECTION_MAP[dir] : dir;

		if (this.#syujinkou != null && resolvedDir != null) {
			this.#syujinkou.setNextDirection(resolvedDir);
		}

		// 初回入力でゲーム開始
		if (this.#waitingStart) {
			this.#waitingStart = false;
			this.#modeStartTime = Date.now();
			this.#lastFruitSpawnTime = Date.now();
			console.log("ゲーム開始");
		}
	}

	/**
	 * プレイヤーと各敵との距離をチェックし、一定距離(collisionThreshold)以内なら
	 * 「衝突」とみなす当たり判定処理。敵がFEVER状態の場合はプレイヤーが敵を倒したことになり、
	 * スコア加算＆敵をDEAD状態にする。それ以外（通常状態の敵）に衝突した場合は、
	 * プレイヤーがダメージを受け(takeDamage)、死亡（ミス）アニメーションを開始する(startDying)。
	 * すでにプレイヤーが死んでいる場合は何もしない。
	 */
	#checkCollision() {
		if (!this.#syujinkou.isAlive()) return;

		const pacCenterX = this.#syujinkou.getX() + MapData.TILE_SIZE / 2;
		const pacCenterY = this.#syujinkou.getY() + MapData.TILE_SIZE / 2;
		const collisionThreshold = MapData.TILE_SIZE * 0.8;

		for (const e of this.#enemies) {
			if (e.getCurrentState() === EnemyState.DEAD) {
				continue;
			}

			const dx = pacCenterX - e.getX();
			const dy = pacCenterY - e.getY();

			if (Math.sqrt(dx * dx + dy * dy) < collisionThreshold) {
				// FEVER中の敵は食べられる
				if (e.getCurrentState() === EnemyState.FEVER) {
					// 効果音
					SoundManager.play(SoundManager.ENEMY_DEAD);

					// 敵を倒したのでスコアを加算し、その場にスコア表示を開始する
					const defeatScore = 200;
					this.#syujinkou.addScore(defeatScore);
					e.onDefeated(defeatScore);
					continue;
				}

				if (e.getCurrentState() === EnemyState.DEAD) {
					continue;
				}

				console.log("💥敵に捕まった！");

				// 効果音
				SoundManager.play(SoundManager.DAMAGE);

				this.#syujinkou.takeDamage();

				if (this.#syujinkou.getHp() === 1) {
          		Bgm.playPinchiBGM();
     		   }
				this.#syujinkou.startDying();

				return;
			}
		}
	}

	// ==================================================
	// getter
	// ==================================================

	// ゲームが一時停止中かどうか返す。
	isPaused() {
		return this.#paused;
	}

	// マップデータ(壁・道・アイテム種別を表す二次元配列)を返す。
	getMap() {
		return this.#map;
	}

	// プレイヤーの現在のX座標(ピクセル)を返す。syujinkouがnullの場合は0を返す。
	getPacX() {
		return this.#syujinkou != null ? this.#syujinkou.getX() : 0;
	}

	// プレイヤーの現在のY座標(ピクセル)を返す。syujinkouがnullの場合は0を返す。
	getPacY() {
		return this.#syujinkou != null ? this.#syujinkou.getY() : 0;
	}

	// 現在のステージ番号(1～3)を返す。
	getStageNumber() {
		return this.#stageNumber;
	}

	// ゲームがまだプレイヤーの初回入力を待っている状態かどうかを返す。
	isWaitingStart() {
		return this.#waitingStart;
	}

	// 敵キャラクターの配列を返す。
	getEnemies() {
		return this.#enemies;
	}

	/**
	 * プレイヤーの現在の移動方向を取得する。
	 * syujinkou、またはsyujinkouの方向がnullの場合はDirection.NONEを返す。
	 *
	 * ※Java版では「common.Direction」と「Characters.Direction」という
	 *   2つの別々のDirection型が存在し、Direction.valueOf(name())で
	 *   名前ベースの変換を行っていた。JS版ではDirectionモジュールを
	 *   全クラスで共有しているため、この変換処理は不要になり、
	 *   単にそのまま返すだけでよい。
	 */
	getPlayerDirection() {
		if (this.#syujinkou == null || this.#syujinkou.getDirection() == null) {
			return Direction.NONE;
		}
		return this.#syujinkou.getDirection();
	}

	// 各マスに配置されているアイテムの二次元配列を返す。
	getItemMap() {
		return this.#itemMap;
	}

	// プレイヤーのキャラクターオブジェクトを返す。
	// ※Java版のメソッド名 getsyujinkou() は小文字始まりのタイポに近い命名だったため、
	//   JS版では getSyujinkou() とキャメルケースに直している。
	getSyujinkou() {
		return this.#syujinkou;
	}

	/**
	 * FEVER状態の残り時間（ミリ秒）を返す。一時停止中は、一時停止した時点での
	 * 残り時間を固定して返す。FEVERが発動していない、または既に終了している場合は
	 * 0以上の値（実質0）を返す。
	 */
	getFeverRemainingTime() {
		if (this.#paused && this.#feverEndTime > 0) {
			return Math.max(0, this.#feverEndTime - this.#pauseStartTime);
		}
		return Math.max(0, this.#feverEndTime - Date.now());
	}

	// 既存の古いゲッターもエラー防止で残し、配列の先頭(赤)を返す
	getEnemy() {
		return this.#enemies.length === 0 ? null : this.#enemies[0];
	}

	// ==================================================
	// setter
	// ==================================================

	// ステージが切り替わったときに外から数値を変更できるようにする
	setStageNumber(stageNum) {
		this.#stageNumber = stageNum;
	}

	// 残りアイテム数が0以下、つまり全てのドット・パワーエサを食べ終えたかどうかを返す(ステージクリア判定)。
	isCleared() {
		return this.#remainingItems <= 0;
	}

	// ゲームオーバーになったかどうかを返す(プレイヤーのHPが0になった後、死亡アニメーション終了時にtrueになる)。
	isGameOver() {
		return this.#gameOver;
	}

	// フルーツ
	getCurrentFruit() {
		return this.#currentFruit;
	}

	// フルーツのスコアポップアップがまだ表示中か判定する（時間経過で自動的にfalseになる）
	isFruitPopupActive() {
		if (this.#fruitPopupActive && Date.now() - this.#fruitPopupStartTime > MapData.#FRUIT_POPUP_DURATION) {
			this.#fruitPopupActive = false;
		}
		return this.#fruitPopupActive;
	}

	// 表示するフルーツのスコア値を返す
	getFruitPopupScore() {
		return this.#fruitPopupScore;
	}

	// 食べた瞬間のX座標を返す（ポップアップ表示用、固定値・ピクセル）
	getFruitPopupX() {
		return this.#fruitPopupX;
	}

	// 食べた瞬間のY座標を返す（ポップアップ表示用、固定値・ピクセル）
	getFruitPopupY() {
		return this.#fruitPopupY;
	}

	// ポップアップの進行度を0.0(開始)〜1.0(終了)で返す
	getFruitPopupProgress() {
		const elapsed = Date.now() - this.#fruitPopupStartTime;
		return Math.min(1.0, Math.max(0.0, elapsed / MapData.#FRUIT_POPUP_DURATION));
	}

	getFruitRow() {
		return this.#fruitRow;
	}

	getFruitCol() {
		return this.#fruitCol;
	}

	// デバッグ用強制クリアボタン
	forceStageClear() {
		this.#remainingItems = 0;
		console.log("【デバッグ】強制ステージクリアを実行しました。");
	}

	getWallOutline() {
		return this.#wallOutline;
	}
}