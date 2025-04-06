import java.util.*;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;

	private static final int[][] DELTAS = new int[][] {
			{-1, 0}, // North
			{0, 1},  // East
			{0, -1}, // West
			{1, 0}   // South
	};

	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);

	private Maze maze;

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) throw new Exception("Maze not initialized");

		int rows = maze.getRows();
		int cols = maze.getColumns();

		int[][] fear = new int[rows][cols];
		for (int[] row : fear)
			Arrays.fill(row, Integer.MAX_VALUE);
		fear[startRow][startCol] = 0;

		PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
		priorityQueue.offer(new int[] {startRow, startCol, 0});

		while (!priorityQueue.isEmpty()) {

			int[] curr = priorityQueue.poll();

			int row = curr[0];
			int col = curr[1];
			int currFear = curr[2];

			if (row == endRow && col == endCol) return currFear;

			int dir = 0;
			for (int[] del : DELTAS) {
				int newRow = row + del[0];
				int newCol = col + del[1];

				if (!inBounds(newRow, newCol, rows, cols)) {
					dir++;
					continue;
				}

				Room currentRoom = maze.getRoom(row, col);
				int wallCost = WALL_FUNCTIONS.get(dir).apply(currentRoom);
				dir++;

				if (wallCost == TRUE_WALL)
					continue;

				int newFear = calculateFear(currFear, wallCost);

				if (newFear >= fear[newRow][newCol])
					continue;

				fear[newRow][newCol] = newFear;
				priorityQueue.offer(new int[] {newRow, newCol, newFear});
			}
		}

		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) throw new Exception("Maze not initialized");

		int rows = maze.getRows();
		int cols = maze.getColumns();

		int[][] fear = new int[rows][cols];
		for (int[] row : fear)
			Arrays.fill(row, Integer.MAX_VALUE);
		fear[startRow][startCol] = 0;

		PriorityQueue<int[]> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
		priorityQueue.offer(new int[] {startRow, startCol, 0});

		while (!priorityQueue.isEmpty()) {
			int[] curr = priorityQueue.poll();
			int row = curr[0];
			int col = curr[1];
			int currFear = curr[2];

			if (row == endRow && col == endCol) return currFear;

			int dir = 0;
			for (int[] del : DELTAS) {
				int newRow = row + del[0];
				int newCol = col + del[1];

				if (!inBounds(newRow, newCol, rows, cols)) {
					dir++;
					continue;
				}

				Room currentRoom = maze.getRoom(row, col);
				int wallCost = WALL_FUNCTIONS.get(dir).apply(currentRoom);
				dir++;

				if (wallCost == TRUE_WALL) continue;

				int newFear = calculateBonusFear(currFear, wallCost);

				if (newFear >= fear[newRow][newCol]) continue;

				fear[newRow][newCol] = newFear;
				priorityQueue.offer(new int[] {newRow, newCol, newFear});
			}
		}

		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// Not implemented
		return null;
	}

	private boolean inBounds(int row, int col, int rows, int cols) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

	private int calculateFear(int currentFear, int wall) {
		if (wall == EMPTY_SPACE) {
			return currentFear + 1;
		} else {
			return currentFear + wall;
		}
	}

	private int calculateBonusFear(int currentFear, int wall) {
		if (wall == EMPTY_SPACE) {
			return currentFear + 1;
		} else {
			return Math.max(currentFear, wall);
		}
	}
}