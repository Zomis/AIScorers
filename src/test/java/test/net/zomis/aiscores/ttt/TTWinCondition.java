package test.net.zomis.aiscores.ttt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import test.net.zomis.aiscores.ttt.SimpleTTT.TTTPlayer;

public class TTWinCondition implements Iterable<TTTSquare> {

	private final List<TTTSquare> winnables;

	public TTWinCondition(TTTSquare... winnables) {
		this(Arrays.asList(winnables));
	}
	
	public TTWinCondition(List<? extends TTTSquare> winnables) {
		if (winnables.isEmpty())
			throw new IllegalArgumentException("Can't have an empty win condition!");
		this.winnables = Collections.unmodifiableList(new ArrayList<TTTSquare>(winnables));
	}
	
	public int neededForWin(TTTPlayer player) {
		return winnables.size() - hasCurrently(player);
	}
	public boolean isWinnable(TTTPlayer byPlayer) {
		return hasCurrently(byPlayer.next()) == 0;
	}
	public int hasCurrently(TTTPlayer player) {
		int i = 0;
		for (TTTSquare winnable : winnables) {
			if (winnable.getPlayedBy() == player)
				i++;
		}
		return i;
	}
	
	public TTTPlayer determineWinnerNew() {
		TTTPlayer winner = null;
		
		for (TTTSquare winnable : winnables) {
			TTTPlayer current = winnable.getPlayedBy();
			if (current == null)
				return null;
			if (winner == null)
				winner = current;
			else if (winner != current)
				return null;
		}
		return winner;
	}
	
	public boolean hasWinnable(TTTSquare field) {
		return winnables.contains(field);
	}
	
	public int size() {
		return winnables.size();
	}

	@Override
	public Iterator<TTTSquare> iterator() {
		return new ArrayList<TTTSquare>(this.winnables).iterator();
	}
	
}
