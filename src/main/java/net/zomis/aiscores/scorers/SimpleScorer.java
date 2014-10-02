package net.zomis.aiscores.scorers;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.ScoreParameters;

public class SimpleScorer<P, F> implements FScorer<P, F> {

	private final ScoreInterface<P, F> scorer;

	public SimpleScorer(ScoreInterface<P, F> scorer) {
		this.scorer = scorer;
	}
	
	@Override
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores) {
		return this;
	}

	@Override
	public double getScoreFor(F field, ScoreParameters<P> scores) {
		return scorer.score(field, scores);
	}
	
}
