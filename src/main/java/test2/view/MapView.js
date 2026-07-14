/**
 * JavaFX の test2.view.MapView を HTML5 Canvas / JavaScript に移植したクラス。
 */
export class MapView {
    // ヘッダー（スコア表示領域）の高さ
    static INFO_HEIGHT = 40;

    /**
     * コンストラクタ
     * @param {MapData} model - 描画対象のゲームデータモデル
     * @param {HTMLElement} [rootElement] - CSSの色や操作をバインドする親要素（オプション）
     */
    constructor(model, rootElement = null) {
        this.model = model;
        this.rootElement = rootElement;

        // JPro互換の安定点滅用カウンター
        this.blinkTick = 0;

        // 画像リソースのプリロード
        this.pacmanImage = new Image();
        this.pacmanImage.src = "/src/main/resources/picture/syujinkou.png"; // パスは環境に合わせて調整してください

        this.pacmanFeverImage = new Image();
        this.pacmanFeverImage.src = "/src/main/resources/picture/syujinkou_Fever.png";

        // モバイルコントローラーの適用（root要素が存在する場合）
        if (this.rootElement) {
            this.applyMobileControls();
        }
    }

    /**
     * JavaFX 側の GameController.applyMobileControls に相当する処理のスタブ
     */
    applyMobileControls() {
        // 必要に応じてここにキーイベントやタッチイベントの初期化を実装します
        console.log("Mobile controls applied to root element.");
    }

    /**
     * ステージ全体を画面サイズに合わせて拡大縮小・中央配置して描画するメインメソッド
     * @param {CanvasRenderingContext2D} ctx - 描画先の Canvas 2D コンテキスト
     * @param {number} canvasWidth - キャンバスの現在の幅
     * @param {number} canvasHeight - キャンバスの現在の高さ
     */
    draw(ctx, canvasWidth, canvasHeight) {
        this.blinkTick++;

        // 1. キャンバスのクリアと上部情報バーの塗りつぶし
        ctx.clearRect(0, 0, canvasWidth, canvasHeight);
        ctx.fillStyle = "black";
        ctx.fillRect(0, 0, canvasWidth, MapView.INFO_HEIGHT);

        // 2. CSS から壁色とパックマン色を取得（取得失敗時はデフォルト色）
        const wallColor = this.getColorFromCSS("game-wall", "#0000ff");       // 青
        const pacmanColor = this.getColorFromCSS("game-pacman", "#ffff00"); // 黄

        // 3. ステージ本来のサイズを計算
        const map = this.model.getMap();
        const cols = map[0].length;
        const rows = map.length;

        const stageWidth = cols * MapData.TILE_SIZE;
        const stageHeight = rows * MapData.TILE_SIZE;
        const scaleX = canvasWidth / stageWidth;
        const scaleY = canvasHeight / stageHeight;

        // 4. 全体を90%の大きさに縮小する調整
        const bufferRatio = 0.9;
        const scale = Math.min(scaleX, scaleY) * bufferRatio;

        // 5. 中央に配置するための余白（オフセット）を計算
        const offsetX = (canvasWidth - (stageWidth * scale)) / 2.0;
        const offsetY = ((canvasHeight - MapView.INFO_HEIGHT) - (stageHeight * scale)) / 2.0 + MapView.INFO_HEIGHT;

        // 6. グラフィックスの状態を保存
        ctx.save();

        // 7. 変換行列を適用（中央へ移動させてから、拡大する）
        ctx.translate(offsetX, offsetY);
        ctx.scale(scale, scale);

        // ステージの枠内を真っ黒に塗りつぶす
        ctx.fillStyle = "black";
        ctx.fillRect(0, 0, stageWidth, stageHeight);

        // 8. 実際の描画処理を呼び出す
        this.drawStageContent(ctx, cols, rows, stageWidth, stageHeight, wallColor, scale, offsetX, offsetY);
        this.drawPacman(ctx);

        // 敵の描画
        const enemies = this.model.getEnemies();
        if (enemies) {
            for (const enemy of enemies) {
                this.drawEnemyInstance(ctx, enemy);
            }
        }

        // 9. グラフィックスの状態を元に戻す
        ctx.restore();

        // --- UI（スコア・ライフ）の描画 ---
        const syujinkou = this.model.getsyujinkou();
        if (syujinkou) {
            // テキストの配置基準を設定
            ctx.textAlign = "left";
            ctx.textBaseline = "top";
            ctx.font = "bold 18px 'PixelMplus12', Arial, sans-serif";
            ctx.fillStyle = "white";

            const scoreText = `SCORE : ${syujinkou.getScore()}  /  HIGH SCORE : ${HighScoreManager.loadHighScore(this.model.getStageNumber())}`;
            ctx.fillText(scoreText, 20, 12);

            // ライフの描画 (❤ をループで再現)
            ctx.fillStyle = "red";
            const hearts = "❤".repeat(Math.max(0, syujinkou.getHp()));
            ctx.textAlign = "right";
            ctx.fillText(hearts, canvasWidth - 100, 12);

            // 区切り線
            ctx.strokeStyle = "darkgray";
            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(0, MapView.INFO_HEIGHT);
            ctx.lineTo(canvasWidth, MapView.INFO_HEIGHT);
            ctx.stroke();
        }
    }

    /**
     * ステージの中身（壁の輪郭とアイテム）を描画する内部メソッド。
     */
    drawStageContent(ctx, cols, rows, stageWidth, stageHeight, wallColor, scale, offsetX, offsetY) {
        const itemMap = this.model.getItemMap();

        // 💡 JProの描画ズレバグ対策の移植：壁の描画時だけ座標行列を完全にリセット（等倍にする）
        ctx.save();
        ctx.setTransform(1, 0, 0, 1, 0, 0); // 生の画面座標に戻す

        // 事前に作成した JavaScript 版の WallOutline クラスを使用
        const outline = new WallOutline(this.model.getMap(), MapData.TILE_SIZE);
        outline.setGeometry(scale, offsetX, offsetY);
        ctx.strokeStyle = wallColor;
        ctx.lineWidth = 2 * scale; // 線の太さも拡大率に追従
        outline.drawOutline(ctx);

        ctx.restore(); // アイテムやキャラを描画するために、スケールされた元の座標系に復元

        // アイテムを描画
        for (let row = 0; row < rows; row++) {
            for (let col = 0; col < cols; col++) {
                const x = col * MapData.TILE_SIZE;
                const y = row * MapData.TILE_SIZE;
                const item = itemMap[row][col];
                if (item) {
                    item.draw(ctx, x, y, MapData.TILE_SIZE);
                }
            }
        }

        // フルーツを描画
        const fruit = this.model.getCurrentFruit();
        if (fruit) {
            const fx = this.model.getFruitCol() * MapData.TILE_SIZE;
            const fy = this.model.getFruitRow() * MapData.TILE_SIZE;
            fruit.draw(ctx, fx, fy, MapData.TILE_SIZE);
        }

        // フルーツ撃破時のスコアポップアップ（フェードイン・フロート）
        if (this.model.isFruitPopupActive()) {
            const progress = this.model.getFruitPopupProgress(); // 0.0〜1.0
            const riseOffset = progress * 20;
            const alpha = 1.0 - progress;

            const popupX = this.model.getFruitPopupX();
            const popupY = this.model.getFruitPopupY() - MapData.TILE_SIZE / 2.0 - 6 - riseOffset;

            ctx.save();
            ctx.globalAlpha = alpha;

            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            ctx.font = "bold 14px Arial, sans-serif";

            // 縁取り（黒）
            ctx.strokeStyle = "black";
            ctx.lineWidth = 3;
            ctx.strokeText(`+${this.model.getFruitPopupScore()}`, popupX, popupY);

            // 本体（白文字）
            ctx.fillStyle = "white";
            ctx.fillText(`+${this.model.getFruitPopupScore()}`, popupX, popupY);

            ctx.restore();
        }
    }

    /**
     * プレイヤー（主人公）を描画する。
     */
    drawPacman(ctx) {
        const syujinkou = this.model.getsyujinkou();
        if (!syujinkou) return;

        if (syujinkou.isDyingAnimation()) {
            this.drawDyingSyujinkou(ctx, syujinkou);
            return;
        }

        if (!syujinkou.isAlive()) return;

        // 画像が何らかの理由で読み込めなかった場合のフォールバック
        if (!this.pacmanImage.complete || this.pacmanImage.naturalWidth === 0) {
            ctx.fillStyle = "yellow";
            ctx.beginPath();
            ctx.arc(syujinkou.getX() + MapData.TILE_SIZE / 2, syujinkou.getY() + MapData.TILE_SIZE / 2, MapData.TILE_SIZE / 2, 0, Math.PI * 2);
            ctx.fill();
            return;
        }

        const pacX = syujinkou.getX() + MapData.TILE_SIZE / 2.0;
        const pacY = syujinkou.getY() + MapData.TILE_SIZE / 2.0;

        ctx.save();
        ctx.translate(pacX, pacY);

        // FEVER終了間際の点滅ロジック (8フレーム間隔)
        if (syujinkou.isFever()) {
            const remain = this.model.getFeverRemainingTime();
            if (remain <= 3000) {
                if (Math.floor(this.blinkTick / 8) % 2 === 0) {
                    ctx.restore();
                    return;
                }
            }
        }

        const currentImage = syujinkou.isFever() ? this.pacmanFeverImage : this.pacmanImage;

        ctx.drawImage(
            currentImage,
            -MapData.TILE_SIZE / 2.0,
            -MapData.TILE_SIZE / 2.0,
            MapData.TILE_SIZE,
            MapData.TILE_SIZE
        );
        ctx.restore();
    }

    /**
     * 敵1体分を描画する。
     */
    drawEnemyInstance(ctx, enemy) {
        if (!enemy) return;

        let img = null;
        let fallbackColor = "red";

        // リフレクション・型チェックの代わりに constructor.name や独自の識別フラグを利用
        const type = enemy.constructor.name; 
        if (type === "RedEnemy") {
            img = enemy.getEnemyImage();
            fallbackColor = "red";
        } else if (type === "GreenEnemy") {
            img = enemy.getEnemyImage();
            fallbackColor = "green";
        } else if (type === "YellowEnemy") {
            img = enemy.getEnemyImage();
            fallbackColor = "yellow";
        } else if (type === "BlueEnemy") {
            img = enemy.getEnemyImage();
            fallbackColor = "blue";
        }

        const enemyLeftX = enemy.getX() - MapData.TILE_SIZE / 2.0;
        const enemyTopY = enemy.getY() - MapData.TILE_SIZE / 2.0;

        if (img && img.complete && img.naturalWidth !== 0) {
            ctx.drawImage(img, enemyLeftX, enemyTopY, MapData.TILE_SIZE, MapData.TILE_SIZE);
        } else {
            // 画像がない場合の簡易フォールバック描画
            ctx.fillStyle = fallbackColor;
            ctx.beginPath();
            ctx.arc(enemy.getX(), enemy.getY(), MapData.TILE_SIZE / 2, 0, Math.PI * 2);
            ctx.fill();

            // 黒い目
            ctx.fillStyle = "black";
            ctx.beginPath();
            ctx.arc(enemy.getX() - 2, enemy.getY() - 2, 2, 0, Math.PI * 2);
            ctx.fill();
        }

        // 敵撃破時のスコアポップアップ表示
        if (enemy.isScorePopupActive()) {
            const progress = enemy.getScorePopupProgress();
            const riseOffset = progress * 20;
            const alpha = 1.0 - progress;

            const popupX = enemy.getDefeatX();
            const popupY = enemy.getDefeatY() - MapData.TILE_SIZE / 2.0 - 6 - riseOffset;

            ctx.save();
            ctx.globalAlpha = alpha;

            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            ctx.font = "bold 14px Arial, sans-serif";

            // 縁取り（黒）
            ctx.strokeStyle = "black";
            ctx.lineWidth = 3;
            ctx.strokeText(`+${enemy.getLastDefeatScore()}`, popupX, popupY);

            // 本体（白文字）
            ctx.fillStyle = "white";
            ctx.fillText(`+${enemy.getLastDefeatScore()}`, popupX, popupY);

            ctx.restore();
        }
    }

    /**
     * プレイヤーの死亡（ミス）演出を描画する。
     */
    drawDyingSyujinkou(ctx, syujinkou) {
        const progress = syujinkou.getDyingProgress();

        const centerX = syujinkou.getX() + MapData.TILE_SIZE / 2.0;
        const centerY = syujinkou.getY() + MapData.TILE_SIZE / 2.0;

        const scale = 1.0 - progress;

        ctx.save();
        ctx.translate(centerX, centerY);
        ctx.rotate((progress * 720 * Math.PI) / 180); // JavaFXの度(degree)からラジアン(radian)に変換
        ctx.scale(scale, scale);
        ctx.globalAlpha = 1.0 - progress;

        ctx.drawImage(
            this.pacmanImage,
            -MapData.TILE_SIZE / 2.0,
            -MapData.TILE_SIZE / 2.0,
            MapData.TILE_SIZE,
            MapData.TILE_SIZE
        );

        ctx.restore();
    }

    /**
     * Web標準のCSSシステムから背景色（あるいは任意の色）を抽出する。
     * JavaFXの Region ダミー部品によるCSSカラーハックを綺麗に代替します。
     * 
     * HTML側で以下のように定義されている想定です：
     * <div class="game-wall" style="background-color: #0000ff; display: none;"></div>
     * 
     * @param {string} className 取得対象のCSSクラス名
     * @param {string} defaultColor 失敗時のフォールバックカラー（HEX形式など）
     */
    getColorFromCSS(className, defaultColor) {
        if (!this.rootElement) return defaultColor;

        // rootElementから該当クラスを持つ要素を探す、なければ一時的に作る
        let dummy = this.rootElement.querySelector(`.${className}`);
        let created = false;

        if (!dummy) {
            dummy = document.createElement("div");
            dummy.className = className;
            dummy.style.display = "none";
            this.rootElement.appendChild(dummy);
            created = true;
        }

        try {
            const computedStyle = window.getComputedStyle(dummy);
            const bgColor = computedStyle.backgroundColor;

            // ブラウザは通常 "rgb(r, g, b)" の形式で返すため、そのままCanvasに適用可能
            if (bgColor && bgColor !== "rgba(0, 0, 0, 0)" && bgColor !== "transparent") {
                return bgColor;
            }
        } catch (e) {
            console.error("Failed to parse color from CSS:", e);
        } finally {
            if (created) {
                dummy.remove(); // 一時作成したものは削除
            }
        }
        return defaultColor;
    }
}