package ca.bgiroux.gravitrips.controller;

import ca.bgiroux.gravitrips.model.Board;
import ca.bgiroux.gravitrips.model.ComputerPlayer;
import ca.bgiroux.gravitrips.model.GameState;
import ca.bgiroux.gravitrips.model.HumanPlayer;
import ca.bgiroux.gravitrips.model.MoveResult;
import ca.bgiroux.gravitrips.model.MoveStrategy;
import ca.bgiroux.gravitrips.model.Player;
import ca.bgiroux.gravitrips.model.RandomMoveStrategy;
import ca.bgiroux.gravitrips.view.BoardView;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class GameController {
    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    private final BoardView boardView;
    private final List<MoveStrategy> availableStrategies;
    private final ExecutorService aiExecutor = Executors.newSingleThreadExecutor();

    private GameState gameState;
    private Player playerOne;
    private Player playerTwo;
    private GameMode mode;
    private MoveStrategy aiStrategy;

    public GameController(BoardView boardView, List<MoveStrategy> availableStrategies) {
        this.boardView = Objects.requireNonNull(boardView);
        this.availableStrategies = Objects.requireNonNull(availableStrategies);
        this.gameState = new GameState();
        this.mode = GameMode.HUMAN_VS_HUMAN;
        this.aiStrategy = availableStrategies.isEmpty() ? new RandomMoveStrategy() : availableStrategies.get(0);
        initializePlayers();
        wireEvents();
        boardView.render(gameState.getBoard());
    }

    public List<MoveStrategy> getAvailableStrategies() {
        return availableStrategies;
    }

    public void startNewGame(GameMode mode, MoveStrategy aiStrategy) {
        this.mode = mode;
        this.aiStrategy = aiStrategy != null ? aiStrategy : availableStrategies.get(0);
        this.gameState = new GameState(gameState.getConfig());
        initializePlayers();
        boardView.render(gameState.getBoard());
        if (isComputerTurn()) {
            runAiTurn();
        }
    }

    public void handleColumnSelected(int column) {
        if (gameState.isGameOver()) {
            return;
        }
        Player current = getCurrentPlayer();
        if (current instanceof ComputerPlayer) {
            return;
        }
        applyMove(column, current);
    }

    public void shutdown() {
        aiExecutor.shutdownNow();
    }

    private void wireEvents() {
        boardView.setOnColumnSelected(this::handleColumnSelected);
    }

    private void initializePlayers() {
        this.playerOne = new HumanPlayer(1, "Red Player");
        if (mode == GameMode.HUMAN_VS_COMPUTER) {
            String aiName = aiStrategy.getName() + " AI";
            this.playerTwo = new ComputerPlayer(2, aiName, this.aiStrategy);
        } else {
            this.playerTwo = new HumanPlayer(2, "Yellow Player");
        }
    }

    private void applyMove(int column, Player current) {
        MoveResult result = gameState.makeMove(column);
        if (result == MoveResult.INVALID) {
            boardView.showInvalidMove();
            return;
        }

        boardView.render(gameState.getBoard());

        if (result == MoveResult.WIN) {
            announceWinner(current.getName());
            LOGGER.info(() -> current.getName() + " wins the game");
        } else if (result == MoveResult.DRAW) {
            announceDraw();
            LOGGER.info("Game ended in a draw");
        } else if (isComputerTurn()) {
            runAiTurn();
        }
    }

    private boolean isComputerTurn() {
        return mode == GameMode.HUMAN_VS_COMPUTER && getCurrentPlayer() instanceof ComputerPlayer;
    }

    private Player getCurrentPlayer() {
        return gameState.getCurrentPlayerId() == 1 ? playerOne : playerTwo;
    }

    private void runAiTurn() {
        Player current = getCurrentPlayer();
        if (!(current instanceof ComputerPlayer computer)) {
            return;
        }
        aiExecutor.submit(() -> {
            Board copy = gameState.getBoard().copy();
            int column = computer.getStrategy().chooseMove(copy, computer.getId());
            Platform.runLater(() -> applyMove(column, computer));
        });
    }

    private void announceWinner(String name) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gravitrips");
            alert.setHeaderText("We have a winner!");
            alert.setContentText(name + " wins the game.");
            alert.showAndWait();
        });
    }

    private void announceDraw() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gravitrips");
            alert.setHeaderText("It's a draw");
            alert.setContentText("Nobody wins this time. Play again?");
            alert.showAndWait();
        });
    }
}
