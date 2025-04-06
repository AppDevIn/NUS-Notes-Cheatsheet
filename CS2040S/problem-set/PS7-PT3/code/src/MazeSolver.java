import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;

	public MazeSolver() {
		// TODO: Initialize variables.
	}
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
		for (int[] row : fear) Arrays.fill(row, Integer.MAX_VALUE);
		fear[startRow][startCol] = 0;

		PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
		pq.offer(new int[] {startRow, startCol, 0});

		while (!pq.isEmpty()) {
			int[] curr = pq.poll();
			int r = curr[0], c = curr[1], f = curr[2];

			if (r == endRow && c == endCol) return f;

			for (int d = 0; d < 4; d++) {
				int newRow = r + DELTAS[d][0];
				int newCol = c + DELTAS[d][1];

				if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) continue;

				int wallCost = getWallCost(maze.getRoom(r, c), d);
				if (wallCost == TRUE_WALL) continue;

				int stepFear = (wallCost == EMPTY_SPACE) ? 1 : wallCost;
				int newFear = f + stepFear;

				if (newFear < fear[newRow][newCol]) {
					fear[newRow][newCol] = newFear;
					pq.offer(new int[] {newRow, newCol, newFear});
				}
			}
		}

		return null;
	}

	private int getWallCost(Room room, int dir) {
		return switch (dir) {
			case 0 -> room.getNorthWall();
			case 1 -> room.getEastWall();
			case 2 -> room.getWestWall();
			case 3 -> room.getSouthWall();
			default -> TRUE_WALL;
		};
	}


	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 0, 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
