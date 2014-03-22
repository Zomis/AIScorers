package test.net.zomis.aiscores.ttt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.zomis.aiscores.ScoreParameters;
import net.zomis.aiscores.ScoreStrategy;

public class SimpleTTT implements ScoreStrategy<SimpleTTT, TTTSquare> {
	private static final int SIZE = 3;
	private TTTSquare[][] squares = new TTTSquare[SIZE][SIZE];
	private TTTPlayer currentPlayer;
	private List<TTWinCondition> winConds;
	
	public enum TTTPlayer {
		X, O;

		public TTTPlayer next() {
			return this == X ? O : X;
		}
	}
	
	public SimpleTTT() {
		currentPlayer = TTTPlayer.X;
		for (int xx = 0; xx < SIZE; xx++) {
			for (int yy = 0; yy < SIZE; yy++) {
				squares[xx][yy] = new TTTSquare(xx, yy);
				
			}
		}
		winConds = setupWins(this);
		
	}
	
	private static void newWin(List<TTWinCondition> conds, int consecutive, List<TTTSquare> winnables) {
		if (winnables.size() >= consecutive) // shorter win conditions doesn't need to be added as they will never be able to win
			conds.add(new TTWinCondition(winnables));
	}

	private static List<TTTSquare> loopAdd(SimpleTTT board,
			int xx, int yy, int dx, int dy) {
		List<TTTSquare> winnables = new ArrayList<TTTSquare>();
		
		TTTSquare tile;
		do {
			tile = board.getSquare(xx, yy);
			xx += dx;
			yy += dy;
			if (tile != null)
				winnables.add(tile);
		}
		while (tile != null);
		
		return winnables;
	}
	
	private TTTSquare getSquare(int xx, int yy) {
		if (xx < 0 || yy < 0)
			return null;
		if (xx >= SIZE || yy >= SIZE)
			return null;
		return squares[xx][yy];
	}

	public static List<TTWinCondition> setupWins(final SimpleTTT board) {
		int consecutive = SIZE;
		List<TTWinCondition> conds = new ArrayList<TTWinCondition>();
		
		// Scan columns for a winner
		for (int xx = 0; xx < SIZE; xx++) {
			newWin(conds, consecutive, loopAdd(board, xx, 0, 0, 1));
		}
		
		// Scan rows for a winner
		for (int yy = 0; yy < SIZE; yy++) {
			newWin(conds, consecutive, loopAdd(board, 0, yy, 1, 0));
		}
		
		newWin(conds, consecutive, loopAdd(board, 0, 0, 1, 1));
		newWin(conds, consecutive, loopAdd(board, 2, 0, -1, 1));
		
		return conds;
	}
	
	
	
	
	
	public void playAt(TTTSquare square) {
		square.setPlayedBy(currentPlayer);
		currentPlayer = currentPlayer == TTTPlayer.X ? TTTPlayer.O : TTTPlayer.X;
	}
	
	public void output() {
		for (int yy = 0; yy < SIZE; yy++) {
			for (int xx = 0; xx < SIZE; xx++)
				System.out.print(this.squares[xx][yy]);
			
			System.out.println();
		}
		System.out.println("---------");
	}

	@Override
	public Collection<TTTSquare> getFieldsToScore(SimpleTTT params) {
		List<TTTSquare> sq = new ArrayList<TTTSquare>();
		for (int yy = 0; yy < SIZE; yy++) {
			for (int xx = 0; xx < SIZE; xx++)
				sq.add(params.squares[xx][yy]);
		}
		return sq;
	}

	@Override
	public boolean canScoreField(ScoreParameters<SimpleTTT> parameters, TTTSquare field) {
		return field.getPlayedBy() == null;
	}

	public List<TTWinCondition> getWinConds() {
		return new ArrayList<TTWinCondition>(winConds);
	}

	public boolean isWon() {
		return determineWinner() != null;
	}
	
	public TTTPlayer determineWinner() {
		for (TTWinCondition wins : winConds) {
			TTTPlayer winner = wins.determineWinnerNew();
			if (winner != null)
				return winner;
		}
		return null;
	}
	
	public TTTPlayer getCurrentPlayer() {
		return currentPlayer;
	}
}
