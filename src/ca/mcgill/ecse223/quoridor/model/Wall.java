/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 16 "../Model.ump"
// line 34 "../Model.ump"
// line 71 "../Model.ump"
public class Wall extends BoardItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Wall Attributes
  private boolean isAvailable;

  //Wall Associations
  private Pawn pawn;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Wall(String aCurrentPosition, boolean aIsAvailable, Pawn aPawn)
  {
    super(aCurrentPosition);
    isAvailable = aIsAvailable;
    boolean didAddPawn = setPawn(aPawn);
    if (!didAddPawn)
    {
      throw new RuntimeException("Unable to create wall due to pawn");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setIsAvailable(boolean aIsAvailable)
  {
    boolean wasSet = false;
    isAvailable = aIsAvailable;
    wasSet = true;
    return wasSet;
  }

  public boolean getIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsAvailable()
  {
    return isAvailable;
  }
  /* Code from template association_GetOne */
  public Pawn getPawn()
  {
    return pawn;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setPawn(Pawn aPawn)
  {
    boolean wasSet = false;
    //Must provide pawn to wall
    if (aPawn == null)
    {
      return wasSet;
    }

    //pawn already at maximum (10)
    if (aPawn.numberOfWalls() >= Pawn.maximumNumberOfWalls())
    {
      return wasSet;
    }
    
    Pawn existingPawn = pawn;
    pawn = aPawn;
    if (existingPawn != null && !existingPawn.equals(aPawn))
    {
      boolean didRemove = existingPawn.removeWall(this);
      if (!didRemove)
      {
        pawn = existingPawn;
        return wasSet;
      }
    }
    pawn.addWall(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Pawn placeholderPawn = pawn;
    this.pawn = null;
    if(placeholderPawn != null)
    {
      placeholderPawn.removeWall(this);
    }
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "isAvailable" + ":" + getIsAvailable()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "pawn = "+(getPawn()!=null?Integer.toHexString(System.identityHashCode(getPawn())):"null");
  }
}