package asu.caie.algorithmproject.tasks.taskfour.algorithms;

import java.util.*;

/**
 * Knight Swap Problem Solver using Iterative Improvement (Beam Search)
 *
 * Problem: Swap 3 black knights and 3 white knights on a 3x4 chessboard
 * using minimum number of standard chess knight moves.
 */
public class KnightSwapAlgorithms {
	// Constants for board dimensions
	private static final int COLS = 3;
	private static final int ROWS = 4;

	// Constants for knight colors
	private static final int EMPTY = 0;
	private static final int BLACK = 1;
	private static final int WHITE = 2;

	// Target positions for black and white knights
	private static final int[][] BLACK_TARGET = {{0, 3}, {1, 3}, {2, 3}};
	private static final int[][] WHITE_TARGET = {{0, 0}, {1, 0}, {2, 0}};

	// Knight move patterns (all possible L-shaped moves)
	private static final int[][] KNIGHT_MOVES = {
			{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
			{1, -2}, {1, 2}, {2, -1}, {2, 1}
	};

	// State represents a board configuration and move history
	static class State {
		int[][] board;  // 0=empty, 1=black knight, 2=white knight
		List<int[]> moves;  // List of moves to reach this state
		int heuristicValue;  // Lower is better

		State(int[][] board, List<int[]> moves) {
			this.board = board;
			this.moves = moves;
			this.heuristicValue = calculateHeuristic();
		}

		// Deep copy constructor
		State(State other) {
			this.board = new int[ROWS][COLS];
			for (int i = 0; i < ROWS; i++) {
				System.arraycopy(other.board[i], 0, this.board[i], 0, COLS);
			}
			this.moves = new ArrayList<>(other.moves);
			this.heuristicValue = other.heuristicValue;
		}

		// Calculate heuristic value - how far knights are from their targets
		private int calculateHeuristic() {
			int distance = 0;

			// Count knights in wrong positions
			int blackInWrongPosition = 0;
			int whiteInWrongPosition = 0;

			// Find positions of all knights
			List<int[]> blackPositions = new ArrayList<>();
			List<int[]> whitePositions = new ArrayList<>();

			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLS; c++) {
					if (board[r][c] == BLACK) {
						blackPositions.add(new int[]{c, r});
					} else if (board[r][c] == WHITE) {
						whitePositions.add(new int[]{c, r});
					}
				}
			}

			// Calculate distance of each black knight to its closest target
			for (int[] pos : blackPositions) {
				int minDist = Integer.MAX_VALUE;
				boolean inTarget = false;

				// Check if this black knight is in any target position
				for (int[] target : BLACK_TARGET) {
					if (pos[0] == target[0] && pos[1] == target[1]) {
						inTarget = true;
						break;
					}

					// Compute minimum knight-move distance to target
					int knightDist = knightMovesDistance(pos[0], pos[1], target[0], target[1]);
					minDist = Math.min(minDist, knightDist);
				}

				if (!inTarget) {
					blackInWrongPosition++;
					distance += minDist;
				}
			}

			// Calculate distance of each white knight to its closest target
			for (int[] pos : whitePositions) {
				int minDist = Integer.MAX_VALUE;
				boolean inTarget = false;

				// Check if this white knight is in any target position
				for (int[] target : WHITE_TARGET) {
					if (pos[0] == target[0] && pos[1] == target[1]) {
						inTarget = true;
						break;
					}

					// Compute minimum knight-move distance to target
					int knightDist = knightMovesDistance(pos[0], pos[1], target[0], target[1]);
					minDist = Math.min(minDist, knightDist);
				}

				if (!inTarget) {
					whiteInWrongPosition++;
					distance += minDist;
				}
			}

			// Add number of moves as a factor (weighted less than distance)
			return distance * 2 + blackInWrongPosition + whiteInWrongPosition + moves.size();
		}

		// Check if this state is the goal state
		boolean isGoalState() {
			// Check if all black knights are in target positions
			for (int[] pos : BLACK_TARGET) {
				if (board[pos[1]][pos[0]] != BLACK) {
					return false;
				}
			}

			// Check if all white knights are in target positions
			for (int[] pos : WHITE_TARGET) {
				if (board[pos[1]][pos[0]] != WHITE) {
					return false;
				}
			}

			return true;
		}

		// Create a unique hash for the board state
		@Override
		public int hashCode() {
			return Arrays.deepHashCode(board);
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof State other)) return false;
			return Arrays.deepEquals(this.board, other.board);
		}
	}

	// Estimate minimum knight moves between two positions
	private static int knightMovesDistance(int x1, int y1, int x2, int y2) {
		// This is a rough estimate based on knight move patterns
		int dx = Math.abs(x1 - x2);
		int dy = Math.abs(y1 - y2);

		// Special cases
		if (dx == 0 && dy == 0) return 0;  // Same position
		if ((dx == 1 && dy == 2) || (dx == 2 && dy == 1)) return 1;  // One knight move away

		// General case: rough approximation
		return (dx + dy + 1) / 3;  // Knight can cover ~3 squares per move
	}

	// Generate all possible next states from current state
	static List<State> generateNextStates(State current) {
		List<State> nextStates = new ArrayList<>();

		// Try moving every knight on the board
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				int piece = current.board[r][c];
				if (piece != EMPTY) {  // Found a knight
					// Try each possible knight move
					for (int[] move : KNIGHT_MOVES) {
						int newR = r + move[0];
						int newC = c + move[1];

						// Check if new position is valid and empty
						if (isValidPosition(newR, newC) && current.board[newR][newC] == EMPTY) {
							// Create new state with this move
							State newState = new State(current);

							// Move the knight
							newState.board[newR][newC] = piece;
							newState.board[r][c] = EMPTY;

							// Record the move: [from_col, from_row, to_col, to_row]
							int[] moveRecord = {c, r, newC, newR};
							newState.moves.add(moveRecord);

							// Recalculate heuristic
							newState.heuristicValue = newState.calculateHeuristic();

							nextStates.add(newState);
						}
					}
				}
			}
		}

		return nextStates;
	}

	// Check if a position is valid on the board
	static boolean isValidPosition(int r, int c) {
		return r >= 0 && r < ROWS && c >= 0 && c < COLS;
	}

	// Create the initial state of the board
	static State createInitialState() {
		int[][] board = new int[ROWS][COLS];

		// Place black knights at the top row
		for (int c = 0; c < COLS; c++) {
			board[0][c] = BLACK;
		}

		// Place white knights at the bottom row
		for (int c = 0; c < COLS; c++) {
			board[ROWS-1][c] = WHITE;
		}

		return new State(board, new ArrayList<>());
	}

	// Print the current board state
	static void printBoard(int[][] board) {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				char symbol = switch (board[r][c]) {
					case BLACK -> 'B';
					case WHITE -> 'W';
					default -> '.';
				};
				System.out.print(symbol + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	// Print a move in readable format
	static void printMove(int[] move) {
		System.out.printf("Move from (%d,%d) to (%d,%d)%n",
				move[0], move[1], move[2], move[3]);
	}

	// Solve using beam search (an iterative improvement algorithm)
	public static List<int[]> solve() {
		// Initialize with the start state
		State initialState = createInitialState();

		// Use a priority queue to keep track of the best states
		PriorityQueue<State> beam = new PriorityQueue<>(
				Comparator.comparingInt(s -> s.heuristicValue)
		);
		beam.add(initialState);

		// Keep track of visited states to avoid cycles
		Set<State> visited = new HashSet<>();
		visited.add(initialState);

		// Beam search parameters
		final int MAX_ITERATIONS = 50000;
		final int BEAM_WIDTH = 5000;

		int iterations = 0;
		while (!beam.isEmpty() && iterations < MAX_ITERATIONS) {
			// Get the best state from the beam
			State current = beam.poll();

			// Check if we've reached the goal
			if (current.isGoalState()) {
				System.out.println("\nSolution found in "+ iterations + " iterations");
				return current.moves;
			}

			// Progress indicator
			if (iterations % 1000 == 0) {
				System.out.println("Iteration " + iterations +
						", Best heuristic: " + current.heuristicValue +
						", Queue size: " + beam.size() +
						", Moves so far: " + current.moves.size());
			}

			// Generate all possible next states
			List<State> nextStates = generateNextStates(current);

			// Add each new state to the beam if not visited
			for (State next : nextStates) {
				if (!visited.contains(next)) {
					beam.add(next);
					visited.add(next);
				}
			}

			// Keep the beam size under control
			if (beam.size() > BEAM_WIDTH) {
				// Create a new priority queue with the best states
				PriorityQueue<State> newBeam = new PriorityQueue<>(
						Comparator.comparingInt(s -> s.heuristicValue)
				);

				// Keep only the BEAM_WIDTH best states
				for (int i = 0; i < BEAM_WIDTH && !beam.isEmpty(); i++) {
					newBeam.add(beam.poll());
				}

				beam = newBeam;
			}

			iterations++;
		}

		System.out.println("No solution found after " + iterations + " iterations.");
		System.out.println("Current best state's heuristic: " +
				(beam.isEmpty() ? "N/A" : beam.peek().heuristicValue));
		System.out.println("Current best state's moves: " +
				(beam.isEmpty() ? "N/A" : beam.peek().moves.size()));

		// Print the best board we have, if any
		if (!beam.isEmpty()) {
			System.out.println("Best board found: ");
			printBoard(beam.peek().board);
		}
		return Collections.emptyList();
	}

	// Solve using beam search (an iterative improvement algorithm)
	public static List<int[]> solveAStar() {
		// Initialize with the start state
		State initialState = createInitialState();

		// Use a priority queue to keep track of the best states
		PriorityQueue<State> beam = new PriorityQueue<>(
				Comparator.comparingInt(s -> s.heuristicValue + s.moves.size())
		);
		beam.add(initialState);

		// Keep track of visited states to avoid cycles
		Set<State> visited = new HashSet<>();
		visited.add(initialState);

		while (!beam.isEmpty()) {
			// Get the best state from the beam
			State current = beam.poll();

			// Check if we've reached the goal
			if (current.isGoalState()) {
				return current.moves;
			}

			// Generate all possible next states
			List<State> nextStates = generateNextStates(current);

			// Add each new state to the beam if not visited
			for (State next : nextStates) {
				if (!visited.contains(next)) {
					beam.add(next);
					visited.add(next);
				}
			}
		}

		System.out.println("No solution found after");
		System.out.println("Current best state's heuristic: " +
				(beam.isEmpty() ? "N/A" : beam.peek().heuristicValue));
		System.out.println("Current best state's moves: " +
				(beam.isEmpty() ? "N/A" : beam.peek().moves.size()));

		// Print the best board we have, if any
		if (!beam.isEmpty()) {
			System.out.println("Best board found: ");
			printBoard(beam.peek().board);
		}
		return Collections.emptyList();
	}
}