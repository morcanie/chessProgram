package lines;

import java.util.List;

import boardFeatures.Square;

/**
 * Represents a basic concept of movement. You can either move forwards, backwards, or neither
 * @author matthewslesinski
 *
 */
public enum Movement {
	BACKWARDS,
	NOWHERE,
	FORWARDS;
	
	private final int increment = this.ordinal() - 1;
	
	/**
	 * Retrieves the increment for this movement
	 */
	public int getIncrement() {
		return increment;
	}
	
	/**
	 * Returns the list of squares that lie either in front of or behind this square, in the line of the provided type
	 * @param square The base square
	 * @param lineType The type of line 
	 * @return
	 */
	public List<Square> getSquaresToMoveThrough(Square square, LineType lineType) {
		switch (increment) {
		case -1:
			return (lineType.getLineBySquare(square)).getSquaresBehind(square);
		case 1:
			return (lineType.getLineBySquare(square)).getSquaresInFront(square);
		default:
			return null;
		}
	}
}
