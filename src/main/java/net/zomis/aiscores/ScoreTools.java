package net.zomis.aiscores;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Contains methods for common use among scoring classes
 */
public class ScoreTools {
	
	/**
	 * Normalize a value to the range 0..1 (inclusive)
	 * @param value Value to normalize
	 * @param min The minimum of all values
	 * @param range The range of the values (max - min)
	 * @return
	 */
	public static double normalized(double value, double min, double range) {
		if (range == 0.0) return 0;
		return ((value - min) / range);
	}
	
	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map, final boolean descending) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
			new Comparator<Map.Entry<K, V>>() {
				@Override
				public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
					int res;
					if (descending)	res = e1.getValue().compareTo(e2.getValue());
					else res = e2.getValue().compareTo(e1.getValue());
					return res != 0 ? -res : 1; // Special fix to preserve items with equal values
				}
		    }
		);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
	
	private static Random staticRandom = new Random();
	public static <E> E getRandom(List<E> list, Random random) {
		if (list.isEmpty()) return null;
		if (random == null) 
			random = staticRandom;
		return list.get(random.nextInt(list.size()));
	}
}
