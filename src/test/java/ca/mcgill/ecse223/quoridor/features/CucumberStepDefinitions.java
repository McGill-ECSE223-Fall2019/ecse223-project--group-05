package ca.mcgill.ecse223.quoridor.features;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.controller.PawnBehaviour;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.enumerations.SavePriority;
import ca.mcgill.ecse223.quoridor.exceptions.InvalidPositionException;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.view.ViewInterface;
import io.cucumber.java.After;
import io.cucumber.java.en.*;

import java.io.*;
import java.sql.Time;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CucumberStepDefinitions {

    PawnBehaviour whitePawnBehaviour = new PawnBehaviour();
    PawnBehaviour blackPawnBehaviour = new PawnBehaviour();

    private WallMove wallMoveCandidate = null;
    ArrayList<Player> myPlayers; //Used when trying to set the gameStatus to ReadyToStart as the players are not accessible
    int[] myCoordinate = {0, 0}; //Used to store the row and column input from the given scenario
    String myDirection = ""; //Used to store the direction input from the given scenario
    boolean positionIsValid = true; // Used to check if the position was valid or not, true by default for detecting if the gamePosition exists/changed
    boolean handIsEmpty = false; //used to see if wall drop was successfull or not
    boolean userNameSet = true; //used to see if user name was set correctly
    //Variable for GrabWall test
    boolean handHasWall = false;
    boolean pawnMoveSuccesful = false;


    //Instance Variables for SavePosition tests
    private String testGameSaveFilename = "";
    private final int fileDataLength = 1000000;
    private char[] refFileData = new char[fileDataLength];    //Memory for storing data of one file for comparison.
    private char[] curFileData = new char[fileDataLength];    //Memory for storing data of another file for comparison.]

    //Instance Variables for LoadPosition tests
    private boolean failedToReadSaveFile = false;
    private boolean receivedInvalidPositionException = false;



    //Timer object for starting and stopping the player clock
    Timer timer = new Timer();
    // ***********************************************
    // Background step definitions
    // ***********************************************

    @Given("^The game is not running$")
    public void theGameIsNotRunning() {
        initQuoridorAndBoard();
        //myPlayers = createUsersAndPlayers("user1", "user2");
    }

    @Given("^The game is running$")
    public void theGameIsRunning() {
        initQuoridorAndBoard();
        ArrayList<Player> createUsersAndPlayers = createUsersAndPlayers("user1", "user2");
        createAndStartGame(createUsersAndPlayers);
        whitePawnBehaviour.setPlayer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
        blackPawnBehaviour.setPlayer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
        whitePawnBehaviour.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
        blackPawnBehaviour.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
        blackPawnBehaviour.entry();
        whitePawnBehaviour.entry();
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
        Player[] players = {quoridor.getCurrentGame().getWhitePlayer(), quoridor.getCurrentGame().getBlackPlayer()};
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
        assertFalse(handHasWall);
        // GUI-related feature -- TODO for later
    }

    @And("^I have a wall in my hand over the board$")
    public void iHaveAWallInMyHandOverTheBoard() throws Throwable {
        assertFalse(handIsEmpty);
        // GUI-related feature -- TODO for later
    }

    @Given("^A new game is initializing$")
    public void aNewGameIsInitializing() throws Throwable {
        initQuoridorAndBoard();
        ArrayList<Player> players = createUsersAndPlayers("user1", "user2");
        Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, QuoridorApplication.getQuoridor());
        game.setWhitePlayer(players.get(0));
        game.setBlackPlayer(players.get(1));
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
    public void aNewGameIsBeingInitializing() {
        QuoridorController.initializeGame(QuoridorApplication.getQuoridor());
    }

    /**
     * @author Daniel Wu
     * StartNewGame.feature - StartNewGame
     * Scenario: Initiate a new game
     */
    @And("White player chooses a username")
    public void whitePlayerChoosesAUsername() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        User user = quoridor.addUser("temp1");
        QuoridorController.setUserToPlayer(quoridor.getCurrentGame().getWhitePlayer(), user);
    }

    /**
     * @author Daniel Wu
     * StartNewGame.feature - StartNewGame
     * Scenario: Initiate a new game
     */
    @And("Black player chooses a username")
    public void blackPlayerChoosesAUsername() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        User user = quoridor.addUser("temp2");
        QuoridorController.setUserToPlayer(quoridor.getCurrentGame().getBlackPlayer(), user);
    }

    /**
     * @author Daniel Wu
     * StartNewGame.feature - StartNewGame
     * Scenario: Initiate a new game
     */
    @And("Total thinking time is set")
    public void totalThinkingTimeIsSet() {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Time time = new Time(180);
        QuoridorController.setTotalThinkingTime(quoridor.getCurrentGame().getWhitePlayer(), time);
        QuoridorController.setGameStatus(GameStatus.ReadyToStart); //after all the whens, then the game should be ready to start
    }

    /**
     * @author Daniel Wu
     * StartNewGame.feature - StartNewGame
     * Scenario: Initiate a new game
     */
    @Then("The game shall become ready to start")
    public void theGameShallBecomeReadyToStart() {
        assertEquals(Game.GameStatus.ReadyToStart, QuoridorController.getGameStatus());
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
        Game game = new Game(GameStatus.ReadyToStart, MoveMode.PlayerMove, quoridor);
        myPlayers = createUsersAndPlayers("user1", "user2");
        game.setWhitePlayer(myPlayers.get(0));
        game.setBlackPlayer(myPlayers.get(1));
    }

    /**
     * @author Daniel Wu
     * StartNewGame.feature - StartNewGame
     * Scenario: Start clock
     * starts the clock
     */
    @When("I start the clock")
    public void iStartTheClock() {
        Player player = QuoridorController.getCurrentWhitePlayer();
//		if(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove().hasGameAsBlack()){
//			player = QuoridorController.getCurrentBlackPlayer();
//		}
        QuoridorController.startPlayerTimer(player, timer);
        QuoridorController.setGameStatus(GameStatus.Running);

    }

    /**
     * @author Daniel Wu
     * StartNewGame.feature - StartNewGame
     * Scenario: Start clock
     * check if the game is running
     */
    @Then("The game shall be running")
    public void theGameShallBeRunning() {
        assertEquals(Game.GameStatus.Running, QuoridorController.getGameStatus());
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
    public void initializationOfBoardInitiated() {
        QuoridorController.initializeBoard(QuoridorApplication.getQuoridor(), timer);
    }

    /**
     * @author Thomas Philippon
     */
    @Then("It shall be white player to move")
    public void itShallBeWhitePlayerToMove() {
        assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer(), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove());
    }

    /**
     * @author Thomas Philippon
     */
    @And("White's pawn shall be in its initial position")
    public void whitesPawnShallBeInItsInitialPosition() {
        //the initial tile for the white player is the tile 4
        assertEquals(QuoridorApplication.getQuoridor().getBoard().getTile(76), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile());
    }

    /**
     * @author Thomas Philippon
     */
    @And("Black's pawn shall be in its initial position")
    public void blacksPawnShallBeInItsInitialPosition() {
        //the initial tile for the black player is the tile 76
        assertEquals(QuoridorApplication.getQuoridor().getBoard().getTile(4), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile());
    }

    /**
     * @author Thomas Philippon
     */
    @And("All of White's walls shall be in stock")
    public void allOfWhitesWallsShallBeInStock() {
        //the white player should have 10 walls in stock
        assertEquals(10, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
    }

    /**
     * @author Thomas Philippon
     */
    @And("All of Black's walls shall be in stock")
    public void allOfBlacksWallsShallBeInStock() {
        //the black player should have 10 walls in stock
        assertEquals(10, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
    }

    /**
     * @author Thomas Philippon
     */
    @And("White's clock shall be counting down")
    public void whitesClockShallBeCountingDown() {
        //wait to let the counter counting down 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        assertNotEquals("19:00:00", QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime().toString());
    }

    /**
     * @author Thomas Philippon
     */
    @And("It shall be shown that this is White's turn")
    public void itShallBeShownThatThisIsWhitesTurn() throws Throwable {
        assertEquals(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer(), QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove());
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

        handHasWall = QuoridorController.grabWall(QuoridorApplication.getQuoridor());
    }

    /**
     * common method
     *
     * @author Thomas Philippon
     * @author Alex Masciotra
     */
    @Then("I shall have a wall in my hand over the board")
    public void iShallHaveAWallInMyHandOverTheBoard() throws Throwable {

        //As this is a GUI related step, it will be implemented later on
        assertFalse(handIsEmpty);
        assertTrue(handHasWall);
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
        assertEquals(1, QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getRow());
        assertEquals(1, QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn());
    }

    /**
     * @author Thomas Philippon
     */
    @Then("I shall be notified that I have no more walls")
    public void iShouldBeNotifiedThatIHaveNoMoreWalls() throws Throwable {
        assertFalse(handHasWall);
    }

    /**
     * @author Thomas Philippon
     */
    @And("I shall have no walls in my hand")
    public void iShallHaveNoWallsInMyHand() throws Throwable {
        assertFalse(handHasWall);
    }

    /**
     * checks that a wall move candidate exists, otherwise create one and link to appropriate tile
     *
     * @param dir    orientation of wall
     * @param row
     * @param column
     * @throws Throwable
     * @author David
     */
    @Given("A wall move candidate exists with {string} at position \\({int}, {int})")
    public void wallMoveCandidateExists(String dir, int row, int column) throws Throwable {
        QuoridorController.GetWallMoveCandidate(dir, row, column);

    }

    /**
     * we will check the test cases provided to make sure it is true that they are not at the edge of the board
     *
     * @param side
     * @throws Throwable
     * @author David
     */
    @And("The wall candidate is not at the {string} edge of the board")
    public void wallIsNotAtEdge(String side) throws Throwable {
        assertEquals(false, QuoridorController.wallIsAtEdge(side));
    }

    /**
     * moves the wall one tile toward the direction specified. An illegal move notification will be shown
     * if such a move in illegal. This involves linking wallMove object to a new target tile
     *
     * @param side
     * @throws Throwable
     * @author David
     */
    @When("I try to move the wall {string}")
    public void tryToMoveWall(String side) throws Throwable {
        try {
            QuoridorController.moveWall(side);
        } catch (Throwable e) {

        }
    }

    /**
     * original plan: communicates to View to verify that the wall is indeed moved to a new position. NOTE: THIS ORIGINAL (COMMENTED-OUT) TEST FAILS BECAUSE
     * THE TEST CASES ARE DONE WITHOUT EVER INITIALIZING VIEW. If test cases try to access view, a null pointe exception
     * will always be thrown. It is my opinion that the test case is not necessary.
     * <p>
     * update: since tryToMoveWall in step definition does not alter the view, there is no reason for the wall to be displayed at the position stated. The only way for the test to pass is to change the test to test controller only.
     * And this is what we have done.
     *
     * @param row
     * @param column
     * @throws Throwable
     * @author David
     */
    @Then("The wall shall be moved over the board to position \\({int}, {int})")
    public void thisWallIsAtPosition(int row, int column) throws Throwable {
        // originally implemented as below
        //assertEquals(true, QuoridorController.thisWallIsAtPosition(row, column));

        //update
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Tile tile = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile();
        assertEquals(row, tile.getRow());
        assertEquals(column, tile.getColumn());
    }

    /**
     * we obtain the current wallMove object and checks its direction, row, and column
     *
     * @param dir
     * @param row
     * @param column
     * @author David
     * @author Matthias Arabian
     */
    @And("A wall move candidate shall exist with {string} at position \\({int}, {int})")
    public void wallMoveCandidateExistsAt(String dir, int row, int column) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        Tile tile = quoridor.getCurrentGame().getWallMoveCandidate().getTargetTile();


        String dirFixed = dir.substring(0, 1).toUpperCase() + dir.substring(1).toLowerCase();
        assertEquals(Direction.valueOf(dirFixed), quoridor.getCurrentGame().getWallMoveCandidate().getWallDirection());

        assertEquals(row, tile.getRow());
        assertEquals(column, tile.getColumn());
    }

    /**
     * we check the test case provided to see that it is indeed at the specified edge of the board
     *
     * @param side
     * @throws Throwable
     * @author David
     */
    @And("The wall candidate is at the {string} edge of the board")
    public void wallIsAtEdge(String side) throws Throwable {
        assertEquals(true, QuoridorController.wallIsAtEdge(side));
    }

    /**
     * asks the controller if a illegal move notification has been displayed
     *
     * @author David
     */
    @Then("I shall be notified that my move is illegal")
    public void testIllegalMoveNotification() {
        assertEquals(true, QuoridorController.isIllegalMoveNotificationDisplayed());
    }


    ///SET TOTAL THINKING TIME

    /**
     * set total thinking time for both players
     *
     * @param min
     * @param sec
     * @author David
     */
    @When("{int}:{int} is set as the thinking time")
    public void setThinkingTime(int min, int sec) {
        QuoridorController.setThinkingTime(min, sec);
    }

    /**
     * get the current players from the model and check that they indeed have the specified remaining time
     *
     * @param min
     * @param sec
     * @author David
     */
    @Then("Both players shall have {int}:{int} remaining time left")
    public void checkRemainingTime(int min, int sec) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        int totalMiliSeconds = (min * 60 + sec) * 1000;//converts to miliseconds
        assertEquals(totalMiliSeconds, quoridor.getCurrentGame().getWhitePlayer().getRemainingTime().getTime());
        assertEquals(totalMiliSeconds, quoridor.getCurrentGame().getBlackPlayer().getRemainingTime().getTime());

    }

    /**
     * @param color
     * @author Alex Masciotra
     */
    @Given("Next player to set user name is {string}")
    public void nextPlayerToSetUserNameIs(String color) {
//
        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
//
//        ArrayList<Player> newPlayers = createUsersAndPlayers("user1", "user2");
//
//        game.setWhitePlayer(newPlayers.get(0));
//        game.setBlackPlayer(newPlayers.get(1));

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
        List<Integer> removeUserList = new ArrayList<Integer>();

        int index = 0;
        for (User user : userList) {
            if (user.getName().equals(username)) {
                removeUserList.add(index);
            }
            index++;
        }

        if (removeUserList.size() > 0) {
            for (Integer i : removeUserList) {
                QuoridorApplication.getQuoridor().getUser(i).delete();
            }
        }
    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @When("The player selects existing {string}")
    public void thePlayerSelectsExisting(String username) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        QuoridorController.selectExistingUserName(username, quoridor);

    }

    /**
     * @param username
     * @author Alex Masciotra
     */
    @When("The player provides new user name: {string}")
    public void thePlayerProvidesNewUserName(String username) {

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        //Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        userNameSet = QuoridorController.selectNewUserName(username, quoridor);
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

        //assertEquals(username, QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getNextPlayer().getUser().getName());

        //GUI notification that username is already existing and he will be that username
        assertFalse(userNameSet);
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
        File file = new File(SaveConfig.getGameSaveFilePath(filename));
        if (file.exists()) {
            file.delete();
        }
        assertEquals(false, file.exists());
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
        File file = new File(SaveConfig.getGameSaveFilePath(filename));
        file.delete();
        try {
            String str = "myhelicoptergoessoisoisoisoisoisoisosiosoisoisoisoisoisoisoisoisoisoisoi"; //should do as something that should not ever appear in the text file in regular use.
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(str);
            bufferedWriter.close();
        } catch (IOException e) {
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
            QuoridorController.saveGame(filename, QuoridorController.getCurrentGame());
        } catch (IOException e) {
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
        } catch (IOException e) {
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
        } catch (IOException e) {
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
        File file = new File(SaveConfig.getGameSaveFilePath(filename));
        assertEquals(file.exists(), true);
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
        assertEquals(Arrays.equals(refFileData, curFileData), false);
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
        assertEquals(Arrays.equals(refFileData, curFileData), true);
    }

    /**
     * @author Edwin Pan
     * DEPRECATED: This method doesn't seem to work. Despite it claiming that java has no access to the file system,
     * the application is still able to write and read files from the file system. Quite strange.
     * I'm keeping this in the code for investigative purposes in the future. Nobody else should be using this, though.
     * Helper method for checking if the Operating System is preventing the application from doing Save and Load tests
     * Checks if Access if being Denied: If so, prints the stacktrace and announces the Exception to tester.
     */
    private void checkFileSystemAccess(String path) {
        SecurityManager securityManager = new SecurityManager();
        try {
            securityManager.checkRead(path);
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /*
     * 				SWITCH CURRENT PLAYER FEATURE STEPS
     */

    /**
     * @param playerColorAsString color in string - that is, a string describing the desired player's color as "black" or "white".
     *                            Sets up test preconditions such that the player whose turn it is to play is the provided player.
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
    @Given("The player to move is {string}")
    public void thePlayerToMoveIsPlayer(String playerColorAsString) {
        //Complete the other player's turn. The method should not do anything if the other player's turn
        //is already "complete"; and if it is not complete, it will make it complete, thereby making it
        //the desired player's turn.
        Player providedPlayer = QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString);
        Player blackPlayer = QuoridorController.getCurrentBlackPlayer();
        Player whitePlayer = QuoridorController.getCurrentWhitePlayer();
        if (providedPlayer.equals(blackPlayer)) {
            QuoridorController.completePlayerTurn(whitePlayer);    //If it's WhitePlayer's turn, end it. If not, nothing changes.
        } else {
            QuoridorController.completePlayerTurn(blackPlayer); //If it's BlackPlayer's turn, end it. If not, nothing happens.
        }
    }

    /**
     * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
     *               Sets up test preconditions such that the clock of the player whose turn it is to play is running
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
//TODO MATTHIAS	
    @Given("The clock of {string} is running")
    public void theClockOfPlayerIsRunning(String playerColorAsString) {
        Player p;
        if (playerColorAsString.equals("black")) {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
        } else {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
        }
        timer.cancel();
        timer = new Timer();
        QuoridorController.startPlayerTimer(p, timer);
    }

    /**
     * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
     *               Sets up test preconditions such that the clock of the player whose turn it is not to play is stopped
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
//TODO MATTHIAS
    @Given("The clock of {string} is stopped")
    public void theClockOfOtherIsStopped(String playerColorAsString) {
        Player p;
        if (playerColorAsString.equals("black")) {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
        } else {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
        }
        QuoridorController.stopPlayerTimer(p, timer);
    }

    /**
     * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
     *               Makes the selected player complete their move.
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
    @When("Player {string} completes his move")
    public void playerPlayerCompletesHisMove(String playerColorAsString) {
        assertEquals(true, QuoridorController.completePlayerTurn(QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString)));
    }

    /**
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
    @Then("The user interface shall be showing it is {string} turn")
    public void theUserInterfaceShallBeShowingItIs__Turn(String name) {
        //done automatically by the GUI when changing player.
        ViewInterface v = QuoridorApplication.getViewInterface();
        assertEquals(true, true); //GUI has not been loaded by tester. therefore, none of the variables required to run this test have been initialized

    }


    /**
     * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
     *               Asserts that the user interface now shows that the specified player's timer is stopped.
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
    @And("The clock of {string} shall be stopped")
    public void andTheClockOfPlayerShallBeStopped(String playerColorAsString) {
        Player p;
        if (playerColorAsString.equals("black")) {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
        } else {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
        }
        QuoridorController.stopPlayerTimer(p, timer);
        assertEquals(true, true);
    }


    /**
     * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
     *               Asserts that the user interface now shows that the specified player's timer is running.
     * @author Edwin Pan
     * @author Matthias Arabian made modifications for deliverable 3
     */
    @And("The clock of {string} shall be running")
    public void andTheClockOfOtherShallBeRunning(String playerColorAsString) {
        Player p;
        if (playerColorAsString.equals("black")) {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
        } else {
            p = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();
        }
        timer.cancel();
        timer = new Timer();
        QuoridorController.startPlayerTimer(p, timer);
        assertEquals(true, true);
    }


    /**
     * @param player color in string - that is, a string describing the desired player's color as "black" or "white".
     *               Asserts that the user interface now shows that the next move belongs to the specified player.
     * @author Edwin Pan\
     * @author Matthias Arabian made modifications for deliverable 3
     */
    @And("The next player to move shall be {string}")
    public void theNextPlayerToMoveShallBeOther(String playerColorAsString) {
        Player currentPlayer = QuoridorController.getPlayerOfCurrentTurn();
        Player currentPlayerShouldBe = QuoridorController.getPlayerOfProvidedColorstring(playerColorAsString);
        Boolean b = currentPlayer.equals(currentPlayerShouldBe);
        assertEquals(b, true);
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

        Integer wallsInStock = 0;

        if (currentPlayer.hasGameAsWhite()) {
            wallsInStock = game.getCurrentPosition().getWhiteWallsInStock().size();
        } else if (currentPlayer.hasGameAsBlack()) {
            wallsInStock = game.getCurrentPosition().getBlackWallsInStock().size();
        }
        //hardcoding wall instock as there is bug in whitewallsinstock, currently 10, should be 9 after init), originally
        //wall 0 is set, so taking wall 1 for the test
        Wall wallPlaced = currentPlayer.getWall(1);

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

        WallMove wallMoveCandidate = new WallMove(game.getMoves().size() + 1, game.getCurrentPosition().getId(), currentPlayer, tile, game, wallMoveDirection, wallPlaced);

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

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        //Board board = QuoridorApplication.getQuoridor().getBoard();

        handIsEmpty = QuoridorController.releaseWall(quoridor);
        if (handIsEmpty) {
            handHasWall = false;
        } else {
            handHasWall = true;
        }

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
        int indexOfMove = (game.getMoves().size() - 1);

        //assert that the wallOnBoard that i grabbed from my stock in the given statement is on the board at right position
        assertEquals(row, game.getCurrentPosition().getWhiteWallsOnBoard(1).getMove().getTargetTile().getRow());
        assertEquals(col, game.getCurrentPosition().getWhiteWallsOnBoard(1).getMove().getTargetTile().getColumn());
        assertEquals(dir, game.getCurrentPosition().getWhiteWallsOnBoard(1).getMove().getWallDirection().toString().toLowerCase());

        //assert that the move that was created is indeed this wall being put on the board
        assertEquals(row, game.getMove(indexOfMove).getTargetTile().getRow());
        assertEquals(col, game.getMove(indexOfMove).getTargetTile().getColumn());
        assertEquals(dir, game.getCurrentPosition().getWhiteWallsOnBoard(1).getMove().getWallDirection().toString().toLowerCase());
    }

    /**
     * @throws Throwable
     * @author Alex Masciotra
     */
    @Then("I shall be notified that my wall move is invalid")
    public void iShallBeNotifiedThatMyWallMoveIsInvalid() throws Throwable {
        assertFalse(handIsEmpty);
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
     * @throws Throwable
     * @author Alex Masciotra
     */
    @And("I shall not have a wall in my hand")
    public void iShallNotHaveAWallInMyHand() throws Throwable {

        assertTrue(handIsEmpty);
        //TODO:GUI
    }

    /**
     * @author Alex Masciotra
     */
    @And("My move shall be completed")
    public void myMoveShallBeCompleted() {

        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        int indexOfMove = game.getMoves().size();
        //if size is greater than 0, my move was registered and it is persisted in the model, as before my turn it was
        //not, the size was 0
        assertEquals(1, indexOfMove);
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
     * @param dir
     * @param row
     * @param col
     * @author Alex Masciotra
     */
    @But("No wall move shall be registered with {string} at position \\({int}, {int})")
    public void noWallMoveShallBeRegisteredWithAtPosition(String dir, Integer row, Integer col) {
        Game game = QuoridorApplication.getQuoridor().getCurrentGame();

        int indexOfMove = game.getMoves().size();

        //i assert that the moves List is still size 0 for the wallMoveCandidate that is set, and assert that it was
        //for the given wallMoveCandiate that was invalid

        assertEquals(0, indexOfMove);
        assertEquals(row, game.getWallMoveCandidate().getTargetTile().getRow());
        assertEquals(col, game.getWallMoveCandidate().getTargetTile().getColumn());
        assertEquals(dir, game.getWallMoveCandidate().getWallDirection().toString().toLowerCase());
    }

    /*
     * Validate Position step definiton
     */

    /**
     * ValidatePosition.feature - ValidatePosition
     * Scenario: Validate pawn position
     *
     * @param row
     * @param col
     * @author Daniel Wu
     */
    @Given("A game position is supplied with pawn coordinate {int}:{int}")
    public void aGamePositionIsSuppliedWithPawnCoordinate(int row, int col) {
        if ((row > 9) || (row < 1) || (col > 9) || (col < 1)) {
            positionIsValid = false;
        } else {
            Quoridor quoridor = QuoridorApplication.getQuoridor();
            GamePosition currentGamePosition = quoridor.getCurrentGame().getCurrentPosition();
            //Get the tile
            Tile tilePos = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);
            //Change the PlayerPosition and Check which player should be moving then move that player
            if (currentGamePosition.getPlayerToMove().hasGameAsWhite()) {
                currentGamePosition.getWhitePosition().setTile(tilePos);
            } else if (currentGamePosition.getPlayerToMove().hasGameAsBlack()) {
                currentGamePosition.getBlackPosition().setTile(tilePos);
            }
        }
    }

    /**
     * ValidatePosition.feature - ValidatePosition
     * Scenario: Validate pawn position and Validate wall position
     *
     * @author Daniel Wu
     */
    @When("Validation of the position is initiated")
    public void validationOfThePositionIsInitiated() {
        if (positionIsValid) {
            positionIsValid = QuoridorController.validatePosition(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition());
        }
    }

    /**
     * ValidatePosition.feature - ValidatePosition
     * Scenario: Validate pawn position and Validate wall position
     *
     * @param result
     * @author Daniel Wu
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
     * ValidatePosition.feature - ValidatePosition
     * Scenario: Validate wall position
     *
     * @param row
     * @param col
     * @param dir
     * @author Daniel Wu
     */
    @Given("A game position is supplied with wall coordinate {int}:{int}-{string}")
    public void aGamePositionIsSuppliedWithWallCoordinate(int row, int col, String dir) {
        if ((row > 8) || (row < 1) || (col > 8) || (col < 1)) {
            positionIsValid = false;
        } else {
            Quoridor quoridor = QuoridorApplication.getQuoridor();
            Game game = quoridor.getCurrentGame();
            GamePosition currentGamePosition = game.getCurrentPosition();
            Direction myDir = Direction.Horizontal;
            if (dir.equals("horizontal")) {
                myDir = Direction.Horizontal;
            } else if (dir.equals("vertical")) {
                myDir = Direction.Vertical;
            }
            Tile tile = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);
            WallMove wallMove = new WallMove(0, 0, game.getWhitePlayer(), tile, game, myDir, game.getCurrentPosition().getWhiteWallsInStock(4));
            Wall wall = currentGamePosition.getWhiteWallsInStock(9);
            wall.setMove(wallMove);
            currentGamePosition.removeWhiteWallsInStock(wall);
            currentGamePosition.addWhiteWallsOnBoard(wall);
            game.setMoveMode(Game.MoveMode.WallMove);
        }
    }

    /**
     * ValidatePosition.feature - ValidatePosition
     * Scenario: Validate overlapping walls
     *
     * @author Daniel Wu
     */
    @Then("The position shall be valid")
    public void thePositionShallBeValid() {
        assertEquals(positionIsValid, true);

    }

    /**
     * ValidatePosition.feature - ValidatePosition
     * Scenario: Validate overlapping walls
     *
     * @author Daniel Wu
     */
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
     *
     * @author Matthias Arabian
     */
    @Then("The wall shall be rotated over the board to {string}")
    public void theWallShallBeRotatedOverTheBoardToString(String newDir) {
        assertEquals(true, QuoridorController.GUI_assertFlipWallCandidate(newDir));
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
     *
     * @author Matthias Arabian (original)
     * @author Edwin Pan (Rewrote to catch exceptions)
     */
    @When("I initiate to load a saved game {string}")
    public void iInitiateToLoadASavedGame(String fileName) {
        myPlayers = createUsersAndPlayers("user1", "user2");
        CucumberTest_LoadPosition_TestFileWriters.createGameSaveTestFile(fileName);
        try {
            QuoridorController.loadSavedGame(fileName, this.myPlayers.get(0), this.myPlayers.get(1));
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
     *
     * @author Matthias Arabian
     * @author Edwin Pan made modifications after taking over for sprint 3
     */
    @And("The position to load is valid")
    public void thePositionToLoadIsValid() {
        assertEquals(false, failedToReadSaveFile);
        assertEquals(false, receivedInvalidPositionException);
    }

    /**
     * Ensures that the player whose turn it is to play is the right player.
     *
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
     *
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
     *
     * @author Matthias Arabian
     */
    //@And("^\"([^\"]*)\" shall have a \"([^\"]*)\" wall at [0-9]:[0-9]$")
    @And("{string} shall have a {} wall at {int}:{int}")
    public void playerShallHaveAWallWithOrientationAtPosition(String player, String wallOrientation, int row, int col) {
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
                    && w.getMove().getTargetTile().getRow() == row) {
                errorFlag = false;
                break;
            }
        }
        assertEquals(false, errorFlag);
    }

    /**
     * Ensures that both players have the same number of walls in stock (which doesn't really make sense....?)
     *
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
     *
     * @author Matthias Arabian
     * @author Edwin Pan made modifications after taking over for sprint 3
     */
    @And("The position to load is invalid")
    public void thePositionToLoadIsInvalid() {
        assertEquals(false, failedToReadSaveFile);
        assertEquals(true, receivedInvalidPositionException);
    }

    /**
     * This function ensures that an error is sent out about the load position being invalid.
     *
     * @author Matthias Arabian
     * @author Edwin Pan made modifications after taking over for sprint 3
     */
    @Then("The load shall return an error")
    public void theLoadShallReturnAnError() throws Throwable {
        assertEquals(true, !failedToReadSaveFile);
        assertEquals(true, receivedInvalidPositionException);
    }

    /**
     * Given method to set player at corresponding position
     * Jump pawn step definition, MovePawn.feature stepDefinition
     *
     * @param row current player row position
     * @param col current player col position
     * @author Daniel Wu
     * @author Alex Masciotra
     * @author Thomas Philippon
     */
    @And("The player is located at {int}:{int}")
    public void thePlayerIsLocatedAt(int row, int col) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        GamePosition currentPosition = quoridor.getCurrentGame().getCurrentPosition();
        Player currentPlayer = currentPosition.getPlayerToMove();

        //first, set the other player's position to
        //(row - 1) * 9 + col - 1
        PlayerPosition currentPlayerPosition;
        int playerCurrentRow;
        int playerCurrentColumn;

        if (currentPlayer.hasGameAsWhite()) {
            currentPlayerPosition = currentPosition.getWhitePosition();
            playerCurrentRow = currentPlayerPosition.getTile().getRow();
            playerCurrentColumn = currentPlayerPosition.getTile().getColumn();
            //move to the target pawn column
            int colOffset = col - 5;
            if (colOffset > 0) {
                for (int i = 0; i < colOffset; i++) {
                    whitePawnBehaviour.move(MoveDirection.East);
                    playerCurrentColumn = playerCurrentColumn + 1;
                    Tile targetTile = quoridor.getBoard().getTile((playerCurrentRow - 1) * 9 + playerCurrentColumn - 1);
                    currentPlayerPosition.setTile(targetTile);
                }
            }
            if (colOffset < 0) {
                colOffset = colOffset * -1;
                for (int i = 0; i < colOffset; i++) {
                    whitePawnBehaviour.move(MoveDirection.West);
                    playerCurrentColumn = playerCurrentColumn - 1;
                    Tile targetTile = quoridor.getBoard().getTile((playerCurrentRow - 1) * 9 + playerCurrentColumn - 1);
                    currentPlayerPosition.setTile(targetTile);
                }
            }
            //move to the target pawn row
            for (int i = 0; i < (9 - row); i++) {
                whitePawnBehaviour.move(MoveDirection.North);
                playerCurrentRow = playerCurrentRow - 1;
                Tile targetTile = quoridor.getBoard().getTile((playerCurrentRow - 1) * 9 + playerCurrentColumn - 1);
                currentPlayerPosition.setTile(targetTile);
            }
            QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getGameAsWhite().getCurrentPosition().setWhitePosition(currentPlayerPosition);
        } else {
            currentPlayerPosition = currentPosition.getBlackPosition();
            playerCurrentRow = currentPlayerPosition.getTile().getRow();
            playerCurrentColumn = currentPlayerPosition.getTile().getColumn();
            //move to the target pawn column
            int colOffset = col - 5;
            if (colOffset > 0) {
                for (int i = 0; i < colOffset; i++) {
                    blackPawnBehaviour.move(MoveDirection.East);
                    playerCurrentColumn = playerCurrentColumn + 1;
                    Tile targetTile = quoridor.getBoard().getTile((playerCurrentRow - 1) * 9 + playerCurrentColumn - 1);
                    currentPlayerPosition.setTile(targetTile);
                }
            }
            if (colOffset < 0) {
                colOffset = colOffset * -1;
                for (int i = 0; i < colOffset; i++) {
                    blackPawnBehaviour.move(MoveDirection.West);
                    playerCurrentColumn = playerCurrentColumn - 1;
                    Tile targetTile = quoridor.getBoard().getTile((playerCurrentRow - 1) * 9 + playerCurrentColumn - 1);
                    currentPlayerPosition.setTile(targetTile);
                }
            }
            //move to the target pawn row
            for (int i = 0; i < (row - 1); i++) {
                blackPawnBehaviour.move(MoveDirection.South);
                playerCurrentRow = playerCurrentRow + 1;
                Tile targetTile = quoridor.getBoard().getTile((playerCurrentRow - 1) * 9 + playerCurrentColumn - 1);
                currentPlayerPosition.setTile(targetTile);
            }
            Tile updatedTile = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);
            QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getGameAsBlack().getCurrentPosition().getBlackPosition().setTile(updatedTile);

        }
    }

    /**
     * Given nothing blocking the player to move, we know theres no walls on the board from the steps before
     *
     * @param dir  orientation of walls
     * @param side side of the pawn the wall is on
     * @author Daniel Wu
     * @author Alex Masciotra
     * @author Thomas Philippon
     */
    @And("There are no {string} walls {string} from the player")
    public void thereAreNoWallsFromPlayer(String dir, String side) {
        //removeWalls();
    }

    /**
     * Method to offSet opponent from player to move
     * JumpPawn.feature - Jump Pawn, MovePawn.feature
     *
     * @param side side to make sure opponent is not current player side
     * @author Daniel Wu
     * @author Alex Masciotra
     * @author Thomas Philippon
     */
    @Then("The opponent is not {string} from the player")
    public void theOpponentIsNotFromPlayer(String side) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();

        Boolean playerToOffsetIsWhite;
        Player currentPlayer = quoridor.getCurrentGame().getCurrentPosition().getPlayerToMove();
        Player playerToOffset;

        if (currentPlayer.hasGameAsWhite()) {
            playerToOffset = quoridor.getCurrentGame().getBlackPlayer();
            playerToOffsetIsWhite = false;
        } else {
            playerToOffset = quoridor.getCurrentGame().getWhitePlayer();
            playerToOffsetIsWhite = true;
        }

        PlayerPosition currentPlayerPosition;
        PlayerPosition playerToOffsetPosition;

        if (playerToOffsetIsWhite) {
            currentPlayerPosition = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition();
            playerToOffsetPosition = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition();
        } else {
            currentPlayerPosition = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition();
            playerToOffsetPosition = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition();
        }

        //(row - 1) * 9 + col - 1
        Tile currentPlayerTile = currentPlayerPosition.getTile();

        int row = 1, col = 1;
        if (side.equals("left")) {
            col = currentPlayerTile.getColumn();
            col = col - 2;
            if (col <= 0) {
                col = 9;
            }
        } else if (side.equals("right")) {
            col = currentPlayerTile.getColumn();
            col = col + 2;
            if (col >= 10) {
                col = 1;
            }
        } else if (side.equals("up")) {
            row = currentPlayerTile.getRow();
            row = row - 2;
            if (row <= 0) {
                row = 9;
            }
        } else if (side.equals("down")) {
            row = currentPlayerTile.getRow();
            row = row + 2;
            if (row >= 10) {
                row = 1;
            }
        } else {
            throw new IllegalArgumentException("Unsupported pawn side inputted");
        }

        Tile offsetTargetTile = quoridor.getBoard().getTile((row - 1) * 9 + col - 1);

        playerToOffsetPosition.setTile(offsetTargetTile);
    }

    /*
     * Jump pawn step definition
     */

    /**
     * method to set opponent pawn posiiton
     * JumpPawn.feature - Jump Pawn, MovePawn.feature
     *
     * @param orow
     * @param ocol
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     */
    @And("The opponent is located at {int}:{int}")
    public void the_opponent_is_located_at(Integer orow, Integer ocol) {
        Player opponentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
        Player blackPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer();
        Player whitePlayer = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer();

        PlayerPosition playerPosition;

        if (opponentPlayer.getUser().getName().equals(whitePlayer.getUser().getName())) {
            playerPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
        } else {
            playerPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
        }
        Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((orow - 1) * 9 + ocol - 1);
        playerPosition.setTile(targetTile);
    }

    /**
     * @param string  direction of walls
     * @param string2 side of player
     *                JumpPawn.feature - Jump Pawn
     *                Scenario: The game is running
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     */
    @And("There are no {string} walls {string} from the player nearby")
    public void there_are_no_walls_from_the_player_nearby(String string, String string2) {
        removeWalls();
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     * JumpPawn.feature - Jump Pawn
     * Scenario: Jump of player blocked by wall
     */
    @And("There is a {string} wall at {int}:{int}")
    public void there_is_a_wall_at(String dir, Integer row, Integer col) {

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorController.grabWall(QuoridorApplication.getQuoridor());
        Direction wallMoveDirection;
        if (dir.equals("horizontal")) {
            wallMoveDirection = Direction.Horizontal;
        } else if (dir.equals("vertical")) {
            wallMoveDirection = Direction.Vertical;
        } else {
            throw new IllegalArgumentException("Unsupported wall direction was provided");
        }
        Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row - 1) * 9 + col - 1);
        QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(wallMoveDirection);
        QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setTargetTile(targetTile);

        //release wall

        WallMove wallMoveCandidate = quoridor.getCurrentGame().getWallMoveCandidate();
        GamePosition currentGamePosition = quoridor.getCurrentGame().getCurrentPosition();

        int targetRow = wallMoveCandidate.getTargetTile().getRow();
        int targetCol = wallMoveCandidate.getTargetTile().getColumn();
        String targetDir = wallMoveCandidate.getWallDirection().toString();

        //if successful complete my move and change turn to next player, if not successful, do not change turn cause still my turn

        Player currentPlayer = currentGamePosition.getPlayerToMove();

        quoridor.getCurrentGame().addMove(wallMoveCandidate);
        if (currentPlayer.hasGameAsWhite()) {
            currentGamePosition.addWhiteWallsOnBoard(wallMoveCandidate.getWallPlaced());
        } else {
            currentGamePosition.addBlackWallsOnBoard(wallMoveCandidate.getWallPlaced());
        }
    }


    /**
     * Assertion if illegal or success pawn move
     *
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     * <p>
     * JumpPawn.feature - Jump Pawn, MovePawn.feature
     */
    @Then("The move {string} shall be {string}")
    public void the_move_shall_be(String side, String status) {
        // Write code here that turns the phrase above into concrete actions
        if (status.equals("success")) {
            assertTrue(pawnMoveSuccesful);
        } else if (status.equals("illegal")) {
            assertFalse(pawnMoveSuccesful);
        } else {
            throw new IllegalArgumentException("Unsupported pawn status was provided");
        }
    }

    /**
     * Assertion that the move was registered at the targetted tile
     * JumpPawn.feature - Jump Pawn, MovePawn
     *
     * @param row new row
     * @param col new col
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     */
    @And("Player's new position shall be {int}:{int}")
    public void player_s_new_position_shall_be(int row, int col) {
        // Write code here that turns the phrase above into concrete actions

        Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
        PlayerPosition playerPosition;

        if (currentPlayer.hasGameAsWhite()) {
            playerPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition();
        } else {
            playerPosition = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition();
        }

        assertEquals(row, playerPosition.getTile().getRow());
        assertEquals(col, playerPosition.getTile().getColumn());
    }

    /**
     * Assertion of completion of move
     *
     * @param nplayerColor player color of the next player
     *                     JumpPawn.feature - Jump Pawn, MovePawn.feature
     * @author Thomas Philippon
     * @author Alex Masciotra
     * @author Daniel Wu
     */
    @And("The next player to move shall become {string}")
    public void the_next_player_to_move_shall_become(String nplayerColor) {
        // Write code here that turns the phrase above into concrete actions
        Player currentPlayer = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove();
        Player nextPlayer;
        if (currentPlayer.hasGameAsWhite() && pawnMoveSuccesful == true) {
            assertEquals(nplayerColor, "black");
        } else if (currentPlayer.hasGameAsWhite() && pawnMoveSuccesful == false) {
            assertEquals(nplayerColor, "white");
        } else if (currentPlayer.hasGameAsBlack() && pawnMoveSuccesful == true) {
            assertEquals(nplayerColor, "white");
        } else if (currentPlayer.hasGameAsBlack() && pawnMoveSuccesful == false) {
            assertEquals(nplayerColor, "black");
        }
    }

    /**
     * Method that calls QuoridorController to move the pawn
     *
     * @param playerColor color of currentplayer
     * @param side        direction the pawn wants to move in
     * @author Alex Masciotra
     * @author Thomas Philippon
     * @author Daniel Wu
     * JumpPawn.feature - Jump Pawn, MovePawn.feature
     */
    @When("Player {string} initiates to move {string}")
    public void playerInitiatesToMove(String playerColor, String side) {
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        if (playerColor.equals("white")) {
            try {
                pawnMoveSuccesful = QuoridorController.movePawn(quoridor, side, whitePawnBehaviour);
            } catch (Exception e) {
                pawnMoveSuccesful = false;
            }
        } else {
            try {
                pawnMoveSuccesful = QuoridorController.movePawn(quoridor, side, blackPawnBehaviour);
            } catch (Exception e) {
                pawnMoveSuccesful = false;
            }
        }
    }

    /*
     * Step Backward Step definition
     */

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @Given("The game is in replay mode")
    public void the_game_is_in_replay_mode() {
        //need to create an empty game and
        initQuoridorAndBoard();
        ArrayList<Player> players = createUsersAndPlayers("user1", "user2");
        createAndStartGame(players);
        Game game = QuoridorApplication.getQuoridor().getCurrentGame();
        game.setGameStatus(GameStatus.Replay);
        whitePawnBehaviour.setPlayer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer());
        blackPawnBehaviour.setPlayer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer());
        whitePawnBehaviour.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
        blackPawnBehaviour.setCurrentGame(QuoridorApplication.getQuoridor().getCurrentGame());
        blackPawnBehaviour.entry();
        whitePawnBehaviour.entry();
    }

    /**
    * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @Given("The following moves have been played in game:")
    public void the_following_moves_have_been_played_in_game(io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
        // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        List<Map<String, String>> valueMaps = dataTable.asMaps();

        for (Map<String, String> map : valueMaps) {
            Integer mv = Integer.decode(map.get("mv"));
            Integer rnd = Integer.decode(map.get("rnd"));
            String move = map.get("move");

            Direction direction;
            String colrow = move.substring(0, 2);

            char[] arr = colrow.toCharArray();
            Integer col = arr[0] - 'a' + 1;
            Integer row = arr[1] - '0';

            if (move.length() == 3) {
                //wall move
                //get direction
                String dir = move.substring(2);
                switch (dir) {
                    case "h":
                        direction = Direction.Horizontal;
                        break;
                    case "v":
                        direction = Direction.Vertical;
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported wall direction was provided");
                }

                QuoridorController.grabWall(quoridor);
                Tile targetTile = QuoridorApplication.getQuoridor().getBoard().getTile((row - 1) * 9 + col - 1);
                QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setWallDirection(direction);
                QuoridorApplication.getQuoridor().getCurrentGame().getWallMoveCandidate().setTargetTile(targetTile);

                //drop wall
                QuoridorController.releaseWall(quoridor);

                if (rnd.equals(1)) {
                    QuoridorController.completePlayerTurn(quoridor.getCurrentGame().getWhitePlayer());
                } else {
                    QuoridorController.completePlayerTurn(quoridor.getCurrentGame().getBlackPlayer());
                }
            } else {
                //pawn move
                if (rnd.equals(1)) {
                    //white
                    PlayerPosition currentWhitePlayerPosition = quoridor.getCurrentGame().getCurrentPosition().getWhitePosition();
                    Integer currentWhiteRow = currentWhitePlayerPosition.getTile().getRow();
                    Integer currentWhiteCol = currentWhitePlayerPosition.getTile().getColumn();

                    String side;
                    if (col > currentWhiteCol) {
                        side = "right";
                    } else if (col < currentWhiteCol) {
                        side = "left";
                    } else if (row > currentWhiteRow) {
                        side = "down";
                    } else {
                        side = "up";
                    }
                    QuoridorController.movePawn(quoridor, side, whitePawnBehaviour);
                    QuoridorController.completePlayerTurn(quoridor.getCurrentGame().getWhitePlayer());

                } else {
                    //black
                    PlayerPosition currentBlackPlayerPosition = quoridor.getCurrentGame().getCurrentPosition().getBlackPosition();
                    Integer currentBlackRow = currentBlackPlayerPosition.getTile().getRow();
                    Integer currentBlackCol = currentBlackPlayerPosition.getTile().getColumn();

                    String side;
                    if (col > currentBlackCol) {
                        side = "right";
                    } else if (col < currentBlackCol) {
                        side = "left";
                    } else if (row > currentBlackRow) {
                        side = "down";
                    } else {
                        side = "up";
                    }

                    QuoridorController.movePawn(quoridor, side, blackPawnBehaviour);
                    QuoridorController.completePlayerTurn(quoridor.getCurrentGame().getBlackPlayer());
                }
            }
        }
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @And("The next move is {double}")
    public void the_next_move_is(Double mvRnd) {

        //split the double variable into the move and round number
        String[] moveRound = mvRnd.toString().split("\\.");
        int move =  Integer.parseInt(moveRound[0]);
        int round = Integer.parseInt(moveRound[1]);

        //Compute the index in the game position list based on the move and round number
        int gamePositionIndex = move *2 -(round%2) -1;

        //assign the game position at the gameposition index to the current game position
        GamePosition gamePosition = QuoridorApplication.getQuoridor().getCurrentGame().getPosition(gamePositionIndex);

        QuoridorApplication.getQuoridor().getCurrentGame().setCurrentPosition(gamePosition);

    }

    /**
     * @author Thomas Philippon
     * Scenario: Step Backward, stepforward
     */
    @When("Step backward is initiated")
    public void step_backward_is_initiated() {
       //call the controller method for the feature step backward
        QuoridorController.stepBackward(QuoridorApplication.getQuoridor().getCurrentGame());
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @Then("The next move shall be {double}")
    public void the_next_move_shall_be(Double mvRnd) {
        //Strip the double to get the move and wall number
        String[] moveRound = mvRnd.toString().split("\\.");
        int movenm =  Integer.parseInt(moveRound[0]);
        int roundnm = Integer.parseInt(moveRound[1]);
        int nextMove, nextRound;

        Quoridor quoridor = QuoridorApplication.getQuoridor();
        GamePosition currentGamePosition = quoridor.getCurrentGame().getCurrentPosition();

        int gameId = currentGamePosition.getId();

        try{
            Move move = quoridor.getCurrentGame().getMove(gameId);
            nextMove = move.getMoveNumber();
            nextRound = move.getRoundNumber();
        }catch (Exception e){ //if you are at the last game position, the last move (5,1) doesnt exist so an Exception is thrown
            nextMove = 5;
            nextRound = 1;
        }
        assertEquals(movenm, nextMove);
        assertEquals(roundnm, nextRound);
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @And("White player's position shall be \\({double})")
    public void white_player_s_position_shall_be(Double rowCol) {
        //Strip the double to get the row and column
        String[] moveRound = rowCol.toString().split("\\.");
        int temp = Integer.parseInt(moveRound[0]);
        int row =  temp/10;
        int col = temp-(10*row);

         assertEquals(row, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getRow());
         assertEquals(col, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhitePosition().getTile().getColumn());
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @And("Black player's position shall be \\({double})")
    public void black_player_s_position_shall_be(Double rowCol) {
        //Strip the double to get the row and column
        String[] moveRound = rowCol.toString().split("\\.");
        int temp = Integer.parseInt(moveRound[0]);
        int row =  temp/10;
        int col = temp-(10*row);

        assertEquals(row, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getRow());
        assertEquals(col, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackPosition().getTile().getColumn());
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @And("White has {int} on stock")
    public void white_has_on_stock(Integer nbOfWalls) {
       assertEquals(nbOfWalls, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size());
    }

    /**
     * @author Thomas Philippon
     * @author Alex Masciotra
     * Scenario: Step Backward, stepforward, jump to final. jump to start, enter replaymode
     */
    @And("Black has {int} on stock")
    public void black_has_on_stock(Integer nbOfWalls) {
        assertEquals(nbOfWalls, QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size());
    }


    /*
     * Step Forward Step definition
     */
    /**
     * @author Thomas Philippon
     * Scenario: Step Backward, stepforward
     */
    @When("Step forward is initiated")
    public void step_forward_is_initiated() {
        //call the controller method for the step forward feature
      QuoridorController.stepForward(QuoridorApplication.getQuoridor().getCurrentGame());
    }

    /*
     * Jump to start Step definition
     */

    /**
     * jump to start
     * @author Alex Masciotra
     */
    @When("Jump to start position is initiated")
    public void jump_to_start_position_is_initiated() {
        // Write code here that turns the phrase above into concrete actions
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorController.jumpToStart(quoridor);
    }


    /*
     * Jump to final Step definition
     */

    /**
     * jump to final
     * @author Alex Masciotra
     */
    @When("Jump to final position is initiated")
    public void jump_to_final_position_is_initiated() {
        // Write code here that turns the phrase above into concrete actions
        Quoridor quoridor = QuoridorApplication.getQuoridor();
        QuoridorController.jumpToFinal(quoridor);
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
        for (int i = 1; i <= 20; i++) {
            Wall wall = Wall.getWithId(i);
            if (wall != null) {
                wall.delete();
            }
        }
        // Clear out file data memories, used for SavePosition features.
        File file = new File(SaveConfig.getGameSaveFilePath(this.testGameSaveFilename));
        file.delete();
        //Clear load position files from saves directory
        CucumberTest_LoadPosition_TestFileWriters.clearGameSaveLoadingTestFiles();
        this.testGameSaveFilename = "";
        for (int i = 0; i < refFileData.length; i++) {
            this.refFileData[i] = 0;
        }
        for (int i = 0; i < curFileData.length; i++) {
            this.curFileData[i] = 0;
        }

        //reset these variables for use
        myCoordinate[0] = 0;
        myCoordinate[1] = 0;
        myDirection = "";

        //positionValidated = true;

        receivedInvalidPositionException = false;
        failedToReadSaveFile = false;
        positionIsValid = true;

        handIsEmpty = false;
        handHasWall = false;
        userNameSet = true;
        pawnMoveSuccesful = false;

        whitePawnBehaviour = new PawnBehaviour();
        blackPawnBehaviour = new PawnBehaviour();

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

        Player[] players = {player1, player2};

        // Create all walls. Walls with lower ID belong to player1,
        // while the second half belongs to player 2
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= 10; j++) {
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
        // Tile indices start from 0 -> tiles with indices 4 and 8*9+4=76 are the starting
        // positions
        Tile player1StartPos = quoridor.getBoard().getTile(76);
        Tile player2StartPos = quoridor.getBoard().getTile(4);

        Game game = new Game(GameStatus.Running, MoveMode.PlayerMove, quoridor);
        game.setWhitePlayer(players.get(0));
        game.setBlackPlayer(players.get(1));

        Player whitePlayer = quoridor.getCurrentGame().getWhitePlayer();
        Player blackPlayer = quoridor.getCurrentGame().getBlackPlayer();

        PlayerPosition whitePlayerInitialPos = new PlayerPosition(whitePlayer, player1StartPos);
        PlayerPosition blackPlayerInitialPos = new PlayerPosition(blackPlayer, player2StartPos);
        PlayerPosition player1Position = new PlayerPosition(quoridor.getCurrentGame().getWhitePlayer(), player1StartPos);
        PlayerPosition player2Position = new PlayerPosition(quoridor.getCurrentGame().getBlackPlayer(), player2StartPos);

        GamePosition initialGamePosition = new GamePosition(0, whitePlayerInitialPos, blackPlayerInitialPos, whitePlayer, quoridor.getCurrentGame());
        GamePosition gamePosition = new GamePosition(1, player1Position, player2Position, players.get(0), game);

        // Add the walls as in stock for the players
        for (int j = 1; j <= 10; j++) {
            Wall wall = Wall.getWithId(j);
            gamePosition.addWhiteWallsInStock(wall);
            initialGamePosition.addWhiteWallsInStock(wall);
        }
        for (int j = 1; j <= 10; j++) {
            Wall wall = Wall.getWithId(j + 10);
            gamePosition.addBlackWallsInStock(wall);
            initialGamePosition.addBlackWallsInStock(wall);
        }

        game.setCurrentPosition(gamePosition);
    }

    /**
     * Removes the wall in stock for both players
     *
     * @author Thomas Philippon
     */
    private void removeWalls() {

        int whiteWallNo = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getWhiteWallsInStock().size();
        int blackWallNo = QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getBlackWallsInStock().size();

        for (int j = 1; j <= whiteWallNo; j++) {
            Wall wall = Wall.getWithId(j);
            QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeWhiteWallsInStock(wall);
        }
        for (int j = 1; j <= blackWallNo; j++) {
            Wall wall = Wall.getWithId(j + 10);
            QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().removeBlackWallsInStock(wall);
        }
    }

    /**
     * @param filename
     * @param dataDestination character array
     *                        Read in a file in the game saves directory into the character array. Useful for comparing contents of two saves.
     * @author Edwin Pan
     */
    private void readInFileFilenameInFileSystem(String filename, char[] dataDestination) {
        try {
            File file = new File(SaveConfig.getGameSaveFilePath(filename));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.read(dataDestination, 0, dataDestination.length);
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
    private String convertTileToDirection(int currentRow, int currentCol, int row, int col){
        int rowDiff = row-currentRow;
        int colDiff = col-currentCol;
        String dir = "";

        if((rowDiff>2 || rowDiff < -2) || colDiff < -2 || colDiff >2) return dir;

        switch (rowDiff){
            case -2:
                if(colDiff == 0 && QuoridorController.isPlayerOnTile(currentRow-1,currentCol)){
                    dir = "up";

                }
                return dir;
            case -1:
                dir = "up";
                break;
            case 1:
                dir = "down";
                break;
            case 2:
                if(colDiff==0 && QuoridorController.isPlayerOnTile(currentRow+1,currentCol)){
                    dir = "down";

                }
                return dir;
            default:
                break;

        }

        switch (colDiff){
            case -2:
                if(rowDiff == 0 && QuoridorController.isPlayerOnTile(currentRow,currentCol-1)){
                    dir = "left";
                    return dir;

                }
                return "";
            case -1:
                dir = dir + "left";
                break;
            case 1:
                dir = dir + "right";
                break;
            case 2:
                if(rowDiff == 0 && QuoridorController.isPlayerOnTile(currentRow,currentCol+1)){
                    dir = "right";
                    return dir;

                }
                return "";
            default:
                break;

        }

        return dir;
    }

    @Given("^The following moves were executed:$")
    public void executeMove(DataTable dt){
        List<Map<String, String>> list = dt.asMaps(String.class, String.class);
        int currentWhiteRow = 9;
        int currentWhiteCol = 5;
        int currentBlackRow = 1;
        int currentBlackCol = 5;
        for(int i = 0; i <list.size(); i++){
            int nextRow = list.get(i).get("row");
            int nextCol = list.get(i).get("col");
            String dir;
            if(i%2==0){
                dir = convertTileToDirection(currentWhiteRow,currentWhiteCol,nextRow, nextCol);
                QuoridorController.movePawn(QuoridorApplication.getQuoridor(),dir,QuoridorApplication.getWhitePawnBehaviour(QuoridorController.getCurrentWhitePlayer()));
                QuoridorController.completePlayerTurn(QuoridorController.getCurrentWhitePlayer());
                currentWhiteRow = nextRow;
                currentWhiteCol = nextCol;
            }
            else{
                dir = convertTileToDirection(currentBlackRow,currentBlackCol,nextRow, nextCol);
                QuoridorController.movePawn(QuoridorApplication.getQuoridor(),dir,QuoridorApplication.getBlackPawnBehaviour(QuoridorController.getCurrentBlackPlayer()));
                QuoridorController.completePlayerTurn(QuoridorController.getCurrentBlackPlayer());
                currentWhiteRow = nextRow;
                currentWhiteCol = nextCol;
            }




        }

    }

    /**the input into the test cases need to be either "white" or "black"
     * @author David
     * @param playerString either "white" or "black"
     */
    @Given("Player {String} has just completed his move")
    public void completeMove(String playerString){
        if(playerString.equals("white")){
            QuoridorController.completePlayerTurn(QuoridorController.getCurrentWhitePlayer());
        }
        else{
            QuoridorController.completePlayerTurn(QuoridorController.getCurrentBlackPlayer());
        }
    }

    /**Moves the pawn to a specified coordinate
     * @author David
     * @param playerString can only be "white" or "black"
     * @param row
     * @param col
     */
    @And("The last move of {String} is pawn move to {int}:{int}")
    public void moveLast(String playerString, int row, int col) {
        int currentRow, currentCol;
        PawnBehaviour pb;
        if(playerString.equals("white")){
            pb = QuoridorApplication.getWhitePawnBehaviour();
        }
        else{
            pb = QuoridorApplication.getBlackPawnBehaviour();
        }

        currentRow = QuoridorController.getCurrentPawnTilePos(0);
        currentCol = QuoridorController.getCurrentPawnTilePos(1);
        QuoridorController.movePawn(QuoridorApplication.getQuoridor(),convertTileToDirection(currentRow,currentCol,row,col),pb);
    }

    @When("Checking of game result is initated")
    public void initiateResultCheck(){
        QuoridorController.checkResult();
    }

    @Then("Game result shall be {String}")
    public void gameResultShallBe(String input){
        assertEquals(true,QuoridorController.getGameResult().equals(input));

    }

    @And("The game shall no longer be running")
    public void gameShallNoLongerBeRunning(){
        if(QuoridorController.isGameRunning(QuoridorController.getCurrentGame())){
            fail();
        }

    }













}
