package asu.caie.algorithmproject.tasks.tasksix.algorithms;

import asu.caie.algorithmproject.tasks.tasksix.models.Point;

import java.util.LinkedList;

public class ApproachTwo {
	public static LinkedList<Point> path = new LinkedList<Point>(); // To store the path

	public static LinkedList<Point> runAlgo(int n) {
		Point startPoint = getStartPoint(n);
		path.add(startPoint);
		solve(n, startPoint);
		return path;
	}

	public static void solve(int n, Point p) {
		if(n == 3) {
			solve3(n, p);
			return;
		};

		if(isEven(n)) { // GO Left then Up
			path.add(new Point(p.x - n + 1, p.y));
			path.add(new Point(p.x - n + 1, p.y + n - 1));
		} else { // GO Right then Down
			path.add(new Point(p.x + n - 1, p.y));
			path.add(new Point(p.x + n - 1, p.y - n + 1));
		}

		if(isEven(n)) {
			solve(n - 1, new Point(p.x - n + 2, p.y + n - 1));
		} else {
			solve(n - 1, new Point(p.x + n - 2, p.y - n + 1));
		}
	}

	public static void solve3(int n, Point p) {
		path.add(new Point(p.x + 3, p.y));
		path.add(new Point(p.x, p.y - 3));
		path.add(new Point(p.x, p.y));
		path.add(new Point(p.x + 2, p.y - 2));
	}

	public static Point getStartPoint(int n) {
		if(n == 3) {
			return new Point(0, n);
		}

		if(isEven(n)) { // Bottom Right
			return new Point(n - 1, 0);
		} else { // Top Left
			return new Point(0, n - 1);
		}
	}

	public static void printPath() {
		for (Point p : path) {
			System.out.println("(" + p.x + ", " + p.y + ")");
		}
	}

	public static boolean isEven(int n) {
		return n % 2 == 0;
	}
}
