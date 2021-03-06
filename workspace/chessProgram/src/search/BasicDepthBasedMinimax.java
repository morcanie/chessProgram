package search;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import moves.Move;
import representation.Board;
import static support.UtilityFunctions.*;

/**
 * An implementation of the minimax algorithm. It uses a recursive helper function to evaluate at various depths. Therefore, it explores
 * the game tree in a depth first search way. Furthermore, there is a {@code MAX_PLIES} static value in this class that determines
 * how many plies ahead the algorithm will look before terminating. Therefore, the algorithm is seriously susceptible to the horizon effect.
 * 
 * The minimax algorithm works by looking through all the boards in the game tree up to {@code MAX_PLIES} depth and assuming that each player
 * will play the move the algorithm determines as best, and then tries to maximize (for white) or minimize (for black) the evaluation of the
 * move the algorithm picks.
 * 
 * Lastly, this implementation makes use of a transposition table, which is reset upon each call to begin the minimax algorithm, that keeps
 * track of boards' evaluations that have already been calculated. It only stores boards as the {@code long} form of their hashcode, so
 * it is susceptible to retrieving an incorrect evaluation because of boards having the same hashcode. The reason that the table is reset
 * on each call to the algorithm is because otherwise previously evaluated boards that are subject to the horizon effect will essentially
 * have evaluations with the horizon moved even closer.
 * @author matthewslesinski
 *
 */
public class BasicDepthBasedMinimax implements AI {

	/** How many plies down the road to look */
	private static final int MAX_PLIES = 3;
	
	/** A comparator for doubles that describes their natural ordering */
	private static final Comparator<Double> NATURAL_ORDER = Comparator.naturalOrder();

	/** A comparator for doubles that describes the reverse of their natural ordering */
	private static final Comparator<Double> REVERSE_ORDER = NATURAL_ORDER.reversed();

	/** A table to hold evaluations for boards that have already been evaluated */
	private static Map<Long, Double> TRANSPOSITION_TABLE = null;
	
	@Override
	public Move bestMove(Board board) {
		TRANSPOSITION_TABLE = new HashMap<>();
		Comparator<Double> comparator = board.whoseMove().isWhite() ? NATURAL_ORDER : REVERSE_ORDER;
		Function<Move, Double> translator = bindAtEnd(BasicDepthBasedMinimax::bestMoveHelper, MAX_PLIES).compose(board::performMove);
		BinaryOperator<Move> operator = (move1, move2) -> argmax(comparator, translator, move1, move2);
		return board.getLegalMoves().stream().reduce(operator).get();
	}
	
	/**
	 * Performs much of the recursive calls for evaluating boards in the game tree. If the board has already been evaluated, that
	 * evaluation is returned. Otherwise, if the maximum depth has been reached,
	 * the boards get evaluated. Otherwise, it looks for the optimal branch for the moving player. Either way, whatever is
	 * returned gets put in the transposition table as the evaluation for the provided board. 
	 * @param board The board being evaluated
	 * @param pliesLeft The number of plies left until the maximum depth has been reached. That maximum depth is when this value is 1
	 * @return The evaluation for the provided board
	 */
	private static double bestMoveHelper(Board board, int pliesLeft) {
		if (TRANSPOSITION_TABLE.containsKey(board.getHashCode())) {
			return TRANSPOSITION_TABLE.get(board.getHashCode());
		}
		if (pliesLeft == 1 || board.isOver()) {
			return recordEvaluation(board, board.evaluate());
		}
		BinaryOperator<Double> optimumFinder = board.whoseMove().isWhite() ? Math::max : Math::min;
		return recordEvaluation(board, board.getLegalMoves().stream()
				.map(bindAtEnd(BasicDepthBasedMinimax::bestMoveHelper, pliesLeft - 1).compose(board::performMove))
				.reduce(optimumFinder).get());
	}
	
	/**
	 * Records a board's evaluation in the transposition table and, for convenience, returns that evaluation
	 * @param board The board with the evaluation
	 * @param evaluation The board's evaluation
	 * @return The evaluation
	 */
	private static double recordEvaluation(Board board, double evaluation) {
		TRANSPOSITION_TABLE.put(board.getHashCode(), evaluation);
		return evaluation;
	}
	

}
