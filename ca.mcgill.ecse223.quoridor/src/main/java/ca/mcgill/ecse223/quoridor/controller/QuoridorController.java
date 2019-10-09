package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorController {

	/**
	 * Gherkin feature: Initialize Board
	 * This controller method is responsible for setting the current player to move to the white player. 
	 * It also assigns the white and the black pawn to their initial position and assigns 10 walls to each 
	 * player. Finally, it starts the white player's timer (thinking time).
	 * 
	 * @author             Thomas Philippon
	 * @param game         - New game 
	 * @return void    
	 */
	public static void initializeBoard(Game game) {
		
		throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
	}

	/**
	 * Gherkin feature: Grab Wall
	 * This controller method is responsible for first checking if the current
	 *  player to move has more walls on stock. If the player has more walls, a wall candidate
	 *  object is created at initial position and the method returns 1. If the player has no more walls 
	 *  on stock, no wall candidate move will be created and the method returns 0.
	 *  
	 * @author             Thomas Philippon
	 * @param game         - Current Game 
	 * @return Boolean     - Returns 1 if a wall candidate object was created and 0 if not 
	 */
	public static Boolean grabWall(Game game) {
		
		throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
	}
	
}
