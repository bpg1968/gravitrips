package ca.bgiroux.gravitrips.model;

import java.util.Objects;

public final class GameConfig {
    private final int rows;
    private final int columns;
    private final int winLength;

    public GameConfig(int rows, int columns, int winLength) {
        if (rows < 4 || columns < 4) {
            throw new IllegalArgumentException("Board must be at least 4x4");
        }
        if (winLength < 3 || winLength > Math.max(rows, columns)) {
            throw new IllegalArgumentException("Invalid win length");
        }
        this.rows = rows;
        this.columns = columns;
        this.winLength = winLength;
    }

    public static GameConfig standard() {
        return new GameConfig(6, 7, 4);
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public int winLength() {
        return winLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameConfig that)) return false;
        return rows == that.rows && columns == that.columns && winLength == that.winLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, winLength);
    }
}
