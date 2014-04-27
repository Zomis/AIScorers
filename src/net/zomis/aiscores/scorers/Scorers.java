package net.zomis.aiscores.scorers;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.ScoreConfig;

public class Scorers {
	
	public static <Params, Field> FScorer<Params, Field> multiplication(FScorer<Params, Field> scorerA, FScorer<Params, Field> scorerB) {
		return new MultiplicationScorer<Params, Field>(scorerA, scorerB);
	}
	public static <Params, Field> FScorer<Params, Field> normalized(ScoreConfig<Params, Field> config) {
		return new NormalizedScorer2<Params, Field>(config);
	}
	public static <Params, Field> FScorer<Params, Field> normalized(FScorer<Params, Field> scorer) {
		return new NormalizedScorer2<Params, Field>(scorer);
	}

}
