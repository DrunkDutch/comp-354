package com.dmens.pokeno.effect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dmens.pokeno.card.Card;
import com.dmens.pokeno.deck.CardContainer;
import com.dmens.pokeno.player.Player;
import com.dmens.pokeno.services.handlers.SourceServiceHandler;
import com.dmens.pokeno.services.handlers.TargetServiceHandler;

public class Search extends Effect {

	private static final Logger LOG = LogManager.getLogger(Search.class);
	
	private String mTarget;
	private String mSource;
	private String mFilter;
	private String mAmount;

	@Override
	public void execute() {
		Player targetPlayer = TargetServiceHandler.getInstance().getService().getPlayer(mTarget);
		CardContainer source = SourceServiceHandler.getInstance().getService().getCardContainerSourceFromPlayer(targetPlayer, mSource);
		List<Card> cardsToSearch = new ArrayList<Card>();
		if(this.hasFilter()) {
			LOG.info("Filtering cards");
			filterCards(source, cardsToSearch);
		}
		else {
			LOG.info("Searching without filter");
			cardsToSearch.addAll(source.getCards());
		}
		// shuffle order
		Collections.shuffle(cardsToSearch);
		// Search and pick cards
		// TODO Change amount to support counters
		int amount = Integer.parseInt(mAmount);
		Player currentPlayer = TargetServiceHandler.getInstance().getService().getPlayer("your-active");
		if(cardsToSearch.size() == 0){
			currentPlayer.displayMessage("No cards to search on source: "+mTarget+":"+mSource);
			return;
		}
		List<Card> cardsSelected = currentPlayer.ChooseMultipleCards(cardsToSearch, amount);
		currentPlayer.addCardsToHand(cardsSelected);
		cardsSelected.forEach(card->source.removeCard(card));
	}
	
	public void filterCards(CardContainer source, List<Card> cardsToSearch){
		String[] filters = mFilter.split(":");
		if(filters[0].equals("pokemon")){
			if(filters.length == 2)
				cardsToSearch.addAll(source.getAllPokemonOfType(filters[1]));
			else	
				cardsToSearch.addAll(source.getAllPokemon());
		}else if(filters[0].equals("energy")){
			if(filters.length == 2)
				cardsToSearch.addAll(source.getAllEnergyOfType(filters[1]));
			else	
				cardsToSearch.addAll(source.getAllEnergy());
		}else if(filters[0].equals("top")){
			cardsToSearch.addAll(source.peekFromTop(Integer.parseInt(filters[1])));
		}else if(filters[0].equals("trainer")){
			if(filters.length == 2)
				cardsToSearch.addAll(source.getAllTrainerOfType(filters[1]));
			else	
				cardsToSearch.addAll(source.getAllTrainer());
		}else if(filters[0].equals("bottom")){
			cardsToSearch.addAll(source.peekFromBottom(Integer.parseInt(filters[1])));
		}
	}
	
	public boolean hasFilter(){
		if(mFilter != null && !mFilter.isEmpty())
			return true;
		return false;
	}

	/**
	 * @return the mTarget
	 */
	public String getTarget() {
		return mTarget;
	}

	/**
	 * @param mTarget the mTarget to set
	 */
	public void setTarget(String target) {
		this.mTarget = target;
	}

	/**
	 * @return the mSource
	 */
	public String getSource() {
		return mSource;
	}

	/**
	 * @param mSource the mSource to set
	 */
	public void setSource(String source) {
		this.mSource = source;
	}
	
	/**
	 * @return the mSource
	 */
	public String getFilter() {
		return mFilter;
	}

	/**
	 * @param mSource the mSource to set
	 */
	public void setFilter(String filter) {
		this.mFilter = filter;
	}

	/**
	 * @return the mAmount
	 */
	public String getAmount() {
		return mAmount;
	}

	/**
	 * @param mAmount the mAmount to set
	 */
	public void setAmount(String amount) {
		this.mAmount = amount;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String str() {
		return String.format("SEARCH");
	}

}
