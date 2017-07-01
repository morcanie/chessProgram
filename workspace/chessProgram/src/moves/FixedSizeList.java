package moves;

import boardFeatures.Direction;
import boardFeatures.Square;
import support.BadArgumentException;

/**
 * Acting like a list and backed by an array, this keeps a list of squares that you can add to, up to the maxSize,
 * but can't remove from. Therefore, this is able to keep track of where the squares are placed, so it can quickly
 * get neighbors as well as the index of a particular square.
 * @author matthewslesinski
 *
 */
public class FixedSizeList implements ImportantSquareList {
	private final Square[] squares;
	private final int[] indices = new int[Square.values().length];
	private final Direction oppositeDirectionOfNewSquares;
	private int size = 0;
	
	public FixedSizeList(int maxSize, Direction dir) {
		this.squares = new Square[maxSize];
		this.oppositeDirectionOfNewSquares = dir.getOppositeDirection();
	}
	
	@Override
	public void add(Square square) {
		if (size >= squares.length) {
			throw new BadArgumentException(square, FixedSizeList.class, "Can't add too many squares to this list");
		}
		Square intermediateSquare = square;
		
		Square previous = size == 0 ? null : squares[size - 1];
		while ((intermediateSquare = intermediateSquare.getNeighbor(oppositeDirectionOfNewSquares)) != previous) {
			indices[intermediateSquare.getIndex()] = size;
		}
		

		squares[size] = square;
		indices[square.getIndex()] = size++;
	}
	
	@Override
	public boolean contains(Square square) {
		return squares[indices[square.getIndex()]] == square;
	}
	
	@Override
	public Square getNeighbor(Square square, int diff) {
		if (size == 0) {
			return null;
		}
		if (diff == 0) {
			return square;
		}
		int index;
		int currentSquare = square.getIndex();
		int currentIndex = indices[currentSquare];
		if (!contains(square)) {
			if (currentSquare > squares[0].getIndex()) {
				currentIndex = size;
			}
			if (diff > 0) {
				currentIndex -= 1;
			}
		}
		
		index = currentIndex + diff;
		if (index < 0 || index >= size) {
			return null;
		}
		return squares[index];
	}
	
}