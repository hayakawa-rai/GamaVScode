import { GameController } from "../control/GameController.js";
import { Bgm } from "./Bgm.js";

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
    startBgm.volume = 0.3;
    startBgm.loop = true;

    // 画面ロード時にBGMをスタート
    Bgm.unlockPlay(startBgm);

    function cleanup() {
        startBgm.pause();
        startBgm.currentTime = 0;
    }

    /**
     * @param {Function} action - 遷移時に実行したい GameController のメソッド
     */
    function transitionTo(action) {
        clickSound.pause();
        clickSound.currentTime = 0;
        Bgm.unlockPlay(clickSound);

        setTimeout(() => {
            cleanup();
            action(); // ここで渡された GameController のメソッドを実行する
        }, 500);
    }

    // ==================================================
    // 3. 各ボタンのクリックイベント
    // ==================================================

    // ストーリーモードへ
    storyBtn.addEventListener("click", () => {
        transitionTo(() => GameController.switchToStory()); 
    });

    // 練習モードへ
    practiceBtn.addEventListener("click", () => {
        transitionTo(() => GameController.switchToPractice()); 
    });

    // ヘルプ画面へ
    helpBtn.addEventListener("click", () => {
        transitionTo(() => GameController.switchToHelp()); 
    });

    // ゲーム終了
    exitBtn.addEventListener("click", () => {
        transitionTo(() => {
            window.location.href = "https://www.bing.com";
        });
    });
});