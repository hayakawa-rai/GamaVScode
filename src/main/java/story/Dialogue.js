class Dialogue {
    /**
     * @param {string} speaker - 発言者の名前
     * @param {string} message - セリフの内容
     * @param {string|Audio} sound - 音声ファイルのパス(String) または Audioオブジェクト
     * @param {string} textColor - CSSで使える色（"red", "#FFFFFF", "rgb(255,0,0)" など）
     */
    constructor(speaker, message, sound, textColor) {
        this.speaker = speaker;
        this.message = message;
        
        // JavaFXのAudioClipの代わりとして、Web標準のAudioオブジェクトを生成
        if (typeof sound === 'string') {
            this.sound = new Audio(sound);
        } else {
            this.sound = sound; // すでにAudioオブジェクトが渡された場合はそのまま保持
        }
        
        // JavaFXのColorの代わり。WebではCSSにそのまま渡せる文字列（"#ffffff"など）にする
        this.textColor = textColor;
    }

    // 音声を再生する便利なメソッド（必要に応じて）
    play() {
        if (this.sound && typeof this.sound.play === 'function') {
            // 再生位置を先頭に戻してから再生（JavaFXのAudioClip的な挙動の再現）
            this.sound.currentTime = 0;
            this.sound.play().catch(err => console.log("音声再生エラー:", err));
        }
    }
}