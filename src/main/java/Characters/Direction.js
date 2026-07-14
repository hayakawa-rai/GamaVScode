function createDirection(name,dx,dy){
	return Object.freeze({
		name,
		dx,
		dy,
		getDX(){return dx;},
		getDY(){return dy;},
	});
}

export const Direction = Object.freeze({
  // 上下左右に1ピクセル移動
  UP: createDirection("UP", 0, -1),
  DOWN: createDirection("DOWN", 0, 1),
  LEFT: createDirection("LEFT", -1, 0),
  RIGHT: createDirection("RIGHT", 1, 0),
  // 押されていない停止状態。始まるまでキャラクターを停止させるため
  NONE: createDirection("NONE", 0, 0),
});

export const DirectionValues = Object.freeze(Object.values(Direction));
	
	// ==================================================
	// 方向決定
	// ==================================================
	// JavaFXのキー入力(KeyCode)から対応するDirection(動き)を返すメソッド
	export function fromKeyCode(key) {

	switch(key){
    // 矢印上
    case "ArrowUp":
      return Direction.UP;
    // 矢印下
    case "ArrowDown":
      return Direction.DOWN;
    // 矢印左
    case "ArrowLeft":
      return Direction.LEFT;
    // 矢印右
    case "ArrowRight":
      return Direction.RIGHT;
    // それ以外のキー
    default:
      return Direction.NONE;
  }
}
