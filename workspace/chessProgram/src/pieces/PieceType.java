package pieces;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import boardFeatures.Direction;


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
	private final static PieceType[] lineMovers = {BISHOP, ROOK, QUEEN};
	
	/**
	 * Holds the utility methods calculating the legal moves for this piece
	 */
	private final PieceUtility utilityInstance;
	private final Set<Direction> longDistanceDirections;
	
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
		List<Direction> possibleLongDistanceDirections = (utilityInstance instanceof LineMover) ? ((LineMover) utilityInstance).getMovementDirections() : Collections.emptyList();
		this.longDistanceDirections = possibleLongDistanceDirections.isEmpty() ? EnumSet.noneOf(Direction.class) : EnumSet.copyOf(possibleLongDistanceDirections);
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
	 * Gets the piece type given the corresponding letter in algebraic notation.
	 * @param letter: The letter used to represent the piece.
	 * @return The piece type corresponding to the input letter.
	 */
	public static PieceType getByLetter(String letter) {
		switch (letter) {
		case "P": return PieceType.PAWN;
		case "N": return PieceType.KNIGHT;
		case "B": return PieceType.BISHOP;
		case "R": return PieceType.ROOK;
		case "Q": return PieceType.QUEEN;
		case "K": return PieceType.KING;
		default: throw new IllegalArgumentException(String.format("Input string \"%s\" does not correspond to a piece.", letter));
		}
	}
	
	/**
	 * Detemrines if this piece can move far across the board in one turn.
	 * @return true iff it can
	 */
	public boolean isLongRange() {
		return this.ordinal() >= 2 && this.ordinal() <= 4;
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
	 * Returns an array of the pieces that move long range
	 * @return The array of {@code PieceType}s
	 */
	public static PieceType[] getLineMovers() {
		return lineMovers;
	}
	
	/**
	 * Determines if this piece type can move far in a particular direction
	 * @param dir The direction
	 * @return true iff it can
	 */
	public boolean movesFarInDirection(Direction dir) {
		return longDistanceDirections.contains(dir);
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
