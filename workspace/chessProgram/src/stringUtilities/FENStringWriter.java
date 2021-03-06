package stringUtilities;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import boardFeatures.Square;
import lines.File;
import lines.Rank;
import pieces.Piece;
import representation.Board;
import representation.CastlingRights;
import static support.Constants.*;
import static support.UtilityFunctions.*;

/**
 * Turns a board into a fen string.
 * @author matthewslesinski
 *
 */
public class FENStringWriter {

	private static final List<Consumer<FENStringWriter>> actions = 
			Arrays.asList(expand(FENStringWriter::addPosition), expand(FENStringWriter::addColorToMove), expand(FENStringWriter::addCastlingRights),
					expand(FENStringWriter::addEnPassantRights), expand(FENStringWriter::addPliesSinceChange), FENStringWriter::addMoveNumber);
	
	/** The board that will be turned into a fen string */
	private final Board board;
	
	/** The moveNumber within the game */
	private final int moveNumber;
	
	/** A stringbuilder to build everything */
	private final StringBuilder builder;
	
	
	public FENStringWriter(Board board, int moveNumber) {
		this.builder = new StringBuilder();
		this.board = board;
		this.moveNumber = moveNumber;
		// Apply the arguments to the actions list
		List<Runnable> appliedActions = actions.stream().map(action -> bind(action, this)).collect(Collectors.toList());
		// There should be a space after each segment
		joinActions(appliedActions, () -> builder.append(SINGLE_SPACE));
	}
	
	
	/**
	 * Compiles the already-calculated FEN string
	 * @return The FEN string
	 */
	public String makeFEN() {
		return builder.toString();
	}
	
	/**
	 * Adds a position to the FEN by adding to each of its board's ranks 
	 * @param board The board being turned into a FEN
	 * @param builder The builder of the FEN string
	 */
	private static void addPosition(Board board, StringBuilder builder) {
		List<Runnable> appendActions = Arrays.stream(Rank.values()).map(rank -> (Runnable) () -> addFENForRank(board, rank, builder)).collect(Collectors.toList());
		joinActions(reverseList(appendActions), () -> builder.append(SLASH));
	}
	
	/**
	 * Adds the section of the FEN string that is specific to a {@code Rank} 
	 * @param board The board to perform this move in the context of
	 * @param rank The rank of the current row
	 * @param builder The string builder to add context to.
	 */
	private static void addFENForRank(Board board, Rank rank, StringBuilder builder) {
		int numSinceLastPiece = 0;
		for (File file : File.values()) {
			Square intersection = Square.getByFileAndRank(file, rank);
			Piece occupant = board.getPieceAtSquare(intersection);
			if (occupant != null && occupant != Piece.NONE) {
				addEmptySquares(numSinceLastPiece, builder);
				numSinceLastPiece = 0;
				builder.append(occupant.toFENCharacter());
			} else {
				numSinceLastPiece += 1;
			}
		}
		addEmptySquares(numSinceLastPiece, builder);
		numSinceLastPiece = 0;
	}
	
	/**
	 * Adds the number of empty squares passed to the string builder
	 * @param numEmptySquares The number
	 * @param builder The builder
	 */
	private static void addEmptySquares(int numEmptySquares, StringBuilder builder) {
		if (numEmptySquares > 0) {
			builder.append(numEmptySquares);
		}
	}
	
	/**
	 * Adds the part of the string depicting whose move it is
	 * @param board The baord
	 * @param builder The builder for the string
	 */
	private static void addColorToMove(Board board, StringBuilder builder) {
		builder.append(board.whoseMove().toString().charAt(0));
	}
	
	/**
	 * Adds the castling rights that are allowed, or a dash if none
	 * @param board The board
	 * @param builder The builder, to add material to
	 */
	private static void addCastlingRights(Board board, StringBuilder builder) {
		boolean addedCastleRight = false;
		for (CastlingRights right : CastlingRights.values()) {
			addedCastleRight |= addCastlingRight(board, builder, right);
		}
		if (!addedCastleRight) {
			builder.append(DASH);
		}
	}
	
	/**
	 * Adds the castling right if it's was taken away in the past
	 * @param board The board being written
	 * @param builder The string builder containing all the input
	 * @param right The castling right
	 * @return When or not it's allowed
	 */
	private static boolean addCastlingRight(Board board, StringBuilder builder, CastlingRights right) {
		boolean canCastle = board.canCastle(right);
		if (canCastle) {
			builder.append(right.toString());
		}
		return canCastle;
	}
	
	/**
	 * Adds the rights allowed by en passant
	 * @param board The board
	 * @param builder The builder for the string to pass back to the user
	 */
	private static void addEnPassantRights(Board board, StringBuilder builder) {
		File enPassantFile = board.enPassantCaptureFile();
		if (enPassantFile != null) {
			builder.append(Square.getByFileAndRank(enPassantFile, board.whoseMove().getEnPassantDestinationRank()));
		} else {
			builder.append(DASH);
		}
	}
	
	/**
	 * Adds the number of plies that have gone without a pawn move/capture
	 * @param board The board
	 * @param builder The builder to add it to
	 */
	private static void addPliesSinceChange(Board board, StringBuilder builder) {
		builder.append(String.valueOf(board.pliesSinceLastIrreversibleChange()));
	}
	
	/**
	 * Adds the number of moves since the start
	 * @param writer The writer to record that
	 */
	private static void addMoveNumber(FENStringWriter writer) {
		writer.builder.append(String.valueOf(writer.moveNumber));
	}
	
	/**
	 * Turns a function of two arguments into a function of one argument
	 * @param originalAction The original action
	 * @return The modified version
	 */
	private static Consumer<FENStringWriter> expand(BiConsumer<Board, StringBuilder> originalAction) {
		return writer -> originalAction.accept(writer.board, writer.builder);
	}
}
