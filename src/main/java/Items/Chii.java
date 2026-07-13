// ==================================================
// 主人公（仙石さん）クラス
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
        this.isAliveFlag = true;

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

    // 初期位置へ戻す
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

    // 死亡アニメーション更新
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

    // 残機を1つ減らす
    decreaseHp() {

        if (this.hp > 0) {

            this.hp--;

            if (this.hp <= 0) {
                this.isAliveFlag = false;
            }
        }
    }

    // ダメージ処理
    takeDamage() {

        if (this.hp > 0) {

            this.hp--;

            if (this.hp === 0) {
                this.die();
        