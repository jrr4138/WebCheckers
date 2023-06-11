package com.webcheckers.model.board;

import com.webcheckers.application.BoardController;
import com.webcheckers.application.MovePiece;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.PieceState;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.ArrayList;
/**
 * Represents the game board 
 */
public class Board {
    private static final String JUMP = "jumpBoard";
    private static final String DUBJUMP = "doubleJumpBoard";
    private static final String TRIPJUMP = "tripleJumpBoard";
    private static final String KINGME = "kingBoard";
    private static final String OUTOFMOVES = "outOfMovesBoard";

    //Board as 2D array
    private Space[][] board = new Space[8][8];
    private LinkedList<Piece> redPieces;
    private LinkedList<Piece> whitePieces;

    /**
     * Initialize game board 
     */
    public Board(){
        redPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();

        BoardController.initBoard(board, redPieces, whitePieces);
    }

    //Constructor to create a copy of the passed in board
    public Board(Board original) {
        redPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = original.getMatrix()[i][j].copySpace();

                Piece piece = valueAt(new Position(i, j));
                if (piece != null) {
                    LinkedList<Piece> list =
                        (piece.getColor() == PieceColor.RED) ? redPieces : whitePieces;
                    list.add(piece);
                }
            }
        }
    }

    public Board(String str){
        redPieces = new LinkedList<>();
        whitePieces = new LinkedList<>();
        if(str.equals(JUMP)){
            BoardController.initJumpBoard(board, redPieces, whitePieces);
        }
        else if(str.equals(DUBJUMP)){
            BoardController.initDoubleJumpBoard(board, redPieces, whitePieces);
        }
        else if(str.equals(TRIPJUMP)){
            BoardController.initTripleJumpBoard(board, redPieces, whitePieces);
        }
        else if(str.equals(KINGME)){
            BoardController.initKingMeBoard(board, redPieces, whitePieces);
        }
        else if(str.equals(OUTOFMOVES)){
            BoardController.initOutOfMovesBoard(board, redPieces, whitePieces);
        }
        else {
            BoardController.initBoard(board, redPieces, whitePieces);
        }

    }

    /**
     * Gets the game board view of this board based on the piece color
     * 
     * @param color The color we want the board view for
     *
     * @return boardview of the gameboard
     */
    public BoardView getBoardView(PieceColor color) {
        return new BoardView(board, color);
    }

    /**
     * Returns the board as a matrix
     *
     * @return array of spaces
     */
    public Space[][] getMatrix(){
        return this.board;
    }

    /**
     * Removes a piece from the game board
     * 
     * @param piece piece to be removed
     */
    public void removePiece(Piece piece){
        if(piece.getColor() == PieceColor.RED)
            redPieces.remove(piece);
        else
            whitePieces.remove(piece);
    }

    /**
     * Clears a position on the board
     * 
     * @param position the position to be cleared 
     */
    public void clearSpace(Position position){
        board[position.getRow()][position.getCell()] = new Space(position.getCell(), null);
    }

    /**
     * Marks the piece for capture at the end of the turn
     * @param piece
     */
    public void capturePiece(Piece piece){
        piece.capture();
    }

    /**
     * Un-marks the given piece for capture
     * @param piece
     */
    public void freePiece(Piece piece){
        piece.free();
    }

    public void clearCapturedPieces(PieceColor color) {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                Piece piece = valueAt(pos);
                if(piece != null && piece.getState() == PieceState.CAPTURED) {
                    removePiece(piece);
                    clearSpace(pos);
                }
            }
        }
    }

    /**
     * Gets the piece at a given position 
     * 
     * @param position position we want the value at
     * @return piece at the given position
     */
    public Piece valueAt(Position position){
        return board[position.getRow()][position.getCell()].getPiece();
    }

    /**
     * Gets the number of red peices on the board
     *
     * @return int
     */
    public int getNumPieces(PieceColor color) {
        int num = 0;
        for(int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                Position pos = new Position(i, j);
                Piece piece = valueAt(pos);
                if(piece != null && piece.getColor() == color)
                    num++;
            }
        }
        return num;
    }

    /**
     * Gets the list of red pieces on the board
     */
    public LinkedList<Piece> getRedPieces() { return redPieces; }

    /**
     * Gets the list of white pieces on the board
     */
    public LinkedList<Piece> getWhitePieces() {
        return whitePieces;
    }

    /**
     * Gets then number of white pieces on the board 
     * 
     * @return int 
     */
    public int getNumWhitePieces() {
        return whitePieces.size();
    }

    /**
     * Gets the location of pieces on the board for a given color 
     *
     * @param color piece colors to find
     * @return ArrayList of Positions
     */
    public ArrayList<Position> getLocationOfPieces( PieceColor color){
        ArrayList<Position> positions = new ArrayList<>(0);
        if (color.equals(PieceColor.RED)){
            for (Piece piece : redPieces) {
                positions.add(piece.getPosition());
            }
        }
        else {
            for (Piece piece : whitePieces) {
                positions.add(piece.getPosition());
            }
        }
        return positions;
    }

    public static Boolean validLocation(int x, int y) {
        return x <= 7 && x >= 0 && y <= 7 && y >= 0;
    }

    public void copyBoard(Board original) {
        for(int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j] = original.getMatrix()[i][j].copySpace();
            }
        }
        this.redPieces.clear();
        this.whitePieces.clear();
        this.redPieces.addAll(original.getRedPieces());
        this.whitePieces.addAll(original.getWhitePieces());
    }

}