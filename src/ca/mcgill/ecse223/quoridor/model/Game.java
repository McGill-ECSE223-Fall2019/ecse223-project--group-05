/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

// line 40 "../../../../../Model.ump"
public class Game
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum GameState { Active, Completed, Paused }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Game Attributes
  private String gameId;
  private Date startDate;
  private Time startTime;
  private GameState gameState;
  private Time thinkingTime;

  //Game Associations
  private QuoridorSystem qSystem;
  private List<Pawn> pawns;
  private List<Wall> walls;
  private List<Tile> tiles;
  private List<Step> steps;
  private Step currentStep;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(String aGameId, Date aStartDate, Time aStartTime, GameState aGameState, Time aThinkingTime, QuoridorSystem aQSystem)
  {
    gameId = aGameId;
    startDate = aStartDate;
    startTime = aStartTime;
    gameState = aGameState;
    thinkingTime = aThinkingTime;
    boolean didAddQSystem = setQSystem(aQSystem);
    if (!didAddQSystem)
    {
      throw new RuntimeException("Unable to create game due to qSystem");
    }
    pawns = new ArrayList<Pawn>();
    walls = new ArrayList<Wall>();
    tiles = new ArrayList<Tile>();
    steps = new ArrayList<Step>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setGameId(String aGameId)
  {
    boolean wasSet = false;
    gameId = aGameId;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setGameState(GameState aGameState)
  {
    boolean wasSet = false;
    gameState = aGameState;
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

  public String getGameId()
  {
    return gameId;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public GameState getGameState()
  {
    return gameState;
  }

  public Time getThinkingTime()
  {
    return thinkingTime;
  }
  /* Code from template association_GetOne */
  public QuoridorSystem getQSystem()
  {
    return qSystem;
  }
  /* Code from template association_GetMany */
  public Pawn getPawn(int index)
  {
    Pawn aPawn = pawns.get(index);
    return aPawn;
  }

  public List<Pawn> getPawns()
  {
    List<Pawn> newPawns = Collections.unmodifiableList(pawns);
    return newPawns;
  }

  public int numberOfPawns()
  {
    int number = pawns.size();
    return number;
  }

  public boolean hasPawns()
  {
    boolean has = pawns.size() > 0;
    return has;
  }

  public int indexOfPawn(Pawn aPawn)
  {
    int index = pawns.indexOf(aPawn);
    return index;
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
  /* Code from template association_GetMany */
  public Tile getTile(int index)
  {
    Tile aTile = tiles.get(index);
    return aTile;
  }

  public List<Tile> getTiles()
  {
    List<Tile> newTiles = Collections.unmodifiableList(tiles);
    return newTiles;
  }

  public int numberOfTiles()
  {
    int number = tiles.size();
    return number;
  }

  public boolean hasTiles()
  {
    boolean has = tiles.size() > 0;
    return has;
  }

  public int indexOfTile(Tile aTile)
  {
    int index = tiles.indexOf(aTile);
    return index;
  }
  /* Code from template association_GetMany */
  public Step getStep(int index)
  {
    Step aStep = steps.get(index);
    return aStep;
  }

  public List<Step> getSteps()
  {
    List<Step> newSteps = Collections.unmodifiableList(steps);
    return newSteps;
  }

  public int numberOfSteps()
  {
    int number = steps.size();
    return number;
  }

  public boolean hasSteps()
  {
    boolean has = steps.size() > 0;
    return has;
  }

  public int indexOfStep(Step aStep)
  {
    int index = steps.indexOf(aStep);
    return index;
  }
  /* Code from template association_GetOne */
  public Step getCurrentStep()
  {
    return currentStep;
  }

  public boolean hasCurrentStep()
  {
    boolean has = currentStep != null;
    return has;
  }
  /* Code from template association_SetOneToMany */
  public boolean setQSystem(QuoridorSystem aQSystem)
  {
    boolean wasSet = false;
    if (aQSystem == null)
    {
      return wasSet;
    }

    QuoridorSystem existingQSystem = qSystem;
    qSystem = aQSystem;
    if (existingQSystem != null && !existingQSystem.equals(aQSystem))
    {
      existingQSystem.removeGame(this);
    }
    qSystem.addGame(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfPawnsValid()
  {
    boolean isValid = numberOfPawns() >= minimumNumberOfPawns() && numberOfPawns() <= maximumNumberOfPawns();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfPawns()
  {
    return 2;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPawns()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPawns()
  {
    return 2;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Pawn addPawn(Pawn.Color aColor, User aPlayer, Tile aCurrentPosition)
  {
    if (numberOfPawns() >= maximumNumberOfPawns())
    {
      return null;
    }
    else
    {
      return new Pawn(aColor, this, aPlayer, aCurrentPosition);
    }
  }

  public boolean addPawn(Pawn aPawn)
  {
    boolean wasAdded = false;
    if (pawns.contains(aPawn)) { return false; }
    if (numberOfPawns() >= maximumNumberOfPawns())
    {
      return wasAdded;
    }

    Game existingGame = aPawn.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPawns() <= minimumNumberOfPawns())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPawn.setGame(this);
    }
    else
    {
      pawns.add(aPawn);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePawn(Pawn aPawn)
  {
    boolean wasRemoved = false;
    //Unable to remove aPawn, as it must always have a game
    if (this.equals(aPawn.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (2)
    if (numberOfPawns() <= minimumNumberOfPawns())
    {
      return wasRemoved;
    }
    pawns.remove(aPawn);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfWallsValid()
  {
    boolean isValid = numberOfWalls() >= minimumNumberOfWalls() && numberOfWalls() <= maximumNumberOfWalls();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfWalls()
  {
    return 20;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfWalls()
  {
    return 20;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfWalls()
  {
    return 20;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Wall addWall(Wall.Orientation aOrientation, Pawn aOwner, Tile aCurrentPosition)
  {
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return null;
    }
    else
    {
      return new Wall(aOrientation, this, aOwner, aCurrentPosition);
    }
  }

  public boolean addWall(Wall aWall)
  {
    boolean wasAdded = false;
    if (walls.contains(aWall)) { return false; }
    if (numberOfWalls() >= maximumNumberOfWalls())
    {
      return wasAdded;
    }

    Game existingGame = aWall.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfWalls() <= minimumNumberOfWalls())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aWall.setGame(this);
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
    //Unable to remove aWall, as it must always have a game
    if (this.equals(aWall.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (20)
    if (numberOfWalls() <= minimumNumberOfWalls())
    {
      return wasRemoved;
    }
    walls.remove(aWall);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTilesValid()
  {
    boolean isValid = numberOfTiles() >= minimumNumberOfTiles() && numberOfTiles() <= maximumNumberOfTiles();
    return isValid;
  }
  /* Code from template association_RequiredNumberOfMethod */
  public static int requiredNumberOfTiles()
  {
    return 81;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTiles()
  {
    return 81;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfTiles()
  {
    return 81;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Tile addTile(int aRow, Character aColumn)
  {
    if (numberOfTiles() >= maximumNumberOfTiles())
    {
      return null;
    }
    else
    {
      return new Tile(aRow, aColumn, this);
    }
  }

  public boolean addTile(Tile aTile)
  {
    boolean wasAdded = false;
    if (tiles.contains(aTile)) { return false; }
    if (numberOfTiles() >= maximumNumberOfTiles())
    {
      return wasAdded;
    }

    Game existingGame = aTile.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfTiles() <= minimumNumberOfTiles())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aTile.setGame(this);
    }
    else
    {
      tiles.add(aTile);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTile(Tile aTile)
  {
    boolean wasRemoved = false;
    //Unable to remove aTile, as it must always have a game
    if (this.equals(aTile.getGame()))
    {
      return wasRemoved;
    }

    //game already at minimum (81)
    if (numberOfTiles() <= minimumNumberOfTiles())
    {
      return wasRemoved;
    }
    tiles.remove(aTile);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSteps()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Step addStep(int aStepNo, String aLog, Step.StepType aStepType, Tile aTile1, Tile aTile2, GameItem aStepItem)
  {
    return new Step(aStepNo, aLog, aStepType, this, aTile1, aTile2, aStepItem);
  }

  public boolean addStep(Step aStep)
  {
    boolean wasAdded = false;
    if (steps.contains(aStep)) { return false; }
    Game existingGame = aStep.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);
    if (isNewGame)
    {
      aStep.setGame(this);
    }
    else
    {
      steps.add(aStep);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeStep(Step aStep)
  {
    boolean wasRemoved = false;
    //Unable to remove aStep, as it must always have a game
    if (!this.equals(aStep.getGame()))
    {
      steps.remove(aStep);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addStepAt(Step aStep, int index)
  {  
    boolean wasAdded = false;
    if(addStep(aStep))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSteps()) { index = numberOfSteps() - 1; }
      steps.remove(aStep);
      steps.add(index, aStep);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveStepAt(Step aStep, int index)
  {
    boolean wasAdded = false;
    if(steps.contains(aStep))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSteps()) { index = numberOfSteps() - 1; }
      steps.remove(aStep);
      steps.add(index, aStep);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addStepAt(aStep, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setCurrentStep(Step aNewCurrentStep)
  {
    boolean wasSet = false;
    currentStep = aNewCurrentStep;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    QuoridorSystem placeholderQSystem = qSystem;
    this.qSystem = null;
    if(placeholderQSystem != null)
    {
      placeholderQSystem.removeGame(this);
    }
    while (pawns.size() > 0)
    {
      Pawn aPawn = pawns.get(pawns.size() - 1);
      aPawn.delete();
      pawns.remove(aPawn);
    }
    
    while (walls.size() > 0)
    {
      Wall aWall = walls.get(walls.size() - 1);
      aWall.delete();
      walls.remove(aWall);
    }
    
    while (tiles.size() > 0)
    {
      Tile aTile = tiles.get(tiles.size() - 1);
      aTile.delete();
      tiles.remove(aTile);
    }
    
    while (steps.size() > 0)
    {
      Step aStep = steps.get(steps.size() - 1);
      aStep.delete();
      steps.remove(aStep);
    }
    
    currentStep = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameId" + ":" + getGameId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameState" + "=" + (getGameState() != null ? !getGameState().equals(this)  ? getGameState().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "thinkingTime" + "=" + (getThinkingTime() != null ? !getThinkingTime().equals(this)  ? getThinkingTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "qSystem = "+(getQSystem()!=null?Integer.toHexString(System.identityHashCode(getQSystem())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "currentStep = "+(getCurrentStep()!=null?Integer.toHexString(System.identityHashCode(getCurrentStep())):"null");
  }
}