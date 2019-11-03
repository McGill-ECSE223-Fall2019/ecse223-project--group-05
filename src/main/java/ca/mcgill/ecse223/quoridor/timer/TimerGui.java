package ca.mcgill.ecse223.quoridor.timer;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.model.Player;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.TimerTask;

public class TimerGui extends TimerTask {

    private Player player;


    public TimerGui() {
    }

        public void run () {
            QuoridorApplication.getViewInterface().timerVal = QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime().toString();
        }

}
