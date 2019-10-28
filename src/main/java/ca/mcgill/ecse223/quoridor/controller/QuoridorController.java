package ca.mcgill.ecse223.quoridor.controller;

import java.net.UnknownServiceException;
import java.util.*;
import java.io.IOException;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;

public class QuoridorController {

    /**
     * Gherkin feature: Initialize Board
     * This controller method is responsible for initializing the board. It sets the current player to move to the white player.
     * It also sets the white and the black pawn to their initial position and assigns 10 walls in stock to each
     * player. Finally, it starts the white player's timer (thinking time).
     *
     * @param quoridor - Quoridor application
     * @return void
     * @author Thomas Philippon
     */
    public static void initializeBoard(Quoridor quoridor) {
        throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
    }

    /**
     * get the wallMove object associated with the current game and current player with the required orientation, row, and column.
     * If no such wallMove object exist, (i.e. there is no such a wall with such a position and orientation), a new object will be created
     * and links added to model. (feature:move wall)
     *
     * @param dir    orientation of the wall "vertical","horizontal"
     * @param row    row number of tile northwest of centrepoint of wall
     * @param column column number
     * @throws Throwable
     * @author David
     */
    public static void getWallMove(String dir, int row, int column) throws Throwable {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * (feature: move wall)
     *
     * @param side the side to check for edges. "left", "right", "up", "down"
     * @return true if the current wall selected is at the edge specified in the parameter
     * @throws Throwable
     * @author David
     */
    public static boolean wallIsAtEdge(String side) throws Throwable {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * moves the wall one tile toward the direction specified. An illegal move notification will be shown
     * if such a move in illegal. This involves linking wallMove object to a new target tile(feature: move wall)
     *
     * @param side the direction at which we move the wall. "left", "right", "up", "down"
     * @throws Throwable
     * @author David
     */
    public static void moveWall(String side) throws Throwable {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Each player is given a fixed time limit for a game. This method changes the remaining thinking
     * time of each player (feature: set total thinking time)
     *
     * @param min the minute part of the time
     * @param sec the second part of the time
     * @throws Throwable
     * @author David
     */
    public static void setThinkingTime(int min, int sec) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Optional feature: Set thinking time for a specific player.
     * We know that both players are supposed to have the same thinking time.
     * However, being able to set time for individual players can to increase the difficulty for a more experienced player without affecting the other opponent.
     * Each player is given a fixed time limit for a game. This method changes the remaining thinking
     * time of each player
     *
     * @param min         the minute part of the time
     * @param sec         the second part of the time
     * @param playerIndex specifies for whom this thinking time is for
     * @throws Throwable
     * @author David
     */
    public static void setThinkingTime(int min, int sec, int playerIndex) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * @param game is the current game
     * @return true if game is running and false if not
     * @author Daniel Wu
     */
    public static boolean isGameRunning(Game game) {
        return game.getGameStatus() == GameStatus.Running;
    }


    /**
     * @param game is the current game
     * @return true if gamestate is initializing and false if not
     * @author Daniel Wu
     */
    public static boolean isGameInitializing(Game game) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * @param player is the player whether it be white or black
     * @return true if the player chose a username and false if not
     * @author Daniel Wu
     */
    public static boolean playerChoseUsername(Player player) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * @param game takes the return from Player.setRemainingTime
     * @return true if the thinking time is set and false if not
     * @author Daniel Wu
     */
    public static boolean thinkingTimeIsSet(Game game) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * @param whitePlayer the white player in the game
     * @param blackPlayer the black player in the game
     * @return true if the clock is started for both players
     * @author Daniel Wu
     */
    public static boolean startClock(Player whitePlayer, Player blackPlayer) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * @return the current gamestatus
     * @author Daniel Wu
     */
    public static Game.GameStatus getGameStatus() {
        return QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus();
    }

    /**
     * @param gameStatus the gameStatus we want to set the current game to
     * @return true if the gamestatus was correctly updated and false if not
     * @author Daniel Wu
     */
    public static boolean setGameStatus(Game.GameStatus gameStatus) {
        getCurrentGame().setGameStatus(gameStatus);
        return getGameStatus().equals(gameStatus);
    }

    /**
     * Communicates to View to check if a Illegal Move Notification is being displayed (feature: move wall)
     *
     * @return true if notification displayed, false otherwise
     * @throws Throwable
     * @author David
     */
    public static boolean isIllegalMoveNotificationDisplayed() {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Communicates to View to check if a wall is currently is being displayed at a certain location (feature: move wall)
     *
     * @return true if wall displayed at specified location, false otherwise
     * @throws Throwable
     * @author David
     */
    public static boolean thisWallIsAtPosition(int row, int column) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * Gherkin feature: Grab Wall
     * This controller method is responsible for first checking if the current
     * player to move has more walls in stock. If the player has more walls, a wall candidate
     * object is created at initial position (Tile at (0, 0)) and the method returns 1. If the player has no more walls
     * in stock, no wall move candidate is created and the method returns 0.
     *
     * @param game - Current Game
     * @return Boolean - Returns 1 if a wall candidate object was created and 0 if not
     * @author Thomas Philippon
     */
    public static Boolean grabWall(Game game) {
        throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
    }

    /**
     * Query method to get the number of walls in stock for both players
     *
     * @param game - Current Game
     * @return List<Integer>  -  List where the number of white walls in stock is at the first index and the number of black walls in stock at the second index
     * @author Thomas Philippon
     */
    public static List<Integer> numberOfWallsInStock(Game game) {
        throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
    }

    /**
     * Method to release current Wall in my hand and register move
     *
     * @param quoridor Current Quoridor Object, to retrieve the wallMoveCandidate in the game and any other necessary info
     * @return True if releaseWall was successful and wall could be registered, false if not
     * @author Alex Masciotra
     */
    public static Boolean releaseWall(Quoridor quoridor) {
        Boolean wasSuccessful = true;

        WallMove wallMoveCandidate = quoridor.getCurrentGame().getWallMoveCandidate();

        GamePosition currentGamePosition = quoridor.getCurrentGame().getCurrentPosition();


        List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();
        List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();

        Map<Tile, String> tileInUseByWallAndDirection = new LinkedHashMap<Tile, String>();

        List<Tile> tilesInUseByWall = new ArrayList<>();

        for (Wall wallOnBoard : whiteWallsOnBoard) {

            Tile tempTile = wallOnBoard.getMove().getTargetTile();
            String Direction = wallOnBoard.getMove().getWallDirection().toString();
            tileInUseByWallAndDirection.put(tempTile, Direction);
            //tilesInUseByWall.add(wallOnBoard.getMove().getTargetTile());
        }

        for (Wall wallOnBoard : blackWallsOnBoard) {

            Tile tempTile = wallOnBoard.getMove().getTargetTile();
            String Direction = wallOnBoard.getMove().getWallDirection().toString();
            tileInUseByWallAndDirection.put(tempTile, Direction);
            //tilesInUseByWall.add(wallOnBoard.getMove().getTargetTile());

        }

        for (Map.Entry element : tileInUseByWallAndDirection.entrySet()) {

            Tile tileInUse = (Tile) element.getKey();
            String Direction = (String) element.getValue();
            if (tileInUse.equals(wallMoveCandidate.getTargetTile())) {
                wasSuccessful = false;
                break;
            }
            if (Direction.equals(wallMoveCandidate.getWallDirection().toString())) {
                Integer colInUse = tileInUse.getColumn();
                Integer rowInUse = tileInUse.getRow();
                Integer targetCol = wallMoveCandidate.getTargetTile().getColumn();
                Integer targetRow = wallMoveCandidate.getTargetTile().getRow();
                if (Direction.equals("Horizontal")) {
                    if (rowInUse.equals(targetRow)) {
                        Integer gap = java.lang.Math.abs(targetCol - colInUse);
                        if (gap == 1) {
                            wasSuccessful = false;
                            break;
                        }
                    }
                } else if (Direction.equals("Vertical")) {
                    if (colInUse.equals(targetCol)) {
                        Integer gap = java.lang.Math.abs(targetRow - rowInUse);
                        if (gap == 1) {
                            wasSuccessful = false;
                            break;
                        }
                    }
                }
            }
        }
//		for(Tile tile : tilesInUseByWall){
//
//			if(tile.equals(wallMoveCandidate.getTargetTile())){
//				wasSuccessful = false;
//				break;
//			}
//		}

		//if successful complete my move and change turn to next player, if not successful, do not change turn cause still my turn

        return wasSuccessful;
    }

    /***
     * Method to set existing username to player
     * @param userName username to set to player
     * @param quoridor Quoridor quoridor in order to get the player and set username to that existing player and also
     * to get list of users
     * @author Alex Masciotra
     */
    public static void selectExistingUserName(String userName, Quoridor quoridor) {

        List<User> existingUsers = quoridor.getUsers();
        Game currentGame = quoridor.getCurrentGame();

        for (User user : existingUsers) {
            if (user.getName().equals(userName)) {
                Player playerToAssignUserTo = currentGame.getWhitePlayer().getNextPlayer();
                playerToAssignUserTo.setUser(user);
            }
        }
    }

    /**
     * Method to assign new Username to a player
     *
     * @param userName username to set to player
     * @param quoridor Quoridor Application in order to get the player and his color and set username  of user to that player
     * @return True assigning of new username was correct / False if name already exists
     * @author Alex Masciotra
     */
    public static Boolean selectNewUserName(String userName, Quoridor quoridor) { //could be boolean to see if it could indeed set

        //get all existing userNames and check to see if new userNAme is unique and available or in use
        Boolean userNameSetSuccessfully = true;
        List<User> existingUsers = quoridor.getUsers();

        Game currentGame = quoridor.getCurrentGame();

        for (User user : existingUsers) {
            if (user.getName().equals(userName)) {
                userNameSetSuccessfully = false;
            }
        }

        if (userNameSetSuccessfully) {
            Player playerToAssignUserTo = currentGame.getWhitePlayer().getNextPlayer();
            playerToAssignUserTo.getUser().setName(userName);
        }

        return userNameSetSuccessfully;
    }

    /***
     * This method is to provide a list of existing user names on the GUI when at the quoridor menu to see a list of
     * existing usernames and select one
     * @return List of UserNames in Quoridor
     * @param game game instance to get list of usernames
     * @author Alex Masciotra
     */
    public static List<String> provideExistingUserNames(Quoridor quoridor) {

        //get all existing userNames and check to see if new userNAme is unique and available or in use
        List<String> existingUserNames = new ArrayList<String>();

        List<User> existingUsers = quoridor.getUsers();

        for (User user : existingUsers) {
            existingUserNames.add(user.getName());
        }

        return existingUserNames;
    }

    /**
     * @param row
     * @param col
     * @return true, if the pawn position with the parameters is valid, false if not
     * @author Daniel Wu
     */
    public static Boolean validatePosition(int row, int col) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * @param row
     * @param col
     * @param dir
     * @return true, if the wall position with the parameters is valid, false if not
     * @author Daniel Wu
     */
    public static Boolean validatePosition(int row, int col, String dir) {
        throw new java.lang.UnsupportedOperationException();
    }

    //Getter for gamestate

    /**
     * Ensures that a wall move candidate exists with parameters dir,row,col
     * Creates one if one does not exist.
     *
     * @return whether or not it succeeded in finding the specified wall move candidate
     * @throws UnsupportedOperationException
     * @author Matthias Arabian
     */
    public static Boolean GetWallMoveCandidate(String dir, int row, int col) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("GetWallMoveCandidate Error");
    }

    /**
     * Changes the WallMoveCandidate's direction from Horizontal to Vertical
     * and vice versa
     *
     * @throws UnsupportedOperationException
     * @author Matthias Arabian
     * @returns true if the wall was successfully flipped. false otherwise
     */
    public static Boolean flipWallCandidate() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("flipWallCandidate Error");
    }

    /**
     * Verifies that the load position is a valid position
     *
     * @return true: position is valid. false otherwise
     * @throws UnsupportedOperationException
     * @author Matthias Arabian
     */
    public static Boolean CheckThatPositionIsValid() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Position is valid");
    }

    /**
     * Loads a saved game by instantiating a new game and populating it with file data
     *
     * @param fileName
     * @throws UnsupportedOperationException
     * @author Matthias Arabian
     */
    public static void loadSavedGame(String fileName) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Game could not be loaded");

    }


    /**
     * Propagates a sort of "Invalid Position to Load" error to wherever it is necessary
     *
     * @return whether the error has been successfully propagated
     * @throws UnsupportedOperationException
     * @author Matthias Arabian
     */
    public static Boolean sendLoadError() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("sendLoadError");
    }

    /**
     * Simple query method for obtaining the application's current game instance.
     * Creates one if there isn't one.
     *
     * @return game
     */
    public static Game getCurrentGame() {
        return QuoridorApplication.getQuoridor().getCurrentGame();
    }

    /**
     * NOT IMPLEMENTED.
     * This method saves a game into a file whose name is specified, but not its path, and whose game is provided.
     * It returns true when serialization of the game is successful and false when unsuccessful.
     * This method is overwrite-averse; it does not overwrite files that already exist.
     *
     * @param filename (no extension will be added)
     * @param game
     * @return savingStatus enum
     * @throws IOException
     * @author Edwin Pan
     */
    public static SavingStatus saveGame(String filename, Game game) throws IOException {
        if (SaveConfig.createFileSavesFolder() == false) {
            return SavingStatus.failed;
        }
        throw new UnsupportedOperationException("QuoridorController.saveGame(filename,game) not yet implemented.");
    }

    /**
     * NOT IMPLEMENTED.
     * This is the overwrite-capable version of saveGame.
     * This method saves a game into a file whose name is specified, but not its path, and whose game is provided.
     * It returns true when serialization of the game is successful and false when unsuccessful.
     *
     * @param filename  (no extension will be added)
     * @param game
     * @param overwrite
     * @return savingStatus enum
     * @throws IOException
     * @author Edwin Pan
     */
    public static SavingStatus saveGame(String filename, Game game, boolean overwrite) throws IOException {
        if (SaveConfig.createFileSavesFolder() == false) {
            return SavingStatus.failed;
        }
        throw new UnsupportedOperationException("QuoridorController.saveGame(filename,game,overwrite) not yet implemented.");
    }

    /**
     * Simple query method for obtaining the player that is currently black.
     * There are NO PROTECTIVE MEASURES for ensuring that this method is usable only when
     * there exists a game and the white player is registered.
     *
     * @return Player instance that is black for current game.
     * @author Edwin Pan
     */
    public static Player getCurrentBlackPlayer() {
        return QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
    }

    /**
     * Simple query method for obtaining the player that is currently white.
     * There are NO PROTECTIVE MEASURES for ensuring that this method is usable only when
     * there exists a game and the white player is registered.
     *
     * @return Player instance that is white for current game.
     * @author Edwin Pan
     */
    public static Player getCurrentWhitePlayer() {
        return QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
    }

    /**
     * Query method for obtaining the player that is either Black or White.
     * Takes string with characters (upper or lower -case) of WHITE or BLACK.
     * There are NO PROTECTIVE MEASURES for ensuring that the game instance exists
     * for this method to obtain the player from.
     *
     * @param string of either "Black" or "White" in either upper or lower cases.
     * @return Player instance of the asked color for the current game.
     * @throws IllegalArgumentException if your color is invalid (not white or black)
     * @author Edwin Pan
     */
    public static Player getPlayerOfProvidedColorstring(String color) {
        String colorCandidate = color;
        colorCandidate = colorCandidate.trim();
        colorCandidate = colorCandidate.toLowerCase();
        if (colorCandidate.equals("white")) {
            return QuoridorController.getCurrentWhitePlayer();
        } else if (colorCandidate.equals("black")) {
            return QuoridorController.getCurrentBlackPlayer();
        } else {
            throw new IllegalArgumentException("Received unsupported color argument in QuoridorController.getPlayerOfProvidedColorstring(colorstring)!");
        }
    }

    /**
     * Simple query method which obtains the player whose turn it currently is.
     * There are NO PROTECTIVE MEASURES for ensuring that this method is usable only
     * when there exists a live game.
     *
     * @return Player instance
     * @author Edwin Pan
     */
    public static Player getPlayerOfCurrentTurn() {
        return QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
    }

    /**
     * PENDING IMPLEMENTATION
     * Completes the turn of the provided player. Aborts (and thereby returns false) if
     * the current turn does not belong to the provided player.
     *
     * @param player
     * @return
     * @author Edwin Pan
     */
    public static boolean completePlayerTurn(Player player) {
        throw new UnsupportedOperationException("QuoridorController.completePlayerturn(player) is not currently implemented!");
    }

    /**
     * PENDING IMPLEMENTATION
     * GUI modifier method.
     * Stops the clock of the provided player.
     *
     * @param player
     * @return operation success boolean
     * @author Edwin Pan
     */
    public static boolean stopPlayerTimer(Player player) {
        throw new UnsupportedOperationException("QuoridorController.playerTimerStop(player) is not currently implemented!");
    }

    /**
     * PENDING IMPLEMENTATION
     * GUI modifier method.
     * Lets the clock of the provided player run.
     *
     * @param player
     * @return operation success boolean
     * @author Edwin Pan
     */
    public static boolean continuePlayerTimer(Player player) {
        throw new UnsupportedOperationException("QuoridorController.playerTimerStop(player) is not currently implemented!");
    }

    /**
     * PENDING IMPLEMENTATION
     * GUI query method.
     * Returns whether or not the provided player's clock (from their turn) is running.
     *
     * @param player
     * @return player timer is running boolean
     * @author Edwin Pan
     */
    public static boolean getPlayerTimerRunning(Player player) {
        throw new UnsupportedOperationException("QuoridorController.playerTimerStop(player) is not currently implemented!");
    }

}



