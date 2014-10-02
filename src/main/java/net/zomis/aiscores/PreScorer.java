package net.zomis.aiscores;

/**
 * Interface for performing analyze work before scorers start scoring.
 * @param <P> Score parameter type
 */
public interface PreScorer<P> {
	/**
	 * Perform analyze and return result of analyze
	 * @param params The score parameters
	 * @return The object that can be retrieved by the scorers
	 */
	Object analyze(P params);
	
	/**
	 * Method that can be used to clean-up variables and resources. Called when a {@link FieldScores} object has been fully completed.
	 */
	void onScoringComplete();
}
