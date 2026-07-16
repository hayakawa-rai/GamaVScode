import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";

document.addEventListener("DOMContentLoaded", () => {
    // 1. シナリオデータの定義
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

    // 2. UI要素の取得
    const ui = {
        container: document.getElementById("game-container"),
        nameText: document.getElementById("name-text"),
        messageText: document.getElementById("dialogue-text"),
        nextMark: document.getElementById("next-mark"),
        menuBtn: document.getElementById("menu-btn"),
        menuOverlay: document.getElementById("menu-overlay"),
        resumeBtn: document.getElementById("resume-btn"),
        titleBtn: document.getElementById("title-btn")
    };

    // --- 各種アニメーション演出 ---

    // 落下アニメーション
    const triggerTakuFall = () => {
        const taku = document.getElementById("taku-wrapper");
        if (taku) {
            taku.style.transition = "transform 1s ease-in-out";
            taku.style.transform = "translateY(100px)";
        }
    };

    // ジャンプアニメーション
    const triggerJump = (speaker) => {
        const views = { "あにき": "aniki-wrapper", "仙石さん": "syujinkou-wrapper" };
        const el = document.getElementById(views[speaker]);
        if (el) {
            StoryUtils.createJumpAnimation(el, () => {});
        }
    };

    // 立ち絵・表情更新
    const updateCharacterDisplay = (speaker, index) => {
        const anikiWrapper = document.getElementById("aniki-wrapper");
        const syujinkouWrapper = document.getElementById("syujinkou-wrapper");
        
        const anikiImg = document.getElementById("aniki");
        const syujinkouImg = document.getElementById("syujinkou");

        // 1. 全てを隠す
        [anikiWrapper, syujinkouWrapper].forEach(el => el.classList.add("hidden"));
        
        // 2. 指定されたキャラクターを表示
        const map = { "あにき": anikiWrapper, "仙石さん": syujinkouWrapper };
        if (map[speaker]) map[speaker].classList.remove("hidden");

        // 3. あにきの表情切り替え
        if (speaker === "あにき") {
            const isAngry = (index >= 2 && index <= 10);
            anikiImg.src = isAngry ? "../../resources/picture/aniki2.png" : "../../resources/picture/aniki-udekumi.png";
        }
    };

    // --- ストーリー進行制御 ---
    const onStep = (index, ui) => {
        const d = dialogues[index];

        updateCharacterDisplay(d.speaker, index);

        // 特殊イベント
        if (index === 0) triggerTakuFall();

        // ジャンプ演出
        if (d.sound && d.sound.includes("jump06")) {
            triggerJump(d.speaker);
        }
    };

    // --- エンジンの起動 ---
        titleBtn: document.getElementById("title-btn"),
        wrappers: {
            syujinkou: document.getElementById("syujinkou-wrapper"),
            aniki: document.getElementById("aniki-wrapper")
        }
    };

    // --- 各種アニメーション演出 ---

     const anikiImg = document.getElementById("aniki");

    const onStep = (index, ui) => {
        const d = dialogues[index];

        // 立ち絵切り替え（共通処理・.active方式に統一）
        StoryUtils.updateCharacterDisplay(d.speaker, ui.wrappers);

        // あにきの表情切り替え（Story4固有の演出はそのまま残す）
        if (d.speaker === "あにき") {
            const isAngry = (index >= 2 && index <= 10);
            anikiImg.src = isAngry
                ? "../../resources/picture/aniki2.png"
                : "../../resources/picture/aniki-udekumi.png";
        }

        // ジャンプ演出（jump06サウンドの時だけ）
        StoryUtils.triggerJumpIfNeeded(d, ui.wrappers, (sound) => sound && sound.includes("jump06"));
    };
    const engine = new StoryEngine(dialogues, {
        bgmPath: "../../resources/music/endhing.mp3",
        ui: ui,
        onStep: onStep,
        onEnd: () => {
            const fadeRect = document.getElementById("fade-rect");
            if (fadeRect) fadeRect.style.opacity = "1";

            setTimeout(() => {
                GameController.switchStoryClear(); 
            }, 1500);
        },
        onTitle: () => {
            GameController.switchStart();
        }

            setTimeout(() => { GameController.switchStoryClear(); }, 1500);
        },
        onTitle: () => { GameController.switchStart(); }
    });
});