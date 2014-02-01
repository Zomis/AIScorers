package net.zomis.aiscores.scorers;

import java.util.Map;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.FieldScore;
import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreParameters;

/**
 * This class is NOT thread-safe.
 */
public class NormalizedScorer<Params, Field> extends AbstractScorer<Params, Field> {

	private Map<Field, FieldScore<Field>>	scores;
	private final FieldScoreProducer<Params, Field>	producer;

	public FieldScoreProducer<Params, Field> getProducer() {
		return producer;
	}
	
	@Override
	public String toString() {
		return super.toString() + "-" + producer.toString();
	}
	
	public NormalizedScorer(FieldScoreProducer<Params, Field> scoreProducer) {
		this.producer = scoreProducer;
	}

	@Override
	public boolean workWith(ScoreParameters<Params> scores) {
		FieldScores<Params, Field> fscores = producer.analyzeAndScore(scores.getParameters());
		if (fscores == null) {
			this.scores = null;
			return false;
		}
		fscores.rankScores(); // Because post-scorers might change the result, we need to re-rank the scores to have properly normalized numbers.
		this.scores = fscores.getScores();
		return true;
	}

	@Override
	public double getScoreFor(Field field, ScoreParameters<Params> scores) {
		return this.scores.get(field).getNormalized();
	}

}
