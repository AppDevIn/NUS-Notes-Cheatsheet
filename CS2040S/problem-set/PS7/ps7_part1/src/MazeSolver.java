import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

record Position(int row, int col) {}
public class MazeSolver implements IMazeSolver {
	private static final int[][] DELTAS = {
			{-1, 0}, // NORTH
			{1, 0},  // SOUTH
			{0, 1},  // EAST
			{0, -1}  // WEST
	};

	private Maze maze;
	private int[][] distanceFromStart;
	private int[][] prevRow, prevCol;
	private List<Integer> reachableCounts;
	private int rows;

	private int cols;

	public MazeSolver() {
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
		distanceFromStart = new int[rows][cols];
		prevRow = new int[rows][cols];
		prevCol = new int[rows][cols];
		reachableCounts = new ArrayList<>();
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (this.maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= this.rows || startCol >= this.cols ||
				endRow < 0 || endCol < 0 || endRow >= this.rows || endCol >= this.cols) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}



		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				maze.getRoom(i, j).onPath = false;
				distanceFromStart[i][j] = -1;
				prevRow[i][j] = -1;
				prevCol[i][j] = -1;
			}
		}

		reachableCounts.clear();

		Queue<Position> queue = new LinkedList<>();
		queue.add(new Position(startRow, startCol));
		distanceFromStart[startRow][startCol] = 0;

		while (!queue.isEmpty()) {
			int levelSize = queue.size();

			reachableCounts.add(levelSize);

			for (int i = 0; i < levelSize; i++) {
				Position current = queue.poll();;
				int row = current.row(), col = current.col();;

				for (int dir = 0; dir < 4; dir++) {
					int newRow = row + DELTAS[dir][0];
					int newCol = col + DELTAS[dir][1];

					if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols)
						continue;


					if (distanceFromStart[newRow][newCol] == -1 && canGo(row, col, dir)) {
						distanceFromStart[newRow][newCol] = distanceFromStart[row][col] + 1;
						prevRow[newRow][newCol] = row;
						prevCol[newRow][newCol] = col;
						queue.add(new Position(newRow, newCol));;
					}
				}
			}
		}

		if (distanceFromStart[endRow][endCol] == -1) return null;
		this.reconstructPath(startRow, startCol, endRow, endCol);
		return distanceFromStart[endRow][endCol];
	}

	private boolean canGo(int row, int col, int dir) {
		Room room = maze.getRoom(row, col);
		return switch (dir) {
			case 0 -> !room.hasNorthWall();
			case 1 -> !room.hasSouthWall();
			case 2 -> !room.hasEastWall();
			case 3 -> !room.hasWestWall();
			default -> false;
		};
	}

	private void reconstructPath(int startRow, int startCol, int endRow, int endCol) {
		int r = endRow, c = endCol;

		while (r != startRow || c != startCol) {
			maze.getRoom(r, c).onPath = true;
			int pr = prevRow[r][c];
			int pc = prevCol[r][c];
			r = pr; c = pc;
		}

		maze.getRoom(startRow, startCol).onPath = true;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k < 0 || k >= reachableCounts.size()) return 0;
		return reachableCounts.get(k);
	}


}
