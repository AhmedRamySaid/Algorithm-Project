package asu.caie.algorithmproject.tasks.taskfour;

import asu.caie.algorithmproject.tasks.taskfour.algorithms.KnightSwapAlgorithms;
import asu.caie.algorithmproject.tasks.taskfour.models.Knight;
import asu.caie.algorithmproject.tasks.taskfour.models.Tile;
import asu.caie.algorithmproject.tasks.tasktwo.algorithm.KnightTourAlgorithm;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

public class KnightSwapManager {
	public static Color tileMainColor =Color.WHITE;
	public static Color tileOffsetColor = Color.SADDLEBROWN;

	public Tile[][] boardTiles;
	public int MAX_WIDTH;
	public int MAX_HEIGHT;
	public String FEN;
	private Clip audio;
	private List<int[]> moves;

	public KnightSwapManager(int MAX_WIDTH, int MAX_HEIGHT, String FEN, int algoCode) {
		this.MAX_WIDTH = MAX_WIDTH;
		this.MAX_HEIGHT = MAX_HEIGHT;
		this.FEN = FEN;
		boardTiles = new Tile[MAX_WIDTH][MAX_HEIGHT];

		switch(algoCode) {
			case 0:
				moves = KnightSwapAlgorithms.solve();
				break;
			case 1:
				moves = KnightSwapAlgorithms.solveAStar();
				break;
			default:
				System.err.println("Invalid algorithm code");
				break;
		}

		if (!moves.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					"Found solution in " + moves.size() + " moves");
			alert.setTitle("Solution Found");
			alert.show();
		} else {
			Alert alert = new Alert(Alert.AlertType.INFORMATION,
					"No solution found");
			alert.setTitle("No Solution");
			alert.show();
		}
	}

	public void addTile(Tile tile) {
		boardTiles[tile.getXPosition()][tile.getYPosition()] = tile;
	}

	public void playMove() {
		if (moves.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "No more moves left");
			alert.setTitle("Game Over");
			alert.show();
			return;
		}
		// Gets the first move and then decodes it
		int[] move = moves.removeFirst();
		Tile startTile = boardTiles[move[0]][move[1]];
		Tile endTile = boardTiles[move[2]][move[3]];
		Knight piece = startTile.getOccupyingPiece();

		// Moves the knight object from the start tile to the end tile
		piece.setOccupiedTile(endTile);
		startTile.setOccupyingPiece(null);
		endTile.setOccupyingPiece(piece);

		// Moves the knight prefab from the start tile to the end tile
		startTile.getStackPane().getChildren().remove(piece);
		endTile.getStackPane().getChildren().add(piece);

		// Stops the audio then reset it then starts it again
		audio.stop();
		audio.setFramePosition(0);
		audio.start();
	}

	public void startGame() {
		int col = 0, row = MAX_HEIGHT-1;

		for (int i = 0; i < FEN.length(); i++) {
			char c = FEN.charAt(i);

			switch (c) {
				case 'n':
					new Knight(boardTiles[col][row], false);
					col++;
					break;
				case 'N':
					new Knight(boardTiles[col][row], true);
					col++;
					break;
				case '/':
					col = 0;
					row--;
					break;
				default:
					col += c - '0';
					break;
			}
		}
		try {
			BufferedInputStream moveStream = new BufferedInputStream(
					KnightSwapManager.class.getResourceAsStream("/asu/caie/algorithmproject/taskfour/move.wav")
			);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(moveStream);
			Clip moveClip = AudioSystem.getClip();
			moveClip.open(audioInputStream);
			audio = moveClip;
		}
		catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
			ex.printStackTrace();
		}
	}
}
