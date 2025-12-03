package ca.bgiroux.gravitrips.view;

import ca.bgiroux.gravitrips.controller.GameController;
import ca.bgiroux.gravitrips.controller.GameMode;
import ca.bgiroux.gravitrips.controller.StrategyLoader;
import ca.bgiroux.gravitrips.model.MoveStrategy;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class ConnectFourApp extends Application {
    private GameController controller;
    private List<MoveStrategy> strategies;

    @Override
    public void start(Stage stage) {
        BoardView boardView = new BoardView();
        strategies = new StrategyLoader().loadStrategies();
        controller = new GameController(boardView, strategies);

        BorderPane root = new BorderPane();
        root.setTop(createMenuBar(stage));
        root.setCenter(boardView);

        Scene scene = new Scene(root, 700, 650);
        stage.setTitle("Gravitrips");
        stage.setScene(scene);
        stage.show();

        controller.startNewGame(GameMode.HUMAN_VS_COMPUTER, strategies.get(0));
    }

    @Override
    public void stop() {
        controller.shutdown();
    }

    private MenuBar createMenuBar(Stage stage) {
        Menu gameMenu = new Menu("Game");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem exit = new MenuItem("Exit");

        newGame.setOnAction(e -> promptNewGame().ifPresent(selection ->
                controller.startNewGame(selection.mode(), selection.strategy())));
        exit.setOnAction(e -> stage.close());

        gameMenu.getItems().addAll(newGame, exit);
        return new MenuBar(gameMenu);
    }

    private Optional<GameSelection> promptNewGame() {
        Dialog<GameSelection> dialog = new Dialog<>();
        dialog.setTitle("New Game");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ToggleGroup modes = new ToggleGroup();
        RadioButton hvh = new RadioButton("Human vs Human");
        hvh.setToggleGroup(modes);
        hvh.setSelected(true);

        RadioButton hvc = new RadioButton("Human vs Computer");
        hvc.setToggleGroup(modes);

        ComboBox<MoveStrategy> strategyBox = new ComboBox<>();
        strategyBox.getItems().addAll(strategies);
        if (!strategies.isEmpty()) {
            strategyBox.getSelectionModel().select(0);
        }
        strategyBox.setCellFactory(list -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(MoveStrategy item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        strategyBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(MoveStrategy item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
        strategyBox.disableProperty().bind(hvh.selectedProperty());

        VBox content = new VBox(10,
                hvh,
                hvc,
                new Label("AI Strategy:"),
                strategyBox
        );
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                GameMode mode = modes.getSelectedToggle() == hvc ? GameMode.HUMAN_VS_COMPUTER : GameMode.HUMAN_VS_HUMAN;
                MoveStrategy strategy = strategyBox.getSelectionModel().getSelectedItem();
                if (strategy == null) {
                    strategy = strategies.isEmpty() ? null : strategies.get(0);
                }
                if (mode == GameMode.HUMAN_VS_COMPUTER && strategy == null) {
                    return null;
                }
                return new GameSelection(mode, strategy);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private record GameSelection(GameMode mode, MoveStrategy strategy) {
    }
}
