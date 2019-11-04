package ca.mcgill.ecse223.quoridor.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;

/**
 * 
 * Class in charge of saving local settings of the application which is persisted into an appdata file.
 * Settings to do with, for example, the GUI, would be persisted into the file system through this class.
 * This class also persists certain behavioural patterns. For example, this manager reads and writes the
 * settings for whether or not the application will automatically continue from a quicksaved game or start
 * fresh with the previously added users.
 * 
 * @author ALPHA Agent_BENNETTE
 *
 */
public class QuoridorSettingsManager {
	
	/*
	 * ==================================================================================================
	 * 	CLASS VARIABLES
	 * ==================================================================================================
	 */
	private static boolean settingsLoaded = false;
	private static boolean settingsFailedToLoadProperly = false;
	
	
	/*
	 * ==================================================================================================
	 * SETTINGS FOR QUORIDOR LOCAL QUORIDOR SYSTEM AND THEIR TAGS
	 * ==================================================================================================
	 */
	private static final String		resumePreviousGameKey	=	"gameresumption";
	private static boolean 			resumePreviousGame 		=	false;
	
	
	/*
	 * ==================================================================================================
	 * LOAD METHOD
	 * ==================================================================================================
	 */
	private static void load() throws IOException {
		File settingsFile = new File( SaveConfig.getAppSettingsFilePath() );
			
		//Reader
		BufferedReader settingsReader;
		try {
			settingsReader = new BufferedReader( new FileReader(settingsFile) );
		} catch( FileNotFoundException e ) {
			settingsLoaded = true;
			return;
		}
		
		//Reading each element into a dynamic matrix of String. 
		//The first element read is supposed to be the key; the second element is the value.
		ArrayList<ArrayList<String>> settingsMatrix = new ArrayList<ArrayList<String>>();
		String stringBuffer = settingsReader.readLine().trim();
		while( stringBuffer != null ) {
			//Produce a settings entry by reading in each word.
			ArrayList<String> settingsEntry = new ArrayList<String>();
			int splitterIndex = stringBuffer.indexOf(" ");
			while( splitterIndex != -1 ) {
				settingsEntry.add( stringBuffer.substring(0,splitterIndex) );
				stringBuffer = stringBuffer.substring( splitterIndex + 1 , stringBuffer.length() ).trim();
				splitterIndex = stringBuffer.indexOf(" ");
			}
			if(stringBuffer.length() != 0 ) {
				settingsEntry.add(stringBuffer);
			}
			settingsMatrix.add(settingsEntry);
			stringBuffer = settingsReader.readLine().trim();
		}
		settingsReader.close();
		
		//READ IN EACH INDIVIDUAL SETTING (HARDCODED)
		int foundelementindex = -1;
		//resumingPreviousGame
		for( int i = 0 ; i < settingsMatrix.size() ; i++ ) {
			if( settingsMatrix.get(i).get(0).equals(resumePreviousGameKey) ) {
				foundelementindex = i;
				break;
			}
		}
		if( foundelementindex != -1 ) {
			resumePreviousGame = !( settingsMatrix.get(foundelementindex).get(1).charAt(0) == '0' );
			settingsMatrix.remove(foundelementindex);
			foundelementindex = -1;
		}
		
		//Wrap up
		settingsLoaded = true;
			
	}
	
	
	/*
	 * ==================================================================================================
	 * QUERY METHODS
	 * ==================================================================================================
	 */
	
	/**
	 * Returns whether or not the settings instruct the game to immediately resume the quicksaved game.
	 * If no settings can be read, then the default FALSE value is returned.
	 * @return
	 */
	public static boolean checkIfToResumePreviousGame() {
		attemptSettingsLoading();
		return resumePreviousGame;
	}
	
	/**
	 * Returns whether settings were able to be read properly.
	 * @return
	 */
	public static boolean checkSettingsLoadedProperly() {
		return !settingsFailedToLoadProperly;
	}
	
	/**
	 * Helper method which tries to ensure that if there were settings, that it was read properly.
	 * Used at the head of every settings query method
	 */
	private static void attemptSettingsLoading() {
		if( !settingsLoaded || settingsFailedToLoadProperly ) {
			try {
				load();
				settingsFailedToLoadProperly = false;
			} catch (IOException e) {
				settingsLoaded = true;
				settingsFailedToLoadProperly = true;
			}
		}
	}
	
	
	/*
	 * ==================================================================================================
	 * FILE SYSTEM SAVING METHODS
	 * ==================================================================================================
	 */
	
	/**
	 * Saves the current settings as observed by this QuoridorSettingsManager into the file system.
	 * @throws IOExceptions.
	 */
	public static void saveSettings() throws IOException {
		//Setup
		File settingsFile = new File( SaveConfig.getAppSettingsFilePath() );
		BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(settingsFile) );
		//Actual writing of settings (Hardcode each one)
		bufferedWriter.write( resumePreviousGameKey + " " + (resumePreviousGame ? "1" : "0") + "\n" );
		//Wrap up.
		bufferedWriter.close();
	}
	
}
