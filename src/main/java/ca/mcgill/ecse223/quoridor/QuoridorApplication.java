package ca.mcgill.ecse223.quoridor;


import ca.mcgill.ecse223.quoridor.model.Quoridor;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import ca.mcgill.ecse223.quoridor.view.ViewInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Scale;


public class QuoridorApplication extends Application{

	private static Quoridor quoridor;
	private static ViewInterface c;
	
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
	
	        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
	       	Scale scale = new Scale(scene.getWidth()/initialW, newVal.doubleValue()/initialH);
	       	scale.setPivotX(0);
	       	scale.setPivotY(0);
	       	scene.getRoot().getTransforms().setAll(scale);
	       });
	        
	        scene.addEventHandler(KeyEvent.KEY_PRESSED, (keyEvent) -> {
	            ViewInterface.MoveWall(keyEvent);
	      });

		} catch (IOException e) {e.printStackTrace();}
	}


	public static Quoridor getQuoridor() {
		if (quoridor == null) {
			quoridor = new Quoridor();
		}
 		return quoridor;
	}
	
	public static ViewInterface getViewInterface() {

		return c;
	}

	public static void main(String[] args) {
		launch(args);
	}



}
