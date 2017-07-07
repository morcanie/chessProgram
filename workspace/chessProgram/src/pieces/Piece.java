package pieces;

import java.util.Set;
import boardFeatures.Square;
import gamePlaying.Color;
import moves.Move;
import representation.Board;
import support.BadArgumentException;

public enum Piece {

	NONE(' '),
	WHITE_PAWN(0x2659),
	WHITE_KNIGHT(0x2658),
	WHITE_BISHOP(0x2657),
	WHITE_ROOK(0x2656),
	WHITE_QUEEN(0x2655),
	WHITE_KING(0x2654),
	BLACK_PAWN(0x265F),
	BLACK_KNIGHT(0x265E),
	BLACK_BISHOP(0x265D),
	BLACK_ROOK(0x265C),
	BLACK_QUEEN(0x265B),
	BLACK_KING(0x265A);
	
	private final PieceType type;
	private final Color color;
	private final String stringPicture;
	
	private Piece(int picture) {
		if (this.ordinal() == 0) {
			this.type = null;
			this.color = null;
		} else {
			this.type = PieceType.values()[(this.ordinal() - 1) % 6];
			this.color = Color.getColor(this.ordinal() < 7);
		}
		stringPicture = ((char) picture) + "";
	}
	
	/**
	 * Gets the type of piece this is
	 * @return the {@code PieceType} of this piece
	 */
	public PieceType getType() {
		return type;
	}
	
	/**
	 * Gets the color of this piece
	 * @return the {@code Color}
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * How to represent this {@code Piece} in bits
	 * @return The representation
	 */
	public int getBitRepresentation() {
		return this.ordinal();
	}
	
	/**
	 * Returns the piece that is represented by these bits
	 * @param bits The bits representing a piece
	 * @return The {@code Piece}
	 */
	public static Piece getPieceByBits(int bits) {
		if (bits >= values().length) {
			throw new BadArgumentException(bits, int.class, "The bit string cannot represent a piece because it is too high");
		}
		return values()[bits];
	}
	
	/**
	 * Gets the piece that has a color and a type
	 * @param color The {@code Color} of the {@code Piece}
	 * @param type The {@code PieceType} of the {@code Piece}
	 * @return The {@code Piece} with that color and type
	 */
	public static Piece getByColorAndType(Color color, PieceType type) {
		if (color == null || type == null) {
			return NONE;
		}
		return values()[color.isWhite() ? 1 : 7 + type.ordinal()];
	}
	
	/**
	 * Gets the legal moves of this piece in any situation.
	 * @param square The {@code Square} mapping to this piece in the {@code Board}
	 * @param board The {@code Board} to get the moves from
	 * @return The {@code Set} of the legal {@code Move}s
	 */
	public Set<Move> getLegalMoves(Square square, Board board) {
		if (!board.isPieceAtSquare(this, square)) {
			throw new BadArgumentException(square, Square.class, "Can't get legal moves for a different piece than what is on the provided square");
		}
		if (board.whoseMove() != this.color) {
			throw new BadArgumentException(this, Piece.class, "Can't calculate legal moves for a piece of the wrong color");
		}
		if (this == NONE) {
			throw new BadArgumentException(this, Piece.class, "Can't calculate legal moves for an empty square");
		}
		return type.getUtilityInstance().getLegalMoves(square, board);
	}
	
	@Override
	public String toString() {
		return this.stringPicture;
	}
	
}
