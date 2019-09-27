/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 70 "../../../../../Model.ump"
public class Wall
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Orientation { Horizontal, Vertical }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wall Attributes
  private Orientation orientation;
  private boolean isAvailable;

  //Wall Associations
  private Pawn pawn;
  private Game game;
  private Tile tile;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(Orientation aOrientation, boolean aIsAvailable, Tile aTile)
  {
    orientation = aOrientation;
    isAvailable = aIsAvailable;
    boolean didAddTile = setTile(aTile);
    if (!didAddTile)
    {
      throw new RuntimeException("Unable to create wall due to tile");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOrientation(Orientation aOrientation)
  {
    boolean wasSet = false;
    orientation = aOrientation;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsAvailable(boolean aIsAvailable)
  {
    boolean wasSet = false;
    isAvailable = aIsAvailable;
    wasSet = true;
    return wasSet;
  }

  public Orientation getOrientation()
  {
    return orientation;
  }

  public boolean getIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
  }

  public boolean hasPawn()
  {
    boolean has = pawn != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }

  public boolean hasGame()
  {
    boolean has = game != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Tile getTile()
  {
    return tile;
  }
  /* Code from template association_SetOptionalOneToOptionalN */
  public boolean setPawn(Pawn aPawn)
  {
    boolean wasSet = false;
    if (aPawn != null && aPawn.numberOfWalls() >= Pawn.maximumNumberOfWalls())
    {
      return wasSet;
    }

    Pawn existingPawn = pawn;
    pawn = aPawn;
    if (existingPawn != null && !existingPawn.equals(aPawn))
    {
      existingPawn.removeWall(this);
    }
    if (aPawn != null)
    {
      aPawn.addWall(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame != null && aGame.numberOfWalls() >= Game.maximumNumberOfWalls())
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removeWall(this);
    }
    if (aGame != null)
    {
      aGame.addWall(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setTile(Tile aNewTile)
  {
    boolean wasSet = false;
    if (aNewTile == null)
    {
      //Unable to setTile to null, as wall must always be associated to a tile
      return wasSet;
    }
    
    Wall existingWall = aNewTile.getWall();
    if (existingWall != null && !equals(existingWall))
    {
      //Unable to setTile, the current tile already has a wall, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Tile anOldTile = tile;
    tile = aNewTile;
    tile.setWall(this);

    if (anOldTile != null)
    {
      anOldTile.setWall(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (pawn != null)
    {
      Pawn placeholderPawn = pawn;
      this.pawn = null;
      placeholderPawn.removeWall(this);
    }
    if (game != null)
    {
      Game placeholderGame = game;
      this.game = null;
      placeholderGame.removeWall(this);
    }
    Tile existingTile = tile;
    tile = null;
    if (existingTile != null)
    {
      existingTile.setWall(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "isAvailable" + ":" + getIsAvailable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orientation" + "=" + (getOrientation() != null ? !getOrientation().equals(this)  ? getOrientation().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tile = "+(getTile()!=null?Integer.toHexString(System.identityHashCode(getTile())):"null");
  }
}