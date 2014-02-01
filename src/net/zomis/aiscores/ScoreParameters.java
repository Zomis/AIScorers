package net.zomis.aiscores;

/**
 * Interface for retrieving analyzes and parameters that are used for scoring 
 * @param <Params> Score parameter type
 */
public interface ScoreParameters<Params> {
	/**
	 * @param clazz The class to get the analyze for
	 * @return The analyze for the specified class, or null if none was found
	 */
	<E> E getAnalyze(Class<E> clazz);
	/**
	 * @return Parameter object that are used in the scoring
	 */
	Params getParameters();
}
