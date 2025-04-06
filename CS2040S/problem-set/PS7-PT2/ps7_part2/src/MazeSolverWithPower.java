import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {

	private static final int[][] DELTAS = {
			{-1, 0}, // North
			{1, 0},  // South
			{0, 1},  // East
			{0, -1}  // West
	};

	private Maze maze;
	private int rows;
	private int cols;
	private int[] reachable;
	private int[][][][] cameFrom;

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
		this.reachable = new int[rows * cols];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) throws Exception {
		if (maze == null)
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		if (!inBounds(startRow, startCol) || !inBounds(endRow, endCol))
			throw new IllegalArgumentException("Start or end point is out of maze bounds.");

		resetMazeState();

		boolean[][][] visited = new boolean[rows][cols][superpowers + 1];
		boolean[][] counted = new boolean[rows][cols];
		cameFrom = new int[rows][cols][superpowers + 1][3];

		Queue<int[]> queue = new LinkedList<>();
		queue.offer(new int[]{startRow, startCol, superpowers, 0});
		visited[startRow][startCol][superpowers] = true;
		counted[startRow][startCol] = true;
		reachable[0]++;

		int[] endPos = bfsWithPowers(queue, visited, counted, endRow, endCol);

		if (endPos == null) return null;

		highlightPath(startRow, startCol, endPos[0], endPos[1], endPos[2]);
		return endPos[3];
	}

	private void highlightPath(int startRow, int startCol, int row, int col, int power) {
		while (!(row == startRow && col == startCol)) {
			maze.getRoom(row, col).onPath = true;
			int[] prev = cameFrom[row][col][power];
			row = prev[0];
			col = prev[1];
			power = prev[2];
		}
		maze.getRoom(startRow, startCol).onPath = true;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		return this.pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (maze == null) throw new Exception("Maze not initialized");
		if (k < 0 || k >= reachable.length) return 0;
		return reachable[k];
	}

	private boolean inBounds(int r, int c) {
		return r >= 0 && r < rows && c >= 0 && c < cols;
	}

	private int[] bfsWithPowers(Queue<int[]> queue, boolean[][][] visited, boolean[][] counted,
								int endRow, int endCol) {

		int[] endPos = null;

		while (!queue.isEmpty()) {
			int[] curr = queue.poll();
			int r = curr[0], c = curr[1], power = curr[2], step = curr[3];

			if (r == endRow && c == endCol && endPos == null)
				endPos = curr;

			for (int d = 0; d < 4; d++) {
				int newRow = r + DELTAS[d][0];
				int newCol = c + DELTAS[d][1];

				if (!inBounds(newRow, newCol)) continue;

				boolean wall = canTraverse(r, c, d);
				int np = wall ? power - 1 : power;

				if (np < 0 || visited[newRow][newCol][np]) continue;

				visited[newRow][newCol][np] = true;
				cameFrom[newRow][newCol][np] = new int[]{r, c, power};
				queue.offer(new int[]{newRow, newCol, np, step + 1});

				if (!counted[newRow][newCol]) {
					counted[newRow][newCol] = true;
					reachable[step + 1]++;
				}
			}
		}

		return endPos;
	}

	private boolean canTraverse(int r, int c, int dir) {
		Room room = maze.getRoom(r, c);
		return switch (dir) {
			case 0 -> room.hasNorthWall();
			case 1 -> room.hasSouthWall();
			case 2 -> room.hasEastWall();
			case 3 -> room.hasWestWall();
			default -> true;
		};
	}

	private void resetMazeState() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				maze.getRoom(r, c).onPath = false;
			}
		}
		Arrays.fill(reachable, 0);
	}
}