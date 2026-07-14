document.addEventListener("DOMContentLoaded", () => {
    // --- 1. データ定義 ---
    const dialogues = [
        { speaker: "仙石さん", message: "おはよ～～！！", sound: "jump", color: "white" },
        { speaker: "あにき", message: "先輩社員サン、ですか。", sound: "jump", color: "red" },
        { speaker: "あにき", message: "今日からここの社長は俺だ。", sound: "jump", color: "red" },
        { speaker: "あにき", message: "休憩時間以外は全て俺のものだ！！！", sound: "jump", color: "red" },
        { speaker: "仙石さん", message: "ふざけるな。ここは俺たちの会社だ。", sound: "jump", color: "white" },
        { speaker: "仙石さん", message: "取り戻してやる！！", sound: "jump", color: "white" },
        { speaker: "あにき", message: "クク……熱いねえ", sound: "jump", color: "red" },
        { speaker: "あにき", message: "だが、まずは順番ってものがある。", sound: "jump", color: "red" },
        { speaker: "あにき", message: "新入社員を育てるのも、上司の務めだろう？", sound: "jump", color: "red" },
        { speaker: "なりなり", message: "ここから先は通しませんよ、先輩。", sound: "jump", color: "orange" },
        { speaker: "なりなり", message: "自分、もう\"研修\"は終わってるんで。", sound: "jump", color: "orange" },
        { speaker: "仙石さん", message: "研修で覚えたのは、会社を乗っ取ることか？", sound: "jump", color: "white" },
        { speaker: "仙石さん", message: "教育しなおしてやる！！", sound: "jump", color: "white" }
    ];

    // --- 2. オーディオ設定 ---
    const sounds = {
        jump: new Audio('../../resources/music/jump06.mp3')
    };
    sounds.jump.volume = 0.2;

    let currentBgm = new Audio('../../resources/music/storybgm.mp3');
    currentBgm.loop = true;

    function playSound(key) {
        if (!key || !sounds[key]) return;
        sounds[key].currentTime = 0; // 連続再生用リセット
        sounds[key].play().catch(e => console.warn("Audio play failed", e));
    }

    // --- 3. DOM要素取得 ---
    const ui = {
        container: document.getElementById('game-container'),
        speakerName: document.getElementById('speaker-name'),
        messageText: document.getElementById('message-text'),
        nextMark: document.getElementById('next-mark'),
        syujinkou: document.getElementById('syujinkou'),
        aniki: document.getElementById('aniki'),
        nari: document.getElementById('nari'),
        menuBtn: document.getElementById('menu-btn'),
        menuOverlay: document.getElementById('menu-overlay'),
        resumeBtn: document.getElementById('resume-btn'),
        titleBtn: document.getElementById('title-btn'),
        fadeOverlay: document.getElementById('fade-overlay')
    };

    // --- 4. 状態管理 ---
    let messageIndex = 0;
    let charIndex = 0;
    let isTyping = false;
    let typeInterval = null;
    let isEndingStarted = false;

    // --- 5. メインロジック ---
    function startStory() {
        // 初回クリックなどでBGMを再生（ブラウザの自動再生ブロック対策）
        currentBgm.play().catch(() => {}); 
        showDialogue();
    }

function showDialogue() {
    if (messageIndex >= dialogues.length) return;

    const d = dialogues[messageIndex];
    
    // --- 立ち絵表示制御 ---
    const syujinkouWrapper = document.getElementById('syujinkou-wrapper');
    const anikiWrapper = document.getElementById('aniki-wrapper');
    const nariWrapper = document.getElementById('nari-wrapper');

    if (d.speaker === "仙石さん") {
        // 主人公の発言時は、今の右側キャラの状態を維持（あるいは必要に応じて変更）
        syujinkouWrapper.classList.add('active');
    } else if (d.speaker === "あにき") {
        // あにきが喋るときは、あにきを表示、なりなりを隠す
        anikiWrapper.classList.add('active');
        nariWrapper.classList.remove('active');
    } else if (d.speaker === "なりなり") {
        // なりなりが喋るときは、なりなりを表示、あにきを隠す
        anikiWrapper.classList.remove('active');
        nariWrapper.classList.add('active');
    }

    // --- 以下、既存のテキスト・アニメーション処理 ---
    ui.speakerName.innerText = d.speaker;
    ui.messageText.style.color = d.color;
    ui.messageText.innerText = "";
    ui.nextMark.style.display = "none";
    charIndex = 0;
    isTyping = true;

    if (d.sound) {
        StoryUtils.triggerJump(d.speaker, ui);
    }

    clearInterval(typeInterval);
    typeInterval = setInterval(typeChar, 50);
}

    function typeChar() {
        const d = dialogues[messageIndex];
        if (charIndex < d.message.length) {
            ui.messageText.innerText += d.message.charAt(charIndex);
            charIndex++;
        } else {
            finishTyping();
        }
    }

    function finishTyping() {
        clearInterval(typeInterval);
        isTyping = false;
        ui.messageText.innerText = dialogues[messageIndex].message;
        ui.nextMark.style.display = "block";
    }


    function progressStory() {
        if (ui.menuOverlay.style.display === "flex" || isEndingStarted) return;

        if (isTyping) {
            // タイピング中ならスキップして全文表示
            finishTyping();
        } else {
            // 次のメッセージへ
            if (messageIndex < dialogues.length - 1) {
                messageIndex++;
                showDialogue();
            } else {
                // エンディングフェーズ（Javaの fade.setOnFinished に相当）
                isEndingStarted = true;
                ui.nextMark.style.display = "none";
                ui.fadeOverlay.style.opacity = "1"; // フェードアウト実行
                currentBgm.pause();
                
                // 1.5秒(フェード時間)待ってから画面遷移
                setTimeout(() => {
                    console.log("Switch to Game1");
                    // 次のシーンへ遷移する処理
                    // window.location.href = "game1.html"; 
                }, 1500);
            }
        }
    }

    // --- 6. イベントリスナー ---
    ui.container.addEventListener("click", progressStory);

    ui.menuBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        ui.menuOverlay.style.display = "flex";
        if (isTyping) clearInterval(typeInterval); // タイピングをポーズ
    });

    ui.resumeBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        ui.menuOverlay.style.display = "none";
        if (isTyping) {
            // タイピング再開
            typeInterval = setInterval(typeChar, 50);
        }
    });

    ui.titleBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        currentBgm.pause();
        console.log("Switch to Start");
        GameController.switchStart();
    });

    // ESCキーでのメニュー表示
    window.addEventListener("keydown", (e) => {
        if (e.code === "Escape") {
            ui.menuBtn.click();
        }
    });

    // --- 初期化起動 ---
    startStory();
});