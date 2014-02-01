package net.zomis.aiscores;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 *
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public class FieldScoreProducer<Params, Field> {
	private final ScoreConfig<Params, Field> config;

	private boolean detailed;
	private final ScoreStrategy<Params, Field> scoreStrategy;

	public FieldScoreProducer(ScoreConfig<Params, Field> config, ScoreStrategy<Params, Field> strat) {
		this.config = config;
		this.scoreStrategy = strat;
	}
	
	public FieldScores<Params, Field> score(Params params, Map<Class<?>, Object> analyzes) {
		FieldScores<Params, Field> scores = new FieldScores<Params, Field>(params, config, scoreStrategy);
		scores.setAnalyzes(analyzes);
		scores.setDetailed(this.detailed);
		
		scores.determineActiveScorers();
		scores.calculateScores();
		scores.rankScores();
		scores.postHandle();
		
		for (PreScorer<Params> prescore : config.getPreScorers()) {
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
	
	public Map<Class<?>, Object> analyze(Params param) {
		Map<Class<?>, Object> analyze = new HashMap<Class<?>, Object>();
		for (PreScorer<Params> preScorers : this.config.getPreScorers()) {
			Object data = preScorers.analyze(param);
			if (data == null) 
				continue; // avoid NullPointerException
			analyze.put(data.getClass(), data);
		}
		return analyze;
	}

	public ScoreConfig<Params, Field> getConfig() {
		return this.config;
	}

	public FieldScores<Params, Field> analyzeAndScore(Params params) {
		return this.score(params, this.analyze(params));
	}
}
