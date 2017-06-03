package representation;

import gamePlaying.Color;

/**
 * Represents a move in the game
 * @author matthewslesinski
 *
 */
public interface Move {

	/**
	 * Returns the color of the piece that gets moved (and also the player making the move)
	 * @return the {@code Color} of the piece that moves
	 */
	public Color getColorOfPiece();
}
