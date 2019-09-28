/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 34 "../../../../../Model.ump"
// line 86 "../../../../../Model.ump"
// line 98 "../../../../../Model.ump"
public class JumpStep extends Step
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //JumpStep Attributes
  private int jumpedRow;
  private Character jumpedColumn;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public JumpStep(String aLog, Tile aEarlierState, int aJumpedRow, Character aJumpedColumn)
  {
    super(aLog, aEarlierState);
    jumpedRow = aJumpedRow;
    jumpedColumn = aJumpedColumn;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setJumpedRow(int aJumpedRow)
  {
    boolean wasSet = false;
    jumpedRow = aJumpedRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setJumpedColumn(Character aJumpedColumn)
  {
    boolean wasSet = false;
    jumpedColumn = aJumpedColumn;
    wasSet = true;
    return wasSet;
  }

  public int getJumpedRow()
  {
    return jumpedRow;
  }

  public Character getJumpedColumn()
  {
    return jumpedColumn;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "jumpedRow" + ":" + getJumpedRow()+ "," +
            "jumpedColumn" + ":" + getJumpedColumn()+ "]";
  }
}