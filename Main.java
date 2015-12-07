package cs170proj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub	
		
		//Comparator to return sequential ordering of filenames
		class FileNameComparator implements Comparator<File> {
		    public int compare( File a, File b ) {
		    	Integer aint = Integer.valueOf(a.getName().replaceAll(".in", ""));
		    	Integer bint = Integer.valueOf(b.getName().replaceAll(".in", ""));
		        return aint.compareTo(bint);
		    }
		}

		File dir = new File("src/gameInstances/");
		
		//Sort directoryListing
		File[] directoryListing = dir.listFiles();	
		
		Arrays.sort(directoryListing, new FileNameComparator() );
		
		//Declare variables
		ArrayList<ArrayList<Integer>> gamePairs = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> playedGames = new ArrayList<Integer>();
		ArrayList<Integer> noPlayedGames = new ArrayList<Integer>();
		
		Map<Integer, Double> eloRankings = new HashMap<Integer, Double>();	
		Map<Integer, Double> sumEloRankings = new HashMap<Integer, Double>();	
		
		ArrayList<Integer> finalRanking = new ArrayList<Integer>();	
		ArrayList<Integer> gamePair = new ArrayList<Integer>();
		
		Integer numberOfPlayers = 0;
		String outputLine = "";
		
		ArrayList<Double> eloChart = new eloTable().createEloTable();

		for(File child : directoryListing)
		{
			BufferedReader br = new BufferedReader(new FileReader(child));
			
			//Instantiate new, empty copies of data structures
			gamePairs = new ArrayList<ArrayList<Integer>>();
			eloRankings = new HashMap<Integer, Double>();			
			finalRanking = new ArrayList<Integer>();
			sumEloRankings = new HashMap<Integer, Double>();
			gamePair = new ArrayList<Integer>();
			playedGames = new ArrayList<Integer>();
			noPlayedGames = new ArrayList<Integer>();
			

			try {
			    String line = br.readLine();
			    
			    int currentPlayer = -1;
			    
			    while (line != null) {	  
			    	String gameBoard = line.replaceAll("\\s+", "");
			    	//First line, get number of players
			    	if(currentPlayer == -1)
			    	{
			    		numberOfPlayers = Integer.parseInt(gameBoard);
			    	}
			    	//Actual player's list
			    	else
			    	{
				    	for(int opponentPlayer = 0; opponentPlayer < numberOfPlayers; opponentPlayer ++)
				    	{
			    			char gameOutcome = gameBoard.charAt(opponentPlayer);
			    			//Fill played games with valid players, fill gamePairs with games
			    			if(Character.getNumericValue(gameOutcome) == 1){
			    				if(!playedGames.contains(currentPlayer)){
			    					playedGames.add(currentPlayer);
			    				}	if(!playedGames.contains(opponentPlayer)){
			    					playedGames.add(opponentPlayer);
			    				}
			    				gamePair = new ArrayList<Integer>();
			    				gamePair.add(currentPlayer);
			    				gamePair.add(opponentPlayer);
			    				gamePairs.add(gamePair);
			    			}
			    		}
				    }
			    	currentPlayer += 1;
			        line = br.readLine();
			    }
			} finally {
			    br.close();
			}		
			// Actual Algorithm
			for(int iternumber = 0; iternumber < 1000; iternumber ++){
				for(int playerNumber = 0; playerNumber < numberOfPlayers; playerNumber ++){
					// Hasn't played a game yet
					if(!playedGames.contains(playerNumber) && !noPlayedGames.contains(playerNumber)){
						noPlayedGames.add(playerNumber);
					// add player to eloRankings
					} else {
						if(playedGames.contains(playerNumber)){
							eloRankings.put(playerNumber, 1000.0);
						}
					}
				}				
				Collections.shuffle(gamePairs);			
				for(int gameNumber = 0; gameNumber < gamePairs.size(); gameNumber ++){
					
					int winner = gamePairs.get(gameNumber).get(0);
					int loser = gamePairs.get(gameNumber).get(1);
					Double winnerElo = eloRankings.get(winner);
					Double loserElo = eloRankings.get(loser);
					
					Double difference = Math.abs(winnerElo - loserElo);
					int differenceInt = (int) Math.round(difference);
					
					Double eloChange = 0.00;
										
					if(differenceInt > eloChart.size() - 1){
						eloChange = 0.99;
					} else {
						eloChange = eloChart.get(differenceInt);
					}
					
					Double changeElo = eloChange * 10.0;
					Double winnerNewElo = Math.round((winnerElo + changeElo) * 100000d) / 100000d;
					Double loserNewElo = Math.round((loserElo - changeElo) * 100000d) / 100000d;
					
					eloRankings.put(winner, winnerNewElo);
					eloRankings.put(loser, loserNewElo);

				}
				for (Map.Entry<Integer, Double> entry : eloRankings.entrySet()){
					Double elo = entry.getValue();
				    Integer player = entry.getKey();					
					if(!sumEloRankings.containsKey(player)){
						sumEloRankings.put(player, elo);
					} else {
						Double newElo = 0.0;
						newElo = (sumEloRankings.get(player) * (iternumber - 1)) + elo;
						newElo = newElo/iternumber;
						sumEloRankings.put(player, newElo);
					}
				}
			}
	
			//Create final output line based on elo rankings

			
			Double minElo = Double.POSITIVE_INFINITY;
			Integer worstPlayer = 0;
			int size = sumEloRankings.size();
			for(int i = 0; i < size; i ++){
				for (Map.Entry<Integer, Double> entry : sumEloRankings.entrySet()){
				    Integer player = entry.getKey();				
					Double playerElo = entry.getValue();
					if(playerElo < minElo){
						minElo = playerElo;
						worstPlayer = player;
					}
				}
				finalRanking.add(worstPlayer);
				sumEloRankings.remove(worstPlayer);
				minElo = Double.POSITIVE_INFINITY;
			}
			
			finalRanking.addAll(0, noPlayedGames);
			
			outputLine = "";
					
			for(int i = 0; i < finalRanking.size(); i ++){
				if(i != finalRanking.size() - 1){
					outputLine = outputLine + (finalRanking.get(i) + 1) + " ";
				} else {
					outputLine = outputLine + (finalRanking.get(i) + 1);
				}
			}
			
			try
			{
			    FileWriter fw = new FileWriter("src/solutions.txt", true); //the true will append the new data
			    fw.write(outputLine + "\n"); //appends the string to the file
			    fw.close();
			}
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}
			
	 
		}
	}
}
