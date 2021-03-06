package immutableArrayBoard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import boardFeatures.Square;
import gamePlaying.Color;
import moves.ProcessedBoard;
import pieces.Piece;
import pieces.PieceType;
import moves.Move;
import representation.MoveGenerator;
import representation.Board;
import support.BadArgumentException;
import static support.Constructors.*;
import static support.UtilityFunctions.*;

/**
 * Calculates the moves for a given position. This is a mutable object, and so it could technically be used to calculate moves
 * for multiple boards. But if so, proceed with caution
 * @author matthewslesinski
 *
 */
public class ImmutableArrayMoveGenerator extends MoveGenerator<ImmutableArrayBoard> {
	
	/** Preprocessing that calculates the import details about the board that are necessary for move calculation */
	private ProcessedBoard<Board> preprocessing;
	/** The {@code Set} of {@code Square}s containing {@code Piece}s that are giving check */
	private Set<Square> checks;
	/** The list of legal moves */
	private List<Move> moves;
	/** The {@code Color} that is to move */
	private Color toMove;
	/** The {@code Square} containing the king of the color to move */
	private Square kingSquare;
	
	/**
	 * A function to get all of the squares that have a piece of the type provided
	 */
	private Function<PieceType, List<Square>> getSquaresForPiecesOfThisColor;
	
	
	@Override
	public Set<Move> calculateMoves(ImmutableArrayBoard board) {
		preprocessing = PRE_PROCESSING_CONSTRUCTOR.apply(board);
		preprocessing.calculateKingSafety();
		checks = preprocessing.whoIsAttackingTheKing();
		toMove = preprocessing.whoseMove();
		moves = new LinkedList<>();
		getSquaresForPiecesOfThisColor =
				bind(Piece::getByColorAndType, toMove).andThen(preprocessing::getListOfSquaresForPiece);
		kingSquare = preprocessing.getListOfSquaresForPiece(Piece.getByColorAndType(toMove, PieceType.KING)).get(0);
		realizeMoves();
		return MOVESET_CONSTRUCTOR.apply(moves);
	}
	
	/**
	 * Actually calculates the moves that are legal for all the pieces
	 */
	private void realizeMoves() {
		Collection<Square> pieceSquares = checks.size() > 1 ? Collections.singleton(kingSquare) :
			concat(Arrays.stream(PieceType.values()).map(getSquaresForPiecesOfThisColor).collect(Collectors.toList()));
		pieceSquares.stream().map(square -> preprocessing.getPieceAtSquare(square).getLegalMoves(square, preprocessing)).forEach(moves::addAll);
	}

	@Override
	public boolean isInCheck() {
		if (checks == null) {
			throw new BadArgumentException(null, Set.class, "Expected moves to already have been calculated");
		}
		return !checks.isEmpty();
	}
}
