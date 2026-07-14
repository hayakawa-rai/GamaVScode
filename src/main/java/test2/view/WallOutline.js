/**
 * マップ上の「壁ではないマス（道）」の外周に沿って輪郭線を描画するクラス。
 * 
 * JavaFX 版の複雑な拡大縮小（scale）と中央配置オフセット（offsetX / offsetY）の
 * 計算ロジックを正確に移植しています。
 */
export class WallOutline {
    /**
     * コンストラクタ
     * @param {number[][]} map - 壁判定に使うマップの二次元配列 (1=壁、それ以外=道)
     * @param {number} tile - 1マスのピクセルサイズ
     */
    constructor(map, tile) {
        this.map = map;
        this.tile = tile;

        // 画面のスケーリングと中央配置用オフセット
        this.scale = 1.0;
        this.offsetX = 0.0;
        this.offsetY = 0.0;
    }

    /**
     * 描画ループから現在のスケーリング情報を同期するためのメソッド。
     * @param {number} scale - 現在の画面倍率
     * @param {number} offsetX - 画面の左側余白
     * @param {number} offsetY - 画面の上側余白
     */
    setGeometry(scale, offsetX, offsetY) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * マップ全体を走査し、壁ではないマスの周囲に輪郭線を描画する。
     * @param {CanvasRenderingContext2D} ctx - 描画先の Canvas 2D コンテキスト
     */
    drawOutline(ctx) {
        for (let ty = 0; ty < this.map.length; ty++) {
            for (let tx = 0; tx < this.map[ty].length; tx++) {
                if (!this.isWall(tx, ty)) {
                    this.drawRoadEdges(ctx, tx, ty);
                }
            }
        }
    }

    /**
     * 指定した道タイル(tx, ty)について、壁と隣接している辺に沿って線を描画する。
     * @param {CanvasRenderingContext2D} ctx - 描画先の Canvas 2D コンテキスト
     * @param {number} tx - 対象タイルの列インデックス
     * @param {number} ty - 対象タイルの行インデックス
     */
    drawRoadEdges(ctx, tx, ty) {
        // tileサイズとずらし量(inset)にスケールを適用
        const scaledTile = this.tile * this.scale;
        const inset = scaledTile * 0.38;

        // タイルの四隅の座標を、スケールと画面オフセットを考慮して直接計算
        const x0 = (tx * this.tile) * this.scale + this.offsetX;
        const y0 = (ty * this.tile) * this.scale + this.offsetY;
        const x1 = x0 + scaledTile;
        const y1 = y0 + scaledTile;

        // --- 上辺 ---
        if (this.isWall(tx, ty - 1)) {
            if (!(!this.isWall(tx - 1, ty) && this.isWall(tx - 1, ty - 1))) {
                let ex = tx;
                while (ex + 1 < this.map[ty].length && !this.isWall(ex + 1, ty) && this.isWall(ex + 1, ty - 1)) {
                    ex++;
                }
                const startX = this.isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
                const endX = this.isWall(ex + 1, ty) 
                    ? ((ex + 1) * this.tile) * this.scale + this.offsetX + inset
                    : ((ex + 1) * this.tile) * this.scale + this.offsetX - inset;
                
                ctx.beginPath();
                ctx.moveTo(startX, y0 - inset);
                ctx.lineTo(endX, y0 - inset);
                ctx.stroke();
            }
        }

        // --- 下辺 ---
        if (this.isWall(tx, ty + 1)) {
            if (!(!this.isWall(tx - 1, ty) && this.isWall(tx - 1, ty + 1))) {
                let ex = tx;
                while (ex + 1 < this.map[ty].length && !this.isWall(ex + 1, ty) && this.isWall(ex + 1, ty + 1)) {
                    ex++;
                }
                const startX = this.isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
                const endX = this.isWall(ex + 1, ty) 
                    ? ((ex + 1) * this.tile) * this.scale + this.offsetX + inset
                    : ((ex + 1) * this.tile) * this.scale + this.offsetX - inset;
                
                ctx.beginPath();
                ctx.moveTo(startX, y1 + inset);
                ctx.lineTo(endX, y1 + inset);
                ctx.stroke();
            }
        }

        // --- 左辺 ---
        if (this.isWall(tx - 1, ty)) {
            if (!(!this.isWall(tx, ty - 1) && this.isWall(tx - 1, ty - 1))) {
                let ey = ty;
                while (ey + 1 < this.map.length && !this.isWall(tx, ey + 1) && this.isWall(tx - 1, ey + 1)) {
                    ey++;
                }
                const startY = this.isWall(tx, ty - 1) ? y0 - inset : y0 + inset;
                const endY = this.isWall(tx, ey + 1) 
                    ? ((ey + 1) * this.tile) * this.scale + this.offsetY + inset
                    : ((ey + 1) * this.tile) * this.scale + this.offsetY - inset;
                
                ctx.beginPath();
                ctx.moveTo(x0 - inset, startY);
                ctx.lineTo(x0 - inset, endY);
                ctx.stroke();
            }
        }

        // --- 右辺 ---
        if (this.isWall(tx + 1, ty)) {
            if (!(!this.isWall(tx, ty - 1) && this.isWall(tx + 1, ty - 1))) {
                let ey = ty;
                while (ey + 1 < this.map.length && !this.isWall(tx, ey + 1) && this.isWall(tx + 1, ey + 1)) {
                    ey++;
                }
                const startY = this.isWall(tx, ty - 1) ? y0 - inset : y0 + inset;
                const endY = this.isWall(tx, ey + 1) 
                    ? ((ey + 1) * this.tile) * this.scale + this.offsetY + inset
                    : ((ey + 1) * this.tile) * this.scale + this.offsetY - inset;
                
                ctx.beginPath();
                ctx.moveTo(x1 + inset, startY);
                ctx.lineTo(x1 + inset, endY);
                ctx.stroke();
            }
        }
    }

    /**
     * 指定した座標が壁（1）かどうかを判定する。
     * @param {number} x - タイルの列インデックス
     * @param {number} y - タイルの行インデックス
     * @returns {boolean} 壁であれば true、それ以外は false
     */
    isWall(x, y) {
        if (x < 0 || y < 0 || y >= this.map.length || x >= this.map[0].length) {
            return false;
        }
        return this.map[y][x] === 1; // 1のみ壁扱い (7や8は道側として扱い、輪郭線を引く)
    }
}