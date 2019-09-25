/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

// line 20 "../Model.ump"
// line 60 "../Model.ump"
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
  private Date startDate;
  private Time startTime;
  private GameState gameState;
  private int gameId;

  //Game Associations
  private QuoridorSystem qSystem;
  private List<Pawn> pawn;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Game(Date aStartDate, Time aStartTime, GameState aGameState, int aGameId, QuoridorSystem aQSystem)
  {
    startDate = aStartDate;
    startTime = aStartTime;
    gameState = aGameState;
    gameId = aGameId;
    boolean didAddQSystem = setQSystem(aQSystem);
    if (!didAddQSystem)
    {
      throw new RuntimeException("Unable to create game due to qSystem");
    }
    pawn = new ArrayList<Pawn>();
  }

  //------------------------
  // INTERFACE
  //------------------------

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

  public boolean setGameId(int aGameId)
  {
    boolean wasSet = false;
    gameId = aGameId;
    wasSet = true;
    return wasSet;
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

  public int getGameId()
  {
    return gameId;
  }
  /* Code from template association_GetOne */
  public QuoridorSystem getQSystem()
  {
    return qSystem;
  }
  /* Code from template association_GetMany */
  public Pawn getPawn(int index)
  {
    Pawn aPawn = pawn.get(index);
    return aPawn;
  }

  public List<Pawn> getPawn()
  {
    List<Pawn> newPawn = Collections.unmodifiableList(pawn);
    return newPawn;
  }

  public int numberOfPawn()
  {
    int number = pawn.size();
    return number;
  }

  public boolean hasPawn()
  {
    boolean has = pawn.size() > 0;
    return has;
  }

  public int indexOfPawn(Pawn aPawn)
  {
    int index = pawn.indexOf(aPawn);
    return index;
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
  public boolean isNumberOfPawnValid()
  {
    boolean isValid = numberOfPawn() >= minimumNumberOfPawn() && numberOfPawn() <= maximumNumberOfPawn();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPawn()
  {
    return 2;
  }
  /* Code from template association_MaximumNumberOfMethod */
  public static int maximumNumberOfPawn()
  {
    return 4;
  }
  /* Code from template association_AddMNToOnlyOne */
  public Pawn addPawn(String aCurrentPosition, String aLastPosition, User aPlayer)
  {
    if (numberOfPawn() >= maximumNumberOfPawn())
    {
      return null;
    }
    else
    {
      return new Pawn(aCurrentPosition, aLastPosition, aPlayer, this);
    }
  }

  public boolean addPawn(Pawn aPawn)
  {
    boolean wasAdded = false;
    if (pawn.contains(aPawn)) { return false; }
    if (numberOfPawn() >= maximumNumberOfPawn())
    {
      return wasAdded;
    }

    Game existingGame = aPawn.getGame();
    boolean isNewGame = existingGame != null && !this.equals(existingGame);

    if (isNewGame && existingGame.numberOfPawn() <= minimumNumberOfPawn())
    {
      return wasAdded;
    }

    if (isNewGame)
    {
      aPawn.setGame(this);
    }
    else
    {
      pawn.add(aPawn);
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
    if (numberOfPawn() <= minimumNumberOfPawn())
    {
      return wasRemoved;
    }
    pawn.remove(aPawn);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPawnAt(Pawn aPawn, int index)
  {  
    boolean wasAdded = false;
    if(addPawn(aPawn))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPawn()) { index = numberOfPawn() - 1; }
      pawn.remove(aPawn);
      pawn.add(index, aPawn);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePawnAt(Pawn aPawn, int index)
  {
    boolean wasAdded = false;
    if(pawn.contains(aPawn))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPawn()) { index = numberOfPawn() - 1; }
      pawn.remove(aPawn);
      pawn.add(index, aPawn);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPawnAt(aPawn, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    QuoridorSystem placeholderQSystem = qSystem;
    this.qSystem = null;
    if(placeholderQSystem != null)
    {
      placeholderQSystem.removeGame(this);
    }
    while (pawn.size() > 0)
    {
      Pawn aPawn = pawn.get(pawn.size() - 1);
      aPawn.delete();
      pawn.remove(aPawn);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "gameId" + ":" + getGameId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "gameState" + "=" + (getGameState() != null ? !getGameState().equals(this)  ? getGameState().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "qSystem = "+(getQSystem()!=null?Integer.toHexString(System.identityHashCode(getQSystem())):"null");
  }
}