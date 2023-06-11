package com.webcheckers.model.board;


/**
 * Represents a move being made
 */
public class Move{
    
    private Position start; //where the move starts
    private Position end; //where the move ends
    private Position pieceJumped;

    /**
     * Create a new Move
     * 
     * @param start starting positon
     * @param end ending position
     */
    public Move(Position start, Position end){
        this.start = start; 
        this.end = end;
        this.pieceJumped = null;
    }

    /**
     * Gets the starting position of the move
     * 
     * @return starting position
     */
    public Position getStart(){
        return this.start;
    }

    /**
     * Gets the ending position of the move
     * 
     * @return ending position
     */
    public Position getEnd(){
        return this.end;
    }

    /**
     * Sets the piece being jumped in the move
     * 
     * @param jumped piece being jumped
     */
    public void setJumped(Position jumped) {
        this.pieceJumped = jumped;
    }

    /**
     * Gets the piece that was jumped
     * 
     * @return piece being jumped
     */
    public Position getPieceJumped(){
        return this.pieceJumped;
    }

    /**
     * Swaps the start and end positions of the move to reverse it
     */
    public void reverseMove() {
        Position temp = start;
        start = end;
        end = temp;
    }

    /**
     * Checks if two objects are equal 
     *
     * @param obj object to be compared to instance of this
     */
    @Override
    public boolean equals(Object obj){
        return obj instanceof Move && ((Move) obj).start == this.start && ((Move) obj).end == this.end;
    }

    /**
     * Gets the starting cell of the move
     * 
     * @return int value of the cell on the board
     */
    public int getStartingX(){
        return start.getCell();
    }

    /**
     * Gets the ending cell of the move
     * 
     * @return int value of the cell on the board
     */
    public int getEndingX(){
        return end.getCell();
    }

    /**
     * Gets the starting row of the move
     * 
     * @return int value of the row on the board
     */
    public int getStartingY(){
        return start.getRow();
    }

    /**
     * Gets the ending row of the move
     * 
     * @return int value of the row on the board
     */
    public int getEndingY(){
        return end.getRow();
    }

}