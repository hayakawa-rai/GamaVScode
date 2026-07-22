/**
 * ResponsiveUtil.js
 *
 * スマホ～PCまで画面サイズに応じてUIを追従させるための
 * 共通レスポンシブユーティリティ。
 */

export class ResponsiveUtil {

    /**
     * コンストラクタ封印
     * Java版の utility class と同じ考え方
     */
    constructor() {
        throw new Error(
            "ResponsiveUtilはインスタンス化できません"
        );
    }

    /**
     * 値を min ～ max の範囲に収める
     *
     * Java:
     * Math.max(min, Math.min(value, max))
     *
     * @param {number} min
     * @param {number} value
     * @param {number} max
     * @returns {number}
     */
    static clamp(min, value, max) {
        return Math.max(
            min,
            Math.min(value, max)
        );
    }

    /**
     * 要素の最大幅を
     * 画面幅 × ratio
     * に追従させる
     *
     * Java:
     * bindMaxWidth(...)
     *
     * @param {HTMLElement} element
     * @param {number} ratio
     */
    static bindMaxWidth(element, ratio) {

        const update = () => {

            element.style.maxWidth =
                `${window.innerWidth * ratio}px`;
        };

        update();

        window.addEventListener(
            "resize",
            update
        );
    }

    /**
     * ImageView の fitWidth 相当
     *
     * Java:
     * bindImageFitWidth(...)
     *
     * @param {HTMLImageElement} image
     * @param {number} ratio
     */
    static bindImageFitWidth(image, ratio) {

        const update = () => {

            image.style.width =
                `${window.innerWidth * ratio}px`;

            image.style.height =
                "auto";
        };

        update();

        window.addEventListener(
            "resize",
            update
        );
    }

    /**
     * 要素サイズを
     * 画面サイズ比率に追従させる
     *
     * Java:
     * bindPrefSize(...)
     *
     * @param {HTMLElement} element
     * @param {number} widthRatio
     * @param {number} heightRatio
     * @param {number} minW
     * @param {number} minH
     */
    static bindPrefSize(
        element,
        widthRatio,
        heightRatio,
        minW,
        minH
    ) {

        const update = () => {

            const width =
                Math.max(
                    minW,
                    window.innerWidth * widthRatio
                );

            const height =
                Math.max(
                    minH,
                    window.innerHeight * heightRatio
                );

            element.style.width =
                `${width}px`;

            element.style.height =
                `${height}px`;
        };

        update();

        window.addEventListener(
            "resize",
            update
        );
    }

    /**
     * ボタンのフォントサイズと余白を
     * 画面幅に応じて動的変更
     *
     * Java:
     * bindButtonFontAndPadding(...)
     *
     * @param {HTMLButtonElement} button
     * @param {number} fontMin
     * @param {number} fontRatio
     * @param {number} fontMax
     * @param {number} padMin
     * @param {number} padRatio
     * @param {number} padMax
     */
    static bindButtonFontAndPadding(
        button,
        fontMin,
        fontRatio,
        fontMax,
        padMin,
        padRatio,
        padMax
    ) {

        const update = () => {

            const width =
                window.innerWidth;

            // フォントサイズ
            const fontSize =
                ResponsiveUtil.clamp(
                    fontMin,
                    width * fontRatio,
                    fontMax
                );

            // 横パディング
            const paddingH =
                ResponsiveUtil.clamp(
                    padMin,
                    width * padRatio,
                    padMax
                );

            button.style.fontSize =
                `${fontSize}px`;

            button.style.padding =
                `8px ${paddingH}px`;
        };

        update();

        window.addEventListener(
            "resize",
            update
        );
    }
}