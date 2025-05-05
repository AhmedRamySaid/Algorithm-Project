package asu.caie.algorithmproject.tasks.taskfour;

import asu.caie.algorithmproject.Main;
import asu.caie.algorithmproject.tasks.taskfour.models.Tile;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class KnightSwapScene {
	KnightSwapManager manager;

	public KnightSwapScene(int algoCode) {
		manager = new KnightSwapManager(3, 4, "nnn/3/3/NNN", algoCode);
		// Create the GridPane for the chessboard
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);

		// Create a StackPane to hold the grid
		VBox centerPane = new VBox(20);
		centerPane.setAlignment(Pos.CENTER);
		centerPane.setStyle("-fx-background-color: cyan");

		// Set padding to scale with the smaller side length of the window
		NumberBinding binding = Bindings.min(Main.primaryStage.widthProperty(),
				Main.primaryStage.heightProperty());

		// Instantiate the tiles
		for (int i = 0; i < manager.MAX_WIDTH; i++) {
			for (int j = 0; j < manager.MAX_HEIGHT; j++) {
				StackPane stackPane = new StackPane();
				manager.addTile(new Tile(i, j, stackPane));
				stackPane.prefWidthProperty().bind(binding.divide(12));
				stackPane.prefHeightProperty().bind(binding.divide(12));

				// GridPane starts from {0,0} and the top left
				gridPane.add(stackPane, i, manager.MAX_HEIGHT-j-1);
			}
		}
		manager.startGame();


		Button playMoveButton = new Button("Play move");
		playMoveButton.prefWidthProperty().bind(binding.divide(8));
		playMoveButton.prefHeightProperty().bind(binding.divide(12));
		playMoveButton.setOnAction(_ -> manager.playMove());

		centerPane.getChildren().addAll(gridPane, playMoveButton);

		// Sets the layout without changing the scene
		Main.primaryStage.getScene().setRoot(centerPane);
	}
}
