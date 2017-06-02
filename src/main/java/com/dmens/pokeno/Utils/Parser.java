package com.dmens.pokeno.Utils;
import com.dmens.pokeno.Ability.Ability;
import com.dmens.pokeno.Card.*;
import com.dmens.pokeno.Effect.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Parser {

    private static final Logger LOG = LogManager.getLogger(Parser.class);
    
    private static boolean mSupportedPokemonOnly = true;
    private static String[] mSupportedPokemon = {"Froakie", "Electrike"};
    
    private static boolean mSupportedTrainersOnly = true;
    private static String[] mSupportedTrainers = {"Potion"};

	private static Parser instance = null;
	private static final String ENCODING = "UTF-8";
	
	private ArrayList<String> mCardList = null;
	private ArrayList<String> mAbilitiesList = null;
	
	private Parser() { }
	
	/*
	 * lazy instantiation
	 */
	public static Parser Instance( ) {
		if(instance == null)
		{
			instance = new Parser();
		}
		
		return instance;
	}
	
	public ArrayList<String> GetCardList() {
		return mCardList;
	}
	
	public ArrayList<String> GetAbilitiesList() {
		return mAbilitiesList;
	}
	
	/*
	 * Get the contents as an ArrayList which will facilitate specific read lines to be accessed.
	 */
	private static ArrayList<String> GetFileContentsAsArrayList(String location) throws FileNotFoundException
	{

		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
		if(in == null)
			throw new FileNotFoundException();
		Scanner scanner = new Scanner(in, ENCODING);
		
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
			//e.printStackTrace();
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
			//e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// public for testing
	public Ability CreateAbility(String abilityInformation)
	{	
    	int i = abilityInformation.indexOf(":");	
    	String name = abilityInformation.substring(0,i);
    	
    	// 1) Create the ability, you got its name
    	Ability ability = new Ability(name);
    	
    	// 2) Look at the rest of the string and figure it out!
    	String restStr = abilityInformation.substring(i + 1, abilityInformation.length());
    	
    	int c = restStr.indexOf(",");
    	
    	if(c > -1)
    	{
    		// Split on Comma
    		String[] bigResults = restStr.split(":");
    		
    		for(int k = 0; k < bigResults.length; ++k)
    		{
    			// it should either be an effect or condition
    			
    			ability.AddEffect(ParseEfect(restStr));
			}
    		
    	}
    	else
    	{
    		ability.AddEffect(ParseEfect(restStr));
    	}
    	
    	return ability;
	}
	
	private Effect ParseEfect(String restStr)
	{
		String[] results = restStr.split(":");
		int c = restStr.indexOf(",");
		
		System.out.println(results[0]);
		
		if(results[0].equals("dam"))
    	{  			
			System.out.println("DAMAGE effect added");
			return new Damage(results[2], Integer.parseInt(results[3]));
    	}
		else if(results[0].equals("heal") && c == -1)
		{
			System.out.println("HEAL effect added");
			return new Heal(results[2], Integer.parseInt(results[3]));
		}
		
		// fix because it wasnt implemented
		return null;
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
			
			if(mSupportedPokemonOnly)
			{
				boolean supported = false;
				
				//check if its supported
				for(int i = 0; i < mSupportedPokemon.length; ++i)
				{
					if(mSupportedPokemon[i].equals(pokemonName))
						supported = true;
				}
				
				if(!supported)
				{
					System.out.println("Pokemon was not supported");
					return (Card) this.ReplaceCardWithRandomEnergy();
				}
					
			}
			
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
						abilities.add(CreateAbility(this.mAbilitiesList.get(abilityIndex - 1)));
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
			if(mSupportedTrainersOnly)
			{
				boolean supported = false;
				
				//check if its supported
				for(int i = 0; i < mSupportedTrainers.length; ++i)
				{
					if(mSupportedTrainers[i].equals(results[0]))
						supported = true;
				}
				
				if(!supported)
				{
					System.out.println("Trainer was not supported");
					return (Card) this.ReplaceCardWithRandomEnergy();
				}	
			}

			System.out.println("Created TrainerCard");
			ArrayList<Ability> abilities = new ArrayList<>();
			abilities.add(CreateAbility(this.mAbilitiesList.get(Integer.parseInt(results[4]) - 1)));
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
	
	private EnergyCard ReplaceCardWithRandomEnergy()
	{
		Random rand = new Random();
		int MIN = 1;
		int MAX = 5;
		
		int num = MIN + rand.nextInt((MAX - MIN) + 1);
		
		System.out.println("Rand #: " + num);
		switch(num)
		{
			case(1):
				return new EnergyCard("Water", "water");
			case(2):
				return new EnergyCard("Lightning", "lightning");
			case(3):
				return new EnergyCard("Fight", "fight");
			case(4):
				return new EnergyCard("Psychic", "psychic");
			default:
				return new EnergyCard("Colorless", "colorless");
		}
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
