package ca.mcgill.ecse223.quoridor;


import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.controller.PawnBehaviour;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorRuntimeModelPersistence;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorSettingsManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import ca.mcgill.ecse223.quoridor.view.ViewInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


public class QuoridorApplication extends Application{


	private static Quoridor quoridor;


	private static PawnBehaviour whitePawnBehaviour;
	private static PawnBehaviour blackPawnBehaviour;
	private static ViewInterface c = null;

	//scaling variables (for resizing of window)
	private double initialH, initialW;

	@Override
	public void start(Stage primaryStage) {
		//load fxml file and display it in the stage
		try {
			File resourceFile = new File("src/main/resources/advs.fxml");
			URL url = resourceFile.toURI().toURL();
			FXMLLoader loader = new FXMLLoader(url);
			Parent root = loader.load();
			c = loader.getController(); //get reference to viewInterface
	        Scene scene = new Scene(root);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
	        primaryStage.setScene(scene);
	        
	        //set the icon and title of window
			File imageFile = new File("src/main/resources/logo_icon.png");
			URL imageUrl = imageFile.toURI().toURL();
	        primaryStage.getIcons().add(new Image(String.valueOf(imageUrl)));
	        primaryStage.setTitle("Quoridor game. Group 5");
	        
	        primaryStage.show();
	        
	        //initialize constants
	        initialH = scene.getHeight();
	        initialW = scene.getWidth();
	        
	        
	        //set event listener for resizing
	        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
	        	Scale scale = new Scale(newVal.doubleValue()/initialW, scene.getHeight()/initialH);
	        	scale.setPivotX(0);
	        	scale.setPivotY(0);
	        	scene.getRoot().getTransforms().setAll(scale);
	       });
			scene.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {
				c.MoveWall(keyEvent);
				ViewInterface.rotateWallEvent(keyEvent);
			});
	        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
	       	Scale scale = new Scale(scene.getWidth()/initialW, newVal.doubleValue()/initialH);
	       	scale.setPivotX(0);
	       	scale.setPivotY(0);
	       	scene.getRoot().getTransforms().setAll(scale);
	       });
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					primaryStage.close();
					Platform.exit();
					System.exit(0);
				}
			});
		} catch (IOException e) {e.printStackTrace();}
	}


	public static Quoridor getQuoridor() {
		if (quoridor == null) {
			
			//Safety behaviour: Ensures that save folders exist.
			SaveConfig.setupSaveDirectories();
			//Special behaviour: Resume previous game or user data.
			if( QuoridorSettingsManager.checkIfToResumePreviousGame() ) {
				quoridor = QuoridorRuntimeModelPersistence.quickload();		//If settings in appdata read that we're supposed to immediately jump into the previous game, then jump in.
			} else {
				quoridor = QuoridorRuntimeModelPersistence.loadUserData();	//If settings in appdata read that we are not supposed to immediately jump into the previous game, then just enter the normal game with users loaded.
			}

		}
 		return quoridor;
	}

	public static PawnBehaviour getWhitePawnBehaviour(Player player){
		if(whitePawnBehaviour==null){
			whitePawnBehaviour = new PawnBehaviour();
			whitePawnBehaviour.setPlayer(player);
			whitePawnBehaviour.setCurrentGame(getQuoridor().getCurrentGame());
			whitePawnBehaviour.entry();
		}
		return whitePawnBehaviour;
	}
	public static PawnBehaviour getBlackPawnBehaviour(Player player){
		if(blackPawnBehaviour==null){
			blackPawnBehaviour = new PawnBehaviour();
			blackPawnBehaviour.setPlayer(player);
			blackPawnBehaviour.setCurrentGame(getQuoridor().getCurrentGame());
			blackPawnBehaviour.entry();
		}
		return blackPawnBehaviour;
	}

	public static PawnBehaviour clearWhitePawnBehaviour() {

		if (whitePawnBehaviour != null)
			whitePawnBehaviour.delete();

		whitePawnBehaviour = null;

		return whitePawnBehaviour;
	}

	public static PawnBehaviour clearBlackPawnBehaviour() {
		if (blackPawnBehaviour != null)
			blackPawnBehaviour.delete();

		blackPawnBehaviour = null;

		return blackPawnBehaviour;
	}

	public static ViewInterface getViewInterface() {

		return c;
	}

	public static void main(String[] args) {
		launch(args);
	}


	private void goThroughSceneElements(Node n){

	}

}
