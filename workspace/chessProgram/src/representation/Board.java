package representation;

import java.util.Set;

import boardFeatures.Square;
import gamePlaying.State;
import moves.Move;
import pieces.Piece;

/**
 * Instances of this class represent a board position at a given point in time. As opposed to {@code State},
 * which describes a general game state not specific to chess, {@code Board}s are specifically chess boards. 
 * @author matthewslesinski
 *
 */
public abstract class Board implements State {

	/**
	 * Stores the legal moves this board supports. If null, the moves haven't been calculated yet. If empty, there are no moves.
	 */
	protected Set<Move> legalMoves = null;
	
	/**
	 * Calculates the legal moves for this position and stores them in the {@code legalMoves Set}.
	 * If the game is over, the {@code legalMoves Set} will be empty
	 */
	public abstract void calculateMoves();
	
	/**
	 * Retrieves the {@code Piece} currently at a {@code Square}
	 * @param square The {@code Square} to retrieve the {@code Piece} at
	 * @return The {@code Piece} at that {@code Square}
	 */
	public abstract Piece getPieceAtSquare(Square square);
	
	/**
	 * Determines if the piece that is currently at a square is the given piece
	 * @param piece The {@code Piece} to look for
	 * @param square The {@code Square} to check
	 * @return If the {@code Piece}
	 */
	public boolean isPieceAtSquare(Piece piece, Square square) {
		return piece.equals(getPieceAtSquare(square));
	}
	
	@Override
	public Set<Move> getLegalMoves() {
		return legalMoves;
	}
	
	@Override
	public boolean isOver() {
		if (legalMoves == null) {
			// TODO log and throw exception
		}
		return legalMoves.isEmpty();
	}
	
	/**
	 * Checks for a check (heh... heh) on the current player to move
	 * @return true if there's a check
	 */
	public abstract boolean isInCheck();
	
	
}
