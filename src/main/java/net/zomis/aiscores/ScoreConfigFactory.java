package net.zomis.aiscores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Factory class for creating a {@link ScoreConfig}
 * 
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public class ScoreConfigFactory<P, F> {
	private ScoreSet<P, F>	scoreSet;
	private final List<PostScorer<P, F>> postScorers;
	private final List<PreScorer<P>> preScorers;

	public static <Params, Field> ScoreConfigFactory<Params, Field> newInstance() {
		return new ScoreConfigFactory<Params, Field>();
	}
	
	public ScoreConfigFactory() {
		this.scoreSet = new ScoreSet<P, F>();
		this.postScorers = new LinkedList<PostScorer<P, F>>();
		this.preScorers = new LinkedList<PreScorer<P>>();
	}
	
	public ScoreConfigFactory<P, F> withScoreConfig(ScoreConfig<P, F> config) {
		ScoreConfigFactory<P, F> result = this;
		
		for (PreScorer<P> pre : config.getPreScorers()) {
			if (!preScorers.contains(pre))
				result = withPreScorer(pre);
		}
		
		for (PostScorer<P, F> post : config.getPostScorers()) {
			if (!postScorers.contains(post))
				result = withPost(post);
		}
		
		for (Entry<FScorer<P, F>, Double> scorer : config.getScorers().entrySet()) {
			FScorer<P, F> key = scorer.getKey();
			double value = scorer.getValue();
			if (!scoreSet.containsKey(key))
				result = withScorer(key, value);
			else {
				scoreSet.put(key, value + scoreSet.get(key));
			}
		}
		
		return result;
	}
	
	public ScoreConfigFactory<P, F> copy() {
		ScoreConfigFactory<P, F> newInstance = new ScoreConfigFactory<P, F>();
		return newInstance.withScoreConfig(this.build());
	}

	/**
	 * Add a scorer to this factory
	 * @param scorer Scorer to add
	 * @return This factory
	 */
	public ScoreConfigFactory<P, F> withScorer(FScorer<P, F> scorer) {
		scoreSet.put(scorer, 1.0);
		return this;
	}
	/**
	 * Add a scorer with the specified weight to this factory.
	 * @param scorer Scorer to add
	 * @param weight Weight that should be applied to the scorer
	 * @return This factory
	 */
	public ScoreConfigFactory<P, F> withScorer(FScorer<P, F> scorer, double weight) {
		scoreSet.put(scorer, weight);
		return this;
	}
	/**
	 * Multiply all current {@link AbstractScorer}s in this factory's {@link ScoreSet} weights by a factor
	 * @param value Factor to multiply with
	 * @return This factory
	 */
	public ScoreConfigFactory<P, F> multiplyAll(double value) {
		ScoreSet<P, F> oldScoreSet = scoreSet;
		scoreSet = new ScoreSet<P, F>();
		for (Map.Entry<FScorer<P, F>, Double> ee : oldScoreSet.entrySet()) {
			scoreSet.put(ee.getKey(), ee.getValue() * value);
		}

		return this;
	}

	/**
	 * Add a {@link PostScorer} to this factory.
	 * @param post PostScorer to add
	 * @return This factory
	 */
	public ScoreConfigFactory<P, F> withPost(PostScorer<P, F> post) {
		postScorers.add(post);
		return this;
	}

	/**
	 * Create a {@link ScoreConfig} from this factory.
	 * @return A {@link ScoreConfig} for the {@link PreScorer}s, {@link PostScorer} and {@link AbstractScorer}s that has been added to this factory.
	 */
	public ScoreConfig<P, F> build() {
		return new ScoreConfig<P, F>(this.preScorers, this.postScorers, this.scoreSet);
	}

	/**
	 * Add a {@link PreScorer} to this factory
	 * @param analyzer PreScorer to add
	 * @return This factory
	 */
	public ScoreConfigFactory<P, F> withPreScorer(PreScorer<P> analyzer) {
		this.preScorers.add(analyzer);
		return this;
	}
	
	
	
}
