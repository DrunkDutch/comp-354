package com.dmens.pokeno.regression;
import static org.junit.Assert.assertEquals;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.ArrayList;
import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class Regression121
{
	@Before
	public void setup(){
		PowerMock.mockStatic(GameController.class);
	}
	
	@Ignore
	public void victoryTest()
	{
		
		//Test for decking out
		stub(method(GameController.class, "updateDeck")).toReturn(0);
		stub(method(GameController.class, "updateHand")).toReturn(0);
		stub(method(GameController.class, "getIsHomePlayerPlaying")).toReturn(true);
		stub(method(GameController.class, "displayMessage")).toReturn(0);
		//PowerMockito.when(System.exit(0)).thenReturn("abc");
		
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
		//System.out.println(GameController.checkGameOver());
		assert(GameController.checkGameOver());
		//assert(true);
	}
		
}
