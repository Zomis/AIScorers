package net.zomis.aiscores;

import java.util.ArrayList;
import java.util.List;

/**
 * Score Configuration containing instances of {@link PreScorer}, {@link PostScorer} and {@link AbstractScorer}
 *
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public class ScoreConfig<P, F> {
	private final ScoreSet<P, F> scorers;
	private final List<PostScorer<P, F>> postScorers;
	private final List<PreScorer<P>> preScorers;
	
	public ScoreConfig(ScoreConfig<P, F> copy) {
		this(copy.preScorers, copy.postScorers, copy.scorers);
	}
	
	public ScoreConfig(List<PreScorer<P>> preScorers, List<PostScorer<P, F>> postScorers, ScoreSet<P, F> scorers) {
		this.postScorers = new ArrayList<PostScorer<P,F>>(postScorers);
		this.preScorers = new ArrayList<PreScorer<P>>(preScorers);
		this.scorers = new ScoreSet<P, F>(scorers);
	}

	public List<PostScorer<P, F>> getPostScorers() {
		return postScorers;
	}

	public ScoreSet<P, F> getScorers() {
		return scorers;
	}

	public List<PreScorer<P>> getPreScorers() {
		return preScorers;
	}
	
	@Override
	public String toString() {
		return "Scorers:{PreScorer: " + preScorers + ", PostScorer: " + postScorers + ", Scorers: " + scorers + "}";
	}
}
