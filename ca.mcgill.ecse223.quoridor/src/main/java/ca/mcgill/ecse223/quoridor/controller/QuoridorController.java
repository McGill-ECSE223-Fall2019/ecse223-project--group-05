package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorController {
	
	/**
	 * Simple query method for obtaining the player that is currently black. 
	 * There are NO PROTECTIVE MEASURES for ensuring that this method is usable only when
	 * there exists a game and the white player is registered.
	 * @author Edwin Pan
	 * @return Player instance that is black for current game.
	 */
	public static Player getCurrentBlackPlayer() {
		return QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
	}
	
	/**
	 * Simple query method for obtaining the player that is currently white. 
	 * There are NO PROTECTIVE MEASURES for ensuring that this method is usable only when
	 * there exists a game and the white player is registered.
	 * @author Edwin Pan
	 * @return Player instance that is white for current game.
	 */
	public static Player getCurrentWhitePlayer() {
		return QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
	}

	/**
	 * Query method for obtaining the player that is either Black or White. 
	 * Takes string with characters (upper or lower -case) of WHITE or BLACK.
	 * There are NO PROTECTIVE MEASURES for ensuring that the game instance exists
	 * for this method to obtain the player from.
	 * @author Edwin Pan
	 * @param string of either "Black" or "White" in either upper or lower cases.
	 * @return Player instance of the asked color for the current game.
	 * @throws IllegalArgumentException if your color is invalid (not white or black)
	 */
	public static Player getPlayerOfProvidedColorstring(String color) {
		String colorCandidate = color;
		colorCandidate = colorCandidate.trim();
		colorCandidate = colorCandidate.toLowerCase();
		if(colorCandidate.equals("white")) {
			return QuoridorController.getCurrentWhitePlayer();
		}
		else if(colorCandidate.equals("black")) {
			return QuoridorController.getCurrentBlackPlayer();
		}
		else {
			throw new IllegalArgumentException("Received unsupported color argument in QuoridorController.getPlayerOfProvidedColorstring(colorstring)!");
		}
	}
	
	/**
	 * Simple query method which obtains the player whose turn it currently is.
	 * There are NO PROTECTIVE MEASURES for ensuring that this method is usable only
	 * when there exists a live game.
	 * @author Edwin Pan
	 * @return Player instance
	 */
	public static Player getPlayerOfCurrentTurn() {
		return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
	}
	
	/**
	 * PENDING IMPLEMENTATION
	 * Completes the turn of the provided player. Aborts (and thereby returns false) if
	 * the current turn does not belong to the provided player.
	 * @author Edwin Pan
	 * @param player
	 * @return
	 */
	public static boolean completePlayerTurn(Player player) {
		throw new UnsupportedOperationException("QuoridorController.completePlayerturn(player) is not currently implemented!");
	}
	
	/**
	 * PENDING IMPLEMENTATION
	 * GUI modifier method.
	 * Stops the clock of the provided player.
	 * @author Edwin Pan
	 * @param player
	 * @return operation success boolean
	 */
	public static boolean stopPlayerTimer(Player player) {
		throw new UnsupportedOperationException("QuoridorController.playerTimerStop(player) is not currently implemented!");
	}
	
	/**
	 * PENDING IMPLEMENTATION
	 * GUI modifier method.
	 * Lets the clock of the provided player run.
	 * @author Edwin Pan
	 * @param player
	 * @return operation success boolean
	 */
	public static boolean continuePlayerTimer(Player player) {
		throw new UnsupportedOperationException("QuoridorController.playerTimerStop(player) is not currently implemented!");
	}
	
	/**
	 * PENDING IMPLEMENTATION
	 * GUI query method.
	 * Returns whether or not the provided player's clock (from their turn) is running.
	 * @author Edwin Pan
	 * @param player
	 * @return player timer is running boolean
	 */
	public static boolean getPlayerTimerRunning(Player player) {
		throw new UnsupportedOperationException("QuoridorController.playerTimerStop(player) is not currently implemented!");
	}
	
}
