package ca.mcgill.ecse223.quoridor.configuration;

import java.io.File;

public class SaveConfig {

	//Path appended to the user's home directory.
	public static final String userSaveDir = "\\Saved Games\\Quoridor\\";
	
	/**
	 * Produces the complete path for a save file, including the name of the save file itself.
	 * @author Edwin Pan
	 * @param filename of the file itself
	 * @return full file path string
	 */
	public static String getSaveFilePath(String filename) {
		return (System.getProperty("user.home")+ userSaveDir + filename);
	}
	
	/**
	 * Checks to see if the file directory for saves has been created.
	 * @author Edwin Pan
	 * @return boolean for if the file saves folder exists
	 */
	public static boolean checkFileSavesFolderExists() {
		File file = new File( SaveConfig.getSaveFilePath("") );
		return file.exists();
	}
	
	/**
	 * Creates the FileSaves folder for the computer user. If it already exists, does nothing and returns true;
	 * if it does not exist, then it attempts to create the directory, returning true if successful and false if not.
	 * @return boolean folder presence
	 */
	public static boolean createFileSavesFolder() {
		if( SaveConfig.checkFileSavesFolderExists() ) {
			return true;
		}
		else {
			File file = new File( SaveConfig.getSaveFilePath("") );
			return file.mkdir();
		}
	}
	
}
