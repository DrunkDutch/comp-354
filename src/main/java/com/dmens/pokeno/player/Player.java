package com.dmens.pokeno.player;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.*;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.deck.Hand;

/**
 * Created by Devin on 2017-05-26.
 */
public class Player {

    private static final Logger LOG = LogManager.getLogger(Player.class);
	private static final int NUM_OF_REWARD_CARDS = 6;

    private Pokemon mActivePokemon = null;
    private ArrayList<Pokemon> mBenchedPokemon = null;
    private Hand mHand = null;
    private Deck mDeck = null;
    private CardContainer mRewards = null;
    private CardContainer mDiscards = null;
    
    private Player opponent;
    private boolean mIsReadyToStart = false;
    private boolean mIsInMulliganState = false;
    
    protected boolean humanPlayer;

    public Player() {
        humanPlayer = true;
    }
    
    public Player(Deck deckList) {
    	mDeck = deckList;
    	mBenchedPokemon = new ArrayList<Pokemon>();
    	mHand = new Hand();
    	mRewards = new CardContainer();
    	mDiscards = new CardContainer();
        humanPlayer = true;
    }

    public Pokemon getActivePokemon() {
        return mActivePokemon;
    }

    public ArrayList<Pokemon> getBenchedPokemon() {
        return mBenchedPokemon;
    }

    public Hand getHand() {
        return mHand;
    }

    public Deck getDeck() {
        return mDeck;
    }

    public CardContainer getRewards() {
        return mRewards;
    }

    public CardContainer getDiscards() {
        return mDiscards;
    }
    
    public void shuffleDeck(){
       this.mDeck.shuffle();
    }
    
    //NOTE: Size of mHand should be at most 7. 
    public Hand drawCardsFromDeck(int numOfCards) {
    	assert numOfCards >= 0;
    	assert mDeck.size() >= numOfCards;
    	
        if (numOfCards > mDeck.size())
            numOfCards = mDeck.size();
        mHand.addCards(mDeck.draw(numOfCards));
        GameController.updateHand(mHand, humanPlayer);
        GameController.updateDeck(mDeck.size(), humanPlayer);
        return mHand;
    }
    
    public void startTurn()
    {
        drawCardsFromDeck(1);
        if (this instanceof AIPlayer)
        {
            AIPlayer ai = (AIPlayer)this;
            GameController.setIsHomePlayerPlaying(false);
            ai.startPhase();
            GameController.setIsHomePlayerPlaying(true);
            opponent.startTurn();
        }
    }
    
    // allows player to pick a specific card from hand and put it back to the deck
    public void putHandBackToDeck(int index) {
    	assert (mHand != null && mHand.size() > 0);
    	
    	mDeck.addCard(mHand.pickCardFromPosition(index));
    }
    // puts all cards from hand back to the deck
    public void putHandBackToDeck() {
    	assert (mHand != null && mHand.size() > 0);
    	mDeck.addCards(mHand.dumpCards());
    }
    
    public void setUpRewards() {
    	mRewards.addCards(mDeck.draw(NUM_OF_REWARD_CARDS));
    	updateBoard();
        
    }
    
    public void updateBoard(){
    	GameController.updateRewards(mRewards.size(), humanPlayer);
        GameController.updateDeck(mDeck.size(), humanPlayer);
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
        GameController.board.setActivePokemon(activePokemon, humanPlayer);
        updateEnergyCounters(mActivePokemon, false);
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
        GameController.board.addCardToBench(benchPokemon, humanPlayer);
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
    	Card pickedCard = mHand.pickCardFromPosition(pickedCardPosition);
    	return pickedCard;
    }
    
    public boolean useActivePokemon(int ability)
    {
        if (mActivePokemon == null)
            return false;
        
        boolean usedAbility =  mActivePokemon.useAbility(ability, opponent.getActivePokemon());
        GameController.board.updateActivePokemon(opponent);
        
        if (opponent.getActivePokemon().getDamage() >= /*opponent.getActivePokemon().getHP()) */ 250)
        {
            checkGameWon();
            opponent.cleanActivePokemon();
            if (humanPlayer)
            {   
                AIPlayer ai = (AIPlayer)opponent;
                ai.activeFainted();
                GameController.board.OpponentBenchPanel.remove(GameController.board.OpponentBenchPanel.getComponent(0));
            }
            else
                opponent.activeFainted();
            collectPrize(mRewards.size()-1);
        }
        return usedAbility;
    }
    
    private void activeFainted()
    {
        //if you have pokemon on the bench, swapping method?
        if (humanPlayer)
        {
            System.out.println("Before popup");
            setActiveFromBench(createPokemonOptionPane("PokemonSwap", "Which Pokemon would you like to set as your new active?", false));
            
        }
    }
    
    private void setActiveFromBench(int pos)
    {
        if (pos == -1)
            return;
        ArrayList<Pokemon> mBench = getBenchedPokemon();
        setActivePokemon(mBench.get(pos));
        mBench.remove(mBench.get(pos));
        GameController.board.PlayerBenchPanel.remove(GameController.board.PlayerBenchPanel.getComponent(pos));
        updateEnergyCounters(mActivePokemon, false);
    }
    
    private void cleanActivePokemon(){
    	mDiscards.addCard(mActivePokemon);
    	mActivePokemon = null;
    	GameController.updateGraveyard(mDiscards.size(), humanPlayer);
    	GameController.cleanActivePokemon(humanPlayer);
    }
    
    private void checkGameWon(){
    	if(opponent.mBenchedPokemon.size() == 0 || mRewards.size() == 0){
            String message = (humanPlayer) ? "You Won! Game will now exit." : "You Lost! Game will now exit.";
            GameController.displayMessage(message);
            System.exit(0);
        }
    }
    
    private void declareMulligan(){
    	mIsInMulliganState = true;
    	GameController.displayMessage(((humanPlayer) ? "Human " : "AI ") + "Player has declared a Mulligan");
    }
    
    public boolean isInMulliganState(){
    	return mIsInMulliganState;
    }
    
    public void mulligan(){
    	if(mHand.hasBasicPokemon()){
    		mIsReadyToStart = true;
    		mIsInMulliganState = false;
    	} else{
    		mIsReadyToStart = false;
    		this.putHandBackToDeck();
    		// Disabled shuffling for now for easier debugging
        	//this.shuffleDeck();
        	opponent.notifyMulligan();
    	}
    }
    
    private void notifyMulligan(){
    	 int reply = JOptionPane.showConfirmDialog(null, "Would you like to draw a card?", "Mulligan", JOptionPane.YES_NO_OPTION);
         if (reply == JOptionPane.YES_OPTION){
          	this.drawCardsFromDeck(1);
          	GameController.displayMessage("Player received an extra card.");
         }
    }

    // for checking if player should declare a mulligan on their starting hand
    public void checkIfPlayerReady(){
    	if(mHand.hasBasicPokemon()){
    		mIsReadyToStart = true;
    	} else{
    		mIsReadyToStart = false;
    		declareMulligan();
    	}
    }
    
    public boolean hasBasicPokemon(){
    	return this.mHand.hasBasicPokemon();
    }
    
    public boolean getIsReadyToStart(){return mIsReadyToStart;}

    public void pickCard(){}

    //Should be able to use this method when the player decides which Pokemon they want when retreating/losing a Pokemon
    private int deprecatedCreatePokemonOptionPane(String title, String message)
    {
        int offset = 1;
        if (mActivePokemon == null)
            offset = 0;
        String[] buttons = new String[mBenchedPokemon.size()+1+offset];
        if (offset == 1)
            buttons[0] = "Active " + mActivePokemon.getName();
        buttons[buttons.length-1] = "Cancel";
        int i = offset;
        for (Pokemon p : mBenchedPokemon)
        {
            buttons[i] = mBenchedPokemon.get(i-offset).getName();
            i++;
        }
        int cardNum = GameController.dispayCustomOptionPane(buttons, title, message);
        if (cardNum == buttons.length-1) //If the user clicks cancel it will return -1
            cardNum = -1;
        return cardNum;
    }
    
    private int createPokemonOptionPane(String title, String message, boolean cancelable)
    {
        ArrayList<String> buttons = new ArrayList<String>(); 
        if (mActivePokemon != null)
            buttons.add("Active " + mActivePokemon.getName());
        int i = 0;
        for (Pokemon p : mBenchedPokemon)
        {
            buttons.add(mBenchedPokemon.get(i).getName());
            i++;
        }
        if (cancelable)
            buttons.add("Cancel");
        String[] buttonsAsArray = new String[buttons.size()];
        buttonsAsArray = buttons.toArray(buttonsAsArray);
        int buttonNum = GameController.dispayCustomOptionPane(buttonsAsArray, title, message);
        if (cancelable && buttonNum == buttonsAsArray.length-1) //If the user clicks cancel it will return -1
            buttonNum = -1;
        return buttonNum;
    }
    
    public boolean useCard(Card card)
    {
        switch(card.getType()){
            case POKEMON:
                if(mActivePokemon == null)
                    setActivePokemon((Pokemon)card);
                else
                    benchPokemon((Pokemon)card);
                break;
            case ENERGY: //Working here
                if (mActivePokemon == null)
                    return false;
                String[] buttons = new String[mBenchedPokemon.size()+2];
                buttons[0] = "Active " + mActivePokemon.getName();
                buttons[buttons.length-1] = "Cancel";
                int i = 1;
                for (Pokemon p : mBenchedPokemon)
                {
                    buttons[i] = mBenchedPokemon.get(i-1).getName();
                    i++;
                }
                int cardNum = GameController.dispayCustomOptionPane(buttons, "Card Select", "Which Pokemon would you like to attach it to?");
                if (cardNum == 0)
                    setEnergy(card, mActivePokemon);
                else if (cardNum == buttons.length-1)
                    return false;
                else
                    setEnergy(card, mBenchedPokemon.get(cardNum-1));
                
                break;
            case TRAINER:
                ((TrainerCard) card).use();
                break;
        }
        mHand.getCards().remove(card);
        GameController.updateHand(mHand, humanPlayer);
        return true;
    }
    
    public void setEnergy(Card energy, Pokemon pokemon){
        pokemon.addEnergy((EnergyCard) energy);
        if(pokemon.equals(mActivePokemon)){
            updateEnergyCounters(mActivePokemon, false);
        }
    }
    
    public void updateEnergyCounters(Pokemon pokemon, boolean preview){
        if(!preview)
            GameController.updateEnergyCounters(pokemon.getMapOfAttachedEnergies(), humanPlayer);
        else
            GameController.updateEnergyCountersPreview(pokemon.getMapOfAttachedEnergies(), humanPlayer);
    }

    /**
     * Allows player to retreat active Pokemon
     * and to swap it with a benched Pokemon. 
     * @param benchedPokemon
     */
    public void swapPokemon(Pokemon benchedPokemon){ //FIXME - misleading name, this is specficially for retreating, not for fainted Pokemon
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
    private void setActiveFromBenchRetreat(int pos)
    {
        if (pos == -1)
            return;
        ArrayList<Pokemon> mBench = getBenchedPokemon();
        setActivePokemon(mBench.get(pos));
        mBench.remove(mBench.get(pos));
        GameController.board.PlayerBenchPanel.remove(GameController.board.PlayerBenchPanel.getComponent(pos));
        updateEnergyCounters(mActivePokemon, false);
    }
    public boolean swapPokemonToBench(Pokemon benchedPokemon){
    	//int numEnergyCards = mActivePokemon.getAttachedEnergy().size();
    	//if(numEnergyCards >= mActivePokemon.getRetreatCost()){		
    	//if(!mActivePokemon.isSleep() || !mActivePokemon.isParalyzed()){
    	
    	mBenchedPokemon.add(mActivePokemon);
    	mActivePokemon = null;
		setActiveFromBenchRetreat(createPokemonOptionPane("PokemonSwap", "Which Pokemon would you like to set as your new active?", false));
		
    		//}
    	//}
		return true;
	}
    
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
    
    /**.
     * Allows player to select a prize card from deck. 
     * TODO: Function should perhaps signal end of match
     * if size of mRewards is 0 (i.e. player wins match). 
     * 
     * @param prizeCardPosition
     */
    public void collectPrize(int prizeCardPosition){
    	assert(mRewards !=null);
    	Card card = mRewards.pickCardFromPosition(prizeCardPosition);
    	updateBoard();
    	mHand.addCard(card);
        if (humanPlayer && mIsReadyToStart)
            GameController.board.addCardToHand(card, humanPlayer);
        GameController.board.setRewardCount(mRewards.size(), humanPlayer);
        if (mRewards.size() <= 0)
        {
            GameController.board.AnnouncementBox.setText("No more reward cards! The player wins!");
        }
    }
    
    public boolean isHumanPlayer(){
    	return humanPlayer;
    }

    //TODO
    public void lookatDeck(){}

}
