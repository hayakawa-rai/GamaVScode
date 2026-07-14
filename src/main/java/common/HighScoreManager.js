// ハイスコアの保存・読み込みを行うクラス
export class HighScoreManager {
    
    // ハイスコア保存先のキー（Javaのファイル名に相当）
    static STORAGE_KEY = "highscore_data";

    /**
     * 指定したステージのハイスコアを読み込む
     * @param {number} stage - ステージ番号
     * @returns {number} ハイスコア
     */
    static loadHighScore(stage) {
        try {
            // localStorageから文字列データを取得
            const data = localStorage.getItem(this.STORAGE_KEY);
            
            if (data) {
                // JSON文字列をJavaScriptのオブジェクトに変換
                const prop = JSON.parse(data);
                
                // stage1 などのキーから値を取得（なければ0）
                return prop[`stage${stage}`] ? parseInt(prop[`stage${stage}`], 10) : 0;
            }
        } catch (e) {
            console.error("ハイスコアの読み込みに失敗しました:", e);
        }
        
        // 読み込み失敗時や未保存時は0を返す
        return 0;
    }

    /**
     * ハイスコア更新
     * @param {number} stage - ステージ番号
     * @param {number} score - 今回のスコア
     * @returns {boolean} 更新されたかどうか
     */
    static updateHighScore(stage, score) {
        try {
            let prop = {};

            // 既存のデータがあれば読み込む
            const data = localStorage.getItem(this.STORAGE_KEY);
            if (data) {
                prop = JSON.parse(data);
            }

            // 現在保存されているハイスコアを取得（なければ0）
            const oldScore = prop[`stage${stage}`] ? parseInt(prop[`stage${stage}`], 10) : 0;

            // 今回のスコアがハイスコアを超えた場合のみ更新
            if (score > oldScore) {
                // 新しいハイスコアをセット
                prop[`stage${stage}`] = score;
                
                // オブジェクトをJSON文字列に変換してlocalStorageへ書き込み
                localStorage.setItem(this.STORAGE_KEY, JSON.stringify(prop));

                return true; // 更新成功
            }
        } catch (e) {
            console.error("ハイスコアの更新に失敗しました:", e);
        }

        // 更新されなかった場合
        return false;
    }
}