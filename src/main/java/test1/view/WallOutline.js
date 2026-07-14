export class WallOutline {

    #map;
    #tile;
    #scale = 1.0;
    #offsetX = 0.0;
    #offsetY = 0.0;

    constructor(map, tile) {
        this.#map = map;
        this.#tile = tile;
    }

    setGeometry(scale, offsetX, offsetY) {
        this.#scale = scale;
        this.#offsetX = offsetX;
        this.#offsetY = offsetY;
    }

    drawOutline(gc, color = '#0000FF') {
        gc.strokeStyle = color;
        gc.lineWidth = 2;

        for (let ty = 0; ty < this.#map.length; ty++) {
            for (let tx = 0; tx < this.#map[ty].length; tx++) {
                if (!this.#isWall(tx, ty)) {
                    this.#drawRoadEdges(gc, tx, ty);
                }
            }
        }
    }

    #drawRoadEdges(gc, tx, ty) {

        const map = this.#map;
        const tile = this.#tile;
        const scale = this.#scale;
        const offsetX = this.#offsetX;
        const offsetY = this.#offsetY;

        const scaledTile = tile * scale;
        const inset = scaledTile * 0.38;

        const x0 = tx * tile * scale + offsetX;
        const y0 = ty * tile * scale + offsetY;
        const x1 = x0 + scaledTile;
        const y1 = y0 + scaledTile;

        // 上辺
        if (this.#isWall(tx, ty - 1)) {
            if (!(!this.#isWall(tx - 1, ty) && this.#isWall(tx - 1, ty - 1))) {
                let ex = tx;
                while (ex + 1 < map[ty].length && !this.#isWall(ex + 1, ty) && this.#isWall(ex + 1, ty - 1)) {
                    ex++;
                }
                const startX = this.#isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
                const endX = this.#isWall(ex + 1, ty)
                    ? (ex + 1) * tile * scale + offsetX + inset
                    : (ex + 1) * tile * scale + offsetX - inset;
                gc.beginPath();
                gc.moveTo(startX, y0 - inset);
                gc.lineTo(endX, y0 - inset);
                gc.stroke();
            }
        }

        // 下辺
        if (this.#isWall(tx, ty + 1)) {
            if (!(!this.#isWall(tx - 1, ty) && this.#isWall(tx - 1, ty + 1))) {
                let ex = tx;
                while (ex + 1 < map[ty].length && !this.#isWall(ex + 1, ty) && this.#isWall(ex + 1, ty + 1)) {
                    ex++;
                }
                const startX = this.#isWall(tx - 1, ty) ? x0 - inset : x0 + inset;
                const endX = this.#isWall(ex + 1, ty)
                    ? (ex + 1) * tile * scale + offsetX + inset
                    : (ex + 1) * tile * scale + offsetX - inset;
                gc.beginPath();
                gc.moveTo(startX, y1 + inset);
                gc.lineTo(endX, y1 + inset);
                gc.stroke();
            }
        }

        // 左辺
        if (this.#isWall(tx - 1, ty)) {
            if (!(!this.#isWall(tx, ty - 1) && this.#isWall(tx - 1, ty - 1))) {
                let ey = ty;
                while (ey + 1 < map.length && !this.#isWall(tx, ey + 1) && this.#isWall(tx - 1, ey + 1)) {
                    ey++;
                }
                const startY = this.#isWall(tx, ty - 1) ? y0 - inset : y0 + inset;
                const endY = this.#isWall(tx, ey + 1)
                    ? (ey + 1) * tile * scale + offsetY + inset
                    : (ey + 1) * tile * scale + offsetY - inset;
                gc.beginPath();
                gc.moveTo(x0 - inset, startY);
                gc.lineTo(x0 - inset, endY);
                gc.stroke();
            }
        }

        // 右辺
        if (this.#isWall(tx + 1, ty)) {
            if (!(!this.#isWall(tx, ty - 1) && this.#isWall(tx + 1, ty - 1))) {
                let ey = ty;
                while (ey + 1 < map.length && !this.#isWall(tx, ey + 1) && this.#isWall(tx + 1, ey + 1)) {
                    ey++;
                }
                const startY = this.#isWall(tx, ty - 1) ? y0 - inset : y0 + inset;
                const endY = this.#isWall(tx, ey + 1)
                    ? (ey + 1) * tile * scale + offsetY + inset
                    : (ey + 1) * tile * scale + offsetY - inset;
                gc.beginPath();
                gc.moveTo(x1 + inset, startY);
                gc.lineTo(x1 + inset, endY);
                gc.stroke();
            }
        }
    }

    #isWall(x, y) {
        const map = this.#map;
        if (x < 0 || y < 0 || y >= map.length || x >= map[0].length)
            return false;
        return map[y][x] == 1;
    }
}