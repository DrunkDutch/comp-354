package com.dmens.pokeno.DeckTest;

import java.util.List;

import com.dmens.pokeno.card.Pokemon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.utils.DeckCreator;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.deck.Hand;

public class HandTest {
	private Hand hand;
	private Deck deck;
	
	@BeforeClass
	public static void loadParser(){		
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
	}
	
	@Before
	public void setupCardContainer(){
		deck = DeckCreator.Instance().DeckCreation("decks/deck1.txt");
		hand = new Hand();
	}
	
	@Test
	public void testDrawCardsToHand(){
		hand.addCards(deck.draw(7));
		Assert.assertEquals(7, hand.size());
		Assert.assertEquals(53, deck.size());
	}
	
	@Test
	public void testGetPokemon(){
		hand.addCards(deck.draw(6));
		List<Pokemon> pokemon = hand.getPokemon();
		Assert.assertTrue(pokemon.size()==2);
	}

}
