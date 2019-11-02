package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.timer.PlayerTimer;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class QuoridorController {

    /**
     * Gherkin feature: Initialize Board
     * This controller method is responsible for initializing the board. It sets the current player to move to the white player.
     * It also sets the white and the black pawn to their initial position and assigns 10 walls in stock to each
     * player. Finally, it starts the white player's timer (thinking time).
     *
     * @param quoridor - Quoridor ca.mcgill.ecse223.quoridor.application
     * @return void
     * @author Thomas Philippon
     */
    public static void initializeBoard(Quoridor quoridor, Timer timer){
        // throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");
        if(quoridor.getCurrentGame() ==null){
            throw new java.lang.UnsupportedOperationException("The Quoridor object does not exist");
        }
        //Create the board object
        Board board = new Board(quoridor);

        //Create tiles
        for (int i = 1; i <= 9; i++) { // rows
            for (int j = 1; j <= 9; j++) { // columns
                board.addTile(i, j);
            }
        }

        //Set the player to move to the white player
        Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
        Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();

        //Set the white and the black player's pawn to their initial positions
        int whiteRow = 1;
        int whiteCol = 5;
        int blackRow = 9;
        int blackCol = 5;
        Tile whitePlayerStartTile = quoridor.getBoard().getTile((whiteRow - 1) * 9 + whiteCol - 1);
        Tile blackPlayerStartTile = quoridor.getBoard().getTile((blackRow - 1) * 9 + blackCol - 1);

        //create position
        PlayerPosition whitePlayerStartPosition = new PlayerPosition(whitePlayer, whitePlayerStartTile);
        PlayerPosition blackPlayerStartPosition = new PlayerPosition(blackPlayer, blackPlayerStartTile);

        GamePosition whitePlayerPosition = new GamePosition(0, whitePlayerStartPosition, blackPlayerStartPosition, whitePlayer, quoridor.getCurrentGame());

        //add 10 walls in stock for both players
        for (int j = 0; j < 10; j++) {
            Wall wall = Wall.getWithId(j);
            whitePlayerPosition.addWhiteWallsInStock(wall);
        }
        for (int j = 0; j < 10; j++) {
            Wall wall = Wall.getWithId(j + 10);
            whitePlayerPosition.addBlackWallsInStock(wall);
        }

        quoridor.getCurrentGame().setCurrentPosition(whitePlayerPosition);
        quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(whitePlayer);

        startPlayerTimer(whitePlayer, timer); //call the controller method "startClock"
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
     * @param quoridor
     * @return true if game is initialized and false if not
     * @author Daniel Wu
     */
    public static boolean InitializeGame(Quoridor quoridor) {
       //could have a false, if there is currently a game running
        /*if (isGameRunning(quoridor.getCurrentGame())){
            return false;
            //or have a popup showup to tell the player if they are sure or something
        }*/
        Game game = new Game(GameStatus.Initializing, Game.MoveMode.PlayerMove, quoridor);
        Player player1 = new Player(null, null, 9, Direction.Horizontal);
        Player player2 = new Player(null, null, 1, Direction.Horizontal);

        Player[] players = { player1, player2 };
        // Create all walls. Walls with lower ID belong to player1,
        // while the second half belongs to player 2
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                new Wall(i * 10 + j, players[i]);
            }
        }

        game.setWhitePlayer(player1);
        game.setBlackPlayer(player2);
        return true;
    }

    /**
     * @param player is the player whether it be white or black
     * @return true if the player chose a username and false if not
     * @author Daniel Wu
     */
    public static boolean playerChoseUsername(Player player) {
        //This returns false if the player didn't, choose a username, but a player can't exist without a username??!?!??!
        if (player.getUser() != null){
            return false;
        }
        return true;
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
     * @param player
     * @param timer
     * @return void
     * @author Daniel Wu
     */
    public static void startPlayerTimer(Player player, Timer timer) {
        PlayerTimer playerTimer = new PlayerTimer(player);
        timer.schedule(playerTimer,0, 1000); //the playerTimer task will be executed every 1 second
    }

    /**
     * @return the current GameStatus
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
     * @param Quoridor - Quoridor application
     * @return Boolean - Returns 1 if a wall candidate object was created and 0 if not
     * @author Thomas Philippon
     */
    public static Boolean grabWall(Quoridor quoridor) {
        // throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");

        Game game = quoridor.getCurrentGame();
        String whitePlayerName = game.getWhitePlayer().getUser().getName();
        Wall wall;
        //check if the player to move has more walls in stock
        Player playerToMove = game.getCurrentPosition().getPlayerToMove();
        Integer nbOfWalls = numberOfWallsInStock(playerToMove, game);

        if(nbOfWalls <= 0) {
            return false; //the current player to move has no more walls in stock
        }
        else{ //the player has more walls in stock
            int lastMoveNumber = game.getMoves().size();
            int roundNumber = game.getCurrentPosition().getId();
            Tile targetTile = quoridor.getBoard().getTile(0); //initialize the wall move candidate to the tile(0,0)

            if(playerToMove.getUser().getName().toString().equals(whitePlayerName)) {
                wall = playerToMove.getWall(nbOfWalls-1);
                game.getCurrentPosition().removeWhiteWallsInStock(wall);
            }
            else{
                wall = playerToMove.getWall(nbOfWalls-1+10);
                game.getCurrentPosition().removeBlackWallsInStock(wall);
            }

            WallMove wallMoveCandidate = new WallMove(lastMoveNumber+1, roundNumber, playerToMove, targetTile, game, Direction.Horizontal, wall);
            game.setWallMoveCandidate(wallMoveCandidate);
            return true;
        }

    }

    /**
     * Query method to get the number of walls in stock for a player
     *
     * @param Player - A player of the game
     * @param Game - The game that the player is playing
     * @return Integer  - The number of walls in stock for the player
     * @author Thomas Philippon
     */
    public static Integer numberOfWallsInStock(Player player, Game game) {

        Integer nbOfWalls = 0;
        //Get both players to determine which player is provided
        String blackPlayerName = game.getBlackPlayer().getUser().getName();
        String whitePlayerName = game.getWhitePlayer().getUser().getName();

        if(player.getUser().getName().toString().equals(whitePlayerName)) {
            nbOfWalls = game.getCurrentPosition().getWhiteWallsInStock().size();
        }
        else if(player.getUser().getName().toString().equals(blackPlayerName)) {
            nbOfWalls = game.getCurrentPosition().getBlackWallsInStock().size();
        }
        return nbOfWalls;
    }

    /**
     * Method to release current Wall in my hand and register move
     *
     * @param quoridor Current Quoridor Object, to retrieve the wallMoveCandidate in the game and any other necessary info
     * @return True if releaseWall was successful and wall could be registered, false if not
     * @author Alex Masciotra
     */
    public static Boolean releaseWall(Quoridor quoridor) {
        Boolean isValid;

        WallMove wallMoveCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
        GamePosition currentGamePosition = quoridor.getCurrentGame().getCurrentPosition();


        int targetRow = wallMoveCandidate.getTargetTile().getRow();
        int targetCol = wallMoveCandidate.getTargetTile().getColumn();
        String targetDir = wallMoveCandidate.getWallDirection().toString();

        isValid = validatePosition(targetRow, targetCol, targetDir);
        //if successful complete my move and change turn to next player, if not successful, do not change turn cause still my turn

        Player currentPlayer = currentGamePosition.getPlayerToMove();

        if (isValid) {

            quoridor.getCurrentGame().addMove(wallMoveCandidate);
            if (currentPlayer.hasGameAsWhite()) {
                currentGamePosition.addWhiteWallsOnBoard(wallMoveCandidate.getWallPlaced());
                currentGamePosition.setPlayerToMove(getCurrentBlackPlayer());
            } else {
                currentGamePosition.addBlackWallsOnBoard(wallMoveCandidate.getWallPlaced());
                currentGamePosition.setPlayerToMove(getCurrentWhitePlayer());
            }
        } else {
            if (currentPlayer.hasGameAsWhite()) {
                currentGamePosition.setPlayerToMove(getCurrentWhitePlayer());
            } else {
                currentGamePosition.setPlayerToMove(getCurrentBlackPlayer());
            }
        }

        return isValid;
    }

    /**
     * Helper Method to set the username to the correct color
     *
     * @param color    of player
     * @param quoridor object
     * @author Alex Masciotra
     */
    public static void assignPlayerColorToUserName(String color, Quoridor quoridor) {

        Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
        Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();

        if (color.equals("white")) {

            quoridor.getCurrentGame().getWhitePlayer().setNextPlayer(whitePlayer);
        } else if (color.equals("black")) {

            quoridor.getCurrentGame().getWhitePlayer().setNextPlayer(blackPlayer);
        } else {

            throw new IllegalArgumentException("Unsupported color was provided");
        }
    }

    /**
     * method to insure no nullpointerExceptions occur when getting items from the model
     *
     * @param quoridor game object
     * @author Alex Masciotra
     */
    public static void initializeQuoridor(Quoridor quoridor) {
        //part of method is taken from given code in the stepDefinitions


        int thinkingTime = 10; //placeholder
        Player player1 = new Player(null, null, 9, Direction.Horizontal);
        Player player2 = new Player(null, null, 1, Direction.Horizontal);

        Player[] players = { player1, player2 };

        // Create all walls. Walls with lower ID belong to player1,
        // while the second half belongs to player 2
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                new Wall(i * 10 + j, players[i]);
            }
        }

        new Game(GameStatus.Initializing, Game.MoveMode.PlayerMove, quoridor);

        quoridor.getCurrentGame().setWhitePlayer(player1);
        quoridor.getCurrentGame().setBlackPlayer(player2);

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
     * @param quoridor quoridor instance to get list of usernames
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
        //Check if out of the board, this should probably throw an exception
        if ((row > 9) || (row < 1) || (col > 9) || (col < 1)) {
            return false;
        }

        //Check if another player is already there, assuming we have to move then we don't have to know who's moving
        GamePosition currentGamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();
        PlayerPosition whitePlayerPosition = currentGamePosition.getWhitePosition();
        PlayerPosition blackPlayerPosition = currentGamePosition.getBlackPosition();

        if ((whitePlayerPosition.getTile().getRow() == row) && (whitePlayerPosition.getTile().getColumn() == col)) {
            return false;
        }
        return (blackPlayerPosition.getTile().getRow() != row) || (blackPlayerPosition.getTile().getColumn() != col);
    }

    /**
     * @param row
     * @param col
     * @param dir
     * @return true, if the wall position with the parameters is valid, false if not
     * @author Daniel Wu
     */
    public static Boolean validatePosition(int row, int col, String dir) {
        //Check if out of the board, this should probably throw an exception
        if ((row > 8) || (row < 1) || (col > 8) || (col < 1)) {
            return false;
        }

        //Check if walls are overlapping
        GamePosition currentGamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition();


        List<Wall> whiteWallsOnBoard = currentGamePosition.getWhiteWallsOnBoard();
        List<Wall> blackWallsOnBoard = currentGamePosition.getBlackWallsOnBoard();

//        Map<Tile, String> tileInUseByWallAndDirection = new LinkedHashMap<Tile, String>();

//        List<Tile> tilesInUseByWall = new ArrayList<>();

        int numberOfWalls = whiteWallsOnBoard.size() + blackWallsOnBoard.size();
        Tile[] tilesInUse = new Tile[numberOfWalls];
        String[] directions = new String[numberOfWalls];

        for (int i = 0; i < numberOfWalls; i++) {
            if (i < whiteWallsOnBoard.size()) {
                //Adding the tiles used and their direction to their respective arrays
                tilesInUse[i] = whiteWallsOnBoard.get(i).getMove().getTargetTile();
                directions[i] = whiteWallsOnBoard.get(i).getMove().getWallDirection().toString();
            } else {
                //When we run out of white one, then to get the 0's index we need to do i - number of white walls on board
                tilesInUse[i] = blackWallsOnBoard.get(i - whiteWallsOnBoard.size()).getMove().getTargetTile();
                directions[i] = blackWallsOnBoard.get(i - whiteWallsOnBoard.size()).getMove().getWallDirection().toString();
            }
        }

        for (int i = 0; i < numberOfWalls; i++) {
            //Check if same tile
            if ((tilesInUse[i].getRow() == row) && (tilesInUse[i].getColumn() == col)) {
                return false;
            }
            //If it's not the same tile then check directionality
            if (directions[i].toLowerCase().equals(dir.toLowerCase())) {
                //If horizontal walls
                if (dir.toLowerCase().equals("horizontal")) {
                    //then check if same row
                    if (tilesInUse[i].getRow() == row) {
                        //then check if too close
                        Integer gap = java.lang.Math.abs(tilesInUse[i].getColumn() - col);
                        if (gap == 1) {
                            return false;
                        }
                    }
                } else if (dir.toLowerCase().equals("vertical")) {
                    //If vertical walls, then check if same column
                    if (tilesInUse[i].getColumn() == col) {
                        //then check if too close
                        Integer gap = java.lang.Math.abs(tilesInUse[i].getRow() - row);
                        if (gap == 1) {
                            return false;
                        }
                    }
                }
            }
        }

        //This will potentially also check if the wall will block the path to the other side

        return true;
    }

    /**
     * This version is used when trying to validate the gamePosition, it checks if all the moves are valid
     *
     * @param gamePosition
     * @return true, if the GamePosition is valid, false if not
     * @author Daniel Wu
     */
    public static Boolean validatePosition(GamePosition gamePosition) {
        //This is obsolete as the gamePosition wouldn't exist in the first place, so I'm skipping whether or not the move is on the board
//    	if ((gamePosition.getBlackPosition().getTile().getRow() >9) || (gamePosition.getBlackPosition().getTile().getRow() <1)
//    			|| (gamePosition.getBlackPosition().getTile().getColumn() >9) || (gamePosition.getBlackPosition().getTile().getColumn() <1));

        List<Wall> whiteWallsOnBoard = gamePosition.getWhiteWallsOnBoard();
        List<Wall> blackWallsOnBoard = gamePosition.getBlackWallsOnBoard();

        int numberOfWalls = whiteWallsOnBoard.size() + blackWallsOnBoard.size();
        Tile[] tilesInUse = new Tile[numberOfWalls];
        String[] directions = new String[numberOfWalls];

        for (int i = 0; i < numberOfWalls; i++) {
            if (i < whiteWallsOnBoard.size()) {
                // Adding the tiles used and their direction to their respective arrays
                tilesInUse[i] = whiteWallsOnBoard.get(i).getMove().getTargetTile();
                directions[i] = whiteWallsOnBoard.get(i).getMove().getWallDirection().toString();
            } else {
                // When we run out of white one, then to get the 0's index we need to do i -
                // number of white walls on board
                tilesInUse[i] = blackWallsOnBoard.get(i - whiteWallsOnBoard.size()).getMove().getTargetTile();
                directions[i] = blackWallsOnBoard.get(i - whiteWallsOnBoard.size()).getMove().getWallDirection().toString();
            }
        }

        for (int i = 0; i < numberOfWalls; i++) {
            for (int j = i + 1; j < numberOfWalls; j++) {
                // Check with every single wall on the board
                // Check if same tile
                if ((tilesInUse[i].getRow() == tilesInUse[j].getRow()) && (tilesInUse[i].getColumn() == tilesInUse[j].getColumn())) {
                    return false;
                }
                // If it's not the same tile then check directionality
                if (directions[i].toLowerCase().equals(directions[j].toLowerCase())) {
                    // If horizontal walls
                    if (directions[i].toLowerCase().equals("horizontal")) {
                        // then check if same row
                        if (tilesInUse[i].getRow() == tilesInUse[j].getRow()) {
                            // then check if too close
                            Integer gap = java.lang.Math.abs(tilesInUse[i].getColumn() - tilesInUse[j].getColumn());
                            if (gap == 1) {
                                return false;
                            }
                        }
                    } else if (directions[i].toLowerCase().equals("vertical")) {
                        // If vertical walls, then check if same column
                        if (tilesInUse[i].getColumn() == tilesInUse[j].getColumn()) {
                            // then check if too close
                            Integer gap = java.lang.Math.abs(tilesInUse[i].getRow() - tilesInUse[j].getRow());
                            if (gap == 1) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
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
     * @author Matthias Arabian
     * @returns true if the wall was successfully flipped. false otherwise
     */
    public static Boolean flipWallCandidate() {
        Quoridor q = QuoridorApplication.getQuoridor();
        Direction d = q.getCurrentGame().getWallMoveCandidate().getWallDirection();
        return q.getCurrentGame().getWallMoveCandidate().setWallDirection(
        		d.equals(Direction.Horizontal) ? Direction.Vertical : Direction.Horizontal
        );
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
     * Simple query method for obtaining the ca.mcgill.ecse223.quoridor.application's current game instance.
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
     * Completes the turn of the provided player. Aborts (and thereby returns false) if
     * the current turn does not belong to the provided player.
     *
     * @param player
     * @return
     * @author Matthias Arabian
     */
    public static boolean completePlayerTurn(Player player) {
    	Quoridor quoridor = QuoridorApplication.getQuoridor();
    	Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
    	if (currentPlayer.equals(player)) {
    		quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer.getNextPlayer());
    		return true;
    	}
    	else
    		return false;
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
    public static void stopPlayerTimer(Player player, Timer timer) {
        timer.cancel();
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



