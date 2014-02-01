package net.zomis.aiscores;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating a {@link ScoreConfig}
 * 
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public class ScoreConfigFactory<Params, Field> {
	private ScoreSet<Params, Field>	scoreSet;
	private final List<PostScorer<Params, Field>> postScorers;
	private final List<PreScorer<Params>> preScorers;

	public static <Params, Field> ScoreConfigFactory<Params, Field> newInstance() {
		return new ScoreConfigFactory<Params, Field>();
	}
	
	public ScoreConfigFactory() {
		this.scoreSet = new ScoreSet<Params, Field>();
		this.postScorers = new LinkedList<PostScorer<Params, Field>>();
		this.preScorers = new LinkedList<PreScorer<Params>>();
	}

	/**
	 * Add a scorer to this factory
	 * @param scorer Scorer to add
	 * @return This factory
	 */
	public ScoreConfigFactory<Params, Field> withScorer(AbstractScorer<Params, Field> scorer) {
		scoreSet.put(scorer, 1.0);
		return this;
	}
	/**
	 * Add a scorer with the specified weight to this factory.
	 * @param scorer Scorer to add
	 * @param weight Weight that should be applied to the scorer
	 * @return This factory
	 */
	public ScoreConfigFactory<Params, Field> withScorer(AbstractScorer<Params, Field> scorer, double weight) {
		scoreSet.put(scorer, weight);
		return this;
	}
	/**
	 * Multiply all current {@link AbstractScorer}s in this factory's {@link ScoreSet} weights by a factor
	 * @param value Factor to multiply with
	 * @return This factory
	 */
	public ScoreConfigFactory<Params, Field> multiplyAll(double value) {
		ScoreSet<Params, Field> oldScoreSet = scoreSet;
		scoreSet = new ScoreSet<Params, Field>();
		for (Map.Entry<AbstractScorer<Params, Field>, Double> ee : oldScoreSet.entrySet()) {
			scoreSet.put(ee.getKey(), ee.getValue() * value);
		}

		return this;
	}

	/**
	 * Add a {@link PostScorer} to this factory.
	 * @param post PostScorer to add
	 * @return This factory
	 */
	public ScoreConfigFactory<Params, Field> withPost(PostScorer<Params, Field> post) {
		postScorers.add(post);
		return this;
	}

	/**
	 * Create a {@link ScoreConfig} from this factory.
	 * @return A {@link ScoreConfig} for the {@link PreScorer}s, {@link PostScorer} and {@link AbstractScorer}s that has been added to this factory.
	 */
	public ScoreConfig<Params, Field> build() {
		return new ScoreConfig<Params, Field>(this.preScorers, this.postScorers, this.scoreSet);
	}

	/**
	 * Add a {@link PreScorer} to this factory
	 * @param analyzer PreScorer to add
	 * @return This factory
	 */
	public ScoreConfigFactory<Params, Field> withPreScorer(PreScorer<Params> analyzer) {
		this.preScorers.add(analyzer);
		return this;
	}
	
	
	
}
