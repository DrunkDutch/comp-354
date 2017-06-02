package com.dmens.pokeno.Player;

import com.dmens.pokeno.Card.*;
import com.dmens.pokeno.Driver.Driver;
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
    
    private Player opponent;
    private boolean mIsReadyToStart = false;
    
    protected boolean humanPlayer;

    public Player() {
        humanPlayer = true;
    }
    
    public Player(ArrayList<Card> deckList) {
    	mDeck = deckList;
    	mBenchedPokemon = new ArrayList<Pokemon>();
    	mHand = new ArrayList<Card>();
    	mRewards = new ArrayList<Card>();
    	mDiscards = new ArrayList<Card>();
        humanPlayer = true;
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
    
    //NOTE: Size of mHand should be at most 7. 
    public void drawCardsFromDeck(int numOfCards) {
    	assert numOfCards >= 0;
    	assert mDeck.size() >= numOfCards;
    	
    	// note that we are drawing from the end of the deck list for better performance
    	for(int i = 0; i < numOfCards; ++i) {
    		Card card = mDeck.get(mDeck.size() - 1);
        	mDeck.remove(card);
        	mHand.add(card);
                
                if (humanPlayer && mIsReadyToStart)
                    Driver.board.addCardToHand(card, humanPlayer);
                
                if (card instanceof Pokemon && !humanPlayer)
                    System.out.println("DrewPokemon");
    	}
    }
    
    public void startTurn()
    {
        drawCardsFromDeck(1);
        if (this instanceof AIPlayer)
        {
            AIPlayer ai = (AIPlayer)this;
            ai.startPhase();
            opponent.startTurn();
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

    public void setOpponent(Player enemy)
    {
        opponent = enemy;
    }

   
    /**
     * Sets a selected Pokemon to be the
     * player's active Pokemon. 
     * 
     * @param activePokemon
     */
    public void setActivePokemon(Pokemon activePokemon){
    	mActivePokemon = activePokemon;
        //if (humanPlayer)
        Driver.board.setActivePokemon(activePokemon, humanPlayer);
    }
   

    /**
     * Sends a Pokemon to player's bench. Condition
     * verifies if player's bench has already reached
     * max capacity or not. 
     * 
     * @param benchPokemon
     */
    public void benchPokemon(Pokemon benchPokemon){
    	assert(mBenchedPokemon.size() < 5);
    	mBenchedPokemon.add(benchPokemon);
        //if (humanPlayer)
        Driver.board.addCardToBench(benchPokemon, humanPlayer);
    }
    
    /**
     * Allows player to pick a card from his/her 
     * hand. (Will allow us to display the card,
     * thus allowing the player to decide whether 
     * to do anything with it). 
     * 
     * @param pickedCardPosition
     * @return Card that the player has chosen. 
     */
    public Card pickCard(int pickedCardPosition){
    	assert(mHand !=null && mHand.size() > 0);
    	Card pickedCard = mHand.get(pickedCardPosition);
    	return pickedCard;
    }
    
    public void useActivePokemon(int ability)
    {
        mActivePokemon.useAbility(ability, opponent.getActivePokemon());
        Driver.board.updateActivePokemon(opponent);
        
        if (opponent.getActivePokemon().getDamage() >= opponent.getActivePokemon().getHP())
        {
            opponent.setActivePokemon(null);
            if (humanPlayer)
            {   
                AIPlayer ai = (AIPlayer)opponent;
                ai.activeFainted();
                Driver.board.OpponentBenchPanel.remove(Driver.board.OpponentBenchPanel.getComponent(0));
            }
            collectPrize(mRewards.size()-1);
        }
    }
    
    public void Mulligan(){}

    // for checking if player should declare a mulligan on their starting hand
    public boolean hasBasicPokemon(){
        for(Card card : mHand)
        {
            // if is a Pokemon card and is the base Pokemon of the evolution line
            if(card.getClass() == Pokemon.class && ((Pokemon)card).getBasePokemonName().equals(card.getName()))
            {
                mIsReadyToStart = true;
                for(int i = mHand.size() - 1; i >= 0; --i)
                {
                    Card cardToShow = mHand.get(i);
                    Driver.board.addCardToHand(cardToShow, humanPlayer);
                }
                return true;
            }
        }
        
        return false;
    }
    
    public boolean getIsReadyToStart(){return mIsReadyToStart;}

    public void pickCard(){}

    public void useCard(Card card)
    {
        if (card instanceof Pokemon)
        {
            if(mActivePokemon == null)
            {
                setActivePokemon((Pokemon)card);
            }
            else
                benchPokemon((Pokemon)card);
        }
        if (card instanceof EnergyCard)
        {
            mActivePokemon.addEnergy((EnergyCard) card);
        }
        mHand.remove(card);
    }

    /**
     * Allows player to retreat active Pokemon
     * and to swap it with a benched Pokemon. 
     * @param benchedPokemon
     */
    public void swapPokemon(Pokemon benchedPokemon){
    	int numEnergyCards = mActivePokemon.getAttachedEnergy().size();
    	if(numEnergyCards >= mActivePokemon.getRetreatCost()){
    		
    		// Pokemon cannot retreat if affected by sleep or paralysis. 
    		if(!mActivePokemon.isSleep() || !mActivePokemon.isParalyzed()){
    			mBenchedPokemon.add(mActivePokemon);
    			this.setActivePokemon(benchedPokemon);
    		}
    	}
    	
    	//TODO: Handle event where number of energy cards isn't sufficient. 
    	// e.g. Pop-up on GUI. 
    		
    }
    
    //TODO
    public void evolvePokemon(Pokemon basePokemon, Pokemon evolvedPokemon){
    	
    }
    
    /**
     * Allows player to attach an Energy card onto
     * a Pokemon. (Can only be done once per turn, per 
     * Pokemon). 
     * 
     * @param energy
     * @param pokemon
     */
    public void attachEnergy(EnergyCard energy, Pokemon pokemon){
    	pokemon.addEnergy(energy);
    }
    
    /**
     * Allows player to select a prize card from deck. 
     * TODO: Function should perhaps signal end of match
     * if size of mRewards is 0 (i.e. player wins match). 
     * 
     * @param prizeCardPosition
     */
    public void collectPrize(int prizeCardPosition){
    	assert(mRewards !=null);
    	Card prizeCard = mRewards.get(prizeCardPosition);
    	mHand.add(prizeCard);
    	mRewards.remove(prizeCard);
        if (humanPlayer && mIsReadyToStart)
            Driver.board.addCardToHand(prizeCard, humanPlayer);
        Driver.board.setRewardCount(mRewards.size(), humanPlayer);
        if (mRewards.size() <= 0)
        {
            Driver.board.AnnouncementBox.setText("No more reward cards! The player wins!");
        }
    }

    //TODO
    public void lookatDeck(){}

}
