package test.net.zomis.aiscores.ttt.scorers;

import test.net.zomis.aiscores.ttt.SimpleTTT;
import test.net.zomis.aiscores.ttt.TTTSquare;
import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;

public class CenterScorer extends AbstractScorer<SimpleTTT, TTTSquare> {

	@Override
	public double getScoreFor(TTTSquare field, ScoreParameters<SimpleTTT> scores) {
		if (field.getX() == 1 && field.getY() == 1)
			return 1;
		return 0;
	}

}
