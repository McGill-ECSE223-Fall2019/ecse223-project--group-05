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
import ca.mcgill.ecse223.quoridor.controller.PawnBehaviour;
import ca.mcgill.ecse223.quoridor.enumerations.SavePriority;
import ca.mcgill.ecse223.quoridor.enumerations.SavingStatus;
import ca.mcgill.ecse223.quoridor.exceptions.InvalidPositionException;
import ca.mcgill.ecse223.quoridor.model.Board;
import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.model.Game;
import ca.mcgill.ecse223.quoridor.model.Game.GameStatus;
import ca.mcgill.ecse223.quoridor.model.Game.MoveMode;
import ca.mcgill.ecse223.quoridor.model.GamePosition;
import ca.mcgill.ecse223.quoridor.model.JumpMove;
import ca.mcgill.ecse223.quoridor.model.Move;
import ca.mcgill.ecse223.quoridor.model.MoveDirection;
import ca.mcgill.ecse223.quoridor.model.Player;
import ca.mcgill.ecse223.quoridor.model.PlayerPosition;
import ca.mcgill.ecse223.quoridor.model.Quoridor;
import ca.mcgill.ecse223.quoridor.model.StepMove;
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
	 * Writes into the file system sprint3 and sprint5 desired types of saves. That is, it saves into two files: .dat files and .mov files - not to be confused with the movie format lul.
	 * The Filename SHOULD NOT INCLUDE AN EXTENSION. If .dat is provided, then only the .dat will be saved; if a .mov is provided, then only the .mov will be saved. If no extension is
	 * provided, then both will be saved; and if an invalid extension is provided, it will be removed and a .dat and .mov will be saved.
	 * @param filename in the form of the file's name and DISCOURAGEDLY its extension, but not its path.
	 * @return
	 */
	public static SavingStatus saveGame( Game game , String filename, SavePriority save_enforcement_type) {
		/*
		 * CANCELATION CHECK
		 */
		if(save_enforcement_type == SavePriority.DO_NOT_SAVE) {
			return SavingStatus.CANCELED;
		}
		
		
		/*
		 * EXTENSION CHECK: Check if .dat or .mov were provided. If they were, save only of the provided type. Otherwise set the booleans to have us save both.
		 * Also checks if we have garbage input.
		 */
		boolean saveDatRequired = true;
		boolean saveMovRequired = true;
		String baseFilename = filename.trim();
		boolean hasDefinedExtension = (baseFilename.lastIndexOf(".") != -1);
		if(hasDefinedExtension) {
			if( baseFilename.substring( baseFilename.lastIndexOf(".") ).equals( SaveConfig.gameMovesExtension) ){
				saveDatRequired = false;
			} else if( baseFilename.substring( baseFilename.lastIndexOf(".") ).equals( SaveConfig.gamePositionExtension ) ) {
				saveMovRequired = false;
			}
			baseFilename = baseFilename.substring( 0 , baseFilename.lastIndexOf(".") );
		}
		
		
		/*
		 * OVERWRITE CHECK: Pulls out if the file already exists and we don't have an overwrite order.
		 */
		//First, check if the game already exists. If it does, then check if the user wants to overwrite it; inform them that it already exists if not.
		//If the game does not exist, but the operation is being used with FORCE_OVERWRITE as an argument, someone's not using this method properly.
		if( saveDatRequired ) {
			File file = new File( SaveConfig.getGameSaveFilePath( baseFilename + SaveConfig.gamePositionExtension ) );
			if( file.exists() ) {
				if( save_enforcement_type != SavePriority.FORCE_OVERWRITE ) {
				return SavingStatus.ALREADY_EXISTS;
				} 
			} else {
				if( save_enforcement_type == SavePriority.FORCE_OVERWRITE) {
					throw new IllegalArgumentException("Programmer has made improper use of save_enforcement_type. Did not check that the file already exists before using FORCE_OVERWRITE: Detected use of FORCE_OVERWRITE with a non-existing file.");
				}
			}
		}
		if( saveMovRequired ) {
			File file = new File( SaveConfig.getGameSaveFilePath( baseFilename + SaveConfig.gameMovesExtension ) );
			if( file.exists() ) {
				if( save_enforcement_type != SavePriority.FORCE_OVERWRITE ) {
				return SavingStatus.ALREADY_EXISTS;
				} 
			} else {
				if( save_enforcement_type == SavePriority.FORCE_OVERWRITE) {
					throw new IllegalArgumentException("Programmer has made improper use of save_enforcement_type. Did not check that the file already exists before using FORCE_OVERWRITE: Detected use of FORCE_OVERWRITE with a non-existing file.");
				}
			}
		}
		
		
		/*
		 * INPUT-GAME SANITY CHECK: Checks if the game is empty and therefore unsaveable. 
		 */
		if( game.getWhitePlayer() == null || game.getBlackPlayer() == null || game.getPositions() == null || game.getCurrentPosition() == null || game.getMoves() == null ) {
			String datacheck = "";
			datacheck = datacheck + "gameAsWhite : " + game.getWhitePlayer() + "\n";
			datacheck = datacheck + "gameAsBlack : " + game.getBlackPlayer() + "\n";
			datacheck = datacheck + "gamePositions : " + game.getPositions() + "\n";
			datacheck = datacheck + "currentPosition : " + game.getCurrentPosition() + "\n";
			datacheck = datacheck + "moves : " + game.getMoves() + "\n";
			throw new RuntimeException(datacheck);
		}
		
		
		/*
		 * FILE WRITER CALLS
		 */
		//Variable for checking how things went
		SavingStatus saveStatusDat = SavingStatus.SAVED;
		SavingStatus saveStatusMov = SavingStatus.SAVED;
		//Actual saving
		if( saveDatRequired ) {
			File file = new File( SaveConfig.getGameSaveFilePath( baseFilename + SaveConfig.gamePositionExtension ) );
			saveStatusDat = savePosition(game, file , save_enforcement_type);
		}
		if( saveMovRequired ) {
			File file = new File( SaveConfig.getGameSaveFilePath( baseFilename + SaveConfig.gameMovesExtension ) );
			saveStatusMov = saveMoves(game, file , save_enforcement_type);
		}
		//Checking how things went and reporting on it.
		if( saveStatusDat == SavingStatus.FAILED || saveStatusMov == SavingStatus.FAILED ) {
			return SavingStatus.FAILED;
		} else if( saveStatusDat == SavingStatus.OVERWRITTEN || saveStatusMov == SavingStatus.OVERWRITTEN ) {
			return SavingStatus.OVERWRITTEN;
		} else {
			return SavingStatus.SAVED;
		}
		
	}
	
	/**
	 * Saves the items as required in sprint3: that is, just the most recent state of the board in the form of the positions of pawns and walls. That's it.
	 * @param game
	 * @param file
	 * @param save_enforcement_type
	 * @return
	 */
	private static SavingStatus savePosition( Game game , File file, SavePriority save_enforcement_type) {
		/*
		 * Output String Parsing
		 */
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
		
		/*
		 * Output File Writing
		 */
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(line1);
			bufferedWriter.write(line2);
			bufferedWriter.close();
		} catch (IOException e) {
			return SavingStatus.FAILED;
		}

		/*
		 * Success Responding
		 */
		if( save_enforcement_type == SavePriority.FORCE_OVERWRITE ) {
			return SavingStatus.OVERWRITTEN;
		}
		return SavingStatus.SAVED;
	}
	
	/**
	 * Saves the items as required in sprint5: that is, just an ordered list of all the moves that have taken place in the game, but no information about the players that made it (colour is implied; but user are ignored.)
	 * @param game
	 * @param file
	 * @param save_enforcement_type
	 * @return
	 */
	private static SavingStatus saveMoves( Game game , File file, SavePriority save_enforcement_type) {
		/*
		 * Output String Parsing
		 */
		//Instantiate useful variables.
		String lines = "";				//We'll use this to temporarily store the entire contents of the file in memory before writing into the file system.
		String whitePosition = "e9";	//We'll use this to keep track of the current white position
		String blackPosition = "e1";	//We'll use this to keep track of the current black position
		//Instantiate the list of all wall moves, since we need to differentiate between wall moves and other moves.
		ArrayList<WallMove> allWallMoves = new ArrayList<WallMove>();
		for( Wall blackWall: game.getCurrentPosition().getBlackWallsOnBoard() ) {
			allWallMoves.add(blackWall.getMove());
		}
		for( Wall whiteWall: game.getCurrentPosition().getWhiteWallsOnBoard() ) {
			allWallMoves.add(whiteWall.getMove());
		}
		//String building
		int totalMoves = game.getMoves().size();
		for( int i = 1 ; i <= totalMoves ; i++ ) {
			
			Move move = game.getMove(i-1);
			
			//While we don't need to differentiate between Step and Jump move, we do need to keep check of WallMove. Here, we check if we have a wallmove.
			boolean isWallMove = false;
			for( WallMove wallMove : allWallMoves ) {
				if( wallMove.getMoveNumber() == move.getMoveNumber() ) {
					isWallMove = true;
					break;
				}
			}
			
			//Now we need to know whether or not to be referring to whitePosition or blackPosition for initial coordinates in the case of a non-wallMove.
			boolean isBlackPawnMove = i % 2 == 0;
			
			//Now we build the text of our textfile into the String lines.
			if( isWallMove ) {	//If we're saving a wall move.
				//Get the string version of the wall's position
				String wallPosition = "";
				wallPosition = wallPosition + columnIntToChar(move.getTargetTile().getColumn());
				wallPosition = wallPosition + move.getTargetTile().getRow();
				wallPosition = wallPosition + ( ((WallMove)move).getWallDirection() == Direction.Horizontal ? "h" : "v" );
				//Write the new line.
				lines = lines + i + "." + wallPosition + "\n";
				
			} else {			//If we're saving a pawn move.
				//Figure out what the second argument of this new line is.
				String currentPosition = isBlackPawnMove ? blackPosition : whitePosition ;
				//Figure out what the third argument of this new line is.
				String nextPosition = "";
				nextPosition = nextPosition + columnIntToChar(move.getTargetTile().getColumn());
				nextPosition = nextPosition + move.getTargetTile().getRow();
				//Write the new line
				lines = lines + i + "." + currentPosition + " " + nextPosition +"\n";
				//Update the currentPosition of the desired pawn.
				if(isBlackPawnMove) {
					blackPosition = nextPosition;
				} else {
					whitePosition = nextPosition;
				}
				
			}
			
		}
		
		/*
		 * Output File Writing
		 */
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(lines);
			bufferedWriter.close();
		} catch (IOException e) {
			return SavingStatus.FAILED;
		}
		
		/*
		 * Success Responding
		 */
		if( save_enforcement_type == SavePriority.FORCE_OVERWRITE ) {
			return SavingStatus.OVERWRITTEN;
		}
		return SavingStatus.SAVED;
	}
	
	/**
	 * Loads a saved game. Behaviour differs based on the extension of the file provided. If it's a .dat, it will load the game position; if it's a .mov, it will load the game
	 * position with its history. Important to note is that data of who were the users of each game aren't saved, so you will have to provide the players for this game.
	 * Note that if neither the .dat or the .mov extension provided, no attempt will be made to open a file.
	 * @param filename of the save to load
	 * @param quoridor of the application
	 * @param game whose data will be replaced with that of the loaded file
	 * @param firstPlayer the white player which moves first
	 * @param secondPlayer the black player which moves second
	 * @return game provided but with loaded data
	 * @throws FileNotFoundException, IOException, InvalidPositionException, IllegalArgumentException
	 */
	public static Game loadGame( String filename, Quoridor quoridor, Player firstPlayer, Player secondPlayer ) throws FileNotFoundException, IOException, InvalidPositionException {
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
		 * Check of what type of save is being read.
		 */
		boolean isDatFile = false;
		boolean isMovFile = false;
		if( filename.trim().substring( filename.lastIndexOf(".") ).equals( SaveConfig.gamePositionExtension) ){
			isDatFile = true;
		} else if( filename.trim().substring( filename.lastIndexOf(".") ).equals( SaveConfig.gameMovesExtension ) ) {
			isMovFile = true;
		}
		
		/*
		 * Execute either the .dat loader or the .mov loader.
		 */
		if(isDatFile) {
			return loadPosition(filename, quoridor, firstPlayer, secondPlayer);
		} else if(isMovFile) {
			return loadMoves(filename, quoridor, firstPlayer, secondPlayer);
		} else {
			throw new IllegalArgumentException("Unknown filetype detected. Loadgame aborted.");
		}
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
	public static Game loadPosition( String filename, Quoridor quoridor , Player firstPlayer, Player secondPlayer) throws FileNotFoundException, IOException, InvalidPositionException {
		
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
			for( int i = 1 ; i <= 10 ; i ++ ) {
				Wall wall = new Wall( i , game.getBlackPlayer() );
				game.getCurrentPosition().addBlackWallsInStock( wall );
				game.getBlackPlayer().addWallAt(wall, i);
			}
			for( int i = 11 ; i <= 20 ; i++ ) {
				Wall wall = new Wall( i , game.getWhitePlayer() );
				game.getCurrentPosition().addWhiteWallsInStock( wall );
				game.getWhitePlayer().addWallAt(wall, i-10);
			}
		} else {
			for( int i = 1 ; i <= 10 ; i ++ ) {
				Wall wall = new Wall( i , game.getWhitePlayer() );
				game.getCurrentPosition().addWhiteWallsInStock( wall );
				game.getWhitePlayer().addWallAt(wall, i);
			}
			for( int i = 11 ; i <= 20 ; i++ ) {
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
			sanityCheckTileCoordinates(playerPositionRow,playerPositionCol);
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
				sanityCheckTileCoordinates(wallRow,wallCol);
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
		validateCurrentGamePosition(game);
		
		
		
		/*
		 * RETURN
		 */
		return game;
	}
	
	
	/**
	 * Reads from the provided .mov file the moves of the game and builds the game from start to most recent state manually. Essentially, simulates things.
	 * @param filename of the save to load
	 * @param quoridor of the application
	 * @param game whose data will be replaced with that of the loaded file
	 * @param firstPlayer the player who moves first
	 * @param secondPlayer the player who moves second
	 * @return game provided but with loaded data
	 * @throws FileNotFoundException, IOException, InvalidPositionException
	 */
	public static Game loadMoves( String filename, Quoridor quoridor , Player firstPlayer, Player secondPlayer) throws FileNotFoundException, IOException, InvalidPositionException {
		
		/*
		 * Read Setup
		 */
		File file = new File( SaveConfig.getGameSaveFilePath(filename) );	//Default behaviour: If we receive a file name, and not a path.
		if(!file.exists()) {			//If the filename behaviour doesn't work
			file = new File( filename );	//Try the filename-was-actually-a-path measure. If that doesn't work, we'll know soon enoguh from the try catch block next.
		}
		BufferedReader bufferedReader;
		try{
			bufferedReader = new BufferedReader( new FileReader(file) );
		} catch (FileNotFoundException e) {
			throw e;
		}
		
		/*
		 * Game setup
		 * Set up the game to be everything it should be at the start of the game.
		 */
		Game game = initializeGame(file,quoridor,firstPlayer,secondPlayer);
		validateCurrentGamePosition(game);
		
		/*
		 * PawnBehaviours setup
		 * Set up PawnBehaviours for the game to use
		 */
		QuoridorApplication.clearWhitePawnBehaviour();
		QuoridorApplication.clearBlackPawnBehaviour();
		
		/*
		 * Game Simulating
		 * Go through each individual line in the save file which each represent a move and re-simulate the game.
		 */
		String bufferedLine = bufferedReader.readLine();
		boolean whiteTurn = true;
		int moveNumber = 1;			//it's the moveNumber'th move for each player
		int roundNumber = 1;		//it's the number of the round within the round... so like, roundNumber 1 is always white; roundNumber 2 is always black.
		while( bufferedLine != null && !bufferedLine.equals("") ) {
			//Every line creates a new game position and a new step. We first create each new game position as a duplicate of the previous game position, add it to game, and then make edits; then we add the step to game.
			//Of course, the first thing we must do before anything is confirm that the move is valid.
			
			//Get the arguments of the line.
			String[] arguments = bufferedLine.split(" ");
			
			//First figure out what kind of move we are dealing with by pre-reading the data.
			//We also complete INITIAL SANITY CHECKS to make sure the numbers that are coming in make sense.
			boolean isWallPlace = (arguments.length == 2);
			boolean isBasicStep = false;
			if(!isWallPlace) {
				//We're going to figure out if we're dealing with a pawn step or a pawn jump.
				int currentCol = (whiteTurn?game.getCurrentPosition().getWhitePosition():game.getCurrentPosition().getBlackPosition()).getTile().getColumn();
				int currentRow = (whiteTurn?game.getCurrentPosition().getWhitePosition():game.getCurrentPosition().getBlackPosition()).getTile().getRow();
				int initialCol = getColumn( arguments[1] );
				int initialRow = getRow( arguments[1] );
				int finalCol = getColumn( arguments[2] );
				int finalRow = getRow( arguments[2] );
				
				//SANITY CHECK: Tile Coordinates for steps
				sanityCheckTileCoordinates(initialRow,initialCol);
				sanityCheckTileCoordinates(finalRow,finalCol);
				
				//SANITY CHECK: the tile for this player in gamePosition should be the same as the initial position for the move in the file.
				if( currentCol != initialCol || currentRow != initialRow ) {
					throw new InvalidPositionException(
							"Move and Position arguments do not match. Attempted to move out of a tile that the pawn wasn't already in. \n "
							+ "See: \"" + bufferedLine + "\". \n " 
							+ "Consider that the current position is " + ((char)(currentCol + 'a' - 1)) + currentRow 
							+ " and it is currently " + (whiteTurn?"white player's":"black player's") + " turn.\n"
							+ "Further consider that it is currrent moveNo " + moveNumber + " and roundNo " + roundNumber + ".");
				}
				
				//SANITY CHECK: the areas that the pawn can ever legitimately jump to form a diamond around it. We will check that the final destination is in this area.
				int deltaCol = initialCol - finalCol;
				int deltaRow = initialRow - finalRow;
				if( Math.abs(deltaCol) + Math.abs(deltaRow) > 2 ) {
					throw new InvalidPositionException("Attempted move is not possible. See : " + bufferedLine);
				}
				
				//Check if pawnStep or pawnJump: isBasicStep is by default false so we need to know if it's a basic step (pawnStep).
				if( Math.abs(deltaCol) == 1 && deltaRow == 0 ) {
					isBasicStep = true;
				} else if ( Math.abs(deltaRow) == 1 && deltaCol == 0 ) {
					isBasicStep = true;
				}
				
			}
			//Note that wall sanity checks are specific to wall instances, and will therefore be checked once we actually start adding things in.
			
			//Now that we know what kind of moves we are dealing with, we now proceed to instantiate the new GamePosition based on the previous one.
			game.setCurrentPosition( 
					duplicateGamePosition( game.getCurrentPosition(), game,
							(whiteTurn?game.getWhitePlayer():game.getBlackPlayer()) ) 
					);
			
			//Now we make adjustments to the current GamePosition based on the current bufferedLine and simultaneously complete some additional Sanity Checks.
			//We also need to simultaneously add the new moves to our list of moves in order.
			GamePosition currentPosition = game.getCurrentPosition();
			if(isWallPlace) {
				//Set up all of the new stuff needed for the wall and put it into the current position.
				Tile targetTile = game.getQuoridor().getBoard().getTile( getTileId( getRow(arguments[1]) , getColumn(arguments[1]) ) );
				Direction direction = ( arguments[1].charAt(2) == 'h' ? Direction.Horizontal : Direction.Vertical );
				Player player = ( whiteTurn? game.getWhitePlayer(): game.getBlackPlayer() );
				if(!( whiteTurn? currentPosition.hasWhiteWallsInStock(): currentPosition.hasBlackWallsInStock() )) {
					int whiteInStock = currentPosition.numberOfWhiteWallsInStock();
					int whiteOnBoard = currentPosition.numberOfWhiteWallsOnBoard();
					int blackInStock = currentPosition.numberOfBlackWallsInStock();
					int blackOnBoard = currentPosition.numberOfBlackWallsOnBoard();
					throw new InvalidPositionException( (whiteTurn?"white":"black") +
							" player has no walls in stock! Cannot place another wall.\n" +
							"white has " + whiteInStock + " in stock and " + whiteOnBoard + " on board, and\n" +
							"black has " + blackInStock + " in stock and " + blackOnBoard + " on board.");
				}
				Wall wall = ( whiteTurn? currentPosition.getWhiteWallsInStock(0): currentPosition.getBlackWallsInStock(0) );
				WallMove wallMove = new WallMove( moveNumber, roundNumber, player, targetTile, game, direction, wall );
				//Put the wall into the current game position.
				if(whiteTurn) {
					currentPosition.removeWhiteWallsInStock(wall);
					currentPosition.addWhiteWallsOnBoard(wall);
				} else {
					currentPosition.removeBlackWallsInStock(wall);
					currentPosition.addBlackWallsOnBoard(wall);
				}
				//Having the WallMove, add it into the game.
				game.addMove(wallMove);
				
			} else if (isBasicStep) {
				//IMPORTANT TO NOTE: stepMoves interact with the PawnBehaviour state machines.
				//First, we need to figure out what direction the step is making our pawn move.
				MoveDirection direction = getStepDirection(getColumn(arguments[1]),getRow(arguments[1]),getColumn(arguments[2]),getRow(arguments[2]));
				if(direction==null) {
					throw new InvalidPositionException("IMPLEMENTATION ERROR FOR STEPMOVE CREATION; THIS CLAUSE SHOULD BE UNREACHABLE. Going from " + arguments[1] + " to " + arguments[2] + ".");
				}
				PawnBehaviour pawnStateMachine = ( whiteTurn? QuoridorApplication.getWhitePawnBehaviour(firstPlayer) : QuoridorApplication.getBlackPawnBehaviour(secondPlayer) );
				try{
					pawnStateMachine.isLegalStep(direction);
				} catch (IllegalArgumentException e) {
					throw new InvalidPositionException(
							"Detected illegal step.\n"+
							"Step direction inputted is " + direction + " " +
							"with initial tile " + arguments[1] + " and target tile " + arguments[2] + ".");
				}
				//Update the state machine
				pawnStateMachine.move(direction);
				//Add the step move to the game
				Tile targetTile = game.getQuoridor().getBoard().getTile( getTileId( getRow(arguments[2]) ,getColumn(arguments[2])) );
				StepMove stepMove = new StepMove( moveNumber, roundNumber, (whiteTurn?firstPlayer:secondPlayer), targetTile, game );
				game.addMove(stepMove);
				//Update the position of the pawn
				PlayerPosition playerPosition = (whiteTurn? currentPosition.getWhitePosition() : currentPosition.getBlackPosition() );
				playerPosition.setTile( targetTile );
				
			} else /* isJumpStep */ {
				//IMPORTANT TO NOTE: jumpMoves interact with the PawnBehaviour state machines.
				//First we need to figure out what direction the jump is making our pawn move.
				MoveDirection direction = getJumpDirection(getColumn(arguments[1]),getRow(arguments[1]),getColumn(arguments[2]),getRow(arguments[2]));
				if(direction==null) {
					throw new InvalidPositionException("IMPLEMENTATION ERROR FOR JUMPMOVE CREATION; THIS CLAUSE SHOULD BE UNREACHABLE. Going from " + arguments[1] + " to " + arguments[2] + ".");
				}
				PawnBehaviour pawnStateMachine = ( whiteTurn? QuoridorApplication.getWhitePawnBehaviour(firstPlayer) : QuoridorApplication.getBlackPawnBehaviour(secondPlayer) );
				try{
					pawnStateMachine.isLegalJump(direction);
				} catch (IllegalArgumentException e) {
					throw new InvalidPositionException(
							"Detected illegal jump."+
							"Step direction inputted is " + direction + " " +
							"with initial tile " + arguments[1] + " and target tile " + arguments[2] + ".");
				}
				//Update the state machine
				pawnStateMachine.jump(direction);
				//Add the step move to the game
				Tile targetTile = game.getQuoridor().getBoard().getTile( getTileId( getRow(arguments[2]) ,getColumn(arguments[2])) );
				JumpMove jumpMove = new JumpMove( moveNumber, roundNumber, (whiteTurn?firstPlayer:secondPlayer), targetTile, game );
				game.addMove(jumpMove);
				//Update the position of the pawn
				PlayerPosition playerPosition = (whiteTurn? currentPosition.getWhitePosition() : currentPosition.getBlackPosition() );
				playerPosition.setTile( targetTile );
				
			}
			
			//Now we complete final sanity checks to do with conflicts between walls and player positions.
			validateCurrentGamePosition(game);
			
			//Now we update the while loop parametres.
			bufferedLine = bufferedReader.readLine();
			if(whiteTurn) {
				whiteTurn = !whiteTurn;
				roundNumber = 2;
			} else {
				moveNumber++;
				whiteTurn = !whiteTurn;
				roundNumber = 1;
			}
			
		} //File reading complete.
		bufferedReader.close();
		
		/*
		 * Game State Checker
		 * Now that we have loaded in all the moves into the game, we now need to check what state the game is in.
		 * Note that all we can analyze is whether or not either player has won. If neither player has won, then the game will be "running".
		 * Note that we have 0 control over GUI-related states such as ReadyToStart and Replay, so those will have to be dealt with elsewhere.
		 * Further note that there isn't Initializing should not be accessible anymore, and I'm pretty sure it's not even logically possible for the game to end in a Draw.
		 */
		game.setGameStatus(GameStatus.Running);
		if( game.getCurrentPosition().getWhitePosition().getTile().getRow() == 1 ) {
			if( game.getCurrentPosition().getBlackPosition().getTile().getRow() == 9 ) {
				game.setGameStatus(GameStatus.Draw);
			} else {
				game.setGameStatus(GameStatus.WhiteWon);
			}
		} else if( game.getCurrentPosition().getBlackPosition().getTile().getRow() == 9 ) {
			game.setGameStatus(GameStatus.BlackWon);
		}
		
		/*
		 * Return the game.
		 */
		return game;
	}
	
	
	/**
	 * Helper method which initializes and configures a new instance of Game and the provided instances of players to that of the start of a game.
	 * @param file
	 * @param quoridor
	 * @param whitePlayer
	 * @param blackPlayer
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Game initializeGame(File file, Quoridor quoridor, Player whitePlayer, Player blackPlayer ) throws FileNotFoundException, IOException {
		//Deletion of old stuff
		if(quoridor.getCurrentGame()!=null) {
			quoridor.getCurrentGame().delete();
		}
		//Instantiation of new stuff
		Game game = new Game(GameStatus.Initializing, MoveMode.PlayerMove, quoridor);
		game.setWhitePlayer(whitePlayer);
		game.setBlackPlayer(blackPlayer);
		whitePlayer.setNextPlayer(blackPlayer);
		blackPlayer.setNextPlayer(whitePlayer);
		List<Wall> oldWalls = new ArrayList<Wall>();
		oldWalls.addAll(whitePlayer.getWalls());
		for(Wall wall : oldWalls ) {
			whitePlayer.removeWall(wall);
			wall.delete();
		}
		oldWalls = new ArrayList<Wall>();
		oldWalls.addAll(blackPlayer.getWalls());
		for(Wall wall : oldWalls ) {
			blackPlayer.removeWall(wall);
			wall.delete();
		}
		PlayerPosition initialWhitePosition = new PlayerPosition(whitePlayer, quoridor.getBoard().getTile( getTileId(9, 5) ) );
		PlayerPosition initialBlackPosition = new PlayerPosition(blackPlayer, quoridor.getBoard().getTile( getTileId(1,5) ) );
		Player playerToMove = ( ( numberOfMovesInMovFile(file) % 2 == 0 )? whitePlayer : blackPlayer );
		GamePosition initialPosition = new GamePosition(0, initialWhitePosition,  initialBlackPosition, playerToMove, game);
		for( int i = 1 ; i <= 10 ; i ++ ) {
			initialPosition.addWhiteWallsInStock( new Wall(i,whitePlayer) );
		}
		for( int i = 11 ; i <= 20 ; i ++ ) {
			initialPosition.addBlackWallsInStock( new Wall(i,blackPlayer) );
		}
		game.setCurrentPosition(initialPosition);
		
		return game;
	}
	
	/**
	 * Helper method which duplicates the GamePosition provided
	 * Useful for recreating the multiple phases of a game being loaded.
	 */
	private static GamePosition duplicateGamePosition(GamePosition gamePosition, Game game, Player playerToMove) {
		int newGamePositionId = gamePosition.getId() + 1;
		PlayerPosition whitePosition = new PlayerPosition( gamePosition.getWhitePosition().getPlayer(), gamePosition.getWhitePosition().getTile() );
		PlayerPosition blackPosition = new PlayerPosition( gamePosition.getBlackPosition().getPlayer(), gamePosition.getBlackPosition().getTile() );
		GamePosition newGamePosition = game.addPosition( newGamePositionId, whitePosition, blackPosition, playerToMove );
		for( int i = 0 ; i < gamePosition.getWhiteWallsInStock().size() ; i ++ ) {
			newGamePosition.addWhiteWallsInStock( gamePosition.getWhiteWallsInStock(i) );
		}
		for( int i = 0 ; i < gamePosition.getWhiteWallsOnBoard().size() ; i ++ ) {
			newGamePosition.addWhiteWallsOnBoard( gamePosition.getWhiteWallsOnBoard(i) );
		}
		for( int i = 0 ; i < gamePosition.getBlackWallsInStock().size() ; i ++ ) {
			newGamePosition.addBlackWallsInStock( gamePosition.getBlackWallsInStock(i) );
		}
		for( int i = 0 ; i < gamePosition.getBlackWallsOnBoard().size() ; i ++ ) {
			newGamePosition.addBlackWallsOnBoard( gamePosition.getBlackWallsOnBoard(i) );
		}
		
		return newGamePosition;
	}
	
	
	/**
	 * Helper Method which produces the sprint3-format string characterizing a player's pawn coordinates and wall coordinates.
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
	private static void sanityCheckTileCoordinates(int row, int col) throws InvalidPositionException {
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
	
	/**
	 * Validates the provided game's current position.
	 * @param game
	 * @throws InvalidPositionException
	 */
	private static void validateCurrentGamePosition(Game game) throws InvalidPositionException{
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
	}
	
	/**
	 * 	Returns the stepping MoveDirection of the two dimensional vector provided.
	 */
	private static MoveDirection getStepDirection( int initialCol, int initialRow, int finalCol, int finalRow ) {
		MoveDirection direction = null;
		
		int deltaCol = finalCol - initialCol;
		int deltaRow = finalRow - initialRow;
		if( deltaCol == 0 && deltaRow == -1 ) {
			direction = MoveDirection.North;
		} else if(deltaCol == 1 && deltaRow == 0) {
			direction = MoveDirection.East;
		} else if(deltaCol == 0 && deltaRow == 1) {
			direction = MoveDirection.South;
		} else if(deltaCol == -1 && deltaRow == 0) {
			direction = MoveDirection.West;
		}
		
		return direction;
	}
	
	/**
	 * 	Returns the jumping MoveDirection of the two dimensional vector provided.
	 */
	private static MoveDirection getJumpDirection( int initialCol, int initialRow, int finalCol, int finalRow ){
		MoveDirection direction = null;
		
		int deltaCol = finalCol - initialCol;
		int deltaRow = finalRow - initialRow;
		if(deltaCol == -2) {
			if(deltaRow == 0) {
				direction = MoveDirection.West;
			}
		} else if(deltaCol == -1) {
			if(deltaRow == -1) {
				direction = MoveDirection.NorthWest;
			} else if( deltaRow == 1 ) {
				direction = MoveDirection.SouthWest;
			}
		} else if(deltaCol == 0 ) {
			if(deltaRow == -2 ) {
				direction = MoveDirection.North;
			} else if(deltaRow == 2) {
				direction = MoveDirection.South;
			}
		} else if(deltaCol == 1 ) {
			if(deltaRow == -1) {
				direction = MoveDirection.NorthEast;
			} else if(deltaRow == 1) {
				direction = MoveDirection.SouthEast;
			}
		} else if(deltaCol == 2 ) {
			if(deltaRow == 0) {
				direction = MoveDirection.East;
			}
		}
		
		return direction;
	}
	
	/**
	 * Helper method
	 * Checks how many steps there are in a .mov save file.
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static int numberOfMovesInMovFile( File file ) throws FileNotFoundException, IOException {
		BufferedReader bufferedReader = new BufferedReader( new FileReader( file ) );
		int numberOfLines = 0;
		String bufferedLine = bufferedReader.readLine();
		while( bufferedLine != null && !bufferedLine.equals("") ) {
			bufferedLine = bufferedReader.readLine();
			numberOfLines++;
		}
		bufferedReader.close();
		return numberOfLines;
	}
	
	/**
	 * Helper method
	 * Gets the column in integer form given the string form of a tile location
	 * @param tile
	 * @return
	 */
	private static int getColumn(String tile) {
		return tile.trim().charAt(0) - 'a' + 1;
	}
	
	/**
	 * Helper method
	 * Gets the row in integer form givent the string form of a tile location
	 * @param tile
	 * @return
	 */
	private static int getRow(String tile) {
		return tile.trim().charAt(1) - '0';
	}
	
	/**
	 * Debugger method
	 * @param walls
	 * @return
	 */
	private static String listOfPlacedWallsToString( List<Wall> walls ) {
		String str = "";
		for( Wall wall : walls ) {
			str = str + "(" + wall.getMove().getTargetTile().getColumn() + "," + wall.getMove().getTargetTile().getRow() + ")\n" ;
		}
		return str;
	}
	
}
