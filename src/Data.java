import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Data {
	private static final String ALIAS_FILENAME = "aliases.txt";
	private static final String GAMES_FILENAME = "games.txt";
	private static final int NUM_PLAYERS = 4; // For the purity of the game...
	
	// The list of all games in the order that they were parsed
	public static List<Game> games = new ArrayList<Game>();
	
	// The set of all players who have played a game
	public static Set<Player> persons = new HashSet<Player>();
	
	// The set of all teams
	public static Set<Player> teams = new HashSet<Player>();
	
	// Maps player names to all team aliases that they are a part of
	public static Map<Player, Set<Player>> nameToAliases = new HashMap<Player, Set<Player>>();
	
	// Maps team alias names to the list of players that constitute that team
	public static Map<Player, Set<Player>> aliasToNames = new HashMap<Player, Set<Player>>();
	
	public Data() {
		System.out.println("Reading '" + ALIAS_FILENAME + "' file...");
		ingestAliases();
		System.out.println("File '" + ALIAS_FILENAME + "' read!");
		System.out.println("Reading '" + GAMES_FILENAME + "' file...");
		ingestGames();
		System.out.println("File '" + ALIAS_FILENAME + "' read!");
	}
	
	private void ingestAliases() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(ALIAS_FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (sc.hasNextLine()) {
			String[] line = sc.nextLine().split("[: ]");
			Set<Player> names = new HashSet<Player>();
			Player alias = getPlayer(line[0], true);
			
			for (int i = 2; i < line.length; i += 2) {
				names.add(getPlayer(line[i], false));
			}
			
			aliasToNames.put(alias, names);
			for (Player player : names) {
				player.addTeam(alias);
				Set<Player> aliases = nameToAliases.get(player);
				if (aliases == null) {
					aliases = new HashSet<Player>();
					nameToAliases.put(player, aliases);
				}
				aliases.add(alias);
			}
		}
	}
	
	private void ingestGames() {
		Scanner sc = null;
		try {
			sc = new Scanner(new File(GAMES_FILENAME));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (sc.hasNextLine()) {
			ingestGame(sc);
		}
	}
	
	private void ingestGame(Scanner sc) {
		String line = sc.nextLine();
		String[] header = line.split("[,/: ]");
		
		Game gm = null;
		if (header[6].equals("AM")) {
			gm = new Game(header[0], header[1], header[2], header[4], header[5], false);
		} else if (header[6].equals("PM")) {
			gm = new Game(header[0], header[1], header[2], header[4], header[5], true);
		} else {
			System.out.println("ERROR: Game data displays " + header[6] + " instead of AM or PM! Exiting...");
			System.exit(0);
		}
		
		for (int i = 0; i < NUM_PLAYERS; i++) {
			line = sc.nextLine();
			String[] playerdata = line.split("[: ]");
			Player player = getPlayer(playerdata[0], false);
			gm.addPlayer(player, playerdata);
			player.addGame(gm, i);
		}
		gm.resolveShoots();
		gm.resolveQueens();
		
		sc.nextLine();
		games.add(gm);
	}
	
	public Player getPlayer(String name, boolean teamMode) {
		for (Player player : persons) {
			if (player.getName().equals(name))
				return player;
		}
		
		for (Player player : teams) {
			if (player.getName().equals(name))
				return player;
		}
		
		Player newPlayer = new Player(name, teamMode);
		if (teamMode) {
			teams.add(newPlayer);
		} else {
			persons.add(newPlayer);
		}
		return newPlayer;
	}
	
	/* BEGIN INTERFACING */
	
	public Set<Player> getPlayers() {
		return persons;
	}
	
	public Set<Player> getTeams() {
		return teams;
	}
	
	public List<Game> getGames() {
		return games;
	}
}