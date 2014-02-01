package net.zomis.aiscores;

import java.util.ArrayList;
import java.util.List;

/**
 * Score Configuration containing instances of {@link PreScorer}, {@link PostScorer} and {@link AbstractScorer}
 *
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public class ScoreConfig<Params, Field> {
	private final ScoreSet<Params, Field> scorers;
	private final List<PostScorer<Params, Field>> postScorers;
	private final List<PreScorer<Params>> preScorers;
	
	public ScoreConfig(ScoreConfig<Params, Field> copy) {
		this(copy.preScorers, copy.postScorers, copy.scorers);
	}
	
	public ScoreConfig(List<PreScorer<Params>> preScorers, List<PostScorer<Params, Field>> postScorers, ScoreSet<Params, Field> scorers) {
		this.postScorers = new ArrayList<PostScorer<Params,Field>>(postScorers);
		this.preScorers = new ArrayList<PreScorer<Params>>(preScorers);
		this.scorers = new ScoreSet<Params, Field>(scorers);
	}

	public List<PostScorer<Params, Field>> getPostScorers() {
		return postScorers;
	}

	public ScoreSet<Params, Field> getScorers() {
		return scorers;
	}

	public List<PreScorer<Params>> getPreScorers() {
		return preScorers;
	}
	
	@Override
	public String toString() {
		return String.format("Scorers:{PreScorer: %s, PostScorer: %s, Scorers: %s}", preScorers, postScorers, scorers);
	}
}
