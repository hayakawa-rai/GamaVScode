package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


// ハイスコアの保存・読み込みを行うクラス
public class HighScoreManager {

	 // ハイスコア保存先ファイル
	private static final Path FILE = Path.of("highscore.properties");

	 // 指定したステージのハイスコアを読み込む
	public static int loadHighScore(int stage) {

		try {

			Properties prop = new Properties();

			// ファイルが存在する場合のみ読み込む
			if (Files.exists(FILE)) {

				prop.load(Files.newInputStream(FILE));

				// stage1=100 のような値を取得
				return Integer.parseInt(prop.getProperty("stage" + stage, "0"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 読み込み失敗時や未保存時は0を返す
		return 0;
	}

	// ハイスコア更新
	public static boolean updateHighScore(int stage, int score) {

		try {

			Properties prop = new Properties();

			// 既存ファイルがあれば読み込む
			if (Files.exists(FILE)) {
				prop.load(Files.newInputStream(FILE));
			}

			// 現在保存されているハイスコアを取得
			int oldScore = Integer.parseInt(prop.getProperty("stage" + stage, "0"));

			// 今回のスコアがハイスコアを超えた場合のみ更新
			if (score > oldScore) {

				// 新しいハイスコアを保存
				prop.setProperty("stage" + stage, String.valueOf(score));
				
				// プロパティファイルへ書き込み
				prop.store(Files.newOutputStream(FILE), "High Score Data");

				return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 更新されなかった場合
		return false;
	}
}