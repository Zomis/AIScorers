package net.zomis.aiscores.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreConfig;

public class Scorers {
	
	public static <Params, Field> AbstractScorer<Params, Field> multiplication(AbstractScorer<Params, Field> scorerA, AbstractScorer<Params, Field> scorerB) {
		return new MultiplicationScorer<Params, Field>(scorerA, scorerB);
	}
	public static <Params, Field> AbstractScorer<Params, Field> normalized(ScoreConfig<Params, Field> config) {
		return new NormalizedScorer2<Params, Field>(config);
	}
	public static <Params, Field> AbstractScorer<Params, Field> normalized(AbstractScorer<Params, Field> scorer) {
		return new NormalizedScorer2<Params, Field>(scorer);
	}

}
