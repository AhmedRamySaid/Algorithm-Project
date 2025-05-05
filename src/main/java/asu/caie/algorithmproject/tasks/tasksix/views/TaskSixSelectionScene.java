package asu.caie.algorithmproject.tasks.tasksix.views;

import asu.caie.algorithmproject.Main;
import asu.caie.algorithmproject.MainScene;
import asu.caie.algorithmproject.tasks.taskfour.views.KnightSwapScene;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class TaskSixSelectionScene {

	public TaskSixSelectionScene() {
		BorderPane pane = new BorderPane();
		pane.setStyle("-fx-background-color: cyan");

		VBox newRoot = new VBox(20);
		newRoot.setAlignment(javafx.geometry.Pos.CENTER);

		Button DPButton = new Button("Dynamic programming algorithm");
		DPButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(4));
		DPButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(10));

		Button approachTwoButton = new Button("Approach two algorithm");
		approachTwoButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(4));
		approachTwoButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(10));

		Button backButton = new Button("Back");
		backButton.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(8));
		backButton.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(14));
		StackPane backButtonPane = new StackPane(backButton);
		backButtonPane.setAlignment(Pos.TOP_LEFT);

		DPButton.setOnAction(_ -> new PathVisualizer(true));
		approachTwoButton.setOnAction(_ -> new PathVisualizer(false));
		backButton.setOnAction(_ -> new MainScene());

		newRoot.getChildren().addAll(DPButton, approachTwoButton);
		pane.setCenter(newRoot);
		pane.setTop(backButtonPane);

		Main.primaryStage.getScene().setRoot(pane);
	}
}