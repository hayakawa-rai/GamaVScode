/**
 * BGM・効果音・台詞音管理クラス (音量一括管理・保存対応版)
 */
export class Bgm {
  static #bgmPlayer = null;
  static #currentPath = null;
  static #currentStageNumber = 1;
  static #isPinchi = false;

  // Web Audio API（システム全体のミキサー）用のプロパティ
  static #audioCtx = null;
  static #masterGain = null;
  static #systemVolume = 0.5; // デフォルト音量（50%）

  // 各オーディオの接続状態を管理するMap（二重接続エラーを防止する）
  static #connectedNodes = new WeakMap();

  // ==================================================
  //   台詞・演出用の効果音、ファイルごとの音量バランス
  //   ※ ここは「全体の最大音量に対して、この効果音は何%にするか」のバランス値になります
  // ==================================================
  static #SOUND_VOLUMES = {
    "jump06.mp3": 0.15,
    "nari.mp3": 0.25,
    "damage.mp3": 0.3,
    "damage2.mp3": 0.3,
    "down.mp3": 0.3,
    "footsteps.mp3": 0.2,
    "appearance.mp3": 0.25,
    "shine.mp3": 0.3,
    "atac.mp3": 0.25,
    "feel.mp3": 0.25,
    "end.mp3": 0.35,
  };
  static #DEFAULT_SE_VOLUME = 0.2;

  // ==================================================
  // ★重要：システム全体のオーディオ初期化と音量取得・設定
  // ==================================================

  /**
   * システムのミキサー（AudioContext）を初期化し、保存されている音量を読み込む
   */
  static initSystem() {
    if (Bgm.#audioCtx) return;

    try {
      // 1. オーディオシステムを起動
      const AudioContextClass = window.AudioContext || window.webkitAudioContext;
      Bgm.#audioCtx = new AudioContextClass();

      // 2. 全体の音量を司るミキサー（GainNode）を作成
      Bgm.#masterGain = Bgm.#audioCtx.createGain();

      // 3. 保存された音量をロード（無ければ 0.5）
      const savedVolume = localStorage.getItem("gameMasterVolume");
      Bgm.#systemVolume = savedVolume !== null ? parseFloat(savedVolume) : 0.5;

      // 4. ミキサーの音量を設定し、スピーカーに接続
      Bgm.#masterGain.gain.value = Bgm.#systemVolume;
      Bgm.#masterGain.connect(Bgm.#audioCtx.destination);
    } catch (e) {
      console.warn("Web Audio API の初期化に失敗しました。標準再生に切り替えます:", e);
    }
  }

  /**
   * 現在の「全体の音量」を取得する（スライダーの初期位置設定に必須）
   */
  static getSystemVolume() {
    Bgm.initSystem();
    return Bgm.#systemVolume;
  }

  /**
   * 【スライダーから呼ぶ用】全体の音量を変更して保存する
   * @param {number} volume (0.0 〜 1.0)
   */
  static setSystemVolume(volume) {
    Bgm.initSystem();
    Bgm.#systemVolume = volume;

    // ミキサーの値をリアルタイムで変更
    if (Bgm.#masterGain) {
      Bgm.#masterGain.gain.setValueAtTime(volume, Bgm.#audioCtx.currentTime);
    }

    // ブラウザに自動保存
    localStorage.setItem("gameMasterVolume", volume);
  }

  /**
   * 音源をマスターミキサーに繋いで再生する内部関数
   * ★修正：play()の成否だけでなく、AudioContextが実際に"running"状態かどうかも確認する。
   *   モバイルブラウザ(特にiOS Safari)では、ページ遷移直後などユーザー操作の
   *   文脈外で呼ばれた場合、play()自体は成功したように見えても、AudioContextが
   *   "suspended"のままで実際には音が出ないことがある。その場合は保留リストに入れて
   *   次のユーザー操作（タップ/クリック/キー入力）で再試行する。
   */
  static #playWithMixer(audioElement) {
    Bgm.initSystem();

    // タッチ制限対策：サスペンド状態なら再開させる
    if (Bgm.#audioCtx && Bgm.#audioCtx.state === "suspended") {
      Bgm.#audioCtx.resume().catch(() => {});
    }

    // ミキサーが使える場合のみ接続処理
    if (Bgm.#audioCtx && Bgm.#masterGain) {
      try {
        if (!Bgm.#connectedNodes.has(audioElement)) {
          const source = Bgm.#audioCtx.createMediaElementSource(audioElement);
          source.connect(Bgm.#masterGain);
          Bgm.#connectedNodes.set(audioElement, source);
        }
      } catch (err) {
        // すでに接続されている場合などの例外をキャッチ
        console.debug("Audio node connection managed:", err);
      }
    }

    // 自動再生ブロック対策を挟んで再生
    Bgm.unlockPlay(audioElement);

    // ★AudioContextがまだrunningでなければ、実質的に音が出ていない状態なので
    //   保留リストに入れて、次のユーザー操作で確実に再試行させる
    if (Bgm.#audioCtx && Bgm.#audioCtx.state !== "running") {
      Bgm.#pending.add(audioElement);
      Bgm.#attachListeners();
    }
  }

  // ==================================================
  // 自動再生ブロック対策（保留リスト）
  // ==================================================
  static #pending = new Set();
  static #listenersAttached = false;

  /**
   * ★修正：保留中の音を再試行する前に、必ずAudioContextの再開を試みる。
   *   これがないと、AudioContextがsuspendedのまま個々のaudio.play()だけ
   *   呼んでも、ミキサーの出口が閉じたままなので音が出ない。
   */
  static #retryPending() {
    if (Bgm.#audioCtx && Bgm.#audioCtx.state === "suspended") {
      Bgm.#audioCtx.resume().catch(() => {});
    }

    Bgm.#pending.forEach((audio) => {
      audio.play().catch((err) => {
        console.warn("再生に失敗しました:", audio.src, err);
      });
    });
    Bgm.#pending.clear();
    Bgm.#detachListeners();
  }

  static #attachListeners() {
    if (Bgm.#listenersAttached) return;
    Bgm.#listenersAttached = true;
    document.addEventListener("pointerdown", Bgm.#retryPending, {
      passive: true,
    });
    document.addEventListener("keydown", Bgm.#retryPending);
  }

  static #detachListeners() {
    document.removeEventListener("pointerdown", Bgm.#retryPending);
    document.removeEventListener("keydown", Bgm.#retryPending);
    Bgm.#listenersAttached = false;
  }

  static unlockPlay(audioElement) {
    const playPromise = audioElement.play();
    if (playPromise !== undefined) {
      playPromise.catch(() => {
        Bgm.#pending.add(audioElement);
        Bgm.#attachListeners();
      });
    }
    return playPromise;
  }

  // ==================================================
  // 共通再生機能（これからは volume の個別設定は不要！）
  // ==================================================

  /**
   * 使い捨ての単発音を再生
   */
  static playOneShot(path, volume = null) {
    const audio = new Audio(path);

    // バランス調整のみ個別で行い、全体音量はミキサーで制御します
    if (volume !== null) {
      audio.volume = volume;
    } else {
      const fileName = path.split("/").pop();
      audio.volume = Bgm.#SOUND_VOLUMES[fileName] ?? Bgm.#DEFAULT_SE_VOLUME;
    }

    Bgm.#playWithMixer(audio);
    return audio;
  }

  /**
   * BGMの開始
   */
  static playBGM(path) {
    if (this.#bgmPlayer !== null && path === this.#currentPath) return;
    this.stopBGM();
    try {
      const audio = new Audio(path);
      audio.loop = true;
      audio.volume = 1.0; // BGMの基礎音量は最大にしてミキサー側で制御

      this.#bgmPlayer = audio;
      this.#currentPath = path;

      Bgm.#playWithMixer(this.#bgmPlayer);
    } catch (error) {
      console.error(
        "BGMファイルの読み込みまたは再生に失敗しました: " + path,
        error,
      );
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
      Bgm.#playWithMixer(this.#bgmPlayer);
    }
  }
}