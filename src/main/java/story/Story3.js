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

    // 2. DOM要素の取得
    const ui = {
        container: document.getElementById("game-container"),
        nameText: document.getElementById("speaker-name"),
        messageText: document.getElementById("message-text"),
        nextMark: document.getElementById("next-mark"),
        menuBtn: document.getElementById("menu-btn"),
        menuOverlay: document.getElementById("menu-overlay"),
        resumeBtn: document.getElementById("resume-btn"),
        titleBtn: document.getElementById("title-btn"),
        volumeSlider: document.getElementById('volume-slider'),
        wrappers: {
            syujinkou: document.getElementById('syujinkou-wrapper'),
            aniki: document.getElementById('aniki-wrapper'),
            taku: document.getElementById('taku-wrapper')
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

    // --- 3. エンジン起動 ---
    const engine = new StoryEngine(dialogues, {
        bgmPath: "../../resources/music/takubgm.mp3",
        bgmVolume: 0.2, // BGM音量
        ui: ui,
        
        onStep: (index, ui) => {
            const d = dialogues[index];
            const w = ui.wrappers;

            // A. 立ち絵切り替え（共通処理）
            StoryUtils.updateCharacterDisplay(d.speaker, w);

            // B. 特殊演出（わだたくの落下）
            if (index === 0 && w.taku) {
                w.taku.classList.remove("falling");
                void w.taku.offsetWidth;
                w.taku.classList.add("falling");
                Bgm.playOneShot("../../resources/music/down.mp3", 0.3);
            }

            // C. 画面揺れ演出
            if (index === 12) {
                StoryUtils.triggerScreenShake(ui.container);
            }

            // D. ジャンプアニメーション
            StoryUtils.triggerJumpIfNeeded(d, w, (sound) => sound && sound.includes("jump06"));
        },
        
        onEnd: () => {
            const fadeRect = document.getElementById("fade-rect");
            if (fadeRect) fadeRect.style.opacity = "1";
            setTimeout(() => { GameController.switchToGame3(); }, 1500);
        },
        
        onTitle: () => {
            GameController.switchStart();
        }
    });
});