/**
 * ヘルプ画面を生成するコンポーネント
 * @param {Object} gameController 画面遷移を管理するオブジェクト (JavaFXのGameControllerに相当)
 * @returns {HTMLElement} ヘルプ画面のルート要素 (StackPaneに相当)
 */
export function createHelpScreen(gameController) {
    // 1. ルート要素の作成 (StackPane)
    const root = document.createElement('div');
    root.style.position = 'relative';
    root.style.width = '100%';
    root.style.height = '100%';
    root.style.overflow = 'hidden';
    root.style.backgroundColor = 'black'; // 白飛び防止
    root.style.display = 'flex';
    root.style.justifyContent = 'center';
    root.style.alignItems = 'center';
    root.style.fontFamily = '"PixelMplus12", sans-serif';

    // 2. BGMの再生 (JavaFXの Bgm.playBGM に相当)
    // ※ブラウザの仕様上、初回操作前に再生するとブロックされる可能性があるため例外処理を入れています
    const bgm = new Audio('/music/startbgm.mp3');
    bgm.loop = true;
    bgm.play().catch(() => {
        // 自動再生が拒否された場合は画面初回クリック時に再生
        document.addEventListener('click', () => bgm.play(), { once: true });
    });

    // 3. 👑 背景の斜めスクロールパネル (Pane & AnimationTimer)
    const bgPane = document.createElement('div');
    bgPane.style.position = 'absolute';
    bgPane.style.top = '0';
    bgPane.style.left = '0';
    bgPane.style.width = '100%';
    bgPane.style.height = '100%';
    bgPane.style.backgroundImage = "url('/picture/background.png')";
    bgPane.style.backgroundRepeat = 'repeat';
    bgPane.style.backgroundPosition = '0px 0px';
    bgPane.style.zIndex = '1';
    root.appendChild(bgPane);

    let bgX = 0;
    const speed = 1.0;
    let animationFrameId = null;

    // AnimationTimerのhandleメソッドに相当
    function scrollBackground() {
        bgX -= speed;
        bgPane.style.backgroundPosition = `${bgX}px ${bgX}px`;
        animationFrameId = requestAnimationFrame(scrollBackground);
    }
    // Platform.runLater に相当（要素が組み立てられた後に開始）
    requestAnimationFrame(scrollBackground);


    // 4. メインコンテナの作成 (VBox)
    const mainContainer = document.createElement('div');
    mainContainer.style.position = 'relative';
    mainContainer.style.zIndex = '2';
    mainContainer.style.display = 'flex';
    mainContainer.style.flexDirection = 'column';
    mainContainer.style.alignItems = 'center';
    mainContainer.style.gap = '15px';
    mainContainer.style.width = '100%';
    mainContainer.style.maxWidth = '560px';
    mainContainer.style.padding = '20px';
    mainContainer.style.boxSizing = 'border-box';

    // 5. 中央の半透明パネル (VBox)
    const panel = document.createElement('div');
    panel.style.width = '100%';
    panel.style.backgroundColor = 'rgba(0,0,0,0.6)';
    panel.style.border = '2px solid #6e6e85';
    panel.style.borderRadius = '4px';
    panel.style.padding = '25px 30px';
    panel.style.display = 'flex';
    panel.style.flexDirection = 'column';
    panel.style.gap = '16px';
    panel.style.alignItems = 'center';
    panel.style.boxSizing = 'border-box';

    // タイトル (Label)
    const title = document.createElement('div');
    title.innerText = '基本説明';
    title.style.color = '#F4C022';
    title.style.fontSize = '36px';
    title.style.fontWeight = 'bold';
    panel.appendChild(title);

    // 区切り線 (Separator)
    const divider = document.createElement('div');
    divider.style.width = '100%';
    divider.style.height = '2px';
    divider.style.backgroundColor = '#6e6e85';
    panel.appendChild(divider);

    // 6. ページコンテンツの定義 (pages配列)
    const pageHtmlStrings = [
        // ページ1: 操作
        `<div class="page-heading" style="color:#4FD8E8; font-size:18px; font-weight:bold;">操作</div>
         <div style="display:flex; flex-direction:column; gap:16px; align-items:center; width:100%;">
            <div style="display:flex; align-items:center; gap:8px; justify-content:center; color:white; font-size:15px;">
                <span>移動：</span><img src="/picture/WASD.png" style="height:65px; object-fit:contain;">
                <span>または</span><img src="/picture/yazirusi.png" style="height:65px; object-fit:contain;">
            </div>
            <div style="height:6px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;">
                <div style="width:40px; min-width:40px;"></div>
                <div style="color:white; font-size:14px; max-width:420px; word-wrap:break-word;">スマホでの移動：画面スワイプ</div>
            </div>
         </div>`,
        // ページ2: アイテム
        `<div class="page-heading" style="color:#4FD8E8; font-size:18px; font-weight:bold;">アイテム</div>
         <div style="display:flex; flex-direction:column; gap:10px; width:100%;">
            <div style="display:flex; align-items:center; gap:10px; width:100%;">
                <div style="width:40px; min-width:40px; display:flex; justify-content:center;"><div style="width:8px; height:8px; background-color:#F4C022; border-radius:50%;"></div></div>
                <div style="color:white; font-size:14px;">エサを食べるとスコアが加算されます。</div>
            </div>
            <div style="height:6px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;">
                <div style="width:40px; min-width:40px; display:flex; justify-content:center;"><img src="/picture/Chii_Item.png" style="width:36px; height:36px; object-fit:contain;"></div>
                <div style="color:white; font-size:14px;">パワーエサを食べるとスコアが加算され、一定時間敵を撃退することができます。</div>
            </div>
            <div style="height:6px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">フルーツを食べると種類に合わせたスコアが加算されます。</div></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">一定数のエサを食べるとランダムな場所に出現し、一定時間が過ぎると消えてなくなります。</div></div>
            <div style="height:6px;"></div>
            <div style="display:flex; gap:12px; justify-content:center; width:100%;">
                <div style="display:flex; flex-direction:column; align-items:center; gap:4px;"><img src="/picture/sakuranbo.png" style="width:32px; height:32px;"><span style="color:#F4C022; font-size:11px; font-weight:bold;">＋100</span></div>
                <div style="display:flex; flex-direction:column; align-items:center; gap:4px;"><img src="/picture/ichigo.png" style="width:32px; height:32px;"><span style="color:#F4C022; font-size:11px; font-weight:bold;">＋300</span></div>
                <div style="display:flex; flex-direction:column; align-items:center; gap:4px;"><img src="/picture/orange.png" style="width:32px; height:32px;"><span style="color:#F4C022; font-size:11px; font-weight:bold;">＋500</span></div>
                <div style="display:flex; flex-direction:column; align-items:center; gap:4px;"><img src="/picture/ringo.png" style="width:32px; height:32px;"><span style="color:#F4C022; font-size:11px; font-weight:bold;">＋700</span></div>
                <div style="display:flex; flex-direction:column; align-items:center; gap:4px;"><img src="/picture/budo.png" style="width:32px; height:32px;"><span style="color:#F4C022; font-size:11px; font-weight:bold;">＋1000</span></div>
            </div>
         </div>`,
        // ページ3: ルール
        `<div class="page-heading" style="color:#4FD8E8; font-size:18px; font-weight:bold;">ルール</div>
         <div style="display:flex; flex-direction:column; gap:10px; width:100%;">
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">全てのエサを食べるとステージクリアになります。</div></div>
            <div style="height:6px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">敵に3回触れるとゲームオーバーになります。</div></div>
            <div style="height:6px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">マップにある左端と右端の通路は「ワープトンネル」で、左端と右端が繫がった状態の通路になっています。</div></div>
         </div>`,
        // ページ4: モード
        `<div class="page-heading" style="color:#4FD8E8; font-size:18px; font-weight:bold;">モード</div>
         <div style="display:flex; flex-direction:column; gap:10px; width:100%;">
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:#FF9632; font-size:18px; font-weight:bold;">▶ストーリー</div></div>
            <div style="height:2px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">物語を進めながら遊ぶモードです。</div></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">すべてのエサを食べると、そのステージをクリアできます。</div></div>
            <div style="height:6px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:#FF9632; font-size:18px; font-weight:bold;">⚔練習モード</div></div>
            <div style="height:2px;"></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">すべてのエサを食べるとエサが再配置され、ゲームオーバーになるまで何度でも遊べます。</div></div>
            <div style="display:flex; align-items:center; gap:10px; width:100%;"><div style="width:40px;"></div><div style="color:white; font-size:14px;">ハイスコアを目指しましょう！</div></div>
         </div>`
    ];

    // ページスタック (StackPane)
    const pageStack = document.createElement('div');
    pageStack.style.width = '100%';
    pageStack.style.maxWidth = '500px';

    const pageElements = pageHtmlStrings.map((htmlStr, idx) => {
        const wrapper = document.createElement('div');
        wrapper.style.display = idx === 0 ? 'flex' : 'none'; // 初期ページのみ表示
        wrapper.style.flexDirection = 'column';
        wrapper.style.gap = '12px';
        wrapper.style.alignItems = 'flex-start';
        wrapper.style.width = '100%';
        wrapper.innerHTML = htmlStr;
        pageStack.appendChild(wrapper);
        return wrapper;
    });
    panel.appendChild(pageStack);

    // 7. インジケータードット (● ○ ○ ○)
    const indicator = document.createElement('div');
    indicator.style.display = 'flex';
    indicator.style.gap = '8px';
    indicator.style.justifyContent = 'center';

    const dots = Array.from({ length: 4 }).map((_, idx) => {
        const dot = document.createElement('div');
        dot.style.width = '8px';
        dot.style.height = '8px';
        dot.style.borderRadius = '50%';
        dot.style.backgroundColor = idx === 0 ? '#4FD8E8' : '#6e6e85';
        dot.style.transition = 'background-color 0.2s';
        indicator.appendChild(dot);
        return dot;
    });
    panel.appendChild(indicator);
    mainContainer.appendChild(panel);

    // 8. 左右の矢印ボタン (ページ切り替え)
    let currentPage = 0;
    function updatePageVisibility() {
        pageElements.forEach((el, idx) => {
            const isCurrent = idx === currentPage;
            el.style.display = isCurrent ? 'flex' : 'none';
            dots[idx].style.backgroundColor = isCurrent ? '#4FD8E8' : '#6e6e85';
        });
    }

    const arrowRow = document.createElement('div');
    arrowRow.style.display = 'flex';
    arrowRow.style.gap = '40px';
    arrowRow.style.justifyContent = 'center';
    arrowRow.style.width = '100%';

    const makeArrow = (symbol, delta) => {
        const btn = document.createElement('button');
        btn.innerText = symbol;
        btn.style.width = '50px';
        btn.style.height = '45px';
        btn.style.fontSize = '18px';
        btn.style.fontWeight = 'bold';
        btn.style.backgroundColor = 'rgba(0, 0, 0, 0.6)';
        btn.style.color = '#4FD8E8';
        btn.style.border = '2px solid currentColor';
        btn.style.borderRadius = '10px';
        btn.style.cursor = 'pointer';
        btn.addEventListener('click', () => {
            currentPage = (currentPage + delta + pageElements.length) % pageElements.length;
            updatePageVisibility();
        });
        return btn;
    };

    arrowRow.appendChild(makeArrow('◀', -1));
    arrowRow.appendChild(makeArrow('▶', 1));
    mainContainer.appendChild(arrowRow);

    // 9. 戻るボタン (ご提示いただいた style.css の .help-button クラスを利用)
    const backBtn = document.createElement('button');
    backBtn.innerText = '戻る';
    backBtn.classList.add('help-button'); // 外部CSSのスタイルを適用
    backBtn.style.width = '200px';
    backBtn.style.height = '50px';
    backBtn.style.fontSize = '20px';

    backBtn.addEventListener('click', () => {
        // AnimationTimerの停止 (timer.stop())
        if (animationFrameId) cancelAnimationFrame(animationFrameId);
        bgm.pause();

        // GameController.switchStart(stage) の再現
        if (gameController && typeof gameController.switchStart === 'function') {
            gameController.switchStart();
        }
    });
    mainContainer.appendChild(backBtn);

    root.appendChild(mainContainer);
    return root;
}