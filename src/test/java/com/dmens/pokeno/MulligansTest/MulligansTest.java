/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dmens.pokeno.MulligansTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dmens.pokeno.Board.GameBoard;
import com.dmens.pokeno.Card.EnergyCard;
import com.dmens.pokeno.Card.Pokemon;
import com.dmens.pokeno.Deck.Deck;
import com.dmens.pokeno.Driver.Driver;
import com.dmens.pokeno.Player.Player;

/**
 *
 * @author Conor
 */
public class MulligansTest {

    //@Ignore
    @Test
    public void TestBasicPokemonValidation()
    {
        Driver.board = new GameBoard();
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        
        Pokemon basic = new Pokemon("Basic",null,1,1);
        assertNotNull(basic);
        basic.setBasePokemonName("Basic");
        
        Pokemon evolved = new Pokemon("BasicKing",null,2,1);
        evolved.setBasePokemonName("Basic");
        assertNotNull(evolved);
        
        EnergyCard notPokemon = new EnergyCard("what","ever");
        assertNotNull(notPokemon);
        
        deck1.addCard(evolved);
        deck1.addCard(notPokemon);
        deck1.addCard(basic);
        
        deck2.addCard(evolved);
        deck2.addCard(notPokemon);
        deck2.addCard(notPokemon);
        
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
