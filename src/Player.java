import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
	private String name;
	private boolean isTeam;
	private List<Game> games;
	private List<Player> teams;
	private int[] placings;
	private int indivShoots;
	private int indivQueens;
	private int indivPoints;
	
	public Player(String name, boolean isTeam) {
		this.name = name;
		this.isTeam = isTeam;
		this.games = new ArrayList<Game>();
		if (!isTeam)
			this.teams = new ArrayList<Player>();
		else
			this.teams = null;
		this.placings = new int[4];
		this.indivShoots = 0;
		this.indivQueens = 0;
		this.indivPoints = 0;
	}
	
	public void addGame(Game game, int place) {
		games.add(game);
		indivPoints += game.getScoreByPlayer(this);
		placings[place]++;
	}
	
	public void addTeam(Player team) {
		teams.add(team);
	}
	
	public void addShoot() {
		indivShoots++;
	}
	
	public void addQueen() {
		indivQueens++;
	}
	
	/* GET METHODS */
	
	public boolean isTeam() {
		return isTeam;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Player> getTeams() {
		return Collections.unmodifiableList(teams);
	}
	
	public int getGameCount(boolean includeTeams) {
		int count = games.size();
		if (includeTeams) {
			for (Player team : teams) {
				count += team.games.size();
			}
		}
		return count;
	}
	
	public int getTotalPoints(boolean includeTeams) {
		int count = indivPoints;
		if (includeTeams) {
			for (Player team : teams) {
				count += team.getTotalPoints(false);
			}
		}
		return count;
	}
	
	public int getShootCount(boolean includeTeams) {
		int count = indivShoots;
		if (includeTeams) {
			for (Player team : teams) {
				count += team.getShootCount(false);
			}
		}
		return count;
	}
	
	public int getQueenCount(boolean includeTeams) {
		int count = indivQueens;
		if (includeTeams) {
			for (Player team : teams) {
				count += team.getQueenCount(false);
			}
		}
		return count;
	}
	
	public int[] getPlacings(boolean includeTeams) {
		int[] counts = new int[4];
		for (int i = 0; i < 4; i++) {
			counts[i] += placings[i];
			if (includeTeams) {
				for (Player team : teams) {
					counts[i] += team.placings[i];
				}
			}
		}
		return counts;
	}

	public double getStdDevGames(boolean includeTeams) {
		int count = 0;
		int sum = 0;
		
		for (Game gm : games) {
			count++;
			sum += gm.getScoresByPlayer(this)[gm.numberOfRounds() - 1];
		}
		if (includeTeams) {
			for (Player team : teams) {
				for (Game gm : team.games) {
					count++;
					sum += gm.getScoresByPlayer(team)[gm.numberOfRounds() - 1];
				}
			}
		}
		
		double mean = ((double) sum) / ((double) count);
		double num = 0;
		
		for (Game gm : games) {
			num += Math.pow(gm.getScoresByPlayer(this)[gm.numberOfRounds() - 1] - mean, 2);
		}
		if (includeTeams) {
			for (Player team : teams) {
				for (Game gm : team.games) {
					num += Math.pow(gm.getScoresByPlayer(team)[gm.numberOfRounds() - 1] - mean, 2);
				}
			}
		}
		
		return Math.sqrt(num / (double) (count));
	}
	
	public double getStdDevHands(boolean includeTeams) {
		int count = 0;
		int sum = 0;
		
		for (Game gm : games) {
			int last = 0;
			for (int hand : gm.getScoresByPlayer(this)) {
				count++;
				sum += hand - last;
				last = hand;
			}
		}
		if (includeTeams) {
			for (Player team : teams) {
				for (Game gm : team.games) {
					int last = 0;
					for (int hand : gm.getScoresByPlayer(team)) {
						count++;
						sum += hand - last;
						last = hand;
					}
				}
			}
		}
		
		double mean = ((double) sum) / ((double) count);
		double num = 0;
		
		for (Game gm : games) {
			int last = 0;
			for (int hand : gm.getScoresByPlayer(this)) {
				num += Math.pow((double) hand - (double) last - mean, 2);
				last = hand;
			}
		}
		if (includeTeams) {
			for (Player team : teams) {
				for (Game gm : team.games) {
					int last = 0;
					for (int hand : gm.getScoresByPlayer(team)) {
						num += Math.pow((double) hand - (double) last - mean, 2);
						last = hand;
					}
				}
			}
		}
		
		return Math.sqrt(num / (double) (count));
	}
}
