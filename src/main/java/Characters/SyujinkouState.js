// ==================================================
// 主人公の状態を管理する enum 相当のクラス
// ==================================================
export class SyujinkouState {
  // ==================================================
  // コンストラクタ
  // ==================================================
  constructor(speedMultiplier) {
    // 速度倍率
    this.speedMultiplier = speedMultiplier;

    // インスタンスが後から書き換えられないようにフリーズ（安全対策）
    Object.freeze(this);
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
// 状態定義（Javaの enum 定数に相当）
// ==================================================

// 通常状態（速度倍率1.0）
SyujinkouState.NORMAL = new SyujinkouState(1.0);

// フィーバー状態（速度倍率1.2にすることでEnemyより早くなり食べられる）
SyujinkouState.FEVER = new SyujinkouState(1.2);

// Javaの values() やvalueOf() に相当するヘルパー群
SyujinkouState.VALUES = Object.freeze([
  SyujinkouState.NORMAL,
  SyujinkouState.FEVER,
]);

// 状態全体も後から追加や削除ができないようにフリーズしておく
Object.freeze(SyujinkouState);
