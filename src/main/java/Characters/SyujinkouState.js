//　主人公の状態を管理する

package Characters;

public enum SyujinkouState {

	// 通常状態(速度倍率1.0)
	NORMAL(1.0),

	// フィーバー状態(速度倍率1.2にすることでEnemyより早くなり食べられる。)
	FEVER(1.2);

	// 「速度倍率」を保存しておくためのspeedMultiplier
	private final double speedMultiplier;

	// ==================================================
	// コンストラクタ
	// ==================================================
	// 速度倍率を設定する// ==================================================
// 主人公の状態を管理する
// ==================================================

class SyujinkouState {

    // ==================================================
    // コンストラクタ
    // ==================================================
    // 速度倍率を設定する
    constructor(speedMultiplier) {

        // 速度倍率
        this.speedMultiplier = speedMultiplier;
    }

    // ==================================================
    // getter
    // ==================================================
    // 速度倍率を取得する
    getSpeedMultiplier() {
        return this.speedMultiplier;
    }
}

// ==================================================
// 状態定義
// ==================================================

// 通常状態（速度倍率1.0）
SyujinkouState.NORMAL =
    new SyujinkouState(1.0);

// フィーバー状態
// （速度倍率1.2にすることでEnemyより早くなり食べられる）
SyujinkouState.FEVER =
    new SyujinkouState(1.2);

// Javaの values() 相当
SyujinkouState.VALUES = [
    SyujinkouState.NORMAL,
    SyujinkouState.FEVER
];

export default SyujinkouState;
	SyujinkouState(double speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}

	// ==================================================
	// getter
	// ==================================================
	// 速度倍率を取得する
	public double getSpeedMultiplier() {
		return speedMultiplier;
	}
}
