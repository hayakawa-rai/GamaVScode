// MapView.js

export class MapView {
  /**
   * @param {Object} model - ゲームのデータモデル (MapDataに相当)
   * @param {Object} images - 読み込み済みのImageオブジェクトをまとめた連想配列
   */
  constructor(model, images = {}) {
    this.model = model;
    this.images = images;

    // ヘッダー（情報バー）の高さ
    this.INFO_HEIGHT = 40;

    // デフォルトの色設定 (JavaFXのCSSダミーの代わり)
    this.wallColor = "#0000FF"; // 青

    // 主人公用画像の読み込み(Java版のpacmanImage/pacmanFeverImage相当)
    this.pacmanImage = new Image();
    this.pacmanImage.src = "../../resources/picture/syujinkou.png";

    this.pacmanFeverImage = new Image();
    this.pacmanFeverImage.src = "../../resources/picture/syujinkou_fever.png";

    // ステージ番号を取得して色を決定
    const stageNumber = model.getStageNumber?.() || 1; // モデル側に取得メソッドがある想定
    const colors = MapView.STAGE_COLORS[stageNumber] || MapView.STAGE_COLORS[1];

    this.bgColor = colors.bg;
    this.wallColor = colors.wall;
    this.pacmanColor = colors.pacman;
  }

  static STAGE_COLORS = {
    1: { bg: "#000000", wall: "rgb(0, 238, 190)", pacman: "#ffffff" },
    2: { bg: "rgb(43, 43, 43)", wall: "#ff44cc", pacman: "rgb(28, 221, 216)" },
    3: { bg: "#1a0000", wall: "rgb(238, 30, 0)", pacman: "#ffaa00" },
  };

  /**
   * メイン描画メソッド
   * @param {CanvasRenderingContext2D} ctx - HTML5 Canvasの2Dコンテキスト
   * @param {number} canvasWidth - キャンバスの横幅
   * @param {number} canvasHeight - キャンバスの縦幅
   */
  draw(ctx, canvasWidth, canvasHeight) {
    const syujinkou = this.model.getSyujinkou?.();
    const TILE_SIZE = this.model.constructor.TILE_SIZE || 20; // マップデータのタイルサイズ

    // 1. キャンバスのクリアとヘッダー背景の塗りつぶし
    ctx.clearRect(0, 0, canvasWidth, canvasHeight);
    ctx.fillStyle = this.bgColor;
    ctx.fillRect(0, 0, canvasWidth, this.INFO_HEIGHT);

    // ステージの行列サイズを取得
    const map = this.model.getMap();
    const rows = map.length;
    const cols = map[0].length;

    const stageWidth = cols * TILE_SIZE;
    const stageHeight = rows * TILE_SIZE;

    // 画面に収まるスケールを計算（全体の90%に縮小）
    const scaleX = canvasWidth / stageWidth;
    const scaleY = (canvasHeight - this.INFO_HEIGHT) / stageHeight;
    const bufferRatio = 0.9;
    const scale = Math.min(scaleX, scaleY) * bufferRatio;

    // 中央配置のためのオフセット計算
    const offsetX = (canvasWidth - stageWidth * scale) / 2.0;
    const offsetY =
      (canvasHeight - this.INFO_HEIGHT - stageHeight * scale) / 2.0 +
      this.INFO_HEIGHT;

    // グラフィックス状態の保存
    ctx.save();

    // 変換行列の適用（中央へ移動して拡大）
    ctx.translate(offsetX, offsetY);
    ctx.scale(scale, scale);

    // ステージ内を真っ黒に塗りつぶし
    ctx.fillStyle = this.bgColor;
    ctx.fillRect(0, 0, stageWidth, stageHeight);

    const outline = this.model.getWallOutline?.();
    if (outline) {
      outline.setGeometry(scale, offsetX, offsetY);
    }

    // 各種コンテンツの描画
    this.drawStageContent(ctx, cols, rows, TILE_SIZE);
    this.drawPacman(ctx, syujinkou, TILE_SIZE);

    // 敵の描画
    const enemies = this.model.getEnemies?.();
    if (enemies) {
      for (const enemy of enemies) {
        this.drawEnemyInstance(ctx, enemy, TILE_SIZE);
      }
    }

    // 状態を元に戻す
    ctx.restore();

    // UI（スコア・ライフ）の描画
    if (syujinkou) {
      ctx.textAlign = "left";
      ctx.textBaseline = "top";
      ctx.font = 'bold 18px "PixelMplus12", Arial';
      ctx.fillStyle = "#FFFFFF";

      // スコア表示
      const scoreText = `SCORE : ${syujinkou.getScore()}  /  HIGH SCORE : ${this.model.getHighScore?.() || 0}`;
      ctx.fillText(scoreText, 20, 12);

      // ライフ（ハート）表示
      ctx.fillStyle = "#FF0000";
      const hearts = "❤".repeat(syujinkou.getHp());
      ctx.textAlign = "right";
      ctx.fillText(hearts, canvasWidth - 20, 12);

      // 区切り線
      ctx.strokeStyle = "#A9A9A9"; // DARKGRAY
      ctx.lineWidth = 1;
      ctx.beginPath();
      ctx.moveTo(0, this.INFO_HEIGHT);
      ctx.lineTo(canvasWidth, this.INFO_HEIGHT);
      ctx.stroke();
    }
  }

  /**
   * ステージの枠（壁）やアイテムを描画
   */
  drawStageContent(ctx, cols, rows, TILE_SIZE) {
    const itemMap = this.model.getItemMap();

    // --- 壁の輪郭 (WallOutline) の描画 ---
    // 元のJavaFXコードでは一時的に行列をリセットしていましたが、JSでは独立した座標計算ロジック（WallOutline側）が
    // 縮小率やオフセットを受け取って直にキャンバスへ描画する設計にするとすっきりします。
    if (this.model.getWallOutline) {
      const outline = this.model.getWallOutline(); // すでに幾何情報が同期されている想定
      ctx.save();
      ctx.resetTransform(); // 完全な画面生の座標系にする（JavaFXのsetTransform(1,0,0,1,0,0)相当）
      outline.drawOutline(ctx, this.wallColor);
      ctx.restore();
    }

    // アイテムの描画
    for (let row = 0; row < rows; row++) {
      for (let col = 0; col < cols; col++) {
        const item = itemMap[row][col];
        if (item) {
          const x = col * TILE_SIZE;
          const y = row * TILE_SIZE;
          item.draw?.(ctx, x, y, TILE_SIZE);
        }
      }
    }

    // フルーツの描画
    const fruit = this.model.getCurrentFruit?.();
    if (fruit) {
      const fx = this.model.getFruitCol() * TILE_SIZE;
      const fy = this.model.getFruitRow() * TILE_SIZE;
      fruit.draw?.(ctx, fx, fy, TILE_SIZE);
    }

    // フルーツ撃破時のスコアポップアップ
    if (this.model.isFruitPopupActive?.()) {
      const progress = this.model.getFruitPopupProgress();
      const riseOffset = progress * 20;
      const alpha = 1.0 - progress;

      const popupX = this.model.getFruitPopupX();
      const popupY =
        this.model.getFruitPopupY() - TILE_SIZE / 2.0 - 6 - riseOffset;

      ctx.save();
      ctx.globalAlpha = alpha;
      ctx.textAlign = "center";
      ctx.textBaseline = "middle";
      ctx.font = "bold 14px Arial";

      // 袋文字（縁取り）
      ctx.strokeStyle = "#000000";
      ctx.lineWidth = 3;
      ctx.strokeText(`+${this.model.getFruitPopupScore()}`, popupX, popupY);

      // 中身
      ctx.fillStyle = "#FFFFFF";
      ctx.fillText(`+${this.model.getFruitPopupScore()}`, popupX, popupY);
      ctx.restore();
    }
  }

  /**
   * プレイヤー（パックマン）の描画
   */
  drawPacman(ctx, syujinkou, TILE_SIZE) {
    if (!syujinkou) return;

    if (syujinkou.isDyingAnimation?.()) {
      this.drawDyingSyujinkou(ctx, syujinkou, TILE_SIZE);
      return;
    }

    if (!syujinkou.isAlive?.()) return;

    const pacX = syujinkou.getX() + TILE_SIZE / 2.0;
    const pacY = syujinkou.getY() + TILE_SIZE / 2.0;

    if (syujinkou.isFever?.()) {
      const remain = this.model.getFeverRemainingTime();
      if (remain <= 3000) {
        if (Math.floor(Date.now() / 150) % 2 === 0) {
          return;
        }
      }
    }

    ctx.save();
    ctx.translate(pacX, pacY);

    const img = syujinkou.isFever?.()
      ? this.pacmanFeverImage
      : this.pacmanImage;

    if (img && img.complete && img.naturalWidth > 0) {
      ctx.drawImage(
        img,
        -TILE_SIZE / 2.0,
        -TILE_SIZE / 2.0,
        TILE_SIZE,
        TILE_SIZE,
      );
    } else {
      ctx.fillStyle = "#FFFF00";
      ctx.beginPath();
      ctx.arc(0, 0, TILE_SIZE / 2.0, 0, Math.PI * 2);
      ctx.fill();
    }
    ctx.restore();
  }

  /**
   * 敵1体の描画
   */
  drawEnemyInstance(ctx, enemy, TILE_SIZE) {
    if (!enemy) return;

    // 各Enemyサブクラスが持つ getEnemyImage() から画像を取得
    const img =
      typeof enemy.getEnemyImage === "function" ? enemy.getEnemyImage() : null;

    const enemyLeftX = enemy.getX() - TILE_SIZE / 2.0;
    const enemyTopY = enemy.getY() - TILE_SIZE / 2.0;

    if (img) {
      ctx.drawImage(img, enemyLeftX, enemyTopY, TILE_SIZE, TILE_SIZE);
    } else {
      // フォールバック(画像未読み込み時)。enemy.type で色分け
      const colorMap = {
        red: "#FF0000",
        green: "#008000",
        yellow: "#FFFF00",
        blue: "#0000FF",
      };
      ctx.fillStyle = colorMap[enemy.type] || "#FFFFFF";

      ctx.beginPath();
      ctx.arc(enemy.getX(), enemy.getY(), TILE_SIZE / 2.0, 0, Math.PI * 2);
      ctx.fill();

      ctx.fillStyle = "#000000";
      ctx.beginPath();
      ctx.arc(enemy.getX() - 2, enemy.getY() - 2, 2, 0, Math.PI * 2);
      ctx.fill();
    }

    // 撃破時のスコアポップアップ表示(変更なし)
    if (enemy.isScorePopupActive?.()) {
      const progress = enemy.getScorePopupProgress();
      const riseOffset = progress * 20;
      const alpha = 1.0 - progress;

      const popupX = enemy.getDefeatX();
      const popupY = enemy.getDefeatY() - TILE_SIZE / 2.0 - 6 - riseOffset;

      ctx.save();
      ctx.globalAlpha = alpha;
      ctx.textAlign = "center";
      ctx.textBaseline = "middle";
      ctx.font = "bold 14px Arial";

      ctx.strokeStyle = "#000000";
      ctx.lineWidth = 3;
      ctx.strokeText(`+${enemy.getLastDefeatScore()}`, popupX, popupY);

      ctx.fillStyle = "#FFFFFF";
      ctx.fillText(`+${enemy.getLastDefeatScore()}`, popupX, popupY);
      ctx.restore();
    }
  }

  /**
   * プレイヤー死亡時の回転・縮小演出
   */
  drawDyingSyujinkou(ctx, syujinkou, TILE_SIZE) {
    const progress = syujinkou.getDyingProgress();

    const centerX = syujinkou.getX() + TILE_SIZE / 2.0;
    const centerY = syujinkou.getY() + TILE_SIZE / 2.0;
    const scale = 1.0 - progress;

    ctx.save();
    ctx.translate(centerX, centerY);
    ctx.rotate((progress * 720 * Math.PI) / 180); // ラジアンに変換
    ctx.scale(scale, scale);
    ctx.globalAlpha = 1.0 - progress;

    // コンストラクタで読み込み済みの pacmanImage / pacmanFeverImage を使う
    const img = syujinkou.isFever?.()
      ? this.pacmanFeverImage
      : this.pacmanImage;

    if (img && img.complete && img.naturalWidth > 0) {
      ctx.drawImage(
        img,
        -TILE_SIZE / 2.0,
        -TILE_SIZE / 2.0,
        TILE_SIZE,
        TILE_SIZE,
      );
    } else {
      // 画像未読み込み時のフォールバック(黄色い円)
      ctx.fillStyle = "#FFFF00";
      ctx.beginPath();
      ctx.arc(0, 0, TILE_SIZE / 2.0, 0, Math.PI * 2);
      ctx.fill();
    }
    ctx.restore();
  }
}
