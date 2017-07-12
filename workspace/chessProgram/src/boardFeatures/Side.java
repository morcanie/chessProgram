package boardFeatures;

public enum Side {

	KINGSIDE,
	QUEENSIDE;
	
	private final boolean isKingside = this.ordinal() == 0 ? true : false;
	
	private final File castlingRookFile = isKingside ? File.H : File.A;
	
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
}