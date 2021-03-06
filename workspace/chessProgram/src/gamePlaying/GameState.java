package gamePlaying;

import representation.Board;

/**
 * Represents the possible states for a {@code Game}
 * @author matthewslesinski
 */
public enum GameState {

	STILL_GOING("*", 0),
	STALEMATE("1/2-1/2", 0),
	WHITE_WINS("1-0", Double.POSITIVE_INFINITY),
	BLACK_WINS("0-1", Double.NEGATIVE_INFINITY),
	;
	
	/** How the state should be represented as a string in pgns */
	private final String pgnRepresentation;
	
	/** The evaluation to supply for this state */
	private final double evaluation;
	
	private GameState(String pgnRepresentation, double evaluation) {
		this.pgnRepresentation = pgnRepresentation;
		this.evaluation = evaluation;
	}
	
	/**
	 * Retrieves the evaluation for this state
	 * @return The evaluation
	 */
	public double getEvaluation() {
		return evaluation;
	}
	
	@Override
	public String toString() {
		return pgnRepresentation;
	}
	
	/**
	 * Gets the state a board is in
	 * @param board The board to get the state for
	 * @return The {@code GameState}
	 */
	public static GameState getByBoard(Board board) {
		if (board.isOver()) {
			if (board.isInCheck()) {
				return board.whoseMove().isWhite() ? BLACK_WINS : WHITE_WINS;
			}
			return STALEMATE;
		}
		return STILL_GOING;
	}
}
