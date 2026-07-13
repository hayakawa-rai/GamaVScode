/**
 * アイテムの基底クラス
 * ドット・パワーエサ・フルーツなどの共通部分を管理する
 */
class Item {

    // =========================
    // コンストラクタ
    // =========================
    constructor(score, view) {

        // アイテム取得時のスコア
        this.score = score;

        // 描画用オブジェクト（画像など）
        this.view = view;
    }

    // =========================
    // 食べる処理
    // =========================
    /**
     * アイテム取得時の処理
     * 子クラス側で実装する
     *
     * @param {Syujinkou} player
     */
    onEaten(player) {
        throw new Error(
            "onEaten() must be implemented."
        );
    }

    // =========================
    // 描画処理
    // =========================
    /**
     * Canvasへ描画する処理
     * 子クラス側で実装する
     *
     * @param {CanvasRenderingContext2D} ctx
     * @param {number} x
     * @param {number} y
     * @param {number} tileSize
     */
    draw(ctx, x, y, tileSize) {
        throw new Error(
            "draw() must be implemented."
        );
    }

    // =========================
    // getter
    // =========================
    getView() {
        return this.view;
    }
}

export default Item;