import { GameController } from "../control/GameController.js";

document.addEventListener("DOMContentLoaded", () => {
    let navigated = false;

    // pointerdown = クリック・タップ・ペンを一括カバー
    document.addEventListener("pointerdown", goToStart);
    document.addEventListener("keydown", goToStart);

    function goToStart() {
        if (navigated) return; // 連打による二重遷移を防止
        navigated = true;
        GameController.switchStart(); // Start.htmlへ遷移
    }
});