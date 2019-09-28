/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 40 "../../../../../Model.ump"
// line 87 "../../../../../Model.ump"
// line 103 "../../../../../Model.ump"
public class WallPlacement extends Step
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Orientation { Horizontal, Vertical }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //WallPlacement Attributes
  private Orientation orientation;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public WallPlacement(String aLog, Tile aEarlierState, Orientation aOrientation)
  {
    super(aLog, aEarlierState);
    orientation = aOrientation;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setOrientation(Orientation aOrientation)
  {
    boolean wasSet = false;
    orientation = aOrientation;
    wasSet = true;
    return wasSet;
  }

  public Orientation getOrientation()
  {
    return orientation;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "orientation" + "=" + (getOrientation() != null ? !getOrientation().equals(this)  ? getOrientation().toString().replaceAll("  ","    ") : "this" : "null");
  }
}