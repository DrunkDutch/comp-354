package com.dmens.pokeno.system;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import com.dmens.pokeno.services.TargetService;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.database.AbilitiesDatabase;
import com.dmens.pokeno.database.CardsDatabase;
import com.dmens.pokeno.deck.Deck;
import com.dmens.pokeno.player.AIPlayer;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.utils.DeckCreator;
import com.dmens.pokeno.view.GameBoard;


public class CreateGameTest {
	
	 private static final Logger LOG = LogManager.getLogger(GameController.class);
	private static final String LOCATION_CARDS = "cards.txt";
	private static final String LOCATION_ABILITIES = "abilities.txt";
	
	private static ArrayList<Player> mPlayers = null;
	
	static Robot robot;
	
    public static GameBoard board;
    
    @BeforeClass
    public static void setup(){
    	AbilitiesDatabase.getInstance().initialize(LOCATION_ABILITIES);
		CardsDatabase.getInstance().initialize(LOCATION_CARDS);
		
		// Setup robot
        try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    }
    
    @Before
    public void startGame(){
		board = new GameBoard();
		GameController.setBoard(board);
        
		Deck[] chosenDecks = {DeckCreator.Instance().DeckCreation("decks/deck2.txt"), DeckCreator.Instance().DeckCreation("decks/deck1.txt")};
		LOG.trace("Creating players and assigning decks...");
		Player homePlayer = Mockito.spy(new Player(chosenDecks[0]));
		Player adversaryPlayer = Mockito.spy(new AIPlayer(chosenDecks[1]));
		homePlayer.setOpponent(adversaryPlayer);
		adversaryPlayer.setOpponent(homePlayer);
		
		mPlayers = new ArrayList<Player>();
		mPlayers.add(homePlayer);
		mPlayers.add(adversaryPlayer);
		GameController.setPlayers(mPlayers);
        do {
        	// Draw Cards and check for mulligans, dismiss JOptionPane popups
        	Mockito.doNothing().when(homePlayer).displayMessage(Mockito.anyString());
        	Mockito.doNothing().when(adversaryPlayer).displayMessage(Mockito.anyString());
        	mPlayers.stream()
        	.filter(player->!player.getIsReadyToStart())
        	.forEach(player-> {
        		board.updateHand(player.drawCardsFromDeck(6), player.isHumanPlayer());
        		player.checkIfPlayerReady();
        	});
            // Execute mulligans, deny drawing card to avoi JOptionPane popup
        	Mockito.doNothing().when(homePlayer).notifyMulligan();
        	Mockito.doNothing().when(adversaryPlayer).notifyMulligan();
        	
            mPlayers.stream()
            .filter(player->player.isInMulliganState())
            .forEach(player->{
            		player.mulligan();
            	});
		} while(!homePlayer.getIsReadyToStart() || !adversaryPlayer.getIsReadyToStart());
        
        mPlayers.forEach(currentPlayer->{ currentPlayer.setUpRewards(); });
        TargetServiceHandler.getInstance().getService().clearInstance();
		TargetServiceHandler.getInstance().setYouPlayer(mPlayers.get(1));
		TargetServiceHandler.getInstance().setThemPlayer(mPlayers.get(0));

        AIPlayer opp = (AIPlayer)mPlayers.get(1);
        opp.selectStarterPokemon();
    }
    
    @After
    public void tearDown(){
    	// wait before closing
    	try {
			Thread.sleep(500);
			// Close any remaining popups if any
			robot.keyPress(32);
	    	robot.keyRelease(32);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    
	@Test
	public void testSetActivePokemonPlayEnergyAndAttack() {
		GameController.playersMayAttack = true;
		Component c =  board.getPlayerHandPanel().getComponent(2);
		click(board.getPlayerHandPanel().getX()+c.getX()+board.getX()+20, (38+(c.getY()+board.getY()+(board.getPlayerHandPanel().getY()))));

		// assert pokemon was played
		Assert.assertNotNull(mPlayers.get(0).getActivePokemon());
		
		// Bench pokemon
		c =  board.getPlayerHandPanel().getComponent(1);
		click(board.getPlayerHandPanel().getX()+c.getX()+board.getX()+20, (40+(c.getY()+board.getY()+(board.getPlayerHandPanel().getY()))));
		
		Assert.assertNotNull("Pokemon must have benn benched", mPlayers.get(0).getBenchedPokemon().get(0));
		
		// Click energy
		// Return pokemon 0 (Active) to receive energy card
		Mockito.doReturn(0).when(mPlayers.get(0)).createPokemonOptionPane(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean());
		
		c =  board.getPlayerHandPanel().getComponent(0);
		click(board.getPlayerHandPanel().getX()+c.getX()+board.getX()+20, (40+(c.getY()+board.getY()+(board.getPlayerHandPanel().getY()))));
    	
		Assert.assertEquals(1, mPlayers.get(0).getActivePokemon().getAttachedEnergy().size());
		// Attack
		board.getPlayerAttack1Btn().doClick();
		// Assert opponent damage
		Assert.assertEquals(10, mPlayers.get(1).getActivePokemon().getDamage());
		Assert.assertEquals(10, board.getOpponentDamageField());
	}

	private void waitSleep(long milis){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void click(int x, int y){
		robot.mouseMove(x, y);
		waitSleep(500);
    	robot.mousePress(InputEvent.BUTTON1_MASK);
    	robot.mouseRelease(InputEvent.BUTTON1_MASK);
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
