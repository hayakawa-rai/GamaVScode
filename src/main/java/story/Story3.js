import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";
import { Bgm } from "../start/Bgm.js";

document.addEventListener("DOMContentLoaded", () => {

    // 1. シナリオデータの定義
    const dialogues = [
        { speaker: "わだたく", message: "……あれ……？もう、あそべない……？", sound: "../../resources/music/down.mp3", color: "red" },
        { speaker: "仙石さん", message: "終わったか……", sound: null, color: "white" },
        { speaker: "あにき", message: "……ペットがやられたな。まあいい。", sound: "../../resources/music/jump06.mp3", color: "red" },
        { speaker: "あにき", message: "代わりはいくらでもいる。", sound: null, color: "red" },
        { speaker: "仙石さん", message: "……ふざけるな。", sound: "../../resources/music/feel.mp3", color: "white" },
        { speaker: "仙石さん", message: "社員を、道具みたいに扱いやがって……！", sound: null, color: "white" },
        { speaker: "仙石さん", message: "会社は、お前の遊び場じゃない！", sound: "../../resources/music/jump06.mp3", color: "white" },
        { speaker: "あにき", message: "会社？", sound: null, color: "red" },
        { speaker: "あにき", message: "ここはもう俺の支配下だ。", sound: "../../resources/music/jump06.mp3", color: "red" },
        { speaker: "あにき", message: "来るか、先輩社員サン。", sound: "../../resources/music/jump06.mp3", color: "red" },
        { speaker: "仙石さん", message: "取り戻す。ここは俺たちの会社だ！", sound: "../../resources/music/jump06.mp3", color: "white" },
        { speaker: "あにき", message: "いいだろう。", sound: null, color: "red" },
        { speaker: "あにき", message: "絶望を教えてやる！！", sound: "../../resources/music/end.mp3", color: "red" }
    ];

    // 2. UI要素の取得
    const ui = {
        container: document.getElementById("game-container"),
        nameText: document.getElementById("speaker-name"),
        messageText: document.getElementById("message-text"),
        nextMark: document.getElementById("next-mark"),
        menuBtn: document.getElementById("menu-btn"),
        menuOverlay: document.getElementById("menu-overlay"),
        resumeBtn: document.getElementById("resume-btn"),
        titleBtn: document.getElementById("title-btn"),
        wrappers: {
            syujinkou: document.getElementById('syujinkou-wrapper'),
            aniki: document.getElementById('aniki-wrapper'),
            taku: document.getElementById('taku-wrapper')
        }
    };

    // --- 各種アニメーション演出 ---

   // 落下アニメーション（ここでBGM.jsをつかっている）
    const triggerTakuFall = () => {
    const taku = ui.wrappers.taku;
    if (taku) {
        taku.classList.remove("falling");
        void taku.offsetWidth;
        taku.classList.add("falling");
        Bgm.playOneShot("../../resources/music/down.mp3", 0.3); //効果音の音量調整
    }
    };

    // 画面揺れアニメーション
    const triggerScreenShake = () => {
        let count = 0;
        const interval = setInterval(() => {
            if (count >= 15) { clearInterval(interval); ui.container.style.transform = "none"; return; }
            const x = Math.random() * 30 - 15;
            const y = Math.random() * 20 - 10;
            ui.container.style.transform = `translate(${x}px, ${y}px)`;
            count++;
        }, 40);
    };

    // ジャンプアニメーション（StoryUtilsの2段ジャンプを呼び出し）
    const triggerJump = (speaker) => {
        const views = { "あにき": "aniki-wrapper", "仙石さん": "syujinkou-wrapper","わだたく": "taku-wrapper" };
        const el = document.getElementById(views[speaker]);
        if (el) {
            StoryUtils.createJumpAnimation(el, () => {
                // 必要に応じてここで音を鳴らす処理を追加可能
            });
        }
    };

    // --- ストーリー進行制御 ---
    const onStep = (index, ui) => {
        const d = dialogues[index];

        // 立ち絵切り替え（共通処理）
        StoryUtils.updateCharacterDisplay(d.speaker, ui.wrappers);

        // 2. 特殊イベント
        if (index === 0) triggerTakuFall();
        if (index === 12) triggerScreenShake();

        // 3. ジャンプ演出
        StoryUtils.triggerJumpIfNeeded(d, ui.wrappers, (sound) => sound && sound.includes("jump06"));
        };

    // --- エンジンの起動 ---
    const engine = new StoryEngine(dialogues, {
        bgmPath: "../../resources/music/takubgm.mp3",
        ui: ui,
        onStep: onStep,
        onEnd: () => {
            const fadeRect = document.getElementById("fade-rect");
            if (fadeRect) fadeRect.style.opacity = "1";
            setTimeout(() => { GameController.switchToGame3(); }, 1500);
        },
        onTitle: () => { GameController.switchStart(); }
    });
});