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
	
	private static final String		userHomeDir					= System.getProperty("user.home") + "\\";
	private static final String		userDefaultSavesDir			= "Saved Games\\Quoridor\\";
	private static final boolean	useUserHome					= false;
	private static final String		baseDir						= useUserHome ? userHomeDir + userDefaultSavesDir : "" ;		//The "root" as far as the application is concerned during runtime.
	private static final String		runtimeDataFolderName		= "env\\";								//Folder for things the user shouldn't ever touch
	private static final String		gameSavesDataFolderName		= "saves\\";								//Folder of the saves that the user can use to reinstantiate games
	public static final String		runtimeDataExtension		= ".qdata";									//Extension for serialized runtime data
	public static final String		gameSavesDataExtension 		= ".qsave";									//Extension for data of individual games
	public static final String		usersDatabaseFilename		= "users" + runtimeDataExtension;			//Name of file which contains serialized Quoridor, User, Board, and Tiles.
	public static final String		autosaveFilename			= "snapshot" + runtimeDataExtension;		//Name of file which contains a complete serialized dataset of the full quoridor application automatically.
	public static final String		localSettingsFilename		= "settings" + runtimeDataExtension;		//Name of file which contains peripheral data to do with non-model related settings, such as the GUI.
	
	
	
	/*
	 * ==================================================================================================================
	 *	DEBUG SWITCH
	 * ==================================================================================================================
	 */
	private static final boolean enableDebugging = false;
	
	
	
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
		return createGameSavesFolder() && createAppDataFolder() ;
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
	public static String getRuntimeDataFolder() {
		return ( baseDir + runtimeDataFolderName );
	}
	
	/**
	 * Produces the path to the application's runtime model data file containing data about the users, quoridor itself, but not of particular games.
	 * @return String of the from-root path to the users.qdata file.
	 */
	public static String getUserDatabaseFilePath() {
		return ( SaveConfig.getRuntimeDataFolder() + usersDatabaseFilename );
	}
	
	/**
	 * Produces the path to the application's autosave model data, which contains a snapshott of the application's full system at a particular moment.
	 * @return String of the from-root path to the snapshot.qdata file.
	 */
	public static String getAutosaveFilePath() {
		return ( SaveConfig.getRuntimeDataFolder() + autosaveFilename );
	}
	
	/**
	 * Produces the path to the application's application settings file.
	 * @return String of the from-root path to the settings.qdata file.
	 */
	public static String getAppSettingsFilePath() {
		return ( SaveConfig.getRuntimeDataFolder() + localSettingsFilename );
	}
	
	/**
	 * Checks to see if the app data folder exists.
	 * @return boolean
	 */
	public static boolean checkAppDataFolderExists() {
		File file = new File ( SaveConfig.getRuntimeDataFolder() );
		return file.exists();
	}
	
	/**
	 * Creates the app data folder. Returns true if it ends up existing and false if it fails to create the directory.
	 * @return
	 */
	public static boolean createAppDataFolder() {
		if( SaveConfig.checkAppDataFolderExists() ) {
			if(enableDebugging)	{
				System.out.println("createAppDataFolder() succesfully executed: found that folder already exists and returning true.");
			}
			return true;
		} else {
			File file = new File( SaveConfig.getRuntimeDataFolder() );
			if(enableDebugging) {
				System.out.println("createAppDataFolder() detected the folder is not pre-existing.");
				boolean success = file.mkdir();
				System.out.println("createAppDataFolder() instantiated File instance of directory " + file.getAbsolutePath());
				System.out.println("outputing if createAppDataFolder() was able to turn this File instance into a real directory : " + success );
				/*
				SecurityManager securityManager = new SecurityManager();
				try {
					securityManager.checkRead(SaveConfig.getRuntimeDataFolder());
					securityManager.checkWrite(SaveConfig.getRuntimeDataFolder());
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				*/
				return success;
			}
			return file.mkdir();
		}
	}
	
	
	/*
	 * ==================================================================================================================
	 * 	INDIVIDUAL SAVES DATA QUERIES AND MODIFIERS
	 * ==================================================================================================================
	 */
	
	/**
	 * Produces the complete path for the folder for game save files
	 * @return
	 */
	public static String getGameSavesFolder() {
		return (baseDir + gameSavesDataFolderName);
	}
	
	/**
	 * Produces the complete path for a save file, including the name of the save file itself.
	 * @param filename of the file itself
	 * @return full file path string
	 */
	public static String getGameSaveFilePath(String filename) {
		return ( getGameSavesFolder() + filename);
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
			if(enableDebugging)	{
				System.out.println("createGameSavesFolder() succesfully executed: found that folder already exists and returning true.");
			}
			return true;
		}
		else {
			File file = new File( SaveConfig.getGameSavesFolder() );
			if(enableDebugging) {
				System.out.println("createGameSavesFolder() detected the folder is not pre-existing.");
				System.out.println("createGameSavesFolder() instantiated File instance of directory " + file.getAbsolutePath() + ".");
				boolean success = file.mkdir();
				System.out.println("outputing if createGameSavesFolder() was able to turn this File instance into a real directory : " + success );
				return success;
			}
			return file.mkdir();
		}
	}
	
}
