package test.net.zomis.aiscores.ttt;

import test.net.zomis.aiscores.ttt.SimpleTTT.TTTPlayer;

public class TTTSquare {
	private final int x;
	private final int y;
	private TTTPlayer playedBy;
	
	public TTTSquare(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setPlayedBy(TTTPlayer player) {
		this.playedBy = player;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public TTTPlayer getPlayedBy() {
		return playedBy;
	}
	@Override
	public String toString() {
		return playedBy == null ? " " : playedBy.toString();
	}
}
