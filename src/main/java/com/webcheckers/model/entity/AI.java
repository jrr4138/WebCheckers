package com.webcheckers.model.entity;

import com.webcheckers.application.BoardController;
import com.webcheckers.application.MovePiece;
import com.webcheckers.model.board.Board;
import com.webcheckers.model.board.Move;
import com.webcheckers.model.board.Piece;
import com.webcheckers.model.board.Position;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.AIDifficulty;

import java.util.ArrayList;
import java.util.LinkedList;


public class AI extends Player{

    private static final int KING_VALUE = 3;
    private static final int SINGLE_VALUE = 1;

    private Node decisionTree;
    private AIDifficulty difficulty;
    private ArrayList<Game> games;

    /**
     * Constructor for AI object, inherits player constructor. Takes in the number of moves it should look ahead. 2 moves = 1 turn
     * AI always has the name "Computer"
     * @param difficulty
     */
    public AI(AIDifficulty difficulty){
        super("Computer");
        this.difficulty = difficulty;

        if(difficulty == AIDifficulty.EASY) {
            name = "WallE";
            achievements.add(-1);
        } else if(difficulty == AIDifficulty.HARD){
            name = "Sputnik";
            achievements.add(-2);
        } else {
            name = "Demo";
        }

        games = new ArrayList<>();
    }

    /**
     * Sets the game the AI is playing in
     * @param game
     */
    public void addGame(Game game) { games.add(game); }

    public void removeGame(Game game) { games.remove(game); }

    /**
     * Sets the root of the tree of all possible moves
     * @param root
     */
    public void setDecisionTree(Node root) {
        this.decisionTree = root;
    }

    /**
     * Gets the value of the root node, the value being the numerical representation of the board the AI is most likely going to achieve
     * @return
     */
    public int getRootValue() {
        return this.decisionTree.getValue();
    }

    /**
     * Gets the next moves the AI is going to make
     * @param board
     * @return Linked list of moves to make
     */
    public LinkedList<Move> getNextMoves(Board board){
        updateDecisionTree(board);

        minimax(decisionTree, true);
        Node maxChild = decisionTree.getMaxChild();

        return maxChild.getMoves();
    }

    /**
     * Creates a new tree of all the possible decisions each player can make off the given board
     * @param board
     */
    public void updateDecisionTree(Board board) {
        decisionTree = new Node(board, null);

        int depth = (this.difficulty == AIDifficulty.EASY || this.difficulty == AIDifficulty.DEMO) ? 2 : 6;
        establishChildren(depth, decisionTree, PieceColor.WHITE);
    }

    /**
     * Get the possible moves off this board and store each one as a child node
     * @param height
     * @param node
     * @param activeColor
     */
    public static void establishChildren(int height, Node node, PieceColor activeColor) {
        //Base case: If at the bottom of the tree, return
        if(height <= 0)
            return;

        //First check for valid jump moves
        ArrayList<LinkedList<Move>> allMoves = MovePiece.getAllValidJumps(node.getBoard(), activeColor);

        //If there are no jump moves available, look for all the valid single moves that can be made
        //If there are jump moves available, only consider those as a jump move has to be made.
        if(allMoves.isEmpty()) {
            allMoves = MovePiece.getAllValidSingleMoves(node.getBoard(), activeColor);
        }

        //Make a child for each possible move
        for(LinkedList<Move> turn : allMoves) {
            //Create a deep copy of the board we can modify
            Board boardCopy = new Board(node.getBoard());

            PieceColor nextColor = (activeColor == PieceColor.RED) ? PieceColor.WHITE : PieceColor.RED;

            BoardController.makeTurn(boardCopy, turn);
            boardCopy.clearCapturedPieces(nextColor);

            Node newChild = new Node(boardCopy, turn);

            //Recursive call at a depth-1 to get all children of this child node
            establishChildren(height -1, newChild, nextColor);

            node.addChild(newChild);
        }
    }

    /**
     * AI algorithm to find the best possible move to make given the AI wants to maximize the score and the player wants to minimize it
     * @param root
     * @param maximizing
     * @return The value of this node which is equivalent to either the maximum or minimum
     */
    private static int minimax(Node root, boolean maximizing){
        //Base case: if no children, evaluate the board and return its value
        if(root.isLeaf()) {
            int value = evaluateBoard(root.getBoard());
            root.setValue(value);
            return value;
        }

        //If the AI, then get the maximum child and set this nodes value to that
        if(maximizing) {
            int maxValue = Integer.MIN_VALUE;
            for(Node child : root.getChildren()) {
                minimax(child, !maximizing);
                maxValue = Integer.max(maxValue, child.getValue());
            }
            root.setValue(maxValue);
            return maxValue;
        }
        //If the player, then get the minimum child and set this nodes value to that
        else {
            int minValue = Integer.MAX_VALUE;
            for(Node child : root.getChildren()) {
                minimax(child, !maximizing);
                minValue = Integer.min(minValue, child.getValue());
            }
            root.setValue(minValue);
            return minValue;
        }
    }

    /**
     * Evaluates and scores the board based on the pieces left
     * @param board
     * @returns Larger values if in favor of red, lower if in favor of white. 0 is equal
     */
    public static int evaluateBoard(Board board){
        int redScore = 0;
        int whiteScore = 0;

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Position pos = new Position(i, j);
                Piece piece = board.valueAt(pos);
                if(piece != null) {
                    if(piece.getColor() == PieceColor.RED)
                        redScore += (piece.isKing()) ? KING_VALUE : SINGLE_VALUE;
                    else
                        whiteScore += (piece.isKing()) ? KING_VALUE : SINGLE_VALUE;
                }

            }
        }

        if(whiteScore == 0)
            return Integer.MIN_VALUE;
        else if(redScore == 0)
            return Integer.MAX_VALUE;

        return whiteScore - redScore;
    }

    public AIDifficulty getDifficulty() {
        return this.difficulty;
    }

    public void justWon() {
        winStreak++;
        wins++;

        if(winStreak >= 3) {
            //Achievement ID for win streak is 2
            achievements.add(2);
        }
    }
}
