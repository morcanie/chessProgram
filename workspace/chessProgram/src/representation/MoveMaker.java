package representation;

import boardFeatures.Side;
import boardFeatures.Square;
import gamePlaying.Color;
import moves.Move;
import pieces.Piece;
import pieces.PieceType;

/**
 * Instances of this class are intended to hold the logic for performing the legal moves for the appropriate type of {@code Board}.
 * @author matthewslesinski
 *
 * @param <B> The type of Board that this performs moves for
 */
public abstract class MoveMaker<B extends Board> {

	/**
	 * Retrieves a new builder to build the new board with
	 * @param board The board to seed the builder with
	 * @return The builder
	 */
	protected abstract BoardBuilder<B> getNewBuilderFromBoard(B board);
	
	/**
	 * Performs a move in a given position.
	 * @param move The move to perform
	 * @param board The board to perform the move on
	 * @return The resulting board. If the move is illegal, null should be returned
	 */
	public B performMove(Move move, B board) {
		if (!board.getLegalMoves().contains(move)) {
			return null;
		}
		BoardBuilder<B> builder = getNewBuilderFromBoard(board);
		// reset the en passant permissions
		builder.withEnPassant(null);
		Color movingColor = move.getMovingColor();
		switch (move.getMovingPiece()) {
		case PAWN:
			// Pawns have special move possibilities
			if (move.isEnPassant()) {
				// Note that these actions don't make use of move.getEndSquare(), since, even though at this point it should be equal
				// to the destination square, it is more implementation agnostic to not refer to it
				switchSquares(builder, PieceType.PAWN, move.getStartSquare(), move.getEnPassantDestinationSquare(), movingColor);
				// Remove the captured pawn
				builder.withPieceAtSquare(Piece.NONE, move.getEnPassantCaptureSquare());
			} else if (move.isPromotion()) {
				// Replace the moving pawn with the promotion piece
				switchSquares(builder, move.getPromotionPiece(), move.getStartSquare(), move.getEndSquare(), movingColor);
			} else {
				// If the pawn is moving two squares forwards, record the en passant might be possible in the resulting board
				if (Math.abs(move.getStartSquare().getRank().getIndex() - move.getEndSquare().getRank().getIndex()) == 2) {
					builder.withEnPassant(move.getStartSquare().getFile());
				}
				switchSquares(builder, move);
			}
			break;
		case KING:
			if (move.isCastle()) {
				// Moving the king will get done below, but the rook needs to get moved too
				CastlingRights relevantRight = CastlingRights.getByColorAndSide(movingColor, Side.getByRelation(move.getEndSquare()));
				switchSquares(builder, PieceType.ROOK, relevantRight.getRookSquare(), relevantRight.getTargetRookSquare(), movingColor);
			}
			// roll on down
		case ROOK:
			// For kings or rooks, make sure the piece can no longer castle
			CastlingRights.getAffectedRightsByColorAndSquare(movingColor, move.getStartSquare())
				.forEach(right -> builder.withCastlingRight(right, false));
		default:
			// perform the move
			switchSquares(builder, move);
			break;
		}
		// If we're capturing a rook, we need to get rid of the castling rights involving that rook. Although it may seem that this is
		// unimportant, since once the rook's gone, castling won't be able to be done in that direction, it's actually important because
		// otherwise two identical positions with the same move set would have different castling rights, which could mess up hashing
		if (move.isCapture() && move.getCapturedPiece() == PieceType.ROOK) {
			CastlingRights.getAffectedRightsByColorAndSquare(movingColor.getOtherColor(), move.getEndSquare())
				.forEach(right -> builder.withCastlingRight(right, false));
		}
		// change whose move it is and record the last move
		return builder.withLastMove(move).withColorToMove(movingColor.getOtherColor()).build();
	}	
	
	/**
	 * Does the basic logic of the move, by switching the square the moving piece starts on with the destination square
	 * @param builder The builder to record the move in
	 * @param move The move being made
	 * @return The builder
	 */
	protected BoardBuilder<B> switchSquares(BoardBuilder<B> builder, Move move) {
		return switchSquares(builder, move.getMovingPiece(), move.getStartSquare(), move.getEndSquare(), move.getMovingColor());
	}
	
	/**
	 * Gets rid of the piece at the start square and puts the moving piece at the end square in the builder
	 * @param builder The builder
	 * @param movingPiece The moving piece
	 * @param start The square it starts on
	 * @param end The square to put the moving piece on
	 * @param whoseMove The color of the player making the move
	 * @return The builder
	 */
	protected BoardBuilder<B> switchSquares(BoardBuilder<B> builder, PieceType movingPiece, Square start, Square end, Color whoseMove) {
		return builder
				.withPieceAtSquare(Piece.getByColorAndType(whoseMove, movingPiece), end)
				.withPieceAtSquare(Piece.NONE, start);
	}
}