import { GameController } from "../control/GameController.js";

// ==========================================================================
// シナリオデータ（JavaFXのDialogue配列をそのまま移植）
// ==========================================================================
const dialogues = [
    { speaker: "わだたく", message: "……あれ……？もう、あそべない……？", sound: "music/down.mp3", color: "red" },
    { speaker: "仙石さん", message: "終わったか……", sound: null, color: "white" },
    { speaker: "あにき", message: "……ペットがやられたな。まあいい。", sound: "music/jump06.mp3", color: "red" },
    { speaker: "あにき", message: "代わりはいくらでもいる。", sound: null, color: "red" },
    { speaker: "仙石さん", message: "……ふざけるな。", sound: "music/feel.mp3", color: "white" },
    { speaker: "仙石さん", message: "社員を、道具みたいに扱いやがって……！", sound: null, color: "white" },
    { speaker: "仙石さん", message: "会社は、お前の遊び場じゃない！", sound: "music/jump06.mp3", color: "white" },
    { speaker: "あにき", message: "会社？", sound: null, color: "red" },
    { speaker: "あにき", message: "ここはもう俺の支配下だ。", sound: "music/jump06.mp3", color: "red" },
    { speaker: "あにき", message: "来るか、先輩社員サン。", sound: "music/jump06.mp3", color: "red" },
    { speaker: "仙石さん", message: "取り戻す。ここは俺たちの会社だ！", sound: "music/jump06.mp3", color: "white" },
    { speaker: "あにき", message: "いいだろう。", sound: null, color: "red" },
    { speaker: "あにき", message: "絶望を教えてやる！！", sound: "music/end.mp3", color: "red" }
];

// ==========================================================================
// 状態管理変数
// ==========================================================================
let messageIndex = 0;
let charIndex = 0;
let isTyping = false;
let typingTimeout = null;
let isEndingStarted = false;
let isPaused = false;

// オーディオキャッシュ用
const audioCache = {};

// DOM要素の取得
const gameContainer = document.getElementById("game-container");
const nameText = document.getElementById("name-text");
const dialogueText = document.getElementById("dialogue-text");
const nextMark = document.getElementById("next-mark");
const messageBox = document.getElementById("message-box");

const syujinkouView = document.getElementById("syujinkou-view");
const anikiView = document.getElementById("aniki-view");
const nariView = document.getElementById("nari-view");
const takuView = document.getElementById("taku-view");

const menuBtn = document.getElementById("menu-btn");
const menuOverlay = document.getElementById("menu-overlay");
const resumeBtn = document.getElementById("resume-btn");
const titleBtn = document.getElementById("title-btn");
const fadeRect = document.getElementById("fade-rect");

// ==========================================================================
// オーディオ再生関数（複数同時・連続再生に対応）
// ==========================================================================
function playSound(soundPath) {
    if (!soundPath || isPaused) return;
    
    // 毎回新しく生成することで、JavaFXのAudioClipのように連続で鳴らせるようにする
    const audio = new Audio(soundPath);
    
    // 音量調整（JavaFXの設定に合わせる）
    if (soundPath.includes("jump06")) audio.volume = 0.2;
    else if (soundPath.includes("down")) audio.volume = 0.3;
    else if (soundPath.includes("feel")) audio.volume = 0.5;
    else if (soundPath.includes("end")) audio.volume = 0.4;
    
    audio.play().catch(e => console.log("オーディオ再生はユーザー操作の後に許可されます: ", e));
}

// BGMのシミュレート（必要に応じて拡張してください）
function startBGM() {
    console.log("BGM再生: takubgm.mp3");
}

// ==========================================================================
// キャラクター立ち絵の制御 ＆ ジャンプアニメーション
// ==========================================================================
function updateCharacters(speaker) {
    // 右側キャラクターの切り替え
    if (speaker === "あにき") {
        anikiView.classList.remove("hidden");
        nariView.classList.add("hidden");
        takuView.classList.add("hidden");
    } else if (speaker === "なりなり") {
        anikiView.classList.add("hidden");
        nariView.classList.remove("hidden");
        takuView.classList.add("hidden");
    } else if (speaker === "わだたく") {
        anikiView.classList.add("hidden");
        nariView.classList.add("hidden");
        takuView.classList.remove("hidden");
    }
}

function triggerJump(speaker) {
    let targetView = null;
    if (speaker === "あにき") targetView = anikiView;
    else if (speaker === "仙石さん") targetView = syujinkouView;
    else if (speaker === "なりなり") targetView = nariView;
    else if (speaker === "わだたく") targetView = takuView;

    if (targetView) {
        // すでにアニメーション中なら一旦リセット
        targetView.style.transition = "none";
        targetView.style.transform = "translateY(0px)";
        
        // 跳ね上がるアニメーション（JavaFXのcreateJumpAnimationを再現）
        setTimeout(() => {
            targetView.style.transition = "transform 0.1s ease-out";
            targetView.style.transform = "translateY(-30px)"; // 上へ
            
            setTimeout(() => {
                targetView.style.transition = "transform 0.15s ease-in";
                targetView.style.transform = "translateY(0px)"; // 元に戻る
            }, 100);
        }, 10);
    }
}

// 画面全体の激しいシェイク（JavaFXの12番目のメッセージでの演出）
function triggerScreenShake() {
    let count = 0;
    const maxCount = 15;
    const interval = setInterval(() => {
        if (count >= maxCount || isPaused) {
            clearInterval(interval);
            gameContainer.style.transform = "none";
            return;
        }
        const x = Math.random() * 30 - 15;
        const y = Math.random() * 20 - 10;
        gameContainer.style.transform = `translate(${x}px, ${y}px)`;
        count++;
    }, 40);
}

// わだたくが力尽きて落ちていく演出（JavaFXのfall：TranslateTransition）
function triggerTakuFall() {
    takuView.style.transition = "transform 0.8s ease-in-out";
    takuView.style.transform = "translateY(200px)";
}

// ==========================================================================
// コアロジック：文字タイピングシステム
// ==========================================================================
function startTyping() {
    if (isPaused) return;

    const d = dialogues[messageIndex];
    
    // 最初の文字を打つ手前での一回限りの演出発動
    if (charIndex === 0) {
        nameText.textContent = d.speaker;
        dialogueText.style.color = d.color;
        nextMark.classList.add("hidden");
        updateCharacters(d.speaker);
        
        // 最初のメッセージでわだたくが沈む
        if (messageIndex === 0) {
            triggerTakuFall();
        }
    }

    if (charIndex < d.message.length) {
        isTyping = true;
        charIndex++;
        dialogueText.textContent = d.message.substring(0, charIndex);
        
        // 50ミリ秒周期で1文字ずつ表示（JavaFXのKeyFrameに相当）
        typingTimeout = setTimeout(startTyping, 50);
    } else {
        isTyping = false;
        nextMark.classList.remove("hidden");
    }
}

// 画面クリック（または画面タップ）時の進む/スキップ処理
function handleAdvance() {
    if (isPaused || isEndingStarted) return;

    const d = dialogues[messageIndex];

    // 1. タイピング中にクリックされたら全文字を即座に表示（スキップ）
    if (isTyping) {
        clearTimeout(typingTimeout);
        dialogueText.textContent = d.message;
        isTyping = false;
        nextMark.classList.remove("hidden");
        return;
    }

    // 2. 次のメッセージへ進む処理
    if (messageIndex < dialogues.length - 1) {
        messageIndex++;
        charIndex = 0;
        
        const nextD = dialogues[messageIndex];
        
        // 効果音＆ジャンプ判定
        if (nextD.sound) {
            playSound(nextD.sound);
            if (nextD.sound.includes("jump06")) {
                triggerJump(nextD.speaker);
            }
        }

        // 12番目のメッセージ（絶望を教えてやる！！の直前）で画面シェイク
        if (messageIndex === 12) {
            triggerScreenShake();
        }

        startTyping();
    } else {
        // 3. 全てのセリフ終了。次のゲーム画面（Game3）へフェードアウト
        startEnding();
    }
}

// エンディング・フェードアウト遷移
function startEnding() {
    isEndingStarted = true;
    nextMark.classList.add("hidden");
    
    // 暗転レイヤーの不透明度を上げて1.5秒かけて真っ黒にする
    fadeRect.style.opacity = "1";

    setTimeout(() => {
        console.log("GameController.switchToGame3(stage) を実行します");
        // ここに次の画面（Game3）へ進むためのURL遷移や関数呼び出しを記述してください
        GameController.switchToGame3();
    }, 1500);
}

// ==========================================================================
// メニュー・一時停止システムの制御
// ==========================================================================
function pauseGame() {
    isPaused = true;
    clearTimeout(typingTimeout);
    menuOverlay.classList.remove("hidden");
}

function resumeGame() {
    isPaused = false;
    menuOverlay.classList.add("hidden");
    if (isTyping) {
        startTyping();
    }
}

function goToTitle() {
    console.log("GameController.switchStart(stage) を実行します（タイトルへ）");
    // タイトル画面のURLや処理へ遷移
    // 例: window.location.href = "title.html";
}

// ==========================================================================
// イベントリスナー登録設定
// ==========================================================================
// メッセージボックスおよび背景クリックでストーリー進行
messageBox.addEventListener("click", (e) => {
    e.stopPropagation(); // メニューボタン等への誤判定防止
    handleAdvance();
});
gameContainer.addEventListener("click", (e) => {
    if (e.target === gameContainer || e.target.id === "bg-view" || e.target.classList.contains("character-left") || e.target.classList.contains("character-right")) {
        handleAdvance();
    }
});

// 各種ボタンのイベント
menuBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    pauseGame();
});
resumeBtn.addEventListener("click", resumeGame);
titleBtn.addEventListener("click", goToTitle);

// ESCキーでメニューを開く（JavaFXのKeyCode.ESCAPEの移植）
document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
        if (isPaused) resumeGame();
        else pauseGame();
    }
});

// ==========================================================================
// 初期化実行
// ==========================================================================
startBGM();
// 初回のセリフの効果音を鳴らしてタイピングを開始
if (dialogues[0].sound) {
    playSound(dialogues[0].sound);
}
startTyping();