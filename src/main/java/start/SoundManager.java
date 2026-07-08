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
	// フルーツ取得時の効果音 
	public static final AudioClip FRUIT_EAT = load("/music/fruiteatsound.mp3");
	// パワーエサ取得時の効果音
	public static final AudioClip POWER_EAT = load("/music/powerup.mp3");
	//ゲームオーバー画面表示時の効果音
	public static final AudioClip GAMEOVER = load("/music/gameover.mp3");
	//リトライ選択時の効果音
	public static final AudioClip RETRY = load("/music/retry.mp3");
	//ボタン選択時の効果音（Startクラスと同じ select.mp3 を共通利用）
	public static final AudioClip SELECT = load("/music/select.mp3");

	// 音量調節
	static {
		WARP.setVolume(0.5); // ワープ音
		ENEMY_DEAD.setVolume(0.3); // 敵撃破
		DAMAGE.setVolume(0.1); // ダメージ
		FRUIT_EAT.setVolume(0.4); // フルーツ取得 
		POWER_EAT.setVolume(0.5);//パワーエサ取得
		GAMEOVER.setVolume(0.5);//ゲームオーバー
		RETRY.setVolume(0.4);//リトライ
		SELECT.setVolume(0.4);//セレクト
	}

	// 指定したパスの効果音を読み込む
	private static AudioClip load(String path) {
		try {
			var url = SoundManager.class.getResource(path);
			// デバッグ用ログ
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

	// 効果音を再生する
	public static void play(AudioClip clip) {
		if (clip != null) {
			// 同じ効果音を連続再生できるよう一度停止
			clip.stop();
			// 効果音再生
			clip.play();

		}
	}
}