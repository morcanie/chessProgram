package boardFeatures;

import java.util.List;
import java.util.stream.Collectors;

import support.BadArgumentException;
import support.UtilityFunctions;

public enum UpRightDiagonal implements Line {
	
	A8,
	A7_B8,
	A6_C8,
	A5_D8,
	A4_E8,
	A3_F8,
	A2_G8,
	A1_H8,
	B1_H7,
	C1_H6,
	D1_H5,
	E1_H4,
	F1_H3,
	G1_H2,
	H1;
	
	private static final int CENTER_INDEX = 7;
	private static final int MAX_LENGTH = 8;
	private static final Direction directionOfLine = Direction.UP_RIGHT;
	private final List<Square> containedSquares = getListOfContainedSquares(this.getIndex());

	/**
	 * The set of squares contained in this {@code File}, but reversed
	 */
	private final List<Square> reverseContainedSquares = UtilityFunctions.reverseList(containedSquares);
	
	
	/**
	 * Gets the list of squares that are in this diagonal
	 * @param index The index identifying the diagonal
	 * @return The list of {@code Square}
	 */
	private static List<Square> getListOfContainedSquares(int index) {
		// The length is 8 minus how far the index is from the center, which is at index 7
		int length = MAX_LENGTH - Math.abs(index - CENTER_INDEX);
		return UtilityFunctions.getRange(0, length).stream()
				.map(offset -> Square.getByFileAndRank(
						// As you go rightward along the diagonal, the file of the square rises proportionally
						// The start file is only either 0 or index minus the center diagonal index when the
						// diagonal has a higher index than the center
						File.getByIndex((byte) (offset + ((index / MAX_LENGTH) * (index - CENTER_INDEX)))),
						// As you go rightward along the diagonal, the rank rises. The start rank is the center diagonal
						// index minus the index, when the index is less than the center's, or 0
						Rank.getByIndex((byte) (((index / MAX_LENGTH) - 1) * -1 * (CENTER_INDEX - index) + offset))))
				.collect(Collectors.toList());
	}

	@Override
	public int getIndex() {
		return this.ordinal();
	}

	@Override
	public String getHumanReadableForm() {
		return this.getSquare((byte) 0).toString() + "-" +
				this.getSquare((byte) (this.getLength() - 1)).toString();
	}

	@Override
	public List<Square> getContainedSquares() {
		return containedSquares;
	}
	
	@Override
	public List<Square> getReverseContainedSquares() {
		return reverseContainedSquares;
	}

	@Override
	public boolean containsSquare(Square square) {
		return square.getUpRightDiagonal() == this;
	}

	@Override
	public int getSpotInLine(Square square) {
		return square.getRank().getIndex();
	}
	
	@Override
	public Direction getForwardDirection() {
		return directionOfLine;
	}
	
	/**
	 * Gets the diagonal that has the given index
	 * @param index The index to get
	 * @return The {@code UpRightDiagonal} with number one greater than the index
	 * @throws BadArgumentException If the index is not an index for a file
	 */
	public static UpRightDiagonal getByIndex(int index) throws BadArgumentException {
		return Line.getByIndex(index, UpRightDiagonal.class);
	}
	
	/**
	 * Returns the {@code UpRightDiagonal} containing a square
	 * @param square the {@code Square} to check
	 * @return the {@code UpRightDiagonal}
	 */
	public static UpRightDiagonal getBySquare(Square square) {
		int index = CENTER_INDEX + square.getFile().getIndex() - square.getRank().getIndex();
		return getByIndex(index);
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}

}