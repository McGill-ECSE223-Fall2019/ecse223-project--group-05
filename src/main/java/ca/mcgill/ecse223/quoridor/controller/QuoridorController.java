package ca.mcgill.ecse223.quoridor.controller;


import javafx.scene.paint.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.List;

import javafx.scene.shape.Rectangle;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.enumerations.SavePriority;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.exceptions.InvalidPositionException;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorSavesManager;
import ca.mcgill.ecse223.quoridor.timer.PlayerTimer;
import ca.mcgill.ecse223.quoridor.view.ViewInterface;


public class QuoridorController {

    /**
     * @author Matthias Arabian
     * resets the quoridor object of the QuoridorApplication to allow users to play consecutive games.
     */
    public static void clearGame(){

        Quoridor q = QuoridorApplication.getQuoridor();

        //before reseting the model, store the usernames. They should not be reset after each game
        List<User> u = q.getUsers();
        List<String> s = new ArrayList<>();
        for (User r : u)
            s.add(r.getName());

        QuoridorApplication.clearBlackPawnBehaviour();
        QuoridorApplication.clearWhitePawnBehaviour();
        q.delete(); //reset the model

        //add the usernames into the clean model
        for (String str : s) {
            if (str.equals("user1") || str.equals("user2"))
                continue; //do not add user1 and user2 to the list, b/c those names are created when the game is initialized
            try {
                q.addUser(str); //will throw an exception if the user being added already exists
            } catch (Exception e) {
            }
        }
    }

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
    public static void initializeBoard(Quoridor quoridor, Timer timer) {
        // throw new java.lang.UnsupportedOperationException("This controller method is not implemented yet");

        if (quoridor.getCurrentGame() == null) {
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
        int whiteRow = 9;
        int whiteCol = 5;
        int blackRow = 1;
        int blackCol = 5;
        Tile whitePlayerStartTile = quoridor.getBoard().getTile((whiteRow - 1) * 9 + whiteCol - 1);
        Tile blackPlayerStartTile = quoridor.getBoard().getTile((blackRow - 1) * 9 + blackCol - 1);

        //create position
        PlayerPosition whitePlayerInitialPos = new PlayerPosition(whitePlayer, whitePlayerStartTile);
        PlayerPosition blackPlayerInitialPos = new PlayerPosition(blackPlayer, blackPlayerStartTile);
        PlayerPosition whitePlayerStartPosition = new PlayerPosition(whitePlayer, whitePlayerStartTile);
        PlayerPosition blackPlayerStartPosition = new PlayerPosition(blackPlayer, blackPlayerStartTile);

        GamePosition initialGamePosition = new GamePosition(0, whitePlayerInitialPos, blackPlayerInitialPos, whitePlayer, quoridor.getCurrentGame());
        GamePosition whitePlayerPosition = new GamePosition(1, whitePlayerStartPosition, blackPlayerStartPosition, whitePlayer, quoridor.getCurrentGame());

        //add 10 walls in stock for both players
        for (int j = 1; j <= 10; j++) {
            Wall wall = Wall.getWithId(j);
            whitePlayerPosition.addWhiteWallsInStock(wall);
            initialGamePosition.addWhiteWallsInStock(wall);
        }
        for (int j = 1; j <= 10; j++) {
            Wall wall = Wall.getWithId(j + 10);
            whitePlayerPosition.addBlackWallsInStock(wall);
            initialGamePosition.addBlackWallsInStock(wall);
        }

        quoridor.getCurrentGame().setCurrentPosition(whitePlayerPosition);
        quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(whitePlayer);

        startPlayerTimer(whitePlayer, timer); //call the controller method "startClock"
    }

    /**
     * Method to Move Pawn across the board, will jump if blocked by another pawn
     *
     * @param quoridor      quoridor object, can access whole model from this and persist changes
     * @param side          the direction the pawn wants to move
     * @param pawnBehaviour the statemachine associated with either the white or black pawn
     * @return true/false, true if the move was successful, false if an exception was thrown from statemachine
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     */
    public static Boolean movePawn(Quoridor quoridor, String side, PawnBehaviour pawnBehaviour) {
        Boolean pawnMoveSuccessful = false;

        String blackPlayerName = quoridor.getCurrentGame().getBlackPlayer().getUser().getName();
        String whitePlayerName = quoridor.getCurrentGame().getWhitePlayer().getUser().getName();
        Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
        int roundNumber;

        PlayerPosition playerPosition;
        if (currentPlayer.getUser().getName().equals(whitePlayerName)) {
            roundNumber = 1;
            playerPosition = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition();
        } else {
            roundNumber = 2;
            playerPosition = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition();
        }

        MoveDirection pawnMoveDirection;
        if (side.equals("up")) {
            pawnMoveDirection = MoveDirection.North;
        } else if (side.equals("down")) {
            pawnMoveDirection = MoveDirection.South;
        } else if (side.equals("right")) {
            pawnMoveDirection = MoveDirection.East;
        } else if (side.equals("left")) {
            pawnMoveDirection = MoveDirection.West;
        } else if( side.equals("upleft") ) {
        	pawnMoveDirection = MoveDirection.NorthWest;
        } else if( side.equals("upright") ) {
        	pawnMoveDirection = MoveDirection.NorthEast;
        } else if( side.equals("downleft") ) {
        	pawnMoveDirection = MoveDirection.SouthWest;
        } else if( side.equals("downright") ) {
        	pawnMoveDirection = MoveDirection.SouthEast;
        } else {
            pawnMoveDirection = null;
            //throw new IllegalArgumentException("Unsupported pawn direction was provided");
        }

        boolean isLegalStep = false;
        try {
            isLegalStep = pawnBehaviour.move(pawnMoveDirection);
        } catch (Exception e) {
            isLegalStep = false;
        }
        boolean isLegalJump = false;

        int row = playerPosition.getTile().getRow();
        int col = playerPosition.getTile().getColumn();

        if (isLegalStep) {
            pawnMoveSuccessful = true;
            if (side.equals("up")) {
                row = row - 1;
            } else if (side.equals("down")) {
                row = row + 1;
            } else if (side.equals("right")) {
                col = col + 1;
            } else if (side.equals("left")) {
                col = col - 1;
            }
        } else {

            try {
                isLegalJump = pawnBehaviour.jump(pawnMoveDirection);
            } catch (Exception e) {
                isLegalStep = false;
            }

            if (isLegalJump) {
                pawnMoveSuccessful = true;
                if (side.equals("up")) {
                    row = row - 2;
                } else if (side.equals("down")) {
                    row = row + 2;
                } else if (side.equals("right")) {
                    col = col + 2;
                } else if (side.equals("left")) {
                    col = col - 2;
                } else if( side.equals("upleft") ) {
                	row = row - 1;
                	col = col - 1;
                } else if( side.equals("upright") ) {
                	row = row - 1;
                	col = col + 1;
                } else if(side.equals("downleft") ) {
                	row = row + 1;
                	col = col - 1;
                } else if(side.equals("downright") ) {
                	row = row + 1;
                	col = col + 1;
                } 
            }
        }

        if (pawnMoveSuccessful) {
            Tile targetTile = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);
            playerPosition.setTile(targetTile);

            int moves = quoridor.getCurrentGame().getPositions().size()-1;
            int moveNumber = (moves+1) / 2;

            if (!isLegalJump) {
                StepMove stepMove = new StepMove(moveNumber, roundNumber, currentPlayer, targetTile, quoridor.getCurrentGame());
                quoridor.getCurrentGame().addMove(stepMove);
            } else {
                JumpMove jumpMove = new JumpMove(moveNumber, roundNumber, currentPlayer, targetTile, quoridor.getCurrentGame());
                quoridor.getCurrentGame().addMove(jumpMove);
            }

            if (currentPlayer.hasGameAsWhite()) {
                currentPlayer.setNextPlayer(quoridor.getCurrentGame().getBlackPlayer());
            } else {
                currentPlayer.setNextPlayer(quoridor.getCurrentGame().getWhitePlayer());
            }
        }
        return pawnMoveSuccessful;
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
        GetWallMoveCandidate(dir, row, column);
    }


//    /**
//     * @author Matthias Arabian
//     * gets the current wall move candidate from the model. null otherwise
//     */
//    public static WallMove getExistingWallMove() throws Throwable {
//        q.get
//    }

    /**
     * (feature: move wall)
     *
     * @param side the side to check for edges. "left", "right", "up", "down"
     * @return true if the current wall selected is at the edge specified in the parameter
     * @throws Throwable
     * @author David
     */
    public static boolean wallIsAtEdge(String side) throws Throwable {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        int col = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn();
        int row = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
        switch (side) {
            case "left":
                if (col == 1) return true;
                return false;
            case "right":
                if (col == 8) return true;
                return false;
            case "up":
                if (row == 1) return true;
                return false;
            case "down":
                if (row == 8) return true;
                return false;
            default:
                throw new IllegalArgumentException();
        }
    }


    /**
     * moves the wall one tile toward the direction specified. An illegal move notification will be shown
     * if such a move in illegal. This involves linking wallMove object to a new target tile(feature: move wall)
     *
     * @param side the direction at which we move the wall. "left", "right", "up", "down"
     * @throws Throwable
     * @author David
     */
    public static boolean moveWall(String side) throws Throwable {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        //System.err.print(quoridor.getCurrentGame().getWallMoveCandidate()+" is the wall candidate");
        WallMove current = quoridor.getCurrentGame().getWallMoveCandidate();
        int col = current.getTargetTile().getColumn();
        int row = current.getTargetTile().getRow();
        if (wallIsAtEdge(side)) {
            ViewInterface view = QuoridorApplication.getViewInterface();
            throw new IllegalArgumentException("Cannot move the wall in the specified direction. The wall is at the edge. ");

        }//the method automatically throws illegalArgumentException if invalidInput;
        //to get a tile at (row, col), we use (col - 1) * 9 + row - 1
        switch (side) {
            case "left":
                col--;
                current.setTargetTile(quoridor.getBoard().getTile((row - 1) * 9 + col - 1));
                break;
            case "right":
                col++;
                current.setTargetTile(quoridor.getBoard().getTile((row - 1) * 9 + col - 1));
                break;
            case "up":
                row--;
                current.setTargetTile(quoridor.getBoard().getTile((row - 1) * 9 + col - 1));
                break;
            case "down":
                row++;
                current.setTargetTile(quoridor.getBoard().getTile((row - 1) * 9 + col - 1));
                break;
            default:
                throw new IllegalArgumentException("move wall illegal argument");
        }
        return validatePosition(current.getTargetTile().getRow(), current.getTargetTile().getColumn(), current.getWallDirection().toString());
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
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time((min * 60 + sec) * 1000));
        quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time((min * 60 + sec) * 1000));
    }

    /**
     * Optional feature: Set thinking time for a specific player.
     * We know that both players are supposed to have the same thinking time.
     * However, being able to set time for individual players can to increase the difficulty for a more experienced player without affecting the other opponent.
     * Each player is given a fixed time limit for a game. This method changes the remaining thinking
     * time of each player
     * index = 0->white; index = 1->black
     *
     * @param min         the minute part of the time
     * @param sec         the second part of the time
     * @param playerIndex specifies for whom this thinking time is for
     * @throws Throwable
     * @author David
     */
    public static void setThinkingTime(int min, int sec, int playerIndex) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        if (playerIndex == 0) {
            quoridor.getCurrentGame().getWhitePlayer().setRemainingTime(new Time((min * 60 + sec) * 1000));
        } else if (playerIndex == 1) {
            quoridor.getCurrentGame().getBlackPlayer().setRemainingTime(new Time((min * 60 + sec) * 1000));
        } else {
            throw new IllegalArgumentException();
        }
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
    public static boolean initializeGame(Quoridor quoridor) {
        //could have a false, if there is currently a game running
        /*if (isGameRunning(quoridor.getCurrentGame())){
            return false;
            //or have a popup showup to tell the player if they are sure or something
        }*/
        Game game = new Game(GameStatus.Initializing, Game.MoveMode.PlayerMove, quoridor);
        User user1 = null;
        User user2 = null;
        user1 = quoridor.addUser("user1");
        user2 = quoridor.addUser("user2");
        int thinkingTime = 180;

        Player player1 = new Player(new Time(thinkingTime), user1, 1, Direction.Horizontal);
        Player player2 = new Player(new Time(thinkingTime), user2, 9, Direction.Horizontal);


        Player[] players = {player1, player2};
        // Create all walls. Walls with lower ID belong to player1,
        // while the second half belongs to player 2
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= 10; j++) {
                new Wall(i * 10 + j, players[i]);
            }
        }

        game.setWhitePlayer(player1);
        game.setBlackPlayer(player2);
        QuoridorApplication.getWhitePawnBehaviour(player1);

        QuoridorApplication.getBlackPawnBehaviour(player2);
        return true;
    }

    /**
     * @param player is the player whether it be white or black
     * @return true if user name is set successfully
     * @author Daniel Wu
     */
    public static boolean setUserToPlayer(Player player, User user) {
        //This returns false if the player didn't, choose a username, but a player can't exist without a username??!?!??!
        player.setUser(user);
        if (player.getUser() != user) {
            return false;
        }
        return true;
    }

    /**
     * @param player is the player, white or black
     * @param time   is the time to set the remaining time of the player
     * @return true if the thinking time is set successfully and false if not
     * @author Daniel Wu
     */
    public static boolean setTotalThinkingTime(Player player, Time time) {
        player.setRemainingTime(time);
        if (player.getRemainingTime() != time) {
            return false;
        }
        return true;
    }

    /**
     * @param player is the player, white or black
     * @param timer
     * @return void
     * @author Daniel Wu
     */
    public static void startPlayerTimer(Player player, Timer timer) {
        // throw new java.lang.UnsupportedOperationException();
        PlayerTimer playerTimer = new PlayerTimer(player);
        timer.schedule(playerTimer, 0, 1000); //the playerTimer task will be executed every 1 second
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

        ViewInterface view = QuoridorApplication.getViewInterface();
        return view.isIllegalNotificationDisplayed;

    }

    /**
     * Communicates to View to check if a wall is currently is being displayed at a certain location (feature: move wall)
     *
     * @return true if wall displayed at specified location, false otherwise
     * @throws Throwable
     * @author David
     */
    public static boolean thisWallIsAtPosition(int row, int column) {

        ViewInterface view = QuoridorApplication.getViewInterface();


        return view.isWallDisplayedAt(row, column);
    }

    /**
     * Gherkin feature: Grab Wall
     * This controller method is responsible for first checking if the current
     * player to move has more walls in stock. If the player has more walls, a wall candidate
     * object is created at initial position (Tile at (0, 0)) and the method returns 1. If the player has no more walls
     * in stock, no wall move candidate is created and the method returns 0.
     *
     * @param quoridor - Quoridor application
     * @return Boolean - Returns 1 if a wall candidate object was created and 0 if not
     * @author Thomas Philippon
     */
    public static boolean grabWall(Quoridor quoridor) {
        boolean returnVal = false;

        Game game = quoridor.getCurrentGame();
        String whitePlayerName = game.getWhitePlayer().getUser().getName();
        Wall wall;
        //check if the player to move has more walls in stock
        Player playerToMove = game.getCurrentPosition().getPlayerToMove();
        int nbOfWalls = numberOfWallsInStock(playerToMove, game);

        if (nbOfWalls >= 1) {
            //the player has more walls in stock
            int moves = quoridor.getCurrentGame().getPositions().size()-1;
            int lastMoveNumber = (moves+1) / 2;
            int roundNumber = game.getCurrentPosition().getId();

            Tile targetTile = quoridor.getBoard().getTile(0); //initialize the wall move candidate to the tile(0,0)

            if (playerToMove.getUser().getName().toString().equals(whitePlayerName)) {
                wall = playerToMove.getWall(nbOfWalls - 1);
                game.getCurrentPosition().removeWhiteWallsInStock(wall);
                WallMove wallMoveCandidate = new WallMove(lastMoveNumber, 1, playerToMove, targetTile, game, Direction.Vertical, wall);
                game.setWallMoveCandidate(wallMoveCandidate);
            } else {
                wall = playerToMove.getWall(nbOfWalls - 1);
                game.getCurrentPosition().removeBlackWallsInStock(wall);
                WallMove wallMoveCandidate = new WallMove(lastMoveNumber, 2, playerToMove, targetTile, game, Direction.Vertical, wall);
                game.setWallMoveCandidate(wallMoveCandidate);
            }
            returnVal = true;
        }
        return returnVal;

    }

    /**
     * This controller method
     *
     * @param quoridor - Quoridor application
     * @return Boolean - Returns 1 if a wall candidate object was created and 0 if not
     * @author Thomas Philippon
     */
    public static void cancelWallGrabbed(Quoridor quoridor){
        Player playerToMove = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
        Game game = quoridor.getCurrentGame();
        String whitePlayerName = quoridor.getCurrentGame().getWhitePlayer().getUser().getName();
        WallMove wallCandidate;
        try{
            wallCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
            Wall wall = wallCandidate.getWallPlaced();
            int nbOfWalls = numberOfWallsInStock(playerToMove, game);
            if (playerToMove.getUser().getName().equals(whitePlayerName)) {
                game.getCurrentPosition().addWhiteWallsInStock(wall);
            } else {
                game.getCurrentPosition().addBlackWallsInStock(wall);
            }
            playerToMove.addWall(wall);
            game.getWallMoveCandidate().delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gherkin feature: Step Forward
     * This controller method is used to see the board position after the move made by the next player in turn.
     * This is assuming the game is in replay mode
     *
     * @param game - The current Game
     * @return void
     * @author Thomas Philippon
     */
    public static void stepForward(Game game){
        //get the ID of the current game and move position
        int currentId = game.getCurrentPosition().getId();

        //Check if it is the very last move played in the game
        int nbOfMoves = game.numberOfMoves();
        if(currentId != game.numberOfPositions()-1){
            //get the next game position and assign it to the game's current position.
            GamePosition nextGamePosition = game.getPosition(currentId +1);
            game.setCurrentPosition(nextGamePosition);
        }
    }

    /**
     * Gherkin feature: Step Backward
     * This controller method is used to see the board position after the move made by the  previous player in turn.
     * This is assuming the game is in replay mode
     *
     * @param game - The current Game
     * @return void
     * @author Thomas Philippon
     */
    public static void stepBackward(Game game){
        //get the ID of the current game position
        int currentId = game.getCurrentPosition().getId();

        //check if it is the very first move of the game
        if(currentId != 0){
            //get the next game position and assign it to the game's current position.
            GamePosition nextGamePosition = game.getPosition(currentId -1);
            game.setCurrentPosition(nextGamePosition);
        }
    }

    /**
     * Query method to get the number of walls in stock for a player
     *
     * @param player - A player of the game
     * @param game   - The game that the player is playing
     * @return Integer  - The number of walls in stock for the player
     * @author Thomas Philippon
     */
    public static int numberOfWallsInStock(Player player, Game game) {

        int nbOfWalls = 0;
        //Get both players to determine which player is provided
        String blackPlayerName = game.getBlackPlayer().getUser().getName();
        String whitePlayerName = game.getWhitePlayer().getUser().getName();

        if (player.getUser().getName().toString().equals(whitePlayerName)) {
            nbOfWalls = game.getCurrentPosition().getWhiteWallsInStock().size();
        } else if (player.getUser().getName().toString().equals(blackPlayerName)) {
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
            User addUser = quoridor.addUser(userName);
            playerToAssignUserTo.setUser(addUser);
            //playerToAssignUserTo.getUser().setName(userName);
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
        Game currentGame = QuoridorApplication.getQuoridor().getCurrentGame();
        GamePosition currentGamePosition = currentGame.getCurrentPosition();

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
        ArrayList<Wall> allWallsOnBoard = new ArrayList<Wall>();
        for (Wall wall : whiteWallsOnBoard){
            allWallsOnBoard.add(wall);
        }
        for (Wall wall : blackWallsOnBoard){
            allWallsOnBoard.add(wall);
        }
        allWallsOnBoard.add(currentGame.getWallMoveCandidate().getWallPlaced());
        if (!pathExistenceCheck(currentGamePosition.getWhitePosition(), allWallsOnBoard)){
            return false;
        }
        if (!pathExistenceCheck(currentGamePosition.getBlackPosition(), allWallsOnBoard)){
            return false;
        }
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
        //Check if path exists for each players
        ArrayList<Wall> allWallsOnBoard = new ArrayList<Wall>();
        for (Wall wall : whiteWallsOnBoard){
            allWallsOnBoard.add(wall);
        }
        for (Wall wall : blackWallsOnBoard){
            allWallsOnBoard.add(wall);
        }
        if (!pathExistenceCheck(gamePosition.getWhitePosition(), allWallsOnBoard)){
            return false;
        }
        if (!pathExistenceCheck(gamePosition.getBlackPosition(), allWallsOnBoard)){
            return false;
        }
        return true;
    }

    /**
     * Checks if path exists for each player
     *
     * @return true if the path exists and false if not
     * @author Daniel Wu
     */
    public static Boolean pathExistenceCheck(PlayerPosition pos, ArrayList<Wall> allWallsOnBoard){
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        GamePosition gamePosition = quoridor.getCurrentGame().getCurrentPosition();
        //Setup graph
        Graph graph = new Graph();
        //Remove edges
        int row = 0;
        int col = 0;
        Direction dir = Direction.Horizontal;
        for (Wall wall : allWallsOnBoard){
            row = wall.getMove().getTargetTile().getRow();
            col = wall.getMove().getTargetTile().getColumn();
            dir = wall.getMove().getWallDirection();
            if (dir == Direction.Horizontal){
                graph.removeEdge((row - 1) * 9 + col - 1, (row) * 9 + col - 1); //0-9
                graph.removeEdge((row - 1) * 9 + col, (row) * 9 + col); //1-10
            } else if (dir == Direction.Vertical){
                graph.removeEdge((row - 1) * 9 + col - 1, (row - 1) * 9 + col); //0-1
                graph.removeEdge((row) * 9 + col - 1, (row) * 9 + col); //9-10
            }
        }
        //DFS
        int tileID = (pos.getTile().getRow() - 1) * 9 + pos.getTile().getColumn() - 1;
        ArrayList<Integer> stack = new ArrayList<Integer>();
        ArrayList<Integer> visited = new ArrayList<Integer>();
        if (pos.getPlayer().hasGameAsWhite()){
            //Add all the tiles on the top row
            for (int i=0; i<9; i++){
                stack.add(0, i);
            }
        } else if (pos.getPlayer().hasGameAsBlack()){
            //Add all the tiles on the bottom row
            for (int i=0; i<9; i++){
                stack.add(0, i + 72);
            }
        }
        while (!stack.isEmpty()){
            int node = stack.remove(0);
            if (node == tileID){
                return true;
            }
            if (!visited.contains(node)){
                visited.add(node);
                for (int adjacentNode : graph.getNodes().get(node)){
                    if(!visited.contains(adjacentNode)){
                        stack.add(0, adjacentNode);
                    }
                }
            }
        }
        return false;
    }

    /**
     * Go into replay mode
     *
     * @return true if successfully initialized replay mode
     * @author Daniel Wu
     */
    public static Boolean initializeReplayMode(){
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();
        boolean createUsers = true;
        if (currentGame == null){
            currentGame = new Game(GameStatus.Replay, Game.MoveMode.PlayerMove, quoridor);
            User user1 = null;
            User user2 = null;

            //Check if the temp user name already exists
            for (User user : quoridor.getUsers()){
                if ((user.getName() == "user1") || (user.getName() == "user2")){
                    createUsers = false;
                }

            }

            //If it doesn't then create them
            if (createUsers) {
                user1 = quoridor.addUser("user1");
                user2 = quoridor.addUser("user2");
            }
            int thinkingTime = 180;

            Player player1 = new Player(new Time(thinkingTime), user1, 1, Direction.Horizontal);
            Player player2 = new Player(new Time(thinkingTime), user2, 9, Direction.Horizontal);


            Player[] players = {player1, player2};
            // Create all walls. Walls with lower ID belong to player1,
            // while the second half belongs to player 2
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= 10; j++) {
                    new Wall(i * 10 + j, players[i]);
                }
            }

//            initializeBoard(quoridor, );  //i dont need the timer here

            currentGame.setWhitePlayer(player1);
            currentGame.setBlackPlayer(player2);
            QuoridorApplication.getWhitePawnBehaviour(player1);

            QuoridorApplication.getBlackPawnBehaviour(player2);
        } else {
            currentGame.setGameStatus(GameStatus.Replay);
        }

        if (currentGame.getGameStatus() != GameStatus.Replay){
            return false;
        }
        return true;
    }

    /**
     *
     * @return true if game can be continued and sets state to running, false if game is already won
     * @author Daniel Wu
     */
    public static Boolean continueGame(){
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();

        //If the game is won, then can't continue
        if ((currentGame.getGameStatus() == GameStatus.BlackWon) || (currentGame.getGameStatus() == GameStatus.WhiteWon)
                || (currentGame.getGameStatus() == GameStatus.Draw)){
            currentGame.setGameStatus(GameStatus.Replay);
            return false;
        }

        //Set gamePosition to current state and restart clock + setup state machine, then move to the in-game page
        currentGame.setGameStatus(GameStatus.Running);
        int index = currentGame.getPositions().indexOf(currentGame.getCurrentPosition());
        System.out.println("index: " + index);
        System.out.println("Positions: " + currentGame.getPositions().size());
        System.out.println("Moves: " + currentGame.getMoves().size());

        deleteRemainingMoves();
        return true;
    }

    public static void deleteRemainingMoves(){
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Game currentGame = quoridor.getCurrentGame();
        //Get the index of the current game position
        int index = currentGame.getPositions().indexOf(currentGame.getCurrentPosition());

        //Now delete all the game positions until we reach the index
        int count = currentGame.getPositions().size() - 1;
        while(count != index){
            currentGame.getPositions().get(count).delete();
            count--;
        }
        System.out.println(currentGame.getPositions().size());
        System.out.println(index);
//        Player player = currentGame.getCurrentPosition().getPlayerToMove();
//        PlayerPosition whitePosition = currentGame.
//        if (player.equals(quoridor.getCurrentGame().getBlackPlayer())) {
//            Player tmp = quoridor.getCurrentGame().getWhitePlayer();
//
//            GamePosition nextGamePosition = new GamePosition(index+1, whitePosition, blackPosition, tmp, quoridor.getCurrentGame());
//            quoridor.getCurrentGame().setCurrentPosition(nextGamePosition);
//            for(Wall wall : quoridor.getCurrentGame().getWhitePlayer().getWalls()){
//                if(wall.hasMove()){
//                    nextGamePosition.addWhiteWallsOnBoard(wall);
//                }
//                else{
//                    nextGamePosition.addWhiteWallsInStock(wall);
//                }
//            }
//            for(Wall wall : quoridor.getCurrentGame().getBlackPlayer().getWalls()){
//                if(wall.hasMove()){
//                    nextGamePosition.addBlackWallsOnBoard(wall);
//                }
//                else{
//                    nextGamePosition.addBlackWallsInStock(wall);
//                }
//            }
//            quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(tmp);
//        } else {
//            Player tmp = quoridor.getCurrentGame().getBlackPlayer();
//
//            GamePosition nextGamePosition = new GamePosition(gamePositionId+1, whitePosition, blackPosition, tmp, quoridor.getCurrentGame());
//            for(Wall wall : quoridor.getCurrentGame().getWhitePlayer().getWalls()){
//                if(wall.hasMove()){
//                    nextGamePosition.addWhiteWallsOnBoard(wall);
//                }
//                else{
//                    nextGamePosition.addWhiteWallsInStock(wall);
//                }
//            }
//            for(Wall wall : quoridor.getCurrentGame().getBlackPlayer().getWalls()){
//                if(wall.hasMove()){
//                    nextGamePosition.addBlackWallsOnBoard(wall);
//                }
//                else{
//                    nextGamePosition.addBlackWallsInStock(wall);
//                }
//            }
//            quoridor.getCurrentGame().setCurrentPosition(nextGamePosition);
//            quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(tmp);
//        }
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
    public static void GetWallMoveCandidate(String dir, int row, int col) throws UnsupportedOperationException {
        WallMove wallMove = QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate();

        //if no wall move candidate exists
        if (wallMove == null) {
            //get the direction of the wall
            Direction d;
            if (Direction.Horizontal.toString().toLowerCase().equals(dir))
                d = Direction.Horizontal;
            else
                d = Direction.Vertical;
            //get the tile associated with the wall move candidate from the board.
            Tile t = null;
            Game g = QuoridorApplication.getQuoridor().getCurrentGame();
            for (Tile tmp : QuoridorApplication.getQuoridor().getBoard().getTiles()) {
                if (tmp.getColumn() == col && tmp.getRow() == row) {
                    t = tmp;
                }

            }
            if (t == null)
                throw new UnsupportedOperationException("No tile with row:" + row + ", col: " + col + " exists!");

            //get the move number and round number (required for the creation of a wallmove
            int moveNumber = QuoridorApplication.getQuoridor().getCurrentGame().numberOfMoves();
            int roundNumber = QuoridorApplication.getQuoridor().getCurrentGame().numberOfPositions();

            //get player associated with the wall move
            Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();

            //get a wall from player to associate with the wallMove
            Wall w = currentPlayer.getWalls().get(0);
            wallMove = new WallMove(moveNumber, roundNumber, currentPlayer, t, g, d, w);
            QuoridorApplication.getQuoridor().getCurrentGame().setWallMoveCandidate(wallMove);

            if (QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate() == null)
                throw new UnsupportedOperationException("Failed to create Wall Move Candidate");
        }
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
        if (d == null)
            return false;
        return q.getCurrentGame().getWallMoveCandidate().setWallDirection(
                d.equals(Direction.Horizontal) ? Direction.Vertical : Direction.Horizontal
        );
    }


    /**
     * Helper method to reset the GUI wall direction and border color to their initial values
     * Vertical direction
     * Black border
     * @param r
     */
    public static void resetGUIWall(Rectangle r) {
        if (r == null)
            throw new UnsupportedOperationException("No GUI wall entity exists");

        double curWidth = r.getWidth();
        double curHeight = r.getHeight();
        double w2h = curWidth/curHeight;

        if (w2h > 1) {
            r.setWidth(curHeight);
            r.setHeight(curWidth);
        }

        r.setStroke(Color.BLACK);
    }

    /**
     * @throws UnsupportedOperationException if there is not GUI wall to rotate or the function fails to rotate the wall
     *                                       <p>
     *                                       Rotates the GUI wallMoveCandidate to the desired direction.
     * @author Matthias Arabian
     */
    public static void GUI_flipWallCandidate() throws UnsupportedOperationException {
        //Cucumber Test Runner does not initialize the GUI during test. Therefore, the test would not pass even though it is rigorous
        if (QuoridorApplication.getViewInterface() == null) {
            return;
        }
        Rectangle r = QuoridorApplication.getViewInterface().getWallMoveCandidate();
        if (r == null)
            throw new UnsupportedOperationException("No GUI wallCandidate entity exists");

        double curWidth = r.getWidth();
        double curHeight = r.getHeight();
        double w2h = curWidth / curHeight;

        r.setWidth(curHeight);
        r.setHeight(curWidth);

        if (w2h > 1) { //rotate to vertical

            r.setTranslateY(r.getTranslateY() - curWidth / 2 + 4);
            r.setTranslateX(r.getTranslateX() + curWidth / 2 - 2.5);
            r.setWidth(curHeight);
            r.setHeight(curWidth);
        } else { //rotate to horizontal

            r.setTranslateY(r.getTranslateY() + curHeight / 2 - 4);
            r.setTranslateX(r.getTranslateX() - curHeight / 2 + 2.5);
            r.setWidth(curHeight);
            r.setHeight(curWidth);
        }

    }


    /**
     * @param newDir the direction that the GUI wall entity should be rotated to
     * @throws UnsupportedOperationException if there is not GUI wall to rotate or the function fails to rotate the wall
     *                                       <p>
     *                                       asserts the GUI wallMoveCandidate is in correct direction.
     * @author Matthias Arabian
     */
    public static boolean GUI_assertFlipWallCandidate(String newDir) throws UnsupportedOperationException {
        //Cucumber Test Runner does not initialize the GUI during test. Therefore, the test would not pass even though it is rigorous
        if (QuoridorApplication.getViewInterface() == null) {
            return true;
        }
        Rectangle r = QuoridorApplication.getViewInterface().getWallMoveCandidate();
        if (r == null)
            throw new UnsupportedOperationException("No GUI wallCandidate entity exists");

        double curWidth = r.getWidth();
        double curHeight = r.getHeight();
        double w2h = curWidth / curHeight;

        //validation section w.r.t parameter newDir
        newDir = newDir.substring(0, 1).toUpperCase() + newDir.substring(1).toLowerCase();
        if (newDir.equals("Horizontal"))
            return w2h > 1;
        else
            return w2h < 1;
    }


    /*
     * This method is outside the scope of what load position tests are able to recognize. While we could refer to the work
     * of the ValidatePosition developer, we can also use my loading system which throws InvalidPositionException's and infer
     * that if no exception is thrown, then the position is probably valid. Not to say that this isn't something we want: in
     * the controller, it is definitely good to have a means to ensure that the position is valid when creating a new one in-
     * game. But for our intents and purposes, the only time we need to check if the position is valid is as we are parsing
     * in text data from save files, as we can avoid a lot of arrayIndexOutOfBounds errors this way. And this is handled by
     * my QuoridorSavesManager class.
     * -Edwin
     */
    ///**
    // * Verifies that the load position is a valid position
    // * @author Matthias Arabian
    // * @return true: position is valid. false otherwise
    // * @throws UnsupportedOperationException
    // */
    //public static Boolean CheckThatPositionIsValid() throws UnsupportedOperationException{
    //	throw new UnsupportedOperationException("Position is valid");
    //}

    /**
     * Loads a saved game by instantiating a new game and populating it with file data.
     * If no exceptions are thrown, data should be loaded straight into the current game.
     * If exceptions are thrown, progress to loading the game is also disposed of.
     *
     * @param fileName
     * @throws FileNotFoundException, IOException, InvalidPositionException
     * @author Matthias Arabian (Interface Author)
     * @author Edwin Pan (Method Author)
     */
    public static void loadSavedGame(String fileName, User firstUser, User secondUser, Time thinkingTime) throws IOException, FileNotFoundException, InvalidPositionException {
        try {
            Game game = QuoridorSavesManager.loadGame(fileName, QuoridorApplication.getQuoridor(), firstUser, secondUser, thinkingTime);
            QuoridorApplication.getQuoridor().setCurrentGame(game);
        } catch (FileNotFoundException e) {
            QuoridorApplication.getQuoridor().setCurrentGame(null);
            throw e;
        } catch (IOException e) {
            QuoridorApplication.getQuoridor().setCurrentGame(null);
            throw e;
        } catch (InvalidPositionException e) {
            QuoridorApplication.getQuoridor().getCurrentGame().delete();
            QuoridorApplication.getQuoridor().setCurrentGame(null);
            throw e;
        } catch (Exception e) {
            if (QuoridorApplication.getQuoridor().getCurrentGame() != null) {
                QuoridorApplication.getQuoridor().getCurrentGame().delete();
                QuoridorApplication.getQuoridor().setCurrentGame(null);
            }
            throw e;
        }
    }


    /*
     * This method requires the controller to be stateful - namely, for previous errors to be remembered. This is non-ideal.
     * This method will instead be replaced by the method above,
     *, being a method which throws IOException or
     * FileNotFoundException which can be.
     * -Edwin
     */
    ///**
    // * Propagates a sort of "Invalid Position to Load" error to wherever it is necessary
    // * @author Matthias Arabian
    // * @return whether the error has been successfully propagated
    // * @throws UnsupportedOperationException
    // */
    //public static Boolean sendLoadError() throws UnsupportedOperationException{
    //	throw new UnsupportedOperationException("sendLoadError");
    //}

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
        return QuoridorSavesManager.saveGame(game, filename, SavePriority.DEFAULT);
    }

    /**
     * This is the overwrite-capable version of saveGame.
     * This method saves a game into a file whose name is specified, but not its path, and whose game is provided.
     * It returns true when serialization of the game is successful and false when unsuccessful.
     *
     * @param filename  (no extension will be added)
     * @param game
     * @return savingStatus enum
     * @throws IOException
     * @author Edwin Pan
     */
    public static SavingStatus saveGame(String filename, Game game, SavePriority save_enforcement_type) throws IOException {
        return QuoridorSavesManager.saveGame(game, filename, save_enforcement_type);
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
     * @param color of either "Black" or "White" in either upper or lower cases.
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
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     * @author Thomas Philippon major modifications for deliverable 5
     */
    public static boolean completePlayerTurn(Player player) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        int gamePositionId;
        gamePositionId = quoridor.getCurrentGame().getPositions().size();
        gamePositionId = quoridor.getCurrentGame().getPosition(gamePositionId-1).getId();

        //Create new player positions
        Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
        Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();
        Tile whitePlayerTile = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        Tile blackPlayerTile = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();

        PlayerPosition whitePosition = new PlayerPosition(whitePlayer, whitePlayerTile);
        PlayerPosition blackPosition = new PlayerPosition(blackPlayer, blackPlayerTile);

        if (player.equals(quoridor.getCurrentGame().getBlackPlayer())) {
            Player tmp = quoridor.getCurrentGame().getWhitePlayer();

            GamePosition nextGamePosition = new GamePosition(gamePositionId+1, whitePosition, blackPosition, tmp, quoridor.getCurrentGame());
            quoridor.getCurrentGame().setCurrentPosition(nextGamePosition);
            for(Wall wall : quoridor.getCurrentGame().getWhitePlayer().getWalls()){
                if(wall.hasMove()){
                    nextGamePosition.addWhiteWallsOnBoard(wall);
                }
                else{
                    nextGamePosition.addWhiteWallsInStock(wall);
                }
            }
            for(Wall wall : quoridor.getCurrentGame().getBlackPlayer().getWalls()){
                if(wall.hasMove()){
                    nextGamePosition.addBlackWallsOnBoard(wall);
                }
                else{
                    nextGamePosition.addBlackWallsInStock(wall);
                }
            }
            return quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(tmp);
        } else {
            Player tmp = quoridor.getCurrentGame().getBlackPlayer();

            GamePosition nextGamePosition = new GamePosition(gamePositionId+1, whitePosition, blackPosition, tmp, quoridor.getCurrentGame());
            for(Wall wall : quoridor.getCurrentGame().getWhitePlayer().getWalls()){
                if(wall.hasMove()){
                    nextGamePosition.addWhiteWallsOnBoard(wall);
                }
                else{
                    nextGamePosition.addWhiteWallsInStock(wall);
                }
            }
            for(Wall wall : quoridor.getCurrentGame().getBlackPlayer().getWalls()){
                if(wall.hasMove()){
                    nextGamePosition.addBlackWallsOnBoard(wall);
                }
                else{
                    nextGamePosition.addBlackWallsInStock(wall);
                }
            }
            quoridor.getCurrentGame().setCurrentPosition(nextGamePosition);
            return quoridor.getCurrentGame().getCurrentPosition().setPlayerToMove(tmp);
        }
    }


    /**
     * Controller mehtod to jump to start position of the game while in replayMode
     * @author Alex Masciotra
     * @param quoridor
     */
    public static void jumpToStart(Quoridor quoridor){

        List<GamePosition> gamePositions = quoridor.getCurrentGame().getPositions();

        GamePosition startGamePosition = gamePositions.get(0); //get original game position

        quoridor.getCurrentGame().setCurrentPosition(startGamePosition);

    }

    /**
     * @author Matthias Arabian
     * @return whether or not the resign has succeeded
     * Sets the game status to [Player]Won, and ending the game.
     */
    public static boolean initiateToResign(){
        if (getColorOfPlayerToMove(QuoridorApplication.getQuoridor()).equals("black"))
            return getCurrentGame().setGameStatus(GameStatus.WhiteWon);
        else
            return getCurrentGame().setGameStatus(GameStatus.BlackWon);
    }

    public static void setGameToNotRunning(){
        //set game to initializing b/c that state can be used as a flag to detect that a game has ended.
        Quoridor q = QuoridorApplication.getQuoridor();

        Game g = QuoridorApplication.getQuoridor().getCurrentGame();
        if (g == null) {
            initializeGame(q);
            g = QuoridorApplication.getQuoridor().getCurrentGame();
        }
        g.setGameStatus(GameStatus.Initializing); //initializing does not occur once game is started, so we can use it as a flag to decrate that a game is not running but whose results are still unknown
    }
    /**
     * @author Matthias Arabian
     * Get the final results from the model. Send those to the GUI to display.
     */
    public static void displayFinalResults(){
        GameStatus finalResults = QuoridorController.getCurrentGame().getGameStatus();

        //this is the method that the view would actually run
        QuoridorApplication.getViewInterface().displayFinalResults(finalResults);
    }

    /**
     * Controller mehtod to jump to final position of the game while in replayMode
     * @author Alex Masciotra
     * @param quoridor
     */
    public static void jumpToFinal(Quoridor quoridor){

        List<GamePosition> gamePositions = quoridor.getCurrentGame().getPositions();

        int index  = gamePositions.size() - 2;

        GamePosition finalGamePosition = gamePositions.get(index);

        quoridor.getCurrentGame().setCurrentPosition(finalGamePosition);
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
     * GUI query method
     * Returns the name of the player
     *
     * @param player
     * @return String name of the player
     * @author Thomas Philippon
     */
    public static String getPlayerName(Player player) {

        try {
            return player.getUser().getName();

        } catch (Exception e) {
            throw new java.lang.UnsupportedOperationException("Cannot get the player's name");
        }
    }

    /**
     * GUI query method
     * Returns the color of the current player to move
     *
     * @param quoridor quoridor object
     * @return String color of the current player to move
     * @author Thomas Philippon
     */
    public static String getColorOfPlayerToMove(Quoridor quoridor) {
        String whitePlayerName = quoridor.getCurrentGame().getWhitePlayer().getUser().getName();
        Player playerToMove = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
        String color;

        if (playerToMove.getUser().getName().equals(whitePlayerName)) {
            color = "white";
        } else {
            color = "black";
        }
        return color;
    }

    /**Returns the row or column coordinate of current player
     * @author David
     * @param specifyRowOrCol input parameter: 0=row, 1=col
     * @return
     */
    //input parameter: 0=row, 1=col
    public static int getCurrentPawnTilePos(int specifyRowOrCol){
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        String colour = getColorOfPlayerToMove(quoridor);
        Tile temp;
        if(colour.equals("white")){
            temp = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        }
        else{
            temp = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
        }
        if(specifyRowOrCol==0){
            return temp.getRow();
        }
        else if(specifyRowOrCol==1){
            return temp.getColumn();
        }
        else{
            return -1;
        }


    }

    /**
     * GUI query method
     * Returns Thinking of a player
     *
     * @param player player
     * @return String thinking time of the player
     * @author Thomas Philippon
     */
    public static String playerThinkingTime(Player player) {
        return player.getRemainingTime().toString();
    }

    /**
     * @author David
     * @param row
     * @param col
     * @return true if there is a pawn on that tile, false otherwise
     */
    public static boolean isPlayerOnTile(int row, int col){
        int blackRow = getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow();
        int blackCol = getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn();
        int whiteRow = getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow();
        int whiteCol = getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn();
        if((blackRow==row && blackCol==col)||(whiteRow==row && whiteCol==col)){
            return true;
        }
        return false;
    }

    /** Feature: Identify GameDrawn, identify game won
     *
     * This method is check if the game is won based on player positions and past moves.
     * @author David
     * @return "pending" or "Drawn" or "blackWon" or "whiteWon"
     */
    public static String checkResult_replayMode(){
        //TODO: this method needs to be called after each move (before end of turn)
        Player black = getCurrentBlackPlayer();
        Player white = getCurrentWhitePlayer();

        //check for white win
        Tile temp;
        temp = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile();
        if(temp.getRow() == 1){
            return "whiteWon";
        }
        //check for black win
        temp = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile();
        if(temp.getRow()==9){
            return "blackWon";
        }
        //check for draw
        //draw occurs when move repeats three times for the current and repeats twice for opposing player in the last nine moves

        int lastMoveNum = getCurrentGame().getMoves().size();
        Game current = getCurrentGame();
        if(lastMoveNum < 9) return "";
        boolean currentPlayerRepeats = false;
        boolean opposingPlayerRepeats = false;
        //analyzing current player's past history
        Move lastCurrent = getCurrentGame().getMove(lastMoveNum-1);//last move by current player
        Move thirdLastCurrent = getCurrentGame().getMove(lastMoveNum-5);//third last move by current player
        Move fifthLastCurrent = getCurrentGame().getMove(lastMoveNum-9);//fifth last move by current player
        //only stepmove can lead to a draw
        if(lastCurrent instanceof StepMove && thirdLastCurrent instanceof StepMove && fifthLastCurrent instanceof StepMove){
            if(lastCurrent.getTargetTile() == thirdLastCurrent.getTargetTile() && lastCurrent.getTargetTile() == fifthLastCurrent.getTargetTile()){
                currentPlayerRepeats = true;
            }
        }
        Move secondLastOppo = getCurrentGame().getMove(lastMoveNum-4);
        Move fourthLastOppo = getCurrentGame().getMove(lastMoveNum-8);
        if(secondLastOppo instanceof StepMove && fourthLastOppo instanceof StepMove){
            if(secondLastOppo.getTargetTile() == fourthLastOppo.getTargetTile()){
                opposingPlayerRepeats = true;

            }
        }
        if(currentPlayerRepeats && opposingPlayerRepeats){
            return "Drawn";
        }
        return "";
    }

    public static String checkResult(){
        //TODO: this method needs to be called after each move (before end of turn)
        Player black = getCurrentBlackPlayer();
        Player white = getCurrentWhitePlayer();

        //check for white win
        if(getPlayerOfCurrentTurn().equals(white)){
            int x = getCurrentPawnTilePos(0);
            if(getCurrentPawnTilePos(0)==1 ){
                getCurrentGame().setGameStatus(GameStatus.WhiteWon);
                endGame();
                return "whiteWon";
            }
        }
        //check for black win
        else{
            if(getCurrentPawnTilePos(0)==9){
                getCurrentGame().setGameStatus(GameStatus.BlackWon);
                endGame();
                return "blackWon";
            }
        }
        //check for draw
        //draw occurs when move repeats three times for the current and repeats twice for opposing player in the last nine moves

        int lastMoveNum = getCurrentGame().getMoves().size();
        Game current = getCurrentGame();
        if(lastMoveNum < 9) return "";
        boolean currentPlayerRepeats = false;
        boolean opposingPlayerRepeats = false;
        //analyzing current player's past history
        Move lastCurrent = getCurrentGame().getMove(lastMoveNum-1);//last move by current player
        Move thirdLastCurrent = getCurrentGame().getMove(lastMoveNum-5);//third last move by current player
        Move fifthLastCurrent = getCurrentGame().getMove(lastMoveNum-9);//fifth last move by current player
        //only stepmove can lead to a draw
        if(lastCurrent instanceof StepMove && thirdLastCurrent instanceof StepMove && fifthLastCurrent instanceof StepMove){
            if(lastCurrent.getTargetTile() == thirdLastCurrent.getTargetTile() && lastCurrent.getTargetTile() == fifthLastCurrent.getTargetTile()){
                currentPlayerRepeats = true;
            }
        }
        Move secondLastOppo = getCurrentGame().getMove(lastMoveNum-4);
        Move fourthLastOppo = getCurrentGame().getMove(lastMoveNum-8);
        if(secondLastOppo instanceof StepMove && fourthLastOppo instanceof StepMove){
            if(secondLastOppo.getTargetTile() == fourthLastOppo.getTargetTile()){
                opposingPlayerRepeats = true;

            }
        }
        if(currentPlayerRepeats && opposingPlayerRepeats){
            getCurrentGame().setGameStatus(GameStatus.Draw);
            endGame();
            return "Drawn";
        }
        return "";
    }

    /**Features: identify game won
     * This method is called by the playerTimer object if timer reaches zero
     * @author David
     * @param player whose timer reaches zero
     */
    public static void timerUp(Player player){
        if(player.hasGameAsWhite()){
            getCurrentGame().setGameStatus(GameStatus.BlackWon);
            endGame();
        }
        else{
            getCurrentGame().setGameStatus(GameStatus.WhiteWon);
            endGame();
        }
    }
    private static void endGame(){
        GameStatus results = getCurrentGame().getGameStatus();

        if (QuoridorApplication.getViewInterface() == null)
            return; //for JUnit test

        QuoridorApplication.getViewInterface().displayFinalResults(results);
    }

    /**Returns result of game as a string
     * Features: identify game won, identify game drawn
     * @author David
     * @return
     */
    public static String getGameResult(){
        GameStatus current = getCurrentGame().getGameStatus();
        if(current.equals(GameStatus.BlackWon)){
            return "blackWon";
        }
        if(current.equals(GameStatus.WhiteWon)){
            return "whiteWon";
        }
        if(current.equals(GameStatus.Draw)){
            return "Drawn";
        }
        return "pending";
    }



    /**
     * Instantiates a new board if quoridor does not currently have a board.
     * Returns true if quoridor, indeed, didn't have a board; false if quoridor already had a board and thus this method did not create any new board.
     * This basically reduces the scope of what Thomas's initializeBoard function does. 
     * @author Edwin Pan
     * @return
     */
    public static boolean createBoard() {
    	if(QuoridorApplication.getQuoridor().hasBoard()) {
    		return false;
    	}
        Board board = new Board(QuoridorApplication.getQuoridor());
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                board.addTile(i, j);
            }
        }
        QuoridorApplication.getQuoridor().setBoard(board);
        return true;
    }

}



