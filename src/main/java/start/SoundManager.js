import { Bgm } from "./Bgm.js";
/**
 * ゲーム内の効果音を何度も呼び出す用のクラス
 */
class SoundClip {
  constructor(path, defaultVolume = 1.0) {
    this.path = path;
    this.volume = defaultVolume;
    // 事前ロード（あらかじめオーディオファイルをメモリに載せる）
    this.audio = new Audio(path);
    this.audio.preload = "auto";
    this.audio.volume = defaultVolume;

    console.log(`SE読込: ${path}`);
  }

  /**
   * 音量を設定します (JavaFXのsetVolumeに相当)
   */
  setVolume(value) {
    this.volume = value;
    if (this.audio) {
      this.audio.volume = value;
    }
  }

  /**
   * 効果音を再生します。
   * 連続再生に対応するため、一時停止して再生位置を0秒に巻き戻してから再生します。
   */
  play() {
    if (!this.audio) return;

    // 同じ効果音を連続再生できるよう一度停止して巻き戻す (clip.stop()の再現)
    this.audio.pause();
    this.currentTime = 0;

    Bgm.unlockPlay(this.audio);
  }

  /**
   * 効果音を停止します
   */
  stop() {
    if (this.audio) {
      this.audio.pause();
      this.audio.currentTime = 0;
    }
  }
}

/**
 * 効果音調節クラス (JavaFXのSoundManagerクラスを移植)
 */
export class SoundManager {
  // 1. 各効果音の定数定義
  static WARP = new SoundClip("../../resources/music/warp.mp3");
  static ENEMY_DEAD = new SoundClip("../../resources/music/enemydeadsound.mp3");
  static DAMAGE = new SoundClip("../../resources/music/syujinkoudeadsound.mp3");
  static FRUIT_EAT = new SoundClip("../../resources/music/fruiteatsound.mp3");
  static POWER_EAT = new SoundClip("../../resources/music/powerup.mp3");
  static GAMEOVER = new SoundClip("../../resources/music/gameover.mp3");
  static RETRY = new SoundClip("../../resources/music/retry.mp3");
  static SELECT = new SoundClip("../../resources/music/select.mp3");
  static CLEAR = new SoundClip("../../resources/music/yay.mp3")

  // 2. 音量調節 (Javaの static 初期化ブロックの再現)
  static {
    this.WARP.setVolume(0.5);       // ワープ音
    this.ENEMY_DEAD.setVolume(0.3); // 敵撃破
    this.DAMAGE.setVolume(0.1);     // ダメージ
    this.FRUIT_EAT.setVolume(0.4);  // フルーツ取得
    this.POWER_EAT.setVolume(0.5);  // パワーエサ取得
    this.GAMEOVER.setVolume(0.5);   // ゲームオーバー
    this.RETRY.setVolume(0.4);      // リトライ
    this.SELECT.setVolume(0.4);     // セレクト
    this.CLEAR.setVolume(0.5);      //クリア
  }

  /**
   * 効果音を再生する
   * @param {SoundClip} clip
   */
  static play(clip) {
    if (clip && typeof clip.play === "function") {
      clip.play();
    }
  }
}
