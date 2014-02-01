package net.zomis.aiscores.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.FieldScoreProducer;

public class Scorers {
	
	public static <Params, Field> AbstractScorer<Params, Field> multiplication(AbstractScorer<Params, Field> scorerA, AbstractScorer<Params, Field> scorerB) {
		return new MultiplicationScorer<Params, Field>(scorerA, scorerB);
	}
	public static <Params, Field> AbstractScorer<Params, Field> normalized(FieldScoreProducer<Params, Field> producer) {
		return new NormalizedScorer<Params, Field>(producer);
	}

}
