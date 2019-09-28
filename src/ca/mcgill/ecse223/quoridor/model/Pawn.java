/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.sql.Time;
import java.util.*;

// line 66 "../../../../../Model.ump"
public class Pawn
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Color { White, Black }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Pawn Attributes
  private Color color;
  private Time thinkingTime;

  //Pawn Associations
  private Game game;
  private User player;
  private List<Wall> onBoard;
  private List<Wall> ownsWall;
  private Tile currentPosition;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(Color aColor, Time aThinkingTime, Game aGame, User aPlayer, Tile aCurrentPosition)
  {
    color = aColor;
    thinkingTime = aThinkingTime;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    boolean didAddPlayer = setPlayer(aPlayer);
    if (!didAddPlayer)
    {
      throw new RuntimeException("Unable to create pawn due to player");
    }
    onBoard = new ArrayList<Wall>();
    ownsWall = new ArrayList<Wall>();
    boolean didAddCurrentPosition = setCurrentPosition(aCurrentPosition);
    if (!didAddCurrentPosition)
    {
      throw new RuntimeException("Unable to create pawn due to currentPosition");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setColor(Color aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public boolean setThinkingTime(Time aThinkingTime)
  {
    boolean wasSet = false;
    thinkingTime = aThinkingTime;
    wasSet = true;
    return wasSet;
  }

  public Color getColor()
  {
    return color;
  }

  public Time getThinkingTime()
  {
    return thinkingTime;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public User getPlayer()
  {
    return player;
  }
  /* Code from template association_GetMany */
  public Wall getOnBoard(int index)
  {
    Wall aOnBoard = onBoard.get(index);
    return aOnBoard;
  }

  public List<Wall> getOnBoard()
  {
    List<Wall> newOnBoard = Collections.unmodifiableList(onBoard);
    return newOnBoard;
  }

  public int numberOfOnBoard()
  {
    int number = onBoard.size();
    return number;
  }

  public boolean hasOnBoard()
  {
    boolean has = onBoard.size() > 0;
    return has;
  }

  public int indexOfOnBoard(Wall aOnBoard)
  {
    int index = onBoard.indexOf(aOnBoard);
    return index;
  }
  /* Code from template association_GetMany */
  public Wall getOwnsWall(int index)
  {
    Wall aOwnsWall = ownsWall.get(index);
    return aOwnsWall;
  }

  public List<Wall> getOwnsWall()
  {
    List<Wall> newOwnsWall = Collections.unmodifiableList(ownsWall);
    return newOwnsWall;
  }

  public int numberOfOwnsWall()
  {
    int number = ownsWall.size();
    return number;
  }

  public boolean hasOwnsWall()
  {
    boolean has = ownsWall.size() > 0;
    return has;
  }

  public int indexOfOwnsWall(Wall aOwnsWall)
  {
    int index = ownsWall.indexOf(aOwnsWall);
    return index;
  }
  /* Code from template association_GetOne */
  public Tile getCurrentPosition()
  {
    return currentPosition;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to pawn
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (2)
    if (aGame.numberOfPawns() >= Game.maximumNumberOfPawns())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePawn(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPawn(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setPlayer(User aPlayer)
  {
    boolean wasSet = false;
    if (aPlayer == null)
    {
      return wasSet;
    }

    User existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      existingPlayer.removePawn(this);
    }
    player.addPawn(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOnBoard()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfOnBoard()
  {
    return 10;
  }
  /* Code from template association_AddOptionalNToOptionalOne */
  public boolean addOnBoard(Wall aOnBoard)
  {
    boolean wasAdded = false;
    if (onBoard.contains(aOnBoard)) { return false; }
    if (numberOfOnBoard() >= maximumNumberOfOnBoard())
    {
      return wasAdded;
    }

    Pawn existingUsedBy = aOnBoard.getUsedBy();
    if (existingUsedBy == null)
    {
      aOnBoard.setUsedBy(this);
    }
    else if (!this.equals(existingUsedBy))
    {
      existingUsedBy.removeOnBoard(aOnBoard);
      addOnBoard(aOnBoard);
    }
    else
    {
      onBoard.add(aOnBoard);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOnBoard(Wall aOnBoard)
  {
    boolean wasRemoved = false;
    if (onBoard.contains(aOnBoard))
    {
      onBoard.remove(aOnBoard);
      aOnBoard.setUsedBy(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOnBoardAt(Wall aOnBoard, int index)
  {  
    boolean wasAdded = false;
    if(addOnBoard(aOnBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOnBoard()) { index = numberOfOnBoard() - 1; }
      onBoard.remove(aOnBoard);
      onBoard.add(index, aOnBoard);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOnBoardAt(Wall aOnBoard, int index)
  {
    boolean wasAdded = false;
    if(onBoard.contains(aOnBoard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOnBoard()) { index = numberOfOnBoard() - 1; }
      onBoard.remove(aOnBoard);
      onBoard.add(index, aOnBoard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOnBoardAt(aOnBoard, index);
    }
    return wasAdded;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfOwnsWallValid()
  {
    boolean isValid = numberOfOwnsWall() >= minimumNumberOfOwnsWall() && numberOfOwnsWall() <= maximumNumberOfOwnsWall();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfOwnsWall()
  {
    return 10;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOwnsWall()
  {
    return 10;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfOwnsWall()
  {
    return 10;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Wall addOwnsWall(Wall.Orientation aOrientation, Game aGame, Tile aCurrentPosition)
  {
    if (numberOfOwnsWall() >= maximumNumberOfOwnsWall())
    {
      return null;
    }
    else
    {
      return new Wall(aOrientation, aGame, this, aCurrentPosition);
    }
  }

  public boolean addOwnsWall(Wall aOwnsWall)
  {
    boolean wasAdded = false;
    if (ownsWall.contains(aOwnsWall)) { return false; }
    if (numberOfOwnsWall() >= maximumNumberOfOwnsWall())
    {
      return wasAdded;
    }

    Pawn existingPawn = aOwnsWall.getPawn();
    boolean isNewPawn = existingPawn != null && !this.equals(existingPawn);

    if (isNewPawn && existingPawn.numberOfOwnsWall() <= minimumNumberOfOwnsWall())
    {
      return wasAdded;
    }

    if (isNewPawn)
    {
      aOwnsWall.setPawn(this);
    }
    else
    {
      ownsWall.add(aOwnsWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOwnsWall(Wall aOwnsWall)
  {
    boolean wasRemoved = false;
    //Unable to remove aOwnsWall, as it must always have a pawn
    if (this.equals(aOwnsWall.getPawn()))
    {
      return wasRemoved;
    }

    //pawn already at minimum (10)
    if (numberOfOwnsWall() <= minimumNumberOfOwnsWall())
    {
      return wasRemoved;
    }
    ownsWall.remove(aOwnsWall);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCurrentPosition(Tile aNewCurrentPosition)
  {
    boolean wasSet = false;
    if (aNewCurrentPosition == null)
    {
      //Unable to setCurrentPosition to null, as pawn must always be associated to a currentPosition
      return wasSet;
    }
    
    Pawn existingPawn = aNewCurrentPosition.getPawn();
    if (existingPawn != null && !equals(existingPawn))
    {
      //Unable to setCurrentPosition, the current currentPosition already has a pawn, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Tile anOldCurrentPosition = currentPosition;
    currentPosition = aNewCurrentPosition;
    currentPosition.setPawn(this);

    if (anOldCurrentPosition != null)
    {
      anOldCurrentPosition.setPawn(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePawn(this);
    }
    User placeholderPlayer = player;
    this.player = null;
    if(placeholderPlayer != null)
    {
      placeholderPlayer.removePawn(this);
    }
    while( !onBoard.isEmpty() )
    {
      onBoard.get(0).setUsedBy(null);
    }
    for(int i=ownsWall.size(); i > 0; i--)
    {
      Wall aOwnsWall = ownsWall.get(i - 1);
      aOwnsWall.delete();
    }
    Tile existingCurrentPosition = currentPosition;
    currentPosition = null;
    if (existingCurrentPosition != null)
    {
      existingCurrentPosition.setPawn(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "thinkingTime" + "=" + (getThinkingTime() != null ? !getThinkingTime().equals(this)  ? getThinkingTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentPosition = "+(getCurrentPosition()!=null?Integer.toHexString(System.identityHashCode(getCurrentPosition())):"null");
  }
}