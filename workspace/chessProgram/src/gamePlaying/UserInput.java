package gamePlaying;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builds the input object that interfaces with the games
 */
public class UserInput {

	/** The default input to pass to inputs supplied by the system instead of directly by the user */
	private static final String DEFAULT_INPUT = "";
	
	/** An action to perform in place of the expected one when none are returned */
	private static final Function<Game, String> NO_ACTION = game -> "The input provided does not correspond to a type of action. Try again.";
	
	/** The types of the input compresed in here */
	private final Collection<InputType> types;
	
	/** The set of actions to perform for this input */
	private final Collection<Function<Game, String>> actions;
	
	public UserInput(String input) {
		this.types = new LinkedList<>();
		this.actions = new LinkedList<>();
		InputType type = InputType.getInputType(input);
		this.types.add(type);
		this.actions.add(type != null ? type.getAction(input) : NO_ACTION);
		
	}
	
	public UserInput(InputType... types) {
		this.types = new LinkedList<>();
		this.types.addAll(Arrays.asList(types));
		this.actions = new LinkedList<>();
		this.actions.addAll(Arrays.asList(types).stream().map(type -> type.getAction(DEFAULT_INPUT)).collect(Collectors.toList()));
	}
	
	/**
	 * Performs the actions specified in this set of inputs
	 * @param game The {@code Game} to perform the action in
	 * @return The string responses to send back
	 */
	public Collection<String> performAction(Game game) {
		return actions.stream().map(action -> action.apply(game)).collect(Collectors.toList());
	}
	
	/**
	 * Performs the actions specified in this set of inputs, and assumes the Game to do it on is the current one
	 * @return The string responses to send back
	 */
	public Collection<String> performAction() {
		return performAction(Game.getCurrentGame());
	}
	
	/**
	 * Combines two {@code UserInput}s, so that they get called in an existing order
	 * @param followUp The set of inputs to chain after this current one gets performed}
	 * @return A new {@code UserInput} with the label(s)/power(s) passed on
	 */
	public UserInput chain(UserInput followUp) {
		this.types.addAll(followUp.types);
		this.actions.addAll(followUp.actions);
		return this;
	}
}
