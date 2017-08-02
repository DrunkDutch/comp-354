package com.dmens.pokeno.regression;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.ArrayList;
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
import com.dmens.pokeno.services.TargetService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class Regression109
{
	@Before
	public void setup(){
		PowerMock.mockStatic(GameController.class);
	}
	
	@Test
	public void test()
	{
		stub(method(GameController.class, "updateDeck")).toReturn(0);
		stub(method(GameController.class, "updateHand")).toReturn(0);
		stub(method(GameController.class, "getIsHomePlayerPlaying")).toReturn(true);
		
		
		boolean cardWasDiscarded =  false;
		
		// Create example trainer card
		Ability abilityPotion = new Ability("Potion");
        TrainerCard tCard = new TrainerCard("Potion", "item", abilityPotion);
        
		EnergyCard e1 = new EnergyCard("Water", "water");
		EnergyCard e2 = new EnergyCard("Water", "water");
		Deck deck = new Deck();
		deck.addCards(Arrays.asList(tCard, e1, e2));
		
		//set player
		Player player = new Player(deck);
		TargetService.getInstance().setYouPlayer(player);
		stub(method(GameController.class, "getActivePlayer")).toReturn(player);
		player.drawCardsFromDeck(3);
		player.useCard(tCard);
		
		ArrayList<Card> graveyard = player.getDiscards().getCards();
		for (Card c : graveyard)
		{
			if (c.equals(tCard))
				cardWasDiscarded = true;
		}
		
		assertEquals(cardWasDiscarded, true);
	}
}
