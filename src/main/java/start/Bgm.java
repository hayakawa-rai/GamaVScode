package start;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Bgm {
    private static MediaPlayer bgmPlayer;
    private static String currentPath;
    private static int currentStageNumber = 1; // ★追加：現在のステージ番号を記憶

    public static void playBGM(String path) {
        if (bgmPlayer != null && path.equals(currentPath)) return;
        stopBGM();

        URL url = Bgm.class.getResource(path);
        if (url == null) {
            System.err.println("BGMファイルが見つかりません: " + path);
            return;
        }

        Media bgm = new Media(url.toExternalForm());
        bgmPlayer = new MediaPlayer(bgm);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        bgmPlayer.setVolume(0.2);
        bgmPlayer.play();
        currentPath = path;
    }

    // ★追加：ステージ番号からBGMパスを決定して再生（ステージ開始時にも、FEVER終了後の復帰にも使う）
    public static void playStageBGM(int stageNumber) {
        currentStageNumber = stageNumber;
        switch (stageNumber) {
            case 1: playBGM("/music/stage1_bgm.mp3"); break;
            case 2: playBGM("/music/stage2_bgm.mp3"); break;
            case 3: playBGM("/music/stage3_bgm.mp3"); break;
            default: playBGM("/music/stage1_bgm.mp3"); break;
        }
    }

    // ★追加：FEVER開始
    public static void playFeverBGM() {
        playBGM("/music/feverbgm.mp3");
    }

    // ★追加：FEVER終了→元のステージBGMに復帰
    public static void stopFeverBGM() {
        playStageBGM(currentStageNumber);
    }

    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
            currentPath = null;
        }
    }

    public static void pauseBGM() {
        if (bgmPlayer != null) bgmPlayer.pause();
    }

    public static void resumeBGM() {
        if (bgmPlayer != null) bgmPlayer.play();
    }
}