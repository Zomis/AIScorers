package test.net.zomis.aiscores;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import test.net.zomis.aiscores.TestScore.Params;

public class ModScorer extends AbstractScorer<Params, Integer> {
	private int	value;

	public ModScorer(int value) {
		this.value = value;
	}
	
	@Override
	public boolean workWith(ScoreParameters<Params> scores) {
		return true;
	}

	@Override
	public double getScoreFor(Integer field, ScoreParameters<Params> scores) {
		return field % value;
	}

	@Override
	public String toString() {
		return super.toString() + value;
	}
}
