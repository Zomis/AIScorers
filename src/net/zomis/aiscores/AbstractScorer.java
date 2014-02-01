package net.zomis.aiscores;

/**
 * Scorer that is responsible to give score to fields
 * 
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public abstract class AbstractScorer<Params, Field> implements Scorer {
	/**
	 * Determine if this scorer should apply scores to the fields under the given circumstances.
	 * 
	 * @param scores Score parameters and analyzes for the scoring
	 * @return True to work with the parameters, false to exclude this scorer entirely from the current scoring process
	 */
	public boolean workWith(ScoreParameters<Params> scores) {
		return true;
	}
	/**
	 * Determine the score for the given field and parameters. 
	 * @param field Field to score
	 * @param scores Parameters and analyzes for the scoring
	 * @return The score to give to the field
	 */
	public abstract double getScoreFor(Field field, ScoreParameters<Params> scores);
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
