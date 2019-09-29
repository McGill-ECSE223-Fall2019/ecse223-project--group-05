/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 70 "../../../../../Model.ump"
// line 80 "../../../../../Model.ump"
public class Wall extends GameItem
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

  //Wall Associations
  private Game game;
  private Pawn owner;
  private Tile currentPosition;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(Orientation aOrientation, Game aGame, Pawn aOwner, Tile aCurrentPosition)
  {
    super();
    orientation = aOrientation;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create wall due to game");
    }
    if (!setOwner(aOwner))
    {
      throw new RuntimeException("Unable to create Wall due to aOwner");
    }
    boolean didAddCurrentPosition = setCurrentPosition(aCurrentPosition);
    if (!didAddCurrentPosition)
    {
      throw new RuntimeException("Unable to create wall due to currentPosition");
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

  public Orientation getOrientation()
  {
    return orientation;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Pawn getOwner()
  {
    return owner;
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
    //Must provide game to wall
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (20)
    if (aGame.numberOfWalls() >= Game.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removeWall(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addWall(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setOwner(Pawn aNewOwner)
  {
    boolean wasSet = false;
    if (aNewOwner != null)
    {
      owner = aNewOwner;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setCurrentPosition(Tile aNewCurrentPosition)
  {
    boolean wasSet = false;
    if (aNewCurrentPosition == null)
    {
      //Unable to setCurrentPosition to null, as wall must always be associated to a currentPosition
      return wasSet;
    }
    
    Wall existingWall = aNewCurrentPosition.getWall();
    if (existingWall != null && !equals(existingWall))
    {
      //Unable to setCurrentPosition, the current currentPosition already has a wall, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Tile anOldCurrentPosition = currentPosition;
    currentPosition = aNewCurrentPosition;
    currentPosition.setWall(this);

    if (anOldCurrentPosition != null)
    {
      anOldCurrentPosition.setWall(null);
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
      placeholderGame.removeWall(this);
    }
    owner = null;
    Tile existingCurrentPosition = currentPosition;
    currentPosition = null;
    if (existingCurrentPosition != null)
    {
      existingCurrentPosition.setWall(null);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orientation" + "=" + (getOrientation() != null ? !getOrientation().equals(this)  ? getOrientation().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "owner = "+(getOwner()!=null?Integer.toHexString(System.identityHashCode(getOwner())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentPosition = "+(getCurrentPosition()!=null?Integer.toHexString(System.identityHashCode(getCurrentPosition())):"null");
  }
}