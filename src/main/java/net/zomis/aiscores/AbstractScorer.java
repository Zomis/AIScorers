package net.zomis.aiscores;

/**
 * Scorer that is responsible to give score to fields
 * 
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public abstract class AbstractScorer<P, F> implements FScorer<P, F> {
	
	@Override
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores) {
		return workWith(scores) ? this : null;
	}
	
	/**
	 * Determine if this scorer should apply scores to the fields under the given circumstances.
	 * 
	 * @param scores Score parameters and analyzes for the scoring
	 * @return True to work with the parameters, false to exclude this scorer entirely from the current scoring process
	 */
	public boolean workWith(ScoreParameters<P> scores) {
		return true;
	}
	
	/**
	 * Determine the score for the given field and parameters. 
	 * @param field Field to score
	 * @param scores Parameters and analyzes for the scoring
	 * @return The score to give to the field
	 */
	public abstract double getScoreFor(F field, ScoreParameters<P> scores);
	
}
