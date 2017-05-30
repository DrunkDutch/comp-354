package com.dmens.pokeno.Player;

import com.dmens.pokeno.Card.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Devin on 2017-05-26.
 */
public class Player {

    private static final Logger LOG = LogManager.getLogger(Player.class);
	private static final int NUM_OF_REWARD_CARDS = 6;

    private Pokemon mActivePokemon = null;
    private ArrayList<Pokemon> mBenchedPokemon = null;
    private ArrayList<Card> mHand = null;
    private ArrayList<Card> mDeck = null;
    private ArrayList<Card> mRewards = null;
    private ArrayList<Card> mDiscards = null;

    public Player() {
    }
    
    public Player(ArrayList<Card> deckList) {
    	mDeck = deckList;
    	mBenchedPokemon = new ArrayList<Pokemon>();
    	mHand = new ArrayList<Card>();
    	mRewards = new ArrayList<Card>();
    	mDiscards = new ArrayList<Card>();
    }

    public Pokemon getActivePokemon() {
        return mActivePokemon;
    }

    public ArrayList<Pokemon> getBenchedPokemon() {
        return mBenchedPokemon;
    }

    public ArrayList<Card> getHand() {
        return mHand;
    }

    public ArrayList<Card> getDeck() {
        return mDeck;
    }

    public ArrayList<Card> getRewards() {
        return mRewards;
    }

    public ArrayList<Card> getDiscards() {
        return mDiscards;
    }
    
    public void shuffleDeck(){
        Collections.shuffle(this.mDeck);
    }

    public void drawCardsFromDeck(int numOfCards) {
    	assert numOfCards >= 0;
    	assert mDeck.size() >= numOfCards;
    	
    	// note that we are drawing from the end of the deck list for better performance
    	for(int i = 0; i < numOfCards; ++i) {
    		Card card = mDeck.get(mDeck.size() - 1);
        	mDeck.remove(card);
        	mHand.add(card);
    	}
    }
    
    // allows player to pick a specific card from hand and put it back to the deck
    public void putHandBackToDeck(int index) {
    	assert (mHand != null && mHand.size() > 0);
    	
    	Card card = mHand.get(index);
    	mHand.remove(card);
    	mDeck.add(card);
    }
    // puts all cards from hand back to the deck
    public void putHandBackToDeck() {
    	assert (mHand != null && mHand.size() > 0);
    	for(int i = mHand.size() - 1; i >= 0; --i) {
        	Card card = mHand.get(i);
        	mHand.remove(card);
        	mDeck.add(card);
    	}
    }
    
    public void setUpRewards() {
    	mRewards = new ArrayList<Card>();
    	for(int i = 0; i < NUM_OF_REWARD_CARDS; ++i) {
    		Card card = mDeck.get(mDeck.size() - 1);
        	mDeck.remove(card);
        	mRewards.add(card);
    	}
    }
    
    public void Mulligan(){}

    public void setActivePokemon(Pokemon activePokemon){}

    public void benchPokemon(Pokemon benchPokemon){}

    public void pickCard(){}

    public void pickCard(int pickedCardPosition){}

    public void useCard(Card card){}

    public void swapPokemon(Pokemon benchedPokemon){}

    public void swapPokemon(Pokemon benchedPokemon, int retreatCost){}

    public void evolvePokemon(Pokemon basePokemon, Pokemon evolvedPokemon){}

    public void attachEnergy(EnergyCard energy, Pokemon pokemon){}

    public void collectPrize(int prizeCardPosition){}

    public void lookatDeck(){}

}
