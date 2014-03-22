package test.net.zomis.aiscores.ttt.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;
import test.net.zomis.aiscores.ttt.SimpleTTT;
import test.net.zomis.aiscores.ttt.SimpleTTT.TTTPlayer;
import test.net.zomis.aiscores.ttt.TTTSquare;
import test.net.zomis.aiscores.ttt.TTWinCondition;

public class CreateTrap extends AbstractScorer<SimpleTTT, TTTSquare> {

	@Override
	public double getScoreFor(TTTSquare field, ScoreParameters<SimpleTTT> scores) {
		int i = 0;
		TTTPlayer player = scores.getParameters().getCurrentPlayer();
		for (TTWinCondition win : scores.getParameters().getWinConds()) {
			if (!win.hasWinnable(field))
				continue;
			if (!win.isWinnable(player))
				continue;
			i++;
		}
		
		return i;
	}

}
