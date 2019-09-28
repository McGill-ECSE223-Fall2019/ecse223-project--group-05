/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;

// line 83 "../../../../../Model.ump"
public class Step
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Step Attributes
  private String log;

  //Step Associations
  private Step next;
  private Step prev;
  private Tile earlierState;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Step(String aLog, Tile aEarlierState)
  {
    log = aLog;
    if (!setEarlierState(aEarlierState))
    {
      throw new RuntimeException("Unable to create Step due to aEarlierState");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setLog(String aLog)
  {
    boolean wasSet = false;
    log = aLog;
    wasSet = true;
    return wasSet;
  }

  public String getLog()
  {
    return log;
  }
  /* Code from template association_GetOne */
  public Step getNext()
  {
    return next;
  }

  public boolean hasNext()
  {
    boolean has = next != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Step getPrev()
  {
    return prev;
  }

  public boolean hasPrev()
  {
    boolean has = prev != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Tile getEarlierState()
  {
    return earlierState;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setNext(Step aNewNext)
  {
    boolean wasSet = false;
    if (aNewNext == null)
    {
      Step existingNext = next;
      next = null;
      
      if (existingNext != null && existingNext.getPrev() != null)
      {
        existingNext.setPrev(null);
      }
      wasSet = true;
      return wasSet;
    }

    Step currentNext = getNext();
    if (currentNext != null && !currentNext.equals(aNewNext))
    {
      currentNext.setPrev(null);
    }

    next = aNewNext;
    Step existingPrev = aNewNext.getPrev();

    if (!equals(existingPrev))
    {
      aNewNext.setPrev(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setPrev(Step aNewPrev)
  {
    boolean wasSet = false;
    if (aNewPrev == null)
    {
      Step existingPrev = prev;
      prev = null;
      
      if (existingPrev != null && existingPrev.getNext() != null)
      {
        existingPrev.setNext(null);
      }
      wasSet = true;
      return wasSet;
    }

    Step currentPrev = getPrev();
    if (currentPrev != null && !currentPrev.equals(aNewPrev))
    {
      currentPrev.setNext(null);
    }

    prev = aNewPrev;
    Step existingNext = aNewPrev.getNext();

    if (!equals(existingNext))
    {
      aNewPrev.setNext(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setEarlierState(Tile aNewEarlierState)
  {
    boolean wasSet = false;
    if (aNewEarlierState != null)
    {
      earlierState = aNewEarlierState;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    if (next != null)
    {
      next.setPrev(null);
    }
    if (prev != null)
    {
      prev.setNext(null);
    }
    earlierState = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "log" + ":" + getLog()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "earlierState = "+(getEarlierState()!=null?Integer.toHexString(System.identityHashCode(getEarlierState())):"null");
  }
}