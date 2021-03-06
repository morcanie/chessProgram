package moveCalculationStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import boardFeatures.SixteenthSector;
import boardFeatures.Square;
import support.BadArgumentException;

/**
 * This divides a board into 16 sectors (slices of a circle), based at some center square. It assumes that it will be given
 * one square per sector, that each square will not be in a sector adjacent to one also with a square, and that they are equidistant
 * from the center. This describes knight moves and king moves (without castling).
 * @author matthewslesinski
 *
 */
public class EvenlySpacedCircleImpl implements EvenlySpacedCircle {

	/** A {@code Map} detailing which {@code Square} in this circle is within each {@code SixteenthSector} */
	private Map<SixteenthSector, Square> relations = new EnumMap<>(SixteenthSector.class);
	
	/** A list of the {@code Square}s that were added to this, to neatly describe this as a wrapped collection */
	private List<Square> addedSquares = new LinkedList<>();
	
	/** The center of this circle */
	private Square center;
	
	public EvenlySpacedCircleImpl(Square center, Square... ring) {
		this(center, Arrays.asList(ring));
	}
	
	/**
	 * Build the circle with a center and a circumference
	 * @param center The center
	 * @param ring The squares in the surface
	 */
	public EvenlySpacedCircleImpl(Square center, Collection<Square> ring) {
		if (ring.size() > 8) {
			throw new BadArgumentException(ring, Collection.class, "Expected at most 8 squares");
		}
		for (Square squareToAdd : ring) {
			SixteenthSector sector = SixteenthSector.getRelation(center, squareToAdd);
			if (relations.containsKey(sector)) {
				throw new BadArgumentException(squareToAdd, Square.class, "The whole point of an EvenlySpacedCircle is to have one square in each sector");
			}
			relations.put(sector, squareToAdd);
			addedSquares.add(squareToAdd);
		}
		this.center = center;
	}

	@Override
	public Iterator<Square> iterator() {
		return addedSquares.iterator();
	}

	@Override
	public List<Square> getNearestSquares(Square remote) {
		// Determine which sector contains the remote square
		SixteenthSector containingSector = SixteenthSector.getRelation(center, remote);
		// If that sector has a square in this circle, return that square
		if (relations.containsKey(containingSector)) {
			return Collections.singletonList(relations.get(containingSector));
		}
		// Otherwise find the closest sectors and return their squares.
		return containingSector.getNearestSectors().stream()
				.map(sector -> relations.get(sector)).filter(square -> square != null)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<Square> getWrappedCollection() {
		return addedSquares;
	}
	
}
