package ca.mcgill.ecse223.quoridor.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.enumerations.SavePriority;
import ca.mcgill.ecse223.quoridor.exceptions.InvalidPositionException;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.User;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.*;

public class CucumberStepDefinitions {
	
	//private WallMove wallMoveCandidate = null; //dead code; never used.
	ArrayList<Player> myPlayers; //Used when trying to set the gameStatus to ReadyToStart as the players are not accessible
	int[] myCoordinate = {0,0}; //Used to store the row and column input from the given scenario
	String myDirection = ""; //Used to store the direction input from the given scenario
	boolean positionIsValid = false; //Used to check if the position was valid or not
	
	//Instance Variables for SavePosition tests
	private String testGameSaveFilename = "";
	private final int fileDataLength = 1000000;
	private char [] refFileData = new char [fileDataLength];	//Memory for storing data of one file for comparison.
	private char [] curFileData = new char [fileDataLength];	//Memory for storing data of another file for comparison.]
	
	//Instance Variables for LoadPosition tests
	private boolean failedToReadSaveFile = false;
	private boolean receivedInvalidPositionException = false;

	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		myPlayers = createUsersAndPlayers("user1", "user2");
	}

	@Given("^The game is running$")
	public void theGameIsRunning() {
		initQuoridorAndBoard();
		ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
		createAndStartGame(createUsersAndPlayers);
	}


	@And("^It is my turn to move$")
	public void itIsMyTurnToMove() throws Throwable {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().setPlayerToMove(currentPlayer);
	}

	@Given("The following walls exist:")
	public void theFollowingWallsExist(io.cucumber.datatable.DataTable dataTable) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		// keys: wrow, wcol, wdir
		Player[] players = { quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer() };
		int playerIdx = 0;
		int wallIdxForPlayer = 0;
		for (Map<String, String> map : valueMaps) {
			Integer wrow = Integer.decode(map.get("wrow"));
			Integer wcol = Integer.decode(map.get("wcol"));
			// Wall to place
			// Walls are placed on an alternating basis wrt. the owners
			//Wall wall = Wall.getWithId(playerIdx * 10 + wallIdxForPlayer);
			Wall wall = players[playerIdx].getWall(wallIdxForPlayer); // above implementation sets wall to null

			String dir = map.get("wdir");

			Direction direction;
			switch (dir) {
			case "horizontal":
				direction = Direction.Horizontal;
				break;
			case "vertical":
				direction = Direction.Vertical;
				break;
			default:
				throw new IllegalArgumentException("Unsupported wall direction was provided");
			}
			new WallMove(0, 1, players[playerIdx], quoridor.getBoard().getTile((wrow - 1) * 9 + wcol - 1), quoridor.getCurrentGame(), direction, wall);
			if (playerIdx == 0) {
				quoridor.getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addWhiteWallsOnBoard(wall);
			} else {
				quoridor.getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
				quoridor.getCurrentGame().getCurrentPosition().addBlackWallsOnBoard(wall);
			}
			wallIdxForPlayer = wallIdxForPlayer + playerIdx;
			playerIdx++;
			playerIdx = playerIdx % 2;
		}
		System.out.println();

	}
	
	@And("I do not have a wall in my hand")
	public void iDoNotHaveAWallInMyHand() {
		// GUI-related feature -- TODO for later
	}

	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}

	@Given("^A new game is initializing$")
	public void aNewGameIsInitializing() throws Throwable {
		initQuoridorAndBoard();
		//ArrayList<Player> players = createUsersAndPlayers("user1", "user2"); // dead code
		new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
	}
	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************
	
	/*
	 * Start new game step definition
	 */
	
	/**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Initiate a new game
	 */
    @When("A new game is being initialized")
    public void aNewGameIsBeingInitializing() throws java.lang.UnsupportedOperationException{
    	
    	QuoridorController.isGameInitializing(QuoridorApplication.getQuoridor().getCurrentGame());
    }
    
    /**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Initiate a new game
	 */
    @And("White player chooses a username")
    public void whitePlayerChosesAUsername() throws java.lang.UnsupportedOperationException{
    	QuoridorController.playerChoseUsername(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
    }
    
    /**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Initiate a new game
	 */
    @And("Black player chooses a username")
    public void blackPlayerChoosesAUsername() throws java.lang.UnsupportedOperationException{
    	QuoridorController.playerChoseUsername(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
    }
    
    /**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Initiate a new game
	 */
    @And("Total thinking time is set")
    public void totalThinkingTimeIsSet()  throws java.lang.UnsupportedOperationException{
    	Game game = QuoridorApplication.getQuoridor().getCurrentGame();
    	QuoridorController.thinkingTimeIsSet(game);
    }
    
    /**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Initiate a new game
	 */
    @Then("The game shall become ready to start")
    public void theGameShallBecomeReadyToStart() {
    	assertEquals(Game.GameStatus.ReadyToStart, QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
    }

    /**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Start clock
	 */
  	@Given("The game is ready to start")
  	public void theGameIsReadyToStart() {
  		//Code is taken from createAndStartGame(Arraylist<Player>) except with GameStatus.Running replaced with GameStatus.ReadyToStart
  		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
  		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);
	
		Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, quoridor);

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, myPlayers.get(0), game);
		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
			}
		
		game.setCurrentPosition(gamePosition);
  	}
  	
  	/**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Start clock
	 * starts the clock
	 */
  	@When("I start the clock")
  	public void iStartTheClock() throws java.lang.UnsupportedOperationException {
  		Player whitePlayer = QuoridorController.getCurrentWhitePlayer();
  		Player blackPlayer = QuoridorController.getCurrentBlackPlayer();
  		QuoridorController.startClock(whitePlayer, blackPlayer);
  	}
  	
  	/**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Start clock
	 * check if the game is running
	 */
  	@Then("The game shall be running")
  	public void theGameShallBeRunning() {
    	assertEquals(Game.GameStatus.Running, QuoridorApplication.getQuoridor().getCurrentGame().getGameStatus());
  	}
  	
  	/**
	 * @author Daniel Wu
	 * StartNewGame.feature - StartNewGame
	 * Scenario: Start clock
	 * check if the board is initialized
	 */
  	@Then("The board shall be initialized")
  	public void theBoardShallBeInitialized() {
  		Quoridor quoridor = QuoridorApplication.getQuoridor();
  		boolean boardInitialized = quoridor.getBoard() != null;
  		assertEquals(true, boardInitialized);
  	}
    
	//Initialize board feature
	/**
	 * @author Thomas Philippon
	 */
	@When("The initialization of the board is initiated")
	public void initializationOfBoardInitiated(){
		QuoridorController.initializeBoard(QuoridorApplication.getQuoridor());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@Then("It shall be white player to move")
	public void itShallBeWhitePlayerToMove() {
		assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer(),QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("White's pawn shall be in its initial position")
	public void whitesPawnShallBeInItsInitialPosition() {
		assertEquals(QuoridorApplication.getQuoridor().getBoard().getTile(36),QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("Black's pawn shall be in its initial position")
	public void blacksPawnshallBeInItsInitialPosition() {
		assertEquals(QuoridorApplication.getQuoridor().getBoard().getTile(44), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("All of White's walls shall be in stock")
	public void allOfWhitesWallsShallBeInStock() {
		//ask mentor about this one
		assertEquals(10, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("All of Black's walls shall be in stock")
	public void allOfBlacksWallsShallBeInStock() {
		//ask mentor about this one too
		assertEquals(10, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("White's clock shall be counting down")
	public void whitesClockShallBeCountingDown() {
		assertNotEquals("19:00:00", QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime().toString());	
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("It shall be shown that this is White's turn")
	public void itShallBeShownThatThisIsWhitesTurn() throws Throwable{
		//GUI step, will be implemented later on. TODO
	}
	
	//Grab wall feature
	/**
	 * @author Thomas Philippon
	 */
	@Given("I have more walls on stock")
	public void iHaveMoreWallsOnStock() {
		//10 walls are in stock for all players,
		//I implemented a query method in the controller that returns the number of walls in stock for both players
		//that query method can be used when refreshing the UI page to display the right amount of walls in stock 
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@Given("I have no more walls on stock")
	public void iHaveNoMoreWallsOnStock() {
		removeWalls(); //helper method
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@When("I try to grab a wall from my stock")
	public void iTryToGrabAWallFromMyStock() {
		QuoridorController.grabWall(QuoridorApplication.getQuoridor().getCurrentGame());	
	}

	/**
	 * common method
	 * @author Thomas Philippon
	 * @author Alex Masciotra 
	 */
	@Then("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard() throws Throwable{
		//As this is a GUI related step, it will be implemented later on
		//TODO 
	}

	/**
	 * @author Thomas Philippon
	 */
	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShallDisappearFromMyStock() {
		//The current player is assigned to the white player in the step definition of "It is my turn to move".
		assertEquals(9, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().numberOfWhiteWallsInStock());
	}

	/**
	 * @author Thomas Philippon
	 */
	@And("A wall move candidate shall be created at initial position")
	public void aWallMoveCandidateShallBeCreatedAtInitialPosition() {
		//Here is assume that the initial position is the tile located at (0,0)
		assertEquals(0,QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow());
		assertEquals(0,QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn());
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@Then("I shall be notified that I have no more walls")
	public void iShouldBeNotifiedThatIHaveNoMoreWalls() throws Throwable{
		//GUI related step, TODO
	}
	
	/**
	 * @author Thomas Philippon
	 */
	@And("I shall have no walls in my hand")
	public void iShallHaveNoWallsInMyHand() throws Throwable{
		//GUI related step, to implemented later. TODO
	}
	
	/**checks that a wall move candidate exists, otherwise create one and link to appropriate tile
	 * @author David
	 * @param dir orientation of wall
	 * @param row
	 * @param column
	 * @throws Throwable
	 */
	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void wallMoveCandidateExists(String dir, int row, int column) throws Throwable{
		QuoridorController.getWallMove(dir, row, column);
		
	}
	/**we will check the test cases provided to make sure it is true that they are not at the edge of the board
	 * @author David
	 * @param side
	 * @throws Throwable
	 */
	@And("The wall candidate is not at the {string} edge of the board")
	public void wallIsNotAtEdge(String side) throws Throwable{
		assertEquals(false, QuoridorController.wallIsAtEdge(side));
	}
	/**moves the wall one tile toward the direction specified. An illegal move notification will be shown
	 *  if such a move in illegal. This involves linking wallMove object to a new target tile
	 * @author David
	 * @param side
	 * @throws Throwable
	 */
	@When("I try to move the wall {string}")
	public void tryToMoveWall(String side) throws Throwable{
		QuoridorController.moveWall(side);
	}
	/**communicates to View to verify that the wall is indeed moved to a new position
	 * @author David
	 * @param row
	 * @param column
	 * @throws Throwable
	 */
	@Then("The wall shall be moved over the board to position \\({int}, {int})")
	public void thisWallIsAtPosition(int row, int column) throws Throwable{
		// GUI-related feature
		assertEquals(true, QuoridorController.thisWallIsAtPosition(row, column));
	}
	/**we obtain the current wallMove object and checks its direction, row, and column
	 * @author David
     * @author Matthias Arabian
	 * @param dir
	 * @param row
	 * @param column
	 */
	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void wallMoveCandidateExistsAt(String dir, int row, int column) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Tile tile = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile();
		
		assertEquals(Direction.valueOf(dir), quoridor.getCurrentGame().getWallMoveCandidate().getWallDirection());
		assertEquals(row, tile.getRow());
		assertEquals(column,tile.getColumn() );
	}
	/**we check the test case provided to see that it is indeed at the specified edge of the board
	 * @author David
	 * @param side
	 * @throws Throwable
	 */
	@And("The wall candidate is at the {string} edge of the board")
	public void wallIsAtEdge(String side) throws Throwable{
		assertEquals(true, QuoridorController.wallIsAtEdge(side));
	}
	/**asks the controller if a illegal move notification has been displayed
	 * @author David
	 */
	@Then("I shall be notified that my move is illegal")
	public void testIllegalMoveNotification() {
		assertEquals(true, QuoridorController.isIllegalMoveNotificationDisplayed());
	}
	
	
	///SET TOTAL THINKING TIME
	/**set total thinking time for both players
	 * @author David
	 * @param min
	 * @param sec
	 */
	@When("{int}:{int} is set as the thinking time")
	public void setThinkingTime(int min, int sec) {
		QuoridorController.setThinkingTime(min,sec);
	}
	/**get the current players from the model and check that they indeed have the specified remaining time
	 * @author David
	 * @param min
	 * @param sec
	 */
	@Then("Both players shall have {int}:{int} remaining time left")
	public void checkRemainingTime(int min, int sec) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		int totalMiliSeconds = (min*60+sec)*1000;//converts to miliseconds
		assertEquals(totalMiliSeconds, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime());
		assertEquals(totalMiliSeconds, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime());
		
	}
    
     /**
     * @param color
     * @author Alex Masciotra
     */
    @Given("Next player to set user name is {string}")
    public void nextPlayerToSetUserNameIs(String color) {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        Player nextPlayerWhite = game.getWhitePlayer();
        Player nextPlayerBlack = game.getBlackPlayer();

        // i am calling getWhitePlayer and assigning the nextPlayer to either white or black depending what color i need
        // the player to be, this way i can "persist" in the model which color to apply the username to
        if (color.equals("white")) {

            game.getWhitePlayer().setNextPlayer(nextPlayerWhite);

        } else if (color.equals("black")) {

            game.getWhitePlayer().setNextPlayer(nextPlayerBlack);
        } else {

            throw new IllegalArgumentException("Unsupported color was provided");
        }
    }

    /**
     * @author Alex Masciotra
     */
    @And("There is existing user {string}")
    public void thereIsExistingUser(String username) {

        QuoridorApplication.getQuoridor().addUser(username);
    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @And("There is no existing user {string}")
    public void thereIsNoExistingUser(String username) {
        QuoridorApplication.getQuoridor().addUser(username);
        List<User> userList = QuoridorApplication.getQuoridor().getUsers();
        for (User user : userList) {

            if (user.getName().equals(username)) {
                QuoridorApplication.getQuoridor().removeUser(user);
            }
        }
    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @When("The player selects existing {string}")
    public void thePlayerSelectsExisting(String username) {
        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        QuoridorController.selectExistingUserName(username, game);

    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @When("The player provides new user name: {string}")
    public void thePlayerProvidesNewUserName(String username) {
        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        QuoridorController.selectNewUserName(username, game);
    }

    /**
     * @param color
     * @param username
     * @author Alex Masciotra
     */
    @Then("The name of player {string} in the new game shall be {string}")
    public void theNameOfPlayerInTheNewGameShallBe(String color, String username) {

//		if(color.equals("white")){
//			assertEquals(username, QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getUser().getName());
//		}
//		else if(color.equals("black")){
//			assertEquals(username, QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getUser().getName());
//		}

        assertEquals(username, QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getNextPlayer().getUser().getName());

    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @Then("The player shall be warned that {string} already exists")
    public void thePlayerShallBeWarnedThatAlreadyExists(String username) {

        assertEquals(username, QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getNextPlayer().getUser().getName());

        //GUI notification that username is already existing and he will be that username
    }

    /**
     * @param color
     * @author Alex Masciotra
     */
    @And("Next player to set user name shall be {string}")
    public void nextPlayerToSetUserNameShallBe(String color) {

        //shall be same player
        Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getNextPlayer();

        //checking to see if the next player is white or black, and to see if it matches with what it should be
        if (currentPlayer.hasGameAsWhite()) {

            assertEquals(color, "white");
        } else if (currentPlayer.hasGameAsBlack()) {
            assertEquals(color, "black");
        }
    }


	/*
	 * 				SAVE POSITION FEATURE STEPS
	 */
	
	/**
	 * @Author Edwin Pan
	 * SavePosition cucumber feature
	 * Ensures that the file <filename> does not exist in game saves directory
	 */
	@Given("No file {string} exists in the filesystem")
	public void noFileFilenameExistsInTheFileSystem(String filename) {
		this.testGameSaveFilename = filename;
		SaveConfig.createGameSavesFolder();
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );
		if (file.exists())	{	file.delete();	}
	}
	
	/**
	 * @author Edwin Pan
	 * SavePosition cucumber feature
	 * Ensures that the file <filename> currently exists in game saves directory.
	 * It actually writes into the file gibberish for later comparative use.
	 * At most one megabyte of all of the file's data is stored for this purpose.
	 */
	@Given("File {string} exists in the filesystem")
	public void fileFilenameExistsInTheFilesystem(String filename) {
		this.testGameSaveFilename = filename;
		SaveConfig.createGameSavesFolder();
		//First put in our control as the existing file for our test.
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );
		file.delete();
		try {
			String str = "myhelicoptergoessoisoisoisoisoisoisosiosoisoisoisoisoisoisoisoisoisoisoi"; //should do as something that should not ever appear in the text file in regular use.
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(str);
			bufferedWriter.close();
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		//Now we keep in memory the contents of our existing file.
		this.readInFileFilenameInFileSystem(filename, this.refFileData);
	}
	
	/**
	 * @author Edwin Pan
	 * SavePosition cucumber feature
	 * Initiates controller method to save the game in game saves directory
	 */
	@When("The user initiates to save the game with name {string}")
	public void theUserInitiatesToSaveTheGameWithNameFilename(String filename) {
		SaveConfig.createGameSavesFolder();
		try {
			QuoridorController.saveGame(filename,QuoridorController.getCurrentGame());
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * @author Edwin Pan
	 * SavePosition cucumber feature
	 * Forces saving the game. Used to test canceling the overwriting of an existing file.
	 */
	@When("The user confirms to overwrite existing file")
	public void theUserConfirmsToOverwriteExistingFile() {
		SaveConfig.createGameSavesFolder();
		try {
			QuoridorController.saveGame(this.testGameSaveFilename, QuoridorController.getCurrentGame(), SavePriority.FORCE_OVERWRITE);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * @author Edwin Pan
	 * SavePosition cucumber feature
	 * Does not force saving the game. Used to test canceling the overwriting of an existing file.
	 */
	@When("The user cancels to overwrite existing file")
	public void theUserCancelsToOverwriteExistingFile() {
		SaveConfig.createGameSavesFolder();
		try {
			QuoridorController.saveGame(this.testGameSaveFilename, QuoridorController.getCurrentGame(), SavePriority.DO_NOT_SAVE);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
	/**
	 * @Author Edwin Pan
	 * SavePosition cucumber feature
	 * Asserts that a file with name <filename> now exists in game saves directory
	 */
	@Then("A file with {string} shall be created in the filesystem")
	public void aFileWithFilenameIsCreatedInTheFilesystem(String filename) {
		SaveConfig.createGameSavesFolder();
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );
		checkFileSystemAccess( SaveConfig.getGameSaveFilePath(filename) );
		assertEquals(file.exists(),true);
	}
	
	/**
	 * @author Edwin Pan
	 * SavePosition cucumber feature
	 * Asserts that the file of name <filename> has been updated.
	 */
	@Then("File with {string} shall be updated in the filesystem")
	public void fileWithFilenameIsUpdatedInTheFileSystem(String filename) {
		SaveConfig.createGameSavesFolder();
		this.readInFileFilenameInFileSystem(filename, this.curFileData);
		checkFileSystemAccess( SaveConfig.getGameSaveFilePath(filename) );
		assertEquals( Arrays.equals(refFileData, curFileData), false );	
	}
	
	/**
	 * @author Edwin Pan
	 * SavePosition cucumber feature
	 * Asserts that the file of name <filename> has not been changed.
	 */
	@Then("File {string} shall not be changed in the filesystem")
	public void fileWithFilenameIsNotChangedInTheFileSystem(String filename) {
		SaveConfig.createGameSavesFolder();
		this.readInFileFilenameInFileSystem(filename, this.curFileData);
		checkFileSystemAccess( SaveConfig.getGameSaveFilePath(filename) );
		assertEquals( Arrays.equals(refFileData, curFileData), true );
	}
	
	/**
	 * @author Edwin Pan
	 * Helper method for checking if the Operating System is preventing the application from doing Save and Load tests
	 * Checks if Access if being Denied: If so, prints the stacktrace and announces the Exception to tester.
	 */
	private void checkFileSystemAccess( String path ) {
		SecurityManager securityManager = new SecurityManager();
		try {
			securityManager.checkRead( path );
		} catch (SecurityException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/*
	 * 				SWITCH CURRENT PLAYER FEATURE STEPS
	 */
	
	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Sets up test preconditions such that the player whose turn it is to player is the provided player.
	 */
	@Given("The player to move is {string}")
	public void thePlayerToMoveIsPlayer(String playerColorAsString){
		//Complete the other player's turn. The method should not do anything if the other player's turn
		//is already "complete"; and if it is not complete, it will make it complete, thereby making it
		//the desired player's turn.
		Player providedPlayer = QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString);
		Player blackPlayer = QuoridorController.getCurrentBlackPlayer();
		Player whitePlayer = QuoridorController.getCurrentWhitePlayer();
		if( providedPlayer.equals(blackPlayer) ) {
			QuoridorController.completePlayerTurn(whitePlayer);	//If it's WhitePlayer's turn, end it. If not, nothing changes.
		} else {
			QuoridorController.completePlayerTurn(blackPlayer); //If it's BlackPlayer's turn, end it. If not, nothing happens.
		}
	}
	
	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Sets up test preconditions such that the clock of the player whose turn it is to play is running
	 */
	@Given("The clock of {string} is running")
	public void theClockOfPlayerIsRunning(String playerColorAsString){
		QuoridorController.continuePlayerTimer( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) );
	}

	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Sets up test preconditions such that the clock of the player whose turn it is not to play is stopped
	 */
	@Given("The clock of {string} is stopped")
	public void theClockOfOtherIsStopped(String playerColorAsString){
		QuoridorController.stopPlayerTimer( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) );
	}

	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Makes the selected player complete their move.
	 */
	@When("Player {string} completes his move")
	public void playerPlayerCompletesHisMove(String playerColorAsString){
		QuoridorController.completePlayerTurn( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) );
	}

	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Asserts that the user interface now shows that the specified player's timer is stopped.
	 */
	@Then("The clock of {string} shall be stopped")
	public void theClockOfPlayerShallBeStopped(String playerColorAsString){
		assertEquals( QuoridorController.getPlayerTimerRunning( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) ) , false );
	}

	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Asserts that the user interface now shows that the specified player's timer is running.
	 */
	@Then("The clock of {string} shall be running")
	public void theClockOfOtherShallBeRunning(String playerColorAsString){
		assertEquals( QuoridorController.getPlayerTimerRunning( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) ) , true );
	}
	
	
	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Asserts that the user interface now shows that the next move belongs to the specified player.
	 */
	@Then("The next player to move shall be {string}")
	public void theNextPlayerToMoveShallBeOther(String playerColorAsString){
		assertEquals( QuoridorController.getPlayerOfCurrentTurn().equals( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) ) , true );
	}
	
	/**
	 * @author Edwin Pan
	 * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
	 * Asserts that the user interface now shows that is now the specified player's turn.
	 */
	@Then("The user interface shall be showing it is {string} turn")
	public void theUserInterfaceShallBeShowingItIsOtherTurn(String playerColorAsString){
		assertEquals( QuoridorController.getPlayerOfCurrentTurn().equals( QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString) ) , true );
	}
	
	
	/**
	 * @param dir
	 * @param row
	 * @param col
	 * @author Alex Masciotra
	 */
	@Given("The wall move candidate with {string} at position \\({int}, {int}) is valid")
	public void theWallMoveCandidateWithAtPositionIsValid(String dir, Integer row, Integer col) {

		Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        Board board = QuoridorApplication.getQuoridor().getBoard();

        Player currentPlayer = game.getCurrentPosition().getPlayerToMove();

//		int wallsInStock = game.getCurrentPosition().numberOfWhiteWallsInStock();
//        Wall wallPlaced = currentPlayer.getWall(10 - wallsInStock);

        //hardcoding wall instock as there is bug in whitewallsinstock, currently 10, should be 9 after init)
        Wall wallPlaced = currentPlayer.getWall(3);

        Direction wallMoveDirection;

        if (dir.equals("horizontal")) {
            wallMoveDirection = Direction.Horizontal;
        } else if (dir.equals("vertical")) {
            wallMoveDirection = Direction.Vertical;
        } else {
            throw new IllegalArgumentException("Unsupported wall direction was provided");
        }

        //Tile tile = new Tile(row, col, QuoridorApplication.getQuoridor().getBoard());

        Tile tile = board.getTile((row - 1) * 9 + col - 1);

        WallMove wallMoveCandidate = new WallMove(1, 1, currentPlayer, tile, game, wallMoveDirection, wallPlaced);

        game.setWallMoveCandidate(wallMoveCandidate);

    }

    /**
     * same method as valid, controller will decide if its invalid or not with its return clause
     *
     * @param dir
     * @param row
     * @param col
     * @author Alex Masciotra
     */
    @Given("The wall move candidate with {string} at position \\({int}, {int}) is invalid")
    public void theWallMoveCandidateWithAtPositionIsInvalid(String dir, Integer row, Integer col) {
        theWallMoveCandidateWithAtPositionIsValid(dir, row, col);

    }

    /**
     * @author Alex Masciotra
     */
    @When("I release the wall in my hand")
    public void iReleaseTheWallInMyHand() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        //Board board = QuoridorApplication.getQuoridor().getBoard();

        QuoridorController.releaseWall(game);
    }

    /**
     * @param dir
     * @param row
     * @param col
     * @author Alex Masciotra
     */
    @Then("A wall move shall be registered with {string} at position \\({int}, {int})")
    public void aWallMoveShallBeRegisteredWithAtPosition(String dir, Integer row, Integer col) {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        int indexOfMove = game.getMoves().size();
        int wallIndex = game.getCurrentPosition().numberOfWhiteWallsOnBoard();

        assertEquals(row, game.getMove(indexOfMove).getTargetTile().getRow());
        assertEquals(col, game.getMove(indexOfMove).getTargetTile().getColumn());
        assertEquals(dir, game.getCurrentPosition().getWhiteWallsOnBoard(wallIndex).getMove().getWallDirection().toString());
    }

   /**
    * @author Alex Masciotra
    * @throws Throwable
    */
    @Then("I shall be notified that my wall move is invalid")
    public void iShallBeNotifiedThatMyWallMoveIsInvalid() throws Throwable {

        //GUI related TODO
    }

    /**
     * @author Alex Masciotra
     */
    @And("It shall be my turn to move")
    public void itShallBeMyTurnToMove() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        //we know whiteplayers turn from it is my turn
        assertEquals(game.getWhitePlayer(), game.getCurrentPosition().getPlayerToMove());
    }

    /**
     * @author Alex Masciotra
     * @throws Throwable
     */
    @And("I shall not have a wall in my hand")
    public void iShallNotHaveAWallInMyHand() throws Throwable {

        //TODO:GUI
    }

    /**
     * @author Alex Masciotra
     */
    @And("My move shall be completed")
    public void myMoveShallBeCompleted() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        int indexOfMove = game.getMoves().size();
        assertEquals(1, indexOfMove);

        //I set the move number to 1 and ruond number to 1, 1 was move before so checking if move was not registered in
        // the list of moves
        assertEquals(1, game.getMove(indexOfMove).getMoveNumber());
        assertEquals(1, game.getMove(indexOfMove).getRoundNumber());

        //check move list size to see if it grew in size from 0 to 1
    }

    /**
     * @author Alex Masciotra
     */
    @And("It shall not be my turn to move")
    public void iTShallNotBeMyTurnToMove() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        //we know it was white turn before check to see blackturn now
        assertEquals(game.getBlackPlayer(), game.getCurrentPosition().getPlayerToMove());
    }

    /**
     * @author Alex Masciotra
     * @param dir
     * @param row
     * @param col
     */
    @But("No wall move shall be registered with {string} at position \\({int}, {int})")
    public void noWallMoveShallBeRegisteredWithAtPosition(String dir, Integer row, Integer col) {
        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        int indexOfMove = game.getMoves().size();
        //I set the move number to 1 and ruond number to 1, 0 was move before so checking if move was not registered in
        // the list of moves
        assertEquals(0, game.getMove(indexOfMove).getMoveNumber());
        assertEquals(1, game.getMove(indexOfMove).getRoundNumber());
    }
    
    /*
     * Validate Position step definiton
     */
  	
  	/**
	 * @author Daniel Wu
	 * ValidatePosition.feature - ValidatePosition
	 * Scenario: Validate pawn position
	 */
  	@Given("A game position is supplied with pawn coordinate {int}:{int}")
  	public void aGamePositionIsSuppliedWithPawnCoordinate(int row, int col) {
  		//This code does not allow incorrect inputs to be used for the following steps
  		//.getTile throws an indexoutofbound and the gameposition is therefore not created
//  		Quoridor quoridor = QuoridorApplication.getQuoridor();
//  		GamePosition currentGamePosition = quoridor.getCurrentGame().getCurrentPosition();
//  		if (((row - 1) * 9 + col - 1) <= 0 || ((row - 1) * 9 + col - 1) >= 81){
//  			throw new IndexOutOfBoundsException();
//  		}
//  		Tile pawnCoord = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);
//  		Player player = currentGamePosition.getPlayerToMove();
//  		PlayerPosition playerPosition = new PlayerPosition(player, pawnCoord);
//  		if(player.hasGameAsBlack()) {
//  			currentGamePosition.setBlackPosition(playerPosition);
//  		}
//  		else if(player.hasGameAsWhite()) {
//  			currentGamePosition.setWhitePosition(playerPosition);
//  		}
//  		quoridor.getCurrentGame().setMoveMode(Game.MoveMode.PlayerMove);
  		
  		//Alternative
  		//Simply store the input for use later
  		myCoordinate[0] = row;
  		myCoordinate[1] = col;
  	}
  	
  	/**
	 * @author Daniel Wu
	 * ValidatePosition.feature - ValidatePosition
	 * Scenario: Validate pawn position and Validate wall position
	 */
  	@When("Validation of the position is initiated")
  	public void validationOfThePositionIsInitiated() {
  		if (myDirection.equals("")) {
  			positionIsValid = QuoridorController.validatePosition(myCoordinate[0], myCoordinate[1]);  			
  		} else {
  			positionIsValid = QuoridorController.validatePosition(myCoordinate[0], myCoordinate[1], myDirection);
  		}
  	}
  	
  	/**
	 * @author Daniel Wu
	 * ValidatePosition.feature - ValidatePosition
	 * Scenario: Validate pawn position and Validate wall position
	 */
    @Then("The position shall be {string}")
    public void thePositionShallBeResult(String result) {
    	String myResult = "";
    	if (positionIsValid == true) {
    		myResult = "ok";
    	} else if (positionIsValid == false) {
    		myResult = "error";
    	}
    	assertEquals(result, myResult);
    }
    
    /**
	 * @author Daniel Wu
	 * ValidatePosition.feature - ValidatePosition
	 * Scenario: Validate wall position
	 */
    @Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
    public void aGamePositionIsSuppliedWithWallCoordinate(int row, int col, String dir) {
    	//This code does not allow incorrect inputs to be used for the following steps
  		//.getTile throws an indexoutofbound and the gameposition is therefore not created
//    	Direction myDir = Direction.Horizontal;
//    	if (dir.equals("Horizontal")) {
//    		myDir = Direction.Horizontal;
//    	}else if(dir.equals("Vertical")) {
//    		myDir = Direction.Vertical;
//    	}
//    	Quoridor quoridor = QuoridorApplication.getQuoridor();
//    	Game game = quoridor.getCurrentGame();
//    	GamePosition gamePosition = game.getCurrentPosition();
//    	Tile tile = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);
//    	WallMove wallMove = new WallMove(0, 0, game.getWhitePlayer(), tile, game, myDir, game.getCurrentPosition().getWhiteWallsInStock(4));
//    	Wall wall = gamePosition.getWhiteWallsInStock(0);
//    	wall.setMove(wallMove);
//    	gamePosition.getWhiteWallsInStock().remove(wall);
//    	gamePosition.getWhiteWallsOnBoard().add(wall);
//    	game.setMoveMode(Game.MoveMode.WallMove);
    	//Alternative
  		//Simply store the input for use later
  		myCoordinate[0] = row;
  		myCoordinate[1] = col;
  		myDirection = dir;
    }
    
    @Then("The position shall be valid")
    public void thePositionShallBeValid() {
    	assertEquals(positionIsValid, true);
    	
    }
    
    @Then("The position shall be invalid")
    public void thePositionShallBeInvalid() {
    	assertEquals(positionIsValid, false);
    }
	///ROTATE WALL
    // DUPLICATE METHOD LEAVING HERE FOR INDIVIDUAL MARKING
//	/**
//	 * Calls the controller to ensure that a wall move candidate exists in the current game.
//	 * If no wall move candidate exists with parameters [dir, row, col], create it.
//	 * Then, assert that the controller function has succeeded.
//	 *
//	 * @author Matthias Arabian
//	 */
//	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
//	public void aWallMoveCandidateExistsWithDirAtPosition(String dir, int row, int col) {
//		assertEquals(true, QuoridorController.GetWallMoveCandidate(dir, row, col));
//	}
	
	/**
	 * Calls a controller method to change the direction of the wall move candidate
	 * Before		|	after
	 * horizontal	|	vertical
	 * vertical		|	horizontal
	 * 
	 * @author Matthias Arabian
	 */
	@When("I try to flip the wall")
	public void iTryToFlipTheWall() {
		QuoridorController.flipWallCandidate();
	}
	
	/**
	 * This function calls for the GUI to update its wall move object to match its new direction
	 * @author Matthias Arabian
	 */
	@Then("The wall shall be rotated over the board to {string}")
	public void theWallShallBeRotatedOverTheBoardToString(String newDir){
		// GUI-related feature -- TODO for later
	}
	
	

	// DUPLICATE METHOD
//	/**
//	 * This function ensures that the wall has been rotated and that no other parameter has been altered
//	 * @author Matthias Arabian
//	 */
//	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
//	public void aWallMoveCandidateShallExistWithNewDirAtPosition(String newDir, int row, int col) {
//		Quoridor quoridor = QuoridorApplication.getQuoridor();
//		WallMove wallMoveCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
//		Tile t = wallMoveCandidate.getTargetTile();
//		assertEquals(t.getColumn(), col);
//		assertEquals(t.getRow(), row);
//		assertEquals(wallMoveCandidate.getWallDirection(), Direction.valueOf(newDir));
//	}

	
	///LOAD POSITION
	/**
	 * This function calls a controller function loadSavedGame("filename").
	 * Loads data from file, initiates the creation of a game and sets positions according to loaded data.
	 * @author Matthias Arabian (original)
	 * @author Edwin Pan (Rewrote to catch exceptions)
	 */
	@When("I initiate to load a saved game {string}")
	public void iInitiateToLoadASavedGame(String fileName) {
		CucumberTest_LoadPosition_TestFileWriters.createGameSaveTestFile(fileName);
		try {
			QuoridorController.loadSavedGame(fileName, this.myPlayers.get(0), this.myPlayers.get(1) );
		} catch (FileNotFoundException e) {
			failedToReadSaveFile = true;
			e.printStackTrace();
		} catch (IOException e) {
			failedToReadSaveFile = true;
			e.printStackTrace();
		} catch (InvalidPositionException e) {
			receivedInvalidPositionException = true;
			e.printStackTrace();
		} 
	}
	
	/**
	 * Ensures that the loaded positions are valid and legal/playable.
	 * @author Matthias Arabian
	 * @author Edwin Pan made modifications after taking over for sprint 3
	 */
	@And("The position to load is valid")
	public void thePositionToLoadIsValid(){
		assertEquals(false, failedToReadSaveFile);
		assertEquals(false, receivedInvalidPositionException);
	}
	
	/**
	 * Ensures that the player whose turn it is to play is the right player.
	 * @author Matthias Arabian
	 */
	@Then("It shall be {string}'s turn")
	public void itShallBePlayer_s_Turn(String player) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
		if (player.equals("black"))
			assertEquals(null, currentPlayer.getGameAsWhite());
		else
			assertEquals(null, currentPlayer.getGameAsBlack());
		
	}
	
	/**
	 * Verifies that the player is at the correct position.
	 * @author Matthias Arabian
	 */
	@And("{string} shall be at {int}:{int}")
	public void PlayerShallBeAtRowCol(String player, int pRow, int pCol) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		PlayerPosition currentPlayer = null;
		if (player.equals("black"))
			currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition();
		else
			currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition();
		assertEquals(currentPlayer.getTile().getColumn(), pCol);
		assertEquals(currentPlayer.getTile().getRow(), pRow);
	}
	
	/**
	 * Verifies that player walls have been properly loaded and positioned.
	 * @author Matthias Arabian
	 */
	//@And("^\"([^\"]*)\" shall have a \"([^\"]*)\" wall at [0-9]:[0-9]$")
	@And("{string} shall have a {} wall at {int}:{int}")
	public void playerShallHaveAWallWithOrientationAtPosition(String player, String wallOrientation, int row, int col){
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Player currentPlayer = null;
		if (player.equals("black"))
			currentPlayer = quoridor.getCurrentGame().getBlackPlayer();
		else
			currentPlayer = quoridor.getCurrentGame().getWhitePlayer();
		
		String cap = wallOrientation.substring(0, 1).toUpperCase() + wallOrientation.substring(1).toLowerCase();
		Direction d = Direction.valueOf(cap);
		
		Boolean errorFlag = true;
		for (Wall w : currentPlayer.getWalls()) {
			if (w.getMove() != null && w.getMove().getWallDirection() == d &&
				w.getMove().getTargetTile().getColumn() == col 
				&& 	w.getMove().getTargetTile().getRow() == row)
			{
				errorFlag = false;
				break;
			}
		}
		assertEquals(false, errorFlag);
	}
	
	/**
	 * Ensures that both players have the same number of walls in stock (which doesn't really make sense....?)
	 * @author Matthias Arabian
	 * @author Edwin Pan fixed code after taking over for sprint 3.
	 */
	@And("Both players shall have {int} in their stacks")
	public void bothPlayersShallHaveRemainingWallsInTheirStacks(int remainingWalls) {
		Game g = QuoridorApplication.getQuoridor().getCurrentGame();
		assertEquals(remainingWalls, g.getCurrentPosition().getBlackWallsInStock().size());
		assertEquals(remainingWalls, g.getCurrentPosition().getWhiteWallsInStock().size());
	}
	
	//LOAD INVALID POSITION
	/**
	 * This function is called when an invalid position is loaded.
	 * It verifies that the position is indeed invalid.
	 * @author Matthias Arabian
	 * @author Edwin Pan made modifications after taking over for sprint 3
	 */
	@And("The position to load is invalid")
	public void thePositionToLoadIsInvalid(){
		assertEquals(false, failedToReadSaveFile);
		assertEquals(true, receivedInvalidPositionException);
	}
	
	/**
	 * This function ensures that an error is sent out about the load position being invalid.
	 * @author Matthias Arabian
	 * @author Edwin Pan made modifications after taking over for sprint 3
	 */
	@Then("The load shall return an error")
	public void theLoadShallReturnAnError() throws Throwable{
		assertEquals(true, !failedToReadSaveFile);
		assertEquals(true, receivedInvalidPositionException);
	}
	
	
	
	// ***********************************************
	// Clean up
	// ***********************************************

	// After each scenario, the test model is discarded
    @After
	public void tearDown() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// Avoid null pointer for step definitions that are not yet implemented.
		if (quoridor != null) {
			quoridor.delete();
			quoridor = null;
		}
		for (int i = 0; i < 20; i++) {
			Wall wall = Wall.getWithId(i);
			if(wall != null) {
				wall.delete();
			}
		}
		// Clear out file data memories, used for SavePosition features.
		File file = new File( SaveConfig.getGameSaveFilePath(this.testGameSaveFilename) );
		file.delete();
		this.testGameSaveFilename = "";
		for( int i = 0 ; i < refFileData.length ; i++ ) {
			this.refFileData[i] = 0;
		}
		for( int i = 0 ; i < curFileData.length ; i++ ) {
			this.curFileData[i] = 0;
		}
		
		//reset these variables for use
		myCoordinate[0] = 0;
		myCoordinate[1] = 0;
		myDirection = "";
		receivedInvalidPositionException = false;
		failedToReadSaveFile = false;
		//boolean positionValidated = false;//dead code
		
		//Clear load position files from saves directory
		CucumberTest_LoadPosition_TestFileWriters.clearGameSaveLoadingTestFiles();
		
	}

	// ***********************************************
	// Extracted helper methods
	// ***********************************************

	// Place your extracted methods below

	private void initQuoridorAndBoard() {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		Board board = new Board(quoridor);
		// Creating tiles by rows, i.e., the column index changes with every tile
		// creation
		for (int i = 1; i <= 9; i++) { // rows
			for (int j = 1; j <= 9; j++) { // columns
				board.addTile(i, j);
			}
		}
	}

	private ArrayList<Player> createUsersAndPlayers(String userName1, String userName2) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		User user1 = quoridor.addUser(userName1);
		User user2 = quoridor.addUser(userName2);

		int thinkingTime = 180;

		// Players are assumed to start on opposite sides and need to make progress
		// horizontally to get to the other side
		//@formatter:off
		/*
		 *  __________
		 * |          |
		 * |          |
		 * |x->    <-x|
		 * |          |
		 * |__________|
		 * 
		 */
		//@formatter:on
		Player player1 = new Player(new Time(thinkingTime), user1, 9, Direction.Horizontal);
		Player player2 = new Player(new Time(thinkingTime), user2, 1, Direction.Horizontal);

		Player[] players = { player1, player2 };

		// Create all walls. Walls with lower ID belong to player1,
		// while the second half belongs to player 2
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {
				new Wall(i * 10 + j, players[i]);
			}
		}
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		playersList.add(player1);
		playersList.add(player2);
		
		return playersList;
	}

	private void createAndStartGame(ArrayList<Player> players) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		// There are total 36 tiles in the first four rows and
		// indexing starts from 0 -> tiles with indices 36 and 36+8=44 are the starting
		// positions
		Tile player1StartPos = quoridor.getBoard().getTile(36);
		Tile player2StartPos = quoridor.getBoard().getTile(44);
		
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
		game.setWhitePlayer(players.get(0));
		game.setBlackPlayer(players.get(1));

		PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
		PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

		GamePosition gamePosition = new GamePosition(0, player1Position, player2Position, players.get(0), game);

		// Add the walls as in stock for the players
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j);
			gamePosition.addWhiteWallsInStock(wall);
		}
		for (int j = 0; j < 10; j++) {
			Wall wall = Wall.getWithId(j + 10);
			gamePosition.addBlackWallsInStock(wall);
		}

		game.setCurrentPosition(gamePosition);
    }
    
	
	/**
	 * Removes the wall in stock for both players
	 * @author Thomas Philippon
	 */
	private  void removeWalls() {
		
		int whiteWallNo = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size();
		int blackWallNo = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size();
		
		for (int j = 0; j < whiteWallNo; j++) {
			Wall wall = Wall.getWithId(j);
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
		}
		for (int j = 0; j < blackWallNo; j++) {
			Wall wall = Wall.getWithId(j + 10);
			QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
		}
    }
	
	/**
	 * @author Edwin Pan
	 * @param filename
	 * @param dataDestination character array
	 * Read in a file in the game saves directory into the character array. Useful for comparing contents of two saves.
	 */
	private void readInFileFilenameInFileSystem(String filename, char [] dataDestination) {
		try {
			File file = new File( SaveConfig.getGameSaveFilePath(filename) );
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			bufferedReader.read(dataDestination, 0, dataDestination.length);
			bufferedReader.close();
		}catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
}
