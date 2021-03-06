package lines;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Square;
import support.BadArgumentException;
import static support.Constants.*;
import static support.UtilityFunctions.*;

/**
 * Represents a vertical column of squares on the board, of which there are 8
 * @author matthewslesinski
 */
public enum File implements Line {

	A("a", "\uff41"),
	B("b", "\uff42"),
	C("c", "\uff43"),
	D("d", "\uff44"),
	E("e", "\uff45"),
	F("f", "\uff46"),
	G("g", "\uff47"),
	H("h", "\uff48");
	
	/** The letter of this {@code File} */
	private final String readableForm;
	
	/** A widened unicode version of the readableForm */
	private final String eclipseSpecificLengthenedForm;
	
	/** The set of squares contained in this {@code File} */
	private List<Square> containedSquares;
	
	/** The set of squares contained in this {@code File}, but reversed */
	private List<Square> reverseContainedSquares;

	private File(String readableForm, String eclipseSpecificLengthenedForm) {
		this.readableForm = readableForm;
		this.eclipseSpecificLengthenedForm = eclipseSpecificLengthenedForm;
	}
			
	
	@Override
	public int getIndex() {
		return this.ordinal();
	}
	
	@Override
	public String getHumanReadableForm() {
		return readableForm;
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
		return square.getFile() == this;
	}
	
	@Override
	public int getManhattanDistanceToSquare(Square square) {
		return Math.abs(square.getFile().getIndex() - this.getIndex());
	}
	
	/**
	 * Gets the file that has the given index
	 * @param index The index to get
	 * @return The file with number one greater than the index
	 * @throws BadArgumentException If the index is not an index for a file
	 */
	public static File getByIndex(int index) throws BadArgumentException {
		return Line.getByIndex(index, type());
	}
	
	/**
	 * Gets the file that has the given letter
	 * @param readableForm The letter
	 * @return The file with the given letter or null if the input is null
	 * @throws BadArgumentException If the string is not a file
	 */
	public static File getByHumanReadableForm(String readableForm) throws BadArgumentException {
		if (readableForm == null) {
			return null;
		}
		int numericalValue = -1;
		try {
			numericalValue = readableForm.charAt(0) - 96;
			return getByIndex(numericalValue - 1);
		} catch (NumberFormatException e) {
			Line.indexOutOfRange(e, readableForm, String.class);
			return null;
		}
	}
	
	/**
	 * Retrieves the {@code enum} describing which type of line this is
	 * @return The {@code LineType}
	 */
	public static LineType type() {
		return LineType.FILE;
	}
	
	@Override
	public String toString() {
		return RUNNING_FROM_ECLIPSE ? eclipseSpecificLengthenedForm : getHumanReadableForm();
	}
    
	/**
	 * Determines which squares are contained in the {@code File}s
	 */
	public static void setContainedSquares() {
		for (File file : values()) {
			file.containedSquares = Arrays.stream(getRange(0, 8))
					.map(bind(Square::getByFileAndRankIndices, file.getIndex()))
					.collect(Collectors.toList());
			file.reverseContainedSquares = reverseList(file.containedSquares);
		}
	}
}
