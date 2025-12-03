package ca.bgiroux.gravitrips.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private final int rows;
    private final int columns;
    private final int winLength;
    private final int[][] grid;

    public Board() {
        this(GameConfig.standard());
    }

    public Board(GameConfig config) {
        this.rows = config.rows();
        this.columns = config.columns();
        this.winLength = config.winLength();
        this.grid = new int[rows][columns];
    }

    private Board(int rows, int columns, int winLength, int[][] grid) {
        this.rows = rows;
        this.columns = columns;
        this.winLength = winLength;
        this.grid = grid;
    }

    public MoveResult applyMove(int column, int playerId) {
        if (column < 0 || column >= columns || playerId <= 0) {
            return MoveResult.INVALID;
        }
        int row = findOpenRow(column);
        if (row < 0) {
            return MoveResult.INVALID;
        }

        grid[row][column] = playerId;

        if (hasConnectFour(row, column, playerId)) {
            return MoveResult.WIN;
        }
        if (isFull()) {
            return MoveResult.DRAW;
        }
        return MoveResult.VALID;
    }

    public boolean isColumnFull(int column) {
        return grid[0][column] != 0;
    }

    public boolean isFull() {
        for (int c = 0; c < columns; c++) {
            if (!isColumnFull(c)) {
                return false;
            }
        }
        return true;
    }

    public int getCell(int row, int column) {
        return grid[row][column];
    }

    public List<Integer> getAvailableColumns() {
        List<Integer> open = new ArrayList<>();
        for (int c = 0; c < columns; c++) {
            if (!isColumnFull(c)) {
                open.add(c);
            }
        }
        return Collections.unmodifiableList(open);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getWinLength() {
        return winLength;
    }

    public Board copy() {
        int[][] clone = new int[rows][columns];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(grid[r], 0, clone[r], 0, columns);
        }
        return new Board(rows, columns, winLength, clone);
    }

    private int findOpenRow(int column) {
        for (int row = rows - 1; row >= 0; row--) {
            if (grid[row][column] == 0) {
                return row;
            }
        }
        return -1;
    }

    private boolean hasConnectFour(int row, int column, int playerId) {
        return countDirection(row, column, playerId, 0, 1) >= winLength
                || countDirection(row, column, playerId, 1, 0) >= winLength
                || countDirection(row, column, playerId, 1, 1) >= winLength
                || countDirection(row, column, playerId, 1, -1) >= winLength;
    }

    private int countDirection(int row, int column, int playerId, int rowDelta, int colDelta) {
        int count = 1; // include the placed token

        count += countOneSide(row, column, playerId, rowDelta, colDelta);
        count += countOneSide(row, column, playerId, -rowDelta, -colDelta);

        return count;
    }

    private int countOneSide(int row, int column, int playerId, int rowDelta, int colDelta) {
        int r = row + rowDelta;
        int c = column + colDelta;
        int count = 0;
        while (r >= 0 && r < rows && c >= 0 && c < columns && grid[r][c] == playerId) {
            count++;
            r += rowDelta;
            c += colDelta;
        }
        return count;
    }
}
