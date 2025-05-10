package asu.caie.algorithmproject.tasks.tasktwo.algorithm;

import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KnightTourAlgorithm {
    //chess board
    private static int n; //size
    private static int[][] board; //board
    private static int x_start;
    private static int y_start;

    //save the failed paths
    private static List<int[]> failedPositions;

    //direction of knight moves
    //north1, north2, west1, west2, south1, south2, east1, east2
    private static final int[] x_direction = {-1, 1, -2, -2, -1, 1, 2, 2};
    private static final int[] y_direction = {-2, -2, -1, 1, 2, 2, -1, 1};


    //public function to set n
    public static void setN(int n) {
        if (n > 3) {
            KnightTourAlgorithm.n = n;
            board = new int[n][n];
            failedPositions = new ArrayList<>();
        } else {
            KnightTourAlgorithm.n = 8;
            board = new int[8][8];
            failedPositions = new ArrayList<>();
            System.out.println("Wrong size, size now = 8");
        }
    }

    //to ensure that the next movement is in the board boundaries and not visited before
    private static boolean inBoard(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    //public function to set x_start and y_start
    public static void setStart(int x_start, int y_start) {
        if (inBoard(x_start, y_start)) {
            KnightTourAlgorithm.x_start = x_start;
            KnightTourAlgorithm.y_start = y_start;
        } else {
            KnightTourAlgorithm.x_start = 0;
            KnightTourAlgorithm.y_start = 1;
            System.out.println("Wrong Start, start now = (0,1)");
        }
    }


    //to ensure that the next movement is in the board boundaries and not visited before
    private static boolean isAvailable(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n && board[x][y] == -1;
    }

    //fill the chess board with these 0--> Task_2.start, -1--> not visited
    private static void fill_board() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
//                if(i == x_start && j == y_start)
//                    board[i][j] = 0;
//                else
                board[i][j] = -1;
            }
        }
    }


    /*Classic greedy*/
    //to count the possible next movements
    private static int countNext(int x, int y) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int x_next = x + x_direction[i];
            int y_next = y + y_direction[i];
            if (isAvailable(x_next, y_next))
                count++;
        }
        return count;
    }

    //to check if the ending cell near to Task_2.start cell or not
    private static boolean isClosed(int x, int y) {
        for (int i = 0; i < 8; i++) {
            if ((x + x_direction[i]) == x_start && (y + y_direction[i]) == y_start) {
                return true;
            }
        }
        return false;
    }


    //use greedy technique --> choose cell has the minimum possible movements
    //Warnsdorff 's rule
    private static int[] nextMove(int x, int y, int moveCount) {
        int minDegree = 99; //used for greedy technique
        int[] next = null;
        int degree = 0; //number of next possible moves
        List<int[]> candidates = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int x_next = x + x_direction[i];
            int y_next = y + y_direction[i];

            if (isAvailable(x_next, y_next)) {
                degree = countNext(x_next, y_next);
                if (degree < minDegree) {
                    minDegree = degree;
                    candidates.clear();
                    candidates.add(new int[]{x_next, y_next});
                } else if (degree == minDegree) {
                    candidates.add(new int[]{x_next, y_next});
                }
            }
        }

        if (candidates.isEmpty()) return null;

        // إذا فاضل خطوة واحدة فقط (آخر خطوة)، لازم نرجع لنقطة البداية
        if (moveCount == n * n - 1) {
            for (int[] c : candidates) {
                if (Math.abs(c[0] - x_start) + Math.abs(c[1] - y_start) == 1 ||
                        (Math.abs(c[0] - x_start) == 2 && Math.abs(c[1] - y_start) == 1) ||
                        (Math.abs(c[0] - x_start) == 1 && Math.abs(c[1] - y_start) == 2)) {
                    return c;
                }
            }
            return candidates.get(0); // مفيش حد بيوصل للبداية؟ نختار أي حاجة
        }
        // نحاول نتجنب الخطوة اللي بتقرب من نقطة البداية
        for (int[] c : candidates) {
            if (!isClosed(c[0], c[1])) {
                return c;
            }
        }

        // مفيش غير اللي بيقرب من البداية؟ يبقى نختار منهم وخلاص
        return candidates.get(0);
    }

    //apply the algorithm on the chess board
    private static List<int[]> greedy() {
        List<int[]> result = new ArrayList<>();
        int x = x_start;
        int y = y_start;

        for (int move = 1; move < n * n; move++) {
            int[] next = nextMove(x, y, move);
            if (next == null) {
                System.out.println("Knight failed to visit all the cells at the move: " + move);
                return Collections.emptyList();
            }

            result.add(new int[]{x, y, next[0], next[1]});
            x = next[0];
            y = next[1];
            board[x][y] = move;
        }

        System.out.print("Knight succeed to visit all the cells and ");
        if (isClosed(x, y)) {
            System.out.println("it is closed trip");
            return result;
        } else {
            System.out.println("it is opened trip");
            return Collections.emptyList();
        }

    }

    //print the chess board with the Task_2.start cell and movements
    private static void print_board() {
        //System.out.println(Arrays.deepToString(board));
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == 0) {
                    System.out.print("S  ");
                    continue;
                }
                if (cell == -1) {
                    System.out.print("XX ");
                    continue;
                }
                if (cell == (n * n) - 1) {
                    System.out.print("F  ");
                    continue;
                }
                System.out.printf("%2d ", cell);
            }
            System.out.println();
        }
        System.out.println("Note: S--> Task_2.start, F--> finish");
    }

    /* This is the greedy algorithm with some improvements to use backtracking */
    //arrange the possible movements using countNext(greedy algorithm)
    private static List<int[]> getSortedMoves(int x, int y) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int nx = x + x_direction[i];
            int ny = y + y_direction[i];
            if (isAvailable(nx, ny)) {
                moves.add(new int[]{nx, ny, countNext(nx, ny)});
            }
        }

        //rearrange with Warndsroff
        // Sort by degree (ascending order - fewest exits first)
        for (int i = 0; i < moves.size(); i++) {
            for (int j = i + 1; j < moves.size(); j++) {
                if (moves.get(i)[2] > moves.get(j)[2]) {
                    // Swap
                    int[] temp = moves.get(i);
                    moves.set(i, moves.get(j));
                    moves.set(j, temp);
                }
            }
        }

        List<int[]> result = new ArrayList<>();
        //it saves the x, y of every move
        for (int[] move : moves)
            result.add(new int[]{move[0], move[1]});

        return result;
    }

    // Check if a position is in the failed positions list
    private static boolean isFailedPosition(int x, int y, int move) {
        for (int[] failedPos : failedPositions) {
            if (failedPos[0] == x && failedPos[1] == y && failedPos[2] == move) {
                return true;
            }
        }
        return false;
    }

    private static boolean greedy_backtracking(int x, int y, int move) {
        board[x][y] = move;

//        // Check if position is already known to fail
//        if (isFailedPosition(x, y, move)) {
//            board[x][y] = -1; // Reset this cell
//            return false;
//        }

        if (move == n * n - 1) {
            if (isClosed(x, y)) {
                return true;
            }
            // If no knight move can return to start, it's not a closed tour
            board[x][y] = -1; // Backtrack
            failedPositions.add(new int[]{x, y, move});
            return false;
        }

        List<int[]> nextMoves = getSortedMoves(x, y);
        for (int[] next : nextMoves) {
            if (greedy_backtracking(next[0], next[1], move + 1)) {
                return true;
            }
        }

        //failed -> backtrack
//        failedPositions.remove(new int[]{x, y, move});
        board[x][y] = -1; // Backtrack
        return false;
    }

    private static List<int[]> reconstructPathFromBoard() {
        List<int[]> path = new ArrayList<>();
        int[][] position = new int[n * n][2];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (board[i][j] != -1)
                    position[board[i][j]] = new int[]{i, j};

        for (int i = 0; i < n * n - 1; i++) {
            int[] from = position[i];
            int[] to = position[i + 1];
            path.add(new int[]{from[0], from[1], to[0], to[1]});
        }

        return path;
    }


    public static List<int[]> start_solve_greedy(int n, int x_start, int y_start) {
        System.out.println("-> This solution is with pure greedy algorithm");
        setN(n);
        setStart(x_start, y_start);
        fill_board();

        List<int[]> result = greedy();

        print_board();
        System.out.println();

        return result;
    }

    public static List<int[]> start_solve_greedy_optimized(int n, int x_start, int y_start) {
        System.out.println("-> This solution is with greedy + backtracking algorithm");
        setN(n);
        setStart(x_start, y_start);
        fill_board();

        boolean found = greedy_backtracking(x_start, y_start, 0);

        print_board();
        System.out.println();

        if (found) {
            System.out.println("Knight succeeded to visit all the cells and it is closed trip");
        } else {
            System.out.println("Knight failed to visit all the cells or it is opened trip");
        }
        //return the list with current x, y and next x, y
        return reconstructPathFromBoard();
    }
}

