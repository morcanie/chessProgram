package support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * This class provides static utility functions
 * @author matthewslesinski
 *
 */
public class UtilityFunctions {

	
	/**
	 * Returns an array-backed list containing the integers between
	 * {@code start} (inclusive) and {@code end} (exclusive)
	 * @param start The start of the range
	 * @param end The first number after the end of the range
	 * @return The list of all the integers between
	 */
	public static int[] getPrimitiveRange(int start, int end) {
		int[] arrayOfInts = new int[end - start];
		for (int i = start; i < end; i++) {
			arrayOfInts[i - start] = i;
		}
		return arrayOfInts;
	}
	
	/**
	 * Returns an array-backed list containing the integers between
	 * {@code start} (inclusive) and {@code end} (exclusive)
	 * @param start The start of the range
	 * @param end The first number after the end of the range
	 * @return The list of all the integers between
	 */
	public static Integer[] getRange(int start, int end) {
		Integer[] arrayOfInts = new Integer[end - start];
		for (int i = start; i < end; i++) {
			arrayOfInts[i - start] = i;
		}
		return arrayOfInts;
	}
	
	/**
	 * Returns a positive, zero, or negative number to represent the sign of the given number
	 * @param value The number
	 * @return -1, 0, or 1
	 */
	public static int getSign(int value) {
		return value == 0 ? 0 : value / Math.abs(value);
	}
	
	/**
	 * Determines if the number is one of the perfect squares less than or equal to 64
	 * @param number The number to check
	 * @return true iff it's a perfect square
	 */
	public static boolean isPerfectSquareUpTo64(int number) {
		switch (number) {
		case 0:
		case 1:
		case 4:
		case 9:
		case 16:
		case 25:
		case 36:
		case 49:
		case 64:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Convenience method for XORing two numbers
	 * @param a The first number
	 * @param b The second number
	 * @return The two numbers XORed together
	 */
	public static long xor(long a, long b) {
		return a ^ b;
	}
	
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
	
	public static <T> T identity(T object) {
		return object;
	}
	
	/**
	 * Takes a function of two arguments and a default value and returns a function of one argument that just calls the original two
	 * argument function with the default value as the first argument
	 * @param originalFunction The original function
	 * @param defaultArgument The default argument to use for the function's first argument
	 * @return A function that only takes one argument
	 */
	public static <T, U, V> Function<U, V> bind(BiFunction<T, U, V> originalFunction, final T defaultArgument) {
		return otherArgument -> originalFunction.apply(defaultArgument, otherArgument);
	}
	
	/**
	 * Takes a function of two arguments and a default value and returns a function of one argument that just calls the original two
	 * argument function with the default value as the last argument
	 * @param originalFunction The original function
	 * @param defaultArgument The default argument to use for the function's first argument
	 * @return A function that only takes one argument
	 */
	public static <T, U, V> Function<T, V> bindAtEnd(BiFunction<T, U, V> originalFunction, final U defaultArgument) {
		return otherArgument -> originalFunction.apply(otherArgument, defaultArgument);
	}
	
	/**
	 * Binds an argument to an action, like the function above, except for a different type of action
	 * @param action The action to bind
	 * @param arg1 The argument to bind to that action
	 * @return The resulting action, which doesn't take any arguments
	 */
	public static <T> Runnable bind(Consumer<T> action, T arg1) {
		return () -> action.accept(arg1);
	}
	
	/**
	 * Sorts two elements naively, since there's only two so the complications of logarithmic sort are unnecessary
	 * @param list The list to sort
	 * @param comparator A comparator for sorting
	 */
	public static <T> void sortTwoElements(List<T> list, Comparator<? super T> comparator) {
		if (list.size() != 2) {
			throw new BadArgumentException(list, List.class, "Expected list of size 2");
		}
		if (comparator.compare(list.get(0), list.get(1)) > 1) {
			Collections.reverse(list);
		}
	}
	
	/**
	 * Flattens a collection of collections into one collection, where each subcollection's element of index i comes before
	 * the elements of every subcollection of index i + 1 or greater
	 * @param lists The collection of collections
	 * @return The flattened collection
	 */
	public static <T> List<T> concat(Collection<? extends Collection<? extends T>> lists) {
		List<T> result = new LinkedList<T>();
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
	
	public static boolean isNull(Object obj) {
		return obj == null;
	}
	
	/**
	 * Determines if a string does not actually contain any characters
	 * @param string The string to check
	 * @return true iff it's null or empty
	 */
	public static boolean isEmpty(String string) {
		return !isNull(string) || string.equals("");
	}
	
	/**
	 * Performs some action on each of the items, as well as some intermediate action in between each action
	 * @param items The items to perform the actions on
	 * @param action The main action to perform
	 * @param joiner The action to perform in between each main action
	 */
	public static <T> void joinActions(Iterable<Runnable> actions, Runnable joiner) {
		Iterator<Runnable> iterator = actions.iterator();
		if (!iterator.hasNext()) {
			return;
		}
		iterator.next().run();
		iterator.forEachRemaining(action -> {
			joiner.run();
			action.run();
		});
	}
}
