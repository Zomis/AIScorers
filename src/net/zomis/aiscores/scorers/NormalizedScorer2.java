package net.zomis.aiscores.scorers;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;

/**
 * This class SHOULD be thread-safe now
 */
public class NormalizedScorer2<P, F> implements FScorer<P, F> {

	private final ScoreConfig<P, F> config;
	
	@Override
	public String toString() {
		return super.toString() + "-" + config;
	}
	
	public NormalizedScorer2(ScoreConfig<P, F> config) {
		this.config = config;
	}
	
	public NormalizedScorer2(FScorer<P, F> scorer) {
		this(new ScoreConfigFactory<P, F>().withScorer(scorer).build());
	}

	@SuppressWarnings("unchecked")
	@Override
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores) {
		FieldScoreProducer<P, F> producer = new FieldScoreProducer<P, F>(config, (ScoreStrategy<P, F>) scores.getScoreStrategy());
		FieldScores<P, F> fscores = producer.score(scores.getParameters(), scores.getAnalyzes());
		if (fscores == null) {
			return null;
		}
		return new CacheScorer<P, F>(fscores.getScores());
	}
	
	@Override
	public double getScoreFor(F field, ScoreParameters<P> scores) {
		throw new UnsupportedOperationException();
	}

}
