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

        stub(method(GameController.class, "updateHand")).toReturn(0);
        stub(method(GameController.class, "getActivePlayer")).toReturn(player);
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

        stub(method(GameController.class, "getIsHomePlayerPlaying")).toReturn(true);
        stub(method(GameController.class, "getHomePlayer")).toReturn(player);
        stub(method(Player.class, "createPokemonOptionPane")).toReturn(0);
        stub(method(GameController.class, "getActivePlayer")).toReturn(player);

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

    @Test
    public void testSwapEffect(){
    	Deck deck = new Deck();
    	// Need to clear out the null pointers in the card database, otherwise 'queryByName' will raise an exception trying to access null pointer
    	((CardsDatabase)CardsDatabase.getInstance()).removeNullPointersInDB();
    	deck.addCards(Arrays.asList(((CardsDatabase)CardsDatabase.getInstance()).queryByName("Switch")));
    	Player player = new Player(deck);
    	
    	stub(method(GameController.class, "updateHand")).toReturn(0);
    	stub(method(GameController.class, "getActivePlayer")).toReturn(player);

    	player.setActivePokemon(new Pokemon("Froakie"));
    	assertEquals("Froakie", player.getActivePokemon().getName());
    	
    	player.benchPokemon(new Pokemon("Pikachu"));
    	assertEquals("Pikachu", player.getBenchedPokemon().get(0).getName());
    	
    	// Draw Switch
    	player.drawCardsFromDeck(1);
    	assertEquals(1, player.getHand().size());
    	assertEquals("Switch", player.getHand().getCards().get(0).getName());
    	
    	// Use Switch
    	(TargetServiceHandler.getInstance()).setYouPlayer(player);
    	stub(method(GameController.class, "dispayCustomOptionPane")).toReturn(0);
    	player.useCard(player.getHand().getCards().get(0));
    	assertEquals(0, player.getHand().size());
    	
    	// varify the active pokemon and the benched pokemon are swapped
    	assertEquals("Pikachu", player.getActivePokemon().getName());
    	assertEquals("Froakie", player.getBenchedPokemon().get(0).getName());
    }
}