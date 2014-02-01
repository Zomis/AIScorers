package net.zomis.aiscores.scorers;

import net.zomis.aiscores.AbstractScorer;
import net.zomis.aiscores.ScoreParameters;

public abstract class SubclassScorer<Params, A, B extends A> extends AbstractScorer<Params, A> {
	
	private final Class<? extends B> clazz;

	public SubclassScorer(Class<? extends B> bClass) {
		this.clazz = bClass;
	}

	@Override
	public final double getScoreFor(A field, ScoreParameters<Params> scores) {
		Class<?> clz = IsSubclassScorer.classFor(field);
		if (clz == null)
			return clazz == null ? this.scoreSubclass(null, scores) : 0;
		if (clazz.isAssignableFrom(clz)) {
			double value = scoreSubclass(clazz.cast(field), scores);
//			CustomFacade.getLog().i("Subclass scorer: " + value + " for " + field + ", " + scores);
			return value;
		}
		else return 0;
	}

	public abstract double scoreSubclass(B cast, ScoreParameters<Params> scores);

	public String getName() {
		return this.getClass().getSimpleName() + "-" + clazz.getSimpleName();
	}
	
}
