package Characters;

public enum EnemyState  {
    SCATTER,    // 縄張りモード（自分の縄張りの四隅へ戻る）
    CHASE,      // 追跡モード（仙石さんを追いかける）
    FEVER,      // フィーバーモード（紫になってランダムに逃げる）
    DEAD        // 死亡・巣に戻るモード（目玉だけになって巣に戻る）
}