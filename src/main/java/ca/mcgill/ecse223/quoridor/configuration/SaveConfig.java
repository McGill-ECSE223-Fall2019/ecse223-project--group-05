package ca.mcgill.ecse223.quoridor.configuration;

import java.io.File;

/**
 * Static Class which contains helper methods and constants to do with WHERE data is saved in the system. 
 * String constants about directories and file extensions are found here.
 * @author Edwin Pan
 */
public class SaveConfig {

	/*
	 * ==================================================================================================================
	 *	SAVE CONFIGURATION CONSTANTS
	 * ==================================================================================================================
	 */
	
	public static final String userAppDataDir 			= "\\Saves Games\\Quoridor\\appdata\\";		//Folder for things the user shouldn't ever touch
	public static final String userGameSavesDir 		= "\\Saved Games\\Quoridor\\saves\\";		//Folder of the saves that the user can use to reinstantiate games
	public static final String appDataExtension 		= ".qdata";									//Extension for serialized runtime data
	public static final String gameSaveExtension 		= ".qsave";									//Extension for data of individual games
	public static final String usersDatabaseFilename	= "users" + appDataExtension;				//Name of file which contains serialized Quoridor, User, Board, and Tiles.
	public static final String autosaveFilename			= "snapshot" + appDataExtension;			//Name of file which contains a complete serialized dataset of the full quoridor application automatically.
	
	
	/*
	 * ==================================================================================================================
	 *	GENERAL SAVE FUNCTIONALITIES
	 * ==================================================================================================================
	 */
	
	/**
	 * Sets up folders for data about the game. Returns true if succesful; false if not.
	 * @return
	 */
	public static boolean setupSaveDirectories() {
		return createAppDataFolder() && createGameSavesFolder() ;
	}
	
	
	/*
	 * ==================================================================================================================
	 *	APPLICATION DATA QUERIES AND MODIFIERS
	 * ==================================================================================================================
	 */
	
	/**
	 * Produces the path to the folder for runtime model data.
	 * @return String of the from-root path to the appdata folder.
	 */
	public static String getAppDataFolderPath() {
		return ( System.getProperty("user.home") + userAppDataDir );
	}
	/**
	 * Produces the path to the application's runtime model data file containing data about the users, quoridor itself, but not of particular games.
	 * @return String of the from-root path to the users.qdata file.
	 */
	public static String getUserDatabaseFilePath() {
		return ( SaveConfig.getAppDataFolderPath() + usersDatabaseFilename );
	}
	/**
	 * Produces the path to the application's autosave model data, which contains a snapshott of the application's full system at a particular moment.
	 * @return String of the from-root path to the snapshot.qdata file.
	 */
	public static String getAutosaveFilePath() {
		return ( SaveConfig.getAppDataFolderPath() + autosaveFilename );
	}
	
	/**
	 * Checks to see if the app data folder exists.
	 * @return boolean
	 */
	public static boolean checkAppDataFolderExists() {
		File file = new File ( SaveConfig.getAppDataFolderPath() );
		return file.exists();
	}
	
	/**
	 * Creates the app data folder. Returns true if it ends up existing and false if it fails to create the directory.
	 * @return
	 */
	public static boolean createAppDataFolder() {
		if( SaveConfig.checkAppDataFolderExists() ) {
			return true;
		} else {
			File file = new File( SaveConfig.getUserDatabaseFilePath() );
			return file.mkdir();
		}
	}
	
	
	/*
	 * ==================================================================================================================
	 * 	INDIVIDUAL SAVES DATA QUERIES AND MODIFIERS
	 * ==================================================================================================================
	 */
	
	/**
	 * Produces the complete path for a save file, including the name of the save file itself.
	 * @param filename of the file itself
	 * @return full file path string
	 */
	public static String getGameSaveFilePath(String filename) {
		return (System.getProperty("user.home")+ userGameSavesDir + filename);
	}
	
	/**
	 * Checks to see if the file directory for saves has been created.
	 * @return boolean for if the file saves folder exists
	 */
	public static boolean checkGameSavesFolderExists() {
		File file = new File( SaveConfig.getGameSaveFilePath("") );
		return file.exists();
	}
	
	/**
	 * Creates the FileSaves folder for the computer user. If it already exists, does nothing and returns true;
	 * if it does not exist, then it attempts to create the directory, returning true if successful and false if not.
	 * @return boolean folder presence
	 */
	public static boolean createGameSavesFolder() {
		if( SaveConfig.checkGameSavesFolderExists() ) {
			return true;
		}
		else {
			File file = new File( SaveConfig.getGameSaveFilePath("") );
			return file.mkdir();
		}
	}
	
}
