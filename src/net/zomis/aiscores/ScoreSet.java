package net.zomis.aiscores;

import java.util.LinkedHashMap;

/**
 * Map of {@link AbstractScorer}s and the weight that should be applied to them.
 *
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public class ScoreSet<P, F> extends LinkedHashMap<FScorer<P, F>, Double> {
	private static final long	serialVersionUID	= 5924233965213820945L;

	ScoreSet() {
	}
	ScoreSet(ScoreSet<P, F> copy) {
		super(copy);
	}
}
