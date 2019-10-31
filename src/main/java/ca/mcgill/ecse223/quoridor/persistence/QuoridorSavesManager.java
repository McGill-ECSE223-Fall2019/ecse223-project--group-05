package ca.mcgill.ecse223.quoridor.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.Tile;
import ca.mcgill.ecse223.quoridor.model.Wall;
import ca.mcgill.ecse223.quoridor.model.WallMove;

/**
 * API for saving and loading currentGames. Although the scope of the project requirements only require
 * that this class only provide a translation layer of player positions and wall positions to and from
 * text files, this manager may be able to do more later.
 * @author Edwin Pan
 *
 */
public class QuoridorSavesManager {
	
	/**
	 * Writes into the file system sprint3-format data about the black and white pawns and walls. 
	 * Only the name of the file with its extension need to be provided. Returns false with IOException.
	 * @param game
	 * @param filename
	 * @return
	 */
	public static boolean saveGamePawnsAndWalls( Game game , String filename) {
		//Dual line setup
		String line1 = "";
		String line2 = "";
		//Getting the descriptor lines for Black and White pawns and walls
		String blackPosition = "B: " + sprint3FormattedPawnsAndWalls( game.getBlackPlayer() );
		String whitePosition = "W: " + sprint3FormattedPawnsAndWalls( game.getWhitePlayer() );
		//Setting the descriptor lines into the proper order
		if( currentPlayerIsBlack(game) ) {
			line1 = blackPosition + "\n";
			line2 = whitePosition;
		} else {
			line1 = whitePosition + "\n";
			line2 = blackPosition;
		}
		
		//File System Writing
		File file = new File(filename);
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(line1);
			bufferedWriter.write(line2);
			bufferedWriter.close();
		} catch (IOException e) {
			return false;
		}
		
		//Success confirmation
		return true;
	}
	
	/**
	 * Reads from the file system a file of provided filename written in sprint3-format data about
	 * pawn and wall positions. sprint3-format is unable to keep track of move ordering, so this is lost.
	 * But it does keep track of which player is to play next.
	 * All of this data which is read is then put into the provided instance of Game, replacing its
	 * current position's pawn positions and wall positions.
	 * @param filename
	 * @param game
	 * @return
	 * @throws FileNotFoundException, IOException
	 */
	public static Game loadGamePawnsAndWalls( String filename, Game game ) throws FileNotFoundException, IOException {
		//Read setup
		String line1;
		String line2;
		File file = new File(filename);
		FileReader fileReader;
		try{
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			throw e;
		}
		//Reading
		try {
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			line1 = bufferedReader.readLine();
			line2 = bufferedReader.readLine();	//While hardcoding line1 and line2 isn't good for scalability, much of this code is going to be scrapped anyways once phase 2 begins. This is a temporary measure.
			bufferedReader.close(); 
		} catch (IOException e) {
			throw e;
		}
		//Parsing
		boolean blackIsNextToPlay = line1.charAt(0) == 'B';
		throw new UnsupportedOperationException();
		//TODO
	}
	
	/**
	 * Produces the sprint3-format string characterizing a player's pawn coordinates and wall coordinates.
	 * @param player
	 * @return String ready for text file representation of position in sprint3.
	 */
	private static String sprint3FormattedPawnsAndWalls( Player player ) {
		//Method variables
		String positionThenWalls = "";
		
		//Shortcuts for referencing either to items with coordinate data or that reference those with coords.
		boolean playerIsBlack;
		Tile playerTile;
		List<Wall> playerWalls;
		
		//Obtaining the player's tile and the player's walls
		if( player.getGameAsBlack() == null ) {
			playerIsBlack = false;
		} else {
			playerIsBlack = true;
		}
		if( playerIsBlack ) {
			playerTile = player.getGameAsBlack().getCurrentPosition().getBlackPosition().getTile();
		} else {
			playerTile = player.getGameAsBlack().getCurrentPosition().getWhitePosition().getTile();
		}
		if( playerIsBlack ) {
			playerWalls = player.getGameAsBlack().getCurrentPosition().getBlackWallsOnBoard();
		} else {
			playerWalls = player.getGameAsWhite().getCurrentPosition().getWhiteWallsOnBoard();
		}
		
		//Writing the player tile's coordinates first
		positionThenWalls = "" + playerTile.getRow() + columnIntToChar(playerTile.getColumn()) +",";
		//Writing all of the player's wall coordinates and orientation.
		for( Wall wall : playerWalls ) {
			positionThenWalls = positionThenWalls + " " + wall.getMove().getTargetTile().getRow() + columnIntToChar(wall.getMove().getTargetTile().getColumn()) + ( wall.getMove().getWallDirection() == Direction.Horizontal ? "h" : "v" ) + "," ;
		}
		positionThenWalls = positionThenWalls.trim();
		//Getting rid of the extra ',' at the end, if present
		if( positionThenWalls.charAt( positionThenWalls.length()-1) == ',' ) {
			positionThenWalls = positionThenWalls.substring(0,positionThenWalls.length()-1);
		}
		
		//Return the string of the player's position coordinates and wall coordinates.
		return positionThenWalls;
	}
	
	/**
	 * Helper method which translates column integer coordinates into their char equivalent.
	 * @param column coordinate (interval [1,9])
	 * @return char equivalent (interval [a,i]) though '0' is outputed for bad inputs.
	 */
	private static char columnIntToChar(int column) {
		switch (column) {
			case 1:
				return 'a';
			case 2:
				return 'b';
			case 3:
				return 'c';
			case 4:
				return 'd';
			case 5:
				return 'e';
			case 6:
				return 'f';
			case 7:
				return 'g';
			case 8:
				return 'h';
			case 9:
				return 'i';
			default:
				return '0';
		}
	}
	
	private static boolean currentPlayerIsBlack( Game game ) {
		return game.getCurrentPosition().getPlayerToMove().equals( game.getBlackPlayer() );
	}
	
	/**
	 * Returns whether or not the Black Player is first based on moves.
	 * If no moves have been made in the game, without GUI elements there is no way
	 * to tell who is the first player to move in that game. As such, we default to the
	 * chess standard - that is, the White Player moves first and the Black Player moves second.
	 * @param game
	 * @return
	 */
	private static boolean firstPlayerIsBlack( Game game ) {
		GamePosition firstGamePosition = game.getPositions().get(0);
		for( GamePosition position : game.getPositions() ) {
			if( position.getId() < firstGamePosition.getId() ) {
				firstGamePosition = position;
			}
		}
		return firstGamePosition.getBlackPosition().getPlayer().equals( firstGamePosition.getPlayerToMove());
	}
	
	/**
	 * Returns the move with move number 1, which should be the first.
	 * If there have been no moves, then it returns null.
	 * @param game
	 * @return
	 */
	private static Move getFirstMove( Game game ) {
		List<Move> allMoves = game.getMoves();
		if( allMoves.size() == 0 ) {
			return null;
		}
		for( Move move : allMoves ) {
			if( move.getMoveNumber() == 1 ) {
				return move;
			}
		}
		return null;
	}
	
	/**
	 * Returns an ArrayList of all of the White Player's moves.
	 * @param game
	 * @return
	 */
	private static ArrayList<Move> getWhitePlayerMoves( Game game ) {
		Player whitePlayer = game.getWhitePlayer();
		ArrayList<Move> whiteMoves = new ArrayList<Move>();
		Move movePosition = getFirstMove(game);
		while( movePosition != null ) {
			if( movePosition.getPlayer().getUser().getName().equals( whitePlayer.getUser().getName() ) ) {
				whiteMoves.add(movePosition);
			}
			movePosition = movePosition.getNextMove();
		}
		return whiteMoves;
	}
	
	/**
	 * Returns an ArrayList of all of the Black Player's moves.
	 * @param game
	 * @return
	 */
	private static ArrayList<Move> getBlackPlayerMoves( Game game ) {
		Player blackPlayer = game.getBlackPlayer();
		ArrayList<Move> blackMoves = new ArrayList<Move>();
		Move movePosition = getFirstMove(game);
		while( movePosition != null ) {
			if( movePosition.getPlayer().getUser().getName().equals( blackPlayer.getUser().getName() ) ) {
				blackMoves.add(movePosition);
			}
			movePosition = movePosition.getNextMove();
		}
		return blackMoves;
	}
	
	
}
