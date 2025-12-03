package ca.bgiroux.gravitrips;

import ca.bgiroux.gravitrips.model.Board;
import ca.bgiroux.gravitrips.model.MoveResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void detectsHorizontalWin() {
        Board board = new Board();

        board.applyMove(0, 1);
        board.applyMove(1, 1);
        board.applyMove(2, 1);
        MoveResult result = board.applyMove(3, 1);

        assertEquals(MoveResult.WIN, result);
    }

    @Test
    void detectsVerticalWin() {
        Board board = new Board();

        board.applyMove(0, 2);
        board.applyMove(0, 2);
        board.applyMove(0, 2);
        MoveResult result = board.applyMove(0, 2);

        assertEquals(MoveResult.WIN, result);
    }

    @Test
    void detectsDiagonalWin() {
        Board board = new Board();

        // Build a bottom-left to top-right diagonal for player 1
        board.applyMove(0, 1); // (5,0)

        board.applyMove(1, 2);
        board.applyMove(1, 1); // (4,1)

        board.applyMove(2, 2);
        board.applyMove(2, 2);
        board.applyMove(2, 1); // (3,2)

        board.applyMove(3, 2);
        board.applyMove(3, 2);
        board.applyMove(3, 2);
        MoveResult result = board.applyMove(3, 1); // (2,3)

        assertEquals(MoveResult.WIN, result);
    }

    @Test
    void rejectsMoveInFullColumn() {
        Board board = new Board();
        for (int i = 0; i < board.getRows(); i++) {
            board.applyMove(0, i % 2 + 1);
        }

        MoveResult result = board.applyMove(0, 1);

        assertEquals(MoveResult.INVALID, result);
    }

    @Test
    void detectsDrawWhenBoardIsFull() {
        Board board = new Board();

        int[][] columnPatterns = new int[][]{
                {1, 1, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1},
                {1, 1, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1},
                {1, 1, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1},
                {1, 1, 2, 2, 1, 2}
        };

        MoveResult lastResult = MoveResult.VALID;
        for (int col = 0; col < board.getColumns(); col++) {
            for (int value : columnPatterns[col]) {
                lastResult = board.applyMove(col, value);
                assertNotEquals(MoveResult.WIN, lastResult, "Unexpected win while filling board");
            }
        }

        assertEquals(MoveResult.DRAW, lastResult);
    }
}
