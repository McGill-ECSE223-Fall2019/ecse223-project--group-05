package ca.mcgill.ecse223.quoridor.timer;

import java.sql.Time;
import java.util.TimerTask;

import ca.mcgill.ecse223.quoridor.controller.QuoridorController;
import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.*;
import ca.mcgill.ecse223.quoridor.controller.*;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class PlayerTimer extends TimerTask{

    private Player player;
    
    public PlayerTimer(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        //get the players remaining thinking time
        long timeRemaining = player.getRemainingTime().getTime();

        //subtract one second since this task is called every second
        timeRemaining = timeRemaining - 1000;

        //set the players remaining thinking time to the updated time
        Time updatedTime = new Time(timeRemaining);
        //System.out.println(updatedTime);
        player.setRemainingTime(updatedTime);
        if (timeRemaining == 0) {
            System.out.println("timer reached 0!!!");

            Platform.runLater(new Runnable() {
                public void run() {
                    QuoridorController.timerUp(player);
                }
            });

            this.cancel();
        }

        //if game isn't running, end timer:
        try{
            if (!QuoridorController.isGameRunning(QuoridorApplication.getQuoridor().getCurrentGame()))
                this.cancel();
        }
        catch(Exception e){
        //it means that there is no current game to the timer cannot be cancelled
        }

    }
}