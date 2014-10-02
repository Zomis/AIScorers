package test.net.zomis.aiscores.ttt.scorers;

import test.net.zomis.aiscores.ttt.SimpleTTT;
import test.net.zomis.aiscores.ttt.TTTSquare;
import test.net.zomis.aiscores.ttt.TTWinCondition;
import test.net.zomis.aiscores.ttt.SimpleTTT.TTTPlayer;

public class OneMissingAnalyze {

	public boolean isOneMissing(SimpleTTT board, TTTSquare field, TTTPlayer opp) {
		for (TTWinCondition win : board.getWinConds()) {
			if (!win.hasWinnable(field))
				continue;
			if (win.hasCurrently(opp) == 2)
				return true;
		}
		return false;
	}
	
	

}
