package com.webcheckers.model.board;

import com.webcheckers.model.states.PieceColor;

import java.util.*;

/**
 * The view of the board
 */
public class BoardView implements Iterable<Row>{

    //board rows
    private List<Row> rows; 
    private PieceColor color;

    /**
     * Creates a new BoardView 
     *
     * @param boardRows 2D array of spaces
     * @param color color of the given boardview
     */
    public BoardView(Space[][] boardRows, PieceColor color) {
        this.color = color;
        rows = new ArrayList<>(8);
        for (int row = 0; row < boardRows.length; row++) {
            rows.add(new Row(row, boardRows[row], color));
        }
    }

    /**
     * Helps display the pieces for red player
     */
    @Override
    public Iterator<Row> iterator() {
        if( color != null && color.equals(PieceColor.RED) )
            return rows.iterator();
        return reverseIterator();
    }

    /**
     * Helps display the pieces for white player
     */
    private Iterator<Row> reverseIterator() {
        Deque<Row> deque = new ArrayDeque<>(rows);
        return deque.descendingIterator();
    }

     /**
      * Checks if two objects are equal 
      *
      * @param other object to be compared to instance of this 
      */
    @Override 
    public boolean equals(Object other){
        if (other instanceof BoardView){
            BoardView otherBoardView = (BoardView)other;
            Iterator<Row> otherIterator = otherBoardView.iterator();
            Iterator<Row> thisIterator = iterator();
            while (otherIterator.hasNext()){
                Row otherRow = otherIterator.next();
                Row thisRow = thisIterator.next();
                if (!otherRow.equals(thisRow)) return false;
            }
        }
        return false;
    }

}