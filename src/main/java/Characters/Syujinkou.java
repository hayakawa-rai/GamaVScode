// ==================================================
// 主人公（仙石さん）クラス
//
// プレイヤーの移動、残機管理、スコア管理、
// FEVER状態、死亡アニメーションの管理を行う
// ==================================================

class Syujinkou extends Character {

    // 1マスのサイズ
    static CELL_SIZE = 30;

    // ==================================================
    // コンストラクタ
    // ==================================================
    constructor(x, y, speed) {

        super(x, y, speed);

        // 残機
        this.hp = 3;

        // スコア
        this.score = 0;

        // 生存状態
        this.alive = true;

        // FEVER状態
        this.fever = false;

        // 次に進みたい方向（先行入力）
        this.nextdirection = Direction.NONE;

        // 初期位置
        this.startX = x;
        this.startY = y;

        // 死亡アニメーション中か
        this.isDyingAnimationFlag = false;

        // アニメーション経過時間
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

    // ミスが起きたときに死亡アニメーション開始
    startDying() {

        this.isDyingAnimationFlag = true;
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

        this.isDyingAnimationFlag = false;
        this.dyingTimer = 0;
    }

    // ==================================================
    // 状態確認
    // ==================================================

    // 生存判定
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

        if (!this.isDyingAnimationFlag) {
            return false;
        }

        this.dyingTimer++;

        // 約60フレーム継続
        if (this.dyingTimer < 60) {
            return false;
        }

        this.isDyingAnimationFlag = false;
        return true;
    }

    // 残機を1つ減らすメソッド
    decreaseHp() {

        if (this.hp > 0) {

            this.hp--;

            if (this.hp <= 0) {
                this.alive = false;
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
        if (
            this.isDyingAnimationFlag ||
            !this.isAlive()
        ) {
            return;
        }

        // 曲がれる場合は方向転換
        if (
            this.isAligned() &&
            this.canMove(this.nextdirection, map)
        ) {

            this.direction = this.nextdirection;

            // マスの中心へ補正
            this.x =
                Math.round(
                    this.x / Syujinkou.CELL_SIZE
                ) * Syujinkou.CELL_SIZE;

            this.y =
                Math.round(
                    this.y / Syujinkou.CELL_SIZE
                ) * Syujinkou.CELL_SIZE;
        }

        // 現在の方向が壁なら停止
        if (
            !this.canMove(
                this.direction,
                map
            )
        ) {
            this.direction = Direction.NONE;
        }

        // 移動
        if (
            this.direction !== Direction.NONE
        ) {

            this.x +=
                this.direction.getDX() *
                this.speed;

            this.y +=
                this.direction.getDY() *
                this.speed;
        }
    }

    // 指定方向へ進めるか判定
    canMove(direction, map) {

        if (direction === Direction.NONE) {
            return false;
        }

        // 進行方向の先端座標を計算
        let checkX = this.x;
        let checkY = this.y;

        if (direction === Direction.RIGHT) {

            // 右端 + speed分先
            checkX =
                this.x +
                Syujinkou.CELL_SIZE -
                1 +
                this.speed;

        } else if (
            direction === Direction.LEFT
        ) {

            // 左端 - speed分先
            checkX =
                this.x -
                this.speed;

        } else if (
            direction === Direction.DOWN
        ) {

            // 下端 + speed分先
            checkY =
                this.y +
                Syujinkou.CELL_SIZE -
                1 +
                this.speed;

        } else if (
            direction === Direction.UP
        ) {

            // 上端 - speed分先
            checkY =
                this.y -
                this.speed;
        }

        const checkCol = Math.floor(
            checkX / Syujinkou.CELL_SIZE
