package test.net.zomis.aiscores.ttt.scorers;

import test.net.zomis.aiscores.ttt.SimpleTTT;
import test.net.zomis.aiscores.ttt.TTTSquare;
import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;

public class CornerScorer extends AbstractScorer<SimpleTTT, TTTSquare> {

	@Override
	public double getScoreFor(TTTSquare field, ScoreParameters<SimpleTTT> scores) {
		return field.getX() != 1 && field.getY() != 1 ? 1 : 0;
	}

}
