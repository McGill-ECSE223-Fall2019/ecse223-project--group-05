package ca.mcgill.ecse223.quoridor.application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.transform.Scale;


public class Main extends Application {
	//scaling variables (for resizing of window)
	private double initialH, initialW;
	
	@Override
	public void start(Stage primaryStage) {
        //load fxml file and display it in the stage
		try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("advs.fxml"));
	        Parent root = loader.load();
	        Scene scene = new Scene(root);
	        primaryStage.setScene(scene);
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
        
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
