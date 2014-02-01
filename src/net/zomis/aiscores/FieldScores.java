package net.zomis.aiscores;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class containing scores, information about ranks, analyzes, and which score configuration that was used.
 *
 * @param <Params> Score parameter type
 * @param <Field> The type to apply scores to
 */
public class FieldScores<Params, Field> implements ScoreParameters<Params> {
	private final ScoreConfig<Params, Field>	config;
	private final Map<Field, FieldScore<Field>> scores = new HashMap<Field, FieldScore<Field>>();
	private final Params 						params;
	private final ScoreStrategy<Params, Field> 	scoreStrategy;
	
	private List<AbstractScorer<Params, Field>> activeScorers;
	private List<List<FieldScore<Field>>> 		rankedScores;
	private Map<Class<?>, Object> analyzes;
	private boolean detailed;
	
	@SuppressWarnings("unchecked")
	public <E> E getAnalyze(Class<E> clazz) {
		return (E) this.analyzes.get(clazz);
	}
	
	public Map<Class<?>, Object> getAnalyzes() {
		return new HashMap<Class<?>, Object>();
	}
	
	FieldScores(Params params, ScoreConfig<Params, Field> config, ScoreStrategy<Params, Field> strat) {
		this.params = params;
		this.config = config;
		this.scoreStrategy = strat;
	}

	/**
	 * Call each {@link AbstractScorer}'s workWith method to determine if that scorer is currently applicable
	 */
	void determineActiveScorers() {
		activeScorers = new ArrayList<AbstractScorer<Params, Field>>();

		for (AbstractScorer<Params, Field> scorer : config.getScorers().keySet()) {
			if (scorer.workWith(this)) {
				activeScorers.add(scorer);
			}
		}
	}

	/**
	 * Process the {@link AbstractScorer}s to let them add their score for each field. Uses the {@link ScoreStrategy} associated with this object to determine which fields should be scored.
	 */
	void calculateScores() {
		for (Field field : this.scoreStrategy.getFieldsToScore(params)) {
			if (!this.scoreStrategy.canScoreField(this, field))
				continue;
			
			FieldScore<Field> fscore = new FieldScore<Field>(field, detailed);
			for (AbstractScorer<Params, Field> scorer : activeScorers) {
				double computedScore = scorer.getScoreFor(field, this);
				double weight = config.getScorers().get(scorer);
				fscore.addScore(scorer, computedScore, weight);
			}
			scores.put(field, fscore);
		}
	}

	/**
	 * Call {@link PostScorer}s to let them do their job, after the main scorers have been processed.
	 */
	void postHandle() {
		for (PostScorer<Params, Field> post : this.config.getPostScorers()) {
			post.handle(this);
		}
	}

	@Override
	public Params getParameters() {
		return this.params;
	}

	/**
	 * Get a List of all the ranks. Each rank is a list of all the {@link FieldScore} objects in that rank
	 * @return A list of all the ranks, where the first item in the list is the best rank
	 */
	public List<List<FieldScore<Field>>> getRankedScores() {
		return rankedScores;
	}

	/**
	 * @return A {@link HashMap} copy of the scores that are contained in this object
	 */
	public Map<Field, FieldScore<Field>> getScores() {
		return new HashMap<Field, FieldScore<Field>>(this.scores);
	}

	/**
	 * Get the {@link FieldScore} object for a specific field.
	 * @param field Field to get data for
	 * @return FieldScore for the specified field.
	 */
	public FieldScore<Field> getScoreFor(Field field) {
		return scores.get(field);
	}

	/**
	 * (Re-)calculates rankings for all the fields, and also calculates a normalization of their score
	 */
	public void rankScores() {
		SortedSet<Entry<Field, FieldScore<Field>>> sorted = entriesSortedByValues(this.scores, true);
		rankedScores = new LinkedList<List<FieldScore<Field>>>();
		if (sorted.isEmpty())
			return;
		double minScore = sorted.last().getValue().getScore();
		double maxScore = sorted.first().getValue().getScore();
		double lastScore = maxScore + 1;
		
		int rank = 0;
		List<FieldScore<Field>> currentRank = new LinkedList<FieldScore<Field>>();
		for (Entry<Field, FieldScore<Field>> score : sorted) {
			if (lastScore != score.getValue().getScore()) {
				lastScore = score.getValue().getScore();
				rank++;
				currentRank = new LinkedList<FieldScore<Field>>();
				rankedScores.add(currentRank);
			}
			score.getValue().setRank(rank);
			double normalized = normalized(score.getValue().getScore(), minScore, maxScore - minScore);

			score.getValue().setNormalized(normalized);
			currentRank.add(score.getValue());
		}
	}
	
	/**
	 * Normalize a value to the range 0..1 (inclusive)
	 * @param value Value to normalize
	 * @param min The minimum of all values
	 * @param range The range of the values (max - min)
	 * @return
	 */
	private static double normalized(double value, double min, double range) {
		if (range == 0.0) return 0;
		return ((value - min) / range);
	}
	
	private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map, final boolean descending) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
			new Comparator<Map.Entry<K, V>>() {
				@Override
				public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
					int res;
					if (descending)	res = e1.getValue().compareTo(e2.getValue());
					else res = e2.getValue().compareTo(e1.getValue());
					return res != 0 ? -res : 1; // Special fix to preserve items with equal values
				}
		    }
		);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
	
	/**
	 * Get all {@link FieldScore} objects for a specific rank
	 * @param rank From 1 to getRankLength() 
	 * @return A list of all FieldScores for the specified rank
	 */
	public List<FieldScore<Field>> getRank(int rank) {
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
	public ScoreConfig<Params, Field> getConfig() {
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
