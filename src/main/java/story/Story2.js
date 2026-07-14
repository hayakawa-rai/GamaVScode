// サウンド管理オブジェクト (BGM用)
const Bgm = {
    currentBgm: null,
    playBGM(src) {
        this.stopBGM();
        this.currentBgm = new Audio(src);
        this.currentBgm.loop = true;
        this.currentBgm.volume = 0.5;
        this.currentBgm.play().catch(e => console.log("BGM再生にはユーザー操作が必要です。"));
    },
    stopBGM() {
        if (this.currentBgm) {
            this.currentBgm.pause();
            this.currentBgm = null;
        }
    }
};

// 対話データの定義クラス (前回の移植版をベース)
class Dialogue {
    constructor(speaker, message, soundPath, textColor) {
        this.speaker = speaker;
        this.message = message;
        this.sound = soundPath ? new Audio(soundPath) : null;
        if (this.sound) this.sound.volume = 0.3;
        this.textColor = textColor;
    }
    playEventSound() {
        if (this.sound) {
            this.sound.currentTime = 0;
            this.sound.play().catch(e => {});
        }
    }
}

// 効果音ファイルの準備（パスは環境に合わせて調整してください）
const jumpSound = "/src/main/resources/music/jump06.mp3";
const cuteSound = "/src/main/resources/music/footsteps.mp3";
const appearSound = "/src/main/resources/music/appearance.mp3";
const mysteriousSound = "/src/main/resources/music/nari.mp3";
const shineSound = "/src/main/resources/music/shine.mp3";
const damageSound = "/src/main/resources/music/damage.mp3";
const aSound = "/src/main/resources/music/damage2.mp3";
const atacSound = "/src/main/resources/music/atac.mp3";

// シナリオデータの配列化 (JavaFXのList<Dialogue>に相当)
const dialogues = [
    new Dialogue("なりなり", "あ、あれっ…！？", mysteriousSound, "orange"),
    new Dialogue("仙石さん", "弱いな！？", jumpSound, "white"),
    new Dialogue("なりなり", "ま、まだだ…まだ終わってない…！", null, "orange"),
    new Dialogue("仙石さん", "もう終わってる。", mysteriousSound, "white"),
    new Dialogue("なりなり", "ぐああaaaa転！！", damageSound, "orange"),
    new Dialogue("あにき", "クク…やはり雑魚か。", null, "red"),
    new Dialogue("仙石さん", "おい、完全に遊ばれてるぞ。", jumpSound, "white"),
    new Dialogue("あにき", "まあいい。次は特別だ。", null, "red"),
    new Dialogue("あにき", "来い、わだたく。", jumpSound, "red"),
    new Dialogue("わだたく", "……とてとて…", cuteSound, "pink"),
    new Dialogue("わだたく", "……ぴょこっ", appearSound, "pink"),
    new Dialogue("仙石さん", "……ん？", mysteriousSound, "white"),
    new Dialogue("仙石さん", "なんだこのかわいい生き物は。", jumpSound, "white"),
    new Dialogue("わだたく", "わだ〜たく〜…♪", shineSound, "pink"),
    new Dialogue("わだたく", "よろしくね♪", shineSound, "pink"),
    new Dialogue("仙石さん", "……弱そうだな。", mysteriousSound, "white"),
    new Dialogue("あにき", "見た目で判断するな。", null, "red"),
    new Dialogue("わだたく", "えいっ", atacSound, "pink"),
    new Dialogue("仙石さん", "ぐっ！？", aSound, "white"),
    new Dialogue("仙石さん", "な、何だ今の一撃は…！", null, "white"),
    new Dialogue("わだたく", "あそぼ？♪", shineSound, "red"),
    new Dialogue("わだたく", "いっぱいあそぼ〜♪", shineSound, "red"),
    new Dialogue("あにき", "そいつは俺のペットでな。", null, "red"),
    new Dialogue("あにき", "強そうに見えないが、遊ばれたら最後だ。", jumpSound, "red")
];

// 状態変数
let messageIndex = 0;
let charIndex = 0;
let isTyping = false;
let typingTimer = null;
let isEndingStarted = false;

// DOM要素の取得
const nameText = document.getElementById("name-text");
const dialogueText = document.getElementById("dialogue-text");
const nextMark = document.getElementById("next-mark");
const messageBox = document.getElementById("message-box");

const syujinkouView = document.getElementById("syujinkou-view");
const anikiView = document.getElementById("aniki-view");
const nariView = document.getElementById("nari-view");
const takuView = document.getElementById("taku-view");
const insertView = document.getElementById("insert-view");

const menuBtn = document.getElementById("menu-btn");
const menuOverlay = document.getElementById("menu-overlay");
const resumeBtn = document.getElementById("resume-btn");
const titleBtn = document.getElementById("title-btn");
const fadeRect = document.getElementById("fade-rect");

// 初回起動処理
function init() {
    Bgm.playBGM("/src/main/resources/music/naribgm.mp3");
    startTyping();
    
    // イベントリスナー登録
    messageBox.addEventListener("click", handleBoxClick);
    menuBtn.addEventListener("click", openMenu);
    resumeBtn.addEventListener("click", closeMenu);
    titleBtn.addEventListener("click", goToTitle);
    
    window.addEventListener("keydown", (e) => {
        if (e.key === "Escape") openMenu();
    });
}

// タイピング演出開始
function startTyping() {
    clearInterval(typingTimer);
    charIndex = 0;
    isTyping = true;
    nextMark.classList.add("hidden");
    
    const d = dialogues[messageIndex];
    nameText.innerText = d.speaker;
    dialogueText.style.color = d.textColor;
    
    // 話者に応じた立ち絵の表示切り替えロジック
    updateCharacterVisibility(d.speaker);

    // 💡 JavaFXのTimeline(50ms)に相当するループ処理
    typingTimer = setInterval(() => {
        if (charIndex < d.message.length) {
            charIndex++;
            dialogueText.innerText = d.message.substring(0, charIndex);
        } else {
            finishTyping();
        }
    }, 50);
}

// 文字表示の即時完了
function finishTyping() {
    clearInterval(typingTimer);
    const d = dialogues[messageIndex];
    dialogueText.innerText = d.message;
    isTyping = false;
    nextMark.classList.remove("hidden");
}

// 話者による画像切り替えロジック
function updateCharacterVisibility(speaker) {
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
        // インデックス10未満は非表示にするJavaFX側の条件の再現
        if (messageIndex < 10) {
            takuView.classList.add("hidden");
        } else {
            takuView.classList.remove("hidden");
        }
    }
}

// 画面（メッセージエリア）がクリックされた時の進行制御
function handleBoxClick() {
    if (menuOverlay.classList.contains("hidden") === false) return;

    if (isTyping) {
        finishTyping();
        return;
    }

    if (messageIndex < dialogues.size - 1) {
        messageIndex++;

        // 特定インデックスによるカットイン画像演出の再現
        if (messageIndex === 4) insertView.classList.remove("hidden");
        if (messageIndex === 5) {
            insertView.classList.add("hidden");
            Bgm.playBGM("/src/main/resources/music/storybgm.mp3");
        }

        // 💡 仙石さんの被弾（シェイク）演出の再現 (Index 18)
        if (messageIndex === 18) {
            syujinkouView.style.opacity = "0.5";
            syujinkouView.classList.add("shake");
            
            setTimeout(() => {
                syujinkouView.style.opacity = "1.0";
                syujinkouView.classList.remove("shake");
            }, 600); // 100ms * 6往復に相当
        }

        // 効果音再生
        const d = dialogues[messageIndex];
        if (d.sound) d.playEventSound();

        // 💡 キャラクターのジャンプ演出について：
        // WebではCSSアニメーションクラスを一時的に追加することで簡単に再現可能です。
        // （今回は割愛していますが、必要に応じて .jump クラス等を作成して要素に付与してください）

        startTyping();
    } else {
        // 全セリフ終了 -> シーン終了（フェードアウト黒）
        if (isEndingStarted) return;
        isEndingStarted = true;

        nextMark.classList.add("hidden");
        fadeRect.classList.remove("hidden");
        
        // CSSのtransitionで1.5秒かけて暗転
        setTimeout(() => {
            fadeRect.style.opacity = "1";
        }, 50);

        setTimeout(() => {
            cleanup();
            console.log("次のゲーム画面へ遷移: GameController.switchToGame2");
            // location.href = "game2.html"; // Webであれば次のページへの遷移など
        }, 1550);
    }
}

// メニュー（ポーズ）関連
function openMenu() {
    menuOverlay.classList.remove("hidden");
    clearInterval(typingTimer); // タイピングの一時停止
}

function closeMenu() {
    menuOverlay.classList.add("hidden");
    if (isTyping) {
        // 再開時にタイピングの続きを行う
        startTyping();
    }
}

function goToTitle() {
    cleanup();
    console.log("タイトル画面へ遷移: GameController.switchStart");
}

function cleanup() {
    clearInterval(typingTimer);
    Bgm.stopBGM();
}

// ページ読み込み完了時に初期化を実行
window.onload = init;