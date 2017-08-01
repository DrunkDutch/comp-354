package com.dmens.pokeno.effectTest;

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

public class DeenergizeTest {

    @Before
    public void setup(){
        AbilitiesDatabase.getInstance().initialize("abilities.txt");
        CardsDatabase.getInstance().initialize("cards.txt");
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
