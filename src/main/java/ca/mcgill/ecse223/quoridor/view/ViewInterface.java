//THE IMPORT STATEMENT BELOW IS REQUIRED IN THE .fxml FILE
//IT GETS DELETED WHEN YOU REGENERATE THE .fxml FILE WITH SCENE BUILDER
//YOU MUST ADD IT IN MANUALLY WHEN THAT HAPPENS

//<?import java.util.ArrayList?>


package ca.mcgill.ecse223.quoridor.view;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import javafx.event.EventHandler;
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
	
	@FXML ListView loadedGameList;
	private int counter = 0;
	
	
	
//Load Game Page
	@FXML private Label lbl_directory;
	
//Game Session Page
	@FXML private GridPane Game_Board;
	@FXML private Rectangle aWall;
	
	public void addToLoadedGameList() {
            	Stage stage = new Stage();
            	DirectoryChooser fileChooser = new DirectoryChooser();
            	fileChooser.setTitle("Open Resource File");
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
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Choose Opponent Page.
	 */
	public void Goto_Choose_Opponent_Page() {
		Goto_Page(Page.CHOOSE_OPPONENT_PAGE);
	}
	
	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Game Session Page.
	 */
	public void Goto_Game_Session_Page() {
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
		
	aWall.setOnMouseClicked(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("mouse click detected! "+event.getSource());
        }
    });
	aWall.setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
        	((Rectangle)event.getSource()).prefWidth(event.getScreenX());
            ((Rectangle)event.getSource()).setX(event.getScreenX());
            ((Rectangle)event.getSource()).setY(event.getScreenY());
        }
    });
	}
}