package com.dmens.pokeno.servicesTest;

import static org.junit.Assert.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.Arrays;

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
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.CountService;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.Deck;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class CountServiceTest {

	private static CountService service = CountService.getInstance();
	private static Player p1 , p2;
	
	@BeforeClass
	public static void setupPlayers(){	 
		Deck firstDeck = new Deck();
		firstDeck.addCards(Arrays.asList(new EnergyCard("Water","water"), new EnergyCard("Water","water"), new EnergyCard("Water", "water"), 
										new EnergyCard("Water","water"), new EnergyCard("Water","water"), new EnergyCard("Water", "water")));
		p1 = Mockito.spy(new Player(firstDeck));
		
		Deck secondDeck = new Deck();
		secondDeck.addCards(Arrays.asList(new EnergyCard("Colorless","Colorless"), new EnergyCard("Colorless","Colorless"), new EnergyCard("Colorless", "Colorless")));
		p2 = Mockito.spy(new Player(secondDeck));
		
		Mockito.doNothing().when(p1).setActiveOnBoard();
		Mockito.doNothing().when(p2).setActiveOnBoard();
		
		// Set Active Pokemon
		p1.setActivePokemon(new Pokemon("Raichu"));
		p2.setActivePokemon(new Pokemon("Machop"));
		
		PowerMockito.mockStatic(GameController.class);
	}
	
	@Test
	public void testGetBenchPokemonCount(){
		Mockito.doNothing().when(p1).benchPokemonOnBoard();
		
		p1.benchPokemon(new Pokemon("Espurr"));
		p1.benchPokemon(new Pokemon("Froakie"));
		p1.benchPokemon(new Pokemon("Pikachu"));
		
		stub(method(GameController.class, "getActivePlayer")).toReturn(p1);
		assertEquals(service.getCount("target:your-bench"), 3);
	}

	@Test
	public void testGetHandCount(){	
    	stub(method(GameController.class, "updateHand")).toReturn(0);
    	stub(method(GameController.class, "updateDeck")).toReturn(0);

		p1.drawCardsFromDeck(3);
		stub(method(GameController.class, "getActivePlayer")).toReturn(p1);
		assertEquals(service.getCount("target:your-hand"), 3);
		
		p2.drawCardsFromDeck(2);
		stub(method(GameController.class, "getActivePlayer")).toReturn(p1);
		Mockito.doReturn(p2).when(p1).getOpponent();
		assertEquals(service.getCount("target:opponent-hand"), 2);
	}
	
	@Test
	public void testGetActiveCount(){	
		p1.getActivePokemon().addDamage(20);
		stub(method(GameController.class, "getActivePlayer")).toReturn(p1);
		assertEquals(service.getCount("target:your-active:damage"), 2);

    	stub(method(GameController.class, "updateHand")).toReturn(0);
    	stub(method(GameController.class, "updateDeck")).toReturn(0);
    	p1.drawCardsFromDeck(3);
		p1.attachEnergy((EnergyCard)(p1.getHand().getCards().get(0)), p1.getActivePokemon());
		p1.attachEnergy((EnergyCard)(p1.getHand().getCards().get(1)), p1.getActivePokemon());
		p1.attachEnergy((EnergyCard)(p1.getHand().getCards().get(2)), p1.getActivePokemon());
		assertEquals(service.getCount("target:your-active:energy"), 3);
	}
}
