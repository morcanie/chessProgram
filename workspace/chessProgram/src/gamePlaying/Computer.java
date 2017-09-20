package gamePlaying;

import moves.Move;
import representation.Board;
import search.AI;
import support.Constructors;

/**
 * Holds the implementation to interface with a computer player
 * @author matthewslesinski
 *
 */
public class Computer extends Player {
	
	/** A default name for the computer */
	private static final String DEFAULT_NAME = "Computer";
	
	/** After each move by the other player, print the board */
	private static final UserInput DEFAULT_ADDON = new UserInput(InputType.PRINT_BOARD);
	
	/** The object that can perform the calculations necessary to evaluate positions for this chess engine and determine moves to make */
	private AI engine = Constructors.AI_CONSTRUCTOR.get();
	
	public Computer() {
		this(DEFAULT_NAME);
	}
	
	public Computer(String name) {
		super(name);
	}
	
	@Override
	public UserInput getNextInput(Board currentPosition) {
		Move bestMove = engine.bestMove(currentPosition);
		return new UserInput(bestMove.getMoveAsStringInContext(currentPosition));
	}

	@Override
	public UserInput getDefaultAddonInput() {
		return DEFAULT_ADDON;
	}

}
