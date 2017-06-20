package boardFeatures;

import java.util.List;
import java.util.stream.Collectors;

import support.BadArgumentException;
import support.UtilityFunctions;

public enum DownRightDiagonal implements Line {
	
	A1,
	A2_B1,
	A3_C1,
	A4_D1,
	A5_E1,
	A6_F1,
	A7_G1,
	A8_H1,
	B8_H2,
	C8_H3,
	D8_H4,
	E8_H5,
	F8_H6,
	G8_H7,
	H8;
	
	
	private static final int CENTER_INDEX = 7;
	private static final int MAX_LENGTH = 8;
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
						// As you go rightward along the diagonal, the rank drops. The start rank is the center diagonal
						// index minus the index, when the index is less than the center's, or 0
						Rank.getByIndex((byte) (((index / MAX_LENGTH) - 1) * -1 * index - offset))))
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
		return square.getDownRightDiagonal() == this;
	}

	@Override
	public int getSpotInLine(Square square) {
		return square.getRank().getIndex();
	}
	
	/**
	 * Gets the diagonal that has the given index
	 * @param index The index to get
	 * @return The {@code DownRightDiagonal} with number one greater than the index
	 * @throws BadArgumentException If the index is not an index for a file
	 */
	public static DownRightDiagonal getByIndex(int index) throws BadArgumentException {
		return Line.getByIndex(index, DownRightDiagonal.class);
	}
	
	/**
	 * Returns the {@code DownRightDiagonal} containing a square
	 * @param square the {@code Square} to check
	 * @return the {@code DownRightDiagonal}
	 */
	public static DownRightDiagonal getBySquare(Square square) {
		int index = square.getFile().getIndex() + square.getRank().getIndex();
		return getByIndex(index);
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}

}
