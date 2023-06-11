package com.webcheckers.model.board;

/**
 * Represents a posiiton on a board
 */
public class Position {

    private int row;
    private int cell;

    /**
     * Creates a new position
     *
     * @param row the row of the postion 
     * @param cell the cell of the position
     */
    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * Gets the given row of the position
     *
     * @return the row 
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets the cell
     *
     * @return the cell of this posiiton
     */
    public int getCell() {
        return this.cell;
    }

    /**
     * Checks if two objects are equal 
     *
     * @param other object to be compared to instance of this 
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof Position){
            Position otherPosition = (Position)other;
            if (otherPosition.getCell() == this.cell){
                if (otherPosition.getRow() == this.row) return true;
            }
        }
        return false;
    }

} 