package pieceUtilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Square;
import gamePlaying.Color;
import lines.Direction;
import moveCalculationStructures.SquareSet;
import moves.Move;
import moves.MoveType;
import moves.ProcessedBoard;
import pieces.Piece;
import pieces.PieceType;
import support.BadArgumentException;
import static support.UtilityFunctions.*;

/**
 * Provides the utility method(s) for calculating a pawn's legal moves
 * @author matthewslesinski
 *
 */
public class Pawn extends PieceUtility {

	
	public Pawn(Color color) {
		super(color);
	}

	@Override
	protected PieceType determinePieceType() {
		return PieceType.PAWN;
	}
	
	@Override
	protected Collection<Direction> getDirectionsToMoveIn() {
		return Arrays.asList(color.getLeftPawnCaptureDirection(), color.getRightPawnCaptureDirection(), color.getPawnPushDirection());
	}
	
	@Override
	protected Collection<Direction> getDirectionsToThreatenIn() {
		return Arrays.asList(color.getLeftPawnCaptureDirection(), color.getRightPawnCaptureDirection());
	}

	
	/**
	 * Calculates the squares a pawn can move to without capturing, in all possible situations
	 * @param fromSquare The square it starts from
	 * @return The collection of the possible squares
	 */
	private Collection<Square> calculatePossibleNonCaptureMoves(Square fromSquare) {
		List<Square> possibleSquares = new LinkedList<>();
		Direction forwards = color.getPawnPushDirection();
		Square pushSquare = fromSquare.getNeighbor(forwards);
		possibleSquares.add(pushSquare);
		if (fromSquare.getRank() == color.getPawnStartRank()) {
			possibleSquares.add(pushSquare.getNeighbor(forwards));
		}
		// En passants, while being calculated, use the enemy pawn's square as the destination
		if (fromSquare.getRank() == color.getEnPassantCaptureRank()) {
			possibleSquares.addAll(Arrays.asList(fromSquare.getNeighbor(Direction.RIGHT), fromSquare.getNeighbor(Direction.LEFT)));
		}
		return possibleSquares.stream().filter(square -> square != null).collect(Collectors.toList());
	}
	
	/**
	 * Determines what direction a pawn's move counts as going in. This should be the same for all moves as normal,
	 * except for en passants, their end square is going to be included as the pawn they are capturing, but their
	 * direction should be counted as the motion that the pawn moves in
	 * @param center The pawn's start square
	 * @param square The pawn's end square
	 * @return The direction from center to square, or if en passant, the direction from center to the square the pawn ends on
	 */
	private Direction getDirectionFromCenter(Square center, Square square) {
		Direction trueDirection = center.getDirectionToSquare(square);
		if (trueDirection.getRankDelta() == 0) {
			return Direction.getByDeltas(trueDirection.getFileDelta(), color.getPawnPushDirection().getRankDelta());
		}
		return trueDirection;
	}
	
	@Override
	public SquareSet calculatePossibleSquaresToMoveTo(Square fromSquare) {
		return new SquareSet(concat(super.calculatePossibleSquaresToMoveTo(fromSquare), calculatePossibleNonCaptureMoves(fromSquare)),
				fromSquare, bind(this::getDirectionFromCenter, fromSquare));
	}

	@Override
	public Collection<Square> calculatePossibleSquaresToThreaten(Square fromSquare) {
		return Arrays.asList(color.getLeftPawnCaptureDirection(), color.getRightPawnCaptureDirection()).stream()
				.map(direction -> fromSquare.getNeighbor(direction)).filter(square -> square != null)
				.collect(Collectors.toList());
	}

	
	@Override
	protected List<Move> convertSquaresToMoves(Square start, List<Square> squares, ProcessedBoard<?> board) {
		if (start.getRank() != color.getOtherColor().getPawnStartRank()) {
			return super.convertSquaresToMoves(start, squares, board);
		}
		PieceType[] promotionPieces = PieceType.getPromotionPieces();
		List<Move> moves = new LinkedList<>();
		
		squares.forEach(moveSquare -> {
			MoveType type = inferMoveType(start, moveSquare, board);
			if (type != MoveType.PROMOTION && type != MoveType.PROMOTION_WITH_CAPTURE) {
				throw new BadArgumentException(type, MoveType.class, "A pawn at its seventh rank needs to promote");
			}
			Arrays.stream(promotionPieces)
				.map(promotionPiece -> convertSquareToMove(start, moveSquare, board, type, promotionPiece))
				.forEach(moves::add);
		});
		return moves;
	}
	
	@Override
	protected MoveType inferMoveType(Square start, Square end, ProcessedBoard<?> board) {
		return MoveType.inferForPawns(start, end, board);
	}
	
	/**
	 * Determines if the pawn move from start to end is legal
	 * @param start The start square
	 * @param end The end square
	 * @param board The context for the move
	 * @return If the move does not have a capture and stays on a file, does have a capture and changes files, or is a legal en passant
	 */
	private boolean validatePawnMove(Square start, Square end, ProcessedBoard<?> board) {
		if (start.getRank() == end.getRank()) {
			return end.getFile() == board.getEnPassantFile() && !board.isEnPassantPinned(start.getFile());
		}
		Piece occupant = board.getPieceAtSquare(end);
		boolean sameFile = (start.getFile() == end.getFile());
		return board.isEmptySquare(end) ? sameFile : (!sameFile && occupant.getColor() != color);
	}
	
	@Override
	protected boolean isMovementAllowed(Square start, Square end, ProcessedBoard<?> board) {
		return validatePawnMove(start, end, board) && !board.isMovementBlocked(start, end);
	}
	
	@Override
	protected boolean addSquareToListOfMoves(Square start, Square end, ProcessedBoard<?> board, List<Square> list) {
		boolean shouldBreak = !board.isEmptySquare(end) && end.getFile() == start.getFile();
		if (!shouldBreak && validatePawnMove(start, end, board)) {
			list.add(end);
		}
		return shouldBreak;
	}
}
