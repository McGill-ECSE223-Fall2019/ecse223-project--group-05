/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.model;
import java.util.*;

// line 77 "../../../../../Model.ump"
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
  private List<Tile> tiles;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Step(String aLog)
  {
    log = aLog;
    tiles = new ArrayList<Tile>();
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
  /* Code from template association_GetMany */
  public Tile getTile(int index)
  {
    Tile aTile = tiles.get(index);
    return aTile;
  }

  public List<Tile> getTiles()
  {
    List<Tile> newTiles = Collections.unmodifiableList(tiles);
    return newTiles;
  }

  public int numberOfTiles()
  {
    int number = tiles.size();
    return number;
  }

  public boolean hasTiles()
  {
    boolean has = tiles.size() > 0;
    return has;
  }

  public int indexOfTile(Tile aTile)
  {
    int index = tiles.indexOf(aTile);
    return index;
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
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfTilesValid()
  {
    boolean isValid = numberOfTiles() >= minimumNumberOfTiles();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTiles()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Tile addTile(int aRow, Character aColumn, Game aGame)
  {
    Tile aNewTile = new Tile(aRow, aColumn, aGame, this);
    return aNewTile;
  }

  public boolean addTile(Tile aTile)
  {
    boolean wasAdded = false;
    if (tiles.contains(aTile)) { return false; }
    Step existingStep = aTile.getStep();
    boolean isNewStep = existingStep != null && !this.equals(existingStep);

    if (isNewStep && existingStep.numberOfTiles() <= minimumNumberOfTiles())
    {
      return wasAdded;
    }
    if (isNewStep)
    {
      aTile.setStep(this);
    }
    else
    {
      tiles.add(aTile);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTile(Tile aTile)
  {
    boolean wasRemoved = false;
    //Unable to remove aTile, as it must always have a step
    if (this.equals(aTile.getStep()))
    {
      return wasRemoved;
    }

    //step already at minimum (1)
    if (numberOfTiles() <= minimumNumberOfTiles())
    {
      return wasRemoved;
    }

    tiles.remove(aTile);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addTileAt(Tile aTile, int index)
  {  
    boolean wasAdded = false;
    if(addTile(aTile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTiles()) { index = numberOfTiles() - 1; }
      tiles.remove(aTile);
      tiles.add(index, aTile);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTileAt(Tile aTile, int index)
  {
    boolean wasAdded = false;
    if(tiles.contains(aTile))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfTiles()) { index = numberOfTiles() - 1; }
      tiles.remove(aTile);
      tiles.add(index, aTile);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addTileAt(aTile, index);
    }
    return wasAdded;
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
    for(int i=tiles.size(); i > 0; i--)
    {
      Tile aTile = tiles.get(i - 1);
      aTile.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "log" + ":" + getLog()+ "]";
  }
}