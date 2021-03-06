package lines;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Square;
import support.BadArgumentException;
import static support.UtilityFunctions.*;

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
		
	/** The ordinal of the long diagonal */
	private static final int CENTER_INDEX = 7;
	
	/** The maximum length any of these diagonals can be */
	private static final int MAX_LENGTH = 8;
	
	// These are set in the setContainedSquares method, which is called by the StaticInitializer to avoid circular dependencies
	/** The set of squares contained in this {@code DownRightDiagonal} */
	private List<Square> containedSquares;

	/** The set of squares contained in this {@code DownRightDiagonal}, but reversed */
	private List<Square> reverseContainedSquares;

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
	public LineType getType() {
		return type();
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
	public int getManhattanDistanceToSquare(Square square) {
		return Math.abs(square.getDownRightDiagonal().getIndex() - this.getIndex());
	}
	
	/**
	 * Gets the diagonal that has the given index
	 * @param index The index to get
	 * @return The {@code DownRightDiagonal} with number one greater than the index
	 * @throws BadArgumentException If the index is not an index for a file
	 */
	public static DownRightDiagonal getByIndex(int index) throws BadArgumentException {
		return Line.getByIndex(index, type());
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
	
	/**
	 * Gets the list of squares that are in this diagonal
	 * @param index The index identifying the diagonal
	 * @return The list of {@code Square}
	 */
	private static List<Square> getListOfContainedSquares(int index) {
		// The length is 8 minus how far the index is from the center, which is at index 7
		int length = MAX_LENGTH - Math.abs(index - CENTER_INDEX);
		return Arrays.stream(getRange(0, length))
				.map(offset -> Square.getByFileAndRank(
						// As you go rightward along the diagonal, the file of the square rises proportionally
						// The start file is only either 0 or index minus the center diagonal index when the
						// diagonal has a higher index than the center
						File.getByIndex(offset + ((index / MAX_LENGTH) * (index - CENTER_INDEX))),
						// As you go rightward along the diagonal, the rank drops. The start rank is the center diagonal
						// index minus the index, when the index is less than the center's, or 0
						Rank.getByIndex(CENTER_INDEX - (index / MAX_LENGTH - 1) * -1 * (CENTER_INDEX - index) - offset)))
				.collect(Collectors.toList());
	}
	
	/**
	 * Retrieves the {@code enum} describing which type of line this is
	 * @return The {@code LineType}
	 */
	public static LineType type() {
		return LineType.DOWN_RIGHT_DIAGONAL;
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}
	
	/**
	 * Determines which squares are contained in the {@code DownRightDiagonal}s
	 */
	public static void setContainedSquares() {
		for (DownRightDiagonal diagonal : values()) {
			diagonal.containedSquares = getListOfContainedSquares(diagonal.getIndex());
			diagonal.reverseContainedSquares = reverseList(diagonal.containedSquares);
		}
	}

}
