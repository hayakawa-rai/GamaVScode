// characters/Character.js
export class Character { ... }

export abstract class Character {
	// 現在のX座標（左右位置）
	protected x: number;
	// 現在のY座標（上下位置）
	protected y: number;
	// 移動速度
	protected speed: number;
	// キャラクターが現在向いている方向
	protected direction: Direction;
	
	// ==================================================
	// コンストラクタ
	// ==================================================
	// 速さデフォルトを 1 にして、主人公 の呼び出しと互換性を持たせる
	constructor(x: number, y: number,speed: number = 1) {
		// 初期X座標
		this.x = x;
		// 初期Y座標
		this.y = y;
		// 初期移動速度
		this.speed = speed;
		// 初期状態では停止
		this.direction = Direction.NONE;
	}
	
	// ==================================================
	// 動き
	// ==================================================
	// キャラクターを移動させる処理
	abstract move(map:number[][]):void;

	//==================================================
	//getter
	//==================================================
	// 子クラスからx座標の情報を取得
	getX():number {
		return this.x;
	}
	
	// 子クラスからy座標の情報を取得
	getY():number {
		return this.y;
	}

	// 子クラスから速度の情報を取得
	getSpeed():number {
		return this.speed;
	}

	// 子クラスから向いている方向の情報を取得
	getDirection():Direction {
		return this.direction;
	}

}