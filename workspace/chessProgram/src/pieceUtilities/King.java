package pieceUtilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import boardFeatures.Side;
import boardFeatures.Square;
import gamePlaying.Color;
import lines.Direction;
import moveCalculationStructures.KingMoveSet;
import moveCalculationStructures.SquareSet;
import moves.MoveType;
import moves.ProcessedBoard;
import pieces.PieceType;
import representation.CastlingRights;
import static support.UtilityFunctions.*;

/**
 * Provides the utility method(s) for calculating a king's legal moves
 * @author matthewslesinski
 *
 */
public class King extends PieceUtility {

	public King(Color color) {
		super(color);
	}

	@Override
	protected PieceType determinePieceType() {
		return PieceType.KING;
	}

	@Override
	protected Collection<Direction> getDirectionsToMoveIn() {
		return Direction.getOutwardDirections();
	}
	
	/**
	 * Returns an empty list, unless this is a start square for the king, in which case it returns a list with the two target squares
	 * @param fromSquare The square the king is on
	 * @return A list with the castling target squares, or an empty list
	 */
	private Collection<Square> calculatePossibleCastleSquares(Square fromSquare) {
		if (fromSquare != color.getKingCastleSquare()) {
			return Collections.emptySet();
		}
		return Arrays.asList(CastlingRights.getByColorAndSide(color, Side.KINGSIDE).getTargetKingSquare(),
							 CastlingRights.getByColorAndSide(color, Side.QUEENSIDE).getTargetKingSquare());
	}
	
	@Override
	public SquareSet calculatePossibleSquaresToMoveTo(Square fromSquare) {
		return new KingMoveSet(concat(super.calculatePossibleSquaresToMoveTo(fromSquare), calculatePossibleCastleSquares(fromSquare)), fromSquare);
	}

	@Override
	public Collection<Square> calculatePossibleSquaresToThreaten(Square fromSquare) {
		return Arrays.asList(Direction.values()).stream()
				.map(direction -> fromSquare.getNeighbor(direction)).filter(square -> square != null)
				.collect(Collectors.toList());
	}
	
	@Override
	protected MoveType inferMoveType(Square start, Square end, ProcessedBoard<?> board) {
		return MoveType.inferForKings(start, end, board);
	}
	
	/**
	 * Determines if castling is capable, assuming no checks or attacks on involved squares
	 * @param end The square to be castled to
	 * @param board The context of the castling
	 * @return If castling rights haven't been disabled and the castling action doesn't have pieces in the way
	 */
	private boolean canCastle(Square end, ProcessedBoard<?> board) {
		CastlingRights rights = CastlingRights.getByColorAndSide(color, Side.getByRelation(end));
		return board.canCastle(rights) && !board.isMovementBlocked(rights.getKingSquare(), rights.getRookSquare())
				&& board.getSafeKingDestinations().contains(rights.getTargetRookSquare());
	}
	
	/**
	 * Does most of the work of determining the legal moves for the king.
	 * @param square The square the king is on
	 * @param board The context of the move
	 * @param castleValidation A function to determine if castling is allowed in the position
	 * @return The list of squares that the king can move to
	 */
	private static List<Square> getLegalSquaresToMoveTo(Square square, ProcessedBoard<?> board, BiPredicate<Square, ProcessedBoard<?>> castleValidation) {
		return board.getSafeKingDestinations().stream()
				.filter(endSquare -> (board.isNotSameColor(endSquare) &&
						(Math.abs(endSquare.getFile().getIndex() - square.getFile().getIndex()) < 2 || castleValidation.test(endSquare, board))))
				.collect(Collectors.toList());
	}
	
	@Override
	protected List<Square> getSquaresToMoveToNoChecks(Square square, ProcessedBoard<?> board) {
		return getLegalSquaresToMoveTo(square, board, this::canCastle);
		
	}

	@Override
	protected List<Square> getSquaresToMoveToOneCheck(Square square, ProcessedBoard<?> board, Square check) {
		return getSquaresToMoveToTwoChecks(square, board);
	}
	
	@Override
	protected List<Square> getSquaresToMoveToTwoChecks(Square square, ProcessedBoard<?> board) {
		return getLegalSquaresToMoveTo(square, board, (endSquare, processedBoard) -> false);
	}

}
