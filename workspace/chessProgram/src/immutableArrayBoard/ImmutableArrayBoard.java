package immutableArrayBoard;


import java.util.function.Supplier;

import boardFeatures.File;
import boardFeatures.Square;
import gamePlaying.Color;
import moves.Move;
import pieces.Piece;
import representation.Board;
import representation.BoardBuilder;
import representation.CastlingRights;
import representation.MoveGenerator;

/**
 * This is the most intuitive representation of a board. It holds a place for each square (at some index and some bit in the {@code board} array) and
 * the value at that square's place represents a piece. Furthermore, making a move to get to a new position will result in creating a new instance of
 * this class. Whether or not there's a more efficient implementation (such as bitboards), this one is the easiest to work with as a starting point.
 * @author matthewslesinski
 *
 */
public class ImmutableArrayBoard extends Board {

	private static final int FIRST_CASTLING_RIGHT_BIT = 0;
	
	private static final int RIGHTS_INDEX = 8;
	
	private static final int ARRAY_SIZE = 9;
	
	private static final int COLOR_MASK = 0x100;
	
	private static final int EN_PASSANT_INDEX = 4;
	
	private static final int EN_PASSANT_PERMISSION_MASK = 0b1000;
	
	private static final int NUMBER_OF_BITS_PER_PIECE = 4;
	
	/**
	 * The array containing the pieces and other board information, and so the actual internal representation of this {@code Board}.
	 * The first 8 indices contain ints representing the file with that index. Each of those ints is divided into 8 groups of four bits.
	 * A piece an be encoded in 4 bits. So that's what's stored in each group of 4 bits: the piece stored at that square when indexing into
	 * the file to get the rank. The last index of the array holds the extra information about board state, such as who's to move. This int
	 * says what castling is potentially allowed, using the first four bits, now or in a deeper position from this one. It also uses second 4 bits
	 * to say what file a pawn could take a pawn from through en passant, and it uses the first bit of the next quarter of the int to say whose move it is.
	 */
	private final int[] board;
	
	private static final Supplier<MoveGenerator<ImmutableArrayBoard>> moveGeneratorConstructor = ImmutableArrayMoveGenerator::new;
		
	
	private ImmutableArrayBoard(int[] board) {
		this.board = board;
	}
		
	
	@Override
	public Piece getPieceAtSquare(Square square) {
		return Piece.getPieceByBits(getBitsAtSquare(square, board));
	}


	@Override
	public Color whoseMove() {
		return Color.getColor((board[RIGHTS_INDEX] & COLOR_MASK) != 0);
	}
	
	@Override
	public boolean canCastle(CastlingRights right) {
		return board[RIGHTS_INDEX] >>> (FIRST_CASTLING_RIGHT_BIT + right.ordinal()) % 2 == 1;
	}

	@Override
	public File enPassantCaptureFile() {
		int relevantInfo = board[RIGHTS_INDEX] >>> EN_PASSANT_INDEX;
		return File.getByIndex((relevantInfo & EN_PASSANT_PERMISSION_MASK) != 0 ?
				relevantInfo & 0b111 : null);
	}

	
	@Override
	public Board performMove(Move move) {
		BoardBuilder<ImmutableArrayBoard> builder = new Builder(this);
		
		// TODO handle rights
		return builder.build();
	}


	@Override
	public void calculateMoves() {
		legalMoves = moveGeneratorConstructor.get().calculateMoves(this);
	}
	
	
	
	public static class Builder extends BoardBuilder<ImmutableArrayBoard> {
		
		private int[] board = new int[ARRAY_SIZE];
		
		/**
		 * Initializes the board based on another board
		 * @param boardContainer The {@code ImmutableArrayBoard} to base this one off of
		 * @return This builder
		 */
		public Builder(int[] board) {
			this.board = board.clone();
		}
		
		/**
		 * Initializes the board based on another board
		 * @param boardContainer The {@code ImmutableArrayBoard} to base this one off of
		 * @return This builder
		 */
		public Builder(ImmutableArrayBoard boardContainer) {
			this(boardContainer.board.clone());
		}
		
		/**
		 * Initializes the board based on an array of pieces. This does not set any rights except whose move.
		 * @param pieces The array of pieces to put on the board
		 * @param whoToMove Whose current move it is
		 */
		public Builder(Piece[] pieces, Color whoToMove) {
			super(pieces, whoToMove);
		}
		
		@Override
		public Builder withColorToMove(Color color) {
			setRightsByBitMask(COLOR_MASK, color.isWhite());
			return this;
		}
		
		@Override
		public Builder withPieceAtSquare(Piece piece, Square square) {
			setPieceAtSquare(piece.getBitRepresentation(), square);
			return this;
		}
		
		@Override
		public Builder withCastlingRight(CastlingRights castlingRight, boolean enabled) {
			setRightsByBitMask(0x1 << (castlingRight.ordinal() + FIRST_CASTLING_RIGHT_BIT), enabled);
			return this;
		}
		
		@Override
		public Builder withEnPassant(File file) {
			setRightsByBitMask(file != null ? (file.getIndex() | EN_PASSANT_PERMISSION_MASK) << EN_PASSANT_INDEX : ~0b11110000, file != null);
			return this;
		}
		
		@Override
		public ImmutableArrayBoard build() {
			return new ImmutableArrayBoard(board);
		}
		
		/**
		 * Sets the rights for the board to have a certain mask
		 * @param mask The mask
		 * @param orBoolean Whether the mask should be ORed, or the reverse of the mask should be ANDed
		 */
		private void setRightsByBitMask(int mask, boolean orBoolean) {
			board[RIGHTS_INDEX] = orBoolean ? board[RIGHTS_INDEX] | mask : board[RIGHTS_INDEX] & ~mask;
		}
		
		/**
		 * Sets a bit representation of a piece at the bit placement for the given square
		 * @param pieceBits The bit representation of the piece
		 * @param square The square the piece is on
		 */
		private void setPieceAtSquare(int pieceBits, Square square) {
			int fileIndex = square.getFile().getIndex();
			board[fileIndex] = board[fileIndex] | (pieceBits << (square.getRank().getIndex() * NUMBER_OF_BITS_PER_PIECE));
		}
	}
	
	/**
	 * Gets the bit representation of the piece at a square
	 * @param square The square to check
	 * @param board The board containing the piece
	 * @return The bit representation, where 0 is nothing, 1-6 is a white piece, and 7-12 is a black piece
	 */
	private int getBitsAtSquare(Square square, int[] board) {
		return (board[square.getFile().getIndex()] >>> (square.getRank().getIndex() * NUMBER_OF_BITS_PER_PIECE)) & 0b1111;
	}

	
}
