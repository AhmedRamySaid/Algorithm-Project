package asu.caie.algorithmproject;

import asu.caie.algorithmproject.tasks.MainScene;
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

		new MainScene();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
