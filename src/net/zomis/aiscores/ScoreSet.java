package net.zomis.aiscores;

import java.util.LinkedHashMap;

/**
 * Map of {@link AbstractScorer}s and the weight that should be applied to them.
 *
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public class ScoreSet<Params, Field> extends LinkedHashMap<AbstractScorer<Params, Field>, Double> {
	private static final long	serialVersionUID	= 5924233965213820945L;

	ScoreSet() {
	}
	ScoreSet(ScoreSet<Params, Field> copy) {
		super(copy);
	}
}
