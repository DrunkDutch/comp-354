package com.dmens.pokeno.DeckTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dmens.pokeno.Card.Card;
import com.dmens.pokeno.Card.CardTypes;
import com.dmens.pokeno.Deck.Deck;
import com.dmens.pokeno.Utils.Parser;

public class DeckTest {
	private Deck validDeck;
	private Deck invalidDeck;
	
	@BeforeClass
	public static void loadParser(){
		Parser.Instance().LoadCards("data/cards.txt");
		Parser.Instance().LoadAbilities("data/abilities.txt");
	}
	
	@Before
	public void setup(){
		validDeck = Parser.Instance().DeckCreation("data/deck1.txt");
		invalidDeck = Parser.Instance().DeckCreation("data/deck1.txt");
		invalidDeck.draw(10);
	}

	@Test
	public void testDeckIsValid() {
		Assert.assertTrue(validDeck.checkValidity());
		Assert.assertFalse(invalidDeck.checkValidity());
	}
	
	@Test
	public void testDrawCard(){
		Card c = validDeck.draw();
		Assert.assertNotNull("Card should not be null",c);
		Assert.assertEquals(59, validDeck.size());
		//Fix this test when Shauna is implemented
		//Assert.assertTrue("Card Drawn should be Shauna. Got "+c.getName(), c.getName().equals("Shauna"));
		Assert.assertTrue("First Card implemented should an energy",c.isType(CardTypes.ENERGY));
	}
	
	@Test
	public void testShuffle(){
		Card c1  = validDeck.viewCardInPosition(6);
		Card c2  = validDeck.viewCardInPosition(12);
		Card c3  = validDeck.viewCardInPosition(13);
		System.out.println(validDeck);
		// Checking 3 cards to reduce test's flakyness 
		Assert.assertEquals("Card should be Machop",  "Machop", c1.getName());
		Assert.assertEquals("Card should be Machop",  "Machop", c2.getName());
		Assert.assertEquals("Card should be Machop",  "Machop", c3.getName());
		validDeck.shuffle();
		c1  = validDeck.viewCardInPosition(6);
		c2  = validDeck.viewCardInPosition(12);
		c3  = validDeck.viewCardInPosition(13);
		System.out.println(validDeck);
		Assert.assertFalse("Cards should not be Machop anymore", c1.getName().equals("Machop") && c2.getName().equals("Machop") && c3.getName().equals("Machop"));
	}

}
