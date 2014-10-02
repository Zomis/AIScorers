package net.zomis.aiscores.scorers;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.ScoreParameters;

public class PredicateScorer<P, F> implements FScorer<P, F> {

	private final ScorePredicate<F> predicate;

	public static interface ScorePredicate<F> {
		boolean matches(F field);
	}
	
	public PredicateScorer(ScorePredicate<F> predicate) {
		this.predicate = predicate;
	}
	
	@Override
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores) {
		return this;
	}
	
	@Override
	public double getScoreFor(F field, ScoreParameters<P> scores) {
		return predicate.matches(field) ? 1 : 0;
	}

}
