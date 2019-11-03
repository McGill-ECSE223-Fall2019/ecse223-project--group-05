package ca.mcgill.ecse223.quoridor;

import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorRuntimeModelPersistence;
import ca.mcgill.ecse223.quoridor.persistence.QuoridorSettingsManager;

public class QuoridorApplication {
	
	private static Quoridor quoridor;

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
			
			
			//quoridor = new Quoridor();
			
		}
 		return quoridor;
	}
	
}
