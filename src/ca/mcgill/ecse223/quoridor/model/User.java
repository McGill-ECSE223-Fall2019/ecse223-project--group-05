/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;

// line 17 "../../../../../Model.ump"
public class User
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //User Attributes
  private String userName;
  private String password;

  //User Associations
  private List<Pawn> pawn;
  private QuoridorSystem qSystem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public User(String aUserName, String aPassword, QuoridorSystem aQSystem)
  {
    userName = aUserName;
    password = aPassword;
    pawn = new ArrayList<Pawn>();
    boolean didAddQSystem = setQSystem(aQSystem);
    if (!didAddQSystem)
    {
      throw new RuntimeException("Unable to create player due to qSystem");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setUserName(String aUserName)
  {
    boolean wasSet = false;
    userName = aUserName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getUserName()
  {
    return userName;
  }

  public String getPassword()
  {
    return password;
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
  /* Code from template association_GetOne */
  public QuoridorSystem getQSystem()
  {
    return qSystem;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPawn()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Pawn addPawn(int aRow, Character aColumn, Pawn.Color aColor, String aLastPosition, Game aGame)
  {
    return new Pawn(aRow, aColumn, aColor, aLastPosition, this, aGame);
  }

  public boolean addPawn(Pawn aPawn)
  {
    boolean wasAdded = false;
    if (pawn.contains(aPawn)) { return false; }
    User existingPlayer = aPawn.getPlayer();
    boolean isNewPlayer = existingPlayer != null && !this.equals(existingPlayer);
    if (isNewPlayer)
    {
      aPawn.setPlayer(this);
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
    //Unable to remove aPawn, as it must always have a player
    if (!this.equals(aPawn.getPlayer()))
    {
      pawn.remove(aPawn);
      wasRemoved = true;
    }
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
      existingQSystem.removePlayer(this);
    }
    qSystem.addPlayer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=pawn.size(); i > 0; i--)
    {
      Pawn aPawn = pawn.get(i - 1);
      aPawn.delete();
    }
    QuoridorSystem placeholderQSystem = qSystem;
    this.qSystem = null;
    if(placeholderQSystem != null)
    {
      placeholderQSystem.removePlayer(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "userName" + ":" + getUserName()+ "," +
            "password" + ":" + getPassword()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "qSystem = "+(getQSystem()!=null?Integer.toHexString(System.identityHashCode(getQSystem())):"null");
  }
}