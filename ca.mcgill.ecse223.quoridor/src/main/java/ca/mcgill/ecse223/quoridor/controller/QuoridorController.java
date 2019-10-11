package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

public class QuoridorController {
	
	public QuoridorController() {
		
	}
	/**
	 * get the wallMove object associated with the current game and current player with the required orientation, row, and column.
	 * If no such wallMove object exist, (i.e. there is no such a wall with such a position and orientation), a new object will be created
	 * and links added to model. (feature:move wall) 
	 * 
	 * @author David
	 * @param dir orientation of the wall "vertical","horizontal"
	 * @param row row number of tile northwest of centrepoint of wall
	 * @param column column number
	 * @throws Throwable
	 */
	public static void getWallMove(String dir, int row, int column) throws Throwable{
		throw new java.lang.UnsupportedOperationException();
	}
	/**(feature: move wall)
	 * 
	 * @author David 
	 * @param side the side to check for edges. "left", "right", "up", "down"
	 * @throws Throwable
	 * @return true if the current wall selected is at the edge specified in the parameter
	 */
	public static boolean wallIsAtEdge(String side) throws Throwable{
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  moves the wall one tile toward the direction specified. An illegal move notification will be shown
	 *  if such a move in illegal. This involves linking wallMove object to a new target tile(feature: move wall)
	 *  @author David
	 * @param side the direction at which we move the wall. "left", "right", "up", "down"
	 * @throws Throwable
	 * 
	 */
	public static void moveWall(String side) throws Throwable{
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  Each player is given a fixed time limit for a game. This method changes the remaining thinking
	 *  time of each player (feature: set total thinking time)
	 * @author David
	 * @param min the minute part of the time
	 * @param second the second part of the time
	 * @throws Throwable
	 * 
	 */
	public static void setThinkingTime(int min, int sec) {
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  Optional feature: Set thinking time for a specific player. 
	 *  We know that both players are supposed to have the same thinking time. 
	 *  However, being able to set time for individual players can to increase the difficulty for a more experienced player without affecting the other opponent. 
	 *  Each player is given a fixed time limit for a game. This method changes the remaining thinking
	 *  time of each player 
	 *  @author David
	 * @param min the minute part of the time
	 * @param second the second part of the time
	 * @param playerIndex specifies for whom this thinking time is for
	 * @throws Throwable
	 * 
	 */
	public static void setThinkingTime(int min, int sec, int playerIndex) {
		
	}
	/**
	 *  Communicates to View to check if a Illegal Move Notification is being displayed (feature: move wall)
	 *  @author David
	 * @throws Throwable
	 * @return true if notification displayed, false otherwise
	 * 
	 */
	public static boolean isIllegalMoveNotificationDisplayed() {
		throw new java.lang.UnsupportedOperationException();
	}
	/**
	 *  Communicates to View to check if a wall is currently is being displayed at a certain location (feature: move wall)
	 *  @author David
	 * @throws Throwable
	 * @return true if wall displayed at specified location, false otherwise
	 * 
	 */
	public static boolean thisWallIsAtPosition(int row, int column) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	

	

	
}
