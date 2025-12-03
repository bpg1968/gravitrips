package ca.bgiroux.gravitrips.model;

public final class Move {
    private final int column;
    private final int row;
    private final int playerId;

    public Move(int column, int row, int playerId) {
        this.column = column;
        this.row = row;
        this.playerId = playerId;
    }

    public int column() {
        return column;
    }

    public int row() {
        return row;
    }

    public int playerId() {
        return playerId;
    }
}
