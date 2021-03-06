package support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import gamePlaying.PlayerType;
import lines.Direction;
import lines.File;

import static support.UtilityFunctions.*;

/**
 * Holds a bunch of constants
 * @author matthewslesinski
 *
 */
public class Constants {

	/** Whether or not the instance of this program is running from eclipse */
	public static final boolean RUNNING_FROM_ECLIPSE = Boolean.getBoolean("eclipse");
	
	/** If the user wants the legal moves to be printed */
	public static final boolean SHOULD_PRINT_MOVES = Boolean.getBoolean("printMoves");
	
	/** The {@code PlayerType} to use for the first player */
	public static final PlayerType PLAYER_1_TYPE = PlayerType.getByIdentifier(System.getProperty("player1"));

	/** The {@code PlayerType} to use for the second player */
	public static final PlayerType PLAYER_2_TYPE = PlayerType.getByIdentifier(System.getProperty("player2"));

	/** The name to use for the first player */
	public static final String PLAYER_1_NAME = System.getProperty("player1Name");
	
	/** The name to use for the second player */
	public static final String PLAYER_2_NAME = System.getProperty("player2Name");
	
	/** A buffered reader used to read from System.in. This must be closed on exit (such as in UserActions.java) */
	public static final BufferedReader INPUT_READER = new BufferedReader(new InputStreamReader(System.in));
	
	/** The newline character in string form */
	public static final String NEWLINE = "\n";
	
	/** The escape character (ESC, not \) in character form */
	public static final String ESCAPE_CHARACTER = Character.toString((char) 27);
	
	/**
	 * The ansi string to color code the background for dark squares. Ironically, this code colors the background
	 * what is supposed to be white, but it looks darker.
	 */
	public static final String ANSI_DARK_SQUARE = "[47m";
	
	/**
	 * The ansi code to reset the current set of attributes. This can be used to get rid of any previously set
	 * color codes.
	 */
	public static final String ANSI_RESET_ATTRIBUTES = "[0m";
	
	/** A space */
	public static final String SINGLE_SPACE = " ";
	
	/** Two spaces */
	public static final String DOUBLE_SPACE = "  ";

	/** Three spaces */
	public static final String TRIPLE_SPACE = "   ";
	
	/** A dash */
	public static final String DASH = "-";
	
	/** A slash */
	public static final String SLASH = "/";
	
	/** The symbol for check */
	public static final String CHECK_SYMBOL = "+";
	
	/** The symbol for checkmate */
	public static final String CHECKMATE_SYMBOL = "#";
	
	/** The value to put for a required pgn tag if no value is known */
	public static final String UNKNOWN_PGN_VALUE = "?";
	
	/** The {@code File} the king starts on */
	public static final File KING_START_FILE = File.E;

	/** The {@code File} the kingside rook starts on */
	public static final File KINGSIDE_ROOK_START_FILE = File.H;

	/** The {@code File} the queenside rook starts on */
	public static final File QUEENSIDE_ROOK_START_FILE = File.A;
	
	public static final String STANDARD_START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	
	/** The directions a bishop can move */
	public static final List<Direction> BISHOP_DIRECTIONS = Arrays.asList(Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT, Direction.UP_LEFT);
	
	/** The directions a rook can move */
	public static final List<Direction> ROOK_DIRECTIONS = Arrays.asList(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
	
	/** The directions a queen can move */
	public static final List<Direction> QUEEN_DIRECTIONS = concat(BISHOP_DIRECTIONS, ROOK_DIRECTIONS);
	
	/** The conventional estimate for a pawn's material worth */
	public static final double ESTIMATED_PAWN_MATERIAL_VALUE = 1.;
	
	/** The conventional estimate for a knight's material worth */
	public static final double ESTIMATED_KNIGHT_MATERIAL_VALUE = 3.;
	
	/** The conventional estimate for a bishop's material worth */
	public static final double ESTIMATED_BISHOP_MATERIAL_VALUE = 3.;
	
	/** The conventional estimate for a rook's material worth */
	public static final double ESTIMATED_ROOK_MATERIAL_VALUE = 5.;
	
	/** The conventional estimate for a queen's material worth */
	public static final double ESTIMATED_QUEEN_MATERIAL_VALUE = 9.;
	
	/** The conventional estimate for a king's material worth */
	public static final double ESTIMATED_KING_MATERIAL_VALUE = 0.;
	
}
