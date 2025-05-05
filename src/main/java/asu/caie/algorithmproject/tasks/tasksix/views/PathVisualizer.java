package asu.caie.algorithmproject.tasks.tasksix.views;

import asu.caie.algorithmproject.tasks.tasksix.models.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import static asu.caie.algorithmproject.tasks.tasksix.algorithms.ApproachTwo.runAlgo;
import static asu.caie.algorithmproject.tasks.tasksix.algorithms.DotsPuzzleSolver.solveWithDP;

public class PathVisualizer extends JFrame {
	private LinkedList<Point> path;
	private int n;
	private static final int CELL_SIZE = 50;
	private static final int PADDING = 30;
	private PathPanel pathPanel;
	private int currentSegmentIndex = 0;
	private Timer animationTimer;
	private ArrayList<Color> segmentColors;
	private static final int ANIMATION_DELAY = 500; // milliseconds between segments
	private boolean isDP;

	public PathVisualizer(boolean isDP) {
		this.isDP = isDP;
		if (isDP) {
			DPVisualization();
		} else {
			approachTwoVisualization();
		}
	}

	public PathVisualizer(LinkedList<Point> path, int n) {
		this.path = path;
		this.n = n;

		setTitle("Dots Puzzle Solver - Grid Size: " + n);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Set the size of the window based on the grid size and padding
		int windowWidth = (n + 1) * CELL_SIZE + 2 * PADDING;
		int windowHeight = (n + 1) * CELL_SIZE + 2 * PADDING + 50; // Extra space for controls
		setSize(windowWidth, windowHeight);

		// Generate random colors for each path segment
		generateSegmentColors();

		// Set up the layout
		setLayout(new BorderLayout());

		// Add the custom drawing panel
		pathPanel = new PathPanel();
		add(pathPanel, BorderLayout.CENTER);

		// Add control panel
		JPanel controlPanel = new JPanel();
		JButton startButton = new JButton("Start Animation");
		JButton resetButton = new JButton("Reset");
		JButton newPuzzleButton = new JButton("New Puzzle");

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startAnimation();
			}
		});

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetAnimation();
			}
		});

		newPuzzleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // Close current window
				new PathVisualizer(isDP); // Open new window
			}
		});

		controlPanel.add(startButton);
		controlPanel.add(resetButton);
		controlPanel.add(newPuzzleButton);
		add(controlPanel, BorderLayout.SOUTH);

		// Set up animation timer
		animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentSegmentIndex < path.size() - 1) {
					currentSegmentIndex++;
					pathPanel.repaint();
				} else {
					// Stop timer when animation is complete
					animationTimer.stop();
				}
			}
		});

		setLocationRelativeTo(null); // Center on screen
		setVisible(true);
	}

	private void generateSegmentColors() {
		segmentColors = new ArrayList<>();
		Random random = new Random();

		// Generate a distinct color for each path segment
		for (int i = 0; i < path.size() - 1; i++) {
			// Generate vibrant colors with good saturation and brightness
			float hue = random.nextFloat();
			float saturation = 0.7f + random.nextFloat() * 0.3f; // 0.7 to 1.0
			float brightness = 0.7f + random.nextFloat() * 0.3f; // 0.7 to 1.0

			segmentColors.add(Color.getHSBColor(hue, saturation, brightness));
		}
	}

	private void startAnimation() {
		if (!animationTimer.isRunning()) {
			if (currentSegmentIndex >= path.size() - 1) {
				resetAnimation();
			}
			animationTimer.start();
		}
	}

	private void resetAnimation() {
		animationTimer.stop();
		currentSegmentIndex = 0;
		pathPanel.repaint();
	}

	private class PathPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			// Enable anti-aliasing for smoother lines
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Draw the grid
			g2d.setColor(Color.LIGHT_GRAY);
			for (int i = 0; i <= n; i++) {
				// Vertical lines
				g2d.drawLine(
						PADDING + i * CELL_SIZE,
						PADDING,
						PADDING + i * CELL_SIZE,
						PADDING + n * CELL_SIZE
				);

				// Horizontal lines
				g2d.drawLine(
						PADDING,
						PADDING + i * CELL_SIZE,
						PADDING + n * CELL_SIZE,
						PADDING + i * CELL_SIZE
				);
			}

			// Draw coordinate labels
			g2d.setColor(Color.BLACK);
			for (int i = 0; i <= n; i++) {
				// X-axis labels
				g2d.drawString(
						Integer.toString(i),
						PADDING + i * CELL_SIZE,
						PADDING - 10
				);

				// Y-axis labels
				g2d.drawString(
						Integer.toString(i),
						PADDING - 15,
						PADDING + i * CELL_SIZE + 5
				);
			}

			// Draw the path if it exists and has points
			if (path != null && !path.isEmpty()) {
				g2d.setStroke(new BasicStroke(3.0f));

				// Draw each segment up to the current index
				for (int i = 0; i < currentSegmentIndex; i++) {
					Point p1 = path.get(i);
					Point p2 = path.get(i + 1);

					int x1 = PADDING + p1.x * CELL_SIZE;
					int y1 = PADDING + p1.y * CELL_SIZE;
					int x2 = PADDING + p2.x * CELL_SIZE;
					int y2 = PADDING + p2.y * CELL_SIZE;

					// Use the color for this segment
					g2d.setColor(segmentColors.get(i));
					g2d.drawLine(x1, y1, x2, y2);
				}

				// Draw the points (circles)
				int pointRadius = 8;
				int visiblePoints = Math.min(currentSegmentIndex + 1, path.size());

				for (int i = 0; i < visiblePoints; i++) {
					Point p = path.get(i);
					int x = PADDING + p.x * CELL_SIZE;
					int y = PADDING + p.y * CELL_SIZE;

					// Draw a filled circle at each point
					if (i == 0) {
						g2d.setColor(Color.GREEN);  // Start point
					} else if (i == path.size() - 1 && i < visiblePoints) {
						g2d.setColor(Color.RED);    // End point
					} else {
						g2d.setColor(Color.BLUE);   // Middle points
					}

					g2d.fillOval(
							x - pointRadius,
							y - pointRadius,
							pointRadius * 2,
							pointRadius * 2
					);

					// Draw point number
					g2d.setColor(Color.WHITE);
					FontMetrics metrics = g2d.getFontMetrics();
					String text = Integer.toString(i + 1);
					int textWidth = metrics.stringWidth(text);
					g2d.drawString(
							text,
							x - textWidth / 2,
							y + metrics.getHeight() / 4
					);
				}
			}
		}
	}

	private void DPVisualization() {
		JFrame inputFrame = new JFrame("Dots Puzzle Solver");
		inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inputFrame.setSize(300, 150);
		inputFrame.setLayout(new FlowLayout());

		JLabel label = new JLabel("Enter grid size (n ≥ 3):");
		JTextField inputField = new JTextField(10);
		JButton solveButton = new JButton("Solve");

		solveButton.addActionListener(e -> {
			try {
				int n = Integer.parseInt(inputField.getText().trim());
				if (n < 3) {
					JOptionPane.showMessageDialog(inputFrame, "Please enter a value of at least 3",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
					return;
				}

				path = solveWithDP(n);
				PathVisualizer.visualizePath(path, n);
				inputFrame.dispose();

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(inputFrame, "Please enter a valid number",
						"Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
		});

		inputFrame.add(label);
		inputFrame.add(inputField);
		inputFrame.add(solveButton);

		inputFrame.setLocationRelativeTo(null);
		inputFrame.setVisible(true);
	}

	private void approachTwoVisualization() {
		JFrame inputFrame = new JFrame("Dots Puzzle Solver");
		inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inputFrame.setSize(300, 150);
		inputFrame.setLayout(new FlowLayout());

		JLabel label = new JLabel("Enter grid size (n ≥ 3):");
		JTextField inputField = new JTextField(10);
		JButton solveButton = new JButton("Solve");

		solveButton.addActionListener(e -> {
			try {
				int n = Integer.parseInt(inputField.getText().trim());
				if (n < 3) {
					JOptionPane.showMessageDialog(inputFrame, "Please enter a value of at least 3",
							"Invalid Input", JOptionPane.ERROR_MESSAGE);
					return;
				}

				path = runAlgo(n);
				PathVisualizer.visualizePath(path, n);
				inputFrame.dispose();

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(inputFrame, "Please enter a valid number",
						"Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
		});

		inputFrame.add(label);
		inputFrame.add(inputField);
		inputFrame.add(solveButton);

		inputFrame.setLocationRelativeTo(null);
		inputFrame.setVisible(true);
	}

	public static void visualizePath(LinkedList<Point> path, int n) {
		SwingUtilities.invokeLater(() -> new PathVisualizer(path, n));
	}
}
