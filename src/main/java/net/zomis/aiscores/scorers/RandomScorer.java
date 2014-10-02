package net.zomis.aiscores.scorers;

import java.util.Random;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;

public class RandomScorer<A, B> extends AbstractScorer<A, B> {

	private Random	random;

	public RandomScorer(Random random) {
		this.random = random;
	}
	public RandomScorer() {
		this(new Random());
	}

	@Override
	public double getScoreFor(B field, ScoreParameters<A> scores) {
		return random.nextDouble();
	}

}
