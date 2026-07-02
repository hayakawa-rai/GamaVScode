package Characters;

public enum SyujinkouState {
	
	//通常状態(速度倍率1.0)
	NORMAL(1.0),
	
	//フィーバー状態(速度倍率1.2にすることでEnemyより早くなり食べられる。)
	FEVER(1.2);
	
	//「速度倍率」を保存しておくためのspeedMultiplier
	private final double speedMultiplier;
	
	//コンストラクタ
	SyujinkouState(double speedMultiplier){
		this.speedMultiplier = speedMultiplier;
	}
	
	//ゲッター
	public double getSpeedMultiplier() {
		
		return speedMultiplier;
	}

}
