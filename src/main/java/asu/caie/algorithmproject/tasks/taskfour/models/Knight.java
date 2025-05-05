package asu.caie.algorithmproject.tasks.taskfour.models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class Knight extends ImageView {
	private static final String location = "/asu/caie/algorithmproject/taskfour/";
	private static final Image[] models = {new Image(Knight.class.getResourceAsStream(location + "white_knight.png")),
			new Image(Knight.class.getResourceAsStream(location + "black_knight.png"))};

	private Tile occupiedTile;

    public Knight(Tile tile, boolean isWhite){
		this.setPreserveRatio(true);
		this.setFitHeight(40);
		occupiedTile = tile;
		if (isWhite) {
			this.setImage(models[0]);
		} else {
			this.setImage(models[1]);
		}

		if (occupiedTile != null){
			initialize(occupiedTile);
		}
    }

	private void initialize(Tile tile){
		occupiedTile = tile;
		tile.getStackPane().getChildren().add(this);

		setOnMouseClicked(_ -> occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_CLICKED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
				true, true, true, true, true, true, true, true, true, true, null)));
		setOnMouseEntered(_ -> occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
				true, true, true, true, true, true, true, true, true, true, null)));
		setOnMouseExited(_ -> occupiedTile.fireEvent(new MouseEvent(MouseEvent.MOUSE_EXITED,0, 0, 0, 0, MouseButton.PRIMARY, 1,
				true, true, true, true, true, true, true, true, true, true, null)));
		tile.setOccupyingPiece(this);

		this.fitWidthProperty().bind(getStackPane().prefWidthProperty());
		this.fitHeightProperty().bind(getStackPane().prefHeightProperty());
	}

	public StackPane getStackPane() { return (StackPane)getParent(); }
	public Tile getOccupiedTile() { return occupiedTile; }
	public void setOccupiedTile(Tile occupiedTile) { this.occupiedTile = occupiedTile; }
}
