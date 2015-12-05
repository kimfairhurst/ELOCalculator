package cs170proj;

import java.util.ArrayList;

public class eloTable {

	public ArrayList<Double> createEloTable(){
		ArrayList<Double> eloTable = new ArrayList<Double>();
		
		eloTable.add(0.50);
		for(int i = 1; i < 20; i ++){
			eloTable.add(0.53);
		} for(int i = 20; i < 40; i ++){
			eloTable.add(0.58);		
		} for(int i = 40; i < 60; i ++){
			eloTable.add(0.62);
		} for(int i = 60; i < 80; i ++){
			eloTable.add(0.66);
		} for(int i = 80; i < 100; i ++){
			eloTable.add(0.69);
		} for(int i = 100; i < 120; i ++){
			eloTable.add(0.73);
		} for(int i = 120; i < 140; i ++){
			eloTable.add(0.76);
		} for(int i = 140; i < 160; i ++){
			eloTable.add(0.79);		
		} for(int i = 160; i < 180; i ++){
			eloTable.add(0.82);		
		} for(int i = 180; i < 200; i ++){
			eloTable.add(0.84);	
		} for(int i = 200; i < 300; i ++){
			eloTable.add(0.93);
		} for(int i = 300; i <= 400; i ++){
			eloTable.add(i, 0.97);
		}
		
		return eloTable;
		
	}

}
