/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;

// line 33 "../../../../../Model.ump"
// line 81 "../../../../../Model.ump"
// line 97 "../../../../../Model.ump"
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

  public WallPlacement(String aLog, Orientation aOrientation)
  {
    super(aLog);
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