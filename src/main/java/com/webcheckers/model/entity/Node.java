package com.webcheckers.model.entity;

import com.webcheckers.model.board.Board;
import com.webcheckers.model.board.Move;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Node {

    //Instance Variables
    private int value;
    private Board board;
    private LinkedList<Move> moves;
    private ArrayList<Node> children;

    /**
     * Constructor for the Node object
     * @param board
     * @param moves
     */
    public Node(Board board, LinkedList<Move> moves) {
        super();
        this.board = board;
        this.moves = moves;
        this.children = new ArrayList<>();
    }

    /**
     * Checks if the node has any children
     * @return True if the node has no children, false otherwise
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * Add the passed in node as a child to this node
     * @param node
     */
    public void addChild(Node node) {
        children.add(node);
    }

    /**
     * Get the value of the node
     * @return
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Gets the board of the node
     * @return
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Gets the array of children
     * @return
     */
    public ArrayList<Node> getChildren() {
        return this.children;
    }

    /**
     * Get the moves made to bring the board from the parent node to this node
     * @return Linked list of moves made
     */
    public LinkedList<Move> getMoves() {
        return this.moves;
    }

    /**
     * Gets the child of the node that has the maximum value
     * @return Node from the children with the highest value
     */
    public Node getMaxChild(){
        if(isLeaf())
            return null;

        int maxValue = Integer.MIN_VALUE;
        ArrayList<Node> maxChildren = new ArrayList<>();
        for(Node child : children) {
            if(child.value > maxValue) {
                maxChildren.clear();
                maxChildren.add(child);
                maxValue = child.value;
            } else if (child.value == maxValue) {
                maxChildren.add(child);
            }
        }

        int randomIndex = (int) (Math.random() * maxChildren.size());
        return maxChildren.get(randomIndex);
    }

    /**
     * Sets the value of the node
     * @param value
     */
    public void setValue(int value) { this.value = value; }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Node && ((Node) obj).getBoard().equals(board))
            return true;
        else
            return false;
    }
}
