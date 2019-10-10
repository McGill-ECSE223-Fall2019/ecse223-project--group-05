package ca.mcgill.ecse223.quoridor.controller;

import java.io.IOException;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorController {

	/**
	 * NOT IMPLEMENTED. Currently always returns SavingStatus.failed.
	 * This method saves a game into a file whose name is specified, but not its path, and whose game is provided.
	 * It returns true when serialization of the game is successful and false when unsuccessful.
	 * This method is overwrite-averse; it does not overwrite files that already exist.
	 * @author Edwin Pan
	 * @param filename (no extension will be added)
	 * @param game
	 * @return savingStatus enum
	 * @throws IOException
	 */
	public static SavingStatus saveGame(String filename, Game game) throws IOException{
		return SavingStatus.failed;
	}
	/**
	 * NOT IMPLEMENTED. Currently always returns SavingStatus.failed.
	 * This is the overwrite-capable version of saveGame.
	 * This method saves a game into a file whose name is specified, but not its path, and whose game is provided.
	 * It returns true when serialization of the game is successful and false when unsuccessful.
	 * @author Edwin Pan
	 * @param filename (no extension will be added)
	 * @param game
	 * @param overwrite
	 * @return savingStatus enum
	 * @throws IOException
	 */
	public static SavingStatus saveGame(String filename, Game game, boolean overwrite) throws IOException{
		return SavingStatus.failed;
	}
	
	
	
}
