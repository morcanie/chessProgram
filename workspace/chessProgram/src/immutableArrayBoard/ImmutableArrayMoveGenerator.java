package immutableArrayBoard;

import java.util.List;
import java.util.Set;

import boardFeatures.Square;
import moves.ProcessedBoard;
import moves.BasicPreProcessing;
import moves.Move;
import representation.MoveGenerator;

/**
 * Calculates the moves for a given position. This is a mutable object, and 
 * @author matthewslesinski
 *
 */
public class ImmutableArrayMoveGenerator extends MoveGenerator<ImmutableArrayBoard> {
	
	
	private ProcessedBoard<ImmutableArrayBoard> preprocessing;
	private List<Square> threateningKnights = null;
	private Square firstSquareOfCheck = null;
	private Square secondSquareOfCheck = null;
	
	
	
	@Override
	public Set<Move> calculateMoves(ImmutableArrayBoard board) {
		preprocessing = new BasicPreProcessing<>(board);
		calculateChecks();
		// TODO
		return null;
	}
	
	private void calculateChecks() {
		
	}
	
	

}
