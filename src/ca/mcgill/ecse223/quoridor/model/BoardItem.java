/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 26 "../../../../../Model.ump"
public abstract class BoardItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BoardItem Attributes
  private int row;
  private Character column;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BoardItem(int aRow, Character aColumn)
  {
    row = aRow;
    column = aColumn;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRow(int aRow)
  {
    boolean wasSet = false;
    row = aRow;
    wasSet = true;
    return wasSet;
  }

  public boolean setColumn(Character aColumn)
  {
    boolean wasSet = false;
    column = aColumn;
    wasSet = true;
    return wasSet;
  }

  public int getRow()
  {
    return row;
  }

  public Character getColumn()
  {
    return column;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "row" + ":" + getRow()+ "," +
            "column" + ":" + getColumn()+ "]";
  }
}