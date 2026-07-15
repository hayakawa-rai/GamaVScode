import { GameController } from "../control/GameController.js";

document.addEventListener("DOMContentLoaded", () => {
    // --- 1. 要素の取得 ---
    const bgm = document.getElementById('bgm');
    const pages = document.querySelectorAll('.page-wrapper');
    const dots = document.querySelectorAll('.indicator-dot');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const backBtn = document.getElementById('back-btn');

    let currentPage = 0;

    // --- 2. ページ切り替え機能 ---
    function updatePage(index) {
        currentPage = (index + pages.length) % pages.length;
        
        pages.forEach((p, i) => {
            p.style.display = (i === currentPage) ? 'block' : 'none';
        });
        dots.forEach((d, i) => {
            d.classList.toggle('active', i === currentPage);
        });
    }

    // --- 3. イベント設定 ---
    prevBtn.addEventListener('click', () => updatePage(currentPage - 1));
    nextBtn.addEventListener('click', () => updatePage(currentPage + 1));

    backBtn.addEventListener('click', () => {
        bgm.pause();
        // ここがポイント！Start.jsと同じ呼び出し方
        GameController.switchStart();
    });

    // --- 4. 初期化 ---
    updatePage(0); // 最初のページを表示

    // BGM再生のトリガー
    document.addEventListener('click', () => {
        if (bgm.paused) bgm.play().catch(e => console.log(e));
    }, { once: true });
});