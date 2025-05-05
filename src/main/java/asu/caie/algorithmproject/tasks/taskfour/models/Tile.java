package asu.caie.algorithmproject.tasks.taskfour.models;

import asu.caie.algorithmproject.tasks.taskfour.KnightSwapManager;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private final int xPosition;
    private final int yPosition;
    private Knight occupyingPiece;

    public Tile(int x, int y, StackPane pane) {
        pane.getChildren().add(this);
        xPosition = x;
        yPosition = y;

        setOnMouseEntered(_ -> this.setOpacity(0.5));
        setOnMouseExited(_ -> this.setOpacity(1));

        this.setColor(KnightSwapManager.tileMainColor,
				KnightSwapManager.tileOffsetColor);
        this.widthProperty().bind(getStackPane().prefWidthProperty());
        this.heightProperty().bind(getStackPane().prefHeightProperty());
    }

    public void setColor(Color mainColor, Color offsetColor) {
        if (xPosition%2 == yPosition%2){
            this.setFill(mainColor);
        }
        else{
            this.setFill(offsetColor);
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Tile other){
			return other.getXPosition() == this.getXPosition() && other.getYPosition() == this.getYPosition();
        }
        return false;
    }

    public void setOccupyingPiece(Knight piece) { occupyingPiece = piece; }
    public Knight getOccupyingPiece() { return occupyingPiece; }
    public StackPane getStackPane() { return (StackPane)getParent(); }
    public int getXPosition(){ return xPosition; }
    public int getYPosition(){ return yPosition; }
}
