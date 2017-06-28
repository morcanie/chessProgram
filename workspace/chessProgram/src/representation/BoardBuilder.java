package representation;

import boardFeatures.File;
import boardFeatures.Square;
import gamePlaying.Color;
import pieces.Piece;

public abstract class BoardBuilder<B extends Board> {

	
	protected BoardBuilder() {};
	
	/**
	 * Ensures that all {@code BoardBuilder}s can be instantiated from an array of {@code Piece}s
	 * @param pieces an array of {@code Piece}s
	 * @param whoToMove The {@code Color} of the player whose turn it is in this position
	 */
	protected BoardBuilder(Piece[] pieces, Color whoToMove) {
		for (Square square : Square.values()) {
			withPieceAtSquare(square.getValueOfSquareInArray(pieces), square);
		}
		withColorToMove(whoToMove);
	}
	
	/**
	 * Sets the color for this board to the given {@code Color}
	 * @param color the {@code Color} to set
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withColorToMove(Color color);
	
	/**
	 * Puts a piece on a square
	 * @param piece The piece to put
	 * @param square The square for the piece
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withPieceAtSquare(Piece piece, Square square);
	
	/**
	 * Sets the given {@code CastlingRight} to either allowed or not, depending on enabled
	 * @param castlingRight The right to set
	 * @param enabled Whether or not that type of castling should be allowed
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withCastlingRight(CastlingRights castlingRight, boolean enabled);
	
	/**
	 * Sets the en passant bit representation to the given file and a 1 in front if enabled
	 * @param file If the last move was a pawn push 2 squares ahead, the file of that pawn, else null
	 * @return This builder
	 */
	public abstract BoardBuilder<B> withEnPassant(File file);
	
	/**
	 * Builds the board from this builder
	 * @return The {@code ImmutableArrayBoard} instance
	 */
	public abstract B build();
}
