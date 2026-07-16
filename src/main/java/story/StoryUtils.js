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


    // 話している人物の対応表
    static SPEAKER_KEY_MAP = {
        "仙石さん": "syujinkou",
        "あにき": "aniki",
        "なりなり": "nari",
        "わだたく": "taku"
    };
    // 右側に表示されるキャラ一覧
    static RIGHT_SIDE_KEYS = ["aniki", "nari", "taku"];

    // 立ち絵の切り替え（全Story共通）
    static updateCharacterDisplay(speaker, wrappers) {
        // 主人公は常に表示
        if (wrappers.syujinkou) wrappers.syujinkou.classList.add('active');

        const key = StoryUtils.SPEAKER_KEY_MAP[speaker];

        // 話者が右側キャラ(あにき/なりなり/わだたく)のときだけ切り替え処理を行う
        if (key && StoryUtils.RIGHT_SIDE_KEYS.includes(key) && wrappers[key]) {
            // 現在表示中のキャラと違う場合のみ切り替える
            StoryUtils.RIGHT_SIDE_KEYS.forEach(k => {
                if (k !== key && wrappers[k]) {
                    wrappers[k].classList.remove('active');
                }
            });
            wrappers[key].classList.add('active');
        }
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