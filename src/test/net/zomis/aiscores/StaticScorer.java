package test.net.zomis.aiscores;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;

public class StaticScorer<Params, Field> extends AbstractScorer<Params, Field> {

	private final double score;

	public StaticScorer(double score) {
		this.score = score;
	}

	@Override
	public boolean workWith(ScoreParameters<Params> scores) {
		return true;
	}

	@Override
	public double getScoreFor(Field field, ScoreParameters<Params> scores) {
		return score;
	}
}
