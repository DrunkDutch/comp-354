package com.dmens.pokeno.regression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
/**
 * Regression test for bug #113(https://github.com/DrunkDutch/comp-354/issues/113)
 */

public class Regression113 {
	
	private static Player p1 = Mockito.spy(new Player());
	private static Player p2 = Mockito.spy(new Player());
	
	@Before
	public void setup(){
		p1.setOpponent(p2);
		p2.setOpponent(p1);
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
	}

	@Test
	public void test() {
		// Get hitmonlee
		Pokemon hitmonlee = (Pokemon) CardsDatabase.getInstance().query(44);
		hitmonlee.addEnergy(new EnergyCard("Fight", "fight"));	// Give required energy to hitmonlee for attack 1
		Pokemon froakie = (Pokemon) CardsDatabase.getInstance().query(9);
		Pokemon zubat = (Pokemon) CardsDatabase.getInstance().query(37);
		
		// set active
		Mockito.doNothing().when(p1).setActiveOnBoard();
		Mockito.doNothing().when(p2).setActiveOnBoard();
		p1.setActivePokemon(hitmonlee);
		p2.setActivePokemon(froakie);
		// set bench for p2
		Mockito.doNothing().when(p2).benchPokemonOnBoard();
		p2.benchPokemon(zubat);
		// Attack with hitmonlee
		TargetServiceHandler.getInstance().setYouPlayer(p1);
		TargetServiceHandler.getInstance().setThemPlayer(p2);
		// Choose zubat on bench to attack until it faints
		Mockito.doReturn(0).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doNothing().when(p1).updateActivePokemonOnBoard();
		Mockito.doNothing().when(p2).updateActivePokemonOnBoard();
		Mockito.doNothing().when(p1).updateBoard();
		Mockito.doNothing().when(p2).updateBoard();
		p1.useActivePokemon(0);
		p1.useActivePokemon(0);
		// Assert zubat fainted and is out of the bench
		assertTrue(p2.getBenchedPokemon().isEmpty());
	}

}
