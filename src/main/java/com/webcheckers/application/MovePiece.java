package com.webcheckers.application;

import com.webcheckers.model.board.*;
import com.webcheckers.model.entity.Game;
import com.webcheckers.model.entity.TurnTracker;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.ui.GetGameRoute;


import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * Responsible for piece validation and verification
 * 
 */
public class MovePiece {

    private static final int SINGLE_DISTANCE = 1;
    private static final int JUMP_DISTANCE = 2;
    //console loggin'
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

   
    /**
     * Checks for any valid move for a given player's color
     *
     * @param game game
     * @param color piece color to check for valid moves
     */
    public static boolean playerHasValidMove(Game game, PieceColor color){
        Board board = game.getBoard();
        ArrayList<Position> positionsOfPieces = board.getLocationOfPieces(color);
        for (Position position : positionsOfPieces) {
            if (hasValidMove(position, board, color)) return true;
        }

        return playerHasValidJumpMove(game);
    }

    /**
     * 
     * Checks if there is a valid move 
     * 
     * @param position starting position 
     * @param board game board 
     * @param color piece color
     *
     * @return true is potential valid move false otherwise 
     */
    public static boolean hasValidMove(Position position, Board board, PieceColor color){
        if( position == null )
            return false;
        int startX = position.getCell();
        int startY = position.getRow();
        Position place;
        Move move;
        for( int row = -1; row < 2; row+=2){
            for( int col = -1; col < 2; col+=2) {
                place = new Position(startY + row, startX + col);
                move = new Move(position, place);
                if(isMoveValid(move, board, color, new LinkedList<>()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Checks for any valid jump move for a given player's color
     *
     * @param game game
     */
    public static boolean playerHasValidJumpMove(Game game){
        ArrayList<LinkedList<Move>> moves = game.getValidJumps();
        return moves.size() > 0;
    }

    /**
     *
     * Checks if there is a valid jump for the given piece
     *
     * @param position starting position
     * @param board game board
     * @param color piece color
     *
     * @return true is potential valid move false otherwise
     */
    public static void getValidJumps(Position position, Board board, PieceColor color, LinkedList<Move> prevMoves, ArrayList<LinkedList<Move>> potentialJumps){
        if(prevMoves == null)
            prevMoves = new LinkedList();


        if( position != null ){
            boolean endJump = true;
            for(int row = -2; row <= 2; row+=4) {
                for(int col = -2; col <= 2; col+=4){
                    Position endPos = new Position(position.getRow()+row, position.getCell()+col);

                    Move potentialMove = new Move(position, endPos);

                    boolean newPosition = true;

                    //Check if the move is just moving back to the most previous space
                    if(!prevMoves.isEmpty()) {
                        Move prevMove = prevMoves.peekLast();
                        if(prevMove != null && potentialMove.getEnd().equals(prevMove.getStart())) {
                            newPosition = false;
                        }
                    }

                    if(newPosition && MovePiece.isMoveValid(potentialMove, board, color, prevMoves)){
                        endJump = false;
                        LinkedList<Move> moves = new LinkedList<>();
                        moves.addAll(prevMoves);
                        Board boardCopy = new Board(board);
                        BoardController.makeMove(boardCopy, potentialMove);
                        moves.add(potentialMove);
                        getValidJumps(potentialMove.getEnd(), boardCopy, color, moves, potentialJumps);
                    }
                }
            }
            //If this is not the last possible jump (mid-way through a multi-jump) or there are no previous moves, do not add this possibility to the list of options
            if(endJump && !prevMoves.isEmpty()) {
                potentialJumps.add(prevMoves);
            }
        }
    }

    public static ArrayList<LinkedList<Move>> getAllValidJumps(Board board, PieceColor activeColor) {
        ArrayList<LinkedList<Move>> moves = new ArrayList<>();

        for(Position startPos : board.getLocationOfPieces(activeColor)) {
            //Add a full jump turn for each jump move available
            ArrayList<LinkedList<Move>> jumps = new ArrayList<>();
            getValidJumps(startPos, board, activeColor, null, jumps);

            if(!jumps.isEmpty())
                moves.addAll(jumps);
        }

        return moves;
    }

    public static ArrayList<LinkedList<Move>> getAllValidSingleMoves(Board board, PieceColor activeColor) {
        ArrayList<LinkedList<Move>> moves = new ArrayList<>();

        //Add a move for each single move a piece can make
        for(Position startPos : board.getLocationOfPieces(activeColor)) {
            for( int row = -1; row < 2; row+=2){
                for( int col = -1; col < 2; col+=2) {
                    Position place = new Position(startPos.getRow() + row, startPos.getCell() + col);
                    Move move = new Move(startPos, place);
                    if(isMoveValid(move, board, activeColor, new LinkedList<>())) {
                        LinkedList<Move> turn = new LinkedList<>();
                        turn.add(move);
                        moves.add(turn);
                    }
                }
            }
        }

        return moves;
    }


    /**
     * Checks if the move being attempted is valid
     *
     * @param move move being attempted
     * @param board game board 
     * @param color piece color of active player
     *
     * @return true if valid false otherwise 
     */
    public static boolean isMoveValid(Move move, Board board, PieceColor color, LinkedList<Move> prevMoves){

        // Check for blank space
        if( !positionIsBlack(move.getEnd()) )
            return false;
        if (!isKing(move.getStart(), board.getMatrix())) {
            //Check for direction
            if (color == PieceColor.RED && movingNorth(move))
                return false;
            if (color == PieceColor.WHITE && movingSouth(move))
                return false;
            //LOG.info("valid direction");
        }

        if( inDistanceOf(move, JUMP_DISTANCE)) {
            if (hasPieceBetween(move, board.getMatrix()) && pieceBetween(move, board.getMatrix()).getColor() != color) {
                move.setJumped(piecePosition(move, board.getMatrix()));
            } else {
                //LOG.info("did not have a piece between");
                return false;
            }
        }
        else if( !inDistanceOf(move, SINGLE_DISTANCE) ){
            //LOG.info("Not within one move");
            return false;
        }

        //If concurrent move
        if(prevMoves.size() > 0) {
            LinkedList<Move> moves = prevMoves;
            for(Move prevMove : moves) {
                //If prev move was not a jump, concurrent moves are invalid
                if (prevMove.getPieceJumped() == null || move.getPieceJumped() == null)
                    return false;

                //Check if new position is end of an old position
                if(prevMove.getPieceJumped() != null && move.getEnd().equals(prevMove.getStart())) {
                    return false;
                }
            }
        }

        //If valid end space
        Position endPos = move.getEnd();

        return (endPos.getCell() <= 7 && endPos.getCell() >= 0 && endPos.getRow() <= 7 && endPos.getRow() >= 0 && pieceAt(endPos, board.getMatrix()) == null);
    }

    /**
     * Checks if there is a piece between the starting spot 
     * and ending spot of a move 
     * 
     * @param move the move being made 
     * @param board game board 
     *
     * @return true if there is a piece between and false otherwise
     */
    private static boolean hasPieceBetween(Move move, Space[][] board) {
        return pieceBetween(move,board) != null;
    }

    /**
     * Actually does the calculation to check if there is a piece object 
     * in the space between the starting and ending object 
     * 
     * @param move the move being made 
     * @param board game board 
     *
     * @return the piece in the space; null if nothing there
     */
    private static Piece pieceBetween(Move move, Space[][] board) {
        int x= (move.getStartingX() + move.getEndingX()) / 2;
        int y = (move.getStartingY() + move.getEndingY()) / 2;

        if(Board.validLocation(x, y)){
            return board[y][x].getPiece();
        } else {
            return null;
        }
    }

    private static Position piecePosition(Move move, Space[][] board){
        int x= (move.getStartingX() + move.getEndingX()) / 2;
        int y = (move.getStartingY() + move.getEndingY()) / 2;

        if(Board.validLocation(x, y)){
            return new Position(y,x);
        } else {
            return null;
        }
    }


    /**
     * Checks if the square at the given position is black
     *
     * @param position position to check
     *
     * @return true if it is black; false otherwise
     */
    private static boolean positionIsBlack(Position position){
        if( position.getRow() % 2 == 0)
            return position.getCell() % 2 == 1;
        else
            return position.getCell() % 2 == 0;
    }

    /**
     * Checks if the move is in a valid distance 
     *
     * @param move move being made 
     * @param dist valid distance of the move
     *
     * @return true if is in distance false otherwise
     */
    private static boolean inDistanceOf(Move move, int dist){
        int deltaX = delta(move.getStartingX(), move.getEndingX());
        int deltaY = delta(move.getStartingY(), move.getEndingY());
        return Math.abs(deltaX) == dist && Math.abs(deltaY) == dist;
    }

    /**
     * gets the difference between two points 
     *
     * @param value1 starting value
     * @param value2 ending value
     *
     * @return difference between two values 
     */
    private static int delta(int value1, int value2){
        return value1 - value2;
    }

    /**
     * gets the piece at a given position  
     *
     * @param position position to check 
     * @param board game board
     *
     * @return the piece at the given spot null otherwise
     */
    private static Piece pieceAt(Position position, Space[][] board){
        int x = position.getCell();
        int y = position.getRow();
        return board[y][x].getPiece();
    }

    /**
     * Checks if the piece is a king 
     *
     * @param position position to check
     * @param board game board
     *
     * @return true if the piece at the position is a king false otherwise
     */
     private static boolean isKing(Position position, Space[][] board){
        int x = position.getCell();
        int y = position.getRow();
        return board[y][x].isKing();
    }


    private static boolean movingNorth(Move move){
        return delta( move.getStartingY(), move.getEndingY() ) < 0;
    }

    private static boolean movingSouth(Move move){
        return delta( move.getStartingY(), move.getEndingY() ) > 0;
    }
    
}