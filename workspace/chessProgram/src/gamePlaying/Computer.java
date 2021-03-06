package gamePlaying;

import moves.Move;
import representation.Board;
import search.AI;
import stringUtilities.MoveWriter;
import support.BadBoardException;

import static support.Constructors.*;

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
	private AI engine = AI_CONSTRUCTOR.get();
	
	public Computer() {
		this(DEFAULT_NAME);
	}
	
	public Computer(String name) {
		super(name);
	}
	
	@Override
	public UserInput getNextInput(Board currentPosition) {
		if (currentPosition.isOver()) {
			return new UserInput(InputType.QUIT);
		}
		Move bestMove = null;
		try {
			bestMove = engine.bestMove(currentPosition);
		} catch (BadBoardException e) {
			// TODO log this better
			e.printBoardTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return new UserInput(MoveWriter.getMoveAsStringInContext(bestMove, currentPosition));
	}

	@Override
	public UserInput getDefaultAddonInput() {
		return DEFAULT_ADDON;
	}
	
	

}
