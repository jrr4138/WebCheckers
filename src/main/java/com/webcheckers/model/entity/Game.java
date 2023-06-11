package com.webcheckers.model.entity;

import com.webcheckers.application.BoardController;
import com.webcheckers.application.MovePiece;
import com.webcheckers.model.board.*;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.entity.Player;
import com.webcheckers.model.states.GameState;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.LinkedList;

import static com.webcheckers.application.MovePiece.*;

/**
 * Represents a checkers game
 *
 */
public class Game implements Iterable<Move> {
  private Player redPlayer;
  private Player whitePlayer;
  private PieceColor activeColor;
  private Board board;
  private TurnTracker turnTracker;
  private GameState gameState;
  private boolean isGameAlive = true;
  private ArrayList<LinkedList<Move>> potentialJumpMoves;
  private ArrayList<Player> spectators;


  /**
   * Creates a new instance of the game object
   *
   * @param redPlayer
   * @param whitePlayer
   */
  public Game(Player redPlayer, Player whitePlayer) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.activeColor = PieceColor.RED;
    this.board = new Board();
    turnTracker = new TurnTracker(board);
    this.gameState = GameState.ACTIVE;
    this.spectators = new ArrayList<>();
    this.potentialJumpMoves = new ArrayList<>();
  }

  /**
   * Creates a new instance of the game object with specific board
   *
   * @param redPlayer
   * @param whitePlayer
   */
  public Game(Player redPlayer, Player whitePlayer, String str) {
    this.redPlayer = redPlayer;
    this.whitePlayer = whitePlayer;
    this.activeColor = PieceColor.RED;
    this.board = new Board(str);
    turnTracker = new TurnTracker(board);
    this.gameState = GameState.ACTIVE;
    this.spectators = new ArrayList<>();
    this.potentialJumpMoves = new ArrayList<>();
  }

  /**
   * Gets the red player data
   *
   * @return red player object
   */
  public Player getRedPlayer() {
    return this.redPlayer;
  }

  /**
   * Sets the red player
   *
   * @param redPlayer player obj
   */
  public void setRedPlayer(Player redPlayer) {
    this.redPlayer = redPlayer;
  }

  /**
   * Gets the white player data
   *
   * @return white player object
   */
  public Player getWhitePlayer() {
    return this.whitePlayer;
  }

  /**
   * Sets the red player
   *
   * @param whitePlayer player obj
   */
  public void setWhitePlayer(Player whitePlayer) {
    this.whitePlayer = whitePlayer;
  }

  /**
   * Gets the PieceColor of a player
   *
   * @param player player to get the color of
   * @return Piececolor of the player
   */
  public PieceColor getPlayerColor(Player player) {
    if (player == this.redPlayer)
      return PieceColor.RED;
    else
      return PieceColor.WHITE;
  }

  /**
   * Gets the boardview based on a PieceColor
   *
   * @param color PieceColor to get the view for
   * @return BoardView of the board
   */
  public BoardView getBoardView(PieceColor color) {
    return board.getBoardView(color);
  }

  /**
   * Gets a matrix representation of the board
   *
   * @return the 2d space array that represents the board
   */
  public Space[][] getMatrix() {
    return board.getMatrix();
  }

  /**
   * Gets the color of the user who's turn it currently is
   *
   * @return PieceColor of the
   */
  public PieceColor getActiveColor() {
    return this.activeColor;
  }

  /**
   * Gets the current game board
   *
   * @return game board
   */
  public Board getBoard() {
    return this.board;
  }

  /**
   * Changes board to preset board
   * @param str <-- board to be changed to
   */
  // public void setBoard(String str) { this.board = new Board(str); }

  /**
   * Gets the state of the game (ACTIVE, RED_RESIGN, WHITE_RESIGN, WHITE_WIN, RED_WIN)
   * @return the state of the game
   */
  public GameState getGameState() { return this.gameState; }


  /**
   * Switches the current active color
   */
  public void changeTurns() {
    if(gameState == GameState.ACTIVE) {
      Move lastMove = turnTracker.peekLast();
      Position endPos = lastMove.getEnd();
      Piece piece = board.valueAt(endPos);

      if (endPos.getRow() == 0 && activeColor == PieceColor.RED)
        piece.king();
      if (endPos.getRow() == 7 && activeColor == PieceColor.WHITE)
        piece.king();

      turnTracker.finalizeTurn();
      if (activeColor == PieceColor.RED)
        activeColor = PieceColor.WHITE;
      else
        activeColor = PieceColor.RED;

      board.clearCapturedPieces(activeColor);

      Player activePlayer = (activeColor == PieceColor.RED) ? redPlayer : whitePlayer;

      isGameOver();

      if(activePlayer instanceof AI) {
        if(isGameAlive()) {
          computerTurn();
        }
      } else {
        updateValidJumpMoves();
      }
    }
  }

  public void computerTurn() {

    if(gameState == GameState.ACTIVE) {
      Player activePlayer = (activeColor == PieceColor.RED) ? redPlayer : whitePlayer;

      LinkedList<Move> moves = ((AI) activePlayer).getNextMoves(board);
      for (Move move : moves) {
        turnTracker.makeMove(move);
      }

      isGameOver();

      if(isGameAlive())
        changeTurns();
    }
  }

  /**
   * Checks to see if the game is still Active
   * @return boolean yes active/no inactive
   */
  public boolean isGameAlive() {
    return gameState == GameState.ACTIVE;
  }

  public void resign(Player playerResigning) {
    PieceColor color = getPlayerColor(playerResigning);
    gameState = (color == PieceColor.RED) ? GameState.RED_RESIGN : GameState.WHITE_RESIGN;
    isGameAlive = false;

    playerResigning.justLost();
    Player opponent = (playerResigning.getTeamColor() == PieceColor.RED) ? getWhitePlayer() : getRedPlayer();
    opponent.justWon();

    if(whitePlayer instanceof AI){
      whitePlayer.endGame();
    }
  }

  @Override
  public Iterator<Move> iterator() {
    return turnTracker.iterator();
  }

  /**
   * Adds a move to the turn tracker queue
   *
   * @param move the move to be made
   */
  public void queueMove(Move move) {
    turnTracker.makeMove(move);
  }

  /**
   * Removes and undoes the last move made
   */
  public void undoMove() {
    turnTracker.undoMove();
  }

  /**
   * Gets the piece the user is moving
   */
  public Boolean canStillMove(){

    if(turnTracker.getTurnLength() > 0){
      Move move = turnTracker.peekLast();
      Position position = new Position(move.getEndingY(), move.getEndingX());
      Piece piece = board.valueAt(position);

      //If the piece jumped another piece check if it can continue jumping
      if(piece != null && move.getPieceJumped() != null){
        ArrayList<LinkedList<Move>> potentialMoves = new ArrayList<>();

        MovePiece.getValidJumps(position, board, piece.getColor(), turnTracker, potentialMoves);

        return (!potentialMoves.isEmpty() && potentialMoves.get(0) != turnTracker);
      }
    }

    return false;
  }

  /**
   * Finds new valid jump moves and adds them to valid jump moves list
   */
  public void updateValidJumpMoves(){
    potentialJumpMoves.clear();

    ArrayList<Position> pieces = board.getLocationOfPieces(activeColor);

    for(Position position : pieces){
      MovePiece.getValidJumps(position, board, activeColor, null, potentialJumpMoves);
    }
  }

  /**
   * Gets the list of valid jumps
   * @return valid jumps list
   */
  public ArrayList<LinkedList<Move>> getValidJumps(){
    return potentialJumpMoves;
  }

  /**
   * Checks if the user is making a non-jumping move
   * @return True if the user is not jumping a piece, false otherwise
   */
  public boolean nonJumpMove(){
    Move move = turnTracker.peekLast();

    return move.getPieceJumped() == null;
  }

  /**
   * Gets the length of the stack from turnTracker
   *
   * @return int size
   */
  public TurnTracker getMoveList(){
    return turnTracker;
  }

  /**
   * Checks for game ending conditions based on board conditions 
   * player either runs out of moves or pieces
   *
   */ 
  public void isGameOver(){
    int red = board.getNumPieces(PieceColor.RED);
    int white = board.getNumPieces(PieceColor.WHITE);
      if( red == 0 || !playerHasValidMove(this, PieceColor.RED) ){ //if no red pieces or there is no move
        redPlayer.justLost();
        whitePlayer.justWon();
        //spectators.forEach(Player::endGame);
        gameState = gameState.WHITE_WIN;
        isGameAlive = false;
      }
      else if( white == 0 || (!playerHasValidMove(this, PieceColor.WHITE) && (MovePiece.getAllValidJumps(board, PieceColor.WHITE).size() == 0))){
        redPlayer.justWon();
        whitePlayer.justLost();
        //spectators.forEach(Player::endGame);
        gameState = gameState.RED_WIN;
        isGameAlive = false;
      }

      if(!isGameAlive && whitePlayer instanceof AI) {
        ((AI) whitePlayer).removeGame(this);
      }
    }

  /**
   * Adds a spectator to the linked list
   *
   * @param player the player object to be added
   * @return true upon success
   */
  public boolean addSpectator(Player player){
      return spectators.add(player);
    }

  /**
   * Removes spectator from viewing game
   * @param player <-- spectator
   */
  public void removeSpectator(Player player) {
      spectators.remove(player);
    }

  /**
   * Gets number of spectators in game
   * @return size of spectators
   */
  public int getAllSpectator(){
      return spectators.size();
    }

  /***
   * Gets all spectators
   * @return all spectators
   */
  public ArrayList<String> getSpectators() {

    ArrayList<String> spectatorNames = new ArrayList<>();

    for (Player spectator: spectators) {
      spectatorNames.add(spectator.getName());
    }

    return spectatorNames;
  }
    

}