package asu.caie.algorithmproject;

import asu.caie.algorithmproject.tasks.taskfour.KnightSwapScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;
		primaryStage.show();
		primaryStage.setTitle("Algorithms Project");

		Scene scene = new Scene(new Pane(), 800, 600);
		primaryStage.setScene(scene);

		new KnightSwapScene(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
