package support;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import boardFeatures.Square;
import convenienceDataStructures.UnmodifiableWrappedSet;
import evaluation.ConventionalMaterialEvaluation;
import evaluation.Evaluator;
import gamePlaying.Color;
import gamePlaying.Game;
import gamePlaying.GameTree;
import hashing.Hasher;
import hashing.PseudoRandomNumbers;
import hashing.RandomNumberGenerator;
import hashing.ZobristHasher;
import immutableArrayBoard.ImmutableArrayBoard;
import moves.BasicMove;
import moves.LazyPreProcessing;
import moves.Move;
import moves.MoveBuilder;
import moves.MoveSet;
import moves.MoveType;
import moves.ProcessedBoard;
import pieces.PieceType;
import representation.Board;
import representation.BoardBuilder;
import search.AI;
import search.BasicDepthBasedMinimax;


/**
 * Houses the constructors used throughout the code that should be abstracted out. Therefore, the only times the actual classes that house implementation,
 * but are used only to represent an interface/abstract class, need to be used are here, and everywhere else, their constructor here can be referenced or they
 * can be referred to by their interface/abstract class.
 * @author matthewslesinski
 *
 */
public class Constructors {

	/** A constructor for the implementation of a full game */
	public static final Supplier<Game> GAME_CONSTRUCTOR = GameTree::new;
	
	/** Used to create the preprocessing for move generation for a board */
	public static final Function<Board, ProcessedBoard<Board>> PRE_PROCESSING_CONSTRUCTOR = LazyPreProcessing<Board>::new; 
	
	/** A method used to get a {@code Move} decompressed from the ints stored in a list of moves */ 
	public static final IntFunction<Move> MOVE_DECOMPRESSOR = BasicMove::new;
	
	public static final Function<Collection<Move>, UnmodifiableWrappedSet<Move>> MOVESET_CONSTRUCTOR = MoveSet::new;
	
	/** The constructor to use to create boards. This in the end determines the class used to represent the board */
	public static final Function<String, BoardBuilder<? extends Board>> BOARD_BUILDER_CONSTRUCTOR = ImmutableArrayBoard.Builder::fromFen;
	
	/**
	 * Constructs a {@code MoveBuilder} using the essential pieces of information for a move.
	 * @param type The type of move that this is
	 * @param movingPiece The type of piece making the move
	 * @param start The starting square
	 * @param end The ending square
	 * @param movingColor The color of the player making the move
	 */
	public static MoveBuilder<? extends Move> MOVE_BUILDER_CONSTRUCTOR(MoveType type, PieceType movingPiece, Square start, Square end, Color movingColor) {
		return new BasicMove.Builder(type, movingPiece, start, end, movingColor);
	}
	
	/** A constructor for a random number generator. Ideally the sequences produced by these generators should not have much linear dependence. */
	public static final Supplier<RandomNumberGenerator> RANDOM_NUMBER_GENERATOR = PseudoRandomNumbers::new;
	
	/** A constructor for an object used to hash {@code Board}s to longs */
	public static final Supplier<Hasher> HASHER_CONSTRUCTOR = ZobristHasher::new;
	
	/** A constructor that returns an object that can estimate who is winning in a position */
	public static final Supplier<Evaluator> EVALUATOR_CONSTRUCTOR = ConventionalMaterialEvaluation::new;
	
	/** A constructor that returns an object that can be used to determine what the computer and its algorithms think is the best move in a position */
	public static final Supplier<AI> AI_CONSTRUCTOR = BasicDepthBasedMinimax::new;
	
}
