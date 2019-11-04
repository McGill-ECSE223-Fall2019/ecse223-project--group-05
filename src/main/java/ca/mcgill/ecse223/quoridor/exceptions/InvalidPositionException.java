package ca.mcgill.ecse223.quoridor.exceptions;

/**
 * Having an actual exception for dealing with invalid positions opens up opportunities for these types of problems to be dealt with by systems calling methods.
 * This InvalidPositionException's purpose is to indicate when there are problems to do with loading positions. Whether it be the methods which load games
 * from files or whether it  be the validator method itself, should either operation detect an error in the provided data they should throw this Exception.
 * @author Edwin
 *
 */
public class InvalidPositionException extends Exception {
	public InvalidPositionException(String message) {
		super(message);
	}
}
