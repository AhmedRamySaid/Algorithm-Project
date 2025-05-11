package asu.caie.algorithmproject.tasks.researchtasks;

import asu.caie.algorithmproject.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JavaFX Application for Graph Coloring Algorithm Visualization
 */
public class GraphColoringVisualization {

	private Canvas canvas;
	private GraphicsContext gc;
	private Graph graph;
	private ComboBox<String> graphTypeComboBox;
	private Spinner<Integer> vertexCountSpinner;
	private Spinner<Integer> edgeProbabilitySpinner;
	private VBox customControlsBox;
	private TextArea infoTextArea;
	private Button solveButton;
	private Button stepButton;
	private Button resetButton;

	// Animation state
	private AnimationState animationState = new AnimationState();

	// Color palette for vertices
	private static final Color[] COLORS = {
			Color.WHITE,       // 0 - uncolored
			Color.INDIANRED,   // 1 - red
			Color.ROYALBLUE,   // 2 - blue
			Color.GOLD,        // 3 - yellow
			Color.MEDIUMTURQUOISE, // 4 - teal
			Color.MEDIUMPURPLE,// 5 - purple
			Color.DARKORANGE,  // 6 - orange
			Color.LIGHTGRAY,   // 7 - gray
			Color.LIMEGREEN,   // 8 - lime
			Color.HOTPINK      // 9 - pink
	};

	 public GraphColoringVisualization() {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(10));

		// Top control panel
		VBox controlPanel = createControlPanel();
		root.setTop(controlPanel);

		// Center canvas
		canvas = new Canvas(600, 500);
		gc = canvas.getGraphicsContext2D();

		BorderPane canvasContainer = new BorderPane(canvas);
		canvasContainer.setPadding(new Insets(10));
		canvasContainer.setStyle("-fx-background-color: white; -fx-border-color: #cccccc;");

		root.setCenter(canvasContainer);

		// Bottom info panel
		infoTextArea = new TextArea();
		infoTextArea.setEditable(false);
		infoTextArea.setPrefHeight(120);
		infoTextArea.setWrapText(true);
		infoTextArea.setText("Select a graph type and click \"Generate Graph\" to begin.");

		VBox bottomPanel = new VBox(10);
		bottomPanel.setPadding(new Insets(10, 0, 0, 0));
		Label infoLabel = new Label("Graph Information:");
		infoLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
		bottomPanel.getChildren().addAll(infoLabel, infoTextArea);
		root.setBottom(bottomPanel);

		Main.primaryStage.getScene().setRoot(root);
	}

	/**
	 * Creates the control panel with buttons and dropdown
	 */
	private VBox createControlPanel() {
		VBox controlPanel = new VBox(10);
		controlPanel.setPadding(new Insets(0, 0, 10, 0));

		// Graph selection controls
		HBox graphSelectionBox = new HBox(10);
		graphSelectionBox.setAlignment(Pos.CENTER_LEFT);

		Label graphTypeLabel = new Label("Select Graph Type:");

		graphTypeComboBox = new ComboBox<>();
		graphTypeComboBox.getItems().addAll(
				"Petersen Graph (Chromatic Number: 3)",
				"Complete Graph K5 (Chromatic Number: 5)",
				"4x4 Grid Graph (Chromatic Number: 2)",
				"Wheel Graph W8 (Chromatic Number: 4)",
				"Custom Random Graph"
		);
		graphTypeComboBox.setValue("Petersen Graph (Chromatic Number: 3)");
		graphTypeComboBox.setPrefWidth(300);

		Button generateButton = new Button("Generate Graph");
		generateButton.setOnAction(e -> generateGraph());

		graphSelectionBox.getChildren().addAll(graphTypeLabel, graphTypeComboBox, generateButton);

		// Custom graph controls
		customControlsBox = new VBox(10);
		customControlsBox.setPadding(new Insets(5, 0, 0, 10));

		HBox vertexBox = new HBox(10);
		vertexBox.setAlignment(Pos.CENTER_LEFT);
		Label vertexCountLabel = new Label("Number of Vertices:");
		vertexCountSpinner = new Spinner<>(3, 20, 10);
		vertexCountSpinner.setEditable(true);
		vertexCountSpinner.setPrefWidth(70);

		HBox edgeBox = new HBox(10);
		edgeBox.setAlignment(Pos.CENTER_LEFT);
		Label edgeProbabilityLabel = new Label("Edge Density (%):");
		edgeProbabilitySpinner = new Spinner<>(10, 90, 50);
		edgeProbabilitySpinner.setEditable(true);
		edgeProbabilitySpinner.setPrefWidth(70);

		vertexBox.getChildren().addAll(vertexCountLabel, vertexCountSpinner);
		edgeBox.getChildren().addAll(edgeProbabilityLabel, edgeProbabilitySpinner);

		customControlsBox.getChildren().addAll(vertexBox, edgeBox);
		customControlsBox.setVisible(false);

		// Add listener to show/hide custom controls
		graphTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
			customControlsBox.setVisible(newVal.contains("Custom"));
		});

		// Algorithm control buttons
		HBox buttonBox = new HBox(10);
		buttonBox.setPadding(new Insets(10, 0, 0, 0));
		buttonBox.setAlignment(Pos.CENTER_LEFT);

		solveButton = new Button("Solve Coloring");
		solveButton.setOnAction(e -> solveColoring());

		stepButton = new Button("Step Algorithm");
		stepButton.setOnAction(e -> stepAnimation());

		resetButton = new Button("Reset");
		resetButton.setOnAction(e -> resetGraph());

		buttonBox.getChildren().addAll(solveButton, stepButton, resetButton);

		// Add all controls to panel
		controlPanel.getChildren().addAll(
				graphSelectionBox,
				customControlsBox,
				new Separator(),
				buttonBox
		);

		return controlPanel;
	}

	/**
	 * Generate a graph based on the selected type
	 */
	private void generateGraph() {
		String selection = graphTypeComboBox.getValue();

		if (selection.contains("Petersen")) {
			graph = createPetersenGraph();
		} else if (selection.contains("Complete")) {
			graph = createCompleteGraph(5);
		} else if (selection.contains("Grid")) {
			graph = createGridGraph(4, 4);
		} else if (selection.contains("Wheel")) {
			graph = createWheelGraph(8);
		} else if (selection.contains("Custom")) {
			int vertices = vertexCountSpinner.getValue();
			int probability = edgeProbabilitySpinner.getValue();
			graph = createRandomGraph(vertices, probability);
		}

		// Reset animation state
		animationState = new AnimationState();

		drawGraph();
		updateGraphInfo();
	}

	/**
	 * Create a Petersen graph (chromatic number 3)
	 */
	private Graph createPetersenGraph() {
		Graph graph = new Graph(10);

		// Calculate positions in a nice layout
		double outerRadius = 180;
		double innerRadius = 100;
		double centerX = canvas.getWidth() / 2;
		double centerY = canvas.getHeight() / 2;

		// Outer pentagon vertices (0-4)
		for (int i = 0; i < 5; i++) {
			double angle = (i * 2 * Math.PI / 5) - Math.PI/2;
			graph.positions.add(new Position(
					centerX + outerRadius * Math.cos(angle),
					centerY + outerRadius * Math.sin(angle)
			));
		}

		// Inner pentagon vertices (5-9)
		for (int i = 0; i < 5; i++) {
			double angle = ((i * 2 * Math.PI / 5) + Math.PI/5) - Math.PI/2;
			graph.positions.add(new Position(
					centerX + innerRadius * Math.cos(angle),
					centerY + innerRadius * Math.sin(angle)
			));
		}

		// Outer pentagon edges
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(2, 3);
		graph.addEdge(3, 4);
		graph.addEdge(4, 0);

		// Inner pentagon edges (with specific connections for Petersen graph)
		graph.addEdge(5, 7);
		graph.addEdge(7, 9);
		graph.addEdge(9, 6);
		graph.addEdge(6, 8);
		graph.addEdge(8, 5);

		// Connecting edges
		graph.addEdge(0, 5);
		graph.addEdge(1, 6);
		graph.addEdge(2, 7);
		graph.addEdge(3, 8);
		graph.addEdge(4, 9);

		return graph;
	}

	/**
	 * Create a complete graph Kn (chromatic number n)
	 */
	private Graph createCompleteGraph(int n) {
		Graph graph = new Graph(n);

		// Calculate positions in a circle
		double radius = 180;
		double centerX = canvas.getWidth() / 2;
		double centerY = canvas.getHeight() / 2;

		for (int i = 0; i < n; i++) {
			double angle = (i * 2 * Math.PI / n) - Math.PI/2;
			graph.positions.add(new Position(
					centerX + radius * Math.cos(angle),
					centerY + radius * Math.sin(angle)
			));
		}

		// Add all possible edges
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				graph.addEdge(i, j);
			}
		}

		return graph;
	}

	/**
	 * Create a grid graph (chromatic number 2 for bipartite)
	 */
	private Graph createGridGraph(int rows, int cols) {
		int n = rows * cols;
		Graph graph = new Graph(n);

		// Calculate positions in a grid
		double cellWidth = 400.0 / (double)cols;
		double cellHeight = 400.0 / (double)rows;
		double startX = (canvas.getWidth() - (cols - 1) * cellWidth) / 2;
		double startY = (canvas.getHeight() - (rows - 1) * cellHeight) / 2;

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				graph.positions.add(new Position(
						startX + c * cellWidth,
						startY + r * cellHeight
				));
			}
		}

		// Add edges
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int vertex = r * cols + c;

				// Connect to right neighbor
				if (c < cols - 1) {
					graph.addEdge(vertex, vertex + 1);
				}

				// Connect to bottom neighbor
				if (r < rows - 1) {
					graph.addEdge(vertex, vertex + cols);
				}
			}
		}

		return graph;
	}

	/**
	 * Create a wheel graph (chromatic number 3 or 4)
	 */
	private Graph createWheelGraph(int n) {
		// n is the total vertices including center (so rim has n-1 vertices)
		Graph graph = new Graph(n);

		double radius = 180;
		double centerX = canvas.getWidth() / 2;
		double centerY = canvas.getHeight() / 2;

		// Center vertex at position 0
		graph.positions.add(new Position(centerX, centerY));

		// Rim vertices
		for (int i = 1; i < n; i++) {
			double angle = ((i - 1) * 2 * Math.PI / (n - 1)) - Math.PI/2;
			graph.positions.add(new Position(
					centerX + radius * Math.cos(angle),
					centerY + radius * Math.sin(angle)
			));
		}

		// Connect center to all rim vertices
		for (int i = 1; i < n; i++) {
			graph.addEdge(0, i);
		}

		// Connect rim vertices in a cycle
		for (int i = 1; i < n; i++) {
			graph.addEdge(i, i == n - 1 ? 1 : i + 1);
		}

		return graph;
	}

	/**
	 * Create a random graph with given vertex count and edge probability
	 */
	private Graph createRandomGraph(int vertices, int probability) {
		Graph graph = new Graph(vertices);

		// Calculate positions in a circle
		double radius = 180;
		double centerX = canvas.getWidth() / 2;
		double centerY = canvas.getHeight() / 2;

		for (int i = 0; i < vertices; i++) {
			double angle = (i * 2 * Math.PI / vertices) - Math.PI/2;
			graph.positions.add(new Position(
					centerX + radius * Math.cos(angle),
					centerY + radius * Math.sin(angle)
			));
		}

		// Add random edges based on probability
		Random random = new Random();
		for (int i = 0; i < vertices; i++) {
			for (int j = i + 1; j < vertices; j++) {
				if (random.nextInt(100) < probability) {
					graph.addEdge(i, j);
				}
			}
		}

		return graph;
	}

	/**
	 * Draw the current state of the graph
	 */
	private void drawGraph() {
		if (graph == null) return;

		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Draw edges
		gc.setStroke(Color.DARKGRAY);
		gc.setLineWidth(2);

		for (int i = 0; i < graph.V; i++) {
			for (int j : graph.adjList.get(i)) {
				if (i < j) { // To avoid drawing edges twice
					gc.beginPath();
					gc.moveTo(graph.positions.get(i).x, graph.positions.get(i).y);
					gc.lineTo(graph.positions.get(j).x, graph.positions.get(j).y);
					gc.stroke();
				}
			}
		}

		// Draw vertices
		for (int i = 0; i < graph.V; i++) {
			int colorIndex = Math.min(graph.colors[i], COLORS.length - 1);
			Position pos = graph.positions.get(i);

			gc.setFill(COLORS[colorIndex]);
			gc.fillOval(pos.x - 20, pos.y - 20, 40, 40);

			gc.setStroke(Color.BLACK);
			gc.setLineWidth(2);
			gc.strokeOval(pos.x - 20, pos.y - 20, 40, 40);

			// Vertex label
			gc.setFill(Color.BLACK);
			gc.setFont(Font.font("System", FontWeight.BOLD, 14));
			gc.fillText(String.valueOf(i), pos.x - 4, pos.y + 5);
		}
	}

	/**
	 * Solve the graph coloring problem and animate the solution
	 */
	private void solveColoring() {
		if (graph == null) return;

		// Reset colors
		Arrays.fill(graph.colors, 0);
		drawGraph();

		// Store original state for animation
		int[] originalColors = Arrays.copyOf(graph.colors, graph.colors.length);

		// Solve
		graph.solveGreedy();

		// Setup animation
		animationState = new AnimationState();
		animationState.step = 0;
		animationState.maxSteps = graph.V;

		// Create animation sequence
		for (int i = 0; i < graph.V; i++) {
			animationState.vertices.add(i);
			animationState.colors.add(graph.colors[i]);
		}

		// Reset graph colors for animation
		graph.colors = Arrays.copyOf(originalColors, originalColors.length);

		// Start animation
		animateColoring();
	}

	/**
	 * Animate one step of the coloring process
	 */
	private void animateColoring() {
		if (animationState.step >= animationState.maxSteps) {
			return;
		}

		int currentVertex = animationState.vertices.get(animationState.step);
		graph.colors[currentVertex] = animationState.colors.get(animationState.step);

		drawGraph();
		animationState.step++;

		if (animationState.step >= animationState.maxSteps) {
			updateGraphInfo();
		}
	}

	/**
	 * Step through the animation one vertex at a time
	 */
	private void stepAnimation() {
		if (graph == null || animationState.maxSteps == 0) return;
		animateColoring();
	}

	/**
	 * Reset the graph to uncolored state
	 */
	private void resetGraph() {
		if (graph == null) return;

		Arrays.fill(graph.colors, 0);

		// Reset animation state
		animationState = new AnimationState();

		drawGraph();
		updateGraphInfo();
	}

	/**
	 * Update the information text area with graph details
	 */
	private void updateGraphInfo() {
		if (graph == null) return;

		Map<Integer, Integer> colorCounts = new HashMap<>();
		int maxColor = 0;

		for (int i = 0; i < graph.V; i++) {
			int color = graph.colors[i];
			if (color > maxColor) maxColor = color;

			colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
		}

		String graphType = graphTypeComboBox.getValue().split("\\(")[0].trim();

		StringBuilder info = new StringBuilder();
		info.append("Type: ").append(graphType).append("\n");
		info.append("Vertices: ").append(graph.V).append("\n");

		// Count edges
		AtomicInteger edgeCount = new AtomicInteger(0);
		for (int i = 0; i < graph.V; i++) {
			edgeCount.addAndGet(graph.adjList.get(i).size());
		}
		edgeCount.set(edgeCount.get() / 2); // Each edge is counted twice in adjacency list

		info.append("Edges: ").append(edgeCount.get()).append("\n");
		info.append("Chromatic Number: ").append(maxColor).append("\n\n");

		info.append("Colors Used:\n");
		for (int i = 1; i <= maxColor; i++) {
			info.append("Color ").append(i).append(": ")
					.append(colorCounts.getOrDefault(i, 0)).append(" vertices\n");
		}

		info.append("\nValid Coloring: ").append(graph.isValidColoring() ? "Yes ✓" : "No ✗");

		infoTextArea.setText(info.toString());
	}

	/**
	 * Graph representation class
	 */
	private static class Graph {
		private final int V; // Number of vertices
		private final List<List<Integer>> adjList; // Adjacency list
		private int[] colors; // Colors assigned to vertices
		private final List<Position> positions; // Positions for drawing

		public Graph(int vertices) {
			this.V = vertices;
			this.adjList = new ArrayList<>(vertices);
			this.colors = new int[vertices];
			this.positions = new ArrayList<>(vertices);

			// Initialize adjacency lists
			for (int i = 0; i < vertices; i++) {
				adjList.add(new ArrayList<>());
				colors[i] = 0; // 0 means uncolored
			}
		}

		public void addEdge(int u, int v) {
			adjList.get(u).add(v);
			adjList.get(v).add(u);
		}

		/**
		 * Simple greedy coloring algorithm for visualization
		 */
		public void solveGreedy() {
			boolean[] available = new boolean[V + 1];
			Arrays.fill(available, true);

			// Assign the first color to first vertex
			colors[0] = 1;

			// Assign colors to remaining V-1 vertices
			for (int u = 1; u < V; u++) {
				// Mark colors of adjacent vertices as unavailable
				for (int v : adjList.get(u)) {
					if (colors[v] > 0) {
						available[colors[v]] = false;
					}
				}

				// Find the first available color
				int cr;
				for (cr = 1; cr <= V; cr++) {
					if (available[cr]) {
						break;
					}
				}

				// Assign the found color
				colors[u] = cr;

				// Reset the available colors for next iteration
				for (int v : adjList.get(u)) {
					if (colors[v] > 0) {
						available[colors[v]] = true;
					}
				}
			}
		}

		/**
		 * Verify if the coloring is valid
		 */
		public boolean isValidColoring() {
			for (int i = 0; i < V; i++) {
				for (int j : adjList.get(i)) {
					if (colors[i] == colors[j] && colors[i] != 0) {
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * Position class for vertex coordinates
	 */
	private static class Position {
		double x;
		double y;

		public Position(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Animation state class
	 */
	private static class AnimationState {
		int step = 0;
		int maxSteps = 0;
		List<Integer> vertices = new ArrayList<>();
		List<Integer> colors = new ArrayList<>();
	}
}