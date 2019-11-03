//THE IMPORT STATEMENT BELOW IS REQUIRED IN THE .fxml FILE
//IT GETS DELETED WHEN YOU REGENERATE THE .fxml FILE WITH SCENE BUILDER
//YOU MUST ADD IT IN MANUALLY WHEN THAT HAPPENS

//<?import java.util.ArrayList?>


package ca.mcgill.ecse223.quoridor.view;


import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.*;


import java.util.List;
import java.util.Timer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.TimerTask;

import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.application.Platform;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ViewInterface {


	//these are the pages that the user can travel to/interact with
	private enum Page {
		TOP_BUTTONS,
		MAIN_PAGE,
		NEW_GAME_PAGE,
		RULES_PAGE,
		LOAD_GAME_PAGE,
		SELECT_HOST_PAGE,
		GAME_SESSION_PAGE,
		CHOOSE_OPPONENT_PAGE;
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




	


	
	
     //Load Game Page
	@FXML private Label lbl_directory;
    @FXML ListView loadedGameList;
    private int counter = 0; //used for debugging of listView
	//Game Session Page
	@FXML private GridPane Game_Board;
	@FXML private Rectangle whiteWall1, whiteWall2, whiteWall3, whiteWall4, whiteWall5, whiteWall6, whiteWall7, whiteWall8, whiteWall9, whiteWall10;
	@FXML private Rectangle blackWall1, blackWall2, blackWall3, blackWall4, blackWall5, blackWall6, blackWall7, blackWall8, blackWall9, wblackWall10;
	@FXML private Label gameSessionNotificationLabel;
	@FXML private Label whiteTimer;
	@FXML private Label blackTimer;
	@FXML private Button btn_whitePlayerTurn, btn_blackPlayerTurn;
	@FXML private Label lbl_black_awaitingMove, lbl_white_awaitingMove;
	@FXML private Label whitePlayerName;
	@FXML private Label blackPlayerName;

	//Grab and Drag wall variables
	double wallXPosition, wallYPosition;

	public static boolean isIllegalNotificationDisplayed = true;
	private static final double HORIZONTALSTEP = 30;
	private static final double VERTICALSTEP = 35;
	private static Rectangle wallSelected;
	private static Quoridor quoridor;
	private Player whitePlayer, blackPlayer;
	private Timer timer;
	private Timer RefreshTimer;

  //Rotate wall variables
	private Rectangle wallMoveCandidate;


	/**
	 * @author Thomas Philippon
	 * Changes the GUI CurrentPage to the Choose Opponent Page.
	 */
	public void GrabWall(MouseEvent mouseEvent) {
		wallSelected = (Rectangle) mouseEvent.getSource();


		wallSelected.toFront();
		boolean grabWallResult;
		try {
			grabWallResult = QuoridorController.grabWall(QuoridorApplication.getQuoridor());
		}
		catch(Exception e){
				throw new java.lang.UnsupportedOperationException("Cannot retrieve the number of walls in stock");
			}

		if (grabWallResult == true ){
			gameSessionNotificationLabel.setText("You have more walls in stock!");
		}
		else{
			gameSessionNotificationLabel.setText("You have no more walls in stock...");
		}
		//System.out.println();
	}

	/**
	 * @author Thomas Philippon
	 * This method is called when the user drags the walls
	 */
	public void MoveWall(MouseEvent mouseEvent) {
		//get the rectangle that is grabbed
		Rectangle wall = (Rectangle) mouseEvent.getSource();
		//Compute the new wall position and move the wall to that position
			double offsetX = mouseEvent.getX();
			double offsetY = mouseEvent.getY();
			double newTranslateX = wall.getTranslateX() + offsetX;
			double newTranslateY = wall.getTranslateY() + offsetY;
			wall.setTranslateX(newTranslateX);
			wall.setTranslateY(newTranslateY);
    
    //ROTATE WALL WILL RUN DURING THE MOVE WALL EVENT
			wallMoveCandidate = wall;
			wallSelected = wall;
	}
	/**
	 * @author David Deng
	 * This method is called when the user moves the wall using the keyboard.
	 * It is suppsoed to change the rectangle to red if the position is invalid.
	 * It had worked with an old version of GUI, but is not working well with a newer version.
	 * We believe that having more time, it should work more smoothly.
	 */

	@FXML
	public static void MoveWall(KeyEvent keyEvent) {
		boolean isValid = true;
		try {
			if(wallSelected==null && (keyEvent.getCode()== KeyCode.UP||keyEvent.getCode()==KeyCode.DOWN||keyEvent.getCode()==KeyCode.LEFT||keyEvent.getCode()==KeyCode.RIGHT)){
				throw new IllegalArgumentException("no wall was selected.");
			}
			if(keyEvent.getCode()==KeyCode.UP) {
				isValid = QuoridorController.moveWall("up");
				wallSelected.setTranslateY(wallSelected.getTranslateY()-VERTICALSTEP);//translates the rectangle by a tilewidth
				System.out.println("detected");
			}
			else if(keyEvent.getCode()==KeyCode.DOWN) {
				isValid = QuoridorController.moveWall("down");
				wallSelected.setTranslateY(wallSelected.getTranslateY()+VERTICALSTEP);
			}
			else if(keyEvent.getCode()==KeyCode.LEFT) {
				isValid = QuoridorController.moveWall("left");
				wallSelected.setTranslateX(wallSelected.getTranslateX()-HORIZONTALSTEP);
			}
			else if(keyEvent.getCode()==KeyCode.RIGHT) {
				isValid = QuoridorController.moveWall("right");
				wallSelected.setTranslateX(wallSelected.getTranslateX()+HORIZONTALSTEP);
			}
			if(isValid) {
				wallSelected.setStroke(Color.RED);
			}
			else {
				wallSelected.setStroke(Color.BLACK);
			}

			System.out.println(wallSelected.getTranslateX());
			System.out.println(wallSelected.getX());
			System.out.println(wallSelected.getTranslateY());
			System.out.println(wallSelected.getY());
		}
		catch(Throwable e) {
			displayIllegalNotification(e.getMessage());
		}


		//wallSelected.setTranslateX(newTranslateX);
		//wallSelected.setTranslateY(newTranslateY);
		wallSelected.toFront();
		//System.out.println("x: " + wallDisplayX());
		//System.out.println("y: " + wallDisplayY());

	}

	/**
	 * @author Alex Masciotra
	 *This method is executed when the user releases the wall
	 */
	public void DropWall(MouseEvent mouseEvent) {
		//gameSessionNotificationLabel.setText("Invalid Wall Placement");

		Boolean dropSuccessful
	}

    /**
     * @author Matthias Arabian
     * opens a DialogWindow prompting the user to select a directory that contains game files.
     * Then parse through that directory and upodate the GUI to display the user input
     */
	public void addToLoadedGameList() {
	    //directory chooser dialog window
		Stage stage = new Stage();
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Select directory with saved games");
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
		    	return name.endsWith("dat");
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
	 */
	public void Goto_Main_Page() {
		Goto_Page(Page.MAIN_PAGE);
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
				QuoridorController.initializeBoard(QuoridorApplication.getQuoridor(), timer);
			} catch (Exception e) {
				throw new java.lang.UnsupportedOperationException("Cannot initialize the board");
			}

			//get both players
			whitePlayer = QuoridorController.getCurrentWhitePlayer();
			blackPlayer = QuoridorController.getCurrentBlackPlayer();

			whitePlayerName.setText(QuoridorController.getPlayerName(whitePlayer));
			blackPlayerName.setText(QuoridorController.getPlayerName(blackPlayer));


			//This tasks runs on a separate thread. It is used to update the GUI every second
			RefreshTimer.schedule(new TimerTask() {
				public void run() {
					Platform.runLater(new Runnable() {
						public void run() {
							//Update white player's thinking time clock
							whiteTimer.setText(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime().toString());
							//Update black's player thinking time clock
							blackTimer.setText(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime().toString());
						}
					});
				}
			}, 0, 1000);

			Goto_Page(Page.GAME_SESSION_PAGE);
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
		CurrentPage.setDisable(false);
		CurrentPage.setVisible(true);
		CurrentPage.toFront();
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
		else 
			return null;
	}

	
	/**
	 * @author Matthias Arabian
	 * initializes the FXML components. this code runs once the application is launched but before the GUI is displayed.
	 */
	public void initialize() {
		//Populate game board with colorful tiles
		for (int row = 0; row < 17; row+=2) {
			for (int col = 0; col < 17; col+=2) {
				AnchorPane tmp = new AnchorPane();
				tmp.setStyle("-fx-background-color: #ffffff");
				Game_Board.add(tmp , row, col);
			}
		}


		//Initialize the timers
		timer = new Timer();
		RefreshTimer = new Timer();

		//quoridor =QuoridorApplication.getQuoridor();

	}

	/**
	 * Method to display ExistingUserNames
	 * @author Alex Masciotra
	 * @param mouseEvent when arrow is pressed
	 */
    public void displayExistingUserNames(MouseEvent mouseEvent) {

		//when the arrow is pressed

		List<String> existingUserNames = null;
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

		try {
			QuoridorController.assignPlayerColorToUserName("white", quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to assign next Player");
		}
		String userNameToSet = whiteExistingName.getValue().toString();

		try {
			QuoridorController.selectExistingUserName(userNameToSet, quoridor);
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
		String userNameToSet = blackExistingName.getValue().toString();

		try {
			QuoridorController.selectExistingUserName(userNameToSet, quoridor);
		} catch (Exception e) {
			throw new java.lang.UnsupportedOperationException("Unable to select Existing UserName");
		}
	}

	/**
	 * Method to create new username for white player
	 * @author Alex Masciotra
	 * @param mouseEvent when done is pressed
	 */
	public void whitePlayerSelectsNewUserName(MouseEvent mouseEvent) {

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
		}
	}

	/**
	 * Method to create new username for black player
	 * @author Alex Masciotra
	 * @param mouseEvent when done is pressed
	 */
	public void blackPlayerSelectsNewUserName(MouseEvent mouseEvent) {

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
		}
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
     * @param mouseEvent
     * detect that the wall should be rotated and acts accordingly.
     * rotates GUI and model wallMoveCandidate.
     */
	public void rotateWallEvent(MouseEvent mouseEvent) {

		//get the rectangle that is grabbed
		Rectangle wall = (Rectangle) mouseEvent.getSource();
		//ROTATE WALL WILL RUN DURING THE MOVE WALL EVENT
		wallMoveCandidate = wall;
		System.out.println("hi"); //used for debuging purposes
		QuoridorController.GUI_flipWallCandidate("horizontal");
        QuoridorController.flipWallCandidate();

	}

	/**
	 * @author Matthias Arabian
	 * @param e
	 * Triggered when a user presses a button that might end a turn. Checks that that is a valid operation
	 * and acts accordingly.
	 * If the button can be pressed, then the player's turn is over. Its timer is turned off, and the other player's turn begins.
	 * Other's timer turns on.
	 */
	public void switchPlayer(Event e) {
		Button b = ((Button)e.getSource());
		if (b.getId().equals(btn_whitePlayerTurn.getId())) {
			if (btn_whitePlayerTurn.getText().equals("END TURN")) { //starts black player turn
				btn_blackPlayerTurn.setText("END TURN");
				lbl_black_awaitingMove.setText("");
                QuoridorController.stopPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()
                        ,timer);
                timer = new Timer();
				btn_whitePlayerTurn.setText("NOT WHITE TURN");
				lbl_white_awaitingMove.setText("AWAITING MOVE");
                QuoridorController.startPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()
                        ,timer);

                //update the model
                Player whitePlayer = QuoridorController.getCurrentWhitePlayer();
                QuoridorController.completePlayerTurn(whitePlayer); //If it's WhitePlayer's turn, end it. If not, nothing happens.
			}
		}
		else { //starts white player turn
			if (btn_blackPlayerTurn.getText().equals("END TURN")) {
				btn_whitePlayerTurn.setText("END TURN");
				lbl_white_awaitingMove.setText("");
				QuoridorController.stopPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer()
                        ,timer);
				timer = new Timer();
				btn_blackPlayerTurn.setText("NOT BLACK TURN");
				lbl_black_awaitingMove.setText("AWAITING MOVE");
                QuoridorController.startPlayerTimer(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer()
                        ,timer);

				//update the model
                Player blackPlayer = QuoridorController.getCurrentBlackPlayer();
                QuoridorController.completePlayerTurn(blackPlayer); //If it's BlackPlayer's turn, end it. If not, nothing happens.
			}
		}
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
}