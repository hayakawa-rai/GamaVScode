// ==========================================================================
// シナリオデータ（JavaFXのDialogue配列をそのまま移植）
// ==========================================================================
const dialogues = [
    { speaker: "あにき", message: "……!?。", sound: "music/damage2.mp3", color: "red" },
    { speaker: "仙石さん", message: "……終わりだな。", sound: null, color: "white" },
    { speaker: "あにき", message: "……ああ、負けだ。", sound: null, color: "red" },
    { speaker: "仙石さん", message: "やりすぎだ。会社まで巻き込んで。", sound: "music/jump06.mp3", color: "white" },
    { speaker: "あにき", message: "分かってる。もうやめる。", sound: "music/jump06.mp3", color: "red" },
    { speaker: "仙石さん", message: "ならいい。今ならまだ戻せる。", sound: null, color: "white" },
    { speaker: "あにき", message: "……悪かった。全部返します。", sound: "music/jump06.mp3", color: "red" },
    { speaker: "仙石さん", message: "……はぁ。やっとか。", sound: null, color: "white" },
    { speaker: "仙石さん", message: "これで普通に働けるな。", sound: "music/jump06.mp3", color: "white" },
    { speaker: "あにき", message: "ああ。一社員としてしっかり働きます。", sound: "music/jump06.mp3", color: "red" },
    { speaker: "仙石さん", message: "よし。しっかり反省してるみたいだな。", sound: null, color: "white" },
    { speaker: "仙石さん", message: "戻るぞ。仕事が待ってる。", sound: "music/jump06.mp3", color: "white" },
    { speaker: "あにき", message: "はい。先輩！！", sound: "music/jump06.mp3", color: "red" }
];

// 画像パスの定数管理（あにきの表情差分用）
const ANIKI_NORMAL = "../../resources/picture/aniki-udekumi.png";
const ANIKI_ANGRY = "../../resources/picture/aniki2.png";

// ==========================================================================
// 状態管理変数
// ==========================================================================
let messageIndex = 0;
let charIndex = 0;
let isTyping = false;
let typingTimeout = null;
let isEndingStarted = false;
let isPaused = false;

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
// オーディオ制御関数
// ==========================================================================
function playSound(soundPath) {
    if (!soundPath || isPaused) return;
    
    const audio = new Audio(soundPath);
    
    // ボリューム個別調整 (JavaFXの設定値に同期)
    if (soundPath.includes("jump06")) audio.volume = 0.2;
    else if (soundPath.includes("damage2")) audio.volume = 0.4;
    
    audio.play().catch(e => console.log("オーディオ再生エラー (ユーザー操作待ち): ", e));
}

function startBGM() {
    console.log("BGM再生: endhing.mp3");
    // 必要に応じてWeb Audio APIやループ用Audioタグをここに実装
}

// ==========================================================================
// グラフィック・アニメーション制御
// ==========================================================================
function updateCharacters(speaker) {
    // 話者による可視化の切り替え
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

    // 💡 あにきの怒り顔⇔通常顔の切り替え判定（メッセージインデックス2〜10は怒り顔）
    if (messageIndex >= 2 && messageIndex <= 10) {
        if (anikiView.getAttribute("src") !== ANIKI_ANGRY) {
            anikiView.src = ANIKI_ANGRY;
        }
    } else {
        if (anikiView.getAttribute("src") !== ANIKI_NORMAL) {
            anikiView.src = ANIKI_NORMAL;
        }
    }
}

function triggerJump(speaker) {
    let targetView = null;
    if (speaker === "あにき") targetView = anikiView;
    else if (speaker === "仙石さん") targetView = syujinkouView;
    else if (speaker === "なりなり") targetView = nariView;
    else if (speaker === "わだたく") targetView = takuView;

    if (targetView) {
        targetView.style.transition = "none";
        targetView.style.transform = "translateY(0px)";
        
        // JavaFX側のピョンと跳ねるTimeline演出を再現
        setTimeout(() => {
            targetView.style.transition = "transform 0.1s ease-out";
            targetView.style.transform = "translateY(-30px)";
            
            setTimeout(() => {
                targetView.style.transition = "transform 0.15s ease-in";
                targetView.style.transform = "translateY(0px)";
            }, 100);
        }, 10);
    }
}

// わだたくがゆっくり下に降りる演出 (JavaFXのfall: TranslateTransition)
function triggerTakuFall() {
    takuView.style.transition = "transform 1s ease-in-out";
    takuView.style.transform = "translateY(100px)";
}

// ==========================================================================
// テキストタイピング処理
// ==========================================================================
function startTyping() {
    if (isPaused) return;

    const d = dialogues[messageIndex];
    
    if (charIndex === 0) {
        nameText.textContent = d.speaker;
        dialogueText.style.color = d.color;
        nextMark.classList.add("hidden");
        updateCharacters(d.speaker);
        
        // 最初のメッセージが表示され始めたタイミングで降下アニメーション
        if (messageIndex === 0) {
            triggerTakuFall();
        }
    }

    if (charIndex < d.message.length) {
        isTyping = true;
        charIndex++;
        dialogueText.textContent = d.message.substring(0, charIndex);
        typingTimeout = setTimeout(startTyping, 50); // 50ms間隔
    } else {
        isTyping = false;
        nextMark.classList.remove("hidden");
    }
}

// シナリオ進行とスキップ管理
function handleAdvance() {
    if (isPaused || isEndingStarted) return;

    const d = dialogues[messageIndex];

    // タイピング中のクリックは全文字即時表示
    if (isTyping) {
        clearTimeout(typingTimeout);
        dialogueText.textContent = d.message;
        isTyping = false;
        nextMark.classList.remove("hidden");
        return;
    }

    // 次のインデックスに進む
    if (messageIndex < dialogues.length - 1) {
        messageIndex++;
        charIndex = 0;
        
        const nextD = dialogues[messageIndex];
        
        if (nextD.sound) {
            playSound(nextD.sound);
            if (nextD.sound.includes("jump06")) {
                triggerJump(nextD.speaker);
            }
        }

        startTyping();
    } else {
        // すべてのテキスト終了、クリア画面へ
        startEnding();
    }
}

// ストーリークリア・フェードアウト処理
function startEnding() {
    isEndingStarted = true;
    nextMark.classList.add("hidden");
    
    // 暗転黒画面への遷移 (1.5秒)
    fadeRect.style.opacity = "1";

    setTimeout(() => {
        console.log("GameController.switchStoryClear(stage) を実行します");
        // クリア画面へのWEB遷移ロジックをここに実装
        // 例: window.location.href = "story_clear.html";
    }, 1500);
}

// ==========================================================================
// 一時停止（メニュー）システム
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
    console.log("GameController.switchStart(stage) を実行します");
    // タイトルへ戻る処理
    // 例: window.location.href = "index.html";
}

// ==========================================================================
// リスナー設定・ロード起動
// ==========================================================================
messageBox.addEventListener("click", (e) => {
    e.stopPropagation();
    handleAdvance();
});

gameContainer.addEventListener("click", (e) => {
    // ボタンやメニューのクリック以外なら進める
    if (e.target === gameContainer || e.target.id === "bg-view" || e.target.tagName === "IMG") {
        handleAdvance();
    }
});

menuBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    pauseGame();
});

resumeBtn.addEventListener("click", resumeGame);
titleBtn.addEventListener("click", goToTitle);

document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
        if (isPaused) resumeGame();
        else pauseGame();
    }
});

// 初期実行
startBGM();
if (dialogues[0].sound) {
    playSound(dialogues[0].sound);
}
startTyping();