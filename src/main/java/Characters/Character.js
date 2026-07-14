// 方向のオブジェクト定義（Java の Direction enum に対応）
const Direction = {
    NONE:  { name: 'NONE',  dx: 0,  dy: 0 },
    UP:    { name: 'UP',    dx: 0,  dy: -1 },
    DOWN:  { name: 'DOWN',  dx: 0,  dy: 1 },
    LEFT:  { name: 'LEFT',  dx: -1, dy: 0 },
    RIGHT: { name: 'RIGHT', dx: 1,  dy: 0 }
};

class Syujinkou {
    // 1マスのサイズ
    static CELL_SIZE = 30;

    constructor(x, y, speed) {
        // 親クラス Character のフィールド（JavaScript ではここで初期化）
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = Direction.NONE;

        // 初期位置（リセット用）
        this.startX = x;
        this.startY = y;

        // 主人公特有のステータス
        this.hp = 3;
        this.score = 0;
        this.isAliveStatus = true; // JavaのisAliveとメソッド名の衝突を避けるためStatusを付与
        this.fever = false;
        this.nextdirection = Direction.NONE;

        // 死亡アニメーション
        this.isDyingAnimation = false;
        this.dyingTimer = 0;
    }

    // ==================================================
    // タイマー
    // ==================================================
    // アニメーションタイマー取得
    getDyingTimer() {
        return this.dyingTimer;
    }

    // ==================================================
    // アニメーション
    // ==================================================
    // ミスが起きたときに 死亡アニメーション開始
    startDying() {
        this.isDyingAnimation = true;
        this.dyingTimer = 0;
        this.direction = Direction.NONE;
        this.nextdirection = Direction.NONE;
    }

    // ==================================================
    // ポジション
    // ==================================================
    // 初期位置に戻すリセットメソッド
    resetToStartPosition() {
        this.x = this.startX;
        this.y = this.startY;
        this.direction = Direction.NONE;
        this.nextdirection = Direction.NONE;
        this.isDyingAnimation = false;
        this.dyingTimer = 0;
    }

    // ==================================================
    // 状態確認
    // ==================================================
    // 生存判定（Java版の hp > 0 チェック）
    isAlive() {
        return this.hp > 0;
    }

    // FEVER状態取得
    isFever() {
        return this.fever;
    }

    // 即死亡
    die() {
        this.hp = 0;
        this.direction = Direction.NONE;
    }

    // ==================================================
    // スコア、更新処理
    // ==================================================
    // スコア加算
    addScore(point) {
        this.score += point;
    }

    // 死亡アニメーションの更新
    updateDyingAnimation() {
        if (!this.isDyingAnimation) {
            return false;
        }
        this.dyingTimer++;
        // 約60フレーム継続
        if (this.dyingTimer < 60) {
            return false;
        }
        this.isDyingAnimation = false;
        return true;
    }

    // 残機を1つ減らすメソッド
    decreaseHp() {
        if (this.hp > 0) {
            this.hp--;
            if (this.hp <= 0) {
                this.isAliveStatus = false;
            }
        }
    }

    // ダメージ処理
    takeDamage() {
        if (this.hp > 0) {
            this.hp--;
            if (this.hp === 0) {
                this.die();
            }
        }
    }

    // ==================================================
    // 動き
    // ==================================================
    // プレイヤー移動処理
    move(map) {
        // 死亡中は移動禁止
        if (this.isDyingAnimation || !this.isAlive()) return;

        // 曲がれる場合は方向転換
        if (this.isAligned() && this.canmove(this.nextdirection, map)) {
            this.direction = this.nextdirection;
            // マスの中心へ補正 (Math.round で四捨五入して整数値に補正)
            this.x = Math.round(this.x / Syujinkou.CELL_SIZE) * Syujinkou.CELL_SIZE;
            this.y = Math.round(this.y / Syujinkou.CELL_SIZE) * Syujinkou.CELL_SIZE;
        }

        // 現在の方向が壁なら停止
        if (!this.canmove(this.direction, map)) {
            this.direction = Direction.NONE;
        }

        // 移動
        if (this.direction !== Direction.NONE) {
            this.x += this.direction.dx * this.speed;
            this.y += this.direction.dy * this.speed;
        }
    }

    // 指定方向へ進めるか判定
    canmove(direction, map) {
        if (!direction || direction === Direction.NONE) {
            return false;
        }

        // 進行方向の「先端座標」を計算する
        let checkX = this.x;
        let checkY = this.y;

        if (direction === Direction.RIGHT) {
            // 右端 + speed分先
            checkX = this.x + Syujinkou.CELL_SIZE - 1 + this.speed;
        } else if (direction === Direction.LEFT) {
            // 左端 - speed分先
            checkX = this.x - this.speed;
        } else if (direction === Direction.DOWN) {
            // 下端 + speed分先
            checkY = this.y + Syujinkou.CELL_SIZE - 1 + this.speed;
        } else if (direction === Direction.UP) {
            // 上端 - speed分先
            checkY = this.y - this.speed;
        }

        // JavaScript用の安全な座標チェック（小数を丸める）
        const checkCol = Math.floor(checkX / Syujinkou.CELL_SIZE);
        const checkRow = Math.floor(checkY / Syujinkou.CELL_SIZE);

        // マップ範囲外チェック（undefined 回避）
        if (checkRow < 0 || checkRow >= map.length || checkCol < 0 || checkCol >= map[0].length) {
            return false;
        }

        // 壁(1)以外なら通行可能
        return map[checkRow][checkCol] !== 1;
    }

    // ==================================================
    // 方向決定
    // ==================================================	
    // 次に進みたい方向を設定
    setNextDirection(direction) {
        this.nextdirection = direction;
        // 停止中は即座に方向変更
        if (this.direction === Direction.NONE && !this.isDyingAnimation) {
            this.direction = direction;
        }
    }

    // マス境界に揃っているか（曲がれるタイミング）
    isAligned() {
        return (this.x % Syujinkou.CELL_SIZE === 0) && (this.y % Syujinkou.CELL_SIZE === 0);
    }

    // ==================================================
    // getter / setter (JavaScript標準メソッドとして定義)
    // ==================================================
    // 死亡アニメーション進行率取得 (0.0 〜 1.0)
    getDyingProgress() {
        return Math.min(1.0, this.dyingTimer / 60.0);
    }

    getScore() {
        return this.score;
    }

    getHp() {
        return this.hp;
    }

    isDyingAnimationActive() {
        return this.isDyingAnimation;
    }

    // Java版のメソッド名のままゲッターとして利用できるように調整
    isDyingAnimation() {
        return this.isDyingAnimation;
    }

    getDirection() {
        return this.direction;
    }

    // ==================================================
    // setter
    // ==================================================
    setFever(fever) {
        this.fever = fever;
    }

    setPosition(x, y) {
        this.x = x;
        this.y = y;
    }

    setX(x) {
        this.x = x;
    }

    getX() {
        return this.x;
    }

    setY(y) {
        this.y = y;
    }

    getY() {
        return this.y;
    }

    // 呼び出し側の「大文字・小文字のブレ」を吸収するためのメソッド
    setnextDirection(direction) {
        this.setNextDirection(direction);
    }

    setnextdirection(direction) {
        this.setNextDirection(direction);
    }
}