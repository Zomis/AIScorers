package net.zomis.aiscores.scorers;

import java.util.Map;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;

/**
 * This class is NOT thread-safe.
 */
public class NormalizedScorer2<Params, Field> extends AbstractScorer<Params, Field> {

	private Map<Field, FieldScore<Field>>	scores;
	private FieldScoreProducer<Params, Field>	producer;
	private final ScoreConfig<Params, Field> config;
	
	@Override
	public String toString() {
		return super.toString() + "-" + config;
	}
	
	public NormalizedScorer2(ScoreConfig<Params, Field> config) {
		this.config = config;
	}
	public NormalizedScorer2(AbstractScorer<Params, Field> scorer) {
		this.config = new ScoreConfigFactory<Params, Field>().withScorer(scorer).build();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean workWith(ScoreParameters<Params> scores) {
		producer = new FieldScoreProducer<Params, Field>(config, (ScoreStrategy<Params, Field>) scores.getScoreStrategy());
		FieldScores<Params, Field> fscores = producer.score(scores.getParameters(), scores.getAnalyzes());
		if (fscores == null) {
			this.scores = null;
			return false;
		}
		this.scores = fscores.getScores();
		return true;
	}

	@Override
	public double getScoreFor(Field field, ScoreParameters<Params> scores) {
		return this.scores.get(field).getNormalized();
	}

}
