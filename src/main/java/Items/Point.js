// ==================================================
// Point（通常ドット）
// ==================================================

class Point extends Item {

    // ==================================================
    // コンストラクタ
    // ==================================================
    constructor(pixelX, pixelY) {

        // 親クラス（Item）へ
        // 10点スコアと描画情報を渡す
        super(10, {
            x: pixelX,
            y: pixelY,
            radius: 3,
            color: "yellow"
        });
    }

    // ==================================================
    // 食べる処理
    // ==================================================
    onEaten(player) {

        // プレイヤーへスコア加算
        player.addScore(this.score);
    }

    // ==================================================
    // 描画処理
    // ==================================================
    draw(ctx, x, y, tileSize) {

        // 自分が持っている描画情報から
        // 半径と色を取得
        const radius = this.view.radius;

        // 描画色設定
        ctx.fillStyle = this.view.color;

        // マス中央を基準に円を描画
        ctx.beginPath();

        ctx.arc(
            x + tileSize / 2,
            y + tileSize / 2,
            radius,
            0,
            Math.PI * 2
        );

        ctx.fill();
    }
}

export default Point;