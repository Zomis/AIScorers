package test.net.zomis.aiscores.ttt.scorers;

import test.net.zomis.aiscores.ttt.SimpleTTT;
import net.zomis.aiscores.PreScorer;

public class OneMissingAnalyzer implements PreScorer<SimpleTTT> {

	@Override
	public Object analyze(SimpleTTT params) {
		return new OneMissingAnalyze();
	}

	@Override
	public void onScoringComplete() {
	}

}
