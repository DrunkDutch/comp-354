package com.dmens.pokeno.Driver;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.Card.Card;
import com.dmens.pokeno.Card.EnergyTypes;
import com.dmens.pokeno.Card.Pokemon;
import com.dmens.pokeno.Deck.Deck;
import com.dmens.pokeno.Deck.Hand;
import com.dmens.pokeno.Player.AIPlayer;
import com.dmens.pokeno.Player.Player;
import com.dmens.pokeno.View.GameBoard;
import com.dmens.pokeno.utils.FileUtils;
import com.dmens.pokeno.utils.Parser;

public class GameController {

    private static final Logger LOG = LogManager.getLogger(GameController.class);

	private static final String LOCATION_DECKS = "decks";
	private static final String LOCATION_CARDS = "cards.txt";
	private static final String LOCATION_ABILITIES = "abilities.txt";
	
	private static ArrayList<Player> mPlayers = null;
	
	private static boolean mGameOver = false;
	private static boolean mIsHomePlayerPlaying = false;
	
    public static GameBoard board;
        
	public static void main(String[] args) {
		
		Deck mFirstDeck = null;
		Deck mSecondDeck = null;
		// Parse Cards
		System.out.println("Parsing cards and abilities...");
		boolean result = Parser.Instance().LoadCards(LOCATION_CARDS);
		assert result;
		result = Parser.Instance().LoadAbilities(LOCATION_ABILITIES);
		assert result;
		
		board = new GameBoard();
        board.setVisible(true);
        
		Deck[] chosenDecks = chooseDeck();
		
		// Deck creation and validation
		System.out.println("Creating decks and validating them...");
		mFirstDeck = chosenDecks[0];
		if(!mFirstDeck.checkValidity()) {
			System.out.println("First Deck is invalid");
			// TODO: how do we handle invalid deck?
		}
		mSecondDeck = chosenDecks[1];
		if(!mSecondDeck.checkValidity()) {
			System.out.println("Second Deck is invalid");
			// TODO: how do we handle invalid deck?
		}
		// Create Players and assign decks
		// TODO: we need to allow player to choose his deck or randomly assign decks
		System.out.println("Creating players and assigning decks...");
		Player homePlayer = new Player(mFirstDeck);
		Player adversaryPlayer = new AIPlayer(mSecondDeck);
		homePlayer.setOpponent(adversaryPlayer);
		adversaryPlayer.setOpponent(homePlayer);
		homePlayer.getDeck().shuffle();
		adversaryPlayer.getDeck().shuffle();
		
		mPlayers = new ArrayList<Player>();
		mPlayers.add(homePlayer);
		mPlayers.add(adversaryPlayer);
        
        AtomicReference<Boolean> readyToStart = new AtomicReference<>(true);
        do
        {
        	// Draw Cards and check for mulligans
        	System.out.println(mPlayers.get(0).getDeck().size());
        	mPlayers.stream()
        	.filter(player->!player.getIsReadyToStart())
        	.forEach(player-> {
        		board.updateHand(player.drawCardsFromDeck(6), player.isHumanPlayer());
        		player.checkIfPlayerReady();
        	});
            // Execute mulligans
            mPlayers.stream()
            .filter(player->player.isInMulliganState())
            .forEach(player->{
            	player.mulligan();
            	readyToStart.set(player.getIsReadyToStart());;
            });
            
        // Repeat until no more mulligans
		}while(!readyToStart.get());
        
        mPlayers.forEach(currentPlayer->{ currentPlayer.setUpRewards(); });
        
        AIPlayer opp = (AIPlayer)mPlayers.get(1);
        opp.selectStarterPokemon();
		
		System.out.println("\nTest Run: PokemonNoGo");
		System.out.println("Print out the Hand of the First Player...");
		for(int i = 0; i < mPlayers.get(0).getHand().size(); ++i){
			System.out.println(mPlayers.get(0).getHand().getCards().get(i));
		}
			
		
		
		//Clean up	
	}
	
	public static boolean useCardForPlayer(Card card, int player){
		return mPlayers.get(player).useCard(card);
	}
	
	public static boolean useActivePokemonForPlayer(int player, int ability){
		return mPlayers.get(player).useActivePokemon(ability);
	}
	
        public static boolean hasActivePokemon(int player){
            return mPlayers.get(player).getActivePokemon() != null;
        }
        
	public static void startAITurn(){
		mPlayers.get(1).startTurn();
	}
	
	public static void displayMessage(String msg){
		JOptionPane.showMessageDialog(null, msg);
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
	
	public static void checkGameStatus(){
		
	}
	
	public static void updateHand(Hand hand, boolean player){
		board.updateHand(hand, player);
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
    	final File folder = new File(LOCATION_DECKS).getAbsoluteFile();
//    	final File folder = new File( GameController.class.getClassLoader().getResource(LOCATION_DECKS).getPath());
    	ArrayList<Component> deckButtons = new ArrayList<Component>();
		for (String fileEntry : FileUtils.getFilesFromFolder(LOCATION_DECKS, ".txt")) {
			System.out.println(LOCATION_DECKS+"/"+fileEntry);
			deckButtons.add(getButton(optionPane, fileEntry, FileUtils.getFileAsImageIcon("images/deckIcon.png", 65, 80), deckList.size()));
			deckList.add(Parser.Instance().DeckCreation(LOCATION_DECKS+"/"+fileEntry));
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
            System.out.println(button.getText());
          }
        };
        button.addActionListener(actionListener);
        return button;
      }
    
    public static int dispayCustomOptionPane(Object[] buttons, String title, String prompt)
    {
        return JOptionPane.showOptionDialog(null, prompt, title,
        0, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
        //System.out.println("Value: " + returnValue);
    }
}
