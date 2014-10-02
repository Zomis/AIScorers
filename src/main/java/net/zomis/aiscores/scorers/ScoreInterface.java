package net.zomis.aiscores.scorers;

import net.zomis.aiscores.ScoreParameters;

public interface ScoreInterface<P, F> {
	double score(F field, ScoreParameters<P> scores);
}
