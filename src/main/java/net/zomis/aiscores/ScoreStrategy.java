package net.zomis.aiscores;

import java.util.Collection;

/**
 * Responsible for determining which fields to score with the specified params
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public interface ScoreStrategy<P, F> {
	/**
	 * Determine the collection of fields to score given the specified params
	 * @param params Parameter for the scoring
	 * @return A collection of fields which to score
	 */
	Collection<F> getFieldsToScore(P params);
	/**
	 * Determine whether or not scoring of a particular field should be done, allowing {@link PreScorer}s from the analyze to be taken into consideration.
	 * @param parameters Parameters, including analyze objects created by {@link PreScorer}s.
	 * @param field The field to score or not to score, that is the question.
	 * @return True to score field, false to skip scoring of it.
	 */
	boolean canScoreField(ScoreParameters<P> parameters, F field);
}
