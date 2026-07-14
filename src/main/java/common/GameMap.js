// GameMap インターフェースの役割を持つベースクラス
export class GameMap {
    // ステージ番号を返す (int)
    getStageNumber() {
        throw new Error("getStageNumber() が実装されていません");
    }

    // パックマンの初期X座標を返す (double)
    getPacX() {
        throw new Error("getPacX() が実装されていません");
    }

    // パックマンの初期Y座標を返す (double)
    getPacY() {
        throw new Error("getPacY() が実装されていません");
    }

    // マップの2次元配列を返す (int[][])
    getMap() {
        throw new Error("getMap() が実装されていません");
    }

    // 開始待機状態かどうかを返す (boolean)
    isWaitingStart() {
        throw new Error("isWaitingStart() が実装されていません");
    }

    // プレイヤーの初期向きを返す (Direction型)
    getPlayerDirection() {
        throw new Error("getPlayerDirection() が実装されていません");
    }

    // 敵（ゴースト）のリストを返す (List<Enemy>)
    getEnemies() {
        throw new Error("getEnemies() が実装されていません");
    }
}