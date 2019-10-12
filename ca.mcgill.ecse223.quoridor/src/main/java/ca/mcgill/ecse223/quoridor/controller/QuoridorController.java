package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorController {
	/**
	 * Ensures that a wall move candidate exists with parameters dir,row,col
	 * Creates one if one does not exist.
	 * @author Matthias Arabian
	 * @return whether or not it succeeded in finding the specified wall move candidate
	 * @throws UnsupportedOperationException
	 */
	public static Boolean GetWallMoveCandidate(String dir, int row, int col) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("GetWallMoveCandidate Error");
	}

	/**
	 * Changes the WallMoveCandidate's direction from Horizontal to Vertical 
	 * and vice versa
	 * @author Matthias Arabian
	 * @returns true if the wall was successfully flipped. false otherwise
	 * @throws UnsupportedOperationException
	 */
	public static Boolean flipWallCandidate() throws UnsupportedOperationException{
		throw new UnsupportedOperationException("flipWallCandidate Error");
	}

	/**
	 * Verifies that the load position is a valid position
	 * @author Matthias Arabian
	 * @return true: position is valid. false otherwise
	 * @throws UnsupportedOperationException
	 */
	public static Boolean CheckThatPositionIsValid() throws UnsupportedOperationException{
		throw new UnsupportedOperationException("Position is valid");
	}

	/**
	 * Loads a saved game by instantiating a new game and populating it with file data
	 * @author Matthias Arabian
	 * @param fileName
	 * @throws UnsupportedOperationException
	 */
	public static void loadSavedGame(String fileName) throws UnsupportedOperationException{
		throw new UnsupportedOperationException("Game could not be loaded");
		
	}


	/**
	 * Propagates a sort of "Invalid Position to Load" error to wherever it is necessary
	 * @author Matthias Arabian
	 * @return whether the error has been successfully propagated
	 * @throws UnsupportedOperationException
	 */
	public static Boolean sendLoadError() throws UnsupportedOperationException{
		throw new UnsupportedOperationException("sendLoadError");
	}
}
