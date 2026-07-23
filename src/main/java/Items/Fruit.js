// フルーツアイテム
import { Item } from "./Item.js";
import { SoundManager } from "../start/SoundManager.js";
export class Fruit extends Item {

  // =========================
  // コンストラクタ
  // =========================
  constructor(type) {
    // Itemクラスへスコアを渡す
    super(type.getScore(), null);

    // フルーツ種類を保存
    this.type = type;

    // 残り表示時間（60FPS基準で約10秒）
    this.remainingTicks = 600;

    // 消滅フラグ
    this.isExpired = false;
  }

  // =========================
  // 更新処理
  // =========================
  update() {
    if (this.remainingTicks > 0) {
      this.remainingTicks--;
    } else {
      this.isExpired = true;
    }
  }

  // =========================
  // 食べる処理
  // =========================
  onEaten(player) {
    // スコア加算
    player.addScore(this.score);

    // 効果音再生
    SoundManager.play(SoundManager.FRUIT_EAT);

    console.log(this.type + "を食べた！ +" + this.score + "点");
  }

  // =========================
  // 描画処理
  // =========================
  draw(ctx, x, y, tileSize) {
    // 残り2秒以下で点滅演出
    if (
      this.remainingTicks < 120 &&
      Math.floor(this.remainingTicks / 10) % 2 === 0
    ) {
      return;
    }

    ctx.drawImage(
      this.type.getImage(),
      x + tileSize * 0.1,
      y + tileSize * 0.1,
      tileSize * 0.9,
      tileSize * 0.9,
    );
  }

  // =========================
  // getter
  // =========================

  // 消滅したか
  isExpiredFruit() {
    return this.isExpired;
  }

  // フルーツ種類
  getType() {
    return this.type;
  }
}
