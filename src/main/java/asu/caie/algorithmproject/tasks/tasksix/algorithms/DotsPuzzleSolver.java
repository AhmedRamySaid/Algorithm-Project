package asu.caie.algorithmproject.tasks.tasksix.algorithms;

import  asu.caie.algorithmproject.tasks.tasksix.models.Point;


import java.util.*;
import java.util.List;

public class DotsPuzzleSolver {
	private static LinkedList<Point> path = new LinkedList<Point>();
	private static Map<Integer, List<Point>> dp = new HashMap<>();

	// DP SOLUTION
	public static LinkedList<Point> solveWithDP(int n) {
		initializeDPTable();

		// DP
		for (int i = 3; i <= n; i++) {
			if (!dp.containsKey(i)) {
				buildSolutionForSize(i);
			}
		}

		// the solution for size n
		return new LinkedList<>(dp.get(n));
	}

	private static void initializeDPTable() {
		// n = 3
		List<Point> base3 = new ArrayList<>();
		base3.add(new Point(0, 3));
		base3.add(new Point(3, 3));
		base3.add(new Point(0, 0));
		base3.add(new Point(0, 3));
		base3.add(new Point(2, 1));
		dp.put(3, base3);
	}

	private static void buildSolutionForSize(int n) {
		List<Point> path = new ArrayList<>();
		Point startPoint = getStartPoint(n);
		path.add(startPoint);

		List<Point> prevPath = dp.get(n - 1);
		Point lastPoint = prevPath.get(prevPath.size() - 1);

		if (isEven(n)) {
			path.add(new Point(startPoint.x - n + 1, startPoint.y));
			path.add(new Point(startPoint.x - n + 1, startPoint.y + n - 1));

			int offsetX = startPoint.x - n + 2 - prevPath.get(0).x;
			int offsetY = startPoint.y + n - 1 - prevPath.get(0).y;

			for (int i = 1; i < prevPath.size(); i++) {
				Point p = prevPath.get(i);
				path.add(new Point(p.x + offsetX, p.y + offsetY));
			}
		} else {
			path.add(new Point(startPoint.x + n - 1, startPoint.y));
			path.add(new Point(startPoint.x + n - 1, startPoint.y - n + 1));

			int offsetX = startPoint.x + n - 2 - prevPath.get(0).x;
			int offsetY = startPoint.y - n + 1 - prevPath.get(0).y;

			for (int i = 1; i < prevPath.size(); i++) {
				Point p = prevPath.get(i);
				path.add(new Point(p.x + offsetX, p.y + offsetY));
			}
		}

		dp.put(n, path);
	}

	private static Point getStartPoint(int n) {
		if (n == 3) {
			return new Point(0, n);
		}

		if (isEven(n)) { // Bottom Right
			return new Point(n - 1, 0);
		} else { // Top Left
			return new Point(0, n - 1);
		}
	}

	private static boolean isEven(int n) {
		return n % 2 == 0;
	}
}
