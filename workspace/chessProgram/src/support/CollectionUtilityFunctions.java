package support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtilityFunctions extends ComparisonUtilityFunctions {

	/**
	 * Returns a new list (backed by an {@code ArrayList}) that contains a reverse-order shallow
	 * copy of the given list
	 * @param list The list to reverse
	 * @return The reverse order list
	 */
	public static <T> List<T> reverseList(List<T> list) {
		List<T> newList = new ArrayList<>(list);
		Collections.reverse(newList);
		return newList;
	}
		
	/**
	 * Returns the element in the array at the index this enum value's ordinal corresponds to
	 * @param array The array to access. This should be of size equal or larger than the values array for the enum's class
	 * @param enumValue The enum to use to access the array
	 * @return The element in the array
	 */
	public static <T> T getValueFromArray(T[] array, Enum<?> enumValue) {
		return array[enumValue.ordinal()];
	}
	

	/**
	 * Flattens a collection of collections into one collection, where each subcollection's element of index i comes before
	 * the elements of every subcollection of index i + 1 or greater
	 * @param lists The collection of collections
	 * @return The flattened collection
	 */
	public static <T> List<T> concat(Collection<? extends Collection<? extends T>> lists) {
		List<T> result = new LinkedList<>();
		// Get the iterators for each collection
		List<Iterator<? extends T>> iterators = lists.stream()
				.map(collection -> collection.iterator()).filter(iterator -> iterator.hasNext())
				.collect(Collectors.toList());
		// while there are still elements unadded
		while (!iterators.isEmpty()) {
			// Put each iterators next element in the result list and remove empty iterators
			iterators = iterators.stream().filter(iterator -> {
				result.add(((Iterator<? extends T>) iterator).next());
				return iterator.hasNext();
			}).collect(Collectors.toList());
		}
		return result;
	}
	
	/**
	 * Concatenates lists, where the values from each are interspersed evenly
	 * @param lists The array of lists
	 * @return The combined list
	 */
	@SafeVarargs
	public static <T> List<T> concat(Collection<? extends T>... lists) {
		return concat(Arrays.asList(lists));
	}
	
	/**
	 * Puts all the non null supplied elements in a list together
	 * @param elements The elements, including null elements
	 * @return The list containing the non-null elements
	 */
	@SafeVarargs
	public static <T> List<T> constructListOfElements(T... elements) {
		return Arrays.stream(elements).filter(element -> element != null).collect(Collectors.toList());
	}
}
