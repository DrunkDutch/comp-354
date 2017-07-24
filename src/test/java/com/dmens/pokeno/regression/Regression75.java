package com.dmens.pokeno.regression;

import static org.junit.Assert.fail;

import org.junit.Assert;

/**
 * Regression test for bug #75(https://github.com/DrunkDutch/comp-354/issues/75)
 */

import org.junit.Test;
import org.mockito.Mockito;

import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.player.Player;

public class Regression75 {
	
	Player player = Mockito.spy(new Player());

	@Test
	public void test() {
		Mockito.doReturn(0).when(player).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
		
		// Create pokemon (Stage-one)
		Pokemon frogadier = new Pokemon("frogadier");
		frogadier.setCategory("stage-one");
		frogadier.setBasePokemonName("froakie");
		try{
			// Try to play frogadier as active
			player.useCard(frogadier);
			Assert.assertNull("Expected Frogadier to not have been played",player.getActivePokemon());
		}catch(Exception e){
			fail("Exception was thrown");
		}
	}

}
