/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 50 "../../../../../Model.ump"
public class Step
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum StepType { PawnMove, WallPlace }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Step Attributes
  private int stepNo;
  private String log;
  private StepType stepType;

  //Step Associations
  private Game game;
  private Step next;
  private Step prev;
  private Tile tile1;
  private Tile tile2;
  private GameItem stepItem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Step(int aStepNo, String aLog, StepType aStepType, Game aGame, Tile aTile1, Tile aTile2, GameItem aStepItem)
  {
    stepNo = aStepNo;
    log = aLog;
    stepType = aStepType;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create step due to game");
    }
    if (!setTile1(aTile1))
    {
      throw new RuntimeException("Unable to create Step due to aTile1");
    }
    if (!setTile2(aTile2))
    {
      throw new RuntimeException("Unable to create Step due to aTile2");
    }
    if (!setStepItem(aStepItem))
    {
      throw new RuntimeException("Unable to create Step due to aStepItem");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setStepNo(int aStepNo)
  {
    boolean wasSet = false;
    stepNo = aStepNo;
    wasSet = true;
    return wasSet;
  }

  public boolean setLog(String aLog)
  {
    boolean wasSet = false;
    log = aLog;
    wasSet = true;
    return wasSet;
  }

  public boolean setStepType(StepType aStepType)
  {
    boolean wasSet = false;
    stepType = aStepType;
    wasSet = true;
    return wasSet;
  }

  public int getStepNo()
  {
    return stepNo;
  }

  public String getLog()
  {
    return log;
  }

  public StepType getStepType()
  {
    return stepType;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public Step getNext()
  {
    return next;
  }

  public boolean hasNext()
  {
    boolean has = next != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Step getPrev()
  {
    return prev;
  }

  public boolean hasPrev()
  {
    boolean has = prev != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Tile getTile1()
  {
    return tile1;
  }
  /* Code from template association_GetOne */
  public Tile getTile2()
  {
    return tile2;
  }
  /* Code from template association_GetOne */
  public GameItem getStepItem()
  {
    return stepItem;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removeStep(this);
    }
    game.addStep(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setNext(Step aNewNext)
  {
    boolean wasSet = false;
    if (aNewNext == null)
    {
      Step existingNext = next;
      next = null;
      
      if (existingNext != null && existingNext.getPrev() != null)
      {
        existingNext.setPrev(null);
      }
      wasSet = true;
      return wasSet;
    }

    Step currentNext = getNext();
    if (currentNext != null && !currentNext.equals(aNewNext))
    {
      currentNext.setPrev(null);
    }

    next = aNewNext;
    Step existingPrev = aNewNext.getPrev();

    if (!equals(existingPrev))
    {
      aNewNext.setPrev(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setPrev(Step aNewPrev)
  {
    boolean wasSet = false;
    if (aNewPrev == null)
    {
      Step existingPrev = prev;
      prev = null;
      
      if (existingPrev != null && existingPrev.getNext() != null)
      {
        existingPrev.setNext(null);
      }
      wasSet = true;
      return wasSet;
    }

    Step currentPrev = getPrev();
    if (currentPrev != null && !currentPrev.equals(aNewPrev))
    {
      currentPrev.setNext(null);
    }

    prev = aNewPrev;
    Step existingNext = aNewPrev.getNext();

    if (!equals(existingNext))
    {
      aNewPrev.setNext(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setTile1(Tile aNewTile1)
  {
    boolean wasSet = false;
    if (aNewTile1 != null)
    {
      tile1 = aNewTile1;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setTile2(Tile aNewTile2)
  {
    boolean wasSet = false;
    if (aNewTile2 != null)
    {
      tile2 = aNewTile2;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setStepItem(GameItem aNewStepItem)
  {
    boolean wasSet = false;
    if (aNewStepItem != null)
    {
      stepItem = aNewStepItem;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removeStep(this);
    }
    if (next != null)
    {
      next.setPrev(null);
    }
    if (prev != null)
    {
      prev.setNext(null);
    }
    tile1 = null;
    tile2 = null;
    stepItem = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "stepNo" + ":" + getStepNo()+ "," +
            "log" + ":" + getLog()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "stepType" + "=" + (getStepType() != null ? !getStepType().equals(this)  ? getStepType().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tile1 = "+(getTile1()!=null?Integer.toHexString(System.identityHashCode(getTile1())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "tile2 = "+(getTile2()!=null?Integer.toHexString(System.identityHashCode(getTile2())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "stepItem = "+(getStepItem()!=null?Integer.toHexString(System.identityHashCode(getStepItem())):"null");
  }
}