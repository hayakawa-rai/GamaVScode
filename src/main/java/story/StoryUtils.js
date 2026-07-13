/**
 * JavaFXのStoryUtilsクラスをJavaScript環境に移植したユーティリティクラス
 */
export class StoryUtils {
    
    /**
     * キャラクターの2連続ジャンプ演出
     * JavaFXのKeyFrameの時間（300ms, 700ms, 1000ms, 1400ms）と挙動を忠実に再現しています。
     * 
     * @param {HTMLElement} charView - アニメーションさせるキャラクター要素（imgなど）
     * @param {string} jumpSoundPath - 効果音の音声ファイルパス
     */
    static createJumpAnimation(charView, jumpSoundPath) {
        // 効果音のストップ＆プレイを再現するためのヘルパー
        const playSound = () => {
            try {
                const sound = new Audio(jumpSoundPath);
                sound.volume = 0.2;
                sound.play();
            } catch (e) {
                console.warn("Audio play failed:", e);
            }
        };

        // アニメーション中のトランスレイトをなめらかにするCSS設定
        charView.style.transition = 'transform 0.3s cubic-bezier(0.25, 1, 0.5, 1)';

        // JavaFXのタイムライン（1.4秒間）を再現
        
        // 1回目：300ms時点で最高打点(-90px)に到達 ＆ 音再生
        const t1 = setTimeout(() => {
            playSound();
            charView.style.transform = 'translateY(-90px)';
        }, 300);

        // 1回目着地：700ms時点で元の位置(0px)に戻る
        const t2 = setTimeout(() => {
            charView.style.transition = 'transform 0.4s cubic-bezier(0.55, 0, 1, 0.45)';
            charView.style.transform = 'translateY(0px)';
        }, 700);

        // 2回目：1000ms時点で再び最高打点(-90px)に到達 ＆ 音再生
        const t3 = setTimeout(() => {
            charView.style.transition = 'transform 0.3s cubic-bezier(0.25, 1, 0.5, 1)';
            playSound();
            charView.style.transform = 'translateY(-90px)';
        }, 1000);

        // 2回目着地：1400ms時点で完全に着地
        const t4 = setTimeout(() => {
            charView.style.transition = 'transform 0.4s cubic-bezier(0.55, 0, 1, 0.45)';
            charView.style.transform = 'translateY(0px)';
        }, 1400);

        // JavaFXのTimelineオブジェクトの代わりに、停止（ストップ）できるオブジェクトを返す
        return {
            stop: () => {
                clearTimeout(t1);
                clearTimeout(t2);
                clearTimeout(t3);
                clearTimeout(t4);
                charView.style.transform = 'translateY(0px)';
            }
        };
    }

    /**
     * ▼マークの点滅アニメーション (0.5秒ごとに表示/非表示を切り替え)
     * 
     * @param {HTMLElement} nextMark - 点滅させるターゲット要素
     * @returns {Object} stopメソッドを持ったオブジェクト（Timelineのシミュレート）
     */
    static createBlink(nextMark) {
        let isVisible = true;
        
        const intervalId = setInterval(() => {
            isVisible = !isVisible;
            nextMark.style.visibility = isVisible ? 'visible' : 'hidden';
        }, 500); // 0.5秒ごと

        return {
            stop: () => {
                clearInterval(intervalId);
                nextMark.style.visibility = 'visible'; // 停止時は表示状態に戻す
            }
        };
    }

    /**
     * ▼を上下に揺らすアニメーション (0.5秒で5px移動、自動反転、無限ループ)
     * Web標準の Web Animations API を使用して、JavaFXの autoReverse を完全再現しています。
     * 
     * @param {HTMLElement} target - 上下揺れさせるターゲット要素
     * @returns {Animation} JavaFXのTimelineのように.stop()（JSでは.cancel()）ができるAnimationインスタンス
     */
    static createArrowMove(target) {
        // キーフレーム定義 (0秒時点で0、0.5秒時点で5px)
        const keyframes = [
            { transform: 'translateY(0px)' },
            { transform: 'translateY(5px)' }
        ];

        // アニメーション設定 (0.5秒、無限ループ、自動反転)
        const options = {
            duration: 500,
            iterations: Infinity,
            direction: 'alternate', // これが JavaFXの setAutoReverse(true) に相当します
            easing: 'ease-in-out'
        };

        // アニメーションを開始し、その制御オブジェクトを返す
        const animation = target.animate(keyframes, options);
        
        // JavaFXのインターフェースに合わせるためのラップ
        return {
            stop: () => animation.cancel()
        };
    }
}