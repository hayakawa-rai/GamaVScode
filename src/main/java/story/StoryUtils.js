/**
 * 純粋な描画・演出用のユーティリティクラス
 */
export class StoryUtils {
    // 話している人物の対応表
    static SPEAKER_KEY_MAP = {
        "仙石さん": "syujinkou",
        "あにき": "aniki",
        "なりなり": "nari",
        "わだたく": "taku"
    };
    // 右側に表示されるキャラ一覧
    static RIGHT_SIDE_KEYS = ["aniki", "nari", "taku"];

    // スマホサイズ判定（一般的なスマホの横幅を想定）
    static NARROW_QUERY = window.matchMedia("(max-width: 480px)");

    // 立ち絵の切り替え（全Story共通）
    static updateCharacterDisplay(speaker, wrappers) {
        const key = StoryUtils.SPEAKER_KEY_MAP[speaker];

        // スマホサイズの場合：喋っている人だけ表示
        if (StoryUtils.NARROW_QUERY.matches) {
            Object.values(wrappers).forEach(el => {
                if (el) el.classList.remove('active');
            });
            if (key && wrappers[key]) {
                wrappers[key].classList.add('active');
            }
            return;
        }

        // ▼通常時の挙動（従来通り）
        if (wrappers.syujinkou) wrappers.syujinkou.classList.add('active');

        if (key && StoryUtils.RIGHT_SIDE_KEYS.includes(key) && wrappers[key]) {
            StoryUtils.RIGHT_SIDE_KEYS.forEach(k => {
                if (k !== key && wrappers[k]) {
                    wrappers[k].classList.remove('active');
                }
            });
            wrappers[key].classList.add('active');
        }
        // speakerが「仙石さん」の場合は右側は直前の状態を維持
    }

    static triggerJumpIfNeeded(d, wrappers, condition = (sound) => !!sound) {
        if (!condition(d.sound)) return;
        const key = StoryUtils.SPEAKER_KEY_MAP[d.speaker];
        const target = key ? wrappers[key] : null;
        if (target) StoryUtils.createJumpAnimation(target, () => {});
    }

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