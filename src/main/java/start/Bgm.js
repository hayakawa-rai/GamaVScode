export class Bgm {
  static #bgmPlayer = null;
  static #currentPath = null;
  static #currentStageNumber = 1;
  static #isPinchi = false;

  static #audioCtx = null;
  static #masterGain = null;
  static #systemVolume = 0.5;

  static #connectedNodes = new WeakMap();

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

  static initSystem() {
    if (Bgm.#audioCtx) {
      if (Bgm.#audioCtx.state === "suspended") {
        Bgm.#audioCtx.resume().catch(() => {});
      }
      return;
    }

    try {
      const AudioContextClass = window.AudioContext || window.webkitAudioContext;
      Bgm.#audioCtx = new AudioContextClass();
      Bgm.#masterGain = Bgm.#audioCtx.createGain();

      const savedVolume = localStorage.getItem("gameMasterVolume");
      Bgm.#systemVolume = savedVolume !== null ? parseFloat(savedVolume) : 0.5;

      Bgm.#masterGain.gain.value = Bgm.#systemVolume;
      Bgm.#masterGain.connect(Bgm.#audioCtx.destination);
    } catch (e) {
      console.warn("Web Audio API の初期化に失敗しました:", e);
    }
  }

  static getSystemVolume() {
    Bgm.initSystem();
    return Bgm.#systemVolume;
  }

  static setSystemVolume(volume) {
    Bgm.initSystem();
    Bgm.#systemVolume = volume;
    if (Bgm.#masterGain && Bgm.#audioCtx) {
      Bgm.#masterGain.gain.setValueAtTime(volume, Bgm.#audioCtx.currentTime);
    }
    localStorage.setItem("gameMasterVolume", volume);
  }

  static #playWithMixer(audioElement) {
    Bgm.initSystem();

    if (Bgm.#audioCtx) {
      if (Bgm.#audioCtx.state === "suspended") {
        Bgm.#audioCtx.resume().then(() => {
          Bgm.#connectAndPlay(audioElement);
        }).catch(() => {
          Bgm.#connectAndPlay(audioElement);
        });
      } else {
        Bgm.#connectAndPlay(audioElement);
      }
    } else {
      Bgm.#connectAndPlay(audioElement);
    }
  }

  static #connectAndPlay(audioElement) {
    if (Bgm.#audioCtx && Bgm.#masterGain) {
      try {
        if (!Bgm.#connectedNodes.has(audioElement)) {
          const source = Bgm.#audioCtx.createMediaElementSource(audioElement);
          source.connect(Bgm.#masterGain);
          Bgm.#connectedNodes.set(audioElement, source);
        }
      } catch (err) {
        console.debug("Audio node connection managed:", err);
      }
    }

    const playPromise = audioElement.play();
    if (playPromise !== undefined) {
      playPromise.catch((err) => {
        console.warn("自動再生がブロックされました。ユーザー操作を待ちます:", audioElement.src, err);
        Bgm.#pending.add(audioElement);
        Bgm.#attachListeners();
      });
    }
  }

  static #pending = new Set();
  static #listenersAttached = false;

  static #retryPending() {
    if (Bgm.#audioCtx && Bgm.#audioCtx.state === "suspended") {
      Bgm.#audioCtx.resume().catch(() => {});
    }

    Bgm.#pending.forEach((audio) => {
      audio.play().catch((err) => {
        console.warn("保留音源の再生に失敗しました:", audio.src, err);
      });
    });
    Bgm.#pending.clear();
    Bgm.#detachListeners();
  }

  static #attachListeners() {
    if (Bgm.#listenersAttached) return;
    Bgm.#listenersAttached = true;
    window.addEventListener("pointerdown", Bgm.#retryPending, { passive: true, once: true });
    window.addEventListener("keydown", Bgm.#retryPending, { once: true });
  }

  static #detachListeners() {
    window.removeEventListener("pointerdown", Bgm.#retryPending);
    window.removeEventListener("keydown", Bgm.#retryPending);
    Bgm.#listenersAttached = false;
  }

  static unlockPlay(audioElement) {
    return audioElement.play();
  }

  static playOneShot(path, volume = null) {
    const audio = new Audio(path);
    if (volume !== null) {
      audio.volume = volume;
    } else {
      const fileName = path.split("/").pop();
      audio.volume = Bgm.#SOUND_VOLUMES[fileName] ?? Bgm.#DEFAULT_SE_VOLUME;
    }
    Bgm.#playWithMixer(audio);
    return audio;
  }

  static playBGM(path) {
    if (this.#bgmPlayer !== null && path === this.#currentPath) {
      if (this.#bgmPlayer.paused) {
        Bgm.#playWithMixer(this.#bgmPlayer);
      }
      return;
    }
    this.stopBGM();
    try {
      const audio = new Audio(path);
      audio.loop = true;
      audio.volume = 1.0;

      this.#bgmPlayer = audio;
      this.#currentPath = path;

      Bgm.#playWithMixer(this.#bgmPlayer);
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
      Bgm.#playWithMixer(this.#bgmPlayer);
    }
  }
}