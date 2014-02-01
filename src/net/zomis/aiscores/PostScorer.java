package net.zomis.aiscores;

/**
 * A scorer that can apply/modify scores after the regular {@link AbstractScorer}s have done their job.
 *
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public abstract class PostScorer<Params, Field> implements Scorer {
	@Override
	public String toString() {
		return "Post-" + this.getClass().getSimpleName();
	}

	/**
	 * Optionally apply any scores to the given {@link FieldScores} object.
	 * @param scores The collection of scores to work on.
	 */
	public abstract void handle(FieldScores<Params, Field> scores);
	
	/**
	 * Add score to a field
	 * @param fscore {@link FieldScore} container for the field
	 * @param score Score to give
	 */
	protected void addScore(FieldScore<Field> fscore, double score) {
		if (fscore == null) 
			throw new NullPointerException("FieldScore was null.");
		fscore.giveExtraScore(this, score);
	}
	/**
	 * Add score to a field
	 * @param scores {@link FieldScores} object containing the field
	 * @param field Field to apply score for
	 * @param score Score to apply
	 */
	protected void addScore(FieldScores<Params, Field> scores, Field field, double score) {
		FieldScore<Field> fscore = scores.getScoreFor(field);
		this.addScore(fscore, score);
	}
	/**
	 * Set score to an exact value for a field
	 * @param scores {@link FieldScores} object containing the field
	 * @param field Field to apply score for
	 * @param score Score to apply
	 */
	protected void setScore(FieldScores<Params, Field> scores, Field field, double score) {
		FieldScore<Field> fscore = scores.getScoreFor(field);
		if (fscore == null)
			throw new NullPointerException("Field " + field + " does not have any matching FieldScore.");
		fscore.giveExtraScore(this, score - fscore.getScore());
	}
}
