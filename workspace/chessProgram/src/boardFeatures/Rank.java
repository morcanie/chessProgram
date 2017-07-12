package boardFeatures;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import support.BadArgumentException;
import support.UtilityFunctions;

/**
 * Represents a horizontal row of squares on the board, of which there are 8
 * @author matthewslesinski
 */
public enum Rank implements Line {

	ONE("1"),
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8");
	
	private String readableForm;
	private static final Direction directionOfLine = Direction.RIGHT;
	
	private Rank(String readableForm) {
		this.readableForm = readableForm;
	}
	
	/**
	 * The set of squares contained in this {@code Rank}
	 */
	private final List<Square> containedSquares = Arrays.asList(File.values()).stream()
			.map(file -> Square.getByFileAndRank(file, this))
			.collect(Collectors.toList());
	
	/**
	 * The set of squares contained in this {@code File}, but reversed
	 */
	private final List<Square> reverseContainedSquares = UtilityFunctions.reverseList(containedSquares);
	
	@Override
	public int getIndex() {
		return this.ordinal();
	}
	
	@Override
	public String getHumanReadableForm() {
		return readableForm;
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
		return square.getRank() == this;
	}

	@Override
	public int getSpotInLine(Square square) {
		return square.getFile().getIndex();
	}
	
	@Override
	public Direction getForwardDirection() {
		return directionOfLine;
	}
	
	/**
	 * Gets the {@code Rank} with the given ordinal/index
	 * @param index The index to get
	 * @return The {@code Rank}
	 */
	public static Rank getByIndex(int index) {
		return Line.getByIndex(index, Rank.class);
	}
	
	/**
	 * Gets the rank that has the given number
	 * @param readableForm The number
	 * @return The rank with the given number
	 * @throws BadArgumentException If the string is not a rank
	 */
	public static Rank getByHumanReadableForm(String readableForm) throws BadArgumentException {
		int numericalValue = -1;
		try {
			numericalValue = Integer.parseInt(readableForm);
			return getByIndex(numericalValue - 1);
		} catch (NumberFormatException e) {
			Line.indexOutOfRange(e, readableForm, String.class);
			// Should never get here because indexOutOfRange always throws an exception
			return null;
		}
	}
	
	@Override
	public String toString() {
		return getHumanReadableForm();
	}
    
}