package hashing;

import java.util.Random;

import static support.UtilityFunctions.*;

/**
 * Generates random longs by using Java's {@code Random} class to randomly set each bit of the longs
 * @author matthewslesinski
 */
public class PseudoRandomNumbers implements RandomNumberGenerator {

	/** A generator for supplying random numbers */
	private static final Random generator = new Random();
	
	/**
	 * Generates the next random long to populate the sequence with
	 * @return The next long
	 */
	private static long getNextLong() {
		long toReturn = 0;
		for (int i : getPrimitiveRange(0, 64)) {
			long mask = generator.nextBoolean() ? 1L << i : 0L;
			toReturn |= mask;
		}
		return toReturn;
	}
	
	@Override
	public long[] generateNumbers(int length) {
		long[] numberStore = new long[length];
		for (int i : getPrimitiveRange(0, length)) {
			numberStore[i] = getNextLong();
		}
		return numberStore;
	}

}
