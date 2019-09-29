/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;

// line 68 "../../../../../Model.ump"
// line 72 "../../../../../Model.ump"
public class Pawn extends GameItem
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

  //Pawn Associations
  private Game game;
  private User player;
  private List<Wall> wallsKept;
  private List<Wall> wallsUsed;
  private Tile currentPosition;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(Color aColor, Game aGame, User aPlayer, Tile aCurrentPosition)
  {
    super();
    color = aColor;
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
    wallsKept = new ArrayList<Wall>();
    wallsUsed = new ArrayList<Wall>();
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

  public Color getColor()
  {
    return color;
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
  public Wall getWallsKept(int index)
  {
    Wall aWallsKept = wallsKept.get(index);
    return aWallsKept;
  }

  public List<Wall> getWallsKept()
  {
    List<Wall> newWallsKept = Collections.unmodifiableList(wallsKept);
    return newWallsKept;
  }

  public int numberOfWallsKept()
  {
    int number = wallsKept.size();
    return number;
  }

  public boolean hasWallsKept()
  {
    boolean has = wallsKept.size() > 0;
    return has;
  }

  public int indexOfWallsKept(Wall aWallsKept)
  {
    int index = wallsKept.indexOf(aWallsKept);
    return index;
  }
  /* Code from template association_GetMany */
  public Wall getWallsUsed(int index)
  {
    Wall aWallsUsed = wallsUsed.get(index);
    return aWallsUsed;
  }

  public List<Wall> getWallsUsed()
  {
    List<Wall> newWallsUsed = Collections.unmodifiableList(wallsUsed);
    return newWallsUsed;
  }

  public int numberOfWallsUsed()
  {
    int number = wallsUsed.size();
    return number;
  }

  public boolean hasWallsUsed()
  {
    boolean has = wallsUsed.size() > 0;
    return has;
  }

  public int indexOfWallsUsed(Wall aWallsUsed)
  {
    int index = wallsUsed.indexOf(aWallsUsed);
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
  public static int minimumNumberOfWallsKept()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWallsKept()
  {
    return 10;
  }
  /* Code from template association_AddUnidirectionalOptionalN */
  public boolean addWallsKept(Wall aWallsKept)
  {
    boolean wasAdded = false;
    if (wallsKept.contains(aWallsKept)) { return false; }
    if (numberOfWallsKept() < maximumNumberOfWallsKept())
    {
      wallsKept.add(aWallsKept);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean removeWallsKept(Wall aWallsKept)
  {
    boolean wasRemoved = false;
    if (wallsKept.contains(aWallsKept))
    {
      wallsKept.remove(aWallsKept);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalOptionalN */
  public boolean setWallsKept(Wall... newWallsKept)
  {
    boolean wasSet = false;
    ArrayList<Wall> verifiedWallsKept = new ArrayList<Wall>();
    for (Wall aWallsKept : newWallsKept)
    {
      if (verifiedWallsKept.contains(aWallsKept))
      {
        continue;
      }
      verifiedWallsKept.add(aWallsKept);
    }

    if (verifiedWallsKept.size() != newWallsKept.length || verifiedWallsKept.size() > maximumNumberOfWallsKept())
    {
      return wasSet;
    }

    wallsKept.clear();
    wallsKept.addAll(verifiedWallsKept);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWallsKeptAt(Wall aWallsKept, int index)
  {  
    boolean wasAdded = false;
    if(addWallsKept(aWallsKept))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWallsKept()) { index = numberOfWallsKept() - 1; }
      wallsKept.remove(aWallsKept);
      wallsKept.add(index, aWallsKept);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWallsKeptAt(Wall aWallsKept, int index)
  {
    boolean wasAdded = false;
    if(wallsKept.contains(aWallsKept))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWallsKept()) { index = numberOfWallsKept() - 1; }
      wallsKept.remove(aWallsKept);
      wallsKept.add(index, aWallsKept);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWallsKeptAt(aWallsKept, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWallsUsed()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWallsUsed()
  {
    return 10;
  }
  /* Code from template association_AddUnidirectionalOptionalN */
  public boolean addWallsUsed(Wall aWallsUsed)
  {
    boolean wasAdded = false;
    if (wallsUsed.contains(aWallsUsed)) { return false; }
    if (numberOfWallsUsed() < maximumNumberOfWallsUsed())
    {
      wallsUsed.add(aWallsUsed);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean removeWallsUsed(Wall aWallsUsed)
  {
    boolean wasRemoved = false;
    if (wallsUsed.contains(aWallsUsed))
    {
      wallsUsed.remove(aWallsUsed);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_SetUnidirectionalOptionalN */
  public boolean setWallsUsed(Wall... newWallsUsed)
  {
    boolean wasSet = false;
    ArrayList<Wall> verifiedWallsUsed = new ArrayList<Wall>();
    for (Wall aWallsUsed : newWallsUsed)
    {
      if (verifiedWallsUsed.contains(aWallsUsed))
      {
        continue;
      }
      verifiedWallsUsed.add(aWallsUsed);
    }

    if (verifiedWallsUsed.size() != newWallsUsed.length || verifiedWallsUsed.size() > maximumNumberOfWallsUsed())
    {
      return wasSet;
    }

    wallsUsed.clear();
    wallsUsed.addAll(verifiedWallsUsed);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWallsUsedAt(Wall aWallsUsed, int index)
  {  
    boolean wasAdded = false;
    if(addWallsUsed(aWallsUsed))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWallsUsed()) { index = numberOfWallsUsed() - 1; }
      wallsUsed.remove(aWallsUsed);
      wallsUsed.add(index, aWallsUsed);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWallsUsedAt(Wall aWallsUsed, int index)
  {
    boolean wasAdded = false;
    if(wallsUsed.contains(aWallsUsed))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWallsUsed()) { index = numberOfWallsUsed() - 1; }
      wallsUsed.remove(aWallsUsed);
      wallsUsed.add(index, aWallsUsed);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWallsUsedAt(aWallsUsed, index);
    }
    return wasAdded;
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
    wallsKept.clear();
    wallsUsed.clear();
    Tile existingCurrentPosition = currentPosition;
    currentPosition = null;
    if (existingCurrentPosition != null)
    {
      existingCurrentPosition.setPawn(null);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentPosition = "+(getCurrentPosition()!=null?Integer.toHexString(System.identityHashCode(getCurrentPosition())):"null");
  }
}