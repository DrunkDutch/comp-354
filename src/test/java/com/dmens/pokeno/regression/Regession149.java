package com.dmens.pokeno.regression;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.ArrayList;
import java.util.Arrays;

import com.dmens.pokeno.view.GameBoard;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.card.TrainerCard;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class Regession149
{
	static GameBoard mockBoard = Mockito.mock(GameBoard.class);
	
	private static Player p1 = Mockito.spy(new Player());
	private static Player p2 = Mockito.spy(new Player());
	
	@Before
	public void setup(){
		p1.setOpponent(p2);
		p2.setOpponent(p1);
		AbilitiesDatabase.getInstance().initialize("abilities.txt");
		CardsDatabase.getInstance().initialize("cards.txt");
		GameController.setBoard(mockBoard);
		stub(method(GameController.class, "updateDeck")).toReturn(0);
		stub(method(GameController.class, "updateHand")).toReturn(0);
		stub(method(GameController.class, "getIsHomePlayerPlaying")).toReturn(true);
		stub(method(GameController.class, "displayMessage")).toReturn(0);
		GameController.setGameOver(false);
	}
	
	@Test
	public void noAttackFirstTest()
	{
		p1 = Mockito.spy(new Player());
		p2 = Mockito.spy(new Player());
		p1.setOpponent(p2);
		p2.setOpponent(p1);
		
		// Get hitmonlee
		Pokemon hitmonlee = (Pokemon) CardsDatabase.getInstance().query(44);
		Pokemon froakie = (Pokemon) CardsDatabase.getInstance().query(9);
		froakie.addEnergy(new EnergyCard("Water", "water"));	// Give required energy to froakie for attack 1
		
		// set active
		Mockito.doNothing().when(p1).setActiveOnBoard();
		Mockito.doNothing().when(p2).setActiveOnBoard();
		p1.setActivePokemon(froakie);
		p2.setActivePokemon(hitmonlee);
		ArrayList<Player> players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
		GameController.setPlayers(players);
		// Attack with froakie
		TargetServiceHandler.getInstance().setYouPlayer(p1);
		TargetServiceHandler.getInstance().setThemPlayer(p2);

		Mockito.doReturn(0).when(p1).chooseCards(Mockito.any(), Mockito.any(), Mockito.any());
		Mockito.doNothing().when(p1).updateActivePokemonOnBoard();
		Mockito.doNothing().when(p2).updateActivePokemonOnBoard();
		Mockito.doNothing().when(p1).updateBoard();
		Mockito.doNothing().when(p2).updateBoard();
    	GameController.useActivePokemonForPlayer(0, 0);

    	//assert that the attack attempt did no damage
		assertTrue(p2.getActivePokemon().getDamage() == 0);
	}
	
}
