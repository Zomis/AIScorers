package test.net.zomis.aiscores;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;

public class TestScore {
	public static class Params {
		public boolean isOK(Integer field) {
			return field >= 0 && field <= 100;
		}
	}
	
	public static void main(String[] args) {
		ScoreConfigFactory<Params, Integer> config = new ScoreConfigFactory<Params, Integer>();
		config.withScorer(new DivisibleByScorer(21), 1);
		config.withScorer(new DivisibleByScorer(2), 1);
		config.withScorer(new CloseToScorer(23), 0.01);
		config.withScorer(new CloseToScorer(50), 0.01);
		config.withScorer(new ModScorer(10), 0.001);
		
		ScoreConfig<Params, Integer> built = config.build();
		System.out.println("Using scorers: " + built.getScorers());
		
		FieldScoreProducer<Params, Integer> is = new FieldScoreProducer<Params, Integer>(built, new IntScores());
		FieldScores<Params, Integer> scores = is.analyzeAndScore(new Params());
//		ZomisUtils.echo(scores.getBestFields());
		for (int rank = 1; rank <= scores.getRankCount(); rank++) {
			System.out.println("Rank " + rank + " = " + scores.getRank(rank));
		}
	}
	
}
