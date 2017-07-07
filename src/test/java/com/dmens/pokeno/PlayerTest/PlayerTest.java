package com.dmens.pokeno.PlayerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static  org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.Arrays;

import javax.swing.JOptionPane;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.DeckCreator;
import com.dmens.pokeno.view.GameBoard;

/**
 * Basic test class for the Player class. 
 * 
 * @author tarikabou-saddik
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class PlayerTest{
	
	private static final String LOCATION_FIRST_DECK = "decks/deck1.txt";
	private static final String LOCATION_SECOND_DECK = "decks/deck2.txt";
	private static final String LOCATION_CARDS = "cards.txt";
	private static final String LOCATION_ABILITIES = "abilities.txt";
	private static Deck testDeck1;
	
	static GameBoard mockBoard = Mockito.mock(GameBoard.class);
	
	@BeforeClass
	public static void setup(){
		AbilitiesDatabase.getInstance().initialize(LOCATION_ABILITIES);
		CardsDatabase.getInstance().initialize(LOCATION_CARDS);
		
		testDeck1 = DeckCreator.Instance().DeckCreation(LOCATION_FIRST_DECK);
		
		//Mocks
		GameController.setBoard(mockBoard);
		PowerMockito.mockStatic(GameController.class);
	}
	
	
	@Test
	public void testSetupRewards() throws NoSuchMethodException, SecurityException, Exception
	{			
		Player player = new Player(testDeck1);
		//! Set up rewards cards for players
		player.setUpRewards();
		
		//! Run assertions to test number of rewards cards attributed to each player. 
		assertTrue(player.getRewards().size() <= 6 && player.getRewards().size() > 0);
	}
	
	@Test
	public void testEvolveActivePokemon(){
		Player player = new Player(testDeck1);
		// Mocks
		stub(method(GameController.class, "dispayCustomOptionPane")).toReturn(0);
		//Create pokemon 1 (Basic)
		Pokemon froakie = new Pokemon("froakie");
		froakie.setCategory("basic");
		// Create pokemon 2 (Stage-one)
		Pokemon frogadier = new Pokemon("frogadier");
		frogadier.setCategory("stage-one");
		frogadier.setBasePokemonName("froakie");
		
		// Set froakie as active
		player.useCard(froakie);
		assertEquals(froakie, player.getActivePokemon());
		// Evolve froakie by using frogadier
		player.useCard(frogadier);
		
		// Frogadier should be the new active pokemon
		assertEquals(frogadier, player.getActivePokemon());
	}	
	
	@Test
	public void testEvolveBenchedPokemon(){
		Player player = new Player(testDeck1);
		// Mocks
		stub(method(GameController.class, "dispayCustomOptionPane")).toReturn(0);
		//Create pokemon 1 (Basic)
		Pokemon froakie = new Pokemon("froakie");
		froakie.setCategory("basic");
		// Create pokemon 2 (Stage-one)
		Pokemon frogadier = new Pokemon("frogadier");
		frogadier.setCategory("stage-one");
		frogadier.setBasePokemonName("froakie");
		// Create Pokemon 3
		Pokemon machop = new Pokemon("Machop");
		machop.setCategory("basic");
		
		// Set Machop as active
		player.useCard(machop);
		assertEquals(machop, player.getActivePokemon());
		// Bench froakie
		player.useCard(froakie);
		assertEquals(froakie, player.getBenchedPokemon().get(0));
		// Evolve froakie by using frogadier
		player.useCard(frogadier);
		// Frogadier should be the only pokemon in the bench now
		assertEquals(frogadier, player.getBenchedPokemon().get(0));
	}	
	
	@Test
	public void testMuliggans(){
		// Mocks & stubs
		stub(method(GameController.class, "displayMessage")).toReturn(0);
		stub(method(GameController.class, "displayConfirmDialog")).toReturn(JOptionPane.YES_OPTION);
		//Create pokemon 1 (Basic)
		Pokemon froakie = new Pokemon("froakie");
		froakie.setCategory("basic");
		// Create pokemon 2 (Stage-one)
		Pokemon frogadier = new Pokemon("frogadier");
		frogadier.setCategory("stage-one");
		frogadier.setBasePokemonName("froakie");
		// Create mock decks
		Deck mockDeck1 = new Deck();
		mockDeck1.addCards(Arrays.asList(frogadier,new EnergyCard("Water", "water"),new EnergyCard("Water", "water"),
				froakie,new EnergyCard("Water", "water"),new EnergyCard("Water", "water")));
		
		Deck mockDeck2 = new Deck();
		mockDeck2.addCards(Arrays.asList(froakie,new EnergyCard("Water", "water"),new EnergyCard("Water", "water"),new EnergyCard("Water", "water")));
		
		// Create players
		Player player1 = new Player(mockDeck1);
		Player player2 = new Player(mockDeck2);
		player1.setOpponent(player2);
		player2.setOpponent(player1);
		
		// Draw cards
		player1.drawCardsFromDeck(3);
		player2.drawCardsFromDeck(3);
		
		// Assert player 1 is not ready and player 2 is ready
		player1.checkIfPlayerReady();
		player2.checkIfPlayerReady();
		assertTrue(player1.isInMulliganState());
		assertFalse(player2.isInMulliganState());
		
		// Do the mulligans on player 1 and get a new hand
		player1.mulligan();
		player1.drawCardsFromDeck(3);
		player1.checkIfPlayerReady();
		
		// Assert opponent got 1 card from the deck
		assertEquals(4, player2.getHand().size());
		
		// Assert both players are ready
		assertTrue(player1.getIsReadyToStart());
		assertTrue(player2.getIsReadyToStart());
	}
}
