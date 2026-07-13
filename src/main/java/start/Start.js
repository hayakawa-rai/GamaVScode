// ==========================================
// タイトル画面用コントローラー (Start.js)
// ==========================================

// タイトル画面の状態管理
const startState = {
    bgm: null,
    clickSound: null,
    isTransitioning: false
};

// 画面遷移先の設定
const NavigationController = {
    startToStory: () => {
        window.location.href = "story.html"; 
    },
    switchToPractice: () => {
        window.location.href = "practice.html"; 
    },
    switchToHelp: () => {
        window.location.href = "help.html"; 
    }
};

// オーディオの停止とクリア
function cleanupStartAudio() {
    if (startState.bgm) {
        startState.bgm.pause();
        startState.bgm = null;
    }
    if (startState.clickSound) {
        startState.clickSound.pause();
        startState.clickSound.currentTime = 0;
    }
}

// 効果音を鳴らしてから500ms後に安全に遷移する処理
function handleStartNavigation(transitionCallback) {
    if (startState.isTransitioning) return;
    startState.isTransitioning = true;

    if (startState.clickSound) {
        startState.clickSound.currentTime = 0;
        startState.clickSound.play().catch(e => console.log("Audio play blocked"));
    }

    setTimeout(() => {
        cleanupStartAudio();
        transitionCallback();
    }, 500);
}

// DOM読み込み完了時のイベント
document.addEventListener("DOMContentLoaded", () => {
    // 必要なボタン要素の取得
    const btnStory = document.getElementById("btn-story");
    const btnPractice = document.getElementById("btn-practice");
    const btnExit = document.getElementById("btn-exit");
    const btnHelp = document.getElementById("btn-help");

    // 要素が存在する場合のみオーディオのセットアップを行う（他画面での誤動作防止）
    if (btnStory || btnPractice || btnExit || btnHelp) {
        startState.bgm = new Audio("music/startbgm.mp3");
        startState.bgm.loop = true;
        
        startState.clickSound = new Audio("music/select.mp3");
        startState.clickSound.volume = 0.4;

        // ブラウザの自動再生制限対策：最初の画面クリックでBGM開始
        document.body.addEventListener("click", () => {
            if (startState.bgm && startState.bgm.paused && !startState.isTransitioning) {
                startState.bgm.play().catch(e => console.log("BGM autoplay restriction bypassed"));
            }
        }, { once: true });
    }

    // 各ボタンのイベントリスナー登録
    if (btnStory) {
        btnStory.addEventListener("click", () => {
            handleStartNavigation(() => { NavigationController.startToStory(); });
        });
    }

    if (btnPractice) {
        btnPractice.addEventListener("click", () => {
            handleStartNavigation(() => { NavigationController.switchToPractice(); });
        });
    }

    if (btnExit) {
        btnExit.addEventListener("click", () => {
            handleStartNavigation(() => { window.location.href = "https://www.bing.com"; });
        });
    }

    if (btnHelp) {
        btnHelp.addEventListener("click", () => {
            handleStartNavigation(() => { NavigationController.switchToHelp(); });
        });
    }
});