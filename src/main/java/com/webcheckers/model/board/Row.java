package com.webcheckers.model.board;

import com.webcheckers.model.states.PieceColor;

import java.util.*;

/**
 * Represents a row on the board
 */
public class Row implements Iterable {

    private int index; //index of the row
    private List<Space> spaces; //list of spaces in the given row
    private PieceColor color;

    /**
     * Creates a new row instance
     *
     * @param index index of the row
     * @param boardCols array of the boards columns 
     * @param color color of the pieces 
     */
    public Row(int index, Space[] boardCols, PieceColor color) {
        spaces = new ArrayList<>();
        this.color = color;
        this.index = index;
        Collections.addAll(spaces, boardCols);
    }

    /**
     * gets the index of the row
     *
     * @return index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Helps display the pieces for red player
     */
    @Override
    public Iterator<Space> iterator() {
         if( color != null && color.equals(PieceColor.RED) )
            return spaces.iterator();
        return reverseIterator();
    }

    /**
     * Helps display the pieces for white player
     */
    private Iterator<Space> reverseIterator(){
        Deque<Space> deque = new ArrayDeque<>(spaces);
        return deque.descendingIterator();
    }

    /**
     * Checks if two objects are equal 
     *
     * @param other object to be compared to instance of this 
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof Row) {
            Row otherRow = (Row) other;
            if (otherRow.getIndex() == this.index);{
                return (otherRow.iterator()).equals(this.iterator());
            }
        }
        return false;
    }
}