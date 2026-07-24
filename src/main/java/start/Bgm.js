export class Bgm {
  static #bgmPlayer = null;
  static #currentPath = null;
  static #currentStageNumber = 1;
  static #isPinchi = false;

  static #systemVolume = 0.5; // デフォルト音量（50%）
  static #pendingAudio = new Set();
  static #listenersAttached = false;
  static #isUnlocked = false; // ユーザー操作によるアンロックが済んだかどうか

  // ファイルごとの音量バランス
  static #SOUND_VOLUMES = {
    "jump06.mp3": 0.85,
    "nari.mp3": 0.95,
    "damage.mp3": 0.9,
    "damage2.mp3": 0.9,
    "down.mp3": 0.9,
    "footsteps.mp3": 0.9,
    "appearance.mp3": 0.85,
    "shine.mp3": 0.9,
    "atac.mp3": 0.85,
    "feel.mp3": 0.85,
    "end.mp3": 0.95,
  };
  static #DEFAULT_SE_VOLUME = 0.2;

  // 初期化と保存された音量のロード
  static initSystem() {
    const savedVolume = localStorage.getItem("gameMasterVolume");
    if (savedVolume !== null) {
      Bgm.#systemVolume = parseFloat(savedVolume);
    }
    // システム初期化時に、まだリスナーがついていなければ常時スタンバイさせる
    Bgm.#attachGlobalListener();
  }

  static getSystemVolume() {
    Bgm.initSystem();
    return Bgm.#systemVolume;
  }

  /**
   * システム全体の音量を変更し、再生中のすべての音にリアルタイム反映する
   */
  static setSystemVolume(volume) {
    Bgm.#systemVolume = Math.min(1.0, Math.max(0.0, volume));
    localStorage.setItem("gameMasterVolume", Bgm.#systemVolume);

    // 再生中のBGMに音量を即時反映
    if (Bgm.#bgmPlayer) {
      Bgm.#bgmPlayer.volume = Bgm.#systemVolume;
    }
  }

  // ユーザーが最初に画面をタッチした瞬間に、保留されていた音源をすべてウェイクアップさせる
  static #handleUserGesture() {
    Bgm.#isUnlocked = true;
    Bgm.#retryPending();
    // 一度アンロックされたらグローバルリスナーは外す（必要に応じて再アタッチも可能）
    Bgm.#detachGlobalListener();
  }

  static #attachGlobalListener() {
    if (Bgm.#listenersAttached) return;
    Bgm.#listenersAttached = true;
    window.addEventListener("pointerdown", Bgm.#handleUserGesture, { passive: true });
    window.addEventListener("keydown", Bgm.#handleUserGesture);
  }

  static #detachGlobalListener() {
    window.removeEventListener("pointerdown", Bgm.#handleUserGesture);
    window.removeEventListener("keydown", Bgm.#handleUserGesture);
    Bgm.#listenersAttached = false;
  }

  // 保留リストにある音源をまとめて再トライする
  static #retryPending() {
    Bgm.#pendingAudio.forEach((audio) => {
      audio.play().catch((err) => {
        console.warn("保留音源の再生に失敗しました:", err);
      });
    });
    Bgm.#pendingAudio.clear();
  }

  // 音声を確実に再生する内部関数（ブロック対策付き）
  static #playAudio(audioElement) {
    Bgm.initSystem();

    const playPromise = audioElement.play();
    if (playPromise !== undefined) {
      playPromise.catch((err) => {
        // 自動再生ブロックされた場合、保留リストに入れてユーザー操作を待つ
        console.warn("自動再生がブロックされました。ユーザー操作を待ちます:", err);
        Bgm.#pendingAudio.add(audioElement);
        Bgm.#attachGlobalListener();
      });
    }
    return playPromise;
  }

  /**
   * 外部からの互換用：Start.js 等のエラーを防ぐためのメソッド
   */
  static unlockPlay(audioElement) {
    if (audioElement instanceof HTMLAudioElement) {
      return Bgm.#playAudio(audioElement);
    }
    return Promise.resolve();
  }

  /**
   * 使い捨ての単発音を再生
   */
  static playOneShot(path, volume = null) {
    Bgm.initSystem();
    const audio = new Audio(path);
    
    const fileName = path.split("/").pop();
    const baseVol = volume !== null ? volume : (Bgm.#SOUND_VOLUMES[fileName] ?? Bgm.#DEFAULT_SE_VOLUME);
    
    // システム音量と個別の音量バランスを掛け合わせる
    audio.volume = Math.min(1.0, Math.max(0.0, baseVol * Bgm.#systemVolume));

    Bgm.#playAudio(audio);
    return audio;
  }

  /**
   * BGMの開始
   */
  static playBGM(path) {
    if (this.#bgmPlayer !== null && path === this.#currentPath) {
      if (this.#bgmPlayer.paused) {
        Bgm.#playAudio(this.#bgmPlayer);
      }
      return;
    }
    this.stopBGM();
    try {
      Bgm.initSystem();
      const audio = new Audio(path);
      audio.loop = true;
      audio.volume = Math.min(1.0, Math.max(0.0, Bgm.#systemVolume));

      this.#bgmPlayer = audio;
      this.#currentPath = path;

      Bgm.#playAudio(this.#bgmPlayer);
    } catch (error) {
      console.error("BGMファイルの読み込みまたは再生に失敗しました: " + path, error);
    }
  }

  static playStageBGM(stageNumber) {
    this.#currentStageNumber = stageNumber;
    switch (stageNumber) {
      case 1:
        this.playBGM("../../resources/music/stage1_bgm.mp3");
        break;
      case 2:
        this.playBGM("../../resources/music/stage2_bgm.mp3");
        break;
      case 3:
        this.playBGM("../../resources/music/stage3_bgm.mp3");
        break;
      default:
        this.playBGM("../../resources/music/stage1_bgm.mp3");
        break;
    }
  }

  static playFeverBGM() {
    this.playBGM("../../resources/music/feverbgm.mp3");
  }

  static stopFeverBGM() {
    if (this.#isPinchi) {
      this.playPinchiBGM();
    } else {
      this.playStageBGM(this.#currentStageNumber);
    }
  }

  static playPinchiBGM() {
    this.#isPinchi = true;
    this.playBGM("../../resources/music/pinchi.mp3");
  }

  static clearPinchiMode() {
    this.#isPinchi = false;
  }

  static stopBGM() {
    if (this.#bgmPlayer !== null) {
      this.#bgmPlayer.pause();
      this.#bgmPlayer.currentTime = 0;
      this.#bgmPlayer = null;
      this.#currentPath = null;
    }
  }

  static pauseBGM() {
    if (this.#bgmPlayer !== null) {
      this.#bgmPlayer.pause();
    }
  }

  static resumeBGM() {
    if (this.#bgmPlayer !== null) {
      Bgm.#playAudio(this.#bgmPlayer);
    }
  }
}