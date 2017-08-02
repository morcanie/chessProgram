package dataStructures;

import java.util.function.Function;
import java.util.function.Supplier;

import boardFeatures.Square;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BreadthFirstTraversal<E> implements Iterator<E> {

	public static class OfSquares extends BreadthFirstTraversal<Square> {
		public OfSquares(Function<Square, Collection<Square>> getNeighbors) {
			super(getNeighbors, () -> EnumSet.noneOf(Square.class));
		}
	}
	
	private final Queue<E> queue = new LinkedList<>();
	private final Set<E> visitedElements;
	private final Function<E, Collection<E>> getNeighbors;
	
	public BreadthFirstTraversal(Function<E, Collection<E>> getNeighbors, Supplier<Set<E>> setConstructor) {
		Collection<E> startNodes = getNeighbors.apply(null);
		this.visitedElements = setConstructor.get();
		visitedElements.addAll(startNodes);
		queue.addAll(startNodes);
		this.getNeighbors = getNeighbors;
	}
	
	
	
	@Override
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	@Override
	public E next() {
		E currentElement = queue.poll();
		Iterable<E> neighbors = getNeighbors.apply(currentElement);
		neighbors.forEach(neighbor -> {
			if (!visitedElements.contains(neighbor)) {
				visitedElements.add(neighbor);
				queue.add(neighbor);
			}
		});
		return currentElement;
	}	
}