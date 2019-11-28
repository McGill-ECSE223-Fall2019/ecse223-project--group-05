//THE IMPORT STATEMENT BELOW IS REQUIRED IN THE .fxml FILE
//IT GETS DELETED WHEN YOU REGENERATE THE .fxml FILE WITH SCENE BUILDER
//YOU MUST ADD IT IN MANUALLY WHEN THAT HAPPENS
//<?import java.util.ArrayList?>
//<?import java.util.ArrayList?>


package ca.mcgill.ecse223.quoridor.view;


import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.controller.*;
import ca.mcgill.ecse223.quoridor.enumerations.SavePriority;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.exceptions.InvalidPositionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Time;
import java.util.TimerTask;

import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorRuntimeModelPersistence;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.application.Platform;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ViewInterface {


    public void displayFinalResults(Game.GameStatus finalResults) {
    	//display the final results of the game.
		//ensure that the game ends once the message is closed.
		//Options: 	1. Go back to main menu
		//			2. Review game
		CurrentPage = getCurrentPage();

		//if page is disabled, then game has already been resigned
		if (CurrentPageIsDisabled()) {
			return;
		}


		//disable the current page, but do not make it invisible or replace it.
		CurrentPage.setDisable(true);

		//display the endOfGame page, and bring it to front to allow for user interaction.
		CurrentPage = getPage(Page.RESULTS_PAGE);
		getPage(Page.RESULTS_PAGE).setDisable(false);
		getPage(Page.RESULTS_PAGE).setVisible(true);
		getPage(Page.RESULTS_PAGE).toFront();
        Top_left_buttons.setDisable(true);

        if (finalResults == Game.GameStatus.WhiteWon)
            lbl_result.setText("White Wins!");
        else
            lbl_result.setText("Black Wins!");
        int num = (int)Math.floor(Math.random() * 9);
        img_result.setImage(new Image("textures/reportFinalResult/img" + num + ".gif"));

    }

    public void fromResultsPageToMainMenu(){
        CurrentPage = getPage(Page.RESULTS_PAGE);
        CurrentPage.setDisable(true);
        CurrentPage.setVisible(false);
        CurrentPage = getCurrentPage();
        CurrentPage.setDisable(false);
        Top_left_buttons.setDisable(false);
        CurrentPage.toFront();
        Goto_Main_Page();
    }

    public void resignGame(){
    	//TODO resign game
        QuoridorController.initiateToResign();
        QuoridorController.displayFinalResults();
	}

    //these are the pages that the user can travel to/interact with
	private enum Page {
		TOP_BUTTONS,
		MAIN_PAGE,
		NEW_GAME_PAGE,
		RULES_PAGE,
		LOAD_GAME_PAGE,
		SELECT_HOST_PAGE,
		GAME_SESSION_PAGE,
		CHOOSE_OPPONENT_PAGE,
		RESULTS_PAGE;
	};

	private enum TileImage {
	    TILE_STANDARD,
        TILE_GRAY,
        TILE_TARGET_WHITE,
        TILE_TARGET_BLACK,
        TILE_TARGET_WHITE_CENTER,
        TILE_TARGET_BLACK_CENTER,
        WHITE_PAWN,
        WHITE_PAWN_SELECTED,
        BLACK_PAWN,
        BLACK_PAWN_SELECTED;
    };
	//this list is initialized in the fxml file and is populated with
	//GUI pages that the user can travel to/interact with.
//////ORDER MATTERS. IF YOU ADD PAGES IN THE FXML PAGE, MAKE SURE THAT	////////
//////YOU UPDATE THE ENUM AND THE getPage(Page Cur) FUNCTION			////////
	@FXML private List<AnchorPane> pageList;
	//generalize events by storing current page as a page/state pair
	private Page currentState = Page.MAIN_PAGE;
	AnchorPane CurrentPage = null;

	@FXML private ComboBox whiteExistingName;
	@FXML private ComboBox blackExistingName;
	@FXML private TextField whiteNewName;
	@FXML private TextField blackNewName;
	@FXML private TextField whiteTimerField;
	@FXML private TextField blackTimerField;
	@FXML private Label whiteUsernameExistsLabel;
	@FXML private Label blackUsernameExistsLabel;
	@FXML private Label invalidWallPlacement;


     //Load Game Page
	@FXML private Label lbl_directory;
    @FXML ListView loadedGameList;
    private int counter = 0; //used for debugging of listView

	//Game Session Page
	@FXML private GridPane Game_Board;
	@FXML private Rectangle whiteWall1, whiteWall2, whiteWall3, whiteWall4, whiteWall5, whiteWall6, whiteWall7, whiteWall8, whiteWall9, whiteWall10;
	@FXML private Rectangle blackWall1, blackWall2, blackWall3, blackWall4, blackWall5, blackWall6, blackWall7, blackWall8, blackWall9, blackWall10;
	@FXML private Button btn_dropWall;
	@FXML private HBox blackStock, whiteStock;
	@FXML private Label gameSessionNotificationLabel;
	@FXML private Label whiteTimer;
	@FXML private Label blackTimer;
	@FXML private Label lbl_black_awaitingMove, lbl_white_awaitingMove;
	@FXML private Label whitePlayerName;
	@FXML private Label blackPlayerName;
	@FXML private Button btn_saveGame;
	@FXML private Button btn_ResignGame;
	private boolean validWallGrab = false; //boolean set to true when a used grabs one of his walls
	private boolean whiteTimeIsUp = false;
	private boolean blackTimeISUp = false;
    private ImageView[] gameBTiles;


    //Results page variables
    @FXML private Label lbl_result;
    @FXML private ImageView img_result;
    @FXML private AnchorPane Top_left_buttons; //buttons that will be disabled when results are displayed.

	//tutorial variables
    @FXML private StackPane mama_pane;
    private boolean isInTutorial;


	//movePawn varaibles
    ImageView whitePlayerTile, blackPlayerTile, initial_whitePlayerTile, initial_blackPlayerTile;
	//Grab and Drag wall variables
	double wallXPosition, wallYPosition;

	public static boolean isIllegalNotificationDisplayed = true;
	private static final double HORIZONTALSTEP = 35;
	private static final double VERTICALSTEP = 35;
    private static final double WALL_WIDTH = 5;
	private static Rectangle wallSelected;
	private static Quoridor quoridor;
	private Player whitePlayer, blackPlayer, playerToMove;
	private Timer timer;
	private Timer RefreshTimer;

	//bool variable to prevent the user from grabbing a second wall during the same turn.
	private boolean wallGrabbed = false;

  //Rotate wall variables
	private Rectangle wallMoveCandidate;

	//For moving the window
    double x,y;

	/**
	 * @author Matthias Arabian
	 * @param color the color of the HBox to return
	 * @return the Hbox of <>color</> player
	 */
	private HBox getStockOf(String color){
    	if (color.toLowerCase().equals("black"))
    		return blackStock;
    	else
    		return whiteStock;
	}

    public void GrabWall(MouseEvent mouseEvent){
	    HBox source = (HBox)mouseEvent.getSource();
	    GrabWall_move(source);
    }
	/**
	 * @author Thomas Philippon
	 * Changes the GUI CurrentPage to the Choose Opponent Page.
	 *
	 * Matthias edited this for deliverable 4. Not called by the walls, but by the HBoxes storing the walls.
	 * Allows player to put the wall back into their stock if they would like.
     * Matthias edited this for deliverable 5. Can be called programatically
	 */
	public void GrabWall_move(HBox stockClicked) {

		playerToMove = QuoridorController.getPlayerOfCurrentTurn();

        //get wall stock based on current player
        HBox source;
		if (stockClicked == null) //event triggered programatically
        {
            if (playerToMove.equals(whitePlayer)) {source = whiteStock;}
            else { source = blackStock; }
        }
		else{
		    source = stockClicked;
        }


		String color = QuoridorController.getColorOfPlayerToMove(QuoridorApplication.getQuoridor());

		//if no more walls in stock
		if (source.getChildren().size() <= 0 && source.equals(getStockOf(color)))
		{
			if (wallMoveCandidate == null) //if the player hasn't grabbed a wall this turn, don't do anything
				gameSessionNotificationLabel.setText("You have no more walls in stock...");
			else
			{
				//return the wall to the stock
                QuoridorController.cancelWallGrabbed(quoridor);
				returnWallToStock(wallMoveCandidate, getStockOf(color));
                validWallGrab = false;
                wallSelected = null;
                wallMoveCandidate = null;
                wallGrabbed = false; //added by Thomas

                //turn btn_dropWall to disabled and invisible
                btn_dropWall.setDisable(true);
                btn_dropWall.setVisible(false);
			}
			return;
		}


		Rectangle wall = (Rectangle) source.getChildren().get(0);
		String wallID = wall.getId();
		System.err.print(1);

		//if the player selects one of the other player's wall
		if(!wallID.contains(color)){
			displayIllegalNotification("This wall does not belong to you");
		}

		// if the current player to move selects a second wall during the same turn
		if(wallID.contains(color) && wallGrabbed == true){
		    if (wallSelected == null) //only send an error if they did not grab a wall this turn
                displayIllegalNotification("You moved your pawn, so you cannot grab a wall this turn!");
            else{
				//otherwise, return the grabbed wall to the stock
                QuoridorController.cancelWallGrabbed(quoridor);
            	returnWallToStock(wallMoveCandidate, getStockOf(color));
                validWallGrab = false;
                wallSelected = null;
                wallMoveCandidate = null;
                wallGrabbed = false; //added by Thomas

                //turn btn_dropWall to disabled and invisible
                btn_dropWall.setDisable(true);
                btn_dropWall.setVisible(false);
            	return;
			}
		}
		//check if the player to move is grabbing his walls and not the opponent's
		if(wallID.contains(color) && wallGrabbed == false) {
			wallGrabbed = true; //prevent the user from selecting a second wall during the same turn.
			boolean grabWallResult;
			try {
				grabWallResult = QuoridorController.grabWall(QuoridorApplication.getQuoridor());
			} catch (Exception e) {
				throw new java.lang.UnsupportedOperationException("Cannot retrieve the number of walls in stock");
			}

			if (grabWallResult == false) {
				gameSessionNotificationLabel.setText("You have no more walls in stock...");
		}
			else{
                //turn btn_dropWall to enabled and visible
                btn_dropWall.setDisable(false);
                btn_dropWall.setVisible(true);
                btn_dropWall.setFocusTraversable(true); //fix bug where the button stole the focus and stopped the "press enter to drop wall" functionality
				validWallGrab = true;
				wallMoveCandidate = wall;
				wallSelected = wall;
				System.err.print(2);
				double prevX = wallSelected.getX();
				double prevY = wallSelected.getY();
				Node p = wallSelected.getParent();
				System.out.println(p.getClass().getName().contains("HBox"));
				if (p.getClass().getName().contains("HBox")) {
                    HBox parent = (HBox) wallSelected.getParent();

                    wallSelected.setX(0);
                    wallSelected.setY(0);
                    wallSelected.setTranslateX(0);
                    wallSelected.setTranslateY(0);
                    parent.getChildren().remove(wallSelected);
                    getCurrentPage().getChildren().add(wallSelected);

                }
                System.err.print(3);
				wallSelected.setLayoutY(0);
				wallSelected.setLayoutX(0);
				wallSelected.setX(189);
                wallSelected.setY(23);

                System.out.println(wallSelected.getX());

                //Validate Position for the tile (1,1)
				if(QuoridorController.validatePosition(1,1, "Vertical")){
					wallSelected.setStroke(Color.GREEN);
				}
				else{
					wallSelected.setStroke(Color.RED);
				}

			}
		}
	}

	/**
	 * @author David Deng
	 * This method is called when the user moves the wall using the keyboard.
	 * It is suppsoed to change the rectangle to red if the position is invalid.
	 * It had worked with an old version of GUI, but is not working well with a newer version.
	 * We believe that having more time, it should work more smoothly.
	 */

	@FXML
	public void MoveWall(KeyEvent keyEvent) {

	    //return if the game isn't currently running
        if (QuoridorApplication.getQuoridor().getCurrentGame() == null)
            return;
        if (!QuoridorController.isGameRunning(QuoridorApplication.getQuoridor().getCurrentGame()))
           return;

        //ensure that a wall is selected
		boolean isValid = true;
		try {
			if(wallSelected==null){
                if (keyEvent.getCode()==KeyCode.Q) //if press q -> grab wall from stock
                    GrabWall_move(null);
				return; //don't do anything, since you cannot move a nonexistant wall
			}
            if (keyEvent.getCode()==KeyCode.Q) { //if press q -> put wall back in stock
                GrabWall_move(null);
                return;
            }
			else if(keyEvent.getCode().equals(KeyCode.ENTER)|| keyEvent.getCharacter().getBytes()[0] == '\n' || keyEvent.getCharacter().getBytes()[0] == '\r') {
				System.out.println("dropping wall detected");
				switchPlayer();
				return;
			}
			else if(keyEvent.getCode()==KeyCode.W) {
				isValid = QuoridorController.moveWall("up");
				wallSelected.setTranslateY(wallSelected.getTranslateY()-VERTICALSTEP);//translates the rectangle by a tilewidth
				System.out.println("detected");
			}
			else if(keyEvent.getCode()==KeyCode.S) {
				isValid = QuoridorController.moveWall("down");
				wallSelected.setTranslateY(wallSelected.getTranslateY()+VERTICALSTEP);
			}
			else if(keyEvent.getCode()==KeyCode.A) {
				isValid = QuoridorController.moveWall("left");
				wallSelected.setTranslateX(wallSelected.getTranslateX()-HORIZONTALSTEP);
			}
			else if(keyEvent.getCode()==KeyCode.D) {
				isValid = QuoridorController.moveWall("right");
				wallSelected.setTranslateX(wallSelected.getTranslateX()+HORIZONTALSTEP);
			} else {
			    return;
            }
			if(!isValid) {
				wallSelected.setStroke(Color.RED);
			}
			else {
				wallSelected.setStroke(Color.GREEN);
			}

			System.out.println(wallSelected.getTranslateX());
			System.out.println(wallSelected.getX());
			System.out.println(wallSelected.getTranslateY());
			System.out.println(wallSelected.getY());
		}
		catch(Throwable e) {
			displayIllegalNotification(e.getMessage());
		}
	}

	/**
	 * @author Alex Masciotra
	 *This method is executed when the user releases the wall
	 */
	public void whiteDropWall(MouseEvent mouseEvent) {
		Boolean dropSuccessful;

		invalidWallPlacement.setText("");
		System.out.println("yeyye");

		try {
			dropSuccessful = QuoridorController.releaseWall(quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to drop Wall");
		}

		if (!dropSuccessful){
		//	invalidWallPlacement.setText("Invalid Wall Placement");
			displayIllegalNotification("Invalid wall placement");

		}
		else{
			validWallGrab=false;
			wallSelected = null;
			wallMoveCandidate=null;
		}
	}

	/**
	 * @author Alex Masciotra
	 *This method is executed when the user releases the wall
	 */

	public void blackDropWall(MouseEvent mouseEvent) {

		Boolean dropSuccessful;

		invalidWallPlacement.setText("");

		try {
			dropSuccessful = QuoridorController.releaseWall(quoridor);

		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to drop Wall");

		}

		if (!dropSuccessful){
			//invalidWallPlacement.setText("Invalid Wall Placement");
			displayIllegalNotification("Invalid wall placement");
		}
		else{
			validWallGrab=false;
			wallSelected = null;
			wallMoveCandidate=null;
		}
	}

	/**
	 * @author Alex Masciotra
	 *This method is executed when the user releases the wall
	 */
	public void DropWall(MouseEvent mouseEvent) {
	}


    /**
     * @author Matthias Arabian
     * opens a DialogWindow prompting the user to select a directory that contains game files.
     */
	public void addToLoadedGameList() {
	    //directory chooser dialog window
		Stage stage = new Stage();
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Select directory with saved games");
		fileChooser.setInitialDirectory( new File(SaveConfig.getGameSavesFolder()) );	//Here's a little something from my SaveConfig to spice things up for you, Matthias
		File file = fileChooser.showDialog(stage);
		stage.close();

		//check that the directory is valid. If not, update the GUI accordingly
		if (file != null) {
			lbl_directory.setText(file.toString()); //display the selected directory
			detectGameFiles(file); //detect game files in directory
		}
		else {
			lbl_directory.setText("Directory could not be opened");
		}
	}


    /**
     * @author Matthias Arabian
     * @param directory the directory to crawl through.
     * Checks every file in <>directory</> and adds it to the listView if is a gameFile.
     */
	private void detectGameFiles(File directory) {

	    //get list of files whose ending corresponds with the endings of game files (.dat)
		File[] gameFiles = directory.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	return name.endsWith("mov");
		    }
		});
//		System.out.println(gameFiles); //debugging flag

        //clears the listView and repopulates it with the new files list
		loadedGameList.getItems().clear();
		for (File f : gameFiles)
			loadedGameList.getItems().add(f.toString());
	}

    /**
     * @author Matthias Arabian
     * This function is used as a debugging tool to detect javaFX listView events visually as they occur.
     */
	public void gotSelected() {
		System.out.println(loadedGameList.getSelectionModel().getSelectedItem());
	}

	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Create New Game Page.
	 */
	public void Goto_New_Game_Page() {
		Goto_Page(Page.NEW_GAME_PAGE);
	}


	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Main Page.
     * Reset the game, if it was initialized.
	 */
	public void Goto_Main_Page() {
	    Game g = QuoridorApplication.getQuoridor().getCurrentGame();
	    if (g != null && QuoridorController.isGameRunning(g))
	        return;

	    //when going to main page, not in tutorial anymore
        isInTutorial = false;
		clearGUI_game_session_page();       //reset the GUI section of the QuoridorApplication
        clearGUI_new_game_page();
        QuoridorController.clearGame();     //reset the model section of the QuoridorApplication
        Goto_Page(Page.MAIN_PAGE);
	}


    /**
     * @author Matthias Arabian
     * resets the state of the NewGamePage to allow users to interact with it as if
     * it were the first time they accessed that page
     */
    private void clearGUI_new_game_page() {
        whiteNewName.setText("");
        blackNewName.setText("");
        whiteUsernameExistsLabel.setText("");
        whiteUsernameExistsLabel.setText("");
    }
	/**
	 * @author Matthias Arabian
	 * resets the state of the GameSessionPage to allow users to start a new game after exiting a previous one
	 */
	private void clearGUI_game_session_page() {
		//set the current player to be WhitePlayer
		resetGUI_playerTurn();
		resetGUI_tutorial();

		//reset the player usernames
		whiteExistingName.getSelectionModel().clearSelection();
		whiteExistingName.valueProperty().setValue(null);
		blackExistingName.getSelectionModel().clearSelection();
		blackExistingName.valueProperty().setValue(null);
		whiteNewName.setText("");
        blackNewName.setText("");

		//reset the GUI wall related global variables
		invalidWallPlacement.setText("");
		gameSessionNotificationLabel.setText("");
		validWallGrab = false;
		wallSelected = null;
		if (wallMoveCandidate != null)
			wallMoveCandidate.setStroke(Color.BLACK);
		wallMoveCandidate = null;
		wallGrabbed = false; //added by Thomas

		//put all blackWalls back into their stock positions
		Rectangle[] blackWalls = {blackWall1, blackWall2, blackWall3, blackWall4, blackWall5, blackWall6
				,blackWall7,blackWall8, blackWall9, blackWall10};
		for (Rectangle r : blackWalls){
			returnWallToStock(r, blackStock);
		}

		//put all whiteWalls back into their stock positions
		Rectangle[] whiteWalls = {whiteWall1, whiteWall2, whiteWall3, whiteWall4, whiteWall5, whiteWall6
				, whiteWall7, whiteWall8, whiteWall9, whiteWall10};
		for (Rectangle r : whiteWalls){
			returnWallToStock(r, whiteStock);
		}
		//set wall stock opacity back to default values (it will be white player's turn)
		blackStock.setOpacity(0.5);
		whiteStock.setOpacity(1);

        //turn btn_dropWall to disabled and invisible
        btn_dropWall.setDisable(true);
        btn_dropWall.setVisible(false);


		//move the pawns back to their initial positions
        setTileImage(whitePlayerTile, emptyTileShouldBe(whitePlayerTile));
        setTileImage(blackPlayerTile, emptyTileShouldBe(blackPlayerTile));
        whitePlayerTile = initial_whitePlayerTile;
		blackPlayerTile = initial_blackPlayerTile;
        setTileImage(whitePlayerTile, TileImage.WHITE_PAWN_SELECTED);
        setTileImage(blackPlayerTile, TileImage.BLACK_PAWN);

	}

	/**
	 * return the wall r to the stock b
	 * @author Matthias Arabian
	 * @param r wall to return to stock
	 * @param b stock that'll be receiving the wall
	 */
	private void returnWallToStock(Rectangle r, HBox b){
		Node p = r.getParent();
		if (!p.getClass().getName().contains("HBox")) {
			AnchorPane parent = (AnchorPane) r.getParent();
			parent.getChildren().remove(r);
			b.getChildren().add(r);
			r.setTranslateX(0);
			r.setTranslateY(0);
			QuoridorController.resetGUIWall(r);
		}
	}
	/**
	 * @author Daniel Wu
	 * Changes the GUI CurrentPage to the Choose Opponent Page.
	 */
	public void Goto_Choose_Opponent_Page() {

		Goto_Page(Page.CHOOSE_OPPONENT_PAGE);

		quoridor = QuoridorApplication.getQuoridor();

		try {
			QuoridorController.initializeGame(quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Cannot initialize the Game");
		}
}


	/**
	 * @author Thomas Philippon
	 * Changes the GUI CurrentPage to the Game Session Page.
	 */
	public void Goto_Game_Session_Page() {

		try {
			setThinkingTime(whiteTimerField.getText(), blackTimerField.getText());
			try {
                //game has started at this point
                quoridor.getCurrentGame().setGameStatus(Game.GameStatus.Running);
				QuoridorController.initializeBoard(QuoridorApplication.getQuoridor(), timer);
			} catch (Exception e) {
                //game has failed to be initialized
                quoridor.getCurrentGame().setGameStatus(Game.GameStatus.Initializing);
				throw new java.lang.UnsupportedOperationException("Cannot initialize the board");
			}

			//get both players
			whitePlayer = QuoridorController.getCurrentWhitePlayer();
			blackPlayer = QuoridorController.getCurrentBlackPlayer();

			whitePlayerName.setText(QuoridorController.getPlayerName(whitePlayer));
			blackPlayerName.setText(QuoridorController.getPlayerName(blackPlayer));

			/*
			 * Addition made by Edwin Pan for SaveGame button to show on the game_session_page
			 */
			btn_saveGame.setVisible(true);
			btn_ResignGame.setVisible(true);

			lbl_white_awaitingMove.setText("It is your Turn!");



			//This tasks runs on a separate thread. It is used to update the GUI every second
			RefreshTimer.schedule(new TimerTask() {
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							//Update white player's thinking time clock
							String whiteTime = QuoridorController.playerThinkingTime(whitePlayer).substring(3);
							String blackTime =  QuoridorController.playerThinkingTime(blackPlayer).substring(3);
							if(whiteTime.equals("00:00")){
								whiteTimeIsUp = true;
							}
							if(blackTime.equals("00:00")){
								blackTimeISUp = true;
							}
							if(whiteTimeIsUp==true){
								whiteTimer.setText("00:00");
							}
							else{
								whiteTimer.setText(whiteTime);
							}
							if(blackTimeISUp==true){
								blackTimer.setText("00:00");
							}
							else{
								blackTimer.setText(blackTime);
							}
						}
					});
				}
			}, 0, 1000);
            setPossibleMoveTiles();

			Goto_Page(Page.GAME_SESSION_PAGE);
//            quoridor.getCurrentGame().setGameStatus(Game.GameStatus.Running);
		}
		catch(Exception e) {
			displayIllegalNotification(e.getMessage());
		}
	}

	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Load Game Page.
	 */
	public void Goto_Load_Game_Page() {
		Goto_Page(Page.LOAD_GAME_PAGE);
	}

	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Select Host Page.
	 */
	public void Goto_Select_Host_Page() {
		Goto_Page(Page.SELECT_HOST_PAGE);
	}

	/**
	 * @author Matthias Arabian
	 * @param p The page to go to
	 * Changes the GUI CurrentPage to the page defined by the parameter p
	 * This function is called by every other Goto_ functions
	 * and serves as a generalized template for page travel.
	 * Disables and turns the past page invisible.
	 */
	private void Goto_Page(Page p) {

		/*
		 * The following line has been added by Edwin Pan in order to have the SaveGame button on the ButtonBar
		 */
		if( p != Page.GAME_SESSION_PAGE) {
			btn_saveGame.setVisible(false);
			btn_ResignGame.setVisible(false);
		}

		CurrentPage = getCurrentPage();

//		Restrict page to page movement when the current page is disabled
//		(something else is happening atm)
		if (CurrentPageIsDisabled())
			return;

		CurrentPage.setDisable(true);
		CurrentPage.setVisible(false);

		currentState = p;
		CurrentPage = getCurrentPage();
		CurrentPage.setDisable(false);
		CurrentPage.setVisible(true);
		CurrentPage.toFront();
	}

	/**
	 * @author Matthias Arabian
	 * Displays the Rules page.
	 * Disables the CurrentPane, but does not replace it.
	 * Overlays the rules on top of the CurrentPage to allow user to return to what
	 * they were doing.
	 */
	public void openRulesPage() {
		CurrentPage = getCurrentPage();

		//if page is disabled, then the rules are already displayed. Therefore, the rules page should be closed.
		if (CurrentPageIsDisabled()) {
			closeRulesPage();
			return;
		}


		//disable the current page, but do not make it invisible or replace it.
		CurrentPage.setDisable(true);

		//display the Rules page, and bring it to front to allow for user interaction.
		CurrentPage = getPage(Page.RULES_PAGE);
		getPage(Page.RULES_PAGE).setDisable(false);
		getPage(Page.RULES_PAGE).setVisible(true);
		getPage(Page.RULES_PAGE).toFront();
		getPage(Page.RULES_PAGE).setDisable(false);
		getPage(Page.TOP_BUTTONS).toFront(); //those need to be on top of the rules to allow for clicking on Rules label to close rules
	}

	/**
	 * @author Matthias Arabian
	 * this function closes the Rules page and re-enables the CurrentPage.
	 */
	public void closeRulesPage() {
		CurrentPage = getPage(Page.RULES_PAGE);
		CurrentPage.setDisable(true);
		CurrentPage.setVisible(false);
		CurrentPage = getCurrentPage();
		CurrentPage.setDisable(false);
		CurrentPage.toFront();
	}

	/**
	 * @author Matthias Arabian
	 * @return whether CurrentPage is disabled
	 */
	private boolean CurrentPageIsDisabled() {
		return getPage(currentState).isDisable();
	}

	/**
	 * @author Matthias Arabian
	 * @return the current page the user is interacting with
	 */
	private AnchorPane getCurrentPage() {
		return getPage(currentState);
	}

	/**
	 * @author Matthias Arabian
	 * @param Cur: an enum type used to discriminate between GUI pages in a user-friendly manner.
	 * @return the page described by the variable Cur
	 *
	 * The order of the pages stored in the pageList variable depends on
	 * their order when the list was declared in the FXML file.
	 * You must ensure that the pages referenced in this function are the intended ones.
	 *
	 * This is a necessary evil that allows for greater generalization in event handlers.
	 */
	private AnchorPane getPage(Page Cur) {
		if (Cur == Page.TOP_BUTTONS)
			return pageList.get(0);
		if (Cur == Page.MAIN_PAGE)
			return pageList.get(1);
		if (Cur == Page.NEW_GAME_PAGE)
			return pageList.get(2);
		if (Cur == Page.RULES_PAGE)
			return pageList.get(3);
		if (Cur == Page.LOAD_GAME_PAGE)
			return pageList.get(4);
		if (Cur == Page.SELECT_HOST_PAGE)
			return pageList.get(5);
		if (Cur == Page.GAME_SESSION_PAGE)
			return pageList.get(6);
		if (Cur == Page.CHOOSE_OPPONENT_PAGE)
			return pageList.get(7);
		if (Cur == Page.RESULTS_PAGE)
			return pageList.get(8);
		else
			return null;
	}

    /**
     * @Author Matthias Arabian
     */
    EventHandler<MouseEvent> hoverEffect = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            if (!wallGrabbed) {
                ImageView img = (ImageView) e.getSource();
                if (img!= blackPlayerTile && img!= whitePlayerTile)
                    img.setOpacity(0.5);
            }
        }
    };
    private void adjustImg_canMoveTo(ImageView img){
        //Instantiating the ColorAdjust class
        ColorAdjust colorAdjust = new ColorAdjust();

        //Setting the contrast value
        //colorAdjust.setContrast(0.8);

//        //top 1
//        //Setting the hue value
//        colorAdjust.setHue(55/255.0);
//
//        //setting brightness
//        //colorAdjust.setBrightness(0.5);
//        //Setting the saturation value
//        colorAdjust.setSaturation(167/255.0);


//        //top 3
//        //Setting the hue value
//        colorAdjust.setHue(55/255.0);
//
//        //setting brightness
//        colorAdjust.setBrightness(0.5);
//        //Setting the saturation value
//        colorAdjust.setSaturation(167/255.0);

        //Setting the hue value
        colorAdjust.setHue(55/255.0);

        //setting brightness
        //colorAdjust.setBrightness(0.5);
        //Setting the saturation value
        colorAdjust.setSaturation(167/255.0);

        colorAdjust.setContrast(0.1);
        //Applying coloradjust effect to the ImageView node
        img.setEffect(colorAdjust);
    }

    /**
     *@Author Matthias Arabian
     */
    EventHandler<MouseEvent> cancelHoverEffect = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            ImageView img = (ImageView)e.getSource();
            img.setOpacity(1);
        }
    };
    /**
     * @Author Matthias Arabian
     */
    EventHandler<MouseEvent> hoverEffect_HBOX = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (!wallGrabbed) {
                        HBox img = (HBox) e.getSource();
                        img.setOpacity(0.5);
                    }
                }
            };

    /**
     *@Author Matthias Arabian
     */
    EventHandler<MouseEvent> cancelHoverEffect_HBOX = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {
            HBox img = (HBox)e.getSource();
            img.setOpacity(1);
        }
    };

    /**
     * @author Matthias Arabian
     * Event handler used to trigger pawn moves.
     */
    EventHandler<MouseEvent> tryToMovePawn = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent e) {

            if (wallGrabbed) //only allow the player to move if they haven't done anything this turn
                return;

            //get the tile object that was clicked, row and column
            ImageView img = (ImageView)e.getSource();
            String id = img.getId();
            int row, col;
            row = Integer.parseInt(id.substring(0,1));
            col = Integer.parseInt(id.substring(2));
            PawnBehaviour currentSM;
            if(QuoridorController.getColorOfPlayerToMove(QuoridorApplication.getQuoridor()).equals("black")){
            	currentSM = QuoridorApplication.getBlackPawnBehaviour(QuoridorController.getCurrentBlackPlayer());
			}
            else{
            	currentSM = QuoridorApplication.getWhitePawnBehaviour(QuoridorController.getCurrentWhitePlayer());
			}
            boolean success;
			try{success = QuoridorController.movePawn(quoridor,convertTileToDirection(row,col), currentSM);}
			catch(Throwable t){
				success = false;
			}
			if(!success) return;


            //update the GUI by moving the pawn to the new position.
            if (QuoridorController.getColorOfPlayerToMove(QuoridorApplication.getQuoridor()).equals("black")) {
                if (getTileImage(img) != TileImage.WHITE_PAWN) { //don't allow pawn to be moved on top of other pawn
                    setTileImage(blackPlayerTile, emptyTileShouldBe(blackPlayerTile));
                    setTileImage(img, TileImage.BLACK_PAWN);
                    blackPlayerTile = img;
                    wallGrabbed = true; //flag used to restrict player from grabbing a wall or moving again
                    switchPlayer();

                }
            } else {
                if (getTileImage(img) != TileImage.BLACK_PAWN) { //don't allow pawn to be moved on top of other pawn
                    setTileImage(whitePlayerTile, emptyTileShouldBe(whitePlayerTile));
                    setTileImage(img, TileImage.WHITE_PAWN);
                    whitePlayerTile = img;
                    wallGrabbed = true; //flag used to restrict player from grabbing a wall or moving again
                    switchPlayer();
                }
            }

            img.setOpacity(1); //hovering over a tile sets the opacity to 0.5, so this resets it to 1 to make the pawn move look better.
        }
    };

    /**
     * @author Matthias Arabian
     * Change the opacity of tiles that the current player can move to.
     */
    private void setPossibleMoveTiles(){
        for (int i = 0; i < 81; i++) //clear previous effects
            gameBTiles[i].setEffect(null);

        //array used to store moves
        MoveDirection[] dir = {
        null,               null,                       MoveDirection.North,        null,                       null,
        null,               MoveDirection.NorthWest,    MoveDirection.North,        MoveDirection.NorthEast,    null,
        MoveDirection.West, MoveDirection.West,         null,                       MoveDirection.East,         MoveDirection.East,
        null,               MoveDirection.SouthWest,    MoveDirection.South,        MoveDirection.SouthEast,    null,
        null,               null,                       MoveDirection.South,        null,                       null
        };
        //array used to decide if stepMove
        boolean[] canStep = {
                false, false,   false,  false, false,
                false, false,   true,   false, false,
                false, true,    true,   true, false,
                false, false,   true,   false, false,
                false, false,   false,  false, false
        };

        //get current player
        Player p = QuoridorController.getPlayerOfCurrentTurn();
        //get position of current player
        ImageView img;
        PawnBehaviour sm;
        if (p.equals(QuoridorController.getPlayerOfProvidedColorstring("black"))) {
            img = blackPlayerTile;
            sm = QuoridorApplication.getBlackPawnBehaviour(p);
        }
        else {
            img = whitePlayerTile;
            sm = QuoridorApplication.getWhitePawnBehaviour(p);
        }

        //get row and column of tile
        String id = img.getId();
        int row, col;
        row = Integer.parseInt(id.substring(0,1)) -1;
        col = Integer.parseInt(id.substring(2)) -1;

        MoveDirection tmpMove;
        for(int r = row-2; r <= row + 2;r++)
        {
            for(int c = col-2; c <= col + 2;c++)
            {
                //check that position is on board
                if (r >= 0 && r < 9 && c >= 0 && c < 9){
                    tmpMove = dir[(r-(row-2))*5 + (c-(col-2))];
                    if (r != row || c != col)
                    {
                        try {
                            if (canStep[(r - (row - 2)) * 5 + (c - (col - 2))]) {
                                if (sm.isLegalStep(tmpMove))
                                    adjustImg_canMoveTo(gameBTiles[r * 9 + c]);
                            } else {
                                if (sm.isLegalJump(tmpMove))
                                    adjustImg_canMoveTo(gameBTiles[r * 9 + c]);
                            }
                        }
                        catch (Exception e){};
                    }

                }
            }
        }

    }
    /**
     * @author Matthias Arabian
     * @param img the tile whose image will be changed
     * @return a reference to the image the tile should be updated with
     * This method uses a tile's coordinates to determine what its image should be when there are not pawns on it.
     * (e.g tiles in the middle of the field would be the standard image. Tiles at the to and bottom have crosses to flag that that's the player's target area)
     */
    private TileImage emptyTileShouldBe(ImageView img){
        //get row and column of tile
        String id = img.getId();
        int row, col;
        row = Integer.parseInt(id.substring(0,1));
        col = Integer.parseInt(id.substring(2));

        //that tile's image will be replaced based on its coordinates because some positions have special tiles associated with them
        if (row == 1){
            if (col == 5)
                return TileImage.TILE_TARGET_WHITE_CENTER;
            else
                return TileImage.TILE_TARGET_WHITE;
        }
        if (row == 9){
            if (col == 5)
                return TileImage.TILE_TARGET_BLACK_CENTER;
            else
                return TileImage.TILE_TARGET_BLACK;
        }
        return TileImage.TILE_STANDARD;
    }

	/**
	 * @author Matthias Arabian
	 * initializes the FXML components. this code runs once the application is launched but before the GUI is displayed.
	 */
	public void initialize() {
		resetGUItoMainPage(); //setGUI pages to right visibility


		//Populate game board with colorful tiles
        gameBTiles = new ImageView[81];
		for (int row = 0; row < 17; row+=2) {
			for (int col = 0; col < 17; col+=2) {
			    //store the images inside panes to have a background.
			    Pane p = new Pane();
			    p.toFront();
                p.setStyle("-fx-background-color: #3d6b99");

                //actual tile is an ImageView b/c we want to be able to change its image
				ImageView tmp = new ImageView();
                setTileImage(tmp, TileImage.TILE_STANDARD);
				p.getChildren().add(tmp);
				Bounds b = Game_Board.getCellBounds(row,col);

				//initialize the tile with a set width, height.
				tmp.setFitWidth(HORIZONTALSTEP - WALL_WIDTH);
				tmp.setFitHeight(VERTICALSTEP - WALL_WIDTH);
                tmp.setId("" + (row/2+1) + "," + (col/2+1)); //store the tile's corrdinates as its ID for later reference
                Game_Board.add(p , col, row); //add tile to game board
                gameBTiles[(row/2)*9 + (col/2)] = tmp; //add tile to accessible game board

				tmp.addEventFilter(MouseEvent.MOUSE_ENTERED, hoverEffect); //event handler used for hover animation
                tmp.addEventFilter(MouseEvent.MOUSE_EXITED, cancelHoverEffect); //event handler used to end hover animation
                tmp.addEventFilter(MouseEvent.MOUSE_CLICKED, tryToMovePawn); //event handler used for jump/move pawn

                //initialize the tiles on the top and bottom to their special image
                if (row == 0){
                    setTileImage(tmp, TileImage.TILE_TARGET_WHITE);
                }
                else if (row/2 == 8) {
                    setTileImage(tmp, TileImage.TILE_TARGET_BLACK);
                }

                //initialize the GUI pawn positions to be at the top/bottom row and center column of the field
                if (col/2 == 4)
                {
                    if (row/2 == 0) //black pawn
                    {
                        blackPlayerTile = tmp;
                        initial_blackPlayerTile = tmp;
                        setTileImage(blackPlayerTile, TileImage.BLACK_PAWN);
                    }
                    else if (row/2 == 8) //white pawn
                    {
                        whitePlayerTile = tmp;
                        initial_whitePlayerTile = tmp;
                        setTileImage(whitePlayerTile, TileImage.WHITE_PAWN_SELECTED);
                    }
                }

				//change opacity of wall stocks
				whiteStock.setOpacity(1);
				blackStock.setOpacity(0.5);

				//turn btn_dropWall to disabled and invisible
                btn_dropWall.setDisable(true);
                btn_dropWall.setVisible(false);
        }
		}


		//Initialize the timers
		timer = new Timer();
		RefreshTimer = new Timer();

		//Hides the save game button
		btn_saveGame.setVisible(false);
		btn_ResignGame.setVisible(false);

		quoridor = QuoridorApplication.getQuoridor();
	}

    /**
     * @author Matthias Arabian
     * @param tile tile whose image to change
     * @param newImage the identifier of the new image
     *
     * Change the image of the tile object to the one given by the newImage parameter
     */
    private void setTileImage(ImageView tile, TileImage newImage){
	    switch (newImage){
            case TILE_GRAY:
                tile.setImage(new Image("textures/tile_monochrome.png"));
                break;
            case TILE_STANDARD:
                tile.setImage(new Image("textures/tile_main.png"));
                break;
            case TILE_TARGET_BLACK:
                tile.setImage(new Image("textures/tile_blackTarget.png"));
                break;
            case TILE_TARGET_WHITE:
                tile.setImage(new Image("textures/tile_whiteTarget.png"));
                break;
            case TILE_TARGET_BLACK_CENTER:
                tile.setImage(new Image("textures/tile_blackTarget_whiteSpawn.png"));
                break;
            case TILE_TARGET_WHITE_CENTER:
                tile.setImage(new Image("textures/tile_whiteTarget_blackSpawn.png"));
                break;
            case BLACK_PAWN:
                tile.setImage(new Image("textures/pawn_black.png"));
                break;
            case WHITE_PAWN:
                tile.setImage(new Image("textures/pawn_white.png"));
                break;
            case BLACK_PAWN_SELECTED:
                tile.setImage(new Image("textures/pawn_black_glowing.png"));
                break;
            case WHITE_PAWN_SELECTED:
                tile.setImage(new Image("textures/pawn_white_glowing.png"));
                break;
        }
    }

    /**
     * @author Matthias Arabian
     * @param tile tile whose image we want to get
     * @return TileImage enum representing the parameter tile 's image
     * Method to get the tile's image in a format that is easily assertable
     */
    private TileImage getTileImage(ImageView tile){
	    String url = tile.getImage().getUrl();
	    System.out.println(url);
	    if (url.contains("tile_monochrome.png"))
	        return TileImage.TILE_GRAY;
        if (url.contains("tile_main.png"))
            return TileImage.TILE_STANDARD;

        if (url.contains("tile_blackTarget.png"))
            return TileImage.TILE_TARGET_BLACK;
        if (url.contains("tile_whiteTarget.png"))
            return TileImage.TILE_TARGET_WHITE;
        if (url.contains("tile_blackTarget_whiteSpawn.png"))
            return TileImage.TILE_TARGET_BLACK_CENTER;
        if (url.contains("tile_whiteTarget_blackSpawn.png"))
            return TileImage.TILE_TARGET_WHITE_CENTER;

        if (url.contains("pawn_black.png"))
            return TileImage.BLACK_PAWN;
        if (url.contains("pawn_black_glowing.png"))
            return TileImage.BLACK_PAWN_SELECTED;
        if (url.contains("pawn_white.png"))
            return TileImage.WHITE_PAWN;
        if (url.contains("pawn_white_glowing.png"))
            return TileImage.WHITE_PAWN_SELECTED;
        return null;
    }

	/**
	 * Method to display ExistingUserNames
	 * @author Alex Masciotra
	 * @param mouseEvent when arrow is pressed
	 */
    public void displayExistingUserNames(MouseEvent mouseEvent) {

		//when the arrow is pressed

		List<String> existingUserNames;
		try {
			existingUserNames = QuoridorController.provideExistingUserNames(quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to display Existing UserNames");
		}

		//this comboBox is for whiteUserChooseFromExistingArrow
		whiteExistingName.setItems(FXCollections.observableList(existingUserNames));

		//this comboBox is for blackUserChooseFromExistingArrow
		blackExistingName.setItems(FXCollections.observableList(existingUserNames));

	}

	/**
	 * Method to select existing username for white player from dropdownList
	 * @author Alex Masciotra
	 * @param actionEvent name selected
	 */
	public void whitePlayerSelectsExistingUserName(ActionEvent actionEvent) {

		String userNameToSet = "";
		try {
			QuoridorController.assignPlayerColorToUserName("white", quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to assign next Player");
		}

		userNameToSet = ((String)whiteExistingName.getSelectionModel().getSelectedItem());

		try {
			QuoridorController.selectExistingUserName(userNameToSet, quoridor);
			whiteUsernameExistsLabel.setText("");
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to select Existing UserName");
		}
	}

	/**
	 * Method to select existing username for black player from dropDownList
	 * @author Alex Masciotra
	 * @param actionEvent name selected
	 */
	public void blackPlayerSelectsExistingUserName(ActionEvent actionEvent) {

		try {
			QuoridorController.assignPlayerColorToUserName("black", quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to assign next Player");
		}
		String userNameToSet = ((String)blackExistingName.getSelectionModel().getSelectedItem());

		try {
			QuoridorController.selectExistingUserName(userNameToSet, quoridor);
			blackUsernameExistsLabel.setText("");
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to select Existing UserName");
		}
	}

	/**
	 * Method to create new username for white player
	 * @author Alex Masciotra
	 * @param event when done is pressed or entered is pressed
	 */
	public void whitePlayerSelectsNewUserName(Event event) {

		Boolean isValid = true;

		whiteUsernameExistsLabel.setText("");

		try {
			QuoridorController.assignPlayerColorToUserName("white", quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to assign next Player");
		}
		String userNameToSet = whiteNewName.getText();

		try {
			isValid = QuoridorController.selectNewUserName(userNameToSet, quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to select New UserName");
		}

		if (!isValid){
			whiteUsernameExistsLabel.setText(userNameToSet + " already exists");
		} else {
			whiteExistingName.getSelectionModel().select(userNameToSet);
			whiteUsernameExistsLabel.setText("");
		}
		whiteNewName.setText("");
	}

	/**
	 * Method to create new username for black player
	 * @author Alex Masciotra
	 * @param mouseEvent when done is pressed
	 */
	public void blackPlayerSelectsNewUserName(Event mouseEvent) {

		Boolean isValid = true;

		blackUsernameExistsLabel.setText("");

		try {
			QuoridorController.assignPlayerColorToUserName("black", quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to assign next Player");
		}

		String userNameToSet = blackNewName.getText();

		try {
			isValid = QuoridorController.selectNewUserName(userNameToSet, quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to select New UserName");
		}

		if (!isValid){
			blackUsernameExistsLabel.setText(userNameToSet + " already exists");
		} else {
			blackExistingName.getSelectionModel().select(userNameToSet);
			blackUsernameExistsLabel.setText("");
		}
		blackNewName.setText("");
	}
	/**
	 * @author Matthias Arabian
	 * @return the wallMoveCandidate currently on the board
	 */
	public Rectangle getWallMoveCandidate() {
		return wallMoveCandidate;
	}

    /**
     * @author Matthias Arabian
     * @param keyEvent
     * detect that the wall should be rotated and acts accordingly.
     * rotates GUI and model wallMoveCandidate.
     */
	public static void rotateWallEvent(KeyEvent keyEvent) {

		//ensure that a wall is selected
		if (wallSelected == null)
			return;
		//get the rectangle that is grabbed
		//Rectangle wall = (Rectangle) mouseEvent.getSource();
		//ROTATE WALL WILL RUN DURING THE MOVE WALL EVENT
		//wallMoveCandidate = wall;
		//System.out.println("hi"); //used for debuging purposes
        Quoridor q = QuoridorApplication.getQuoridor();

		if(keyEvent.getCode()==KeyCode.R) {
			QuoridorController.GUI_flipWallCandidate();
			QuoridorController.flipWallCandidate();
			q.getCurrentGame().getWallMoveCandidate().getTargetTile().getRow();
			if (!QuoridorController.validatePosition(q.getCurrentGame().getWallMoveCandidate().getTargetTile().getRow()
            , q.getCurrentGame().getWallMoveCandidate().getTargetTile().getColumn(), q.getCurrentGame().getWallMoveCandidate().getWallDirection().toString())){
                wallSelected.setStroke(Color.RED);
            }
            else {
                wallSelected.setStroke(Color.GREEN);
            }
		}


	}

    private void doDropWallLogicAtEndOfTurn(){
        //dropWall implemented here
        if (wallSelected != null) {
            boolean dropSuccessful;
            try {
                dropSuccessful = QuoridorController.releaseWall(quoridor);
            } catch (Exception ee) {
                throw new java.lang.UnsupportedOperationException("Unable to drop Wall");
            }

            if (!dropSuccessful) {
                //invalidWallPlacement.setText("Invalid Wall Placement");
                displayIllegalNotification("Invalid wall placement");
                return;
            } else {
                invalidWallPlacement.setText("");
                gameSessionNotificationLabel.setText("");
                validWallGrab = false;
                wallSelected = null;
                wallMoveCandidate.setStroke(Color.BLACK);
                wallMoveCandidate = null;
                wallGrabbed = false; //added by Thomas
            }
        } //end dropWall

        wallGrabbed = false; //reset flag even if no wall has been placed b/c movePawn uses it as well.
    }

	/**
	 * @author Matthias Arabian
	 * Triggered when a user presses a button that might end a turn. Checks that that is a valid operation
	 * and acts accordingly.
	 * If the button can be pressed, then the player's turn is over. Its timer is turned off, and the other player's turn begins.
	 * Other's timer turns on.
	 */
	public void switchPlayer() {
        //wallGrabbed is used as a flag to assert that the player has completed an action.
        //wallGrabbed is set to true when a pawn is moved AND when a wall is grabbed.
	    String color = QuoridorController.getColorOfPlayerToMove(quoridor);

		if (!wallGrabbed)
            return;

		//if there is a wall on the board, make sure it's in a legal position
        if (wallMoveCandidate != null && !wallSelected.getStroke().equals((Color.GREEN)))
            return;

        //turn btn_dropWall to disabled and invisible
        btn_dropWall.setDisable(true);
        btn_dropWall.setVisible(false);
        //clear the wall error strings
        invalidWallPlacement.setText("");
        gameSessionNotificationLabel.setText("");

		if (color.equals("white")) {
			 //starts black player turn
			doDropWallLogicAtEndOfTurn();
			//change opacity of wall stocks
			whiteStock.setOpacity(0.5);
			blackStock.setOpacity(1);

			//change image of players accordingly
			setTileImage(blackPlayerTile, TileImage.BLACK_PAWN_SELECTED);
			setTileImage(whitePlayerTile, TileImage.WHITE_PAWN);

			//change the text of the GUI and turn timers on/off
			lbl_black_awaitingMove.setText("");
			QuoridorController.stopPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()
					,timer);
			timer = new Timer();
			lbl_white_awaitingMove.setText("AWAITING MOVE");
			lbl_black_awaitingMove.setText("It is your turn!");

			QuoridorController.startPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()
					,timer);

			//update the model
			Player whitePlayer = QuoridorController.getCurrentWhitePlayer();
			QuoridorController.completePlayerTurn(whitePlayer); //If it's WhitePlayer's turn, end it. If not, nothing happens.
		}
		else { //starts white player turn
			//change opacity of wall stocks
			whiteStock.setOpacity(1);
			blackStock.setOpacity(0.5);

			//change image of players accordingly
			setTileImage(whitePlayerTile, TileImage.WHITE_PAWN_SELECTED);
			setTileImage(blackPlayerTile, TileImage.BLACK_PAWN);

			//do the drop wall logic
			doDropWallLogicAtEndOfTurn();
			lbl_white_awaitingMove.setText("");
			QuoridorController.stopPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()
					,timer);
			timer = new Timer();
			lbl_white_awaitingMove.setText("It is your turn!");
			lbl_black_awaitingMove.setText("AWAITING MOVE");
			QuoridorController.startPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()
					,timer);

			//update the model
			Player blackPlayer = QuoridorController.getCurrentBlackPlayer();
			QuoridorController.completePlayerTurn(blackPlayer); //If it's BlackPlayer's turn, end it. If not, nothing happens.
		}
        //draw all legal moves the new player can make;
        setPossibleMoveTiles();
	}

    /**
     * @author Matthias Arabian
     * Resets all GUI elements that are related to player turns to their default values
     * e.g. It should be the White player's turn. All timers should be off.
     */
	private void resetGUI_playerTurn(){
		if (QuoridorApplication.getQuoridor().hasBoard() == false)
			return;
		if (QuoridorController.getColorOfPlayerToMove(QuoridorApplication.getQuoridor()).toLowerCase().equals("black")){
			lbl_white_awaitingMove.setText("");
			lbl_white_awaitingMove.setText("It is your turn!");
			lbl_black_awaitingMove.setText("AWAITING MOVE");
		}
		timer.cancel();
		timer = new Timer(); //stop the player timers
	}


	//input1=white, input2=black

	/**
	 * process the thinking time data from the thinking time text field fed into the method
	 * @param input1 white player
	 * @param input2 black player
	 * @author David
	 */
	private void setThinkingTime(String input1, String input2){

		if(input1.length()!=5 || input2.length()!=5) {
			throw new IllegalArgumentException("cannot set thinkingTime. Thinking time must follow format 00:00");
		}
		try {
			int min = Integer.parseInt(input1.substring(0, 2));
			int second = Integer.parseInt(input1.substring(3, 5));
			QuoridorController.setThinkingTime(min, second, 0);
			min = Integer.parseInt(input2.substring(0, 2));
			second = Integer.parseInt(input2.substring(3, 5));
			QuoridorController.setThinkingTime(min, second, 1);

		}
		catch(Throwable e){
			throw new IllegalArgumentException("cannot set thinkingTime. Thinking time must follow format 00:00");
		}


	}

	/**
	 * display an alert message that can be clicked away
	 * @param message to be displayed
	 */
	public static void displayIllegalNotification(String message) {
		try{
		Text text = new Text();
		Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);

		alert.showAndWait();

		isIllegalNotificationDisplayed = true;

		if (alert.getResult() == ButtonType.OK) {
			isIllegalNotificationDisplayed = false;
		}}
		catch(Throwable e)
	{
		isIllegalNotificationDisplayed = true;//to account to missing user interaction during testing. No one will be there to see error window, but they exist.
	}
	}

	/**
	 * @author David
	 * @return true if there is currently an IllegalNotification (alert) displayed
	 */
	public boolean isIllegalNotificationDisplayed() {
		return isIllegalNotificationDisplayed;
	}

	/**
	 * The following two methods return coordinate of the wallSelected using the old
	 * coodinate system. Be cautious using it in the new one.
	 * @return
	 */
	private static double wallDisplayX() {
		if(wallSelected==null) return -1;
		return wallSelected.getTranslateX();
	}
	private static double wallDisplayY() {
		if(wallSelected==null) return -1;
		return wallSelected.getTranslateY();
	}

	/**
	 * Checks whether the wall is displayed at the given coordinate in GUI
	 * @param row
	 * @param col
	 * @author David
	 * @return true if it is, false otherwise
	 */
	public static boolean isWallDisplayedAt(int row, int col) {

		if((wallDisplayX()==110+(col-1)*30) && (wallDisplayY()==35+(row-1)*30)){
			return true;
		}

		return false;
  }
	/**
	 * @author Matthias Arabian
	 * @return value of string awaitingMove
	 * this is used to ensure that the player switching has been announced to the players
	 */
	public String getWhitePlayerStatus(){
        if (lbl_white_awaitingMove == null || lbl_white_awaitingMove.equals(""))
            return null;
        return lbl_white_awaitingMove.getText();

	}

	/**
	 * @author Matthias Arabian
	 * @return value of string awaitingMove
	 * this is used to ensure that the player switching has been announced to the players
	 */
	public String getBlackPlayerStatus(){
	    if (lbl_black_awaitingMove == null || lbl_black_awaitingMove.equals(""))
	        return null;
		return lbl_black_awaitingMove.getText();

	}


    /**
     * @author Matthias Arabian
     * @param event this parameter is sent to the saveGame function to allow it to work properly
     *
     * Method used to pause and resume the player timers while saving a game.
     */
	public void saveGameContainerFunction(Event event){
		//pause timer
		QuoridorController.stopPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				,timer);
		timer = new Timer();

		//disable page
        getCurrentPage().setDisable(true);
        getPage(Page.TOP_BUTTONS).setDisable(true);

		saveGame(event); //save game

        //enable page
        getCurrentPage().setDisable(false);
        getPage(Page.TOP_BUTTONS).setDisable(false);

		//restart timer
		QuoridorController.startPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getCurrentPosition().getPlayerToMove()
				,timer);
	}

	/**
	 * Event Listener. When the saveGame event is called, this method begins saving the current game instance into a file through dialog windows.
	 * @author Edwin Pan
	 * @param event
	 */
	public void saveGame(Event event) {
		//Good reference: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
		//Look for "Saving Files" section header.

		//Prepare the file choosing window
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Your Game");
		List<String> extensions = new ArrayList<String>();
		extensions.add(".dat");
		extensions.add(SaveConfig.gameSavesDataExtension);
		ExtensionFilter extensionsFilter = new ExtensionFilter("quoridor data saves files",extensions);
		fileChooser.setSelectedExtensionFilter( extensionsFilter );
		fileChooser.setInitialFileName("newgamesave.dat");
		SaveConfig.createGameSavesFolder();
		fileChooser.setInitialDirectory( new File(SaveConfig.getGameSavesFolder()) );

		//Show the file choosing window and await response
		Stage stage = new Stage();
		File file = fileChooser.showSaveDialog(stage);

		//If the user did not choose a file, let them know they did not choose a file and finish.
		if( file == null ) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Save Game Warning");
			alert.setHeaderText("Your game was not saved.");
			alert.setContentText(null);
			alert.showAndWait();
			return;
		}

		//If the user chose a file, then attempt to save the file.
		try {
			SavingStatus saveStatus = QuoridorController.saveGame( file.getAbsolutePath() , QuoridorController.getCurrentGame() );
			switch (saveStatus) {
				case ALREADY_EXISTS:	//If it already exists, ask if the player wants to overwrite it.
					//set up Alert
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Confirm Overwrite?");
					alert.setHeaderText("You are about to overwrite your file '" + file.getName() + "'. Proceed?");
					alert.setContentText(null);
					//receive from alert
					Optional<ButtonType> overwriteConfirmation = alert.showAndWait();
					if( overwriteConfirmation.get() == ButtonType.OK ) {	//If we are good to overwrite

						//Now attempt to overwrite the file.
						SavingStatus saveStatus2 = QuoridorController.saveGame( file.getAbsolutePath() , QuoridorController.getCurrentGame(), SavePriority.FORCE_OVERWRITE );
						switch( saveStatus2 ) {
							case OVERWRITTEN:
								return;
							case FAILED:
								Alert alert2 = new Alert(AlertType.ERROR);
								alert2.setTitle("Failed To Overwrite");
								alert2.setHeaderText("We were unable to overwrite your game onto the pre-existing save. Sorry!");
								alert2.setContentText(null);
								alert2.showAndWait();
								return;
							default:
								Alert alert3 = new Alert(AlertType.ERROR);
								alert3.setTitle("pog");
								alert3.setHeaderText("How tho");
								alert3.setContentText("... You have to be looking for these bugs... Like how did you even do that? Tell you what: how about you go skirt along, writing down what steps you took, and mail'em to some rando named Edwin Pan? He won't respond but at least your strange antiques will have finally amazed someone. Thanks!");
								alert3.showAndWait();
								return;

						}

					}
					//If the user does not want to overwrite; it is canceled. so we stop all here.
					return;

				case FAILED:			//If the save failed, let the player know and stop the saving.
					Alert alert4 = new Alert(AlertType.ERROR);
					alert4.setTitle("Failed To Save");
					alert4.setHeaderText("We were unable to save your game. Sorry!");
					alert4.setContentText(null);
					alert4.showAndWait();
					return;

				case SAVED:				//If the save succeeded, no need to do anything.
					return;

				default:
					Alert alert5 = new Alert(AlertType.ERROR);
					alert5.setTitle("wut");
					alert5.setHeaderText("MonkaS");
					alert5.setContentText("So uhhhh You just broke the code here! I'm not even sure how you got here.... How about this? Write down or remember how you got here, and go look for some rando named Edwin Pan who worked on this and tell him you done fucked up? Thanks!");
					alert5.showAndWait();
					return;

			}

		} catch (IOException err) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Save Game Failed Warning");
			alert.setHeaderText("The Application was unable to save your game into your file system due to some File System input-output error. Sorry!");
			alert.setContentText(null);
			alert.showAndWait();
			return;
		}


	}


	/**
	 * Event Listener for a button. When the button for continuing a selected previous game is pressed, this method attempts to load in the data of that save file. If the save file cannot be loaded, Alert dialog messages are popped; if the save file is loaded, the application then
	 * heads straight for the game session page.
	 * @param event
	 */
	public void continuePreviousGame(Event event) {
		//Make sure we have a selected item.
		if( this.loadedGameList.getSelectionModel().getSelectedItem() == null ) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("No Save Selected!");
			alert.setHeaderText("You did not select a save!");
			alert.setContentText(null);
			alert.showAndWait();
			return;
		}
		String selectedPath = this.loadedGameList.getSelectionModel().getSelectedItem().toString();

		//Find the players to used for reloading into this game.
		//Because game saves in sprint3-format do not contain player data, I am unfortunately forced to use random users
		//THERE IS ALSO A BUG IN MY QUORIDORSAVESMANAGER CODE: the load game should require user input, not player input, as player input would give no choice in destination.
		User user1, user2;
		if( QuoridorApplication.getQuoridor().getUsers().size() < 2 ) {
			user1 = new User("firstboi",QuoridorApplication.getQuoridor());
			user2 = new User("secondboi",QuoridorApplication.getQuoridor());
		} else {
			user1 = QuoridorApplication.getQuoridor().getUser(0);
			user2 = QuoridorApplication.getQuoridor().getUser(1);
		}
		Player player1 = new Player( new Time((10*60+10)*1000) , user1 , 1 , Direction.Horizontal );
		Player player2 = new Player( new Time((10*60+10)*1000) , user1 , 9 , Direction.Horizontal );

		//Attempt to load the save game.
		try{
			QuoridorController.loadSavedGame(selectedPath, player1, player2);
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Save Was Not Found");
			alert.setHeaderText("The save file selected could not be loaded due to an apparent file-not-found error.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return;
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Save Could Not Be Loaded");
			alert.setHeaderText("The save file selected could not be loaded due to a file system input-output error.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return;
		} catch (InvalidPositionException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Save Is Corrupted");
			alert.setHeaderText("The save file selected appears to have invalid data. It cannot be loaded.");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			return;
		}

		//Once we're doing loading in the game, go on and actually continue the game in the game session page.
		this.Goto_Game_Session_Page();
	}

    /**
     * @author Matthias Arabian
     * Method used to ensure that the view's scenes are properly initialized to their default values.
     */
	private void resetGUItoMainPage(){
		for (Page p : Page.values()){
			getPage(p).setVisible(false);
			getPage(p).setDisable(true);
		}
		getPage(Page.TOP_BUTTONS).setDisable(false);
		getPage(Page.TOP_BUTTONS).setVisible(true);
		getPage(Page.MAIN_PAGE).setDisable(false);
		getPage(Page.MAIN_PAGE).setVisible(true);
		currentState = Page.MAIN_PAGE;
	}

	/**
	 * Updates position of window according to position of mouse
	 * @param event
	 * @author Daniel Wu
	 */
    public void topDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

	/**
	 * Updates position of mouse
	 * @param event
	 * @author Daniel Wu
	 */
	public void topPressed(MouseEvent event){
        x = event.getSceneX();
        y = event.getSceneY();
    }

	/**
	 * Close and stop program when x button is pressed
	 * @author Daniel Wu
	 */
	public void quit(){
        System.exit(0);
    }

	/**
	 * Toggles maximization when square button is pressed
	 * @param event
	 * @author Daniel Wu
	 */
	public void toggleMaximize(MouseEvent event){
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		if(stage.isMaximized()){
			stage.setMaximized(false);
		} else {
			stage.setMaximized(true);
		}
	}
	public boolean isBlackTimeUp(){
		return blackTimeISUp;
	}
	public boolean isWhiteTimeUp(){
		return whiteTimeIsUp;
	}

	/**This method is used by mouse click event handler to convert row and column clicked
	 * to a direction to be fed to controller movepawn method
	 *
	 * The method checks for possible positions of tiles clicked. The method only
	 * returns a non-empty direction only if the tile clicked is immediately close of the tile
	 * of pawn, or if tile clicked is separated by pawn by one other pawn, or if tile clicked is diagonal to
	 * tile of pawn
	 * @author David
	 * @param row
	 * @param col
	 * @return direction as a String, with the possible eight directions.
	 */
	private String convertTileToDirection(int row, int col){
		int currentRow = QuoridorController.getCurrentPawnTilePos(0);
		int currentCol = QuoridorController.getCurrentPawnTilePos(1);
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
		/*if(rowDiff>0){

			dir = "down";

		}
		else if(rowDiff<0){
			dir = "up";
		}

		if(colDiff>0){
			dir = dir + "right";
		}
		else if(colDiff<0){
			dir = dir + "left";
		}

		return dir;*/


	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //TUTORIAL METHODS
    public void Start_Tutorial(){
        isInTutorial = true;
    }

    public boolean isInTutorial() {
        return isInTutorial;
    }

    private void resetGUI_tutorial() {
        for (Node child : mama_pane.getChildren()){
            AnchorPane c = (AnchorPane)child;
            if (c.getId().equals("Game_Session_Page"))
            {
                HBox papa_container = (HBox)c.getChildren().get(0);
                for (Node kid: papa_container.getChildren()) {
                    VBox v = (VBox) kid;
                    if (!v.getId().equals("gameBoard")){
                        for (Node bebe: v.getChildren())
                            bebe.setRotate(0);
                    }
                }
            }
        }
    }

	public void UI_dance(){
	    for (Node child : mama_pane.getChildren()){
	        AnchorPane c = (AnchorPane)child;
	        if (c.getId().equals("Game_Session_Page"))
            {
                HBox papa_container = (HBox)c.getChildren().get(0);
                for (Node kid: papa_container.getChildren()) {
                    VBox v = (VBox) kid;
                    if (!v.getId().equals("gameBoard")){
                        for (Node bebe: v.getChildren())
                            if (bebe.getRotate() == 0) {
                                bebe.setRotate(15);
                            } else
                                bebe.setRotate(-bebe.getRotate());
                    }

                }
            }
        }
    }

}