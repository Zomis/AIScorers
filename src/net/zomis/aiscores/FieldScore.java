package net.zomis.aiscores;

import java.util.HashMap;
import java.util.Map;

/**
 * Score container for a specific field.
 *
 * @param <F> The type to apply scores to.
 */
public class FieldScore<F> implements Comparable<FieldScore<F>> {
	private int rank;
	private double score;
	private final F field;
	private final Map<Scorer, Double> specificScorers;
	private double	normalized;
	
	public FieldScore(F field) {
		this(field, false);
	}
	/**
	 * 
	 * @param field Field to score
	 * @param detailed If true, details about how much score is given from each scorer will be saved
	 */
	public FieldScore(F field, boolean detailed) {
		this.field = field;
		this.specificScorers = detailed ? new HashMap<Scorer, Double>() : null;
	}

	void addScore(FScorer<?, F> scorer, double score, double weight) {
		double add = score * weight;
		this.saveScore(scorer, add);
	}
	
	private void saveScore(Scorer scorer, double score) {
		this.score += score;
		if (scorer != null && specificScorers != null) {
			this.specificScorers.put(scorer, score);
		}
	}

	void setRank(int rank) {
		this.rank = rank;
	}
	void setNormalized(double normalized) {
		this.normalized = normalized;
	}

	@Override
	public int compareTo(FieldScore<F> other) {
		return Double.compare(this.score, other.score);
	}

	public double getScore() {
		return this.score;
	}

	/**
	 * Get the field represented by this {@link FieldScore}
	 * @return The field that this object contains score for
	 */
	public F getField() {
		return this.field;
	}

	void giveExtraScore(PostScorer<?, F> scorer, double bonus) {
		this.saveScore(scorer, bonus);
	}
	
	/**
	 * Get this field's rank.
	 * @return The rank score of this field, where 1 is the best rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Get this field's normalized score
	 * @return Normalized score, from 0 to 1.
	 */
	public double getNormalized() {
		return this.normalized;
	}

	/**
	 * Get detailed information about which scorer gave what score to this field
	 * @return Detailed information, or null if this field did not save details
	 */
	public Map<Scorer, Double> getScoreMap() {
		return this.specificScorers == null ? null : new HashMap<Scorer, Double>(this.specificScorers);
	}
	
	@Override
	public String toString() {
		return "(" + this.field + " score " + this.score + ")";
	}
}
