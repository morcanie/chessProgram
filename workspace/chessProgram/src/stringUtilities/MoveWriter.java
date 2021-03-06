package stringUtilities;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import boardFeatures.Side;
import boardFeatures.Square;
import lines.File;
import lines.Rank;
import moves.Move;
import pieces.PieceType;
import representation.Board;

public class MoveWriter {

	
	/**
	 * Turns a {@code Move} into a string, assuming it would be played on a given board
	 * @param move The move to stringify
	 * @param board The context
	 * @return The move in string form
	 */
	public static String getMoveAsStringInContext(Move move, Board board) {
		Set<Move> legalMoves = board.getLegalMoves();
		return getMoveAsString(mapEndSquaresToMovesForPieces(legalMoves), move);
	}
	
	/**
	 * Maps each relevant piece/square combination to the list of moves involving that piece moving to that square.
	 * This is important for figuring out how much information to include in the strings of moves that get printed.
	 * @param moves The {@code Set} of {@code Move}s that can be made
	 * @return
	 */
	public static Map<PieceType, Map<Square, List<Move>>> mapEndSquaresToMovesForPieces(Set<Move> moves) {
		Map<PieceType, Map<Square, List<Move>>> map = new EnumMap<>(PieceType.class);
		moves.forEach(move -> {
			PieceType piece = move.getMovingPieceType();
			Map<Square, List<Move>> squareMapping = map.getOrDefault(piece, new EnumMap<>(Square.class));
			map.putIfAbsent(piece, squareMapping);
			Square endSquare = move.getEndSquare();
			List<Move> movesWithRelevantDestination = squareMapping.getOrDefault(endSquare, new LinkedList<>());
			squareMapping.putIfAbsent(endSquare, movesWithRelevantDestination);
			movesWithRelevantDestination.add(move);
		});
		return map;
	}
	
	/**
	 * Gets the string form of a move. Whether to include the start file or rank is inferred using
	 * the provided {@code Map}, which tells which moves involve moving a piece and end on a square.
	 * @param endSquareMap A mapping describing which moves involve a piece moving to an end square
	 * @param move The move to convert to a string
	 * @return The string of the move
	 */
	public static String getMoveAsString(Map<PieceType, Map<Square, List<Move>>> endSquareMap, Move move) {
		boolean includeFile = false;
		boolean includeRank = false;
		File startFile = move.getStartSquare().getFile();
		Rank startRank = move.getStartSquare().getRank();
		List<Move> candidates = endSquareMap.get(move.getMovingPieceType()).get(move.getEndSquare());
		if (candidates.size() > 1) {
			// There can be multiple moves with the same start, end, and moving piece, because of promotions
			boolean otherCandidates = false;
			for (Move candidate : candidates) {
				if (!move.getStartSquare().equals(candidate.getStartSquare())) {
					
					// If the candidate has the same file or rank, then we need to include the corresponding bit of information.
					// Also, we've found some other square that has a piece of the same moving piece which can move to the end
					// square of this move
					otherCandidates = true;
					includeRank |= startFile == candidate.getStartSquare().getFile();
					includeFile |= startRank == candidate.getStartSquare().getRank();
				}
			}
			// If there would still be ambiguity about which square is moving to the end square, cut it off
			if (!includeFile && !includeRank && otherCandidates) {
				includeFile = true;
			}
		}
		return getMoveAsString(move, includeFile, includeRank);
	}
	
	
	/**
	 * Converts a move into a string
	 * @param move The move to convert
	 * @param includeFile Whether to include the start file
	 * @param includeRank Whether to include the start rank
	 * @return The string of the move
	 */
	public static String getMoveAsString(Move move, boolean includeFile, boolean includeRank) {
		if (move.isCastle()) {
			return Side.getByRelation(move.getEndSquare()).isKingside() ? "0-0" : "0-0-0";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(move.getMovingPieceType());
		if (includeFile || (move.getMovingPieceType() == PieceType.PAWN && move.isCapture())) {
			builder.append(move.getStartSquare().getFile().getHumanReadableForm());
		}
		if (includeRank) {
			builder.append(move.getStartSquare().getRank().getHumanReadableForm());
		}
		if (move.isCapture()) {
			builder.append("x");
		}
		builder.append(move.getEndSquare());
		if (move.isPromotion()) {
			builder.append("=").append(move.getPromotionPieceType());
		}
		return builder.toString();
	}
	
	
}
