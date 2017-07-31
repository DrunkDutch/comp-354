package com.dmens.pokeno.servicesTest;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.TargetService;

public class TargetServiceTest {
	
	private static TargetService service = TargetService.getInstance();
	private static Player p1 = Mockito.spy(new Player()), p2 = Mockito.spy(new Player());
	
	@BeforeClass
	public static void setupPlayers(){	
		Mockito.doNothing().when(p1).setActiveOnBoard();
		Mockito.doNothing().when(p2).setActiveOnBoard();
		// Set Active Pokemon
		p1.setActivePokemon(new Pokemon("Pikachu"));
		p2.setActivePokemon(new Pokemon("Machop"));
		
		p1.setOpponent(p2);
		p2.setOpponent(p1);
		
		// Set target service turn
		service.setYouPlayer(p1);
		service.setThemPlayer(p2);
	}
	
	@Test
	public void testGetActivePokemon(){
		Mockito.doNothing().when(p1).setActiveOnBoard();
		// P1's turn
		//expect ' opponent-active to be p2's active pokemon
		assertEquals((Pokemon) service.getTarget("opponent-active").get(0), p2.getActivePokemon());
		// Assert your-active equals p1's active
		assertEquals((Pokemon) service.getTarget("your-active").get(0), p1.getActivePokemon());
		// Switch to P2's turn
		service.passTurn();
		// Assert opponent-active now is p1's
		assertEquals((Pokemon) service.getTarget("opponent-active").get(0), p1.getActivePokemon());
		// Assert your-active equals p2's active
		assertEquals((Pokemon) service.getTarget("your-active").get(0), p2.getActivePokemon());
		// Switch turn back to p1
		service.passTurn();
	}
	
	@Test
	public void testChoosePokemon(){
		Mockito.doNothing().when(p1).benchPokemonOnBoard();
		Mockito.doNothing().when(p2).benchPokemonOnBoard();
		// Bench one pokemon
		p1.benchPokemon(new Pokemon("Ducklett"));
		p1.benchPokemon(new Pokemon("Zubat"));
		p2.benchPokemon(new Pokemon("Ducklett"));
		p2.benchPokemon(new Pokemon("Zubat"));
		// when choose pokemon target return active
		Mockito.doReturn(0).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		assertEquals(service.getTarget("choice:your").get(0), p1.getActivePokemon());
		// when choose pokemon target return first in the bench
		Mockito.doReturn(1).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		assertEquals(service.getTarget("choice:your").get(0), p1.getBenchedPokemon().get(0));
		// when choose benched pokemon target return first in the bench
		Mockito.doReturn(0).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		assertEquals(service.getTarget("choice:your-bench").get(0), p1.getBenchedPokemon().get(0));
		Mockito.doReturn(1).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		assertEquals(service.getTarget("choice:your-bench").get(0), p1.getBenchedPokemon().get(1));
		// Choose from opponent bench
		Mockito.doReturn(0).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		assertEquals(service.getTarget("choice:opponent-bench").get(0), p2.getBenchedPokemon().get(0));
	}

}
