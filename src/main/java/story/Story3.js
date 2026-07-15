import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";

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
        titleBtn: document.getElementById("title-btn")
    };

    // --- 各種アニメーション演出 ---

   // 落下アニメーション（CSSクラス .falling を付与）
    const triggerTakuFall = () => {
        const taku = document.getElementById("taku-wrapper");
        if (taku) {
            // 1. クラスを一旦削除してリセット（これにより何度でもアニメーションが発生する）
            taku.classList.remove("falling");
            
            // 2. 強制リフロー（重要：これがないとクラスを再付与してもCSSが即座に認識されない）
            void taku.offsetWidth; 

            // 3. クラスを付与してアニメーション開始
            taku.classList.add("falling");

            // 4. 音声再生
            const audio = new Audio("../../resources/music/down.mp3");
            audio.volume = 0.3; // 音量を調整してください
            audio.play().catch(error => console.warn("音声再生エラー:", error));
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

        // 1. 立ち絵の切り替え（hiddenクラスの付け外し）
        const views = ["aniki-view", "nari-view", "taku-view", "syujinkou-view"];
        views.forEach(id => {
            const el = document.getElementById(id);
            if (el) el.classList.add("hidden"); 
        });
        
        const map = { "あにき": "aniki-view", "なりなり": "nari-view", "わだたく": "taku-view", "仙石さん": "syujinkou-view" };
        const targetEl = document.getElementById(map[d.speaker]);
        if (targetEl) targetEl.classList.remove("hidden");

        // 2. 特殊イベント
        if (index === 0) triggerTakuFall();
        if (index === 12) triggerScreenShake();

        // 3. ジャンプ演出
        if (d.sound && d.sound.includes("jump06")) {
            triggerJump(d.speaker);
        }
    };

    // --- エンジンの起動 ---
    const engine = new StoryEngine(dialogues, {
        bgmPath: "../../resources/music/takubgm.mp3",
        ui: ui,
        onStep: onStep, // ストーリー進行時に呼ばれる
        onEnd: () => {
            const fadeRect = document.getElementById("fade-rect");
            if (fadeRect) fadeRect.style.opacity = "1";
            setTimeout(() => {
                GameController.switchToGame3(); 
            }, 1500);
        },
        onTitle: () => {
            GameController.switchStart();
        }
    });

    // これにより最初の立ち絵表示や落下アニメーションが正しく発生します
    onStep(0, ui);
});