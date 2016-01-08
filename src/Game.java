import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {
	private int month;
	private int day;
	private int year;
	private int hour;
	private int minute;
	private boolean pm;
	
	private int numberOfRounds = -1;
	private Map<Player, Integer[]> scores = new HashMap<Player, Integer[]>();
	private Set<Integer> shootHands = new HashSet<Integer>();
	
	public Game(String month, String day, String year, String hour, String minute, boolean pm) {
		this.month = Integer.parseInt(month);
		this.day = Integer.parseInt(day);
		this.year = Integer.parseInt(year);
		this.hour = Integer.parseInt(hour);
		this.minute = Integer.parseInt(minute);
		this.pm = pm;
	}
	
	public void addPlayer(Player player, String[] playerdata) {
		if (scores.size() == 0) {
			numberOfRounds = playerdata.length - 2;
		}
		Integer[] curScores = new Integer[playerdata.length - 2];
		for (int i = 2; i < playerdata.length; i++) {
			int score = Integer.parseInt(playerdata[i]);
			curScores[i - 2] = score;
			if ((i == 2 && curScores[i - 2] == 26) || (i > 2 && curScores[i - 2] - curScores[i - 3] == 26)) {
				shootHands.add(i - 2);
			}
		}
		scores.put(player, curScores);
	}
	
	public int resolveShoots() {
		for (int hand : shootHands) {
			for (Player player : scores.keySet()) {
				if ((hand == 0 && scores.get(player)[hand] == 0) || (hand > 0 && scores.get(player)[hand] - scores.get(player)[hand - 1] == 0)) {
					player.addShoot();
					break;
				}
			}
		}
		return shootHands.size();
	}
	
	public void resolveQueens() {
		for (Player player : scores.keySet()) {
			Integer[] cur = scores.get(player);
			if (cur[0] >= 13)
				player.addQueen();
			for (int i = 1; i < numberOfRounds; i++) {
				if (cur[i] - cur[i - 1] >= 13) {
					player.addQueen();
				}
			}
		}
	}
	
	public int numberOfRounds() {
		return numberOfRounds;
	}
	
	public String toString() {
		String str = "" + month + "/" + day + "/" + year + ", " + hour + ":" + minute;
		if (pm) {
			str += " PM";
		} else {
			str += " AM";
		}
		return str;
	}
	
	public Set<Player> getPlayers() {
		return scores.keySet();
	}
	
	public Integer[] getScoresByPlayer(Player player) {
		return scores.get(player);
	}
	
	public int getScoreByPlayer(Player player) {
		return scores.get(player)[numberOfRounds - 1];
	}
}
