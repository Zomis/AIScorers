package net.zomis.aiscores.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedSet;

import net.zomis.aiscores.FScorer;
import net.zomis.aiscores.PostScorer;
import net.zomis.aiscores.PreScorer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.ScoreSet;
import net.zomis.aiscores.ScoreTools;

public class ScoreGenetics<A, B, C> {
	private final GeneticInterface<A, B, C> geneticFace;
	private final Random random;
	
	public ScoreGenetics(GeneticInterface<A, B, C> fitness, Random random) {
		this.geneticFace = fitness;
		this.random = random != null ? random : new Random();
	}
	
	public void output(List<C> configs) {
		for (C ai : configs) {
			ScoreConfig<A, B> conf = geneticFace.getConfigFor(ai);
			System.out.println(conf);
		}
	}

	public List<C> iterationFight(List<C> ais) {
		final int keep = ais.size() / 2;
		
		Map<C, Double> fitness = this.geneticFace.fitness(ais);
		System.out.println(fitness);
		SortedSet<Entry<C, Double>> results = ScoreTools.entriesSortedByValues(fitness, true);
		List<C> qualified = new ArrayList<C>();
		
		int i = 0;
		for (Entry<C, Double> ee : results) {
			if (i < keep)
				qualified.add(ee.getKey());
			i++;
		}
		
		// Make modifications of the qualified AIs
		for (C ai : new ArrayList<C>(qualified)) {
			qualified.add(modify(ai));
		}
		return qualified;
	}
	
	private C modify(C ai) {
		ScoreConfig<A, B> config = this.geneticFace.getConfigFor(ai);
		ScoreSet<A, B> scores = config.getScorers();
		List<FScorer<A, B>> scorerList = new ArrayList<FScorer<A, B>>(scores.keySet());
		FScorer<A, B> rand = ScoreTools.getRandom(scorerList, random);
		
		ScoreConfigFactory<A, B> factory = new ScoreConfigFactory<A, B>();
		for (PreScorer<A> ee : config.getPreScorers())
			factory.withPreScorer(ee);
		for (PostScorer<A, B> ee : config.getPostScorers())
			factory.withPost(ee);
		
		for (Entry<FScorer<A, B>, Double> ee : scores.entrySet()) {
			if (ee.getKey() == rand)
				factory.withScorer(ee.getKey(), (random.nextDouble() - 0.5) * 2);
			else factory.withScorer(ee.getKey(), ee.getValue());
		}
		
		return this.geneticFace.newFromConfig(factory.build());
	}

}
