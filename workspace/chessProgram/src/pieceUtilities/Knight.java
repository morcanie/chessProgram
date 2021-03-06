package pieceUtilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import boardFeatures.Square;
import moveCalculationStructures.EvenlySpacedCircle;
import moveCalculationStructures.EvenlySpacedCircleImpl;
import moveCalculationStructures.Ring;
import pieces.PieceType;

/**
 * Provides the utility method(s) for calculating a knight's legal moves
 * @author matthewslesinski
 *
 */
public class Knight extends PieceUtility{
	
	public Knight() {
		super();
	}
	
	/** The offsets between a knight's current square and the squares it can move to */
	public static final List<List<Integer>> KNIGHT_MOVE_OFFSETS = Arrays.asList(
		Arrays.asList(2, 1),
		Arrays.asList(2, -1),
		Arrays.asList(1, -2),
		Arrays.asList(-1, -2),
		Arrays.asList(-2, -1),
		Arrays.asList(-2, 1),
		Arrays.asList(-1, 2),
		Arrays.asList(1, 2)
	);

	@Override
	protected PieceType determinePieceType() {
		return PieceType.KNIGHT;
	}

	@Override
	public Collection<Square> calculatePossibleSquaresToThreaten(Square fromSquare) {
		List<Square> possibleJumps = Knight.KNIGHT_MOVE_OFFSETS.stream().map(list -> {
			int fileOffset = list.get(0);
			int rankOffset = list.get(1);
			return fromSquare.getSquareByOffset(fileOffset, rankOffset);
		}).filter(square -> square != null).collect(Collectors.toList());
		EvenlySpacedCircle circle = new EvenlySpacedCircleImpl(fromSquare, possibleJumps);
		return new Ring.OfSquares(circle);
	}
}
