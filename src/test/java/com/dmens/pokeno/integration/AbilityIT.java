package com.dmens.pokeno.integration;
import static org.junit.Assert.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.Arrays;

import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.dmens.pokeno.ability.Ability;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.effect.ApplyStatus;
import com.dmens.pokeno.effect.Damage;
import com.dmens.pokeno.effect.DrawCard;
import com.dmens.pokeno.effect.Heal;
import com.dmens.pokeno.player.Player;

/**
 *
 * @author James
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameController.class})
@PowerMockIgnore("javax.management.*")
public class AbilityIT {

    static String mAbilityName = "Ability";
    static String mEffectTarget = "opponent-active";
    static int mEffectValue = 20;
    static int mEffectValueDifferent = 10;
    static String mEffectStatus = "asleep";
    static String mEffectStatusDifferent = "poisoned";

    @Before
    public void setup(){
        AbilitiesDatabase.getInstance().initialize("abilities.txt");
        CardsDatabase.getInstance().initialize("cards.txt");

        //Mocks
        PowerMockito.mockStatic(GameController.class);
    }

    @Test
    public void testDrawEffect(){
        Deck deck = new Deck();
        deck.addCards(Arrays.asList(((CardsDatabase)CardsDatabase.getInstance()).queryByName("Tierno"),
                new EnergyCard("Water","water"), new EnergyCard("Water","water"), new EnergyCard("Water", "water")));
        Player player = Mockito.spy(new Player(deck));
        TargetServiceHandler.getInstance().setYouPlayer(player);

        Mockito.doNothing().when(player).setActiveOnBoard();
        Mockito.doNothing().when(player).updateActivePokemonOnBoard();
        Mockito.doNothing().when(player).benchPokemonOnBoard();
        Mockito.doNothing().when(player).updateBoard();
        Mockito.doNothing().when(player).updateDiscardsOnBoard();
        
        // Draw Tierno
        player.drawCardsFromDeck(1);
        assertEquals(1, player.getHand().size());
        assertEquals("Tierno", player.getHand().getCards().get(0).getName());
        // Use Tierno
        player.useCard(player.getHand().getCards().get(0));
        // Expect hand size to be 3 now
        assertEquals(3, player.getHand().size());
    }

    @Test
    public void testHealEffect(){
        Deck deck = new Deck();
        deck.addCards(Arrays.asList(((CardsDatabase)CardsDatabase.getInstance()).queryByName("Potion")));
        Player player = Mockito.spy(new Player(deck));
        TargetServiceHandler.getInstance().setYouPlayer(player);

        Mockito.doNothing().when(player).setActiveOnBoard();
        Mockito.doNothing().when(player).updateActivePokemonOnBoard();
        Mockito.doNothing().when(player).benchPokemonOnBoard();
        Mockito.doNothing().when(player).updateBoard();
        Mockito.doNothing().when(player).updateDiscardsOnBoard();

        // Set Froakie as the active pokemon
        player.setActivePokemon(new Pokemon("Froakie"));
        assertEquals("Froakie", player.getActivePokemon().getName());

        //Add 40 damage to Froakie
        player.getActivePokemon().addDamage(40);
        assertEquals(40, player.getActivePokemon().getDamage());

        // Draw Potion
        player.drawCardsFromDeck(1);
        assertEquals(1, player.getHand().size());
        assertEquals("Potion", player.getHand().getCards().get(0).getName());

        // Use Potion
        player.useCard(player.getHand().getCards().get(0));
        assertEquals(0, player.getHand().size());

        // Expect the damage taken by Froakie to be 10 (Potion heals 30 damage)
        assertEquals(10, player.getActivePokemon().getDamage());
    }

}