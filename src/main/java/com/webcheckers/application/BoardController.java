package com.webcheckers.application;

import com.webcheckers.model.board.*;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.PieceType;
import com.webcheckers.model.board.Space.State;

import java.util.LinkedList;

public class BoardController {

    /**
     * Initializes the Board based on the 2D array of Spaces and provided by a
     * linked lists of pieces
     *
     * @param board 2D array of spaces that make up the board
     * @param redPieces Linked list of red player pieces
     * @param whitePieces Linked list of white player pieces
     *
     */
    public static void initBoard(Space[][] board, LinkedList<Piece> redPieces, LinkedList<Piece> whitePieces){

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Space(col, null);
            }
        }

        for (int col = 0; col < 8; col++) {
            if (col % 2 == 1) {
                board[0][col].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(0, col)));
                board[0][col].setState(State.OCCUPIED);
                whitePieces.add(board[0][col].getPiece());
                board[2][col].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(2, col)));
                board[2][col].setState(State.OCCUPIED);
                whitePieces.add(board[2][col].getPiece());
                board[6][col].setPiece(new Piece(PieceType.SINGLE, PieceColor.RED, new Position(6, col)));
                board[6][col].setState(State.OCCUPIED);
                redPieces.add(board[6][col].getPiece());

                //mark white spaces invalid
                board[1][col].setState(State.INVALID);
                board[3][col].setState(State.INVALID);
                board[5][col].setState(State.INVALID);
                board[7][col].setState(State.INVALID);

            } else {
                board[1][col].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(1, col)));
                board[1][col].setState(State.OCCUPIED);
                whitePieces.add(board[1][col].getPiece());
                board[5][col].setPiece(new Piece(PieceType.SINGLE, PieceColor.RED, new Position(5, col)));
                board[5][col].setState(State.OCCUPIED);
                redPieces.add(board[5][col].getPiece());
                board[7][col].setPiece(new Piece(PieceType.SINGLE, PieceColor.RED, new Position(7, col)));
                board[7][col].setState(State.OCCUPIED);
                redPieces.add(board[7][col].getPiece());

                //mark white spaces invalid
                board[0][col].setState(State.INVALID);
                board[2][col].setState(State.INVALID);
                board[4][col].setState(State.INVALID);
                board[6][col].setState(State.INVALID);
            }
        }
    }

    /**
     * Initializes a Board based on a 2D array that sets up a single-jump move
     *
     * @param board 2D array of spaces that make up the board
     * @param redPieces Linked list of red player pieces
     * @param whitePieces Linked list of white player pieces
     *
     */
    public static void initJumpBoard(Space[][] board, LinkedList<Piece> redPieces, LinkedList<Piece> whitePieces) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Space(col, null);
            }
        }

        board[1][2].setPiece(new Piece(PieceType.KING, PieceColor.RED, new Position(1, 2)));
        board[1][2].setState(State.OCCUPIED);
        redPieces.add(board[1][2].getPiece());
        board[2][3].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(2, 3)));
        board[2][3].setState(State.OCCUPIED);
        whitePieces.add(board[2][3].getPiece());

        for (int col = 0; col < 8; col++) {
            if (col % 2 == 1) {
                board[1][col].setState(State.INVALID);
                board[3][col].setState(State.INVALID);
                board[5][col].setState(State.INVALID);
                board[7][col].setState(State.INVALID);
            }
            else {
                board[0][col].setState(State.INVALID);
                board[2][col].setState(State.INVALID);
                board[4][col].setState(State.INVALID);
                board[6][col].setState(State.INVALID);
            }
        }
    }

    /**
     * Initializes a Board based on a 2D array that sets up a double-jump move
     *
     * @param board 2D array of spaces that make up the board
     * @param redPieces Linked list of red player pieces
     * @param whitePieces Linked list of white player pieces
     *
     */
    public static void initDoubleJumpBoard(Space[][] board, LinkedList<Piece> redPieces, LinkedList<Piece> whitePieces) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Space(col, null);
            }
        }

        board[2][1].setPiece(new Piece(PieceType.SINGLE, PieceColor.RED, new Position(2, 1)));
        board[2][1].setState(State.OCCUPIED);
        redPieces.add(board[2][1].getPiece());

        board[1][2].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(1, 2)));
        board[1][2].setState(State.OCCUPIED);
        whitePieces.add(board[1][2].getPiece());
        board[1][4].setPiece(new Piece(PieceType.KING, PieceColor.WHITE, new Position(1, 4)));
        board[1][4].setState(State.OCCUPIED);
        whitePieces.add(board[1][4].getPiece());

        for (int col = 0; col < 8; col++) {
            if (col % 2 == 1) {
                board[1][col].setState(State.INVALID);
                board[3][col].setState(State.INVALID);
                board[5][col].setState(State.INVALID);
                board[7][col].setState(State.INVALID);
            }
            else {
                board[0][col].setState(State.INVALID);
                board[2][col].setState(State.INVALID);
                board[4][col].setState(State.INVALID);
                board[6][col].setState(State.INVALID);
            }
        }
    }

    /**
     * Initializes a Board based on a 2D array that sets up a triple-jump move
     *
     * @param board 2D array of spaces that make up the board
     * @param redPieces Linked list of red player pieces
     * @param whitePieces Linked list of white player pieces
     *
     */
    public static void initTripleJumpBoard(Space[][] board, LinkedList<Piece> redPieces, LinkedList<Piece> whitePieces) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Space(col, null);
            }
        }

        board[1][2].setPiece(new Piece(PieceType.KING, PieceColor.RED, new Position(1, 2)));
        board[1][2].setState(State.OCCUPIED);
        redPieces.add(board[1][2].getPiece());

        board[2][3].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(2, 3)));
        board[2][3].setState(State.OCCUPIED);
        whitePieces.add(board[2][3].getPiece());
        board[4][3].setPiece(new Piece(PieceType.KING, PieceColor.WHITE, new Position(4, 3)));
        board[4][3].setState(State.OCCUPIED);
        whitePieces.add(board[4][3].getPiece());
        board[6][3].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(6, 3)));
        board[6][3].setState(State.OCCUPIED);
        whitePieces.add(board[6][3].getPiece());

        for (int col = 0; col < 8; col++) {
            if (col % 2 == 1) {
                board[1][col].setState(State.INVALID);
                board[3][col].setState(State.INVALID);
                board[5][col].setState(State.INVALID);
                board[7][col].setState(State.INVALID);
            }
            else {
                board[0][col].setState(State.INVALID);
                board[2][col].setState(State.INVALID);
                board[4][col].setState(State.INVALID);
                board[6][col].setState(State.INVALID);
            }
        }
    }

    /**
     * Initializes a Board based on a 2D array that sets up a KingMe piece move for both Red and White pieces
     *
     * @param board 2D array of spaces that make up the board
     * @param redPieces Linked list of red player pieces
     * @param whitePieces Linked list of white player pieces
     *
     */
    public static void initKingMeBoard(Space[][] board, LinkedList<Piece> redPieces, LinkedList<Piece> whitePieces) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Space(col, null);
            }
        }

        board[1][4].setPiece(new Piece(PieceType.SINGLE, PieceColor.RED, new Position(1, 4)));
        board[1][4].setState(State.OCCUPIED);
        redPieces.add(board[1][4].getPiece());

        board[6][3].setPiece(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(6, 3)));
        board[6][3].setState(State.OCCUPIED);
        whitePieces.add(board[6][3].getPiece());

        for (int col = 0; col < 8; col++) {
            if (col % 2 == 1) {
                board[1][col].setState(State.INVALID);
                board[3][col].setState(State.INVALID);
                board[5][col].setState(State.INVALID);
                board[7][col].setState(State.INVALID);
            }
            else {
                board[0][col].setState(State.INVALID);
                board[2][col].setState(State.INVALID);
                board[4][col].setState(State.INVALID);
                board[6][col].setState(State.INVALID);
            }
        }
    }

    /**
     * Initializes a Board based on a 2D array that sets up a board that has no more moves
     *
     * @param board 2D array of spaces that make up the board
     * @param redPieces Linked list of red player pieces
     * @param whitePieces Linked list of white player pieces
     *
     */
    public static void initOutOfMovesBoard(Space[][] board, LinkedList<Piece> redPieces, LinkedList<Piece> whitePieces) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = new Space(col, null);
            }
        }

        for (int col = 0; col < 8; col++) {
            if (col % 2 == 1) {
                board[1][col].setState(State.INVALID);
                board[3][col].setState(State.INVALID);
                board[5][col].setState(State.INVALID);
                board[7][col].setState(State.INVALID);
            }
            else {
                board[0][col].setState(State.INVALID);
                board[2][col].setState(State.INVALID);
                board[4][col].setState(State.INVALID);
                board[6][col].setState(State.INVALID);
            }
        }
        board[6][1].setState(State.INVALID);
        board[7][0].setPiece(new Piece(PieceType.KING, PieceColor.WHITE, new Position(7, 0)));
        board[7][0].setState(State.OCCUPIED);
        whitePieces.add(board[7][0].getPiece());

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board[i][j].getState() != State.INVALID && board[i][j].getState() != State.OCCUPIED) {
                    board[i][j].setPiece(new Piece(PieceType.KING, PieceColor.RED, new Position(i, j)));
                    board[i][j].setState(State.OCCUPIED);
                    redPieces.add(board[i][j].getPiece());
                }
            }
        }
        board[6][1].setState(State.OPEN);

    }

    /**
     * Makes a move for a given Board and Move object
     *
     * @param board instance of the given board
     * @param move the instance of the move that is being made
     *
     */
    public static void makeMove(Board board, Move move){
        Position start = move.getStart();
        Position end = move.getEnd();
        Space[][] spaces = board.getMatrix();
        Piece piece = spaces[start.getRow()][start.getCell()].getPiece();
        spaces[start.getRow()][start.getCell()].setPiece(null);

        spaces[end.getRow()][end.getCell()].setPiece(piece);
        spaces[start.getRow()][start.getCell()].setState(State.OPEN);
        capturePiece(board, move.getPieceJumped());
        piece.movePiece(end);

        if (piece.getColor() == PieceColor.RED && end.getRow() == 0)
            piece.king();
        else if (piece.getColor() == PieceColor.WHITE && end.getRow() == 7)
            piece.king();
    }

    public static void undoMove(Board board, Move move){
        Position start = move.getStart();
        Position end = move.getEnd();
        Space[][] spaces = board.getMatrix();
        Piece piece = spaces[end.getRow()][end.getCell()].getPiece();
        spaces[end.getRow()][end.getCell()].setPiece(null);

        spaces[start.getRow()][start.getCell()].setPiece(piece);
        spaces[end.getRow()][end.getCell()].setState(State.OPEN);
        freePiece(board, move.getPieceJumped());
        piece.movePiece(start);

        if (piece.getColor() == PieceColor.RED && end.getRow() == 0)
            piece.unking();
        else if (piece.getColor() == PieceColor.WHITE && end.getRow() == 7)
            piece.unking();
    }

    /**
     * Sets the piece at the given location on the board to captured
     *
     * @param board the instance of the games board
     * @param position the position of the piece to be removed
     */
    private static void capturePiece(Board board, Position position) {
        if( position == null )
            return;
        Piece piece = board.valueAt(position);
        board.capturePiece(piece);
    }

    private static void freePiece(Board board, Position position) {
        if(position != null) {
            Piece piece = board.valueAt(position);
            board.freePiece(piece);
        }
    }

    public static void makeTurn(Board board, LinkedList<Move> moves) {
        for (Move move : moves)
            makeMove(board, move);
    }
}