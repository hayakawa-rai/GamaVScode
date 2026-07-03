package start;

import javafx.scene.media.AudioClip;

public class SoundManager {

	public static final AudioClip WARP = load("/music/warp.mp3");

	public static final AudioClip ENEMY_DEAD = load("/music/enemydeadsound.mp3");

	public static final AudioClip DAMAGE = load("/music/enemydeadsound2.mp3");

	private static AudioClip load(String path) {

		try {

			var url = SoundManager.class.getResource(path);

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