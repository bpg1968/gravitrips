package ca.bgiroux.gravitrips;

import ca.bgiroux.gravitrips.model.Board;
import ca.bgiroux.gravitrips.model.RandomMoveStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomMoveStrategyTest {

    @Test
    void choosesAvailableColumn() {
        Board board = new Board();
        int[][] columnPatterns = new int[][]{
                {1, 1, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1},
                {1, 1, 2, 2, 1, 2},
                {}, // leave column 3 empty
                {1, 1, 2, 2, 1, 2},
                {2, 2, 1, 1, 2, 1},
                {1, 1, 2, 2, 1, 2}
        };

        for (int col = 0; col < board.getColumns(); col++) {
            for (int value : columnPatterns[col]) {
                board.applyMove(col, value);
            }
        }

        RandomMoveStrategy strategy = new RandomMoveStrategy();
        int move = strategy.chooseMove(board, 1);

        assertEquals(3, move);
    }
}
