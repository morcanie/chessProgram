package support;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionalUtilityFunctions extends CollectionUtilityFunctions {

	/**
	 * A function that returns the input directly, without doing anything else
	 * @param object The input
	 * @return The input
	 */
	public static <T> T identity(T object) {
		return object;
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
}
