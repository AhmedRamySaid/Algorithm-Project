package asu.caie.algorithmproject.tasks.tasktwo.view;

import asu.caie.algorithmproject.Main;
import asu.caie.algorithmproject.MainScene;
import asu.caie.algorithmproject.tasks.taskfour.views.KnightSwapScene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TaskTwoSelectionScene {

    public TaskTwoSelectionScene() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: cyan");

        VBox newRoot = new VBox(20);
        newRoot.setAlignment(javafx.geometry.Pos.CENTER);

        Button classicGreedyButton = new Button("Classic greedy best first");
        classicGreedyButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(4));
        classicGreedyButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(10));

        Button backtrackingGreedy = new Button("Greedy (with backtracking)");
        backtrackingGreedy.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(4));
        backtrackingGreedy.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(10));


        Button backButton = new Button("Back");
        backButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(8));
        backButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(14));
        StackPane backButtonPane = new StackPane(backButton);
        backButtonPane.setAlignment(Pos.TOP_LEFT);

        classicGreedyButton.setOnAction(_ -> new KnightSwapScene(2, 8));
        backtrackingGreedy.setOnAction(_ -> new KnightSwapScene(3, 8));
        backButton.setOnAction(_ -> new MainScene());

        newRoot.getChildren().addAll(classicGreedyButton, backtrackingGreedy);
        pane.setCenter(newRoot);
        pane.setTop(backButtonPane);

        Main.primaryStage.getScene().setRoot(pane);
    }
}
