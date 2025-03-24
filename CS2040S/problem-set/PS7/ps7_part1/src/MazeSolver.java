import java.util.*;

public class MazeSolver implements IMazeSolver {

	private static final int[][] DELTAS = {
			{-1, 0}, // UP
			{1, 0},  // DOWN
			{0, 1},  // RIGHT
			{0, -1}  // LEFT
	};

	private Maze mazeRef;
	private int[][] visited;
	private Room[][] cameFrom;
	private List<Integer> exploredPerLevel;

	@Override
	public void initialize(Maze maze) {
		this.mazeRef = maze;

		int totalRows = maze.getRows();
		int totalCols = maze.getColumns();
		
		visited = new int[totalRows][totalCols];
		cameFrom = new Room[totalRows][totalCols];
		exploredPerLevel = new ArrayList<>();
	}

	@Override
	public Integer pathSearch(int startR, int startC, int targetR, int targetC) throws Exception {
		if (mazeRef == null) throw new Exception("Maze is not initialized!");

		if (!withinBounds(startR, startC) || !withinBounds(targetR, targetC)) {
			throw new IllegalArgumentException("Start or end point is out of maze bounds.");
		}

		int R = mazeRef.getRows();
		int C = mazeRef.getColumns();

		for (int i = 0; i < R; i++) {

			for (int j = 0; j < C; j++) {
				visited[i][j] = -1;
				cameFrom[i][j] = null;
				mazeRef.getRoom(i, j).onPath = false;


			}
		}

		exploredPerLevel.clear();

		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[] {startR, startC});
		visited[startR][startC] = 0;

		while (!queue.isEmpty()) {

				int batchSize = queue.size();

			exploredPerLevel.add(batchSize);

			while (batchSize-- > 0) {
				int[] cell = queue.poll();
				int r = cell[0], c = cell[1];

				for (int d = 0; d < 4; d++) {
					int nextR = r + DELTAS[d][0];
					int nextC = c + DELTAS[d][1];

					if (!withinBounds(nextR, nextC) || visited[nextR][nextC] != -1) continue;

					if (!canTraverse(r, c, d)) continue;

					cameFrom[nextR][nextC] = mazeRef.getRoom(r, c);
					visited[nextR][nextC] = visited[r][c] + 1;

					queue.add(new int[] {nextR, nextC});
				}
			}
		}

		if (visited[targetR][targetC] == -1) return null;

		highlightPath(startR, startC, targetR, targetC);

		return visited[targetR][targetC];
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		return (k >= 0 && k < exploredPerLevel.size()) ? exploredPerLevel.get(k) : 0;
	}

	private boolean withinBounds(int r, int c) {
		return r >= 0 && r < mazeRef.getRows() && c >= 0 && c < mazeRef.getColumns();
	}

	private boolean canTraverse(int r, int c, int dir) {
		Room current = mazeRef.getRoom(r, c);
		return switch (dir) {
			case 0 -> !current.hasNorthWall();
			case 1 -> !current.hasSouthWall();
			case 2 -> !current.hasEastWall();
			case 3 -> !current.hasWestWall();
			default -> false;
		};
	}

	private void highlightPath(int startR, int startC, int targetR, int targetC) {
		int r = targetR, c = targetC;
		while (!(r == startR && c == startC)) {
			mazeRef.getRoom(r, c).onPath = true;
			Room from = cameFrom[r][c];
			boolean backtracked = false;

			for (int d = 0; d < 4 && !backtracked; d++) {
				int pr = r - DELTAS[d][0], pc = c - DELTAS[d][1];
				if (withinBounds(pr, pc) && mazeRef.getRoom(pr, pc) == from) {
					r = pr;
					c = pc;
					backtracked = true;
				}
			}
		}
		mazeRef.getRoom(startR, startC).onPath = true;
	}
}
