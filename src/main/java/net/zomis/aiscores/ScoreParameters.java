package net.zomis.aiscores;

import java.util.Map;

/**
 * Interface for retrieving analyzes and parameters that are used for scoring 
 * @param <P> Score parameter type
 */
public interface ScoreParameters<P> {
	/**
	 * @param clazz The class to get the analyze for
	 * @return The analyze for the specified class, or null if none was found
	 */
	<E> E getAnalyze(Class<E> clazz);
	/**
	 * @return Parameter object that are used in the scoring
	 */
	P getParameters();
	/**
	 * @return All available analyze objects
	 */
	Map<Class<?>, Object> getAnalyzes();
	ScoreStrategy<P, ?> getScoreStrategy();
}
