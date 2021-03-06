package boardFeatures;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import support.BadArgumentException;

/**
 * This enum describes teh 16 different sectors that result when you slice a circle into 16 sections.
 * @author matthewslesinski
 *
 */
//This enum is defined specifically for usage in the EvenlySpacedCircle data structure. If you wish to use it elsewhere, proceed with caution
public enum SixteenthSector {
	UP, // 0, 1
	UP_UP_RIGHT, // 1, 2
	UP_RIGHT, // 1, 1
	UP_RIGHT_RIGHT, // 2, 1
	RIGHT, // 1, 0
	RIGHT_RIGHT_DOWN, // 2, -1
	RIGHT_DOWN, // 1, -1
	RIGHT_DOWN_DOWN, // 1, -2
	DOWN, // 0, -1
	DOWN_DOWN_LEFT, // -1, -2
	DOWN_LEFT, // -1, -1
	DOWN_LEFT_LEFT, // -2, -1
	LEFT, // -1, 0
	LEFT_LEFT_UP, // -2, 1
	LEFT_UP, // -1, 1
	LEFT_UP_UP; // -1, 2
	
	// Initialize after values() has been initialized
	static {
		for (SixteenthSector sector : values()) {
			// there's not a clear nearestDiagonal if this isn't between lines. If it is, if this is further in it's quartile, subtract 1,
			// otherwise add 1. If it isn't between lines, return the two neighbors. The reason that 1 sector is returned when between lines
			// is because the expectation is that that would only be necessary when the calling EvenlySpacedCircle contains the squares right
			// around a king, and so the nearest of those squares will always be on the corner, unless the target is specifically on a line
			// with a non-corner.
			sector.nearestSectors = sector.isBetweenLines ?
					Collections.singletonList(values()[sector.ordinal() + Math.floorMod(sector.ordinal(), 4) * -1 + 2]) :
					Arrays.asList(values()[Math.floorMod(sector.ordinal() + 1, 16)], values()[Math.floorMod(sector.ordinal() - 1, 16)]);
		}
	}
	
	/** Whether this Sector falls between lines or is a line */
	final boolean isBetweenLines;
	
	/** The neighbor(s) of this sector */
	List<SixteenthSector> nearestSectors;
	
	private SixteenthSector() {
		// It's between lines if its ordinal is odd
		this.isBetweenLines = this.ordinal() % 2 == 1;
	}
	
	/**
	 * Retrieves the neighbors of this sector, or just the one that's on a diagonal if this is between lines
	 * @return The list of those neighbors
	 */
	public List<SixteenthSector> getNearestSectors() {
		return this.nearestSectors;
	}
	
	/**
	 * Calculates which sector contains the motion described by the two provided offsets
	 * @param fileDifference The difference in files
	 * @param rankDifference The difference in ranks
	 * @return The {@code Sector} containing the motion
	 */
	private static SixteenthSector getByIncrements(int fileDifference, int rankDifference) {
		if (fileDifference == 0 && rankDifference == 0) {
			throw new BadArgumentException(Arrays.asList(fileDifference, rankDifference), int.class, "Expected a non zero increment");
		}
		int ordinal = 0;
		// If the file moves to the left or direction is straight down
		if (fileDifference < 0 || (fileDifference == 0 && rankDifference < 0)) {
			ordinal = 8;
			// reflect to other half
			fileDifference *= -1;
			rankDifference *= -1;
		}
		// If the rank moves downwards or the file moves straight right
		if (rankDifference <= 0) {
			ordinal += 4;
			// swap, and get rid of negatives
			int tmp = fileDifference;
			fileDifference = -1 * rankDifference;
			rankDifference = tmp;
		}
		// If the rank changes less than the file
		if (rankDifference <= fileDifference) {
			ordinal += 2;
			fileDifference -= rankDifference;
		}
		if (fileDifference > 0) {
			ordinal += 1;
		}
		return values()[ordinal];
	}
	
	/**
	 * Determine which {@code Sector}, when based at {@code center}, contains the {@code remote} {@code Square}
	 * @param center The base of comparison
	 * @param remote The {@code Square} that is offset by some distance from {@code center}
	 * @return The {@code Sector} containing {@code remote}
	 */
	public static SixteenthSector getRelation(Square center, Square remote) {
		int fileDifference = remote.getFile().getIndex() - center.getFile().getIndex();
		int rankDifference = remote.getRank().getIndex() - center.getRank().getIndex();			
		return getByIncrements(fileDifference, rankDifference);
	}
}