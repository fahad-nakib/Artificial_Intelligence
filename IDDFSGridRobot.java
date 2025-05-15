import java.util.*;

public class IDDFSGridRobot {

    private static final int OBSTACLE = 1;
    private static final int START = 2;
    private static final int GOAL = 3;
    private static final int PATH = 4;
    private static final int VISITED = 5;

    private static class Point {
        int x, y;
        Point parent;

        Point(int x, int y, Point parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grid size (N): ");
        int n = scanner.nextInt();

        System.out.print("Enter number of obstacles: ");
        int numObstacles = scanner.nextInt();

        int[][] grid = new int[n][n];
        Point start = null, goal = null;

        // Initialize grid with obstacles
        Random random = new Random();
        int obstaclesPlaced = 0;
        while (obstaclesPlaced < numObstacles) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            if (grid[x][y] == 0) {
                grid[x][y] = OBSTACLE;
                obstaclesPlaced++;
            }
        }

        // Place start point
        while (start == null) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            if (grid[x][y] == 0) {
                start = new Point(x, y, null);
                grid[x][y] = START;
            }
        }

        // Place goal point
        while (goal == null) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            if (grid[x][y] == 0) {
                goal = new Point(x, y, null);
                grid[x][y] = GOAL;
            }
        }

        System.out.println("\nInitial Grid:");
        printGrid(grid);

        // Perform IDDFS
        Point result = iddfs(grid, start, goal);

        if (result != null) {
            System.out.println("Path found!");
            markPath(grid, result);
            System.out.println("Solution Grid:");
            printGrid(grid);
        } else {
            System.out.println("No path found!");
        }

        // Check for cycles
        if (hasCycle(grid, start)) {
            System.out.println("Cycle detected in the graph!");
        } else {
            System.out.println("No cycle detected in the graph.");
        }
    }

    private static Point iddfs(int[][] grid, Point start, Point goal) {
        int depth = 0;
        while (depth < grid.length * grid.length) {
            Set<Point> visited = new HashSet<>();
            Point result = dfs(grid, start, goal, depth, visited);
            if (result != null) {
                return result;
            }
            depth++;
        }
        return null;
    }

    private static Point dfs(int[][] grid, Point current, Point goal, int depth, Set<Point> visited) {
        if (depth == 0 && current.x == goal.x && current.y == goal.y) {
            return current;
        }

        if (depth > 0) {
            visited.add(current);

            // Explore neighbors (up, down, left, right)
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (isValid(grid, newX, newY)) {
                    Point neighbor = new Point(newX, newY, current);
                    if (!visited.contains(neighbor)) {
                        Point result = dfs(grid, neighbor, goal, depth - 1, visited);
                        if (result != null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean isValid(int[][] grid, int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid.length
                && (grid[x][y] != OBSTACLE);
    }

    private static void markPath(int[][] grid, Point end) {
        while (end != null && grid[end.x][end.y] != START) {
            grid[end.x][end.y] = PATH;
            end = end.parent;
        }
    }

    private static boolean hasCycle(int[][] grid, Point start) {
        Set<Point> visited = new HashSet<>();
        return dfsCycleDetection(grid, start, null, visited);
    }

    private static boolean dfsCycleDetection(int[][] grid, Point current, Point parent, Set<Point> visited) {
        visited.add(current);

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newX = current.x + dir[0];
            int newY = current.y + dir[1];

            if (isValid(grid, newX, newY)) {
                Point neighbor = new Point(newX, newY, null);
                if (!neighbor.equals(parent)) {
                    if (visited.contains(neighbor)) {
                        return true; // Cycle detected
                    }
                    if (dfsCycleDetection(grid, neighbor, current, visited)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                switch (cell) {
                    case OBSTACLE: System.out.print("X "); break;
                    case START: System.out.print("S "); break;
                    case GOAL: System.out.print("G "); break;
                    case PATH: System.out.print(". "); break;
                    case VISITED: System.out.print("~ "); break;
                    default: System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }
}