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
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.player.Player;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class Regression121
{

	static GameBoard mockBoard = Mockito.mock(GameBoard.class);
	@Before
	public void setup(){
		GameController.setBoard(mockBoard);
		stub(method(GameController.class, "updateDeck")).toReturn(0);
		stub(method(GameController.class, "updateHand")).toReturn(0);
		stub(method(GameController.class, "getIsHomePlayerPlaying")).toReturn(true);
		stub(method(GameController.class, "displayMessage")).toReturn(0);
		GameController.setGameOver(false);
	}
	
	@Test
	public void victoryTest()
	{
		EnergyCard e1 = new EnergyCard("Water", "water");
		EnergyCard e2 = new EnergyCard("Water", "water");
		Deck deck = new Deck();
		deck.addCards(Arrays.asList(e1, e2));
		
		//System.out.println(GameController.checkGameOver());
		//set player
		//stub(method(Player.class, "getDeck")).toReturn(deck);
		Player player = new Player(deck);//mock(Player.class);
		//stub(method(GameController.class, "getActivePlayer")).toReturn(player);
		player.drawCardsFromDeck(2);
		player.startTurn();
		assertTrue(GameController.checkGameOver());
		//assert(true);
	}

	@Test
	public void notDeckedTest(){
		EnergyCard e1 = new EnergyCard("Water", "water");
		EnergyCard e2 = new EnergyCard("Water", "water");
		Deck deck = new Deck();
		deck.addCards(Arrays.asList(e1, e2));

		//System.out.println(GameController.checkGameOver());
		//set player
		//stub(method(Player.class, "getDeck")).toReturn(deck);
		Player player = new Player(deck);//mock(Player.class);
		//stub(method(GameController.class, "getActivePlayer")).toReturn(player);
		player.startTurn();
		assertFalse(GameController.checkGameOver());
		//assert(true);
	}

	@Test
	public void rewardVictoryTest() { 
		EnergyCard e1 = new EnergyCard("Water", "water");
		EnergyCard e2 = new EnergyCard("Water", "water");
		EnergyCard e3 = new EnergyCard("Water", "water");
		EnergyCard e4 = new EnergyCard("Water", "water");
		EnergyCard e5 = new EnergyCard("Water", "water");
		EnergyCard e6 = new EnergyCard("Water", "water");
		EnergyCard e7 = new EnergyCard("Water", "water");
		Deck deck = new Deck();
		deck.addCards(Arrays.asList(e1, e2, e3,e4,e5,e6, e7));

		Pokemon p1 = mock(Pokemon.class);


		Player player = new Player(deck);//mock(Player.class);

		Player opponent = spy(new Player());
		player.setOpponent(opponent);
		opponent.benchPokemon(p1);

		player.setUpRewards();
		for (int i = 0; i < 6; i++){
			player.collectPrize(0);
		}
		assertTrue(GameController.checkGameOver());


	}
}
