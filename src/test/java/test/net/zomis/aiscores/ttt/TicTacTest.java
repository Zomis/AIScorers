package test.net.zomis.aiscores.ttt;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import net.zomis.aiscores.FieldScoreProducer;
import net.zomis.aiscores.ScoreConfig;
import net.zomis.aiscores.ScoreConfigFactory;
import net.zomis.aiscores.extra.ParamAndField;
import net.zomis.aiscores.extra.ScoreUtils;
import test.net.zomis.aiscores.ttt.SimpleTTT.TTTPlayer;
import test.net.zomis.aiscores.ttt.scorers.CenterScorer;
import test.net.zomis.aiscores.ttt.scorers.CornerScorer;
import test.net.zomis.aiscores.ttt.scorers.CreateTrap;
import test.net.zomis.aiscores.ttt.scorers.OneMissingAnalyzer;
import test.net.zomis.aiscores.ttt.scorers.PreventOppWin;
import test.net.zomis.aiscores.ttt.scorers.WinIfYouCan;

public class TicTacTest {
	private ScoreConfig<SimpleTTT, TTTSquare> x = new ScoreConfigFactory<SimpleTTT, TTTSquare>()
			.withPreScorer(new OneMissingAnalyzer())
			.withScorer(new CenterScorer(), 10)
			.withScorer(new WinIfYouCan(), 5)
			.withScorer(new CornerScorer())
			.withScorer(new CreateTrap())
			.withScorer(new PreventOppWin(), 0.1)
			.build();
	
	private ScoreConfig<SimpleTTT, TTTSquare> o = new ScoreConfigFactory<SimpleTTT, TTTSquare>()
			.withPreScorer(new OneMissingAnalyzer())
			.withScorer(new CenterScorer(), 10)
			.withScorer(new CornerScorer(), -1)
			.withScorer(new PreventOppWin(), 5)
			.withScorer(new WinIfYouCan())
			.build();
	
	
	private SimpleTTT ttt = new SimpleTTT();
	
	private final Random random = new Random();
	
	@Test
	public void testFirstMove() {
		assertFalse(ttt.isWon());
		assertEquals(ttt.getSquare(1, 1), aiPlay());
	}
	
	@Test
	public void fullTest() {
		while (!ttt.isWon()) {
			ttt.output();
			aiPlay();
		}
		ttt.output();
		System.out.println("Winner is " + ttt.determineWinner());
	}
	
	private TTTSquare aiPlay() {
		FieldScoreProducer<SimpleTTT, TTTSquare> prod = new FieldScoreProducer<SimpleTTT, TTTSquare>(getConfig(ttt), ttt);
		ParamAndField<SimpleTTT, TTTSquare> pos = ScoreUtils.pickBest(prod, ttt, random);
		
		if (pos == null) {
			return null;
		}
		ttt.playAt(pos.getField());
		return pos.getField();
	}

	public ScoreConfig<SimpleTTT, TTTSquare> getConfig(SimpleTTT param) {
		if (param.getCurrentPlayer() == TTTPlayer.X) {
			return x;
		}
		else {
			return o;
		}
	}
}
