package deprecated;

import moves.Move;
import pieces.PieceType;

//This interface was written and then I realized it wasn't necessary. I'm still keeping it up in case it becomes useful
@Deprecated
public interface Castling extends Move {
	
	
	@Override
	public default PieceType getMovingPieceType() {
		return PieceType.KING;
	}

	@Override
	public default boolean isCastle() {
		return true;
	}
}
