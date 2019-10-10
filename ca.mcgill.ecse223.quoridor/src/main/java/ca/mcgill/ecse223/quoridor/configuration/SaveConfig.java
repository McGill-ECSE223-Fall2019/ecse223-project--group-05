package ca.mcgill.ecse223.quoridor.configuration;

public class SaveConfig {

	public static final String userSaveDir = "\\Saved Games\\Quoridor\\";
	public static String getSaveFilePath(String filename) {
		return (System.getProperty("user.home")+ userSaveDir + filename);
	}
	
}
