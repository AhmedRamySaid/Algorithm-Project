package asu.caie.algorithmproject.tasks.tasktwo.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KnightTourAlgorithm {
    //chess board
    private static int n;
    private static int[][] board;
    private static int x_start;
    private static int y_start;
    //direction of Task_2.knight moves
    //north1, north2, west1, west2, south1, south2, east1, east2
    private static final int[] x_direction = {-1, 1, -2, -2, -1, 1, 2, 2};
    private static final int[] y_direction = {-2, -2, -1, 1, 2, 2, -1, 1};

    //public function to access n (size)
    public static int getN() {
        return n;
    }

    //public function to set n
    public static boolean setN(int n) {
        if (n > 2) {
            KnightTourAlgorithm.n = n;
            board = new int[n][n];
            return true;
        } else
            return false;
    }

    //public function to access x_start and y_start
    public static int getX_start() {
        return x_start;
    }

    public static int getY_start() {
        return y_start;
    }

    //to ensure that the next movement is in the board boundaries and not visited before
    private static boolean inBoard(int x, int y) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    //to ensure that the next movement is in the board boundaries and not visited before
    private static boolean isAvailable(int x, int y) {
        return inBoard(x, y) && board[x][y] == -1;
    }

    //public function to set x_start and y_start
    public static boolean setStart(int x_start, int y_start) {
        if (inBoard(x_start, y_start)) {
            KnightTourAlgorithm.x_start = x_start;
            KnightTourAlgorithm.y_start = y_start;
            return true;
        } else
            return false;
    }

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
        boolean isClosed = false;
        for (int i = 0; i < 8; i++) {
            if (x + x_direction[i] == x_start && y + y_direction[i] == y_start) {
                isClosed = true;
                break;
            }
        }

        return isClosed;
    }

    //fill the chess board with these 0--> Task_2.start, -1--> not visited
    private static void fill_board() {
        board = new int[8][8];
        n = 8;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (i == x_start && j == y_start)
                    board[i][j] = 0; //starting cell
                else
                    board[i][j] = -1;
            }
        }
    }

    //print the chess board with the Task_2.start cell and movements
    private static void print_board() {
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
    private static boolean greedy() {
        int x = x_start;
        int y = y_start;

        for (int move = 1; move < n * n; move++) {
            int[] next = nextMove(x, y, move);
            if (next == null) {
                System.out.println("Knight failed to visit all the cells at the move: " + move);
                return false;
            }
            x = next[0];
            y = next[1];
            board[x][y] = move;
        }

        System.out.print("Knight succeed to visit all the cells and ");
        //boolean result = false;
        if (isClosed(x, y)) {
            System.out.println("it is a closed trip");
            return true;
        } else {
            System.out.println("it is open trip");
            return false;
        }

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
        //this rearranges the possible moves  using the a[2]-> countNext
        moves.sort(Comparator.comparingInt(a -> a[2]));
        List<int[]> result = new ArrayList<>();
        //it saves the x, y of every move
        for (int[] move : moves)
            result.add(new int[]{move[0], move[1]});

        return result;
    }

    private static List<int[]> greedy_backtracking(int x, int y, int move, List<int[]> ansList) {
        board[x][y] = move;

        if (move == n * n - 1) {
            if (isClosed(x, y)) return ansList;
        }

        List<int[]> nextMoves = getSortedMoves(x, y);
        for (int[] next : nextMoves) {
            ansList.add(new int[] {x, y, next[0], next[1]});
            List<int[]> ans = greedy_backtracking(next[0], next[1], move + 1, ansList);
            if (!ans.isEmpty()) return ans;
        }

        board[x][y] = -1; // Backtrack
        return Collections.emptyList();
    }


    public static boolean start_solve_greedy() {
        System.out.println("-> This solution is with pure greedy algorithm");
        fill_board();
        boolean result = greedy();
        print_board();
        System.out.println("\n");
        return result;
    }

    public static List<int[]> start_solve_greedy_optimized(int x, int y) {
        System.out.println("-> This solution is with greedy + backtracking algorithm");
        fill_board();

        List<int[]> result = greedy_backtracking(x, y, 1, new ArrayList<>());
        if (!result.isEmpty())
            System.out.println("Knight succeed to visit all the cells and it is a closed trip");
        else
            System.out.println("Knight failed to visit all the cells or it is open trip");
        return result;
    }



    private static int[] nextMove01(int x, int y) {
        int min_degree = 99; //used for greedy technique
        int[] next = null;
        int degree; //number of next possible moves
        int[] move;

        for (int i = 0; i < 8; i++) {
            int x_next = x + x_direction[i];
            int y_next = y + y_direction[i];

            if (isAvailable(x_next, y_next)) {
                move = new int[]{x_next, y_next};
                degree = countNext(move[0], move[1]);
                if (degree < min_degree) {
                    min_degree = degree;
                    next = new int[]{move[0], move[1]};
                }
            }
        }
        return next;
    }

    private static boolean greedy01() {
        int x = x_start;
        int y = y_start;

        //try all cells
        for (int move = 1; move < n * n; move++) {
            int[] next = nextMove01(x, y);
            if (next == null) {
                System.out.println("Task_2.knight failed to visit all cells");
                return false;
            }
            x = next[0];
            y = next[1];
            board[x][y] = move; //update it as visited
        }

        //succeed to visited all cells
        System.out.print("Task_2.knight visited all the cells ");
        //check if it is closed or not
        if(isClosed(x_start, y_start)){
            System.out.println("and it is closed tour.");
            return true;
        }
        else{
            System.out.println("and it is opened tour.");
            return false;
        }
    }

    public static void start_solve_greedy01() {
        System.out.println("-> This solution is with pure greedy01 algorithm");
        fill_board();
        greedy01();
        print_board();
        System.out.println("\n");
    }

}
