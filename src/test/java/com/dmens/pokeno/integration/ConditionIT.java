package com.dmens.pokeno.integration;

import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;

public class ConditionIT {

    @Before
    public void setup(){
        AbilitiesDatabase.getInstance().initialize("abilities.txt");
        CardsDatabase.getInstance().initialize("cards.txt");
    }

    @Ignore
    @Test
    public void testHealedCondition(){
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

        Pokemon seaking = (Pokemon) ((CardsDatabase) CardsDatabase.getInstance()).query(6);
        Pokemon suicune = (Pokemon) ((CardsDatabase) CardsDatabase.getInstance()).query(11);
        
        // Sanity checks
        Assert.assertEquals("Seaking", seaking.getName());
        Assert.assertEquals("Suicune", suicune.getName());
        
        // Add energy for move
        seaking.addEnergy(new EnergyCard("Water", "water"));
        
        // Damage Seaking
        seaking.addDamage(40);
        
        // "Heal" Seaking
        Assert.assertNotNull(seaking);
        System.out.println(seaking.getName());
        seaking.removeDamage(10);
        Assert.assertEquals(true, seaking.isHealed());
        
        p1.setActivePokemon(seaking);
        p2.setActivePokemon(suicune);
        
        boolean usedMove = p1.useActivePokemon(0);
        Assert.assertEquals(true, usedMove);

        // Expect suicune to be damaged 90
        Assert.assertEquals(90, p2.getActivePokemon().getDamage());
    }
}
