package net.zomis.aiscores.scorers;

import java.util.Map;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.ScoreParameters;

public class CacheScorer<P, F> implements FScorer<P, F> {

	private final Map<F, FieldScore<F>>	scores;

	public CacheScorer(Map<F, FieldScore<F>> scores) {
		this.scores = scores;
	}

	@Override
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getScoreFor(F field, ScoreParameters<P> scores) {
		FieldScore<F> value = this.scores.get(field);
		if (value == null)
			throw new NullPointerException("Cached scores did not contain " + value);
		return value.getScore();
	}

}
