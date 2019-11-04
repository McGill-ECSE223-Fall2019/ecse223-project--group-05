/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.io.Serializable;

// line 55 "../../../../../QuoridorGamePersistence.ump"
// line 82 "../../../../../QuoridorGame.ump"
public class StepMove extends Move implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public StepMove(int aMoveNumber, int aRoundNumber, Player aPlayer, Tile aTargetTile, Game aGame)
  {
    super(aMoveNumber, aRoundNumber, aPlayer, aTargetTile, aGame);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 58 "../../../../../QuoridorGamePersistence.ump"
  private static final long serialVersionUID = 0L ;

  
}