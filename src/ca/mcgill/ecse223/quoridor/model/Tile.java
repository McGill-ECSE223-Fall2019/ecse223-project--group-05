/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.sql.Time;

// line 21 "../../../../../Model.ump"
// line 87 "../../../../../Model.ump"
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
  private Step step;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tile(int aRow, Character aColumn, Game aGame, Step aStep)
  {
    row = aRow;
    column = aColumn;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create tile due to game");
    }
    boolean didAddStep = setStep(aStep);
    if (!didAddStep)
    {
      throw new RuntimeException("Unable to create tile due to step");
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
  /* Code from template association_GetOne */
  public Step getStep()
  {
    return step;
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
    if (pawn != null && !pawn.equals(aNewPawn) && equals(pawn.getTile()))
    {
      //Unable to setPawn, as existing pawn would become an orphan
      return wasSet;
    }

    pawn = aNewPawn;
    Tile anOldTile = aNewPawn != null ? aNewPawn.getTile() : null;

    if (!this.equals(anOldTile))
    {
      if (anOldTile != null)
      {
        anOldTile.pawn = null;
      }
      if (pawn != null)
      {
        pawn.setTile(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setWall(Wall aNewWall)
  {
    boolean wasSet = false;
    if (wall != null && !wall.equals(aNewWall) && equals(wall.getTile()))
    {
      //Unable to setWall, as existing wall would become an orphan
      return wasSet;
    }

    wall = aNewWall;
    Tile anOldTile = aNewWall != null ? aNewWall.getTile() : null;

    if (!this.equals(anOldTile))
    {
      if (anOldTile != null)
      {
        anOldTile.wall = null;
      }
      if (wall != null)
      {
        wall.setTile(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setStep(Step aStep)
  {
    boolean wasSet = false;
    //Must provide step to tile
    if (aStep == null)
    {
      return wasSet;
    }

    if (step != null && step.numberOfTiles() <= Step.minimumNumberOfTiles())
    {
      return wasSet;
    }

    Step existingStep = step;
    step = aStep;
    if (existingStep != null && !existingStep.equals(aStep))
    {
      boolean didRemove = existingStep.removeTile(this);
      if (!didRemove)
      {
        step = existingStep;
        return wasSet;
      }
    }
    step.addTile(this);
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
    Step placeholderStep = step;
    this.step = null;
    if(placeholderStep != null)
    {
      placeholderStep.removeTile(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "row" + ":" + getRow()+ "," +
            "column" + ":" + getColumn()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "wall = "+(getWall()!=null?Integer.toHexString(System.identityHashCode(getWall())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "step = "+(getStep()!=null?Integer.toHexString(System.identityHashCode(getStep())):"null");
  }
}