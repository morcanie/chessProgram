package stringUtilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gamePlaying.Game;
import io.FileHandler;
import static support.Constants.*;

/**
 * Houses the logic for turning a pgn formatted string into a {@code Game}
 * @author matthewslesinski
 */
public class PGNParser {

	/** A string for the name of the regex group matching a tag's name */
	private final static String TAG_NAME = "tagName";
	
	/** A string for the name of the regex group matching a tag's value */
	private final static String TAG_VALUE = "tagValue";
	
	/** The string of the regex that can be used to parse tags */
	private final static String TAG_REGEX_AS_STRING = "\\[(?<" + TAG_NAME + ">\\w+)\\s+\"(?<" + TAG_VALUE + ">.+)\"\\]";
	
	/** The above tag-parsing regex turned into a {@code Pattern} */
	private final static Pattern TAG_REGEX = Pattern.compile(TAG_REGEX_AS_STRING);
	
	/**
	 * Reads a .pgn file and turns it into a game
	 * @param filepath The filepath for the file
	 * @return The {@code Game} interpretation of the .pgn file
	 */
	public static Game fromFile(String filepath) {
		return fromLines(FileHandler.getLinesFromFile(filepath));
	}
	
	/**
	 * Takes a list of strings, corresponding to lines of a pgn file, and parses them into
	 * a {@code Game}. First it reads each tag line and extracts all the tag key/values,
	 * and then when all the tags have been exhausted, it appends the rest into a single
	 * space-separated line and parses it. 
	 * @param lines The lines of the file
	 * @return The {@code Game} represented by the pgn
	 */
	private static Game fromLines(List<String> lines) {
		Map<String, String> tags = new HashMap<>();
		Iterator<String> iterator = lines.iterator();
		Matcher matcher;
		String line = null;
		boolean matches = false;
		// gets the next line (if there is one), and recalculates if the line matches the regex
		while (iterator.hasNext() && (matches = (matcher = TAG_REGEX.matcher(line = iterator.next())).matches())) {
			String name = matcher.group(TAG_NAME);
			String value = matcher.group(TAG_VALUE);
			tags.put(name, value);
		}
		
		Game game = initializeGameFromTags(tags);
		
		// The only time matches is true is if the iterator ran out (which means there is no actual game content)
		if (matches) {
			return null;
		}
		StringBuilder gameContentBuilder = new StringBuilder(line);
		iterator.forEachRemaining((gameContentBuilder.append(SINGLE_SPACE))::append);
		String gameContent = gameContentBuilder.toString();
		
		return addGameContent(game, gameContent) ? game : null;
	}
	
	/**
	 * Initializes a {@code Game} instance with the details provided by the pgn
	 * @param tags The tags provided, and their values
	 * @return The {@code Game}, with no actual moves
	 */
	private static Game initializeGameFromTags(Map<String, String> tags) {
		// TODO
		return null;
	}
	
	/**
	 * Adds the moves of the game to the {@code Game} instance
	 * @param game The {@code Game} instance
	 * @param content The space-separated string of game content lines from a pgn file
	 * @return Whether adding content was successful
	 */
	private static boolean addGameContent(Game game, String content) {
		// TODO
		return false;
	}
}
