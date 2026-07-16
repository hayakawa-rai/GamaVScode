import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";

document.addEventListener("DOMContentLoaded", () => {
    // --- 1. データ定義 ---
    const dialogues = [
        { speaker: "なりなり", message: "あ、あれっ…！？", sound: "../../resources/music/nari.mp3", textColor: "orange" },
        { speaker: "仙石さん", message: "弱いな！？", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "なりなり", message: "ま、まだだ…まだ終わってない…！", sound: null, textColor: "orange" },
        { speaker: "仙石さん", message: "もう終わってる。", sound: "../../resources/music/nari.mp3", textColor: "white" },
        { speaker: "なりなり", message: "ぐああああああ！！", sound: "../../resources/music/damage.mp3", textColor: "orange" },
        { speaker: "あにき", message: "クク…やはり雑魚か。", sound: null, textColor: "red" },
        { speaker: "仙石さん", message: "おい、完全に遊ばれてるぞ。", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "あにき", message: "まあいい。次は特別だ。", sound: null, textColor: "red" },
        { speaker: "あにき", message: "来い、わだたく。", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "わだたく", message: "……とてとて…", sound: "../../resources/music/footsteps.mp3", textColor: "pink" },
        { speaker: "わだたく", message: "……ぴょこっ", sound: "../../resources/music/appearance.mp3", textColor: "pink" },
        { speaker: "仙石さん", message: "……ん？", sound: "../../resources/music/nari.mp3", textColor: "white" },
        { speaker: "仙石さん", message: "なんだこのかわいい生き物は。", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "わだたく", message: "わだ〜たく〜…♪", sound: "../../resources/music/shine.mp3", textColor: "pink" },
        { speaker: "わだたく", message: "よろしくね♪", sound: "../../resources/music/shine.mp3", textColor: "pink" },
        { speaker: "仙石さん", message: "……弱そうだな。", sound: "../../resources/music/nari.mp3", textColor: "white" },
        { speaker: "あにき", message: "見た目で判断するな。", sound: null, textColor: "red" },
        { speaker: "わだたく", message: "えいっ", sound: "../../resources/music/atac.mp3", textColor: "pink" },
        { speaker: "仙石さん", message: "ぐっ！？", sound: "../../resources/music/damage2.mp3", textColor: "white" },
        { speaker: "仙石さん", message: "な、何だ今の一撃は…！", sound: null, textColor: "white" },
        { speaker: "わだたく", message: "あそぼ？♪", sound: "../../resources/music/shine.mp3", textColor: "red" },
        { speaker: "わだたく", message: "いっぱいあそぼ〜♪", sound: "../../resources/music/shine.mp3", textColor: "red" },
        { speaker: "あにき", message: "そいつは俺のペットでな。", sound: null, textColor: "red" },
        { speaker: "あにき", message: "強そうに見えないが、遊ばれたら最後だ。", sound: "../../resources/music/jump06.mp3", textColor: "red" }
    ];

    // --- 2. DOM要素取得 ---
    const ui = {
        container: document.getElementById('game-container'),
        nameText: document.getElementById('speaker-name'),
        messageText: document.getElementById('message-text'),
        nextMark: document.getElementById('next-mark'),
        menuBtn: document.getElementById('menu-btn'),
        menuOverlay: document.getElementById('menu-overlay'),
        resumeBtn: document.getElementById('resume-btn'),
        titleBtn: document.getElementById('title-btn'),
        insertView: document.getElementById('insert-view'),
        fadeOverlay: document.getElementById('fade-overlay'),
        wrappers: {
            syujinkou: document.getElementById('syujinkou-wrapper'),
            aniki: document.getElementById('aniki-wrapper'),
            nari: document.getElementById('nari-wrapper'),
            taku: document.getElementById('taku-wrapper')
        }
    };

    // --- 3. エンジン起動 ---
    const engine = new StoryEngine(dialogues, {
        bgmPath: '../../resources/music/naribgm.mp3',
        ui: ui,
        
        onStep: (index, ui) => {
            const d = dialogues[index];
            const w = ui.wrappers;

    // A. 立ち絵切り替え（共通処理）
    StoryUtils.updateCharacterDisplay(d.speaker, w);

            // B. 特殊演出（差し込み絵）
            if (index === 4) ui.insertView.style.display = "block";
            if (index === 5) {
                ui.insertView.style.display = "none";
                engine.changeBGM("../../resources/music/storybgm.mp3");
            }

            // C. 揺れ演出（Index 18）
            if (index === 18) {
        w.syujinkou.classList.add('shake');
        setTimeout(() => w.syujinkou.classList.remove('shake'), 600);
            }

            // D. ジャンプアニメーション
            StoryUtils.triggerJumpIfNeeded(d, w);
        },
        
        onEnd: () => {
            ui.fadeOverlay.style.opacity = "1";
            setTimeout(() => GameController.switchToGame2(), 1500);
        },
        
        onTitle: () => {
            GameController.switchStart();
        }
    });
});