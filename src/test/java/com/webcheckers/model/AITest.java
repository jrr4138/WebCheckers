package com.webcheckers.model;

import com.webcheckers.application.BoardController;
import com.webcheckers.model.board.Board;
import com.webcheckers.model.board.Move;
import com.webcheckers.model.board.Piece;
import com.webcheckers.model.board.Position;
import com.webcheckers.model.entity.AI;
import com.webcheckers.model.entity.Node;
import com.webcheckers.model.states.AIDifficulty;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AITest {

    private AI CuT;
    private Board board;
    private Node decisionTree;

    @BeforeEach
    public void setup() {
        board = new Board();
        BoardController.initBoard(board.getMatrix(), new LinkedList<>(), new LinkedList<>());
        decisionTree = new Node(board, null);

        CuT = new AI(AIDifficulty.EASY);
    }

    @Test
    public void TestEvaluation() {
        //Return a starter board


        //Assert new board evaluates to 0
        int value = AI.evaluateBoard(board);
        assertEquals(0, value);

        //Assert each king piece is weighted
        LinkedList<Piece> redPieces = board.getRedPieces();
        redPieces.getFirst().king();

        value = AI.evaluateBoard(board);
        assertEquals(2, value);

        //Assert board has max value if red wins
        while(!board.getWhitePieces().isEmpty())
            board.removePiece(board.getWhitePieces().getFirst());
        value = AI.evaluateBoard(board);
        assertEquals(Integer.MAX_VALUE, value);

        //Assert board has min value if white wins
        while(!board.getRedPieces().isEmpty())
            board.removePiece(board.getRedPieces().getFirst());
        board.getWhitePieces().add(new Piece(PieceType.SINGLE, PieceColor.WHITE, new Position(4, 0)));
        value = AI.evaluateBoard(board);
        assertEquals(Integer.MIN_VALUE, value);
    }

    @Test
    public void TestMinimax() {

        LinkedList<Move> moves0 = new LinkedList<>();
        LinkedList<Move> moves1 = new LinkedList<>();
        moves0.add(new Move(new Position(0, 0), new Position(1, 1)));
        moves1.add(new Move(new Position(0, 0), new Position(2, 2)));

        Node child0 = new Node(null, moves0);
        Node child1 = new Node(null, moves1);

        Node child00 = mock(Node.class);
        when(child00.getBoard()).thenReturn(board);
        when(child00.getValue()).thenReturn(6);

        Node child01 = mock(Node.class);
        when(child01.getBoard()).thenReturn(board);
        when(child01.getValue()).thenReturn(12);

        Node child02 = mock(Node.class);
        when(child02.getBoard()).thenReturn(board);
        when(child02.getValue()).thenReturn(-2);

        Node child10 = mock(Node.class);
        when(child10.getBoard()).thenReturn(board);
        when(child10.getValue()).thenReturn(-13);

        Node child11 = mock(Node.class);
        when(child11.getBoard()).thenReturn(board);
        when(child11.getValue()).thenReturn(Integer.MAX_VALUE);

        Node child12 = mock(Node.class);
        when(child12.getBoard()).thenReturn(board);
        when(child12.getValue()).thenReturn(0);

        child0.addChild(child00);
        child0.addChild(child01);
        child0.addChild(child02);

        child1.addChild(child10);
        child1.addChild(child11);
        child1.addChild(child12);

        //Run minimax on children and test for expected result
        decisionTree.addChild(child0);
        decisionTree.addChild(child1);

        CuT.setDecisionTree(decisionTree);

        LinkedList<Move> moves = CuT.getNextMoves(board);
        int value = CuT.getRootValue();
        assertEquals(child02.getValue(), value);
    }
}
