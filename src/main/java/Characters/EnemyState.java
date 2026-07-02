package Characters;

public enum EnemyState  {
	// 縄張りモード（自分の縄張りの四隅へ戻る）
    SCATTER,
    // 追跡モード（仙石さんを追いかける）
    CHASE,
    // フィーバーモード（紫になってランダムに逃げる）
    FEVER,
    // 死亡・巣に戻るモード（目玉だけになって巣に戻る）
    DEAD
}