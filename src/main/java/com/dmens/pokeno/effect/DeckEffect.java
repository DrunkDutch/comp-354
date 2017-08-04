package com.dmens.pokeno.effect;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.card.Pokemon;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.deck.Hand;
import com.dmens.pokeno.player.Player;

public class DeckEffect extends Effect {
	
	private static final Logger LOG = LogManager.getLogger(Reenergize.class);
	
	private String mDestination;
	private String mCountType;
	private int mAmount;
	
	public DeckEffect(String target, String countType, String destination, int amt) {
		mTarget = target;
		mDestination = destination;
		mCountType = countType;
		mAmount = amt;
	}
	
	public DeckEffect(DeckEffect d) {
		mTarget = d.mTarget;
		mDestination = d.mDestination;
		mCountType = d.mCountType;
	}
	
	public int getAmount() {
		return mAmount;
	}
	
	@Override
	public void execute() {
		Player activePlayer = GameController.getActivePlayer();
		Player target;
		Hand targetHand = null;
		CardContainer destination = null;
		//This could be changed to use the Source Service but it is not implemented at this time
		
		if (mTarget.equals("self"))
			target = activePlayer;
		else
			target = activePlayer.getOpponent();
		
		if (mDestination.contains("deck"))
			destination = target.getDeck();
		else if (mDestination.contains("discard"))
			destination = target.getDiscards();
		
		// Act Cute:deck:target:opponent:destination:deck:bottom:choice:them:1
		if(mAmount > 0) {
			for(int i = 0; i < mAmount; i++) {
				targetHand = target.getHand();
				List<Card> potentialSources =  new ArrayList<Card>(targetHand.getCards());
				
				// 1) select a card to remove
				int index = target.createPokemonOptionPane("Card Removal", "Select a card:", false, potentialSources);
				
				// 2) move it to the destination
				destination.addCard(targetHand.getCards().get(index));
				
				// 3) remove the card
				GameController.updateDeck(destination.getCards().size(), target.isHumanPlayer());
				targetHand.removeCard(targetHand.getCards().get(index));
			}
		}
		else {
			if (mCountType.contains("hand")) {
				if (mCountType.contains("your")) {
					targetHand = activePlayer.getHand();
				}
				else
					targetHand = activePlayer.getOpponent().getHand();
				
				for (Card c : targetHand.getCards()) {
					//TODO - sometimes bottom of deck
					destination.getCards().add(c);
				}
				System.out.println(destination.getCards().size() + " " + mTarget);
				GameController.updateDeck(destination.getCards().size(), mTarget.contains("your"));
				targetHand.getCards().clear();
			}
		}
	}

	@Override
	public String toString() {
		return String.format("DECK: Target: %s count: %s to: %s amt: %d", this.mTarget, this.mCountType, this.mDestination, this.mAmount);
	}

}

