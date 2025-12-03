package ca.bgiroux.gravitrips.model;

import java.util.OptionalInt;

public class GameState {
    private final GameConfig config;
    private Board board;
    private int currentPlayerId;
    private boolean gameOver;
    private Integer winnerId;

    public GameState() {
        this(GameConfig.standard());
    }

    public GameState(GameConfig config) {
        this.config = config;
        this.board = new Board(config);
        this.currentPlayerId = 1;
        this.gameOver = false;
    }

    public MoveResult makeMove(int column) {
        if (gameOver) {
            return MoveResult.INVALID;
        }

        MoveResult result = board.applyMove(column, currentPlayerId);
        if (result == MoveResult.INVALID) {
            return result;
        }

        if (result == MoveResult.WIN) {
            gameOver = true;
            winnerId = currentPlayerId;
        } else if (result == MoveResult.DRAW) {
            gameOver = true;
        } else {
            switchPlayer();
        }
        return result;
    }

    public void reset() {
        this.board = new Board(config);
        this.currentPlayerId = 1;
        this.gameOver = false;
        this.winnerId = null;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public OptionalInt getWinnerId() {
        return winnerId == null ? OptionalInt.empty() : OptionalInt.of(winnerId);
    }

    public GameConfig getConfig() {
        return config;
    }

    private void switchPlayer() {
        currentPlayerId = currentPlayerId == 1 ? 2 : 1;
    }
}
