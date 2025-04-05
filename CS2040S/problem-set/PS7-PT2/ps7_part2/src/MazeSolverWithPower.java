import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 }
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
		return this.pathSearch(startR, startC, targetR, targetC, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		return (k >= 0 && k < exploredPerLevel.size()) ? exploredPerLevel.get(k) : 0;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int superpowers) throws Exception {
		if (mazeRef == null) throw new Exception("Maze is not initialized!");
		if (!withinBounds(startRow, startCol) || !withinBounds(endRow, endCol)) {
			throw new IllegalArgumentException("Start or end point is out of maze bounds.");
		}

		int R = mazeRef.getRows();
		int C = mazeRef.getColumns();
		boolean[][][] visited = new boolean[R][C][superpowers + 1];
		int[][][][] cameFromPower = new int[R][C][superpowers + 1][3];

		exploredPerLevel.clear();

		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[] { startRow, startCol, superpowers, 0 });
		visited[startRow][startCol][superpowers] = true;

		int bestPowerLeft = -1;
		int bestDistance = -1;

		while (!queue.isEmpty()) {
			int batchSize = queue.size();
			exploredPerLevel.add(batchSize);

			while (batchSize-- > 0) {
				int[] curr = queue.poll();
				int r = curr[0], c = curr[1], powersLeft = curr[2], dist = curr[3];

				if (r == endRow && c == endCol) {
					bestPowerLeft = powersLeft;
					bestDistance = dist;
				}



				for (int d = 0; d < 4; d++) {
					int nextR = r + DELTAS[d][0];
					int nextC = c + DELTAS[d][1];
					if (!withinBounds(nextR, nextC)) continue;

					boolean hasWall = !canTraverse(r, c, d);
					if (hasWall && powersLeft > 0 && !visited[nextR][nextC][powersLeft - 1]) {
						visited[nextR][nextC][powersLeft - 1] = true;
						cameFromPower[nextR][nextC][powersLeft - 1] = new int[] { r, c, powersLeft };
						queue.add(new int[] { nextR, nextC, powersLeft - 1, dist + 1 });
					} else if (!hasWall && !visited[nextR][nextC][powersLeft]) {
						visited[nextR][nextC][powersLeft] = true;
						cameFromPower[nextR][nextC][powersLeft] = new int[] { r, c, powersLeft };
						queue.add(new int[] { nextR, nextC, powersLeft, dist + 1 });
					}
				}
			}
		}

		if (bestPowerLeft != -1) {
			highlightPath(startRow, startCol, endRow, endCol, cameFromPower, bestPowerLeft);
			return bestDistance;
		}
		return null;
	}

	private void highlightPath(int startR, int startC, int targetR, int targetC,
							   int[][][][] cameFromPower, int finalPower) {
		int r = targetR, c = targetC, power = finalPower;
		while (!(r == startR && c == startC)) {
			mazeRef.getRoom(r, c).onPath = true;
			int[] prev = cameFromPower[r][c][power];
			r = prev[0];
			c = prev[1];
			power = prev[2];
		}
		mazeRef.getRoom(startR, startC).onPath = true;
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
}