package ca.mcgill.ecse223.quoridor.timer;

import java.sql.Time;
import java.util.TimerTask;
import ca.mcgill.ecse223.quoridor.model.*;
import javafx.scene.control.Label;

public class PlayerTimer extends TimerTask{

    private Player player;
    private Label GUIlabel;
    
    public PlayerTimer(Player player, Label lbl) {
        this.player = player;
        GUIlabel = lbl;
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
        GUIlabel.setText(updatedTime.toString());
    }
}