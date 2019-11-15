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

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.configuration.SaveConfig;
import ca.mcgill.ecse223.quoridor.enumerations.SavePriority;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.exceptions.InvalidPositionException;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
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
	 * Only the name of the file with its extension need to be provided. Returns false in the case of an IOException.
	 * @param game in the form of an active current game.
	 * @param filename in the form of the file's name and its extension, but not its path.
	 * @return
	 */
	public static SavingStatus saveGamePawnsAndWalls( Game game , String filename, SavePriority save_enforcement_type) {
		/*
		 * CANCELATION CHECK
		 */
		if(save_enforcement_type == SavePriority.DO_NOT_SAVE) {
			return SavingStatus.CANCELED;
		}
		
		
		/*
		 * FILE SYSTEM CHECK
		 */
		//First, check if the game already exists. If it does, then check if the user wants to overwrite it; inform them that it already exists if not.
		//If the game does not exist, but the operation is being used with FORCE_OVERWRITE as an argument, someone's not using this method properly.
		File file;
		if( filename.charAt(0) == 'C' || filename.charAt(0) == 'c' ) {
			file = new File(filename);
		} else {
			file = new File( SaveConfig.getGameSaveFilePath(filename) );
		}
		if( file.exists() ) {
			if( save_enforcement_type != SavePriority.FORCE_OVERWRITE ) {
				return SavingStatus.ALREADY_EXISTS;
			} 
		} else {
			if( save_enforcement_type == SavePriority.FORCE_OVERWRITE) {
				throw new IllegalArgumentException("Programmer has made improper use of save_enforcement_type. Did not check that the file already exists before using FORCE_OVERWRITE: Detected use of FORCE_OVERWRITE with a non-existing file.");
			}
		}
		
		
		/*
		 * GAME INSTANCE DATA CHECK
		 */
		String datacheck = "";
		datacheck = datacheck + "gameAsWhite : " + game.getWhitePlayer() + "\n";
		datacheck = datacheck + "gameAsBlack : " + game.getBlackPlayer() + "\n";
		datacheck = datacheck + "gamePositions : " + game.getPositions() + "\n";
		datacheck = datacheck + "currentPosition : " + game.getCurrentPosition() + "\n";
		datacheck = datacheck + "moves : " + game.getMoves() + "\n";
		if( game.getWhitePlayer() == null || game.getBlackPlayer() == null || game.getPositions() == null || game.getCurrentPosition() == null || game.getMoves() == null ) {
			throw new RuntimeException(datacheck);
		}
		
		
		/*
		 * FILE WRITING
		 */
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
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(line1);
			bufferedWriter.write(line2);
			bufferedWriter.close();
		} catch (IOException e) {
			return SavingStatus.FAILED;
		}
		
		//Success confirmation
		if( save_enforcement_type == SavePriority.FORCE_OVERWRITE ) {
			return SavingStatus.OVERWRITTEN;
		}
		return SavingStatus.SAVED;
	}
	
	/**
	 * Reads from the file system a file of provided filename written in sprint3-format data about
	 * pawn and wall positions. sprint3-format is unable to keep track of move ordering, so this is lost.
	 * But it does keep track of which player is to play next.
	 * All of this data which is read is then put into the provided instance of Game.
	 * @param filename of the save to load
	 * @param quoridor of the application
	 * @param game whose data will be replaced with that of the loaded file
	 * @param firstPlayer the player who moves first
	 * @param secondPlayer the player who moves second
	 * @return game provided but with loaded data
	 * @throws FileNotFoundException, IOException, InvalidPositionException
	 */
	public static Game loadGamePawnsAndWalls( String filename, Quoridor quoridor , Player firstPlayer, Player secondPlayer) throws FileNotFoundException, IOException, InvalidPositionException {
		
		/*
		 * Input Sanity Check
		 */
		if(quoridor==null) {
			throw new NullPointerException("Provided quoridor is null.");
		}
		if(firstPlayer==null) {
			throw new NullPointerException("Provided firstPlayer is null.");
		}
		if(secondPlayer==null) {
			throw new NullPointerException("Provided secondPlayer is null.");
		}
		
		
		
		/*
		 * Read Setup
		 */
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );	//Default behaviour: If we receive a file name, and not a path.
		if(!file.exists()) {			//If the filename behaviour doesn't work
			file = new File( filename );	//Try the filename-was-actually-a-path measure. If that doesn't work, we'll know soon enoguh from the try catch block next.
		}
		FileReader fileReader;
		try{
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			throw e;
		}
		
		
		
		/*
		 * Reading
		 */
		String[] lines = new String [2];
		try {
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			lines[0] = bufferedReader.readLine();
			lines[1] = bufferedReader.readLine();	//While hardcoding line1 and line2 isn't good for scalability, much of this code is going to be scrapped anyways once phase 2 begins. This is a temporary measure.
			bufferedReader.close(); 
		} catch (IOException e) {
			throw e;
		}
		
		
		
		/*
		 * PARSING SECTION
		 * Line 0 is for player 0; Line 1 is for player 1. By index, of course.
		 */
		
		//Figure out what color is first to play. We already who is first to play from function argument order.
		boolean blackIsNextToPlay = lines[0].charAt(0) == 'B';
		//We already know the players come in what order
		firstPlayer.setNextPlayer(secondPlayer);
		secondPlayer.setNextPlayer(firstPlayer);
		/*
		 * Set up Quoridor Board
		 * In case this is the first time Quoridor is being booted, we need to initialize the board.
		 */
		if(	QuoridorApplication.getQuoridor().getBoard() == null ) {
			Board board = new Board( QuoridorApplication.getQuoridor() );	//Creates a new board; also automatically links board to quoridor.
			for( int row = 1 ; row < 10 ; row ++ ) {
				for( int col = 1 ; col < 10 ; col ++ ) {
					board.addTile(row,col);									//Creates all 81 new tiles; also automatically links those new tiles to the board.
				}
			}
		}
		/*
		 * Setting up the Game instance
		 * Initializes a new Game instance and adds generalized instances it contains. This means adding walls and gameposition, but not players which will be defined once we know what color goes first.
		 */
		Game game = new Game( Game.GameStatus.Initializing , Game.MoveMode.PlayerMove , quoridor );
		if(blackIsNextToPlay) {
			game.setBlackPlayer(firstPlayer);
			PlayerPosition blackPosition = new PlayerPosition( firstPlayer, quoridor.getBoard().getTile( getTileId(1,5) ) );
			game.setWhitePlayer(secondPlayer);
			PlayerPosition whitePosition = new PlayerPosition( secondPlayer, quoridor.getBoard().getTile( getTileId(9,5) ) );
			GamePosition baseGamePosition = new GamePosition(0, whitePosition, blackPosition, firstPlayer, game);
			game.addPosition(baseGamePosition);
			game.setCurrentPosition(baseGamePosition);
		} else {
			game.setWhitePlayer(firstPlayer);
			PlayerPosition whitePosition = new PlayerPosition( firstPlayer, quoridor.getBoard().getTile( getTileId(9,5) ) );
			game.setBlackPlayer(secondPlayer);
			PlayerPosition blackPosition = new PlayerPosition( secondPlayer, quoridor.getBoard().getTile( getTileId(1,5) ) );
			GamePosition baseGamePosition = new GamePosition(0, whitePosition, blackPosition, firstPlayer, game);
			game.addPosition(baseGamePosition);
			game.setCurrentPosition(baseGamePosition);
		}
		/*
		 *      RESETING PLAYER WALL DATA
		 *      The players provided already have all of their walls. This needs to be overridden since we are completely replacing their data anyways.
		 */
		{
			List<Wall> firstPlayerOldWalls = new ArrayList<Wall>();
			firstPlayerOldWalls.addAll( firstPlayer.getWalls() );
			for(Wall wall: firstPlayerOldWalls) {
				( blackIsNextToPlay?game.getBlackPlayer():game.getWhitePlayer() ).removeWall(wall);
			}
			while(!firstPlayerOldWalls.isEmpty()) {
				firstPlayerOldWalls.remove(0).delete();
			}
		
			List<Wall> secondPlayerOldWalls = new ArrayList<Wall>();
			secondPlayerOldWalls.addAll( secondPlayer.getWalls() );
			for(Wall wall: secondPlayerOldWalls) {
				( !blackIsNextToPlay?game.getBlackPlayer():game.getWhitePlayer() ).removeWall(wall);
			}
			while(!secondPlayerOldWalls.isEmpty()) {
				secondPlayerOldWalls.remove(0).delete();
			}
		}
        //Adding the new walls
		if(blackIsNextToPlay) {
			for( int i = 0 ; i < 10 ; i ++ ) {
				Wall wall = new Wall( i , game.getBlackPlayer() );
				game.getCurrentPosition().addBlackWallsInStock( wall );
				game.getBlackPlayer().addWallAt(wall, i);
			}
			for( int i = 10 ; i < 20 ; i++ ) {
				Wall wall = new Wall( i , game.getWhitePlayer() );
				game.getCurrentPosition().addWhiteWallsInStock( wall );
				game.getWhitePlayer().addWallAt(wall, i-10);
			}
		} else {
			for( int i = 0 ; i < 10 ; i ++ ) {
				Wall wall = new Wall( i , game.getWhitePlayer() );
				game.getCurrentPosition().addWhiteWallsInStock( wall );
				game.getWhitePlayer().addWallAt(wall, i);
			}
			for( int i = 10 ; i < 20 ; i++ ) {
				Wall wall = new Wall( i , game.getBlackPlayer() );
				game.getCurrentPosition().addBlackWallsInStock( wall );
				game.getBlackPlayer().addWallAt(wall, i-10);
			}
		}
		
		//Parsing into data for each player (0: first player; 1: second player)
			//For reference, consider:	W: e9, e3v, d3v
			//			tens indices	000000000011111
			//			ones indices	012345678901234
		for( int i = 0 ; i < 2 ; i ++ ) {
			int playerPositionCol = lines[i].charAt(3) - 'a' + 1;
			int playerPositionRow = lines[i].charAt(4) - '1' + 1;
			sanityCheckTileCoordinate(playerPositionRow,playerPositionCol);
			if(i==0) {	//If first player
				if(blackIsNextToPlay) {		//If we're modifying the first playerPosition and first player is black
					game.getCurrentPosition().getBlackPosition().setTile( game.getQuoridor().getBoard().getTile( getTileId(playerPositionRow, playerPositionCol) ) );
				} else {					//If we're modifying the second playerPosition and second player is white
					game.getCurrentPosition().getWhitePosition().setTile( game.getQuoridor().getBoard().getTile( getTileId(playerPositionRow, playerPositionCol) ) );
				}
			} else {	//If second player
				if(blackIsNextToPlay) {		//If we're modifying the first playerPosition and first player is white
					game.getCurrentPosition().getWhitePosition().setTile( game.getQuoridor().getBoard().getTile( getTileId(playerPositionRow, playerPositionCol) ) );
				} else {					//If we're modifying the second playerPosition and second player is black
					game.getCurrentPosition().getBlackPosition().setTile( game.getQuoridor().getBoard().getTile( getTileId(playerPositionRow, playerPositionCol) ) );
				}
			}
			int wallsPlaced = (lines[i].length()-5)/5;
			for( int j = 0 ; j < wallsPlaced ; j++ ) {
				
				//Get previous wallMove for reference. It must be the most recent.
				Move prevMove;
				if( game.getMoves().size() == 0 ) {
					prevMove = null;
				} else {
					List<Move> allWallMoves = game.getMoves();
					prevMove = game.getMove( game.getMoves().size() - 1 );
					for( Move m : allWallMoves ) {
						if( m.getMoveNumber() > prevMove.getMoveNumber() ) {
							prevMove = m;
						}
					}
				}
				
				//Read in the j'th wall word for constructing the new WallMove
				int			wallCol = lines[i].charAt( 7 + 5*j ) - 'a' + 1;
				int			wallRow = lines[i].charAt( 8 + 5*j ) - '1' + 1;
				sanityCheckTileCoordinate(wallRow,wallCol);
				Direction	wallDir = (lines[i].charAt( 9 + 5*j ) == 'v') ? Direction.Vertical : Direction.Horizontal;
				//Collect remaining data for constructing the new WallMove
				int 		newMoveNumber 	= prevMove != null ? prevMove.getMoveNumber() + 1 : 1 ;
				int			newRoundNumber	= j + 1;
				Tile		targetTile		= game.getQuoridor().getBoard().getTile( getTileId(wallRow, wallCol) );
				//Move around walls so that a wall is taken from the stock and put into the on-the-board list for input as a placed wall in constructor.
				Wall		wall;
				if( i==0 && blackIsNextToPlay || i==1 && !blackIsNextToPlay ) {
					//we deal with the black player if we're dealing with the first player and the first player is black or we're dealing with the last player and the first player was white.
					wall = game.getCurrentPosition().getBlackWallsInStock( game.getCurrentPosition().getBlackWallsInStock().size()-1 );
					game.getCurrentPosition().removeBlackWallsInStock( wall );
					game.getCurrentPosition().addBlackWallsOnBoard(wall);
				} else {
					//we deal with the white player if we're dealing with the first player and the first player is white ore we're dealing with the last player and the first player was black. 
					wall = game.getCurrentPosition().getWhiteWallsInStock( game.getCurrentPosition().getWhiteWallsInStock().size()-1 );
					game.getCurrentPosition().removeWhiteWallsInStock(wall);
					game.getCurrentPosition().addWhiteWallsOnBoard(wall);
				}
				//Create the new WallMove
				WallMove newWallMove = new WallMove( newMoveNumber, newRoundNumber, (i==0)?firstPlayer:secondPlayer, targetTile, game, wallDir, wall);
				//Add the new WallMove into the game's moves
				if(prevMove != null) {
					prevMove.setNextMove(newWallMove);
				}
				newWallMove.setPrevMove(prevMove);
				game.addMove(newWallMove);
				
			}
		}
		
		
		
		/*
		 *	POST-PARSING VALIDATION SECTION
		 */
		//First check if the two pawns are on the same tile
		if( game.getCurrentPosition().getBlackPosition().getTile().getColumn() == game.getCurrentPosition().getWhitePosition().getTile().getColumn() ) {
			if( game.getCurrentPosition().getBlackPosition().getTile().getRow() == game.getCurrentPosition().getWhitePosition().getTile().getRow() ) {
				throw new InvalidPositionException("The players are on the same tile!");
			}
		}
		//Now check that all walls are not out of the bounds of the board
		for( Wall wall : game.getCurrentPosition().getBlackWallsOnBoard() ) {
			sanityCheckWallLocation(wall);
		}
		for( Wall wall : game.getCurrentPosition().getWhiteWallsOnBoard() ) {
			sanityCheckWallLocation(wall);
		}
		//Now check that none of the walls overlap each other.
		ArrayList<Wall> allPlacedWalls = new ArrayList<Wall>();
		allPlacedWalls.addAll( game.getCurrentPosition().getBlackWallsOnBoard() );
		allPlacedWalls.addAll( game.getCurrentPosition().getWhiteWallsOnBoard() );
		for( int i = 0 ; i < allPlacedWalls.size(); i++ ) {
			for( int j = i + 1 ; j < allPlacedWalls.size(); j++ ) {
				sanityCheckWallOverlap( allPlacedWalls.get(i), allPlacedWalls.get(j), allPlacedWalls );
			}
		}
		
		
		
		/*
		 * RETURN
		 */
		return game;
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
			playerTile = player.getGameAsWhite().getCurrentPosition().getWhitePosition().getTile();
		}
		if( playerIsBlack ) {
			playerWalls = player.getGameAsBlack().getCurrentPosition().getBlackWallsOnBoard();
		} else {
			playerWalls = player.getGameAsWhite().getCurrentPosition().getWhiteWallsOnBoard();
		}
		
		//Writing the player tile's coordinates first
		positionThenWalls = "" + columnIntToChar(playerTile.getColumn()) + playerTile.getRow() +",";
		//Writing all of the player's wall coordinates and orientation.
		for( Wall wall : playerWalls ) {
			positionThenWalls = positionThenWalls + " " + columnIntToChar(wall.getMove().getTargetTile().getColumn()) + wall.getMove().getTargetTile().getRow() + ( wall.getMove().getWallDirection() == Direction.Horizontal ? "h" : "v" ) + "," ;
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
	
	/**
	 * Checks if the game's currentPlayerToMove is black.
	 * @param game
	 * @return
	 */
	private static boolean currentPlayerIsBlack( Game game ) {
		return game.getCurrentPosition().getPlayerToMove().equals( game.getBlackPlayer() );
	}
	
	/**
	 * Returns TileId for a tile in the quoridor board given the row and col in integers.
	 * @param row
	 * @param col
	 * @return
	 */
	private static int getTileId( int row, int col ) {
		return (row-1)*9+col-1 ;
	}
	
	/**
	 * Checks if any of the input tiles coordinates are utterly invalid. this must be used directly in the parser to avoid IndexOutOfBoundsException's.
	 * @param row
	 * @param col
	 * @throws InvalidPositionException
	 */
	private static void sanityCheckTileCoordinate(int row, int col) throws InvalidPositionException {
		if( row < 1 || row > 9 || col < 1 || row > 9 ) {
			throw new InvalidPositionException("Detected invalid Tile Coordinates: (" + col + "," + row + "). This would be read as a \"" + (char)(col + 'a' -1) + (char)(row + '1' - 1) + "\" in the file.");
		}
	}
	
	/**
	 * Checks if the tile on which a wall is placed is acceptable.
	 * @param wall
	 * @throws InvalidPositionException
	 */
	private static void sanityCheckWallLocation( Wall wall ) throws InvalidPositionException {
		//Some walls have not been placed. This would be indicated through a null pointer for the Move attribute, which is needed for a wall to be placed.
		if( wall.getMove() == null ) {
			return;
		}
		//If the wall has been placed, then we check what tile it is located on. The rule is simple: if the wall is on the rightside or bottomside border, it is out of bounds.
		int tileRow = wall.getMove().getTargetTile().getRow();
		int tileCol = wall.getMove().getTargetTile().getColumn();
		if( tileRow == 9 && tileCol == 9 ) {	//If in the corner of bad-ness
			throw new InvalidPositionException("Detected invalid wall placement on the bottom-right edge, which is utterly unacceptable: (9,9).");
		} else if ( tileRow == 9 ) {			//If on the bottom edge
			throw new InvalidPositionException("Detected invalid wall placement on the bottom edge: ("+tileCol+",9).");
		} else if ( tileCol == 9 ) {			//If on the right edge
			throw new InvalidPositionException("Detected invalid wall placement on the right edge: (9,"+tileRow+")");
		}
		return;	//Not out of bounds if we reach the end.
	}
	
	/**
	 * Checks if the two provided tiles are overlapping.
	 * @param wall1
	 * @param wall2
	 * @throws InvalidPositionException
	 */
	private static void sanityCheckWallOverlap( Wall wall1, Wall wall2, List<Wall> allWalls ) throws InvalidPositionException {
		//If either of the walls are null, then they're not going to overlap lul.
		if( wall1.getMove() == null || wall2.getMove() == null ) {
			return;
		}
		//Aight no we're going through a list of possible ways walls can be overlapped, with the first wall as the master wall.
		WallMove wall1Move = (WallMove)wall1.getMove();
		WallMove wall2Move = (WallMove)wall2.getMove();
		int wall1row = wall1Move.getTargetTile().getRow();
		int wall2row = wall2Move.getTargetTile().getRow();
		int wall1col = wall1Move.getTargetTile().getColumn();
		int wall2col = wall2Move.getTargetTile().getColumn();
		if( wall1Move.getWallDirection() == Direction.Horizontal ) {
			if( wall2Move.getWallDirection() == Direction.Horizontal ) {
				//If both walls are Horizontal, then they can only intersect by being on the same row and close.
				if( wall1row != wall2row ) {
					//If they do not intersect as they are not on the same row
					return;
				}
				if( wall1col == wall2col - 1 || wall1col == wall2col || wall1col == wall2col + 1 ) {
					//If wall2 is 1 to the left, on, or 1 to the right of wall1, it is intersecting.
					throw new InvalidPositionException("Detected overlapping walls: ("+wall1col+","+wall1row+")h and ("+wall2col+","+wall2row+")h. Consider : \n" + listOfPlacedWallsToString(allWalls) );
				}
				
			} else {
				//If wall1 is horizontal and wall2 vertical, the only way they intersect is if they are on the same target tile.
				if( wall1row == wall2row && wall1col == wall2col ) {
					throw new InvalidPositionException("Detected overlapping walls: ("+wall1col+","+wall1row+")h and ("+wall2col+","+wall2row+")v. Consider : \n" + listOfPlacedWallsToString(allWalls) );
				} else {
					return;
				}
				
			}
		} else {
			if( wall2Move.getWallDirection() == Direction.Horizontal ) {
				//If wall1 is vertical and wall2 horizontal, the only way they intersect is by being on the same target tile.
				if( wall1row == wall2row && wall1col == wall2col ) {
					throw new InvalidPositionException("Detected overlapping walls: ("+wall1col+","+wall1row+")v and ("+wall2col+","+wall2row+")h. Consider : \n" + listOfPlacedWallsToString(allWalls) );
				} else {
					return;
				}
				
			} else {
				//If both walls are Vertical, then they can only intersect by being on the same column and close.
				if( wall1col != wall2col ) {
					//If they do not intersect as they are not on the same column
					return;
				}
				if( wall1row == wall2row - 1 || wall1row == wall2row || wall1row == wall2row + 1 ) {
					//If wall1 is 1 above, on, or 1 below wall2, it is intersecting
					throw new InvalidPositionException("Detected overlapping walls: ("+wall1col+","+wall1row+")v and ("+wall2col+","+wall2row+")v. Consider : \n" + listOfPlacedWallsToString(allWalls) );
				}
				
			}
		}
	}
	
	private static String listOfPlacedWallsToString( List<Wall> walls ) {
		String str = "";
		for( Wall wall : walls ) {
			str = str + "(" + wall.getMove().getTargetTile().getColumn() + "," + wall.getMove().getTargetTile().getRow() + ")\n" ;
		}
		return str;
	}
	
}
