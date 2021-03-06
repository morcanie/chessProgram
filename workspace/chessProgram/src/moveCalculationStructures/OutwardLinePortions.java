package moveCalculationStructures;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import boardFeatures.Square;
import independentDataStructures.Cluster;
import independentDataStructures.ListBackedByMaps;
import lines.Direction;
import pieces.Piece;

public class OutwardLinePortions implements Cluster<Square> {
	
	/** The center of this instance */
	private final Square center;
	
	/** A straightforward list of the elements in this instance */
	private final ListBackedByMaps<Square> elements;
	
	/** A {@code Map} describing which {@code Square} in this instance is the closest in a given direction from the center */
	private final Map<Direction, Square> closestInDirections;
	
	/** The list of the directions with squares going outward from the center of this instance */
	private final ListBackedByMaps<Direction> directions;
	
	/** 
	 * A comparator of squares that sorts them into segments with the same direction to the center. Within the segments, the closest ones
	 * are put earlier in the sorting
	 */
	private final Comparator<Square> comparator = new Comparator<>() {

		@Override
		public int compare(Square o1, Square o2) {
			int directionDifference = center.getDirectionToSquare(o1).ordinal() - center.getDirectionToSquare(o2).ordinal();
			if (directionDifference != 0) {
				return directionDifference;
			}
			if (center.whichIsCloser(o1, o2) == o1) {
				return -1;
			}
			return 1;
		}
		
	};

	public OutwardLinePortions(Square center, List<Square> importantSquares) {
		this.center = center;
		sortLines(importantSquares);
		this.elements = importantSquares.isEmpty() ? new ListBackedByMaps<>(Square.class) : new ListBackedByMaps<>(importantSquares);
		this.closestInDirections = new EnumMap<>(Direction.class);
		importantSquares.forEach(square -> closestInDirections.merge(center.getDirectionToSquare(square), square, center::whichIsCloser));
		this.directions = importantSquares.isEmpty() ? new ListBackedByMaps<>(Direction.class) : new ListBackedByMaps<>(closestInDirections.keySet());
	}
	
	@Override
	public Set<Square> getWrappedSet() {
		return elements;
	}

	@Override
	public Square getCenter() {
		return center;
	}
	
	/**
	 * If there's a piece on the current square, this returns the first square in the next direction to cycle over, otherwise the next one to
	 * consider in general
	 * @param curr The current square
	 * @param occupants A function that describes which pieces are at which squares
	 * @return The next square
	 */
	public Square getNext(Square curr, Function<Square, Piece> occupants) {
		Piece occupant = occupants.apply(curr);
		if (occupant == Piece.NONE || occupant == null) {
			return elements.getNext(curr);
		}
		Direction dirToCurr = center.getDirectionToSquare(curr);
		Direction nextDirection = directions.getNext(dirToCurr);
		return nextDirection == null ? null : closestInDirections.get(nextDirection);
	}
	
	/**
	 * Sorts a collection according to the comparator specified in this class
	 * @param collection The collection to sort
	 */
	private void sortLines(List<Square> collection) {
		Collections.sort(collection, comparator);
	}
}
