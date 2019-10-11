package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorController {
	
	public QuoridorController() {
		
	}
	/**
	 * get the wallMove object associated with the current game and current player with the required orientation, row, and column.
	 * If no such wallMove object exist, (i.e. there is no such a wall with such a position and orientation), a new object will be created
	 * and linkedin added to model. 
	 * @param dir orientation of the wall "vertical","horizontal"
	 * @param row row number of tile northwest of centrepoint of wall
	 * @param column column number
	 * @throws Throwable
	 */
	public static void getWallMove(String dir, int row, int column) throws Throwable{
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  
	 * @param side the side to check for edges. "left", "right", "up", "down"
	 * @throws Throwable
	 * @return true if the current wall selected is at the edge specified in the parameter
	 */
	public static boolean wallIsAtEdge(String side) throws Throwable{
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  moves the wall one tile toward the direction specified. An illegal move notification will be shown
	 *  if such a move in illegal
	 * @param side the direction at which we move the wall. "left", "right", "up", "down"
	 * @throws Throwable
	 * 
	 */
	public static void moveWall(String side) throws Throwable{
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  Each player is given a fixed time limit for a game. This method changes the remaining thinking
	 *  time of each player
	 * @param min the minute part of the time
	 * @param second the second part of the time
	 * @throws Throwable
	 * 
	 */
	public static void setThinkingTime(int min, int sec) {
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  Communicates to View to check if a Illegal Move Notification is being displayed
	 * @throws Throwable
	 * @return true if notification displayed, false otherwise
	 * 
	 */
	public static boolean isIllegalMoveNotificationDisplayed() {
		throw new java.lang.UnsupportedOperationException();
	}
	
	
	

	

	
}
