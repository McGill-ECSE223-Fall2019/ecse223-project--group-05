/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 77 "../../../../../Model.ump"
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

  //Wall Associations
  private Game game;
  private Pawn usedBy;
  private Pawn pawn;
  private Tile currentPosition;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(Orientation aOrientation, Game aGame, Pawn aPawn, Tile aCurrentPosition)
  {
    orientation = aOrientation;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create wall due to game");
    }
    boolean didAddPawn = setPawn(aPawn);
    if (!didAddPawn)
    {
      throw new RuntimeException("Unable to create ownsWall due to pawn");
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
  public Pawn getUsedBy()
  {
    return usedBy;
  }

  public boolean hasUsedBy()
  {
    boolean has = usedBy != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
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
  /* Code from template association_SetOptionalOneToOptionalN */
  public boolean setUsedBy(Pawn aUsedBy)
  {
    boolean wasSet = false;
    if (aUsedBy != null && aUsedBy.numberOfOnBoard() >= Pawn.maximumNumberOfOnBoard())
    {
      return wasSet;
    }

    Pawn existingUsedBy = usedBy;
    usedBy = aUsedBy;
    if (existingUsedBy != null && !existingUsedBy.equals(aUsedBy))
    {
      existingUsedBy.removeOnBoard(this);
    }
    if (aUsedBy != null)
    {
      aUsedBy.addOnBoard(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPawn(Pawn aPawn)
  {
    boolean wasSet = false;
    //Must provide pawn to ownsWall
    if (aPawn == null)
    {
      return wasSet;
    }

    //pawn already at maximum (10)
    if (aPawn.numberOfOwnsWall() >= Pawn.maximumNumberOfOwnsWall())
    {
      return wasSet;
    }
    
    Pawn existingPawn = pawn;
    pawn = aPawn;
    if (existingPawn != null && !existingPawn.equals(aPawn))
    {
      boolean didRemove = existingPawn.removeOwnsWall(this);
      if (!didRemove)
      {
        pawn = existingPawn;
        return wasSet;
      }
    }
    pawn.addOwnsWall(this);
    wasSet = true;
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
    if (usedBy != null)
    {
      Pawn placeholderUsedBy = usedBy;
      this.usedBy = null;
      placeholderUsedBy.removeOnBoard(this);
    }
    Pawn placeholderPawn = pawn;
    this.pawn = null;
    if(placeholderPawn != null)
    {
      placeholderPawn.removeOwnsWall(this);
    }
    Tile existingCurrentPosition = currentPosition;
    currentPosition = null;
    if (existingCurrentPosition != null)
    {
      existingCurrentPosition.setWall(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orientation" + "=" + (getOrientation() != null ? !getOrientation().equals(this)  ? getOrientation().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "usedBy = "+(getUsedBy()!=null?Integer.toHexString(System.identityHashCode(getUsedBy())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentPosition = "+(getCurrentPosition()!=null?Integer.toHexString(System.identityHashCode(getCurrentPosition())):"null");
  }
}