package net.zomis.aiscores;

public interface FScorer<P, F> extends Scorer {

	/**
	 * Determine if this scorer should apply scores to the fields under the given circumstances.
	 * 
	 * @param scores Score parameters and analyzes for the scoring
	 * @return True to work with the parameters, false to exclude this scorer entirely from the current scoring process
	 */
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores);
	
	/**
	 * Determine the score for the given field and parameters. 
	 * @param field Field to score
	 * @param scores Parameters and analyzes for the scoring
	 * @return The score to give to the field
	 */
	public abstract double getScoreFor(F field, ScoreParameters<P> scores);
	
}
