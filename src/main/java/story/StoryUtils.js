/**
 * 純粋な描画・演出用のユーティリティクラス
 */
export class StoryUtils {

    // ジャンプアニメーション（2段ジャンプを維持）
    static createJumpAnimation(charView, playAudio) {
        // リセット
        charView.style.transition = 'none';
        charView.style.transform = 'translateY(0px)';
        void charView.offsetWidth; // 強制リフロー

        const height = '-50px'; 
        const upDuration = '0.2s'; 
        const downDuration = '0.25s';

        // 1回目のジャンプ開始
        const t1 = setTimeout(() => {
            charView.style.transition = `transform ${upDuration} cubic-bezier(0.25, 1, 0.5, 1)`;
            charView.style.transform = `translateY(${height})`;
            if (playAudio) playAudio(); 
        }, 0);

        // 1回目着地
        const t2 = setTimeout(() => {
            charView.style.transition = `transform ${downDuration} cubic-bezier(0.55, 0, 1, 0.45)`;
            charView.style.transform = 'translateY(0px)';
        }, 200);

        // 2回目のジャンプ開始
        const t3 = setTimeout(() => {
            charView.style.transition = `transform ${upDuration} cubic-bezier(0.25, 1, 0.5, 1)`;
            charView.style.transform = `translateY(${height})`;
        }, 500);

        // 2回目着地
        const t4 = setTimeout(() => {
            charView.style.transition = `transform ${downDuration} cubic-bezier(0.55, 0, 1, 0.45)`;
            charView.style.transform = 'translateY(0px)';
        }, 700);

        return {
            stop: () => {
                [t1, t2, t3, t4].forEach(clearTimeout);
                charView.style.transform = 'translateY(0px)';
            }
        };
    }

    // ▼マークの点滅
    static createBlink(nextMark) {
        let isVisible = true;
        const intervalId = setInterval(() => {
            isVisible = !isVisible;
            nextMark.style.visibility = isVisible ? 'visible' : 'hidden';
        }, 500);
        return { stop: () => { clearInterval(intervalId); nextMark.style.visibility = 'visible'; } };
    }

    // ▼マークの上下揺れ
    static createArrowMove(target) {
        const animation = target.animate(
            [{ transform: 'translateY(0px)' }, { transform: 'translateY(5px)' }],
            { duration: 500, iterations: Infinity, direction: 'alternate', easing: 'ease-in-out' }
        );
        return { stop: () => { animation.cancel(); } };
    }
}