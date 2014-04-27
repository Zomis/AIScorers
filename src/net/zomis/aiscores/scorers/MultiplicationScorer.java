package net.zomis.aiscores.scorers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.ScoreParameters;

public class MultiplicationScorer<P, F> implements FScorer<P, F> {

	private Collection<FScorer<P, F>> scorers;

	public MultiplicationScorer(FScorer<P, F>... scorers) {
		this(new ArrayList<FScorer<P, F>>(Arrays.asList(scorers)));
	}
	
	public MultiplicationScorer(List<FScorer<P, F>> scorers) {
		this.scorers = new ArrayList<FScorer<P,F>>(scorers);
	}
	
	@SuppressWarnings("unchecked")
	public MultiplicationScorer(FScorer<P, F> scorerA, FScorer<P, F> scorerB) {
		this(new ArrayList<FScorer<P, F>>(Arrays.asList(scorerA, scorerB)));
	}
	
	@Override
	public FScorer<P, F> scoreWith(ScoreParameters<P> scores) {
		List<FScorer<P, F>> use = new ArrayList<FScorer<P,F>>(scorers.size());
		for (FScorer<P, F> scorer : scorers) {
			FScorer<P, F> useScorer = scorer.scoreWith(scores);
			if (useScorer == null)
				return null;
			use.add(useScorer);
		}
		return new MultiplicationScorer<P, F>(use);
	}
	
	@Override
	public double getScoreFor(F field, ScoreParameters<P> scores) {
		double ret = 1;
		for (FScorer<P, F> scorer : scorers)
			ret = ret * scorer.getScoreFor(field, scores);
		return ret;
	}

}
