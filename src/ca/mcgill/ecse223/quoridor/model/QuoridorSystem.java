/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 28 "../../../../../Model.ump"
public class QuoridorSystem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //QuoridorSystem Attributes
  private String quoridorSystemId;

  //QuoridorSystem Associations
  private List<Game> games;
  private List<User> players;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public QuoridorSystem(String aQuoridorSystemId)
  {
    quoridorSystemId = aQuoridorSystemId;
    games = new ArrayList<Game>();
    players = new ArrayList<User>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setQuoridorSystemId(String aQuoridorSystemId)
  {
    boolean wasSet = false;
    quoridorSystemId = aQuoridorSystemId;
    wasSet = true;
    return wasSet;
  }

  public String getQuoridorSystemId()
  {
    return quoridorSystemId;
  }
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = games.get(index);
    return aGame;
  }

  public List<Game> getGames()
  {
    List<Game> newGames = Collections.unmodifiableList(games);
    return newGames;
  }

  public int numberOfGames()
  {
    int number = games.size();
    return number;
  }

  public boolean hasGames()
  {
    boolean has = games.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = games.indexOf(aGame);
    return index;
  }
  /* Code from template association_GetMany */
  public User getPlayer(int index)
  {
    User aPlayer = players.get(index);
    return aPlayer;
  }

  public List<User> getPlayers()
  {
    List<User> newPlayers = Collections.unmodifiableList(players);
    return newPlayers;
  }

  public int numberOfPlayers()
  {
    int number = players.size();
    return number;
  }

  public boolean hasPlayers()
  {
    boolean has = players.size() > 0;
    return has;
  }

  public int indexOfPlayer(User aPlayer)
  {
    int index = players.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGames()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Game addGame(String aGameId, Date aStartDate, Time aStartTime, Game.GameState aGameState, Time aThinkingTime)
  {
    return new Game(aGameId, aStartDate, aStartTime, aGameState, aThinkingTime, this);
  }

  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (games.contains(aGame)) { return false; }
    QuoridorSystem existingQSystem = aGame.getQSystem();
    boolean isNewQSystem = existingQSystem != null && !this.equals(existingQSystem);
    if (isNewQSystem)
    {
      aGame.setQSystem(this);
    }
    else
    {
      games.add(aGame);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGame(Game aGame)
  {
    boolean wasRemoved = false;
    //Unable to remove aGame, as it must always have a qSystem
    if (!this.equals(aGame.getQSystem()))
    {
      games.remove(aGame);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGameAt(Game aGame, int index)
  {  
    boolean wasAdded = false;
    if(addGame(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGames()) { index = numberOfGames() - 1; }
      games.remove(aGame);
      games.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(games.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGames()) { index = numberOfGames() - 1; }
      games.remove(aGame);
      games.add(index, aGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameAt(aGame, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public User addPlayer(String aUserName, String aPassword)
  {
    return new User(aUserName, aPassword, this);
  }

  public boolean addPlayer(User aPlayer)
  {
    boolean wasAdded = false;
    if (players.contains(aPlayer)) { return false; }
    QuoridorSystem existingQSystem = aPlayer.getQSystem();
    boolean isNewQSystem = existingQSystem != null && !this.equals(existingQSystem);
    if (isNewQSystem)
    {
      aPlayer.setQSystem(this);
    }
    else
    {
      players.add(aPlayer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePlayer(User aPlayer)
  {
    boolean wasRemoved = false;
    //Unable to remove aPlayer, as it must always have a qSystem
    if (!this.equals(aPlayer.getQSystem()))
    {
      players.remove(aPlayer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPlayerAt(User aPlayer, int index)
  {  
    boolean wasAdded = false;
    if(addPlayer(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlayerAt(User aPlayer, int index)
  {
    boolean wasAdded = false;
    if(players.contains(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayers()) { index = numberOfPlayers() - 1; }
      players.remove(aPlayer);
      players.add(index, aPlayer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPlayerAt(aPlayer, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (games.size() > 0)
    {
      Game aGame = games.get(games.size() - 1);
      aGame.delete();
      games.remove(aGame);
    }
    
    while (players.size() > 0)
    {
      User aPlayer = players.get(players.size() - 1);
      aPlayer.delete();
      players.remove(aPlayer);
    }
    
  }


  public String toString()
  {
    return super.toString() + "["+
            "quoridorSystemId" + ":" + getQuoridorSystemId()+ "]";
  }
}