package start;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Bgm   {

    private static MediaPlayer bgmPlayer;

    public static void playBGM(String path) {
        if (bgmPlayer != null) return; // すでに再生中なら何もしない

        Media bgm = new Media(
            Bgm.class.getResource(path).toExternalForm()
        );

        bgmPlayer = new MediaPlayer(bgm);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.2);
        bgmPlayer.play();
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer = null;
        }
    }
}