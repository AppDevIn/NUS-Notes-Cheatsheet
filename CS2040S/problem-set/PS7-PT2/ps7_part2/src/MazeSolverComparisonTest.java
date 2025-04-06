import java.util.Objects;

public class MazeSolverComparisonTest {
    public static void main(String[] args) {
        try {
            String[] mazeFiles = {"maze-dense.txt", "maze-sample.txt"};
            int superpowers = 2;

            for (String mazeFile : mazeFiles) {
                System.out.println("Testing on maze: " + mazeFile);
                Maze maze1 = Maze.readMaze(mazeFile);
                Maze maze2 = Maze.readMaze(mazeFile);

                IMazeSolverWithPower solver1 = new MazeSolverWithPower();
                IMazeSolverWithPower solver2 = new MazeSolverWithPower2();

                solver1.initialize(maze1);
                solver2.initialize(maze2);

                int rows = maze1.getRows();
                int cols = maze1.getColumns();

                for (int startR = 0; startR < rows; startR++) {
                    for (int startC = 0; startC < cols; startC++) {
                        for (int endR = 0; endR < rows; endR++) {
                            for (int endC = 0; endC < cols; endC++) {
                                try {
                                    Integer result1 = solver1.pathSearch(startR, startC, endR, endC, superpowers);
                                    Integer result2 = solver2.pathSearch(startR, startC, endR, endC, superpowers);

                                    if (!Objects.equals(result1, result2)) {
                                        System.out.printf("Mismatch at (%d,%d) -> (%d,%d): Solver1=%s, Solver2=%s\n",
                                                startR, startC, endR, endC,
                                                result1 != null ? result1 : "null",
                                                result2 != null ? result2 : "null");
                                    }

                                } catch (Exception e) {
                                    System.out.printf("Error at (%d,%d) -> (%d,%d): %s\n",
                                            startR, startC, endR, endC, e.getMessage());
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("✅ Finished comparison of all permutations.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}