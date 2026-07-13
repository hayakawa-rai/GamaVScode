// ==================================================
// WindowUtil
//
// ウィンドウサイズ管理
// Canvasの自動リサイズ管理
// ==================================================

class WindowUtil {

    // 登録済みリスナー管理
    static listenerAttached = new WeakSet();

    // デフォルトのスマホ対応最小サイズ
    static DEFAULT_MIN_WIDTH = 320;

    static DEFAULT_MIN_HEIGHT = 480;

    // デフォルトの最大サイズ制限
    static DEFAULT_MAX_WIDTH = 1920;

    static DEFAULT_MAX_HEIGHT = 1080;

    // ==================================================
    // ウィンドウサイズを個別指定
    // ==================================================

    static fullScreen(
        root,
        minWidth,
        minHeight,
        maxWidth,
        maxHeight
    ) {

        if (!root) {
            return;
        }

        // サイズ制限を保存
        root.dataset.minWidth = minWidth;
        root.dataset.minHeight = minHeight;
        root.dataset.maxWidth = maxWidth;
        root.dataset.maxHeight = maxHeight;

        // 初回だけリサイズ監視
        if (
            !WindowUtil.listenerAttached.has(
                root
            )
        ) {

            WindowUtil.listenerAttached.add(
                root
            );

            window.addEventListener(
                "resize",
                () =>
                    WindowUtil.forceRelayout(
                        root
                    )
            );
        }

        // レイアウト更新
        WindowUtil.forceRelayout(root);
    }

    // ==================================================
    // デフォルトサイズで全画面
    // ==================================================

    static fullScreen(root) {

        WindowUtil.fullScreen(
            root,
            WindowUtil.DEFAULT_MIN_WIDTH,
            WindowUtil.DEFAULT_MIN_HEIGHT,
            WindowUtil.DEFAULT_MAX_WIDTH,
            WindowUtil.DEFAULT_MAX_HEIGHT
        );
    }

    // ==================================================
    // レイアウト再計算
    // ==================================================

    static forceRelayout(root) {

        if (!root) {
            return;
        }

        const minWidth =
            Number(root.dataset.minWidth);

        const minHeight =
            Number(root.dataset.minHeight);

        const maxWidth =
            Number(root.dataset.maxWidth);

        const maxHeight =
            Number(root.dataset.maxHeight);

        const width = Math.min(
            maxWidth,
            Math.max(
                minWidth,
                window.innerWidth
            )
        );

        const height = Math.min(
            maxHeight,
            Math.max(
                minHeight,
                window.innerHeight
            )
        );

        root.style.width =
            width + "px";

        root.style.height =
            height + "px";
    }

    // ==================================================
    // 強制再レイアウト
    // ==================================================

    static nudge(root) {

        if (!root) {
            return;
        }

        const width = root.offsetWidth;
        const height = root.offsetHeight;

        root.style.width =
            (width - 1) + "px";

        root.style.height =
            (height - 1) + "px";

        requestAnimationFrame(() => {

            root.style.width =
                width + "px";

            root.style.height =
                height + "px";
        });
    }

    // ==================================================
    // Canvasを親要素サイズへ追従
    // ==================================================

    static bindCanvasToRoot(
        canvas,
        root
    ) {

        const resizeCanvas = () => {

            canvas.width =
                root.clientWidth;

            canvas.height =
                root.clientHeight;

            WindowUtil.redraw(canvas);
        };

        resizeCanvas();

        window.addEventListener(
            "resize",
            resizeCanvas
        );
    }

    // ==================================================
    // 再描画
    // ==================================================

    static redraw(canvas) {

        // ここでゲーム側の描画処理を呼ぶ
    }
}

export default WindowUtil;