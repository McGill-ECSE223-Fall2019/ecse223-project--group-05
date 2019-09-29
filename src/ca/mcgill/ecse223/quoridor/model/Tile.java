/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 58 "../../../../../Model.ump"
public class Tile
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tile Attributes
  private int row;
  private Character column;

  //Tile Associations
  private Game game;
  private Pawn pawn;
  private Wall wall;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tile(int aRow, Character aColumn, Game aGame)
  {
    row = aRow;
    column = aColumn;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create tile due to game");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRow(int aRow)
  {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setColumn(Character aColumn)
  {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public int getRow()
  {
    return row;
  }

  public Character getColumn()
  {
    return column;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
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
  public Wall getWall()
  {
    return wall;
  }

  public boolean hasWall()
  {
    boolean has = wall != null;
    return has;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to tile
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (81)
    if (aGame.numberOfTiles() >= Game.maximumNumberOfTiles())
    {
      return wasSet;
    }
    
    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removeTile(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addTile(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setPawn(Pawn aNewPawn)
  {
    boolean wasSet = false;
    if (pawn != null && !pawn.equals(aNewPawn) && equals(pawn.getCurrentPosition()))
    {
      //Unable to setPawn, as existing pawn would become an orphan
      return wasSet;
    }

    pawn = aNewPawn;
    Tile anOldCurrentPosition = aNewPawn != null ? aNewPawn.getCurrentPosition() : null;

    if (!this.equals(anOldCurrentPosition))
    {
      if (anOldCurrentPosition != null)
      {
        anOldCurrentPosition.pawn = null;
      }
      if (pawn != null)
      {
        pawn.setCurrentPosition(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setWall(Wall aNewWall)
  {
    boolean wasSet = false;
    if (wall != null && !wall.equals(aNewWall) && equals(wall.getCurrentPosition()))
    {
      //Unable to setWall, as existing wall would become an orphan
      return wasSet;
    }

    wall = aNewWall;
    Tile anOldCurrentPosition = aNewWall != null ? aNewWall.getCurrentPosition() : null;

    if (!this.equals(anOldCurrentPosition))
    {
      if (anOldCurrentPosition != null)
      {
        anOldCurrentPosition.wall = null;
      }
      if (wall != null)
      {
        wall.setCurrentPosition(this);
      }
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
      placeholderGame.removeTile(this);
    }
    Pawn existingPawn = pawn;
    pawn = null;
    if (existingPawn != null)
    {
      existingPawn.delete();
    }
    Wall existingWall = wall;
    wall = null;
    if (existingWall != null)
    {
      existingWall.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "row" + ":" + getRow()+ "," +
            "column" + ":" + getColumn()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "wall = "+(getWall()!=null?Integer.toHexString(System.identityHashCode(getWall())):"null");
  }
}