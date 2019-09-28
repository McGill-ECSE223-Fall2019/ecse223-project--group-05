/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 81 "../../../../../Model.ump"
public class Step
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Step Attributes
  private String log;

  //Step Associations
  private Game gameSteps;
  private Game game;
  private Step next;
  private Step prev;
  private Tile earlierState;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Step(String aLog, Game aGameSteps, Game aGame, Tile aEarlierState)
  {
    log = aLog;
    boolean didAddGameSteps = setGameSteps(aGameSteps);
    if (!didAddGameSteps)
    {
      throw new RuntimeException("Unable to create step due to gameSteps");
    }
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create currentStep due to game");
    }
    if (!setEarlierState(aEarlierState))
    {
      throw new RuntimeException("Unable to create Step due to aEarlierState");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLog(String aLog)
  {
    boolean wasSet = false;
    log = aLog;
    wasSet = true;
    return wasSet;
  }

  public String getLog()
  {
    return log;
  }
  /* Code from template association_GetOne */
  public Game getGameSteps()
  {
    return gameSteps;
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
  public Tile getEarlierState()
  {
    return earlierState;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGameSteps(Game aGameSteps)
  {
    boolean wasSet = false;
    if (aGameSteps == null)
    {
      return wasSet;
    }

    Game existingGameSteps = gameSteps;
    gameSteps = aGameSteps;
    if (existingGameSteps != null && !existingGameSteps.equals(aGameSteps))
    {
      existingGameSteps.removeStep(this);
    }
    gameSteps.addStep(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToOptionalOne */
  public boolean setGame(Game aNewGame)
  {
    boolean wasSet = false;
    if (aNewGame == null)
    {
      //Unable to setGame to null, as currentStep must always be associated to a game
      return wasSet;
    }
    
    Step existingCurrentStep = aNewGame.getCurrentStep();
    if (existingCurrentStep != null && !equals(existingCurrentStep))
    {
      //Unable to setGame, the current game already has a currentStep, which would be orphaned if it were re-assigned
      return wasSet;
    }
    
    Game anOldGame = game;
    game = aNewGame;
    game.setCurrentStep(this);

    if (anOldGame != null)
    {
      anOldGame.setCurrentStep(null);
    }
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
  public boolean setEarlierState(Tile aNewEarlierState)
  {
    boolean wasSet = false;
    if (aNewEarlierState != null)
    {
      earlierState = aNewEarlierState;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    Game placeholderGameSteps = gameSteps;
    this.gameSteps = null;
    if(placeholderGameSteps != null)
    {
      placeholderGameSteps.removeStep(this);
    }
    Game existingGame = game;
    game = null;
    if (existingGame != null)
    {
      existingGame.setCurrentStep(null);
    }
    if (next != null)
    {
      next.setPrev(null);
    }
    if (prev != null)
    {
      prev.setNext(null);
    }
    earlierState = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "log" + ":" + getLog()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "gameSteps = "+(getGameSteps()!=null?Integer.toHexString(System.identityHashCode(getGameSteps())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "earlierState = "+(getEarlierState()!=null?Integer.toHexString(System.identityHashCode(getEarlierState())):"null");
  }
}