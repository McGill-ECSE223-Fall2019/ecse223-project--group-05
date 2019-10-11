package ca.mcgill.ecse223.quoridor.controller;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;

import java.util.List;

public class QuoridorController {


    /**
     * Method to release current Wall in my hand and register move
     * @param game Current Game Object, to retrieve the wallMoveCandidate in the game and any other necessary info
     * @return True if releaseWall was successful and wall could be registered, false if not
     * @author Alex Masciotra
     */
    public static Boolean releaseWall(Game game){
        throw new java.lang.UnsupportedOperationException("Not yet implemented");
    }


    /***
     * Method to set existing username to player
     * @param userName username to set to player
     * @param game Quoridor Game in order to get the player and set username to that existing player
     * @return True if successful, false if not
     * @author Alex Masciotra
     */
    public static Boolean selectExistingUserName(String userName, Game game){

        throw new java.lang.UnsupportedOperationException("Not yet implemented");


        //get all existing usernames and see if username provided is found
    }

    /**
     * Method to assign new Username to a player
     * @param userName username to set to player
     * @param game Quoridor Game Application in order to get the player and set username to that player
     * @author Alex Masciotra
     * @return True assigning of new username was correct / False if name already exists
     */
    public static Boolean selectNewUserName(String userName, Game game){ //could be boolean to see if it could indeed set

        throw new java.lang.UnsupportedOperationException("Not yet implemented");


        //get all existing userNames and check to see if new userNAme is unique and available or in use
    }

    /***
     * This method is to provide a list of existing user names on the UI when at the quoridor menu to see a list of
     * existing usernames and select one
     * @return List of UserNames in Quoridor
     * @author Alex Masciotra
     */
    public List <String> provideExistingUserNames(){

        throw new java.lang.UnsupportedOperationException("Not yet implemented");

    }


}

