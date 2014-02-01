package test.net.zomis.aiscores;

import java.util.Collection;
import java.util.LinkedList;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;
import test.net.zomis.aiscores.TestScore.Params;

public class IntScores implements ScoreStrategy<Params, Integer> {

	@Override
	public Collection<Integer> getFieldsToScore(Params params) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = -10; i <= 10000; i++) {
			list.add(i);
		}
		return list;
	}

	@Override
	public boolean canScoreField(ScoreParameters<Params> parameters, Integer field) {
		return parameters.getParameters().isOK(field);
	}

}
