import { GameController } from "../control/GameController.js";
import { StoryEngine } from "./StoryEngine.js";
import { StoryUtils } from "./StoryUtils.js";
import { Bgm } from "../start/Bgm.js"; // ★新しくなったBgmクラスをインポート

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
        volumeSlider: document.getElementById('volume-slider'), // ★HTMLに追加したスライダーを取得
        wrappers: {
            syujinkou: document.getElementById('syujinkou-wrapper'),
            aniki: document.getElementById('aniki-wrapper'),
            nari: document.getElementById('nari-wrapper')
        },
        fadeOverlay: document.getElementById('fade-overlay')
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
    new StoryEngine(dialogues, {
        bgmPath: '../../resources/music/storybgm.mp3',
        bgmVolume: 0.2, //ここでBGMの音を調整できる。デフォは0.3
        ui: ui,
        
        onStep: (index, ui) => {
            const d = dialogues[index];
            StoryUtils.updateCharacterDisplay(d.speaker, ui.wrappers);
            StoryUtils.triggerJumpIfNeeded(d, ui.wrappers); // Story1/2は音があれば常にジャンプ
        },
        
        onEnd: () => {
            ui.fadeOverlay.style.opacity = "1";
            setTimeout(() => GameController.switchToGame1(), 1500);
        },
        onTitle: () => {GameController.switchStart();}
    });
});