document.addEventListener("DOMContentLoaded", () => {
    // ==================================================
    // 1. DOM要素の取得
    // ==================================================
    const storyBtn = document.getElementById("story-btn");
    const practiceBtn = document.getElementById("practice-btn");
    const exitBtn = document.getElementById("exit-btn");
    const helpBtn = document.getElementById("help-btn");

    // ==================================================
    // 2. オーディオの生成と初期設定
    // ==================================================
    const clickSound = new Audio("../../resources/music/select.mp3");
    const startBgm = new Audio("../../resources/music/startbgm.mp3");

    clickSound.volume = 0.4;
    startBgm.volume = 0.5;
    startBgm.loop = true;

    // 画面ロード時にBGMをスタート（ブラウザの仕様上、初回クリック後に鳴り始めます）
    startBgm.play().catch(err => console.log("ユーザー操作後にBGMが再生されます", err));

    /**
     * JavaFXのcleanup()に相当するオーディオ停止処理
     */
    function cleanup() {
        startBgm.pause();
        startBgm.currentTime = 0;
    }

    /**
     * JavaFXのTimeline(0.5秒ディレイ)を伴う安全な画面遷移
     * @param {string} url - 遷移先のHTMLファイル名
     */
    function transitionTo(url) {
        // 連打された時のために既存のSEを停止して最初から再生
        clickSound.pause();
        clickSound.currentTime = 0;
        clickSound.play();

        // 0.5秒（500ミリ秒）待ってからクリーンアップして遷移
        setTimeout(() => {
            cleanup();
            window.location.href = url;
        }, 500);
    }

    // ==================================================
    // 3. 各ボタンのクリックイベント（setOnActionの移植）
    // ==================================================

    // ① ストーリーモードへ
    storyBtn.addEventListener("click", () => {
        // GameController.startToStory(stage) のWeb版遷移
        transitionTo("story1.html"); 
    });

    // ② 練習モードへ
    practiceBtn.addEventListener("click", () => {
        // GameController.switchToPractice(stage) のWeb版遷移
        transitionTo("practice.html"); 
    });

    // ③ ヘルプ画面へ
    helpBtn.addEventListener("click", () => {
        // GameController.switchToHelp(stage) のWeb版遷移
        transitionTo("help.html"); 
    });

    // ④ ゲーム終了（WebAPIのJProロジックを完全移植）
    exitBtn.addEventListener("click", () => {
        clickSound.play();

        setTimeout(() => {
            cleanup();
            // JavaFX内のWebAPI.getWebAPI(stage)の仕様を踏襲：
            // ブラウザ環境であれば指定のURL（Bing等）へリダイレクトさせ、Webタブを終了したように見せる
            window.location.href = "https://www.bing.com";
        }, 500);
    });
});