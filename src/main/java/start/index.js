import { GameController } from "../control/GameController.js";

let navigated = false;

function goToStart() {
    if (navigated) return;
    navigated = true;

    // 連打された際に視覚的・操作的にフリーズしたように見せないため、全体を無効化
    document.body.style.pointerEvents = "none";

    setTimeout(() => {
        try {
            GameController.switchStart();
        } catch (err) {
            console.error("画面遷移に失敗しました:", err);
            navigated = false;
            document.body.style.pointerEvents = "auto";
        }
    }, 50);
}

function attachListeners() {
    document.addEventListener("pointerdown", goToStart, { once: true });
    document.addEventListener("keydown", goToStart, { once: true });
}

document.addEventListener("DOMContentLoaded", attachListeners);

window.addEventListener("pageshow", (event) => {
    if (event.persisted) {
        navigated = false;                              // フラグをリセット
        document.body.style.pointerEvents = "auto";     // 固まっていたクリック無効化を解除
        attachListeners();                              // {once:true}で消えたリスナーを再登録
    }
});