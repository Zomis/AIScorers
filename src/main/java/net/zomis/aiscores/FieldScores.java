package net.zomis.aiscores;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;

/**
 * Class containing scores, information about ranks, analyzes, and which score configuration that was used.
 *
 * @param <P> Score parameter type
 * @param <F> The type to apply scores to
 */
public class FieldScores<P, F> implements ScoreParameters<P> {
	private final ScoreConfig<P, F>	config;
	private final Map<F, FieldScore<F>> scores = new HashMap<F, FieldScore<F>>();
	private final P params;
	private final ScoreStrategy<P, F> scoreStrategy;
	
	private Map<FScorer<P, F>, Double> activeScorers;
	private List<List<FieldScore<F>>> rankedScores;
	private Map<Class<?>, Object> analyzes;
	private boolean detailed;
	
	@SuppressWarnings("unchecked")
	public <E> E getAnalyze(Class<E> clazz) {
		E value = (E) this.analyzes.get(clazz);
		if (value == null)
			throw new NullPointerException("Analyze " + clazz + " not found. Did you forget to add a PreScorer using ScoreConfigFactory.withPreScorer?");
		return value;
	}
	
	@Override
	public Map<Class<?>, Object> getAnalyzes() {
		return new HashMap<Class<?>, Object>(this.analyzes);
	}
	
	FieldScores(P params, ScoreConfig<P, F> config, ScoreStrategy<P, F> strat) {
		this.params = params;
		this.config = config;
		this.scoreStrategy = strat;
	}
	
	@Override
	public ScoreStrategy<P, F> getScoreStrategy() {
		return scoreStrategy;
	}

	/**
	 * Call each {@link AbstractScorer}'s workWith method to determine if that scorer is currently applicable
	 */
	void determineActiveScorers() {
		activeScorers = new HashMap<FScorer<P, F>, Double>();

		for (FScorer<P, F> scorer : config.getScorers().keySet()) {
			FScorer<P, F> realScorer = scorer.scoreWith(this);
			if (realScorer != null) {
				activeScorers.put(realScorer, config.getScorers().get(scorer));
			}
		}
	}

	/**
	 * Process the {@link AbstractScorer}s to let them add their score for each field. Uses the {@link ScoreStrategy} associated with this object to determine which fields should be scored.
	 */
	void calculateScores() {
		if (config == null)
			throw new NullPointerException("Config is null");
		if (config.getScorers() == null)
			throw new NullPointerException("Config does not have any scorers");
		for (F field : this.scoreStrategy.getFieldsToScore(params)) {
			if (!this.scoreStrategy.canScoreField(this, field))
				continue;
			
			FieldScore<F> fscore = new FieldScore<F>(field, detailed);
			for (Entry<FScorer<P, F>, Double> ee : activeScorers.entrySet()) {
				FScorer<P, F> scorer = ee.getKey();
				double computedScore = scorer.getScoreFor(field, this);
				double weight = ee.getValue();
				fscore.addScore(scorer, computedScore, weight);
			}
			scores.put(field, fscore);
		}
	}

	/**
	 * Call {@link PostScorer}s to let them do their job, after the main scorers have been processed.
	 */
	void postHandle() {
		for (PostScorer<P, F> post : this.config.getPostScorers()) {
			post.handle(this);
			this.rankScores(); // Because post-scorers might change the result, re-rank the scores to always have proper numbers.
		}
	}

	@Override
	public P getParameters() {
		return this.params;
	}

	/**
	 * Get a List of all the ranks. Each rank is a list of all the {@link FieldScore} objects in that rank
	 * @return A list of all the ranks, where the first item in the list is the best rank
	 */
	public List<List<FieldScore<F>>> getRankedScores() {
		return rankedScores;
	}

	/**
	 * @return A {@link HashMap} copy of the scores that are contained in this object
	 */
	public Map<F, FieldScore<F>> getScores() {
		return new HashMap<F, FieldScore<F>>(this.scores);
	}

	/**
	 * Get the {@link FieldScore} object for a specific field.
	 * @param field Field to get data for
	 * @return FieldScore for the specified field.
	 */
	public FieldScore<F> getScoreFor(F field) {
		return scores.get(field);
	}

	/**
	 * (Re-)calculates rankings for all the fields, and also calculates a normalization of their score
	 */
	public void rankScores() {
		SortedSet<Entry<F, FieldScore<F>>> sorted = ScoreTools.entriesSortedByValues(this.scores, true);
		rankedScores = new LinkedList<List<FieldScore<F>>>();
		if (sorted.isEmpty())
			return;
		double minScore = sorted.last().getValue().getScore();
		double maxScore = sorted.first().getValue().getScore();
		double lastScore = maxScore + 1;
		
		int rank = 0;
		List<FieldScore<F>> currentRank = new LinkedList<FieldScore<F>>();
		for (Entry<F, FieldScore<F>> score : sorted) {
			if (lastScore != score.getValue().getScore()) {
				lastScore = score.getValue().getScore();
				rank++;
				currentRank = new LinkedList<FieldScore<F>>();
				rankedScores.add(currentRank);
			}
			score.getValue().setRank(rank);
			double normalized = ScoreTools.normalized(score.getValue().getScore(), minScore, maxScore - minScore);

			score.getValue().setNormalized(normalized);
			currentRank.add(score.getValue());
		}
	}
	
	/**
	 * Get all {@link FieldScore} objects for a specific rank
	 * @param rank From 1 to getRankLength() 
	 * @return A list of all FieldScores for the specified rank
	 */
	public List<FieldScore<F>> getRank(int rank) {
		if (rankedScores.isEmpty()) return null;
		return rankedScores.get(rank - 1);
	}
	/**
	 * Get the number of ranks
	 * @return The number of ranks
	 */
	public int getRankCount() {
		return rankedScores.size();
	}
	/**
	 * @return The score configuration that was used to calculate these field scores.
	 */
	public ScoreConfig<P, F> getConfig() {
		return this.config;
	}

	void setAnalyzes(Map<Class<?>, Object> analyzes) {
		this.analyzes = new HashMap<Class<?>, Object>(analyzes);
	}

	/**
	 * @param detailed True to store detailed information about which scorer gives which score to which field. False otherwise
	 */
	public void setDetailed(boolean detailed) {
		this.detailed = detailed;
	}
}
