package com.webcheckers.model.entity;

import com.webcheckers.application.BoardController;
import com.webcheckers.model.board.Board;
import com.webcheckers.model.board.Move;

import java.util.LinkedList;
import java.util.Stack;

public class TurnTracker extends LinkedList<Move> {

    /**
     * Games Board
     */
    private Board board;

    /**
     * Constructor
     * @param board
     */
    public TurnTracker(Board board){
        super();
        this.board = board;
    }

    public void undoMove() {
        Move move = removeLast();
        BoardController.undoMove(board, move);
    }

    /**
     * Add move to list and submit move to the board
     * @param move
     */
    public void makeMove(Move move) {
        add(move);
        BoardController.makeMove(board, move);
    }

    /**
     * Checks for the size of the Stack
     *
     * @return int Size
     */
    public int getTurnLength(){
       return size();
    }

    /**
     * finishes the turn by clearing all the moves in the move list
     */
    public void finalizeTurn(){
        clear();
    }
}