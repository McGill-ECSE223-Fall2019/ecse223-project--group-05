/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.quoridor.controller;
import java.util.List;
import java.util.ArrayList;
import ca.mcgill.ecse223.quoridor.model.*;

// line 10 "../../../../../PawnStateMachine.ump"
public class PawnBehaviour
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PawnBehaviour State Machines
  public enum PawnSM { positionStates }
  public enum PawnSMPositionStatesRowState { Null, RowState }
  public enum PawnSMPositionStatesRowStateRowState { Null, initialVerticalState, OnNorthBorder, NearNorthBorder, BetweenNorthSouthBorders, NearSouthBorder, OnSouthBorder }
  public enum PawnSMPositionStatesColumnState { Null, ColumnState }
  public enum PawnSMPositionStatesColumnStateColumnState { Null, BetweenWestEastBorders, OnWestBorder, NearWestBorder, NearEastBorder, OnEastBorder }
  private PawnSM pawnSM;
  private PawnSMPositionStatesRowState pawnSMPositionStatesRowState;
  private PawnSMPositionStatesRowStateRowState pawnSMPositionStatesRowStateRowState;
  private PawnSMPositionStatesColumnState pawnSMPositionStatesColumnState;
  private PawnSMPositionStatesColumnStateColumnState pawnSMPositionStatesColumnStateColumnState;

  //PawnBehaviour Associations
  private Game currentGame;
  private Player player;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PawnBehaviour()
  {
    setPawnSMPositionStatesRowState(PawnSMPositionStatesRowState.Null);
    setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
    setPawnSMPositionStatesColumnState(PawnSMPositionStatesColumnState.Null);
    setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.Null);
    setPawnSM(PawnSM.positionStates);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getPawnSMFullName()
  {
    String answer = pawnSM.toString();
    if (pawnSMPositionStatesRowState != PawnSMPositionStatesRowState.Null) { answer += "." + pawnSMPositionStatesRowState.toString(); }
    if (pawnSMPositionStatesRowStateRowState != PawnSMPositionStatesRowStateRowState.Null) { answer += "." + pawnSMPositionStatesRowStateRowState.toString(); }
    if (pawnSMPositionStatesColumnState != PawnSMPositionStatesColumnState.Null) { answer += "." + pawnSMPositionStatesColumnState.toString(); }
    if (pawnSMPositionStatesColumnStateColumnState != PawnSMPositionStatesColumnStateColumnState.Null) { answer += "." + pawnSMPositionStatesColumnStateColumnState.toString(); }
    return answer;
  }

  public PawnSM getPawnSM()
  {
    return pawnSM;
  }

  public PawnSMPositionStatesRowState getPawnSMPositionStatesRowState()
  {
    return pawnSMPositionStatesRowState;
  }

  public PawnSMPositionStatesRowStateRowState getPawnSMPositionStatesRowStateRowState()
  {
    return pawnSMPositionStatesRowStateRowState;
  }

  public PawnSMPositionStatesColumnState getPawnSMPositionStatesColumnState()
  {
    return pawnSMPositionStatesColumnState;
  }

  public PawnSMPositionStatesColumnStateColumnState getPawnSMPositionStatesColumnStateColumnState()
  {
    return pawnSMPositionStatesColumnStateColumnState;
  }

  public boolean entry()
  {
    boolean wasEventProcessed = false;
    
    PawnSMPositionStatesRowStateRowState aPawnSMPositionStatesRowStateRowState = pawnSMPositionStatesRowStateRowState;
    switch (aPawnSMPositionStatesRowStateRowState)
    {
      case initialVerticalState:
        if (playerIsBlack())
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.OnNorthBorder);
          wasEventProcessed = true;
          break;
        }
        if (playerIsWhite())
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.OnSouthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean move(MoveDirection dir)
  {
    boolean wasEventProcessed = false;
    
    PawnSMPositionStatesRowStateRowState aPawnSMPositionStatesRowStateRowState = pawnSMPositionStatesRowStateRowState;
    PawnSMPositionStatesColumnStateColumnState aPawnSMPositionStatesColumnStateColumnState = pawnSMPositionStatesColumnStateColumnState;
    switch (aPawnSMPositionStatesRowStateRowState)
    {
      case OnNorthBorder:
        if (dir.equals(MoveDirection.South)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.NearNorthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearNorthBorder:
        if (dir.equals(MoveDirection.North)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.OnNorthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.South)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case BetweenNorthSouthBorders:
        if (dir.equals(MoveDirection.North)&&getCurrentPawnRow()==3&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.NearNorthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.North)&&getCurrentPawnRow()>=4&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.South)&&getCurrentPawnRow()==7&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.NearSouthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.South)&&getCurrentPawnRow()<=6&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearSouthBorder:
        if (dir.equals(MoveDirection.South)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.OnSouthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.North)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case OnSouthBorder:
        if (dir.equals(MoveDirection.North)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.NearSouthBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMPositionStatesColumnStateColumnState)
    {
      case BetweenWestEastBorders:
        if (dir.equals(MoveDirection.West)&&getCurrentPawnColumn()==3&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.NearWestBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.West)&&getCurrentPawnColumn()>=4&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&getCurrentPawnColumn()==7&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.NearEastBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&getCurrentPawnColumn()<=6&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case OnWestBorder:
        if (dir.equals(MoveDirection.East)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.NearWestBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearWestBorder:
        if (dir.equals(MoveDirection.West)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.OnWestBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearEastBorder:
        if (dir.equals(MoveDirection.West)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.OnEastBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      case OnEastBorder:
        if (dir.equals(MoveDirection.West)&&isLegalStep(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.NearEastBorder);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean jump(MoveDirection dir)
  {
    boolean wasEventProcessed = false;
    
    PawnSMPositionStatesRowStateRowState aPawnSMPositionStatesRowStateRowState = pawnSMPositionStatesRowStateRowState;
    PawnSMPositionStatesColumnStateColumnState aPawnSMPositionStatesColumnStateColumnState = pawnSMPositionStatesColumnStateColumnState;
    switch (aPawnSMPositionStatesRowStateRowState)
    {
      case OnNorthBorder:
        if (dir.equals(MoveDirection.South)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearNorthBorder:
        if (dir.equals(MoveDirection.South)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case BetweenNorthSouthBorders:
        if (dir.equals(MoveDirection.North)&&getCurrentPawnRow()==3&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.OnNorthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.North)&&getCurrentPawnRow()==4&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.NearNorthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.North)&&getCurrentPawnRow()>=5&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.South)&&getCurrentPawnRow()==7&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.OnSouthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.South)&&getCurrentPawnRow()==6&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.NearSouthBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.South)&&getCurrentPawnRow()<=5&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearSouthBorder:
        if (dir.equals(MoveDirection.North)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case OnSouthBorder:
        if (dir.equals(MoveDirection.North)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesRowStateRowState();
          setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.BetweenNorthSouthBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aPawnSMPositionStatesColumnStateColumnState)
    {
      case BetweenWestEastBorders:
        if (dir.equals(MoveDirection.West)&&getCurrentPawnColumn()==3&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.OnWestBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.West)&&getCurrentPawnColumn()==4&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.NearWestBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.West)&&getCurrentPawnColumn()>=5&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&getCurrentPawnColumn()==7&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.OnEastBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&getCurrentPawnColumn()==6&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.NearEastBorder);
          wasEventProcessed = true;
          break;
        }
        if (dir.equals(MoveDirection.East)&&getCurrentPawnColumn()<=5&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case OnWestBorder:
        if (dir.equals(MoveDirection.East)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearWestBorder:
        if (dir.equals(MoveDirection.East)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case NearEastBorder:
        if (dir.equals(MoveDirection.West)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      case OnEastBorder:
        if (dir.equals(MoveDirection.West)&&isLegalJump(dir))
        {
          exitPawnSMPositionStatesColumnStateColumnState();
          setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitPawnSM()
  {
    switch(pawnSM)
    {
      case positionStates:
        exitPawnSMPositionStatesRowState();
        exitPawnSMPositionStatesColumnState();
        break;
    }
  }

  private void setPawnSM(PawnSM aPawnSM)
  {
    pawnSM = aPawnSM;

    // entry actions and do activities
    switch(pawnSM)
    {
      case positionStates:
        if (pawnSMPositionStatesRowState == PawnSMPositionStatesRowState.Null) { setPawnSMPositionStatesRowState(PawnSMPositionStatesRowState.RowState); }
        if (pawnSMPositionStatesColumnState == PawnSMPositionStatesColumnState.Null) { setPawnSMPositionStatesColumnState(PawnSMPositionStatesColumnState.ColumnState); }
        break;
    }
  }

  private void exitPawnSMPositionStatesRowState()
  {
    switch(pawnSMPositionStatesRowState)
    {
      case RowState:
        exitPawnSMPositionStatesRowStateRowState();
        setPawnSMPositionStatesRowState(PawnSMPositionStatesRowState.Null);
        break;
    }
  }

  private void setPawnSMPositionStatesRowState(PawnSMPositionStatesRowState aPawnSMPositionStatesRowState)
  {
    pawnSMPositionStatesRowState = aPawnSMPositionStatesRowState;
    if (pawnSM != PawnSM.positionStates && aPawnSMPositionStatesRowState != PawnSMPositionStatesRowState.Null) { setPawnSM(PawnSM.positionStates); }

    // entry actions and do activities
    switch(pawnSMPositionStatesRowState)
    {
      case RowState:
        if (pawnSMPositionStatesRowStateRowState == PawnSMPositionStatesRowStateRowState.Null) { setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.initialVerticalState); }
        break;
    }
  }

  private void exitPawnSMPositionStatesRowStateRowState()
  {
    switch(pawnSMPositionStatesRowStateRowState)
    {
      case initialVerticalState:
        setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
        break;
      case OnNorthBorder:
        setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
        break;
      case NearNorthBorder:
        setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
        break;
      case BetweenNorthSouthBorders:
        setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
        break;
      case NearSouthBorder:
        setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
        break;
      case OnSouthBorder:
        setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState.Null);
        break;
    }
  }

  private void setPawnSMPositionStatesRowStateRowState(PawnSMPositionStatesRowStateRowState aPawnSMPositionStatesRowStateRowState)
  {
    pawnSMPositionStatesRowStateRowState = aPawnSMPositionStatesRowStateRowState;
    if (pawnSMPositionStatesRowState != PawnSMPositionStatesRowState.RowState && aPawnSMPositionStatesRowStateRowState != PawnSMPositionStatesRowStateRowState.Null) { setPawnSMPositionStatesRowState(PawnSMPositionStatesRowState.RowState); }
  }

  private void exitPawnSMPositionStatesColumnState()
  {
    switch(pawnSMPositionStatesColumnState)
    {
      case ColumnState:
        exitPawnSMPositionStatesColumnStateColumnState();
        setPawnSMPositionStatesColumnState(PawnSMPositionStatesColumnState.Null);
        break;
    }
  }

  private void setPawnSMPositionStatesColumnState(PawnSMPositionStatesColumnState aPawnSMPositionStatesColumnState)
  {
    pawnSMPositionStatesColumnState = aPawnSMPositionStatesColumnState;
    if (pawnSM != PawnSM.positionStates && aPawnSMPositionStatesColumnState != PawnSMPositionStatesColumnState.Null) { setPawnSM(PawnSM.positionStates); }

    // entry actions and do activities
    switch(pawnSMPositionStatesColumnState)
    {
      case ColumnState:
        if (pawnSMPositionStatesColumnStateColumnState == PawnSMPositionStatesColumnStateColumnState.Null) { setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.BetweenWestEastBorders); }
        break;
    }
  }

  private void exitPawnSMPositionStatesColumnStateColumnState()
  {
    switch(pawnSMPositionStatesColumnStateColumnState)
    {
      case BetweenWestEastBorders:
        setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.Null);
        break;
      case OnWestBorder:
        setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.Null);
        break;
      case NearWestBorder:
        setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.Null);
        break;
      case NearEastBorder:
        setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.Null);
        break;
      case OnEastBorder:
        setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState.Null);
        break;
    }
  }

  private void setPawnSMPositionStatesColumnStateColumnState(PawnSMPositionStatesColumnStateColumnState aPawnSMPositionStatesColumnStateColumnState)
  {
    pawnSMPositionStatesColumnStateColumnState = aPawnSMPositionStatesColumnStateColumnState;
    if (pawnSMPositionStatesColumnState != PawnSMPositionStatesColumnState.ColumnState && aPawnSMPositionStatesColumnStateColumnState != PawnSMPositionStatesColumnStateColumnState.Null) { setPawnSMPositionStatesColumnState(PawnSMPositionStatesColumnState.ColumnState); }
  }
  /* Code from template association_GetOne */
  public Game getCurrentGame()
  {
    return currentGame;
  }

  public boolean hasCurrentGame()
  {
    boolean has = currentGame != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setCurrentGame(Game aNewCurrentGame)
  {
    boolean wasSet = false;
    currentGame = aNewCurrentGame;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setPlayer(Player aNewPlayer)
  {
    boolean wasSet = false;
    player = aNewPlayer;
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    currentGame = null;
    player = null;
  }


  /**
   * Returns the current row number of the pawn
   */
  // line 103 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnRow(){
    PlayerPosition currentPosition = getCurrentPlayerPosition();
		return currentPosition.getTile().getRow();
  }


  /**
   * Returns the current column number of the pawn
   */
  // line 108 "../../../../../PawnStateMachine.ump"
  public int getCurrentPawnColumn(){
    PlayerPosition currentPosition = getCurrentPlayerPosition();
		return currentPosition.getTile().getColumn();
  }


  /**
   * Returns if it is legal to step in the given direction
   */
  // line 113 "../../../../../PawnStateMachine.ump"
  public boolean isLegalStep(MoveDirection dir){
    boolean stepWasLegal = true;

	    //Compute useful data
		int initialRow = getCurrentPawnRow();
		int initialCol = getCurrentPawnColumn();
		int finalRow = initialRow;
		int finalCol = initialCol;
		int otherRow = getOtherPawnRow();       //Row of other pawn
		int otherCol = getOtherPawnColumn();    //Column of other pawn
		//Check if the final destination is out of bounds
		if(!moveIsInBounds( initialRow, initialCol, dir )){
		    stepWasLegal =  false;
		}
		//Check if the path is blocked by a wall
        if( pathIsBlockedByWall( initialRow, initialCol, dir ) ){
            stepWasLegal =  false;   //If the move is blocked by a wall
        }
		//Get the final coordinates
		switch( dir ) {
			case East:
				//Compute new position
				finalRow = initialRow;
				finalCol = initialCol + 1;
				break;
			case West:
				//Compute new position
				finalRow = initialRow;
				finalCol = initialCol - 1;
				break;
			case North:
				//Compute new position
				finalRow = initialRow - 1;
				finalCol = initialCol;
				break;
			case South:
				//Compute new position
				finalRow = initialRow + 1;
				finalCol = initialCol;
				break;
		}
		//Check if pawns would conflict with move
		if( finalCol == otherCol && finalRow == otherRow ){
			stepWasLegal =  false;
		}

		//If any test failed, call the illegalMove method
		if( !stepWasLegal ){
		    illegalMove();
		}

		//All tests passed.
		return stepWasLegal;
  }


  /**
   * Returns if it is legal to jump in the given direction
   */
  // line 168 "../../../../../PawnStateMachine.ump"
  public boolean isLegalJump(MoveDirection dir){
    boolean stepWasLegal = true;

		//Compute useful data for calculating
		int initialRow = getCurrentPawnRow();       //Starting row of player's pawn
		int initialCol = getCurrentPawnColumn();       //Starting column of player's pawn
		int otherRow = getOtherPawnRow();           //Current row of other player's pawn
		int otherCol = getOtherPawnColumn();           //Current column of other player's pawn
		int deltaRow = otherRow - initialRow;       //Displacement between other player's pawn and player's pawn in row.
		int deltaCol = otherCol - initialCol;       //Displacement between other player's pawn and player's pawn in column.
		//First check if the enemy pawn is adjacent. If not, return false.
		if( initialRow != otherRow && initialCol != otherCol ){
			stepWasLegal =  false;
		} else if ( deltaRow == 0 ) {
			if( deltaCol != 1 && deltaCol != -1 ){
				stepWasLegal =  false;   //If you're on the same row but too far away from another still.
			}
		} else if ( deltaCol == 0 ) {
			if( deltaRow != 1 && deltaRow != -1 ){
				stepWasLegal =  false;   //If you're on the same column but too far away from another still.
			}
		}
		//Now figure out what direction the adjacent pawn is to our current pawn, and verify it does not counter our dir.
		MoveDirection adjacentPawnDir;
		if( deltaCol == 1 ){
			adjacentPawnDir = MoveDirection.East;
			if( dir == MoveDirection.West ){
				stepWasLegal =  false; //Moving West of the pawn to your East just returns you back to where you are.
			}
		} else if ( deltaRow == 1 ) {
			adjacentPawnDir = MoveDirection.South;
			if( dir == MoveDirection.North ){
				stepWasLegal =  false; //Moving North of the pawn to your South just returns you back to where you are.
			}
		} else if ( deltaCol == -1 ) {
			adjacentPawnDir = MoveDirection.West;
			if( dir == MoveDirection.East ){
				stepWasLegal =  false; //Moving East of the pawn to your West just returns you back to where you are.
			}
		} else if ( deltaRow == -1 ) {
			adjacentPawnDir = MoveDirection.North;
			if( dir == MoveDirection.South) {
				stepWasLegal =  false; //Moving South of the pawn to your North just returns you back to where you are.
			}
		} else {
			throw new IllegalArgumentException("Detected unacceptable MoveDirection.");
		}
		//Now that we know a pawn is adjacent, we need to figure out if there is a wall in between.
		if( pathIsBlockedByWall( initialRow, initialCol, adjacentPawnDir ) ){
			stepWasLegal =  false;   //If the move is prevented by a wall blocking access to the adjacent pawn.
		}
		//Now that we know we are clear, let us now see if moving in the provided direction from the adjacent pawn is blocked by a wall.
		if( pathIsBlockedByWall( otherRow, otherCol, dir ) ){
			stepWasLegal =  false;   //The jump is blocked from the adjacent pawn's position.
		}
		//The check before checks to see if there is a wall in the way to jumping over the opponent, but does not consider that a wall needs to be behind the opponent for jumping to the opp's side.
		//Now we check, if the jump is a valid direction and not to the opposite side, if there was that requisite board.
		if( dir != adjacentPawnDir ){	//If we are jumping to the side of the opponent, not behind
			if( !pathIsBlockedByWall( otherRow, otherCol, adjacentPawnDir ) ) {	//If there is no wall behind the opponent
				stepWasLegal = false;
			}
		}
		//Let's not forget that we need to check that the final destination is on the board.
		if(!moveIsInBounds( otherRow, otherCol, dir )){
		    stepWasLegal =  false;
		}

		//If any of the tests failed, call the illegalMove alerter.
		if( !stepWasLegal ){
		    illegalMove();
		}
		//If all this works, then all tests have passed. Return true for legal jump.
		return true;
  }


  /**
   * Action to be called when an illegal move is attempted
   */
  // line 244 "../../../../../PawnStateMachine.ump"
  public void illegalMove(){
    throw new IllegalArgumentException("Detected an illegal move. Do something.");
  }


  /**
   * THIS ENUMERATION, UNSUPPORTED BY UMPLE, HAS BEEN MANUALLY ADDED INTO THE MODELS FOLDER.
   * Enumeration for the possible moving directions
   * (directions are from the viewpoint of white player)
   * enum MoveDirection { East, South, West, North; }
   * Helper: Gets the current position of the player
   */
  // line 257 "../../../../../PawnStateMachine.ump"
   private PlayerPosition getCurrentPlayerPosition(){
    PlayerPosition playerPosition;
		if( player.getGameAsWhite() != null ){
			playerPosition = player.getGameAsWhite().getCurrentPosition().getWhitePosition();
		} else {
			playerPosition = player.getGameAsBlack().getCurrentPosition().getBlackPosition();
		}
		return playerPosition;
  }


  /**
   * Helper: Gets the current position of the other player
   */
  // line 267 "../../../../../PawnStateMachine.ump"
   private PlayerPosition getCurrentOtherPlayerPosition(){
    PlayerPosition playerPosition;
		if( player.getGameAsWhite() != null ){
			playerPosition = player.getGameAsWhite().getCurrentPosition().getBlackPosition();
		} else {
			playerPosition = player.getGameAsBlack().getCurrentPosition().getWhitePosition();
		}
		return playerPosition;
  }


  /**
   * Helper: Gets the current row number of the other player
   */
  // line 277 "../../../../../PawnStateMachine.ump"
   private int getOtherPawnRow(){
    return getCurrentOtherPlayerPosition().getTile().getRow();
  }


  /**
   * Helper: Gets the current column number of the other player
   */
  // line 281 "../../../../../PawnStateMachine.ump"
   private int getOtherPawnColumn(){
    return getCurrentOtherPlayerPosition().getTile().getColumn();
  }


  /**
   * Helper: Gets all of the walls placed onto the board
   */
  // line 285 "../../../../../PawnStateMachine.ump"
   private List<Wall> getAllWallsOnBoard(){
    List<Wall> placedWalls = new ArrayList<Wall>();
		placedWalls.addAll( currentGame.getCurrentPosition().getBlackWallsOnBoard() );
		placedWalls.addAll( currentGame.getCurrentPosition().getWhiteWallsOnBoard() );
		return placedWalls;
  }


  /**
   * Helper: Returns if a path off of a provided tile, through row and column, towards a direction is blocked by a wall
   */
  // line 292 "../../../../../PawnStateMachine.ump"
   private boolean pathIsBlockedByWall(int row, int col, MoveDirection dir){
    for( Wall wall : getAllWallsOnBoard() ){
			//Check if the wall's orientation is capable of blocking the direction.
			if( wall.getMove().getWallDirection() == Direction.Horizontal ){
				if( dir == MoveDirection.East || dir == MoveDirection.West ){
					continue;   //Skip the horizontal wall which can't block horizontal movement.
				}
			} else {
				if( dir == MoveDirection.North || dir == MoveDirection.South ){
					continue;   //Skip the vertical wall which can't block vertical movement.
				}
			}
			//Compute useful numbers.
			int wallRow = wall.getMove().getTargetTile().getRow();
			int wallCol = wall.getMove().getTargetTile().getColumn();
			int deltaRow = wallRow - row;
			int deltaCol = wallCol - col;
			switch ( dir ) {
				case East:
					//Check if the path to the right of col row is blocked by wall. If so, then flag.
					if( deltaCol == 0 ){
						if( deltaRow == -1 || deltaRow == 0 ){
							return true;    //Return true for path is blocked by wall
						}
					}
					break;
				case West:
					//Check if the path to the left of col row is blocked by wall. If so, then flag.
					if( deltaCol == -1 ){
						if( deltaRow == -1 || deltaRow == 0 ){
							return true;    //Return true for path is blocked by wall
						}
					}
					break;
				case North:
					//Check if the path to the top of col row is blocked by wall. If so, then flag.
					if( deltaRow == -1 ){
						if( deltaCol == -1 || deltaCol == 0 ){
							return true;    //Return true for path is blocked by wall
						}
					}
					break;
				case South:
					//Check if the path to the bottom of col row is blocked by wall. If so, then flag.
					if( deltaRow == 0 ){
						if( deltaCol == -1 || deltaCol == 0 ){
							return true;    //Return true for path is blocked by wall
						}
					}
					break;
			}
		}
		//No walls were detected to be blocking its path; return false for path is blocked by wall.
		return false;
  }


  /**
   * Helper: Returns if a move in the dir off of the provided tile coordinates is valid
   */
  // line 348 "../../../../../PawnStateMachine.ump"
   private boolean moveIsInBounds(int initialRow, int initialCol, MoveDirection dir){
    int finalRow;
		int finalCol;
		switch( dir ) {
			case East:
				//Check if the player is already on the east border
				if( initialCol == 9 ){
					return false;
				}
				break;
			case West:
				//Check if the player is already on the west border
				if( initialCol == 0 ){
					return false;
				}
				break;
			case North:
				//Check if the player is already on the north border
				if( initialRow == 0 ){
					return false;
				}
				break;
			case South:
				//Check if the player is already on the south border
				if( initialRow == 9 ){
					return false;
				}
				break;
		}
		//All tests passed.
		return true;
  }


  /**
   * Helper method for knowing if the player has a white pawn
   */
  // line 381 "../../../../../PawnStateMachine.ump"
   private boolean playerIsWhite(){
    return player.getGameAsWhite() != null;
  }


  /**
   * Helper method for knowing if the player has a black pawn
   */
  // line 385 "../../../../../PawnStateMachine.ump"
   private boolean playerIsBlack(){
    return player.getGameAsBlack() != null;
  }

}