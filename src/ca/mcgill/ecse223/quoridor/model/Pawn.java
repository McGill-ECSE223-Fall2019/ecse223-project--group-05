/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.sql.Time;
import java.util.*;

// line 61 "../../../../../Model.ump"
public class Pawn
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Orientation { Horizontal, Vertical }
  public enum Color { White, Black }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Pawn Attributes
  private Color color;
  private Time thinkingTime;

  //Pawn Associations
  private User player;
  private Game game;
  private List<Wall> walls;
  private Tile tile;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Pawn(Color aColor, Time aThinkingTime, User aPlayer, Game aGame, Tile aTile)
  {
    color = aColor;
    thinkingTime = aThinkingTime;
    boolean didAddPlayer = setPlayer(aPlayer);
    if (!didAddPlayer)
    {
      throw new RuntimeException("Unable to create pawn due to player");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create pawn due to game");
    }
    walls = new ArrayList<Wall>();
    boolean didAddTile = setTile(aTile);
    if (!didAddTile)
    {
      throw new RuntimeException("Unable to create pawn due to tile");
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
  public User getPlayer()
  {
    return player;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public Wall getWall(int index)
  {
    Wall aWall = walls.get(index);
    return aWall;
  }

  public List<Wall> getWalls()
  {
    List<Wall> newWalls = Collections.unmodifiableList(walls);
    return newWalls;
  }

  public int numberOfWalls()
  {
    int number = walls.size();
    return number;
  }

  public boolean hasWalls()
  {
    boolean has = walls.size() > 0;
    return has;
  }

  public int indexOfWall(Wall aWall)
  {
    int index = walls.indexOf(aWall);
    return index;
  }
  /* Code from template association_GetOne */
  public Tile getTile()
  {
    return tile;
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
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWalls()
  {
    return 0;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWalls()
  {
    return 10;
  }
  /* Code from template association_AddOptionalNToOptionalOne */
  public boolean addWall(Wall aWall)
  {
    boolean wasAdded = false;
    if (walls.contains(aWall)) { return false; }
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return wasAdded;
    }

    Pawn existingPawn = aWall.getPawn();
    if (existingPawn == null)
    {
      aWall.setPawn(this);
    }
    else if (!this.equals(existingPawn))
    {
      existingPawn.removeWall(aWall);
      addWall(aWall);
    }
    else
    {
      walls.add(aWall);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeWall(Wall aWall)
  {
    boolean wasRemoved = false;
    if (walls.contains(aWall))
    {
      walls.remove(aWall);
      aWall.setPawn(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addWallAt(Wall aWall, int index)
  {  
    boolean wasAdded = false;
    if(addWall(aWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWalls()) { index = numberOfWalls() - 1; }
      walls.remove(aWall);
      walls.add(index, aWall);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveWallAt(Wall aWall, int index)
  {
    boolean wasAdded = false;
    if(walls.contains(aWall))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfWalls()) { index = numberOfWalls() - 1; }
      walls.remove(aWall);
      walls.add(index, aWall);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addWallAt(aWall, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setTile(Tile aNewTile)
  {
    boolean wasSet = false;
    if (aNewTile == null)
    {
      //Unable to setTile to null, as pawn must always be associated to a tile
      return wasSet;
    }
    
    Pawn existingPawn = aNewTile.getPawn();
    if (existingPawn != null && !equals(existingPawn))
    {
      //Unable to setTile, the current tile already has a pawn, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Tile anOldTile = tile;
    tile = aNewTile;
    tile.setPawn(this);

    if (anOldTile != null)
    {
      anOldTile.setPawn(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    User placeholderPlayer = player;
    this.player = null;
    if(placeholderPlayer != null)
    {
      placeholderPlayer.removePawn(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePawn(this);
    }
    while( !walls.isEmpty() )
    {
      walls.get(0).setPawn(null);
    }
    Tile existingTile = tile;
    tile = null;
    if (existingTile != null)
    {
      existingTile.setPawn(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "thinkingTime" + "=" + (getThinkingTime() != null ? !getThinkingTime().equals(this)  ? getThinkingTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tile = "+(getTile()!=null?Integer.toHexString(System.identityHashCode(getTile())):"null");
  }
}