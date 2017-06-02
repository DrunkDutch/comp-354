/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dmens.pokeno.MulligansTest;

import com.dmens.pokeno.Card.Card;
import com.dmens.pokeno.Card.EnergyCard;
import com.dmens.pokeno.Card.Pokemon;
import com.dmens.pokeno.Driver.Driver;
import com.dmens.pokeno.Player.Player;
import com.dmens.pokeno.Utils.Parser;
import java.util.ArrayList;
import java.util.function.Predicate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Conor
 */
public class MulligansTest {

    @Ignore
    @Test
    public void TestBasicPokemonValidation()
    {
        ArrayList<Card> deck1 = new ArrayList<Card>();
        ArrayList<Card> deck2 = new ArrayList<Card>();
        
        Pokemon basic = new Pokemon("Basic",null,1,1);
        assertNotNull(basic);
        basic.setBasePokemonName("Basic");
        
        Pokemon evolved = new Pokemon("BasicKing",null,2,1);
        evolved.setBasePokemonName("Basic");
        assertNotNull(evolved);
        
        EnergyCard notPokemon = new EnergyCard("what","ever");
        assertNotNull(notPokemon);
        
        deck1.add(evolved);
        deck1.add(notPokemon);
        deck1.add(basic);
        
        deck2.add(evolved);
        deck2.add(notPokemon);
        deck2.add(notPokemon);
        
        Player player1 = new Player(deck1);
        assertNotNull(player1);
        
        player1.drawCardsFromDeck(3);
        
        assertTrue(player1.hasBasicPokemon());
        
        Player player2 = new Player(deck2);
        assertNotNull(player2);
        
        player2.drawCardsFromDeck(3);
        
        assertFalse(player2.hasBasicPokemon());
    }
}
