package net.zomis.aiscores;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public class FieldScoreProducer<P, F> {
	private final ScoreConfig<P, F> config;

	private boolean detailed;
	private final ScoreStrategy<P, F> scoreStrategy;

	public FieldScoreProducer(ScoreConfig<P, F> config, ScoreStrategy<P, F> strat) {
		this.config = config;
		this.scoreStrategy = strat;
	}
	
	public FieldScores<P, F> score(P params, Map<Class<?>, Object> analyzes) {
		FieldScores<P, F> scores = new FieldScores<P, F>(params, config, scoreStrategy);
		scores.setAnalyzes(analyzes);
		scores.setDetailed(this.detailed);
		scores.determineActiveScorers();
		scores.calculateScores();
		scores.rankScores();
		scores.postHandle();
		
		for (PreScorer<P> prescore : config.getPreScorers()) {
			prescore.onScoringComplete();
		}
		
		return scores;
	}
	
	public boolean isDetailed() {
		return detailed;
	}
	/**
	 * Set whether or not each FieldScore should contain detailed information about how much score the field got from all different scorers (including post scorers)
	 * @param detailed True if detailed, false otherwise.
	 */
	public void setDetailed(boolean detailed) {
		this.detailed = detailed;
	}
	
	public Map<Class<?>, Object> analyze(P param) {
		Map<Class<?>, Object> analyze = new HashMap<Class<?>, Object>();
		for (PreScorer<P> preScorers : this.config.getPreScorers()) {
			Object data = preScorers.analyze(param);
			if (data == null) 
				continue; // avoid NullPointerException
			analyze.put(data.getClass(), data);
		}
		return analyze;
	}

	public ScoreConfig<P, F> getConfig() {
		return this.config;
	}

	public FieldScores<P, F> analyzeAndScore(P params) {
		return this.score(params, this.analyze(params));
	}
}
