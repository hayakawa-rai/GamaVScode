import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";
import { Bgm } from "../start/Bgm.js";

document.addEventListener("DOMContentLoaded", () => {
    // --- 1. シナリオデータの定義 ---
    const dialogues = [
        { speaker: "あにき", message: "……!?。", sound: "../../resources/music/damage2.mp3", color: "red" },
        { speaker: "仙石さん", message: "……終わりだな。", sound: null, color: "white" },
        { speaker: "あにき", message: "……ああ、負けだ。", sound: null, color: "red" },
        { speaker: "仙石さん", message: "やりすぎだ。会社まで巻き込んで。", sound: "../../resources/music/jump06.mp3", color: "white" },
        { speaker: "あにき", message: "分かってる。もうやめる。", sound: "../../resources/music/jump06.mp3", color: "red" },
        { speaker: "仙石さん", message: "ならいい。今ならまだ戻せる。", sound: null, color: "white" },
        { speaker: "あにき", message: "……悪かった。全部返します。", sound: "../../resources/music/jump06.mp3", color: "red" },
        { speaker: "仙石さん", message: "……はぁ。やっとか。", sound: null, color: "white" },
        { speaker: "仙石さん", message: "これで普通に働けるな。", sound: "../../resources/music/jump06.mp3", color: "white" },
        { speaker: "あにき", message: "ああ。一社員としてしっかり働きます。", sound: "../../resources/music/jump06.mp3", color: "red" },
        { speaker: "仙石さん", message: "よし。しっかり反省してるみたいだな。", sound: null, color: "white" },
        { speaker: "仙石さん", message: "戻るぞ。仕事が待ってる。", sound: "../../resources/music/jump06.mp3", color: "white" },
        { speaker: "あにき", message: "はい。先輩！！", sound: "../../resources/music/jump06.mp3", color: "red" }
    ];

    // --- 2. DOM要素の取得 ---
    const ui = {
        container: document.getElementById("game-container"),
        nameText: document.getElementById("name-text"),
        messageText: document.getElementById("dialogue-text"),
        nextMark: document.getElementById("next-mark"),
        menuBtn: document.getElementById("menu-btn"),
        menuOverlay: document.getElementById("menu-overlay"),
        resumeBtn: document.getElementById("resume-btn"),
        titleBtn: document.getElementById("title-btn"),
        volumeSlider: document.getElementById('volume-slider'),
        wrappers: {
            syujinkou: document.getElementById("syujinkou-wrapper"),
            aniki: document.getElementById("aniki-wrapper")
        }
    };

    // --- 2.5 音量スライダーの初期化と連動イベント設定 ---
    if (ui.volumeSlider) {
        // 保存されているシステム全体の音量をロードしてスライダーの位置を合わせる
        ui.volumeSlider.value = Bgm.getSystemVolume();

        // つまみが動かされたら、システム全体の音量をリアルタイム変更（自動保存）
        ui.volumeSlider.addEventListener("input", (e) => {
            Bgm.setSystemVolume(parseFloat(e.target.value));
        });
    }
    
    // あにきの画像要素（表情切り替え用）
    const anikiImg = document.getElementById("aniki");

    // --- 3. エンジン起動 ---
    const engine = new StoryEngine(dialogues, {
        bgmPath: "../../resources/music/endhing.mp3",
        bgmVolume: 0.2, // BGM音量
        ui: ui,
        
        onStep: (index, ui) => {
            const d = dialogues[index];
            const w = ui.wrappers;

            // A. 立ち絵切り替え（共通処理）
            StoryUtils.updateCharacterDisplay(d.speaker, w);

            // B. あにきの表情切り替え
            if (d.speaker === "あにき" && anikiImg) {
                const isAngry = (index >= 2 && index <= 10);
                anikiImg.src = isAngry
                    ? "../../resources/picture/aniki2.png"
                    : "../../resources/picture/aniki-udekumi.png";
            }

            // C. ジャンプ演出
            StoryUtils.triggerJumpIfNeeded(d, w, (sound) => sound && sound.includes("jump06"));
        },
        
        onEnd: () => {
            const fadeRect = document.getElementById("fade-rect");
            if (fadeRect) fadeRect.style.opacity = "1";
            setTimeout(() => { GameController.switchStoryClear(); }, 1500);
        },
        
        onTitle: () => {
            GameController.switchStart();
        }
    });
});