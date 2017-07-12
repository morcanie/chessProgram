package boardFeatures;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import support.UtilityFunctions;


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
	
	private static final int CENTER_INDEX = 4;
	private final int fileDelta;
	private final int rankDelta;
	private final Class<? extends Line> containingLineType;
	private final Movement movement;
	private final List<Direction> outwardDirections; 
	private Direction(int fileDelta, int rankDelta) {
		this.fileDelta = fileDelta;
		this.rankDelta = rankDelta;
		containingLineType = getLineType();
		movement = determineMovement();
		outwardDirections = calculateOutwardDirections();
	}
	
	/**
	 * Gets the opposite direction from this one
	 * @return The opposite {@code Direction}
	 */
	public Direction getOppositeDirection() {
		return values()[CENTER_INDEX - this.ordinal()];
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
	 * Retrieves the class of the {@code Line} that contains movement in this {@code Direction}
	 * @return The {@code Class}
	 */
	public Class<? extends Line> getContainingLineType() {
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
	public List<Direction> getOutwardDirections() {
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
			return null;
		}
		return Direction.values()[(fileDelta + 1) * 3 + rankDelta + 1];
	}
	
	/**
	 * Gets the type of line this direction goes along
	 * @return The {@code Line} subclass
	 */
	private Class<? extends Line> getLineType() {
		switch (Math.abs(CENTER_INDEX - this.ordinal())) {
		case 1:
			return File.class;
		case 2:
			return DownRightDiagonal.class;
		case 3:
			return Rank.class;
		case 4:
			return UpRightDiagonal.class;
		default:
			return null;
		}
	}
	
	/**
	 * Returns the movement this direction proceeds by along its {@code Line}
	 * @return The {@code Movement}
	 */
	private Movement determineMovement() {
		switch (UtilityFunctions.getSign(CENTER_INDEX - this.ordinal())) {
		case 1:
			return Movement.FORWARDS;
		case -1:
			return Movement.BACKWARDS;
		default:
			return Movement.NOWHERE;
		}
	}
	
	/**
	 * Determines the list of directions that face outward
	 * @return The list of the directions
	 */
	private List<Direction> calculateOutwardDirections() {
		return Arrays.asList(values()).stream().filter(dir -> dir != NONE).collect(Collectors.toList());
	}
}