/**
 * Represents a single room in the maze.
 */
public class Room {
	private boolean westWall, eastWall, northWall, southWall;
	public boolean onPath;

	Room(boolean north, boolean south, boolean east, boolean west) {
		northWall = north;
		southWall = south;
		eastWall = east;
		westWall = west;

		onPath = false;
	}

	/**
	 * @return true iff there is a wall to the west of the room
	 */
	public boolean hasWestWall() {
		return westWall;
	}

	/**
	 * @return true iff there is a wall to the east of the room
	 */
	public boolean hasEastWall() {
		return eastWall;
	}

	/**
	 * @return true iff there is a wall to the north of the room
	 */
	public boolean hasNorthWall() {
		return northWall;
	}

	/**
	 * @return true iff there is a wall to the south of the room
	 */
	public boolean hasSouthWall() {
		return southWall;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Room other = (Room) obj;
		return this.hasNorthWall() == other.hasNorthWall()
				&& this.hasSouthWall() == other.hasSouthWall()
				&& this.hasEastWall() == other.hasEastWall()
				&& this.hasWestWall() == other.hasWestWall()
				&& this.onPath == other.onPath; // or remove if comparing structure only
	}
}
