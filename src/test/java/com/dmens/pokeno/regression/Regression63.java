package com.dmens.pokeno.regression;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.player.Player;
/**
 * Regression test for bug #63(https://github.com/DrunkDutch/comp-354/issues/63)
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class Regression63 {	
	@Before
	public void setup(){
		PowerMock.mockStatic(GameController.class);
	}

	@Test
	public void test() {
		stub(method(GameController.class, "setActivePokemonOnBoard")).toReturn(0);
		stub(method(GameController.class, "updateDeck")).toReturn(0);
		stub(method(GameController.class, "updateEnergyCounters")).toReturn(0);
		stub(method(GameController.class, "updateHand")).toReturn(0);
		stub(method(GameController.class, "dispayCustomOptionPane")).toReturn(0);
		// Create pokemon
		Pokemon froakie = new Pokemon("Froakie");
		froakie.setCategory("basic");
		EnergyCard e1 = new EnergyCard("Water", "water");
		EnergyCard e2 = new EnergyCard("Water", "water");
		Deck deck = new Deck();
		deck.addCards(Arrays.asList(froakie, e1, e2));
		Player player = new Player(deck);
		player.drawCardsFromDeck(3);
		// set player
		// Give pokemon to player
		player.useCard(froakie);
		
		Assert.assertNotNull("Active pokemon should be set", player.getActivePokemon());
		
		// try to attach energy twice
		player.useCard(e1);
		player.useCard(e2);
		
		// Only 1 of them should have been attached and the other should still be in hand
		assertEquals(1, player.getActivePokemon().getAttachedEnergy().size());
		assertEquals(1, player.getHand().getCards().size());
		assertEquals(e2, player.getHand().getCards().get(0));
		
		// end turn and play other energy
		player.endTurn();
		
		player.useCard(e2);
		
		assertEquals(2, player.getActivePokemon().getAttachedEnergy().size());
		assertEquals(0, player.getHand().getCards().size());
		
	}

}
