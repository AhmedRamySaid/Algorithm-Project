package asu.caie.algorithmproject.tasks.tasktwo.view_2.components;

import asu.caie.algorithmproject.tasks.tasktwo.algorithm.KnightTourAlgorithm;
import asu.caie.algorithmproject.tasks.tasktwo.algorithm.KnightTourResult;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class KnightBoard {

    private final int size;
    private final int startX, startY;
    private final boolean useOptimized;
    private final KnightTourResult tourResult;
    private List<int[]> path;
    private int step = 0;

    private final GridPane boardGrid = new GridPane();
    private Rectangle[][] cells;  // تعريف الخلايا هنا (المصفوفة)

    public KnightBoard(int size, int startX, int startY, boolean useOptimized) {
        this.size = size;
        this.startX = startX;
        this.startY = startY;
        this.useOptimized = useOptimized;

        // تهيئة الخلايا بناءً على الحجم المحدد
        cells = new Rectangle[size][size];  // تهيئة المصفوفة التي تحتوي على الخلايا

        // استدعاء الخوارزمية بناءً على الخيار
        if (useOptimized) {
            this.tourResult = KnightTourAlgorithm.start_solve_greedy_optimized(size, startX, startY);
        } else {
            this.tourResult = KnightTourAlgorithm.start_solve_greedy(size, startX, startY);
        }

        this.path = tourResult.path;
    }

    public KnightTourResult getTourResult() {
        return tourResult;
    }

    public void startTour(Stage stage) {
        boardGrid.setAlignment(Pos.CENTER);
        drawBoard();

        Button nextButton = new Button("Next move");
        nextButton.setOnAction(e -> {
            if (step < path.size()) {
                int[] pos = path.get(step);
                markCell(pos[0], pos[1], step + 1);
                step++;
            } else {
                nextButton.setDisable(true);
            }
        });

        VBox layout = new VBox(10, boardGrid, nextButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 650);
        stage.setScene(scene);
        stage.setTitle("Knight's Tour Visualization");
        stage.show();
    }

    private void drawBoard() {
        boardGrid.getChildren().clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Rectangle cell = new Rectangle(60, 60);
                cell.setFill((i + j) % 2 == 0 ? Color.BEIGE : Color.BROWN);
                cell.setStroke(Color.BLACK);
                cells[i][j] = cell;  // تهيئة الخلايا هنا

                StackPane cellStack = new StackPane(cell);
                boardGrid.add(cellStack, j, i);
            }
        }
    }

    private void markCell(int x, int y, int stepNumber) {
        StackPane cellStack = new StackPane(cells[x][y], new Text(String.valueOf(stepNumber)));
        boardGrid.add(cellStack, y, x);
    }


}