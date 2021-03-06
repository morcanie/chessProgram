package stringUtilities;

import java.util.Arrays;

import boardFeatures.Square;
import lines.File;
import lines.Rank;
import moves.Move;
import pieces.Piece;
import representation.Board;
import static support.Constants.*;
import static support.UtilityFunctions.*;

public class BoardStringifier<B extends Board> {

	private static final String WHOSE_MOVE_TEXT = "Player to move: ";
	private static final String LAST_MOVE_TEXT = "The previous move: ";
	private final Board board;
	private final Piece[] pieces;
	private final boolean includeLastMove;
	private final boolean includeAnsi;
	
	StringBuilder builder = new StringBuilder();
	
	public BoardStringifier(B board, boolean includeAnsi) {
		this(board, includeAnsi, true);
	}
	
	public BoardStringifier(B board, boolean includeAnsi, boolean includeLastMove) {
		this.board = board;
		pieces = board.toPieceArray();
		this.includeLastMove = includeLastMove;
		this.includeAnsi = includeAnsi;
	}
	
	/**
	 * Appends a dashed line
	 */
	private void addDashedLine() {
		builder.append(NEWLINE);
		builder.append(TRIPLE_SPACE);
		builder.append("-----------------------------");
	}
	
	/**
	 * Turns the given square into its string representation
	 * @param currentSquare
	 */
	private void stringifySquare(Square currentSquare) {
		Piece piece = currentSquare.getValueOfSquareInArray(pieces);
		if (currentSquare.isDarkSquare() && includeAnsi) {
			builder.append(ESCAPE_CHARACTER);
			builder.append(ANSI_DARK_SQUARE);
		}
		builder.append(SINGLE_SPACE);
		builder.append(piece);
		builder.append(SINGLE_SPACE);
		if (currentSquare.isDarkSquare() && includeAnsi) {
			builder.append(ESCAPE_CHARACTER);
			builder.append(ANSI_RESET_ATTRIBUTES);
		}
	}
	
	/**
	 * Adds the letter names for the coordinates for each file below the board
	 */
	private void addFileNames() {
		builder.append(DOUBLE_SPACE);
		for (File file : File.values()) {
			builder.append(DOUBLE_SPACE);
			builder.append(file);
		}
		builder.append(NEWLINE);
	}
	
	/**
	 * Shows the actual board
	 */
	private void stringifyBoard() {
		builder.append(NEWLINE);
		for (Rank rank : reverseList(Arrays.asList(Rank.values()))) {
			builder.append(rank + DOUBLE_SPACE);
			for (File file : File.values()) {
				stringifySquare(Square.getByFileAndRank(file, rank));
			}
			builder.append(NEWLINE);
		}
		addFileNames();
	}
	
	
	/**
	 * Details which pieces have been captured for each side
	 */
	private static void addCapturedPieces() {
		// TODO complete and make not static
		return;
	}
	
	/**
	 * Includes which player has the current turn
	 */
	private void addPlayerToMove() {
		builder.append(TRIPLE_SPACE);
		builder.append(WHOSE_MOVE_TEXT);
		builder.append(board.whoseMove());
		builder.append(NEWLINE);
	}
	
	/**
	 * Includes what the last move made was, and if there's currently a check on the board
	 */
	private void addLastMove() {
		Move last = board.lastMove();
		if (last != null) {
			builder.append(LAST_MOVE_TEXT);
			builder.append(MoveWriter.getMoveAsStringInContext(last, board.getPreviousPosition()));
			if (board.isInCheck()) {
				if (board.isOver()) {
					builder.append(CHECKMATE_SYMBOL);
				} else {
					builder.append(CHECK_SYMBOL);
				}
			}
			builder.append(NEWLINE);
		}
	}
	
	/**
	 * Fills the {@code StringBuilder} with the string representation of the {@code Board} passed to this instance
	 * @return The {@code String} build by the {@code StringBuilder}
	 */
	public String stringify() {
		addDashedLine();
		stringifyBoard();
		addCapturedPieces();
		addPlayerToMove();
		if (includeLastMove) {
			addLastMove();
		}
		addDashedLine();
		return builder.toString();
	}
}
