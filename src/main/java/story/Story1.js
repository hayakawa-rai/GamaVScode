import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";

document.addEventListener("DOMContentLoaded", () => {
    // --- 1. データ定義 ---
    const dialogues = [
        { speaker: "仙石さん", message: "おはよ～～！！", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "あにき", message: "先輩社員サン、ですか。", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "あにき", message: "今日からここの社長は俺だ。", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "あにき", message: "休憩時間以外は全て俺のものだ！！！", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "仙石さん", message: "ふざけるな。ここは俺たちの会社だ。", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "仙石さん", message: "取り戻してやる！！", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "あにき", message: "クク……熱いねえ", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "あにき", message: "だが、まずは順番ってものがある。", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "あにき", message: "新入社員を育てるのも、上司の務めだろう？", sound: "../../resources/music/jump06.mp3", textColor: "red" },
        { speaker: "なりなり", message: "ここから先は通しませんよ、先輩。", sound: "../../resources/music/jump06.mp3", textColor: "orange" },
        { speaker: "なりなり", message: "自分、もう\"研修\"は終わってるんで。", sound: "../../resources/music/jump06.mp3", textColor: "orange" },
        { speaker: "仙石さん", message: "研修で覚えたのは、会社を乗っ取ることか？", sound: "../../resources/music/jump06.mp3", textColor: "white" },
        { speaker: "仙石さん", message: "教育しなおしてやる！！", sound: "../../resources/music/jump06.mp3", textColor: "white" }
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
        wrappers: {
            syujinkou: document.getElementById('syujinkou-wrapper'),
            aniki: document.getElementById('aniki-wrapper'),
            nari: document.getElementById('nari-wrapper')
        },
        fadeOverlay: document.getElementById('fade-overlay')
    };

    // 音声を鳴らすための汎用ヘルパー
    const playEffect = (path) => {
        if (!path) return;
        const audio = new Audio(path);
        audio.volume = 0.2;
        audio.play().catch(() => {});
    };

    // --- 3. エンジン起動 ---
    new StoryEngine(dialogues, {
        bgmPath: '../../resources/music/storybgm.mp3',
        ui: ui,
        
        onStep: (index, ui) => {
            const d = dialogues[index];
            const w = ui.wrappers;

            // A. 立ち絵の切り替え
            
            // 主人公は常に表示
            w.syujinkou.classList.add('active');
            
            // 右側のキャラの表示ロジック
            // 話者が「あにき」か「なりなり」のときだけ、そのキャラを有効化する
            // 「仙石さん」の場合は処理を行わないため、直前の状態が維持される
            if (d.speaker === "あにき") {
                w.aniki.classList.add('active');
                w.nari.classList.remove('active');
            } else if (d.speaker === "なりなり") {
                w.nari.classList.add('active');
                w.aniki.classList.remove('active');
            }

            // B. ジャンプ演出
            if (d.sound) {
                const target = d.speaker === "仙石さん" ? w.syujinkou :
                               d.speaker === "あにき" ? w.aniki :
                               d.speaker === "なりなり" ? w.nari : null;
                
                if (target) {
                    StoryUtils.createJumpAnimation(target, () => playEffect(d.sound));
                }
            }
        },
        
        onEnd: () => {
            ui.fadeOverlay.style.opacity = "1";
            setTimeout(() => GameController.switchToGame1(), 1500);
        },
        
        onTitle: () => {
            GameController.switchStart();
        }
    });
});