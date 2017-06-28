package pieces;

import java.util.function.Function;


/**
 * 
 * @author matthewslesinski
 *
 */
public enum PieceType {

	PAWN("pawn", Pawn::new),
	KNIGHT("knight", Knight::new),
	BISHOP("bishop", Bishop::new),
	ROOK("rook", Rook::new),
	QUEEN("queen", Queen::new),
	KING("king", King::new);
	
	private final String readableForm;
	private final static PieceType[] promotionPieces = {KNIGHT, BISHOP, ROOK, QUEEN};
	
	/**
	 * Holds the utility methods calculating the legal moves for this piece
	 */
	private final PieceUtility utilityInstance;
	
	/**
	 * Describes a particular type of piece.
	 * @param readableForm How to describe this piece type in plain english
	 * @param constructor A constructor for the utility class for this type of piece. A constructor
	 * is an argument here because the utility class can't be instantiated earlier, since its constructor
	 * takes this {@code PieceType} as an argument.
	 */
	private PieceType(String readableForm, Function<PieceType, PieceUtility> constructor) {
		this.readableForm = readableForm;
		utilityInstance = constructor.apply(this);
	}
	
	
	/**
	 * Gets the piece type given by the index
	 * @param index The index to get
	 * @return The {@code PieceType} at that index in the array of {@code PieceType}s
	 */
	public static PieceType getByIndex(int index) {
		return values()[index];
	}
	
	/**
	 * Determines if the piece can be promoted to
	 * @return true iff so
	 */
	public boolean isPromotionPiece() {
		return this.ordinal() >= 1 && this.ordinal() <= 4;
	}
	
	/**
	 * Returns an array of the pieces that a pawn can promote to
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getPromotionPieces() {
		return promotionPieces;
	}
	
	
	/**
	 * Gets the utility class instance for this type
	 * @return The {@code PieceUtility}
	 */
	public PieceUtility getUtilityInstance() {
		return utilityInstance;
	}
	
	@Override
	public String toString() {
		return this.readableForm;
	}
}
