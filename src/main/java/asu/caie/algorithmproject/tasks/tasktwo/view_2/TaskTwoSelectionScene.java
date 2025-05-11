package asu.caie.algorithmproject.tasks.tasktwo.view_2;

import asu.caie.algorithmproject.tasks.tasktwo.view_2.components.KnightBoard;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TaskTwoSelectionScene {

    public TaskTwoSelectionScene() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20");

        TextField sizeField = new TextField();
        sizeField.setPromptText("Board size > 3");
        TextField xField = new TextField();
        xField.setPromptText("Start X");
        TextField yField = new TextField();
        yField.setPromptText("Start Y");

        RadioButton greedy = new RadioButton("Greedy Only");
        RadioButton optimized = new RadioButton("Greedy + Backtracking");
        ToggleGroup algoGroup = new ToggleGroup();
        greedy.setToggleGroup(algoGroup);
        optimized.setToggleGroup(algoGroup);
        greedy.setSelected(true);

        Button startBtn = new Button("Start");

        startBtn.setOnAction(e -> {
            try {
                int n = Integer.parseInt(sizeField.getText());
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());

                if (n <= 3 || x < 0 || x >= n || y < 0 || y >= n) {
                    showAlert("Invalid input. Make sure n > 3 and coordinates are inside the board.");
                    return;
                }

                boolean useOptimized = optimized.isSelected();

                KnightBoard board = new KnightBoard(n, x, y, useOptimized);
                board.startTour((Stage) layout.getScene().getWindow());

            } catch (Exception ex) {
                showAlert("Please enter valid numeric inputs.");
            }
        });

        layout.getChildren().addAll(
                new Label("Board Size (>3):"), sizeField,
                new Label("Start X:"), xField,
                new Label("Start Y:"), yField,
                greedy, optimized,
                startBtn
        );

        Scene scene = new Scene(layout, 400, 400);
        asu.caie.algorithmproject.Main.primaryStage.setScene(scene);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
