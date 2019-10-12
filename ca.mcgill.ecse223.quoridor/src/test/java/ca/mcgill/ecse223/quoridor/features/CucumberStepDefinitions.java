package ca.mcgill.ecse223.quoridor.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
	
	// ***********************************************
	// Scenario and scenario outline step definitions
	// ***********************************************

	///ROTATE WALL
	/**
	 * Calls the controller to ensure that a wall move candidate exists in the current game.
	 * If no wall move candidate exists with parameters [dir, row, col], create it.
	 * Then, assert that the controller function has succeeded.
	 * 
	 * @author Matthias Arabian
	 */
	@Given("A wall move candidate exists with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateExistsWithDirAtPosition(String dir, int row, int col) {
		assertEquals(true, QuoridorController.GetWallMoveCandidate(dir, row, col));
	}
	
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
	
	/**
	 * This function ensures that the wall has been rotated and that no other parameter has been altered
	 * @author Matthias Arabian
	 */
	@And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
	public void aWallMoveCandidateShallExistWithNewDirAtPosition(String newDir, int row, int col) {
		Quoridor quoridor = QuoridorApplication.getQuoridor();
		WallMove wallMoveCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
		Tile t = wallMoveCandidate.getTargetTile();
		assertEquals(t.getColumn(), col);
		assertEquals(t.getRow(), row);
		assertEquals(wallMoveCandidate.getWallDirection(), Direction.valueOf(newDir));	
	}
	
	
	///LOAD POSITION
	/**
	 * This function calls a controller function loadSavedGame("filename").
	 * Loads data from file, initiates the creation of a game and sets positions according to loaded data.
	 * @author Matthias Arabian
	 */
	@When("I initiate to load a saved game {string}")
	public void iInitiateToLoadASavedGame(String fileName) {
		QuoridorController.loadSavedGame(fileName);
	}
	
	/**
	 * Ensures that the loaded positions are valid and legal/playable.
	 * @author Matthias Arabian
	 */
	@And("The position to load is valid")
	public void thePositionToLoadIsValid(){
		Boolean positionIsValid = QuoridorController.CheckThatPositionIsValid();
		assertEquals(true, positionIsValid);
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
	@And("{string} shall have a {string} wall at {int}:{int}")
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
	 */
	@And("Both players shall have {int} in their stacks")
	public void bothPlayersShallHaveRemainingWallsInTheirStacks(int remainingWalls) {
		Game g = QuoridorApplication.getQuoridor().getCurrentGame();
		assertEquals(remainingWalls, g.getBlackPlayer().numberOfWalls());
		assertEquals(remainingWalls, g.getWhitePlayer().numberOfWalls());
	}
	
	//LOAD INVALID POSITION
	/**
	 * This function is called when an invalid position is loaded.
	 * It verifies that the position is indeed invalid.
	 * @author Matthias Arabian
	 */
	@And("The position to load is invalid")
	public void thePositionToLoadIsInvalid(){
		Boolean positionIsValid = QuoridorController.CheckThatPositionIsValid();
		assertEquals(false, positionIsValid);
	}
	
	/**
	 * This function ensures that an error is sent out about the load position being invalid.
	 * @author Matthias Arabian
	 */
	@Then("The load shall return an error")
	public void theLoadShallReturnAnError() throws Throwable{
		assertEquals(true, QuoridorController.sendLoadError());
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

}
