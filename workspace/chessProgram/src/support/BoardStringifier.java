package support;

import java.util.Arrays;

import boardFeatures.File;
import boardFeatures.Rank;
import boardFeatures.Square;
import pieces.Piece;
import representation.Board;

public class BoardStringifier<B extends Board> {

	private static final String WHOSE_MOVE_TEXT = "Player to move: ";
	private final Board board;
	private final Piece[] pieces;
	StringBuilder builder = new StringBuilder();
	
	public BoardStringifier(B board) {
		this.board = board;
		pieces = board.toPieceArray();
	}
	
	/**
	 * Appends a dashed line
	 */
	private void addDashedLine() {
		builder.append(Constants.NEWLINE);
		builder.append(Constants.TRIPLE_SPACE);
		builder.append("-----------------------------");
	}
	
	/**
	 * Turns the given square into its string representation
	 * @param currentSquare
	 */
	private void stringifySquare(Square currentSquare) {
		Piece piece = currentSquare.getValueOfSquareInArray(pieces);
		if (currentSquare.isDarkSquare()) {
			builder.append(Constants.ESCAPE_CHARACTER);
			builder.append(Constants.ANSI_DARK_SQUARE);
		}
		builder.append(Constants.SINGLE_SPACE);
		builder.append(piece);
		builder.append(Constants.SINGLE_SPACE);
		if (currentSquare.isDarkSquare()) {
			builder.append(Constants.ESCAPE_CHARACTER);
			builder.append(Constants.ANSI_RESET_ATTRIBUTES);
		}
	}
	
	/**
	 * Adds the letter names for the coordinates for each file below the board
	 */
	private void addFileNames() {
		builder.append(Constants.DOUBLE_SPACE);
		for (File file : File.values()) {
			builder.append(Constants.DOUBLE_SPACE);
			builder.append(file);
		}
		builder.append(Constants.NEWLINE);
	}
	
	/**
	 * Shows the actual board
	 */
	private void stringifyBoard() {
		builder.append(Constants.NEWLINE);
		for (Rank rank : UtilityFunctions.reverseList(Arrays.asList(Rank.values()))) {
			builder.append(rank + Constants.DOUBLE_SPACE);
			for (File file : File.values()) {
				stringifySquare(Square.getByFileAndRank(file, rank));
			}
			builder.append(Constants.NEWLINE);
		}
		addFileNames();
	}
	
	
	/**
	 * Details which pieces have been captured for each side
	 */
	private void addCapturedPieces() {
		// TODO
		return;
	}
	
	/**
	 * Includes which player has the current turn
	 */
	private void addPlayerToMove() {
		builder.append(Constants.TRIPLE_SPACE);
		builder.append(WHOSE_MOVE_TEXT);
		builder.append(board.whoseMove());
	}
	
	/**
	 * Includes what the last move made was, and if there's currently a check on the board
	 */
	private void addLastMove() {
		// TODO
		return;
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
		addLastMove();
		addDashedLine();
		return builder.toString();
	}
	
	/*
	 * For reference, this is the copy/pasted version of the toString function I used in the previous attempt at a chess program.
	 * I extracted a lot of the logic for the logic in this file, of course changing the logic where necessary and improving the code style.
	 * I'm leaving this here because there are a few portions that I wasn't able to implement yet: specifically a row of the pieces captured,
	 * and a description of what the last move was and if the player's in check.
	 */
//	public String stringify1() {
//		String space = game.eclipse ? "\u3000" : " ";
//		String aa = game.eclipse ? "\uff41" : "a";
//		String bb = game.eclipse ? "\uff42" : "b";
//		String cc = game.eclipse ? "\uff43" : "c";
//		String dd = game.eclipse ? "\uff44" : "d";
//		String ee = game.eclipse ? "\uff45" : "e";
//		String ff = game.eclipse ? "\uff46" : "f";
//		String gg = game.eclipse ? "\uff47" : "g";
//		String hh = game.eclipse ? "\uff48" : "h";
//		String string = "\n";
//		for (int i = eight; i >= one; i--) {
//			string += (i + 1) + "  ";
//			for (int j = a; j <= h; j++) {
//				if ((i + j) % 2 == 0) {
//					string += (char)27 + "[47m";
//				}
//				string += " ";
//				Square square = game.square(j, i);
//				int piece = accessSquare(square);
//				if (piece != 0) string += pieceToString(piece);
//				else string += space;
//				string += " ";
//				string += (char)27 + "[0m";
//				
//			}
//			string += "\n";
//		}
//		string += "    " + aa + "  " + bb + "  " + cc + "  " + dd + "  " + ee + "  " + ff + "  " + gg + "  " + hh + "\n";
//		String captures = "";
//		boolean capture = false;
//		for (int i = queen; i >= pawn; i--) {
//			for (int j = 0; j < howManyCaptured(white, i); j++) {
//				capture = true;
//				captures += pieceToString(i);
//			}
//		}
//		for (int i = queen; i >= pawn; i--) {
//			for (int j = 0; j < howManyCaptured(black, i); j++) {
//				capture = true;
//				captures += pieceToString(i * -1);
//			}
//		}
//		if (capture) string += captures + "\n";
//		string += "Turn: ";
//		string += toMove ? "white" : "black";
//		int lastCapture;
//		if (last != null) string += "\n" + (toMove ? "black" : "white") + " just moved a " + 
//				pieceToString(game.gameHistory.get(game.moveCount - 1).accessSquare(last.prev)) + 
//				" from " + last.prev + " to " + last.next + 
//				((lastCapture = lastCapture()) == 0 ? "" : ", capturing a " + pieceToString(lastCapture) + (last.isEnPassant() ? " via en passant" : "")) +
//				(lastMovePromotion() ? (lastCapture == 0 ? ", promoting" : ", and promoting") + " to a " + pieceToString(accessSquare(last.next)): "");
//		if (numChecks() > 0) string += "\ncheck";
//		return string;
//	}
	
}