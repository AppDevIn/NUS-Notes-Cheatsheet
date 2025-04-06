import java.util.LinkedList;
import java.util.Queue;

public class MazeSolverWithPower2
		implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};

	// Create a class to store state with power
	private static class State {
		int row, col, power;
		State parent;

		public State(int row, int col, int power, State parent) {
			this.row = row;
			this.col = col;
			this.power = power;
			this.parent = parent;
		}
	}

	private Maze maze;
	private int rows, cols;
	private int[] steps;

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
		this.steps = new int[this.rows * this.cols];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// Reset the maze path
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.maze.getRoom(i, j).onPath = false;
			}
		}

		// Reset steps array
		for (int i = 0; i < this.rows * this.cols; i++) {
			this.steps[i] = 0;
		}

		// Use 3D visited array to track [row][col][power]
		boolean[][][] visited = new boolean[this.rows][this.cols][superpowers + 1];
		boolean[][] counted = new boolean[this.rows][this.cols];

		Queue<State> queue = new LinkedList<>();
		State startState = new State(startRow, startCol, superpowers, null);
		queue.add(startState);
		visited[startRow][startCol][superpowers] = true;
		counted[startRow][startCol] = true;
		this.steps[0]++;

		int currentSteps = 0, ans = -1;
		State endState = null;

		while (!queue.isEmpty()) {
			int queueSize = queue.size();

			for (int i = 0; i < queueSize; i++) {
				State current = queue.poll();
				if (current == null) break;

				int row = current.row, col = current.col, power = current.power;

				if (row == endRow && col == endCol && ans == -1) {
					endState = current;
					ans = currentSteps;
				}

				if (!counted[row][col]) {
					counted[row][col] = true;
					this.steps[currentSteps]++;
				}

				for (int direction = 0; direction < 4; ++direction) {
					int newRow = row + DELTAS[direction][0];
					int newCol = col + DELTAS[direction][1];

					// Check if new position is valid
					if (newRow < 0 || newRow >= this.rows || newCol < 0 || newCol >= this.cols) {
						continue;
					}

					boolean hasWall = hasWall(row, col, direction);

					if (!hasWall && !visited[newRow][newCol][power]) {
						visited[newRow][newCol][power] = true;
						queue.add(new State(newRow, newCol, power, current));
					} else if (hasWall && power > 0 && !visited[newRow][newCol][power - 1]) {
						visited[newRow][newCol][power - 1] = true;
						queue.add(new State(newRow, newCol, power - 1, current));
					}
				}
			}

			currentSteps++;
		}

		// Trace back the path if solution found
		if (endState != null) {
			State curr = endState;
			while (curr != null) {
				this.maze.getRoom(curr.row, curr.col).onPath = true;
				curr = curr.parent;
			}
			this.maze.getRoom(startRow, startCol).onPath = true;
			return ans;
		}

		return null;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		return pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (k >= this.rows * this.cols) {
			return 0;
		}

		return this.steps[k];
	}

	private boolean hasWall(int row, int col, int dir) {
		return switch (dir) {
			case NORTH -> maze.getRoom(row, col).hasNorthWall();
			case SOUTH -> maze.getRoom(row, col).hasSouthWall();
			case EAST -> maze.getRoom(row, col).hasEastWall();
			case WEST -> maze.getRoom(row, col).hasWestWall();
			default -> false;
		};
	}

//	public static void main(String[] args) {
//		try {
//			Maze maze = Maze.readMaze("maze-dense.txt");
//			IMazeSolver solver = new MazeSolverWithPower();
//
//			solver.initialize(maze);
//			System.out.println(solver.pathSearch(0, 0, 3, 3));
//			ImprovedMazePrinter.printMaze(maze, 0, 0);
//
//			System.out.println();
//
//			System.out.println(solver.pathSearch(0, 0, 2, 3));
//			MazePrinter.printMaze(maze);
//
//			System.out.println("Average is the new cool!");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public static void main(String[] args) {
//		try {
//			Maze maze = Maze.readMaze("maze-dense.txt");
//			IMazeSolver solver = new MazeSolverWithPower();
//			solver.initialize(maze);
//
//			int rows = maze.getRows();
//			int cols = maze.getColumns();
//
//			for (int startR = 0; startR < rows; startR++) {
//				for (int startC = 0; startC < cols; startC++) {
//					for (int endR = 0; endR < rows; endR++) {
//						for (int endC = 0; endC < cols; endC++) {
//							if (startR == endR && startC == endC) continue; // Skip same point
//
//							// Re-initialize maze before each run to clear onPath
//							solver.initialize(maze);
//
//							Integer pathLength = solver.pathSearch(startR, startC, endR, endC);
//							System.out.printf("Path from (%d,%d) to (%d,%d): %s\n",
//									startR, startC, endR, endC,
//									(pathLength != null) ? pathLength : "No path");
//
//							// Optional: Uncomment to print the maze
//							// ImprovedMazePrinter.printMaze(maze, startR, startC);
//							// System.out.println();
//						}
//					}
//				}
//			}
//
//			System.out.println("Done testing all start-end pairs.");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public static void main(String[] args) {
//		try {
//			Maze maze = Maze.readMaze("maze-dense.txt");
//			IMazeSolverWithPower solver = new MazeSolverWithPower2();
//
//			solver.initialize(maze);
//			System.out.println(solver.pathSearch(0, 0, 3, 3, 2));
//			ImprovedMazePrinter.printMaze(maze, 0, 0);
//
//
//
//			System.out.println("Average is the new cool!");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower2();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 3, 3, 2));
			ImprovedMazePrinter.printMaze(maze,0,0);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
