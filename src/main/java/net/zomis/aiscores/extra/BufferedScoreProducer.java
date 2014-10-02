package net.zomis.aiscores.extra;

import java.util.Map;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.FieldScores;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreStrategy;

public class BufferedScoreProducer<Params, Field> extends FieldScoreProducer<Params, Field> {

	private FieldScores<Params, Field> savedScores;

	public BufferedScoreProducer(ScoreConfig<Params, Field> config,	ScoreStrategy<Params, Field> strat) {
		super(config, strat);
	}

	@Override
	public FieldScores<Params, Field> score(Params params, Map<Class<?>, Object> analyzes) {
		savedScores = super.score(params, analyzes);
		return savedScores;
	}
	
	public FieldScores<Params, Field> getSavedScores() {
		return savedScores;
	}
	
}
