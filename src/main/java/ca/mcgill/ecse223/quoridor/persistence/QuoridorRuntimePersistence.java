package ca.mcgill.ecse223.quoridor.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.model.Quoridor;

/**
 * 
 * @author Edwin Pan
 *
 *	Persistence class which uses object serialization rather than a coded instance-to-textfile algorithm.
 *
 */
public class QuoridorRuntimePersistence {
		
	/**
	 * Saves the provided instance of Quoridor into an serialized java object output stream.
	 * Throws runtime errors if there is a FileNotFoundException or IOException.
	 * @param quoridor
	 */
	private static void save( Quoridor quoridor , String path) {
		
		FileOutputStream fis; 
		try{
			fis = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		ObjectOutputStream ois ;
		try{
			ois = new ObjectOutputStream(fis);
			ois.writeObject(quoridor);
			ois.flush();
			ois.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	/**
	 * Loads the set file of quoridor data and returns it.
	 * Throws runtime errors if there is a FileNotFoundException or IOException.
	 * @return quoridor instance with data.
	 */
	private static Quoridor load(String path) {
		
		FileInputStream fis; 
		try{
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		ObjectInputStream ois ;
		Quoridor quoridor;
		try{
			ois = new ObjectInputStream(fis);
			try {
				quoridor = (Quoridor)( ois.readObject() );
				ois.close();
			} catch (ClassNotFoundException e) {
				ois.close();
				throw new RuntimeException("File \"" + path + "\" was not able to be read.", e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return quoridor;
		
	}
	
	
}
