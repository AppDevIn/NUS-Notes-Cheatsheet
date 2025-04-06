import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {

	private static final int[][] DELTAS = {
			{-1, 0}, // North
			{1, 0},  // South
			{0, 1},  // East
			{0, -1}  // West
	};

	private Maze maze;
	private int rows, cols;
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
	public Integer pathSearch(int sr, int sc, int er, int ec, int superpowers) throws Exception {
		if (maze == null) throw new Exception("Maze not initialized");
		if (!inBounds(sr, sc) || !inBounds(er, ec)) throw new IllegalArgumentException("Invalid coordinates");

		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				maze.getRoom(r, c).onPath = false;

		Arrays.fill(reachable, 0);
		boolean[][][] visited = new boolean[rows][cols][superpowers + 1];
		boolean[][] counted = new boolean[rows][cols];
		cameFrom = new int[rows][cols][superpowers + 1][3]; // [row][col][power] = {prevRow, prevCol, prevPower}

		Queue<int[]> queue = new LinkedList<>();
		queue.offer(new int[]{sr, sc, superpowers, 0});
		visited[sr][sc][superpowers] = true;
		counted[sr][sc] = true;
		reachable[0]++;

		int[] endPos = null;

		while (!queue.isEmpty()) {
			int[] curr = queue.poll();
			int r = curr[0], c = curr[1], power = curr[2], step = curr[3];

			if (r == er && c == ec && endPos == null)
				endPos = curr;

			for (int d = 0; d < 4; d++) {
				int nr = r + DELTAS[d][0], nc = c + DELTAS[d][1];
				if (!inBounds(nr, nc)) continue;

				boolean wall = isBlocked(r, c, d);
				int np = wall ? power - 1 : power;
				if (np < 0 || visited[nr][nc][np]) continue;

				visited[nr][nc][np] = true;
				cameFrom[nr][nc][np] = new int[]{r, c, power};
				queue.offer(new int[]{nr, nc, np, step + 1});

				if (!counted[nr][nc]) {
					counted[nr][nc] = true;
					reachable[step + 1]++;
				}
			}
		}

		if (endPos == null) return null;

		tracePath(sr, sc, endPos[0], endPos[1], endPos[2]);
		return endPos[3];
	}

	private void tracePath(int sr, int sc, int r, int c, int power) {
		while (!(r == sr && c == sc)) {
			maze.getRoom(r, c).onPath = true;
			int[] prev = cameFrom[r][c][power];
			r = prev[0];
			c = prev[1];
			power = prev[2];
		}
		maze.getRoom(sr, sc).onPath = true;
	}

	@Override
	public Integer pathSearch(int sr, int sc, int er, int ec) throws Exception {
		return pathSearch(sr, sc, er, ec, 0);
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

	private boolean isBlocked(int r, int c, int dir) {
		Room room = maze.getRoom(r, c);
		return switch (dir) {
			case 0 -> room.hasNorthWall();
			case 1 -> room.hasSouthWall();
			case 2 -> room.hasEastWall();
			case 3 -> room.hasWestWall();
			default -> true;
		};
	}
}