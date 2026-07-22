// Help.js

import { GameController } from "../control/GameController.js";
import { Bgm } from "./Bgm.js";

document.addEventListener("DOMContentLoaded", () => {
    // --- 1. 要素の取得 ---
    const bgm = document.getElementById('bgm');
    const pages = document.querySelectorAll('.page-wrapper');
    const dots = document.querySelectorAll('.indicator-dot');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const backBtn = document.getElementById('back-btn');

    let currentPage = 0;
    // ==================================================
    // 2. オーディオの生成と初期設定
    // ==================================================
    const clickSound = new Audio("../../resources/music/select.mp3");
    const startBgm = new Audio("../../resources/music/startbgm.mp3");

    clickSound.volume = 0.4;
    startBgm.volume = 0.5;
    startBgm.loop = true;

    // 画面ロード時にBGMをスタート
    Bgm.unlockPlay(startBgm);

    function transitionTo() {
        clickSound.pause();
        clickSound.currentTime = 0;
        Bgm.unlockPlay(clickSound);
    }

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
    prevBtn.addEventListener('click', () => {
        transitionTo();
        updatePage(currentPage - 1)
    });
    
    nextBtn.addEventListener('click', () => {
        transitionTo();
        updatePage(currentPage + 1)
    });

    backBtn.addEventListener('click', () => {
        transitionTo();

        setTimeout(() => { 
        bgm.pause();
        // ここがポイント！Start.jsと同じ呼び出し方
        GameController.switchStart();
        }, 400);

        
    });

    // --- 4. 初期化 ---
    updatePage(0); // 最初のページを表示

});