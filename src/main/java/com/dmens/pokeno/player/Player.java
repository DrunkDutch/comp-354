package com.dmens.pokeno.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.card.TrainerCard;
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
    
    public Player getOpponent() {
    	return this.opponent;
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
        GameController.setActivePokemonOnBoard(activePokemon, humanPlayer);
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
    	GameController.updateBenchedPokemon(mBenchedPokemon, humanPlayer);
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
        
        if (opponent.getActivePokemon().getDamage() >= opponent.getActivePokemon().getHP()) //250)//
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
            LOG.trace("Popup PokemonSwap is opening");
            setActiveFromBench(createPokemonOptionPane("PokemonSwap", "Which Pokemon would you like to set as your new active?", false));
            LOG.trace("Benched Pokemon set to Active: " + mActivePokemon.getName());
            
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
    	ArrayList<EnergyCard> attachedEnergy = mActivePokemon.getAttachedEnergy();
    	for(int i = 0; i < attachedEnergy.size(); ++i) {
    		mDiscards.addCard(attachedEnergy.get(i));
    	}
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
        if (!this.isInMulliganState()) {
            int reply = GameController.displayConfirmDialog("Would you like to draw a card?", "Mulligan");
            if (reply == JOptionPane.YES_OPTION) {
                this.drawCardsFromDeck(1);
                GameController.displayMessage(((humanPlayer) ? "Human " : "AI ") + "Player received an extra card.");
            }
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
            buttons.add(p.getName() + " " + i);
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
    
    private Pokemon choosePokemonToEvolve(Pokemon evolution){
    	List<Pokemon> choiceList = new ArrayList<Pokemon>();
    	StringBuilder sb = new StringBuilder();
    	String message = "Which Pokemon would you like to attach it to?";
    	if(mActivePokemon != null && mActivePokemon.getName().equals(evolution.getBasePokemonName()))
    			choiceList.add(mActivePokemon);
    	if(mBenchedPokemon != null){
	    	choiceList.addAll(mBenchedPokemon.stream().filter(pokemon -> pokemon.getName().equals(evolution.getBasePokemonName())).collect(Collectors.toList()));
	    	choiceList.forEach(choice -> sb.append(choice.getName()+"-"));
    	}
    	String[] buttons = null;
    	if(choiceList.isEmpty()){
    		String[] button = {"Back"};
    		buttons = button;
    		message = "Base type " + evolution.getBasePokemonName() + " not in play.";
    	}else{
	    	sb.append("Cancel");
	    	buttons = sb.toString().split("-");
	    	if(choiceList.get(0).equals(mActivePokemon))
	    		buttons[0] = "Active " + buttons[0];
    	}
	    	int cardNum = choosePokemonInScreen(buttons, "Card Select", message);
	    	if(cardNum == buttons.length-1)
	    		return null;
	    	else
	    		return choiceList.get(cardNum);
    }
    
    public int choosePokemonInScreen(Object[] options, String title, String message){
    	return GameController.dispayCustomOptionPane(options, "Card Select", message);
    }
    
    public void swapPokemonFromBench(Pokemon before, Pokemon after){
    	int position = mBenchedPokemon.indexOf(before);
    	mBenchedPokemon.remove(before);
    	mBenchedPokemon.add(position, after);
    }
    
    public boolean useCard(Card card)
    {
        switch(card.getType()){
            case POKEMON:
            	Pokemon pokemon = (Pokemon) card;
            	if(pokemon.isEvolvedCategory()){
            		Pokemon toEvolve = choosePokemonToEvolve(pokemon);
            		if(toEvolve == null)
            			return false;
            		// If Active was chosen
            		if(toEvolve.equals(mActivePokemon)){
            			LOG.info("Active Pokemon is evolving!");
            			pokemon.evolvePokemon(mActivePokemon);
                		setActivePokemon(pokemon);
            		}
            		// If benched Pokemon was chosen
            		else{
            			LOG.info("Benched Pokemon is evolving!");
            			pokemon.evolvePokemon(toEvolve);
            			swapPokemonFromBench(toEvolve, pokemon);
            		}
            	}else if(mActivePokemon == null)
                    setActivePokemon(pokemon);
                else
                    benchPokemon(pokemon);
                break;
            case ENERGY:
                int cardNum = createPokemonOptionPane("Select a Pokemon", "Which Pokemon would you like to attach the energy to?", true);
                System.out.println("Selection: " + cardNum); //Removeme
                if (cardNum == 0)
                    setEnergy(card, mActivePokemon);
                else if (cardNum == -1)
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
        GameController.updateBenchedPokemon(mBenchedPokemon, isHumanPlayer());
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
