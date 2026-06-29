package test3.model;

import Items.Chii;
import Items.Item;
import Items.Point;
import sample.Sengoku;

public class MapData {

	//マップ定義(28×31マス)
	//1マスのサイズ(30×30ピクセル)
	public static final int TILE_SIZE = 30;

	//0: 道, 1: 壁, 9: ワープ
	private final int[][] map = {

			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, //■■■■■■■■■■■■　　　　■■■■■■■■■■■■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //■　　　　　　　　　　■　　　　■　　　　　　　　　　■
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■■■■　■　　　　■　■■■■■■■■　■
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■■■■　■　　　　■　■■■■■■■■　■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 }, //■　　　　　　　■■　■　　　　■　■■　　　　　　　■
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■　■■　■　　　　■　■■　■■■■■　■
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■　■■　■　　　　■　■■　■■■■■　■
			{ 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1 }, //■　　　　■■　■■　■■■■■■　■■　■■　　　　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■　　　　　　　　　　　　　　■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■　■■■■■■■■■■■■　■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■　■■■■■■■■■■■■　■■　■■　■
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, //■　■■　　　　　　　　　　　　　　　　　　　　■■　■
			{ 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1 }, //■　■■■■　■■　■■■　　■■■　■■　■■■■　■
			{ 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1 }, //■　■■■■　■■　■　　　　　　■　■■　■■■■　■
			{ 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1 }, //■　　　　　　■■　■　　　　　　■　■■　　　　　　■
			{ 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1 }, //■　■■■■　■■　■　　　　　　■　■■　■■■■　■
			{ 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1 }, //■　■■■■　■■　■■■■■■■■　■■　■■■■　■
			{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1 }, //■　■■　　　　　　　　　　　　　　　　　　　　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■■■　■■■■■■■■　■■■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■■■　■■■■■■■■　■■■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■　　　　　　　　　　　　　　■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■　■■　■■■■■■　■■　■■　■■　■
			{ 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1 }, //■　■■　■■　■■　■　　　　■　■■　■■　■■　■
			{ 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1 }, //■　　　　■■　■■　■　　　　■　■■　■■　　　　■
			{ 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■　　　　■　　　　■　　　　■■■■■　■
			{ 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■　■■　■　　　　■　■■　■■■■■　■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 }, //■　　　　　　　■■　■　　　　■　■■　　　　　　　■
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■■■■　■　　　　■　■■■■■■■■　■
			{ 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, //■　■■■■■■■■　■　　　　■　■■■■■■■■　■
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, //■　　　　　　　　　　■　　　　■　　　　　　　　　　■
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } //■■■■■■■■■■■■　　　　■■■■■■■■■■■■

	};

	private Item[][] itemMap;
	private Sengoku sengoku;

	// パックマンの状態
	//現在のX座標(初期値は14マス目の中央)
	private double pacX = 14 * TILE_SIZE + TILE_SIZE / 2;
	//現在のY座標(初期値は17マス目の中央)
	private double pacY = 17 * TILE_SIZE + TILE_SIZE / 2;

	//現在の移動方向(X軸)。1:右、-1:左、0:静止
	private int dirX = 1;
	//現在の移動方向(Y軸)。1:下、-1:上、0:静止
	private int dirY = 0;

	//次の曲がり角で転換する予定の方向(X軸予約)
	private int nextDirX = 1;
	//次の曲がり角で転換する予定の方向(Y軸予約)
	private int nextDirY = 0;

	//移動速度
	private static final double speed = 2.0;

	//一時停止フラグ
	private boolean paused = false;

	//口パク
	//口の開閉角度
	private double mouthAngle = 45;
	//口の開閉アニメーションの向き。1: 開く, -1: 閉じる
	private int mouthOpening = -1;
	//壁に正面衝突して停止しているかどうか
	private boolean isBlocked = false;

	// ワープ制御
	//ワープ直後かどうか(連続ワープ防止)
	private boolean justWarped = false;
	//ワープロック
	private boolean warpLock = false;
	//最後にワープした位置
	private int lastWarpX = -1;
	private int lastWarpY = -1;

	//getters / setters
	public int[][] getMap() {
		return map;
	}

	public double getPacX() {
		return pacX;
	}

	public double getPacY() {
		return pacY;
	}

	public double getMouthAngle() {
		return mouthAngle;
	}

	public Item getItem(int x, int y) {
		if (x < 0 || x >= itemMap[0].length)
			return null;
		if (y < 0 || y >= itemMap.length)
			return null;
		return itemMap[y][x];
	}

	public int getDirX() {
		return dirX;
	}

	public int getDirY() {
		return dirY;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	//コントローラーからの曲がり角先行入力を予約
	public void setNextDirection(int nx, int ny) {
		nextDirX = nx;
		nextDirY = ny;
	}

	public void togglePause() {
		paused = !paused;
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

	}

	//更新ロジック（外部から呼ぶ)
	public void updatePacman() {
		if (paused)
			return;

		//現在いるタイル位置を計算
		int tileX = (int) (pacX / TILE_SIZE);
		int tileY = (int) (pacY / TILE_SIZE);

		// 範囲外防止
		if (tileY < 0 || tileY >= map.length || tileX < 0 || tileX >= map[0].length) {
			return;
		}

		//ワープ処理
		if (map[tileY][tileX] == 9 && !justWarped) {
			int targetX = tileX;

			//右ワープ
			if (dirX > 0) {
				for (int x = 0; x < map[0].length; x++) {
					if (map[tileY][x] == 9 && x != tileX) {
						targetX = x;
						break;
					}
				}
			}

			//左ワープ
			else if (dirX < 0) {
				for (int x = map[0].length - 1; x >= 0; x--) {
					if (map[tileY][x] == 9 && x != tileX) {
						targetX = x;
						break;
					}
				}
			}

			// ワープ先へ移動
			pacX = targetX * TILE_SIZE + TILE_SIZE / 2;
			pacY = tileY * TILE_SIZE + TILE_SIZE / 2;

			//ワープ直後に少しだけ進めてつまり防止
			pacX += dirX * speed;
			pacY += dirY * speed;

			//ワープ処理完了フラグ
			justWarped = true;
			lastWarpX = targetX;
			lastWarpY = tileY;
			warpLock = true;
			isBlocked = false; // ワープ直後はスタックを解除

			return;
		}

		// ワープゾーンから完全に抜けたらフラグをリセット
		if (justWarped) {
			int currentTileX = (int) (pacX / TILE_SIZE);
			int currentTileY = (int) (pacY / TILE_SIZE);
			// ワープ先の『9』のマスから出たらワープ可能状態に戻す
			if (currentTileX >= 0 && currentTileX < map[0].length &&
					currentTileY >= 0 && currentTileY < map.length) {
				if (map[currentTileY][currentTileX] != 9) {
					justWarped = false;
				}
			}
		}

		// 方向転換（タイル中央でのみ）
		double cx = tileX * TILE_SIZE + TILE_SIZE / 2; //現在のタイルが中心X
		double cy = tileY * TILE_SIZE + TILE_SIZE / 2; //現在のタイルが中心Y

		//1フレームの移動スピード未満の距離まで近づいたか判定
		boolean atCenter = Math.abs(pacX - cx) < speed && // 判定の閾値をspeed未満に調整
				Math.abs(pacY - cy) < speed;

		if (atCenter) {
			//先行入力された予約方向(nextDir)の1マス先を計算
			int nx = tileX + nextDirX;
			int ny = tileY + nextDirY;

			//壁チェック
			if (nx >= 0 && nx < map[0].length &&
					ny >= 0 && ny < map.length &&
					map[ny][nx] != 1) {
				dirX = nextDirX;
				dirY = nextDirY;
			}
		}

		//壁衝突チェック
		int nextTileX = tileX + dirX;
		int nextTileY = tileY + dirY;

		if (nextTileX >= 0 && nextTileX < map[0].length &&
				nextTileY >= 0 && nextTileY < map.length) {

			if (map[nextTileY][nextTileX] == 1) {
				isBlocked = true; //壁衝突フラグON
				double centerX = tileX * TILE_SIZE + TILE_SIZE / 2;
				double centerY = tileY * TILE_SIZE + TILE_SIZE / 2;

				//壁にめり込まないよう、タイルの中心に向けてマイルドに位置を戻す補正
				pacX += (centerX - pacX) * 0.3;
				pacY += (centerY - pacY) * 0.3;
				return; //壁があるのでこれ以上進まない(ここで処理を抜ける)
			}
		}

		//前方に壁がなければ移動可能
		isBlocked = false;

		// 移動処理
		pacX += dirX * speed;
		pacY += dirY * speed;

		// 軸がずれないようにする補正（ワープ直後はスキップ）
		if (!justWarped) {
			if (dirX != 0) { // 左右移動中は、上下のズレを補正
				pacY += (tileY * TILE_SIZE + TILE_SIZE / 2 - pacY) * 0.2;
			}
			if (dirY != 0) { // 上下移動中は、左右のズレを補正
				pacX += (tileX * TILE_SIZE + TILE_SIZE / 2 - pacX) * 0.2;
			}
		}
		// アイテム取得処理（点を食べる）
		int tileX2 = (int) (pacX / TILE_SIZE);
		int tileY2 = (int) (pacY / TILE_SIZE);

		Item item = getItem(tileX2, tileY2);
		if (item != null) {
			item.onEaten(sengoku); // スコア加算
			itemMap[tileY2][tileX2] = null; // 点を消す
		}
	}

	//口パクアニメーション
	public void updateMouth() {
		if (paused || isBlocked)
			return; //動いていないときは口パクしない
		//口の角度を変更
		mouthAngle += mouthOpening * 2;

		//角度制限(10度〜45度の間を往復させる)
		if (mouthAngle <= 10)
			mouthOpening = +1; //閉じきったら次は開く方向へ
		if (mouthAngle >= 45)
			mouthOpening = -1; //開ききったら次は閉じる方向へ
	}
}
