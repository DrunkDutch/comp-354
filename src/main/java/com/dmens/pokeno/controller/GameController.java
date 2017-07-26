package com.dmens.pokeno.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import com.dmens.pokeno.services.TargetService;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import com.dmens.pokeno.card.CardTypes;
import com.dmens.pokeno.view.MultiCardViewerFrame;
import com.dmens.pokeno.view.StarterSelecter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.EnergyTypes;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.deck.Hand;
import com.dmens.pokeno.player.AIPlayer;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.FileUtils;
import com.dmens.pokeno.utils.DeckCreator;
import com.dmens.pokeno.view.GameBoard;

public class GameController {

    private static final Logger LOG = LogManager.getLogger(GameController.class);

	private static final String LOCATION_DECKS = "decks";
	private static final String LOCATION_CARDS = "cards.txt";
	private static final String LOCATION_ABILITIES = "abilities.txt";
	
	public static final int POISON_DAMAGE_AMOUNT = 1;
	
	private static ArrayList<Player> mPlayers = null;
	
	private static boolean mGameOver = false;
	private static boolean mIsHomePlayerPlaying = true;
	
    public static GameBoard board;
        
	public static void main(String[] args) {
		AbilitiesDatabase.getInstance().initialize(LOCATION_ABILITIES);
		CardsDatabase.getInstance().initialize(LOCATION_CARDS);
		Deck mFirstDeck = null;
		Deck mSecondDeck = null;
		
		setBoard(new GameBoard());
        
		Deck[] chosenDecks = chooseDeck();
		
		// Deck creation and validation
		LOG.trace("Creating decks and validating them...");
		mFirstDeck = chosenDecks[0];
		if(!mFirstDeck.checkValidity()) {
			LOG.info("First Deck is invalid");
			// TODO: how do we handle invalid deck?
		}
		mSecondDeck = chosenDecks[1];
		if(!mSecondDeck.checkValidity()) {
			LOG.info("Second Deck is invalid");
			// TODO: how do we handle invalid deck?
		}
		// Create Players and assign decks
		LOG.trace("Creating players and assigning decks...");
		Player homePlayer = new Player(mFirstDeck);
		Player adversaryPlayer = new AIPlayer(mSecondDeck);
		homePlayer.setOpponent(adversaryPlayer);
		adversaryPlayer.setOpponent(homePlayer);
//		homePlayer.getDeck().shuffle();
//		adversaryPlayer.getDeck().shuffle();
		
		mPlayers = new ArrayList<Player>();
		mPlayers.add(homePlayer);
		mPlayers.add(adversaryPlayer);
        do {
        	// Draw Cards and check for mulligans
        	mPlayers.stream()
        	.filter(player->!player.getIsReadyToStart())
        	.forEach(player-> {
        		board.updateHand(player.drawCardsFromDeck(6), player.isHumanPlayer());
        		player.checkIfPlayerReady();
        	});
            // Execute mulligans
            mPlayers.stream()
            .filter(player->player.isInMulliganState())
            .forEach(player->player.mulligan());
            
        // Repeat until no more mulligans
		} while(!homePlayer.getIsReadyToStart() || !adversaryPlayer.getIsReadyToStart());
        
        mPlayers.forEach(currentPlayer->{ currentPlayer.setUpRewards(); });
        
		setFirstTurnActivePokemon();

		TargetService service = TargetServiceHandler.getInstance().getService();

		service.setYouPlayer(mPlayers.get(1));
		service.setThemPlayer(mPlayers.get(0));

        AIPlayer opp = (AIPlayer)mPlayers.get(1);
        opp.selectStarterPokemon();
	}

	public static void  setFirstTurnActivePokemon() {
        if (!(GameController.getHomePlayer().getActivePokemon()  instanceof Pokemon)) {
            GameController.displayMessage("Please choose a starting pokemon");
            List<Card> startPokemon = GameController.getHomePlayer().getHand().getPokemon();
			ArrayList<Pokemon> starterPokemon = startPokemon.stream().filter(p -> p instanceof Pokemon).map(
                    p -> (Pokemon) p).
                    filter(
                            p -> !(p.isEvolvedCategory()))
                    .collect(Collectors.toCollection(ArrayList::new));
            StarterSelecter starterView = new StarterSelecter(starterPokemon, GameController.getHomePlayer());
            starterView.setSize(150, 250);
            starterView.setVisible(true);
			GameController.updateHand(getHomePlayer().getHand(),true);
            GameController.board.update();

        }

    }
	
	public static void retreatActivePokemon(boolean player){
    	if(player){
    		
    		Pokemon activePoke = mPlayers.get(0).getActivePokemon();
    		
    		LOG.debug(activePoke.getName() + " has been sent to Home's bench");
    		
    		if(!activePoke.isParalyzed() && !activePoke.isSleep()){
    			//1. Remove any special conditions affecting Pokemon. 
	    		mPlayers.get(0).resolveEffects(activePoke);
	    		
	    		//2. Remove appropriate number of Energy from Pokemon (Retreat Cost)
	    		
	    		if (mPlayers.get(0).getBenchedPokemon().size() <= 0)
	    		{
	    			displayMessage("At least 1 benched Pokemon required to retreat");
	    			return;
	    		}
	    		
	    		int activeRetreatCost = activePoke.getRetreatCost();
	    		if (activeRetreatCost <= mPlayers.get(0).getActivePokemon().getAttachedEnergy().size())
	    		{
	    			for (int i = 0; i < activeRetreatCost; i++)
	    			{
		    			EnergyTypes type = mPlayers.get(0).createEnergyOptionPane(mPlayers.get(0).getActivePokemon(), "Remove an Energy", "Which energy would you like to remove?", false);
		    			mPlayers.get(0).getActivePokemon().removeSingleEnergy(type);
	    			}
	    		}
	    		else
	    		{
	    			displayMessage("Not enough energy cards attached to satisfy retreat cost");
	    			return;
	    		}
	    		
	    		/*if(!activePoke.removeEnergy(activePoke.getAttachedEnergy(), activeRetreatCost))
	    		{
	    			displayMessage("Not enough energy cards attached to satisfy retreat cost");
	    			return;
	    		}*/
	    		
	    		//4. Remove Active Pokemon from ActivePokemonPanel.
	    		cleanActivePokemon(player);
	    		mPlayers.get(0).setActivePokemon(null);
	    		mPlayers.get(0).setActiveFromBench(mPlayers.get(0).createPokemonOptionPane("Select new Active", "Which Pokemon would you like to set as your new active?", false));
	    		//mPlayers.get(0).updateEnergyCounters(mPlayers.get(0).getActivePokemon(), false);
	    		//board.update();
	    		
	    		//3. Send Active Pokemon to bench
	    		board.addCardToBench(activePoke, player);
	    		getHomePlayer().benchPokemon(activePoke);
	    		
	    		//5. Reset text fields.
	    		//board.clearRetreatedPokemon(player);
    		}
    		else
    			displayMessage("Your Active Pokemon is asleep or paralyzed. Cannot retreat!");
    	}
    	
    	// TODO Retreat for AI. 
    	// else
    	//{
    	//}
    }
	
	public static void setPlayers(ArrayList<Player> players){
		mPlayers = players;
	}
	
	public static void setBoard(GameBoard newBoard){
		board = newBoard;
        board.setVisible(true);
	}
	
	public static boolean useCardForPlayer(Card card, int player){
		return mPlayers.get(player).useCard(card);
	}
	
	public static boolean useActivePokemonForPlayer(int player, int ability){
		return mPlayers.get(player).useActivePokemon(ability);
	}
	
	public static void setActivePokemonOnBoard(Pokemon pokemon, boolean player){
		board.setActivePokemon(pokemon, player);
	}
	
	public static boolean hasActivePokemonBlocked(int player) {
		return mPlayers.get(player).isActivePokemonBlocked();
	}
	
	public static void startAITurn(){
		mPlayers.get(1).startTurn();
	}
	
	public static int displayMessage(String msg){
		JOptionPane.showMessageDialog(null, msg);
		return 0;
	}
	
	public static Player getHomePlayer() {
		return mPlayers.get(0);
	}
	
	public static Player getAIPlayer() {
		return mPlayers.get(1);
	}
	
	public static boolean getIsHomePlayerPlaying() {
		return mIsHomePlayerPlaying;
	}
	
	public static void setIsHomePlayerPlaying(boolean isPlaying) {
		mIsHomePlayerPlaying = isPlaying;
	}
	
	public static Player getActivePlayer() {
		if(mIsHomePlayerPlaying) {
			return mPlayers.get(0);
		} else {
			return mPlayers.get(1);
		}
	}
	
	public static void checkGameStatus(){
		
	}
	
	public static int updateHand(Hand hand, boolean player){
		board.updateHand(hand, player);
		return 0;
	}
        
    public static void updateDeck(int deckSize, boolean player){
        board.updateDeckSize(deckSize, player);
    }
    
    public static void updateGraveyard(int graveyard, boolean player){
    	board.updateGraveyard(graveyard, player);
    }
    
    public static void updateRewards(int rewardsSize, boolean player){
        board.setRewardCount(rewardsSize, player);
    }
    
    public static void cleanActivePokemon(boolean player){
    	board.clearActivePokemon(player);
    }
    
    public static void updateEnergyCountersForCard(Card card, int player){
        mPlayers.get(player).updateEnergyCounters((Pokemon) card, true);
    }
    
    public static void updateEnergyCounters(Map<EnergyTypes, Integer> energies, boolean player){
        board.setEnergy(getAttachedEnergyList(energies), player);
    }
    
    public static void updateEnergyCountersPreview(Map<EnergyTypes, Integer> energies, boolean player){
        board.setEnergyPreview(getAttachedEnergyList(energies));
    }
    
    public static void updateBenchedPokemon(List<Pokemon> bench, boolean player){
    	board.updateBench(bench, player);
    }
    
    public static ArrayList<Integer> getAttachedEnergyList(Map<EnergyTypes, Integer> energies){
        ArrayList<Integer> energyList = new ArrayList<Integer>(5);
        energyList.add(0);energyList.add(0);energyList.add(0);energyList.add(0);energyList.add(0);
    	energies.forEach((energyType, amount) -> {
    		switch(energyType){
    		case FIGHT:
                    energyList.set(0, amount);
                    break;
    		case LIGHTNING:
                    energyList.set(1, amount);
                    break;
    		case PSYCHIC:
                    energyList.set(2, amount);
                    break;
    		case WATER:
                    energyList.set(3, amount);
                    break;
    		case COLORLESS:
                    energyList.set(4, amount);
                    break;
    		case FIRE:
                    break;
    		case GRASS:
                    break;
    		}
    	});
        return energyList;
    }
    
    private static Deck[] chooseDeck()
    {
    	JFrame frame = new JFrame();
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage("Choose you playing deck!");
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    	
    	List<Deck> deckList = new ArrayList<Deck>();
    	ArrayList<Component> deckButtons = new ArrayList<Component>();
		for (String fileEntry : FileUtils.getFilesFromFolder(LOCATION_DECKS, ".txt")) {
			deckButtons.add(getButton(optionPane, fileEntry, FileUtils.getFileAsImageIcon("images/deckIcon.png", 100, 125), deckList.size()));
			deckList.add(DeckCreator.Instance().DeckCreation(LOCATION_DECKS+"/"+fileEntry));
	    }
		
		optionPane.setOptions(deckButtons.toArray());
		JDialog dialog = optionPane.createDialog(frame, "Choose a deck.");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        
        
        int choice = (int) optionPane.getValue();
        Deck[] decks = new Deck[2];
        decks[0] = deckList.get(choice);
        deckList.remove(decks[0]);
        decks[1] = chooseRandomDeck(deckList);
        return decks;
    }
    
    private static Deck chooseRandomDeck(List<Deck> decks){
    	return decks.get((new Random()).nextInt(decks.size()));
    }
    
    private static JButton getButton(final JOptionPane optionPane, String text, Icon icon, int orderPosition) {
        final JButton button = new JButton(text, icon);
        ActionListener actionListener = new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            // Return position in deck list
            optionPane.setValue(orderPosition);
          }
        };
        button.addActionListener(actionListener);
        return button;
      }
    
    public static int deprecatedDispayCustomOptionPane(Object[] buttons, String title, String prompt)
    {
        return JOptionPane.showOptionDialog(null, prompt, title,
        0, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
    }
    
    public static int dispayCustomOptionPane(Object[] buttons, String title, String prompt)
    {
    	final JOptionPane jop = new JOptionPane(prompt, 0, JOptionPane.DEFAULT_OPTION, null, buttons, buttons[0]);
    	JDialog dialog = jop.createDialog(null, title);
    	dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	dialog.setVisible(true);
    	dialog.dispose();
    	
    	String selection = (String)jop.getValue();
    	for (int i = 0; i < buttons.length; i++)
    	{
    		if (((String)buttons[i]).equals(selection))
	    		return i;
    	}
    	
    	return -1;//Integer.parseInt((String)jop.getValue());
    }
    
    public static int displayConfirmDialog(String message, String title){
    	return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
    }
}
