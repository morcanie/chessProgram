package lines;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static support.UtilityFunctions.*;

/**
 * Directions are used to indicate a relative motion along either a diagonal, file, or rank. If the motion is not along
 * such a line, then it is represented by NONE. Each {@code Direction} is defined by an increment in the x and y directions.
 * The increments are all the number of columns/rows over the next square in that direction would be. Furthermore, {@code Direction}s
 * all have a {@code LineType} that they operate in, though they can't have specific {@code Line}s they're within since they're
 * relative motion. Exactly two {@code Direction}s will have the same {@code LineType}, and those {@code Direction}s are trivially
 * the opposite direction from each other. 
 * @author matthewslesinski
 *
 */
public enum Direction {

	DOWN_LEFT(-1, -1),
	LEFT(-1, 0),
	UP_LEFT(-1, 1),
	DOWN(0, -1),
	NONE(0, 0),
	UP(0, 1),
	DOWN_RIGHT(1, -1),
	RIGHT(1, 0),
	UP_RIGHT(1, 1);
	
	/** Holds all the {@code Directions}s that aren't NONE */
	private static final List<Direction> outwardDirections = calculateOutwardDirections();
	
	
	/** The ordinal of NONE */
	private static final int CENTER_INDEX = 4;
	
	/** The ordinal of the last {@code Direction} in the order listed */
	private static final int LAST_INDEX = CENTER_INDEX << 1;
	
	/** The increment for this direction along the x axis */
	private final int fileDelta;
	
	/** The increment for this direction along the y axis */
	private final int rankDelta;
	
	/** The type of line this direction moves along. This is null for NONE */
	private final LineType containingLineType;
	
	/** Whether this {@code Direction} moves forwards or backwards along the {@code Line}s it can move along */
	private final Movement movement;
	

	private Direction(int fileDelta, int rankDelta) {
		this.fileDelta = fileDelta;
		this.rankDelta = rankDelta;
		containingLineType = getLineType();
		movement = determineMovement();
	}
	
	/**
	 * Gets the opposite direction from this one
	 * @return The opposite {@code Direction}
	 */
	public Direction getOppositeDirection() {
		return values()[LAST_INDEX - this.ordinal()];
	}
	
	/**
	 * Gets how many indices over the next file in this direction is
	 * @return the change
	 */
	public int getFileDelta() {
		return fileDelta;
	}
	
	/**
	 * Gets how many indices over the next rank in this direction is
	 * @return the change
	 */
	public int getRankDelta() {
		return rankDelta;
	}
	
	/**
	 * Calculates what the manhattan distance between two consecutive squares in this direction is (in other words
	 * it returns 1 if the direction is vertical/horizontal, 2 if diagonal, and 0 if {@code NONE})
	 * @return The distance
	 */
	public int getSuccessiveManhattanDistanceDelta() {
		return Math.abs(fileDelta) + Math.abs(rankDelta);
	}
	
	/**
	 * Retrieves the class of the {@code Line} that contains movement in this {@code Direction}
	 * @return The {@code LineType}
	 */
	public LineType getContainingLineType() {
		return containingLineType;
	}
	
	/**
	 * Retrieves the type of movement this direction uses along its line
	 * @return The {@code Movement}
	 */
	public Movement getMovement() {
		return this.movement;
	}
	
	/**
	 * Retrieves the list of the directions that face outward
	 * @return The list of directions
	 */
	public static List<Direction> getOutwardDirections() {
		return outwardDirections;
	}
	
	/**
	 * Gets the direction based on the deltas, which can't have a magnitude greater than 1
	 * @param fileDelta The difference in the file index
	 * @param rankDelta The difference in the rank index
	 * @return The direction represented by that difference
	 */
	public static Direction getByDeltas(int fileDelta, int rankDelta) {
		if (Math.abs(fileDelta) > 1 || Math.abs(rankDelta) > 1) {
			return Direction.NONE;
		}
		return Direction.values()[(fileDelta + 1) * 3 + rankDelta + 1];
	}
	
	/**
	 * Gets the type of line this direction goes along
	 * @return The {@code LineType} enum
	 */
	private LineType getLineType() {
		switch (Math.abs(CENTER_INDEX - this.ordinal())) {
		case 1:
			return LineType.FILE;
		case 2:
			return LineType.DOWN_RIGHT_DIAGONAL;
		case 3:
			return LineType.RANK;
		case 4:
			return LineType.UP_RIGHT_DIAGONAL;
		default:
			return null;
		}
	}
	
	/**
	 * Returns the movement this direction proceeds by along its {@code Line}
	 * @return The {@code Movement}
	 */
	private Movement determineMovement() {
		switch (getSign(CENTER_INDEX - this.ordinal())) {
		case -1:
			return Movement.FORWARDS;
		case 1:
			return Movement.BACKWARDS;
		default:
			return Movement.NOWHERE;
		}
	}
	
	/**
	 * Determines the list of directions that face outward
	 * @return The list of the directions
	 */
	private static List<Direction> calculateOutwardDirections() {
		return Arrays.asList(values()).stream().filter(dir -> dir != NONE).collect(Collectors.toList());
	}
}
