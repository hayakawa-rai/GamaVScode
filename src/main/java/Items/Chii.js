// パワーエサ（Chii）クラス。Item クラスを継承

import { Item } from "./Item.js";

export class Chii extends Item {
    // 画面上の画像の大きさ（Javaの static final double に対応）
    static IMAGE_SIZE = 37.0;

    // ==================================================
    // コンストラクタ
    // ==================================================
    constructor(pixelX, pixelY) {
        // 親クラス（Item）のコンストラクタを呼び出す（スコア値 50 を設定）
        super(50);

        // マスの中心に画像がくるように位置を調整して保持
        this.x = pixelX - (Chii.IMAGE_SIZE / 2.0);
        this.y = pixelY - (Chii.IMAGE_SIZE / 2.0);

        // 画像の読み込み状態管理
        this.imageLoaded = false;
        this.img = new Image();

        // 保険用の円データ（画像が読み込めなかった場合用）
        this.fallbackCircle = {
            x: pixelX,
            y: pixelY,
            radius: 8,
            color: 'chartreuse' // 黄緑色
        };

        // 画像ファイルの読み込み開始
        this.img.src = "/src/main/resources/picture/Chii_Item.png";

        this.img.onload = () => {
            this.imageLoaded = true;
        };

        this.img.onerror = () => {
            console.error("画像の読み込みに失敗しました。パスが正しいか確認してください。");
            this.imageLoaded = false;
        };
    }

    // ==================================================
    // 食べる処理
    // ==================================================
    onEaten(player) {
        // 主人公クラスの addScore メソッドを呼び出す
        player.addScore(this.score);
    }

    // ==================================================
    // 描画処理
    // ==================================================
    // Javaの GraphicsContext -> HTML5 Canvas の ctx に対応
    draw(ctx, x, y, tileSize) {
        // 1. 画像が正常に読み込めている場合
        if (this.imageLoaded && this.img) {
            ctx.drawImage(
                this.img,
                x + tileSize / 2.0 - (Chii.IMAGE_SIZE / 2.0),
                y + tileSize / 2.0 - (Chii.IMAGE_SIZE / 2.0),
                Chii.IMAGE_SIZE, 
                Chii.IMAGE_SIZE
            );
            // 描画が終わったので終了
            return;
        }

        // 2. 画像がない場合は、保険として生成した黄緑の円を描画する
        const circle = this.fallbackCircle;
        ctx.fillStyle = circle.color;
        ctx.beginPath();
        ctx.arc(
            x + tileSize / 2.0, 
            y + tileSize / 2.0, 
            circle.radius, 
            0, 
            Math.PI * 2
        );
        ctx.fill();
    }

    // ==================================================
    // getter
    // ==================================================
    // 外部から画像単体を取り出したい時用
    getImage() {
        return this.imageLoaded ? this.img : null;
    }

    // 外部からサイズを知りたい時用
    getSize() {
        return Chii.IMAGE_SIZE;
    }
}