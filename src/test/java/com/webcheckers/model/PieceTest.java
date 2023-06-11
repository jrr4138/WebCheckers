package com.webcheckers.model;

import com.webcheckers.model.board.Piece;
import com.webcheckers.model.board.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.webcheckers.model.states.PieceColor;
import com.webcheckers.model.states.PieceType;

/**
 * Piece class tester
 *
 * @author <a href='jrr4138@rit.edu'>Joshua Ross</a>
 */
public class PieceTest {
  private final PieceType SINGLE = PieceType.SINGLE;
  private final PieceType KING = PieceType.KING;
  private final PieceColor RED = PieceColor.RED;
  private final PieceColor WHITE = PieceColor.WHITE;
  private Piece piece;

  private Piece createPiece(PieceType type, PieceColor color, Position position){
    piece = new Piece(type, color, position);
    return piece;
  }

  @Test
  void test_single_and_red(){
    piece = createPiece(SINGLE, RED, new Position(0,0));
    PieceColor tmp = piece.getColor();
    PieceType type = piece.getType();
    assertEquals(RED, tmp);
    assertEquals(SINGLE, type);
  }

  @Test
  void test_single_and_white(){
    piece = createPiece(SINGLE, WHITE, new Position(0, 0));
    PieceColor tmp = piece.getColor();
    PieceType type = piece.getType();
    assertEquals(WHITE, tmp);
    assertEquals(SINGLE, type);
  }

  @Test
  void test_king_and_red(){
    piece = createPiece(KING, RED, new Position(0, 0));
    PieceColor tmp = piece.getColor();
    PieceType type = piece.getType();
    assertEquals(RED, tmp);
    assertEquals(KING, type);
  }

  @Test
  void test_king_and_white(){
    piece = createPiece(KING, WHITE, new Position(0,0));
    PieceColor tmp = piece.getColor();
    PieceType type = piece.getType();
    assertEquals(WHITE, tmp);
    assertEquals(KING, type);
  }
}
