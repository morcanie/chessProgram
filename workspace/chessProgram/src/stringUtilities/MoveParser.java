package stringUtilities;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import boardFeatures.Side;
import boardFeatures.Square;
import lines.File;
import lines.Rank;
import moves.Move;
import pieces.PieceType;
import representation.Board;
import representation.CastlingRights;
import support.BadArgumentException;

public class MoveParser {
	
	// The names of each group within the regex
	private static final String PIECE_MOVE_SECTION = "pieceMove";
	private static final String PIECE_TYPE = "pieceType";
	private static final String START_FILE = "startFile";
	private static final String START_RANK = "startRank";
	private static final String CAPTURE_SYMBOL_NAME = "capture";
	private static final String DESTINATION_FILE = "destFile";
	private static final String DESTINATION_RANK = "destRank";
	private static final String PAWN_MOVE_SECTION = "pawnMove";
	private static final String PAWN_CAPTURE = "pawnCapture";
	private static final String PAWN_START_FILE = "pawnStartFile";
	private static final String PAWN_DESTINATION_FILE = "pawnDestFile";
	private static final String PUSHED_STATE = "pushedState";
	private static final String PAWN_DESTINATION_RANK = "pawnDestRank";
	private static final String PROMOTION_DESTINATION_RANK = "promotionDestRank";
	private static final String PROMOTION_PIECE = "promotionPiece";
	private static final String CASTLE = "castle";
	private static final String QUEENSIDE_CASTLE = "queenside";
	
	// Commonly used strings in creating the regex
	private static final String OPENING_NAMED_SECTION = "(?<";
	private static final String CLOSING_ARROW = ">";
	private static final String OR = "|";
	private static final String CLOSING_PARENS = ")";
	private static final String OPTIONAL = "?";
	private static final String ALL_FILES = "[a-h]";
	private static final String ALL_RANKS = "[1-8]";
	private static final String CAPTURE_SYMBOL = "x";
	// any of these three o-shaped characters can be used for castling, even if internally this program chooses "0" when outputting a castling move
	private static final String CASTLE_O = "[0Oo]";
	private static final String CASTLE_DASH = "-";
	
	// Actually build the regex string
	private static final String REGEX = buildRegex();
	
	// compile the regex into a pattern
	private static final Pattern COMPILED_REGEX = Pattern.compile(REGEX);
	
	/**
	 * Parses a move in algebraic notation, such as exd4=Q or Nb5 or 0-0 into a {@code Move}. If there is no legal move that can
	 * be made to correspond with the input string, then null is returned
	 * @param board The board the move would happen on
	 * @param moveString The inputted string
	 * @return The {@code Move} represented by the string, or null if none
	 */
	public static Move parseAlgebraicNotation(Board board, String moveString) {
		Set<Move> legalMoves = board.getLegalMoves();
		
		// Retrieve a mapping of which pieces can move to each end square
		Map<PieceType, Map<Square, List<Move>>> endSquareMappings = MoveWriter.mapEndSquaresToMovesForPieces(legalMoves);
		
		// Actually match the string. Not only will this determine if the input is of the right form, but it will put each named group
		// according to the regex into groups in the matcher
		Matcher parser = COMPILED_REGEX.matcher(moveString);
		if (!parser.matches()) {
			return null;
		}
		// initialize variables
		PieceType movingPiece;
		File startFile = null;
		Rank startRank = null;
		Square endSquare;
		boolean isCapture = false;
		PieceType promotion = null;
		
		
		if (parser.group(CASTLE) != null) {
			// castling is easy to interpret. The only difference is if it's kingside or queenside, and there's a named group to determine that
			movingPiece = PieceType.KING;
			boolean isKingside = parser.group(QUEENSIDE_CASTLE) == null;
			CastlingRights right = CastlingRights.getByColorAndSide(board.whoseMove(), Side.getByBoolean(isKingside));
			Square startSquare = right.getKingSquare();
			startFile = startSquare.getFile();
			startRank = startSquare.getRank();
			endSquare = right.getTargetKingSquare();
		} else {
			File endFile;
			Rank endRank;
			
			// If the regex interpreted the input as a piece move
			if (parser.group(PIECE_MOVE_SECTION) != null) {
				// Each detail of the move should be specified in specific groups
				movingPiece = PieceType.getByLetter(parser.group(PIECE_TYPE));
				endFile = File.getByHumanReadableForm(parser.group(DESTINATION_FILE));
				endRank = Rank.getByHumanReadableForm(parser.group(DESTINATION_RANK));
				startFile = File.getByHumanReadableForm(parser.group(START_FILE));
				startRank = Rank.getByHumanReadableForm(parser.group(START_RANK));
				isCapture = parser.group(CAPTURE_SYMBOL_NAME) != null;
				
			// Pawn moves are more complicated
			} else if (parser.group(PAWN_MOVE_SECTION) != null) {
				movingPiece = PieceType.PAWN;
				endFile = File.getByHumanReadableForm(parser.group(PAWN_DESTINATION_FILE));
				
				// This will be null if it's a promotion
				String naivePawnDestRank = parser.group(PAWN_DESTINATION_RANK);
				if (naivePawnDestRank == null) {
					// If it's a promotion, the rank will instead be stored here
					endRank = Rank.getByHumanReadableForm(parser.group(PROMOTION_DESTINATION_RANK));
					promotion = PieceType.getByLetter(parser.group(PROMOTION_PIECE));
				} else {
					endRank = Rank.getByHumanReadableForm(naivePawnDestRank);
				}
				// If there's an 'x' in the notation
				if (parser.group(PAWN_CAPTURE) != null) {
					isCapture = true;
					// the start file must be specified
					startFile = File.getByHumanReadableForm(parser.group(PAWN_START_FILE));
				} else {
					startFile = endFile;
				}
			} else {
				// shouldn't ever get here
				throw new BadArgumentException(moveString, String.class, "wtf");				
			}
			endSquare = Square.getByFileAndRank(endFile, endRank);
		}
		// Declare new, duplicate variables, so that the filtering of candidate moves can make use of these values,
		// since it believes they're not effectively final
		File startFileFinal = startFile;
		Rank startRankFinal = startRank;
		PieceType promotionFinal = promotion;
		
		// Get the possible moves that involve moving a piece to the endSquare
		List<Move> candidates = endSquareMappings.getOrDefault(movingPiece, Collections.emptyMap()).get(endSquare);
		if (candidates == null) {
			return null;
		}
		// Get rid of moves that don't match the start square or promotion piece
		candidates = candidates.stream().filter(move -> {
			Square candidateSquare = move.getStartSquare();
			return (startFileFinal == null || candidateSquare.getFile() == startFileFinal) &&
					(startRankFinal == null || candidateSquare.getRank() == startRankFinal) &&
					move.getPromotionPieceType() == promotionFinal;
		}).collect(Collectors.toList());
		
		// If we aren't left with one choice by now, then we need either more or less specificity
		if (candidates.size() != 1) {
			return null;
		}
		Move candidate = candidates.get(0);
		// Just make sure that the move that we end up with agrees with capturing or no
		if (candidate.isCapture() != isCapture) {
			return null;
		}
		return candidate;
		
	}
	
	/**
	 * Constructs the regex to use to parse moves.
	 * @return The regex
	 */
	private static String buildRegex() {
		StringBuilder sb = new StringBuilder();
		sb.append("^\\s*")
		.append(OPENING_NAMED_SECTION).append(PIECE_MOVE_SECTION).append(CLOSING_ARROW)
			.append(OPENING_NAMED_SECTION).append(PIECE_TYPE).append(CLOSING_ARROW).append("[KQRBN]").append(CLOSING_PARENS)
			.append(OPENING_NAMED_SECTION).append(START_FILE).append(CLOSING_ARROW).append(ALL_FILES).append(CLOSING_PARENS).append(OPTIONAL)
			.append(OPENING_NAMED_SECTION).append(START_RANK).append(CLOSING_ARROW).append(ALL_RANKS).append(CLOSING_PARENS).append(OPTIONAL)
			.append(OPENING_NAMED_SECTION).append(CAPTURE_SYMBOL_NAME).append(CLOSING_ARROW).append(CAPTURE_SYMBOL).append(CLOSING_PARENS).append(OPTIONAL)
			.append(OPENING_NAMED_SECTION).append(DESTINATION_FILE).append(CLOSING_ARROW).append(ALL_FILES).append(CLOSING_PARENS)
			.append(OPENING_NAMED_SECTION).append(DESTINATION_RANK).append(CLOSING_ARROW).append(ALL_RANKS).append(CLOSING_PARENS).append(CLOSING_PARENS)
		.append(OR)
		.append(OPENING_NAMED_SECTION).append(PAWN_MOVE_SECTION).append(CLOSING_ARROW)
			.append(OPENING_NAMED_SECTION).append(PAWN_CAPTURE).append(CLOSING_ARROW)
				.append(OPENING_NAMED_SECTION).append(PAWN_START_FILE).append(CLOSING_ARROW).append(ALL_FILES).append(CLOSING_PARENS).append(CAPTURE_SYMBOL).append(CLOSING_PARENS).append(OPTIONAL)
			.append(OPENING_NAMED_SECTION).append(PAWN_DESTINATION_FILE).append(CLOSING_ARROW).append(ALL_FILES).append(CLOSING_PARENS)
			.append(OPENING_NAMED_SECTION).append(PUSHED_STATE).append(CLOSING_ARROW)
				.append(OPENING_NAMED_SECTION).append(PAWN_DESTINATION_RANK).append(CLOSING_ARROW).append("[2-7]").append(CLOSING_PARENS)
				.append(OR)
				.append(OPENING_NAMED_SECTION).append(PROMOTION_DESTINATION_RANK).append(CLOSING_ARROW).append("[18]").append(CLOSING_PARENS)
				.append("=")
				.append(OPENING_NAMED_SECTION).append(PROMOTION_PIECE).append(CLOSING_ARROW).append("[QRBN]").append(CLOSING_PARENS).append(CLOSING_PARENS).append(CLOSING_PARENS)
		.append(OR)
		.append(OPENING_NAMED_SECTION).append(CASTLE).append(CLOSING_ARROW).append(CASTLE_O).append(CASTLE_DASH).append(CASTLE_O)
			.append(OPENING_NAMED_SECTION).append(QUEENSIDE_CASTLE).append(CLOSING_ARROW).append(CASTLE_DASH).append(CASTLE_O).append(CLOSING_PARENS).append(OPTIONAL).append(CLOSING_PARENS)
		.append("[+#?!]?\\s*$");
		
		/*
		 * The regex should end up being (all as one line):
		 * ^\s*(?<pieceMove>(?<pieceType>[KQRBN])(?<startFile>[a-h])?(?<startRank>[1-8])?(?<capture>x)?(?<destFile>[a-h])(?<destRank>[1-8]))|
		 * (?<pawnMove>(?<pawnCapture>(?<pawnStartFile>[a-h])x)?(?<pawnDestFile>[a-h])(?<pushedState>(?<pawnDestRank>[2-7])|(?<promotionDestRank>[18])=(?<promotionPiece>[QRBN])))|
		 * (?<castle>[0Oo]-[0Oo](?<queenside>-[0Oo])?)[+#?!]?\s*$
		 */
		return sb.toString();
	}
}
