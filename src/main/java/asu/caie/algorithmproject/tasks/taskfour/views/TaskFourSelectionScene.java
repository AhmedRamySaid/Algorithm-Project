package asu.caie.algorithmproject.tasks.taskfour.views;

import asu.caie.algorithmproject.Main;
import asu.caie.algorithmproject.MainScene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TaskFourSelectionScene {

	public TaskFourSelectionScene() {
		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: cyan");

		VBox newRoot = new VBox(20);
		newRoot.setAlignment(javafx.geometry.Pos.CENTER);

		Button beamButton = new Button("Beam algorithm");
		beamButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(4));
		beamButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(10));

		Button AStarButton = new Button("A* algorithm");
		AStarButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(4));
		AStarButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(10));

		Button backButton = new Button("Back");
		backButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(8));
		backButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(14));
		StackPane backButtonPane = new StackPane(backButton);
		backButtonPane.setAlignment(Pos.TOP_LEFT);

		beamButton.setOnAction(_ -> new KnightSwapScene(0));
		AStarButton.setOnAction(_ -> new KnightSwapScene(1));
		backButton.setOnAction(_ -> new MainScene());

		newRoot.getChildren().addAll(beamButton, AStarButton);
		pane.setCenter(newRoot);
		pane.setTop(backButtonPane);

		Main.primaryStage.getScene().setRoot(pane);
	}
}
