package moves;

import java.util.List;

import boardFeatures.Direction;
import boardFeatures.File;
import boardFeatures.Square;
import gamePlaying.Color;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;

/**
 * Used to preprocess a board before calculating the moves, so that calculating them is easier than accessing the board directly
 */
public interface ProcessedBoard<B extends Board> {

	
	/**
	 * Gets the color to move
	 * @return the {@code Color}
	 */
	public Color whoseMove();
	
	/**
	 * Can castling be done the particular way, without considering the specifics of the position?
	 * @param right The castling way
	 * @return if it can be done
	 */
	public boolean canCastle(CastlingRights right);
	
	/**
	 * What file can an en passant capture be onto? If none, this is null
	 * @return The {@code File}
	 */
	public File getEnPassantFile();
	
	/**
	 * Gets the list of squares instances of this piece are on
	 * @param piece The piece to get the list for
	 * @return The list of squares
	 */
	public List<Square> getListOfSquaresForPiece(Piece piece);
	
	/**
	 * Gets the piece that is at a square
	 * @param square The square with the piece
	 * @return The {@code Piece}
	 */
	public Piece getPieceAtSquare(Square square);
	
	/**
	 * Figures out if the king is in check and what squares around it it can move to safely
	 * @param The possible squares that the king could move to if they're safe
	 */
	public void calculateKingSafety(List<Square> possibleSquares);
	
	/**
	 * Retrieves the list of squares with pieces that are giving check
	 * @return The list of squares
	 */
	public List<Square> whoIsAttackingTheKing();
	
	/**
	 * Returns the list of the squares around the king that are safe to move to
	 * @return The list of safe squares
	 */
	public List<Square> getSafeKingDestinations();
	
	/**
	 * Determines if a piece on a square is pinned to the king. If so, the direction to the pinning piece will be returned, otherwise null
	 * @param square The square of the possibly pinned piece
	 * @return The directino to the pinning piece
	 */
	public Direction isPiecePinned(Square square);
	
	/**
	 * Determines if en passant is disallowed because moving the pawn and removing the opposing pawn would allow the king to be captured
	 * @param movingPawnFile The file of the pawn performing the en passant
	 * @return true iff it's pinned
	 */
	public boolean isEnPassantPinned(File movingPawnFile);
	
}
