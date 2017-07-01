package moves;

import boardFeatures.Square;

public interface ImportantSquareList {

	
	/**
	 * Adds a square to this list
	 * @param square The square to add
	 */
	public void add(Square square);
	
	/**
	 * Does this list contain the square, or in other words, does this square have a piece?
	 * @param square The square to check
	 * @return if it's in this list
	 */
	public boolean contains(Square square);
	
	/**
	 * Gets the square in this list that is a certain number of other squares removed from the current one
	 * @param square The current square
	 * @param diff The number of squares removed the other one is
	 * @return The square a certain number of other squares removed
	 */
	public Square getNeighbor(Square square, int diff);
}
