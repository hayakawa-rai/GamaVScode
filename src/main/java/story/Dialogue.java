package story;

import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

public class Dialogue {
    public String speaker;
    public String message;
    public AudioClip sound;
    public Color textColor;

    public Dialogue(String speaker, String message, AudioClip sound, Color textColor) {
        this.speaker = speaker;
        this.message = message;
        this.sound = sound;
        this.textColor = textColor;
    }
}