package ca.bgiroux.gravitrips.view;

import ca.bgiroux.gravitrips.model.Board;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.function.IntConsumer;

public class BoardView extends BorderPane {
    private static final double CELL_SIZE = 70;
    private final GridPane gridPane = new GridPane();
    private Circle[][] discs = new Circle[0][0];
    private IntConsumer onColumnSelected;
    private int rows;
    private int columns;

    public BoardView() {
        setPadding(new Insets(10));
        gridPane.setHgap(6);
        gridPane.setVgap(6);
        setCenter(gridPane);
        setStyle("-fx-background-color: linear-gradient(to bottom, #1c3faa, #182b75);");
    }

    public void setOnColumnSelected(IntConsumer onColumnSelected) {
        this.onColumnSelected = onColumnSelected;
    }

    public void render(Board board) {
        if (board.getRows() != rows || board.getColumns() != columns) {
            rows = board.getRows();
            columns = board.getColumns();
            rebuildGrid();
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int cellValue = board.getCell(row, col);
                Circle circle = discs[row][col];
                if (cellValue == 1) {
                    circle.setFill(Color.CRIMSON);
                } else if (cellValue == 2) {
                    circle.setFill(Color.GOLD);
                } else {
                    circle.setFill(Color.WHITE);
                }
            }
        }
    }

    public void showInvalidMove() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid move");
        alert.setHeaderText(null);
        alert.setContentText("That column is full. Try a different one.");
        alert.show();
    }

    private void rebuildGrid() {
        gridPane.getChildren().clear();
        discs = new Circle[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                StackPane cell = createCell(row, col);
                gridPane.add(cell, col, row);
            }
        }
    }

    private StackPane createCell(int row, int col) {
        Rectangle slot = new Rectangle(CELL_SIZE, CELL_SIZE);
        slot.setArcWidth(16);
        slot.setArcHeight(16);
        slot.setFill(Color.rgb(33, 61, 160));

        Circle disc = new Circle(CELL_SIZE / 2 - 6);
        disc.setFill(Color.WHITE);
        discs[row][col] = disc;

        StackPane stack = new StackPane(slot, disc);
        stack.setOnMouseClicked(e -> {
            if (onColumnSelected != null) {
                onColumnSelected.accept(col);
            }
        });
        return stack;
    }
}
