import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Query {
	private Data data;
	private Scanner input;
	
	private boolean teamMode;
	private boolean closing;
	
	private String filename;
	private PrintStream ps;
	
	/* CONSTRUCTORS */
	
	// Constructor without output file
	public Query(Scanner input) {
		this.filename = null;
		this.ps = null;
		
		this.data = new Data();
		this.input = input;
		this.teamMode = true;
		
		this.closing = false;
	}
	
	// Constructor with output file
	public Query(String filename, Scanner input) {
		this.filename = filename;
		try {
			this.ps = new PrintStream(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("fnf");
		}
		
		this.data = new Data();
		this.input = input;
		this.teamMode = true;
		
		this.closing = false;
	}
	
	/* HELPER METHODS */
	
	protected boolean close() {
		return closing;
	}
	
	private void print(String str) {
		System.out.print(str);
		if (ps != null)
			ps.print(str);
	}
	
	private void println(String str) {
		print(str + "\n");
	}
	
	private void println() {
		println("");
	}
	
	private String get() {
		String line = input.nextLine();
		if (ps != null)
			ps.println(line);
		return line;
	}
	
	private boolean getYesNo(String outstr) {
		print(outstr);
		String answer = get();
		while (true) {
			if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")) {
				return true;
			} else if (answer.equalsIgnoreCase("n") || answer.equalsIgnoreCase("no")) {
				return false;
			} else {
				println("Output not recognized.");
			}
		}
	}
	
	/* USER OPTIONS

	Method Template for option 'do the thing':
	
	public void doTheThing(boolean helpMode) {
		String help = "<LINE 1>/n"
					+ "<LINE 2>/n"
					+ ...
					+ "<LINE LAST>";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		<METHOD HERE>
	}
	*/
	
	public void quit(boolean helpMode) {
		String help = "Exits the program.";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		println("Goodbye!");
		closing = true;
	}
	
	public void help(boolean helpMode) {
		String help = "Help about help: so meta.";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		System.out.println("This option is not yet supported!");
	}
	
		/* Settings */
	
	public void enableFileOutput(boolean helpMode) {
		String help = "Use this to change file output.\n"
					+ "Program will write console output to this file.";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		if (ps != null) {
			if (!getYesNo("Already outputting to file '" + filename + "': switch output file (y/n)? ")) {
				println("Continuing output to '" + filename + "'.");
				return;
			}
		}
		
		print("Enter the file to output to: ");
		String filename = get();
		this.filename = filename;
		
		File file = new File(filename);
		try {
			if (file.exists()) {
				if (getYesNo("File '" + filename + "' already exists. Append to file (y/n)? ")) {
					this.ps = new PrintStream(new FileOutputStream(file, true));
				} else {
					this.ps = new PrintStream(new FileOutputStream(file, false));
				}
			} else {
				this.ps = new PrintStream(file);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Failed to open file for writing: disabling file output.");
			filename = null;
			ps = null;
		}
		println("Output file set to '" + filename + "'.");
	}
	
	public void disableFileOutput(boolean helpMode) {
		String help = "Use this to turn off file output.";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		if (ps == null) {
			println("File output already disabled.");
		} else {
			println("Disabling output to file '" + filename + "'.");
			filename = null;
			ps = null;
		}
	}
	
	public void enableTeamMode(boolean helpMode) {
		String help = "Enable Team Mode.\n"
					+ "Team Mode will cause data output to\n"
					+ "include data from all teams the\n"
					+ "player is a member of.";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		println("Team Mode enabled.");
		teamMode = true;
	}
	
	public void disableTeamMode(boolean helpMode) {
		String help = "Enable Team Mode.\n"
					+ "Team Mode will cause data output to\n"
					+ "include data from all teams the\n"
					+ "player is a member of.";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		println("Team Mode disabled.");
		teamMode = false;
	}
	
		/* Data Lookup */
	
	public void getShootCounts(boolean helpMode) {
		String help = "";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		print("");
	}
	
	public void listPlayerTeams(boolean helpMode) {
		String help = "";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		print("");
	}
	
	public void getPlayerCard(boolean helpMode) {
		String help = "";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		print("Enter the player to get data for: ");
		String playername = get();
		getPlayerCard(playername);
	}
	
	public void getShootCount(boolean helpMode) {
		String help = "";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		print("Enter the player to get data for: ");
		String playername = get();
		println("Shoots: " + data.getPlayer(playername, teamMode).getShootCount(teamMode));
	}
	
	public void getPlayerCardAll(boolean helpMode) {
		String help = "";
		if (helpMode) {
			System.out.println(help);
			return;
		}
		
		for (Player player : data.getPlayers()) {
			if (player.getGameCount(true) > 0) {
				getPlayerCard(player.getName());
				println("----------------------");
			}
		}
		
		if (teamMode) {
			for (Player team : data.getTeams()) {
				if (team.getGameCount(false) > 0) {
					getPlayerCard(team.getName());
					println("----------------------");
				}
			}
		}
	}
	
	private void getPlayerCard(String playername) {
		Player player = data.getPlayer(playername, false);
		
		int indivGames = player.getGameCount(false);
		int totalGames = indivGames;
		if (!player.isTeam()) {
			totalGames = player.getGameCount(true);
		}
		
		print("Player name: " + player.getName());
		if (player.isTeam()) {
			print(" (team)");
		}
		println();
		println("Individual games played: " + indivGames);
		println("Individual shoots: " + player.getShootCount(false));
		if (!player.isTeam()) {
			println("Total games played: " + totalGames);
			println("Total shoots: " + player.getShootCount(true));
			if (teamMode) {
				for (Player team : player.getTeams()) {
					if (team.getGameCount(false) > 0)
						println("\tGames played as team '" + team.getName() + "': " + team.getGameCount(false) + " (" + team.getShootCount(false) + " shoots)");
				}
			}
		}
		
		if (totalGames == 0) return;
		
		int[] placings;
		if (indivGames > 0) {
			placings = player.getPlacings(false);
			println("Individual games finishing 1st: " + placings[0] + " (" + (100 * placings[0]) / indivGames + "%)");
			println("Individual games finishing 2nd: " + placings[1] + " (" + (100 * placings[1]) / indivGames + "%)");
			println("Individual games finishing 3rd: " + placings[2] + " (" + (100 * placings[2]) / indivGames + "%)");
			println("Individual games finishing 4th: " + placings[3] + " (" + (100 * placings[3]) / indivGames + "%)");
			println("Individual points accrued: " + player.getTotalPoints(false));
			println("Average individual points per game: " + (player.getTotalPoints(false) / indivGames));
			println("Individual queens taken: " + player.getQueenCount(false));
			println("Average individual queens per game: " + ((double) player.getQueenCount(false) / indivGames));
		}
		
		if (!player.isTeam()) {
			placings = player.getPlacings(true);
			println("Total games finishing 1st: " + placings[0] + " (" + (100 * placings[0]) / totalGames + "%)");
			println("Total games finishing 2nd: " + placings[1] + " (" + (100 * placings[1]) / totalGames + "%)");
			println("Total games finishing 3rd: " + placings[2] + " (" + (100 * placings[2]) / totalGames + "%)");
			println("Total games finishing 4th: " + placings[3] + " (" + (100 * placings[3]) / totalGames + "%)");
			println("Total points accrued: " + player.getTotalPoints(true));
			println("Average total points per game: " + (player.getTotalPoints(true) / totalGames));
			println("Total queens taken: " + player.getQueenCount(true));
			println("Average total queens per game: " + ((double) player.getQueenCount(true) / totalGames));
		}
	}
}