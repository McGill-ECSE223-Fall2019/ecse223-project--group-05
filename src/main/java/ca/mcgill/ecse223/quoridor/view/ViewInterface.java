//THE IMPORT STATEMENT BELOW IS REQUIRED IN THE .fxml FILE
//IT GETS DELETED WHEN YOU REGENERATE THE .fxml FILE WITH SCENE BUILDER
//YOU MUST ADD IT IN MANUALLY WHEN THAT HAPPENS

//<?import java.util.ArrayList?>


package ca.mcgill.ecse223.quoridor.view;


import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;

import java.util.List;
import java.util.Timer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.TimerTask;

import ca.mcgill.ecse223.quoridor.model.Quoridor;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
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




	
	@FXML ListView loadedGameList;
	private int counter = 0;
	
	
     //Load Game Page
	@FXML private Label lbl_directory;

	//Game Session Page
	@FXML private GridPane Game_Board;
	@FXML private Rectangle whiteWall1, whiteWall2, whiteWall3, whiteWall4, whiteWall5, whiteWall6, whiteWall7, whiteWall8, whiteWall9, whiteWall10;
	@FXML private Rectangle blackWall1, blackWall2, blackWall3, blackWall4, blackWall5, blackWall6, blackWall7, blackWall8, blackWall9, wblackWall10;
	@FXML private Label gameSessionNotificationLabel;
	@FXML public Label whiteTimer;
	@FXML private Label blackTimer;

//Grab and Drad wall variables
	double wallXPosition, wallYPosition;


	private static Quoridor quoridor;
	public Timer timer;
	public Timer RefreshTimer;
  public String timerVal;

  //Rotate wall variables
	private Rectangle wallMoveCandidate;


	/**
	 * @author Thomas Philippon
	 * Changes the GUI CurrentPage to the Choose Opponent Page.
	 */
	public void GrabWall(MouseEvent mouseEvent) {

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
	}

	/**
	 * @author Thomas Philippon
	 *This method is executed when the user releases the wall
	 */
	public void DropWall(MouseEvent mouseEvent) {
		gameSessionNotificationLabel.setText("Invalid Wall Placement");
    wallMoveCandidate = null;
	}


	public void addToLoadedGameList() {
		Stage stage = new Stage();
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Select directory with saved games");
		File file = fileChooser.showDialog(stage);
		stage.close();
		if (file != null) {
			lbl_directory.setText(file.toString());
			detectGameFiles(file);
		}
		else {
			lbl_directory.setText("Directory could not be opened");
		}
	}



	private void detectGameFiles(File file) {
		File[] gameFiles = file.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	return name.endsWith("dat");
		    }
		});
		System.out.println(gameFiles);
		loadedGameList.getItems().clear();
		for (File f : gameFiles)
			loadedGameList.getItems().add(f.toString());
	}

	
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
			QuoridorController.initializeBoard(QuoridorApplication.getQuoridor(), timer);
		}
		catch(Exception e){
			throw new java.lang.UnsupportedOperationException("Cannot initialize the board");
		}


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
	@SuppressWarnings("deprecation")
	public void initialize() {
		//Populate game board with colorful tiles
		for (int row = 0; row < 17; row+=2) {
			for (int col = 0; col < 17; col+=2) {
				AnchorPane tmp = new AnchorPane();
				tmp.setStyle("-fx-background-color: #ffffff");
				Game_Board.add(tmp , row, col);

			}
		}


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

	/**
	 * @author Matthias Arabian
	 * @return the wallMoveCandidate currently on the board
	 */
	public Rectangle getWallMoveCandidate() {
		return wallMoveCandidate;
	}
	
	public void rotateWallEvent(MouseEvent mouseEvent) {

		//get the rectangle that is grabbed
		Rectangle wall = (Rectangle) mouseEvent.getSource();
		//ROTATE WALL WILL RUN DURING THE MOVE WALL EVENT
		wallMoveCandidate = wall;
		System.out.println("hi");
		QuoridorController.GUI_flipWallCandidate("horizontal");

	}
}