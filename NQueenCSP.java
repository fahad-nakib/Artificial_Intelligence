import java.util.*;

public class NQueenCSP {
    private int N;
    private List<int[]> solutions = new ArrayList<>();

    public NQueenCSP(int N) {
        this.N = N;
    }

    public void solve() {
        int[] board = new int[N];  // board[row] = col
        dfs(0, board);
    }

    private void dfs(int row, int[] board) {
        if (row == N) {
            solutions.add(board.clone());
            return;
        }

        for (int col = 0; col < N; col++) {
            if (isSafe(row, col, board)) {
                board[row] = col;
                dfs(row + 1, board);
            }
        }
    }

    private boolean isSafe(int row, int col, int[] board) {
        for (int prevRow = 0; prevRow < row; prevRow++) {
            int prevCol = board[prevRow];
            if (prevCol == col || Math.abs(prevRow - row) == Math.abs(prevCol - col)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidSolution(int[] candidate) {
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (candidate[i] == candidate[j]) return false;
                if (Math.abs(i - j) == Math.abs(candidate[i] - candidate[j])) return false;
            }
        }
        return true;
    }

    private int[] transpose(int[] solution) {
        int[] transposed = new int[N];
        Arrays.fill(transposed, -1);
        for (int i = 0; i < N; i++) {
            int col = solution[i];
            if (col < N) {
                transposed[col] = i;
            }
        }
        return transposed;
    }

    public void analyzeSolutions() {
        System.out.println("Total Solutions Found: " + solutions.size());

        int transposableCount = 0;
        int matchesAnotherSolution = 0;

        for (int i = 0; i < solutions.size(); i++) {
            int[] sol = solutions.get(i);
            int[] trans = transpose(sol);

            if (isValidSolution(trans)) {
                transposableCount++;
                System.out.println("Solution " + (i + 1) + " has a valid transpose:");
                printSolution(trans);

                // Check if trans matches any other solution (excluding itself)
                for (int j = 0; j < solutions.size(); j++) {
                    if (i != j && Arrays.equals(trans, solutions.get(j))) {
                        matchesAnotherSolution++;
                        System.out.println("  --> This transpose matches Solution " + (j + 1));
                        break; // No need to check further if a match is found
                    }
                }
            }
        }

        System.out.println("Number of solutions with valid transposes: " + transposableCount);
        System.out.println("Number of transposes that match another solution: " + matchesAnotherSolution);
    }

    public void printAllSolutions() {
        int count = 1;
        for (int[] sol : solutions) {
            System.out.println("Solution " + count++ + ":");
            printSolution(sol);
        }
    }

    private void printSolution(int[] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i] == j) System.out.print("Q ");
                else System.out.print(". ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter value of N for N-Queens: ");
        int N = sc.nextInt();

        NQueenCSP nQueen = new NQueenCSP(N);
        nQueen.solve();
        nQueen.printAllSolutions();
        nQueen.analyzeSolutions();
    }
}
