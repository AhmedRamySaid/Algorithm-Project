package asu.caie.algorithmproject.tasks.tasksix.algorithms;

import asu.caie.algorithmproject.tasks.tasksix.views.PathVisualizer;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DotsPuzzleSolver {
	private static LinkedList<asu.caie.algorithmproject.tasks.tasksix.models.Point> path = new LinkedList<asu.caie.algorithmproject.tasks.tasksix.models.Point>();
	private static Map<Integer, List<asu.caie.algorithmproject.tasks.tasksix.models.Point>> dp = new HashMap<>();

	// DP SOLUTION
	public static LinkedList<asu.caie.algorithmproject.tasks.tasksix.models.Point> solveWithDP(int n) {
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
		List<asu.caie.algorithmproject.tasks.tasksix.models.Point> base3 = new ArrayList<>();
		base3.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(0, 3));
		base3.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(3, 3));
		base3.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(0, 0));
		base3.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(0, 3));
		base3.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(2, 1));
		dp.put(3, base3);
	}

	private static void buildSolutionForSize(int n) {
		List<asu.caie.algorithmproject.tasks.tasksix.models.Point> path = new ArrayList<>();
		asu.caie.algorithmproject.tasks.tasksix.models.Point startPoint = getStartPoint(n);
		path.add(startPoint);

		List<asu.caie.algorithmproject.tasks.tasksix.models.Point> prevPath = dp.get(n - 1);
		asu.caie.algorithmproject.tasks.tasksix.models.Point lastPoint = prevPath.get(prevPath.size() - 1);

		if (isEven(n)) {
			path.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(startPoint.x - n + 1, startPoint.y));
			path.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(startPoint.x - n + 1, startPoint.y + n - 1));

			int offsetX = startPoint.x - n + 2 - prevPath.get(0).x;
			int offsetY = startPoint.y + n - 1 - prevPath.get(0).y;

			for (int i = 1; i < prevPath.size(); i++) {
				asu.caie.algorithmproject.tasks.tasksix.models.Point p = prevPath.get(i);
				path.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(p.x + offsetX, p.y + offsetY));
			}
		} else {
			path.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(startPoint.x + n - 1, startPoint.y));
			path.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(startPoint.x + n - 1, startPoint.y - n + 1));

			int offsetX = startPoint.x + n - 2 - prevPath.get(0).x;
			int offsetY = startPoint.y - n + 1 - prevPath.get(0).y;

			for (int i = 1; i < prevPath.size(); i++) {
				asu.caie.algorithmproject.tasks.tasksix.models.Point p = prevPath.get(i);
				path.add(new asu.caie.algorithmproject.tasks.tasksix.models.Point(p.x + offsetX, p.y + offsetY));
			}
		}

		dp.put(n, path);
	}

	private static asu.caie.algorithmproject.tasks.tasksix.models.Point getStartPoint(int n) {
		if (n == 3) {
			return new asu.caie.algorithmproject.tasks.tasksix.models.Point(0, n);
		}

		if (isEven(n)) { // Bottom Right
			return new asu.caie.algorithmproject.tasks.tasksix.models.Point(n - 1, 0);
		} else { // Top Left
			return new asu.caie.algorithmproject.tasks.tasksix.models.Point(0, n - 1);
		}
	}

	private static boolean isEven(int n) {
		return n % 2 == 0;
	}
}
