package com.dmens.pokeno.integration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.stub;

import java.util.Arrays;
import java.util.Map;

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
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.card.TrainerCard;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

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
    
    @Test
    public void testSearchEffect(){
    	Deck deck = new Deck();
    	// Need to clear out the null pointers in the card database, otherwise 'queryByName' will raise an exception trying to access null pointer
    	((CardsDatabase)CardsDatabase.getInstance()).removeNullPointersInDB();
    	TrainerCard clemont = (TrainerCard) ((CardsDatabase)CardsDatabase.getInstance()).queryByName("Clemont");
    	EnergyCard e1 = new EnergyCard("Colorless", "colorless");
    	EnergyCard e2 = new EnergyCard("Colorless", "colorless");
    	EnergyCard e3 = new EnergyCard("Colorless", "colorless");
    	EnergyCard e4 = new EnergyCard("Colorless", "colorless");
    	deck.addCard(clemont);
    	deck.addCard(e1);
    	deck.addCard(e2);
    	deck.addCard(e3);
    	deck.addCard(e4);
    	Player p1 = Mockito.spy(new Player(deck));
    	Player p2 = Mockito.spy(new Player());
    	Mockito.doNothing().when(p1).updateBoard();
    	Mockito.doNothing().when(p1).updateDiscardsOnBoard();
    	Mockito.doReturn(Arrays.asList(e1,e2,e3,e4)).when(p1).ChooseMultipleCards(Mockito.anyList(), Mockito.anyInt());
    	TargetServiceHandler.getInstance().setYouPlayer(p1);
    	TargetServiceHandler.getInstance().setThemPlayer(p2);
    	p1.drawCardsFromDeck(1);
    	assertEquals(clemont, p1.getHand().getCards().get(0));
    	p1.useCard(p1.getHand().getCards().get(0));
    	assertEquals(0, p1.getDeck().size());
    	assertEquals(4, p1.getHand().size());
    }
    
    @Test
    public void testDeenergizeEffect(){
        Player p1 = Mockito.spy(new Player());
        Player p2 = Mockito.spy(new Player());
        p1.setOpponent(p2);
        p2.setOpponent(p1);
        TargetServiceHandler.getInstance().setYouPlayer(p1);
        TargetServiceHandler.getInstance().setThemPlayer(p2);

        Mockito.doNothing().when(p1).setActiveOnBoard();
        Mockito.doNothing().when(p2).setActiveOnBoard();
        Mockito.doNothing().when(p1).updateActivePokemonOnBoard();
        Mockito.doNothing().when(p2).updateActivePokemonOnBoard();
        Mockito.doNothing().when(p1).benchPokemonOnBoard();
        Mockito.doNothing().when(p2).benchPokemonOnBoard();
        Mockito.doNothing().when(p1).updateBoard();
        Mockito.doNothing().when(p2).updateBoard();
        stub(method(GameController.class, "getHomePlayer")).toReturn(p1);
        Mockito.doReturn(EnergyTypes.LIGHTNING).when(p1).createEnergyOptionPane(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean());
        Mockito.doReturn(EnergyTypes.LIGHTNING).when(p2).createEnergyOptionPane(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyBoolean());

        Pokemon zubat = (Pokemon) ((CardsDatabase) CardsDatabase.getInstance()).query(37);
        zubat.addEnergy(new EnergyCard("Lightning", "lightning"));
        Pokemon helioptile = (Pokemon) ((CardsDatabase) CardsDatabase.getInstance()).query(19);
        // Give required energy for Destructive Beam
        helioptile.addEnergy(new EnergyCard("Lightning", "lightning"));
        helioptile.addEnergy(new EnergyCard("Lightning", "lightning"));
        p1.setActivePokemon(helioptile);
        p2.setActivePokemon(zubat);

        p1.useActivePokemon(1);

        //Expect zubat to loose energy
        assertTrue(zubat.getAttachedEnergy().size() == 0);
    }
}