package asu.caie.algorithmproject;

import asu.caie.algorithmproject.tasks.taskfour.views.TaskFourSelectionScene;
import asu.caie.algorithmproject.tasks.tasksix.views.TaskSixSelectionScene;
import asu.caie.algorithmproject.tasks.tasktwo.view.TaskTwoSelectionScene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainScene {

	public MainScene() {
		VBox root = new VBox(20);
		root.setAlignment(javafx.geometry.Pos.CENTER);
		root.setStyle("-fx-background-color: cyan");

		for (int i = 1; i <= 6; i++) {
			Button button = new Button("Task " + (i));
			button.prefWidthProperty().bind(Main.primaryStage.widthProperty().divide(6));
			button.prefHeightProperty().bind(Main.primaryStage.heightProperty().divide(12));
			// Create final copy as lambda expressions can't use changing variables
			final int taskNumber = i;
			button.setOnAction(_ -> {
				switch (taskNumber) {
					case 1 -> System.out.println("Task 1");
					case 2 -> new TaskTwoSelectionScene();
					case 3 -> System.out.println("Task 3");
					case 4 -> new TaskFourSelectionScene();
					case 5 -> System.out.println("Task 5");
					case 6 -> new TaskSixSelectionScene();
				}
			});
			root.getChildren().add(button);
		}

		Main.primaryStage.getScene().setRoot(root);
	}
}
