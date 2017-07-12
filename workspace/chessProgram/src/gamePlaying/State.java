package gamePlaying;

import java.util.Set;

import moves.Move;
import representation.Board;

/**
 * Represents a game state at a given point in time
 * @author matthewslesinski
 */
public interface State {

	/**
	 * Returns a list of the legal moves for the current position
	 * @return The list of moves
	 */
	public Set<Move> getLegalMoves();
	
	/**
	 * Outputs the color of the player whose move it is
	 * @return The color
	 */
	public Color whoseMove();
	
	/**
	 * Whether or not the game has ended
	 * @return a boolean stating if it is over
	 */
	public boolean isOver();
	
	/**
	 * Transitions the state to the next state when given a move
	 * @param move The move to make
	 * @return The next state
	 */
	public Board performMove(Move move);
	
	
}