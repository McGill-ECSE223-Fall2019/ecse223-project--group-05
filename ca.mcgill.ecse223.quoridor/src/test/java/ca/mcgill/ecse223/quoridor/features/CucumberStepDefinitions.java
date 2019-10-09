package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
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
import com.sun.org.apache.xpath.internal.operations.Quo;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CucumberStepDefinitions {

	// ***********************************************
	// Background step definitions
	// ***********************************************

	@Given("^The game is not running$")
	public void theGameIsNotRunning() {
		initQuoridorAndBoard();
		createUsersAndPlayers("user1", "user2");
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

	@And("^I have a wall in my hand over the board$")
	public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
		// GUI-related feature -- TODO for later
	}
	
	@Given("A new game is initializing")
	public void theGameIsInitializing() {
		
		//TODO:model in here
	}
	
	
	@Given("^A new game is initializing$")
	public void aNewGameIsInitializing() throws Throwable {
		initQuoridorAndBoard();
		ArrayList<Player> players = createUsersAndPlayers("user1", "user2");
		new Game(GameStatus.Initializing, MoveMode.PlayerMove, players.get(0), players.get(1), QuoridorApplication.getQuoridor());
	}
	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

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
	@And("The wall in my hand shall disappear from my stock")
	public void theWallInMyHandShallDisappearFromMyStock() {
		//The current player is assigned to the white player in the step definition of "It is my turn to move".
		assertEquals(9, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
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
	
	/*
	 * TODO Insert your missing step definitions here
	 * 
	 * Call the methods of the controller that will manipulate the model once they
	 * are implemented
	 * 
	 */
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
        //TODO:model in here
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

        //TODO:controller method select existing user name
    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @When("The player provides new user name: {string}")
    public void thePlayerProvidesNewUserName(String username) {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        QuoridorController.selectNewUserName(username, game);
        //TODO: controller method create new user name
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

        //TODO: assert that user name and color are set properly
    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @Then("The player shall be warned that {string} already exists")
    public void thePlayerShallBeWarnedThatAlreadyExists(String username) {

        assertEquals(username, QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getNextPlayer().getUser().getName());

        //GUI notification that username is already existing and he will be that username
        //TODO: assert that user name is already in use???
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


        //currentPlayer

        //TODO: assert

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

        //TODO:model in here
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

        //TODO:model
    }

    /**
     * @author Alex Masciotra
     */
    @When("I release the wall in my hand")
    public void iReleaseTheWallInMyHand() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        //Board board = QuoridorApplication.getQuoridor().getBoard();

        QuoridorController.releaseWall(game);

        //TODO: controller release wall;
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

        //TODO:Assert position
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
        //TODO:assert still my turn
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


        //TODO:Assert move completed
    }

    /**
     * @author Alex Masciotra
     */
    @And("It shall not be my turn to move")
    public void iTShallNotBeMyTurnToMove() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        //we know it was white turn before check to see blackturn now
        assertEquals(game.getBlackPlayer(), game.getCurrentPosition().getPlayerToMove());
        //TODO:Assert which turn it is black turn
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

        //TODO:assert move is not registered
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
		
		Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, players.get(0), players.get(1), quoridor);

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

	private void removeWallInStock(Player player) {
		//TODO;
	}
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

}
