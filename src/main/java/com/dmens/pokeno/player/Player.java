package com.dmens.pokeno.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyCard;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.card.TrainerCard;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.deck.Hand;
import com.dmens.pokeno.services.TargetService;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import com.dmens.pokeno.utils.Randomizer;
import com.dmens.pokeno.view.MultiCardSelector;

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
    private boolean mHasPlayedEnergy = false;
    
    protected boolean humanPlayer;

    public Player() {
        humanPlayer = true;
        mBenchedPokemon = new ArrayList<Pokemon>();
    	mHand = new Hand();
    	mRewards = new CardContainer();
    	mDiscards = new CardContainer();
    }
    
    public Player(Deck deckList) {
    	mDeck = deckList;
    	mBenchedPokemon = new ArrayList<Pokemon>();
    	mHand = new Hand();
    	mRewards = new CardContainer();
    	mDiscards = new CardContainer();
        humanPlayer = true;
    }
    
    //A rarely used method for if the player's deck ever needs to be set outside of construction
    public void setDeck(Deck deckList)
    {
    	mDeck = deckList;
    }
    
    public Pokemon getActivePokemon() {
        return mActivePokemon;
    }
    
    public boolean isActivePokemonBlocked() {
    	if(null != mActivePokemon)
		{
    		return mActivePokemon.isSleep() || mActivePokemon.isParalyzed();
		}
    	else
    	{
    		return true;	// null blocked...
    	}
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
    	//assert numOfCards >= 0;
    	//assert mDeck.size() >= numOfCards;
    	
        if (numOfCards > mDeck.size())
            numOfCards = mDeck.size();
        mHand.addCards(getDeck().draw(numOfCards));
        updateBoard();
        return mHand;
    }
    
    public void addCardsToHand(List<Card> cards){
    	mHand.addCards(cards);
    	updateBoard();
    }
    
    public void discardCard(Card card){
    	this.mDiscards.addCard(card);
    	updateDiscardsOnBoard();
    }
    
    public void updateDiscardsOnBoard(){
    	GameController.updateGraveyard(this.mDiscards.size(), isHumanPlayer());
    }
    
    public void startTurn()
    {
    	// Reset has picked energy flag every turn
    	mHasPlayedEnergy = false;
    	GameController.setIsHomePlayerPlaying(this.isHumanPlayer());
        TargetService service = TargetServiceHandler.getInstance().getService();
        service.setYouPlayer(this);
        service.setThemPlayer(opponent);
        if (getDeck().size() == 0)
        	loseGame();
        drawCardsFromDeck(1);
        
        if (this instanceof AIPlayer)
        {
            AIPlayer ai = (AIPlayer)this;
            ai.startPhase();
            opponent.startTurn();
        }
    }
    
    public void resolveEffects(Pokemon poke)
    {
    	LOG.debug((GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + "turn has ended - resolving effects.");
    	if(null != poke)
    	{
    		String msgPrefix = !this.isHumanPlayer() ? "AI's " : "Your ";
			//1 paralyzed .. clear it on end of turn
			if(poke.isParalyzed())
			{
				poke.setParalyzed(false);
			}
			//2 asleep .. 50% chance of waking up
			if(poke.isSleep())
			{
				if(Randomizer.Instance().getFiftyPercentChance())
				{
					poke.setSleep(false);
					LOG.debug((GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + poke.getName() + " has been woken up.");
					displayMessage(msgPrefix + poke.getName() + " has woken up!");
					GameController.board.clearStatus(1, GameController.getIsHomePlayerPlaying());		
				}
			}
			//3 stuck .. clear it on end of turn
			if(poke.isStuck())
			{
				poke.setStuck(false);
				LOG.debug((GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + poke.getName() + " is no longer stuck.");
				displayMessage(msgPrefix + poke.getName() + " no longer stuck!");
				GameController.board.clearStatus(2, GameController.getIsHomePlayerPlaying());	
			}
			//4 poisoned .. hurt 'em
			if(poke.isPoisoned()) 
			{
				poke.addDamage(GameController.POISON_DAMAGE_AMOUNT);
				LOG.debug((GameController.getIsHomePlayerPlaying() ? "Home's " : "AI's ") + poke.getName() + " had been damaged " + GameController.POISON_DAMAGE_AMOUNT + "by Poison." );
				displayMessage(msgPrefix + poke.getName() + " damaged " + GameController.POISON_DAMAGE_AMOUNT + "by poison." );
			}
			//5 healed .. tracks if a pokemon was healed during the turn, set the flag to false once turn has ended.
			if(poke.isHealed())
			{
				LOG.info("Healed pokemon's Healed flag was reset.");
				poke.setHealed(false);
			}
    	}
    }
    
    /*
     * Rules: "Status effects are always removed when retreating or evolving" 
     */
    public void clearEffects(Pokemon poke)
    {
    	if(null != poke)
    	{
    		poke.setParalyzed(false);
        	poke.setPoisoned(false);
        	poke.setSleep(false);
        	poke.setStuck(false);
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
    	GameController.updateHand(mHand, isHumanPlayer());
    	benchPokemonOnBoard();
    	GameController.updateRewards(mRewards.size(), humanPlayer);
    	updateDiscardsOnBoard();
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
    	if (mActivePokemon != null)
    		setActiveOnBoard();
    }
    
    /**
     * Call Controller to set active on board
     */
    public void setActiveOnBoard(){
    	GameController.setActivePokemonOnBoard(mActivePokemon, humanPlayer);
    	updateEnergyCounters(mActivePokemon, false);
    	
    	// update active pokemon status
    	if(mActivePokemon.isParalyzed()) {
    		GameController.board.addStatus(0, GameController.getActivePlayer() == this);
    	} 
    	if(mActivePokemon.isSleep()) {
    		GameController.board.addStatus(1, GameController.getActivePlayer() == this);
    	} 
    	if(mActivePokemon.isStuck()) {
    		GameController.board.addStatus(2, GameController.getActivePlayer() == this);
    	} 
    	if(mActivePokemon.isPoisoned()) {
    		GameController.board.addStatus(3, GameController.getActivePlayer() == this);
    	}  
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
    	benchPokemonOnBoard();
    }
    
    /**
     * Call Controller to bench pokemon on board
     */
    public void benchPokemonOnBoard(){
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
    
    public void updateActivePokemonOnBoard(){
    	GameController.board.updateActivePokemon(this);
    	updateEnergyCounters(mActivePokemon,false);
        GameController.board.updateActivePokemon(opponent);
        opponent.updateEnergyCounters(opponent.getActivePokemon(),false);
    }
    
    public boolean useActivePokemon(int ability)
    {
        if (mActivePokemon == null)
            return false;
        
        boolean usedAbility =  mActivePokemon.useAbility(ability, opponent.getActivePokemon());
        if (usedAbility == false)
        	return false;
        updateActivePokemonOnBoard();
        checkBenchedPokemonFainted();
        opponent.checkBenchedPokemonFainted();
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
    
    private void checkBenchedPokemonFainted(){
    	List<Pokemon> fainted = mBenchedPokemon.stream().filter(pokemon->pokemon.isFainted()).collect(Collectors.toList());
    	fainted.forEach(pokemon-> mBenchedPokemon.remove(pokemon));
    	updateBoard();
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
    
    public void setActiveFromBench(int pos)
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
            String message = (humanPlayer) ? "You Won! You may exit the game." : "You Lost! You may exit the game.";
            displayMessage(message);
            GameController.endGame();
        }
    }
    
    public void loseGame()
    {
    	String message = (humanPlayer) ? "You Lost! You may exit the game." : "You Won! You may exit the game.";
        displayMessage(message);
        GameController.endGame();
    }
    
    private void declareMulligan(){
    	mIsInMulliganState = true;
    	displayMessage(((humanPlayer) ? "Human " : "AI ") + "Player has declared a Mulligan");
    }
    
    public void displayMessage(String message){
    	GameController.displayMessage(message);
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
    
    public void notifyMulligan(){
        if (!this.isInMulliganState()) {
            int reply = makeChoice("Would you like to draw a card?");
            if (reply == JOptionPane.YES_OPTION) {
                this.drawCardsFromDeck(1);
                displayMessage(((humanPlayer) ? "Human " : "AI ") + "Player received an extra card.");
            }
        }
    }
    
    public int makeChoice(String message){
    	return GameController.displayConfirmDialog(message, "Choice");
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
        int cardNum = chooseCards(buttons, title, message);
        if (cardNum == buttons.length-1) //If the user clicks cancel it will return -1
            cardNum = -1;
        return cardNum;
    }
    
    public int createPokemonOptionPane(String title, String message, boolean cancelable)
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
        int buttonNum = chooseCards(buttonsAsArray, title, message);
        if (cancelable && buttonNum == buttonsAsArray.length-1) //If the user clicks cancel it will return -1
            buttonNum = -1;
        return buttonNum;
    }
    
    public EnergyTypes createEnergyOptionPane(Pokemon target, String title, String message, boolean cancelable)
    {
        ArrayList<String> buttons = new ArrayList<String>(); 
        ArrayList<Integer> counts = GameController.getAttachedEnergyList(target.getMapOfAttachedEnergies());
        boolean [] energyTypes = new boolean[5];
        if (counts.get(0) != 0)
        {
        	buttons.add("FIGHT");
        	energyTypes[0] = true;
        }
        if (counts.get(1) != 0)
        {
        	buttons.add("LIGHTNING");
        	energyTypes[1] = true;
        }
        if (counts.get(2) != 0)
        {
        	buttons.add("PSYCHIC");
        	energyTypes[2] = true;
        }
        if (counts.get(3) != 0)
        {
        	buttons.add("WATER");
        	energyTypes[3] = true;
        }
        if (counts.get(4) != 0)
        {
        	buttons.add("COLOURLESS");
        	energyTypes[4] = true;
        }
        
        System.out.println("Types: ");
        for (boolean c : energyTypes)
        {
        	System.out.println(c);
        }
        
        if (cancelable)
            buttons.add("Cancel");
        String[] buttonsAsArray = new String[buttons.size()];
        buttonsAsArray = buttons.toArray(buttonsAsArray);
        int buttonNum = GameController.dispayCustomOptionPane(buttonsAsArray, title, message);
        if (cancelable && buttonNum == buttonsAsArray.length-1) //If the user clicks cancel it will return -1
            buttonNum = -1;
        
        //If you select button 0 it isn't necessarily FIGHT, it's the first used
        int typeCounter = 0;
        for (int i = 0; i < energyTypes.length; i++)
        {
        	if (energyTypes[i])
        	{
        		if (typeCounter == buttonNum)
        		{
        			EnergyTypes t = EnergyTypes.GRASS;
        			switch(i)
        			{
        				case 0: t = EnergyTypes.FIGHT; break;
        				case 1: t = EnergyTypes.LIGHTNING; break;
        				case 2: t = EnergyTypes.PSYCHIC; break;
        				case 3: t = EnergyTypes.WATER; break;
        				case 4: t = EnergyTypes.COLORLESS; break;
        			}
        			return t;
        		}
    			typeCounter++;
        	}
        }
        return EnergyTypes.FIRE; //FIXME - this is a temp fix, should be a cancel of sorts
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
	    	int cardNum = chooseCards(buttons, "Card Select", message);
	    	if(cardNum == buttons.length-1)
	    		return null;
	    	else
	    		return choiceList.get(cardNum);
    }
    
    public int chooseCards(Object[] options, String title, String message){
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
                else if(mBenchedPokemon.size() < 5)
                    benchPokemon(pokemon);
                else{
                    displayMessage("Bench is full");
                    return false;
                }
                break;
            case ENERGY:
            	if(!mHasPlayedEnergy){
	                int cardNum = createPokemonOptionPane("Select a Pokemon", "Which Pokemon would you like to attach the energy to?", true);
	                System.out.println("Selection: " + cardNum); //Removeme
	                if (cardNum == 0)
	                    setEnergy(card, mActivePokemon);
	                else if (cardNum == -1)
	                    return false;
	                else
	                    setEnergy(card, mBenchedPokemon.get(cardNum-1));
            	}else{
            		return false;
            	}
                break;
            case TRAINER:
                ((TrainerCard) card).use();
                break;
        }
        mHand.getCards().remove(card);
        updateBoard();
        return true;
    }
    
    public void setEnergy(Card energy, Pokemon pokemon){
        pokemon.addEnergy((EnergyCard) energy);
        if(pokemon.equals(mActivePokemon)){
            updateEnergyCounters(mActivePokemon, false);
        }
        mHasPlayedEnergy = true;
    }
    
    public boolean hasPlayedEnergyInTurn(){
    	return mHasPlayedEnergy;
    }
    
    public void updateEnergyCounters(Pokemon pokemon, boolean preview){
        if(!preview)
            GameController.updateEnergyCounters(pokemon.getMapOfAttachedEnergies(), humanPlayer);
        else
            GameController.updateEnergyCountersPreview(pokemon.getMapOfAttachedEnergies(), humanPlayer);
    }
    
    public void updatePokemonStatusOnBoard(){
			if (mActivePokemon.isParalyzed())
				GameController.board.addStatus(0, !GameController.getIsHomePlayerPlaying());
			if (mActivePokemon.isSleep())
				GameController.board.addStatus(1, !GameController.getIsHomePlayerPlaying());
			if (mActivePokemon.isStuck())
				GameController.board.addStatus(2, !GameController.getIsHomePlayerPlaying());
			if (mActivePokemon.isPoisoned())
				GameController.board.addStatus(3, !GameController.getIsHomePlayerPlaying());
    }
    
    public void endTurn(){
    	mHasPlayedEnergy = false;
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
            //GameController.board.AnnouncementBox.setText("No more reward cards! The player wins!");
        	checkGameWon();
        }
    }
    
    public boolean isHumanPlayer(){
    	return humanPlayer;
    }

    public Card chooseFromAll(){
        StringBuilder sb = new StringBuilder();
        sb.append(mActivePokemon.getName() + ";");
        for(int i = 1; i <= mBenchedPokemon.size(); i++){
            sb.append(" "+ mBenchedPokemon.get(i-1) + ";");
        }
        int choice = chooseCards(sb.toString().split(";"), "Choose Card", "Choose Pokemon.");
        if(choice == 0)
            return mActivePokemon;
        return mBenchedPokemon.get(choice-1);
    }

    public Card chooseFromBench(){
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= mBenchedPokemon.size(); i++){
            sb.append(" "+ mBenchedPokemon.get(i-1)+ ";");
        }
        int choice = chooseCards(sb.toString().split(";"), "Choose Card", "Choose Pokemon.");
        return mBenchedPokemon.get(choice);
    }
    
    public Card chooseFromOpponentBench(){
    	if(opponent.getBenchedPokemon().isEmpty())
    		return null;
    	StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= opponent.getBenchedPokemon().size(); i++){
            sb.append(i+" "+ opponent.getBenchedPokemon().get(i-1).getName()+ ";");
        }
        int choice = chooseCards(sb.toString().split(";"), "Choose Card", "Choose Pokemon.");
        return opponent.getBenchedPokemon().get(choice);
    }
    
    public Card chooseFromOpponentAll(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(opponent.getActivePokemon().getName() + ";");
        for(int i = 1; i <= opponent.getBenchedPokemon().size(); i++){
            sb.append(i+" "+ opponent.getBenchedPokemon().get(i-1).getName()+ ";");
        }
        int choice = chooseCards(sb.toString().split(";"), "Choose Card", "Choose Pokemon.");
        if(choice == 0)
            return opponent.getActivePokemon();
        return opponent.getBenchedPokemon().get(choice-1);
    }
    
    public List<Card> ChooseMultipleCards(List<Card> cards, int amount){
    	MultiCardSelector selector = new MultiCardSelector(cards, this, amount);
    	return selector.getSelectedCards();
    }
    
    public boolean flipCoin(){
    	boolean heads = Randomizer.Instance().getFiftyPercentChance();
    	displayMessage("Coin: " + (heads ? "Heads" : "Tails"));
    	return heads;
    }

    //TODO
    public void lookatDeck(){}

}
