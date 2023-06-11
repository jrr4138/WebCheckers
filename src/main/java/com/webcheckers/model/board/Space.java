package com.webcheckers.model.board;

import com.webcheckers.model.states.PieceType;

/**
 * Represents a space on the board    
 */
public class Space {

    public enum State {
        INVALID,
        OPEN,
        OCCUPIED
    }

    private int cellIdx; //index of the space
    private Piece piece; //piece in the given space
    private State state;

    /**
     * Creates a new space object
     *
     * @param cellIdx index of the space 
     * @param piece piece of the index
     */
    public Space(int cellIdx, Piece piece) {
        this.cellIdx = cellIdx;
        if (piece != null) {
            this.piece = piece;
            this.state = State.OCCUPIED;
        } else {
            this.piece = null;
            this.state = State.OPEN;
        }
    }

    /**
     * Gets the state of the space 
     *
     * @return the state 
     */
    public State getState() {
        return this.state;
    }

    /**
     * Sets the state of the space
     *
     * @param state new state of the space
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Gets the cell index
     *
     * @return the cell index
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * Gets the piece in the given space
     *
     * @return Piece obj if there is one, null if unoccupied
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Sets the piece on the space and marks it occupied
     * only used when initialized
     *
     * @param piece piece to put in the space
     */
    public void setPiece(Piece piece){
        this.piece = piece;
        this.state = State.OCCUPIED;
    }

    /**
     * Checks if the space is valid to move to based on state
     *
     * @return true if the space is free false otherwise
     */
    public boolean isValid() {
        return (state != State.INVALID && state != State.OCCUPIED);
    }

    /**
     * Checks if the space has a king
     *
     * @return true if the space has a king false otherwise
     */
    public boolean isKing(){
        return piece.getType() == PieceType.KING;
    }

    public Space copySpace() {
        Piece pieceCopy;
        if(piece != null)
            pieceCopy = piece.copyPiece();
        else
            pieceCopy = null;
        Space spaceCopy = new Space(cellIdx, pieceCopy);
        return spaceCopy;
    }

    /**
     * Checks if two objects are equal 
     *
     * @param other object to be compared to instance of this 
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof Space){
            Space otherSpace = (Space)other;
            if (otherSpace.getCellIdx() == this.cellIdx){
                return (otherSpace.getPiece()).equals(this.piece);
            }
        }
        return false;
    }
}