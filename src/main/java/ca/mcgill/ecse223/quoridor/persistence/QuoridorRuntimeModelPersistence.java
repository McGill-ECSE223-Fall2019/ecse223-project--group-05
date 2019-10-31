package ca.mcgill.ecse223.quoridor.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.User;

/**
 * 
 * Persists runtime instances of the model into save files. In conjunction with paths provided by SaveConfig.java, QuoridorRuntimePersistence produces full-memory snapshots and the local game's user database into the local disk in the form of text files.
 * Note that this QuoridorRuntimePersistence does not save games for the purpose of game saving for future replays, which is the job of QuoridorSavesManager. QuoridorRuntimePersistence's job is to give autosave functionality automatically
 * during the quoridor games through a quicksave API that can afterwards be reloaded in, for example, the case of a crash. It also gives functionality for saving the list of all users on a local quoridor application to allow for this list of
 * users to be re-instantiated once the application has been turned off through a users API.
 * 
 * @author Edwin Pan
 *
 */
public class QuoridorRuntimeModelPersistence {
		
	/*
	 * ==================================================================================================
	 * QUICKSAVE API
	 * ==================================================================================================
	 */
	
	/**
	 * Saves all of the application (through QuoridorApplication.java)'s current data model onto disk.
	 * Useful for saving a snapshot of an instance of the application.
	 * Should be mainly used for autosaving purposes as it quicksaves overwrite previous quicksaves.
	 * @param quoridor
	 */
	public static void quicksave() {
		save( 
			QuoridorApplication.getQuoridor(), 
			SaveConfig.getAutosaveFilePath() 
		);
	}
	
	/**
	 * Returns the saved quicksave application snapshot in instantiated Quoridor form.
	 * @return quoridor
	 */
	public static Quoridor quickload() {
		return load( SaveConfig.getAutosaveFilePath() );
	}
	

	
	/*
	 * ==================================================================================================
	 * USERDATA API
	 * ==================================================================================================
	 */
	
	/**
	 * Saves data of the current application to do with Quoridor itself, and most important the users.
	 * Most significantly, it ignores the currentGame; but it keeps Board and Tile which are universal.
	 * Accesses the Quoridor Application through the QuoridorApplication.java.
	 */
	public static void saveUserData() {
		save(	
			pruneOfGame( QuoridorApplication.getQuoridor() ),	
			SaveConfig.getUserDatabaseFilePath()	
		);
	}
	
	/**
	 * Returns the saved database of users in the form of a Quoridor instance loaded with the users and
	 * the board and its tile, if the users have been previously saved.
	 * @return
	 */
	public static Quoridor loadUserData() {
		return load( SaveConfig.getUserDatabaseFilePath() );
	}
	
	
	
	/*
	 * ==================================================================================================
	 * HELPERS
	 * ==================================================================================================
	 */
	
	/**
	 * Returns a modified clone of the provided quoridor instance minus the current game it contains.
	 * Useful for instantiating an application state without a game but that keeps the users.
	 * @param quoridor instance to prune
	 * @return pruned quoridor instance
	 */
	private static Quoridor pruneOfGame( Quoridor quoridor ) {
		Quoridor quoridorWithJustUsers = new Quoridor();
		for( User u: quoridor.getUsers() ) {
			quoridorWithJustUsers.addUser(u);
		}
		quoridorWithJustUsers.setBoard( quoridor.getBoard() );
		return quoridorWithJustUsers;
	}
	
	/**
	 * Saves the provided instance of Quoridor into an serialized java object output stream.
	 * Throws runtime errors if there is a FileNotFoundException or IOException.
	 * @param quoridor
	 */
	private static void save( Quoridor quoridor , String path) {
		
		FileOutputStream fis; 
		try{
			fis = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		ObjectOutputStream ois ;
		try{
			ois = new ObjectOutputStream(fis);
			ois.writeObject(quoridor);
			ois.flush();
			ois.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	/**
	 * Loads the set file of quoridor data and returns it.
	 * Throws runtime errors if there is a FileNotFoundException or IOException.
	 * @return quoridor instance with data.
	 */
	private static Quoridor load(String path) {
		
		FileInputStream fis; 
		try{
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		ObjectInputStream ois ;
		Quoridor quoridor;
		try{
			ois = new ObjectInputStream(fis);
			try {
				quoridor = (Quoridor)( ois.readObject() );
				ois.close();
			} catch (ClassNotFoundException e) {
				ois.close();
				throw new RuntimeException("File \"" + path + "\" was not able to be read.", e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return quoridor;
		
	}
	
	
}
