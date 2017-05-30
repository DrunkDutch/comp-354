package com.dmens.pokeno.Utils;
import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Card.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser {

    private static final Logger LOG = LogManager.getLogger(Parser.class);

	private static Parser instance = null;
	private static final String ENCODING = "UTF-8";
	
	private ArrayList<String> mCardList = null;
	private ArrayList<String> mAbilitiesList = null;
	
	private Parser()
	{ }
	
	/*
	 * lazy instantiation
	 */
	public static Parser Instance( )
	{
		if(instance == null)
		{
			instance = new Parser();
		}
		
		return instance;
	}
	
	/*
	 * Get the contents as an ArrayList which will facilitate specific read lines to be accessed.
	 */
	private static ArrayList<String> GetFileContentsAsArrayList(String location) throws FileNotFoundException
	{
		File file = new File(location);
		Scanner scanner = new Scanner(file, ENCODING);
		
		ArrayList<String> contents = new ArrayList<String>();

		// Processing the file line by line
		while(scanner.hasNext())
		{
		    String nextLine = scanner.nextLine();
		    contents.add(nextLine);
		}

		scanner.close();
		return contents;
	}
	
	/*
	 * Get the file contents as a Set, thus ensuring that there are no duplicate cards.
	 */
	private static Set<String> GetFileContentsAsSet(String location) throws FileNotFoundException
	{
		File file = new File(location);
		Scanner scanner = new Scanner(file, ENCODING);
		
		Set<String> contents = new HashSet<String>();

		// Processing the file line by line
		while(scanner.hasNext())
		{    
		    String nextLine = scanner.nextLine();
		    contents.add(nextLine);
		}

		scanner.close();
		return contents;
	}
	
	/*
	 * Point to the location of the file and load it.
	 */
	public boolean LoadCards(String cardLocation)
	{
		try
		{
			this.mCardList = Parser.GetFileContentsAsArrayList(cardLocation);
			
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	 * Point to the location of the file and load it.
	 */
	public boolean LoadAbilities(String abilitiesLocation)
	{
		try
		{
			this.mAbilitiesList = Parser.GetFileContentsAsArrayList(abilitiesLocation);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/*
	 * Parse the cardContents string which represents a line in cards.txt.
	 * Creates the appropriate card that will then be added to the deck.
	 */
	private Card CreateCard(String cardContents)
	{
		Card c = null;
		
		if(cardContents.matches("(.*):pokemon:(.*)"))
		{
			System.out.println("Created PokemonCard");
			// split the line with 2 delimiters : and ,
			String[] results = cardContents.split(":|\\,");
			String pokemonName = null;
			String basePokemonName = null;
			int hpIndex = -1;
			int hp = 0;
			ArrayList<String> categories = new ArrayList<String>();
			int retreatIndex = -1;
			int retreatCost = 0;
			int abilitiesIndex = -1;
			ArrayList<Ability> abilities = new ArrayList<Ability>();
			
			// parse pokemon name
			pokemonName = results[0];
			
			// parse initial hp, we are assuming the first integer will be the HP value
			for(int i = 1; i < results.length - 1; ++i) {
				try {
					hp = Integer.parseInt(results[i]);
					hpIndex = i;
					break;
				} catch (NumberFormatException e) {}
			}
			
			// parse pokemon categories
			for(int i = 1; i <  hpIndex; ++i) {
				if(results[i].equals("cat")) {
					categories.add(results[i+1]);
					if(results[i+1].equals("stage-one")) {
						basePokemonName = results[i+2];
						++i;
					}
					++i;
				}
			}
			
			for(int i = 0; i < results.length; ++i) {
				if(results[i].equals("retreat")) {
					retreatIndex = i;
					break;
				}
			}
			
			// in case we have a label called "retreat"
			if(retreatIndex != -1) {
				int retreatCostIndex = hpIndex + 4;
				// parse pokemon retreat cost
				try {
					retreatCost = Integer.parseInt(results[retreatCostIndex]);	
				} catch (NumberFormatException e) {}
			} else {
				// in case there is no retreat information due to typo, we will set the cost to 1
				retreatCost = 1;
			}
			
			for(int i = 0; i < results.length; ++i) {
				if(results[i].equals("attacks")) {
					abilitiesIndex = i;
					break;
				}
			}
			
			if(abilitiesIndex != -1) {
				// parse pokemon abilities
				for(int i = abilitiesIndex + 1; i < results.length; i += 4) {
					try {
						int abilityIndex = Integer.parseInt(results[i]);
						abilities.add(new Ability(this.mAbilitiesList.get(abilityIndex - 1)));
					} catch (NumberFormatException e) {}
				} 
			}
			c = new Pokemon(pokemonName, categories, hp, retreatCost, abilities);
			if(basePokemonName != null) 
				((Pokemon)c).setBasePokemonName(basePokemonName);
		}
		else if(cardContents.matches("(.*)trainer(.*)"))
		{
			String[] results = cardContents.split(":");
			System.out.println("Created TrainerCard");
			ArrayList<Ability> abilities = new ArrayList<>();
			abilities.add(new Ability(this.mAbilitiesList.get(Integer.parseInt(results[4]) - 1)));
			c = new TrainerCard(results[0], results[3], abilities);

		}
		else if(cardContents.matches("(.*)energy(.*)"))
		{
			String[] results = cardContents.split(":");
			System.out.println("Created EnergyCard");
			c = new EnergyCard(results[0], results[3]);	
		}

		System.out.print(c.toString());
		return c;
	}
	
	public ArrayList<Card> DeckCreation(String deckLocation)
	{
		assert mCardList != null;
		
		ArrayList<Card> deck = new ArrayList<Card>();
		
		try
		{
			ArrayList<String> deckNumbers = Parser.GetFileContentsAsArrayList(deckLocation);
		
			for (String line : deckNumbers)
			{
			    Card e = CreateCard(this.mCardList.get(Integer.parseInt(line) - 1));
			    deck.add(e);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
				
		System.out.println("--------\nDsize: " + deck.size() + "\n--------");
		
		return deck;
	}
}
