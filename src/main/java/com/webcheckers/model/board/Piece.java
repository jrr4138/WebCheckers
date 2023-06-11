package com.webcheckers.model.board;

import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.PieceState;
import com.webcheckers.model.states.PieceType;


/**
 * Represents a piece on the board
 */
public class Piece {

    private PieceType type; 
    private PieceColor color;
    private PieceState state;
    private Position position;

    /**
     * Creates a new piece
     *
     * @param type piece type of this instance
     * @param color color of the piece 
     */
    public Piece(PieceType type, PieceColor color, Position position){
        this.type = type;
        this.color = color;
        this.state = PieceState.FREE;
        this.position = position;
    }

    /**
     * Gets the piece type 
     *
     * @return the PieceType of the piece
     */
    public PieceType getType() {
        return this.type;
    }

    /**
     * Gets the state of the piece
     * @return the PieceState of the piece
     */
    public PieceState getState() { return this.state; }

    public Position getPosition() { return this.position; }

    /**
     * Gets the piece color 
     *
     * @return the color of the piece
     */
    public PieceColor getColor() {
        return this.color;
    }
    
    /**
     * Checks if the given piece is a king 
     *
     * @return true if the piece is a king false otherwise
     */
    public boolean isKing() {
        return this.type == PieceType.KING;
    }

     /**
      * Set the piece type to king
      */
    public void king() {
        this.type = PieceType.KING;
    }

    /**
     * Removes the king status from a piece
     */
    public void unking() {
        this.type = PieceType.SINGLE;
    }

    /**
     * Sets the piece state to captured
     */
    public void capture(){ this.state = PieceState.CAPTURED; }

    /**
     * Sets the piece state to free
     */
    public void free() { this.state = PieceState.FREE; }

    public void movePiece(Position newPosition) {
        this.position = newPosition;
    }

    /**
     * Gets the color as a string 
     *
     * @return String representation of the color
     */
    public String toString(){
        return color.toString();
    }

    public Piece copyPiece() {
        Piece pieceCopy = new Piece(type, color, position);
        return pieceCopy;
    }

    /**
     * Checks if two objects are equal 
     *
     * @param other object to be compared to instance of this 
     */
    @Override
    public boolean equals(Object other){
        if (other instanceof Piece){
            Piece otherPiece = (Piece)other;
            return (this.position == otherPiece.position && this.color == otherPiece.color && this.type == otherPiece.type);
        }
        return false;
    }
}