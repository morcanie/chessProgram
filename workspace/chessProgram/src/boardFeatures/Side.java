package boardFeatures;

import lines.File;
import static support.Constants.*;

/**
 * Represents the two left/right sides of the chessboard, the kingside and the queenside.
 * @author matthewslesinski
 *
 */
public enum Side {

	KINGSIDE,
	QUEENSIDE;
	
	/** A boolean representation distinguisher for these enums */
	private final boolean isKingside = this.ordinal() == 0 ? true : false;
	
	/** The {@code File} that is on the edge of this side, and the rook from this side starts on */
	private final File castlingRookFile = isKingside ? KINGSIDE_ROOK_START_FILE : QUEENSIDE_ROOK_START_FILE;
	
	/**
	 * Returns whether this side is the kingside or queenside
	 * @return Whether its the kingside
	 */
	public boolean isKingside() {
		return isKingside;
	}
	
	/**
	 * Gets the {@code File} that the rook on this side starts on
	 * @return The rook {@code File}
	 */
	public File getRookFile() {
		return castlingRookFile;
	}
	
	/**
	 * Sees whether the given square is on this side
	 * @param square The given square
	 * @return Whether it's on this side
	 */
	public boolean isOnThisSide(Square square) {
		int fileOrdinal = square.getFile().ordinal();
		return fileOrdinal >= (isKingside ? 4 : 0) &&
				fileOrdinal <= (isKingside ? 7 : 3);
	}
	
	/**
	 * Retrieves the {@code Side} based on if it's the kingside or not
	 * @param isKingside If the retrieved {@code Side} should be KINGSIDE
	 * @return The {@code Side}
	 */
	public static Side getByBoolean(boolean isKingside) {
		return isKingside ? KINGSIDE : QUEENSIDE;
	}
	
	/**
	 * Gets the {@code Side} containing this square
	 * @param square The square on the side
	 * @return The {@code Side}
	 */
	public static Side getByRelation(Square square) {
		return getByBoolean(square.getFile().getIndex() > 3);
	}
}
