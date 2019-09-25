/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 13 "../Model.ump"
// line 55 "../Model.ump"
public class BoardItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BoardItem Attributes
  private String currentPosition;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BoardItem(String aCurrentPosition)
  {
    currentPosition = aCurrentPosition;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCurrentPosition(String aCurrentPosition)
  {
    boolean wasSet = false;
    currentPosition = aCurrentPosition;
    wasSet = true;
    return wasSet;
  }

  public String getCurrentPosition()
  {
    return currentPosition;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "currentPosition" + ":" + getCurrentPosition()+ "]";
  }
}