package com.dmens.pokeno.ParserTest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.dmens.pokeno.Card.Card;
import com.dmens.pokeno.Card.EnergyCard;
import com.dmens.pokeno.Card.Pokemon;
import com.dmens.pokeno.Card.TrainerCard;
import com.dmens.pokeno.Utils.Parser;

public class ParserTest {

    private static final Logger LOG = LogManager.getLogger(ParserTest.class);

    static Parser mParserToTest = null;
    static String mLocationCurrentDir = null;
    static String mLocationDataDir = null;
    static String mValidCards = null;
    static String mLocationValidCards = null;
    static String mInvalidCards = null;
    static String mLocationInvalidCards = null;
    static String mValidAbilities = null;
    static String mLocationValidAbilities = null;
    static String mInvalidAbilities = null;
    static String mLocationInvalidAbilities = null;
    static String mValidDeck = null;
    static String mLocationValidDeck = null;
    static String mInvalidDeck = null;
    static String mLocationInvalidDeck= null;
    
	@BeforeClass
	public static void setUp() {
        LOG.info("Setting up Parser testing environment...");
		mParserToTest = Parser.Instance();
		mLocationCurrentDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		//mLocationDataDir = mLocationCurrentDir + "/data";
		mValidCards = "data/validCards.txt";
		mLocationValidCards = mLocationCurrentDir + mValidCards;
		mInvalidCards = "data/invalidCards.txt";
		mLocationInvalidCards = mLocationCurrentDir + mInvalidCards;
		mValidAbilities = "data/validAbilities.txt";
		mLocationValidAbilities = mLocationCurrentDir + mValidAbilities;
		mInvalidAbilities = "data/invalidAbilities.txt";
		mLocationInvalidAbilities = mLocationCurrentDir + mInvalidAbilities;
		mValidDeck = "data/validDeck.txt";
		mLocationValidDeck = mLocationCurrentDir + mValidDeck;
		mInvalidDeck = "data/invalidDeck.txt";
		mLocationInvalidDeck = mLocationCurrentDir + mInvalidDeck;

		File dataDir = new File(mLocationCurrentDir + "data");
		// if the data directory does not exist, create it
		if (!dataDir.exists()) {
	        LOG.info("Creating directory: : {}.", dataDir.getName());
		    try{
		        dataDir.mkdir();
		    } 
		    catch(Exception se){
				fail("Failed to create directory: " + dataDir.getName());
		    }        
		} 
	}
	
	@Test
	public void ParserLoadCardsTest() {
        LOG.info("Start running ParserLoadCardsTest...");
		ArrayList<String> validCardsContent = new ArrayList<String>();
		validCardsContent.add("Pokémon Center Lady:trainer:cat:supporter:1");
		validCardsContent.add("Doduo:pokemon:cat:basic:cat:colorless:60:retreat:cat:colorless:1:attacks:cat:colorless:1:2,cat:colorless:1:3");
		validCardsContent.add("Fight:energy:cat:fight");
		
		try{
		    PrintWriter writer = new PrintWriter(mLocationValidCards, "UTF-8");
		    for(int i = 0; i < validCardsContent.size(); ++i) {
		    	writer.println(validCardsContent.get(i));
		    }
		    writer.close();
		} catch (IOException e) {
			fail("Failed to create a valid cards file");
		}
		
		boolean result = mParserToTest.LoadCards(mValidCards);
		Assert.assertEquals(result, true);
		ArrayList<String> cardsContent = mParserToTest.GetCardList();
		Assert.assertNotNull(cardsContent);
		Assert.assertEquals(validCardsContent.size(), cardsContent.size());
		for(int i = 0; i < cardsContent.size(); ++i) {
			Assert.assertEquals(validCardsContent.get(i), cardsContent.get(i));
	    }
	
		File invalidCards = new File(mLocationInvalidCards);
		invalidCards.delete();
		result = mParserToTest.LoadCards(mInvalidCards);
		Assert.assertEquals(result, false);
        LOG.info("Success: ParserLoadCardsTest");
	}
	
	@Test
	public void ParserLoadAbilitiesTest() {
        LOG.info("Start running ParserLoadAbilitiesTest...");
		ArrayList<String> validAbilitiesContent = new ArrayList<String>();
		validAbilitiesContent.add("Rain Splash:dam:target:opponent-active:20");
		validAbilitiesContent.add("Doduo Delivery:draw:2");
		validAbilitiesContent.add("Mach Cross:dam:target:opponent-active:60");
		
		try{
		    PrintWriter writer = new PrintWriter(mLocationValidAbilities, "UTF-8");
		    for(int i = 0; i < validAbilitiesContent.size(); ++i) {
		    	writer.println(validAbilitiesContent.get(i));
		    }
		    writer.close();
		} catch (IOException e) {
			fail("Failed to create a valid abilities file");
		}
		
		boolean result = mParserToTest.LoadAbilities(mValidAbilities);
		Assert.assertEquals(result, true);
		ArrayList<String> abilitiesContent = mParserToTest.GetAbilitiesList();
		Assert.assertNotNull(abilitiesContent);
		Assert.assertEquals(validAbilitiesContent.size(), abilitiesContent.size());
		for(int i = 0; i < abilitiesContent.size(); ++i) {
			Assert.assertEquals(validAbilitiesContent.get(i), abilitiesContent.get(i));
	    }
	
		File invalidAbilities = new File(mLocationInvalidAbilities);
		invalidAbilities.delete();
		result = mParserToTest.LoadAbilities(mInvalidAbilities);
		Assert.assertEquals(result, false);
        LOG.info("Success: ParserLoadAbilitiesTest");
	}

	@Test
	public void ParserDeckCreationTest() {
        LOG.info("Start running ParserDeckCreationTest...");
        
        ArrayList<String> validCardsContent = new ArrayList<String>();
		validCardsContent.add("Pokémon Center Lady:trainer:cat:supporter:1");
		validCardsContent.add("Doduo:pokemon:cat:basic:cat:colorless:60:retreat:cat:colorless:1:attacks:cat:colorless:1:2,cat:colorless:1:3");
		validCardsContent.add("Fight:energy:cat:fight");
		
		try{
		    PrintWriter writer = new PrintWriter(mLocationValidCards, "UTF-8");
		    for(int i = 0; i < validCardsContent.size(); ++i) {
		    	writer.println(validCardsContent.get(i));
		    }
		    writer.close();
		} catch (IOException e) {
			fail("Failed to create a valid cards file");
		}
		boolean result = mParserToTest.LoadCards(mValidCards);
		// make sure cards are loaded successfully
		Assert.assertEquals(result, true);

		ArrayList<String> validAbilitiesContent = new ArrayList<String>();
		validAbilitiesContent.add("Rain Splash:dam:target:opponent-active:20");
		validAbilitiesContent.add("Doduo Delivery:draw:2");
		validAbilitiesContent.add("Mach Cross:dam:target:opponent-active:60");
		
		try{
		    PrintWriter writer = new PrintWriter(mLocationValidAbilities, "UTF-8");
		    for(int i = 0; i < validAbilitiesContent.size(); ++i) {
		    	writer.println(validAbilitiesContent.get(i));
		    }
		    writer.close();
		} catch (IOException e) {
			fail("Failed to create a valid abilities file");
		}
		
		result = mParserToTest.LoadAbilities(mValidAbilities);
		// make sure abilities are loaded successfully
		Assert.assertEquals(result, true);
		
		ArrayList<String> validDeckContent = new ArrayList<String>();
		validDeckContent.add("2");
		validDeckContent.add("1");
		validDeckContent.add("3");
		
		try{
		    PrintWriter writer = new PrintWriter(mLocationValidDeck, "UTF-8");
		    for(int i = 0; i < validDeckContent.size(); ++i) {
		    	writer.println(validDeckContent.get(i));
		    }
		    writer.close();
		} catch (IOException e) {
			fail("Failed to create a valid deck file");
		}
		
		ArrayList<Card> deckContent = mParserToTest.DeckCreation(mValidDeck);
		Assert.assertNotNull(deckContent);
		Assert.assertEquals(validDeckContent.size(), deckContent.size());
		
        LOG.info("Start validating each card in the deck...");
		Card card = deckContent.get(0);
/*		Assert.assertEquals(card instanceof Pokemon, true);
		Pokemon pokemonCard = (Pokemon)card;
		Assert.assertEquals(pokemonCard.getName(), "Doduo");
		Assert.assertEquals(pokemonCard.getHP(), 60);
		Assert.assertEquals(pokemonCard.getCategories().get(0), "basic");
		Assert.assertEquals(pokemonCard.getCategories().get(1), "colorless");
		Assert.assertEquals(pokemonCard.getRetreatCost(), 1);
		*/
		// TODO: validate its abilities

		
		card = deckContent.get(1);
		/*Assert.assertEquals(card instanceof TrainerCard, true);
		TrainerCard trainerCard = (TrainerCard)card;
		Assert.assertEquals(trainerCard.getName(), "Pokémon Center Lady");
		Assert.assertEquals(trainerCard.getCategory(), "supporter");
		*/
		// TODO: validate its ability

		card = deckContent.get(2);
		Assert.assertEquals(card instanceof EnergyCard, true);
		EnergyCard energyCard = (EnergyCard)card;
		Assert.assertEquals(energyCard.getName(), "Fight");
		Assert.assertEquals(energyCard.getCategory(), "fight");
			
        LOG.info("Success: ParserDeckCreationTest");
	}
}
