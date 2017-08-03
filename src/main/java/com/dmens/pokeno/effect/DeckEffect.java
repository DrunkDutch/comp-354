package com.dmens.pokeno.effect;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.controller.GameController;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.deck.Hand;
import com.dmens.pokeno.player.Player;

public class DeckEffect extends Effect
{
	private String mDestination;
	private String mOrigin;
	
	public DeckEffect(String target, String origin, String destination)
	{
		mTarget = target;
		mDestination = destination;
		mOrigin = origin;
	}
	
	public DeckEffect(DeckEffect d)
	{
		mTarget = d.mTarget;
		mDestination = d.mDestination;
		mOrigin = d.mOrigin;
	}
	
	@Override
	public void execute()
	{
		Player activePlayer = GameController.getActivePlayer();
		Player target;
		Hand targetHand;
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
		
		if (mOrigin.contains("hand")) {
			if (mOrigin.contains("your")) {
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

	@Override
	public String toString() {
		return String.format("DECK: Target: %s from: %s to: %s", this.mTarget, this.mOrigin, this.mDestination);
	}
	
	@Override
	public String str() {
		return String.format("DECK %s, %s", this.mTarget, this.mDestination);
	}
}
