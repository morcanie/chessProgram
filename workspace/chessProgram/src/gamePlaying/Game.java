package gamePlaying;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import representation.Board;
import representation.BoardBuilder;
import support.Constants;

/**
 * Comprises a complete game
 * @author matthewslesinski
 *
 */
public class Game {

	/** Holds the current game being played, so that it is always accessible */
	private static Game CURRENT_GAME;
	
	/** The list of positions that have occurred in this game. The last one is the current position */
	private final List<Board> positions = new LinkedList<>();
	
	/** The {@code Player} representation of the first player */
	private final Player player1;
	
	/** The {@code Player} representation of the second player */
	private final Player player2;
	
	/** Keeps track of which player has which color */
	private final Map<Color, Player> colorMapping = new EnumMap<>(Color.class);
	
	/** Builds the {@code Game} using constants passed as System properties */
	public Game() {
		this(Constants.PLAYER_1_TYPE, Constants.PLAYER_1_NAME, Constants.PLAYER_2_TYPE, Constants.PLAYER_2_NAME, Constants.BOARD_BUILDER_CONSTRUCTOR);
	}
	
	public Game(PlayerType player1Type, String name1, PlayerType player2Type, String name2, Function<String, BoardBuilder<? extends Board>> constructor) {
		this(player1Type, name1, player2Type, name2, constructor, Constants.STANDARD_START_POSITION);
	}
	
	public Game(PlayerType player1Type, String name1, PlayerType player2Type, String name2, Function<String, BoardBuilder<? extends Board>> constructor, String startPosition) {
		this.player1 = player1Type.createNew(name1);
		this.player2 = player2Type.createNew(name2);
		colorMapping.put(Color.WHITE, player1);
		colorMapping.put(Color.BLACK, player2);
		positions.add(constructor.apply(startPosition).build());
		CURRENT_GAME = this;
	}
	
	/**
	 * Retrieves the current position
	 * @return The {@code Board}
	 */
	public Board getCurrentPosition() {
		return positions.get(positions.size() - 1);
	}
	
	/**
	 * Retrieves the {@code Player} object representing the player whose move it is
	 * @return The {@code Player}
	 */
	public Player getCurrentPlayer() {
		return colorMapping.get(getCurrentPosition().whoseMove());
	}
	
	/**
	 * Retrieves the {@code Player} object representing the player whose move it is not
	 * @return The {@code Player}
	 */
	public Player getIdlePlayer() {
		return colorMapping.get(getCurrentPosition().whoseMove().getOtherColor());
	}
	
	/**
	 * pops the last 'plies' number of positions from this game
	 * @param plies The number of plies to go back
	 */
	public void undoPlies(int plies) {
		while (plies-- > 0) {
			positions.remove(positions.size() - 1);
		}
	}
	
	/**
	 * Gets the number of plies played in this game
	 * @return
	 */
	public int getPlyNumber() {
		return positions.size();
	}
	
	/**
	 * Performs the action of waiting for a player to make their move or perform some other action, and then performs that action and prints the responses
	 */
	public void takeTurn() {
		Board currentPosition = getCurrentPosition();
		Player player = getCurrentPlayer();
		UserInput nextAction = player.getFullResponse(currentPosition);
		Collection<String> responses = nextAction.performAction(this);
		printResponses(responses);
	}
	
	/**
	 * Prints the responses to the action(s) performed.
	 * @param responses The responses to print sequentially
	 */
	private void printResponses(Collection<String> responses) {
		// TODO send this to the Players instead of System.out
		responses.stream().filter(string -> string != null).forEach(System.out::println);
	}
	
	/**
	 * Pushes a position onto the game stack
	 * @param board The {@code Board} with the position to push
	 */
	public void addPosition(Board board) {
		positions.add(board);
	}
	
	/**
	 * Repeatedly takes turns for eternity. The only way out is closing the program or a user quitting
	 */
	public void play() {
		printResponses(getCurrentPlayer().getDefaultAddonInput().performAction(this));
		for (;;) {
			takeTurn();
		}
	}
	
	/**
	 * Retrieves the current game being played. This is static so it can be used anywhere that wants access to the game
	 * @return The {@code Game}
	 */
	public static Game getCurrentGame() {
		return CURRENT_GAME;
	}
	
	/**
	 * Sets up a game and plays it
	 */
	public static void startPlaying() {
		Game game = new Game();
		game.play();
	}
	
	/**
	 * Enters the program
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		startPlaying();
	}
}