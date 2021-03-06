package independentDataStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.IntFunction;

import convenienceDataStructures.IrreversibleWrappedCollection;
import support.BadArgumentException;
import static support.UtilityFunctions.*;

/**
 * A {@code Set} of elements that are ordered, but also are fixed, so it's easy to get elements some
 * distance away.
 * @author matthewslesinski
 *
 * @param <E> The type of the elements
 */
public interface FixedOrderingSet<E> extends NavigableSet<E>, IrreversibleWrappedCollection<E> {

	
	/**
	 * Gets the element in this set that is a certain number of indices removed from the given one
	 * @param element The given element
	 * @param offset The number of indices removed the other one is. The sign of the number determines the direction.
	 * @return The element a certain number of indices away
	 */
	public E retrieveOffsetFromElement(E element, int offset);
	
	/**
	 * Gets the actual function used to retrieve an element at some index
	 * @return The function
	 */
	public IntFunction<E> getIndexFunction();

	/**
	 * Gets the neighbors on either side of the given element in this set
	 * @param e The element
	 * @param elementsToExclude possible elements to gloss over and not include
	 * @return The neighbors on either side
	 */
	public default Collection<E> getNeighbors(E e, Set<E> elementsToExclude) {
		return constructListOfElements(lower(e, elementsToExclude), higher(e, elementsToExclude));
	}
	
	/**
	 * Gets the neighbors on either side of the given element in this set
	 * @param e The element
	 * @return The neighbors on either side
	 */
	public default Collection<E> getNeighbors(E e) {
		return constructListOfElements(lower(e), higher(e));
	}
	
	/**
	 * Determines if the second element is higher or lower than the first, and gets the next element in the sequence in that direction
	 * @param first The first element
	 * @param second The second one, setting a direction from first
	 * @return The third element in that direction
	 */
	public default E getThirdInSequence(E first, E second) {
		switch (getSign(comparator().compare(first, second))) {
		case -1:
			return higher(second);
		case 1:
			return lower(second);
		default:
			throw new BadArgumentException(Arrays.asList(first, second), first.getClass(), "The two provided elements must be in a sequence and not be the same");
		}
	}
	
	@Override
	public default E lower(E e) {
		return retrieveOffsetFromElement(e, -1);
	}
	
	/**
	 * Returns the previous element in the set that is not one of the elements to exclude
	 * @param e The one to get the previous for
	 * @param elementsToExclude elements to gloss over
	 * @return The previous in the set
	 */
	public default E lower(E e, Set<E> elementsToExclude) {
		do {
			e = lower(e);
		} while (elementsToExclude.contains(e));
		return e;
	}

	@Override
	public default E floor(E e) {
		return contains(e) ? e : lower(e);
	}

	@Override
	public default E ceiling(E e) {
		return contains(e) ? e : higher(e);
	}

	@Override
	public default E higher(E e) {
		return retrieveOffsetFromElement(e, 1);
	}
	
	/**
	 * Returns the next element in the set that is not one of the elements to exclude
	 * @param e The one to get the next for
	 * @param elementsToExclude elements to gloss over
	 * @return The next in the set
	 */
	public default E higher(E e, Set<E> elementsToExclude) {
		do {
			e = higher(e);
		} while (elementsToExclude.contains(e));
		return e;
	}
	
	@Override
	public default E first() {
		return isEmpty() ? null : getIndexFunction().apply(0);
	}

	@Override
	public default E last() {
		return isEmpty() ? null : getIndexFunction().apply(size() - 1);
	}

	@Override
	public default E pollFirst() {
		throw new BadArgumentException(this, this.getClass(), MODIFICATION_MESSAGE);
	}

	@Override
	public default E pollLast() {
		throw new BadArgumentException(this, this.getClass(), MODIFICATION_MESSAGE);
	}
	
	@Override
	public default Iterator<E> descendingIterator() {
		return new Iterator<>() {
			private int currentIndex = size();
			@Override
			public boolean hasNext() {
				return currentIndex > 0;
			}

			@Override
			public E next() {
				return getIndexFunction().apply(--currentIndex);
			}
		};
	}
	
	@Override
	public default NavigableSet<E> headSet(E toElement, boolean inclusive) {
		return subSet(first(), true, toElement, inclusive);
	}

	@Override
	public default NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
		return subSet(fromElement, inclusive, last(), true);
	}

	@Override
	public default SortedSet<E> subSet(E fromElement, E toElement) {
		return subSet(fromElement, true, toElement, false);
	}

	@Override
	public default SortedSet<E> headSet(E toElement) {
		return headSet(toElement, false);
	}

	@Override
	public default SortedSet<E> tailSet(E fromElement) {
		return tailSet(fromElement, true);
	}
	
	@Override
	public default int size() {
		return IrreversibleWrappedCollection.super.size();
	}

	@Override
	public default boolean isEmpty() {
		return IrreversibleWrappedCollection.super.isEmpty();
	}

	@Override
	public default boolean contains(Object o) {
		return IrreversibleWrappedCollection.super.contains(o);
	}

	@Override
	public default Iterator<E> iterator() {
		return IrreversibleWrappedCollection.super.iterator();
	}

	@Override
	public default Object[] toArray() {
		return IrreversibleWrappedCollection.super.toArray();
	}

	@Override
	public default <T> T[] toArray(T[] a) {
		return IrreversibleWrappedCollection.super.toArray(a);
	}
	
	@Override
	public default boolean containsAll(Collection<?> c) {
		return IrreversibleWrappedCollection.super.containsAll(c);
	}
	
	@Override
	public default boolean remove(Object o) {
		return IrreversibleWrappedCollection.super.remove(o);
	}
	
	@Override
	public default boolean retainAll(Collection<?> c) {
		return IrreversibleWrappedCollection.super.retainAll(c);
	}

	@Override
	public default boolean removeAll(Collection<?> c) {
		return IrreversibleWrappedCollection.super.removeAll(c);
	}

	@Override
	public default void clear() {
		IrreversibleWrappedCollection.super.clear();
	}
}
