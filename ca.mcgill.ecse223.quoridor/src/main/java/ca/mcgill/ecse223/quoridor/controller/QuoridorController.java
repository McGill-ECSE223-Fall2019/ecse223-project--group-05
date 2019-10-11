package ca.mcgill.ecse223.quoridor.controller;

import java.util.List;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.util.List;

public class QuoridorController {

	/**
	 * Gherkin feature: Initialize Board
	 * This controller method is responsible for creating a game object (current game) and player objects. Then is sets the current player to move to the white player. 
	 * It also assigns the white and the black pawn to their initial position and assigns 10 walls to each 
	 * player. Finally, it starts the white player's timer (thinking time). 
	 * 
	 * @author             Thomas Philippon
	 * @param Quoridor      - Quoridor application 
	 * @return void    
	 */
	public static void initializeBoard(Quoridor quoridor) {
		
		throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
	}
	
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
	 */
	public static boolean isIllegalMoveNotificationDisplayed() {
		throw new java.lang.UnsupportedOperationException();
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
	
	/**
	 * Query method to know the number of walls in stock for both the white and black players
	 *  
	 * @author             Thomas Philippon
	 * @param game         - Current Game 
	 * @return List<Integer>  -  List where the number of white walls in stock is at the first index and the number of black walls in stock at the second index
	 */
	public static List<Integer> numberOfWallsInStock(Game game) {
		
		throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
	}
	
    /**
     * Method to release current Wall in my hand and register move
     * @param game Current Game Object, to retrieve the wallMoveCandidate in the game and any other necessary info
     * @return True if releaseWall was successful and wall could be registered, false if not
     * @author Alex Masciotra
     */
    public static Boolean releaseWall(Game game){
        throw new java.lang.UnsupportedOperationException("Not yet implemented");
    }

    /***
     * Method to set existing username to player
     * @param userName username to set to player
     * @param game Quoridor Game in order to get the player and set username to that existing player
     * @return True if successful, false if not
     * @author Alex Masciotra
     */
    public static Boolean selectExistingUserName(String userName, Game game){

        throw new java.lang.UnsupportedOperationException("Not yet implemented");


        //get all existing usernames and see if username provided is found
    }

    /**
     * Method to assign new Username to a player
     * @param userName username to set to player
     * @param game Quoridor Game Application in order to get the player and set username to that player
     * @author Alex Masciotra
     * @return True assigning of new username was correct / False if name already exists
     */
    public static Boolean selectNewUserName(String userName, Game game){ //could be boolean to see if it could indeed set

        throw new java.lang.UnsupportedOperationException("Not yet implemented");

    /**
     * Method to release current Wall in my hand and register move
     * @param game Current Game Object, to retrieve the wallMoveCandidate in the game and any other necessary info
     * @return True if releaseWall was successful and wall could be registered, false if not
     * @author Alex Masciotra
     */
    public static Boolean releaseWall(Game game){
        throw new java.lang.UnsupportedOperationException("Not yet implemented");
    }


    /***
     * Method to set existing username to player
     * @param userName username to set to player
     * @param game Quoridor Game in order to get the player and set username to that existing player
     * @return True if successful, false if not
     * @author Alex Masciotra
     */
    public static Boolean selectExistingUserName(String userName, Game game){

        throw new java.lang.UnsupportedOperationException("Not yet implemented");


        //get all existing usernames and see if username provided is found
    }

    /**
     * Method to assign new Username to a player
     * @param userName username to set to player
     * @param game Quoridor Game Application in order to get the player and set username to that player
     * @author Alex Masciotra
     * @return True assigning of new username was correct / False if name already exists
     */
    public static Boolean selectNewUserName(String userName, Game game){ //could be boolean to see if it could indeed set

        throw new java.lang.UnsupportedOperationException("Not yet implemented");


        //get all existing userNames and check to see if new userNAme is unique and available or in use
    }

    /***
     * This method is to provide a list of existing user names on the UI when at the quoridor menu to see a list of
     * existing usernames and select one
     * @return List of UserNames in Quoridor
     * @author Alex Masciotra
     */
    public List <String> provideExistingUserNames(){

        throw new java.lang.UnsupportedOperationException("Not yet implemented");

        //get all existing userNames and check to see if new userNAme is unique and available or in use
    }

    /***
     * This method is to provide a list of existing user names on the UI when at the quoridor menu to see a list of
     * existing usernames and select one
     * @return List of UserNames in Quoridor
     * @author Alex Masciotra
     */
    public List <String> provideExistingUserNames(){

        throw new java.lang.UnsupportedOperationException("Not yet implemented");

    }
}

