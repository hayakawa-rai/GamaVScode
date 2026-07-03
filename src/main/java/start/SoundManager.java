//効果音調節クラス

package start;

import javafx.scene.media.AudioClip;

public class SoundManager {

	// ワープ効果音
	public static final AudioClip WARP = load("/music/warp.mp3");

	// 敵を倒した時の効果音
	public static final AudioClip ENEMY_DEAD = load("/music/enemydeadsound.mp3");

	// 主人公DEAD時の効果音
	public static final AudioClip DAMAGE = load("/music/syujinkoudeadsound.mp3");

	static {
		// 音量調節
		WARP.setVolume(0.5); // ワープ音
		ENEMY_DEAD.setVolume(0.3); // 敵撃破
		DAMAGE.setVolume(0.1); // ダメージ
	}

	private static AudioClip load(String path) {

		try {

			var url = SoundManager.class.getResource(path);

			System.out.println("SE読込: " + path);
			System.out.println("URL = " + url);

			if (url != null) {
				return new AudioClip(url.toExternalForm());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void play(AudioClip clip) {

		if (clip != null) {
			clip.stop();
			clip.play();
		}
	}
}