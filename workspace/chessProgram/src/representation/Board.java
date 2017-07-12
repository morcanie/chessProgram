package representation;

import java.util.Set;

import boardFeatures.File;
import boardFeatures.Rank;
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
	
	/**
	 * Determines if castling in the specified way is allowed in this position
	 * @param right the way of castling
	 * @return If it's allowed
	 */
	public abstract boolean canCastle(CastlingRights right);

	/**
	 * Gets the {@code File} the current player can capture onto with an en passant, or null if none/not allowed
	 * @return The destination {@code File} for the pawn
	 */
	public abstract File enPassantCaptureFile();

	
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
	
	@Override
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("--------\n");
		for (int rank = 8; rank >= 1; rank++) {
			for (char file = 'a'; file <= 'h'; file++) {
				toReturn.append("|");
				toReturn.append(getPieceAtSquare(Square.getByFileAndRank(File.getByHumanReadableForm(file+""), Rank.getByHumanReadableForm(rank+""))));
			}
			toReturn.append("\n");
		}
		toReturn.append("|");
		toReturn.append("--------\n");
		
		return toReturn.toString();
	}
	
	
}