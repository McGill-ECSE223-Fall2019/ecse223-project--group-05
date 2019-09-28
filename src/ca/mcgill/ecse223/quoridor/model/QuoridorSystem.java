/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;
import java.sql.Date;
import java.sql.Time;

// line 45 "../../../../../Model.ump"
public class QuoridorSystem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //QuoridorSystem Associations
  private List<Game> game;
  private List<User> player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public QuoridorSystem()
  {
    game = new ArrayList<Game>();
    player = new ArrayList<User>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Game getGame(int index)
  {
    Game aGame = game.get(index);
    return aGame;
  }

  public List<Game> getGame()
  {
    List<Game> newGame = Collections.unmodifiableList(game);
    return newGame;
  }

  public int numberOfGame()
  {
    int number = game.size();
    return number;
  }

  public boolean hasGame()
  {
    boolean has = game.size() > 0;
    return has;
  }

  public int indexOfGame(Game aGame)
  {
    int index = game.indexOf(aGame);
    return index;
  }
  /* Code from template association_GetMany */
  public User getPlayer(int index)
  {
    User aPlayer = player.get(index);
    return aPlayer;
  }

  public List<User> getPlayer()
  {
    List<User> newPlayer = Collections.unmodifiableList(player);
    return newPlayer;
  }

  public int numberOfPlayer()
  {
    int number = player.size();
    return number;
  }

  public boolean hasPlayer()
  {
    boolean has = player.size() > 0;
    return has;
  }

  public int indexOfPlayer(User aPlayer)
  {
    int index = player.indexOf(aPlayer);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGame()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Game addGame(Date aStartDate, Time aStartTime, Game.GameState aGameState, int aGameId)
  {
    return new Game(aStartDate, aStartTime, aGameState, aGameId, this);
  }

  public boolean addGame(Game aGame)
  {
    boolean wasAdded = false;
    if (game.contains(aGame)) { return false; }
    QuoridorSystem existingQSystem = aGame.getQSystem();
    boolean isNewQSystem = existingQSystem != null && !this.equals(existingQSystem);
    if (isNewQSystem)
    {
      aGame.setQSystem(this);
    }
    else
    {
      game.add(aGame);
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
      game.remove(aGame);
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
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGameAt(Game aGame, int index)
  {
    boolean wasAdded = false;
    if(game.contains(aGame))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGame()) { index = numberOfGame() - 1; }
      game.remove(aGame);
      game.add(index, aGame);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGameAt(aGame, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPlayer()
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
    if (player.contains(aPlayer)) { return false; }
    QuoridorSystem existingQSystem = aPlayer.getQSystem();
    boolean isNewQSystem = existingQSystem != null && !this.equals(existingQSystem);
    if (isNewQSystem)
    {
      aPlayer.setQSystem(this);
    }
    else
    {
      player.add(aPlayer);
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
      player.remove(aPlayer);
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
      if(index > numberOfPlayer()) { index = numberOfPlayer() - 1; }
      player.remove(aPlayer);
      player.add(index, aPlayer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePlayerAt(User aPlayer, int index)
  {
    boolean wasAdded = false;
    if(player.contains(aPlayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPlayer()) { index = numberOfPlayer() - 1; }
      player.remove(aPlayer);
      player.add(index, aPlayer);
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
    while (game.size() > 0)
    {
      Game aGame = game.get(game.size() - 1);
      aGame.delete();
      game.remove(aGame);
    }
    
    while (player.size() > 0)
    {
      User aPlayer = player.get(player.size() - 1);
      aPlayer.delete();
      player.remove(aPlayer);
    }
    
  }

}