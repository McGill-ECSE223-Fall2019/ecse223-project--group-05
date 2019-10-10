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
import io.cucumber.java.After;
import io.cucumber.java.en.*;

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
	
	@And("The wall in my hand should disappear from my stock")
	public void theWallInMyHandShoulDisappearFromMyStock() {
		//I think this is a GUI related step
		//assertEquals(9, game.getCurrentPosition().getWhiteWallsInStock().size());
	}
	/**
	 * @author Thomas Philippon
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
	@Given("Next player to set user name is {string}")
	public void nextPlayerToSetUserNameIs(String color) {
		
		//TODO:model in here
		
	}
	
	@And("There is existing user {string}")
	public void thereIsExistingUser(String username) {
		
		//TODO:model in here
	}
	
	@And("There is no existing user {string}")
	public void thereIsNoExistingUser(String username) {
		
		//TODO:model in here
	}
	
	@When("The player selects existing {string}")
	public void thePlayerSelectsExisting(String username) {
		
		//TODO:controller method select existing user name
	}
	
	@When("The player provides new user name: {string}")
	public void thePlayerProvidesNewUserName(String username) {
		
		//TODO: controller method create new user name
	}
	
	@Then("The name of player {string} in the new game shall be {string}")
	public void theNameOfPlayerInTheNewGameShallBe(String username, String color) {
		
		//TODO: assert that user name and color are set properly
	}
	
	@Then("The player shall be warned that {string} already exists")
	public void thePlayerShallBeWarnedThatAlreadyExists(String username) {
		
		//TODO: assert that user name is already in use???
	}

	@And("Next player to set user name shall be {string}")
		public void nextPlayerToSetUserNameShallBe(String color){

		//TODO: assert

		}

	@Given("The wall move candidate with {string} at position {int} {int} is valid")
	public void theWallMoveCandidateWithAtPosition(String dir, Integer row, Integer col){

		//TODO:model in here

	}

	@Given("The wall move candidate with {string} at position {int} {int} is invalid")
	public void theWallMoveCandidateWithAtPositionIsInvalid(String dir, Integer row, Integer col){

		//TODO:model
	}

	@When("I release the wall in my hand")
	public void iReleaseTheWallInMyHand(){

		//TODO: controller release wall;
	}

	@Then("A wall move shall be registered with {string} at position {int} {int}")
	public void aWallMoveShallBeRegisteredWithAtPosition(String color, Integer row, Integer col){

		//TODO:Assert position
	}

	@Then("I shall be notified that my wall move is invalid")
	public void iShallBeNotifiedThatMyWallMoveIsInvalid(){

		//TODO:assert invalid move
	}

	@And("I shall have a wall in my hand over the board")
	public void iShallHaveAWallInMyHandOverTheBoard(){

		//TODO:GUI
	}

	@And("It shall be my turn to move")
	public void itShallBeMyTurnToMove(){

		//TODO:assert still my turn
	}

	@And("I shall not have a wall in my hand")
	public void iShallNotHaveAWallInMyHand(){

		//TODO:Assert wall is indeed dropped
	}

	@And("My move shall be completed")
	public void myMoveShallBeCompleted(){

		//TODO:Assert move completed
	}

	@And("It shall not be my turn to move")
	public void iTShallNotBeMyTurnToMove(){

		//TODO:Assert which turn it is
	}

	@But("No wall move shall be registered with {string} at position {int} {int}")
	public void noWallMoveShallBeRegisteredWithAtPosition(String dir, Integer row, Integer col){

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
}
