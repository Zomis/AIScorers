package net.zomis.aiscores.extra;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ScoreConfig;



public interface ScoreProducer<Params, Field> {
	// TODO: Deprecate interface ScoreProducer
	ScoreConfig<Params, Field> getConfig();
	FieldScoreProducer<Params, Field> createScoreProvider();
	ParamAndField<Params, Field> play();
}
