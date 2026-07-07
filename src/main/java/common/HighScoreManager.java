package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class HighScoreManager {

	private static final Path FILE = Path.of("highscore.properties");

	// ハイスコア読込
	public static int loadHighScore(int stage) {

		try {

			Properties prop = new Properties();

			if (Files.exists(FILE)) {

				prop.load(Files.newInputStream(FILE));

				return Integer.parseInt(prop.getProperty("stage" + stage, "0"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	// ハイスコア更新
	public static boolean updateHighScore(int stage, int score) {

		try {

			Properties prop = new Properties();

			if (Files.exists(FILE)) {
				prop.load(Files.newInputStream(FILE));
			}

			int oldScore = Integer.parseInt(prop.getProperty("stage" + stage, "0"));

			if (score > oldScore) {

				prop.setProperty("stage" + stage, String.valueOf(score));
				prop.store(Files.newOutputStream(FILE), "High Score Data");

				return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
}