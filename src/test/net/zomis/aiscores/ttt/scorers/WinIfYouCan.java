package test.net.zomis.aiscores.ttt.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import test.net.zomis.aiscores.ttt.SimpleTTT;
import test.net.zomis.aiscores.ttt.SimpleTTT.TTTPlayer;
import test.net.zomis.aiscores.ttt.TTTSquare;

public class WinIfYouCan extends AbstractScorer<SimpleTTT, TTTSquare> {

	@Override
	public double getScoreFor(TTTSquare field, ScoreParameters<SimpleTTT> scores) {
		TTTPlayer opp = scores.getParameters().getCurrentPlayer();
		return scores.getAnalyze(OneMissingAnalyze.class).isOneMissing(scores.getParameters(), field, opp) ? 1 : 0;
	}

}
