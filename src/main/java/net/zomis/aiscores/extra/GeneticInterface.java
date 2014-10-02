package net.zomis.aiscores.extra;

import java.util.List;
import java.util.Map;

import net.zomis.aiscores.ScoreConfig;

public interface GeneticInterface<Param, Field, Population> {
	ScoreConfig<Param, Field> getConfigFor(Population c);
	ScoreConfig<Param, Field> newConfig();
	Population newFromConfig(ScoreConfig<Param, Field> config);
	Map<Population, Double> fitness(List<Population> ais);
}
